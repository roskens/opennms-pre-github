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

package org.opennms.netmgt.config.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opennms.core.utils.ConfigFileConstants;
import org.opennms.core.xml.JaxbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * A factory for creating JdbcDataCollectionConfig objects.
 */
public class JdbcDataCollectionConfigFactory {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(JdbcDataCollectionConfigFactory.class);

    /** The m_jdbc data collection config. */
    private JdbcDataCollectionConfig m_jdbcDataCollectionConfig = null;

    /**
     * Instantiates a new jdbc data collection config factory.
     */
    public JdbcDataCollectionConfigFactory() {
        try {
            File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.JDBC_COLLECTION_CONFIG_FILE_NAME);
            LOG.debug("init: config file path: {}", cfgFile.getPath());
            InputStream reader = new FileInputStream(cfgFile);
            unmarshall(reader);
            reader.close();
        } catch (IOException e) {
            // TODO rethrow.
        }
    }

    /**
     * Unmarshall.
     *
     * @param configFile
     *            the config file
     * @return the jdbc data collection config
     */
    public JdbcDataCollectionConfig unmarshall(InputStream configFile) {
        try {
            m_jdbcDataCollectionConfig = JaxbUtils.unmarshal(JdbcDataCollectionConfig.class,
                                                             new InputSource(configFile));
            return m_jdbcDataCollectionConfig;
        } catch (Throwable e) {
            // TODO!!
            // throw new
            // ForeignSourceRepositoryException("unable to access default foreign source resource",
            // e);
        }
        return m_jdbcDataCollectionConfig;
    }

}
