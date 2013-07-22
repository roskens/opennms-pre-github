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

package org.opennms.netmgt.icmp.jna;

import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import junit.framework.TestCase;

import org.opennms.core.utils.CollectionMath;
import org.opennms.netmgt.icmp.EchoPacket;
import org.opennms.netmgt.icmp.PingConstants;
import org.opennms.netmgt.icmp.PingResponseCallback;

/**
 * The Class JnaPingTest.
 *
 * @author <a href="mailto:ranger@opennms.org>Ben Reed</a>
 */
public class JnaPingTest extends TestCase {

    /** The s_jna pinger. */
    private static JnaPinger s_jnaPinger = new JnaPinger();

    /** The m_good host. */
    private InetAddress m_goodHost = null;

    /** The m_bad host. */
    private InetAddress m_badHost = null;

    /** The m_ipv6good host. */
    private InetAddress m_ipv6goodHost = null;

    /** The m_ipv6bad host. */
    private InetAddress m_ipv6badHost = null;

    /**
     * Don't run this test unless the runPingTests property
     * is set to "true".
     *
     * @throws Throwable
     *             the throwable
     */
    @Override
    protected void runTest() throws Throwable {
        if (!isRunTest()) {
            System.err.println("Skipping test '" + getName() + "' because system property '" + getRunTestProperty()
                    + "' is not set to 'true'");
            return;
        }

        try {
            System.err.println("------------------- begin " + getName() + " ---------------------");
            super.runTest();
        } finally {
            System.err.println("------------------- end " + getName() + " -----------------------");
        }
    }

    /**
     * Checks if is run test.
     *
     * @return true, if is run test
     */
    private boolean isRunTest() {
        return Boolean.getBoolean(getRunTestProperty());
    }

    /**
     * Gets the run test property.
     *
     * @return the run test property
     */
    private String getRunTestProperty() {
        return "runPingTests";
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        if (!isRunTest()) {
            return;
        }

        super.setUp();
        m_goodHost = InetAddress.getLocalHost();
        // 192.0.2.0/24 is reserved for documentation purposes
        m_badHost = InetAddress.getByName("192.0.2.123");
        m_ipv6goodHost = InetAddress.getByName("::1");
        // 2001:db8 prefix is reserved for documentation purposes suffix is
        // 'BadAddr!' as ascii
        m_ipv6badHost = InetAddress.getByName("2001:0db8::4261:6441:6464:7221");
        assertEquals(16, m_ipv6badHost.getAddress().length);

    }

    /**
     * Single ping good.
     *
     * @param addr
     *            the addr
     * @throws Exception
     *             the exception
     */
    private void singlePingGood(InetAddress addr) throws Exception {
        Number rtt = s_jnaPinger.ping(addr);
        assertNotNull("No RTT value returned from ping, looks like the ping failed", rtt);
        assertTrue("Negative RTT value returned from ping", rtt.doubleValue() > 0);
    }

    /**
     * Test single ping i pv4.
     *
     * @throws Exception
     *             the exception
     */
    public void testSinglePingIPv4() throws Exception {
        singlePingGood(m_goodHost);
    }

    /**
     * Test single ping i pv6.
     *
     * @throws Exception
     *             the exception
     */
    public void testSinglePingIPv6() throws Exception {
        singlePingGood(m_ipv6goodHost);
    }

    /**
     * The Class TestPingResponseCallback.
     */
    private static class TestPingResponseCallback implements PingResponseCallback {

        /** The m_latch. */
        private final CountDownLatch m_latch = new CountDownLatch(1);

        /** The m_address. */
        private InetAddress m_address;

        /** The m_packet. */
        private EchoPacket m_packet;

        /** The m_throwable. */
        private Throwable m_throwable;

        /** The m_timeout. */
        private boolean m_timeout = false;

