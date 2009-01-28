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

import org.opennms.netmgt.config.invd.*;
import org.opennms.netmgt.config.invd.Package;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Enumeration;
import java.util.Iterator;

public class InvdConfig {
    private InvdConfiguration m_config;
    private Collection<InvdPackage> m_packages;

    /**
     * Convenience object for CollectdConfiguration.
     *
     * @param config collectd configuration object
     */
    protected InvdConfig(InvdConfiguration config) {
        m_config = config;

        createPackageObjects();

        initialize();

    }

    public InvdConfiguration getConfig() {
        return m_config;
    }

    public Collection<InvdPackage> getPackages() {
        return m_packages;
    }

    public int getThreads() {
        return m_config.getThreads();
    }

    /**
     * This method is used to establish package agaist iplist mapping, with
     * which, the iplist is selected per package via the configured filter rules
     * from the database.
     */
    public void createPackageIpListMap() {
        // Multiple threads maybe asking for the m_pkgIpMap field so create
        // with temp map then assign when finished.

        for (Iterator<InvdPackage> it = getPackages().iterator(); it.hasNext();) {
            InvdPackage wpkg = it.next();
            wpkg.createIpList();
        }
    }

    private void createPackageObjects() {
        m_packages = new LinkedList<InvdPackage>();
        Enumeration<org.opennms.netmgt.config.invd.Package> pkgEnum = m_config.enumeratePackage();
        while (pkgEnum.hasMoreElements()) {
            Package pkg = pkgEnum.nextElement();
            m_packages.add(new InvdPackage(pkg));
        }
    }

    protected void initialize()  {
        createPackageIpListMap();

    }

    public InvdPackage getPackage(String name) {
        for (Iterator<InvdPackage> it = getPackages().iterator(); it.hasNext();) {
            InvdPackage wpkg = it.next();
            if (wpkg.getName().equals(name)) {
                return wpkg;
            }
        }
        return null;
    }

    /**
     * Returns true if the specified interface is included by at least one
     * package which has the specified service and that service is enabled (set
     * to "on").
     *
     * @param ipAddr
     *            IP address of the interface to lookup
     * @param svcName
     *            The service name to lookup
     * @return true if Invd config contains a package which includes the
     *         specified interface and has the specified service enabled.
     */
    public boolean isServiceCollectionEnabled(String ipAddr, String svcName) {
        boolean result = false;

        for (Iterator<InvdPackage> it = getPackages().iterator(); it.hasNext();) {
            InvdPackage wpkg = it.next();

            // Does the package include the interface?
            //
            if (wpkg.interfaceInPackage(ipAddr)) {
                // Yes, now see if package includes
                // the service and service is enabled
                if (wpkg.serviceInPackageAndEnabled(svcName)) {
                    // Thats all we need to know...
                    result = true;
                }
            }
        }

        return result;
    }
}
