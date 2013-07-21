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

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class TrapInformation.
 */
public abstract class TrapInformation implements TrapNotification {

    /** The Constant LOG. */
    private static final transient Logger LOG = LoggerFactory.getLogger(TrapInformation.class);

    /** The internet address of the sending agent. */
    private InetAddress m_agent;

    /** The community string from the actual SNMP packet. */
    private String m_community;

    /** The m_trap processor. */
    private TrapProcessor m_trapProcessor;

    /**
     * Instantiates a new trap information.
     *
     * @param agent
     *            the agent
     * @param community
     *            the community
     * @param trapProcessor
     *            the trap processor
     */
    protected TrapInformation(InetAddress agent, String community, TrapProcessor trapProcessor) {
        m_agent = agent;
        m_community = community;
        m_trapProcessor = trapProcessor;

    }

    /**
     * Gets the trap address.
     *
     * @return the trap address
     */
    protected abstract InetAddress getTrapAddress();

    /**
     * Returns the sending agent's internet address.
     *
     * @return the agent
     */
    protected InetAddress getAgent() {
        return m_agent;
    }

    /**
     * Returns the SNMP community string from the received packet.
     *
     * @return the community
     */
    protected String getCommunity() {
        return m_community;
    }

    /**
     * Validate.
     */
    protected void validate() {
        // by default we do nothing;
    }

    /**
     * Gets the agent address.
     *
     * @return the agent address
     */
    protected InetAddress getAgentAddress() {
        return getAgent();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.TrapNotification#getTrapProcessor()
     */
    @Override
    public TrapProcessor getTrapProcessor() {
        // We do this here to processing of the data is delayed until it is
        // requested.
        processTrap();
        return m_trapProcessor;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    protected abstract String getVersion();

    /**
     * Gets the pdu length.
     *
     * @return the pdu length
     */
    protected abstract int getPduLength();

    /**
     * Gets the time stamp.
     *
     * @return the time stamp
     */
    protected abstract long getTimeStamp();

    /**
     * Gets the trap identity.
     *
     * @return the trap identity
     */
    protected abstract TrapIdentity getTrapIdentity();

    /**
     * Process trap.
     */
    protected void processTrap() {

        validate();

        m_trapProcessor.setVersion(getVersion());
        m_trapProcessor.setCommunity(getCommunity());
        m_trapProcessor.setAgentAddress(getAgentAddress());
        m_trapProcessor.setTrapAddress(getTrapAddress());

        LOG.debug("{} trap - trapInterface: ()", getVersion(), getTrapAddress());

        // time-stamp
        m_trapProcessor.setTimeStamp(getTimeStamp());

        m_trapProcessor.setTrapIdentity(getTrapIdentity());

        for (int i = 0; i < getPduLength(); i++) {
            processVarBindAt(i);
        } // end for loop
    }

    /**
     * Process var bind at.
     *
     * @param i
     *            the i
     */
    protected abstract void processVarBindAt(int i);

    /**
     * Process var bind.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     */
    protected void processVarBind(SnmpObjId name, SnmpValue value) {
        m_trapProcessor.processVarBind(name, value);
    }

}
