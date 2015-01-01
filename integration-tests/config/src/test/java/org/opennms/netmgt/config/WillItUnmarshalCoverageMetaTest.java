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

package org.opennms.netmgt.config;

import static org.junit.Assert.assertTrue;
import static org.opennms.core.test.ConfigurationTestUtils.getDaemonEtcDirectory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This is a meta test, testing the coverage of {@link WillItUnmarshalTest}.
 *
 * The {@link WillItUnmarshalTest} uses parameterized tests to check
 * unmarshalling of provided config files. To ensure the checked list of files
 * covers all files in the example directories, this test fetches the file list
 * of the directories to test and checks them against the defined files to test.
 *
 * @author Dustin Frisch<fooker@lab.sh>
 */
@RunWith(value = Parameterized.class)
public class WillItUnmarshalCoverageMetaTest {
    private static final Logger LOG = LoggerFactory.getLogger(WillItUnmarshalCoverageMetaTest.class);

    /**
     * A set of test parameters to execute.
     *
     * See {@link #files()} for detailed information.
     */
    private static SortedSet<String> FILES = new TreeSet<String>();

    /**
     * Adds all .xml files in the given director to the list of files to test
     * coverage for.
     *
     * @param directory the directory to scan for .xml files
     *
     * @param recursive iff true, the directory is scanned recursively
     * @throws IOException
     */
    private static void addDirectory(final Path directory, final boolean recursive) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory);) {
            for (Path path : stream) {
                if (Files.isDirectory(path) && recursive) {
                    addDirectory(path, recursive);
                } else {
                    FILES.add(path.toString());
                }
            }
        } catch (IOException ex) {
        }
    }

    /**
     * Ignores a file in the coverage test by removing it from the list of files
     * to check coverage for.
     *
     * @param file the file to ignore
     */
    private static void ignoreFile(final Path path) {
        assert FILES.remove(path);
    }

    static {
        addDirectory(getDaemonEtcDirectory(), true);

        LOG.debug("FILES.size() = {}", FILES.size());
        final Path droolsDirectory = getDaemonEtcDirectory().toAbsolutePath().resolve("examples").resolve("drools-engine.d").resolve("nodeParentRules");
        System.err.println("drools directory = " + droolsDirectory);
        ignoreFile(droolsDirectory.resolve("drools-engine.xml"));
        ignoreFile(droolsDirectory.resolve("nodeParentRules-context.xml"));
        ignoreFile(droolsDirectory.resolve("locationMonitorRules-context.xml"));
        ignoreFile(getDaemonEtcDirectory().resolve("examples/nsclient-config.xml"));

        ignoreFile(getDaemonEtcDirectory().resolve("syslog/ApacheHTTPD.syslog.xml"));
        ignoreFile(getDaemonEtcDirectory().resolve("syslog/LinuxKernel.syslog.xml"));
        ignoreFile(getDaemonEtcDirectory().resolve("syslog/OpenSSH.syslog.xml"));
        ignoreFile(getDaemonEtcDirectory().resolve("syslog/Procmail.syslog.xml"));
        ignoreFile(getDaemonEtcDirectory().resolve("syslog/POSIX.syslog.xml"));
        ignoreFile(getDaemonEtcDirectory().resolve("syslog/Postfix.syslog.xml"));
        ignoreFile(getDaemonEtcDirectory().resolve("syslog/Sudo.syslog.xml"));
        ignoreFile(getDaemonEtcDirectory().resolve("log4j2.xml"));
        LOG.debug("FILES.size() = {}", FILES.size());
    }

    /**
     * Returns the list of files to check coverage for.
     *
     * The returned file list is stored in {@link #FILES} which is filled in the
     * static constructor.
     *
     * For each XML file to test, this method must return an entry in the list.
     * Each entry consists of the following parts:
     * <ul>
     *   <li>The {@link File} to check coverage for</li>
     * </ul>
     *
     * @return list of parameters for the test
     */
    @Parameterized.Parameters
    public static Collection<Object[]> files() {
        ImmutableList<Object[]> list = ImmutableList.copyOf(Collections2.transform(FILES, new Function<String, Object[]>() {
            @Override
            public Object[] apply(final String file) {
                return new Object[] {file};
            }
        }));
        return list;
    }

    /**
     * The set of files covered by the {@link WillItUnmarshalTest}.
     */
    final static Set<Path> COVERED_FILES = new HashSet<Path>();

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Get the constructor of test to create the instances - we can assume
        // that there is only one constructor as JUnit requires it.
        @SuppressWarnings("unchecked")
        final Constructor<WillItUnmarshalTest> constructor = (Constructor<WillItUnmarshalTest>) WillItUnmarshalTest.class.getConstructors()[0];

        // Build set of covered files
        for (final Object[] parameters : WillItUnmarshalTest.files()) {
            // Create instance of test
            final WillItUnmarshalTest test = constructor.newInstance(parameters);

            // Get the file for the resource used by the test instance and add
            // it to the set of covered files
            COVERED_FILES.add(Paths.get(test.createResource().getURI()));
        }
    }

    /**
     * The file to test coverage for.
     */
    private final Path path;

    public WillItUnmarshalCoverageMetaTest(final String path) {
        this.path = Paths.get(path);
    }

    /**
     * Tests if the current file is in the set of covered files.
     *
     * If this test fails, you should add an entry for the uncovered file to
     * {@link WillItUnmarshalTest#FILES} in the static constructor of
     * {@link WillItUnmarshalTest} or mark the file as ignored by calling
     * {@link #ignoreFile(java.io.File)} in the static constructor of
     * {@link WillItUnmarshalMetaTest}.
     */
    @Test
    public void testCoverage() {
        // Check if the file is in the set of covvered files
        assertTrue("File is not covered: " + path, COVERED_FILES.contains(path));
    }
}
