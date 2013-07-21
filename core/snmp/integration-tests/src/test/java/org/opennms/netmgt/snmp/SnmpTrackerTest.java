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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.snmp.annotations.JUnitSnmpAgent;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.config.SnmpPeerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * The Class SnmpTrackerTest.
 */
@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-proxy-snmp.xml" })
@JUnitSnmpAgent(host = "172.20.1.205", resource = "classpath:snmpTestData1.properties")
public class SnmpTrackerTest implements InitializingBean {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(SnmpTrackerTest.class);

    /** The m_snmp peer factory. */
    @Autowired
    private SnmpPeerFactory m_snmpPeerFactory;

    /**
     * The Class SnmpTableConstants.
     */
    public static final class SnmpTableConstants {

        /** The Constant ifTable. */
        static final SnmpObjId ifTable = SnmpObjId.get(".1.3.6.1.2.1.2.2.1");

        /** The Constant ifIndex. */
        static final SnmpObjId ifIndex = SnmpObjId.get(ifTable, "1");

        /** The Constant ifDescr. */
        static final SnmpObjId ifDescr = SnmpObjId.get(ifTable, "2");

        /** The Constant ifType. */
        static final SnmpObjId ifType = SnmpObjId.get(ifTable, "3");

        /** The Constant ifMtu. */
        static final SnmpObjId ifMtu = SnmpObjId.get(ifTable, "4");

        /** The Constant ifSpeed. */
        static final SnmpObjId ifSpeed = SnmpObjId.get(ifTable, "5");

        /** The Constant ifPhysAddress. */
        static final SnmpObjId ifPhysAddress = SnmpObjId.get(ifTable, "6");

        /** The Constant ifAdminStatus. */
        static final SnmpObjId ifAdminStatus = SnmpObjId.get(ifTable, "7");

        /** The Constant ifOperStatus. */
        static final SnmpObjId ifOperStatus = SnmpObjId.get(ifTable, "8");

        /** The Constant ifLastChange. */
        static final SnmpObjId ifLastChange = SnmpObjId.get(ifTable, "9");

        /** The Constant ifInOctets. */
        static final SnmpObjId ifInOctets = SnmpObjId.get(ifTable, "10");

        /** The Constant ifInUcastPkts. */
        static final SnmpObjId ifInUcastPkts = SnmpObjId.get(ifTable, "11");

        /** The Constant ifInNUcastPkts. */
        static final SnmpObjId ifInNUcastPkts = SnmpObjId.get(ifTable, "12");

        /** The Constant ifInDiscards. */
        static final SnmpObjId ifInDiscards = SnmpObjId.get(ifTable, "13");

        /** The Constant ifInErrors. */
        static final SnmpObjId ifInErrors = SnmpObjId.get(ifTable, "14");

        /** The Constant ifUnknownProtos. */
        static final SnmpObjId ifUnknownProtos = SnmpObjId.get(ifTable, "15");

        /** The Constant ifOutOctets. */
        static final SnmpObjId ifOutOctets = SnmpObjId.get(ifTable, "16");

        /** The Constant ifOutUcastPkts. */
        static final SnmpObjId ifOutUcastPkts = SnmpObjId.get(ifTable, "17");

        /** The Constant ifOutNUcastPkts. */
        static final SnmpObjId ifOutNUcastPkts = SnmpObjId.get(ifTable, "18");

        /** The Constant ifOutDiscards. */
        static final SnmpObjId ifOutDiscards = SnmpObjId.get(ifTable, "19");

        /** The Constant ifOutErrors. */
        static final SnmpObjId ifOutErrors = SnmpObjId.get(ifTable, "20");

        /** The Constant ifOutQLen. */
        static final SnmpObjId ifOutQLen = SnmpObjId.get(ifTable, "21");

        /** The Constant ifSpecific. */
        static final SnmpObjId ifSpecific = SnmpObjId.get(ifTable, "22");
    }

    /**
     * The Class CountingColumnTracker.
     */
    private static class CountingColumnTracker extends ColumnTracker {

        /** The m_count. */
        private long m_count = 0;

        /**
         * Instantiates a new counting column tracker.
         *
         * @param base
         *            the base
         */
        public CountingColumnTracker(final SnmpObjId base) {
            super(base);
        }

