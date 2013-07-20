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

package org.opennms.netmgt.capsd.snmp;

import org.opennms.netmgt.snmp.AbstractSnmpStore;

/**
 * <P>
 * This object contains a list of all the elements defined in the MIB-II
 * interface table. An instance object is initialized by calling the constructor
 * and passing in a variable list from an SNMP PDU. The actual data can be
 * recovered via the base class map interface.
 * </P>
 * <P>
 * Once an instance is created and its data set either via the constructor or
 * from the update method, the actual elements can be retrieved using the
 * instance names. The names include: <EM>ifIndex</EM>,<EM>ifDescr</EM>,
 * <EM>ifSpeed</EM>,<EM>etc</EM>. The information can also be accessed by using
 * the complete object identifier for the entry.
 * </P>
 * <P>
 * For more information on the individual fields, and to find out their
 * respective object identifiers see RFC1213 from the IETF.
 * </P>
 *
 * @author <A HREF="mailto:sowmya@opennms.org">Sowmya </A>
 * @author <A HREF="mailto:weave@oculan.com">Weave </A>
 * @author <A>Jon Whetzel </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 * @see <A HREF="http://www.ietf.org/rfc/rfc1213.txt">RFC1213 </A>
 */
public final class IfTableEntry extends SnmpStore {
    //
    // Lookup strings for specific table entries
    //
    /** Constant <code>IF_INDEX="AbstractSnmpStore.IFINDEX"</code> */
    public static final String IF_INDEX = AbstractSnmpStore.IFINDEX;

    /** Constant <code>IF_DESCR="ifDescr"</code> */
    public static final String IF_DESCR = "ifDescr";

    /** Constant <code>IF_TYPE="ifType"</code> */
    public static final String IF_TYPE = "ifType";

    /** Constant <code>IF_MTU="ifMtu"</code> */
    public static final String IF_MTU = "ifMtu";

    /** Constant <code>IF_SPEED="ifSpeed"</code> */
    public static final String IF_SPEED = "ifSpeed";

    /** Constant <code>IF_PHYS_ADDR="ifPhysAddr"</code> */
    public static final String IF_PHYS_ADDR = "ifPhysAddr";

    /** Constant <code>IF_ADMIN_STATUS="ifAdminStatus"</code> */
    public static final String IF_ADMIN_STATUS = "ifAdminStatus";

    /** Constant <code>IF_OPER_STATUS="ifOperStatus"</code> */
    public static final String IF_OPER_STATUS = "ifOperStatus";

    /** Constant <code>IF_LAST_CHANGE="ifLastChange"</code> */
    public static final String IF_LAST_CHANGE = "ifLastChange";

    /** Constant <code>IF_IN_OCTETS="ifInOctets"</code> */
    public static final String IF_IN_OCTETS = "ifInOctets";

    /** Constant <code>IF_IN_UCAST="ifInUcastPkts"</code> */
    public static final String IF_IN_UCAST = "ifInUcastPkts";

    /** Constant <code>IF_IN_NUCAST="ifInNUcastPkts"</code> */
    public static final String IF_IN_NUCAST = "ifInNUcastPkts";

    /** Constant <code>IF_IN_DISCARDS="ifInDiscards"</code> */
    public static final String IF_IN_DISCARDS = "ifInDiscards";

    /** Constant <code>IF_IN_ERRORS="ifInErrors"</code> */
    public static final String IF_IN_ERRORS = "ifInErrors";

    /** Constant <code>IF_IN_UKNOWN_PROTOS="ifInUnknownProtos"</code> */
    public static final String IF_IN_UKNOWN_PROTOS = "ifInUnknownProtos";

    /** Constant <code>IF_OUT_OCTETS="ifOutOctets"</code> */
    public static final String IF_OUT_OCTETS = "ifOutOctets";

    /** Constant <code>IF_OUT_UCAST="ifOutUcastPkts"</code> */
    public static final String IF_OUT_UCAST = "ifOutUcastPkts";

    /** Constant <code>IF_OUT_NUCAST="ifOutNUcastPkts"</code> */
    public static final String IF_OUT_NUCAST = "ifOutNUcastPkts";

    /** Constant <code>IF_OUT_DISCARDS="ifOutDiscards"</code> */
    public static final String IF_OUT_DISCARDS = "ifOutDiscards";

    /** Constant <code>IF_OUT_ERRORS="ifOutErrors"</code> */
    public static final String IF_OUT_ERRORS = "ifOutErrors";

    /** Constant <code>IF_OUT_QLEN="ifOutQLen"</code> */
    public static final String IF_OUT_QLEN = "ifOutQLen";

    /** Constant <code>IF_SPECIFIC="ifSpecific"</code> */
    public static final String IF_SPECIFIC = "ifSpecific";

