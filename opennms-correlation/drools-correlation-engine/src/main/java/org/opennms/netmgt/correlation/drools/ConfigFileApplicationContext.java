/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.correlation.drools;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.Resource;

/**
 * The Class ConfigFileApplicationContext.
 */
public class ConfigFileApplicationContext extends AbstractXmlApplicationContext {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(ConfigFileApplicationContext.class);

    /** The m_resource. */
    private Resource m_resource;

    /** The m_config file location. */
    private String m_configFileLocation;

    /**
     * Instantiates a new config file application context.
     *
     * @param basePath
     *            the base path
     * @param configFileLocation
     *            the config file location
     * @param parent
     *            the parent
     */
    public ConfigFileApplicationContext(Resource basePath, final String configFileLocation,
            final ApplicationContext parent) {
        super(parent);
        m_resource = basePath;
        m_configFileLocation = configFileLocation;
        refresh();
    }

    /* (non-Javadoc)
     * @see org.springframework.context.support.AbstractRefreshableConfigApplicationContext#getConfigLocations()
     */
    @Override
    protected String[] getConfigLocations() {
        if (m_configFileLocation == null) {
            return null;
        }
        return new String[] { m_configFileLocation };
    }

    /* (non-Javadoc)
     * @see org.springframework.core.io.DefaultResourceLoader#getResourceByPath(java.lang.String)
     */
    @Override
    protected Resource getResourceByPath(final String path) {
        try {
            return m_resource.createRelative(path);
        } catch (IOException e) {
            LOG.error("Unable to create resource for path {} relative the directory of {}", path, m_resource, e);
            throw new IllegalArgumentException("Failed to create relative path for " + path);
        }
    }

}
