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

package org.opennms.core.test.snmp;

import static org.opennms.core.utils.InetAddressUtils.addr;
import static org.opennms.core.utils.InetAddressUtils.str;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.opennms.core.test.snmp.annotations.JUnitSnmpAgent;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgents;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.mock.snmp.MockSnmpAgent;
import org.opennms.netmgt.config.SnmpAgentConfigFactory;
import org.opennms.netmgt.config.SnmpAgentConfigProxyMapper;
import org.opennms.netmgt.snmp.SnmpAgentAddress;
import org.opennms.netmgt.snmp.mock.MockSnmpStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * This {@link TestExecutionListener} looks for the {@link JUnitSnmpAgent}
 * annotation
 * and uses attributes on it to launch a mock SNMP agent for use during unit
 * testing.
 *
 * @see JUnitSnmpAgentExecutionEvent
 */
public class JUnitSnmpAgentExecutionListener extends AbstractTestExecutionListener {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(JUnitSnmpAgentExecutionListener.class);

    /** The Constant useMockSnmpStrategyDefault. */
    private static final Boolean useMockSnmpStrategyDefault = false;

    /** The Constant USE_STRATEGY_PROPERTY. */
    private static final String USE_STRATEGY_PROPERTY = "org.opennms.core.test-api.snmp.useMockSnmpStrategy";

    /** The Constant STRATEGY_CLASS_PROPERTY. */
    private static final String STRATEGY_CLASS_PROPERTY = "org.opennms.snmp.strategyClass";

    /** The Constant STRATEGY_CLASS_KEY. */
    private static final String STRATEGY_CLASS_KEY = "org.opennms.core.test-api.snmp.strategyClass";

    /** The Constant AGENT_KEY. */
    private static final String AGENT_KEY = "org.opennms.core.test-api.snmp.agentList";

    /** The Constant PROVIDER_KEY. */
    private static final String PROVIDER_KEY = "org.opennms.core.test-api.snmp.dataProvider";

    /* (non-Javadoc)
     * @see org.springframework.test.context.support.AbstractTestExecutionListener#beforeTestMethod(org.springframework.test.context.TestContext)
     */
    @Override
    public void beforeTestMethod(final TestContext testContext) throws Exception {
        super.beforeTestClass(testContext);

        final JUnitSnmpAgents agents = findAgentListAnnotation(testContext);
        final JUnitSnmpAgent agent = findAgentAnnotation(testContext);

        if (agents == null && agent == null) {
            // no annotations found
            return;
        }

        final String strategy = System.getProperty(STRATEGY_CLASS_PROPERTY);
        LOG.debug("Initializing JUnit SNMP Agent with strategy: {}", strategy);

        testContext.setAttribute(STRATEGY_CLASS_KEY, strategy);
        final HashMap<SnmpAgentAddress, MockSnmpAgent> mockAgents = new HashMap<SnmpAgentAddress, MockSnmpAgent>();
        testContext.setAttribute(AGENT_KEY, mockAgents);

        final String useMockSnmpStrategyString = System.getProperty(USE_STRATEGY_PROPERTY,
                                                                    useMockSnmpStrategyDefault.toString());
        final Boolean useMockSnmpStrategy = Boolean.valueOf(useMockSnmpStrategyString);
        final MockSnmpDataProvider provider;
        if (useMockSnmpStrategy) {
            System.setProperty(STRATEGY_CLASS_PROPERTY, MockSnmpStrategy.class.getName());
            provider = new MockSnmpStrategyDataProvider();
        } else {
            provider = new MockSnmpAgentDataProvider(mockAgents);
        }
        testContext.setAttribute(PROVIDER_KEY, provider);

        if (agents != null) {
            for (final JUnitSnmpAgent a : agents.value()) {
                handleSnmpAgent(testContext, a, provider);
            }
        }

        handleSnmpAgent(testContext, findAgentAnnotation(testContext), provider);

        if (testContext.getTestInstance() instanceof MockSnmpDataProviderAware) {
            LOG.debug("injecting data provider into MockSnmpDataProviderAware test: {}", testContext.getTestInstance());
            ((MockSnmpDataProviderAware) testContext.getTestInstance()).setMockSnmpDataProvider(provider);
        }
    }

