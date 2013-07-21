/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * The Class DefaultJaspertReportConfigDaoTest.
 */
public class DefaultJaspertReportConfigDaoTest {

    /** The Constant ID. */
    private static final String ID = "sample-report";

    /** The Constant TEMPLATE. */
    private static final String TEMPLATE = "sample-report.jxrml";

    /** The Constant ENGINE. */
    private static final String ENGINE = "jdbc";

    /** The m_dao. */
    private static DefaultJasperReportConfigDao m_dao;

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        Resource resource = new ClassPathResource("/jasper-reports-testdata.xml");
        m_dao = new DefaultJasperReportConfigDao();
        m_dao.setConfigResource(resource);
        m_dao.afterPropertiesSet();
    }

    /**
     * Test config.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testConfig() throws Exception {
        assertEquals(TEMPLATE, m_dao.getTemplateLocation(ID));
        assertEquals(ENGINE, m_dao.getEngine(ID));
    }

}
