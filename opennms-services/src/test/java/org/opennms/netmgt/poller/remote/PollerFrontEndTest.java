/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.poller.remote;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.springframework.util.ObjectUtils.nullSafeEquals;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.opennms.netmgt.config.DefaultServiceMonitorLocator;
import org.opennms.netmgt.model.OnmsLocationMonitor.MonitorStatus;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.DistributionContext;
import org.opennms.netmgt.poller.ServiceMonitorLocator;
import org.opennms.netmgt.poller.monitors.HttpMonitor;
import org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd;
import org.opennms.test.mock.EasyMockUtils;

/**
 * The Class PollerFrontEndTest.
 */
public class PollerFrontEndTest extends TestCase {

    /**
     * The Class PolledServiceChangeEventEquals.
     */
    public static class PolledServiceChangeEventEquals implements IArgumentMatcher {

        /** The m_expected. */
        private ServicePollStateChangedEvent m_expected;

        /**
         * Instantiates a new polled service change event equals.
         *
         * @param value
         *            the value
         */
        PolledServiceChangeEventEquals(ServicePollStateChangedEvent value) {
            m_expected = value;
        }

        /* (non-Javadoc)
         * @see org.easymock.IArgumentMatcher#appendTo(java.lang.StringBuffer)
         */
        @Override
        public void appendTo(StringBuffer buffer) {
            buffer.append(m_expected);
        }

        /* (non-Javadoc)
         * @see org.easymock.IArgumentMatcher#matches(java.lang.Object)
         */
        @Override
        public boolean matches(Object argument) {
            ServicePollStateChangedEvent actual = (ServicePollStateChangedEvent) argument;
            if (m_expected == null) {
                return actual == null;
            }

            return (m_expected.getSource() == actual.getSource() && m_expected.getIndex() == actual.getIndex());
        }

    }

    /**
     * The Class PropertyChangeEventEquals.
     */
    public static class PropertyChangeEventEquals implements IArgumentMatcher {

        /** The m_expected. */
        private PropertyChangeEvent m_expected;

        /**
         * Instantiates a new property change event equals.
         *
         * @param value
         *            the value
         */
        PropertyChangeEventEquals(PropertyChangeEvent value) {
            m_expected = value;
        }

        /* (non-Javadoc)
         * @see org.easymock.IArgumentMatcher#appendTo(java.lang.StringBuffer)
         */
        @Override
        public void appendTo(StringBuffer buffer) {
            buffer.append(m_expected);
            buffer.append(" property=");
            buffer.append(m_expected.getPropertyName());
            buffer.append(", oldValue=");
            buffer.append(m_expected.getOldValue());
            buffer.append(", newValue=");
            buffer.append(m_expected.getNewValue());

        }

        /* (non-Javadoc)
         * @see org.easymock.IArgumentMatcher#matches(java.lang.Object)
         */
        @Override
        public boolean matches(Object argument) {
            PropertyChangeEvent actual = (PropertyChangeEvent) argument;
            if (m_expected == actual)
                return true;

            if (m_expected == null) {
                return actual == null;
            }

            return (m_expected.getSource() == actual.getSource()
                    && m_expected.getPropertyName().equals(actual.getPropertyName())
                    && nullSafeEquals(m_expected.getOldValue(), actual.getOldValue()) && nullSafeEquals(m_expected.getNewValue(),
                                                                                                        actual.getNewValue()));
        }

    }

    /** The m_back end. */
    private PollerBackEnd m_backEnd;

    /** The m_config change listener. */
    private ConfigurationChangedListener m_configChangeListener;

    /** The m_front end. */
    private DefaultPollerFrontEnd m_frontEnd;

    /** The m_mock. */
    private EasyMockUtils m_mock = new EasyMockUtils();

    /** The m_old poller configuration. */
    private DemoPollerConfiguration m_oldPollerConfiguration;

    /** The m_polled service listener. */
    private ServicePollStateChangedListener m_polledServiceListener;

    /** The m_poller configuration. */
    private DemoPollerConfiguration m_pollerConfiguration;

    /** The m_poll service. */
    private PollService m_pollService;

    /** The m_registered id. */
    private Integer m_registeredId;

    /** The m_registration listener. */
    private PropertyChangeListener m_registrationListener;

    /** The m_service status. */
    private PollStatus m_serviceStatus;

    /** The m_settings. */
    private PollerSettings m_settings;

    /** The m_old status. */
    private MonitorStatus m_oldStatus = MonitorStatus.STARTED;

