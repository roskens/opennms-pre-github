/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.linkd;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.utils.LogUtils;
import org.opennms.mock.snmp.JUnitSnmpAgent;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.db.JUnitConfigurationEnvironment;
import org.opennms.netmgt.dao.db.JUnitTemporaryDatabase;
import org.opennms.netmgt.model.NetworkBuilder;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.test.mock.MockLogAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations= {
		"classpath:/META-INF/opennms/applicationContext-dao.xml",
        "classpath:/META-INF/opennms/applicationContext-commonConfigs.xml",
		"classpath:/META-INF/opennms/applicationContext-daemon.xml",
		"classpath:/META-INF/opennms/applicationContext-proxy-snmp.xml",
		"classpath:/META-INF/opennms/mockEventIpcManager.xml",
		"classpath:/META-INF/opennms/applicationContext-databasePopulator.xml",
		"classpath:/META-INF/opennms/applicationContext-setupIpLike-enabled.xml",
		"classpath:/META-INF/opennms/applicationContext-linkd.xml",
		"classpath:/applicationContext-minimal-conf.xml"
})
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase
public class LinkdTest {

    @Autowired
	private Linkd m_linkd;

	@Autowired
	private NodeDao m_nodeDao;

	@Autowired
	private TransactionTemplate m_transactionTemplate;
	
	@Before
	public void setUp() throws Exception {
		// Use the mock.logLevel system property to control the log level
		MockLogAppender.setupLogging(true);

		NetworkBuilder nb = new NetworkBuilder();
		nb.addNode("test.example.com").setForeignSource("linkd").setForeignId("1").setSysObjectId(".1.3.6.1.4.1.1724.81").setType("A");
		nb.addInterface("192.168.1.10").setIsSnmpPrimary("P").setIsManaged("M");
		m_nodeDao.save(nb.getCurrentNode());
		m_nodeDao.flush();

		// Make sure that the localhost SNMP connection config factory has overridden
		// the normal config factory
		///assertTrue(m_adapter.getSnmpPeerFactory() instanceof ProxySnmpAgentConfigFactory);
	}

	@Test
	@Transactional
	@JUnitSnmpAgent(resource = "/westell-smartjack.properties")
	public void testWestellSmartjackLldp() throws Exception {

		final OnmsNode node = m_nodeDao.findByForeignId("linkd", "1");
		LogUtils.debugf(this, "node = %s, primary interface = %s", node, node.getPrimaryInterface());

		m_transactionTemplate.execute(new TransactionCallback<Object>() {

			@Override
			public Object doInTransaction(final TransactionStatus status) {
				assertTrue(m_linkd.scheduleNodeCollection(node.getId()));
				return null;
			}
			
		});

//		Thread.sleep(60000);

	}
}
