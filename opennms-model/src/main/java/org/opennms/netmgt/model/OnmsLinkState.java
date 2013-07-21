/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlIDREF;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The Class OnmsLinkState.
 */
@Entity
/**
 * <p>OnmsLinkState class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
@Table(name = "linkstate")
public class OnmsLinkState implements Serializable, Comparable<OnmsLinkState> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5586375063168201398L;

    /**
     * The Interface LinkStateTransition.
     */
    public interface LinkStateTransition {

        /**
         * On link up.
         */
        public void onLinkUp();

        /**
         * On link down.
         */
        public void onLinkDown();

        /**
         * On link unknown.
         */
        public void onLinkUnknown();
    }

    /**
     * The Enum LinkState.
     */
    public static enum LinkState {

        /** The link up. */
        LINK_UP {
            @Override
            public LinkState nodeDown(LinkStateTransition transition) {
                transition.onLinkDown();
                return LINK_NODE_DOWN;
            }

            @Override
            public LinkState parentNodeDown(LinkStateTransition transition) {
                transition.onLinkDown();
                return LINK_PARENT_NODE_DOWN;
            }

            @Override
            public LinkState nodeEndPointDeleted(LinkStateTransition transition) {
                transition.onLinkUnknown();
                return LINK_NODE_UNMANAGED;
            }

            @Override
            public LinkState parentNodeEndPointDeleted(LinkStateTransition transition) {
                transition.onLinkUnknown();
                return LINK_PARENT_NODE_UNMANAGED;
            }

            @Override
            public String getDataLinkInterfaceStateType() {
                return "G";
            }
        },

        /** The link node down. */
        LINK_NODE_DOWN {
            @Override
            public LinkState nodeUp(LinkStateTransition transition) {
                transition.onLinkUp();
                return LINK_UP;
            }

            @Override
            public LinkState parentNodeDown(LinkStateTransition transition) {
                return LINK_BOTH_DOWN;
            }

            @Override
            public LinkState nodeEndPointDeleted(LinkStateTransition transition) {
                transition.onLinkUnknown();
                return LINK_NODE_UNMANAGED;
            }

            @Override
            public LinkState parentNodeEndPointDeleted(LinkStateTransition transition) {
                transition.onLinkUnknown();
                return LINK_PARENT_NODE_UNMANAGED;
            }

            @Override
            public String getDataLinkInterfaceStateType() {
                return "B";
            }
        },

        /** The link parent node down. */
        LINK_PARENT_NODE_DOWN {

            @Override
            public LinkState nodeDown(LinkStateTransition transition) {
                return LINK_BOTH_DOWN;
            }

            @Override
            public LinkState parentNodeUp(LinkStateTransition transition) {
                transition.onLinkUp();
                return LINK_UP;
            }

            @Override
            public LinkState nodeEndPointDeleted(LinkStateTransition transition) {
                transition.onLinkUnknown();
                return LINK_NODE_UNMANAGED;
            }

            @Override
            public LinkState parentNodeEndPointDeleted(LinkStateTransition transition) {
                transition.onLinkUnknown();
                return LINK_PARENT_NODE_UNMANAGED;
            }

            @Override
            public String getDataLinkInterfaceStateType() {
                return "B";
            }

        },

        /** The link both down. */
        LINK_BOTH_DOWN {

            @Override
            public LinkState nodeUp(LinkStateTransition transition) {
                return LINK_PARENT_NODE_DOWN;
            }

            @Override
            public LinkState parentNodeUp(LinkStateTransition transition) {
                return LINK_NODE_DOWN;
            }

            @Override
            public LinkState nodeEndPointDeleted(LinkStateTransition transition) {
                transition.onLinkUnknown();
                return LINK_NODE_UNMANAGED;
            }

            @Override
            public LinkState parentNodeEndPointDeleted(LinkStateTransition transition) {
                transition.onLinkUnknown();
                return LINK_PARENT_NODE_UNMANAGED;
            }

            @Override
            public String getDataLinkInterfaceStateType() {
                return "B";
            }
        },

        /** The link both unmanaged. */
        LINK_BOTH_UNMANAGED {
            @Override
            public LinkState nodeEndPointFound(LinkStateTransition transition) {
                return LINK_PARENT_NODE_UNMANAGED;
            }

            @Override
            public LinkState parentNodeEndPointFound(LinkStateTransition transition) {
                return LINK_NODE_UNMANAGED;
            }

            @Override
            public String getDataLinkInterfaceStateType() {
                return "U";
            }
        },

        /** The link parent node unmanaged. */
        LINK_PARENT_NODE_UNMANAGED {

            @Override
            public LinkState parentNodeEndPointFound(LinkStateTransition transition) {
                transition.onLinkUp();
                return LINK_UP;
            }

            @Override
            public LinkState nodeEndPointDeleted(LinkStateTransition transition) {
                return LINK_BOTH_UNMANAGED;
            }

            @Override
            public String getDataLinkInterfaceStateType() {
                return "U";
            }

        },

        /** The link node unmanaged. */
        LINK_NODE_UNMANAGED {

            @Override
            public LinkState parentNodeEndPointDeleted(LinkStateTransition transition) {
                return LINK_BOTH_UNMANAGED;
            }

            @Override
            public LinkState nodeEndPointFound(LinkStateTransition transition) {
                transition.onLinkUp();
                return LINK_UP;
            }

            @Override
            public String getDataLinkInterfaceStateType() {
                return "U";
            }

        };

        /**
         * Gets the data link interface state type.
         *
         * @return the data link interface state type
         */
        public abstract String getDataLinkInterfaceStateType();

        /**
         * Node down.
         *
         * @param transition
         *            the transition
         * @return the link state
         */
        public LinkState nodeDown(LinkStateTransition transition) {
            return this;
        }

        /**
         * Parent node down.
         *
         * @param transition
         *            the transition
         * @return the link state
         */
        public LinkState parentNodeDown(LinkStateTransition transition) {
            return this;
        }

        /**
         * Node up.
         *
         * @param transition
         *            the transition
         * @return the link state
         */
        public LinkState nodeUp(LinkStateTransition transition) {
            return this;
        }

        /**
         * Parent node up.
         *
         * @param transition
         *            the transition
         * @return the link state
         */
        public LinkState parentNodeUp(LinkStateTransition transition) {
            return this;
        }

        /**
         * Down.
         *
         * @param isParent
         *            the is parent
         * @param transition
         *            the transition
         * @return the link state
         */
        public LinkState down(boolean isParent, LinkStateTransition transition) {
            return isParent ? parentNodeDown(transition) : nodeDown(transition);
        }

        /**
         * Up.
         *
         * @param isParent
         *            the is parent
         * @param transition
         *            the transition
         * @return the link state
         */
        public LinkState up(boolean isParent, LinkStateTransition transition) {
            return isParent ? parentNodeUp(transition) : nodeUp(transition);
        }

        /**
         * Node end point found.
         *
         * @param transition
         *            the transition
         * @return the link state
         */
        public LinkState nodeEndPointFound(LinkStateTransition transition) {
            return this;
        }

        /**
         * Parent node end point found.
         *
         * @param transition
         *            the transition
         * @return the link state
         */
        public LinkState parentNodeEndPointFound(LinkStateTransition transition) {
            return this;
        }

        /**
         * Parent node end point deleted.
         *
         * @param transition
         *            the transition
         * @return the link state
         */
        public LinkState parentNodeEndPointDeleted(LinkStateTransition transition) {
            return this;

        }

        /**
         * Node end point deleted.
         *
         * @param transition
         *            the transition
         * @return the link state
         */
        public LinkState nodeEndPointDeleted(LinkStateTransition transition) {
            return this;
        }
    }

    /** The m_id. */
    private Integer m_id;

    /** The m_data link interface. */
    private DataLinkInterface m_dataLinkInterface;

    /** The m_link state. */
    private LinkState m_linkState = LinkState.LINK_UP;

    /**
     * <p>
     * Constructor for OnmsLinkState.
     * </p>
     */
    public OnmsLinkState() {
    }

    /**
     * <p>
     * Constructor for OnmsLinkState.
     * </p>
     *
     * @param dataLinkInterface
     *            a {@link org.opennms.netmgt.model.DataLinkInterface} object.
     * @param linkState
     *            a {@link org.opennms.netmgt.model.OnmsLinkState.LinkState}
     *            object.
     */
    public OnmsLinkState(DataLinkInterface dataLinkInterface, LinkState linkState) {
        m_dataLinkInterface = dataLinkInterface;
        m_linkState = linkState;
    }

    /**
     * <p>
     * getId
     * </p>
     * .
     *
     * @return a {@link java.lang.Integer} object.
     */
    @Id
    @Column(nullable = false)
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    public Integer getId() {
        return m_id;
    }

    /**
     * <p>
     * setId
     * </p>
     * .
     *
     * @param id
     *            a {@link java.lang.Integer} object.
     */
    public void setId(Integer id) {
        m_id = id;
    }

    /**
     * <p>
     * getDataLinkInterface
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.model.DataLinkInterface} object.
     */
    @XmlIDREF
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datalinkinterfaceid")
    public DataLinkInterface getDataLinkInterface() {
        return m_dataLinkInterface;
    }

    /**
     * <p>
     * setDataLinkInterface
     * </p>
     * .
     *
     * @param dataLinkInterface
     *            a {@link org.opennms.netmgt.model.DataLinkInterface} object.
     */
    public void setDataLinkInterface(DataLinkInterface dataLinkInterface) {
        m_dataLinkInterface = dataLinkInterface;
    }

    /**
     * <p>
     * getLinkState
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.model.OnmsLinkState.LinkState}
     *         object.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "linkstate", length = 24, nullable = false)
    public LinkState getLinkState() {
        return m_linkState;
    }

    /**
     * <p>
     * setLinkState
     * </p>
     * .
     *
     * @param linkState
     *            a {@link org.opennms.netmgt.model.OnmsLinkState.LinkState}
     *            object.
     */
    public void setLinkState(LinkState linkState) {
        m_linkState = linkState;
    }

    /**
     * <p>
     * toString
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).append("datalink interface", getDataLinkInterface()).append("link state",
                                                                                                                           getLinkState()).toString();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (o instanceof OnmsLinkState) {
            OnmsLinkState lso = (OnmsLinkState) o;

            return new EqualsBuilder().append(getId(), lso.getId()).append(getDataLinkInterface(),
                                                                           lso.getDataLinkInterface()).append(getLinkState(),
                                                                                                              lso.getLinkState()).isEquals();
        }
        return false;
    }

    /**
     * <p>
     * compareTo
     * </p>
     * .
     *
     * @param o
     *            a {@link org.opennms.netmgt.model.OnmsLinkState} object.
     * @return a int.
     */
    @Override
    public int compareTo(OnmsLinkState o) {
        return new CompareToBuilder().append(getId(), o.getId()).append(getDataLinkInterface(),
                                                                        o.getDataLinkInterface()).append(getLinkState(),
                                                                                                         o.getLinkState()).toComparison();
    }

    /**
     * <p>
     * hashCode
     * </p>
     * .
     *
     * @return a int.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).append(getDataLinkInterface()).append(getLinkState()).toHashCode();
    }
}
