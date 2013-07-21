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

package org.opennms.web.svclayer.support;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.config.surveillanceViews.Columns;
import org.opennms.netmgt.config.surveillanceViews.Rows;
import org.opennms.netmgt.config.surveillanceViews.View;
import org.opennms.netmgt.dao.api.CategoryDao;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.dao.api.SurveillanceViewConfigDao;
import org.opennms.netmgt.model.OnmsCategory;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.test.mock.EasyMockUtils;

/**
 * The Class DefaultSurveillanceServiceTest.
 */
public class DefaultSurveillanceServiceTest {

    /** The m_mock utils. */
    private EasyMockUtils m_mockUtils;

    /** The m_node dao. */
    private NodeDao m_nodeDao;

    /** The m_category dao. */
    private CategoryDao m_categoryDao;

    /** The m_surveillance view config dao. */
    private SurveillanceViewConfigDao m_surveillanceViewConfigDao;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public final void setUp() throws Exception {
        m_mockUtils = new EasyMockUtils();

        m_nodeDao = m_mockUtils.createMock(NodeDao.class);
        m_categoryDao = m_mockUtils.createMock(CategoryDao.class);
        m_surveillanceViewConfigDao = m_mockUtils.createMock(SurveillanceViewConfigDao.class);
    }

    /**
     * Test create surveillance table.
     */
    @Test
    public final void testCreateSurveillanceTable() {

        View view = new View();
        Rows rows = new Rows();

        view.setRows(rows);

        Columns columns = new Columns();

        view.setColumns(columns);

        expect(m_surveillanceViewConfigDao.getView(eq("default"))).andReturn(view).atLeastOnce();

        m_mockUtils.replayAll();

        DefaultSurveillanceService surveillanceSvc = new DefaultSurveillanceService();
        surveillanceSvc.setNodeDao(m_nodeDao);
        surveillanceSvc.setCategoryDao(m_categoryDao);
        surveillanceSvc.setSurveillanceConfigDao(m_surveillanceViewConfigDao);

        surveillanceSvc.createSurveillanceTable();

        m_mockUtils.verifyAll();

    }

    /**
     * Creates the categories.
     *
     * @param catNames
     *            the cat names
     * @return the collection
     */
    public final Collection<OnmsCategory> createCategories(final List<String> catNames) {
        Collection<OnmsCategory> categories = createCategoryNameCollection(catNames);
        return categories;
    }

    /**
     * Creates the category name collection.
     *
     * @param categoryNames
     *            the category names
     * @return the collection
     */
    private Collection<OnmsCategory> createCategoryNameCollection(final List<String> categoryNames) {

        Collection<OnmsCategory> categories = new ArrayList<OnmsCategory>();
        for (String catName : categoryNames) {
            categories.add(m_categoryDao.findByName(catName));
        }
        return categories;
    }

    /**
     * Test url maker.
     */
    @Test
    public final void testUrlMaker() {
        System.err.println(createNodePageUrl("1 of 10"));

    }

    /**
     * Creates the node page url.
     *
     * @param label
     *            the label
     * @return the string
     */
    private String createNodePageUrl(String label) {
        OnmsNode m_foundDownNode = new OnmsNode();
        m_foundDownNode.setId(1);
        if (m_foundDownNode != null) {
            label = "<a href=\"element/node.jsp?node=" + m_foundDownNode.getId() + "\">" + label + "</a>";
        }
        return label;
    }

}
