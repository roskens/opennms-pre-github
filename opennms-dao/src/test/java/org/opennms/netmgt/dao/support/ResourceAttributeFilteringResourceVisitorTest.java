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

package org.opennms.netmgt.dao.support;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.opennms.netmgt.mock.MockResourceType;
import org.opennms.netmgt.model.ExternalValueAttribute;
import org.opennms.netmgt.model.OnmsAttribute;
import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.ResourceVisitor;
import org.opennms.netmgt.model.StringPropertyAttribute;
import org.opennms.test.ThrowableAnticipator;
import org.opennms.test.mock.EasyMockUtils;

/**
 * The Class ResourceAttributeFilteringResourceVisitorTest.
 *
 * @author <a href="dj@opennms.org">DJ Gregor</a>
 */
public class ResourceAttributeFilteringResourceVisitorTest extends TestCase {

    /** The m_mocks. */
    private EasyMockUtils m_mocks = new EasyMockUtils();

    /** The m_delegated visitor. */
    private ResourceVisitor m_delegatedVisitor = m_mocks.createMock(ResourceVisitor.class);

    /**
     * Test after properties set.
     *
     * @throws Exception
     *             the exception
     */
    public void testAfterPropertiesSet() throws Exception {
        ResourceAttributeFilteringResourceVisitor filteringVisitor = new ResourceAttributeFilteringResourceVisitor();
        filteringVisitor.setDelegatedVisitor(m_delegatedVisitor);
        filteringVisitor.setResourceAttributeKey("ifSpeed");
        filteringVisitor.setResourceAttributeValueMatch("100000000");
        filteringVisitor.afterPropertiesSet();
    }

    /**
     * Test after properties set no delegated visitor.
     *
     * @throws Exception
     *             the exception
     */
    public void testAfterPropertiesSetNoDelegatedVisitor() throws Exception {
        ResourceAttributeFilteringResourceVisitor filteringVisitor = new ResourceAttributeFilteringResourceVisitor();

        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalStateException("property delegatedVisitor must be set to a non-null value"));

        filteringVisitor.setDelegatedVisitor(null);
        filteringVisitor.setResourceAttributeKey("ifSpeed");

        try {
            filteringVisitor.afterPropertiesSet();
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        ta.verifyAnticipated();
    }

    /**
     * Test after properties set no resource type match.
     *
     * @throws Exception
     *             the exception
     */
    public void testAfterPropertiesSetNoResourceTypeMatch() throws Exception {
        ResourceAttributeFilteringResourceVisitor filteringVisitor = new ResourceAttributeFilteringResourceVisitor();

        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalStateException("property resourceAttributeKey must be set to a non-null value"));

        filteringVisitor.setDelegatedVisitor(m_delegatedVisitor);
        filteringVisitor.setResourceAttributeValueMatch("1000000000");

        try {
            filteringVisitor.afterPropertiesSet();
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        ta.verifyAnticipated();
    }

    /**
     * Test after properties set no resource attribute value match.
     *
     * @throws Exception
     *             the exception
     */
    public void testAfterPropertiesSetNoResourceAttributeValueMatch() throws Exception {
        ResourceAttributeFilteringResourceVisitor filteringVisitor = new ResourceAttributeFilteringResourceVisitor();

        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalStateException("property resourceAttributeValueMatch must be set to a non-null value"));

        filteringVisitor.setDelegatedVisitor(m_delegatedVisitor);
        filteringVisitor.setResourceAttributeKey("ifSpeed");

        try {
            filteringVisitor.afterPropertiesSet();
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        ta.verifyAnticipated();
    }

    /**
     * Test visit with external value match.
     *
     * @throws Exception
     *             the exception
     */
    public void testVisitWithExternalValueMatch() throws Exception {
        ResourceAttributeFilteringResourceVisitor filteringVisitor = new ResourceAttributeFilteringResourceVisitor();
        filteringVisitor.setDelegatedVisitor(m_delegatedVisitor);
        filteringVisitor.setResourceAttributeKey("ifSpeed");
        filteringVisitor.setResourceAttributeValueMatch("1000000000");
        filteringVisitor.afterPropertiesSet();

        Set<OnmsAttribute> attributes = new HashSet<OnmsAttribute>(1);
        attributes.add(new ExternalValueAttribute("ifSpeed", "1000000000"));

        MockResourceType resourceType = new MockResourceType();
        resourceType.setName("interfaceSnmp");
        OnmsResource resource = new OnmsResource("1", "Node One", resourceType, attributes);

        // Expect
        m_delegatedVisitor.visit(resource);

        m_mocks.replayAll();
        filteringVisitor.visit(resource);
        m_mocks.verifyAll();
    }

    /**
     * Test visit with string property match.
     *
     * @throws Exception
     *             the exception
     */
    public void testVisitWithStringPropertyMatch() throws Exception {
        ResourceAttributeFilteringResourceVisitor filteringVisitor = new ResourceAttributeFilteringResourceVisitor();
        filteringVisitor.setDelegatedVisitor(m_delegatedVisitor);
        filteringVisitor.setResourceAttributeKey("ifSpeed");
        filteringVisitor.setResourceAttributeValueMatch("1000000000");
        filteringVisitor.afterPropertiesSet();

        Set<OnmsAttribute> attributes = new HashSet<OnmsAttribute>(1);
        attributes.add(new StringPropertyAttribute("ifSpeed", "1000000000"));

        MockResourceType resourceType = new MockResourceType();
        resourceType.setName("interfaceSnmp");
        OnmsResource resource = new OnmsResource("1", "Node One", resourceType, attributes);

        // Expect
        m_delegatedVisitor.visit(resource);

        m_mocks.replayAll();
        filteringVisitor.visit(resource);
        m_mocks.verifyAll();
    }

    /**
     * Test visit without match.
     *
     * @throws Exception
     *             the exception
     */
    public void testVisitWithoutMatch() throws Exception {
        ResourceAttributeFilteringResourceVisitor filteringVisitor = new ResourceAttributeFilteringResourceVisitor();
        filteringVisitor.setDelegatedVisitor(m_delegatedVisitor);
        filteringVisitor.setResourceAttributeKey("ifSpeed");
        filteringVisitor.setResourceAttributeValueMatch("1000000000");
        filteringVisitor.afterPropertiesSet();

        MockResourceType resourceType = new MockResourceType();
        resourceType.setName("something other than interfaceSnmp");
        OnmsResource resource = new OnmsResource("1", "Node One", resourceType, new HashSet<OnmsAttribute>(0));

        m_mocks.replayAll();
        filteringVisitor.visit(resource);
        m_mocks.verifyAll();
    }
}
