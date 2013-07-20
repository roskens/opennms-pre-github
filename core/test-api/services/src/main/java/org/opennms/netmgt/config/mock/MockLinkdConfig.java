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

package org.opennms.netmgt.config.mock;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.config.LinkdConfig;
import org.opennms.netmgt.config.LinkdConfigManager;
import org.opennms.netmgt.config.linkd.LinkdConfiguration;
import org.opennms.netmgt.config.linkd.Package;

/**
 * The Class MockLinkdConfig.
 */
public class MockLinkdConfig implements LinkdConfig {

    /** The m_global lock. */
    private final ReadWriteLock m_globalLock = new ReentrantReadWriteLock();

    /** The m_read lock. */
    private final Lock m_readLock = m_globalLock.readLock();

    /** The m_write lock. */
    private final Lock m_writeLock = m_globalLock.writeLock();

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getIpList(org.opennms.netmgt.config.linkd.Package)
     */
    @Override
    public List<InetAddress> getIpList(final Package pkg) {
        return Collections.emptyList();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#isInterfaceInPackage(java.net.InetAddress, org.opennms.netmgt.config.linkd.Package)
     */
    @Override
    public boolean isInterfaceInPackage(final InetAddress iface, final Package pkg) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#isInterfaceInPackageRange(java.net.InetAddress, org.opennms.netmgt.config.linkd.Package)
     */
    @Override
    public boolean isInterfaceInPackageRange(final InetAddress iface, final Package pkg) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getFirstPackageMatch(java.net.InetAddress)
     */
    @Override
    public Package getFirstPackageMatch(final InetAddress ipaddr) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getAllPackageMatches(java.net.InetAddress)
     */
    @Override
    public List<String> getAllPackageMatches(final InetAddress ipAddr) {
        return Collections.emptyList();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#isAutoDiscoveryEnabled()
     */
    @Override
    public boolean isAutoDiscoveryEnabled() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#isVlanDiscoveryEnabled()
     */
    @Override
    public boolean isVlanDiscoveryEnabled() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#enumeratePackage()
     */
    @Override
    public Enumeration<Package> enumeratePackage() {
        final List<Package> list = Collections.emptyList();
        return Collections.enumeration(list);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getPackage(java.lang.String)
     */
    @Override
    public Package getPackage(final String pkgName) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getThreads()
     */
    @Override
    public int getThreads() {
        return 1;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#enableDiscoveryDownload()
     */
    @Override
    public boolean enableDiscoveryDownload() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#useIpRouteDiscovery()
     */
    @Override
    public boolean useIpRouteDiscovery() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#forceIpRouteDiscoveryOnEthernet()
     */
    @Override
    public boolean forceIpRouteDiscoveryOnEthernet() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#saveRouteTable()
     */
    @Override
    public boolean saveRouteTable() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#useCdpDiscovery()
     */
    @Override
    public boolean useCdpDiscovery() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#useLldpDiscovery()
     */
    @Override
    public boolean useLldpDiscovery() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#useBridgeDiscovery()
     */
    @Override
    public boolean useBridgeDiscovery() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#saveStpNodeTable()
     */
    @Override
    public boolean saveStpNodeTable() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#saveStpInterfaceTable()
     */
    @Override
    public boolean saveStpInterfaceTable() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getInitialSleepTime()
     */
    @Override
    public long getInitialSleepTime() {
        return 3000;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getSnmpPollInterval()
     */
    @Override
    public long getSnmpPollInterval() {
        return 3000;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getDiscoveryLinkInterval()
     */
    @Override
    public long getDiscoveryLinkInterval() {
        return 3000;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#update()
     */
    @Override
    public void update() {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#save()
     */
    @Override
    public void save() throws MarshalException, IOException, ValidationException {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getConfiguration()
     */
    @Override
    public LinkdConfiguration getConfiguration() {
        return new LinkdConfiguration();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getVlanClassName(java.lang.String)
     */
    @Override
    public String getVlanClassName(final String sysoid) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#hasClassName(java.lang.String)
     */
    @Override
    public boolean hasClassName(final String sysoid) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getReadLock()
     */
    @Override
    public Lock getReadLock() {
        return m_readLock;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getWriteLock()
     */
    @Override
    public Lock getWriteLock() {
        return m_writeLock;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#hasIpRouteClassName(java.lang.String)
     */
    @Override
    public boolean hasIpRouteClassName(final String sysoid) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getIpRouteClassName(java.lang.String)
     */
    @Override
    public String getIpRouteClassName(final String sysoid) {
        return LinkdConfigManager.DEFAULT_IP_ROUTE_CLASS_NAME;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#getDefaultIpRouteClassName()
     */
    @Override
    public String getDefaultIpRouteClassName() {
        return LinkdConfigManager.DEFAULT_IP_ROUTE_CLASS_NAME;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#reload()
     */
    @Override
    public void reload() throws IOException, MarshalException, ValidationException {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.LinkdConfig#useOspfDiscovery()
     */
    @Override
    public boolean useOspfDiscovery() {
        return false;
    }

}