    /** The m_monitor status. */
    private MonitorStatus m_monitorStatus = MonitorStatus.CONFIG_CHANGED;

    /**
     * Test after properties set when not registered.
     *
     * @throws Exception
     *             the exception
     */
    public void testAfterPropertiesSetWhenNotRegistered() throws Exception {
        testAfterPropertiesSetWithRegisteredId(null);
    }

    /**
     * Test after properties set when registered.
     *
     * @throws Exception
     *             the exception
     */
    public void testAfterPropertiesSetWhenRegistered() throws Exception {
        testAfterPropertiesSetWithRegisteredId(1);
    }

    /**
     * Test already registered.
     *
     * @throws Exception
     *             the exception
     */
    public void testAlreadyRegistered() throws Exception {

        setRegistered();

        anticipateAfterPropertiesSet();

        m_mock.replayAll();

        m_frontEnd.afterPropertiesSet();

        assertTrue(m_frontEnd.isRegistered());

        m_mock.verifyAll();
    }

    /**
     * Test config check.
     *
     * @throws Exception
     *             the exception
     */
    public void testConfigCheck() throws Exception {

        setRegistered();

        anticipateAfterPropertiesSet();

        anticipateCheckConfig();

        m_mock.replayAll();

        m_frontEnd.afterPropertiesSet();

        m_frontEnd.checkConfig();

        m_mock.verifyAll();
    }

    /**
     * Test details.
     */
    public void testDetails() {
        Map<String, String> details = m_frontEnd.getDetails();
        assertPropertyEquals("os.name", details);
        assertPropertyEquals("os.version", details);
    }

    /**
     * Test is registered.
     *
     * @throws Exception
     *             the exception
     */
    public void testIsRegistered() throws Exception {
        setRegistered();

        anticipateAfterPropertiesSet();

        m_mock.replayAll();

        m_frontEnd.afterPropertiesSet();

        assertTrue(m_frontEnd.isRegistered());

        m_mock.verifyAll();
    }

    /**
     * Test not yet registered.
     *
     * @throws Exception
     *             the exception
     */
    public void testNotYetRegistered() throws Exception {
        setRegisteredId(null);

        anticipateAfterPropertiesSet();

        m_mock.replayAll();

        m_frontEnd.afterPropertiesSet();

        assertFalse(m_frontEnd.isRegistered());

        m_mock.verifyAll();
    }

    /**
     * Test poll.
     *
     * @throws Exception
     *             the exception
     */
    public void testPoll() throws Exception {

        setRegistered();

        anticipateAfterPropertiesSet();

        anticipatePollService();

        anticipateGetServicePollState();

        m_mock.replayAll();

        m_frontEnd.afterPropertiesSet();

        m_frontEnd.pollService(pollConfig().getFirstId());

        ServicePollState pollState = m_frontEnd.getServicePollState(pollConfig().getFirstId());

        m_mock.verifyAll();

        assertEquals(PollStatus.SERVICE_AVAILABLE, pollState.getLastPoll().getStatusCode());

    }

    /**
     * Test register new monitor.
     *
     * @throws Exception
     *             the exception
     */
    public void testRegisterNewMonitor() throws Exception {

        anticipateAfterPropertiesSet();

        anticiapateRegister();

        m_mock.replayAll();

        m_frontEnd.afterPropertiesSet();

        assertFalse(m_frontEnd.isRegistered());

        m_frontEnd.register("OAK");

        assertTrue(m_frontEnd.isRegistered());

        m_mock.verifyAll();

    }

    /**
     * Test set initial poll time.
     *
     * @throws Exception
     *             the exception
     */
    public void testSetInitialPollTime() throws Exception {

        Date start = new Date(1200000000000L);

        setRegistered();

        anticipateAfterPropertiesSet();

        int polledServiceId = pollConfig().getFirstId();

        anticipateSetInitialPollTime();

        anticipateGetServicePollState();

        // expect(m_settings.getMonitorId()).andReturn(1).atLeastOnce();

        // anticipateNewConfig(pollConfig());

        // expect(m_backEnd.pollerStarting(1,
        // getPollerDetails())).andReturn(true);

        m_mock.replayAll();

        m_frontEnd.afterPropertiesSet();

        m_frontEnd.setInitialPollTime(polledServiceId, start);

        assertEquals(start, m_frontEnd.getServicePollState(polledServiceId).getNextPollTime());

        m_mock.verifyAll();
    }