        /* (non-Javadoc)
         * @see org.opennms.netmgt.icmp.PingResponseCallback#handleResponse(java.net.InetAddress, org.opennms.netmgt.icmp.EchoPacket)
         */
        @Override
        public void handleResponse(InetAddress address, EchoPacket response) {
            m_address = address;
            m_packet = response;
            m_latch.countDown();
            System.err.println("RESPONSE COUNTED DOWN");
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.icmp.PingResponseCallback#handleTimeout(java.net.InetAddress, org.opennms.netmgt.icmp.EchoPacket)
         */
        @Override
        public void handleTimeout(InetAddress address, EchoPacket request) {
            m_timeout = true;
            m_address = address;
            m_packet = request;
            m_latch.countDown();
            System.err.println("TIMEOUT COUNTED DOWN");
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.icmp.PingResponseCallback#handleError(java.net.InetAddress, org.opennms.netmgt.icmp.EchoPacket, java.lang.Throwable)
         */
        @Override
        public void handleError(InetAddress address, EchoPacket request, Throwable t) {
            m_address = address;
            m_packet = request;
            m_throwable = t;
            m_latch.countDown();
            System.err.println("ERROR COUNTED DOWN");
            t.printStackTrace();
        }

        /**
         * Await.
         *
         * @throws InterruptedException
         *             the interrupted exception
         */
        public void await() throws InterruptedException {
            m_latch.await();
        }

        /**
         * Gets the address.
         *
         * @return the address
         */
        public InetAddress getAddress() {
            return m_address;
        }

        /**
         * Gets the packet.
         *
         * @return the packet
         */
        public EchoPacket getPacket() {
            return m_packet;
        }

        /**
         * Gets the throwable.
         *
         * @return the throwable
         */
        public Throwable getThrowable() {
            return m_throwable;
        }

        /**
         * Checks if is timeout.
         *
         * @return the timeout
         */
        public boolean isTimeout() {
            return m_timeout;
        }

    };

    /**
     * Test ping callback timeout i pv4.
     *
     * @throws Exception
     *             the exception
     */
    public void testPingCallbackTimeoutIPv4() throws Exception {
        pingCallbackTimeout(m_badHost);
    }

    /**
     * Test ping callback timeout i pv6.
     *
     * @throws Exception
     *             the exception
     */
    public void testPingCallbackTimeoutIPv6() throws Exception {
        pingCallbackTimeout(m_ipv6badHost);
    }

    /**
     * Ping callback timeout.
     *
     * @param addr
     *            the addr
     * @throws Exception
     *             the exception
     */
    private void pingCallbackTimeout(InetAddress addr) throws Exception {
        TestPingResponseCallback cb = new TestPingResponseCallback();

        s_jnaPinger.ping(addr, PingConstants.DEFAULT_TIMEOUT, PingConstants.DEFAULT_RETRIES,
                         PingConstants.DEFAULT_PACKET_SIZE, 1, cb);

        cb.await();

        assertTrue("Unexpected Error sending ping to " + addr + ": " + cb.getThrowable(), cb.getThrowable() == null
                || cb.getThrowable() instanceof NoRouteToHostException);
        assertTrue(cb.isTimeout());
        assertNotNull(cb.getPacket());
        assertNotNull(cb.getAddress());
    }

    /**
     * Test single ping failure i pv4.
     *
     * @throws Exception
     *             the exception
     */
    public void testSinglePingFailureIPv4() throws Exception {
        assertNull(s_jnaPinger.ping(m_badHost));
    }

    /**
     * Test single ping failure i pv6.
     *
     * @throws Exception
     *             the exception
     */
    public void testSinglePingFailureIPv6() throws Exception {
        assertNull(s_jnaPinger.ping(m_ipv6badHost));
    }

    /**
     * Test parallel ping i pv4.
     *
     * @throws Exception
     *             the exception
     */
    public void testParallelPingIPv4() throws Exception {
        parallelPingGood(m_goodHost);
    }

    /**
     * Test parallel ping i pv6.
     *
     * @throws Exception
     *             the exception
     */
    public void testParallelPingIPv6() throws Exception {
        parallelPingGood(m_ipv6goodHost);
    }

    /**
     * Parallel ping good.
     *
     * @param addr
     *            the addr
     * @throws Exception
     *             the exception
     * @throws InterruptedException
     *             the interrupted exception
     */
    private void parallelPingGood(InetAddress addr) throws Exception, InterruptedException {
        List<Number> items = s_jnaPinger.parallelPing(addr, 20, PingConstants.DEFAULT_TIMEOUT, 50);
        Thread.sleep(1000);
        printResponse(items);
        assertTrue("Collection contained all null values, all parallel pings failed",
                   CollectionMath.countNotNull(items) > 0);
        for (Number item : items) {
            assertNotNull("Found a null reponse time in the response", item);
            assertTrue("Negative RTT value returned from ping", item.floatValue() > 0);
        }
    }

    /**
     * Test parallel ping failure i pv4.
     *
     * @throws Exception
     *             the exception
     */
    public void testParallelPingFailureIPv4() throws Exception {
        parallelPingFailure(m_badHost);
    }

    /**
     * Test parallel ping failure i pv6.
     *
     * @throws Exception
     *             the exception
     */
    public void testParallelPingFailureIPv6() throws Exception {
        parallelPingFailure(m_ipv6badHost);
    }

    /**
     * Parallel ping failure.
     *
     * @param addr
     *            the addr
     * @throws Exception
     *             the exception
     */
    private void parallelPingFailure(InetAddress addr) throws Exception {
        List<Number> items = s_jnaPinger.parallelPing(addr, 20, PingConstants.DEFAULT_TIMEOUT, 50);
        Thread.sleep(PingConstants.DEFAULT_TIMEOUT + 100);
        printResponse(items);
        assertTrue("Collection contained some numeric values when all parallel pings should have failed",
                   CollectionMath.countNotNull(items) == 0);
    }

    /**
     * Prints the response.
     *
     * @param items
     *            the items
     */
    private void printResponse(List<Number> items) {
        Long passed = CollectionMath.countNotNull(items);
        Long failed = CollectionMath.countNull(items);
        Number passedPercent = CollectionMath.percentNotNull(items);
        Number failedPercent = CollectionMath.percentNull(items);
        Number average = CollectionMath.average(items);
        Number median = CollectionMath.median(items);

        if (passedPercent == null) {
            passedPercent = Long.valueOf(0);
        }
        if (failedPercent == null) {
            failedPercent = Long.valueOf(100);
        }
        if (median == null) {
            median = Double.valueOf(0);
        }

        if (average == null) {
            average = new Double(0);
        } else {
            average = new Double(average.doubleValue() / 1000.0);
        }

        StringBuffer sb = new StringBuffer();
        sb.append("response times = ").append(items);
        sb.append("\n");

        sb.append("pings = ").append(items.size());
        sb.append(", passed = ").append(passed).append(" (").append(passedPercent).append("%)");
        sb.append(", failed = ").append(failed).append(" (").append(failedPercent).append("%)");
        sb.append(", median = ").append(median);
        sb.append(", average = ").append(average).append("ms");
        sb.append("\n");
        System.out.print(sb);
    }
}
