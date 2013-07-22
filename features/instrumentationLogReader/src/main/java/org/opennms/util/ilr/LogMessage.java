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

package org.opennms.util.ilr;

import java.util.Date;

/**
 * The Interface LogMessage.
 */
public interface LogMessage {

    /**
     * Checks if is end message.
     *
     * @return true, if is end message
     */
    boolean isEndMessage();

    /**
     * Checks if is persist message.
     *
     * @return true, if is persist message
     */
    boolean isPersistMessage();

    /**
     * Checks if is persist begin message.
     *
     * @return true, if is persist begin message
     */
    boolean isPersistBeginMessage();

    /**
     * Checks if is persist end message.
     *
     * @return true, if is persist end message
     */
    boolean isPersistEndMessage();

    /**
     * Checks if is begin message.
     *
     * @return true, if is begin message
     */
    boolean isBeginMessage();

    /**
     * Checks if is error message.
     *
     * @return true, if is error message
     */
    boolean isErrorMessage();

    /**
     * Checks if is collector begin message.
     *
     * @return true, if is collector begin message
     */
    boolean isCollectorBeginMessage();

    /**
     * Checks if is collector end message.
     *
     * @return true, if is collector end message
     */
    boolean isCollectorEndMessage();

    /**
     * Gets the date.
     *
     * @return the date
     */
    Date getDate();

    /**
     * Gets the service id.
     *
     * @return the service id
     */
    String getServiceID();

    /**
     * Gets the thread.
     *
     * @return the thread
     */
    String getThread();

}
