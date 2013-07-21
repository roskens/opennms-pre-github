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

package org.opennms.netmgt.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.netmgt.model.OnmsArpInterface.StatusType;

/**
 * The Class OnmsVlan.
 */
@XmlRootElement(name = "vlan")
@Entity
@Table(name = "vlan", uniqueConstraints = { @UniqueConstraint(columnNames = { "nodeId", "vlanId" }) })
public class OnmsVlan {

    /**
     * The Class VlanStatus.
     */
    @Embeddable
    public static class VlanStatus implements Comparable<VlanStatus>, Serializable {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -5676188320482765289L;

        /**
         * <p>
         * String identifiers for the enumeration of values:
         * </p>
         * .
         */
        public static final int VLAN_STATUS_UNKNOWN = 0;

        /** Constant <code>CISCOVTP_VLAN_STATUS_OPERATIONAL=1</code>. */
        public static final int CISCOVTP_VLAN_STATUS_OPERATIONAL = 1;

        /** Constant <code>CISCOVTP_VLAN_STATUS_SUSPENDED=2</code>. */
        public static final int CISCOVTP_VLAN_STATUS_SUSPENDED = 2;

        /** Constant <code>CISCOVTP_VLAN_STATUS_mtuTooBigForDevice=3</code>. */
        public static final int CISCOVTP_VLAN_STATUS_mtuTooBigForDevice = 3;

        /** Constant <code>CISCOVTP_VLAN_STATUS_mtuTooBigForTrunk=4</code>. */
        public static final int CISCOVTP_VLAN_STATUS_mtuTooBigForTrunk = 4;

        // RowStatus Definition and mapping
        /** The Constant ROWSTATUS_STARTING_INDEX. */
        public static final int ROWSTATUS_STARTING_INDEX = 4;

        /** The Constant SNMPV2C_ROWSTATUS_ACTIVE. */
        public static final int SNMPV2C_ROWSTATUS_ACTIVE = 5;

        /** The Constant SNMPV2C_ROWSTATUS_NOTINSERVICE. */
        public static final int SNMPV2C_ROWSTATUS_NOTINSERVICE = 6;

        /** The Constant SNMPV2C_ROWSTATUS_NOTREADY. */
        public static final int SNMPV2C_ROWSTATUS_NOTREADY = 7;

        /** The Constant SNMPV2C_ROWSTATUS_CREATEANDGO. */
        public static final int SNMPV2C_ROWSTATUS_CREATEANDGO = 8;

        /** The Constant SNMPV2C_ROWSTATUS_CREATEANDWAIT. */
        public static final int SNMPV2C_ROWSTATUS_CREATEANDWAIT = 9;

        /** The Constant SNMPV2C_ROWSTATUS_DESTROY. */
        public static final int SNMPV2C_ROWSTATUS_DESTROY = 10;

        /** The Constant s_order. */
        private static final Integer[] s_order = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        /** The m_vlan status. */
        private Integer m_vlanStatus;

        /** The Constant vlanStatusMap. */
        private static final Map<Integer, String> vlanStatusMap = new HashMap<Integer, String>();

        static {
            vlanStatusMap.put(0, "unknown");
            vlanStatusMap.put(1, "operational");
            vlanStatusMap.put(2, "ciscovtp/suspended");
            vlanStatusMap.put(3, "ciscovtp/mtuTooBigForDevice");
            vlanStatusMap.put(4, "ciscovtp/mtuTooBigForTrunk");
            vlanStatusMap.put(5, "rowStatus/active");
            vlanStatusMap.put(6, "rowStatus/notInService");
            vlanStatusMap.put(7, "rowStatus/notReady");
            vlanStatusMap.put(8, "rowStatus/createAndGo");
            vlanStatusMap.put(9, "rowStatus/createAndWait");
            vlanStatusMap.put(10, "rowStatus/destroy");
        }

        /**
         * Instantiates a new vlan status.
         */
        @SuppressWarnings("unused")
        private VlanStatus() {
        }

        /**
         * Instantiates a new vlan status.
         *
         * @param vlanType
         *            the vlan type
         */
        public VlanStatus(Integer vlanType) {
            m_vlanStatus = vlanType;
        }

