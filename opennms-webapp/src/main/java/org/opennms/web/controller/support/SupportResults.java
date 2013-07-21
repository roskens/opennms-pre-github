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

package org.opennms.web.controller.support;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.netmgt.rt.RTTicket;

/**
 * The Class SupportResults.
 */
public class SupportResults implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2119247915337079075L;

    /** The m_success. */
    private boolean m_success = true;

    /** The m_needs login. */
    private boolean m_needsLogin = false;

    /** The m_base url. */
    private String m_baseUrl;

    /** The m_username. */
    private String m_username;

    /** The m_queue. */
    private String m_queue;

    /** The m_latest tickets. */
    private List<RTTicket> m_latestTickets;

    /** The m_message. */
    private String m_message;

    /**
     * Instantiates a new support results.
     */
    public SupportResults() {
    }

    /**
     * Gets the success.
     *
     * @return the success
     */
    public boolean getSuccess() {
        return m_success;
    }

    /**
     * Sets the success.
     *
     * @param success
     *            the new success
     */
    public void setSuccess(final boolean success) {
        m_success = success;
    }

    /**
     * Gets the needs login.
     *
     * @return the needs login
     */
    public boolean getNeedsLogin() {
        return m_needsLogin;
    }

    /**
     * Sets the needs login.
     *
     * @param needsLogin
     *            the new needs login
     */
    public void setNeedsLogin(final boolean needsLogin) {
        m_needsLogin = needsLogin;
    }

    /**
     * Gets the rT url.
     *
     * @return the rT url
     */
    public String getRTUrl() {
        return m_baseUrl;
    }

    /**
     * Sets the rT url.
     *
     * @param baseUrl
     *            the new rT url
     */
    public void setRTUrl(final String baseUrl) {
        m_baseUrl = baseUrl;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return m_username;
    }

    /**
     * Sets the username.
     *
     * @param username
     *            the new username
     */
    public void setUsername(final String username) {
        m_username = username;
    }

    /**
     * Gets the queue.
     *
     * @return the queue
     */
    public String getQueue() {
        return m_queue;
    }

    /**
     * Sets the queue.
     *
     * @param queue
     *            the new queue
     */
    public void setQueue(final String queue) {
        m_queue = queue;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return m_message;
    }

    /**
     * Sets the message.
     *
     * @param message
     *            the new message
     */
    public void setMessage(final String message) {
        m_message = message;
    }

    /**
     * Gets the latest tickets.
     *
     * @return the latest tickets
     */
    public List<RTTicket> getLatestTickets() {
        return m_latestTickets;
    }

    /**
     * Sets the latest tickets.
     *
     * @param tickets
     *            the new latest tickets
     */
    public void setLatestTickets(final List<RTTicket> tickets) {
        m_latestTickets = tickets;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("success", m_success).append("needsLogin", m_needsLogin).append("baseUrl",
                                                                                                                m_baseUrl).append("username",
                                                                                                                                  m_username).append("queue",
                                                                                                                                                     m_queue).append("message",
                                                                                                                                                                     m_message).append("latestTickets",
                                                                                                                                                                                       m_latestTickets).toString();
    }

}
