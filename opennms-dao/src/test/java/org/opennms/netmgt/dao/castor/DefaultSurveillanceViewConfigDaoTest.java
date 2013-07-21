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

package org.opennms.netmgt.dao.castor;

import java.io.IOException;
import java.util.Map;

import junit.framework.TestCase;

import org.opennms.core.test.ConfigurationTestUtils;
import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.config.surveillanceViews.Columns;
import org.opennms.netmgt.config.surveillanceViews.Rows;
import org.opennms.netmgt.config.surveillanceViews.View;
import org.opennms.netmgt.config.surveillanceViews.Views;
import org.opennms.test.mock.MockUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

/**
 * The Class DefaultSurveillanceViewConfigDaoTest.
 */
public class DefaultSurveillanceViewConfigDaoTest extends TestCase {

    /** The Constant CONFIG_WITH_VIEWS_RESOURCE. */
    private static final String CONFIG_WITH_VIEWS_RESOURCE = "/surveillance-views.testdata.xml";

    /** The Constant CONFIG_NO_VIEWS_RESOURCE. */
    private static final String CONFIG_NO_VIEWS_RESOURCE = "/surveillance-views.testdata.noviews.xml";

    /** The m_dao. */
    private DefaultSurveillanceViewConfigDao m_dao;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockUtil.println("------------ Begin Test " + getName() + " --------------------------");
        MockLogAppender.setupLogging();

        createDaoWithResource(CONFIG_WITH_VIEWS_RESOURCE);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#runTest()
     */
    @Override
    public void runTest() throws Throwable {
        super.runTest();
        MockLogAppender.assertNoWarningsOrGreater();
        MockUtil.println("------------ End Test " + getName() + " --------------------------");
    }

    /**
     * Test nothing.
     */
    public void testNothing() {
        // test that setUp() / tearDown() works
    }

    /**
     * Test default view.
     */
    public void testDefaultView() {
        View view = m_dao.getDefaultView();
        assertNotNull("default view should not be null", view);
        assertEquals("default view name", "default", view.getName());

        Columns columns = view.getColumns();
        assertNotNull("default view columns should not be null", columns);
        assertEquals("default view column count", 3, columns.getColumnDefCount());

        Rows rows = view.getRows();
        assertNotNull("default view rows should not be null", rows);
        assertEquals("default view row count", 3, rows.getRowDefCount());
    }

    /**
     * Test view by name.
     */
    public void testViewByName() {
        View view = m_dao.getView("default");
        assertNotNull("default view should not be null", view);
        assertEquals("default view name", "default", view.getName());

        Columns columns = view.getColumns();
        assertNotNull("default view columns should not be null", columns);
        assertEquals("default view column count", 3, columns.getColumnDefCount());

        Rows rows = view.getRows();
        assertNotNull("default view rows should not be null", rows);
        assertEquals("default view row count", 3, rows.getRowDefCount());
    }

    /**
     * Test get views.
     */
    public void testGetViews() {
        Views views = m_dao.getViews();
        assertNotNull("views should not be null", views);
        assertEquals("view count", 1, views.getViewCount());

        View view = views.getView(0);
        assertNotNull("first view should not be null", view);
        assertEquals("first view name", "default", view.getName());

        Columns columns = view.getColumns();
        assertNotNull("first view columns should not be null", columns);
        assertEquals("first view column count", 3, columns.getColumnDefCount());

        Rows rows = view.getRows();
        assertNotNull("first view rows should not be null", rows);
        assertEquals("first view row count", 3, rows.getRowDefCount());
    }

    /**
     * Test get view map.
     */
    public void testGetViewMap() {
        Map<String, View> viewMap = m_dao.getViewMap();
        assertEquals("view count", 1, viewMap.size());

        assertNull("shouldn't have 'bogus' view", viewMap.get("bogus"));

        View view = viewMap.get("default");
        assertNotNull("should have 'default' view", view);

        assertNotNull("first view should not be null", view);
        assertEquals("first view name", "default", view.getName());

        Columns columns = view.getColumns();
        assertNotNull("first view columns should not be null", columns);
        assertEquals("first view column count", 3, columns.getColumnDefCount());

        Rows rows = view.getRows();
        assertNotNull("first view rows should not be null", rows);
        assertEquals("first view row count", 3, rows.getRowDefCount());
    }

    /**
     * Test init no views.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void testInitNoViews() throws IOException {
        createDaoWithResource(CONFIG_NO_VIEWS_RESOURCE);
    }

    /**
     * Test get default view no views.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void testGetDefaultViewNoViews() throws IOException {
        createDaoWithResource(CONFIG_NO_VIEWS_RESOURCE);

        View view = m_dao.getDefaultView();
        assertNull("default view should be null", view);
    }

    /**
     * Test get view by name no views.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void testGetViewByNameNoViews() throws IOException {
        createDaoWithResource(CONFIG_NO_VIEWS_RESOURCE);

        View view = m_dao.getView("default");
        assertNull("view by name 'default' should be null", view);
    }

    /**
     * Test get views no views.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void testGetViewsNoViews() throws IOException {
        createDaoWithResource(CONFIG_NO_VIEWS_RESOURCE);

        Views views = m_dao.getViews();
        assertNotNull("views should not be null", views);
        assertEquals("view count", 0, views.getViewCount());
    }

    /**
     * Test get view map no views.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void testGetViewMapNoViews() throws IOException {
        createDaoWithResource(CONFIG_NO_VIEWS_RESOURCE);

        Map<String, View> viewMap = m_dao.getViewMap();
        assertNotNull("viewMap should not be null", viewMap);
        assertEquals("view count", 0, viewMap.size());
    }

    /**
     * Test config production.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void testConfigProduction() throws IOException {
        createDaoWithConfigFile("surveillance-views.xml");
    }

    /**
     * Test config example.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void testConfigExample() throws IOException {
        createDaoWithConfigFile("examples/surveillance-views.xml");
    }

    /**
     * Creates the dao with resource.
     *
     * @param configResource
     *            the config resource
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void createDaoWithResource(final String configResource) throws IOException {
        Resource resource = new ClassPathResource(configResource);
        m_dao = new DefaultSurveillanceViewConfigDao();
        m_dao.setConfigResource(resource);
        m_dao.afterPropertiesSet();
    }

    /**
     * Creates the dao with config file.
     *
     * @param configFileName
     *            the config file name
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void createDaoWithConfigFile(final String configFileName) throws IOException {
        Resource resource = new InputStreamResource(ConfigurationTestUtils.getInputStreamForConfigFile(configFileName));
        m_dao = new DefaultSurveillanceViewConfigDao();
        m_dao.setConfigResource(resource);
        m_dao.afterPropertiesSet();
    }

}
