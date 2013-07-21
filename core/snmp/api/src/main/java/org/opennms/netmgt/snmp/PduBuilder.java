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

/**
 * The Class PduBuilder.
 */
public abstract class PduBuilder {

    /** The m_max vars per pdu. */
    private int m_maxVarsPerPdu;

    /**
     * Instantiates a new pdu builder.
     */
    public PduBuilder() {
        this(SnmpAgentConfig.DEFAULT_MAX_VARS_PER_PDU);
    }

    /**
     * Instantiates a new pdu builder.
     *
     * @param maxVarsPerPdu
     *            the max vars per pdu
     */
    public PduBuilder(int maxVarsPerPdu) {
        m_maxVarsPerPdu = maxVarsPerPdu;
    }

    /**
     * Adds the oid.
     *
     * @param snmpObjId
     *            the snmp obj id
     */
    public abstract void addOid(SnmpObjId snmpObjId);

    /**
     * Sets the non repeaters.
     *
     * @param numNonRepeaters
     *            the new non repeaters
     */
    public abstract void setNonRepeaters(int numNonRepeaters);

    /**
     * Sets the max repetitions.
     *
     * @param maxRepetitions
     *            the new max repetitions
     */
    public abstract void setMaxRepetitions(int maxRepetitions);

    /**
     * Gets the max vars per pdu.
     *
     * @return the max vars per pdu
     */
    public int getMaxVarsPerPdu() {
        return m_maxVarsPerPdu;
    }

    /**
     * Sets the max vars per pdu.
     *
     * @param maxVarsPerPdu
     *            the new max vars per pdu
     */
    public void setMaxVarsPerPdu(int maxVarsPerPdu) {
        m_maxVarsPerPdu = maxVarsPerPdu;
    }

}
