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

package org.opennms.netmgt.snmp;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * The Interface SnmpStrategy.
 */
public interface SnmpStrategy {

    /**
     * Creates the walker.
     *
     * @param agentConfig
     *            the agent config
     * @param name
     *            the name
     * @param tracker
     *            the tracker
     * @return the snmp walker
     */
    SnmpWalker createWalker(SnmpAgentConfig agentConfig, String name, CollectionTracker tracker);

    /**
     * Sets the.
     *
     * @param agentConfig
     *            the agent config
     * @param oid
     *            the oid
     * @param value
     *            the value
     * @return the snmp value
     */
    SnmpValue set(SnmpAgentConfig agentConfig, SnmpObjId oid, SnmpValue value);

    /**
     * Sets the.
     *
     * @param agentConfig
     *            the agent config
     * @param oid
     *            the oid
     * @param value
     *            the value
     * @return the snmp value[]
     */
    SnmpValue[] set(SnmpAgentConfig agentConfig, SnmpObjId[] oid, SnmpValue[] value);

    /**
     * Gets the.
     *
     * @param agentConfig
     *            the agent config
     * @param oid
     *            the oid
     * @return the snmp value
     */
    SnmpValue get(SnmpAgentConfig agentConfig, SnmpObjId oid);

    /**
     * Gets the.
     *
     * @param agentConfig
     *            the agent config
     * @param oids
     *            the oids
     * @return the snmp value[]
     */
    SnmpValue[] get(SnmpAgentConfig agentConfig, SnmpObjId[] oids);

    /**
     * Gets the next.
     *
     * @param agentConfig
     *            the agent config
     * @param oid
     *            the oid
     * @return the next
     */
    SnmpValue getNext(SnmpAgentConfig agentConfig, SnmpObjId oid);

    /**
     * Gets the next.
     *
     * @param agentConfig
     *            the agent config
     * @param oids
     *            the oids
     * @return the next
     */
    SnmpValue[] getNext(SnmpAgentConfig agentConfig, SnmpObjId[] oids);

    /**
     * Gets the bulk.
     *
     * @param agentConfig
     *            the agent config
     * @param oids
     *            the oids
     * @return the bulk
     */
    SnmpValue[] getBulk(SnmpAgentConfig agentConfig, SnmpObjId[] oids);

    /**
     * Register for traps.
     *
     * @param listener
     *            the listener
     * @param processorFactory
     *            the processor factory
     * @param address
     *            the address
     * @param snmpTrapPort
     *            the snmp trap port
     * @param snmpv3Users
     *            the snmpv3 users
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void registerForTraps(TrapNotificationListener listener, TrapProcessorFactory processorFactory,
            InetAddress address, int snmpTrapPort, List<SnmpV3User> snmpv3Users) throws IOException;

    /**
     * Register for traps.
     *
     * @param listener
     *            the listener
     * @param processorFactory
     *            the processor factory
     * @param address
     *            the address
     * @param snmpTrapPort
     *            the snmp trap port
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void registerForTraps(TrapNotificationListener listener, TrapProcessorFactory processorFactory,
            InetAddress address, int snmpTrapPort) throws IOException;

    /**
     * Register for traps.
     *
     * @param listener
     *            the listener
     * @param processorFactory
     *            the processor factory
     * @param snmpTrapPort
     *            the snmp trap port
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void registerForTraps(TrapNotificationListener listener, TrapProcessorFactory processorFactory, int snmpTrapPort)
            throws IOException;

    /**
     * Unregister for traps.
     *
     * @param listener
     *            the listener
     * @param address
     *            the address
     * @param snmpTrapPort
     *            the snmp trap port
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void unregisterForTraps(TrapNotificationListener listener, InetAddress address, int snmpTrapPort)
            throws IOException;

    /**
     * Unregister for traps.
     *
     * @param listener
     *            the listener
     * @param snmpTrapPort
     *            the snmp trap port
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    void unregisterForTraps(TrapNotificationListener listener, int snmpTrapPort) throws IOException;

    /**
     * Gets the value factory.
     *
     * @return the value factory
     */
    SnmpValueFactory getValueFactory();

    /**
     * Gets the v1 trap builder.
     *
     * @return the v1 trap builder
     */
    SnmpV1TrapBuilder getV1TrapBuilder();

    /**
     * Gets the v2 trap builder.
     *
     * @return the v2 trap builder
     */
    SnmpTrapBuilder getV2TrapBuilder();

    /**
     * Gets the v3 trap builder.
     *
     * @return the v3 trap builder
     */
    SnmpV3TrapBuilder getV3TrapBuilder();

    /**
     * Gets the v2 inform builder.
     *
     * @return the v2 inform builder
     */
    SnmpV2TrapBuilder getV2InformBuilder();

    /**
     * Gets the v3 inform builder.
     *
     * @return the v3 inform builder
     */
    SnmpV3TrapBuilder getV3InformBuilder();

    /**
     * Gets the local engine id.
     *
     * @return the local engine id
     */
    byte[] getLocalEngineID();

}
