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

package org.opennms.web.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.config.SnmpPeerFactory;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.web.snmpinfo.SnmpInfo;

/*
 * TODO
 * 1. Need to figure it out how to create a Mock for EventProxy to validate events sent by RESTful service
 */

public class SnmpConfigRestServiceTest extends AbstractSpringJerseyRestTestCase {

	private static final int DEFAULT_PORT = 9161;
	private static final int DEFAULT_RETRIES = 5;
	private static final int DEFAULT_TIMEOUT = 2000;
	private static final String DEFAULT_COMMUNITY = "myPublic";
	private static final String DEFAULT_VERSION = "v1";
	private static final int DEFAULT_MAX_VARS_PER_PDU = 100;
	private static final int DEFAULT_MAX_REPETITIONS = 3;

	private JAXBContext m_jaxbContext;
	private File m_snmpConfigFile;

	@Override
	protected void beforeServletStart() throws Exception {
		File dir = new File("target/test-work-dir");
		dir.mkdirs();
		setSnmpConfigFile(getSnmpDefaultConfigFileForSnmpV1());
		m_jaxbContext = JAXBContext.newInstance(SnmpInfo.class);
	}
	
	private String getSnmpDefaultConfigFileForSnmpV1() {
		return String.format("<?xml version=\"1.0\"?>"
				+ "<snmp-config port=\"%s\" retry=\"%s\" timeout=\"%s\"\n"
				+ "             read-community=\"%s\" \n" 
				+ "				version=\"%s\" \n"
				+ "             max-vars-per-pdu=\"%s\" max-repetitions=\"%s\"  />", 
				DEFAULT_PORT, DEFAULT_RETRIES, DEFAULT_TIMEOUT, DEFAULT_COMMUNITY, DEFAULT_VERSION, 
				DEFAULT_MAX_VARS_PER_PDU, DEFAULT_MAX_REPETITIONS);
	}
	
	private String getSnmpDefaultConfigFileForSnmpV3() {
		return String.format("<?xml version=\"1.0\"?>"
				+ "<snmp-config port=\"%s\" retry=\"%s\" timeout=\"%s\"\n"
				+ "				version=\"%s\" \n"
				+ "             max-vars-per-pdu=\"%s\" max-repetitions=\"%s\"  />", 
				DEFAULT_PORT, DEFAULT_RETRIES, DEFAULT_TIMEOUT, "v3", 
				DEFAULT_MAX_VARS_PER_PDU, DEFAULT_MAX_REPETITIONS);
	}
	
	private void setSnmpConfigFile(final String snmpConfigContent) throws IOException {
		m_snmpConfigFile = File.createTempFile("snmp-config-", ".xml");
		m_snmpConfigFile.deleteOnExit();
		FileUtils.writeStringToFile(m_snmpConfigFile, snmpConfigContent);
		SnmpPeerFactory.setFile(m_snmpConfigFile);
		SnmpPeerFactory.init();
	}

