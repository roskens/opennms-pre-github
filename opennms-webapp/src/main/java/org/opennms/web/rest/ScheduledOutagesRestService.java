/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

package org.opennms.web.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.config.CollectdConfigFactory;
import org.opennms.netmgt.config.CollectdPackage;
import org.opennms.netmgt.config.NotifdConfigFactory;
import org.opennms.netmgt.config.PollOutagesConfigFactory;
import org.opennms.netmgt.config.PollerConfigFactory;
import org.opennms.netmgt.config.ThreshdConfigFactory;
import org.opennms.netmgt.config.poller.Outage;
import org.opennms.netmgt.config.poller.Outages;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.jersey.spi.resource.PerRequest;

/**
 * <p>ScheduledOutagesRestService class.</p>
 * 
 * <ul>
 * <li><b>GET /sched-outages</b><br>to get a list of configured scheduled outages.</li>
 * <li><b>POST /sched-outages</b><br>to add a new outage (or update an existing one).</li>
 * <li><b>GET /sched-outages/{outageId}</b><br>to get the details of a specific outage.</li>
 * <li><b>DELETE /sched-outages/{outageId}</b><br>to delete a specific outage.</li>
 * <li><b>PUT /sched-outages/{outageId}/collectd/{package}</b><br>to add a specific outage to a collectd's package.</li>
 * <li><b>PUT /sched-outages/{outageId}/pollerd/{package}</b><br>to add a specific outage to a pollerd's package.</li>
 * <li><b>PUT /sched-outages/{outageId}/threshd/{package}</b><br>to add a specific outage to a threshd's package.</li>
 * <li><b>PUT /sched-outages/{outageId}/notifd</b><br>to add a specific outage to the notifications.</li>
 * <li><b>DELETE /sched-outages/{outageId}/collectd/{package}</b><br>to remove a specific outage from a collectd's package.</li>
 * <li><b>DELETE /sched-outages/{outageId}/pollerd/{package}</b><br>to remove a specific outage from a pollerd's package.</li>
 * <li><b>DELETE /sched-outages/{outageId}/threshd/{package}</b><br>to remove a specific outage from a threshd's package.</li>
 * <li><b>DELETE /sched-outages/{outageId}/notifd</b><br>to remove a specific outage from the notifications.</li>
 * </ul>
 * 
 * <p>Node and Interface status (the requests return true or false):</p>
 * <ul>
 * <li><b>GET /sched-outages/{outageId}/nodeInOutage/{nodeId}</b><br>to check if a node (with a specific nodeId) is currently on outage for a specific scheduled outage calendar.</li>
 * <li><b>GET /sched-outages/{outageId}/interfaceInOutage/{ipAddr}</b><br>to check if an interface (with a specific IP address) is currently on outage for a specific scheduled outage calendar.</li>
 * <li><b>GET /sched-outages/nodeInOutage/{nodeId}</b><br>to check if a node (with a specific nodeId) is currently in outage.</li>
 * <li><b>GET /sched-outages/interfaceInOutage/{ipAddr}</b><br>to check if an interface (with a specific IP address) is currently on outage.</li>
 * </ul>
 * 
 * @author Alejandro Galue <agalue@opennms.org>
 */
@Component
@PerRequest
@Scope("prototype")
@Path("sched-outages")
public class ScheduledOutagesRestService extends OnmsRestService {

    private enum ConfigAction { ADD, REMOVE, REMOVE_FROM_ALL };
    
    @Context 
    UriInfo m_uriInfo;
    
    @Autowired
    protected PollOutagesConfigFactory m_configFactory;

    @Autowired
    protected EventProxy m_eventProxy;

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Outages getOutages() {
        readLock();
        try {
            Outages outages = new Outages();
            outages.setOutage(m_configFactory.getOutages());
            return outages;
        } finally {
            readUnlock();
        }
    }

