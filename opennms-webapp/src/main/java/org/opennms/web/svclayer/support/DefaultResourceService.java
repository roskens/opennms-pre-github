/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.svclayer.support;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.dao.api.GraphDao;
import org.opennms.netmgt.dao.api.ResourceDao;
import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.PrefabGraph;
import org.opennms.netmgt.model.RrdGraphAttribute;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventProxy;
import org.opennms.netmgt.model.events.EventProxyException;
import org.opennms.web.api.Util;
import org.opennms.web.svclayer.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * <p>DefaultResourceService class.</p>
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 */
public class DefaultResourceService implements ResourceService, InitializingBean {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultResourceService.class);

    private ResourceDao m_resourceDao;
    private GraphDao m_graphDao;
    private EventProxy m_eventProxy;

    /**
     * <p>getResourceDao</p>
     *
     * @return a {@link org.opennms.netmgt.dao.api.ResourceDao} object.
     */
    public final ResourceDao getResourceDao() {
        return m_resourceDao;
    }

    /**
     * <p>setResourceDao</p>
     *
     * @param resourceDao a {@link org.opennms.netmgt.dao.api.ResourceDao} object.
     */
    public final void setResourceDao(final ResourceDao resourceDao) {
        m_resourceDao = resourceDao;
    }

    /**
     * <p>getGraphDao</p>
     *
     * @return a {@link org.opennms.netmgt.dao.api.GraphDao} object.
     */
    public final GraphDao getGraphDao() {
        return m_graphDao;
    }

    /**
     * <p>setGraphDao</p>
     *
     * @param graphDao a {@link org.opennms.netmgt.dao.api.GraphDao} object.
     */
    public final void setGraphDao(final GraphDao graphDao) {
        m_graphDao = graphDao;
    }

    /**
     * <p>setEventProxy</p>
     *
     * @param eventProxy a {@link org.opennms.netmgt.model.events.EventProxy} object.
     */
    public final void setEventProxy(final EventProxy eventProxy) {
        m_eventProxy = eventProxy;
    }

    /**
     * <p>afterPropertiesSet</p>
     *
     * @throws java.lang.Exception if any.
     */
    @Override
    public final void afterPropertiesSet() throws Exception {
        Assert.state(m_resourceDao != null, "resourceDao property is not set");
        Assert.state(m_graphDao != null, "graphDao property is not set");
        Assert.state(m_eventProxy != null, "eventProxy property is not set");
    }

    /**
     * <p>getRrdDirectory</p>
     *
     * @return a {@link java.io.File} object.
     */
    @Override
    public final File getRrdDirectory() {
        return m_resourceDao.getRrdDirectory();
    }

    /**
     * <p>findDomainResources</p>
     *
     * @return a {@link java.util.List} object.
     */
    @Override
    public final List<OnmsResource> findDomainResources() {
        return m_resourceDao.findDomainResources();
    }

    /**
     * <p>findNodeSourceResources</p>
     *
     * @return a {@link java.util.List} object.
     */
    @Override
    public final List<OnmsResource> findNodeSourceResources() {
        return m_resourceDao.findNodeSourceResources();
    }

    /**
     * <p>findNodeResources</p>
     *
     * @return a {@link java.util.List} object.
     */
    @Override
    public final List<OnmsResource> findNodeResources() {
        return m_resourceDao.findNodeResources();
    }

    /**
     * <p>findTopLevelResources</p>
     *
     * @return a {@link java.util.List} object.
     */
    @Override
    public final List<OnmsResource> findTopLevelResources() {
        return m_resourceDao.findTopLevelResources();
    }

    /** {@inheritDoc} */
    @Override
    public final List<OnmsResource> findNodeChildResources(final int nodeId) {
        List<OnmsResource> resources = new ArrayList<OnmsResource>();
        OnmsResource resource = m_resourceDao.getResourceById(OnmsResource.createResourceId("node", Integer.toString(nodeId)));
        if (resource != null) {
            resources = resource.getChildResources();
            resources.size(); // Get the size to force the list to be loaded
        }
        return resources;
    }

    /** {@inheritDoc} */
    @Override
    public final List<OnmsResource> findDomainChildResources(final String domain) {
        List<OnmsResource> resources = new ArrayList<OnmsResource>();
        OnmsResource resource = m_resourceDao.getResourceById(OnmsResource.createResourceId("domain", domain));
        if (resource != null) {
            resources = resource.getChildResources();
            resources.size(); // Get the size to force the list to be loaded
        }
        return resources;
    }

    /** {@inheritDoc} */
    @Override
    public final List<OnmsResource> findNodeSourceChildResources(final String nodeSource) {
        List<OnmsResource> resources = new ArrayList<OnmsResource>();
        OnmsResource resource = m_resourceDao.getResourceById(OnmsResource.createResourceId("nodeSource", nodeSource));
        if (resource != null) {
            resources = resource.getChildResources();
            resources.size(); // Get the size to force the list to be loaded
        }
        return resources;
    }

    /**
     * <p>findChildResources</p>
     *
     * @param resource a {@link org.opennms.netmgt.model.OnmsResource} object.
     * @param resourceTypeMatches a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    @Override
    public final List<OnmsResource> findChildResources(final OnmsResource resource, final String... resourceTypeMatches) {
        List<OnmsResource> matchingChildResources = new LinkedList<OnmsResource>();

        if (resource != null) {
            for (OnmsResource childResource : resource.getChildResources()) {
                boolean addGraph = false;
                if (resourceTypeMatches.length > 0) {
                    for (String resourceTypeMatch : resourceTypeMatches) {
                        if (resourceTypeMatch.equals(childResource.getResourceType().getName())) {
                            addGraph = true;
                            break;
                        }
                    }
                } else {
                    addGraph = true;
                }

                if (addGraph) {
                    matchingChildResources.add(checkLabelForQuotes(childResource));
                }
            }
        }

        return matchingChildResources;
    }

    private static OnmsResource checkLabelForQuotes(final OnmsResource childResource) {

        String lbl  = Util.convertToJsSafeString(childResource.getLabel());

        OnmsResource resource = new OnmsResource(childResource.getName(), lbl, childResource.getResourceType(), childResource.getAttributes());
        resource.setParent(childResource.getParent());
        resource.setEntity(childResource.getEntity());
        resource.setLink(childResource.getLink());
        return resource;
    }

    /** {@inheritDoc} */
    @Override
    public final OnmsResource getResourceById(final String id) {
        return m_resourceDao.getResourceById(id);
    }

    /** {@inheritDoc} */
    @Override
    public final List<OnmsResource> getResourceListById(final String resourceId) {
        return m_resourceDao.getResourceListById(resourceId);
    }

    /** {@inheritDoc} */
    @Override
    public final PrefabGraph[] findPrefabGraphsForResource(final OnmsResource resource) {
        return m_graphDao.getPrefabGraphsForResource(resource);
    }

    /** {@inheritDoc} */
    @Override
    public final void promoteGraphAttributesForResource(final OnmsResource resource) {
        String baseDir = getRrdDirectory().getAbsolutePath();
        List<String> rrdFiles = new LinkedList<String>();
        for(RrdGraphAttribute attribute : resource.getRrdGraphAttributes().values()) {
            rrdFiles.add(baseDir + File.separator + attribute.getRrdRelativePath());
        }
        EventBuilder bldr = new EventBuilder(EventConstants.PROMOTE_QUEUE_DATA_UEI, "OpenNMS.Webapp");
        bldr.addParam(EventConstants.PARM_FILES_TO_PROMOTE, rrdFiles);

        try {
            m_eventProxy.send(bldr.getEvent());
        } catch (EventProxyException e) {
            LOG.warn("Unable to send file promotion event to opennms: {}", e, e);
        }
    }

    /**
     * <p>promoteGraphAttributesForResource</p>
     *
     * @param resourceId a {@link java.lang.String} object.
     */
    @Override
    public final void promoteGraphAttributesForResource(final String resourceId) {
        promoteGraphAttributesForResource(getResourceById(resourceId));
    }



    /**
     * <p>findPrefabGraphsForChildResources</p>
     *
     * @param resource a {@link org.opennms.netmgt.model.OnmsResource} object.
     * @param resourceTypeMatches a {@link java.lang.String} object.
     * @return an array of {@link org.opennms.netmgt.model.PrefabGraph} objects.
     */
    @Override
    public final PrefabGraph[] findPrefabGraphsForChildResources(final OnmsResource resource, final String... resourceTypeMatches) {
        Map<String, PrefabGraph> childGraphs = new LinkedHashMap<String, PrefabGraph>();
        for (OnmsResource r : findChildResources(resource, resourceTypeMatches)) {
            for (PrefabGraph g : findPrefabGraphsForResource(r)) {
                childGraphs.put(g.getName(), g);
            }
        }
        return childGraphs.values().toArray(new PrefabGraph[childGraphs.size()]);
    }

    /** {@inheritDoc} */
    @Override
    public final PrefabGraph getPrefabGraph(final String name) {
        return m_graphDao.getPrefabGraph(name);
    }

}
