/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.collectd;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.opennms.core.test.ConfigurationTestUtils;
import org.opennms.netmgt.collection.api.ServiceCollector;
import org.opennms.netmgt.config.DataCollectionConfigFactory;
import org.opennms.netmgt.config.DatabaseSchemaConfigFactory;
import org.opennms.netmgt.config.DefaultDataCollectionConfigDao;
import org.opennms.netmgt.config.HttpCollectionConfigFactory;
import org.opennms.netmgt.rrd.RrdUtils;
import org.opennms.netmgt.rrd.jrobin.JRobinRrdStrategy;
import org.opennms.test.PathAnticipator;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * <p>This {@link TestExecutionListener} looks for the {@link JUnitCollector} annotation
 * and uses attributes on it to:</p>
 * <ul>
 * <li>Load configuration files for the {@link ServiceCollector}</li>
 * <li>Set up {@link PathAnticipator} checks for files created
 * during the unit test execution</li>
 * </ul>
 */
public class JUnitCollectorExecutionListener extends AbstractTestExecutionListener {

    private Path m_snmpRrdDirectory;
    private PathAnticipator m_pathAnticipator;

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        m_pathAnticipator = new PathAnticipator();

        JUnitCollector config = findCollectorAnnotation(testContext);
        if (config == null) {
            return;
        }

        RrdUtils.setStrategy(new JRobinRrdStrategy());

        // make a fake database schema with hibernate
        InputStream is = ConfigurationTestUtils.getInputStreamForResource(testContext.getTestInstance(), config.schemaConfig());
        DatabaseSchemaConfigFactory.setInstance(new DatabaseSchemaConfigFactory(is));
        is.close();

        // set up temporary directories for RRD files
        m_snmpRrdDirectory = m_pathAnticipator.tempDir("snmp");
        Files.createDirectories(m_snmpRrdDirectory);
        testContext.setAttribute("pathAnticipator", m_pathAnticipator);
        testContext.setAttribute("rrdDirectory", m_snmpRrdDirectory);

        // set up the collection configuration factory
        if ("http".equalsIgnoreCase(config.datacollectionType()) || "https".equalsIgnoreCase(config.datacollectionType())) {
            is = ConfigurationTestUtils.getInputStreamForResourceWithReplacements(testContext.getTestInstance(), config.datacollectionConfig(), new String[]{"%rrdRepository%", m_snmpRrdDirectory.toString()});
            HttpCollectionConfigFactory factory = new HttpCollectionConfigFactory(is);
            HttpCollectionConfigFactory.setInstance(factory);
            HttpCollectionConfigFactory.init();
        } else if ("snmp".equalsIgnoreCase(config.datacollectionType())) {
            Resource r = ConfigurationTestUtils.getSpringResourceForResourceWithReplacements(testContext.getTestInstance(), config.datacollectionConfig(), new String[]{"%rrdRepository%", m_snmpRrdDirectory.toString()});
            DefaultDataCollectionConfigDao dataCollectionDao = new DefaultDataCollectionConfigDao();
            dataCollectionDao.setConfigResource(r);
            dataCollectionDao.afterPropertiesSet();
            DataCollectionConfigFactory.setInstance(dataCollectionDao);
        } else {
            throw new UnsupportedOperationException("data collection type '" + config.datacollectionType() + "' not supported");
        }
        IOUtils.closeQuietly(is);
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        JUnitCollector config = findCollectorAnnotation(testContext);
        if (config == null) {
            return;
        }

        boolean shouldIgnoreNonExistent = testContext.getTestException() != null;

        /*
        if (config.anticipateFiles().length > 0 || config.anticipateRrds().length > 0) {
            // make sure any RRDs have time to get written
            Thread.sleep(config.timeout());
        }
        */

        if (config.anticipateRrds().length > 0) {
            for (String rrdFile : config.anticipateRrds()) {
                m_pathAnticipator.expecting(m_snmpRrdDirectory, rrdFile + RrdUtils.getExtension());

                //the nrtg feature requires .meta files in parallel to the rrd/jrb files.
                //this .meta files are expected
                m_pathAnticipator.expecting(m_snmpRrdDirectory, rrdFile + ".meta");
            }
        }

        if (config.anticipateFiles().length > 0) {
            for (String file : config.anticipateFiles()) {
                m_pathAnticipator.expecting(m_snmpRrdDirectory, file);
            }
        }

        Exception e = null;
        if (m_pathAnticipator.isInitialized()) {
            final long finished = System.currentTimeMillis() + config.timeout();
            while (System.currentTimeMillis() <= finished) {
                if (m_pathAnticipator.foundExpected()) {
                    break;
                }
                try {
                    Thread.sleep(200);
                } catch (final InterruptedException ie) {
                    break;
                }
            }

            try {
                m_pathAnticipator.deleteExpected(shouldIgnoreNonExistent);
            } catch (Throwable t) {
                e = new RuntimeException(t);
            }
        }

        deleteRecursively(m_snmpRrdDirectory);
        m_pathAnticipator.tearDown();

        if (e != null) {
            throw e;
        }
    }

    private static void deleteRecursively(Path directory) {
        if (!Files.exists(directory)) {
            return;
        }

        if (Files.isDirectory(directory)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory);) {
                for (Path f : stream) {
                    deleteRecursively(f);
                }
            } catch (IOException e) {
            }
        }

        try {
            Files.delete(directory);
        } catch (IOException ex) {
        }
    }

    private static JUnitCollector findCollectorAnnotation(TestContext testContext) {
        Method testMethod = testContext.getTestMethod();
        JUnitCollector config = testMethod.getAnnotation(JUnitCollector.class);
        if (config != null) {
            return config;
        }

        Class<?> testClass = testContext.getTestClass();
        return testClass.getAnnotation(JUnitCollector.class);
    }

}