        /**
         * Gets the int code.
         *
         * @return the int code
         */
        @Column(name = "vlanStatus")
        public Integer getIntCode() {
            return m_vlanStatus;
        }

        /**
         * Sets the int code.
         *
         * @param vlanType
         *            the new int code
         */
        public void setIntCode(Integer vlanType) {
            m_vlanStatus = vlanType;
        }

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(VlanStatus o) {
            return getIndex(m_vlanStatus) - getIndex(o.m_vlanStatus);
        }

        /**
         * Gets the index.
         *
         * @param code
         *            the code
         * @return the index
         */
        private static int getIndex(Integer code) {
            for (int i = 0; i < s_order.length; i++) {
                if (s_order[i] == code) {
                    return i;
                }
            }
            throw new IllegalArgumentException("illegal vlanStatus code '" + code + "'");
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof VlanStatus) {
                return m_vlanStatus.intValue() == ((VlanStatus) o).m_vlanStatus.intValue();
            }
            return false;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return String.valueOf(m_vlanStatus);
        }

        /**
         * Gets the.
         *
         * @param code
         *            the code
         * @return the vlan status
         */
        public static VlanStatus get(Integer code) {
            if (code == null)
                return VlanStatus.UNKNOWN;
            switch (code) {
            case VLAN_STATUS_UNKNOWN:
                return UNKNOWN;
            case CISCOVTP_VLAN_STATUS_OPERATIONAL:
                return CISCOVTP_OPERATIONAL;
            case CISCOVTP_VLAN_STATUS_SUSPENDED:
                return CISCOVTP_SUSPENDED;
            case CISCOVTP_VLAN_STATUS_mtuTooBigForDevice:
                return CISCOVTP_mtuTooBigForDevice;
            case CISCOVTP_VLAN_STATUS_mtuTooBigForTrunk:
                return CISCOVTP_mtuTooBigForTrunk;
            case SNMPV2C_ROWSTATUS_ACTIVE:
                return ROWSTATUS_ACTIVE;
            case SNMPV2C_ROWSTATUS_NOTINSERVICE:
                return ROWSTATUS_NOTINSERVICE;
            case SNMPV2C_ROWSTATUS_NOTREADY:
                return ROWSTATUS_NOTREADY;
            case SNMPV2C_ROWSTATUS_CREATEANDGO:
                return ROWSTATUS_CREATEANDGO;
            case SNMPV2C_ROWSTATUS_CREATEANDWAIT:
                return ROWSTATUS_CREATEANDWAIT;
            case SNMPV2C_ROWSTATUS_DESTROY:
                return ROWSTATUS_DESTROY;
            default:
                throw new IllegalArgumentException("Cannot create vlanStatus from code " + code);
            }
        }

        /**
         * <p>
         * getVlanStatusString
         * </p>
         * .
         *
         * @param code
         *            the code
         * @return a {@link java.lang.String} object.
         */
        /**
         */
        public static String getVlanStatusString(Integer code) {
            if (vlanStatusMap.containsKey(code))
                return vlanStatusMap.get(code);
            return null;
        }

        /** The unknown. */
        public static VlanStatus UNKNOWN = new VlanStatus(VLAN_STATUS_UNKNOWN);

        /** The ciscovtp operational. */
        public static VlanStatus CISCOVTP_OPERATIONAL = new VlanStatus(CISCOVTP_VLAN_STATUS_OPERATIONAL);

        /** The ciscovtp suspended. */
        public static VlanStatus CISCOVTP_SUSPENDED = new VlanStatus(CISCOVTP_VLAN_STATUS_SUSPENDED);

        /** The CISCOVT p_mtu too big for device. */
        public static VlanStatus CISCOVTP_mtuTooBigForDevice = new VlanStatus(CISCOVTP_VLAN_STATUS_mtuTooBigForDevice);

        /** The CISCOVT p_mtu too big for trunk. */
        public static VlanStatus CISCOVTP_mtuTooBigForTrunk = new VlanStatus(CISCOVTP_VLAN_STATUS_mtuTooBigForTrunk);

