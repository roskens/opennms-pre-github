/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.rest;

import java.util.Collections;
import java.util.Comparator;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.opennms.netmgt.config.GroupManager;
import org.opennms.netmgt.model.OnmsGroup;
import org.opennms.netmgt.model.OnmsGroupList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.spi.resource.PerRequest;

/**
 * Basic Web Service using REST for OnmsGroup entity.
 *
 * @author <a href="mailto:ranger@opennms.org">Benjamin Reed</a>
 * @since 1.9.93
 */
@Component
@PerRequest
@Scope("prototype")
@Path("groups")
@Transactional
public class GroupRestService extends OnmsRestService {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(GroupRestService.class);

    /** The m_group manager. */
    @Autowired
    private GroupManager m_groupManager;

    /** The m_uri info. */
    @Context
    UriInfo m_uriInfo;

    /** The m_context. */
    @Context
    ResourceContext m_context;

    /**
     * Gets the groups.
     *
     * @return the groups
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML })
    public OnmsGroupList getGroups() {
        readLock();

        try {
            final OnmsGroupList list;
            list = m_groupManager.getOnmsGroupList();
            Collections.sort(list, new Comparator<OnmsGroup>() {
                @Override
                public int compare(final OnmsGroup a, final OnmsGroup b) {
                    return a.getName().compareTo(b.getName());
                }
            });
            return list;
        } catch (final Throwable t) {
            throw getException(Status.BAD_REQUEST, t);
        } finally {
            readUnlock();
        }
    }

    /**
     * Gets the group.
     *
     * @param groupName
     *            the group name
     * @return the group
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML })
    @Path("{groupName}")
    public OnmsGroup getGroup(@PathParam("groupName")
    final String groupName) {
        readLock();

        try {
            final OnmsGroup group = m_groupManager.getOnmsGroup(groupName);
            if (group != null) {
                return group;
            }
            throw getException(Status.NOT_FOUND, groupName + " does not exist");
        } catch (final Throwable t) {
            if (t instanceof WebApplicationException) {
                throw (WebApplicationException) t;
            }
            throw getException(Status.BAD_REQUEST, t);
        } finally {
            readUnlock();
        }
    }

    /**
     * Adds the group.
     *
     * @param group
     *            the group
     * @return the response
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response addGroup(final OnmsGroup group) {
        writeLock();

        try {
            LOG.debug("addGroup: Adding group {}", group);
            m_groupManager.save(group);
            return Response.seeOther(getRedirectUri(m_uriInfo, group.getName())).build();
        } catch (final Throwable t) {
            throw getException(Status.BAD_REQUEST, t);
        } finally {
            writeUnlock();
        }
    }

    /**
     * Update group.
     *
     * @param groupName
     *            the group name
     * @param params
     *            the params
     * @return the response
     */
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("{groupName}")
    public Response updateGroup(@PathParam("groupName")
    final String groupName, final MultivaluedMapImpl params) {
        writeLock();

        try {
            OnmsGroup group = null;
            try {
                group = m_groupManager.getOnmsGroup(groupName);
            } catch (final Throwable t) {
                throw getException(Status.BAD_REQUEST, t);
            }
            if (group == null) {
                throw getException(Status.BAD_REQUEST, "updateGroup: Group does not exist: " + groupName);
            }
            LOG.debug("updateGroup: updating group {}", group);
            final BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(group);
            for (final String key : params.keySet()) {
                if (wrapper.isWritableProperty(key)) {
                    final String stringValue = params.getFirst(key);
                    @SuppressWarnings("unchecked")
                    final Object value = wrapper.convertIfNecessary(stringValue, wrapper.getPropertyType(key));
                    wrapper.setPropertyValue(key, value);
                }
            }
            LOG.debug("updateGroup: group {} updated", group);
            try {
                m_groupManager.save(group);
            } catch (final Throwable t) {
                throw getException(Status.INTERNAL_SERVER_ERROR, t);
            }
            return Response.seeOther(getRedirectUri(m_uriInfo)).build();
        } finally {
            writeUnlock();
        }
    }

    /**
     * Delete group.
     *
     * @param groupName
     *            the group name
     * @return the response
     */
    @DELETE
    @Path("{groupName}")
    public Response deleteGroup(@PathParam("groupName")
    final String groupName) {
        writeLock();
        try {
            final OnmsGroup group = getOnmsGroup(groupName);
            LOG.debug("deleteGroup: deleting group {}", group);
            m_groupManager.deleteGroup(groupName);
            return Response.ok().build();
        } catch (final Throwable t) {
            throw getException(Status.BAD_REQUEST, t);
        } finally {
            writeUnlock();
        }
    }

    /**
     * Adds the user.
     *
     * @param groupName
     *            the group name
     * @param userName
     *            the user name
     * @return the response
     */
    @PUT
    @Path("{groupName}/users/{userName}")
    public Response addUser(@PathParam("groupName")
    final String groupName, @PathParam("userName")
    final String userName) {
        writeLock();
        try {
            final OnmsGroup group = getOnmsGroup(groupName);
            group.addUser(userName);
            m_groupManager.save(group);
            return Response.seeOther(getRedirectUri(m_uriInfo)).build();
        } catch (final Throwable t) {
            throw getException(Status.INTERNAL_SERVER_ERROR, t);
        } finally {
            writeUnlock();
        }
    }

    /**
     * Removes the user.
     *
     * @param groupName
     *            the group name
     * @param userName
     *            the user name
     * @return the response
     */
    @DELETE
    @Path("{groupName}/users/{userName}")
    public Response removeUser(@PathParam("groupName")
    final String groupName, @PathParam("userName")
    final String userName) {
        writeLock();
        try {
            final OnmsGroup group = getOnmsGroup(groupName);
            if (group.getUsers().contains(userName)) {
                group.removeUser(userName);
                m_groupManager.save(group);
                return Response.ok().build();
            } else {
                throw getException(Status.BAD_REQUEST, "User is not in the group '" + groupName + "': " + userName);
            }
        } catch (final Throwable t) {
            if (t instanceof WebApplicationException) {
                throw (WebApplicationException) t;
            }
            throw getException(Status.INTERNAL_SERVER_ERROR, t);
        } finally {
            writeUnlock();
        }
    }

    /**
     * Gets the onms group.
     *
     * @param groupName
     *            the group name
     * @return the onms group
     */
    protected OnmsGroup getOnmsGroup(final String groupName) {
        OnmsGroup group = null;
        try {
            group = m_groupManager.getOnmsGroup(groupName);
        } catch (final Throwable t) {
            throw getException(Status.BAD_REQUEST, t);
        }
        if (group == null) {
            throw getException(Status.BAD_REQUEST, "Group does not exist: " + groupName);
        }
        return group;
    }

}
