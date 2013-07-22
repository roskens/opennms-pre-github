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
 * <p>
 * AtInterface class.
 * </p>
 *
 * @author <a href="mailto:antonio@opennms.it">Antonio Russo</a>
 * @version $Id: $
 */
@XmlRootElement(name = "stpNode")
@Entity
@Table(name = "stpNode", uniqueConstraints = { @UniqueConstraint(columnNames = { "nodeId", "baseVlan" }) })
public class OnmsStpNode {

    /**
     * The Class BridgeBaseType.
     */
    @Embeddable
    public static class BridgeBaseType implements Comparable<BridgeBaseType>, Serializable {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 4211573691385106051L;

        /** The Constant BASE_TYPE_UNKNOWN. */
        public static final int BASE_TYPE_UNKNOWN = 1;

        /** The Constant BASE_TYPE_TRANSPARENT_ONLY. */
        public static final int BASE_TYPE_TRANSPARENT_ONLY = 2;

        /** The Constant BASE_TYPE_SOURCEROUTE_ONLY. */
        public static final int BASE_TYPE_SOURCEROUTE_ONLY = 3;

        /** The Constant BASE_TYPE_SRT. */
        public static final int BASE_TYPE_SRT = 4;

        /** The Constant s_order. */
        private static final Integer[] s_order = { 1, 2, 3, 4 };

        /** The m_basebridgetype. */
        private Integer m_basebridgetype;

        /** The Constant baseBridgeTypeMap. */
        private static final Map<Integer, String> baseBridgeTypeMap = new HashMap<Integer, String>();

        static {
            baseBridgeTypeMap.put(1, "unknown");
            baseBridgeTypeMap.put(2, "transparent-only");
            baseBridgeTypeMap.put(3, "sourceroute-only");
            baseBridgeTypeMap.put(4, "srt");
        }

        /**
         * Instantiates a new bridge base type.
         */
        @SuppressWarnings("unused")
        private BridgeBaseType() {
        }

        /**
         * Instantiates a new bridge base type.
         *
         * @param bridgeBaseType
         *            the bridge base type
         */
        public BridgeBaseType(Integer bridgeBaseType) {
            m_basebridgetype = bridgeBaseType;
        }

        /**
         * Gets the int code.
         *
         * @return the int code
         */
        @Column(name = "baseType")
        public Integer getIntCode() {
            return m_basebridgetype;
        }

        /**
         * Sets the int code.
         *
         * @param baseBridgeType
         *            the new int code
         */
        public void setIntCode(Integer baseBridgeType) {
            m_basebridgetype = baseBridgeType;
        }

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(BridgeBaseType o) {
            return getIndex(m_basebridgetype) - getIndex(o.m_basebridgetype);
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
            throw new IllegalArgumentException("illegal baseBridgeType code '" + code + "'");
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof BridgeBaseType) {
                return m_basebridgetype.intValue() == ((BridgeBaseType) o).m_basebridgetype.intValue();
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
            return String.valueOf(m_basebridgetype);
        }

        /**
         * Gets the.
         *
         * @param code
         *            the code
         * @return the bridge base type
         */
        public static BridgeBaseType get(Integer code) {
            if (code == null) {
                return BridgeBaseType.UNKNOWN;
            }
            switch (code) {
            case BASE_TYPE_UNKNOWN:
                return UNKNOWN;
            case BASE_TYPE_TRANSPARENT_ONLY:
                return TRANSPARENT_ONLY;
            case BASE_TYPE_SOURCEROUTE_ONLY:
                return SOURCEROUTE_ONLY;
            case BASE_TYPE_SRT:
                return SRT;
            default:
                throw new IllegalArgumentException("Cannot create BridgeBaseType from code " + code);
            }
        }

        /**
         * <p>
         * getBridgeBaseTypeString
         * </p>
         * .
         *
         * @param code
         *            the code
         * @return a {@link java.lang.String} object.
         */
        /**
         */
        public static String getBridgeBaseTypeString(Integer code) {
            if (baseBridgeTypeMap.containsKey(code)) {
                return baseBridgeTypeMap.get(code);
            }
            return null;
        }

