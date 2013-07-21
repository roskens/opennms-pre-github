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

package org.opennms.jicmp.standalone;

import java.util.concurrent.TimeUnit;

/**
 * PingReply.
 *
 * @author brozow
 */
interface PingReply {

    /**
     * Gets the sent time nanos.
     *
     * @return the sent time nanos
     */
    public abstract long getSentTimeNanos();

    /**
     * Gets the received time nanos.
     *
     * @return the received time nanos
     */
    public abstract long getReceivedTimeNanos();

    /**
     * Gets the elapsed time nanos.
     *
     * @return the elapsed time nanos
     */
    public abstract long getElapsedTimeNanos();

    /**
     * Elapsed time.
     *
     * @param unit
     *            the unit
     * @return the double
     */
    public abstract double elapsedTime(TimeUnit unit);

    /**
     * Gets the thread id.
     *
     * @return the thread id
     */
    public abstract long getThreadId();

}
