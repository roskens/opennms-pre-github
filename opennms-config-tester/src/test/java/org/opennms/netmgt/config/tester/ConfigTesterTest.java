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

package org.opennms.netmgt.config.tester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opennms.core.db.DataSourceFactory;
import org.opennms.core.test.ConfigurationTestUtils;
import org.opennms.test.DaoTestConfigBean;
import org.springframework.util.StringUtils;

/**
 * The Class ConfigTesterTest.
 */
public class ConfigTesterTest {

    /** The m_files tested. */
    private static Set<String> m_filesTested = new HashSet<String>();

    /** The m_files ignored. */
    private static Set<String> m_filesIgnored = new HashSet<String>();

    // private ConfigTesterDataSource m_dataSource;

    /**
     * Inits the.
     */
    @Before
    public void init() {
        DaoTestConfigBean daoTestConfig = new DaoTestConfigBean();
        daoTestConfig.afterPropertiesSet();
    }

    /**
     * Done.
     */
    @After
    public void done() {
        ConfigTesterDataSource dataSource = (ConfigTesterDataSource) DataSourceFactory.getDataSource();

        if (dataSource != null && dataSource.getConnectionGetAttempts().size() > 0) {
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            for (SQLException e : dataSource.getConnectionGetAttempts()) {
                e.printStackTrace(printWriter);
            }
            fail(dataSource.getConnectionGetAttempts().size() + " DataSource.getConnection attempts were made: \n"
                    + writer.toString());
        }
    }

    /**
     * Test system properties.
     */
    @Test
    public void testSystemProperties() {
        assertEquals("false", System.getProperty("distributed.layoutApplicationsVertically"));
        assertEquals("target/test/logs", System.getProperty("opennms.webapplogs.dir"));
    }

    /**
     * Test access point monitor configuration.
     */
    @Test
    public void testAccessPointMonitorConfiguration() {
        ignoreConfigFile("access-point-monitor-configuration.xml");
    }

    /**
     * Test ackd configuration.
     */
    @Test
    public void testAckdConfiguration() {
        testConfigFile("ackd-configuration.xml");
    }

    /**
     * Test actiond configuration.
     */
    @Test
    public void testActiondConfiguration() {
        testConfigFile("actiond-configuration.xml");
    }

    /**
     * Test ami config.
     */
    @Test
    public void testAmiConfig() {
        testConfigFile("ami-config.xml");
    }

    /**
     * Test asterisk configuration.
     */
    @Test
    /**
     * FIXME: AsteriskConfig doesn't appear to be in our classpath.
     */
    public void testAsteriskConfiguration() {
        ignoreConfigFile("asterisk-configuration.properties");
    }

    /**
     * Test availability reports.
     */
    @Test
    public void testAvailabilityReports() {
        testConfigFile("availability-reports.xml");
    }

    /**
     * Test c3p0 properties.
     */
    @Test
    /**
     * This file isn't read directly by OpenNMS.
     */
    public void testC3p0Properties() {
        ignoreConfigFile("c3p0.properties");
    }

    /**
     * Test capsd configuration.
     */
    @Test
    public void testCapsdConfiguration() {
        testConfigFile("capsd-configuration.xml");
    }

    /**
     * Test categories.
     */
    @Test
    public void testCategories() {
        testConfigFile("categories.xml");
    }

    /**
     * Test chart configuration.
     */
    @Test
    public void testChartConfiguration() {
        testConfigFile("chart-configuration.xml");
    }

    /**
     * Test collectd configuration.
     */
    @Test
    /**
     * Database access.
     */
    public void testCollectdConfiguration() {
        ignoreConfigFile("collectd-configuration.xml");
    }

    /**
     * Test database reports.
     */
    @Test
    public void testDatabaseReports() {
        testConfigFile("database-reports.xml");
    }

    /**
     * Test database schema.
     */
    @Test
    public void testDatabaseSchema() {
        testConfigFile("database-schema.xml");
    }

    /**
     * Test data collection config.
     */
    @Test
    public void testDataCollectionConfig() {
        testConfigFile("datacollection-config.xml");
    }

    /**
     * Test destination paths.
     */
    @Test
    public void testDestinationPaths() {
        testConfigFile("destinationPaths.xml");
    }

    /**
     * Test discovery configuration.
     */
    @Test
    public void testDiscoveryConfiguration() {
        testConfigFile("discovery-configuration.xml");
    }

    /**
     * Test event conf.
     */
    @Test
    public void testEventConf() {
        testConfigFile("eventconf.xml");
    }

    /**
     * Test eventd configuration.
     */
    @Test
    public void testEventdConfiguration() {
        testConfigFile("eventd-configuration.xml");
    }

