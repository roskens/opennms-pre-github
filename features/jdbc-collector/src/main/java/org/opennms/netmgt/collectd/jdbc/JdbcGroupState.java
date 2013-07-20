/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.collectd.jdbc;

import java.util.Date;

/**
 * The Class JdbcGroupState.
 */
public class JdbcGroupState {

    /** The available. */
    private boolean available = false;

    /** The last checked. */
    private Date lastChecked;

    /**
     * Instantiates a new jdbc group state.
     *
     * @param isAvailable
     *            the is available
     */
    public JdbcGroupState(boolean isAvailable) {
        this(isAvailable, new Date());
    }

    /**
     * Instantiates a new jdbc group state.
     *
     * @param isAvailable
     *            the is available
     * @param lastChecked
     *            the last checked
     */
    public JdbcGroupState(boolean isAvailable, Date lastChecked) {
        this.available = isAvailable;
        this.lastChecked = lastChecked;
    }

    /**
     * Checks if is available.
     *
     * @return true, if is available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Gets the last checked.
     *
     * @return the last checked
     */
    public Date getLastChecked() {
        return lastChecked;
    }

    /**
     * Sets the last checked.
     *
     * @param lastChecked
     *            the new last checked
     */
    public void setLastChecked(Date lastChecked) {
        this.lastChecked = lastChecked;
    }

    /**
     * Sets the available.
     *
     * @param available
     *            the new available
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

}
