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
package org.opennms.netmgt.accesspointmonitor.poller;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.ParameterMap;
import org.opennms.netmgt.config.SnmpPeerFactory;
import org.opennms.netmgt.config.accesspointmonitor.Package;
import org.opennms.netmgt.dao.AccessPointDao;
import org.opennms.netmgt.model.OnmsAccessPoint;
import org.opennms.netmgt.model.OnmsAccessPointCollection;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpInstId;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Table strategy for polling access-points: 1) Walks the table at the
 * configured OID, uses the values as the AP MACs 2) If the AP MAC is in the
 * table, the AP is ONLINE.
 *
 * @author <a href="mailto:jwhite@datavalet.com">Jesse White</a>
 */
public class TableStrategy implements AccessPointPoller {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(TableStrategy.class);

    /** The m_iface. */
    private OnmsIpInterface m_iface;

    /** The m_package. */
    private Package m_package;

    /** The m_parameters. */
    private Map<String, String> m_parameters;

    /** The m_access point dao. */
    private AccessPointDao m_accessPointDao;

    /**
     * Instantiates a new table strategy.
     */
    public TableStrategy() {

    }

    /* (non-Javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public OnmsAccessPointCollection call() throws IOException {
        OnmsAccessPointCollection apsUp = new OnmsAccessPointCollection();
        InetAddress ipaddr = m_iface.getIpAddress();

        // Retrieve this interface's SNMP peer object
        SnmpAgentConfig agentConfig = SnmpPeerFactory.getInstance().getAgentConfig(ipaddr);
        if (agentConfig == null) {
            throw new IllegalStateException("SnmpAgentConfig object not available for interface " + ipaddr);
        }
        final String hostAddress = InetAddressUtils.str(ipaddr);
        LOG.debug("poll: setting SNMP peer attribute for interface {}", hostAddress);

        // Get configuration parameters
        String oid = ParameterMap.getKeyedString(m_parameters, "oid", null);
        if (oid == null) {
            throw new IllegalStateException("oid parameter is not set.");
        }

        agentConfig.hashCode();

        // Set timeout and retries on SNMP peer object
        agentConfig.setTimeout(ParameterMap.getKeyedInteger(m_parameters, "timeout", agentConfig.getTimeout()));
        agentConfig.setRetries(ParameterMap.getKeyedInteger(m_parameters, "retry",
                                                            ParameterMap.getKeyedInteger(m_parameters, "retries",
                                                                                         agentConfig.getRetries())));
        agentConfig.setPort(ParameterMap.getKeyedInteger(m_parameters, "port", agentConfig.getPort()));

        LOG.debug("TableStrategy.poll: SnmpAgentConfig address={}", agentConfig);

        // Establish SNMP session with interface
        try {
            SnmpObjId snmpObjectId = SnmpObjId.get(oid);

            Map<SnmpInstId, SnmpValue> map = SnmpUtils.getOidValues(agentConfig, "AccessPointMonitor::TableStrategy",
                                                                    snmpObjectId);

            if (map.size() <= 0) {
                throw new IOException("No entries found in table (possible timeout).");
            }

            for (Map.Entry<SnmpInstId, SnmpValue> entry : map.entrySet()) {
                SnmpValue value = entry.getValue();

                String physAddr = getPhysAddrFromValue(value);

                LOG.debug("AP at value '{}' with MAC '{}' is considered to be ONLINE on controller '{}'",
                          value.toHexString(), physAddr, m_iface.getIpAddress());
                OnmsAccessPoint ap = m_accessPointDao.get(physAddr);
                if (ap != null) {
                    if (ap.getPollingPackage().compareToIgnoreCase(getPackage().getName()) == 0) {
                        // Save the controller's IP address
                        ap.setControllerIpAddress(ipaddr);
                        apsUp.add(ap);
                    } else {
                        LOG.info("AP with MAC '{}' is in a different package.", physAddr);
                    }
                } else {
                    LOG.info("No matching AP in database for value '{}'.", value.toHexString());
                }
            }
        } catch (InterruptedException e) {
            LOG.error("Interrupted while polling {}", hostAddress, e);
        }

        return apsUp;
    }

    /**
     * Gets the phys addr from value.
     *
     * @param value
     *            the value
     * @return the phys addr from value
     */
    public static String getPhysAddrFromValue(SnmpValue value) {
        String hexString = value.toHexString();
        if (hexString.length() != 12) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i += 2) {
            sb.append(hexString.substring(i, i + 2));
            if (i < 10) {
                sb.append(':');
            }
        }

        return sb.toString().toUpperCase();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.poller.AccessPointPoller#setInterfaceToPoll(org.opennms.netmgt.model.OnmsIpInterface)
     */
    @Override
    public void setInterfaceToPoll(OnmsIpInterface interfaceToPoll) {
        m_iface = interfaceToPoll;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.poller.AccessPointPoller#getInterfaceToPoll()
     */
    @Override
    public OnmsIpInterface getInterfaceToPoll() {
        return m_iface;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.poller.AccessPointPoller#setPackage(org.opennms.netmgt.config.accesspointmonitor.Package)
     */
    @Override
    public void setPackage(Package pkg) {
        m_package = pkg;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.poller.AccessPointPoller#getPackage()
     */
    @Override
    public Package getPackage() {
        return m_package;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.poller.AccessPointPoller#setPropertyMap(java.util.Map)
     */
    @Override
    public void setPropertyMap(Map<String, String> parameters) {
        m_parameters = parameters;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.poller.AccessPointPoller#getPropertyMap()
     */
    @Override
    public Map<String, String> getPropertyMap() {
        return m_parameters;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.poller.AccessPointPoller#setAccessPointDao(org.opennms.netmgt.dao.AccessPointDao)
     */
    @Override
    public void setAccessPointDao(AccessPointDao accessPointDao) {
        m_accessPointDao = accessPointDao;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.accesspointmonitor.poller.AccessPointPoller#getAccessPointDao()
     */
    @Override
    public AccessPointDao getAccessPointDao() {
        return m_accessPointDao;
    }
};
