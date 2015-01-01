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

package org.opennms.netmgt.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import org.apache.commons.io.IOUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.ConfigFileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>DefaultCapsdConfigManager class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class DefaultCapsdConfigManager extends CapsdConfigManager {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCapsdConfigManager.class);
    /**
     * Timestamp of the file for the currently loaded configuration
     */
    private FileTime m_currentVersion = null;

    /**
     * <p>Constructor for DefaultCapsdConfigManager.</p>
     */
    public DefaultCapsdConfigManager() {
        super();
    }

    /**
     * <p>Constructor for DefaultCapsdConfigManager.</p>
     *
     * @param is a {@link java.io.InputStream} object.
     * @throws org.exolab.castor.xml.MarshalException if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    public DefaultCapsdConfigManager(InputStream is) throws MarshalException, ValidationException {
        super(is);
    }

    /**
     * <p>update</p>
     *
     * @throws java.io.IOException if any.
     * @throws java.io.FileNotFoundException if any.
     * @throws org.exolab.castor.xml.MarshalException if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    @Override
    protected synchronized void update() throws IOException, MarshalException, ValidationException {
        Path configFile = ConfigFileConstants.getFile(ConfigFileConstants.CAPSD_CONFIG_FILE_NAME);

        LOG.debug("Checking to see if capsd configuration should be reloaded from {}", configFile);

        if (m_currentVersion == null || m_currentVersion.compareTo(Files.getLastModifiedTime(configFile)) < 0) {
            LOG.debug("Reloading capsd configuration file");

            try (InputStream is = Files.newInputStream(configFile);) {
                loadXml(is);
                // Update currentVersion after we have successfully reloaded
                m_currentVersion = Files.getLastModifiedTime(configFile);
            }

            LOG.info("Reloaded capsd configuration file");
        }
    }

    /** {@inheritDoc} */
    @Override
    protected synchronized void saveXml(String xml) throws IOException {
        if (xml != null) {
            try (Writer fileWriter = new OutputStreamWriter(Files.newOutputStream(ConfigFileConstants.getFile(ConfigFileConstants.CAPSD_CONFIG_FILE_NAME)), "UTF-8");) {
                fileWriter.write(xml);
                fileWriter.flush();
            }
        }
    }
}
