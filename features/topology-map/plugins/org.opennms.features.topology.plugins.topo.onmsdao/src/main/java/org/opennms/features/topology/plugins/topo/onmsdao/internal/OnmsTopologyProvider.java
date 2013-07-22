/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.features.topology.plugins.topo.onmsdao.internal;

import java.util.ArrayList;
import java.util.List;

import org.opennms.features.topology.api.topo.AbstractTopologyProvider;
import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.GraphProvider;
import org.opennms.features.topology.api.topo.SimpleGroup;
import org.opennms.features.topology.api.topo.SimpleLeafVertex;
import org.opennms.features.topology.api.topo.Vertex;
import org.opennms.features.topology.api.topo.VertexRef;
import org.opennms.netmgt.dao.api.DataLinkInterfaceDao;
import org.opennms.netmgt.dao.api.OnmsMapDao;
import org.opennms.netmgt.dao.api.OnmsMapElementDao;
import org.opennms.netmgt.model.DataLinkInterface;
import org.opennms.netmgt.model.OnmsMap;
import org.opennms.netmgt.model.OnmsMapElement;

/**
 * The Class OnmsTopologyProvider.
 */
public class OnmsTopologyProvider extends AbstractTopologyProvider implements GraphProvider {

    /** The Constant TOPOLOGY_NAMESPACE_ONMSDAO. */
    private static final String TOPOLOGY_NAMESPACE_ONMSDAO = "onmsdao";

    /** The m_onms map dao. */
    private OnmsMapDao m_onmsMapDao;

    /** The m_onms map element dao. */
    private OnmsMapElementDao m_onmsMapElementDao;

    /** The m_data link interface dao. */
    private DataLinkInterfaceDao m_dataLinkInterfaceDao;

    /**
     * Gets the onms map dao.
     *
     * @return the onms map dao
     */
    public OnmsMapDao getOnmsMapDao() {
        return m_onmsMapDao;
    }

    /**
     * Sets the onms map dao.
     *
     * @param onmsMapDao
     *            the new onms map dao
     */
    public void setOnmsMapDao(OnmsMapDao onmsMapDao) {
        m_onmsMapDao = onmsMapDao;
    }

    /**
     * Gets the onms map element dao.
     *
     * @return the onms map element dao
     */
    public OnmsMapElementDao getOnmsMapElementDao() {
        return m_onmsMapElementDao;
    }

    /**
     * Sets the onms map element dao.
     *
     * @param onmsMapElementDao
     *            the new onms map element dao
     */
    public void setOnmsMapElementDao(OnmsMapElementDao onmsMapElementDao) {
        m_onmsMapElementDao = onmsMapElementDao;
    }

    /**
     * Gets the data link interface dao.
     *
     * @return the data link interface dao
     */
    public DataLinkInterfaceDao getDataLinkInterfaceDao() {
        return m_dataLinkInterfaceDao;
    }

    /**
     * Sets the data link interface dao.
     *
     * @param dataLinkInterfaceDao
     *            the new data link interface dao
     */
    public void setDataLinkInterfaceDao(DataLinkInterfaceDao dataLinkInterfaceDao) {
        m_dataLinkInterfaceDao = dataLinkInterfaceDao;
    }

    /**
     * Instantiates a new onms topology provider.
     */
    public OnmsTopologyProvider() {
        super(TOPOLOGY_NAMESPACE_ONMSDAO);
    }