    /* (non-Javadoc)
     * @see org.springframework.test.context.support.AbstractTestExecutionListener#afterTestMethod(org.springframework.test.context.TestContext)
     */
    @Override
    public void afterTestMethod(final TestContext testContext) throws Exception {
        super.afterTestMethod(testContext);

        final MockSnmpDataProvider provider = (MockSnmpDataProvider) testContext.getAttribute(PROVIDER_KEY);
        if (provider != null) {
            LOG.debug("Tearing down JUnit SNMP Agent provider: {}", provider);
            provider.resetData();
        }

        final String strategyClass = (String) testContext.getAttribute(STRATEGY_CLASS_KEY);
        if (strategyClass != null) {
            System.setProperty(STRATEGY_CLASS_PROPERTY, strategyClass);
        }
    }

    /**
     * Handle snmp agent.
     *
     * @param testContext
     *            the test context
     * @param config
     *            the config
     * @param provider
     *            the provider
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws UnknownHostException
     *             the unknown host exception
     * @throws InterruptedException
     *             the interrupted exception
     */
    private void handleSnmpAgent(final TestContext testContext, final JUnitSnmpAgent config,
            MockSnmpDataProvider provider) throws IOException, UnknownHostException, InterruptedException {
        if (config == null) {
            return;
        }

        String factoryClassName = "unknown";
        try {
            final SnmpAgentConfigFactory factory = testContext.getApplicationContext().getBean("snmpPeerFactory",
                                                                                               SnmpAgentConfigFactory.class);
            factoryClassName = factory.getClass().getName();
        } catch (final Throwable t) {
            // ignore
        }
        if (!factoryClassName.contains("ProxySnmpAgentConfigFactory")) {
            LOG.warn("SNMP Peer Factory ({}) is not the ProxySnmpAgentConfigFactory -- did you forget to include applicationContext-proxy-snmp.xml?",
                     factoryClassName);
        }

        final String useMockSnmpStrategy = System.getProperty(USE_STRATEGY_PROPERTY,
                                                              useMockSnmpStrategyDefault.toString());
        LOG.debug("handleSnmpAgent(testContext, {}, {})", config, useMockSnmpStrategy);

        String host = config.host();
        if (host == null || "".equals(host)) {
            /*
             * NOTE: This call produces different results on different platforms
             * so make
             * sure your client code is aware of this. If you use the {@link
             * ProxySnmpAgentConfigFactory}
             * by including the
             * <code>classpath:/META-INF/opennms/applicationContext
             * -proxy-snmp.xml</code>
             * Spring context, you probably won't need to deal with this. It
             * will override the
             * SnmpPeerFactory with the correct values.
             * Linux: 127.0.0.1
             * Mac OS: primary external interface
             */
            host = InetAddressUtils.getLocalHostAddressAsString();
            // host = "127.0.0.1";
        }

        final ResourceLoader loader = new DefaultResourceLoader();
        final Resource resource = loader.getResource(config.resource());

        // NOTE: The default value for config.port is specified inside {@link
        // JUnitSnmpAgent}
        final InetAddress hostAddress = addr(host);
        final int port = config.port();
        final SnmpAgentAddress agentAddress = new SnmpAgentAddress(hostAddress, port);

        final InetAddress localHost = InetAddress.getLocalHost();
        final SnmpAgentConfigProxyMapper mapper = SnmpAgentConfigProxyMapper.getInstance();

        SnmpAgentAddress listenAddress = null;

        // try to find an unused port on localhost
        int mappedPort = 1161;
        do {
            listenAddress = new SnmpAgentAddress(localHost, mappedPort++);
        } while (mapper.contains(listenAddress));

        if (Boolean.valueOf(useMockSnmpStrategy)) {
            // map to itself =)
            mapper.addProxy(hostAddress, agentAddress);
        } else {
            MockSnmpAgent agent = null;
            while (agent == null) {
                try {
                    agent = MockSnmpAgent.createAgentAndRun(resource.getURL(), str(listenAddress.getAddress()) + "/"
                            + listenAddress.getPort());
                    break;
                } catch (final RuntimeException e) {
                    boolean rethrow = true;
                    Throwable cause = e;
                    while (cause != null) {
                        if (cause instanceof BindException && cause.getMessage().contains("already in use")) {
                            do {
                                listenAddress = new SnmpAgentAddress(localHost, mappedPort++);
                            } while (mapper.contains(listenAddress));
                            cause = null;
                            rethrow = false;
                        } else {
                            cause = cause.getCause();
                        }
                    }
                    if (rethrow) {
                        throw e;
                    }
                }
            }

            mapper.addProxy(hostAddress, listenAddress);

            LOG.debug("using MockSnmpAgent on {} for 'real' address {}", listenAddress, agentAddress);

            @SuppressWarnings("unchecked")
            final Map<SnmpAgentAddress, MockSnmpAgent> agents = (Map<SnmpAgentAddress, MockSnmpAgent>) testContext.getAttribute(AGENT_KEY);
            agents.put(agentAddress, agent);
        }

        provider.setDataForAddress(agentAddress, resource);
    }

