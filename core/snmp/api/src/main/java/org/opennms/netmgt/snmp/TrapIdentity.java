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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class TrapIdentity.
 */
public class TrapIdentity {

    /** The Constant LOG. */
    private static final transient Logger LOG = LoggerFactory.getLogger(TrapIdentity.class);

    /** The m_generic. */
    private int m_generic;

    /** The m_specific. */
    private int m_specific;

    /** The m_enterprise id. */
    private String m_enterpriseId;

    /** The standard traps list. */
    private static final List<SnmpObjId> GENERIC_TRAPS;

    /** The dot separator in an OID. */
    private static final char DOT_CHAR = '.';

    /**
     * The SNMP trap enterprise OID, which if present in a V2 trap is the last
     * varbind.
     * ref - book 'SNMP, SNMPv2, SNMPv3..' by William Stallings, third edition,
     * section 13.1.3
     */
    private static final String SNMP_TRAP_ENTERPRISE_ID = ".1.3.6.1.6.3.1.1.4.3.0";

    /**
     * The snmpTraps value to be used in case a standard trap comes in without
     * the SNMP_TRAP_ENTERPRISE_ID as the last varbind.
     */
    private static final String SNMP_TRAPS = ".1.3.6.1.6.3.1.1.5";

    /**
     * Create the standard traps list - used in v2 processing
     */
    static {
        GENERIC_TRAPS = new ArrayList<SnmpObjId>();
        GENERIC_TRAPS.add(new SnmpObjId("1.3.6.1.6.3.1.1.5.1")); // coldStart
        GENERIC_TRAPS.add(new SnmpObjId("1.3.6.1.6.3.1.1.5.2")); // warmStart
        GENERIC_TRAPS.add(new SnmpObjId("1.3.6.1.6.3.1.1.5.3")); // linkDown
        GENERIC_TRAPS.add(new SnmpObjId("1.3.6.1.6.3.1.1.5.4")); // linkUp
        GENERIC_TRAPS.add(new SnmpObjId("1.3.6.1.6.3.1.1.5.5")); // authenticationFailure
        GENERIC_TRAPS.add(new SnmpObjId("1.3.6.1.6.3.1.1.5.6")); // egpNeighborLoss
    }

    /**
     * Instantiates a new trap identity.
     *
     * @param snmpTrapOid
     *            the snmp trap oid
     * @param lastVarBindOid
     *            the last var bind oid
     * @param lastVarBindValue
     *            the last var bind value
     */
    public TrapIdentity(SnmpObjId snmpTrapOid, SnmpObjId lastVarBindOid, SnmpValue lastVarBindValue) {
        String snmpTrapOidValue = snmpTrapOid.toString();

        LOG.debug("snmpTrapOID: {}", snmpTrapOidValue);

        // get the last subid
        int lastIndex = snmpTrapOidValue.lastIndexOf(TrapIdentity.DOT_CHAR);
        String lastSubIdStr = snmpTrapOidValue.substring(lastIndex + 1);
        int lastSubId = -1;
        try {
            lastSubId = Integer.parseInt(lastSubIdStr);
        } catch (NumberFormatException nfe) {
            lastSubId = -1;
        }
        // Check if standard trap
        if (TrapIdentity.GENERIC_TRAPS.contains(snmpTrapOid)) {
            // set generic
            setGeneric(lastSubId - 1);

            // set specific to zero
            setSpecific(0);

            // if present, the 'snmpTrapEnterprise' OID occurs as
            // the last OID
            // Check the last varbind to see if it is the enterprise ID
            String varBindName = lastVarBindOid.toString();
            if (varBindName.equals(TrapIdentity.SNMP_TRAP_ENTERPRISE_ID)) {
                // if present, set the value of the varbind as the
                // enterprise id
                setEnterpriseId(lastVarBindValue.toString());
            } else {
                // if not present, set the value of the varbind as the
                // snmpTraps value defined as in RFC 1907
                setEnterpriseId(TrapIdentity.SNMP_TRAPS + "." + snmpTrapOidValue.charAt(snmpTrapOidValue.length() - 1));
            }

        } else // not standard trap
        {
            // set generic to 6
            setGeneric(6);

            setSpecific(lastSubId);

            // get the next to last subid
            int nextToLastIndex = snmpTrapOidValue.lastIndexOf(TrapIdentity.DOT_CHAR, lastIndex - 1);
            // check if value is zero
            String nextToLastSubIdStr = snmpTrapOidValue.substring(nextToLastIndex + 1, lastIndex);
            if (nextToLastSubIdStr.equals("0")) {
                // set enterprise value to trap oid minus the
                // the last two subids
                setEnterpriseId(snmpTrapOidValue.substring(0, nextToLastIndex));
            } else {
                setEnterpriseId(snmpTrapOidValue.substring(0, lastIndex));
            }
        }
    }

    /**
     * Instantiates a new trap identity.
     *
     * @param entId
     *            the ent id
     * @param generic
     *            the generic
     * @param specific
     *            the specific
     */
    public TrapIdentity(SnmpObjId entId, int generic, int specific) {
        m_enterpriseId = entId.toString();
        m_generic = generic;
        m_specific = specific;
    }

    /**
     * Gets the generic.
     *
     * @return the generic
     */
    public int getGeneric() {
        return m_generic;
    }

    /**
     * Sets the generic.
     *
     * @param generic
     *            the new generic
     */
    private void setGeneric(int generic) {
        m_generic = generic;
    }

    /**
     * Gets the specific.
     *
     * @return the specific
     */
    public int getSpecific() {
        return m_specific;
    }

    /**
     * Sets the specific.
     *
     * @param specific
     *            the new specific
     */
    private void setSpecific(int specific) {
        m_specific = specific;
    }

    /**
     * Gets the enterprise id.
     *
     * @return the enterprise id
     */
    public String getEnterpriseId() {
        return m_enterpriseId;
    }

    /**
     * Sets the enterprise id.
     *
     * @param enterpriseId
     *            the new enterprise id
     */
    private void setEnterpriseId(String enterpriseId) {
        m_enterpriseId = enterpriseId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[Generic=" + getGeneric() + ", Specific=" + getSpecific() + ", EnterpriseId=" + getEnterpriseId() + "]";
    }

}