    /**
     * Test stop.
     *
     * @throws Exception
     *             the exception
     */
    public void testStop() throws Exception {

        setRegistered();

        anticipateAfterPropertiesSet();

        anticipateStop();

        m_mock.replayAll();

        m_frontEnd.afterPropertiesSet();

        assertTrue(m_frontEnd.isStarted());

        m_frontEnd.stop();

        assertFalse(m_frontEnd.isStarted());

        m_mock.verifyAll();
    }

    /**
     * Test pause.
     *
     * @throws Exception
     *             the exception
     */
    public void testPause() throws Exception {
        setRegistered();

        anticipateAfterPropertiesSet();

        setMonitorStatus(MonitorStatus.PAUSED);

        anticipateCheckConfig();

        setMonitorStatus(MonitorStatus.STARTED);

        anticipateCheckConfig();

        m_mock.replayAll();

        m_frontEnd.afterPropertiesSet();

        m_frontEnd.checkConfig();

        m_frontEnd.checkConfig();

        m_mock.verifyAll();
    }

    /**
     * Test disconnect.
     *
     * @throws Exception
     *             the exception
     */
    public void testDisconnect() throws Exception {
        setRegistered();

        anticipateAfterPropertiesSet();

        setMonitorStatus(MonitorStatus.DISCONNECTED);

        anticipateCheckConfig();

        setMonitorStatus(MonitorStatus.STARTED);

        anticipateCheckConfig();

        m_mock.replayAll();

        m_frontEnd.afterPropertiesSet();

        m_frontEnd.checkConfig();

        m_frontEnd.checkConfig();

        m_mock.verifyAll();
    }

    /**
     * Sets the monitor status.
     *
     * @param status
     *            the new monitor status
     */
    private void setMonitorStatus(MonitorStatus status) {
        m_oldStatus = m_monitorStatus;
        m_monitorStatus = status;
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        m_backEnd = m_mock.createMock(PollerBackEnd.class);
        m_settings = m_mock.createMock(PollerSettings.class);
        m_pollService = m_mock.createMock(PollService.class);
        m_registrationListener = m_mock.createMock(PropertyChangeListener.class);
        m_polledServiceListener = m_mock.createMock(ServicePollStateChangedListener.class);
        m_configChangeListener = m_mock.createMock(ConfigurationChangedListener.class);

        setPollConfig(new DemoPollerConfiguration());
        m_oldPollerConfiguration = null;

        m_frontEnd = new DefaultPollerFrontEnd();

        m_frontEnd.setTimeAdjustment(new DefaultTimeAdjustment());

        // ServerUnreachableAdaptor adaptor = new ServerUnreachableAdaptor();
        // adaptor.setRemoteBackEnd(m_backEnd);
        // m_frontEnd.setPollerBackEnd(adaptor);

        m_frontEnd.setPollerBackEnd(m_backEnd);

        m_frontEnd.setPollerSettings(m_settings);
        m_frontEnd.setPollService(m_pollService);

        m_frontEnd.addConfigurationChangedListener(m_configChangeListener);
        m_frontEnd.addPropertyChangeListener(m_registrationListener);
        m_frontEnd.addServicePollStateChangedListener(m_polledServiceListener);

        m_serviceStatus = PollStatus.available(1234.0);

    }

    /**
     * Anticiapate register.
     */
    private void anticiapateRegister() {

        anticipateRegisterLocationMonitor();

        anticipateDoPollerStart();

    }

    /**
     * Anticipate after properties set.
     */
    private void anticipateAfterPropertiesSet() {
        anticipateGetMonitorId();

        if (getRegisteredId() == null)
            return;

        anticipateDoPollerStart();

    }

    /**
     * Anticipate do poller start.
     */
    private void anticipateDoPollerStart() {
        anticipateGetMonitorId();
        anticipatePollerStarting();
        anticipateDoLoadConfig();
        anticipateFirePropertyChangeEvent("registered", false, true);
        anticipateFirePropertyChangeEvent("started", false, true);

    }

    /**
     * Anticipate do load config.
     */
    private void anticipateDoLoadConfig() {
        anticipatePollServiceSetMonitorLocators();
        anticipateGetMonitorId();
        anticipateGetConfiguration();
        anticipatePolledServicesInitialized();
        anticipateFireConfigurationChangeEvent();
    }

    /**
     * Anticipate poller starting.
     */
    private void anticipatePollerStarting() {
        expect(m_backEnd.pollerStarting(getRegisteredId(), getPollerDetails())).andReturn(true);
    }

    /**
     * Anticipate check config.
     */
    private void anticipateCheckConfig() {
        anticipateDoCheckIn();
    }

