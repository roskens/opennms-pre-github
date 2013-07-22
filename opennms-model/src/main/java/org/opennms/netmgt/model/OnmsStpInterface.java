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

/*
 * Created on 9-mar-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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

import org.opennms.netmgt.model.OnmsArpInterface.StatusType;

/**
 * <p>
 * BridgeStpInterface class.
 * </p>
 *
 * @author antonio
 */
@XmlRootElement(name = "stpInterface")
@Entity
@Table(name = "stpInterface", uniqueConstraints = { @UniqueConstraint(columnNames = { "nodeId", "bridgePort", "stpVlan" }) })
public class OnmsStpInterface {

    /**
     * The Class StpPortStatus.
     */
    @Embeddable
    public static class StpPortStatus implements Comparable<StpPortStatus>, Serializable {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 7669097061380115150L;

        /** The Constant STP_PORT_STATUS_UNKNOWN. */
        public static final int STP_PORT_STATUS_UNKNOWN = 0;

        /** The Constant STP_PORT_STATUS_DISABLED. */
        public static final int STP_PORT_STATUS_DISABLED = 1;

        /** The Constant STP_PORT_STATUS_BLOCKING. */
        public static final int STP_PORT_STATUS_BLOCKING = 2;

        /** The Constant STP_PORT_STATUS_LISTENING. */
        public static final int STP_PORT_STATUS_LISTENING = 3;

        /** The Constant STP_PORT_STATUS_LEARNING. */
        public static final int STP_PORT_STATUS_LEARNING = 4;

        /** The Constant STP_PORT_STATUS_FORWARDING. */
        public static final int STP_PORT_STATUS_FORWARDING = 5;

        /** The Constant STP_PORT_STATUS_BROKEN. */
        public static final int STP_PORT_STATUS_BROKEN = 6;

        /** The Constant s_order. */
        private static final Integer[] s_order = { 0, 1, 2, 3, 4, 5, 6 };

        /** The m_stp port status. */
        private Integer m_stpPortStatus;

        /** The Constant stpPortStatusMap. */
        private static final Map<Integer, String> stpPortStatusMap = new HashMap<Integer, String>();

        static {
            stpPortStatusMap.put(0, "Unknown");
            stpPortStatusMap.put(1, "disabled");
            stpPortStatusMap.put(2, "blocking");
            stpPortStatusMap.put(3, "listening");
            stpPortStatusMap.put(4, "learning");
            stpPortStatusMap.put(5, "forwarding");
            stpPortStatusMap.put(6, "broken");
        }

        /**
         * Instantiates a new stp port status.
         */
        @SuppressWarnings("unused")
        private StpPortStatus() {
        }

        /**
         * Instantiates a new stp port status.
         *
         * @param stpPortStatus
         *            the stp port status
         */
        public StpPortStatus(Integer stpPortStatus) {
            m_stpPortStatus = stpPortStatus;
        }

        /**
         * Gets the int code.
         *
         * @return the int code
         */
        @Column(name = "stpPortState")
        public Integer getIntCode() {
            return m_stpPortStatus;
        }

        /**
         * Sets the int code.
         *
         * @param stpPortStatus
         *            the new int code
         */
        public void setIntCode(Integer stpPortStatus) {
            m_stpPortStatus = stpPortStatus;
        }

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(StpPortStatus o) {
            return getIndex(m_stpPortStatus) - getIndex(o.m_stpPortStatus);
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
            throw new IllegalArgumentException("illegal stpPortStatus code '" + code + "'");
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof StpPortStatus) {
                return m_stpPortStatus.intValue() == ((StpPortStatus) o).m_stpPortStatus.intValue();
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
            return String.valueOf(m_stpPortStatus);
        }

