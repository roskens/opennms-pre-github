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
package org.opennms.netmgt.invd.scanners.wmi;

import org.opennms.netmgt.invd.InventoryScanner;
import org.opennms.netmgt.invd.ScanningClient;
import org.opennms.netmgt.invd.InventorySet;
import org.opennms.netmgt.invd.InventoryException;
import org.opennms.netmgt.model.events.EventProxy;
import org.opennms.netmgt.config.DataSourceFactory;
import org.opennms.protocols.wmi.WmiClient;
import org.opennms.protocols.wmi.WmiException;
import org.opennms.core.utils.ThreadCategory;
import org.apache.log4j.Category;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.SQLException;
import java.beans.PropertyVetoException;

public class WmiScanner implements InventoryScanner {
    private final HashMap<Integer, WmiClientState> m_scheduledNodes = new HashMap<Integer, WmiClientState>();
    public static void main(String[] args) {
        try {
            WmiClient wmiClient = new WmiClient("localhost");
            wmiClient.connect("WORKGROUP", "mraykowski", "changedthissorry");
        } catch(WmiException e) {
            // Handle this some how.
        }
    }

    private Category log() {
        return ThreadCategory.getInstance(getClass());
    }

    public void initialize(Map<String, String> parameters) {
        log().debug("initialize: Initializing WmiCollector.");
        m_scheduledNodes.clear();
        //initWMIPeerFactory();
        //initWMICollectionConfig();
        initDatabaseConnectionFactory();
        //initializeRrdRepository();
    }

    public void initialize(ScanningClient client, Map<String, String> parameters) {
        log().debug("initialize: Initializing WMI collection for agent: " + client);
        Integer scheduledNodeKey = new Integer(client.getNodeId());
        WmiClientState nodeState = m_scheduledNodes.get(scheduledNodeKey);

        if (nodeState != null) {
            log().info("initialize: Not scheduling interface for WMI collection: " + nodeState.getAddress());
            final StringBuffer sb = new StringBuffer();
            sb.append("initialize service: ");

            sb.append(" for address: ");
            sb.append(nodeState.getAddress());
            sb.append(" already scheduled for collection on node: ");
            sb.append(client);
            log().debug(sb.toString());
            throw new IllegalStateException(sb.toString());
        } else {
            nodeState = new WmiClientState(client.getInetAddress(), parameters);
            log().info("initialize: Scheduling interface for collection: " + nodeState.getAddress());
            m_scheduledNodes.put(scheduledNodeKey, nodeState);
        }
    }

    public void release() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    //public void initialize(ScanningClient agent, Map<String, String> parameters) {
    //    //To change body of implemented methods use File | Settings | File Templates.
    //}

    public void release(ScanningClient agent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public InventorySet collect(ScanningClient client, EventProxy eproxy, Map<String, String> parameters) throws InventoryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void initDatabaseConnectionFactory() {
        try {
            DataSourceFactory.init();
        } catch (IOException e) {
            log().fatal("initDatabaseConnectionFactory: IOException getting database connection", e);
            throw new UndeclaredThrowableException(e);
        } catch (MarshalException e) {
            log().fatal("initDatabaseConnectionFactory: Marshall Exception getting database connection", e);
            throw new UndeclaredThrowableException(e);
        } catch (ValidationException e) {
            log().fatal("initDatabaseConnectionFactory: Validation Exception getting database connection", e);
            throw new UndeclaredThrowableException(e);
        } catch (SQLException e) {
            log().fatal("initDatabaseConnectionFactory: Failed getting connection to the database.", e);
            throw new UndeclaredThrowableException(e);
        } catch (PropertyVetoException e) {
            log().fatal("initDatabaseConnectionFactory: Failed getting connection to the database.", e);
            throw new UndeclaredThrowableException(e);
        } catch (ClassNotFoundException e) {
            log().fatal("initDatabaseConnectionFactory: Failed loading database driver.", e);
            throw new UndeclaredThrowableException(e);
        }
    }
}
