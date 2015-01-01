/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.rrd.rrdtool;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.MockLogAppender;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

/**
 * Unit tests for the JniRrdStrategy.  This requires that the shared object
 * for JNI rrdtool support can be found and linked (see findJrrdLibrary).
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({})
public class JniRrdStrategyTest {

    private JniRrdStrategy m_strategy;

    @Before
    public void setUp() throws Exception {
        MockLogAppender.setupLogging();
        String rrdLib = System.getProperty("opennms.library.jrrd");
        if (rrdLib != null && !rrdLib.equals("${opennms.library.jrrd}")) {
            Path libFile = Paths.get(rrdLib);
            if (Files.exists(libFile)) {
                m_strategy = new JniRrdStrategy();
            } else {
                throw new NoSuchFileException(rrdLib);
            }
        } else {
            System.err.println("System property 'opennms.library.jrrd' not set: skipping tests");
        }
    }

    @Test
    public void testInitialize() {
    }

    @Test
    public void testGraph() throws Exception {
        if (m_strategy != null) {
            String rrdtoolBin = System.getProperty("install.rrdtool.bin");
            if (rrdtoolBin != null) {
                Path rrdtoolFile = Paths.get(rrdtoolBin);
                if (!Files.exists(rrdtoolFile)) {
                    System.err.println(rrdtoolBin + " does not exist");
                    return;
                }
            } else {
                System.err.println("System property 'install.rrdtool.bin' not set: skipping test");
                return;
            }

            long end = System.currentTimeMillis();
            long start = end - (24 * 60 * 60 * 1000);
            String[] command = new String[] {
                    rrdtoolBin,
                    "graph",
                    "-",
                    "--start=" + start,
                    "--end=" + end,
                    "COMMENT:test"
            };

            m_strategy.createGraph(StringUtils.arrayToDelimitedString(command, " "), Paths.get(rrdtoolBin).getParent());
        }
    }
}