    /**
     * Test events archiver configuration.
     */
    @Test
    public void testEventsArchiverConfiguration() {
        testConfigFile("events-archiver-configuration.xml");
    }

    /**
     * Test exclude ueis.
     */
    @Test
    public void testExcludeUeis() {
        testConfigFile("exclude-ueis.properties");
    }

    /**
     * Test groups.
     */
    @Test
    public void testGroups() {
        testConfigFile("groups.xml");
    }

    /**
     * Test http datacollection config.
     */
    @Test
    public void testHttpDatacollectionConfig() {
        testConfigFile("http-datacollection-config.xml");
    }

    /**
     * Test jasper reports.
     */
    @Test
    public void testJasperReports() {
        testConfigFile("jasper-reports.xml");
    }

    /**
     * Test javamail configuration properties.
     */
    @Test
    public void testJavamailConfigurationProperties() {
        testConfigFile("javamail-configuration.properties");
    }

    /**
     * Test javamail configuration xml.
     */
    @Test
    public void testJavamailConfigurationXml() {
        testConfigFile("javamail-configuration.xml");
    }

    /**
     * Test jdbc datacollection config.
     */
    @Test
    public void testJdbcDatacollectionConfig() {
        testConfigFile("jdbc-datacollection-config.xml");
    }

    /**
     * Test jmx datacollection config.
     */
    @Test
    public void testJmxDatacollectionConfig() {
        testConfigFile("jmx-datacollection-config.xml");
    }

    /**
     * Test ksc performance reports.
     */
    @Test
    public void testKscPerformanceReports() {
        testConfigFile("ksc-performance-reports.xml");
    }

    /**
     * Test linkd configuration.
     */
    @Test
    /**
     * FIXME: Database access.
     */
    public void testLinkdConfiguration() {
        ignoreConfigFile("linkd-configuration.xml");
    }

    /**
     * Test log4j2 config.
     */
    @Test
    public void testLog4j2Config() {
        ignoreConfigFile("log4j2.xml");
    }

    /**
     * Test log4j2 archive events config.
     */
    @Test
    public void testLog4j2ArchiveEventsConfig() {
        ignoreConfigFile("log4j2-archive-events.xml");
    }

    /**
     * Test magic users.
     */
    @Test
    public void testMagicUsers() {
        testConfigFile("magic-users.properties");
    }

    /**
     * Test map.
     */
    @Test
    public void testMap() {
        testConfigFile("map.properties");
    }

    /**
     * Test mapsadapter configuration.
     */
    @Test
    /**
     * FIXME: Database access.
     */
    public void testMapsadapterConfiguration() {
        ignoreConfigFile("mapsadapter-configuration.xml");
    }

    /**
     * Test microblog configuration.
     */
    @Test
    public void testMicroblogConfiguration() {
        testConfigFile("microblog-configuration.xml");
    }

    /**
     * Test model importer.
     */
    @Test
    public void testModelImporter() {
        testConfigFile("model-importer.properties");
    }

    /**
     * Test modem config.
     */
    @Test
    /**
     * FIXME: Don't know why this is ignored.
     *
     * See GatewayGroupLoader for the code that we'd need to call in the ConfigTester.
     */
    public void testModemConfig() {
        ignoreConfigFile("modemConfig.properties");
    }

    /**
     * Test monitoring locations.
     */
    @Test
    /**
     * FIXME: Use LocationMonitorDaoHibernate to parse the config file
     */
    public void testMonitoringLocations() {
        ignoreConfigFile("monitoring-locations.xml");
    }

    /**
     * Test notifd configuration.
     */
    @Test
    public void testNotifdConfiguration() {
        testConfigFile("notifd-configuration.xml");
    }

    /**
     * Test notification commands.
     */
    @Test
    public void testNotificationCommands() {
        testConfigFile("notificationCommands.xml");
    }

    /**
     * Test notifications.
     */
    @Test
    /**
     * FIXME: Database access.
     */
    public void testNotifications() {
        ignoreConfigFile("notifications.xml");
    }

    /**
     * Test nsclient config.
     */
    @Test
    @Ignore
    public void testNsclientConfig() {
        testConfigFile("nsclient-config.xml");
    }

    /**
     * Test nsclient datacollection config.
     */
    @Test
    @Ignore
    public void testNsclientDatacollectionConfig() {
        testConfigFile("nsclient-datacollection-config.xml");
    }

    /**
     * Test opennms datasources.
     */
    @Test
    public void testOpennmsDatasources() {
        testConfigFile("opennms-datasources.xml");
    }

    /**
     * Test opennms server.
     */
    @Test
    public void testOpennmsServer() {
        testConfigFile("opennms-server.xml");
    }

