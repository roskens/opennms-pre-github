/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2012 The OpenNMS Group, Inc.
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

import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.spi.resource.PerRequest;
import org.opennms.netmgt.dao.api.GraphDao;
import org.opennms.netmgt.dao.api.ResourceDao;
import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.PrefabGraph;
import org.opennms.netmgt.model.PrefabGraphCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Component
@PerRequest
@Scope("prototype")
@Path("prefabgraphs")
@Transactional
public class PrefabGraphsRestService extends OnmsRestService {

    @Autowired
    private ResourceDao m_resourceDao;

    @Autowired
    private GraphDao m_graphDao;

    @Context
    UriInfo m_uriInfo;

    @Context
    ResourceContext m_context;

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public PrefabGraphCollection getPrefabGraphs() {
        readLock();

        try {
            List<PrefabGraph> prefabGraphs = m_graphDao.getAllPrefabGraphs();

            if (prefabGraphs == null) {
                throw getException(Response.Status.BAD_REQUEST, "getPrefabGraphs: Can't find prefabgraphs entries");
            }

            return new PrefabGraphCollection(prefabGraphs);
        } finally {
            readUnlock();
        }
    }

    @GET
    @Path("/{resourceId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public PrefabGraphCollection getPrefabGraphsForResourceId(@PathParam("resourceId") String resourceId) {
        readLock();

        try {
            OnmsResource resource = m_resourceDao.getResourceById(resourceId);

            if (resource == null) {
                throw getException(Response.Status.BAD_REQUEST, "getPrefabGraphsForResourceId: Can't find resourceId " + resourceId);
            }

            PrefabGraph[] queries = m_graphDao.getPrefabGraphsForResource(resource);

            return new PrefabGraphCollection(queries);
        } finally {
            readUnlock();
        }
    }
}
