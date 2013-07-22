/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.core.tasks;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * TaskWaiter.
 *
 * @author brozow
 * @version $Id: $
 */
public interface TaskWaiter {

    /**
     * <p>
     * waitFor
     * </p>
     * .
     *
     * @throws InterruptedException
     *             the interrupted exception
     * @throws ExecutionException
     *             the execution exception
     */
    void waitFor() throws InterruptedException, ExecutionException;

    /**
     * <p>
     * waitFor
     * </p>
     * .
     *
     * @param timeout
     *            a long.
     * @param unit
     *            a {@link java.util.concurrent.TimeUnit} object.
     * @throws InterruptedException
     *             the interrupted exception
     * @throws ExecutionException
     *             the execution exception
     * @throws TimeoutException
     *             the timeout exception
     */
    void waitFor(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
