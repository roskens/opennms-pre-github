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

package org.opennms.sms.reflector.smsservice;

/**
 * <p>
 * MobileMsgResponseCallback interface.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
public interface MobileMsgResponseCallback {

    /**
     * return true if this completes the response for this request
     * return false if more responses are expected.
     *
     * @param request
     *            a
     * @param packet
     *            a
     * @return a boolean.
     *         {@link org.opennms.sms.reflector.smsservice.MobileMsgRequest}
     *         object.
     *         {@link org.opennms.sms.reflector.smsservice.MobileMsgResponse}
     *         object.
     */
    boolean handleResponse(MobileMsgRequest request, MobileMsgResponse packet);

    /**
     * <p>
     * handleTimeout
     * </p>
     * .
     *
     * @param request
     *            a
     *            {@link org.opennms.sms.reflector.smsservice.MobileMsgRequest}
     *            object.
     */
    void handleTimeout(MobileMsgRequest request);

    /**
     * <p>
     * handleError
     * </p>
     * .
     *
     * @param request
     *            a
     * @param t
     *            a {@link java.lang.Throwable} object.
     *            {@link org.opennms.sms.reflector.smsservice.MobileMsgRequest}
     *            object.
     */
    void handleError(MobileMsgRequest request, Throwable t);

}
