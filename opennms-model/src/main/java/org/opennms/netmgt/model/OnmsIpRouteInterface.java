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
 * The Class OnmsIpRouteInterface.
 */
@XmlRootElement(name = "ipRouteInterface")
@Entity
@Table(name = "ipRouteInterface", uniqueConstraints = { @UniqueConstraint(columnNames = { "nodeId", "routeDest" }) })
public class OnmsIpRouteInterface {

    /** The m_id. */
    private Integer m_id;

    /** The m_node. */
    private OnmsNode m_node;

    /** The m_route dest. */
    private String m_routeDest;

    /** The m_route mask. */
    private String m_routeMask;

    /** The m_route next hop. */
    private String m_routeNextHop;

    /** The m_route if index. */
    private Integer m_routeIfIndex;

    /** The m_route metric1. */
    private Integer m_routeMetric1;

    /** The m_route metric2. */
    private Integer m_routeMetric2;

    /** The m_route metric3. */
    private Integer m_routeMetric3;

    /** The m_route metric4. */
    private Integer m_routeMetric4;

    /** The m_route metric5. */
    private Integer m_routeMetric5;

    /** The m_route type. */
    private RouteType m_routeType;

    /** The m_route proto. */
    private Integer m_routeProto;

    /** The m_status. */
    private StatusType m_status = StatusType.UNKNOWN;

    /** The m_last poll time. */
    private Date m_lastPollTime;

    /**
     * The Class RouteType.
     */
    @Embeddable
    public static class RouteType implements Comparable<RouteType>, Serializable {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -4784344871599250528L;

        /** The Constant ROUTE_TYPE_OTHER. */
        public static final int ROUTE_TYPE_OTHER = 1;

        /** The Constant ROUTE_TYPE_INVALID. */
        public static final int ROUTE_TYPE_INVALID = 2;

        /** The Constant ROUTE_TYPE_DIRECT. */
        public static final int ROUTE_TYPE_DIRECT = 3;

        /** The Constant ROUTE_TYPE_INDIRECT. */
        public static final int ROUTE_TYPE_INDIRECT = 4;

        /** The Constant s_order. */
        private static final Integer[] s_order = { ROUTE_TYPE_OTHER, ROUTE_TYPE_INVALID, ROUTE_TYPE_DIRECT,
                ROUTE_TYPE_INDIRECT };

        /** The m_route type. */
        private Integer m_routeType;

        /** The Constant routeTypeMap. */
        private static final Map<Integer, String> routeTypeMap = new HashMap<Integer, String>();

        static {
            routeTypeMap.put(ROUTE_TYPE_OTHER, "Other");
            routeTypeMap.put(ROUTE_TYPE_INVALID, "Invalid");
            routeTypeMap.put(ROUTE_TYPE_DIRECT, "Direct");
            routeTypeMap.put(ROUTE_TYPE_INDIRECT, "Indirect");
        }

        /**
         * Instantiates a new route type.
         */
        @SuppressWarnings("unused")
        private RouteType() {
        }

        /**
         * Instantiates a new route type.
         *
         * @param routeType
         *            the route type
         */
        public RouteType(Integer routeType) {
            m_routeType = routeType;
        }

        /**
         * Gets the int code.
         *
         * @return the int code
         */
        @Column(name = "routeType")
        public Integer getIntCode() {
            return m_routeType;
        }

        /**
         * Sets the int code.
         *
         * @param routeType
         *            the new int code
         */
        public void setIntCode(Integer routeType) {
            m_routeType = routeType;
        }

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(RouteType o) {
            return getIndex(m_routeType) - getIndex(o.m_routeType);
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
            throw new IllegalArgumentException("illegal routeType code '" + code + "'");
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof RouteType) {
                return m_routeType.intValue() == ((RouteType) o).m_routeType.intValue();
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
            return String.valueOf(m_routeType);
        }

        /**
         * Gets the.
         *
         * @param code
         *            the code
         * @return the route type
         */
        public static RouteType get(Integer code) {
            if (code == null) {
                return null;
            }
            switch (code) {
            case ROUTE_TYPE_OTHER:
                return OTHER;
            case ROUTE_TYPE_INVALID:
                return INVALID;
            case ROUTE_TYPE_DIRECT:
                return DIRECT;
            case ROUTE_TYPE_INDIRECT:
                return INDIRECT;
            default:
                throw new IllegalArgumentException("Cannot create routeType from code " + code);
            }
        }

        /**
         * <p>
         * getRouteTypeString
         * </p>
         * .
         *
         * @param code
         *            the code
         * @return a {@link java.lang.String} object.
         */
        /**
         */
        public static String getRouteTypeString(Integer code) {
            if (routeTypeMap.containsKey(code)) {
                return routeTypeMap.get(code);
            }
            return null;
        }

        /** The other. */
        public static RouteType OTHER = new RouteType(ROUTE_TYPE_OTHER);

        /** The invalid. */
        public static RouteType INVALID = new RouteType(ROUTE_TYPE_INVALID);

        /** The direct. */
        public static RouteType DIRECT = new RouteType(ROUTE_TYPE_DIRECT);

