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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ColumnTracker.
 */
public class ColumnTracker extends CollectionTracker {

    /** The Constant LOG. */
    private static final transient Logger LOG = LoggerFactory.getLogger(ColumnTracker.class);

    /** The m_base. */
    private SnmpObjId m_base;

    /** The m_last. */
    private SnmpObjId m_last;

    /** The m_max repetitions. */
    private int m_maxRepetitions;

    /**
     * Instantiates a new column tracker.
     *
     * @param base
     *            the base
     */
    public ColumnTracker(SnmpObjId base) {
        this(null, base);
    }

    /**
     * Instantiates a new column tracker.
     *
     * @param base
     *            the base
     * @param maxRepititions
     *            the max repititions
     */
    public ColumnTracker(SnmpObjId base, int maxRepititions) {
        this(null, base, maxRepititions);
    }

    /**
     * Instantiates a new column tracker.
     *
     * @param parent
     *            the parent
     * @param base
     *            the base
     */
    public ColumnTracker(CollectionTracker parent, SnmpObjId base) {
        this(parent, base, 2);
    }

    /**
     * Instantiates a new column tracker.
     *
     * @param parent
     *            the parent
     * @param base
     *            the base
     * @param maxRepititions
     *            the max repititions
     */
    public ColumnTracker(CollectionTracker parent, SnmpObjId base, int maxRepititions) {
        super(parent);
        m_base = base;
        m_last = base;
        m_maxRepetitions = maxRepititions;
    }

    /**
     * Gets the base.
     *
     * @return the base
     */
    public SnmpObjId getBase() {
        return m_base;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("base", m_base).append("last oid", m_last).append("max repetitions",
                                                                                                  m_maxRepetitions).append("finished?",
                                                                                                                           isFinished()).toString();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.CollectionTracker#buildNextPdu(org.opennms.netmgt.snmp.PduBuilder)
     */
    @Override
    public ResponseProcessor buildNextPdu(PduBuilder pduBuilder) {
        if (pduBuilder.getMaxVarsPerPdu() < 1) {
            throw new IllegalArgumentException("maxVarsPerPdu < 1");
        }

        LOG.debug("Requesting oid following: {}", m_last);
        pduBuilder.addOid(m_last);
        pduBuilder.setNonRepeaters(0);
        pduBuilder.setMaxRepetitions(getMaxRepetitions());

        ResponseProcessor rp = new ResponseProcessor() {

            @Override
            public void processResponse(SnmpObjId responseObjId, SnmpValue val) {
                if (val.isEndOfMib()) {
                    receivedEndOfMib();
                    return;
                }
                LOG.debug("Processing varBind: {} = {}", responseObjId, val);

                m_last = responseObjId;
                if (m_base.isPrefixOf(responseObjId) && !m_base.equals(responseObjId)) {
                    SnmpInstId inst = responseObjId.getInstance(m_base);
                    if (inst != null) {
                        storeResult(new SnmpResult(m_base, inst, val));
                    }
                }

                if (!m_base.isPrefixOf(m_last)) {
                    setFinished(true);
                }

            }

            @Override
            public boolean processErrors(int errorStatus, int errorIndex) {
                if (errorStatus == NO_ERR) {
                    return false;
                } else if (errorStatus == TOO_BIG_ERR) {
                    throw new IllegalArgumentException("Unable to handle tooBigError for next oid request after "
                            + m_last);
                } else if (errorStatus == GEN_ERR) {
                    reportGenErr("Received genErr requesting next oid after " + m_last
                            + ". Marking column is finished.");
                    errorOccurred();
                    return true;
                } else if (errorStatus == NO_SUCH_NAME_ERR) {
                    reportNoSuchNameErr("Received noSuchName requesting next oid after " + m_last
                            + ". Marking column is finished.");
                    errorOccurred();
                    return true;
                } else {
                    throw new IllegalArgumentException("Unexpected error processing next oid after " + m_last
                            + ". Aborting!");
                }
            }
        };

        return rp;
    }

    /**
     * Gets the max repetitions.
     *
     * @return the max repetitions
     */
    public int getMaxRepetitions() {
        return m_maxRepetitions;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.CollectionTracker#setMaxRepetitions(int)
     */
    @Override
    public void setMaxRepetitions(int maxRepetitions) {
        m_maxRepetitions = maxRepetitions;
    }

    /**
     * Received end of mib.
     */
    protected void receivedEndOfMib() {
        setFinished(true);
    }

    /**
     * Error occurred.
     */
    protected void errorOccurred() {
        setFinished(true);
    }

    /**
     * Gets the last instance.
     *
     * @return the last instance
     */
    public SnmpInstId getLastInstance() {
        if (m_base.isPrefixOf(m_last) && !m_base.equals(m_last)) {
            return m_last.getInstance(m_base);
        } else {
            return null;
        }
    }

}
