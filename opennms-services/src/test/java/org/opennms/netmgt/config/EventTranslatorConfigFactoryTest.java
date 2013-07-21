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

package org.opennms.netmgt.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.opennms.core.test.MockLogAppender;
import org.opennms.netmgt.dao.mock.EventAnticipator;
import org.opennms.netmgt.dao.mock.MockEventIpcManager;
import org.opennms.netmgt.mock.OpenNMSTestCase;
import org.opennms.netmgt.mock.OutageAnticipator;
import org.opennms.netmgt.translator.EventTranslator;

/**
 * The Class EventTranslatorConfigFactoryTest.
 *
 * @author mhuot
 */
public class EventTranslatorConfigFactoryTest extends OpenNMSTestCase {

    /** The m_translator. */
    private EventTranslator m_translator;

    /** The m_event mgr. */
    private MockEventIpcManager m_eventMgr;

    /** The m_passive status configuration. */
    private String m_passiveStatusConfiguration = getStandardConfig();

    /** The m_config. */
    private EventTranslatorConfigFactory m_config;

    /** The m_anticipator. */
    private EventAnticipator m_anticipator;

    /** The m_outage anticipator. */
    private OutageAnticipator m_outageAnticipator;

    /*
     * @see TestCase#setUp()
     */
    /* (non-Javadoc)
     * @see org.opennms.netmgt.mock.OpenNMSTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockLogAppender.setupLogging();

        createAnticipators();

        m_eventMgr = new MockEventIpcManager();
        m_eventMgr.setEventWriter(m_db);
        m_eventMgr.setEventAnticipator(m_anticipator);
        m_eventMgr.addEventListener(m_outageAnticipator);
        m_eventMgr.setSynchronous(true);

        InputStream rdr = new ByteArrayInputStream(m_passiveStatusConfiguration.getBytes("UTF-8"));
        m_config = new EventTranslatorConfigFactory(rdr, m_db);
        EventTranslatorConfigFactory.setInstance(m_config);

        m_translator = EventTranslator.getInstance();
        m_translator.setEventManager(m_eventMgr);
        m_translator.setConfig(EventTranslatorConfigFactory.getInstance());
        m_translator.setDataSource(m_db);

        m_translator.init();
        m_translator.start();

    }

    /*
     * @see TestCase#tearDown()
     */
    /* (non-Javadoc)
     * @see org.opennms.netmgt.mock.OpenNMSTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        MockLogAppender.assertNoWarningsOrGreater();

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.mock.OpenNMSTestCase#testDoNothing()
     */
    @Override
    public void testDoNothing() {
        // FIXME: This is because the below test is commented out
    }

    /**
     * Creates the anticipators.
     */
    private void createAnticipators() {
        m_anticipator = new EventAnticipator();
        m_outageAnticipator = new OutageAnticipator(m_db);
    }

    /**
     * Gets the standard config.
     *
     * @return the standard config
     */
    private String getStandardConfig() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<event-translator-configuration \n"
                + "xmlns=\"http://xmlns.opennms.org/xsd/translator-configuration\" \n"
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" >\n"
                + "  <translation>\n"
                + "   <event-translation-spec uei=\"uei.opennms.org/services/translationTest\">\n"
                + "      <mappings>\n"
                + "        <mapping>\n"
                + "          <assignment type=\"field\" name=\"nodeid\">\n"
                + "            <value type=\"sql\" result=\"select node.nodeid from node, ipInterface where node.nodeLabel=? and ipinterface.ipaddr=? and node.nodeId=ipinterface.nodeid and ipInterface.isManaged != 'D' and node.nodeType != 'D'\" >\n"
                + "				<value type=\"parameter\" name=\"passiveNodeLabel\" matches=\"Router\" result=\"Firewall\" />\n"
                + "				<value type=\"constant\" result=\"192.168.1.4\" />\n"
                + "			</value>\n"
                + "          </assignment>\n"
                + "          <assignment type=\"parameter\" name=\"nodeLabel\">\n"
                + "            <value type=\"field\" name=\"host\" result=\"Switch\" />\n"
                + "          </assignment>\n"
                + "          <assignment type=\"field\" name=\"interface\">\n"
                + "            <value type=\"parameter\" name=\"passiveIpAddr\" matches=\".*(192\\.168\\.1\\.1).*\" result=\"192.168.1.1\" />\n"
                + "          </assignment>\n"
                + "		  <assignment type=\"field\" name=\"host\">\n"
                + "			<value type=\"field\" name=\"host\" result=\"www.opennms.org\" />\n"
                + "		  </assignment>\n"
                + "		  <assignment type=\"field\" name=\"descr\">\n"
                + "			<value type=\"constant\" result=\"a generated event\" />\n"
                + "		  </assignment>\n"
                + "          <assignment type=\"field\" name=\"service\">\n"
                + "            <value type=\"parameter\" name=\"passiveServiceName\" result=\"PSV\" />\n"
                + "          </assignment>\n"
                + "          <assignment type=\"parameter\" name=\"passiveStatus\">\n"
                + "            <value type=\"parameter\" name=\"passiveStatus\" matches=\".*(Up|Down).*\" result=\"${1}\" />\n"
                + "          </assignment>\n" + "        </mapping>\n" + "      </mappings>\n"
                + "    </event-translation-spec>\n" + "  </translation>\n" + "</event-translator-configuration>\n" + "";
    }
}
