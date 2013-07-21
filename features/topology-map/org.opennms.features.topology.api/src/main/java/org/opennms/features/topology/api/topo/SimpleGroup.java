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

/**
 * The Class SimpleGroup.
 */
public class SimpleGroup extends AbstractVertex {

    /** The m_mapid. */
    private int m_mapid;

    /**
     * Instantiates a new simple group.
     *
     * @param namespace
     *            the namespace
     * @param groupId
     *            the group id
     */
    public SimpleGroup(String namespace, String groupId) {
        this(namespace, groupId, -1);
    }

    /**
     * Instantiates a new simple group.
     *
     * @param namespace
     *            the namespace
     * @param groupId
     *            the group id
     * @param mapid
     *            the mapid
     */
    public SimpleGroup(String namespace, String groupId, int mapid) {
        super(namespace, groupId);
        m_mapid = mapid;
    }

    /**
     * Gets the mapid.
     *
     * @return the mapid
     */
    public int getMapid() {
        return m_mapid;
    }

    /**
     * Sets the mapid.
     *
     * @param mapid
     *            the new mapid
     */
    public void setMapid(int mapid) {
        m_mapid = mapid;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.AbstractVertex#isGroup()
     */
    @Override
    public boolean isGroup() {
        return true;
    }
}
