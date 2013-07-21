/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class OnmsGroupList.
 */
@XmlRootElement(name = "groups")
public class OnmsGroupList extends LinkedList<OnmsGroup> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3120131643998397193L;

    /** The m_total count. */
    private int m_totalCount;

    /**
     * Instantiates a new onms group list.
     */
    public OnmsGroupList() {
        super();
    }

    /**
     * Instantiates a new onms group list.
     *
     * @param c
     *            the c
     */
    public OnmsGroupList(final Collection<? extends OnmsGroup> c) {
        super(c);
    }

    /**
     * Gets the groups.
     *
     * @return the groups
     */
    @XmlElement(name = "group")
    public List<OnmsGroup> getGroups() {
        return this;
    }

    /**
     * Sets the groups.
     *
     * @param groups
     *            the new groups
     */
    public void setGroups(final List<OnmsGroup> groups) {
        if (groups == this)
            return;
        clear();
        addAll(groups);
    }

    /**
     * Gets the count.
     *
     * @return the count
     */
    @XmlAttribute(name = "count")
    public int getCount() {
        return this.size();
    }

    // The property has a getter "" but no setter. For unmarshalling, please
    // define setters.
    /**
     * Sets the count.
     *
     * @param count
     *            the new count
     */
    public void setCount(final int count) {
    }

    /**
     * Gets the total count.
     *
     * @return the total count
     */
    @XmlAttribute(name = "totalCount")
    public int getTotalCount() {
        return m_totalCount;
    }

    /**
     * <p>
     * setTotalCount
     * </p>
     * .
     *
     * @param count
     *            a int.
     */
    public void setTotalCount(final int count) {
        m_totalCount = count;
    }
}