        /**
         * Instantiates a new counting column tracker.
         *
         * @param base
         *            the base
         * @param maxRepetitions
         *            the max repetitions
         */
        public CountingColumnTracker(final SnmpObjId base, final int maxRepetitions) {
            super(base, maxRepetitions);
        }

        /**
         * Gets the count.
         *
         * @return the count
         */
        public long getCount() {
            return m_count;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.CollectionTracker#storeResult(org.opennms.netmgt.snmp.SnmpResult)
         */
        @Override
        protected void storeResult(final SnmpResult res) {
            LOG.debug("storing result: {}", res);
            m_count++;
        }

    }

    /**
     * The Class ResultTable.
     */
    private static final class ResultTable {

        /** The m_rows added. */
        private int m_rowsAdded = 0;

        /** The m_results. */
        private Map<SnmpInstId, SnmpRowResult> m_results = new HashMap<SnmpInstId, SnmpRowResult>();

        /**
         * Gets the result.
         *
         * @param base
         *            the base
         * @param inst
         *            the inst
         * @return the result
         */
        SnmpValue getResult(final SnmpObjId base, final SnmpInstId inst) {
            final SnmpRowResult row = m_results.get(inst);
            if (row == null) {
                return null;
            }
            return row.getValue(base);
        }

        /**
         * Gets the result.
         *
         * @param base
         *            the base
         * @param inst
         *            the inst
         * @return the result
         */
        SnmpValue getResult(final SnmpObjId base, final String inst) {
            return getResult(base, new SnmpInstId(inst));
        }

        /**
         * Gets the rows added.
         *
         * @return the rows added
         */
        int getRowsAdded() {
            return m_rowsAdded;
        }

        /**
         * Adds the snmp row result.
         *
         * @param row
         *            the row
         */
        void addSnmpRowResult(final SnmpRowResult row) {
            m_rowsAdded++;
            m_results.put(row.getInstance(), row);
        }

        /**
         * Gets the row count.
         *
         * @return the row count
         */
        public int getRowCount() {
            return m_results.size();
        }

        /**
         * Gets the column count.
         *
         * @return the column count
         */
        public int getColumnCount() {
            int maxColumns = Integer.MIN_VALUE;
            for (final SnmpRowResult row : m_results.values()) {
                maxColumns = Math.max(maxColumns, row.getColumnCount());
            }
            return maxColumns;
        }

    }

    /**
     * The Class TestRowCallback.
     */
    private static class TestRowCallback implements RowCallback {

        /** The m_responses. */
        private final List<SnmpRowResult> m_responses = new ArrayList<SnmpRowResult>();

        /** The m_results. */
        private final ResultTable m_results = new ResultTable();

        /* (non-Javadoc)
         * @see org.opennms.netmgt.snmp.RowCallback#rowCompleted(org.opennms.netmgt.snmp.SnmpRowResult)
         */
        @Override
        public void rowCompleted(final SnmpRowResult row) {
            m_responses.add(row);
            m_results.addSnmpRowResult(row);
        }

        /**
         * Gets the responses.
         *
         * @return the responses
         */
        public List<SnmpRowResult> getResponses() {
            return m_responses;
        }

        /**
         * Gets the results.
         *
         * @return the results
         */
        public ResultTable getResults() {
            return m_results;
        }
    }

    /**
     * Walk.
     *
     * @param c
     *            the c
     * @param maxVarsPerPdu
     *            the max vars per pdu
     * @param maxRepetitions
     *            the max repetitions
     * @throws Exception
     *             the exception
     */
    private void walk(final CollectionTracker c, final int maxVarsPerPdu, final int maxRepetitions) throws Exception {
        final SnmpAgentConfig config = m_snmpPeerFactory.getAgentConfig(InetAddressUtils.addr("172.20.1.205"));
        config.setVersion(SnmpAgentConfig.VERSION2C);
        config.setMaxVarsPerPdu(maxVarsPerPdu);
        config.setMaxRepetitions(maxRepetitions);
        final SnmpWalker walker = SnmpUtils.createWalker(config, "test", c);
        assertNotNull(walker);
        walker.start();
        walker.waitFor();
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        MockLogAppender.setupLogging();
        SnmpPeerFactory.setInstance(m_snmpPeerFactory);
    }

