package org.opennms.netmgt.config.tester;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.opennms.netmgt.config.tester.ConfigTester;
import org.opennms.test.DaoTestConfigBean;


public class ConfigTesterTest {
	@Before
	public void init() {
        DaoTestConfigBean daoTestConfig = new DaoTestConfigBean();
        daoTestConfig.afterPropertiesSet();
	}

	@Test
	public void testAckdConfiguration() {
        ConfigTester.main(new String[] { "ackd-configuration.xml" });
	}

	@Test
	public void testActiondConfiguration() {
        ConfigTester.main(new String[] { "actiond-configuration.xml" });
	}

	@Test
	public void testAmiConfig() {
        ConfigTester.main(new String[] { "ami-config.xml" });
	}
	
	@Test
	@Ignore
	public void testAsteriskConfiguration() {
        ConfigTester.main(new String[] { "asterisk-configuration.properties" });
	}
	
	@Test
	public void testAvailabilityReports() {
        ConfigTester.main(new String[] { "availability-reports.xml" });
	}
	
	@Test
	@Ignore
	/**
	 * This file isn't read directly by OpenNMS.
	 */
	public void testC3p0Properties() {
        ConfigTester.main(new String[] { "c3p0.properties" });
	}
	
	@Test
	public void testCapsdConfiguration() {
        ConfigTester.main(new String[] { "capsd-configuration.xml" });
	}
	
	@Test
	public void testCategories() {
        ConfigTester.main(new String[] { "categories.xml" });
	}
	
	@Test
	public void testChartConfiguration() {
        ConfigTester.main(new String[] { "chart-configuration.xml" });
	}
	
	@Test
	@Ignore
	public void testCollectdConfiguration() {
        ConfigTester.main(new String[] { "collectd-configuration.xml" });
	}
	
	@Test
	public void testDatabaseReports() {
        ConfigTester.main(new String[] { "database-reports.xml" });
	}
	
	@Test
	public void testDatabaseSchema() {
        ConfigTester.main(new String[] { "database-schema.xml" });
	}
	
	@Test
	public void testDataCollectionConfig() {
        ConfigTester.main(new String[] { "datacollection-config.xml" });
	}
	
	@Test
	public void testDestinationPaths() {
        ConfigTester.main(new String[] { "destinationPaths.xml" });
	}
	
	@Test
	public void testDhcpdConfiguration() {
        ConfigTester.main(new String[] { "dhcpd-configuration.xml" });
	}
	
	@Test
	public void testDiscoveryConfiguration() {
        ConfigTester.main(new String[] { "discovery-configuration.xml" });
	}

	@Test
	@Ignore
	public void testEventConf() {
        ConfigTester.main(new String[] { "eventconf.xml" });
	}
    
	@Test
    public void testEventdConfiguration() {
        ConfigTester.main(new String[] { "eventd-configuration.xml" });
    }
    
	@Test
    public void testEventsArchiverConfiguration() {
        ConfigTester.main(new String[] { "events-archiver-configuration.xml" });
    }
    
	@Test
    public void testExcludeUeis() {
        ConfigTester.main(new String[] { "exclude-ueis.properties" });
    }

	@Test
    public void testGroups() {
        ConfigTester.main(new String[] { "groups.xml" });
    }
    
	@Test
    public void testHttpDatacollectionConfig() {
        ConfigTester.main(new String[] { "http-datacollection-config.xml" });
    }

	@Test
    public void testJasperReports() {
        ConfigTester.main(new String[] { "jasper-reports.xml" });
    }
    
	@Test
    public void testJavamailConfigurationProperties() {
        ConfigTester.main(new String[] { "javamail-configuration.properties" });
    }
    
	@Test
    public void testJavamailConfigurationXml() {
        ConfigTester.main(new String[] { "javamail-configuration.xml" });
    }

	@Test
    public void testJdbcDatacollectionConfig() {
        ConfigTester.main(new String[] { "jdbc-datacollection-config.xml" });
    }
    