	/**
	 * Tests if the default values are set correctly according to the default configuration file if 
	 * the SNMP version is v1.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testGetForUnknownIpSnmpV1() throws Exception {
        String url = "/snmpConfig/1.1.1.1";
        
        // Testing GET Collection
        SnmpInfo config = getXmlObject(m_jaxbContext, url, 200, SnmpInfo.class);
        SnmpInfo expectedConfig = createSnmpInfoWithDefaultsForSnmpV1();
        assertConfiguration(expectedConfig, config);
        assertSnmpV3PropertiesHaveNotBeenSet(config);
    }
	
	/**
	 * Tests if the default values are set correctly according to the default configuration file if 
	 * the SNMP version is v3.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testGetForUnknownIpSnmpV3() throws Exception {
        String url = "/snmpConfig/1.1.1.1";
        
        setSnmpConfigFile(getSnmpDefaultConfigFileForSnmpV3());
        
        // Testing GET Collection
        SnmpInfo config = getXmlObject(m_jaxbContext, url, 200, SnmpInfo.class);
		SnmpInfo expectedConfig = createSnmpInfoWithDefaultsForSnmpV3("1.1.1.1");
        assertConfiguration(expectedConfig, config); // check if expected defaults matches actual defaults
        assertSnmpV1PropertiesHaveNotBeenSet(config);
        
    }

	@Test
	public void testSetNewValueForSnmpV2c() throws Exception {
		String url = "/snmpConfig/1.1.1.1";

		// Testing GET Collection
		SnmpInfo config = getXmlObject(m_jaxbContext, url, 200, SnmpInfo.class);
		SnmpInfo expectedConfig = createSnmpInfoWithDefaultsForSnmpV3("1.1.1.1");
		assertConfiguration(expectedConfig, config); // check if expected defaults matches actual defaults
		
		// change values
		config.setAuthPassPhrase("authPassPhrase");
		config.setAuthProtocol("authProtocol");
		config.setCommunity("community");
		config.setContextEngineId("contextEngineId");
		config.setContextName("contextName");
		config.setEngineId("engineId");
		config.setEnterpriseId("enterpriseId");
		config.setMaxRepetitions(1000);
		config.setMaxVarsPerPdu(2000);
		config.setPort(3000);
		config.setPrivPassPhrase("privPassPhrase");
		config.setPrivProtocol("privProtocol");
		config.setRetries(4000);
		config.setSecurityLevel(5000);
		config.setSecurityName("securityName");
		config.setTimeout(6000);
		config.setVersion("v2c");

		// store them via REST
		putXmlObject(m_jaxbContext, url, 303, config, "/snmpConfig/1.1.1.1");

		/*
		 * 1. Check the data in file.
		 * The data in the file may be invalid (e.g. readCommunity-String in a v3-config) 
		 */
		// read data from file
		SnmpAgentConfig snmpAgentConfig = SnmpPeerFactory.getInstance().getAgentConfig(InetAddressUtils.addr("1.1.1.1"));
		assertConfiguration(config, snmpAgentConfig);
		
		/*
		 * 2. Check the data via REST. 
		 * The data via REST only returns parameters which are valid according to 
		 * the snmp version. 
		 */
		// read via REST
		SnmpInfo newConfig = getXmlObject(m_jaxbContext, url, 200, SnmpInfo.class);
		
		// check ...
		assertConfiguration(config, newConfig); // ... if Changes were made
		assertSnmpV3PropertiesHaveNotBeenSet(newConfig); // ... that no v3 parameters were set

