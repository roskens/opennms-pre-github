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

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SnmpWalker.
 */
public abstract class SnmpWalker {

    /** The Constant LOG. */
    private static final transient Logger LOG = LoggerFactory.getLogger(SnmpWalker.class);

    /**
     * The Class WalkerPduBuilder.
     */
    protected abstract static class WalkerPduBuilder extends PduBuilder {

        /**
         * Instantiates a new walker pdu builder.
         *
         * @param maxVarsPerPdu
         *            the max vars per pdu
         */
        protected WalkerPduBuilder(int maxVarsPerPdu) {
            super(maxVarsPerPdu);
        }

        /**
         * Reset.
         */
        public abstract void reset();
    }

    /** The m_name. */
    private final String m_name;

    /** The m_tracker. */
    private final CollectionTracker m_tracker;

    /** The m_signal. */
    private final CountDownLatch m_signal;

    /** The m_address. */
    private InetAddress m_address;

    /** The m_pdu builder. */
    private WalkerPduBuilder m_pduBuilder;

    /** The m_response processor. */
    private ResponseProcessor m_responseProcessor;

    /** The m_max vars per pdu. */
    private final int m_maxVarsPerPdu;

    /** The m_error. */
    private boolean m_error = false;

    /** The m_error message. */
    private String m_errorMessage = "";

    /** The m_error throwable. */
    private Throwable m_errorThrowable = null;

    /**
     * Instantiates a new snmp walker.
     *
     * @param address
     *            the address
     * @param name
     *            the name
     * @param maxVarsPerPdu
     *            the max vars per pdu
     * @param maxRepititions
     *            the max repititions
     * @param tracker
     *            the tracker
     */
    protected SnmpWalker(InetAddress address, String name, int maxVarsPerPdu, int maxRepititions,
            CollectionTracker tracker) {
        m_address = address;
        m_signal = new CountDownLatch(1);

        m_name = name;

        m_tracker = tracker;
        m_tracker.setMaxRepetitions(maxRepititions);

        m_maxVarsPerPdu = maxVarsPerPdu;
    }

    /**
     * Creates the pdu builder.
     *
     * @param maxVarsPerPdu
     *            the max vars per pdu
     * @return the walker pdu builder
     */
    protected abstract WalkerPduBuilder createPduBuilder(int maxVarsPerPdu);

    /**
     * Start.
     */
    public void start() {
        m_pduBuilder = createPduBuilder(m_maxVarsPerPdu);
        try {
            buildAndSendNextPdu();
        } catch (Throwable e) {
            handleFatalError(e);
        }
    }

    /**
     * Gets the max vars per pdu.
     *
     * @return the max vars per pdu
     */
    public int getMaxVarsPerPdu() {
        return (m_pduBuilder == null ? m_maxVarsPerPdu : m_pduBuilder.getMaxVarsPerPdu());
    }

    /**
     * Builds the and send next pdu.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected void buildAndSendNextPdu() throws IOException {
        if (m_tracker.isFinished()) {
            handleDone();
        } else {
            m_pduBuilder.reset();
            m_responseProcessor = m_tracker.buildNextPdu(m_pduBuilder);
            sendNextPdu(m_pduBuilder);
        }
    }

    /**
     * Send next pdu.
     *
     * @param pduBuilder
     *            the pdu builder
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected abstract void sendNextPdu(WalkerPduBuilder pduBuilder) throws IOException;

    /**
     * Handle done.
     */
    protected void handleDone() {
        finish();
    }

    /**
     * <P>
     * Returns the success or failure code for collection of the data.
     * </P>
     *
     * @return true, if successful
     */
    public boolean failed() {
        return m_error;
    }

    /**
     * Timed out.
     *
     * @return true, if successful
     */
    public boolean timedOut() {
        return m_tracker.timedOut();
    }

    /**
     * Handle auth error.
     *
     * @param msg
     *            the msg
     */
    protected void handleAuthError(String msg) {
        m_tracker.setFailed(true);
        processError("Authentication error processing", msg, null);
    }

    /**
     * Handle error.
     *
     * @param msg
     *            the msg
     */
    protected void handleError(String msg) {
        // XXX why do we set timedOut to false here? should we be doing this
        // everywhere?
        m_tracker.setTimedOut(false);
        processError("Error retrieving", msg, null);
    }

    /**
     * Handle error.
     *
     * @param msg
     *            the msg
     * @param t
     *            the t
     */
    protected void handleError(String msg, Throwable t) {
        // XXX why do we set timedOut to false here? should we be doing this
        // everywhere?
        m_tracker.setTimedOut(false);
        processError("Error retrieving", msg, t);
    }

    /**
     * Handle fatal error.
     *
     * @param e
     *            the e
     */
    protected void handleFatalError(Throwable e) {
        m_tracker.setFailed(true);
        processError("Unexpected error occurred processing", e.toString(), e);
    }

    /**
     * Handle timeout.
     *
     * @param msg
     *            the msg
     */
    protected void handleTimeout(String msg) {
        m_tracker.setTimedOut(true);
        processError("Timeout retrieving", msg, null);
    }

    /**
     * Process error.
     *
     * @param reason
     *            the reason
     * @param cause
     *            the cause
     * @param t
     *            the t
     */
    private void processError(String reason, String cause, Throwable t) {
        String logMessage = reason + " " + getName() + " for " + m_address + ": " + cause;

        m_error = true;
        m_errorMessage = logMessage;
        m_errorThrowable = t;

        finish();
    }

    /**
     * Finish.
     */
    private void finish() {
        signal();
        try {
            close();
        } catch (IOException e) {
            LOG.error("{}: Unexpected Error occured closing SNMP session for: {}", getName(), m_address, e);
        }
    }

    /**
     * Close.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    protected abstract void close() throws IOException;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Signal.
     */
    private void signal() {
        synchronized (this) {
            notifyAll();
        }
        if (m_signal != null) {
            m_signal.countDown();
        }
    }

    /**
     * Wait for.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    public void waitFor() throws InterruptedException {
        m_signal.await();
    }

    /**
     * Wait for.
     *
     * @param timeout
     *            the timeout
     * @throws InterruptedException
     *             the interrupted exception
     */
    public void waitFor(long timeout) throws InterruptedException {
        m_signal.await(timeout, TimeUnit.MILLISECONDS);
    }

    // processErrors returns true if we need to retry the request and false
    // otherwise
    /**
     * Process errors.
     *
     * @param errorStatus
     *            the error status
     * @param errorIndex
     *            the error index
     * @return true, if successful
     */
    protected boolean processErrors(int errorStatus, int errorIndex) {
        return m_responseProcessor.processErrors(errorStatus, errorIndex);
    }

    /**
     * Process response.
     *
     * @param receivedOid
     *            the received oid
     * @param val
     *            the val
     */
    protected void processResponse(SnmpObjId receivedOid, SnmpValue val) {
        m_responseProcessor.processResponse(receivedOid, val);
    }

    /**
     * Sets the address.
     *
     * @param address
     *            the new address
     */
    protected void setAddress(InetAddress address) {
        m_address = address;
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    protected InetAddress getAddress() {
        return m_address;
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return m_errorMessage;
    }

    /**
     * Gets the error throwable.
     *
     * @return the error throwable
     */
    public Throwable getErrorThrowable() {
        return m_errorThrowable;
    }

}