        /** The rowstatus active. */
        public static VlanStatus ROWSTATUS_ACTIVE = new VlanStatus(SNMPV2C_ROWSTATUS_ACTIVE);

        /** The rowstatus notinservice. */
        public static VlanStatus ROWSTATUS_NOTINSERVICE = new VlanStatus(SNMPV2C_ROWSTATUS_NOTINSERVICE);

        /** The rowstatus notready. */
        public static VlanStatus ROWSTATUS_NOTREADY = new VlanStatus(SNMPV2C_ROWSTATUS_NOTREADY);

        /** The rowstatus createandgo. */
        public static VlanStatus ROWSTATUS_CREATEANDGO = new VlanStatus(SNMPV2C_ROWSTATUS_CREATEANDGO);

        /** The rowstatus createandwait. */
        public static VlanStatus ROWSTATUS_CREATEANDWAIT = new VlanStatus(SNMPV2C_ROWSTATUS_CREATEANDWAIT);

        /** The rowstatus destroy. */
        public static VlanStatus ROWSTATUS_DESTROY = new VlanStatus(SNMPV2C_ROWSTATUS_DESTROY);

    }

    /**
     * The Class VlanType.
     */
    @Embeddable
    public static class VlanType implements Comparable<VlanType>, Serializable {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -7012640218990540145L;

        /**
         * <p>
         * String identifiers for the enumeration of values:
         * </p>
         * .
         */
        public static final int VLAN_TYPE_UNKNOWN = 0;

        /** The Constant VLAN_TYPE_VTP_ETHERNET. */
        public static final int VLAN_TYPE_VTP_ETHERNET = 1;

        /** The Constant VLAN_TYPE_VTP_FDDI. */
        public static final int VLAN_TYPE_VTP_FDDI = 2;

        /** The Constant VLAN_TYPE_VTP_TOKENRING. */
        public static final int VLAN_TYPE_VTP_TOKENRING = 3;

        /** The Constant VLAN_TYPE_VTP_FDDINET. */
        public static final int VLAN_TYPE_VTP_FDDINET = 4;

        /** The Constant VLAN_TYPE_VTP_TRNET. */
        public static final int VLAN_TYPE_VTP_TRNET = 5;

        /** The Constant VLAN_TYPE_VTP_DEPRECATED. */
        public static final int VLAN_TYPE_VTP_DEPRECATED = 6;

        /** The Constant VLAN_TYPE_EXTREME_LAYERTWO. */
        public static final int VLAN_TYPE_EXTREME_LAYERTWO = 7;

        /** The Constant THREECOM_STARTING_INDEX. */
        public static final int THREECOM_STARTING_INDEX = 7;

        /** The Constant VLAN_TYPE_THREECOM_vlanLayer2. */
        public static final int VLAN_TYPE_THREECOM_vlanLayer2 = 8;

        /** The Constant VLAN_TYPE_THREECOM_vlanUnspecifiedProtocols. */
        public static final int VLAN_TYPE_THREECOM_vlanUnspecifiedProtocols = 9;

        /** The Constant VLAN_TYPE_THREECOM_vlanIPProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanIPProtocol = 10;

        /** The Constant VLAN_TYPE_THREECOM_vlanIPXProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanIPXProtocol = 11;

        /** The Constant VLAN_TYPE_THREECOM_vlanAppleTalkProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanAppleTalkProtocol = 12;

        /** The Constant VLAN_TYPE_THREECOM_vlanXNSProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanXNSProtocol = 13;

        /** The Constant VLAN_TYPE_THREECOM_vlanISOProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanISOProtocol = 14;

        /** The Constant VLAN_TYPE_THREECOM_vlanDECNetProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanDECNetProtocol = 15;

        /** The Constant VLAN_TYPE_THREECOM_vlanNetBIOSProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanNetBIOSProtocol = 16;

        /** The Constant VLAN_TYPE_THREECOM_vlanSNAProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanSNAProtocol = 17;

        /** The Constant VLAN_TYPE_THREECOM_vlanVINESProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanVINESProtocol = 18;

        /** The Constant VLAN_TYPE_THREECOM_vlanX25Protocol. */
        public static final int VLAN_TYPE_THREECOM_vlanX25Protocol = 19;

        /** The Constant VLAN_TYPE_THREECOM_vlanIGMPProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanIGMPProtocol = 20;

        /** The Constant VLAN_TYPE_THREECOM_vlanSessionLayer. */
        public static final int VLAN_TYPE_THREECOM_vlanSessionLayer = 21;

        /** The Constant VLAN_TYPE_THREECOM_vlanNetBeui. */
        public static final int VLAN_TYPE_THREECOM_vlanNetBeui = 22;

        /** The Constant VLAN_TYPE_THREECOM_vlanLayeredProtocols. */
        public static final int VLAN_TYPE_THREECOM_vlanLayeredProtocols = 23;

        /** The Constant VLAN_TYPE_THREECOM_vlanIPXIIProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanIPXIIProtocol = 24;

        /** The Constant VLAN_TYPE_THREECOM_vlanIPX8022Protocol. */
        public static final int VLAN_TYPE_THREECOM_vlanIPX8022Protocol = 25;

        /** The Constant VLAN_TYPE_THREECOM_vlanIPX8023Protocol. */
        public static final int VLAN_TYPE_THREECOM_vlanIPX8023Protocol = 26;

        /** The Constant VLAN_TYPE_THREECOM_vlanIPX8022SNAPProtocol. */
        public static final int VLAN_TYPE_THREECOM_vlanIPX8022SNAPProtocol = 27;

        /**
         * vlanLayer2 (1), vlanUnspecifiedProtocols (2), vlanIPProtocol (3),
         * vlanIPXProtocol (4), vlanAppleTalkProtocol (5), vlanXNSProtocol (6),
         * vlanISOProtocol (7), vlanDECNetProtocol (8), vlanNetBIOSProtocol (9),
         * vlanSNAProtocol (10), vlanVINESProtocol (11), vlanX25Protocol (12),
         * vlanIGMPProtocol (13), vlanSessionLayer (14), vlanNetBeui (15),
         * vlanLayeredProtocols (16), vlanIPXIIProtocol (17), vlanIPX8022Protocol
         * (18), vlanIPX8023Protocol (19), vlanIPX8022SNAPProtocol (20).
         */

        private static final Integer[] s_order = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
                19, 20, 21, 22, 23, 24, 25, 26, 27 };

        /** The m_vlan type. */
        private Integer m_vlanType;

        /** The Constant vlanTypeMap. */
        private static final Map<Integer, String> vlanTypeMap = new HashMap<Integer, String>();

        static {
            vlanTypeMap.put(0, "Unknown");
            vlanTypeMap.put(1, "Ethernet");
            vlanTypeMap.put(2, "CiscoVtp/FDDI");
            vlanTypeMap.put(3, "CiscoVtp/TokenRing");
            vlanTypeMap.put(4, "CiscoVtp/FDDINet");
            vlanTypeMap.put(5, "CiscoVtp/TRNet");
            vlanTypeMap.put(6, "CiscoVtp/Deprecated");
            vlanTypeMap.put(7, "Extreme/LayerTwo");
            vlanTypeMap.put(8, "3com/vlanLayer2");
            vlanTypeMap.put(9, "3com/vlanUnspecifiedProtocols");
            vlanTypeMap.put(10, "3com/vlanIPProtocol");
            vlanTypeMap.put(11, "3com/vlanIPXProtocol");
            vlanTypeMap.put(12, "3com/vlanAppleTalkProtocol");
            vlanTypeMap.put(13, "3com/vlanXNSProtocol");
            vlanTypeMap.put(14, "3com/vlanISOProtocol");
            vlanTypeMap.put(15, "3com/vlanDECNetProtocol");
            vlanTypeMap.put(16, "3com/vlanNetBIOSProtocol");
            vlanTypeMap.put(17, "3com/vlanSNAProtocol");
            vlanTypeMap.put(18, "3com/vlanVINESProtocol");
            vlanTypeMap.put(19, "3com/vlanX25Protocol");
            vlanTypeMap.put(20, "3com/vlanIGMPProtocol");
            vlanTypeMap.put(21, "3com/vlanSessionLayer");
            vlanTypeMap.put(22, "3com/vlanNetBeui");
            vlanTypeMap.put(23, "3com/vlanLayeredProtocols");
            vlanTypeMap.put(24, "3com/vlanIPXIIProtocol");
            vlanTypeMap.put(25, "3com/vlanIPX8022Protocol");
            vlanTypeMap.put(26, "3com/vlanIPX8023Protocol");
            vlanTypeMap.put(27, "3com/vlanIPX8022SNAPProtocol");
        }

        /**
         * Instantiates a new vlan type.
         */
        @SuppressWarnings("unused")
        private VlanType() {
        }

        /**
         * Instantiates a new vlan type.
         *
         * @param vlanType
         *            the vlan type
         */
        public VlanType(Integer vlanType) {
            m_vlanType = vlanType;
        }

        /**
         * Gets the int code.
         *
         * @return the int code
         */
        @Column(name = "vlanType")
        public Integer getIntCode() {
            return m_vlanType;
        }

        /**
         * Sets the int code.
         *
         * @param vlanType
         *            the new int code
         */
        public void setIntCode(Integer vlanType) {
            m_vlanType = vlanType;
        }

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(VlanType o) {
            return getIndex(m_vlanType) - getIndex(o.m_vlanType);
        }

        /**
         * Gets the index.
         *
         * @param code
         *            the code
         * @return the index
         */
        private static int getIndex(Integer code) {
            for (int i = 0; i < s_order.length; i++) {
                if (s_order[i] == code) {
                    return i;
                }
            }
            throw new IllegalArgumentException("illegal vlanType code '" + code + "'");
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof VlanType) {
                return m_vlanType.intValue() == ((VlanType) o).m_vlanType.intValue();
            }
            return false;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return String.valueOf(m_vlanType);
        }

        /**
         * Gets the.
         *
         * @param code
         *            the code
         * @return the vlan type
         */
        public static VlanType get(Integer code) {
            if (code == null)
                return VlanType.UNKNOWN;
            switch (code) {
            case VLAN_TYPE_UNKNOWN:
                return UNKNOWN;
            case VLAN_TYPE_VTP_ETHERNET:
                return CISCO_VTP_ETHERNET;
            case VLAN_TYPE_VTP_FDDI:
                return CISCO_VTP_FDDI;
            case VLAN_TYPE_VTP_TOKENRING:
                return CISCO_VTP_TOKENRING;
            case VLAN_TYPE_VTP_FDDINET:
                return CISCO_VTP_FDDINET;
            case VLAN_TYPE_VTP_TRNET:
                return CISCO_VTP_TRNET;
            case VLAN_TYPE_VTP_DEPRECATED:
                return CISCO_VTP_DEPRECATED;
            case VLAN_TYPE_EXTREME_LAYERTWO:
                return EXTREME_LAYER2;
            case VLAN_TYPE_THREECOM_vlanLayer2:
                return THREECOM_vlanLayer2;
            case VLAN_TYPE_THREECOM_vlanUnspecifiedProtocols:
                return THREECOM_vlanUnspecifiedProtocols;
            case VLAN_TYPE_THREECOM_vlanIPProtocol:
                return THREECOM_vlanIPProtocol;
            case VLAN_TYPE_THREECOM_vlanIPXProtocol:
                return THREECOM_vlanIPXProtocol;
            case VLAN_TYPE_THREECOM_vlanAppleTalkProtocol:
                return THREECOM_vlanAppleTalkProtocol;
            case VLAN_TYPE_THREECOM_vlanXNSProtocol:
                return THREECOM_vlanXNSProtocol;
            case VLAN_TYPE_THREECOM_vlanISOProtocol:
                return THREECOM_vlanISOProtocol;
            case VLAN_TYPE_THREECOM_vlanDECNetProtocol:
                return THREECOM_vlanDECNetProtocol;
            case VLAN_TYPE_THREECOM_vlanNetBIOSProtocol:
                return THREECOM_vlanNetBIOSProtocol;
            case VLAN_TYPE_THREECOM_vlanSNAProtocol:
                return THREECOM_vlanSNAProtocol;
            case VLAN_TYPE_THREECOM_vlanVINESProtocol:
                return THREECOM_vlanVINESProtocol;
            case VLAN_TYPE_THREECOM_vlanX25Protocol:
                return THREECOM_vlanX25Protocol;
            case VLAN_TYPE_THREECOM_vlanIGMPProtocol:
                return THREECOM_vlanIGMPProtocol;
            case VLAN_TYPE_THREECOM_vlanSessionLayer:
                return THREECOM_vlanSessionLayer;
            case VLAN_TYPE_THREECOM_vlanNetBeui:
                return THREECOM_vlanNetBeui;
            case VLAN_TYPE_THREECOM_vlanLayeredProtocols:
                return THREECOM_vlanLayeredProtocols;
            case VLAN_TYPE_THREECOM_vlanIPXIIProtocol:
                return THREECOM_vlanIPXIIProtocol;
            case VLAN_TYPE_THREECOM_vlanIPX8022Protocol:
                return THREECOM_vlanIPX8022Protocol;
            case VLAN_TYPE_THREECOM_vlanIPX8023Protocol:
                return THREECOM_vlanIPX8023Protocol;
            case VLAN_TYPE_THREECOM_vlanIPX8022SNAPProtocol:
                return THREECOM_vlanIPX8022SNAPProtocol;
            default:
                throw new IllegalArgumentException("Cannot create vlanType from code " + code);
            }
        }

        /**
         * <p>
         * getVlanTypeString
         * </p>
         * .
         *
         * @param code
         *            the code
         * @return a {@link java.lang.String} object.
         */
        /**
         */
        public static String getVlanTypeString(Integer code) {
            if (vlanTypeMap.containsKey(code))
                return vlanTypeMap.get(code);
            return null;
        }

        /** The unknown. */
        public static VlanType UNKNOWN = new VlanType(VLAN_TYPE_UNKNOWN);

        /** The cisco vtp ethernet. */
        public static VlanType CISCO_VTP_ETHERNET = new VlanType(VLAN_TYPE_VTP_ETHERNET);

        /** The cisco vtp fddi. */
        public static VlanType CISCO_VTP_FDDI = new VlanType(VLAN_TYPE_VTP_FDDI);

        /** The cisco vtp tokenring. */
        public static VlanType CISCO_VTP_TOKENRING = new VlanType(VLAN_TYPE_VTP_TOKENRING);

        /** The cisco vtp fddinet. */
        public static VlanType CISCO_VTP_FDDINET = new VlanType(VLAN_TYPE_VTP_FDDINET);

        /** The cisco vtp trnet. */
        public static VlanType CISCO_VTP_TRNET = new VlanType(VLAN_TYPE_VTP_TRNET);

        /** The cisco vtp deprecated. */
        public static VlanType CISCO_VTP_DEPRECATED = new VlanType(VLAN_TYPE_VTP_DEPRECATED);

        /** The EXTREM e_ laye r2. */
        public static VlanType EXTREME_LAYER2 = new VlanType(VLAN_TYPE_EXTREME_LAYERTWO);

        /** The THREECO m_vlan layer2. */
        public static VlanType THREECOM_vlanLayer2 = new VlanType(VLAN_TYPE_THREECOM_vlanLayer2);

        /** The THREECO m_vlan unspecified protocols. */
        public static VlanType THREECOM_vlanUnspecifiedProtocols = new VlanType(
                                                                                VLAN_TYPE_THREECOM_vlanUnspecifiedProtocols);

        /** The THREECO m_vlan ip protocol. */
        public static VlanType THREECOM_vlanIPProtocol = new VlanType(VLAN_TYPE_THREECOM_vlanIPProtocol);

        /** The THREECO m_vlan ipx protocol. */
        public static VlanType THREECOM_vlanIPXProtocol = new VlanType(VLAN_TYPE_THREECOM_vlanIPXProtocol);

        /** The THREECO m_vlan apple talk protocol. */
        public static VlanType THREECOM_vlanAppleTalkProtocol = new VlanType(VLAN_TYPE_THREECOM_vlanAppleTalkProtocol);

        /** The THREECO m_vlan xns protocol. */
        public static VlanType THREECOM_vlanXNSProtocol = new VlanType(VLAN_TYPE_THREECOM_vlanXNSProtocol);

        /** The THREECO m_vlan iso protocol. */
        public static VlanType THREECOM_vlanISOProtocol = new VlanType(VLAN_TYPE_THREECOM_vlanISOProtocol);

        /** The THREECO m_vlan dec net protocol. */
        public static VlanType THREECOM_vlanDECNetProtocol = new VlanType(VLAN_TYPE_THREECOM_vlanDECNetProtocol);

        /** The THREECO m_vlan net bios protocol. */
        public static VlanType THREECOM_vlanNetBIOSProtocol = new VlanType(VLAN_TYPE_THREECOM_vlanNetBIOSProtocol);

        /** The THREECO m_vlan sna protocol. */
        public static VlanType THREECOM_vlanSNAProtocol = new VlanType(VLAN_TYPE_THREECOM_vlanSNAProtocol);

        /** The THREECO m_vlan vines protocol. */
        public static VlanType THREECOM_vlanVINESProtocol = new VlanType(VLAN_TYPE_THREECOM_vlanVINESProtocol);

        /** The THREECO m_vlan x25 protocol. */
        public static VlanType THREECOM_vlanX25Protocol = new VlanType(VLAN_TYPE_THREECOM_vlanX25Protocol);

        /** The THREECO m_vlan igmp protocol. */
        public static VlanType THREECOM_vlanIGMPProtocol = new VlanType(VLAN_TYPE_THREECOM_vlanIGMPProtocol);

        /** The THREECO m_vlan session layer. */
        public static VlanType THREECOM_vlanSessionLayer = new VlanType(VLAN_TYPE_THREECOM_vlanSessionLayer);

        /** The THREECO m_vlan net beui. */
        public static VlanType THREECOM_vlanNetBeui = new VlanType(VLAN_TYPE_THREECOM_vlanNetBeui);

        /** The THREECO m_vlan layered protocols. */
        public static VlanType THREECOM_vlanLayeredProtocols = new VlanType(VLAN_TYPE_THREECOM_vlanLayeredProtocols);

        /** The THREECO m_vlan ipxii protocol. */
        public static VlanType THREECOM_vlanIPXIIProtocol = new VlanType(VLAN_TYPE_THREECOM_vlanIPXIIProtocol);

        /** The THREECO m_vlan ip x8022 protocol. */
        public static VlanType THREECOM_vlanIPX8022Protocol = new VlanType(VLAN_TYPE_THREECOM_vlanIPX8022Protocol);

        /** The THREECO m_vlan ip x8023 protocol. */
        public static VlanType THREECOM_vlanIPX8023Protocol = new VlanType(VLAN_TYPE_THREECOM_vlanIPX8023Protocol);

        /** The THREECO m_vlan ip x8022 snap protocol. */
        public static VlanType THREECOM_vlanIPX8022SNAPProtocol = new VlanType(
                                                                               VLAN_TYPE_THREECOM_vlanIPX8022SNAPProtocol);
    }

    /** The m_id. */
    private Integer m_id;

    /** The m_node. */
    private OnmsNode m_node;

    /** The m_vlan id. */
    private Integer m_vlanId;

    /** The m_vlan name. */
    private String m_vlanName;

    /** The m_vlan type. */
    private VlanType m_vlanType = VlanType.UNKNOWN;

    /** The m_vlan status. */
    private VlanStatus m_vlanStatus = VlanStatus.UNKNOWN;

    /** The m_status. */
    private StatusType m_status = StatusType.UNKNOWN;

    /** The m_last poll time. */
    private Date m_lastPollTime;

    /**
     * Instantiates a new onms vlan.
     */
    public OnmsVlan() {
    }

    /**
     * Instantiates a new onms vlan.
     *
     * @param index
     *            the index
     * @param name
     *            the name
     * @param status
     *            the status
     * @param type
     *            the type
     */
    public OnmsVlan(final int index, final String name, final VlanStatus status, final VlanType type) {
        m_vlanId = index;
        m_vlanName = name;
        m_vlanStatus = status;
        m_vlanType = type;
    }

    /**
     * Instantiates a new onms vlan.
     *
     * @param index
     *            the index
     * @param name
     *            the name
     * @param status
     *            the status
     */
    public OnmsVlan(final int index, final String name, final VlanStatus status) {
        m_vlanId = index;
        m_vlanName = name;
        m_vlanStatus = status;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Id
    @Column(nullable = false)
    @XmlTransient
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    public Integer getId() {
        return m_id;
    }

    /**
     * Gets the interface id.
     *
     * @return the interface id
     */
    @XmlID
    @XmlAttribute(name = "id")
    @Transient
    public String getInterfaceId() {
        return getId().toString();
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(final Integer id) {
        m_id = id;
    }

    /**
     * <p>
     * getNode
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.model.OnmsNode} object.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "nodeId")
    @XmlElement(name = "nodeId")
    @XmlIDREF
    public OnmsNode getNode() {
        return m_node;
    }

    /**
     * <p>
     * setNode
     * </p>
     * .
     *
     * @param node
     *            a {@link org.opennms.netmgt.model.OnmsNode} object.
     */
    public void setNode(final OnmsNode node) {
        m_node = node;
    }

    /**
     * Gets the vlan id.
     *
     * @return the vlan id
     */
    @XmlAttribute
    @Column(nullable = false)
    public Integer getVlanId() {
        return m_vlanId;
    }

    /**
     * Sets the vlan id.
     *
     * @param vlanId
     *            the new vlan id
     */
    public void setVlanId(final Integer vlanId) {
        m_vlanId = vlanId;
    }

    /**
     * Gets the vlan name.
     *
     * @return the vlan name
     */
    @XmlAttribute(name = "name")
    @Column(nullable = false)
    public String getVlanName() {
        return m_vlanName;
    }

    /**
     * Sets the vlan name.
     *
     * @param vlanName
     *            the new vlan name
     */
    public void setVlanName(final String vlanName) {
        m_vlanName = vlanName;
    }

    /**
     * Gets the vlan type.
     *
     * @return the vlan type
     */
    @XmlAttribute(name = "type")
    @Column
    public VlanType getVlanType() {
        return m_vlanType;
    }

    /**
     * Sets the vlan type.
     *
     * @param vlanType
     *            the new vlan type
     */
    public void setVlanType(final VlanType vlanType) {
        m_vlanType = vlanType;
    }

    /**
     * Gets the vlan status.
     *
     * @return the vlan status
     */
    @XmlAttribute
    @Column
    public VlanStatus getVlanStatus() {
        return m_vlanStatus;
    }

    /**
     * Sets the vlan status.
     *
     * @param vlanStatus
     *            the new vlan status
     */
    public void setVlanStatus(final VlanStatus vlanStatus) {
        m_vlanStatus = vlanStatus;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    @XmlAttribute
    @Column(nullable = false)
    public StatusType getStatus() {
        return m_status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(final StatusType status) {
        m_status = status;
    }

    /**
     * Gets the last poll time.
     *
     * @return the last poll time
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @XmlElement
    public Date getLastPollTime() {
        return m_lastPollTime;
    }

    /**
     * Sets the last poll time.
     *
     * @param lastPollTime
     *            the new last poll time
     */
    public void setLastPollTime(final Date lastPollTime) {
        m_lastPollTime = lastPollTime;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("dbId", m_id).append("node", m_node).append("id", m_vlanId).append("name",
                                                                                                                   m_vlanName).append("type",
                                                                                                                                      m_vlanType).append("status",
                                                                                                                                                         m_vlanStatus).append("dbStatus",
                                                                                                                                                                              m_status).append("lastPollTime",
                                                                                                                                                                                               m_lastPollTime).toString();
    }
}