    /**
     * Test column tracker.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testColumnTracker() throws Exception {
        final CountingColumnTracker ct = new CountingColumnTracker(SnmpObjId.get(".1.3.6.1.2.1.2.2.1.1"));
        walk(ct, 10, 3);
        assertEquals("number of columns returned must match test data", Long.valueOf(6).longValue(), ct.getCount());
    }

    /**
     * Test table tracker with full table.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testTableTrackerWithFullTable() throws Exception {
        final TestRowCallback rc = new TestRowCallback();
        final TableTracker tt = new TableTracker(rc, SnmpTableConstants.ifIndex, SnmpTableConstants.ifDescr,
                                                 SnmpTableConstants.ifSpeed);

        walk(tt, 3, 10);

        final ResultTable results = rc.getResults();
        assertTrue("tracker must be finished", tt.isFinished());
        assertEquals("number of rows added must match test data", 6, results.getRowsAdded());
        assertEquals("number of rows must match test data", 6, results.getRowCount());
        assertEquals("number of columns must match test data", 3, results.getColumnCount());
        assertEquals("ifIndex.5 must be 5", 5, results.getResult(SnmpTableConstants.ifIndex, "5").toInt());
        assertEquals("ifName.2 must be gif0", "gif0", results.getResult(SnmpTableConstants.ifDescr, "2").toString());
        assertEquals("ifSpeed.3 must be 0", 0, results.getResult(SnmpTableConstants.ifSpeed, "3").toLong());
        assertEquals("ifSpeed.4 must be 10000000", 10000000,
                     results.getResult(SnmpTableConstants.ifSpeed, "4").toLong());

    }

    /**
     * Test incomplete table data.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    @JUnitSnmpAgent(host = "172.20.1.205", resource = "classpath:snmpTestDataIncompleteTable.properties")
    public void testIncompleteTableData() throws Exception {
        final TestRowCallback rc = new TestRowCallback();
        final TableTracker tt = new TableTracker(rc, SnmpTableConstants.ifIndex, SnmpTableConstants.ifDescr,
                                                 SnmpTableConstants.ifMtu, SnmpTableConstants.ifLastChange,
                                                 SnmpTableConstants.ifInUcastPkts, SnmpTableConstants.ifInErrors,
                                                 SnmpTableConstants.ifOutUcastPkts, SnmpTableConstants.ifOutNUcastPkts,
                                                 SnmpTableConstants.ifOutErrors);

        walk(tt, 4, 3);

        printResponses(rc);
        final ResultTable results = rc.getResults();
        assertTrue("tracker must be finished", tt.isFinished());
        assertEquals("number of rows added must match test data", 6, results.getRowsAdded());
        assertEquals("number of rows must match test data", 6, results.getRowCount());
        assertEquals("number of columns must match test data", 9, results.getColumnCount());
        assertNull("ifMtu.4 should be null", results.getResult(SnmpTableConstants.ifMtu, "4"));
        assertEquals("ifDescr.5 should be en1", "en1", results.getResult(SnmpTableConstants.ifDescr, "5").toString());
        assertEquals("ifMtu.6 should be 4078", 4078, results.getResult(SnmpTableConstants.ifMtu, "6").toInt());
    }

    /**
     * Test aggregate table.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    @Ignore("Hmm, what *should* this do?  When using a callback, we don't pass storeResult() up-stream...")
    public void testAggregateTable() throws Exception {
        final TestRowCallback rc = new TestRowCallback();
        final TableTracker[] tt = new TableTracker[2];
        tt[0] = new TableTracker(rc, SnmpTableConstants.ifIndex, SnmpTableConstants.ifDescr);
        tt[1] = new TableTracker(rc, SnmpTableConstants.ifMtu, SnmpTableConstants.ifLastChange);
        final AggregateTracker at = new AggregateTracker(tt);

        walk(at, 4, 10);

        printResponses(rc);
    }

    /**
     * Prints the responses.
     *
     * @param rc
     *            the rc
     */
    private void printResponses(final TestRowCallback rc) {
        final List<SnmpRowResult> responses = rc.getResponses();
        for (int i = 0; i < responses.size(); i++) {
            final SnmpRowResult row = responses.get(i);
            LOG.debug("{}: instance={}", i, row.getInstance());
            for (final SnmpResult res : row.getResults()) {
                LOG.debug("    {}={}", res.getBase(), res.getValue());
            }
        }
    }

}
