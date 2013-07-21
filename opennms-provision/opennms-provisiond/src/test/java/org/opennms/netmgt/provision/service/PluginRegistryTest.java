/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.service;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.utils.BeanUtils;
import org.opennms.netmgt.provision.IpInterfacePolicy;
import org.opennms.netmgt.provision.NodePolicy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * PluginRegistryTest.
 *
 * @author brozow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/pluginRegistryTest-context.xml" })
public class PluginRegistryTest implements InitializingBean {

    /** The m_app context. */
    @Autowired
    ApplicationContext m_appContext;

    /** The m_plugin registry. */
    @Autowired
    PluginRegistry m_pluginRegistry;

    /**
     * The Interface BeanMatcher.
     *
     * @param <T>
     *            the generic type
     */
    interface BeanMatcher<T> {

        /**
         * Matches.
         *
         * @param t
         *            the t
         * @return true, if successful
         */
        boolean matches(T t);
    }

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
    }

    /**
     * Test go.
     */
    @Test
    public void testGo() {

        Collection<NodePolicy> nodePolicies = m_pluginRegistry.getAllPlugins(NodePolicy.class);

        Collection<IpInterfacePolicy> ifPolicies = m_pluginRegistry.getAllPlugins(IpInterfacePolicy.class);

        assertEquals(3, nodePolicies.size());
        assertEquals(1, ifPolicies.size());

    }

    /**
     * Gets the beans of type.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return the beans of type
     */
    public <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return m_appContext.getBeansOfType(clazz, true, true);
    }

}
