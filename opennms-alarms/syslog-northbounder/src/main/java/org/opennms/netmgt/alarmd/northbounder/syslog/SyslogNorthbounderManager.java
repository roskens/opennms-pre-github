/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.alarmd.northbounder.syslog;

import java.util.List;

import org.opennms.core.soa.Registration;
import org.opennms.core.soa.ServiceRegistry;
import org.opennms.netmgt.alarmd.api.Northbounder;
import org.opennms.netmgt.dao.api.NodeDao;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * The Class SyslogNorthbounderManager.
 */
public class SyslogNorthbounderManager implements InitializingBean, DisposableBean {

    /** The m_service registry. */
    @Autowired
    private ServiceRegistry m_serviceRegistry;

    /** The m_config dao. */
    @Autowired
    private SyslogNorthbounderConfigDao m_configDao;

    /** The m_node dao. */
    @Autowired
    private NodeDao m_nodeDao;

    /** The m_registration. */
    private Registration m_registration = null;

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        Assert.notNull(m_nodeDao);
        Assert.notNull(m_configDao);
        Assert.notNull(m_serviceRegistry);

        SyslogNorthbounderConfig config = m_configDao.getConfig();

        List<SyslogDestination> destinations = config.getDestinations();
        for (SyslogDestination syslogDestination : destinations) {
            SyslogNorthbounder nbi = new SyslogNorthbounder(config, syslogDestination);
            nbi.setNodeDao(m_nodeDao);
            nbi.afterPropertiesSet();
            m_registration = m_serviceRegistry.register(nbi, Northbounder.class);
        }

    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        m_registration.unregister();
    }

}
