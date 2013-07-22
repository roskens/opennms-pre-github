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
package org.opennms.features.topology.api.topo;

import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

/**
 * The Class AbstractTopologyProviderTest.
 */
public class AbstractTopologyProviderTest {

    /**
     * Test id generator.
     *
     * @throws MalformedURLException
     *             the malformed url exception
     * @throws JAXBException
     *             the jAXB exception
     */
    @Test
    public void testIdGenerator() throws MalformedURLException, JAXBException {
        AbstractTopologyProvider provider = new AbstractTopologyProvider("test") {

            @Override
            public void save() {
                ; // nothing to do
            }

            @Override
            public void refresh() {
                ; // nothing to do
            }

            @Override
            public void load(String filename) throws MalformedURLException, JAXBException {
                for (int i = 0; i < 10; i++) {
                    addVertex(0, i);
                }

                for (int i = 0; i < 5; i++) {
                    addGroup("group" + i, "group");
                }

                for (int i = 0; i < 2; i++) {
                    addEdges(new AbstractEdge("test", getNextEdgeId(), getVertices().get(i), getVertices().get(i + 1)));
                }
            }
        };
        provider.load(null);

        Assert.assertEquals(10, provider.getVerticesWithoutGroups().size());
        Assert.assertEquals(5, provider.getGroups().size());
        Assert.assertEquals(15, provider.getVertices().size());
        Assert.assertEquals(2, provider.getEdges().size());

        Assert.assertEquals("e2", provider.getNextEdgeId());
        Assert.assertEquals("g5", provider.getNextGroupId());
        Assert.assertEquals("v10", provider.getNextVertexId());
    }
}