        /**
         * Gets the.
         *
         * @param code
         *            the code
         * @return the stp port status
         */
        public static StpPortStatus get(Integer code) {
            if (code == null) {
                return StpPortStatus.UNKNOWN;
            }
            switch (code) {
            case STP_PORT_STATUS_UNKNOWN:
                return UNKNOWN;
            case STP_PORT_STATUS_DISABLED:
                return DISABLED;
            case STP_PORT_STATUS_BLOCKING:
                return BLOCKING;
            case STP_PORT_STATUS_LISTENING:
                return LISTENING;
            case STP_PORT_STATUS_LEARNING:
                return LEARNING;
            case STP_PORT_STATUS_FORWARDING:
                return FORWARDING;
            case STP_PORT_STATUS_BROKEN:
                return BROKEN;

            default:
                throw new IllegalArgumentException("Cannot create vlanStatus from code " + code);
            }
        }

        /**
         * <p>
         * getPortStatusString
         * </p>
         * .
         *
         * @param code
         *            the code
         * @return a {@link java.lang.String} object.
         */
        /**
         */
        public static String getStpPortStatusString(Integer code) {
            if (stpPortStatusMap.containsKey(code)) {
                return stpPortStatusMap.get(code);
            }
            return null;
        }

        /** The unknown. */
        public static StpPortStatus UNKNOWN = new StpPortStatus(STP_PORT_STATUS_UNKNOWN);

        /** The disabled. */
        public static StpPortStatus DISABLED = new StpPortStatus(STP_PORT_STATUS_DISABLED);

        /** The blocking. */
        public static StpPortStatus BLOCKING = new StpPortStatus(STP_PORT_STATUS_BLOCKING);

        /** The listening. */
        public static StpPortStatus LISTENING = new StpPortStatus(STP_PORT_STATUS_LISTENING);

        /** The learning. */
        public static StpPortStatus LEARNING = new StpPortStatus(STP_PORT_STATUS_LEARNING);

        /** The forwarding. */
        public static StpPortStatus FORWARDING = new StpPortStatus(STP_PORT_STATUS_FORWARDING);

        /** The broken. */
        public static StpPortStatus BROKEN = new StpPortStatus(STP_PORT_STATUS_BROKEN);

    }

    /** The m_id. */
    private Integer m_id;

    /** The m_node. */
    private OnmsNode m_node;

    /** The m_bridge port. */
    private Integer m_bridgePort;

    /** The m_if index. */
    private Integer m_ifIndex = -1;

    /** The m_stp port state. */
    private StpPortStatus m_stpPortState;

    /** The m_stp port path cost. */
    private Integer m_stpPortPathCost;

    /** The m_stp port designated root. */
    private String m_stpPortDesignatedRoot;

    /** The m_stp port designated cost. */
    private Integer m_stpPortDesignatedCost;

    /** The m_stp port designated bridge. */
    private String m_stpPortDesignatedBridge;

    /** The m_stp port designated port. */
    private String m_stpPortDesignatedPort;

    /** The m_status. */
    private StatusType m_status = StatusType.UNKNOWN;

    /** The m_last poll time. */
    private Date m_lastPollTime;

    /** The m_vlan. */
    private Integer m_vlan;

    /**
     * Instantiates a new onms stp interface.
     */
    public OnmsStpInterface() {
    }

    /**
     * Instantiates a new onms stp interface.
     *
     * @param bridgePort
     *            the bridge port
     * @param vlanIndex
     *            the vlan index
     */
    public OnmsStpInterface(final Integer bridgePort, final Integer vlanIndex) {
        m_bridgePort = bridgePort;
        m_vlan = vlanIndex;
    }

