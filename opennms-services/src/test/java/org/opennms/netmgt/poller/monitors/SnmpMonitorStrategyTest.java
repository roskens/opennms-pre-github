/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.poller.monitors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.MonitoredService;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.test.ThrowableAnticipator;

/**
 * The Class SnmpMonitorStrategyTest.
 *
 * @author brozow
 *         TODO To change the template for this generated type comment go to
 *         Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class SnmpMonitorStrategyTest {

    /** The monitor. */
    private SnmpMonitorStrategy monitor = new SnmpMonitorStrategy() {
        @Override
        public PollStatus poll(MonitoredService svc, Map<String, Object> parameters) {
            throw new UnsupportedOperationException("method not implemented; go away, punk!");
        }
    };

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        MockLogAppender.setupLogging();
    }

    /**
     * Test meets criteria with null result.
     */
    @Test
    public void testMeetsCriteriaWithNullResult() {
        SnmpValue result = null;
        assertFalse(monitor.meetsCriteria(result, null, null));
    }

    /**
     * Test meets criteria with snmp null.
     */
    @Test
    public void testMeetsCriteriaWithSnmpNull() {
        SnmpValue result = nullValue();
        testSyntaxEquals(result, "", "1");
    }

    /**
     * Test meets criteria with string.
     */
    @Test
    public void testMeetsCriteriaWithString() {
        SnmpValue result = octetString("A Test String");
        testSyntaxEquals(result, "A Test String", "a test string");
        testSyntaxMatches(result, "[tT][eE][sS][tT]", "test");
        testSyntaxMatches(result, "^A Test String$", "^A Test$");
    }

    /**
     * Test meets criteria with object id.
     */
    @Test
    public void testMeetsCriteriaWithObjectID() {
        SnmpValue result = oid(".1.2.3.4.5.6.7.8.9");
        testSyntaxEquals(result, ".1.2.3.4.5.6.7.8.9", "..1.2.3.4.5.6.7.8.9");
        testSyntaxMatches(result, "\\.7\\.", "\\.11\\.");
    }

    /**
     * Test meets criteria with ip addr.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testMeetsCriteriaWithIPAddr() throws Exception {
        SnmpValue result = ipAddr("10.1.1.1");
        testSyntaxEquals(result, "10.1.1.1", "10.1.1.2");
        testSyntaxMatches(result, "10\\.1\\.1\\.[1-5]", "10\\.1\\.1\\.[02-9]");
    }

    /**
     * Test numeric string.
     */
    @Test
    public void testNumericString() {
        SnmpValue result = octetString("12345");
        testOrderOperations(result, 12345);
    }

    /**
     * Test meets criteria with integer.
     */
    @Test
    public void testMeetsCriteriaWithInteger() {
        SnmpValue result = int32Value(1234);
        testSyntaxEquals(result, "1234", "2234");
        testOrderOperations(result, 1234);
        testSyntaxMatches(result, "23", "14");
    }

    /**
     * Int32 value.
     *
     * @param i
     *            the i
     * @return the snmp value
     */
    private SnmpValue int32Value(int i) {
        return SnmpUtils.getValueFactory().getInt32(i);
    }

    /**
     * Test meets criteria with counter32.
     */
    @Test
    public void testMeetsCriteriaWithCounter32() {
        SnmpValue result = counter32Value(1);
        testSyntaxEquals(result, "1", "2");
        testOrderOperations(result, 1);
    }

    /**
     * Counter32 value.
     *
     * @param i
     *            the i
     * @return the snmp value
     */
    private SnmpValue counter32Value(int i) {
        return SnmpUtils.getValueFactory().getCounter32(i);
    }

    /**
     * Test meets criteria with gauge32.
     */
    @Test
    public void testMeetsCriteriaWithGauge32() {
        SnmpValue result = gauge32Value(1);
        testSyntaxEquals(result, "1", "2");
        testOrderOperations(result, 1);
    }

    /**
     * Gauge32 value.
     *
     * @param i
     *            the i
     * @return the snmp value
     */
    private SnmpValue gauge32Value(int i) {
        return SnmpUtils.getValueFactory().getGauge32(i);
    }

    /**
     * Test meets criteria with time ticks.
     */
    @Test
    public void testMeetsCriteriaWithTimeTicks() {
        SnmpValue result = timeticks("1");
        testSyntaxEquals(result, "1", "2");
        testOrderOperations(result, 1);
    }

    /**
     * Timeticks.
     *
     * @param val
     *            the val
     * @return the snmp value
     */
    private SnmpValue timeticks(String val) {
        return SnmpUtils.getValueFactory().getTimeTicks(Long.parseLong(val));
    }

    /**
     * Test meets criteria with counter64.
     */
    @Test
    public void testMeetsCriteriaWithCounter64() {
        SnmpValue result = counter64Value(1);
        testSyntaxEquals(result, "1", "2");
        testOrderOperations(result, 1);
    }

    /**
     * Counter64 value.
     *
     * @param i
     *            the i
     * @return the snmp value
     */
    private SnmpValue counter64Value(int i) {
        return SnmpUtils.getValueFactory().getCounter64(BigInteger.valueOf(i));
    }

    /**
     * Test error conditions.
     */
    @Test
    public void testErrorConditions() {
        SnmpValue result = int32Value(1);

        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException("operator X is unknown"));
        try {
            monitor.meetsCriteria(result, "X", "123");
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        ta.verifyAnticipated();
    }

    /**
     * Test error conditions2.
     */
    @Test
    public void testErrorConditions2() {
        SnmpValue result = int32Value(1);

        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new NumberFormatException("For input string: \"abc\""));
        try {
            monitor.meetsCriteria(result, "<", "abc");
            fail("expected an exception");
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        ta.verifyAnticipated();
    }

    /**
     * Test syntax equals.
     *
     * @param result
     *            the result
     * @param eqString
     *            the eq string
     * @param neString
     *            the ne string
     */
    private void testSyntaxEquals(SnmpValue result, String eqString, String neString) {
        assertTrue(monitor.meetsCriteria(result, null, null));

        assertTrue("result '" + result + "' should pass equal test with '" + eqString + "'",
                   monitor.meetsCriteria(result, SnmpMonitor.EQUALS, eqString));
        assertFalse("result '" + result + "' should fail equal test with '" + neString + "'",
                    monitor.meetsCriteria(result, SnmpMonitor.EQUALS, neString));

        assertFalse("result '" + result + "' should fail not equal test with '" + eqString + "'",
                    monitor.meetsCriteria(result, SnmpMonitor.NOT_EQUAL, eqString));
        assertTrue("result '" + result + "' should pass not equal test with '" + neString + "'",
                   monitor.meetsCriteria(result, SnmpMonitor.NOT_EQUAL, neString));

    }

    /**
     * Test syntax matches.
     *
     * @param result
     *            the result
     * @param matchString
     *            the match string
     * @param noMatchString
     *            the no match string
     */
    private void testSyntaxMatches(SnmpValue result, String matchString, String noMatchString) {
        assertTrue(monitor.meetsCriteria(result, SnmpMonitor.MATCHES, matchString));
        assertFalse(monitor.meetsCriteria(result, SnmpMonitor.MATCHES, noMatchString));
    }

    /**
     * Test order operations.
     *
     * @param result
     *            the result
     * @param value
     *            the value
     */
    private void testOrderOperations(SnmpValue result, int value) {
        // less-than
        assertTrue(monitor.meetsCriteria(result, SnmpMonitor.LESS_THAN, Integer.toString(value + 1)));
        assertFalse(monitor.meetsCriteria(result, SnmpMonitor.LESS_THAN, Integer.toString(value)));
        assertFalse(monitor.meetsCriteria(result, SnmpMonitor.LESS_THAN, Integer.toString(value - 1)));

        // less-equals
        assertTrue(monitor.meetsCriteria(result, SnmpMonitor.LESS_THAN_EQUALS, Integer.toString(value + 1)));
        assertTrue(monitor.meetsCriteria(result, SnmpMonitor.LESS_THAN_EQUALS, Integer.toString(value)));
        assertFalse(monitor.meetsCriteria(result, SnmpMonitor.LESS_THAN_EQUALS, Integer.toString(value - 1)));

        // greater-than
        assertFalse(monitor.meetsCriteria(result, SnmpMonitor.GREATER_THAN, Integer.toString(value + 1)));
        assertFalse(monitor.meetsCriteria(result, SnmpMonitor.GREATER_THAN, Integer.toString(value)));
        assertTrue(monitor.meetsCriteria(result, SnmpMonitor.GREATER_THAN, Integer.toString(value - 1)));

        // greater-equals
        assertFalse(monitor.meetsCriteria(result, SnmpMonitor.GREATER_THAN_EQUALS, Integer.toString(value + 1)));
        assertTrue(monitor.meetsCriteria(result, SnmpMonitor.GREATER_THAN_EQUALS, Integer.toString(value)));
        assertTrue(monitor.meetsCriteria(result, SnmpMonitor.GREATER_THAN_EQUALS, Integer.toString(value - 1)));
    }

    /**
     * Octet string.
     *
     * @param val
     *            the val
     * @return the snmp value
     */
    SnmpValue octetString(String val) {
        return SnmpUtils.getValueFactory().getOctetString(val.getBytes());
    }

    /**
     * Null value.
     *
     * @return the snmp value
     */
    SnmpValue nullValue() {
        return SnmpUtils.getValueFactory().getNull();
    }

    /**
     * Oid.
     *
     * @param objectId
     *            the object id
     * @return the snmp value
     */
    SnmpValue oid(String objectId) {
        return SnmpUtils.getValueFactory().getObjectId(SnmpObjId.get(objectId));
    }

    /**
     * Ip addr.
     *
     * @param addr
     *            the addr
     * @return the snmp value
     * @throws UnknownHostException
     *             the unknown host exception
     */
    private SnmpValue ipAddr(String addr) throws UnknownHostException {
        return SnmpUtils.getValueFactory().getIpAddress(InetAddressUtils.addr(addr));
    }

}
