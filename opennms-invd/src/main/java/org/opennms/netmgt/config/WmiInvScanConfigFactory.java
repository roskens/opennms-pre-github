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
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//
package org.opennms.netmgt.config;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.dao.castor.CastorUtils;
import org.opennms.netmgt.ConfigFileConstants;
import org.opennms.netmgt.config.wmi.WmiInvscanConfig;
import org.opennms.netmgt.config.wmi.WmiInventory;
import org.opennms.core.utils.ThreadCategory;
import org.apache.log4j.Category;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.List;

/**
 * <P>
 * This class loads and presents the WMI inventory scanner configuration file
 * for use with the WMI inventory scanning classes.
 * </P>
 *
 * @author <A HREF="mailto:matt.raykowski@gmail.com">Matt Raykowski </A>
 * @author <A HREF="http://www.opennsm.org">OpenNMS </A>
 */
public class WmiInvScanConfigFactory {
    /** The singleton instance. */
     private static WmiInvScanConfigFactory m_instance;

     private static boolean m_loadedFromFile = false;

     /** Boolean indicating if the init() method has been called. */
     protected boolean initialized = false;

     /** Timestamp of the WMI collection config, used to know when to reload from disk. */
     protected static long m_lastModified;

     private static WmiInvscanConfig m_config;

     public WmiInvScanConfigFactory(String configFile) throws MarshalException, ValidationException, IOException {
         InputStreamReader rdr = new InputStreamReader(new FileInputStream(configFile));

         try {
             initialize(rdr);
         } finally {
             rdr.close();
         }
     }

     public WmiInvScanConfigFactory(Reader rdr) throws MarshalException, ValidationException {
         initialize(rdr);
     }

     private void initialize(Reader rdr) throws MarshalException, ValidationException {
         log().debug("initialize: initializing WMI collection config factory.");
         m_config = CastorUtils.unmarshal(WmiInvscanConfig.class, rdr);
     }

     /** Be sure to call this method before calling getInstance(). */
     public static synchronized void init() throws IOException, FileNotFoundException, MarshalException, ValidationException {

         if (m_instance == null) {
             File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.WMI_INV_SCAN_CONFIG_FILE_NAME);
             m_instance = new WmiInvScanConfigFactory(cfgFile.getPath());
             m_lastModified = cfgFile.lastModified();
             m_loadedFromFile = true;
         }
     }

     /**
      * Singleton static call to get the only instance that should exist
      *
      * @return the single factory instance
      * @throws IllegalStateException
      *             if init has not been called
      */
     public static synchronized WmiInvScanConfigFactory getInstance() {

         if (m_instance == null) {
             throw new IllegalStateException("You must call WmiCollectionConfigFactory.init() before calling getInstance().");
         }
         return m_instance;
     }

     public static synchronized void setInstance(WmiInvScanConfigFactory instance) {
         m_instance = instance;
         m_loadedFromFile = false;
     }

     public synchronized void reload() throws IOException, FileNotFoundException, MarshalException, ValidationException {
         m_instance = null;
         init();
     }


     /**
      * Reload the wmi-datacollection-config.xml file if it has been changed since we last
      * read it.
      */
     protected void updateFromFile() throws IOException, MarshalException, ValidationException {
         if (m_loadedFromFile) {
             File surveillanceViewsFile = ConfigFileConstants.getFile(ConfigFileConstants.WMI_COLLECTION_CONFIG_FILE_NAME);
             if (m_lastModified != surveillanceViewsFile.lastModified()) {
                 this.reload();
             }
         }
     }

     public synchronized static WmiInvscanConfig getConfig() {
         return m_config;
     }

     public synchronized static void setConfig(WmiInvscanConfig m_config) {
         WmiInvScanConfigFactory.m_config = m_config;
     }

     private Category log() {
         return ThreadCategory.getInstance();
     }

      @SuppressWarnings("unchecked")
     public WmiInventory getWmiInventory(String inventoryName) {
        WmiInventory[] inventories = m_config.getWmiInventory();
         WmiInventory inventory = null;
         for (WmiInventory inv : inventories) {
             if (inv.getName().equalsIgnoreCase(inventoryName)) inventory = inv;
             break;
         }
         if (inventory == null) {
             throw new IllegalArgumentException("getWmiInventory: inventory name: "
                     +inventoryName+" specified in invd configuration not found in WMI scan configuration.");
         }
         return inventory;
     }
}
