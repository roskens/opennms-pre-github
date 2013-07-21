/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.scheduler;

import junit.framework.TestCase;

import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.poller.mock.MockInterval;
import org.opennms.netmgt.poller.mock.MockScheduler;

/**
 * Represents a ScheduleTest.
 *
 * @author brozow
 */
public class ScheduleTest extends TestCase {

    /** The m_schedulable. */
    private MockSchedulable m_schedulable;

    /** The m_interval. */
    private MockInterval m_interval;

    /** The m_scheduler. */
    private MockScheduler m_scheduler;

    /** The m_sched. */
    private Schedule m_sched;

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ScheduleTest.class);
    }

    /**
     * The Class MockSchedulable.
     */
    class MockSchedulable implements ReadyRunnable {

        /** The run count. */
        private volatile int runCount = 0;

        /** The m_calling adjust schedule. */
        private volatile boolean m_callingAdjustSchedule;

        /* (non-Javadoc)
         * @see org.opennms.netmgt.scheduler.ReadyRunnable#isReady()
         */
        @Override
        public boolean isReady() {
            return true;
        }

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            runCount++;
            if (isCallingAdjustSchedule())
                m_sched.adjustSchedule();
        }

        /**
         * Gets the run count.
         *
         * @return the run count
         */
        public int getRunCount() {
            return runCount;
        }

        /**
         * Sets the calling adjust schedule.
         *
         * @param callingAdjustSchedule
         *            the new calling adjust schedule
         */
        public void setCallingAdjustSchedule(boolean callingAdjustSchedule) {
            m_callingAdjustSchedule = callingAdjustSchedule;
        }

        /**
         * Checks if is calling adjust schedule.
         *
         * @return true, if is calling adjust schedule
         */
        public boolean isCallingAdjustSchedule() {
            return m_callingAdjustSchedule;
        }

    }

    /*
     * @see TestCase#setUp()
     */
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockLogAppender.setupLogging();
        m_schedulable = new MockSchedulable();
        m_scheduler = new MockScheduler();
        m_interval = new MockInterval(m_scheduler, 1000L);
        m_sched = new Schedule(m_schedulable, m_interval, m_scheduler);
    }

    /*
     * @see TestCase#tearDown()
     */
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        MockLogAppender.assertNoWarningsOrGreater();
        super.tearDown();
    }

    /**
     * Test schedule.
     */
    public void testSchedule() {
        m_sched.schedule();

        assertRunAndScheduled(0, 0, 0, 1);

        m_scheduler.next();

        assertRunAndScheduled(0, 1000, 1, 1);

        m_scheduler.next();

        assertRunAndScheduled(1000, 1000, 2, 1);
    }

    /**
     * Test adjust schedule.
     */
    public void testAdjustSchedule() {

        m_sched.schedule();

        assertRunAndScheduled(0, 0, 0, 1);

        m_scheduler.next();

        m_interval.setInterval(900);
        m_sched.adjustSchedule();

        assertRunAndScheduled(0, 900, 1, 2);

        m_scheduler.next();

        assertRunAndScheduled(900, 900, 2, 2);

        // jump to the expired entry
        m_scheduler.next();

        // note that we don't increase the run count
        assertRunAndScheduled(1000, 800, 2, 1);

        m_scheduler.next();

        assertRunAndScheduled(1800, 900, 3, 1);

        m_scheduler.next();

        assertRunAndScheduled(2700, 900, 4, 1);

        m_interval.setInterval(1000);
        m_sched.adjustSchedule();

        // jump to the expired entry
        m_scheduler.next();

        assertRunAndScheduled(3600, 100, 4, 1);

        m_scheduler.next();

        assertRunAndScheduled(3700, 1000, 5, 1);

    }

    /**
     * Test unschedule.
     */
    public void testUnschedule() {
        m_sched.schedule();

        assertRunAndScheduled(0, 0, 0, 1);

        m_scheduler.next();

        assertRunAndScheduled(0, 1000, 1, 1);

        m_scheduler.next();

        assertRunAndScheduled(1000, 1000, 2, 1);

        m_sched.unschedule();

        // jump to the expired entry
        m_scheduler.next();

        assertRunAndScheduled(2000, -1, 2, 0);
    }

    /**
     * Test temporarily suspend.
     */
    public void testTemporarilySuspend() {
        m_interval.addSuspension(1500, 2500);

        m_sched.schedule();

        assertRunAndScheduled(0, 0, 0, 1);

        m_scheduler.next();

        assertRunAndScheduled(0, 1000, 1, 1);

        m_scheduler.next();

        assertRunAndScheduled(1000, 1000, 2, 1);

        // this is the suspended entry
        m_scheduler.next();

        // assert that the entry has not run
        assertRunAndScheduled(2000, 1000, 2, 1);

        m_scheduler.next();

        assertRunAndScheduled(3000, 1000, 3, 1);
    }

    /**
     * Test adjust schedule within run.
     */
    public void testAdjustScheduleWithinRun() {
        m_schedulable.setCallingAdjustSchedule(true);

        m_sched.schedule();

        assertRunAndScheduled(0, 0, 0, 1);

        m_scheduler.next();

        assertRunAndScheduled(0, 1000, 1, 1);

        m_scheduler.next();

        assertRunAndScheduled(1000, 1000, 2, 1);
    }

    /**
     * Assert run and scheduled.
     *
     * @param currentTime
     *            the current time
     * @param interval
     *            the interval
     * @param count
     *            the count
     * @param entryCount
     *            the entry count
     */
    private void assertRunAndScheduled(long currentTime, long interval, int count, int entryCount) {
        assertEquals(count, m_schedulable.getRunCount());
        assertEquals(currentTime, m_scheduler.getCurrentTime());
        assertEquals(entryCount, m_scheduler.getEntryCount());
        if (entryCount > 0)
            assertNotNull(m_scheduler.getEntries().get(Long.valueOf(currentTime + interval)));

    }

}
