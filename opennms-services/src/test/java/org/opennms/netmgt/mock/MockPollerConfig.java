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

package org.opennms.netmgt.mock;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.config.BasicScheduleUtils;
import org.opennms.netmgt.config.PollOutagesConfigManager;
import org.opennms.netmgt.config.PollerConfig;
import org.opennms.netmgt.config.poller.Downtime;
import org.opennms.netmgt.config.poller.Interface;
import org.opennms.netmgt.config.poller.Node;
import org.opennms.netmgt.config.poller.Outage;
import org.opennms.netmgt.config.poller.Package;
import org.opennms.netmgt.config.poller.Parameter;
import org.opennms.netmgt.config.poller.PollerConfiguration;
import org.opennms.netmgt.config.poller.Service;
import org.opennms.netmgt.config.poller.Time;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.ServiceSelector;
import org.opennms.netmgt.poller.DistributionContext;
import org.opennms.netmgt.poller.ServiceMonitor;
import org.opennms.netmgt.poller.ServiceMonitorLocator;
import org.springframework.core.io.ByteArrayResource;

/**
 * The Class MockPollerConfig.
 */
public class MockPollerConfig extends PollOutagesConfigManager implements PollerConfig {

    /** The m_critical svc name. */
    private String m_criticalSvcName;

    /** The m_current pkg. */
    private Package m_currentPkg = new Package();

    /** The m_outage processing enabled. */
    private boolean m_outageProcessingEnabled = false;

    /** The m_pkgs. */
    private Vector<Package> m_pkgs = new Vector<Package>();

    /** The m_svc monitors. */
    private Map<String, ServiceMonitor> m_svcMonitors = new TreeMap<String, ServiceMonitor>();

    /** The m_threads. */
    private int m_threads = 1;

    /** The m_default poll interval. */
    private long m_defaultPollInterval = 7654L;

    /** The m_poll all. */
    private boolean m_pollAll = true;

    /** The m_service unresponsive enabled. */
    private boolean m_serviceUnresponsiveEnabled = false;

    /** The m_next outage id sql. */
    private String m_nextOutageIdSql;

    /** The m_current svc. */
    private Service m_currentSvc;

    /** The m_network. */
    private MockNetwork m_network;

    /**
     * Instantiates a new mock poller config.
     *
     * @param network
     *            the network
     */
    public MockPollerConfig(final MockNetwork network) {
        m_network = network;
        setConfigResource(new ByteArrayResource("<outages></outages>".getBytes()));
        afterPropertiesSet();
    }

    /**
     * <p>
     * parameters
     * </p>
     * .
     *
     * @param svc
     *            a {@link org.opennms.netmgt.config.poller.Service} object.
     * @return a {@link java.lang.Iterable} object.
     */
    @Override
    public Iterable<Parameter> parameters(final Service svc) {
        getReadLock().lock();
        try {
            return svc.getParameterCollection();
        } finally {
            getReadLock().unlock();
        }
    }

    /**
     * Adds the downtime.
     *
     * @param interval
     *            the interval
     * @param begin
     *            the begin
     * @param end
     *            the end
     * @param delete
     *            the delete
     */
    public void addDowntime(long interval, long begin, long end, boolean delete) {
        Downtime downtime = new Downtime();
        downtime.setDelete(delete ? "true" : "false");
        downtime.setBegin(begin);
        downtime.setInterval(interval);
        if (end >= 0)
            downtime.setEnd(end);
        m_currentPkg.addDowntime(downtime);
    }

