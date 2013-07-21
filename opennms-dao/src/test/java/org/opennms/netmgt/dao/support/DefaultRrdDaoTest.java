/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao.support;

import static org.easymock.EasyMock.expect;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import junit.framework.TestCase;

import org.opennms.netmgt.mock.MockResourceType;
import org.opennms.netmgt.model.OnmsAttribute;
import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.RrdGraphAttribute;
import org.opennms.netmgt.rrd.DefaultRrdGraphDetails;
import org.opennms.netmgt.rrd.RrdException;
import org.opennms.netmgt.rrd.RrdStrategy;
import org.opennms.netmgt.rrd.RrdUtils;
import org.opennms.netmgt.rrd.jrobin.JRobinRrdStrategy;
import org.opennms.test.ThrowableAnticipator;
import org.opennms.test.mock.EasyMockUtils;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.StringUtils;

/**
 * The Class DefaultRrdDaoTest.
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class DefaultRrdDaoTest extends TestCase {

    /** The m_mocks. */
    private EasyMockUtils m_mocks = new EasyMockUtils();

    /** The m_rrd strategy. */
    private RrdStrategy<?, ?> m_rrdStrategy = m_mocks.createMock(RrdStrategy.class);

    /** The m_dao. */
    private DefaultRrdDao m_dao;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        RrdUtils.setStrategy(new JRobinRrdStrategy());

        m_dao = new DefaultRrdDao();
        m_dao.setRrdStrategy(m_rrdStrategy);
        m_dao.setRrdBaseDirectory(new File(System.getProperty("java.io.tmpdir")));
        m_dao.setRrdBinaryPath("/bin/true");
        m_dao.afterPropertiesSet();
    }

    /**
     * Test init.
     */
    public void testInit() {
        // Don't do anything... test that the setUp method works
    }

    /**
     * Test print value.
     *
     * @throws Exception
     *             the exception
     */
    public void testPrintValue() throws Exception {
        long end = System.currentTimeMillis();
        long start = end - (24 * 60 * 60 * 1000);
        OnmsResource childResource = preparePrintValueTest(start, end, "1");

        m_mocks.replayAll();
        Double value = m_dao.getPrintValue(childResource.getAttributes().iterator().next(), "AVERAGE", start, end);
        m_mocks.verifyAll();

        assertNotNull("value should not be null", value);
        assertEquals("value", 1.0, value);
    }

    /**
     * Test print value with na n.
     *
     * @throws Exception
     *             the exception
     */
    public void testPrintValueWithNaN() throws Exception {
        long end = System.currentTimeMillis();
        long start = end - (24 * 60 * 60 * 1000);
        OnmsResource childResource = preparePrintValueTest(start, end, "NaN");

        m_mocks.replayAll();
        Double value = m_dao.getPrintValue(childResource.getAttributes().iterator().next(), "AVERAGE", start, end);
        m_mocks.verifyAll();

        assertNotNull("value should not be null", value);
        assertEquals("value", Double.NaN, value);
    }

    /**
     * Test print value withnan.
     *
     * @throws Exception
     *             the exception
     */
    public void testPrintValueWithnan() throws Exception {
        long end = System.currentTimeMillis();
        long start = end - (24 * 60 * 60 * 1000);
        OnmsResource childResource = preparePrintValueTest(start, end, "nan");

        m_mocks.replayAll();
        Double value = m_dao.getPrintValue(childResource.getAttributes().iterator().next(), "AVERAGE", start, end);
        m_mocks.verifyAll();

        assertNotNull("value should not be null", value);
        assertEquals("value", Double.NaN, value);
    }

    // NMS-5275
    /**
     * Test print value with negative nan.
     *
     * @throws Exception
     *             the exception
     */
    public void testPrintValueWithNegativeNan() throws Exception {
        long end = System.currentTimeMillis();
        long start = end - (24 * 60 * 60 * 1000);
        OnmsResource childResource = preparePrintValueTest(start, end, "-nan");

        m_mocks.replayAll();
        Double value = m_dao.getPrintValue(childResource.getAttributes().iterator().next(), "AVERAGE", start, end);
        m_mocks.verifyAll();

        assertNotNull("value should not be null", value);
        assertEquals("value", Double.NaN, value);
    }

    /**
     * Test print value with bogus line.
     *
     * @throws Exception
     *             the exception
     */
    public void testPrintValueWithBogusLine() throws Exception {
        long end = System.currentTimeMillis();
        long start = end - (24 * 60 * 60 * 1000);
        String printLine = "blah blah blah this should be a floating point number blah blah blah";

        OnmsResource childResource = preparePrintValueTest(start, end, printLine);

        m_mocks.replayAll();

        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new DataAccessResourceFailureException(
                                                             "Value of line 1 of output from RRD is not a valid floating point number: '"
                                                                     + printLine + "'"));
        try {
            m_dao.getPrintValue(childResource.getAttributes().iterator().next(), "AVERAGE", start, end);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        m_mocks.verifyAll();

        ta.verifyAnticipated();
    }

    /**
     * Prepare print value test.
     *
     * @param start
     *            the start
     * @param end
     *            the end
     * @param printLine
     *            the print line
     * @return the onms resource
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws RrdException
     *             the rrd exception
     */
    private OnmsResource preparePrintValueTest(long start, long end, String printLine) throws IOException, RrdException {
        String rrdDir = "snmp" + File.separator + "1" + File.separator + "eth0";
        String rrdFile = "ifInOctets.jrb";

        String escapedFile = rrdDir + File.separator + rrdFile;
        if (File.separatorChar == '\\') {
            escapedFile = escapedFile.replace("\\", "\\\\");
        }

        String[] command = new String[] { m_dao.getRrdBinaryPath(), "graph", "-", "--start=" + (start / 1000),
                "--end=" + (end / 1000), "DEF:ds=" + escapedFile + ":ifInOctets:AVERAGE", "PRINT:ds:AVERAGE:\"%le\"" };
        String commandString = StringUtils.arrayToDelimitedString(command, " ");

        OnmsResource topResource = new OnmsResource("1", "Node One", new MockResourceType(),
                                                    new HashSet<OnmsAttribute>(0));

        OnmsAttribute attribute = new RrdGraphAttribute("ifInOctets", rrdDir, rrdFile);
        HashSet<OnmsAttribute> attributeSet = new HashSet<OnmsAttribute>(1);
        attributeSet.add(attribute);

        MockResourceType childResourceType = new MockResourceType();
        OnmsResource childResource = new OnmsResource("eth0", "Interface One: eth0", childResourceType, attributeSet);
        childResource.setParent(topResource);

        DefaultRrdGraphDetails details = new DefaultRrdGraphDetails();
        details.setPrintLines(new String[] { printLine });
        expect(m_rrdStrategy.createGraphReturnDetails(commandString, m_dao.getRrdBaseDirectory())).andReturn(details);

        return childResource;
    }

    /**
     * Test fetch last value.
     *
     * @throws Exception
     *             the exception
     */
    public void testFetchLastValue() throws Exception {
        String rrdDir = "snmp" + File.separator + "1" + File.separator + "eth0";
        String rrdFile = "ifInOctets.jrb";

        OnmsResource topResource = new OnmsResource("1", "Node One", new MockResourceType(),
                                                    new HashSet<OnmsAttribute>(0));

        OnmsAttribute attribute = new RrdGraphAttribute("ifInOctets", rrdDir, rrdFile);
        HashSet<OnmsAttribute> attributeSet = new HashSet<OnmsAttribute>(1);
        attributeSet.add(attribute);

        MockResourceType childResourceType = new MockResourceType();
        OnmsResource childResource = new OnmsResource("eth0", "Interface One: eth0", childResourceType, attributeSet);
        childResource.setParent(topResource);

        int interval = 300000;
        Double expectedValue = new Double(1.0);

        String fullRrdFilePath = m_dao.getRrdBaseDirectory().getAbsolutePath() + File.separator + rrdDir
                + File.separator + rrdFile;
        expect(m_rrdStrategy.fetchLastValue(fullRrdFilePath, attribute.getName(), interval)).andReturn(expectedValue);

        m_mocks.replayAll();
        Double value = m_dao.getLastFetchValue(attribute, interval);
        m_mocks.verifyAll();

        assertNotNull("last fetched value must not be null, but was null", value);
        assertEquals("last fetched value", expectedValue, value);
    }

    /**
     * Test fetch last value in range.
     *
     * @throws Exception
     *             the exception
     */
    public void testFetchLastValueInRange() throws Exception {
        String rrdDir = "snmp" + File.separator + "1" + File.separator + "eth0";
        String rrdFile = "ifInOctets.jrb";

        OnmsResource topResource = new OnmsResource("1", "Node One", new MockResourceType(),
                                                    new HashSet<OnmsAttribute>(0));

        OnmsAttribute attribute = new RrdGraphAttribute("ifInOctets", rrdDir, rrdFile);
        HashSet<OnmsAttribute> attributeSet = new HashSet<OnmsAttribute>(1);
        attributeSet.add(attribute);

        MockResourceType childResourceType = new MockResourceType();
        OnmsResource childResource = new OnmsResource("eth0", "Interface One: eth0", childResourceType, attributeSet);
        childResource.setParent(topResource);

        int interval = 300000;
        int range = 300000;
        Double expectedValue = new Double(1.0);

        String fullRrdFilePath = m_dao.getRrdBaseDirectory().getAbsolutePath() + File.separator + rrdDir
                + File.separator + rrdFile;
        expect(m_rrdStrategy.fetchLastValueInRange(fullRrdFilePath, attribute.getName(), interval, range)).andReturn(expectedValue);

        m_mocks.replayAll();
        Double value = m_dao.getLastFetchValue(attribute, interval, range);
        m_mocks.verifyAll();

        assertNotNull("last fetched value must not be null, but was null", value);
        assertEquals("last fetched value", expectedValue, value);
    }
}
