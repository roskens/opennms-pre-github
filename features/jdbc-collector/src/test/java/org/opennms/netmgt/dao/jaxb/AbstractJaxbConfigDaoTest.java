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

package org.opennms.netmgt.dao.jaxb;

import java.io.InputStream;

import junit.framework.TestCase;

import org.opennms.core.test.ConfigurationTestUtils;
import org.opennms.core.xml.AbstractJaxbConfigDao;
import org.opennms.core.xml.MarshallingResourceFailureException;
import org.opennms.netmgt.config.jdbc.JdbcDataCollectionConfig;
import org.opennms.test.ThrowableAnticipator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

/**
 * The Class AbstractJaxbConfigDaoTest.
 */
public class AbstractJaxbConfigDaoTest extends TestCase {

    /**
     * Test after properties set with no config set.
     */
    public void testAfterPropertiesSetWithNoConfigSet() {
        TestJaxbConfigDao dao = new TestJaxbConfigDao();

        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new IllegalStateException("property configResource must be set and be non-null"));

        try {
            dao.afterPropertiesSet();
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        ta.verifyAnticipated();
    }

    /**
     * Test after properties set with bogus file resource.
     *
     * @throws Exception
     *             the exception
     */
    public void testAfterPropertiesSetWithBogusFileResource() throws Exception {
        Resource resource = new FileSystemResource("/bogus-file");
        TestJaxbConfigDao dao = new TestJaxbConfigDao();
        dao.setConfigResource(resource);

        ThrowableAnticipator ta = new ThrowableAnticipator();
        ta.anticipate(new MarshallingResourceFailureException(ThrowableAnticipator.IGNORE_MESSAGE));

        try {
            dao.afterPropertiesSet();
        } catch (Throwable t) {
            ta.throwableReceived(t);
        }
        ta.verifyAnticipated();
    }

    /**
     * Test after properties set with good config file.
     *
     * @throws Exception
     *             the exception
     */
    public void testAfterPropertiesSetWithGoodConfigFile() throws Exception {
        TestJaxbConfigDao dao = new TestJaxbConfigDao();

        InputStream in = ConfigurationTestUtils.getInputStreamForConfigFile("jdbc-datacollection-config.xml");
        dao.setConfigResource(new InputStreamResource(in));
        dao.afterPropertiesSet();

        assertNotNull("jdbc data collection should not be null", dao.getDataCollectionConfig());
    }

    /**
     * The Class TestJaxbConfigDao.
     */
    public static class TestJaxbConfigDao extends
            AbstractJaxbConfigDao<JdbcDataCollectionConfig, JdbcDataCollectionConfig> {

        /**
         * Instantiates a new test jaxb config dao.
         */
        public TestJaxbConfigDao() {
            super(JdbcDataCollectionConfig.class, "jdbc data collection configuration");
        }

        /* (non-Javadoc)
         * @see org.opennms.core.xml.AbstractJaxbConfigDao#translateConfig(java.lang.Object)
         */
        @Override
        protected JdbcDataCollectionConfig translateConfig(JdbcDataCollectionConfig jaxbConfig) {
            return jaxbConfig;
        }

        /**
         * Gets the data collection config.
         *
         * @return the data collection config
         */
        public JdbcDataCollectionConfig getDataCollectionConfig() {
            return getContainer().getObject();
        }
    }
}
