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
package org.opennms.features.topology.app.internal;

import java.net.MalformedURLException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.topo.AbstractTopologyProvider;
import org.opennms.features.topology.api.topo.GraphProvider;
import org.opennms.features.topology.api.topo.Vertex;
import org.opennms.features.topology.api.topo.VertexRef;

/**
 * The Class GCFilterableContainerTest.
 */
public class GCFilterableContainerTest {

    /** The graph container. */
    private GraphContainer graphContainer;

    /**
     * Sets the up.
     *
     * @throws MalformedURLException
     *             the malformed url exception
     * @throws JAXBException
     *             the jAXB exception
     */
    @Before
    public void setUp() throws MalformedURLException, JAXBException {
        GraphProvider provider = new AbstractTopologyProvider("test") {
            @Override
            public void save() {
            }

            @Override
            public void refresh() {
            }

            @Override
            public void load(String filename) throws MalformedURLException, JAXBException {
                resetContainer();

                String vId1 = getNextVertexId();
                TestVertex v1 = new TestVertex(vId1, 0, 0);
                v1.setLabel("a leaf vertex");

                String vId2 = getNextVertexId();
                TestVertex v2 = new TestVertex(vId2, 0, 0);
                v2.setLabel("another leaf");

                addVertices(v1, v2);
            }
        };
        provider.load(null);
        graphContainer = new VEProviderGraphContainer(provider, new ProviderManager());
    }

    /**
     * Creates the top level group.
     */
    @Test
    public void createTopLevelGroup() {
        // elements to group
        List<Vertex> allVertices = graphContainer.getBaseTopology().getVertices();

        // create group
        String groupName = "groupName";
        VertexRef groupId = graphContainer.getBaseTopology().addGroup(groupName, "group");

        // Link all targets to the newly-created group
        for (VertexRef vertexRef : allVertices) {
            graphContainer.getBaseTopology().setParent(vertexRef, groupId);
        }

        // Save the topology
        graphContainer.getBaseTopology().save();
        graphContainer.redoLayout();
        graphContainer.getBaseTopology().refresh();

        // all childs must have group as parent
        for (Vertex vertex : allVertices) {
            Assert.assertEquals(groupId, vertex.getParent());
        }

        // group must have no parent
        Assert.assertEquals(null, graphContainer.getBaseTopology().getVertex(groupId).getParent());
    }

}
