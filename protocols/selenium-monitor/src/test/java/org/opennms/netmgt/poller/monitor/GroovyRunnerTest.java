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

package org.opennms.netmgt.poller.monitor;

import static org.junit.Assert.assertEquals;
import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Test;
import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.opennms.netmgt.junit.runner.SeleniumComputer;

/**
 * The Class GroovyRunnerTest.
 */
public class GroovyRunnerTest {

    /**
     * Test groovy class loader fail constructor error.
     *
     * @throws CompilationFailedException
     *             the compilation failed exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGroovyClassLoaderFailConstructorError() throws CompilationFailedException, IOException {
        String filename = "src/test/resources/groovy/SeleniumGroovyTest.groovy";
        Result result = runJUnitTests(getGroovyClass(filename));
        assertEquals(1, result.getFailureCount());
    }

    /**
     * Test annotated groovy class with base url.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testAnnotatedGroovyClassWithBaseUrl() throws IOException {
        String filename = "src/test/resources/groovy/AnnotatedGroovyTest.groovy";
        Result result = runJUnitTests(getGroovyClass(filename));

        assertEquals(0, result.getFailureCount());
    }

    /**
     * Test custom j unit runner with computer.
     *
     * @throws CompilationFailedException
     *             the compilation failed exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testCustomJUnitRunnerWithComputer() throws CompilationFailedException, IOException {
        String filename = "src/test/resources/groovy/GroovyRunnerTest.groovy";

        Computer computer = new SeleniumComputer("http://www.papajohns.co.uk");

        Result result = runJunitTestWithComputer(computer, getGroovyClass(filename));

        assertEquals(0, result.getFailureCount());
    }

    /**
     * Run junit test with computer.
     *
     * @param computer
     *            the computer
     * @param clazz
     *            the clazz
     * @return the result
     */
    private Result runJunitTestWithComputer(Computer computer, Class<?> clazz) {
        Result result = JUnitCore.runClasses(computer, clazz);

        List<Failure> failures = result.getFailures();
        for (Failure failure : failures) {
            System.out.println(failure.getMessage());
        }
        return result;
    }

    /**
     * Gets the groovy class.
     *
     * @param filename
     *            the filename
     * @return the groovy class
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private Class<?> getGroovyClass(String filename) throws IOException {
        GroovyClassLoader gcl = new GroovyClassLoader();
        Class<?> clazz = gcl.parseClass(new File(filename));
        return clazz;
    }

    /**
     * Run j unit tests.
     *
     * @param clazz
     *            the clazz
     * @return the result
     */
    private Result runJUnitTests(Class<?> clazz) {
        Result result = JUnitCore.runClasses(clazz);
        List<Failure> failures = result.getFailures();
        for (Failure failure : failures) {
            System.out.println(failure.getMessage());
        }
        return result;
    }
}
