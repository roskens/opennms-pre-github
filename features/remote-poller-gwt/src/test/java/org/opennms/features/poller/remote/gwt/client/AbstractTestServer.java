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

import java.util.List;
import java.util.Set;

import org.opennms.features.poller.remote.gwt.client.location.LocationDetails;
import org.opennms.features.poller.remote.gwt.client.location.LocationInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.novanic.eventservice.client.event.RemoteEventService;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.filter.EventFilter;
import de.novanic.eventservice.client.event.listener.RemoteEventListener;
import de.novanic.eventservice.client.event.listener.unlisten.UnlistenEvent;
import de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener;
import de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener.Scope;

/**
 * The Class AbstractTestServer.
 */
public abstract class AbstractTestServer implements RemoteEventService, LocationStatusServiceAsync {

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.LocationStatusServiceAsync#start(com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void start(AsyncCallback<Void> anAsyncCallback) {
        throw new UnsupportedOperationException("start is not implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.LocationStatusServiceAsync#getLocationInfo(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getLocationInfo(String locationName, AsyncCallback<LocationInfo> callback) {
        throw new UnsupportedOperationException("getLocationInfo is not implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.LocationStatusServiceAsync#getLocationDetails(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getLocationDetails(String locationName, AsyncCallback<LocationDetails> callback) {
        throw new UnsupportedOperationException("getLocationDetails is not implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.LocationStatusServiceAsync#getApplicationInfo(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getApplicationInfo(String applicationName, AsyncCallback<ApplicationInfo> callback) {
        throw new UnsupportedOperationException("getApplicationInfo is not implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.LocationStatusServiceAsync#getApplicationDetails(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void getApplicationDetails(String applicationName, AsyncCallback<ApplicationDetails> callback) {
        throw new UnsupportedOperationException("getApplicationDetails is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#addListener(de.novanic.eventservice.client.event.domain.Domain, de.novanic.eventservice.client.event.listener.RemoteEventListener)
     */
    @Override
    public void addListener(Domain aDomain, RemoteEventListener aRemoteListener) {
        throw new UnsupportedOperationException("addListener is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#addListener(de.novanic.eventservice.client.event.domain.Domain, de.novanic.eventservice.client.event.listener.RemoteEventListener, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void addListener(Domain aDomain, RemoteEventListener aRemoteListener, AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("addListener is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#addListener(de.novanic.eventservice.client.event.domain.Domain, de.novanic.eventservice.client.event.listener.RemoteEventListener, de.novanic.eventservice.client.event.filter.EventFilter)
     */
    @Override
    public void addListener(Domain aDomain, RemoteEventListener aRemoteListener, EventFilter anEventFilter) {
        throw new UnsupportedOperationException("addListener is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#addListener(de.novanic.eventservice.client.event.domain.Domain, de.novanic.eventservice.client.event.listener.RemoteEventListener, de.novanic.eventservice.client.event.filter.EventFilter, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void addListener(Domain aDomain, RemoteEventListener aRemoteListener, EventFilter anEventFilter,
            AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("addListener is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#addUnlistenListener(de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void addUnlistenListener(UnlistenEventListener anUnlistenEventListener, AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("addUnlistenListener is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#addUnlistenListener(de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener.Scope, de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void addUnlistenListener(Scope anUnlistenScope, UnlistenEventListener anUnlistenEventListener,
            AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("addUnlistenListener is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#addUnlistenListener(de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener, de.novanic.eventservice.client.event.listener.unlisten.UnlistenEvent, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void addUnlistenListener(UnlistenEventListener anUnlistenEventListener, UnlistenEvent anUnlistenEvent,
            AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("addUnlistenListener is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#addUnlistenListener(de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener.Scope, de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener, de.novanic.eventservice.client.event.listener.unlisten.UnlistenEvent, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void addUnlistenListener(Scope anUnlistenScope, UnlistenEventListener anUnlistenEventListener,
            UnlistenEvent anUnlistenEvent, AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("addUnlistenListener is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#removeListener(de.novanic.eventservice.client.event.domain.Domain, de.novanic.eventservice.client.event.listener.RemoteEventListener)
     */
    @Override
    public void removeListener(Domain aDomain, RemoteEventListener aRemoteListener) {
        throw new UnsupportedOperationException("removeListener is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#removeListener(de.novanic.eventservice.client.event.domain.Domain, de.novanic.eventservice.client.event.listener.RemoteEventListener, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void removeListener(Domain aDomain, RemoteEventListener aRemoteListener, AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("removeListener is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#registerEventFilter(de.novanic.eventservice.client.event.domain.Domain, de.novanic.eventservice.client.event.filter.EventFilter)
     */
    @Override
    public void registerEventFilter(Domain aDomain, EventFilter anEventFilter) {
        throw new UnsupportedOperationException("registerEventFilter is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#registerEventFilter(de.novanic.eventservice.client.event.domain.Domain, de.novanic.eventservice.client.event.filter.EventFilter, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void registerEventFilter(Domain aDomain, EventFilter anEventFilter, AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("registerEventFilter is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#deregisterEventFilter(de.novanic.eventservice.client.event.domain.Domain)
     */
    @Override
    public void deregisterEventFilter(Domain aDomain) {
        throw new UnsupportedOperationException("deregisterEventFilter is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#deregisterEventFilter(de.novanic.eventservice.client.event.domain.Domain, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void deregisterEventFilter(Domain aDomain, AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("deregisterEventFilter is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#isActive()
     */
    @Override
    public boolean isActive() {
        throw new UnsupportedOperationException("isActive is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#getActiveDomains()
     */
    @Override
    public Set<Domain> getActiveDomains() {
        throw new UnsupportedOperationException("getActiveDomains is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#getRegisteredListeners(de.novanic.eventservice.client.event.domain.Domain)
     */
    @Override
    public List<RemoteEventListener> getRegisteredListeners(Domain aDomain) {
        throw new UnsupportedOperationException("getRegisteredListeners is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#removeListeners()
     */
    @Override
    public void removeListeners() {
        throw new UnsupportedOperationException("removeListeners is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#removeListeners(com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void removeListeners(AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("removeListeners is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#removeListeners(java.util.Set)
     */
    @Override
    public void removeListeners(Set<Domain> aDomains) {
        throw new UnsupportedOperationException("removeListeners is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#removeListeners(java.util.Set, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void removeListeners(Set<Domain> aDomains, AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("removeListeners is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#removeListeners(de.novanic.eventservice.client.event.domain.Domain)
     */
    @Override
    public void removeListeners(Domain aDomain) {
        throw new UnsupportedOperationException("removeListeners is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#removeListeners(de.novanic.eventservice.client.event.domain.Domain, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void removeListeners(Domain aDomain, AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("removeListeners is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#removeUnlistenListener(de.novanic.eventservice.client.event.listener.unlisten.UnlistenEventListener, com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void removeUnlistenListener(UnlistenEventListener anUnlistenEventListener, AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("removeUnlistenListener is not implemented");
    }

    /* (non-Javadoc)
     * @see de.novanic.eventservice.client.event.RemoteEventService#removeUnlistenListeners(com.google.gwt.user.client.rpc.AsyncCallback)
     */
    @Override
    public void removeUnlistenListeners(AsyncCallback<Void> aCallback) {
        throw new UnsupportedOperationException("removeUnlistenListeners is not implemented");
    }

}