    /**
     * Find agent annotation.
     *
     * @param testContext
     *            the test context
     * @return the j unit snmp agent
     */
    private JUnitSnmpAgent findAgentAnnotation(final TestContext testContext) {
        final Method testMethod = testContext.getTestMethod();
        final JUnitSnmpAgent config = testMethod.getAnnotation(JUnitSnmpAgent.class);
        if (config != null) {
            return config;
        }

        final Class<?> testClass = testContext.getTestClass();
        return testClass.getAnnotation(JUnitSnmpAgent.class);

    }

    /**
     * Find agent list annotation.
     *
     * @param testContext
     *            the test context
     * @return the j unit snmp agents
     */
    private JUnitSnmpAgents findAgentListAnnotation(final TestContext testContext) {
        final Method testMethod = testContext.getTestMethod();
        final JUnitSnmpAgents config = testMethod.getAnnotation(JUnitSnmpAgents.class);
        if (config != null) {
            return config;
        }

        final Class<?> testClass = testContext.getTestClass();
        return testClass.getAnnotation(JUnitSnmpAgents.class);

    }

    /**
     * The Class MockSnmpStrategyDataProvider.
     */
    private static final class MockSnmpStrategyDataProvider implements MockSnmpDataProvider {

        /* (non-Javadoc)
         * @see org.opennms.core.test.snmp.MockSnmpDataProvider#setDataForAddress(org.opennms.netmgt.snmp.SnmpAgentAddress, org.springframework.core.io.Resource)
         */
        @Override
        public void setDataForAddress(final SnmpAgentAddress address, final Resource resource) {
            try {
                MockSnmpStrategy.setDataForAddress(address, resource);
            } catch (final Throwable t) {
                LOG.warn("Unable to set mock SNMP data for {}", address, t);
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.core.test.snmp.MockSnmpDataProvider#resetData()
         */
        @Override
        public void resetData() {
            MockSnmpStrategy.resetData();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "MockSnmpStrategyDataProvider[]";
        }
    }

    /**
     * The Class MockSnmpAgentDataProvider.
     */
    private static final class MockSnmpAgentDataProvider implements MockSnmpDataProvider {

        /** The m_agents. */
        private final Map<SnmpAgentAddress, MockSnmpAgent> m_agents;

        /**
         * Instantiates a new mock snmp agent data provider.
         *
         * @param mockAgents
         *            the mock agents
         */
        public MockSnmpAgentDataProvider(final Map<SnmpAgentAddress, MockSnmpAgent> mockAgents) {
            m_agents = mockAgents;
        }

        /* (non-Javadoc)
         * @see org.opennms.core.test.snmp.MockSnmpDataProvider#setDataForAddress(org.opennms.netmgt.snmp.SnmpAgentAddress, org.springframework.core.io.Resource)
         */
        @Override
        public void setDataForAddress(final SnmpAgentAddress address, final Resource resource) throws IOException {
            final MockSnmpAgent agent = m_agents.get(address);
            if (agent == null) {
                LOG.warn("Unable to set mock SNMP data for {}: no such agent", address);
                return;
            }
            agent.updateValuesFromResource(resource.getURL());
        }

        /* (non-Javadoc)
         * @see org.opennms.core.test.snmp.MockSnmpDataProvider#resetData()
         */
        @Override
        public void resetData() {
            for (final MockSnmpAgent agent : m_agents.values()) {
                try {
                    LOG.debug("Shutting down agent: {}", agent);
                    agent.shutDownAndWait();
                } catch (final InterruptedException e) {
                    LOG.debug("Unable to shut down agent {}", agent, e);
                    // Thread.currentThread().interrupt();
                }
            }
            m_agents.clear();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "MockSnmpAgentDataProvider[" + (m_agents == null ? "" : (m_agents.size() + " agents")) + "]";
        }

    }

}