        /** The unknown. */
        public static BridgeBaseType UNKNOWN = new BridgeBaseType(BASE_TYPE_UNKNOWN);

        /** The transparent only. */
        public static BridgeBaseType TRANSPARENT_ONLY = new BridgeBaseType(BASE_TYPE_TRANSPARENT_ONLY);

        /** The sourceroute only. */
        public static BridgeBaseType SOURCEROUTE_ONLY = new BridgeBaseType(BASE_TYPE_SOURCEROUTE_ONLY);

        /** The srt. */
        public static BridgeBaseType SRT = new BridgeBaseType(BASE_TYPE_SRT);

    }

    /**
     * The Class StpProtocolSpecification.
     */
    @Embeddable
    public static class StpProtocolSpecification implements Comparable<StpProtocolSpecification>, Serializable {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -1815947324977781143L;

        /** The Constant STP_PROTOCOL_SPECIFICATION_UNKNOWN. */
        public static final int STP_PROTOCOL_SPECIFICATION_UNKNOWN = 1;

        /** The Constant STP_PROTOCOL_SPECIFICATION_DECLB100. */
        public static final int STP_PROTOCOL_SPECIFICATION_DECLB100 = 2;

        /** The Constant STP_PROTOCOL_SPECIFICATION_IEEE8021D. */
        public static final int STP_PROTOCOL_SPECIFICATION_IEEE8021D = 3;

        /** The Constant s_order. */
        private static final Integer[] s_order = { 1, 2, 3 };

        /** The m_stpprotocolspecification. */
        private Integer m_stpprotocolspecification;

        /** The Constant stpProtocolSpecificationMap. */
        private static final Map<Integer, String> stpProtocolSpecificationMap = new HashMap<Integer, String>();

        static {
            stpProtocolSpecificationMap.put(1, "unknown");
            stpProtocolSpecificationMap.put(2, "decLb100");
            stpProtocolSpecificationMap.put(3, "ieee8021d");
        }

        /**
         * Instantiates a new stp protocol specification.
         */
        @SuppressWarnings("unused")
        private StpProtocolSpecification() {
        }

        /**
         * Instantiates a new stp protocol specification.
         *
         * @param stpprotocolspecification
         *            the stpprotocolspecification
         */
        public StpProtocolSpecification(Integer stpprotocolspecification) {
            m_stpprotocolspecification = stpprotocolspecification;
        }

        /**
         * Gets the int code.
         *
         * @return the int code
         */
        @Column(name = "stpProtocolSpecification")
        public Integer getIntCode() {
            return m_stpprotocolspecification;
        }

        /**
         * Sets the int code.
         *
         * @param stpProtocolSpecification
         *            the new int code
         */
        public void setIntCode(Integer stpProtocolSpecification) {
            m_stpprotocolspecification = stpProtocolSpecification;
        }

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(StpProtocolSpecification o) {
            return getIndex(m_stpprotocolspecification) - getIndex(o.m_stpprotocolspecification);
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
            throw new IllegalArgumentException("illegal StpProtocolSpecification code '" + code + "'");
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof StpProtocolSpecification) {
                return m_stpprotocolspecification.intValue() == ((StpProtocolSpecification) o).m_stpprotocolspecification.intValue();
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
            return String.valueOf(m_stpprotocolspecification);
        }

        /**
         * Gets the.
         *
         * @param code
         *            the code
         * @return the stp protocol specification
         */
        public static StpProtocolSpecification get(Integer code) {
            if (code == null) {
                return StpProtocolSpecification.UNKNOWN;
            }
            switch (code) {
            case STP_PROTOCOL_SPECIFICATION_UNKNOWN:
                return UNKNOWN;
            case STP_PROTOCOL_SPECIFICATION_DECLB100:
                return DECLB100;
            case STP_PROTOCOL_SPECIFICATION_IEEE8021D:
                return IEEE8021D;
            default:
                throw new IllegalArgumentException("Cannot create StpProtocolSpecification from code " + code);
            }
        }