    /**
     * Adds a scehduled outage to pkg from begin to end, for the nodeid.
     *
     * @param pkg
     *            - the package to which
     * @param outageName
     *            - a name, arbitrary
     * @param begin
     *            - time, in seconds since epoch, when the outage starts
     * @param end
     *            - time, in seconds since the epoch, when the outage ends
     * @param nodeid
     *            - the node the outage applies to
     */
    public void addScheduledOutage(Package pkg, String outageName, long begin, long end, int nodeid) {

        Outage outage = new Outage();
        outage.setName(outageName);

        Node node = new Node();
        node.setId(nodeid);
        outage.addNode(node);

        Time time = new Time();
        Date beginDate = new Date(begin);
        Date endDate = new Date(end);
        time.setBegins(new SimpleDateFormat(BasicScheduleUtils.FORMAT1).format(beginDate));
        time.setEnds(new SimpleDateFormat(BasicScheduleUtils.FORMAT1).format(endDate));

        outage.addTime(time);

        getConfig().addOutage(outage);

        pkg.addOutageCalendar(outageName);
    }

    /**
     * Adds a scehduled outage from begin to end, for the nodeid.
     *
     * @param outageName
     *            - a name, arbitrary
     * @param begin
     *            - time, in seconds since epoch, when the outage starts
     * @param end
     *            - time, in seconds since the epoch, when the outage ends
     * @param nodeid
     *            - the node the outage applies to
     */
    public void addScheduledOutage(String outageName, long begin, long end, int nodeid) {
        addScheduledOutage(m_currentPkg, outageName, begin, end, nodeid);
    }

    /**
     * Adds the scheduled outage.
     *
     * @param pkg
     *            the pkg
     * @param outageName
     *            the outage name
     * @param begin
     *            the begin
     * @param end
     *            the end
     * @param ipAddr
     *            the ip addr
     */
    public void addScheduledOutage(Package pkg, String outageName, long begin, long end, String ipAddr) {
        Outage outage = new Outage();
        outage.setName(outageName);

        Interface iface = new Interface();
        iface.setAddress(ipAddr);

        outage.addInterface(iface);

        Time time = new Time();
        Date beginDate = new Date(begin);
        Date endDate = new Date(end);
        time.setBegins(new SimpleDateFormat(BasicScheduleUtils.FORMAT1).format(beginDate));
        time.setEnds(new SimpleDateFormat(BasicScheduleUtils.FORMAT1).format(endDate));

        outage.addTime(time);

        getConfig().addOutage(outage);

        pkg.addOutageCalendar(outageName);

    }

    /**
     * Adds the scheduled outage.
     *
     * @param outageName
     *            the outage name
     * @param begin
     *            the begin
     * @param end
     *            the end
     * @param ipAddr
     *            the ip addr
     */
    public void addScheduledOutage(String outageName, long begin, long end, String ipAddr) {
        addScheduledOutage(m_currentPkg, outageName, begin, end, ipAddr);
    }

    /**
     * Adds the scheduled outage.
     *
     * @param pkg
     *            the pkg
     * @param outageName
     *            the outage name
     * @param dayOfWeek
     *            the day of week
     * @param beginTime
     *            the begin time
     * @param endTime
     *            the end time
     * @param ipAddr
     *            the ip addr
     */
    public void addScheduledOutage(Package pkg, String outageName, String dayOfWeek, String beginTime, String endTime,
            String ipAddr) {
        Outage outage = new Outage();
        outage.setName(outageName);
        outage.setType("weekly");

        Interface iface = new Interface();
        iface.setAddress(ipAddr);

        outage.addInterface(iface);

        Time time = new Time();
        time.setDay(dayOfWeek);
        time.setBegins(beginTime);
        time.setEnds(endTime);

        outage.addTime(time);

        getConfig().addOutage(outage);

        pkg.addOutageCalendar(outageName);
    }

    /**
     * Adds the scheduled outage.
     *
     * @param outageName
     *            the outage name
     * @param dayOfWeek
     *            the day of week
     * @param beginTime
     *            the begin time
     * @param endTime
     *            the end time
     * @param ipAddr
     *            the ip addr
     */
    public void addScheduledOutage(String outageName, String dayOfWeek, String beginTime, String endTime, String ipAddr) {
        addScheduledOutage(m_currentPkg, outageName, dayOfWeek, beginTime, endTime, ipAddr);
    }

