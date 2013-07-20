/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao.jaxb;

import org.opennms.core.xml.AbstractJaxbConfigDao;
import org.opennms.netmgt.config.accesspointmonitor.AccessPointMonitorConfig;
import org.opennms.netmgt.dao.AccessPointMonitorConfigDao;

/**
 * <p>
 * AccessPointMonitorConfigDaoJaxb
 * </p>
 *
 * @author <a href="mailto:jwhite@datavalet.com">Jesse White</a>
 */
public class AccessPointMonitorConfigDaoJaxb extends
        AbstractJaxbConfigDao<AccessPointMonitorConfig, AccessPointMonitorConfig> implements
        AccessPointMonitorConfigDao {

    public AccessPointMonitorConfigDaoJaxb() {
        super(AccessPointMonitorConfig.class, "Access Point Monitor Configuration");
    }

    @Override
    public AccessPointMonitorConfig getConfig() {
        return getContainer().getObject();
    }

    @Override
    public AccessPointMonitorConfig translateConfig(AccessPointMonitorConfig jaxbConfig) {
        return jaxbConfig;
    }

}
