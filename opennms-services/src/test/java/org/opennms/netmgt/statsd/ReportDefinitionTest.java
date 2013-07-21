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

package org.opennms.netmgt.statsd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.opennms.netmgt.config.statsd.model.PackageReport;
import org.opennms.netmgt.config.statsd.model.StatsdPackage;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.dao.api.ResourceDao;
import org.opennms.netmgt.dao.api.RrdDao;
import org.opennms.netmgt.dao.support.BottomNAttributeStatisticVisitor;
import org.opennms.netmgt.filter.FilterDao;
import org.opennms.netmgt.mock.MockResourceType;
import org.opennms.netmgt.model.AttributeStatisticVisitorWithResults;
import org.opennms.netmgt.model.ExternalValueAttribute;
import org.opennms.netmgt.model.OnmsAttribute;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.RrdGraphAttribute;
import org.opennms.test.ThrowableAnticipator;
import org.opennms.test.mock.EasyMockUtils;

/**
 * The Class ReportDefinitionTest.
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
public class ReportDefinitionTest extends TestCase {

    /** The m_mocks. */
    private EasyMockUtils m_mocks = new EasyMockUtils();

    /** The m_node dao. */
    private NodeDao m_nodeDao = m_mocks.createMock(NodeDao.class);

    /** The m_resource dao. */
    private ResourceDao m_resourceDao = m_mocks.createMock(ResourceDao.class);

    /** The m_rrd dao. */
    private RrdDao m_rrdDao = m_mocks.createMock(RrdDao.class);

    /** The m_filter dao. */
    private FilterDao m_filterDao = m_mocks.createMock(FilterDao.class);

    /* (non-Javadoc)
     * @see junit.framework.TestCase#runTest()
     */
    @Override
    protected void runTest() throws Throwable {
        super.runTest();

        m_mocks.verifyAll();
    }

    /**
     * Test bogus report class.
     *
     * @throws Exception
     *             the exception
     */
    @SuppressWarnings("unchecked")
    public void testBogusReportClass() throws Exception {
        // Not replaying anything, but need to do it before verifyAll() happens
        m_mocks.replayAll();

        ReportDefinition def = new ReportDefinition();

        Class<? extends AttributeStatisticVisitorWithResults> clazz = (Class<? extends AttributeStatisticVisitorWithResults>) Class.forName("java.lang.String");

        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalArgumentException(ThrowableAnticipator.IGNORE_MESSAGE));
        try {
            def.setReportClass(clazz);
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }

        ta.verifyAnticipated();
    }

    /**
     * Test after properties set.
     */
    public void testAfterPropertiesSet() {
        // Not replaying anything, but need to do it before verifyAll() happens
        m_mocks.replayAll();

        createReportDefinition();
    }

    /**
     * Test report walking.
     *
     * @throws Exception
     *             the exception
     */
    public void testReportWalking() throws Exception {
        EasyMock.expect(m_resourceDao.findTopLevelResources()).andReturn(new ArrayList<OnmsResource>(0));

        ReportDefinition def = createReportDefinition();
        def.setResourceAttributeKey("ifSpeed");
        def.setResourceAttributeValueMatch("100000000");
        ReportInstance report = def.createReport(m_nodeDao, m_resourceDao, m_rrdDao, m_filterDao);

        m_mocks.replayAll();

        report.walk();

        assertEquals("results size", 0, report.getResults().size());
    }

    /**
     * Test unfiltered resource attribute filtering with no match.
     *
     * @throws Exception
     *             the exception
     */
    public void testUnfilteredResourceAttributeFilteringWithNoMatch() throws Exception {
        MockResourceType resourceType = new MockResourceType();
        resourceType.setName("interfaceSnmp");
        OnmsAttribute attribute = new RrdGraphAttribute("IfInOctets", "something", "something else");
        OnmsResource resource = new OnmsResource("1", "Node One", resourceType, Collections.singleton(attribute));

        EasyMock.expect(m_resourceDao.findTopLevelResources()).andReturn(Collections.singletonList(resource));

        ReportDefinition def = createReportDefinition();
        def.setResourceAttributeKey("ifSpeed");
        def.setResourceAttributeValueMatch("100000000");
        ReportInstance report = def.createReport(m_nodeDao, m_resourceDao, m_rrdDao, m_filterDao);

        m_mocks.replayAll();

        report.walk();

        assertEquals("results size", 0, report.getResults().size());
    }

    /**
     * Test unfiltered resource attribute filtering with match.
     *
     * @throws Exception
     *             the exception
     */
    public void testUnfilteredResourceAttributeFilteringWithMatch() throws Exception {
        OnmsAttribute rrdAttribute = new RrdGraphAttribute("IfInOctets", "something", "something else");
        ExternalValueAttribute externalValueAttribute = new ExternalValueAttribute("ifSpeed", "100000000");

        Set<OnmsAttribute> attributes = new HashSet<OnmsAttribute>();
        attributes.add(rrdAttribute);
        attributes.add(externalValueAttribute);

        MockResourceType resourceType = new MockResourceType();
        resourceType.setName("interfaceSnmp");
        OnmsResource resource = new OnmsResource("1", "Node One", resourceType, attributes);

        EasyMock.expect(m_resourceDao.findTopLevelResources()).andReturn(Collections.singletonList(resource));

        ReportDefinition def = createReportDefinition();
        def.setResourceAttributeKey(externalValueAttribute.getName());
        def.setResourceAttributeValueMatch(externalValueAttribute.getValue());
        ReportInstance report = def.createReport(m_nodeDao, m_resourceDao, m_rrdDao, m_filterDao);

        EasyMock.expect(m_rrdDao.getPrintValue(rrdAttribute, def.getConsolidationFunction(), report.getStartTime(),
                                               report.getEndTime())).andReturn(1.0);

        m_mocks.replayAll();

        report.walk();

        m_mocks.verifyAll();

        assertEquals("results size", 1, report.getResults().size());

        m_mocks.replayAll();
    }

    /**
     * Test filtered resource attribute filtering with no match.
     *
     * @throws Exception
     *             the exception
     */
    public void testFilteredResourceAttributeFilteringWithNoMatch() throws Exception {
        final OnmsNode node = new OnmsNode();
        node.setId(1);
        node.setLabel("Node One");
        EasyMock.expect(m_nodeDao.load(1)).andReturn(node);

        MockResourceType resourceType = new MockResourceType();
        resourceType.setName("interfaceSnmp");
        OnmsAttribute attribute = new RrdGraphAttribute("IfInOctets", "something", "something else");
        OnmsResource resource = new OnmsResource(node.getId().toString(), node.getLabel(), resourceType,
                                                 Collections.singleton(attribute));

        ReportDefinition def = createReportDefinition();
        def.getReport().getPackage().setFilter("");
        def.setResourceAttributeKey("ifSpeed");
        def.setResourceAttributeValueMatch("100000000");
        ReportInstance report = def.createReport(m_nodeDao, m_resourceDao, m_rrdDao, m_filterDao);

        SortedMap<Integer, String> sortedNodeMap = new TreeMap<Integer, String>();
        sortedNodeMap.put(node.getId(), node.getLabel());
        EasyMock.expect(m_filterDao.getNodeMap("")).andReturn(sortedNodeMap);

        EasyMock.expect(m_resourceDao.getResourceForNode(node)).andReturn(resource);

        m_mocks.replayAll();

        report.walk();

        assertEquals("results size", 0, report.getResults().size());
    }

    /**
     * Test filtered resource attribute filtering with match.
     *
     * @throws Exception
     *             the exception
     */
    public void testFilteredResourceAttributeFilteringWithMatch() throws Exception {
        OnmsAttribute rrdAttribute = new RrdGraphAttribute("IfInOctets", "something", "something else");
        ExternalValueAttribute externalValueAttribute = new ExternalValueAttribute("ifSpeed", "100000000");

        Set<OnmsAttribute> attributes = new HashSet<OnmsAttribute>();
        attributes.add(rrdAttribute);
        attributes.add(externalValueAttribute);

        final OnmsNode node = new OnmsNode();
        node.setId(1);
        node.setLabel("Node One");
        EasyMock.expect(m_nodeDao.load(1)).andReturn(node);

        MockResourceType resourceType = new MockResourceType();
        resourceType.setName("interfaceSnmp");
        OnmsResource resource = new OnmsResource(node.getId().toString(), node.getLabel(), resourceType, attributes);

        ReportDefinition def = createReportDefinition();
        def.getReport().getPackage().setFilter("");
        def.setResourceAttributeKey(externalValueAttribute.getName());
        def.setResourceAttributeValueMatch(externalValueAttribute.getValue());
        ReportInstance report = def.createReport(m_nodeDao, m_resourceDao, m_rrdDao, m_filterDao);

        SortedMap<Integer, String> sortedNodeMap = new TreeMap<Integer, String>();
        sortedNodeMap.put(node.getId(), node.getLabel());
        EasyMock.expect(m_filterDao.getNodeMap("")).andReturn(sortedNodeMap);

        EasyMock.expect(m_resourceDao.getResourceForNode(node)).andReturn(resource);

        EasyMock.expect(m_rrdDao.getPrintValue(rrdAttribute, def.getConsolidationFunction(), report.getStartTime(),
                                               report.getEndTime())).andReturn(1.0);

        m_mocks.replayAll();

        report.walk();

        assertEquals("results size", 1, report.getResults().size());
    }

    /**
     * Creates the report definition.
     *
     * @return the report definition
     */
    private static ReportDefinition createReportDefinition() {
        ReportDefinition def;
        def = new ReportDefinition();
        def.setReport(createPackageReport());
        def.setCount(10);
        def.setConsolidationFunction("AVERAGE");
        def.setRelativeTime(RelativeTime.LASTHOUR);
        def.setResourceTypeMatch("interfaceSnmp");
        def.setAttributeMatch("IfInOctets");
        def.setReportClass(BottomNAttributeStatisticVisitor.class);
        def.afterPropertiesSet();
        return def;
    }

    /**
     * Creates the package report.
     *
     * @return the package report
     */
    private static PackageReport createPackageReport() {
        PackageReport packageReport;
        packageReport = new PackageReport();
        packageReport.setDescription("a package!");
        packageReport.setEnabled(true);
        packageReport.setPackage(new StatsdPackage());
        packageReport.setReport(null);
        packageReport.setRetainInterval(Long.valueOf(86400 * 1000));
        packageReport.setSchedule("hmm");
        return packageReport;
    }
}