    /**
     * Adds the service.
     *
     * @param name
     *            the name
     * @param monitor
     *            the monitor
     */
    public void addService(String name, ServiceMonitor monitor) {
        addService(name, m_defaultPollInterval, monitor);
    }

    /**
     * Adds the service.
     *
     * @param name
     *            the name
     * @param interval
     *            the interval
     * @param monitor
     *            the monitor
     */
    public void addService(String name, long interval, ServiceMonitor monitor) {
        Service service = findService(m_currentPkg, name);
        if (service == null) {
            service = new Service();
            service.setName(name);
            service.setInterval(interval);
            m_currentPkg.addService(service);
            m_currentSvc = service;
        }
        addServiceMonitor(name, monitor);
    }

    /**
     * Adds the service monitor.
     *
     * @param name
     *            the name
     * @param monitor
     *            the monitor
     */
    private void addServiceMonitor(String name, ServiceMonitor monitor) {
        if (!hasServiceMonitor(name))
            m_svcMonitors.put(name, monitor);
    }

    /**
     * Adds the service.
     *
     * @param svc
     *            the svc
     */
    public void addService(MockService svc) {
        addService(svc.getSvcName(), m_defaultPollInterval, new MockMonitor(svc.getNetwork(), svc.getSvcName()));
        m_currentPkg.addSpecific(svc.getIpAddr());
    }

    /**
     * Clear downtime.
     */
    public void clearDowntime() {
        m_currentPkg.removeAllDowntime();
    }

