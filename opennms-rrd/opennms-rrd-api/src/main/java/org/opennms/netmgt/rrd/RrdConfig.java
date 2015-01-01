/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2004-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.rrd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.opennms.core.utils.ConfigFileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides access to the rrd configuration data.
 */
public abstract class RrdConfig {
    private static final Logger LOG = LoggerFactory.getLogger(RrdConfig.class);

    private static Properties m_properties = null;

    /**
     * This loads the configuration file.
     *
     * @return a Properties object representing the configuration properties
     * @throws java.io.IOException if any.
     */
    public static Properties getProperties() throws IOException {
        if (m_properties == null) {
            m_properties = new Properties(System.getProperties());
            try {
                Path configFile = ConfigFileConstants.getFile(ConfigFileConstants.RRD_CONFIG_FILE_NAME);
                LOG.debug("RRD_CONFIG_FILE_NAME: {}", configFile);
                try (InputStream in = Files.newInputStream(configFile);) {
                    m_properties.load(in);
                }
            } catch (FileNotFoundException e) {
                // Only in unit tests should this happen.
                LOG.info("{} not found, loading RRD configuration solely from system properties", ConfigFileConstants.getFileName(ConfigFileConstants.RRD_CONFIG_FILE_NAME));
            }
        }
        return m_properties;
    }
}
