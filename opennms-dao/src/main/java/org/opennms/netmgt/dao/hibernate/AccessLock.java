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

package org.opennms.netmgt.dao.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class AccessLock.
 */
@Entity
@Table(name = "accessLocks")
public class AccessLock {

    /** The m_lock name. */
    private String m_lockName;

    /**
     * Instantiates a new access lock.
     */
    public AccessLock() {
    }

    /**
     * Instantiates a new access lock.
     *
     * @param lockName
     *            the lock name
     */
    public AccessLock(String lockName) {
        m_lockName = lockName;
    }

    /**
     * Gets the lock name.
     *
     * @return the lock name
     */
    @Id
    @Column(name = "lockName")
    public String getLockName() {
        return m_lockName;
    }

    /**
     * Sets the lock name.
     *
     * @param lockName
     *            the new lock name
     */
    public void setLockName(String lockName) {
        m_lockName = lockName;
    }

}