    /**
     * Test opennms.
     */
    @Test
    /**
     * FIXME: Don't know why this is off.
     */
    public void testOpennms() {
        ignoreConfigFile("opennms.properties");
    }

    /**
     * Test otrs.
     */
    @Test
    public void testOtrs() {
        testConfigFile("otrs.properties");
    }

    /**
     * Test poll outages.
     */
    @Test
    public void testPollOutages() {
        testConfigFile("poll-outages.xml");
    }

    /**
     * Test poller config.
     */
    @Test
    public void testPollerConfig() {
        testConfigFile("poller-config.properties");
    }

    /**
     * Test poller configuration.
     */
    @Test
    /**
     * FIXME: Database access.
     */
    public void testPollerConfiguration() {
        ignoreConfigFile("poller-configuration.xml");
    }

    /**
     * Test provisiond configuration.
     */
    @Test
    public void testProvisiondConfiguration() {
        testConfigFile("provisiond-configuration.xml");
    }

    /**
     * Test rancid configuration.
     */
    @Test
    /**
     * FIXME: Not part of the standard build?
     */
    public void testRancidConfiguration() {
        ignoreConfigFile("rancid-configuration.xml");
    }

    /**
     * Test reportd configuration.
     */
    @Test
    public void testReportdConfiguration() {
        testConfigFile("reportd-configuration.xml");
    }

    /**
     * Test response adhoc graph.
     */
    @Test
    public void testResponseAdhocGraph() {
        testConfigFile("response-adhoc-graph.properties");
    }

    /**
     * Test response prefab graph.
     */
    @Test
    public void testResponsePrefabGraph() {
        testConfigFile("response-graph.properties");
    }

    /**
     * Test rrd configuration.
     */
    @Test
    public void testRrdConfiguration() {
        testConfigFile("rrd-configuration.properties");
    }

    /**
     * Test rt.
     */
    @Test
    public void testRt() {
        testConfigFile("rt.properties");
    }

    /**
     * Test rtc configuration.
     */
    @Test
    public void testRtcConfiguration() {
        testConfigFile("rtc-configuration.xml");
    }

    /**
     * Test rws configuration.
     */
    @Test
    public void testRwsConfiguration() {
        testConfigFile("rws-configuration.xml");
    }

    /**
     * Test scriptd configuration.
     */
    @Test
    public void testScriptdConfiguration() {
        testConfigFile("scriptd-configuration.xml");
    }

    /**
     * Test service configuration.
     */
    @Test
    public void testServiceConfiguration() {
        testConfigFile("service-configuration.xml");
    }

    /**
     * Test site status views.
     */
    @Test
    public void testSiteStatusViews() {
        testConfigFile("site-status-views.xml");
    }

    /**
     * Test sms phonebook.
     */
    @Test
    public void testSmsPhonebook() {
        testConfigFile("smsPhonebook.properties");
    }

    /**
     * Test snmp adhoc graph.
     */
    @Test
    public void testSnmpAdhocGraph() {
        testConfigFile("snmp-adhoc-graph.properties");
    }

    /**
     * Test snmp asset adapter configuration.
     */
    @Test
    /**
     * FIXME: Not part of the standard build?
     */
    public void testSnmpAssetAdapterConfiguration() {
        ignoreConfigFile("snmp-asset-adapter-configuration.xml");
    }

    /**
     * Test snmp config.
     */
    @Test
    public void testSnmpConfig() {
        testConfigFile("snmp-config.xml");
    }

    /**
     * Test snmp prefab graph.
     */
    @Test
    public void testSnmpPrefabGraph() {
        testConfigFile("snmp-graph.properties");
    }

    /**
     * Test snmp interface poller configuration.
     */
    @Test
    /**
     * FIXME: Database access.
     */
    public void testSnmpInterfacePollerConfiguration() {
        ignoreConfigFile("snmp-interface-poller-configuration.xml");
    }

    /**
     * Test statsd configuration.
     */
    @Test
    public void testStatsdConfiguration() {
        testConfigFile("statsd-configuration.xml");
    }

    /**
     * Test surveillance views.
     */
    @Test
    public void testSurveillanceViews() {
        testConfigFile("surveillance-views.xml");
    }

    /**
     * Test syslogd configuration.
     */
    @Test
    public void testSyslogdConfiguration() {
        testConfigFile("syslogd-configuration.xml");
    }

    /**
     * Test syslog northbounder configuration.
     */
    @Test
    public void testSyslogNorthbounderConfiguration() {
        testConfigFile("syslog-northbounder-configuration.xml");
    }

    /**
     * Test threshd configuration.
     */
    @Test
    /**
     * FIXME: Database access.
     */
    public void testThreshdConfiguration() {
        ignoreConfigFile("threshd-configuration.xml");
    }