	@Test
    public void testJmxDatacollectionConfig() {
        ConfigTester.main(new String[] { "jmx-datacollection-config.xml" });
    }

    @Test
	public void testKscPerformanceReports() {
        ConfigTester.main(new String[] { "ksc-performance-reports.xml" });
	}

    @Test
    @Ignore
    public void testLinkdConfiguration() {
        ConfigTester.main(new String[] { "linkd-configuration.xml" });
    }

    @Test
    public void testMagicUsers() {
        ConfigTester.main(new String[] { "magic-users.properties" });
    }

    @Test
    public void testMap() {
        ConfigTester.main(new String[] { "map.properties" });
    }
    
    @Test
    @Ignore
    public void testMapsadapterConfiguration() {
        ConfigTester.main(new String[] { "mapsadapter-configuration.xml" });
    }
    
    @Test
    public void testMicroblogConfiguration() {
        ConfigTester.main(new String[] { "microblog-configuration.xml" });
    }
    
    @Test
    public void testModelImporter() {
        ConfigTester.main(new String[] { "model-importer.properties" });
    }
    
    @Test
    @Ignore
    /**
     * See GatewayGroupLoader for the code that we'd need to call in the ConfigTester.
     */
    public void testModemConfig() {
        ConfigTester.main(new String[] { "modemConfig.properties" });
    }
    
    @Test
    public void testMonitoringLocations() {
        ConfigTester.main(new String[] { "monitoring-locations.xml" });
    }
    
    @Test
    public void testNotifdConfiguration() {
        ConfigTester.main(new String[] { "notifd-configuration.xml" });
    }

    @Test
    public void testNotificationCommands() {
        ConfigTester.main(new String[] { "notificationCommands.xml" });
    }
    
    @Test
    @Ignore
    public void testNotifications() {
        ConfigTester.main(new String[] { "notifications.xml" });
    }
    
    @Test
    public void testNsclientConfig() {
        ConfigTester.main(new String[] { "nsclient-config.xml" });
    }
    
    @Test
    public void testNsclientDatacollectionConfig() {
        ConfigTester.main(new String[] { "nsclient-datacollection-config.xml" });
    }
    
    @Test
    public void testOpennmsDatasources() {
        ConfigTester.main(new String[] { "opennms-datasources.xml" });
    }
    
    @Test
    public void testOpennmsServer() {
        ConfigTester.main(new String[] { "opennms-server.xml" });
    }
    
    @Test
    @Ignore
    public void testOpennms() {
        ConfigTester.main(new String[] { "opennms.properties" });
    }
    
    @Test
    public void testOtrs() {
        ConfigTester.main(new String[] { "otrs.properties" });
    }
    
    @Test
    public void testPollOutages() {
        ConfigTester.main(new String[] { "poll-outages.xml" });
    }
    
    @Test
    public void testPollerConfig() {
        ConfigTester.main(new String[] { "poller-config.properties" });
    }
    
    @Test
    @Ignore
    public void testPollerConfiguration() {
        ConfigTester.main(new String[] { "poller-configuration.xml" });
    }
    
    @Test
    public void testProvisiondConfiguration() {
        ConfigTester.main(new String[] { "provisiond-configuration.xml" });
    }

    @Test
    public void testRancidConfiguration() {
        ConfigTester.main(new String[] { "rancid-configuration.xml" });
    }

    @Test
    public void testReportdConfiguration() {
        ConfigTester.main(new String[] { "reportd-configuration.xml" });
    }
	
	@Test
	public void testResponseAdhocGraph() {
        ConfigTester.main(new String[] { "response-adhoc-graph.properties" });
	}
	
	@Test
	public void testResponsePrefabGraph() {
        ConfigTester.main(new String[] { "response-graph.properties" });
	}

	@Test
    public void testRrdConfiguration() {
        ConfigTester.main(new String[] { "rrd-configuration.properties" });
    }

	@Test
    public void testRt() {
        ConfigTester.main(new String[] { "rt.properties" });
    }

