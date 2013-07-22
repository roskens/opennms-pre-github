/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.core.queue;

/**
 * <p>
 * This interface defines a FIFO queue that can be open and closed to control
 * the addition of elements to the queue. When the queue is opened it is
 * possible to add new elements to the queue. When the queue is closed, it is
 * should not be possible to add elements to the queue. It should always be
 * possible to read elements from the queue, so long as it is not empty.
 * </p>
 *
 * @param <T>
 *            the generic type
 * @author <a href="mailto:weave@oculan.com">Brian Weaver </a>
 */
public interface ClosableFifoQueue<T> extends FifoQueue<T> {

    /**
     * Returns true if the queue is currently open.
     *
     * @return True if the queue is open.
     */
    boolean isOpen();

    /**
     * Returns true if the queue is currently closed.
     *
     * @return True if the queue is closed.
     */
    boolean isClosed();

    /**
     * Closes a currently open queue. When a queue is closed is should still
     * allow elements already in the queue to be removed, but new elements
     * should not be added.
     *
     * @throws FifoQueueException
     *             the fifo queue exception
     */
    void close() throws FifoQueueException;

    /**
     * Ensures that the queue is open and new elements can be added to the
     * queue.
     *
     * @throws FifoQueueException
     *             the fifo queue exception
     */
    void open() throws FifoQueueException;
}
