/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.dao.support;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.opennms.netmgt.dao.api.ResourceDao;
import org.opennms.netmgt.model.OnmsAttribute;
import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.OnmsResourceType;
import org.opennms.netmgt.model.ResourceTypeUtils;
import org.opennms.netmgt.rrd.RrdFileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectRetrievalFailureException;

public class NodeSnmpResourceType implements OnmsResourceType {

    private static final Logger LOG = LoggerFactory.getLogger(NodeSnmpResourceType.class);
    private ResourceDao m_resourceDao;

    /**
     * <p>Constructor for NodeSnmpResourceType.</p>
     *
     * @param resourceDao a {@link org.opennms.netmgt.dao.api.ResourceDao} object.
     */
    public NodeSnmpResourceType(ResourceDao resourceDao) {
        m_resourceDao = resourceDao;
    }

    /**
     * <p>getName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getName() {
        return "nodeSnmp";
    }

    /**
     * <p>getLabel</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getLabel() {
        return "SNMP Node Data";
    }

    /** {@inheritDoc} */
    @Override
    public boolean isResourceTypeOnNode(int nodeId) {
        return Files.isDirectory(getResourceDirectory(nodeId, false));
    }

    /**
     * <p>getResourceDirectory</p>
     *
     * @param nodeId a int.
     * @param verify a boolean.
     * @return a {@link java.io.File} object.
     */
    public Path getResourceDirectory(int nodeId, boolean verify) {
        Path snmp = m_resourceDao.getRrdDirectory(verify).resolve(ResourceTypeUtils.SNMP_DIRECTORY);

        Path node = snmp.resolve(Integer.toString(nodeId));
        if (verify && !Files.isDirectory(node)) {
            throw new ObjectRetrievalFailureException(File.class, "No node directory exists for node " + nodeId + ": " + node);
        }

        return node;
    }

    /** {@inheritDoc} */
    @Override
    public List<OnmsResource> getResourcesForNode(int nodeId) {
        ArrayList<OnmsResource> resources = new ArrayList<OnmsResource>();

        Set<OnmsAttribute> attributes = ResourceTypeUtils.getAttributesAtRelativePath(m_resourceDao.getRrdDirectory(), getRelativePathForResource(nodeId));

        OnmsResource resource = new OnmsResource("", "Node-level Performance Data", this, attributes);
        resources.add(resource);
        return resources;
    }

    private Path getRelativePathForResource(int nodeId) {
        return Paths.get(ResourceTypeUtils.SNMP_DIRECTORY, Integer.toString(nodeId));
    }

    /**
     * {@inheritDoc}
     *
     * This resource type is never available for domains.
     * Only the interface resource type is available for domains.
     */
    @Override
    public boolean isResourceTypeOnDomain(String domain) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public List<OnmsResource> getResourcesForDomain(String domain) {
        return Collections.emptyList();
    }

    /** {@inheritDoc} */
    @Override
    public String getLinkForResource(OnmsResource resource) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isResourceTypeOnNodeSource(String nodeSource, int nodeId) {
        Path nodeSnmpDir = m_resourceDao.getRrdDirectory().resolve(ResourceTypeUtils.SNMP_DIRECTORY).resolve(ResourceTypeUtils.getRelativeNodeSourceDirectory(nodeSource));
        if (!Files.isDirectory(nodeSnmpDir)) { // A node without performance metrics should not have a directory
            return false;
        }
        List<Path> intfDirs = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(nodeSnmpDir, RrdFileConstants.RRD_FILENAME_FILTER);) {
            for (Path path : stream) {
                intfDirs.add(path);
            }
        } catch (IOException e) {
            LOG.error("ioexception", e);
        }
        return intfDirs.size() > 0;
    }

    /** {@inheritDoc} */
    @Override
    public List<OnmsResource> getResourcesForNodeSource(String nodeSource, int nodeId) {
        ArrayList<OnmsResource> resources = new ArrayList<OnmsResource>();
        Path relPath = Paths.get(ResourceTypeUtils.SNMP_DIRECTORY).resolve(ResourceTypeUtils.getRelativeNodeSourceDirectory(nodeSource));

        Set<OnmsAttribute> attributes = ResourceTypeUtils.getAttributesAtRelativePath(m_resourceDao.getRrdDirectory(), relPath);

        OnmsResource resource = new OnmsResource("", "Node-level Performance Data", this, attributes);
        resources.add(resource);
        return resources;
    }

}
