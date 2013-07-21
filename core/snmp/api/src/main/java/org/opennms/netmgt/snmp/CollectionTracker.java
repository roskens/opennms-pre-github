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

/**
 * The Class CollectionTracker.
 */
public abstract class CollectionTracker implements Collectable {

    /** The Constant NO_ERR. */
    public static final int NO_ERR = 0;

    /** The Constant TOO_BIG_ERR. */
    public static final int TOO_BIG_ERR = 1;

    /** The Constant NO_SUCH_NAME_ERR. */
    public static final int NO_SUCH_NAME_ERR = 2;

    /** The Constant GEN_ERR. */
    public static final int GEN_ERR = 5;

    /** The m_parent. */
    private CollectionTracker m_parent;

    /** The m_failed. */
    private boolean m_failed = false;

    /** The m_timed out. */
    private boolean m_timedOut = false;

    /** The m_finished. */
    private boolean m_finished = false;

    /**
     * Instantiates a new collection tracker.
     */
    public CollectionTracker() {
        this(null);
    }

    /**
     * Instantiates a new collection tracker.
     *
     * @param parent
     *            the parent
     */
    public CollectionTracker(CollectionTracker parent) {
        m_parent = parent;
    }

    /**
     * Sets the parent.
     *
     * @param parent
     *            the new parent
     */
    public void setParent(CollectionTracker parent) {
        m_parent = parent;
    }

    /**
     * Gets the parent.
     *
     * @return the parent
     */
    public CollectionTracker getParent() {
        return m_parent;
    }

    /**
     * Failed.
     *
     * @return true, if successful
     */
    public boolean failed() {
        return m_failed || m_timedOut;
    }

    /**
     * Timed out.
     *
     * @return true, if successful
     */
    public boolean timedOut() {
        return m_timedOut;
    }

    /**
     * Sets the max repetitions.
     *
     * @param maxRepetitions
     *            the new max repetitions
     */
    public abstract void setMaxRepetitions(int maxRepetitions);

    /**
     * Sets the failed.
     *
     * @param failed
     *            the new failed
     */
    public void setFailed(boolean failed) {
        m_failed = failed;
    }

    /**
     * Sets the timed out.
     *
     * @param timedOut
     *            the new timed out
     */
    public void setTimedOut(boolean timedOut) {
        m_timedOut = timedOut;
    }

    /**
     * Store result.
     *
     * @param res
     *            the res
     */
    protected void storeResult(SnmpResult res) {
        if (m_parent != null) {
            m_parent.storeResult(res);
        }
    }

    /**
     * Checks if is finished.
     *
     * @return true, if is finished
     */
    public boolean isFinished() {
        return m_finished;
    }

    /**
     * Sets the finished.
     *
     * @param finished
     *            the new finished
     */
    public final void setFinished(boolean finished) {
        m_finished = finished;
    }

    /**
     * Builds the next pdu.
     *
     * @param pduBuilder
     *            the pdu builder
     * @return the response processor
     */
    public abstract ResponseProcessor buildNextPdu(PduBuilder pduBuilder);

    /**
     * Report too big err.
     *
     * @param msg
     *            the msg
     */
    protected void reportTooBigErr(String msg) {
        if (m_parent != null) {
            m_parent.reportTooBigErr(msg);
        }
    }

    /**
     * Report gen err.
     *
     * @param msg
     *            the msg
     */
    protected void reportGenErr(String msg) {
        if (m_parent != null) {
            m_parent.reportGenErr(msg);
        }
    }

    /**
     * Report no such name err.
     *
     * @param msg
     *            the msg
     */
    protected void reportNoSuchNameErr(String msg) {
        if (m_parent != null) {
            m_parent.reportNoSuchNameErr(msg);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.snmp.Collectable#getCollectionTracker()
     */
    @Override
    public CollectionTracker getCollectionTracker() {
        return this;
    }

}
