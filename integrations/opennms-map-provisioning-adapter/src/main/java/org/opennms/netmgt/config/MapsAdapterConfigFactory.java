/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2002-2014 The OpenNMS Group, Inc.
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
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.ConfigFileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>MapsAdapterConfigFactory class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class MapsAdapterConfigFactory extends MapsAdapterConfigManager {
    private static final Logger LOG = LoggerFactory.getLogger(MapsAdapterConfigFactory.class);

    /**
     * The singleton instance of this factory
     */
    private static MapsAdapterConfigFactory m_singleton = null;

    /**
     * This member is set to true if the configuration file has been loaded.
     */
    private static boolean m_loaded = false;

    /**
     * Loaded version
     */
    private FileTime m_currentVersion = FileTime.fromMillis(-1L);


    /**
     * constructor constructor
     *
     * @exception java.io.IOException
     *                Thrown if the specified config file cannot be read
     * @exception org.exolab.castor.xml.MarshalException
     *                Thrown if the file does not conform to the schema.
     * @exception org.exolab.castor.xml.ValidationException
     *                Thrown if the contents do not match the required schema.
     * @param currentVersion a long.
     * @param reader a {@link java.io.InputStream} object.
     * @param localServer a {@link java.lang.String} object.
     * @param verifyServer a boolean.
     * @throws org.exolab.castor.xml.MarshalException if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     * @throws java.io.IOException if any.
     */
    public MapsAdapterConfigFactory(FileTime currentVersion, InputStream reader, String localServer, boolean verifyServer) throws MarshalException, ValidationException, IOException {
        super(reader, localServer, verifyServer);
        m_currentVersion = currentVersion;
    }


    /**
     * Load the config from the default config file and create the singleton
     * instance of this factory.
     *
     * @exception java.io.IOException
     *                Thrown if the specified config file cannot be read
     * @exception org.exolab.castor.xml.MarshalException
     *                Thrown if the file does not conform to the schema.
     * @exception org.exolab.castor.xml.ValidationException
     *                Thrown if the contents do not match the required schema.
     * @throws java.io.IOException if any.
     * @throws org.exolab.castor.xml.MarshalException if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    public static synchronized void init() throws IOException, MarshalException, ValidationException {
        if (m_loaded) {
            // init already called - return
            // to reload, reload() will need to be called
            return;
        }
        OpennmsServerConfigFactory.init();
        OpennmsServerConfigFactory onmsSvrConfig = OpennmsServerConfigFactory.getInstance();

        Path cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.MAPS_ADAPTER_CONFIG_FILE_NAME);

        LOG.debug("init: config file path: {}", cfgFile);

        try (InputStream reader = Files.newInputStream(cfgFile);) {
            MapsAdapterConfigFactory config = new MapsAdapterConfigFactory(Files.getLastModifiedTime(cfgFile), reader, onmsSvrConfig.getServerName(), onmsSvrConfig.verifyServer());
            setInstance(config);
        } catch (IOException e) {
        }

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
    public static synchronized void reload() throws IOException, MarshalException, ValidationException {
        init();
        getInstance().update();
    }

    /**
     * Return the singleton instance of this factory.
     *
     * @return The current factory instance.
     * @throws java.lang.IllegalStateException
     *             Thrown if the factory has not yet been initialized.
     */
    public static synchronized MapsAdapterConfigFactory getInstance() {
        if (!m_loaded) {
            throw new IllegalStateException("The factory has not been initialized");
        }
        return m_singleton;
    }

    private static void setInstance(final MapsAdapterConfigFactory instance) {
        m_singleton = instance;
        m_loaded = true;
    }

    /**
     * <p>saveXml</p>
     *
     * @param xml a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    protected void saveXml(final String xml) throws IOException {
        if (xml != null) {
            getWriteLock().lock();
            final long timestamp = System.currentTimeMillis();
            final Path cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.MAPS_ADAPTER_CONFIG_FILE_NAME);
            try (final Writer fileWriter = Files.newBufferedWriter(cfgFile, Charset.forName("UTF-8"));) {
                LOG.debug("saveXml: saving config file at {}: {}", timestamp, cfgFile);
                fileWriter.write(xml);
                fileWriter.flush();
                LOG.debug("saveXml: finished saving config file: {}", cfgFile);
            } finally {
                getWriteLock().unlock();
            }
        }
    }

    /**
     * <p>update</p>
     *
     * @throws java.io.IOException if any.
     * @throws org.exolab.castor.xml.MarshalException if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    @Override
    public void update() throws IOException, MarshalException, ValidationException {
        getWriteLock().lock();
        try {
            final Path cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.MAPS_ADAPTER_CONFIG_FILE_NAME);
            if (!Files.getLastModifiedTime(cfgFile).equals(m_currentVersion)) {
                m_currentVersion = Files.getLastModifiedTime(cfgFile);
                LOG.debug("init: config file path: {}", cfgFile);
                reloadXML(Files.newInputStream(cfgFile));
                LOG.debug("init: finished loading config file: {}", cfgFile);
            }
        } finally {
            getWriteLock().unlock();
        }
    }

}
