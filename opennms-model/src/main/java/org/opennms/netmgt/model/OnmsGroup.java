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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The Class OnmsGroup.
 */
@XmlRootElement(name = "group")
@XmlAccessorType(XmlAccessType.FIELD)
public class OnmsGroup implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7658664550834146185L;

    /** The m_name. */
    @XmlElement(name = "name", required = true)
    private String m_name;

    /** The m_comments. */
    @XmlElement(name = "comments", required = false)
    private String m_comments;

    /** The m_users. */
    @XmlElement(name = "user", required = false)
    private List<String> m_users = new ArrayList<String>();

    /**
     * Instantiates a new onms group.
     */
    public OnmsGroup() {
    }

    /**
     * Instantiates a new onms group.
     *
     * @param groupName
     *            the group name
     */
    public OnmsGroup(final String groupName) {
        m_name = groupName;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        m_name = name;
    }

    /**
     * Gets the comments.
     *
     * @return the comments
     */
    public String getComments() {
        return m_comments;
    }

    /**
     * Sets the comments.
     *
     * @param comments
     *            the new comments
     */
    public void setComments(final String comments) {
        m_comments = comments;
    }

    /**
     * Gets the users.
     *
     * @return the users
     */
    public List<String> getUsers() {
        return m_users;
    }

    /**
     * Sets the users.
     *
     * @param users
     *            the new users
     */
    public void setUsers(final List<String> users) {
        m_users = users;
    }

    /**
     * Adds the user.
     *
     * @param userName
     *            the user name
     */
    public void addUser(final String userName) {
        if (m_users == null) {
            m_users = new ArrayList<String>();
        }
        m_users.add(userName.intern());
    }

    /**
     * Removes the user.
     *
     * @param userName
     *            the user name
     */
    public void removeUser(final String userName) {
        if (m_users == null)
            return;
        m_users.remove(userName);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", m_name).append("comments", m_comments).append("users", m_users).toString();
    }
}
