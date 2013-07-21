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

package org.opennms.netmgt.provision.support;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The Class ConversationExchangeDefaultImpl.
 *
 * @param <Request>
 *            the generic type
 * @param <RespType>
 *            the generic type
 */
public class ConversationExchangeDefaultImpl<Request, RespType> implements ConversationExchange<Request, RespType> {

    /** The m_request builder. */
    private final RequestBuilder<Request> m_requestBuilder;

    /** The m_response validator. */
    private final ResponseValidator<RespType> m_responseValidator;

    /**
     * Instantiates a new conversation exchange default impl.
     *
     * @param reqBuilder
     *            the req builder
     * @param respValidator
     *            the resp validator
     */
    public ConversationExchangeDefaultImpl(RequestBuilder<Request> reqBuilder, ResponseValidator<RespType> respValidator) {
        m_requestBuilder = reqBuilder;
        m_responseValidator = respValidator;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.support.RequestBuilder#getRequest()
     */
    @Override
    public Request getRequest() {
        return m_requestBuilder == null ? null : m_requestBuilder.getRequest();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.support.ResponseValidator#validate(java.lang.Object)
     */
    @Override
    public boolean validate(RespType response) {
        return m_responseValidator.validate(response);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("request", getRequest());
        builder.append("responseValidator", m_responseValidator);
        return builder.toString();
    }
}
