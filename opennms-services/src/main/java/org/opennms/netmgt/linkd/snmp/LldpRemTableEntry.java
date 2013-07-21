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

package org.opennms.netmgt.linkd.snmp;

import java.net.InetAddress;

import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpResult;
import org.opennms.netmgt.snmp.SnmpUtils;

/**
 * The Class LldpRemTableEntry.
 */
public class LldpRemTableEntry extends SnmpStore {

    /** The Constant LLDP_REM_LOCAL_PORTNUM_ALIAS. */
    public static final String LLDP_REM_LOCAL_PORTNUM_ALIAS = "lldpRemLocalPortNum";

    /** The Constant LLDP_REM_CHASSISID_SUBTYPE_ALIAS. */
    public static final String LLDP_REM_CHASSISID_SUBTYPE_ALIAS = "lldpRemChassisIdSubtype";

    /** The Constant LLDP_REM_CHASSISID_ALIAS. */
    public static final String LLDP_REM_CHASSISID_ALIAS = "lldpRemChassisId";

    /** The Constant LLDP_REM_PORTID_SUBTYPE_ALIAS. */
    public static final String LLDP_REM_PORTID_SUBTYPE_ALIAS = "lldpRemPortIdSubtype";

    /** The Constant LLDP_REM_PORTID_ALIAS. */
    public static final String LLDP_REM_PORTID_ALIAS = "lldpRemPortId";

    /** The Constant LLDP_REM_SYSNAME_ALIAS. */
    public static final String LLDP_REM_SYSNAME_ALIAS = "lldpRemSysName";

    /** The Constant LLDP_REM_LOCAL_PORTNUM_OID. */
    public static final String LLDP_REM_LOCAL_PORTNUM_OID = ".1.0.8802.1.1.2.1.4.1.1.2";

    /** The Constant LLDP_REM_CHASSISID_SUBTYPE_OID. */
    public static final String LLDP_REM_CHASSISID_SUBTYPE_OID = ".1.0.8802.1.1.2.1.4.1.1.4";

    /** The Constant LLDP_REM_CHASSISID_OID. */
    public static final String LLDP_REM_CHASSISID_OID = ".1.0.8802.1.1.2.1.4.1.1.5";

    /** The Constant LLDP_REM_PORTID_SUBTYPE_OID. */
    public static final String LLDP_REM_PORTID_SUBTYPE_OID = ".1.0.8802.1.1.2.1.4.1.1.6";

    /** The Constant LLDP_REM_PORTID_OID. */
    public static final String LLDP_REM_PORTID_OID = ".1.0.8802.1.1.2.1.4.1.1.7";

    /** The Constant LLDP_REM_SYSNAME_OID. */
    public static final String LLDP_REM_SYSNAME_OID = ".1.0.8802.1.1.2.1.4.1.1.9";

    /** The Constant lldpremtable_elemList. */
    public static final NamedSnmpVar[] lldpremtable_elemList = new NamedSnmpVar[] {

    new NamedSnmpVar(NamedSnmpVar.SNMPINT32, LLDP_REM_LOCAL_PORTNUM_ALIAS, LLDP_REM_LOCAL_PORTNUM_OID, 1),
    /**
     * "The type of encoding used to identify the chassis associated
     * with the remote system."
     */
    new NamedSnmpVar(NamedSnmpVar.SNMPINT32, LLDP_REM_CHASSISID_SUBTYPE_ALIAS, LLDP_REM_CHASSISID_SUBTYPE_OID, 2),

    /**
     * "The string value used to identify the chassis component
     * associated with the remote system."
     */
    new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, LLDP_REM_CHASSISID_ALIAS, LLDP_REM_CHASSISID_OID, 3),

    /**
     * "The type of port identifier encoding used in the associated
     * 'lldpRemPortId' object."
     */
    new NamedSnmpVar(NamedSnmpVar.SNMPINT32, LLDP_REM_PORTID_SUBTYPE_ALIAS, LLDP_REM_PORTID_SUBTYPE_OID, 4),

    /**
     * "The string value used to identify the port component
     * associated with the remote system."
     */
    new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, LLDP_REM_PORTID_ALIAS, LLDP_REM_PORTID_OID, 5),

    /**
     * "The string value used to identify the port component
     * associated with the remote system."
     */
    new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, LLDP_REM_SYSNAME_ALIAS, LLDP_REM_SYSNAME_OID, 6)

    };

    /** The Constant TABLE_OID. */
    public static final String TABLE_OID = ".1.0.8802.1.1.2.1.4.1.1"; // start
                                                                      // of
                                                                      // table
                                                                      // (GETNEXT)

    /**
                                                                         * The
                                                                         * has
                                                                         * lldp
                                                                         * loc
                                                                         * port
                                                                         * id.
                                                                         */
                                                                      private boolean hasLldpLocPortId = false;

    /**
     * Instantiates a new lldp rem table entry.
     */
    public LldpRemTableEntry() {
        super(lldpremtable_elemList);
    }

    /**
     * Gets the lldp rem local port num.
     *
     * @return the lldp rem local port num
     */
    public Integer getLldpRemLocalPortNum() {
        return getInt32(LLDP_REM_LOCAL_PORTNUM_ALIAS);
    }

    /**
     * Gets the lldp rem chassisid subtype.
     *
     * @return the lldp rem chassisid subtype
     */
    public Integer getLldpRemChassisidSubtype() {
        return getInt32(LLDP_REM_CHASSISID_SUBTYPE_ALIAS);
    }

    /**
     * Gets the lldp rem chassiid.
     *
     * @return the lldp rem chassiid
     */
    public String getLldpRemChassiid() {
        return getHexString(LLDP_REM_CHASSISID_ALIAS);
    }

    /**
     * Gets the lldp rem portid subtype.
     *
     * @return the lldp rem portid subtype
     */
    public Integer getLldpRemPortidSubtype() {
        return getInt32(LLDP_REM_PORTID_SUBTYPE_ALIAS);
    }

    /**
     * Gets the lldp rem portid.
     *
     * @return the lldp rem portid
     */
    public String getLldpRemPortid() {
        return getDisplayString(LLDP_REM_PORTID_ALIAS);
    }

    /**
     * Gets the lldp rem sysname.
     *
     * @return the lldp rem sysname
     */
    public String getLldpRemSysname() {
        return getDisplayString(LLDP_REM_SYSNAME_ALIAS);
    }

    /**
     * Gets the lldp rem mac address.
     *
     * @return the lldp rem mac address
     */
    public String getLldpRemMacAddress() {
        return getHexString(LLDP_REM_PORTID_ALIAS);
    }

    /**
     * Gets the lldp rem ip address.
     *
     * @return the lldp rem ip address
     */
    public InetAddress getLldpRemIpAddress() {
        return getIPAddress(LLDP_REM_PORTID_ALIAS);
    }

    /** {@inheritDoc} */
    @Override
    public void storeResult(SnmpResult res) {
        if (!hasLldpLocPortId) {
            int lldpLocPortId = res.getInstance().getSubIdAt(1);
            super.storeResult(new SnmpResult(SnmpObjId.get(LLDP_REM_LOCAL_PORTNUM_OID), res.getInstance(),
                                             SnmpUtils.getValueFactory().getInt32(lldpLocPortId)));
            hasLldpLocPortId = true;
        }
        super.storeResult(res);
    }

}
