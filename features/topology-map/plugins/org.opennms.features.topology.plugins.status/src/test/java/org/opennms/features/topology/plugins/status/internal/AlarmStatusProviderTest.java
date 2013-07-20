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
package org.opennms.features.topology.plugins.status.internal;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.opennms.features.topology.api.topo.AbstractRef;
import org.opennms.features.topology.api.topo.Status;
import org.opennms.features.topology.api.topo.Vertex;
import org.opennms.features.topology.api.topo.VertexRef;
import org.opennms.netmgt.dao.api.AlarmDao;
import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.netmgt.model.alarm.AlarmSummary;

import com.vaadin.data.Item;

/**
 * The Class AlarmStatusProviderTest.
 */
public class AlarmStatusProviderTest {

    /**
     * The Class TestVertex.
     */
    private class TestVertex extends AbstractRef implements Vertex {

        /**
         * Instantiates a new test vertex.
         */
        public TestVertex() {
            super("nodes", "1", null);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#getKey()
         */
        @Override
        public String getKey() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#getItem()
         */
        @Override
        public Item getItem() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#getTooltipText()
         */
        @Override
        public String getTooltipText() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#getIconKey()
         */
        @Override
        public String getIconKey() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#getStyleName()
         */
        @Override
        public String getStyleName() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#isGroup()
         */
        @Override
        public boolean isGroup() {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#setParent(org.opennms.features.topology.api.topo.VertexRef)
         */
        @Override
        public void setParent(VertexRef parent) {
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#getParent()
         */
        @Override
        public VertexRef getParent() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#getX()
         */
        @Override
        public Integer getX() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#getY()
         */
        @Override
        public Integer getY() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#isLocked()
         */
        @Override
        public boolean isLocked() {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#isSelected()
         */
        @Override
        public boolean isSelected() {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#getIpAddress()
         */
        @Override
        public String getIpAddress() {
            return null;
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.topo.Vertex#getNodeID()
         */
        @Override
        public Integer getNodeID() {
            return 1;
        }

    }

    /** The m_alarm dao. */
    private AlarmDao m_alarmDao;

    /** The m_status provider. */
    private AlarmStatusProvider m_statusProvider;

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        m_alarmDao = EasyMock.createMock(AlarmDao.class);
        m_statusProvider = new AlarmStatusProvider();
        m_statusProvider.setAlarmDao(m_alarmDao);
    }

    /**
     * Test get alarm status.
     */
    @Test
    public void testGetAlarmStatus() {
        Vertex vertex = new TestVertex();

        EasyMock.expect(m_alarmDao.getNodeAlarmSummaries(EasyMock.anyInt())).andReturn(createNormalAlarmSummaryList());

        EasyMock.replay(m_alarmDao);

        Status vertexStatus = m_statusProvider.getStatusForVertex(vertex);
        String computeStatus = vertexStatus.computeStatus();
        assertTrue(computeStatus.equals("indeterminate"));

        EasyMock.verify(m_alarmDao);
    }

    /**
     * Creates the normal alarm summary list.
     *
     * @return the list
     */
    private List<AlarmSummary> createNormalAlarmSummaryList() {
        List<AlarmSummary> alarms = new ArrayList<AlarmSummary>();
        alarms.add(new AlarmSummary(1, "node1", new Date(), OnmsSeverity.INDETERMINATE, 1L));
        return alarms;
    }

}
