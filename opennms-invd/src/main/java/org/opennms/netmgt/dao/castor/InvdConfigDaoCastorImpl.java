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
package org.opennms.netmgt.dao.castor;

import org.opennms.netmgt.dao.InvdConfigDao;
import org.opennms.netmgt.config.invd.*;
import org.opennms.netmgt.config.invd.Package;
import org.opennms.netmgt.config.CollectdConfigFactory;
import org.opennms.netmgt.config.InvdConfig;
import org.opennms.netmgt.config.InvdConfigFactory;
import org.opennms.netmgt.config.InvdPackage;
import org.opennms.core.utils.ThreadCategory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.apache.log4j.Category;

import java.util.Collection;
import java.lang.reflect.UndeclaredThrowableException;
import java.io.IOException;

public class InvdConfigDaoCastorImpl implements InvdConfigDao {
    public InvdConfigDaoCastorImpl() {
        loadConfigFactory();

    }

    public Category log() {
        return ThreadCategory.getInstance(getClass());
    }

    private InvdConfig getConfig() {
        return InvdConfigFactory.getInstance().getInvdConfig();
    }

    private void loadConfigFactory() {
        // Load collectd configuration file
        try {
            // XXX was reload(); this doesn't work well from unit tests, however
            InvdConfigFactory.init();
        } catch (MarshalException ex) {
            log().fatal("loadConfigFactory: Failed to load invd configuration", ex);
            throw new UndeclaredThrowableException(ex);
        } catch (ValidationException ex) {
            log().fatal("loadConfigFactory: Failed to load invd configuration", ex);
            throw new UndeclaredThrowableException(ex);
        } catch (IOException ex) {
            log().fatal("loadConfigFactory: Failed to load invd configuration", ex);
            throw new UndeclaredThrowableException(ex);
        }
    }
    
    public int getSchedulerThreads() {
        return getConfig().getThreads();
    }

    public Collection<Scanner> getScanners() {
        return getConfig().getConfig().getScannerCollection();
    }

    public void rebuildPackageIpListMap() {
        getConfig().createPackageIpListMap();
    }

    public Collection<InvdPackage> getPackages() {
        return getConfig().getPackages();
    }

    public InvdPackage getPackage(String name) {
        return getConfig().getPackage(name);
    }
}
