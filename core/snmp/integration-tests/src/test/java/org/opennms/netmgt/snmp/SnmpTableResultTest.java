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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * The Class SnmpTableResultTest.
 *
 * @author brozow
 */
public class SnmpTableResultTest {

    /** The m_table result. */
    private SnmpTableResult m_tableResult;

    /** The m_row callback. */
    private TestRowCallback m_rowCallback;

    /** The m_columns. */
    private SnmpObjId[] m_columns;

    /** The m_anticipated rows. */
    private List<SnmpRowResult> m_anticipatedRows = new ArrayList<SnmpRowResult>();

    /** The m_received rows. */
    private List<SnmpRowResult> m_receivedRows = new ArrayList<SnmpRowResult>();

    /**
     * The Class TestRowCallback.
     */
    private class TestRowCallback implements RowCallback {

        /** The m_row count. */
        private int m_rowCount = 0;

        /**
         * Gets the row count.
         *
         * @return the row count
         */
        public int getRowCount() {
            return m_rowCount;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.RowCallback#rowCompleted(org.opennms.netmgt.snmp.SnmpRowResult)
         */
        @Override
        public void rowCompleted(SnmpRowResult result) {
            m_rowCount++;
            m_receivedRows.add(result);
            System.err.println("Received Row: " + result);
        }

        /**
         * Reset.
         */
        public void reset() {
            m_rowCount = 0;
        }

    }

    /**
     * Value.
     *
     * @param val
     *            the val
     * @return the snmp value
     */
    public SnmpValue value(String val) {
        return SnmpUtils.getValueFactory().getOctetString(val.getBytes());
    }

    /**
     * Result.
     *
     * @param base
     *            the base
     * @param inst
     *            the inst
     * @return the snmp result
     */
    public SnmpResult result(SnmpObjId base, String inst) {
        return new SnmpResult(base, new SnmpInstId(inst), value(inst));
    }

    /**
     * Verify row count.
     *
     * @param expected
     *            the expected
     */
    public void verifyRowCount(int expected) {
        assertEquals(expected, m_rowCallback.getRowCount());
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        m_rowCallback = new TestRowCallback();
        m_columns = new SnmpObjId[] { SnmpTrackerTest.SnmpTableConstants.ifIndex,
                SnmpTrackerTest.SnmpTableConstants.ifDescr, SnmpTrackerTest.SnmpTableConstants.ifMtu };
        m_tableResult = new SnmpTableResult(m_rowCallback, m_columns);
        System.err.println("---");
    }

    /**
     * Anticipate rows.
     *
     * @param instances
     *            the instances
     */
    public void anticipateRows(String... instances) {
        for (String inst : instances) {
            SnmpRowResult row = new SnmpRowResult(m_columns.length, new SnmpInstId(inst));
            m_anticipatedRows.add(row);
        }
    }

    /**
     * Verify rows.
     */
    public void verifyRows() {
        Iterator<SnmpRowResult> anticipated = m_anticipatedRows.iterator();
        Iterator<SnmpRowResult> received = m_receivedRows.iterator();

        int count = 1;
        for (; anticipated.hasNext();) {
            assertTrue("expected more rows but received none!", received.hasNext());
            SnmpRowResult anticipatedRow = anticipated.next();
            SnmpRowResult receivedRow = received.next();
            assertEquals("Unexpected instance id for row " + count + ": ", anticipatedRow.getInstance(),
                         receivedRow.getInstance());
            count++;
        }

        if (received.hasNext()) {
            StringBuilder buf = new StringBuilder();
            while (received.hasNext()) {
                buf.append("Unexpected Row: ").append(received.next()).append('\n');
            }
            fail(buf.toString());
        }

        m_anticipatedRows.clear();
        m_receivedRows.clear();
    }

    /*
     * propagate maxRepetitions to children/columns
     * handle timeout
     * handle errors
     * work inside an aggregate tracker
     * ensure 'processedRows' are 'freed'
     * properly handle maxVarsPerPdu correctly
     */

    /**
     * Test simple.
     */
    @Test
    public void testSimple() {

        anticipateRows("1");

        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifIndex, "1"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifDescr, "1"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifMtu, "1"));

        verifyRows();

        anticipateRows("2");

        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifIndex, "2"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifDescr, "2"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifMtu, "2"));

        verifyRows();

        for (SnmpObjId m_column : m_columns) {
            m_tableResult.columnFinished(m_column);
        }
        m_tableResult.tableFinished();

        verifyRows();
    }

    /**
     * Test initial value missing for column.
     */
    @Test
    public void testInitialValueMissingForColumn() {

        anticipateRows("1", "2");

        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifIndex, "1"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifMtu, "1"));

        /*
         * no way to tell that a result is not coming for ifDescr so no rows
         * expected
         */
        verifyRowCount(0);

        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifIndex, "2"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifDescr, "2"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifMtu, "2"));

        verifyRows();

        for (SnmpObjId m_column : m_columns) {
            m_tableResult.columnFinished(m_column);
        }
        m_tableResult.tableFinished();

        verifyRows();
    }

    /**
     * Test column incomplete.
     */
    @Test
    public void testColumnIncomplete() {

        anticipateRows("1", "2", "3");

        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifIndex, "1"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifDescr, "1"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifMtu, "1"));

        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifIndex, "2"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifDescr, "2"));
        m_tableResult.columnFinished(SnmpTrackerTest.SnmpTableConstants.ifMtu);

        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifIndex, "3"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifDescr, "3"));

        m_tableResult.columnFinished(SnmpTrackerTest.SnmpTableConstants.ifIndex);
        m_tableResult.columnFinished(SnmpTrackerTest.SnmpTableConstants.ifDescr);

        m_tableResult.tableFinished();

        verifyRows();
    }

    /**
     * Test non int instances.
     */
    @Test
    public void testNonIntInstances() {

        anticipateRows("127.0.0.1");

        /*
         * yes, yes, I know these are not the right places to put them in The
         * Real World ;)
         */
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifIndex, "127.0.0.1"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifDescr, "127.0.0.1"));
        m_tableResult.storeResult(result(SnmpTrackerTest.SnmpTableConstants.ifMtu, "127.0.0.1"));

        m_tableResult.columnFinished(SnmpTrackerTest.SnmpTableConstants.ifIndex);
        m_tableResult.columnFinished(SnmpTrackerTest.SnmpTableConstants.ifDescr);
        m_tableResult.columnFinished(SnmpTrackerTest.SnmpTableConstants.ifMtu);

        m_tableResult.tableFinished();

        verifyRows();

    }
}