		dumpConfig();
	}
	
	@Test
	public void testSetNewValueForSnmpV3() throws Exception {
		String url = "/snmpConfig/1.1.1.1";

		// Testing GET Collection
		SnmpInfo config = getXmlObject(m_jaxbContext, url, 200, SnmpInfo.class);
		SnmpInfo expectedConfig = createSnmpInfoWithDefaultsForSnmpV3("1.1.1.1");
		assertConfiguration(expectedConfig, config); // check if expected defaults matches actual defaults
		
		// change values
		config.setAuthPassPhrase("authPassPhrase");
		config.setAuthProtocol("authProtocol");
		config.setCommunity("community");
		config.setContextEngineId("contextEngineId");
		config.setContextName("contextName");
		config.setEngineId("engineId");
		config.setEnterpriseId("enterpriseId");
		config.setMaxRepetitions(1000);
		config.setMaxVarsPerPdu(2000);
		config.setPort(3000);
		config.setPrivPassPhrase("privPassPhrase");
		config.setPrivProtocol("privProtocol");
		config.setRetries(4000);
		config.setSecurityLevel(5000);
		config.setSecurityName("securityName");
		config.setTimeout(6000);
		config.setVersion("v3");

		// store them via REST
		putXmlObject(m_jaxbContext, url, 303, config, "/snmpConfig/1.1.1.1");

		/*
		 * 1. Check the data in file.
		 * The data in the file may be invalid (e.g. readCommunity-String in a v3-config) 
		 */
		// read data from file
		SnmpAgentConfig snmpAgentConfig = SnmpPeerFactory.getInstance().getAgentConfig(InetAddressUtils.addr("1.1.1.1"));
		assertConfiguration(config, snmpAgentConfig);
		
		/*
		 * 2. Check the data via REST. 
		 * The data via REST only returns parameters which are valid according to 
		 * the snmp version. 
		 */
		// read via REST
		SnmpInfo newConfig = getXmlObject(m_jaxbContext, url, 200, SnmpInfo.class);
		
		// check ...
		assertConfiguration(config, newConfig); // ... if Changes were made
		assertSnmpV1PropertiesHaveNotBeenSet(newConfig); // ... that no v3 parameters were set

		dumpConfig();
	}

	private void dumpConfig() throws Exception {
		IOUtils.copy(new FileInputStream(m_snmpConfigFile), System.out);
	}

	private SnmpInfo createSnmpInfoWithDefaultsForSnmpV3(final String ipAddress) {
		SnmpAgentConfig agentConfig = SnmpPeerFactory.getInstance().getAgentConfig(InetAddressUtils.addr(ipAddress));
		SnmpInfo config = new SnmpInfo(agentConfig);
		return config;
	}
	
	private SnmpInfo createSnmpInfoWithDefaultsForSnmpV1() {
		SnmpInfo config = new SnmpInfo();
		config.setPort(DEFAULT_PORT);
		config.setRetries(DEFAULT_RETRIES);
		config.setTimeout(DEFAULT_TIMEOUT);
		config.setCommunity(DEFAULT_COMMUNITY);
		config.setVersion(DEFAULT_VERSION);
		config.setMaxVarsPerPdu(DEFAULT_MAX_VARS_PER_PDU);
		config.setMaxRepetitions(DEFAULT_MAX_REPETITIONS);
		return config;
	}
	
	private void assertConfiguration(SnmpInfo configLeft, SnmpAgentConfig configRight) {
		assertEquals(configLeft.getAuthPassPhrase(), configRight.getAuthPassPhrase());
		assertEquals(configLeft.getAuthProtocol(), configRight.getAuthProtocol());
		assertEquals(configLeft.getCommunity(), configRight.getReadCommunity());
		assertEquals(configLeft.getContextEngineId(), configRight.getContextEngineId());
		assertEquals(configLeft.getContextName(), configRight.getContextEngineId());
		assertEquals(configLeft.getEngineId(), configRight.getEngineId());
		assertEquals(configLeft.getEnterpriseId(), configRight.getEnterpriseId());
		assertEquals(configLeft.getMaxRepetitions(), configRight.getMaxRepetitions());
		assertEquals(configLeft.getMaxVarsPerPdu(), configRight.getMaxVarsPerPdu());
		assertEquals(configLeft.getPort(), configRight.getPort());
		assertEquals(configLeft.getPrivPassPhrase(), configRight.getPrivPassPhrase());
		assertEquals(configLeft.getPrivProtocol(), configRight.getPrivProtocol());
		assertEquals(configLeft.getRetries(), configRight.getRetries());
		assertEquals(configLeft.getSecurityLevel(), configRight.getSecurityLevel());
		assertEquals(configLeft.getSecurityName(), configRight.getSecurityName());
		assertEquals(configLeft.getTimeout(), configRight.getTimeout());
		assertEquals(configLeft.getVersion(), configRight.getVersionAsString());
	}
	
	private void assertConfiguration(SnmpInfo expectedConfig, SnmpInfo actualConfig) {
		assertNotNull(expectedConfig);
		assertNotNull(actualConfig);
		assertEquals(expectedConfig, actualConfig);
	}
	
	/**
	 * Ensures that no SNMP v3 only parameter is set. This is necessary 
	 * so we do not have an invalid SnmpInfo object if the default version is v1 or v2c.
	 * @param config
	 */
	private void assertSnmpV3PropertiesHaveNotBeenSet(SnmpInfo config) {
		assertEquals(false, config.hasSecurityLevel());
		assertEquals(-1, config.getSecurityLevel());
		assertEquals(null, config.getSecurityName());
		assertEquals(null, config.getAuthPassPhrase());
		assertEquals(null, config.getAuthProtocol());
		assertEquals(null, config.getEngineId());
		assertEquals(null, config.getContextEngineId());
		assertEquals(null, config.getContextName());
		assertEquals(null, config.getPrivPassPhrase());
		assertEquals(null, config.getPrivProtocol());
		assertEquals(null, config.getEnterpriseId());
	}
	
	/**
	 * Ensures that no SNMP v1 only parameter is set. This is necessary 
	 * so we do not have an invalid SnmpInfo object if the default version is v3.
	 * @param config
	 */
	private void assertSnmpV1PropertiesHaveNotBeenSet(SnmpInfo config) {
		assertEquals(null, config.getCommunity()); // community String must be null !
	}
}