    /**
     * Anticipate do check in.
     */
    private void anticipateDoCheckIn() {
        anticipateGetMonitorId();
        anticipatePollerCheckingIn();
        switch (m_monitorStatus) {
        case CONFIG_CHANGED:
            anticipateDoLoadConfig();
            break;
        case PAUSED:
            anticipateDoPause();
            break;
        case DISCONNECTED:
            anticipateDoDisconnect();
            break;
        case STARTED:
            if (m_oldStatus == MonitorStatus.PAUSED) {
                anticipateDoResume();
            } else if (m_oldStatus == MonitorStatus.DISCONNECTED) {
                anticipateReconnect();
            }
            break;
        }
    }

    /**
     * Anticipate reconnect.
     */
    private void anticipateReconnect() {
        anticipateDoLoadConfig();
        anticipateFirePropertyChangeEvent("disconnected", true, false);
    }

    /**
     * Anticipate do disconnect.
     */
    private void anticipateDoDisconnect() {
        anticipateDoLoadConfig();
        anticipateFirePropertyChangeEvent("disconnected", false, true);
    }

    /**
     * Anticipate do resume.
     */
    private void anticipateDoResume() {
        anticipateDoLoadConfig();
        anticipateFirePropertyChangeEvent("paused", true, false);
    }

    /**
     * Anticipate do pause.
     */
    private void anticipateDoPause() {
        anticipateFirePropertyChangeEvent("paused", false, true);
    }

    /**
     * Anticipate do poll.
     */
    private void anticipateDoPoll() {
        anticipateGetPolledService();

        expect(m_pollService.poll(pollConfig().getFirstService())).andReturn(m_serviceStatus);

    }

    /**
     * Anticipate fire configuration change event.
     */
    private void anticipateFireConfigurationChangeEvent() {
        PropertyChangeEvent e = new PropertyChangeEvent(m_frontEnd, "configuration", (oldConfig() == null ? null
            : oldConfig().getConfigurationTimestamp()), (pollConfig() == null ? null
            : pollConfig().getConfigurationTimestamp()));
        m_configChangeListener.configurationChanged(eq(e));
    }

    /**
     * Anticipate fire property change event.
     *
     * @param property
     *            the property
     * @param oldValue
     *            the old value
     * @param newValue
     *            the new value
     */
    private void anticipateFirePropertyChangeEvent(String property, Object oldValue, Object newValue) {
        PropertyChangeEvent e = new PropertyChangeEvent(m_frontEnd, property, oldValue, newValue);
        m_registrationListener.propertyChange(eq(e));
    }

    /**
     * Anticipate fire service poll state changed.
     */
    private void anticipateFireServicePollStateChanged() {
        ServicePollStateChangedEvent e = new ServicePollStateChangedEvent(pollConfig().getFirstService(), 0);
        m_polledServiceListener.pollStateChange(eq(e));
    }

    /**
     * Anticipate get configuration.
     */
    private void anticipateGetConfiguration() {
        expect(m_backEnd.getPollerConfiguration(1)).andReturn(pollConfig());
    }

    /**
     * Anticipate get monitor id.
     */
    private void anticipateGetMonitorId() {
        expect(m_settings.getMonitorId()).andReturn(getRegisteredId());
    }

    /**
     * Anticipate get polled service.
     */
    private void anticipateGetPolledService() {
        anticipateGetServicePollState();
    }

    /**
     * Anticipate get service poll state.
     */
    private void anticipateGetServicePollState() {
    }

    /**
     * Anticipate polled services initialized.
     */
    private void anticipatePolledServicesInitialized() {
        m_pollService.initialize(isA(PolledService.class));
        expectLastCall().times(pollConfig().getPolledServices().length);
    }

    /**
     * Anticipate poller checking in.
     */
    private void anticipatePollerCheckingIn() {

        Date oldTimestamp = pollConfig().getConfigurationTimestamp();
        switch (m_monitorStatus) {
        case CONFIG_CHANGED:
            setPollConfig(new DemoPollerConfiguration());
            break;
        case DISCONNECTED:
            setPollConfig(new DemoPollerConfiguration(new Date(0)));
            break;
        case STARTED:
            if (m_oldStatus != MonitorStatus.STARTED) {
                setPollConfig(new DemoPollerConfiguration());
            }
        }

        expect(m_backEnd.pollerCheckingIn(1, oldTimestamp)).andReturn(m_monitorStatus);

    }

    /**
     * Anticipate poller stopping.
     */
    private void anticipatePollerStopping() {
        m_backEnd.pollerStopping(getRegisteredId());
    }

