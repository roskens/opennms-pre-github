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

package org.opennms.features.poller.remote.gwt.client;

import org.opennms.features.poller.remote.gwt.client.remoteevents.MapRemoteEvent;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;

/**
 * The Class TestServer.
 */
public class TestServer extends AbstractTestServer {

    /** The m_user specific listener. */
    private RemoteEventListener m_userSpecificListener;

    /** The m_domain listener. */
    private RemoteEventListener m_domainListener;

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.AbstractTestServer#addListener(de.novanic.eventservice.client.event.domain.Domain, de.novanic.eventservice.client.event.listener.RemoteEventListener)
     */
    @Override
    public void addListener(Domain aDomain, RemoteEventListener aRemoteListener) {
        if (aDomain == null) {
            m_userSpecificListener = aRemoteListener;
        } else {
            m_domainListener = aRemoteListener;
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.AbstractTestServer#start(com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void start(AsyncCallback<Void> anAsyncCallback) {
        anAsyncCallback.onSuccess(null);
    }

    /**
     * Send user specific event.
     *
     * @param remoteEvent
     *            the remote event
     */
    public void sendUserSpecificEvent(MapRemoteEvent remoteEvent) {
        m_userSpecificListener.apply(remoteEvent);
    }

    /**
     * Send domain event.
     *
     * @param remoteEvent
     *            the remote event
     */
    public void sendDomainEvent(MapRemoteEvent remoteEvent) {
        m_domainListener.apply(remoteEvent);
    }

}
