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

/**
 *
 */
package org.opennms.web.svclayer.dao.support;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.config.CategoryFactory;
import org.opennms.netmgt.config.categories.Category;
import org.opennms.netmgt.mock.MockCategoryFactory;
import org.opennms.test.mock.MockUtil;

/**
 * The Class DefaultCategoryConfigDaoTest.
 *
 * @author jsartin
 */
public class DefaultCategoryConfigDaoTest extends TestCase {

    /** The m_dao. */
    DefaultCategoryConfigDao m_dao;

    /** The m_cat factory. */
    protected MockCategoryFactory m_catFactory;

    /**
     * Instantiates a new default category config dao test.
     *
     * @param arg0
     *            the arg0
     */
    public DefaultCategoryConfigDaoTest(final String arg0) {
        super(arg0);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected final void setUp() throws Exception {
        super.setUp();
        MockUtil.println("------------ Begin Test " + getName() + " --------------------------");
        MockLogAppender.setupLogging();
        m_catFactory = new MockCategoryFactory();
        CategoryFactory.setInstance(m_catFactory);
        m_dao = new DefaultCategoryConfigDao();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#runTest()
     */
    @Override
    public final void runTest() throws Throwable {
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
     * Test method for.
     * {@link org.opennms.web.svclayer.dao.support.DefaultCategoryConfigDao#findAll()}
     * .
     */
    public final void testFindAll() {
        Collection<Category> catColl = m_dao.findAll();
        assertFalse(catColl.isEmpty());
        assertEquals(catColl.size(), 2);
        Iterator<Category> i = catColl.iterator();
        assertEquals(i.next().getLabel(), "Network Interfaces");
        assertEquals(i.next().getLabel(), "Web Servers");
        /*
         * Iterator i = list.iterator();
         * while(i.hasNext()) {
         * Category cat = (Category)i.next();
         * MockUtil.println("found a category --" + cat.getLabel());
         * }
         */
    }

}
