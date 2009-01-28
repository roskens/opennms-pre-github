//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
// OpenNMS Licensing       <license@opennms.org>
//     http://www.opennms.org/
//     http://www.opennms.com/
//
package org.opennms.netmgt.config;

import org.springframework.util.Assert;
import org.apache.log4j.Category;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.ConfigFileConstants;
import org.opennms.netmgt.config.OpennmsServerConfigFactory;
import org.opennms.netmgt.config.invd.InvdConfiguration;
import org.opennms.netmgt.dao.castor.CastorUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.io.File;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class InvdConfigFactory {
    final static String SELECT_METHOD_MIN = "min";

    /**
     * The singleton instance of this factory.  Null if the factory hasn't been
     * initialized.
     */
    private static InvdConfigFactory m_singleton = null;

    private InvdConfig m_invdConfig;
    /**
     * Return the singleton instance of this factory.
     *
     * @return The current factory instance.
     *
     * @throws java.lang.IllegalStateException
     *             Thrown if the factory has not yet been initialized.
     */
    public static synchronized InvdConfigFactory getInstance() {
        Assert.state(isInitialized(), "The factory has not been initialized");

        return m_singleton;
    }

    public static synchronized void setInstance(InvdConfigFactory instance) {
        m_singleton = instance;
    }

    public InvdConfig getInvdConfig() {
        return m_invdConfig;
    }

    private static boolean isInitialized() {
        return m_singleton != null;
    }

    private static Category log() {
        return ThreadCategory.getInstance(InvdConfigFactory.class);
    }

    /**
     * This method is used to rebuild the package agaist iplist mapping when
     * needed. When a node gained service event occurs, collectd has to
     * determine which package the ip/service combination is in, but if the
     * interface is a newly added one, the package iplist should be rebuilt so
     * that collectd could know which package this ip/service pair is in.
     */
    public synchronized void rebuildPackageIpListMap() {
        m_invdConfig.createPackageIpListMap();
    }


    /**
     * Private constructor
     *
     * @exception java.io.IOException
     *                Thrown if the specified config file cannot be read
     * @exception org.exolab.castor.xml.MarshalException
     *                Thrown if the file does not conform to the schema.
     * @exception org.exolab.castor.xml.ValidationException
     *                Thrown if the contents do not match the required schema.
     */
    private InvdConfigFactory(String configFile) throws IOException, MarshalException, ValidationException {
        InputStreamReader rdr = new InputStreamReader(new FileInputStream(configFile));

        try {
            initialize(rdr);
        } finally {
            rdr.close();
        }
    }

    /**
     * Public constructor
     *
     * @exception java.io.IOException
     *                Thrown if the specified config file cannot be read
     * @exception org.exolab.castor.xml.MarshalException
     *                Thrown if the file does not conform to the schema.
     * @exception org.exolab.castor.xml.ValidationException
     *                Thrown if the contents do not match the required schema.
     */
    public InvdConfigFactory(Reader rdr, String localServer, boolean verifyServer) throws IOException, MarshalException, ValidationException {
        initialize(rdr);
    }

    private void initialize(Reader rdr) throws MarshalException, ValidationException, IOException {

        InvdConfiguration config = CastorUtils.unmarshal(InvdConfiguration.class, rdr);
        m_invdConfig = new InvdConfig(config);
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
     */
    public static synchronized void init() throws IOException, MarshalException, ValidationException {
        if (isInitialized()) {
            // init already called return; to reload, reload() will need to be called
            return;
        }

        OpennmsServerConfigFactory.init();

        File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.INVD_CONFIG_FILE_NAME);

        log().debug("init: config file path: " + cfgFile.getPath());

         setInstance(new InvdConfigFactory(cfgFile.getPath()));
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
     */
    public static synchronized void reload() throws IOException, MarshalException, ValidationException {
        m_singleton = null;

        init();
    }

    /**
     * Saves the current in-memory configuration to disk and reloads
     */
    public synchronized void saveCurrent() throws MarshalException, IOException, ValidationException {
        File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.INVD_CONFIG_FILE_NAME);

        InvdConfiguration config = m_invdConfig.getConfig();

        CastorUtils.marshalViaString(config, cfgFile);

        reload();
    }



    public InvdPackage getPackage(String name) {
        return m_invdConfig.getPackage(name);
    }

    public boolean isServiceCollectionEnabled(String ipAddr, String svcName) {
        return m_invdConfig.isServiceCollectionEnabled(ipAddr, svcName);
    }
}
