/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision;

import java.util.EventListener;

/**
 * The listener interface for receiving detectFuture events.
 * The class that is interested in processing a detectFuture
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDetectFutureListener<code> method. When
 * the detectFuture event occurs, that object's appropriate
 * method is invoked.
 *
 * @param <F>
 *            the generic type
 * @see DetectFutureEvent
 */
public interface DetectFutureListener<F extends DetectFuture> extends EventListener {

    /**
     * Operation complete.
     *
     * @param future
     *            the future
     */
    void operationComplete(F future);
}
