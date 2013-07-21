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
import java.util.Collection;
import java.util.List;

/**
 * The Class AggregateTracker.
 */
public class AggregateTracker extends CollectionTracker {

    /**
     * The Class ChildTrackerPduBuilder.
     */
    private static class ChildTrackerPduBuilder extends PduBuilder {

        /** The m_oids. */
        private List<SnmpObjId> m_oids = new ArrayList<SnmpObjId>();

        /** The m_non repeaters. */
        private int m_nonRepeaters = 0;

        /** The m_max repititions. */
        private int m_maxRepititions = 0;

        /** The m_response processor. */
        private ResponseProcessor m_responseProcessor;

        /** The m_non repeater start index. */
        private int m_nonRepeaterStartIndex;

        /** The m_repeater start index. */
        private int m_repeaterStartIndex;

        /**
         * Instantiates a new child tracker pdu builder.
         *
         * @param maxVarsPerPdu
         *            the max vars per pdu
         */
        public ChildTrackerPduBuilder(int maxVarsPerPdu) {
            super(maxVarsPerPdu);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#addOid(org.opennms.netmgt.snmp.SnmpObjId)
         */
        @Override
        public void addOid(SnmpObjId snmpObjId) {
            m_oids.add(snmpObjId);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#setNonRepeaters(int)
         */
        @Override
        public void setNonRepeaters(int nonRepeaters) {
            m_nonRepeaters = nonRepeaters;
        }

        /**
         * Gets the non repeaters.
         *
         * @return the non repeaters
         */
        public int getNonRepeaters() {
            return m_nonRepeaters;
        }

        /**
         * Gets the repeaters.
         *
         * @return the repeaters
         */
        public int getRepeaters() {
            return size() - getNonRepeaters();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.PduBuilder#setMaxRepetitions(int)
         */
        @Override
        public void setMaxRepetitions(int maxRepititions) {
            m_maxRepititions = maxRepititions;
        }

        /**
         * Gets the max repititions.
         *
         * @return the max repititions
         */
        public int getMaxRepititions() {
            return hasRepeaters() ? m_maxRepititions : Integer.MAX_VALUE;
        }

        /**
         * Size.
         *
         * @return the int
         */
        public int size() {
            return m_oids.size();
        }

        /**
         * Sets the response processor.
         *
         * @param responseProcessor
         *            the new response processor
         */
        public void setResponseProcessor(ResponseProcessor responseProcessor) {
            m_responseProcessor = responseProcessor;
        }

        /**
         * Gets the response processor.
         *
         * @return the response processor
         */
        public ResponseProcessor getResponseProcessor() {
            return m_responseProcessor;
        }

        /**
         * Adds the non repeaters.
         *
         * @param pduBuilder
         *            the pdu builder
         */
        public void addNonRepeaters(PduBuilder pduBuilder) {
            for (int i = 0; i < m_nonRepeaters; i++) {
                SnmpObjId oid = m_oids.get(i);
                pduBuilder.addOid(oid);
            }
        }

        /**
         * Adds the repeaters.
         *
         * @param pduBuilder
         *            the pdu builder
         */
        public void addRepeaters(PduBuilder pduBuilder) {
            for (int i = m_nonRepeaters; i < m_oids.size(); i++) {
                SnmpObjId oid = m_oids.get(i);
                pduBuilder.addOid(oid);
            }
        }

        /**
         * Checks for repeaters.
         *
         * @return true, if successful
         */
        public boolean hasRepeaters() {
            return getNonRepeaters() < size();
        }

        /**
         * Sets the non repeater start index.
         *
         * @param nonRepeaterStartIndex
         *            the new non repeater start index
         */
        public void setNonRepeaterStartIndex(int nonRepeaterStartIndex) {
            m_nonRepeaterStartIndex = nonRepeaterStartIndex;
        }

        /**
         * Gets the non repeater start index.
         *
         * @return the non repeater start index
         */
        public int getNonRepeaterStartIndex() {
            return m_nonRepeaterStartIndex;
        }

        /**
         * Sets the repeater start index.
         *
         * @param repeaterStartIndex
         *            the new repeater start index
         */
        public void setRepeaterStartIndex(int repeaterStartIndex) {
            m_repeaterStartIndex = repeaterStartIndex;
        }

        /**
         * Gets the repeater start index.
         *
         * @return the repeater start index
         */
        public int getRepeaterStartIndex() {
            return m_repeaterStartIndex;
        }

        /**
         * Checks if is non repeater.
         *
         * @param canonicalIndex
         *            the canonical index
         * @return true, if is non repeater
         */
        boolean isNonRepeater(int canonicalIndex) {
            return getNonRepeaterStartIndex() <= canonicalIndex
                    && canonicalIndex < getNonRepeaterStartIndex() + getNonRepeaters();
        }

        /**
         * Checks if is repeater.
         *
         * @param canonicalIndex
         *            the canonical index
         * @return true, if is repeater
         */
        boolean isRepeater(int canonicalIndex) {
            return getRepeaterStartIndex() <= canonicalIndex
                    && canonicalIndex < getRepeaterStartIndex() + getRepeaters();
        }

        /**
         * Gets the child index.
         *
         * @param canonicalIndex
         *            the canonical index
         * @return the child index
         */
        public int getChildIndex(int canonicalIndex) {
            if (isNonRepeater(canonicalIndex)) {
                return canonicalIndex - getNonRepeaterStartIndex();
            }

            if (isRepeater(canonicalIndex)) {
                return canonicalIndex - getRepeaterStartIndex() + getNonRepeaters();
            }

            throw new IllegalArgumentException("index out of range for tracker " + this);
        }
    }

    /**
     * The Class ChildTrackerResponseProcessor.
     */
    private class ChildTrackerResponseProcessor implements ResponseProcessor {

        /** The m_repeaters. */
        private final int m_repeaters;

        /** The m_pdu builder. */
        private final PduBuilder m_pduBuilder;

        /** The m_non repeaters. */
        private final int m_nonRepeaters;

        /** The m_child pdu builders. */
        private final List<ChildTrackerPduBuilder> m_childPduBuilders;

        /** The m_curr response index. */
        private int m_currResponseIndex = 0;

        /**
         * Instantiates a new child tracker response processor.
         *
         * @param pduBuilder
         *            the pdu builder
         * @param builders
         *            the builders
         * @param nonRepeaters
         *            the non repeaters
         * @param repeaters
         *            the repeaters
         */
        public ChildTrackerResponseProcessor(PduBuilder pduBuilder, List<ChildTrackerPduBuilder> builders,
                int nonRepeaters, int repeaters) {
            m_repeaters = repeaters;
            m_pduBuilder = pduBuilder;
            m_nonRepeaters = nonRepeaters;
            m_childPduBuilders = builders;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.ResponseProcessor#processResponse(org.opennms.netmgt.snmp.SnmpObjId, org.opennms.netmgt.snmp.SnmpValue)
         */
        @Override
        public void processResponse(SnmpObjId snmpObjId, SnmpValue val) {
            ChildTrackerPduBuilder childBuilder = getChildBuilder(m_currResponseIndex++);
            childBuilder.getResponseProcessor().processResponse(snmpObjId, val);
        }

        /**
         * Process child error.
         *
         * @param errorStatus
         *            the error status
         * @param errorIndex
         *            the error index
         * @return true, if successful
         */
        public boolean processChildError(int errorStatus, int errorIndex) {
            int canonicalIndex = getCanonicalIndex(errorIndex - 1);
            ChildTrackerPduBuilder childBuilder = getChildBuilder(canonicalIndex);
            int childIndex = childBuilder.getChildIndex(canonicalIndex);
            return childBuilder.getResponseProcessor().processErrors(errorStatus, childIndex + 1);
        }

        /**
         * Gets the child builder.
         *
         * @param zeroBasedIndex
         *            the zero based index
         * @return the child builder
         */
        private ChildTrackerPduBuilder getChildBuilder(int zeroBasedIndex) {
            int canonicalIndex = getCanonicalIndex(zeroBasedIndex);
            for (ChildTrackerPduBuilder childBuilder : m_childPduBuilders) {
                if (childBuilder.isNonRepeater(canonicalIndex) || childBuilder.isRepeater(canonicalIndex)) {
                    return childBuilder;
                }
            }

            throw new IllegalStateException("Unable to find childBuilder for index " + zeroBasedIndex);
        }

        /**
         * Gets the canonical index.
         *
         * @param zeroBasedIndex
         *            the zero based index
         * @return the canonical index
         */
        private int getCanonicalIndex(int zeroBasedIndex) {
            if (zeroBasedIndex <= 0) {
                return 0;
            }
            if (zeroBasedIndex < m_nonRepeaters) {
                return zeroBasedIndex;
            }

            // return the smallest index of the repeater this index refers to
            return ((zeroBasedIndex - m_nonRepeaters) % m_repeaters) + m_nonRepeaters;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.ResponseProcessor#processErrors(int, int)
         */
        @Override
        public boolean processErrors(int errorStatus, int errorIndex) {
            if (errorStatus == TOO_BIG_ERR) {
                int maxVarsPerPdu = m_pduBuilder.getMaxVarsPerPdu();
                if (maxVarsPerPdu <= 1) {
                    throw new IllegalArgumentException("Unable to handle tooBigError when maxVarsPerPdu = "
                            + maxVarsPerPdu);
                }
                m_pduBuilder.setMaxVarsPerPdu(maxVarsPerPdu / 2);
                reportTooBigErr("Reducing maxVarsPerPdu for this request to " + m_pduBuilder.getMaxVarsPerPdu());
                return true;
            } else if (errorStatus == GEN_ERR) {
                return processChildError(errorStatus, errorIndex);
            } else if (errorStatus == NO_SUCH_NAME_ERR) {
                return processChildError(errorStatus, errorIndex);
            } else if (errorStatus != NO_ERR) {
                throw new IllegalArgumentException("Unrecognized errorStatus " + errorStatus);
            } else {
                // Continue on.. no need to retry
                return false;
            }
        }
    }

    /** The m_children. */
    private CollectionTracker[] m_children;

    /**
     * Instantiates a new aggregate tracker.
     *
     * @param children
     *            the children
     */
    public AggregateTracker(Collection<Collectable> children) {
        this(children, null);
    }

    /**
     * Instantiates a new aggregate tracker.
     *
     * @param children
     *            the children
     * @param parent
     *            the parent
     */
    public AggregateTracker(Collection<Collectable> children, CollectionTracker parent) {
        this(children.toArray(new Collectable[children.size()]), parent);
    }

    /**
     * Instantiates a new aggregate tracker.
     *
     * @param children
     *            the children
     */
    public AggregateTracker(Collectable[] children) {
        this(children, null);
    }

    /**
     * Instantiates a new aggregate tracker.
     *
     * @param children
     *            the children
     * @param parent
     *            the parent
     */
    public AggregateTracker(Collectable[] children, CollectionTracker parent) {
        super(parent);

        m_children = new CollectionTracker[children.length];
        for (int i = 0; i < m_children.length; i++) {
            m_children[i] = children[i].getCollectionTracker();
            m_children[i].setParent(this);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.CollectionTracker#setFailed(boolean)
     */
    @Override
    public void setFailed(boolean failed) {
        super.setFailed(failed);
        for (CollectionTracker child : m_children) {
            child.setFailed(failed);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.CollectionTracker#setTimedOut(boolean)
     */
    @Override
    public void setTimedOut(boolean timedOut) {
        super.setTimedOut(timedOut);
        for (CollectionTracker child : m_children) {
            child.setTimedOut(timedOut);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.CollectionTracker#setMaxRepetitions(int)
     */
    @Override
    public void setMaxRepetitions(int maxRepititions) {
        for (CollectionTracker child : m_children) {
            child.setMaxRepetitions(maxRepititions);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.CollectionTracker#isFinished()
     */
    @Override
    public boolean isFinished() {
        for (CollectionTracker child : m_children) {
            if (!child.isFinished()) {
                return false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.CollectionTracker#buildNextPdu(org.opennms.netmgt.snmp.PduBuilder)
     */
    @Override
    public ResponseProcessor buildNextPdu(final PduBuilder parentBuilder) {

        // first process the child trackers that aren't finished up to maxVars
        int count = 0;
        int maxVars = parentBuilder.getMaxVarsPerPdu();
        final List<ChildTrackerPduBuilder> builders = new ArrayList<ChildTrackerPduBuilder>(m_children.length);
        for (int i = 0; i < m_children.length && count < maxVars; i++) {
            CollectionTracker childTracker = m_children[i];
            if (!childTracker.isFinished()) {
                ChildTrackerPduBuilder childBuilder = new ChildTrackerPduBuilder(maxVars - count);
                ResponseProcessor rp = childTracker.buildNextPdu(childBuilder);
                childBuilder.setResponseProcessor(rp);
                builders.add(childBuilder);
                count += childBuilder.size();
            }
        }

        // set the nonRepeaters in the passed in pduBuilder and store indices in
        // the childTrackers
        int nonRepeaters = 0;
        for (ChildTrackerPduBuilder childBuilder : builders) {
            childBuilder.setNonRepeaterStartIndex(nonRepeaters);
            childBuilder.addNonRepeaters(parentBuilder);
            nonRepeaters += childBuilder.getNonRepeaters();
        }

        // set the repeaters in the passed in pduBuilder and store indices in
        // the childTrackers
        int maxRepititions = Integer.MAX_VALUE;
        int repeaters = 0;
        for (ChildTrackerPduBuilder childBuilder : builders) {
            childBuilder.setRepeaterStartIndex(nonRepeaters + repeaters);
            childBuilder.addRepeaters(parentBuilder);
            maxRepititions = Math.min(maxRepititions, childBuilder.getMaxRepititions());
            repeaters += childBuilder.getRepeaters();
        }

        // set the non repeaters and max repetitions
        parentBuilder.setNonRepeaters(nonRepeaters);
        parentBuilder.setMaxRepetitions(maxRepititions == Integer.MAX_VALUE ? 1 : maxRepititions);

        // construct a response processor that tracks the changes and informs
        // the response processors
        // for the child trackers
        return new ChildTrackerResponseProcessor(parentBuilder, builders, nonRepeaters, repeaters);
    }
}
