/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2011 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.opennms.netmgt.dao.ResourceDao;
import org.opennms.netmgt.dao.support.RrdFileConstants;
import org.opennms.netmgt.model.OnmsAttribute;
import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.OnmsResourceType;
import org.opennms.netmgt.model.RrdGraphAttribute;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.dao.DataAccessException;

import org.opennms.netmgt.config.attrmap.Attrmap;
import org.opennms.netmgt.config.attrmap.Datasource;
import org.apache.commons.io.IOUtils;
import org.opennms.core.utils.LogUtils;
import org.opennms.core.xml.CastorUtils;

public class NodeSnmpResourceType implements OnmsResourceType {

    private ResourceDao m_resourceDao;
    public static String ATTRMAP_XML_FILE = "attrmap.xml";

    /**
     * <p>Constructor for NodeSnmpResourceType.</p>
     *
     * @param resourceDao a {@link org.opennms.netmgt.dao.ResourceDao} object.
     */
    public NodeSnmpResourceType(ResourceDao resourceDao) {
        m_resourceDao = resourceDao;
    }

    /**
     * <p>getName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return "nodeSnmp";
    }
    
    /**
     * <p>getLabel</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLabel() {
        return "SNMP Node Data";
    }
    
    /** {@inheritDoc} */
    public boolean isResourceTypeOnNode(int nodeId) {
        return getResourceDirectory(nodeId, false).isDirectory();
    }
    
    /**
     * <p>getResourceDirectory</p>
     *
     * @param nodeId a int.
     * @param verify a boolean.
     * @return a {@link java.io.File} object.
     */
    public File getResourceDirectory(int nodeId, boolean verify) {
        File snmp = new File(m_resourceDao.getRrdDirectory(verify), DefaultResourceDao.SNMP_DIRECTORY);
        
        File node = new File(snmp, Integer.toString(nodeId));
        if (verify && !node.isDirectory()) {
            throw new ObjectRetrievalFailureException(File.class, "No node directory exists for node " + nodeId + ": " + node);
        }
        
        return node;
    }
    
    /** {@inheritDoc} */
    public List<OnmsResource> getResourcesForNode(int nodeId) {
        ArrayList<OnmsResource> resources = new ArrayList<OnmsResource>();

        Set<OnmsAttribute> attributes = ResourceTypeUtils.getAttributesAtRelativePath(m_resourceDao.getRrdDirectory(), getRelativePathForResource(nodeId));
        loadMappedAttributes(m_resourceDao.getRrdDirectory(), getRelativePathForResource(nodeId), attributes);

        OnmsResource resource = new OnmsResource("", "Node-level Performance Data", this, attributes);
        resources.add(resource);
        return resources;
    }
    
    private String getRelativePathForResource(int nodeId) {
        return DefaultResourceDao.SNMP_DIRECTORY + File.separator + Integer.toString(nodeId);
    }

    /**
     * {@inheritDoc}
     *
     * This resource type is never available for domains.
     * Only the interface resource type is available for domains.
     */
    public boolean isResourceTypeOnDomain(String domain) {
        return false;
    }

    /** {@inheritDoc} */
    public List<OnmsResource> getResourcesForDomain(String domain) {
        List<OnmsResource> empty = Collections.emptyList();
        return empty;
    }

    /** {@inheritDoc} */
    public String getLinkForResource(OnmsResource resource) {
        return null;
    }
    
    /** {@inheritDoc} */
    public boolean isResourceTypeOnNodeSource(String nodeSource, int nodeId) {
        File nodeSnmpDir = new File(m_resourceDao.getRrdDirectory(), DefaultResourceDao.SNMP_DIRECTORY + File.separator
                       + ResourceTypeUtils.getRelativeNodeSourceDirectory(nodeSource).toString());
        if (!nodeSnmpDir.isDirectory()) {
            throw new ObjectRetrievalFailureException(File.class, "No directory exists for nodeSource " + nodeSource);
        }
        return nodeSnmpDir.listFiles(RrdFileConstants.RRD_FILENAME_FILTER).length > 0; 
    }
    
    /** {@inheritDoc} */
    public List<OnmsResource> getResourcesForNodeSource(String nodeSource, int nodeId) {
        ArrayList<OnmsResource> resources = new ArrayList<OnmsResource>();
        File relPath = new File(DefaultResourceDao.SNMP_DIRECTORY, ResourceTypeUtils.getRelativeNodeSourceDirectory(nodeSource).toString());

        Set<OnmsAttribute> attributes = ResourceTypeUtils.getAttributesAtRelativePath(m_resourceDao.getRrdDirectory(), relPath.toString());

        OnmsResource resource = new OnmsResource("", "Node-level Performance Data", this, attributes);
        resources.add(resource);
        return resources;
    }

    private void loadMappedAttributes(File rrdDirectory, String relativePath, Set<OnmsAttribute> attributes) {
        File resourceDir = new File(rrdDirectory, relativePath);
        File attrMapFile = new File(resourceDir, ATTRMAP_XML_FILE);
        InputStream is = null;

        if (! attrMapFile.exists()) { return; }

        try {
            is = new FileInputStream(attrMapFile);
            Attrmap am = CastorUtils.unmarshalWithTranslatedExceptions(Attrmap.class, is);

            for (Datasource ds : am.getDatasourceCollection()) {
                String dsName = ds.getDsName();
                String relPath = ds.getDsType() + File.separator + ds.getRealNode();

                if (ResourceTypeUtils.isStoreByGroup()) {
                    Properties props = ResourceTypeUtils.getDsProperties(new File(rrdDirectory, relPath));
                    if (props.containsKey(ds.getRealDatasource())) {
                        attributes.add(new RrdGraphAttribute(dsName, relPath, props.getProperty(ds.getRealDatasource()) + RrdFileConstants.getRrdSuffix()));
                    }
                } else {
                    attributes.add(new RrdGraphAttribute(dsName, relPath, ds.getRealDatasource() + RrdFileConstants.getRrdSuffix()));
                }
            }
        } catch (FileNotFoundException e) {
            // Required to catch this, but should never execute this block.
        } catch (DataAccessException e) {
            // Failed parsing input file
            LogUtils.warnf(this, "Failed to parse %s: %s", attrMapFile.getAbsolutePath(), e.getMessage());

        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
        }
    }

}
