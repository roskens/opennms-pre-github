/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.sms.monitor.internal;

import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.core.tasks.Callback;
import org.opennms.sms.monitor.MobileSequenceSession;
import org.opennms.sms.monitor.internal.config.MobileSequenceResponse;
import org.opennms.sms.monitor.internal.config.MobileSequenceTransaction;
import org.opennms.sms.reflector.smsservice.MobileMsgRequest;
import org.opennms.sms.reflector.smsservice.MobileMsgResponse;
import org.opennms.sms.reflector.smsservice.MobileMsgResponseHandler;

/**
 * MobileTransactionExecution.
 *
 * @author brozow
 * @version $Id: $
 */
public class MobileTransactionExecution {

    /**
     * TransactionResponseHandler.
     *
     * @author brozow
     */
    private final class TransactionResponseHandler implements MobileMsgResponseHandler {

        /** The m_cb. */
        private final Callback<MobileMsgResponse> m_cb;

        /** The m_session. */
        private final MobileSequenceSession m_session;

        /** The m_pending responses. */
        private final Set<MobileSequenceResponse> m_pendingResponses;

        /**
         * Instantiates a new transaction response handler.
         *
         * @param session
         *            the session
         * @param cb
         *            the cb
         */
        private TransactionResponseHandler(MobileSequenceSession session, Callback<MobileMsgResponse> cb) {
            m_cb = cb;
            m_session = session;
            m_pendingResponses = Collections.synchronizedSet(new LinkedHashSet<MobileSequenceResponse>(
                                                                                                       getTransaction().getResponses()));
        }

        /* (non-Javadoc)
         * @see org.opennms.sms.reflector.smsservice.MobileMsgResponseMatcher#matches(org.opennms.sms.reflector.smsservice.MobileMsgRequest, org.opennms.sms.reflector.smsservice.MobileMsgResponse)
         */
        @Override
        public boolean matches(MobileMsgRequest request, MobileMsgResponse response) {

            synchronized (m_pendingResponses) {

                for (MobileSequenceResponse r : m_pendingResponses) {
                    if (r.matches(m_session, request, response)) {
                        return true;
                    }
                }

                return false;
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.sms.reflector.smsservice.MobileMsgResponseCallback#handleTimeout(org.opennms.sms.reflector.smsservice.MobileMsgRequest)
         */
        @Override
        public void handleTimeout(MobileMsgRequest request) {
            SocketTimeoutException err = new SocketTimeoutException("timed out processing request " + request);
            setError(err);
            m_cb.handleException(err);
        }

        /* (non-Javadoc)
         * @see org.opennms.sms.reflector.smsservice.MobileMsgResponseCallback#handleResponse(org.opennms.sms.reflector.smsservice.MobileMsgRequest, org.opennms.sms.reflector.smsservice.MobileMsgResponse)
         */
        @Override
        public boolean handleResponse(MobileMsgRequest request, MobileMsgResponse response) {
            if (request != null) {
                setSendTime(request.getSentTime());
            }
            if (response != null) {
                setReceiveTime(response.getReceiveTime());
            }

            synchronized (m_pendingResponses) {
                // remove processing response
                for (Iterator<MobileSequenceResponse> it = m_pendingResponses.iterator(); it.hasNext();) {
                    MobileSequenceResponse r = it.next();
                    if (r.matches(m_session, request, response)) {

                        r.processResponse(m_session, request, response);

                        it.remove();
                    }
                }

            }
            m_cb.complete(response);

            // return true only if all of the expected responses have been
            // processed
            return !m_pendingResponses.isEmpty();
        }

        /* (non-Javadoc)
         * @see org.opennms.sms.reflector.smsservice.MobileMsgResponseCallback#handleError(org.opennms.sms.reflector.smsservice.MobileMsgRequest, java.lang.Throwable)
         */
        @Override
        public void handleError(MobileMsgRequest request, Throwable t) {
            setError(t);
            m_cb.handleException(t);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return new ToStringBuilder(this).append("callback", m_cb).append("session", m_session).toString();
        }
    }

    /** The m_transaction. */
    private MobileSequenceTransaction m_transaction;

    /** The m_send time. */
    private Long m_sendTime;

    /** The m_receive time. */
    private Long m_receiveTime;

    /** The m_error. */
    private Throwable m_error;

    /**
     * <p>
     * Constructor for MobileTransactionExecution.
     * </p>
     *
     * @param transaction
     *            a
     *            {@link org.opennms.sms.monitor.internal.config.MobileSequenceTransaction}
     *            object.
     */
    public MobileTransactionExecution(MobileSequenceTransaction transaction) {
        m_transaction = transaction;
    }

    /**
     * Sets the send time.
     *
     * @param sendTime
     *            the new send time
     */
    private void setSendTime(Long sendTime) {
        m_sendTime = sendTime;
    }

    /**
     * Sets the receive time.
     *
     * @param receiveTime
     *            the new receive time
     */
    private void setReceiveTime(Long receiveTime) {
        m_receiveTime = receiveTime;
    }

    /**
     * <p>
     * getLatency
     * </p>
     * .
     *
     * @return the latency
     */
    public Long getLatency() {
        return m_sendTime == null || m_receiveTime == null ? null : m_receiveTime - m_sendTime;
    }

    /**
     * <p>
     * getError
     * </p>
     * .
     *
     * @return the error
     */
    public Throwable getError() {
        return m_error;
    }

    /**
     * <p>
     * setError
     * </p>
     * .
     *
     * @param error
     *            the error to set
     */
    public void setError(Throwable error) {
        m_error = error;
    }

    /**
     * <p>
     * getTransaction
     * </p>
     * .
     *
     * @return a
     *         {@link org.opennms.sms.monitor.internal.config.MobileSequenceTransaction}
     *         object.
     */
    public MobileSequenceTransaction getTransaction() {
        return m_transaction;
    }

    /**
     * Gets the callback.
     *
     * @return the callback
     */
    Callback<MobileMsgResponse> getCallback() {
        return new Callback<MobileMsgResponse>() {
            @Override
            public void complete(MobileMsgResponse t) {
                if (t != null) {
                    setSendTime(t.getRequest().getSentTime());
                    setReceiveTime(t.getReceiveTime());
                }
            }

            @Override
            public void handleException(Throwable t) {
                setError(t);
            }
        };
    }

    /**
     * Send request.
     *
     * @param session
     *            the session
     * @param cb
     *            the cb
     */
    void sendRequest(MobileSequenceSession session, Callback<MobileMsgResponse> cb) {
        getTransaction().sendRequest(session, new TransactionResponseHandler(session, cb));
    }

}
