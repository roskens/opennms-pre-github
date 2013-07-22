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

package org.opennms.netmgt.poller.remote.support;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.OnmsLocationMonitor.MonitorStatus;
import org.opennms.netmgt.model.OnmsMonitoringLocationDefinition;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.DistributionContext;
import org.opennms.netmgt.poller.remote.ConfigurationChangedListener;
import org.opennms.netmgt.poller.remote.PollService;
import org.opennms.netmgt.poller.remote.PolledService;
import org.opennms.netmgt.poller.remote.PollerBackEnd;
import org.opennms.netmgt.poller.remote.PollerConfiguration;
import org.opennms.netmgt.poller.remote.PollerFrontEnd;
import org.opennms.netmgt.poller.remote.PollerSettings;
import org.opennms.netmgt.poller.remote.ServicePollState;
import org.opennms.netmgt.poller.remote.ServicePollStateChangedEvent;
import org.opennms.netmgt.poller.remote.ServicePollStateChangedListener;
import org.opennms.netmgt.poller.remote.TimeAdjustment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * DefaultPollerFrontEnd class.
 * </p>
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @version $Id: $
 */
public class DefaultPollerFrontEnd implements PollerFrontEnd, InitializingBean, DisposableBean {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPollerFrontEnd.class);

    /**
     * The Class Disconnected.
     */
    private class Disconnected extends RunningState {

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.State#isDisconnected()
         */
        @Override
        public boolean isDisconnected() {
            return true;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.RunningState#stop()
         */
        @Override
        public void stop() {
            // don't call do stop as we are disconnected from the server
            setState(new Registering());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.RunningState#onConfigChanged()
         */
        @Override
        protected void onConfigChanged() {
            doLoadConfig();
            setState(new Running());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.RunningState#onPaused()
         */
        @Override
        protected void onPaused() {
            doPause();
            setState(new Paused());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.RunningState#onStarted()
         */
        @Override
        protected void onStarted() {
            doLoadConfig();
            setState(new Running());
        }

    }

    /**
     * The Class Initial.
     */
    public class Initial extends State {

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.State#initialize()
         */
        @Override
        public void initialize() {
            try {
                final Integer monitorId = doInitialize();
                if (monitorId == null) {
                    setState(new Registering());
                } else if (doPollerStart()) {
                    setState(new Running());
                } else {
                    // the poller has been deleted
                    doDelete();
                    setState(new Registering());
                }
            } catch (final RuntimeException e) {
                setState(new FatalExceptionOccurred());

                // rethrow the exception on initialize so we exit if we fail to
                // initialize
                throw e;
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.State#isRegistered()
         */
        @Override
        public boolean isRegistered() {
            return false;
        }

    }

    /**
     * The Class Paused.
     */
    private class Paused extends RunningState {

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.RunningState#onConfigChanged()
         */
        @Override
        protected void onConfigChanged() {
            doLoadConfig();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.State#isPaused()
         */
        @Override
        public boolean isPaused() {
            return true;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.RunningState#onDisconnected()
         */
        @Override
        protected void onDisconnected() {
            doDisconnected();
            setState(new Disconnected());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.RunningState#onStarted()
         */
        @Override
        protected void onStarted() {
            doResume();
            setState(new Running());
        }

    }

    /**
     * The Class Registering.
     */
    private class Registering extends State {

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.State#isRegistered()
         */
        @Override
        public boolean isRegistered() {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.State#register(java.lang.String)
         */
        @Override
        public void register(final String location) {
            try {
                doRegister(location);
                setState(new Running());
            } catch (final Exception e) {
                LOG.warn("Unable to register.", e);
                setState(new Disconnected());
            }
        }
    }

    /**
     * The Class RunningState.
     */
    private class RunningState extends State {

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.State#pollService(java.lang.Integer)
         */
        @Override
        public void pollService(final Integer serviceId) {
            /* most running states do nothing here */
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.State#checkIn()
         */
        @Override
        public void checkIn() {
            try {
                final MonitorStatus status = doCheckIn();
                switch (status) {
                case CONFIG_CHANGED:
                    onConfigChanged();
                    break;
                case DELETED:
                    onDeleted();
                    break;
                case DISCONNECTED:
                    onDisconnected();
                    break;
                case PAUSED:
                    onPaused();
                    break;
                case STARTED:
                    onStarted();
                    break;
                default:
                    LOG.debug("Unhandled status on checkIn(): {}", status);
                    break;
                }
            } catch (final Exception e) {
                LOG.error("Unexpected exception occurred while checking in.", e);
                setState(new FatalExceptionOccurred());
            }
            final String killSwitchFileName = System.getProperty("opennms.poller.killSwitch.resource");
            if (!"".equals(killSwitchFileName) && killSwitchFileName != null) {
                final File killSwitch = new File(System.getProperty("opennms.poller.killSwitch.resource"));
                if (!killSwitch.exists()) {
                    LOG.info("Kill-switch file {} does not exist; stopping.", killSwitch.getPath());
                    doStop();
                }
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.State#isStarted()
         */
        @Override
        public boolean isStarted() {
            return true;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.State#stop()
         */
        @Override
        public void stop() {
            try {
                doStop();
                setState(new Registering());
            } catch (final Exception e) {
                LOG.error("Unexpected exception occurred while stopping.", e);
                setState(new FatalExceptionOccurred());
            }
        }

        /**
         * On config changed.
         */
        protected void onConfigChanged() {
            /* do nothing be default */
        }

        /**
         * On deleted.
         */
        protected void onDeleted() {
            doDelete();
            setState(new Registering());
        }

        /**
         * On disconnected.
         */
        protected void onDisconnected() {
            /* do nothing be default */
        }

        /**
         * On paused.
         */
        protected void onPaused() {
            /* do nothing be default */
        }

        /**
         * On started.
         */
        protected void onStarted() {
            /* do nothing be default */
        }

    }

    /**
     * The Class Running.
     */
    public class Running extends RunningState {

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.RunningState#pollService(java.lang.Integer)
         */
        @Override
        public void pollService(final Integer polledServiceId) {
            try {
                doPollService(polledServiceId);
            } catch (Throwable e) {
                LOG.error("Unexpected exception occurred while polling service ID {}.", polledServiceId, e);
                setState(new FatalExceptionOccurred());
            }

        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.RunningState#onConfigChanged()
         */
        @Override
        protected void onConfigChanged() {
            doLoadConfig();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.RunningState#onDisconnected()
         */
        @Override
        protected void onDisconnected() {
            doDisconnected();
            setState(new Disconnected());
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.RunningState#onPaused()
         */
        @Override
        protected void onPaused() {
            doPause();
            setState(new Paused());
        }

    }

    /**
     * The Class FatalExceptionOccurred.
     */
    public class FatalExceptionOccurred extends State {

        /* (non-Javadoc)
         * @see org.opennms.netmgt.poller.remote.support.DefaultPollerFrontEnd.State#isExitNecessary()
         */
        @Override
        public boolean isExitNecessary() {
            return true;
        }
    }

    /**
     * The Class State.
     */
    private abstract class State {

        /**
         * Check in.
         */
        public void checkIn() {
            // a pollerCheckingIn in any state that doesn't respond just does
            // nothing
        }

        /**
         * Illegal state.
         *
         * @param msg
         *            the msg
         * @return the illegal state exception
         */
        public IllegalStateException illegalState(final String msg) {
            return new IllegalStateException(msg + " State: " + this);
        }

        /**
         * Initialize.
         */
        public void initialize() {
            throw illegalState("Initialize called on invalid state.");
        }

        /**
         * Checks if is initialized.
         *
         * @return true, if is initialized
         */
        public boolean isInitialized() {
            return true;
        }

        /**
         * Checks if is registered.
         *
         * @return true, if is registered
         */
        public boolean isRegistered() {
            return true;
        }

        /**
         * Checks if is started.
         *
         * @return true, if is started
         */
        public boolean isStarted() {
            return false;
        }

        /**
         * Checks if is paused.
         *
         * @return true, if is paused
         */
        public boolean isPaused() {
            return false;
        }

        /**
         * Checks if is disconnected.
         *
         * @return true, if is disconnected
         */
        public boolean isDisconnected() {
            return false;
        }

        /**
         * Checks if is exit necessary.
         *
         * @return true, if is exit necessary
         */
        public boolean isExitNecessary() {
            return false;
        }

        /**
         * Poll service.
         *
         * @param serviceId
         *            the service id
         */
        public void pollService(final Integer serviceId) {
            throw illegalState("Cannot poll from this state.");
        }

        /**
         * Register.
         *
         * @param location
         *            the location
         */
        public void register(final String location) {
            throw illegalState("Cannot register from this state.");
        }

        /**
         * Stop.
         */
        public void stop() {
            // do nothing here by default as the actual exit is managed by the
            // external program
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return getClass().getSimpleName();
        }

    }

    /** The m_state. */
    private State m_state = new Initial();

    // injected dependencies
    /** The m_back end. */
    private PollerBackEnd m_backEnd;

    /** The m_poller settings. */
    private PollerSettings m_pollerSettings;

    /** The m_poll service. */
    private PollService m_pollService;

    /** The m_time adjustment. */
    private TimeAdjustment m_timeAdjustment;

    // listeners
    /** The m_property change listeners. */
    private LinkedList<PropertyChangeListener> m_propertyChangeListeners = new LinkedList<PropertyChangeListener>();

    /** The m_service poll state changed listeners. */
    private LinkedList<ServicePollStateChangedListener> m_servicePollStateChangedListeners = new LinkedList<ServicePollStateChangedListener>();

    /** The m_config change listeners. */
    private LinkedList<ConfigurationChangedListener> m_configChangeListeners = new LinkedList<ConfigurationChangedListener>();

    // current configuration
    /** The m_poller configuration. */
    private PollerConfiguration m_pollerConfiguration;

    // current state of polled services
    /** The m_poll state. */
    private Map<Integer, ServicePollState> m_pollState = new LinkedHashMap<Integer, ServicePollState>();

    /** {@inheritDoc} */
    @Override
    public void addConfigurationChangedListener(ConfigurationChangedListener l) {
        m_configChangeListeners.addFirst(l);
    }

    /**
     * <p>
     * doResume
     * </p>
     * .
     */
    public void doResume() {
        doLoadConfig();
    }

    /**
     * <p>
     * doPause
     * </p>
     * .
     */
    public void doPause() {
        // do I need to do anything here?
    }

    /**
     * <p>
     * doDisconnected
     * </p>
     * .
     */
    public void doDisconnected() {
        doLoadConfig();
    }

    /** {@inheritDoc} */
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        m_propertyChangeListeners.addFirst(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void addServicePollStateChangedListener(final ServicePollStateChangedListener listener) {
        m_servicePollStateChangedListeners.addFirst(listener);
    }

    /**
     * <p>
     * afterPropertiesSet
     * </p>
     * .
     *
     * @throws Exception
     *             the exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        assertNotNull(m_timeAdjustment, "timeAdjustment");
        m_state.initialize();
    }

    /**
     * <p>
     * checkConfig
     * </p>
     * .
     */
    public void checkConfig() {
        m_state.checkIn();
    }

    /**
     * <p>
     * destroy
     * </p>
     * .
     *
     * @throws Exception
     *             the exception
     */
    @Override
    public void destroy() throws Exception {
        stop();
    }

    /**
     * <p>
     * doCheckIn
     * </p>
     * .
     *
     * @return a
     *         {@link org.opennms.netmgt.model.OnmsLocationMonitor.MonitorStatus}
     *         object.
     */
    public MonitorStatus doCheckIn() {
        return m_backEnd.pollerCheckingIn(getMonitorId(), getCurrentConfigTimestamp());
    }

    /**
     * <p>
     * doDelete
     * </p>
     * .
     */
    public void doDelete() {
        setMonitorId(null);
    }

    /**
     * <p>
     * doInitialize
     * </p>
     * .
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer doInitialize() {
        assertNotNull(m_backEnd, "pollerBackEnd");
        assertNotNull(m_pollService, "pollService");
        assertNotNull(m_pollerSettings, "pollerSettings");

        return getMonitorId();
    }

    /**
     * <p>
     * doPollerStart
     * </p>
     * .
     *
     * @return a boolean.
     */
    public boolean doPollerStart() {

        if (!m_backEnd.pollerStarting(getMonitorId(), getDetails())) {
            // the monitor has been deleted on the server
            return false;
        }

        doLoadConfig();

        return true;

    }

    /**
     * <p>
     * doPollService
     * </p>
     * .
     *
     * @param polledServiceId
     *            a {@link java.lang.Integer} object.
     */
    public void doPollService(final Integer polledServiceId) {
        final PollStatus result = doPoll(polledServiceId);
        if (result == null) {
            return;
        }

        updateServicePollState(polledServiceId, result);

        m_backEnd.reportResult(getMonitorId(), polledServiceId, result);
    }

    /**
     * <p>
     * doRegister
     * </p>
     * .
     *
     * @param location
     *            a {@link java.lang.String} object.
     */
    public void doRegister(final String location) {

        int monitorId = m_backEnd.registerLocationMonitor(location);
        setMonitorId(monitorId);

        doPollerStart();

    }

    /**
     * <p>
     * doStop
     * </p>
     * .
     */
    public void doStop() {
        m_backEnd.pollerStopping(getMonitorId());
    }

    /**
     * <p>
     * getDetails
     * </p>
     * .
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> getDetails() {
        final HashMap<String, String> details = new HashMap<String, String>();
        final Properties p = System.getProperties();

        for (final Map.Entry<Object, Object> e : p.entrySet()) {
            if (e.getKey().toString().startsWith("os.") && e.getValue() != null) {
                details.put(e.getKey().toString(), e.getValue().toString());
            }
        }

        final InetAddress us = InetAddressUtils.getLocalHostAddress();
        details.put("org.opennms.netmgt.poller.remote.hostAddress", InetAddressUtils.str(us));
        details.put("org.opennms.netmgt.poller.remote.hostName", us.getHostName());

        return Collections.unmodifiableMap(details);
    }

    /**
     * <p>
     * getMonitorId
     * </p>
     * .
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getMonitorId() {
        return m_pollerSettings.getMonitorId();
    }

    /**
     * <p>
     * getMonitoringLocations
     * </p>
     * .
     *
     * @return a {@link java.util.Collection} object.
     */
    @Override
    public Collection<OnmsMonitoringLocationDefinition> getMonitoringLocations() {
        assertInitialized();
        return m_backEnd.getMonitoringLocations();
    }

    /**
     * <p>
     * getMonitorName
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getMonitorName() {
        return (isRegistered() ? m_backEnd.getMonitorName(getMonitorId()) : "");
    }

    /**
     * <p>
     * getPolledServices
     * </p>
     * .
     *
     * @return a {@link java.util.Collection} object.
     */
    @Override
    public Collection<PolledService> getPolledServices() {
        return Arrays.asList(m_pollerConfiguration.getPolledServices());
    }

    /**
     * <p>
     * getPollerPollState
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    @Override
    public List<ServicePollState> getPollerPollState() {
        synchronized (m_pollState) {
            return new LinkedList<ServicePollState>(m_pollState.values());
        }
    }

    /** {@inheritDoc} */
    @Override
    public ServicePollState getServicePollState(int polledServiceId) {
        synchronized (m_pollState) {
            return m_pollState.get(polledServiceId);
        }
    }

    /**
     * <p>
     * getStatus
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getStatus() {
        return m_state.toString();
    }

    /**
     * <p>
     * isRegistered
     * </p>
     * .
     *
     * @return a boolean.
     */
    @Override
    public boolean isRegistered() {
        return m_state.isRegistered();
    }

    /**
     * <p>
     * isStarted
     * </p>
     * .
     *
     * @return a boolean.
     */
    @Override
    public boolean isStarted() {
        return m_state.isStarted();
    }

    /** {@inheritDoc} */
    @Override
    public void pollService(final Integer polledServiceId) {
        m_state.pollService(polledServiceId);
    }

    /** {@inheritDoc} */
    @Override
    public void register(final String monitoringLocation) {
        m_state.register(monitoringLocation);
    }

    /** {@inheritDoc} */
    @Override
    public void removeConfigurationChangedListener(final ConfigurationChangedListener listener) {
        m_configChangeListeners.remove(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        m_propertyChangeListeners.remove(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void removeServicePollStateChangedListener(final ServicePollStateChangedListener listener) {
        m_servicePollStateChangedListeners.remove(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void setInitialPollTime(final Integer polledServiceId, final Date initialPollTime) {
        final ServicePollState pollState = getServicePollState(polledServiceId);
        if (pollState == null) {
            return;
        }

        pollState.setInitialPollTime(initialPollTime);
        fireServicePollStateChanged(pollState.getPolledService(), pollState.getIndex());
    }

    /**
     * <p>
     * setMonitorId
     * </p>
     * .
     *
     * @param monitorId
     *            a {@link java.lang.Integer} object.
     */
    public void setMonitorId(final Integer monitorId) {
        m_pollerSettings.setMonitorId(monitorId);
    }

    /**
     * <p>
     * setPollerBackEnd
     * </p>
     * .
     *
     * @param backEnd
     *            a {@link org.opennms.netmgt.poller.remote.PollerBackEnd}
     *            object.
     */
    public void setPollerBackEnd(final PollerBackEnd backEnd) {
        m_backEnd = backEnd;
    }

    /**
     * <p>
     * setPollerSettings
     * </p>
     * .
     *
     * @param settings
     *            a {@link org.opennms.netmgt.poller.remote.PollerSettings}
     *            object.
     */
    public void setPollerSettings(final PollerSettings settings) {
        m_pollerSettings = settings;
    }

    /**
     * Sets the time adjustment.
     *
     * @param timeAdjustment
     *            the timeAdjustment to set
     */
    public void setTimeAdjustment(TimeAdjustment timeAdjustment) {
        m_timeAdjustment = timeAdjustment;
    }

    /**
     * <p>
     * setPollService
     * </p>
     * .
     *
     * @param pollService
     *            a {@link org.opennms.netmgt.poller.remote.PollService} object.
     */
    public void setPollService(final PollService pollService) {
        m_pollService = pollService;
    }

    /**
     * <p>
     * stop
     * </p>
     * .
     */
    @Override
    public void stop() {
        m_state.stop();
    }

    /**
     * Assert initialized.
     */
    private void assertInitialized() {
        Assert.isTrue(isInitialized(), "afterProperties set has not been called");
    }

    /**
     * Assert not null.
     *
     * @param propertyValue
     *            the property value
     * @param propertyName
     *            the property name
     */
    private void assertNotNull(final Object propertyValue, final String propertyName) {
        Assert.state(propertyValue != null, propertyName + " must be set for instances of " + getClass());
    }

    /**
     * Assert registered.
     */
    @SuppressWarnings("unused")
    private void assertRegistered() {
        Assert.state(isRegistered(), "The poller must be registered before we can poll or get its configuration");
    }

    /**
     * Do load config.
     */
    private void doLoadConfig() {
        Date oldTime = getCurrentConfigTimestamp();

        try {
            m_pollService.setServiceMonitorLocators(m_backEnd.getServiceMonitorLocators(DistributionContext.REMOTE_MONITOR));
            m_pollerConfiguration = retrieveLatestConfiguration();

            synchronized (m_pollState) {

                int i = 0;
                m_pollState.clear();
                for (final PolledService service : getPolledServices()) {
                    m_pollService.initialize(service);
                    m_pollState.put(service.getServiceId(), new ServicePollState(service, i++));
                }
            }

            fireConfigurationChange(oldTime, getCurrentConfigTimestamp());
        } catch (final Exception e) {
            LOG.warn("Unable to get updated poller configuration.", e);
            if (m_pollerConfiguration == null) {
                m_pollerConfiguration = new EmptyPollerConfiguration();
            }
        }
    }

    /**
     * Retrieve latest configuration.
     *
     * @return the poller configuration
     */
    private PollerConfiguration retrieveLatestConfiguration() {
        PollerConfiguration config = m_backEnd.getPollerConfiguration(getMonitorId());
        m_timeAdjustment.setMasterTime(config.getServerTime());
        return config;
    }

    /**
     * Do poll.
     *
     * @param polledServiceId
     *            the polled service id
     * @return the poll status
     */
    private PollStatus doPoll(final Integer polledServiceId) {

        final PolledService polledService = getPolledService(polledServiceId);
        if (polledService == null) {
            return null;
        }
        final PollStatus result = m_pollService.poll(polledService);
        return result;
    }

    /**
     * Fire configuration change.
     *
     * @param oldTime
     *            the old time
     * @param newTime
     *            the new time
     */
    private void fireConfigurationChange(final Date oldTime, final Date newTime) {
        final PropertyChangeEvent e = new PropertyChangeEvent(this, "configuration", oldTime, newTime);
        for (final ConfigurationChangedListener listener : m_configChangeListeners) {
            listener.configurationChanged(e);
        }
    }

    /**
     * Fire property change.
     *
     * @param propertyName
     *            the property name
     * @param oldValue
     *            the old value
     * @param newValue
     *            the new value
     */
    private void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
        if (nullSafeEquals(oldValue, newValue)) {
            // no change no event
            return;

        }
        final PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);

        for (final PropertyChangeListener listener : m_propertyChangeListeners) {
            listener.propertyChange(event);
        }
    }

    /**
     * Null safe equals.
     *
     * @param oldValue
     *            the old value
     * @param newValue
     *            the new value
     * @return true, if successful
     */
    private boolean nullSafeEquals(final Object oldValue, final Object newValue) {
        return (oldValue == newValue ? true : ObjectUtils.nullSafeEquals(oldValue, newValue));
    }

    /**
     * Fire service poll state changed.
     *
     * @param polledService
     *            the polled service
     * @param index
     *            the index
     */
    private void fireServicePollStateChanged(final PolledService polledService, final int index) {
        final ServicePollStateChangedEvent event = new ServicePollStateChangedEvent(polledService, index);

        for (final ServicePollStateChangedListener listener : m_servicePollStateChangedListeners) {
            listener.pollStateChange(event);
        }
    }

    /**
     * Gets the current config timestamp.
     *
     * @return the current config timestamp
     */
    private Date getCurrentConfigTimestamp() {
        return (m_pollerConfiguration == null ? null : m_pollerConfiguration.getConfigurationTimestamp());
    }

    /**
     * Gets the polled service.
     *
     * @param polledServiceId
     *            the polled service id
     * @return the polled service
     */
    private PolledService getPolledService(final Integer polledServiceId) {
        final ServicePollState servicePollState = getServicePollState(polledServiceId);
        return (servicePollState == null ? null : servicePollState.getPolledService());
    }

    /**
     * Checks if is initialized.
     *
     * @return true, if is initialized
     */
    private boolean isInitialized() {
        return m_state.isInitialized();
    }

    /**
     * Sets the state.
     *
     * @param newState
     *            the new state
     */
    private void setState(final State newState) {
        final boolean started = isStarted();
        final boolean registered = isRegistered();
        final boolean paused = isPaused();
        final boolean disconnected = isDisconnected();
        final boolean exitNecessary = isExitNecessary();
        m_state = newState;
        firePropertyChange("exitNecessary", exitNecessary, isExitNecessary());
        firePropertyChange("started", started, isStarted());
        firePropertyChange("registered", registered, isRegistered());
        firePropertyChange("paused", paused, isPaused());
        firePropertyChange("disconnected", disconnected, isDisconnected());

    }

    /**
     * Checks if is disconnected.
     *
     * @return true, if is disconnected
     */
    private boolean isDisconnected() {
        return m_state.isDisconnected();
    }

    /**
     * Checks if is paused.
     *
     * @return true, if is paused
     */
    private boolean isPaused() {
        return m_state.isPaused();
    }

    /**
     * <p>
     * isExitNecessary
     * </p>
     * .
     *
     * @return a boolean.
     */
    @Override
    public boolean isExitNecessary() {
        return m_state.isExitNecessary();
    }

    /**
     * Update service poll state.
     *
     * @param polledServiceId
     *            the polled service id
     * @param result
     *            the result
     */
    private void updateServicePollState(final Integer polledServiceId, final PollStatus result) {
        final ServicePollState pollState = getServicePollState(polledServiceId);
        if (pollState == null) {
            return;
        }
        pollState.setLastPoll(result);
        fireServicePollStateChanged(pollState.getPolledService(), pollState.getIndex());
    }

}
