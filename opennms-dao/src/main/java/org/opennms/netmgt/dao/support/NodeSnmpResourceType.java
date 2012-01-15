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
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.dao.ResourceDao;
import org.opennms.netmgt.model.OnmsAttribute;
import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.OnmsResourceType;
import org.opennms.netmgt.model.RrdGraphAttribute;
import org.springframework.orm.ObjectRetrievalFailureException;

import org.opennms.netmgt.config.attrmap.Attrmap;
import org.opennms.netmgt.config.attrmap.Filelink;
import org.opennms.netmgt.config.attrmap.Datasource;
import org.apache.commons.io.IOUtils;
import org.opennms.core.xml.CastorUtils;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

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
	for(OnmsAttribute attr : attributes) {
	    OnmsResource res = attr.getResource();
            if (res == null) {
                LogUtils.debugf(this, "%d: attr[%s]", nodeId, attr.getName());
            } else {
                LogUtils.debugf(this, "%d: attr[%s]: resource: %s", nodeId, attr.getName(), res.toString());
            }

            try {
                RrdGraphAttribute rga = (RrdGraphAttribute) attr;
                LogUtils.debugf(this, "%d: rga[%s]: %s", nodeId, rga.toString(), rga.getRrdRelativePath());
            } catch (Exception e) {
                LogUtils.debugf(this, "%d: attr[%s] through exception", nodeId, attr.getName());
            }
        }
        
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

    private void loadMappedAttributes(File rrdDirectory, String relativePath, Set<OnmsAttribute> attributes) {
        int suffixLength = RrdFileConstants.getRrdSuffix().length();
        File resourceDir = new File(rrdDirectory, relativePath);
        File attrMapFile = new File(resourceDir, ATTRMAP_XML_FILE);

        if (! attrMapFile.exists()) { return; }
        try {
            LogUtils.debugf(this, "attrMapFile.exists(): true [%s]", attrMapFile.getCanonicalPath());
        } catch (IOException e) {
        }

        try {
            final InputStream is = new FileInputStream(attrMapFile);
            Attrmap am = CastorUtils.unmarshal(Attrmap.class, is);
            IOUtils.closeQuietly(is);

            for (Filelink fl : am.getFilelinkCollection()) {
            try {
		LogUtils.debugf(this, "link: %s -> %s", fl.getName(), fl.getRealpath());

                if (fl.getDatasourceCount() == 0) {
		    LogUtils.debugf(this, "link[%s] has no datasources", fl.getName());
                    /*
		     * Add all datasources for the real file.
                     */
                    if (ResourceTypeUtils.isStoreByGroup()) {
		        LogUtils.debugf(this, "ResourceTypeUtils.isStoreByGroup(): true");
                        File realParentDir = new File(rrdDirectory, fl.getRealpath());

                        String groupName = fl.getName().substring(0, fl.getName().length() - suffixLength);
	                LogUtils.debugf(this, "groupName: %s", groupName);

                        Properties props = ResourceTypeUtils.getDsProperties(realParentDir);
                        for (Object o : props.keySet()) {
                            String dsName = (String)o;
                            if (props.getProperty(dsName).equals(groupName)) {
                                LogUtils.debugf(this, "RrdGraphAttribute('%s', '%s', '%s')", dsName, fl.getRealpath(), fl.getName());
                                attributes.add(new RrdGraphAttribute(dsName, fl.getRealpath(), fl.getName()));
                            }
                        }
                    } else {
		        LogUtils.debugf(this, "ResourceTypeUtils.isStoreByGroup(): false");
                        File realFile = new File(fl.getRealpath(), fl.getName());
                        String dsName = fl.getName();
			if (dsName.endsWith(RrdFileConstants.getRrdSuffix())) {
                            dsName = dsName.substring(0, dsName.length() - suffixLength);
			}

		        LogUtils.debugf(this, "RrdGraphAttribute('%s', '%s', '%s')", dsName, fl.getRealpath(), fl.getName());
                        attributes.add(new RrdGraphAttribute(dsName, fl.getRealpath(), fl.getName()));
                    }
                } else {
		    LogUtils.debugf(this, "link[%s] has %d datasources", fl.getName(), fl.getDatasourceCount());
		    /*
		     * Our file link has a list of datasource mappings
		     */
/*
                    for (Datasource ds : fl.getDatasourceCollection()) {
			// ds.getName()
			// ds.getOrigName()
                        String dsName = fileName.substring(0, fileName.length() - suffixLength);
                        attributes.add(new RrdGraphAttribute(dsName, relativePath, fileName));
                    }

                    if (ResourceTypeUtils.isStoreByGroup()) {
                    } else {
                    }
*/
                }
            } catch (Exception e) {
                LogUtils.debugf(this, "e: %s", e.getMessage());
            }
            }
        } catch (Exception e) {
            LogUtils.debugf(this, "e: %s", e.getMessage());
        }
    }
}
