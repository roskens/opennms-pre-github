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
package org.opennms.features.topology.plugins.ncs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.plugins.ncs.NCSEdgeProvider.NCSVertex;
import org.opennms.features.topology.plugins.ncs.NCSPathEdgeProvider.NCSPathEdge;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.ncs.NCSComponentRepository;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class NCSServicePath.
 */
public class NCSServicePath {

    /** The m_dao. */
    private NCSComponentRepository m_dao;

    /** The m_node dao. */
    private NodeDao m_nodeDao;

    /** The m_node foreign source. */
    private String m_nodeForeignSource;

    /** The m_vertices. */
    private LinkedList<NCSVertex> m_vertices = new LinkedList<NCSVertex>();

    /** The m_service foreign source. */
    private String m_serviceForeignSource;

    /** The m_device a foreign id. */
    private String m_deviceAForeignID;

    /** The m_device z foreign id. */
    private String m_deviceZForeignID;

    /** The m_service name. */
    private String m_serviceName;

    /** The m_status code. */
    private int m_statusCode;

    /**
     * Instantiates a new nCS service path.
     *
     * @param data
     *            the data
     * @param dao
     *            the dao
     * @param nodeDao
     *            the node dao
     * @param nodeForeignSource
     *            the node foreign source
     * @param serviceForeignSource
     *            the service foreign source
     * @param deviceAID
     *            the device aid
     * @param deviceZID
     *            the device zid
     * @param serviceName
     *            the service name
     */
    public NCSServicePath(Node data, NCSComponentRepository dao, NodeDao nodeDao, String nodeForeignSource,
            String serviceForeignSource, String deviceAID, String deviceZID, String serviceName) {
        m_dao = dao;
        m_nodeDao = nodeDao;
        m_nodeForeignSource = nodeForeignSource;
        m_serviceForeignSource = serviceForeignSource;
        m_deviceAForeignID = deviceAID;
        m_deviceZForeignID = deviceZID;
        m_serviceName = serviceName;

        setStatusCode(data);

        // Add device A to path, its not sent in the path
        m_vertices.add(getVertexRefForForeignId(m_deviceAForeignID, m_nodeForeignSource));
        NodeList childNodes = getServicePath(data);
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeName().equals("LSPPath")) {
                parsePath(item);
            }
        }

        // Add device Z to path, its not sent in the path from the server
        m_vertices.add(getVertexRefForForeignId(m_deviceZForeignID, m_nodeForeignSource));
    }

    /**
     * Gets the service path.
     *
     * @param data
     *            the data
     * @return the service path
     */
    private NodeList getServicePath(Node data) {
        NodeList servicePath = data.getOwnerDocument().getElementsByTagName("ServicePath");
        return servicePath.item(0).getChildNodes();
    }

    /**
     * Sets the status code.
     *
     * @param data
     *            the new status code
     */
    private void setStatusCode(Node data) {
        NodeList childNodes = data.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeName().equals("Status")) {
                Node firstChild = item.getFirstChild();
                String nodeValue = firstChild.getFirstChild().getNodeValue();
                m_statusCode = Integer.valueOf(nodeValue);
            }
        }

    }

    /**
     * Gets the status code.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return m_statusCode;
    }

    /**
     * Parses the path.
     *
     * @param item
     *            the item
     */
    private void parsePath(Node item) {
        NodeList lspNode = item.getChildNodes();
        for (int i = 0; i < lspNode.getLength(); i++) {
            Node node = lspNode.item(i);
            if (node.getNodeName().equals("LSPNode")) {
                String nodeForeignId = node.getLastChild().getLastChild().getTextContent();
                if (!m_deviceAForeignID.equals(nodeForeignId) && !m_deviceZForeignID.equals(nodeForeignId)) {
                    NCSVertex vertex = getVertexRefForForeignId(nodeForeignId, m_nodeForeignSource);
                    if (vertex != null) {
                        m_vertices.add(vertex);
                    }
                }
            }
        }
    }

    /**
     * Gets the vertex ref for foreign id.
     *
     * @param nodeForeignId
     *            the node foreign id
     * @param nodeForeignSource
     *            the node foreign source
     * @return the vertex ref for foreign id
     */
    private NCSVertex getVertexRefForForeignId(String nodeForeignId, String nodeForeignSource) {
        OnmsNode node = m_nodeDao.findByForeignId(nodeForeignSource, nodeForeignId);
        if (node != null) {
            NCSVertex vertex = new NCSVertex(String.valueOf(node.getId()), node.getLabel());
            return vertex;
        } else {
            return null;
        }

    }

    /**
     * Gets the vertices.
     *
     * @return the vertices
     */
    public Collection<NCSVertex> getVertices() {
        return m_vertices;
    }

    /**
     * Gets the edges.
     *
     * @return the edges
     */
    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<Edge>();

        if (m_vertices.size() >= 2) {
            String deviceA = m_vertices.get(0).getLabel();
            String deviceZ = m_vertices.get(m_vertices.size() - 1).getLabel();
            ListIterator<NCSVertex> iterator = m_vertices.listIterator();
            while (iterator.hasNext()) {

                NCSVertex sourceRef = iterator.next();
                if (iterator.hasNext()) {
                    NCSVertex targetRef = m_vertices.get(iterator.nextIndex());
                    NCSPathEdge ncsPathEdge = new NCSPathEdge(m_serviceName, deviceA, deviceZ, sourceRef, targetRef);
                    ncsPathEdge.setStyleName("ncs edge direct");
                    edges.add(ncsPathEdge);
                }

            }

        }

        return edges;
    }

}