	@Test
    public void testRtcConfiguration() {
        ConfigTester.main(new String[] { "rtc-configuration.xml" });
    }

	@Test
    public void testRwsConfiguration() {
        ConfigTester.main(new String[] { "rws-configuration.xml" });
    }

	@Test
    public void testScriptdConfiguration() {
        ConfigTester.main(new String[] { "scriptd-configuration.xml" });
    }

	@Test
    public void testServiceConfiguration() {
        ConfigTester.main(new String[] { "service-configuration.xml" });
    }

	@Test
    public void testSiteStatusViews() {
        ConfigTester.main(new String[] { "site-status-views.xml" });
    }

	@Test
    public void testSmsPhonebook() {
        ConfigTester.main(new String[] { "smsPhonebook.properties" });
    }

	@Test
	public void testSnmpAdhocGraph() {
        ConfigTester.main(new String[] { "snmp-adhoc-graph.properties" });
	}

	@Test
    public void testSnmpAssetAdapterConfiguration() {
        ConfigTester.main(new String[] { "snmp-asset-adapter-configuration.xml" });
    }

	@Test
    public void testSnmpConfig() {
        ConfigTester.main(new String[] { "snmp-config.xml" });
    }

	@Test
	public void testSnmpPrefabGraph() {
        ConfigTester.main(new String[] { "snmp-graph.properties" });
	}

	@Test
	@Ignore
    public void testSnmpInterfacePollerConfiguration() {
        ConfigTester.main(new String[] { "snmp-interface-poller-configuration.xml" });
    }

	@Test
    public void testStatsdConfiguration() {
        ConfigTester.main(new String[] { "statsd-configuration.xml" });
    }

	@Test
    public void testSurveillanceViews() {
        ConfigTester.main(new String[] { "surveillance-views.xml" });
    }

	@Test
    public void testSyslogdConfiguration() {
        ConfigTester.main(new String[] { "syslogd-configuration.xml" });
    }

	@Test
	@Ignore
    public void testThreshdConfiguration() {
        ConfigTester.main(new String[] { "threshd-configuration.xml" });
    }

	@Test
    public void testThresholds() {
        ConfigTester.main(new String[] { "thresholds.xml" });
    }

	@Test
    public void testTl1dConfiguration() {
        ConfigTester.main(new String[] { "tl1d-configuration.xml" });
    }

	@Test
	@Ignore
    public void testTranslatorConfiguration() {
        ConfigTester.main(new String[] { "translator-configuration.xml" });
    }

	@Test
    public void testTrapdConfiguration() {
        ConfigTester.main(new String[] { "trapd-configuration.xml" });
    }

	@Test
    public void testUsers() {
        ConfigTester.main(new String[] { "users.xml" });
    }

	@Test
    public void testVacuumdConfiguration() {
        ConfigTester.main(new String[] { "vacuumd-configuration.xml" });
    }

	@Test
    public void testViewsdisplay() {
        ConfigTester.main(new String[] { "viewsdisplay.xml" });
    }

	@Test
    public void testVulnscandConfiguration() {
        ConfigTester.main(new String[] { "vulnscand-configuration.xml" });
    }

	@Test
    public void testWmiConfig() {
        ConfigTester.main(new String[] { "wmi-config.xml" });
    }

	@Test
    public void testWmiDatacollectionConfig() {
        ConfigTester.main(new String[] { "wmi-datacollection-config.xml" });
    }

	@Test
    public void testXmlrpcdConfiguration() {
        ConfigTester.main(new String[] { "xmlrpcd-configuration.xml" });
    }

	@Test
    public void testXmpConfig() {
        ConfigTester.main(new String[] { "xmp-config.xml" });
    }

	@Test
    public void testXmpDatacollectionConfig() {
        ConfigTester.main(new String[] { "xmp-datacollection-config.xml" });
    }

	@Test
	@Ignore
    public void testXmppConfiguration() {
        ConfigTester.main(new String[] { "xmpp-configuration.properties" });
    }
}