    /**
     * Gets the map.
     *
     * @param mapId
     *            the map id
     * @return the map
     */
    private OnmsMap getMap(int mapId) {
        return getOnmsMapDao().findMapById(mapId);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#save()
     */
    @Override
    public void save() {
        save("1");
    }

    /**
     * Save.
     *
     * @param filename
     *            the filename
     */
    public void save(String filename) {

        List<Vertex> vertices = super.getVertices();
        int rootMapid = Integer.parseInt(filename);
        OnmsMap rootMap = getMap(rootMapid);
        getOnmsMapElementDao().deleteElementsByMapId(rootMap);

        for (Vertex vertex : vertices) {
            if (vertex.isGroup()) {
                Integer mapid = ((SimpleGroup) vertex).getMapid();
                getOnmsMapElementDao().deleteElementsByMapId(getMap(mapid));
            }
        }

        for (Vertex vertex : vertices) {
            Integer mapid;
            Integer id;
            String type;
            if (!vertex.isGroup()) {
                id = ((SimpleLeafVertex) vertex).getNodeID();
                type = OnmsMapElement.NODE_TYPE;
            } else {
                id = ((SimpleGroup) vertex).getMapid();
                type = OnmsMapElement.MAP_TYPE;
            }
            VertexRef parentRef = vertex.getParent();
            Vertex parent = getParent(parentRef);
            if (parent == null) {
                mapid = rootMapid;
            } else {
                mapid = ((SimpleGroup) parent).getMapid();
            }
            OnmsMap map = getMap(mapid);
            getOnmsMapElementDao().save(new OnmsMapElement(map, id, type, "Here is the label", vertex.getIconKey(),
                                                           vertex.getX(), vertex.getY()));
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#refresh()
     */
    @Override
    public void refresh() {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#load(java.lang.String)
     */
    @Override
    public void load(String filename) {

        OnmsMap map = getMap(Integer.parseInt(filename));
        List<Vertex> vertices = getVertex(map.getId(), null);
        List<Edge> edges = getEdges(vertices);

        clearVertices();
        addVertices(vertices.toArray(new Vertex[] {}));

        clearEdges();
        addEdges(edges.toArray(new Edge[] {}));
    }

    /**
     * Gets the vertex.
     *
     * @param mapId
     *            the map id
     * @param parent
     *            the parent
     * @return the vertex
     */
    private List<Vertex> getVertex(int mapId, SimpleGroup parent) {
        List<Vertex> vertexes = new ArrayList<Vertex>();
        for (OnmsMapElement element : getOnmsMapElementDao().findNodeElementsOnMap(mapId)) {
            SimpleLeafVertex vertex = new SimpleLeafVertex(getVertexNamespace(), Integer.toString(element.getId()),
                                                           element.getX(), element.getY());
            vertex.setLocked(false);
            vertex.setSelected(false);
            vertex.setIconKey(element.getIconName());
            vertex.setParent(parent);
            vertexes.add(vertex);
        }

        for (OnmsMapElement element : getOnmsMapElementDao().findMapElementsOnMap(mapId)) {
            SimpleGroup vertex = new SimpleGroup(getVertexNamespace(), Integer.toString(element.getId()));
            vertex.setLocked(false);
            vertex.setSelected(false);
            vertex.setIconKey(element.getIconName());
            vertex.setParent(parent);
            vertexes.add(vertex);
            vertexes.addAll(getVertex(element.getElementId(), vertex));
        }

        return vertexes;
    }

    /**
     * Gets the edges.
     *
     * @param vertexes
     *            the vertexes
     * @return the edges
     */
    private List<Edge> getEdges(List<Vertex> vertexes) {
        List<Edge> edges = new ArrayList<Edge>();
        List<Vertex> leafs = new ArrayList<Vertex>();
        for (Vertex vertex : leafs) {
            if (!vertex.isGroup()) {
                leafs.add((SimpleLeafVertex) vertex);
            }
        }

        for (Vertex target : leafs) {
            for (DataLinkInterface link : getDataLinkInterfaceDao().findByNodeParentId(((SimpleLeafVertex) target).getNodeID())) {
                for (Vertex source : leafs) {
                    if (link.getNode().getId() == ((SimpleLeafVertex) source).getNodeID()) {
                        connectVertices(link.getId().toString(), source, target);
                    }
                }
            }
        }
        return edges;
    }
}