    /**
     * Instantiates a new onms stp interface.
     *
     * @param node
     *            the node
     * @param bridgePort
     *            the bridge port
     * @param vlanIndex
     *            the vlan index
     */
    public OnmsStpInterface(final OnmsNode node, final Integer bridgePort, final Integer vlanIndex) {
        m_node = node;
        m_bridgePort = bridgePort;
        m_vlan = vlanIndex;
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
     * Gets the if index.
     *
     * @return the if index
     */
    @XmlAttribute
    @Column(nullable = false)
    public Integer getIfIndex() {
        return m_ifIndex;
    }

    /**
     * Sets the if index.
     *
     * @param ifIndex
     *            the new if index
     */
    public void setIfIndex(final Integer ifIndex) {
        m_ifIndex = ifIndex;
    }

    /**
     * Gets the stp port state.
     *
     * @return the stp port state
     */
    @XmlElement
    @Column
    public StpPortStatus getStpPortState() {
        return m_stpPortState;
    }

    /**
     * Sets the stp port state.
     *
     * @param stpPortState
     *            the new stp port state
     */
    public void setStpPortState(final StpPortStatus stpPortState) {
        m_stpPortState = stpPortState;
    }

    /**
     * Gets the stp port path cost.
     *
     * @return the stp port path cost
     */
    @XmlElement
    @Column
    public Integer getStpPortPathCost() {
        return m_stpPortPathCost;
    }

    /**
     * Sets the stp port path cost.
     *
     * @param stpPortPathCost
     *            the new stp port path cost
     */
    public void setStpPortPathCost(final Integer stpPortPathCost) {
        m_stpPortPathCost = stpPortPathCost;
    }

    /**
     * Gets the stp port designated root.
     *
     * @return the stp port designated root
     */
    @XmlElement
    @Column(length = 16)
    public String getStpPortDesignatedRoot() {
        return m_stpPortDesignatedRoot;
    }

    /**
     * Sets the stp port designated root.
     *
     * @param stpPortDesignatedRoot
     *            the new stp port designated root
     */
    public void setStpPortDesignatedRoot(final String stpPortDesignatedRoot) {
        m_stpPortDesignatedRoot = stpPortDesignatedRoot;
    }

    /**
     * Gets the stp port designated cost.
     *
     * @return the stp port designated cost
     */
    @XmlElement
    @Column
    public Integer getStpPortDesignatedCost() {
        return m_stpPortDesignatedCost;
    }

    /**
     * Sets the stp port designated cost.
     *
     * @param stpPortDesignatedCost
     *            the new stp port designated cost
     */
    public void setStpPortDesignatedCost(final Integer stpPortDesignatedCost) {
        m_stpPortDesignatedCost = stpPortDesignatedCost;
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
     * Gets the bridge port.
     *
     * @return the bridge port
     */
    @XmlElement
    @Column(nullable = false)
    public Integer getBridgePort() {
        return m_bridgePort;
    }

    /**
     * Sets the bridge port.
     *
     * @param bridgePort
     *            the new bridge port
     */
    public void setBridgePort(final Integer bridgePort) {
        m_bridgePort = bridgePort;
    }

    /**
     * Gets the stp port designated bridge.
     *
     * @return the stp port designated bridge
     */
    @XmlElement
    @Column(length = 16)
    public String getStpPortDesignatedBridge() {
        return m_stpPortDesignatedBridge;
    }

    /**
     * Sets the stp port designated bridge.
     *
     * @param stpPortDesignatedBridge
     *            the new stp port designated bridge
     */
    public void setStpPortDesignatedBridge(final String stpPortDesignatedBridge) {
        m_stpPortDesignatedBridge = stpPortDesignatedBridge;
    }

    /**
     * Gets the stp port designated port.
     *
     * @return the stp port designated port
     */
    @XmlElement
    @Column(length = 4)
    public String getStpPortDesignatedPort() {
        return m_stpPortDesignatedPort;
    }

    /**
     * Sets the stp port designated port.
     *
     * @param stpPortDesignatedPort
     *            the new stp port designated port
     */
    public void setStpPortDesignatedPort(final String stpPortDesignatedPort) {
        m_stpPortDesignatedPort = stpPortDesignatedPort;
    }

    /**
     * Gets the vlan.
     *
     * @return the vlan
     */
    @XmlElement(name = "stpVlan")
    @Column(name = "stpVlan", nullable = false)
    public Integer getVlan() {
        return m_vlan;
    }

    /**
     * Sets the vlan.
     *
     * @param vlan
     *            the new vlan
     */
    public void setVlan(final Integer vlan) {
        m_vlan = vlan;
    }
}