    /** Constant <code>ms_elemList</code> */
    public static final NamedSnmpVar[] ms_elemList = new NamedSnmpVar[] {
            new NamedSnmpVar(NamedSnmpVar.SNMPINT32, IF_INDEX, ".1.3.6.1.2.1.2.2.1.1", 1),
            new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, IF_DESCR, ".1.3.6.1.2.1.2.2.1.2", 2),
            new NamedSnmpVar(NamedSnmpVar.SNMPINT32, IF_TYPE, ".1.3.6.1.2.1.2.2.1.3", 3),
            new NamedSnmpVar(NamedSnmpVar.SNMPINT32, IF_MTU, ".1.3.6.1.2.1.2.2.1.4", 4),
            new NamedSnmpVar(NamedSnmpVar.SNMPGAUGE32, IF_SPEED, ".1.3.6.1.2.1.2.2.1.5", 5),
            new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, IF_PHYS_ADDR, ".1.3.6.1.2.1.2.2.1.6", 6),
            new NamedSnmpVar(NamedSnmpVar.SNMPINT32, IF_ADMIN_STATUS, ".1.3.6.1.2.1.2.2.1.7", 7),
            new NamedSnmpVar(NamedSnmpVar.SNMPINT32, IF_OPER_STATUS, ".1.3.6.1.2.1.2.2.1.8", 8),
            new NamedSnmpVar(NamedSnmpVar.SNMPTIMETICKS, IF_LAST_CHANGE, ".1.3.6.1.2.1.2.2.1.9", 9)
    // new NamedSnmpVar(NamedSnmpVar.SNMPCOUNTER32, IF_IN_OCTETS,
    // ".1.3.6.1.2.1.2.2.1.10", 10),
    // new NamedSnmpVar(NamedSnmpVar.SNMPCOUNTER32, IF_IN_UCAST,
    // ".1.3.6.1.2.1.2.2.1.11", 11),
    // new NamedSnmpVar(NamedSnmpVar.SNMPCOUNTER32, IF_IN_NUCAST,
    // ".1.3.6.1.2.1.2.2.1.12", 12),
    // new NamedSnmpVar(NamedSnmpVar.SNMPCOUNTER32, IF_IN_DISCARDS,
    // ".1.3.6.1.2.1.2.2.1.13", 13),
    // new NamedSnmpVar(NamedSnmpVar.SNMPCOUNTER32, IF_IN_ERRORS,
    // ".1.3.6.1.2.1.2.2.1.14", 14),
    // new NamedSnmpVar(NamedSnmpVar.SNMPCOUNTER32, IF_IN_UKNOWN_PROTOS,
    // ".1.3.6.1.2.1.2.2.1.15", 15),
    // new NamedSnmpVar(NamedSnmpVar.SNMPCOUNTER32, IF_OUT_OCTETS,
    // ".1.3.6.1.2.1.2.2.1.16", 16),
    // new NamedSnmpVar(NamedSnmpVar.SNMPCOUNTER32, IF_OUT_UCAST,
    // ".1.3.6.1.2.1.2.2.1.17", 17),
    // new NamedSnmpVar(NamedSnmpVar.SNMPCOUNTER32, IF_OUT_NUCAST,
    // ".1.3.6.1.2.1.2.2.1.18", 18),
    // new NamedSnmpVar(NamedSnmpVar.SNMPCOUNTER32, IF_OUT_DISCARDS,
    // ".1.3.6.1.2.1.2.2.1.19", 19),
    // new NamedSnmpVar(NamedSnmpVar.SNMPCOUNTER32, IF_OUT_ERRORS,
    // ".1.3.6.1.2.1.2.2.1.20", 20),
    // new NamedSnmpVar(NamedSnmpVar.SNMPGAUGE32, IF_OUT_QLEN,
    // ".1.3.6.1.2.1.2.2.1.21", 21),
    // new NamedSnmpVar(NamedSnmpVar.SNMPOBJECTID, IF_SPECIFIC,
    // ".1.3.6.1.2.1.2.2.1.22", 22)
    };

    /**
     * <P>
     * The TABLE_OID is the object identifier that represents the root of the
     * interface table in the MIB forest.
     * </P>
     */
    public static final String TABLE_OID = ".1.3.6.1.2.1.2.2.1"; // start of
                                                                 // table
                                                                 // (GETNEXT)

    /**
     * <P>
     * The class constructor used to initialize the object to its initial state.
     * Although the object's attributes and data can be changed after its
     * created, this constructor will initialize all the variables as per their
     * named varbind in the passed array. This array should have been collected
     * from an SnmpPduRequest that was received from a remote host.
     * </P>
     */
    public IfTableEntry() {
        super(ms_elemList);
    }

    /**
     * <p>
     * getIfType
     * </p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getIfType() {
        return getInt32(IfTableEntry.IF_TYPE);
    }

    /**
     * <p>
     * getIfAdminStatus
     * </p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getIfAdminStatus() {
        return getInt32(IfTableEntry.IF_ADMIN_STATUS);
    }

    /**
     * <p>
     * getIfDescr
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getIfDescr() {
        return getDisplayString(IfTableEntry.IF_DESCR);
    }

    /**
     * <p>
     * getPhysAddr
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPhysAddr() {
        return getHexString(IfTableEntry.IF_PHYS_ADDR);
    }

    /**
     * <p>
     * getIfOperStatus
     * </p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getIfOperStatus() {
        return getInt32(IfTableEntry.IF_OPER_STATUS);
    }

    /**
     * <p>
     * getIfSpeed
     * </p>
     *
     * @return a {@link java.lang.Long} object.
     */
    public Long getIfSpeed() {
        return getUInt32(IfTableEntry.IF_SPEED);
    }

}