        /**
         * <p>
         * getStpProtocolSpecificationString
         * </p>
         * .
         *
         * @param code
         *            the code
         * @return a {@link java.lang.String} object.
         */
        /**
         */
        public static String getStpProtocolSpecificationString(Integer code) {
            if (stpProtocolSpecificationMap.containsKey(code)) {
                return stpProtocolSpecificationMap.get(code);
            }
            return null;
        }

        /** The unknown. */
        public static StpProtocolSpecification UNKNOWN = new StpProtocolSpecification(
                                                                                      STP_PROTOCOL_SPECIFICATION_UNKNOWN);

        /** The DECL b100. */
        public static StpProtocolSpecification DECLB100 = new StpProtocolSpecification(
                                                                                       STP_PROTOCOL_SPECIFICATION_DECLB100);

        /** The IEE e8021 d. */
        public static StpProtocolSpecification IEEE8021D = new StpProtocolSpecification(
                                                                                        STP_PROTOCOL_SPECIFICATION_IEEE8021D);

    }

    /** The m_id. */
    private Integer m_id;

    /** The m_node. */
    private OnmsNode m_node;

    /** The m_base bridge address. */
    private String m_baseBridgeAddress;

    /** The m_base num ports. */
    private Integer m_baseNumPorts;

    /** The m_base type. */
    private BridgeBaseType m_baseType;

    /** The m_stp protocol specification. */
    private StpProtocolSpecification m_stpProtocolSpecification;

    /** The m_stp priority. */
    private Integer m_stpPriority;

    /** The m_stp designated root. */
    private String m_stpDesignatedRoot;

    /** The m_stp root cost. */
    private Integer m_stpRootCost;

    /** The m_stp root port. */
    private Integer m_stpRootPort;

    /** The m_status. */
    private StatusType m_status = StatusType.UNKNOWN;

    /** The m_last poll time. */
    private Date m_lastPollTime;

    /** The m_base vlan. */
    private Integer m_baseVlan;

    /** The m_base vlan name. */
    private String m_baseVlanName;

    /**
     * Instantiates a new onms stp node.
     */
    public OnmsStpNode() {
    }

    /**
     * Instantiates a new onms stp node.
     *
     * @param node
     *            the node
     * @param vlanIndex
     *            the vlan index
     */
    public OnmsStpNode(final OnmsNode node, final Integer vlanIndex) {
        m_node = node;
        m_baseVlan = vlanIndex;
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
     * Gets the node.
     *
     * @return the node
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "nodeId")
    @XmlElement(name = "nodeId")
    @XmlIDREF
    public OnmsNode getNode() {
        return m_node;
    }

    /**
     * Sets the node.
     *
     * @param node
     *            the new node
     */
    public void setNode(final OnmsNode node) {
        m_node = node;
    }

    /**
     * Gets the base bridge address.
     *
     * @return the base bridge address
     */
    @XmlElement
    @Column(length = 12, nullable = false)
    public String getBaseBridgeAddress() {
        return m_baseBridgeAddress;
    }

    /**
     * Sets the base bridge address.
     *
     * @param baseBridgeAddress
     *            the new base bridge address
     */
    public void setBaseBridgeAddress(final String baseBridgeAddress) {
        m_baseBridgeAddress = baseBridgeAddress;
    }

    /**
     * Gets the base num ports.
     *
     * @return the base num ports
     */
    @XmlElement
    @Column
    public Integer getBaseNumPorts() {
        return m_baseNumPorts;
    }

    /**
     * Sets the base num ports.
     *
     * @param baseNumPorts
     *            the new base num ports
     */
    public void setBaseNumPorts(final Integer baseNumPorts) {
        m_baseNumPorts = baseNumPorts;
    }

    /**
     * Gets the base type.
     *
     * @return the base type
     */
    @XmlElement
    @Column
    public BridgeBaseType getBaseType() {
        return m_baseType;
    }

    /**
     * Sets the base type.
     *
     * @param baseType
     *            the new base type
     */
    public void setBaseType(final BridgeBaseType baseType) {
        m_baseType = baseType;
    }

