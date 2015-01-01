/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013-2014 The OpenNMS Group, Inc.
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

package org.opennms.features.vaadin.mibcompiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jsmiparser.parser.SmiDefaultParser;
import org.jsmiparser.smi.SmiMib;
import org.jsmiparser.smi.SmiModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opennms.features.vaadin.mibcompiler.api.MibParser;
import org.opennms.features.vaadin.mibcompiler.services.JsmiMibParser;
import org.opennms.netmgt.xml.eventconf.Events;
import org.opennms.netmgt.xml.eventconf.Maskelement;

/**
 * The Test Class for <a href="http://issues.opennms.org/browse/SPC-592">SPC-592</a>
 *
 * @author <a href="mailto:agalue@opennms.org">Jeff Gehlbach</a>
 */
public class SPC592Test {

    /** The Constant MIB_DIR. */
    protected static final Path MIB_DIR = Paths.get("src", "test", "resources");

    /** The parser. */
    protected MibParser parser;

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        parser = new JsmiMibParser();
        parser.setMibDirectory(MIB_DIR);
    }

    /**
     * Test standard parse.
     * <p>This test is to verify that the problem is not JsmiParser.</p>
     *
     * @throws Exception the exception
     */
    @Test
    public void testStandardParse() throws Exception {
        SmiDefaultParser parser = new SmiDefaultParser();
        List<URL> inputUrls = new ArrayList<URL>();
        try {
            inputUrls.add(MIB_DIR.resolve("SNMPv2-SMI.txt").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("SNMPv2-TC.txt").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("SNMPv2-CONF.txt").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("SNMPv2-MIB.txt").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("INET-ADDRESS-MIB.txt").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("SNMP-FRAMEWORK-MIB.txt").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("IANAifType-MIB.txt").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("IF-MIB.txt").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("INTEGRATED-SERVICES-MIB.mib").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("DIFFSERV-DSCP-TC.mib").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("DIFFSERV-MIB.mib").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("ISIS-MIB.mib").toUri().toURL());
        } catch (Exception e) {
            Assert.fail();
        }
        parser.getFileParserPhase().setInputUrls(inputUrls);
        SmiMib mib = parser.parse();
        if (parser.getProblemEventHandler().isOk()) {
            Assert.assertNotNull(mib);
            boolean found = false;
            for (SmiModule m : mib.getModules()) {
                if (m.getId().equals("ISIS-MIB"))
                    found = true;
            }
            Assert.assertTrue(found);
        } else {
            Assert.fail("The ISIS-MIB.mib couldn't be compiled: " + parser.getProblemEventHandler().getTotalCount() + " problems encountered");
        }
    }

    /**
     * Test custom parse.
     *
     * @throws Exception the exception
     */
    @Test
    public void testCustomParse() throws Exception {
        if (parser.parseMib(MIB_DIR.resolve("ISIS-MIB.mib"))) {
            Assert.assertTrue(parser.getMissingDependencies().isEmpty());
            Assert.assertNull(parser.getFormattedErrors());
        } else {
            Assert.fail("The ISIS-MIB.mib.mib couldn't be compiled");
        }
    }

    /**
     * Test trap-OIDs in events from ISIS-MIB notifications.
     *
     * @throws Exception the exception
     */
    @Test
    public void testIsisMibTrapOids() throws Exception {
        if (!parser.parseMib(MIB_DIR.resolve("ISIS-MIB.mib"))) {
            Assert.fail("The ISIS-MIB.mib must parse successfully");
        } else {
            Assert.assertTrue(parser.getMissingDependencies().isEmpty());
        }
        Events isisEvents = parser.getEvents("uei.opennms.org/issues/SPC592/");
        assertEquals(isisEvents.getEventCollection().size(), 18);
        assertEquals("uei.opennms.org/issues/SPC592/isisAdjacencyChange", isisEvents.getEvent(16).getUei());

        boolean foundId = false;
        for (Maskelement me : isisEvents.getEvent(16).getMask().getMaskelementCollection()) {
            if ("id".equals(me.getMename())) {
                foundId = true;
            }
            if (foundId) {
                assertEquals("Only one me-value should be present for trap OID", 1, me.getMevalueCount());
                assertFalse("The 'id' mask-element value for the isisAdjacencyChange event must not end in .0", me.getMevalue()[0].endsWith(".0"));
                assertEquals("The 'id' mask-element value for the isisAdjacencyChange event is incorrect", me.getMevalue()[0], ".1.3.6.1.2.1.138");
                break;
            }
        }
        if (! foundId) {
            Assert.fail("Never found the 'id' mask-element in the isisAdjacencyChange event");
        }

        boolean foundSpecific = false;
        for (Maskelement me : isisEvents.getEvent(16).getMask().getMaskelementCollection()) {
            if ("specific".equals(me.getMename())) {
                foundSpecific = true;
            }
            if (foundSpecific) {
                assertEquals("Only one me-value should be present for the specific-type", 1, me.getMevalueCount());
                assertEquals("The 'specific' mask-element value for the isisAdjacencyChange event must be 17", me.getMevalue()[0], "17");
            }
        }
        if (! foundSpecific) {
            Assert.fail("Never found the 'specific' mask-element in the isisAdjacencyChange event");
        }
    }

}