    /**
     * Test thresholds.
     */
    @Test
    public void testThresholds() {
        testConfigFile("thresholds.xml");
    }

    /**
     * Test tl1d configuration.
     */
    @Test
    public void testTl1dConfiguration() {
        testConfigFile("tl1d-configuration.xml");
    }

    /**
     * Test translator configuration.
     */
    @Test
    public void testTranslatorConfiguration() {
        testConfigFile("translator-configuration.xml");
    }

    /**
     * Test trapd configuration.
     */
    @Test
    public void testTrapdConfiguration() {
        testConfigFile("trapd-configuration.xml");
    }

    /**
     * Test users.
     */
    @Test
    public void testUsers() {
        testConfigFile("users.xml");
    }

    /**
     * Test vacuumd configuration.
     */
    @Test
    public void testVacuumdConfiguration() {
        testConfigFile("vacuumd-configuration.xml");
    }

    /**
     * Test viewsdisplay.
     */
    @Test
    public void testViewsdisplay() {
        testConfigFile("viewsdisplay.xml");
    }

    /**
     * Test wmi config.
     */
    @Test
    public void testWmiConfig() {
        testConfigFile("wmi-config.xml");
    }

    /**
     * Test wmi datacollection config.
     */
    @Test
    public void testWmiDatacollectionConfig() {
        testConfigFile("wmi-datacollection-config.xml");
    }

    /**
     * Test xmlrpcd configuration.
     */
    @Test
    public void testXmlrpcdConfiguration() {
        testConfigFile("xmlrpcd-configuration.xml");
    }

    /**
     * Test v mware cim datacollection config.
     */
    @Test
    public void testVMwareCimDatacollectionConfig() {
        testConfigFile("vmware-cim-datacollection-config.xml");
    }

    /**
     * Test v mware config config.
     */
    @Test
    public void testVMwareConfigConfig() {
        testConfigFile("vmware-config.xml");
    }

    /**
     * Test v mware datacollection config.
     */
    @Test
    public void testVMwareDatacollectionConfig() {
        testConfigFile("vmware-datacollection-config.xml");
    }

    /**
     * Test xmp config.
     */
    @Test
    @Ignore
    public void testXmpConfig() {
        testConfigFile("xmp-config.xml");
    }

    /**
     * Test xmp datacollection config.
     */
    @Test
    @Ignore
    public void testXmpDatacollectionConfig() {
        testConfigFile("xmp-datacollection-config.xml");
    }

    /**
     * Test xmpp configuration.
     */
    @Test
    /**
     * FIXME: Configuration code is not in its own class.
     *
     * It's embedded in XMPPNotificationManager's constructor.
     */
    public void testXmppConfiguration() {
        ignoreConfigFile("xmpp-configuration.properties");
    }

    /**
     * Test remote repositoy config.
     */
    @Test
    public void testRemoteRepositoyConfig() {
        ignoreConfigFile("remote-repository.xml");
    }

    /**
     * Test all configs.
     */
    @Test
    public void testAllConfigs() {
        ConfigTester.main(new String[] { "-a" });
    }

    /**
     * Test config file.
     *
     * @param file
     *            the file
     */
    private void testConfigFile(String file) {
        /*
         * Add to the tested list first, so if we get a test failure
         * for a specific file test, we don't also make
         * testCheckAllDaemonXmlConfigFilesTested fail.
         */
        m_filesTested.add(file);

        ConfigTester.main(new String[] { file });
    }

    /**
     * Ignore config file.
     *
     * @param file
     *            the file
     */
    private void ignoreConfigFile(String file) {
        m_filesIgnored.add(file);
    }

    /**
     * Test check all daemon xml config files tested.
     */
    @Test
    public void testCheckAllDaemonXmlConfigFilesTested() {
        File someConfigFile = ConfigurationTestUtils.getFileForConfigFile("discovery-configuration.xml");
        File configDir = someConfigFile.getParentFile();
        assertTrue("daemon configuration directory exists at " + configDir.getAbsolutePath(), configDir.exists());
        assertTrue("daemon configuration directory is a directory at " + configDir.getAbsolutePath(),
                   configDir.isDirectory());

        String[] configFiles = configDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(".xml");
            }
        });

        Set<String> allXml = new HashSet<String>(Arrays.asList(configFiles));

        allXml.removeAll(m_filesTested);
        allXml.removeAll(m_filesIgnored);

        if (allXml.size() > 0) {
            List<String> files = new ArrayList<String>(allXml);
            Collections.sort(files);
            fail("These files in " + configDir.getAbsolutePath() + " were not tested: \n\t"
                    + StringUtils.collectionToDelimitedString(files, "\n\t"));
        }
    }
}
