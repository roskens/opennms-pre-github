/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2014 The OpenNMS Group, Inc.
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

package org.opennms.web.rest;

import java.util.Date;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.core.test.rest.AbstractSpringJerseyRestTestCase;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(OpenNMSJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations={
        "classpath:/org/opennms/web/rest/applicationContext-test.xml",
        "classpath:/META-INF/opennms/applicationContext-commonConfigs.xml",
        "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-dao.xml",
        "classpath*:/META-INF/opennms/component-service.xml",
        "classpath*:/META-INF/opennms/component-dao.xml",
        "classpath:/META-INF/opennms/applicationContext-reportingCore.xml",
        "classpath:/META-INF/opennms/applicationContext-databasePopulator.xml",
        "classpath:/org/opennms/web/svclayer/applicationContext-svclayer.xml",
        "classpath:/META-INF/opennms/applicationContext-mockEventProxy.xml",
        "classpath:/applicationContext-jersey-test.xml",
        "classpath:/META-INF/opennms/applicationContext-reporting.xml",
        "classpath:/META-INF/opennms/applicationContext-mock-usergroup.xml",
        "classpath:/META-INF/opennms/applicationContext-minimal-conf.xml",
        "file:src/main/webapp/WEB-INF/applicationContext-spring-security.xml",
        "file:src/main/webapp/WEB-INF/applicationContext-jersey.xml"
})
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase
public class TimelineRestServiceTest extends AbstractSpringJerseyRestTestCase {

    private static final Logger LOG = LoggerFactory.getLogger(TimelineRestServiceTest.class);

    private static int m_nodeCounter = 1;
    private static int m_intfCounter = 0;

    @Override
    protected void afterServletStart() throws Exception {
        MockLogAppender.setupLogging(true, "DEBUG");
        m_nodeCounter = 0;
    }

    @Ignore
    @Test
    public void testTimelineDashboard() throws Exception {
        for (int i = 0; i < 50; i++) {
            createIpInterface();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("/timeline/dashboard/");
        final Date end = new Date();
        final Date start = new Date(end.getTime() - 86400000L);
        sb.append(start.getTime()).append("/").append(end.getTime()).append("/200/100");

        // GET admin user
        String xml = sendRequest(GET, sb.toString(), 200);
        assertTrue(xml.contains(">admin<"));
    }

    @Override
    protected void createNode() throws Exception {
        m_nodeCounter++;
        String node = "<node type=\"A\" label=\"TestMachine" + m_nodeCounter + "\">"
          + "<labelSource>H</labelSource>"
          + "<sysContact>The Owner</sysContact>"
          + "<sysDescription>"
          + "Darwin TestMachine 9.4.0 Darwin Kernel Version 9.4.0: Mon Jun  9 19:30:53 PDT 2008; root:xnu-1228.5.20~1/RELEASE_I386 i386"
          + "</sysDescription>"
          + "<sysLocation>DevJam</sysLocation>"
          + "<sysName>TestMachine" + m_nodeCounter + "</sysName>"
          + "<sysObjectId>.1.3.6.1.4.1.8072.3.2.255</sysObjectId>"
          + "</node>";
        sendPost("/nodes", node, 303, null);
    }

    @Override
    protected void createIpInterface() throws Exception {
        createNode();
        String ipInterface = "<ipInterface isManaged=\"M\" snmpPrimary=\"P\">"
          + "<ipAddress>10.10.10.10</ipAddress>"
          + "<hostName>TestMachine" + m_nodeCounter + "</hostName>"
          + "</ipInterface>";
        sendPost("/nodes/" + m_nodeCounter + "/ipinterfaces", ipInterface, 303, "/nodes/" + m_nodeCounter + "/ipinterfaces/10.10.10.10");
    }

    protected void createTwoIpInterface() throws Exception {
        createNode();
        String ipInterface = "<ipInterface isManaged=\"M\" snmpPrimary=\"P\">"
          + "<ipAddress>10.10.10.10</ipAddress>"
          + "<hostName>TestMachine" + m_nodeCounter + "</hostName>"
          + "</ipInterface>";
        sendPost("/nodes/" + m_nodeCounter + "/ipinterfaces", ipInterface, 303, "/nodes/" + m_nodeCounter + "/ipinterfaces/10.10.10.10");

        String ipInterface2 = "<ipInterface isManaged=\"M\" snmpPrimary=\"P\">"
          + "<ipAddress>10.10.10.11</ipAddress>"
          + "<hostName>TestMachine" + (m_nodeCounter + 1) + "</hostName>"
          + "</ipInterface>";
        sendPost("/nodes/" + m_nodeCounter + "/ipinterfaces", ipInterface2, 303, "/nodes/" + m_nodeCounter + "/ipinterfaces/10.10.10.11");

    }

}
