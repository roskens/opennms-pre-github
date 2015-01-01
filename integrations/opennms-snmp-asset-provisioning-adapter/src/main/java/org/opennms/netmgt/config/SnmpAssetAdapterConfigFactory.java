/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2014 The OpenNMS Group, Inc.
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.ConfigFileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>SnmpAssetAdapterConfigFactory</p>
 */
public class SnmpAssetAdapterConfigFactory {
    private static final Logger LOG = LoggerFactory.getLogger(SnmpAssetAdapterConfigFactory.class);

	/**
	 * Singleton instance of configuration that this factory provides.
	 */
	private final SnmpAssetAdapterConfigManager m_config;

	public SnmpAssetAdapterConfigFactory() throws MarshalException, ValidationException, IOException {
        final Path cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.SNMP_ASSET_ADAPTER_CONFIG_FILE_NAME);
		LOG.debug("init: config file path: {}", cfgFile);
        final InputStream reader = Files.newInputStream(cfgFile);
        m_config = new SnmpAssetAdapterConfigManager(Files.getLastModifiedTime(cfgFile), reader);
		IOUtils.closeQuietly(reader);
	}

	/**
	 * Reload the config from the default config file
	 *
	 * @exception java.io.IOException
	 *                Thrown if the specified config file cannot be read/loaded
	 * @exception org.exolab.castor.xml.MarshalException
	 *                Thrown if the file does not conform to the schema.
	 * @exception org.exolab.castor.xml.ValidationException
	 *                Thrown if the contents do not match the required schema.
	 * @throws java.io.IOException if any.
	 * @throws org.exolab.castor.xml.MarshalException if any.
	 * @throws org.exolab.castor.xml.ValidationException if any.
	 */
	public void reload() throws IOException, MarshalException, ValidationException {
		m_config.update();
	}

	/**
	 * <p>saveXml</p>
	 *
	 * @param xml a {@link java.lang.String} object.
	 * @throws java.io.IOException if any.
	 */
	protected void save(final String xml) throws IOException {
        if (xml != null) {
            m_config.getWriteLock().lock();
            final long timestamp = System.currentTimeMillis();
            final Path cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.SNMP_ASSET_ADAPTER_CONFIG_FILE_NAME);
            LOG.debug("saveXml: saving config file at {}: {}", timestamp, cfgFile);
            try (BufferedWriter fileWriter = Files.newBufferedWriter(cfgFile, Charset.forName("UTF-8"));) {
    			fileWriter.write(xml);
    			fileWriter.flush();
    			LOG.debug("saveXml: finished saving config file: {}", cfgFile);
            } finally {
                m_config.getWriteLock().unlock();
            }
        }
    }

	/**
	 * Return the singleton instance of this factory.
	 *
	 * @return The current factory instance.
	 * @throws IOException
	 * @throws ValidationException
	 * @throws MarshalException
	 * @throws java.lang.IllegalStateException
	 *             Thrown if the factory has not yet been initialized.
	 */
	public SnmpAssetAdapterConfig getInstance() {
	    m_config.getReadLock().lock();
	    try {
	        return m_config;
	    } finally {
	        m_config.getReadLock().unlock();
	    }
	}
}