    /**
     * Anticipate poll service.
     */
    private void anticipatePollService() {
        anticipateDoPoll();

        anticipateUpdateServicePollState();

        anticipateGetMonitorId();

        anticipateReportResult();
    }

    /**
     * Anticipate poll service set monitor locators.
     */
    private void anticipatePollServiceSetMonitorLocators() {
        ServiceMonitorLocator locator = new DefaultServiceMonitorLocator("HTTP", HttpMonitor.class);
        Set<ServiceMonitorLocator> locators = Collections.singleton(locator);
        expect(m_backEnd.getServiceMonitorLocators(DistributionContext.REMOTE_MONITOR)).andReturn(locators);
        m_pollService.setServiceMonitorLocators(locators);
    }

    /**
     * Anticipate register location monitor.
     */
    private void anticipateRegisterLocationMonitor() {
        setRegistered();
        expect(m_backEnd.registerLocationMonitor("OAK")).andReturn(getRegisteredId());
        m_settings.setMonitorId(getRegisteredId());
    }

    /**
     * Anticipate report result.
     */
    private void anticipateReportResult() {
        m_backEnd.reportResult(getRegisteredId(), pollConfig().getFirstId(), m_serviceStatus);
    }

    /**
     * Anticipate set initial poll time.
     */
    private void anticipateSetInitialPollTime() {
        anticipateGetServicePollState();
        anticipateFireServicePollStateChanged();
    }

    /**
     * Anticipate stop.
     */
    private void anticipateStop() {
        anticipateGetMonitorId();
        anticipatePollerStopping();
        anticipateFirePropertyChangeEvent("registered", true, false);
        anticipateFirePropertyChangeEvent("started", true, false);
    }

    /**
     * Anticipate update service poll state.
     */
    private void anticipateUpdateServicePollState() {
        anticipateGetServicePollState();
        anticipateFireServicePollStateChanged();
    }

    /**
     * Assert property equals.
     *
     * @param propertyName
     *            the property name
     * @param details
     *            the details
     */
    private void assertPropertyEquals(String propertyName, Map<String, String> details) {
        assertNotNull("has " + propertyName, details.get(propertyName));
        assertEquals(propertyName, System.getProperty(propertyName), details.get(propertyName));
    }

    /**
     * Eq.
     *
     * @param e
     *            the e
     * @return the property change event
     */
    private PropertyChangeEvent eq(PropertyChangeEvent e) {
        EasyMock.reportMatcher(new PropertyChangeEventEquals(e));
        return null;
    }

    /**
     * Eq.
     *
     * @param e
     *            the e
     * @return the service poll state changed event
     */
    private ServicePollStateChangedEvent eq(ServicePollStateChangedEvent e) {
        EasyMock.reportMatcher(new PolledServiceChangeEventEquals(e));
        return null;

    }

    /**
     * Gets the poller details.
     *
     * @return the poller details
     */
    private Map<String, String> getPollerDetails() {
        return m_frontEnd.getDetails();
    }

    /**
     * Gets the registered id.
     *
     * @return the registered id
     */
    private Integer getRegisteredId() {
        return m_registeredId;
    }

    /**
     * Old config.
     *
     * @return the demo poller configuration
     */
    private DemoPollerConfiguration oldConfig() {
        return m_oldPollerConfiguration;
    }

    /**
     * Poll config.
     *
     * @return the demo poller configuration
     */
    private DemoPollerConfiguration pollConfig() {
        return m_pollerConfiguration;
    }

    /**
     * Sets the poll config.
     *
     * @param pollerConfiguration
     *            the new poll config
     */
    private void setPollConfig(DemoPollerConfiguration pollerConfiguration) {
        m_oldPollerConfiguration = pollConfig();
        m_pollerConfiguration = pollerConfiguration;
    }

    /**
     * Sets the registered.
     */
    private void setRegistered() {
        setRegisteredId(1);
    }

    /**
     * Sets the registered id.
     *
     * @param registeredId
     *            the new registered id
     */
    private void setRegisteredId(Integer registeredId) {
        m_registeredId = registeredId;
    }

    /**
     * Test after properties set with registered id.
     *
     * @param registeredId
     *            the registered id
     * @throws Exception
     *             the exception
     */
    private void testAfterPropertiesSetWithRegisteredId(Integer registeredId) throws Exception {
        setRegisteredId(registeredId);

        anticipateAfterPropertiesSet();

        m_mock.replayAll();

        m_frontEnd.afterPropertiesSet();

        m_mock.verifyAll();
    }

}
