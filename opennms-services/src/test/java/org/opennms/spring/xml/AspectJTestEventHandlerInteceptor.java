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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.opennms.netmgt.xml.event.Event;
import org.springframework.core.Ordered;

/**
 * The Class AspectJTestEventHandlerInteceptor.
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 */
@Aspect
public class AspectJTestEventHandlerInteceptor implements Ordered {

    /**
     * Test methods.
     */
    @Pointcut("execution(* *..AspectJTestEventHandler.*(..))")
    public void testMethods() {
    }

    /**
     * Event handlers.
     */
    @Pointcut("@annotation(org.opennms.netmgt.model.events.annotations.EventHandler)")
    public void eventHandlers() {
    }

    /**
     * Test event handlers.
     */
    @Pointcut("testMethods() && eventHandlers()")
    public void testEventHandlers() {
    }

    /** The m_pre event count. */
    private int m_preEventCount;

    /** The m_post event count. */
    private int m_postEventCount;

    /** The m_handled exception count. */
    private int m_handledExceptionCount;

    /** The m_order. */
    private int m_order = 0;

    /**
     * Gets the pre event count.
     *
     * @return the pre event count
     */
    public int getPreEventCount() {
        return m_preEventCount;
    }

    /**
     * Gets the post event count.
     *
     * @return the post event count
     */
    public int getPostEventCount() {
        return m_postEventCount;
    }

    /**
     * Gets the handled exception count.
     *
     * @return the handled exception count
     */
    public int getHandledExceptionCount() {
        return m_handledExceptionCount;
    }

    /**
     * On event.
     *
     * @param pjp
     *            the pjp
     * @param event
     *            the event
     * @throws Throwable
     *             the throwable
     */
    @Around("testEventHandlers() && args(event)")
    public void onEvent(ProceedingJoinPoint pjp, Event event) throws Throwable {
        preEvent(event);

        try {
            pjp.proceed();
            postEvent(event);
        } catch (RuntimeException ex) {
            handleException(event, ex);
        }
    }

    /**
     * Handle exception.
     *
     * @param event
     *            the event
     * @param ex
     *            the ex
     */
    private void handleException(Event event, RuntimeException ex) {
        System.err.println("handleException");
        m_handledExceptionCount++;
    }

    /**
     * Post event.
     *
     * @param event
     *            the event
     */
    private void postEvent(Event event) {
        System.err.println("postEvent");
        m_postEventCount++;
    }

    /**
     * Pre event.
     *
     * @param event
     *            the event
     */
    private void preEvent(Event event) {
        System.err.println("preEvent");
        m_preEventCount++;
    }

    /**
     * Reset.
     */
    public void reset() {
        m_preEventCount = 0;
        m_postEventCount = 0;
        m_handledExceptionCount = 0;
    }

    /**
     * Sets the order.
     *
     * @param order
     *            the new order
     */
    public void setOrder(int order) {
        m_order = order;
    }

    /* (non-Javadoc)
     * @see org.springframework.core.Ordered#getOrder()
     */
    @Override
    public int getOrder() {
        return m_order;
    }

}