    @GET
    @Path("{outageId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Outage getOutage(@PathParam("outageId") Long outageId) throws IllegalArgumentException {
        readLock();
        try {
            Outage outage = m_configFactory.getOutage(outageId);
            if (outage == null) throw new IllegalArgumentException("Scheduled outage " + outageId + " does not exist.");
            return outage;
        } finally {
            readUnlock();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response saveOrUpdateOutage(final Outage newOutage) {
        writeLock();
        try {
            if (newOutage == null) throw getException(Status.BAD_REQUEST, "Outage object can't be null");
            Outage oldOutage = m_configFactory.getOutage(newOutage.getId());
            if (oldOutage == null) {
                log().debug("saveOrUpdateOutage: adding outage " + newOutage.getId());
                m_configFactory.addOutage(newOutage);
            } else {
                log().debug("saveOrUpdateOutage: updating outage " + newOutage.getId());
                m_configFactory.replaceOutage(oldOutage, newOutage);
            }
            m_configFactory.saveCurrent();
            sendConfigChangedEvent();
            return Response.seeOther(m_uriInfo.getBaseUriBuilder().path(this.getClass(), "getOutage").build(newOutage.getId())).build();
            // return Response.ok().build();
        } catch (Exception e) {
            throw getException(Status.BAD_REQUEST, "Can't save or update the scheduled outage " + newOutage.getId() + " because, " + e.getMessage());
        } finally {
            writeUnlock();
        }
    }

    @DELETE
    @Path("{outageId}")
    public Response deleteOutage(@PathParam("outageId") Long outageId) {
        writeLock();
        try {
            log().debug("deleteOutage: deleting outage " + outageId);
            updateCollectd(ConfigAction.REMOVE_FROM_ALL, outageId, null);
            updatePollerd(ConfigAction.REMOVE_FROM_ALL, outageId, null);
            updateThreshd(ConfigAction.REMOVE_FROM_ALL, outageId, null);
            updateNotifd(ConfigAction.REMOVE, outageId);
            m_configFactory.removeOutage(outageId);
            m_configFactory.saveCurrent();
            sendConfigChangedEvent();
            return Response.ok().build();
        } catch (Exception e) {
            throw getException(Status.BAD_REQUEST, "Can't delete the scheduled outage " + outageId + " because, " + e.getMessage());
        } finally {
            writeUnlock();
        }
    }

    @PUT
    @Path("{outageId}/collectd/{packageName}")
    public Response addOutageToCollector(@PathParam("outageId") Long outageId, @PathParam("packageName") String packageName) {
        writeLock();
        try {
            updateCollectd(ConfigAction.ADD, outageId, packageName);
            sendConfigChangedEvent();
            return Response.seeOther(m_uriInfo.getBaseUriBuilder().path(this.getClass(), "getOutage").build(outageId)).build();
            // return Response.ok().build();
        } catch (Exception e) {
            throw getException(Status.BAD_REQUEST, "Can't add scheduled outage " + outageId + " to collector package " + packageName + ", because: " + e.getMessage());
        } finally {
            writeUnlock();
        }
    }

    @DELETE
    @Path("{outageId}/collectd/{packageName}")
    public Response removeOutageFromCollector(@PathParam("outageId") Long outageId, @PathParam("packageName") String packageName) {
        writeLock();
        try {
            updateCollectd(ConfigAction.REMOVE, outageId, packageName);
            sendConfigChangedEvent();
            return Response.ok().build();
        } catch (Exception e) {
            throw getException(Status.BAD_REQUEST, "Can't remove scheduled outage " + outageId + " from collector package " + packageName + ", because: " + e.getMessage());
        } finally {
            writeUnlock();
        }
    }

    @PUT
    @Path("{outageId}/pollerd/{packageName}")
    public Response addOutageToPoller(@PathParam("outageId") Long outageId, @PathParam("packageName") String packageName) {
        writeLock();
        try {
            updatePollerd(ConfigAction.ADD, outageId, packageName);
            sendConfigChangedEvent();
            return Response.seeOther(m_uriInfo.getBaseUriBuilder().path(this.getClass(), "getOutage").build(outageId)).build();
            // return Response.ok().build();
        } catch (Exception e) {
            throw getException(Status.BAD_REQUEST, "Can't add scheduled outage " + outageId + " to poller package " + packageName  + ", because: " + e.getMessage());
        } finally {
            writeUnlock();
        }
    }

    @DELETE
    @Path("{outageId}/pollerd/{packageName}")
    public Response removeOutageFromPoller(@PathParam("outageId") Long outageId, @PathParam("packageName") String packageName) {
        writeLock();
        try {
            updatePollerd(ConfigAction.REMOVE, outageId, packageName);
            sendConfigChangedEvent();
            return Response.ok().build();
        } catch (Exception e) {
            throw getException(Status.BAD_REQUEST, "Can't remove scheduled outage " + outageId + " from poller package " + packageName + ", because: " + e.getMessage());
        } finally {
            writeUnlock();
        }
    }

    @PUT
    @Path("{outageId}/threshd/{packageName}")
    public Response addOutageToThresholder(@PathParam("outageId") Long outageId, @PathParam("packageName") String packageName) {
        writeLock();
        try {
            updateThreshd(ConfigAction.ADD, outageId, packageName);
            sendConfigChangedEvent();
            return Response.seeOther(m_uriInfo.getBaseUriBuilder().path(this.getClass(), "getOutage").build(outageId)).build();
            // return Response.ok().build();
        } catch (Exception e) {
            throw getException(Status.BAD_REQUEST, "Can't add scheduled outage " + outageId + " to threshold package " + packageName + ", because: " + e.getMessage());
        } finally {
            writeUnlock();
        }
    }

    @DELETE
    @Path("{outageId}/threshd/{packageName}")
    public Response removeOutageFromThresholder(@PathParam("outageId") Long outageId, @PathParam("packageName") String packageName) {
        writeLock();
        try {
            updateThreshd(ConfigAction.REMOVE, outageId, packageName);
            sendConfigChangedEvent();
            return Response.ok().build();
        } catch (Exception e) {
            throw getException(Status.BAD_REQUEST, "Can't remove scheduled outage " + outageId + " from threshold package " + packageName + ", because: " + e.getMessage());
        } finally {
            writeUnlock();
        }
    }

    @PUT
    @Path("{outageId}/notifd")
    public Response addOutageToNotifications(@PathParam("outageId") Long outageId) {
        writeLock();
        try {
            updateNotifd(ConfigAction.ADD, outageId);
            sendConfigChangedEvent();
            return Response.seeOther(m_uriInfo.getBaseUriBuilder().path(this.getClass(), "getOutage").build(outageId)).build();
            // return Response.ok().build();
        } catch (Exception e) {
            throw getException(Status.BAD_REQUEST, "Can't add scheduled outage " + outageId + " to notifications because: " + e.getMessage());
        } finally {
            writeUnlock();
        }
    }

    @DELETE
    @Path("{outageId}/notifd")
    public Response removeOutageFromNotifications(@PathParam("outageId") Long outageId) {
        writeLock();
        try {
            updateNotifd(ConfigAction.REMOVE, outageId);
            sendConfigChangedEvent();
            return Response.ok().build();
        } catch (Exception e) {
            throw getException(Status.BAD_REQUEST, "Can't remove scheduled outage " + outageId + " from notifications because: " + e.getMessage());
        } finally {
            writeUnlock();
        }
    }

    @GET
    @Path("{outageId}/nodeInOutage/{nodeId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String isNodeInOutage(@PathParam("outageId") Long outageId, @PathParam("nodeId") Integer nodeId) {
        readLock();
        try {
            Outage outage = getOutage(outageId);
            Boolean inOutage = m_configFactory.isNodeIdInOutage(nodeId, outage) && m_configFactory.isCurTimeInOutage(outage);
            return inOutage.toString();
        } finally {
            readUnlock();
        }
    }

    @GET
    @Path("nodeInOutage/{nodeId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String isNodeInOutage(@PathParam("nodeId") Integer nodeId) {
        readLock();
        try {
            for (Outage outage : m_configFactory.getOutages()) {
                if (m_configFactory.isNodeIdInOutage(nodeId, outage) && m_configFactory.isCurTimeInOutage(outage)) {
                    return Boolean.TRUE.toString();
                }
            }
            return Boolean.FALSE.toString();
        } finally {
            readUnlock();
        }
    }

    @GET
    @Path("{outageId}/interfaceInOutage/{ipAddr}")
    @Produces(MediaType.TEXT_PLAIN)
    public String isInterfaceInOutage(@PathParam("outageId") Long outageId, @PathParam("ipAddr") String ipAddr) {
        readLock();
        try {
            validateAddress(ipAddr);
            Outage outage = getOutage(outageId);
            Boolean inOutage = m_configFactory.isInterfaceInOutage(ipAddr, outage) && m_configFactory.isCurTimeInOutage(outage);
            return inOutage.toString();
        } finally {
            readUnlock();
        }
    }

    @GET
    @Path("interfaceInOutage/{ipAddr}")
    @Produces(MediaType.TEXT_PLAIN)
    public String isInterfaceInOutage(@PathParam("ipAddr") String ipAddr) {
        readLock();
        try {
            for (Outage outage : m_configFactory.getOutages()) {
                if (m_configFactory.isInterfaceInOutage(ipAddr, outage) && m_configFactory.isCurTimeInOutage(outage)) {
                    return Boolean.TRUE.toString();
                }
            }
            return Boolean.FALSE.toString();
        } finally {
            readUnlock();
        }
    }

    private void validateAddress(String ipAddress) {
        boolean valid = false;
        try {
            valid = InetAddressUtils.addr(ipAddress) != null;
        } catch (Exception e) {
            valid = false;
        }
        if (!valid) {
            throw getException(Status.BAD_REQUEST, "Malformed IP Address " + ipAddress);
        }
    }

    private void updateCollectd(ConfigAction action, Long outageId, String packageName) throws Exception {
        getOutage(outageId); // Validate if outageName exists.
        if (action.equals(ConfigAction.ADD)) {
            CollectdPackage pkg = getCollectdPackage(packageName);
            if (!pkg.getPackage().getOutageIdCollection().contains(outageId))
                pkg.getPackage().addOutageId(outageId);
        }
        if (action.equals(ConfigAction.REMOVE)) {
            CollectdPackage pkg = getCollectdPackage(packageName);
            pkg.getPackage().addOutageId(outageId);
        }
        if (action.equals(ConfigAction.REMOVE_FROM_ALL)) {
            for (CollectdPackage pkg : CollectdConfigFactory.getInstance().getCollectdConfig().getPackages()) {
                pkg.getPackage().addOutageId(outageId);
            }
        }
        CollectdConfigFactory.getInstance().saveCurrent();
    }

    private CollectdPackage getCollectdPackage(String packageName) throws IllegalArgumentException {
        CollectdPackage pkg = CollectdConfigFactory.getInstance().getPackage(packageName);
        if (pkg == null) throw new IllegalArgumentException("Collectd package " + packageName + " does not exist.");
        return pkg;
    }

    private void updatePollerd(ConfigAction action, Long outageId, String packageName) throws Exception {
        getOutage(outageId); // Validate if outageName exists.
        if (action.equals(ConfigAction.ADD)) {
            org.opennms.netmgt.config.poller.Package pkg = getPollerdPackage(packageName);
            if (!pkg.getOutageIdCollection().contains(outageId))
                pkg.addOutageId(outageId);
        }
        if (action.equals(ConfigAction.REMOVE)) {
            org.opennms.netmgt.config.poller.Package pkg = getPollerdPackage(packageName);
            pkg.removeOutageId(outageId);
        }
        if (action.equals(ConfigAction.REMOVE_FROM_ALL)) {
            for (org.opennms.netmgt.config.poller.Package pkg : PollerConfigFactory.getInstance().getConfiguration().getPackage()) {
                pkg.removeOutageId(outageId);
            }
        }
        PollerConfigFactory.getInstance().save();
    }

    private org.opennms.netmgt.config.poller.Package getPollerdPackage(String packageName) throws IllegalArgumentException {
        org.opennms.netmgt.config.poller.Package pkg = PollerConfigFactory.getInstance().getPackage(packageName);
        if (pkg == null) throw new IllegalArgumentException("Poller package " + packageName + " does not exist.");
        return pkg;
    }

    private void updateThreshd(ConfigAction action, Long outageId, String packageName) throws Exception {
        getOutage(outageId); // Validate if outageName exists.
        if (action.equals(ConfigAction.ADD)) {
            org.opennms.netmgt.config.threshd.Package pkg = getThreshdPackage(packageName);
            if (!pkg.getOutageIdCollection().contains(outageId))
                pkg.addOutageId(outageId);
        }
        if (action.equals(ConfigAction.REMOVE)) {
            org.opennms.netmgt.config.threshd.Package pkg = getThreshdPackage(packageName);
            pkg.addOutageId(outageId);
        }
        if (action.equals(ConfigAction.REMOVE_FROM_ALL)) {
            for (org.opennms.netmgt.config.threshd.Package pkg : ThreshdConfigFactory.getInstance().getConfiguration().getPackage()) {
                pkg.addOutageId(outageId);
            }
        }
        ThreshdConfigFactory.getInstance().saveCurrent();
    }

    private org.opennms.netmgt.config.threshd.Package getThreshdPackage(String packageName) throws IllegalArgumentException {
        org.opennms.netmgt.config.threshd.Package pkg = ThreshdConfigFactory.getInstance().getPackage(packageName);
        if (pkg == null) throw new IllegalArgumentException("Threshold package " + packageName + " does not exist.");
        return pkg;
    }

    private void updateNotifd(ConfigAction action, Long outageId) throws Exception {
        getOutage(outageId); // Validate if outageName exists.
        NotifdConfigFactory factory = NotifdConfigFactory.getInstance();
        if (action.equals(ConfigAction.ADD)) {
            factory.getConfiguration().addOutageId(outageId);
        }
        if (action.equals(ConfigAction.REMOVE) || action.equals(ConfigAction.REMOVE_FROM_ALL)) {
            factory.getConfiguration().removeOutageId(outageId);
        }
        factory.saveCurrent();
    }

    private void sendConfigChangedEvent() {
        EventBuilder builder = new EventBuilder(EventConstants.SCHEDOUTAGES_CHANGED_EVENT_UEI, "Web UI");
        try {
            m_eventProxy.send(builder.getEvent());
        } catch (Throwable e) {
            throw getException(Status.BAD_REQUEST, "Could not send event " + builder.getEvent().getUei() + " because, " + e.getMessage());
        }
    }

}
