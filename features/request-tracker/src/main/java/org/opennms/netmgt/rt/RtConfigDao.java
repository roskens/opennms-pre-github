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

package org.opennms.netmgt.rt;

import java.io.IOException;
import java.util.List;

/**
 * The Interface RtConfigDao.
 */
public interface RtConfigDao {

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername();

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword();

    /**
     * Gets the queue.
     *
     * @return the queue
     */
    public String getQueue();

    /**
     * Gets the valid closed status.
     *
     * @return the valid closed status
     */
    public List<String> getValidClosedStatus();

    /**
     * Gets the valid open status.
     *
     * @return the valid open status
     */
    public List<Integer> getValidOpenStatus();

    /**
     * Gets the valid cancelled status.
     *
     * @return the valid cancelled status
     */
    public List<String> getValidCancelledStatus();

    /**
     * Gets the open status.
     *
     * @return the open status
     */
    public String getOpenStatus();

    /**
     * Gets the closed status.
     *
     * @return the closed status
     */
    public String getClosedStatus();

    /**
     * Gets the cancelled status.
     *
     * @return the cancelled status
     */
    public String getCancelledStatus();

    /**
     * Gets the requestor.
     *
     * @return the requestor
     */
    public String getRequestor();

    /**
     * Gets the base url.
     *
     * @return the base url
     */
    public String getBaseURL();

    /**
     * Gets the timeout.
     *
     * @return the timeout
     */
    public int getTimeout();

    /**
     * Gets the retry.
     *
     * @return the retry
     */
    public int getRetry();

    /**
     * Save.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void save() throws IOException;
}
