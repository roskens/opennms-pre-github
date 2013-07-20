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

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.opennms.protocols.rt.RequestLocator;

/**
 * MatchingRequestLocator.
 *
 * @author brozow
 * @version $Id: $
 */
public class MatchingRequestLocator implements RequestLocator<MobileMsgRequest, MobileMsgResponse> {

    /** The m_requests. */
    private final Set<MobileMsgRequest> m_requests = new CopyOnWriteArraySet<MobileMsgRequest>();

    /**
     * <p>
     * trackRequest
     * </p>
     * .
     *
     * @param request
     *            a
     * @return a boolean.
     *         {@link org.opennms.sms.reflector.smsservice.MobileMsgRequest}
     *         object.
     */
    @Override
    public boolean trackRequest(MobileMsgRequest request) {
        m_requests.add(request);
        return true;
    }

    /**
     * <p>
     * locateMatchingRequest
     * </p>
     * .
     *
     * @param response
     *            a
     * @return a {@link org.opennms.sms.reflector.smsservice.MobileMsgRequest}
     *         object.
     *         {@link org.opennms.sms.reflector.smsservice.MobileMsgResponse}
     *         object.
     */
    @Override
    public MobileMsgRequest locateMatchingRequest(MobileMsgResponse response) {
        for (MobileMsgRequest request : m_requests) {

            if (request.matches(response)) {
                return request;
            }
        }

        return null;
    }

    /**
     * <p>
     * requestTimedOut
     * </p>
     * .
     *
     * @param timedOutRequest
     *            a
     * @return a {@link org.opennms.sms.reflector.smsservice.MobileMsgRequest}
     *         object.
     *         {@link org.opennms.sms.reflector.smsservice.MobileMsgRequest}
     *         object.
     */
    @Override
    public MobileMsgRequest requestTimedOut(MobileMsgRequest timedOutRequest) {
        return m_requests.remove(timedOutRequest) ? timedOutRequest : null;
    }

    /**
     * <p>
     * requestComplete
     * </p>
     * .
     *
     * @param request
     *            a
     *            {@link org.opennms.sms.reflector.smsservice.MobileMsgRequest}
     *            object.
     */
    @Override
    public void requestComplete(MobileMsgRequest request) {
        m_requests.remove(request);
    }

}