        /** The indirect. */
        public static RouteType INDIRECT = new RouteType(ROUTE_TYPE_INDIRECT);

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
     * Gets the route dest.
     *
     * @return the route dest
     */
    @XmlElement
    @Column(nullable = false, length = 16)
    public String getRouteDest() {
        return m_routeDest;
    }

    /**
     * Sets the route dest.
     *
     * @param routeDest
     *            the new route dest
     */
    public void setRouteDest(final String routeDest) {
        m_routeDest = routeDest;
    }

    /**
     * Gets the route mask.
     *
     * @return the route mask
     */
    @XmlElement
    @Column(nullable = false, length = 16)
    public String getRouteMask() {
        return m_routeMask;
    }

    /**
     * Sets the route mask.
     *
     * @param routeMask
     *            the new route mask
     */
    public void setRouteMask(final String routeMask) {
        m_routeMask = routeMask;
    }

    /**
     * Gets the route next hop.
     *
     * @return the route next hop
     */
    @XmlElement
    @Column(nullable = false, length = 16)
    public String getRouteNextHop() {
        return m_routeNextHop;
    }

    /**
     * Sets the route next hop.
     *
     * @param routeNextHop
     *            the new route next hop
     */
    public void setRouteNextHop(final String routeNextHop) {
        m_routeNextHop = routeNextHop;
    }

    /**
     * Gets the route if index.
     *
     * @return the route if index
     */
    @XmlElement
    @Column(nullable = false)
    public Integer getRouteIfIndex() {
        return m_routeIfIndex;
    }

    /**
     * Sets the route if index.
     *
     * @param routeIfIndex
     *            the new route if index
     */
    public void setRouteIfIndex(final Integer routeIfIndex) {
        m_routeIfIndex = routeIfIndex;
    }

    /**
     * Gets the route metric1.
     *
     * @return the route metric1
     */
    @XmlElement
    @Column
    public Integer getRouteMetric1() {
        return m_routeMetric1;
    }

    /**
     * Sets the route metric1.
     *
     * @param routeMetric1
     *            the new route metric1
     */
    public void setRouteMetric1(final Integer routeMetric1) {
        m_routeMetric1 = routeMetric1;
    }

    /**
     * Gets the route metric2.
     *
     * @return the route metric2
     */
    @XmlElement
    @Column
    public Integer getRouteMetric2() {
        return m_routeMetric2;
    }

    /**
     * Sets the route metric2.
     *
     * @param routeMetric2
     *            the new route metric2
     */
    public void setRouteMetric2(final Integer routeMetric2) {
        m_routeMetric2 = routeMetric2;
    }

    /**
     * Gets the route metric3.
     *
     * @return the route metric3
     */
    @XmlElement
    @Column
    public Integer getRouteMetric3() {
        return m_routeMetric3;
    }

    /**
     * Sets the route metric3.
     *
     * @param routeMetric3
     *            the new route metric3
     */
    public void setRouteMetric3(final Integer routeMetric3) {
        m_routeMetric3 = routeMetric3;
    }

    /**
     * Gets the route metric4.
     *
     * @return the route metric4
     */
    @XmlElement
    @Column
    public Integer getRouteMetric4() {
        return m_routeMetric4;
    }

    /**
     * Sets the route metric4.
     *
     * @param routeMetric4
     *            the new route metric4
     */
    public void setRouteMetric4(final Integer routeMetric4) {
        m_routeMetric4 = routeMetric4;
    }

    /**
     * Gets the route metric5.
     *
     * @return the route metric5
     */
    @XmlElement
    @Column
    public Integer getRouteMetric5() {
        return m_routeMetric5;
    }

    /**
     * Sets the route metric5.
     *
     * @param routeMetric5
     *            the new route metric5
     */
    public void setRouteMetric5(final Integer routeMetric5) {
        m_routeMetric5 = routeMetric5;
    }

    /**
     * Gets the route type.
     *
     * @return the route type
     */
    @XmlElement
    @Column
    public RouteType getRouteType() {
        return m_routeType;
    }

    /**
     * Sets the route type.
     *
     * @param routeType
     *            the new route type
     */
    public void setRouteType(final RouteType routeType) {
        m_routeType = routeType;
    }

    /**
     * Gets the route proto.
     *
     * @return the route proto
     */
    @XmlElement
    @Column
    public Integer getRouteProto() {
        return m_routeProto;
    }

    /**
     * Sets the route proto.
     *
     * @param routeProto
     *            the new route proto
     */
    public void setRouteProto(final Integer routeProto) {
        m_routeProto = routeProto;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    @XmlElement
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
        return new ToStringBuilder(this).append("id", m_id).append("node", m_node).append("routedest", m_routeDest).append("routemask",
                                                                                                                           m_routeMask).append("routenexthop",
                                                                                                                                               m_routeNextHop).append("routeifindex",
                                                                                                                                                                      m_routeIfIndex).append("routetype",
                                                                                                                                                                                             RouteType.getRouteTypeString(m_routeType.getIntCode())).append("routeprotocol",
                                                                                                                                                                                                                                                            m_routeProto).append("routemetric1",
                                                                                                                                                                                                                                                                                 m_routeMetric1).toString();
    }

}
