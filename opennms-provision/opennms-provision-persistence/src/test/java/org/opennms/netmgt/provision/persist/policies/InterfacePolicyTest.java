/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.persist.policies;

import static org.junit.Assert.assertEquals;
import static org.opennms.core.utils.InetAddressUtils.str;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.utils.BeanUtils;
import org.opennms.netmgt.dao.DatabasePopulator;
import org.opennms.netmgt.dao.api.IpInterfaceDao;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class InterfacePolicyTest.
 */
@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-mockDao.xml",
        "classpath:/META-INF/opennms/applicationContext-mockEventd.xml" })
@JUnitConfigurationEnvironment
public class InterfacePolicyTest implements InitializingBean {

    /** The m_ip interface dao. */
    @Autowired
    private IpInterfaceDao m_ipInterfaceDao;

    /** The m_populator. */
    @Autowired
    private DatabasePopulator m_populator;

    /** The m_interfaces. */
    private List<OnmsIpInterface> m_interfaces;

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        MockLogAppender.setupLogging();
        m_populator.populateDatabase();
        m_interfaces = m_ipInterfaceDao.findAll();
    }

    /**
     * Tear down.
     */
    @After
    public void tearDown() {
        m_populator.resetDatabase();
    }

    /**
     * Test matching policy.
     */
    @Test
    @Transactional
    public void testMatchingPolicy() {
        OnmsIpInterface o = null;

        final MatchingIpInterfacePolicy p = new MatchingIpInterfacePolicy();
        p.setAction("DO_NOT_PERSIST");
        p.setMatchBehavior("NO_PARAMETERS");
        p.setIpAddress("~^10\\..*$");

        final List<OnmsIpInterface> populatedInterfaces = new ArrayList<OnmsIpInterface>();
        final List<OnmsIpInterface> matchedInterfaces = new ArrayList<OnmsIpInterface>();

        for (final OnmsIpInterface iface : m_interfaces) {
            System.err.println(iface);
            o = p.apply(iface);
            if (o != null) {
                matchedInterfaces.add(o);
            }
            InetAddress addr = iface.getIpAddress();

            if (str(addr).startsWith("10.")) {
                populatedInterfaces.add(iface);
            }
        }

        assertEquals(populatedInterfaces, matchedInterfaces);
    }

}
