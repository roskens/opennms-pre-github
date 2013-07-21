/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.event.filter;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.dao.DatabasePopulator;
import org.opennms.netmgt.model.OnmsDistPoller;
import org.opennms.netmgt.model.OnmsEvent;
import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.opennms.web.alarm.filter.AlarmIdFilter;
import org.opennms.web.event.Event;
import org.opennms.web.event.WebEventRepository;
import org.opennms.web.filter.Filter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class WebEventRepositoryFilterTest.
 */
@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-dao.xml", "classpath*:/META-INF/opennms/component-dao.xml",
        "classpath*:/META-INF/opennms/component-service.xml", "classpath:/daoWebRepositoryTestContext.xml",
        "classpath:/jdbcWebRepositoryTestContext.xml",
        "classpath:/META-INF/opennms/applicationContext-minimal-conf.xml" })
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase
public class WebEventRepositoryFilterTest implements InitializingBean {

    /** The m_db populator. */
    @Autowired
    DatabasePopulator m_dbPopulator;

    /** The m_dao event repo. */
    @Autowired
    @Qualifier("dao")
    WebEventRepository m_daoEventRepo;

    /** The m_jdbc event repo. */
    @Autowired
    @Qualifier("jdbc")
    WebEventRepository m_jdbcEventRepo;

    /** The m_app context. */
    @Autowired
    ApplicationContext m_appContext;

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public final void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    /**
     * Sets the up.
     */
    @Before
    public final void setUp() {
        m_dbPopulator.populateDatabase();

        OnmsEvent event = new OnmsEvent();
        event.setDistPoller(getDistPoller("localhost", "127.0.0.1"));
        event.setAlarm(m_dbPopulator.getAlarmDao().get(1));
        event.setNode(m_dbPopulator.getNode1());
        event.setEventUei("uei.opennms.org/test2");
        event.setEventTime(new Date());
        event.setEventSource("test");
        event.setEventCreateTime(new Date());
        event.setEventSeverity(OnmsSeverity.CLEARED.getId());
        event.setEventLog("Y");
        event.setEventDisplay("Y");
        event.setIfIndex(11);
        event.setIpAddr(InetAddressUtils.getInetAddress("192.168.1.1"));
        event.setEventLogMsg("This is a test log message");
        event.setEventDescr("This is a test event");
        event.setServiceType(m_dbPopulator.getServiceTypeDao().get(1));
        m_dbPopulator.getEventDao().save(event);
        m_dbPopulator.getEventDao().flush();
    }

    /**
     * Gets the dist poller.
     *
     * @param localhost
     *            the localhost
     * @param localhostIp
     *            the localhost ip
     * @return the dist poller
     */
    private OnmsDistPoller getDistPoller(final String localhost, final String localhostIp) {
        OnmsDistPoller distPoller = m_dbPopulator.getDistPollerDao().get(localhost);
        if (distPoller == null) {
            distPoller = new OnmsDistPoller(localhost, localhostIp);
            m_dbPopulator.getDistPollerDao().save(distPoller);
            m_dbPopulator.getDistPollerDao().flush();
        }
        return distPoller;
    }

    /**
     * Test event id filter.
     */
    @Test
    @JUnitTemporaryDatabase
    // Relies on specific IDs so we need a fresh database
    public final void testEventIdFilter() {
        EventIdFilter filter = new EventIdFilter(1);
        assert1Result(filter);
    }

    /**
     * Test event id list filter.
     */
    @Test
    @JUnitTemporaryDatabase
    // Relies on specific IDs so we need a fresh database
    public final void testEventIdListFilter() {
        int[] ids = { 1 };
        EventIdListFilter filter = new EventIdListFilter(ids);
        assert1Result(filter);

    }

    /**
     * Test acknowledge by filter.
     */
    @Test
    @JUnitTemporaryDatabase
    // Relies on specific IDs so we need a fresh database
    public final void testAcknowledgeByFilter() {
        AcknowledgedByFilter filter = new AcknowledgedByFilter("TestUser");
        EventCriteria criteria = new EventCriteria(filter);

        Event[] events = m_daoEventRepo.getMatchingEvents(criteria);
        assertEquals(0, events.length);

        m_daoEventRepo.acknowledgeMatchingEvents("TestUser", new Date(), new EventCriteria(new EventIdFilter(1)));

        events = m_daoEventRepo.getMatchingEvents(criteria);
        assertEquals(1, events.length);
        assertEquals("TestUser", events[0].getAcknowledgeUser());

        m_daoEventRepo.unacknowledgeAll();

        events = m_jdbcEventRepo.getMatchingEvents(criteria);
        assertEquals(0, events.length);

        m_daoEventRepo.acknowledgeAll("TestUser", new Date());
        events = m_jdbcEventRepo.getMatchingEvents(criteria);
        assertEquals(2, events.length);

    }

