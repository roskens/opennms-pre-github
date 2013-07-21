/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.snmp.snmp4j;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.After;
import org.junit.Before;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.mock.snmp.MockSnmpAgent;
import org.opennms.netmgt.snmp.SnmpAgentAddress;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.mock.MockSnmpStrategy;
import org.opennms.test.mock.MockUtil;
import org.springframework.core.io.ClassPathResource;

/**
 * The Class MockSnmpAgentTestCase.
 */
public abstract class MockSnmpAgentTestCase {

    /** The m_agent address. */
    private InetAddress m_agentAddress;

    /** The m_agent port. */
    private int m_agentPort = 1691;

    /** The m_properties resource. */
    private ClassPathResource m_propertiesResource = new ClassPathResource("loadSnmpDataTest.properties");

    /** The m_agent. */
    private MockSnmpAgent m_agent;

    /**
     * Instantiates a new mock snmp agent test case.
     */
    public MockSnmpAgentTestCase() {
        setAgentAddress(InetAddressUtils.getLocalHostAddress());
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        MockUtil.println("------------ Strategy = " + System.getProperty("org.opennms.snmp.strategyClass")
                + " --------------------------");

        MockLogAppender.setupLogging();

        agentSetup();
    }

    /**
     * Agent setup.
     *
     * @throws InterruptedException
     *             the interrupted exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected void agentSetup() throws InterruptedException, IOException {
        if (usingMockStrategy()) {
            MockSnmpStrategy.setDataForAddress(new SnmpAgentAddress(getAgentAddress(), getAgentPort()),
                                               m_propertiesResource);
        } else {
            m_agent = MockSnmpAgent.createAgentAndRun(m_propertiesResource.getURL(), getAgentAddress().getHostAddress()
                    + "/" + m_agentPort);
        }
    }

    /**
     * Using mock strategy.
     *
     * @return true, if successful
     */
    protected boolean usingMockStrategy() {
        return MockSnmpStrategy.class.getName().equals(System.getProperty("org.opennms.snmp.strategyClass"));
    }

    /**
     * Tear down.
     *
     * @throws Exception
     *             the exception
     */
    @After
    public void tearDown() throws Exception {

        agentCleanup();

        // MockLogAppender.assertNoWarningsOrGreater();

        MockUtil.println("------------ End Test --------------------------");
    }

    /**
     * Agent cleanup.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected void agentCleanup() throws InterruptedException {
        MockSnmpStrategy.removeHost(new SnmpAgentAddress(getAgentAddress(), getAgentPort()));

        if (m_agent != null) {
            m_agent.shutDownAndWait();
        }

    }

    /**
     * Gets the agent config.
     *
     * @return the agent config
     */
    protected SnmpAgentConfig getAgentConfig() {
        SnmpAgentConfig config = new SnmpAgentConfig();
        config.setAddress(getAgentAddress());
        config.setPort(getAgentPort());
        config.setVersion(SnmpAgentConfig.VERSION1);
        return config;
    }

    /**
     * Gets the agent address.
     *
     * @return the agent address
     */
    public InetAddress getAgentAddress() {
        return m_agentAddress;
    }

    /**
     * Sets the agent address.
     *
     * @param agentAddress
     *            the new agent address
     */
    protected void setAgentAddress(final InetAddress agentAddress) {
        m_agentAddress = agentAddress;
    }

    /**
     * Gets the agent port.
     *
     * @return the agent port
     */
    public int getAgentPort() {
        return m_agentPort;
    }

    /**
     * Sets the agent port.
     *
     * @param agentPort
     *            the new agent port
     */
    protected void setAgentPort(final int agentPort) {
        m_agentPort = agentPort;
    }

    /**
     * Gets the properties resource.
     *
     * @return the properties resource
     */
    public ClassPathResource getPropertiesResource() {
        return m_propertiesResource;
    }

    /**
     * Sets the properties resource.
     *
     * @param propertiesResource
     *            the new properties resource
     */
    public void setPropertiesResource(final ClassPathResource propertiesResource) {
        m_propertiesResource = propertiesResource;
    }

    /**
     * Gets the agent.
     *
     * @return the agent
     */
    public MockSnmpAgent getAgent() {
        return m_agent;
    }
}
