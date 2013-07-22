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
 * The Class OnmsUserList.
 */
@XmlRootElement(name = "users")
public class OnmsUserList extends LinkedList<OnmsUser> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 870025150817734414L;

    /** The m_total count. */
    private int m_totalCount;

    /**
     * Instantiates a new onms user list.
     */
    public OnmsUserList() {
        super();
    }

    /**
     * Instantiates a new onms user list.
     *
     * @param c
     *            the c
     */
    public OnmsUserList(final Collection<? extends OnmsUser> c) {
        super(c);
    }

    /**
     * Gets the users.
     *
     * @return the users
     */
    @XmlElement(name = "user")
    public List<OnmsUser> getUsers() {
        return this;
    }

    /**
     * Sets the users.
     *
     * @param users
     *            the new users
     */
    public void setUsers(final List<OnmsUser> users) {
        if (users == this) {
            return;
        }
        clear();
        addAll(users);
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
