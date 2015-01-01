/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012-2014 The OpenNMS Group, Inc.
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

/**
 * The Test Class for <a href="http://issues.opennms.org/browse/SPC-465">SPC-465</a>
 *
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
public class SPC465Test {

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
            inputUrls.add(MIB_DIR.resolve("JUNIPER-SMI.mib").toUri().toURL());
            inputUrls.add(MIB_DIR.resolve("JUNIPER-JS-SMI.mib").toUri().toURL());
        } catch (Exception e) {
            Assert.fail();
        }
        parser.getFileParserPhase().setInputUrls(inputUrls);
        SmiMib mib = parser.parse();
        if (parser.getProblemEventHandler().isOk()) {
            Assert.assertNotNull(mib);
            boolean found = false;
            for (SmiModule m : mib.getModules()) {
                if (m.getId().equals("JUNIPER-JS-SMI"))
                    found = true;
            }
            Assert.assertTrue(found);
        } else {
            Assert.fail("The JUNIPER-JS-SMI.mib couldn't be compiled");
        }
    }

    /**
     * Test custom parse.
     *
     * @throws Exception the exception
     */
    @Test
    public void testCustomParse() throws Exception {
        if (parser.parseMib(MIB_DIR.resolve("JUNIPER-JS-SMI.mib"))) {
            Assert.assertTrue(parser.getMissingDependencies().isEmpty());
            Assert.assertNull(parser.getFormattedErrors());
        } else {
            Assert.fail("The JUNIPER-JS-SMI.mib couldn't be compiled");
        }
    }

    /**
     * Test Juniper IF-MIB.
     *
     * @throws Exception the exception
     */
    @Test
    public void testJuniperIfMib() throws Exception {
        if (parser.parseMib(MIB_DIR.resolve("JUNIPER-IF-MIB.mib"))) {
            Assert.fail("The JUNIPER-IF-MIB.mib must contain problems");
        } else {
            Assert.assertFalse(parser.getMissingDependencies().isEmpty());
            Assert.assertEquals("[HCNUM-TC]", parser.getMissingDependencies().toString());
        }
    }

}
