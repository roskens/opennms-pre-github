/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.spring.xml;

import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.model.events.annotations.EventHandler;
import org.opennms.netmgt.model.events.annotations.EventListener;
import org.opennms.netmgt.xml.event.Event;

/**
 * The Class AspectJTestEventHandler.
 */
@EventListener(name = "AspectJTestEventHandler")
public class AspectJTestEventHandler {

    /** The thrown exception. */
    private Throwable thrownException = null;

    /** The handler call count. */
    private int handlerCallCount = 0;

    /**
     * Sets the thrown exception.
     *
     * @param throwable
     *            the new thrown exception
     */
    public void setThrownException(Throwable throwable) {
        this.thrownException = throwable;
    }

    /**
     * Gets the handler call count.
     *
     * @return the handler call count
     */
    public int getHandlerCallCount() {
        return handlerCallCount;
    }

    /**
     * Sets the handler call count.
     *
     * @param handlerCallCount
     *            the new handler call count
     */
    public void setHandlerCallCount(int handlerCallCount) {
        this.handlerCallCount = handlerCallCount;
    }

    /**
     * Handle an event.
     *
     * @param e
     *            the e
     * @throws Throwable
     *             the throwable
     */
    @EventHandler(uei = EventConstants.ADD_INTERFACE_EVENT_UEI)
    public void handleAnEvent(Event e) throws Throwable {
        System.err.println("Received Event " + e.getUei());
        handlerCallCount++;
        if (thrownException != null) {
            throw thrownException;
        }
    }

    /**
     * Reset.
     */
    public void reset() {
        handlerCallCount = 0;
        thrownException = null;
    }
}
