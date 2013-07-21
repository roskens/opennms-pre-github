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

package org.opennms.netmgt.correlation.drools;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedList;
import java.util.List;

import org.opennms.core.xml.JaxbUtils;
import org.opennms.netmgt.correlation.CorrelationEngine;
import org.opennms.netmgt.correlation.CorrelationEngineRegistrar;
import org.opennms.netmgt.correlation.drools.config.EngineConfiguration;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * <p>
 * DroolsCorrelationEngineBuilder class.
 * </p>
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @version $Id: $
 */
public class DroolsCorrelationEngineBuilder extends PropertyEditorRegistrySupport implements InitializingBean,
        ApplicationListener<ApplicationEvent> {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(DroolsCorrelationEngineBuilder.class);

    /** The Constant PLUGIN_CONFIG_FILE_NAME. */
    public static final String PLUGIN_CONFIG_FILE_NAME = "drools-engine.xml";

    /**
     * The Class PluginConfiguration.
     */
    private static class PluginConfiguration {

        /** The m_config resource. */
        private Resource m_configResource;

        /** The m_configuration. */
        private EngineConfiguration m_configuration;

        /**
         * Instantiates a new plugin configuration.
         *
         * @param configResource
         *            the config resource
         */
        public PluginConfiguration(Resource configResource) {
            m_configResource = configResource;
        }

        /**
         * Read config.
         */
        public void readConfig() {
            LOG.info("Parsing drools engine configuration at {}.", m_configResource);
            m_configuration = JaxbUtils.unmarshal(EngineConfiguration.class, m_configResource);
        }

        /**
         * Construct engines.
         *
         * @param appContext
         *            the app context
         * @param eventIpcManager
         *            the event ipc manager
         * @return the correlation engine[]
         */
        public CorrelationEngine[] constructEngines(ApplicationContext appContext, EventIpcManager eventIpcManager) {
            LOG.info("Creating drools engins for configuration {}.", m_configResource);

            return m_configuration.constructEngines(m_configResource, appContext, eventIpcManager);
        }

    }

    // injected
    /** The m_config directory. */
    private File m_configDirectory;

    /** The m_config resource. */
    private Resource m_configResource;

    /** The m_event ipc manager. */
    private EventIpcManager m_eventIpcManager;

    /** The m_correlator. */
    private CorrelationEngineRegistrar m_correlator;

    // built
    /** The m_plugin configurations. */
    private PluginConfiguration[] m_pluginConfigurations;

    /**
     * <p>
     * Constructor for DroolsCorrelationEngineBuilder.
     * </p>
     */
    public DroolsCorrelationEngineBuilder() {
        registerDefaultEditors();
    }

    /**
     * <p>
     * assertSet
     * </p>
     * .
     *
     * @param obj
     *            a {@link java.lang.Object} object.
     * @param name
     *            a {@link java.lang.String} object.
     */
    public void assertSet(final Object obj, final String name) {
        Assert.state(obj != null, name + " required for DroolsEngineFactoryBean");
    }

    /**
     * <p>
     * afterPropertiesSet
     * </p>
     * .
     *
     * @throws Exception
     *             the exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        assertSet(m_configDirectory, "configurationDirectory");
        assertSet(m_eventIpcManager, "eventIpcManager");
        assertSet(m_correlator, "correlator");

        Assert.state(!m_configDirectory.exists() || m_configDirectory.isDirectory(), m_configDirectory
                + " must be a directory!");

        readConfiguration();
    }

    /**
     * Register engines.
     *
     * @param appContext
     *            the app context
     */
    private void registerEngines(final ApplicationContext appContext) {
        for (PluginConfiguration pluginConfig : m_pluginConfigurations) {
            m_correlator.addCorrelationEngines(pluginConfig.constructEngines(appContext, m_eventIpcManager));
        }

    }

    /**
     * <p>
     * setEventIpcManager
     * </p>
     * .
     *
     * @param eventIpcManager
     *            a {@link org.opennms.netmgt.model.events.EventIpcManager}
     *            object.
     */
    public void setEventIpcManager(final EventIpcManager eventIpcManager) {
        m_eventIpcManager = eventIpcManager;
    }

    /**
     * <p>
     * setConfigurationResource
     * </p>
     * .
     *
     * @param configResource
     *            a {@link org.springframework.core.io.Resource} object.
     */
    public void setConfigurationResource(final Resource configResource) {
        m_configResource = configResource;
    }

    /**
     * <p>
     * setConfigurationDirectory
     * </p>
     * .
     *
     * @param configDirectory
     *            a {@link java.io.File} object.
     */
    public void setConfigurationDirectory(final File configDirectory) {
        m_configDirectory = configDirectory;
    }

    /**
     * <p>
     * setCorrelationEngineRegistrar
     * </p>
     * .
     *
     * @param correlator
     *            a
     *            {@link org.opennms.netmgt.correlation.CorrelationEngineRegistrar}
     *            object.
     */
    public void setCorrelationEngineRegistrar(final CorrelationEngineRegistrar correlator) {
        m_correlator = correlator;
    }

    /**
     * Read configuration.
     *
     * @throws Exception
     *             the exception
     */
    private void readConfiguration() throws Exception {
        m_pluginConfigurations = locatePluginConfigurations();

        // now parse all of the configuration files
        for (PluginConfiguration pluginCofig : m_pluginConfigurations) {
            pluginCofig.readConfig();
        }

    }

    /**
     * Locate plugin configurations.
     *
     * @return the plugin configuration[]
     * @throws Exception
     *             the exception
     */
    private PluginConfiguration[] locatePluginConfigurations() throws Exception {
        List<PluginConfiguration> pluginConfigs = new LinkedList<PluginConfiguration>();

        // first we see if the config is etc exists
        if (m_configResource != null && m_configResource.isReadable()) {
            LOG.info("Found Drools Plugin config file {}.", m_configResource);
            pluginConfigs.add(new PluginConfiguration(m_configResource));
        }

        // then we look in each plugin dir for a config
        File[] pluginDirs = getPluginDirs();

        for (File pluginDir : pluginDirs) {
            File configFile = new File(pluginDir, PLUGIN_CONFIG_FILE_NAME);
            if (!configFile.exists()) {
                LOG.error("Drools Plugin directory {} does not contains a {} config file.  Ignoring plugin.",
                          pluginDir, PLUGIN_CONFIG_FILE_NAME);
            } else {
                LOG.info("Found Drools Plugin directory {} containing a {} config file.", pluginDir,
                         PLUGIN_CONFIG_FILE_NAME);
                pluginConfigs.add(new PluginConfiguration(new FileSystemResource(configFile)));
            }
        }

        return pluginConfigs.toArray(new PluginConfiguration[0]);
    }

    /**
     * Gets the plugin dirs.
     *
     * @return the plugin dirs
     * @throws Exception
     *             the exception
     */
    private File[] getPluginDirs() throws Exception {

        LOG.debug("Checking {} for drools correlation plugins", m_configDirectory);

        if (!m_configDirectory.exists()) {
            LOG.debug("Plugin configuration directory does not exists.");
            return new File[0];
        }

        File[] pluginDirs = m_configDirectory.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        LOG.debug("Found {} drools correlation plugin sub directories", pluginDirs.length);

        return pluginDirs;
    }

    /** {@inheritDoc} */
    @Override
    public void onApplicationEvent(final ApplicationEvent appEvent) {
        if (appEvent instanceof ContextRefreshedEvent) {
            final ApplicationContext appContext = ((ContextRefreshedEvent) appEvent).getApplicationContext();
            if (!(appContext instanceof ConfigFileApplicationContext)) {
                registerEngines(appContext);
            }
        }

    }

}