    /**
     * Test after date filter.
     */
    @Test
    @Transactional
    public final void testAfterDateFilter() {
        AfterDateFilter filter = new AfterDateFilter(yesterday());

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(2, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(2, events.length);
    }

    /**
     * Test alarm id filter.
     */
    @Test
    @JUnitTemporaryDatabase
    // Relies on specific IDs so we need a fresh database
    public final void testAlarmIdFilter() {
        AlarmIdFilter filter = new AlarmIdFilter(1);

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);
    }

    /**
     * Test before date filter.
     */
    @Test
    @Transactional
    public final void testBeforeDateFilter() {
        BeforeDateFilter filter = new BeforeDateFilter(new Date());

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(2, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(2, events.length);
    }

    /**
     * Test description substring filter test.
     */
    @Test
    @Transactional
    public final void testDescriptionSubstringFilterTest() {
        DescriptionSubstringFilter filter = new DescriptionSubstringFilter("test event");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);
        assertEquals("This is a test event", events[0].getDescription());

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);
        assertEquals("This is a test event", events[0].getDescription());
    }

    /**
     * Test exact uei filter.
     */
    @Test
    @Transactional
    public final void testExactUEIFilter() {
        ExactUEIFilter filter = new ExactUEIFilter("uei.opennms.org/test2");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);
        assertEquals("uei.opennms.org/test2", events[0].getUei());

        events = getMatchingJdbcEvents(new ExactUEIFilter("uei.opennms.org/test"));
        assertEquals(1, events.length);
        assertEquals("uei.opennms.org/test", events[0].getUei());
    }

    /**
     * Test if index filter.
     */
    @Test
    @Transactional
    public final void testIfIndexFilter() {
        IfIndexFilter filter = new IfIndexFilter(11);

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingDaoEvents(new IfIndexFilter(1));
        assertEquals(0, events.length);

        events = getMatchingJdbcEvents(new IfIndexFilter(1));
        assertEquals(0, events.length);
    }

    /**
     * Test interface filter.
     */
    @Test
    @Transactional
    public final void testInterfaceFilter() {
        InterfaceFilter filter = new InterfaceFilter("192.168.1.1");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);
        assertEquals("192.168.1.1", events[0].getIpAddress());

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);
        assertEquals("192.168.1.1", events[0].getIpAddress());

    }

    /**
     * Test ip addr like filter.
     */
    @Test
    @Transactional
    public final void testIpAddrLikeFilter() {
        IPAddrLikeFilter filter = new IPAddrLikeFilter("192.168.*.*");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);

        filter = new IPAddrLikeFilter("193.168");
        events = getMatchingDaoEvents(filter);
        assertEquals(0, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(0, events.length);
    }

    /**
     * Test log message matches any.
     */
    @Test
    @Transactional
    public final void testLogMessageMatchesAny() {
        LogMessageMatchesAnyFilter filter = new LogMessageMatchesAnyFilter("This is a");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = null;

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);
    }

    /**
     * Test log message substring filter.
     */
    @Test
    @Transactional
    public final void testLogMessageSubstringFilter() {
        LogMessageSubstringFilter filter = new LogMessageSubstringFilter("is a test");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);
    }

    /**
     * Test negative acknowledged by filter.
     */
    @Test
    @Transactional
    public final void testNegativeAcknowledgedByFilter() {
        NegativeAcknowledgedByFilter filter = new NegativeAcknowledgedByFilter("TestUser");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(2, events.length);

        events = null;

        events = getMatchingJdbcEvents(filter);
        assertEquals(2, events.length);

        m_daoEventRepo.acknowledgeAll("TestUser", new Date());

        events = getMatchingDaoEvents(filter);
        assertEquals(0, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(0, events.length);
    }

    /**
     * Test negative exact uei filter.
     */
    @Test
    @Transactional
    public final void testNegativeExactUeiFilter() {
        NegativeExactUEIFilter filter = new NegativeExactUEIFilter("uei.opennms.org/test2");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);

        filter = new NegativeExactUEIFilter("uei.opennms.org/nontest");

        events = getMatchingDaoEvents(filter);
        assertEquals(2, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(2, events.length);
    }

    /**
     * Test negative interface filter.
     */
    @Test
    @Transactional
    public final void testNegativeInterfaceFilter() {
        NegativeInterfaceFilter filter = new NegativeInterfaceFilter("192.168.1.1");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);

        filter = new NegativeInterfaceFilter("27.0.0.1");

        events = getMatchingDaoEvents(filter);
        assertEquals(2, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(2, events.length);
    }

    /**
     * Test negative node filter.
     */
    @Test
    @Transactional
    public final void testNegativeNodeFilter() {
        NegativeNodeFilter filter = new NegativeNodeFilter(m_dbPopulator.getNode2().getId(), m_appContext);

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(2, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(2, events.length);

        filter = new NegativeNodeFilter(m_dbPopulator.getNode1().getId(), m_appContext);

        events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);

        assertEquals("node is not node1", filter.getTextDescription());
    }

    /**
     * Test negative partial uei filter.
     */
    @Test
    @Transactional
    public final void testNegativePartialUeiFilter() {
        NegativePartialUEIFilter filter = new NegativePartialUEIFilter("uei.opennms.org");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(0, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(0, events.length);

        filter = new NegativePartialUEIFilter("puei.org.opennms");

        events = getMatchingDaoEvents(filter);
        assertEquals(2, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(2, events.length);
    }

    /**
     * Test negative service filter.
     */
    @Test
    @JUnitTemporaryDatabase
    // Relies on specific IDs so we need a fresh database
    public final void testNegativeServiceFilter() {
        NegativeServiceFilter filter = new NegativeServiceFilter(1, m_appContext);

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);

        filter = new NegativeServiceFilter(2, m_appContext);

        events = getMatchingDaoEvents(filter);
        assertEquals(2, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(2, events.length);
    }

    /**
     * Test negative severity filter.
     */
    @Test
    @Transactional
    public final void testNegativeSeverityFilter() {
        NegativeSeverityFilter filter = new NegativeSeverityFilter(OnmsSeverity.CRITICAL.getId());

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(2, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(2, events.length);

        filter = new NegativeSeverityFilter(OnmsSeverity.CLEARED.getId());

        events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);
    }

    /**
     * Test node filter.
     */
    @Test
    @JUnitTemporaryDatabase
    // Relies on specific IDs so we need a fresh database
    public final void testNodeFilter() {
        NodeFilter filter = new NodeFilter(1, m_appContext);

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);

        filter = new NodeFilter(2, m_appContext);

        events = getMatchingDaoEvents(filter);
        assertEquals(0, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(0, events.length);

        assertEquals("node=node2", filter.getTextDescription());
    }

    /**
     * Test node name like filter.
     */
    @Test
    @Transactional
    public final void testNodeNameLikeFilter() {
        NodeNameLikeFilter filter = new NodeNameLikeFilter("node1");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);

        filter = new NodeNameLikeFilter("testNode");

        events = getMatchingDaoEvents(filter);
        assertEquals(0, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(0, events.length);

    }

    /**
     * Test partial uei filter.
     */
    @Test
    @Transactional
    public final void testPartialUeiFilter() {
        PartialUEIFilter filter = new PartialUEIFilter("uei.opennms.org/t");

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(2, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(2, events.length);

        filter = new PartialUEIFilter("unknown");

        events = getMatchingDaoEvents(filter);
        assertEquals(0, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(0, events.length);
    }

    /**
     * Test service filter.
     */
    @Test
    @JUnitTemporaryDatabase
    // Relies on specific IDs so we need a fresh database
    public final void testServiceFilter() {
        ServiceFilter filter = new ServiceFilter(2, m_appContext);

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(0, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(0, events.length);

        filter = new ServiceFilter(1, m_appContext);

        events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);
    }

    /**
     * Test severity filter.
     */
    @Test
    @Transactional
    public final void testSeverityFilter() {
        SeverityFilter filter = new SeverityFilter(OnmsSeverity.CLEARED.getId());

        Event[] events = getMatchingDaoEvents(filter);
        assertEquals(1, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(1, events.length);

        filter = new SeverityFilter(OnmsSeverity.MAJOR.getId());

        events = getMatchingDaoEvents(filter);
        assertEquals(0, events.length);

        events = getMatchingJdbcEvents(filter);
        assertEquals(0, events.length);
    }

    /**
     * Gets the criteria.
     *
     * @param filters
     *            the filters
     * @return the criteria
     */
    private static EventCriteria getCriteria(final Filter... filters) {
        return new EventCriteria(filters);
    }

    /**
     * Gets the matching dao events.
     *
     * @param filters
     *            the filters
     * @return the matching dao events
     */
    private Event[] getMatchingDaoEvents(final Filter... filters) {
        return m_daoEventRepo.getMatchingEvents(getCriteria(filters));
    }

    /**
     * Gets the matching jdbc events.
     *
     * @param filters
     *            the filters
     * @return the matching jdbc events
     */
    private Event[] getMatchingJdbcEvents(final Filter... filters) {
        return m_jdbcEventRepo.getMatchingEvents(getCriteria(filters));
    }

    /**
     * Assert1 result.
     *
     * @param filter
     *            the filter
     */
    private void assert1Result(final Filter filter) {
        EventCriteria criteria = new EventCriteria(filter);

        Event[] events = m_jdbcEventRepo.getMatchingEvents(criteria);
        assertEquals(1, events.length);

        events = m_daoEventRepo.getMatchingEvents(criteria);
        assertEquals(1, events.length);
    }

    /**
     * Yesterday.
     *
     * @return the date
     */
    private static Date yesterday() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}
