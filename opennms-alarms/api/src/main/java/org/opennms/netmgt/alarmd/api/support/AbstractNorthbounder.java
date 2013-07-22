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

package org.opennms.netmgt.alarmd.api.support;

import java.util.List;

import org.opennms.netmgt.alarmd.api.NorthboundAlarm;
import org.opennms.netmgt.alarmd.api.Northbounder;
import org.opennms.netmgt.alarmd.api.NorthbounderException;

/**
 * AbstractNorthBounder
 * The purpose of this class is manage the queue of alarms that need to be
 * forward and receive queries to/from a Southbound Interface.
 * It passes Alarms on to the forwardAlarms method implemented by base classes
 * in batches as they are
 * added to the queue. The forwardAlarms method does the actual work of sending
 * them to the Southbound Interface.
 * preserve, accept and discard are called to add the Alarms to the queue as
 * appropriate.
 *
 * @author <a mailto:david@opennms.org>David Hustace</a>
 */

public abstract class AbstractNorthbounder implements Northbounder, Runnable, StatusFactory<NorthboundAlarm> {

    /** The m_name. */
    private final String m_name;

    /** The m_queue. */
    private final AlarmQueue<NorthboundAlarm> m_queue;

    /** The m_thread. */
    private Thread m_thread;

    /** The m_stopped. */
    private volatile boolean m_stopped = true;

    /** The m_retry interval. */
    private long m_retryInterval = 1000;

    /**
     * Instantiates a new abstract northbounder.
     *
     * @param name
     *            the name
     */
    protected AbstractNorthbounder(String name) {
        m_name = name;
        m_queue = new AlarmQueue<NorthboundAlarm>(this);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.alarmd.api.Northbounder#getName()
     */
    @Override
    public String getName() {
        return m_name;
    }

    /**
     * Sets the nagles delay.
     *
     * @param delay
     *            the new nagles delay
     */
    public void setNaglesDelay(long delay) {
        m_queue.setNaglesDelay(delay);
    }

    /**
     * Sets the retry interval.
     *
     * @param retryInterval
     *            the new retry interval
     */
    public void setRetryInterval(int retryInterval) {
        m_retryInterval = retryInterval;
    }

    /**
     * Sets the max batch size.
     *
     * @param maxBatchSize
     *            the new max batch size
     */
    public void setMaxBatchSize(int maxBatchSize) {
        m_queue.setMaxBatchSize(maxBatchSize);
    }

    /**
     * Sets the max preserved alarms.
     *
     * @param maxPreservedAlarms
     *            the new max preserved alarms
     */
    public void setMaxPreservedAlarms(int maxPreservedAlarms) {
        m_queue.setMaxPreservedAlarms(maxPreservedAlarms);
    }

    /** Override this to perform actions before startup. **/
    protected void onPreStart() {
    }

    /** Override this to perform actions after startup. **/
    protected void onPostStart() {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.alarmd.api.Northbounder#start()
     */
    @Override
    public final void start() throws NorthbounderException {
        if (!m_stopped) {
            return;
        }
        this.onPreStart();
        m_stopped = false;
        m_queue.init();
        m_thread = new Thread(this, getName() + "-Thread");
        m_thread.start();
        this.onPostStart();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.alarmd.api.Northbounder#onAlarm(org.opennms.netmgt.alarmd.api.NorthboundAlarm)
     */
    @Override
    public final void onAlarm(NorthboundAlarm alarm) throws NorthbounderException {
        if (accepts(alarm)) {
            m_queue.accept(alarm);
        }
    };

    /**
     * Accepts.
     *
     * @param alarm
     *            the alarm
     * @return true, if successful
     */
    protected abstract boolean accepts(NorthboundAlarm alarm);

    /**
     * Preserve.
     *
     * @param alarm
     *            the alarm
     * @throws NorthbounderException
     *             the northbounder exception
     */
    protected void preserve(NorthboundAlarm alarm) throws NorthbounderException {
        m_queue.preserve(alarm);
    }

    /**
     * Discard.
     *
     * @param alarm
     *            the alarm
     * @throws NorthbounderException
     *             the northbounder exception
     */
    protected void discard(NorthboundAlarm alarm) throws NorthbounderException {
        m_queue.discard(alarm);
    }

    /** Override this to perform actions when stopping. **/
    protected void onStop() {
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.alarmd.api.Northbounder#stop()
     */
    @Override
    public final void stop() throws NorthbounderException {
        this.onStop();
        m_stopped = true;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

        try {

            while (!m_stopped) {

                List<NorthboundAlarm> alarmsToForward = m_queue.getAlarmsToForward();

                try {
                    forwardAlarms(alarmsToForward);
                    m_queue.forwardSuccessful(alarmsToForward);
                } catch (Exception e) {
                    m_queue.forwardFailed(alarmsToForward);
                    if (!m_stopped) {
                        // a failure occurred so sleep a moment and try again
                        Thread.sleep(m_retryInterval);
                    }
                }

            }

        } catch (InterruptedException e) {
            // thread interrupted so complete it
        }

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.alarmd.api.support.StatusFactory#createSyncLostMessage()
     */
    @Override
    public NorthboundAlarm createSyncLostMessage() {
        return NorthboundAlarm.SYNC_LOST_ALARM;
    }

    /**
     * Forward alarms.
     *
     * @param alarms
     *            the alarms
     * @throws NorthbounderException
     *             the northbounder exception
     */
    public abstract void forwardAlarms(List<NorthboundAlarm> alarms) throws NorthbounderException;

}