    /**
     * Adds the package.
     *
     * @param name
     *            the name
     */
    public void addPackage(String name) {
        m_currentPkg = new Package();
        m_currentPkg.setName(name);

        m_pkgs.add(m_currentPkg);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#addMonitor(java.lang.String, java.lang.String)
     */
    @Override
    public void addMonitor(String svcName, String className) {
        addServiceMonitor(svcName, new MockMonitor(m_network, svcName));

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#enumeratePackage()
     */
    @Override
    public Enumeration<Package> enumeratePackage() {
        return m_pkgs.elements();
    }

    /**
     * Find service.
     *
     * @param pkg
     *            the pkg
     * @param svcName
     *            the svc name
     * @return the service
     */
    private Service findService(Package pkg, String svcName) {
        for (Service svc : pkg.getServiceCollection()) {
            if (svcName.equals(svc.getName())) {
                return svc;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getCriticalService()
     */
    @Override
    public String getCriticalService() {
        return m_criticalSvcName;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getFirstPackageMatch(java.lang.String)
     */
    @Override
    public Package getFirstPackageMatch(String ipaddr) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getNextOutageIdSql()
     */
    @Override
    public String getNextOutageIdSql() {
        return m_nextOutageIdSql;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getPackage(java.lang.String)
     */
    @Override
    public Package getPackage(String name) {
        for (Package pkg : m_pkgs) {
            if (pkg.getName().equals(name)) {
                return pkg;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getRRAList(org.opennms.netmgt.config.poller.Package)
     */
    @Override
    public List<String> getRRAList(Package pkg) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getServiceMonitor(java.lang.String)
     */
    @Override
    public ServiceMonitor getServiceMonitor(String svcName) {
        return getServiceMonitors().get(svcName);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getServiceMonitors()
     */
    @Override
    public Map<String, ServiceMonitor> getServiceMonitors() {
        return m_svcMonitors;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getStep(org.opennms.netmgt.config.poller.Package)
     */
    @Override
    public int getStep(Package pkg) {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getThreads()
     */
    @Override
    public int getThreads() {
        return m_threads;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#shouldNotifyXmlrpc()
     */
    @Override
    public boolean shouldNotifyXmlrpc() {
        return false;
    }

    /**
     * Checks for service monitor.
     *
     * @param svcName
     *            the svc name
     * @return true, if successful
     */
    public boolean hasServiceMonitor(final String svcName) {
        return getServiceMonitor(svcName) != null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#isInterfaceInPackage(java.lang.String, org.opennms.netmgt.config.poller.Package)
     */
    @Override
    public boolean isInterfaceInPackage(final String iface, final Package pkg) {
        for (final String ipAddr : pkg.getSpecificCollection()) {
            if (ipAddr.equals(iface))
                return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#isPolled(java.lang.String)
     */
    @Override
    public boolean isPolled(final String ipaddr) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#isPolled(java.lang.String, org.opennms.netmgt.config.poller.Package)
     */
    @Override
    public boolean isPolled(final String svcName, final Package pkg) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#isPolled(java.lang.String, java.lang.String)
     */
    @Override
    public boolean isPolled(final String ipaddr, final String svcName) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#isNodeOutageProcessingEnabled()
     */
    @Override
    public boolean isNodeOutageProcessingEnabled() {
        return m_outageProcessingEnabled;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#shouldPollAllIfNoCriticalServiceDefined()
     */
    @Override
    public boolean shouldPollAllIfNoCriticalServiceDefined() {
        // TODO Auto-generated method stub
        return m_pollAll;
    }

    /**
     * Sets the poll all if no critical service defined.
     *
     * @param pollAll
     *            the new poll all if no critical service defined
     */
    public void setPollAllIfNoCriticalServiceDefined(final boolean pollAll) {
        m_pollAll = pollAll;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#rebuildPackageIpListMap()
     */
    @Override
    public void rebuildPackageIpListMap() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#isServiceInPackageAndEnabled(java.lang.String, org.opennms.netmgt.config.poller.Package)
     */
    @Override
    public boolean isServiceInPackageAndEnabled(final String svcName, final Package pkg) {
        for (final Service svc : pkg.getServiceCollection()) {
            if (svc.getName().equals(svcName))
                return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#isServiceMonitored(java.lang.String)
     */
    @Override
    public boolean isServiceMonitored(final String svcName) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#isServiceUnresponsiveEnabled()
     */
    @Override
    public boolean isServiceUnresponsiveEnabled() {
        return m_serviceUnresponsiveEnabled;
    }

    /**
     * Sets the next outage id sql.
     *
     * @param nextOutageIdSql
     *            the new next outage id sql
     */
    public void setNextOutageIdSql(final String nextOutageIdSql) {
        m_nextOutageIdSql = nextOutageIdSql;
    }

    /**
     * Sets the service unresponsive enabled.
     *
     * @param serviceUnresponsiveEnabled
     *            the new service unresponsive enabled
     */
    public void setServiceUnresponsiveEnabled(final boolean serviceUnresponsiveEnabled) {
        m_serviceUnresponsiveEnabled = serviceUnresponsiveEnabled;
    }

    /**
     * Sets the critical service.
     *
     * @param criticalSvcName
     *            the new critical service
     */
    public void setCriticalService(final String criticalSvcName) {
        m_criticalSvcName = criticalSvcName;
    }

    /**
     * Sets the interface match.
     *
     * @param matchRegexp
     *            the new interface match
     */
    public void setInterfaceMatch(final String matchRegexp) {
        m_currentPkg.addIncludeUrl(matchRegexp);
    }

    /**
     * Sets the node outage processing enabled.
     *
     * @param outageProcessingEnabled
     *            the new node outage processing enabled
     */
    public void setNodeOutageProcessingEnabled(final boolean outageProcessingEnabled) {
        m_outageProcessingEnabled = outageProcessingEnabled;
    }

    /**
     * Sets the poll interval.
     *
     * @param svcName
     *            the svc name
     * @param interval
     *            the interval
     */
    public void setPollInterval(final String svcName, final long interval) {
        setPollInterval(m_currentPkg, svcName, interval);
    }

    /**
     * Sets the poll interval.
     *
     * @param pkg
     *            the pkg
     * @param svcName
     *            the svc name
     * @param interval
     *            the interval
     */
    public void setPollInterval(final Package pkg, final String svcName, final long interval) {
        final Service svc = findService(pkg, svcName);
        if (svc == null)
            throw new IllegalArgumentException("No service named: " + svcName + " in package " + pkg);

        svc.setInterval(interval);
    }

    /**
     * Sets the poller threads.
     *
     * @param threads
     *            the new poller threads
     */
    public void setPollerThreads(final int threads) {
        m_threads = threads;
    }

    /**
     * Sets the default poll interval.
     *
     * @param defaultPollInterval
     *            the new default poll interval
     */
    public void setDefaultPollInterval(final long defaultPollInterval) {
        m_defaultPollInterval = defaultPollInterval;
    }

    /**
     * Populate package.
     *
     * @param network
     *            the network
     */
    public void populatePackage(final MockNetwork network) {
        final MockVisitor populator = new MockVisitorAdapter() {
            @Override
            public void visitService(final MockService svc) {
                addService(svc);
            }
        };
        network.visit(populator);
    }

    /**
     * Save xml.
     *
     * @param xmlString
     *            the xml string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    protected void saveXML(final String xmlString) throws IOException, MarshalException, ValidationException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getServiceInPackage(java.lang.String, org.opennms.netmgt.config.poller.Package)
     */
    @Override
    public Service getServiceInPackage(final String svcName, final Package pkg) {
        return findService(pkg, svcName);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollOutagesConfig#update()
     */
    @Override
    public void update() {

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#save()
     */
    @Override
    public void save() {

    }

    /**
     * Adds the parameter.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public void addParameter(final String key, final String value) {
        final Parameter param = new Parameter();
        param.setKey(key);
        param.setValue(value);
        m_currentSvc.addParameter(param);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#addPackage(org.opennms.netmgt.config.poller.Package)
     */
    @Override
    public void addPackage(final Package pkg) {
        m_pkgs.add(pkg);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getConfiguration()
     */
    @Override
    public PollerConfiguration getConfiguration() {
        // FIXME: need to actually implement this
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getAllPackageMatches(java.lang.String)
     */
    @Override
    public List<String> getAllPackageMatches(final String ipAddr) {
        return new ArrayList<String>(0);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#isPathOutageEnabled()
     */
    @Override
    public boolean isPathOutageEnabled() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#releaseAllServiceMonitors()
     */
    @Override
    public void releaseAllServiceMonitors() {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getIpList(org.opennms.netmgt.config.poller.Package)
     */
    @Override
    public List<InetAddress> getIpList(final Package pkg) {
        return Collections.emptyList();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getServiceSelectorForPackage(org.opennms.netmgt.config.poller.Package)
     */
    @Override
    public ServiceSelector getServiceSelectorForPackage(final Package pkg) {
        return null;
    }

    /**
     * Save response time data.
     *
     * @param locationMonitor
     *            the location monitor
     * @param monSvc
     *            the mon svc
     * @param responseTime
     *            the response time
     * @param pkg
     *            the pkg
     */
    public void saveResponseTimeData(final String locationMonitor, final OnmsMonitoredService monSvc,
            final double responseTime, final Package pkg) {
        throw new UnsupportedOperationException("not yet implemented");

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getServiceMonitorLocators(org.opennms.netmgt.poller.DistributionContext)
     */
    @Override
    public Collection<ServiceMonitorLocator> getServiceMonitorLocators(final DistributionContext context) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#getFirstLocalPackageMatch(java.lang.String)
     */
    @Override
    public Package getFirstLocalPackageMatch(final String ipaddr) {
        throw new UnsupportedOperationException("MockPollerConfig.getFirstLocalPackageMatch is not yet implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#isPolledLocally(java.lang.String)
     */
    @Override
    public boolean isPolledLocally(final String ipaddr) {
        throw new UnsupportedOperationException("MockPollerConfig.isPolledLocally is not yet implemented");
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.PollerConfig#isPolledLocally(java.lang.String, java.lang.String)
     */
    @Override
    public boolean isPolledLocally(final String ipaddr, final String svcName) {
        throw new UnsupportedOperationException("MockPollerConfig.isPolledLocally is not yet implemented");
    }

}