    /**
     * Gets the stp protocol specification.
     *
     * @return the stp protocol specification
     */
    @XmlElement
    @Column
    public StpProtocolSpecification getStpProtocolSpecification() {
        return m_stpProtocolSpecification;
    }

    /**
     * Sets the stp protocol specification.
     *
     * @param stpProtocolSpecification
     *            the new stp protocol specification
     */
    public void setStpProtocolSpecification(final StpProtocolSpecification stpProtocolSpecification) {
        m_stpProtocolSpecification = stpProtocolSpecification;
    }

    /**
     * Gets the stp priority.
     *
     * @return the stp priority
     */
    @XmlElement
    @Column
    public Integer getStpPriority() {
        return m_stpPriority;
    }

    /**
     * Sets the stp priority.
     *
     * @param stpPriority
     *            the new stp priority
     */
    public void setStpPriority(final Integer stpPriority) {
        m_stpPriority = stpPriority;
    }

    /**
     * Gets the stp designated root.
     *
     * @return the stp designated root
     */
    @XmlElement
    @Column(length = 16)
    public String getStpDesignatedRoot() {
        return m_stpDesignatedRoot;
    }

    /**
     * Sets the stp designated root.
     *
     * @param stpDesignatedRoot
     *            the new stp designated root
     */
    public void setStpDesignatedRoot(final String stpDesignatedRoot) {
        m_stpDesignatedRoot = stpDesignatedRoot;
    }

    /**
     * Gets the stp root cost.
     *
     * @return the stp root cost
     */
    @XmlElement
    @Column
    public Integer getStpRootCost() {
        return m_stpRootCost;
    }

    /**
     * Sets the stp root cost.
     *
     * @param stpRootCost
     *            the new stp root cost
     */
    public void setStpRootCost(final Integer stpRootCost) {
        m_stpRootCost = stpRootCost;
    }

    /**
     * Gets the stp root port.
     *
     * @return the stp root port
     */
    @XmlElement
    @Column
    public Integer getStpRootPort() {
        return m_stpRootPort;
    }

    /**
     * Sets the stp root port.
     *
     * @param stpRootPort
     *            the new stp root port
     */
    public void setStpRootPort(final Integer stpRootPort) {
        m_stpRootPort = stpRootPort;
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

    /**
     * Gets the base vlan.
     *
     * @return the base vlan
     */
    @XmlElement
    @Column(nullable = false)
    public Integer getBaseVlan() {
        return m_baseVlan;
    }

    /**
     * Sets the base vlan.
     *
     * @param baseVlan
     *            the new base vlan
     */
    public void setBaseVlan(final Integer baseVlan) {
        m_baseVlan = baseVlan;
    }

    /**
     * Gets the base vlan name.
     *
     * @return the base vlan name
     */
    @XmlElement
    @Column(length = 32)
    public String getBaseVlanName() {
        return m_baseVlanName;
    }

    /**
     * Sets the base vlan name.
     *
     * @param baseVlanName
     *            the new base vlan name
     */
    public void setBaseVlanName(final String baseVlanName) {
        m_baseVlanName = baseVlanName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", m_id).append("node", m_node).append("baseBridgeAddress",
                                                                                          m_baseBridgeAddress).append("baseNumPorts",
                                                                                                                      m_baseNumPorts).append("baseType",
                                                                                                                                             m_baseType).append("stpProtocolSpecification",
                                                                                                                                                                m_stpProtocolSpecification).append("stpPriority",
                                                                                                                                                                                                   m_stpPriority).append("stpDesignatedRoot",
                                                                                                                                                                                                                         m_stpDesignatedRoot).append("stpRootCost",
                                                                                                                                                                                                                                                     m_stpRootCost).append("stpRootPort",
                                                                                                                                                                                                                                                                           m_stpRootPort).append("status",
                                                                                                                                                                                                                                                                                                 m_status).append("lastPollTime",
                                                                                                                                                                                                                                                                                                                  m_lastPollTime).append("baseVlan",
                                                                                                                                                                                                                                                                                                                                         m_baseVlan).append("baseVlanName",
                                                                                                                                                                                                                                                                                                                                                            m_baseVlanName).toString();
    }
}
