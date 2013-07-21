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

package org.opennms.netmgt.snmp.mock;

/**
 * The Class BulkPdu.
 */
public class BulkPdu extends RequestPdu {

    /** The m_non repeaters. */
    int m_nonRepeaters;

    /** The m_max repititions. */
    int m_maxRepititions;

    /**
     * Instantiates a new bulk pdu.
     */
    public BulkPdu() {
        super();
    }

    /**
     * Sets the non repeaters.
     *
     * @param nonRepeaters
     *            the new non repeaters
     */
    public void setNonRepeaters(int nonRepeaters) {
        m_nonRepeaters = nonRepeaters;
    }

    /**
     * Sets the max repititions.
     *
     * @param maxRepititions
     *            the new max repititions
     */
    public void setMaxRepititions(int maxRepititions) {
        m_maxRepititions = maxRepititions;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.mock.RequestPdu#getNonRepeaters()
     */
    @Override
    public int getNonRepeaters() {
        return m_nonRepeaters;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.mock.RequestPdu#getMaxRepititions()
     */
    @Override
    public int getMaxRepititions() {
        return m_maxRepititions;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.mock.RequestPdu#send(org.opennms.netmgt.snmp.mock.TestAgent)
     */
    @Override
    public ResponsePdu send(TestAgent agent) {
        if (agent.isVersion1())
            throw new IllegalStateException("can't send a getBulk pack to a V1 Agent");

        return super.send(agent);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.mock.RequestPdu#handleTooBig(org.opennms.netmgt.snmp.mock.TestAgent, org.opennms.netmgt.snmp.mock.ResponsePdu)
     */
    @Override
    protected ResponsePdu handleTooBig(TestAgent agent, ResponsePdu resp) {
        resp.getVarBinds().subList(agent.getMaxResponseSize(), resp.size()).clear();
        return resp;
    }
}
