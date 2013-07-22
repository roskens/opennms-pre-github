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
 * PingResponseMatcher.
 *
 * @author brozow
 */
class PingResponseMatcher implements MobileMsgResponseMatcher {

    /* (non-Javadoc)
     * @see org.opennms.sms.reflector.smsservice.MobileMsgResponseMatcher#matches(org.opennms.sms.reflector.smsservice.MobileMsgRequest, org.opennms.sms.reflector.smsservice.MobileMsgResponse)
     */
    @Override
    public boolean matches(MobileMsgRequest request, MobileMsgResponse response) {

        if (!(request instanceof SmsRequest)) {
            return false;
        }
        if (!(response instanceof SmsResponse)) {
            return false;
        }

        SmsRequest smsRequest = (SmsRequest) request;
        SmsResponse smsResponse = (SmsResponse) response;

        return smsRequest.getRecipient().equals(smsResponse.getOriginator())
                && "pong".equalsIgnoreCase(smsResponse.getText());
    }
}
