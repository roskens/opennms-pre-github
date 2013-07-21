/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.syslogd;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * The Class SyslogRegexTest.
 */
@RunWith(Parameterized.class)
public class SyslogRegexTest {

    /** The m_count. */
    private final int m_count = 1000000;

    /** The m_match pattern. */
    private String m_matchPattern;

    /** The m_log message. */
    private String m_logMessage;

    /**
     * Data.
     *
     * @return the collection
     * @throws ParseException
     *             the parse exception
     */
    @Parameters
    public static Collection<Object[]> data() throws ParseException {
        return Arrays.asList(new Object[][] {
                { "\\s(19|20)\\d\\d([-/.])(0[1-9]|1[012])\\2(0[1-9]|[12][0-9]|3[01])(\\s+)(\\S+)(\\s)(\\S.+)",
                        "<6>main: 2010-08-19 localhost foo23: load test 23 on tty1" },
                { "\\s(19|20)\\d\\d([-/.])(0[1-9]|1[012])\\2(0[1-9]|[12][0-9]|3[01])(\\s+)(\\S+)(\\s)(\\S.+)",
                        "<6>main: 2010-08-01 localhost foo23: load test 23 on tty1" },
                { "foo0: .*load test (\\S+) on ((pts\\/\\d+)|(tty\\d+))",
                        "<6>main: 2010-08-19 localhost foo23: load test 23 on tty1" },
                { "foo23: .*load test (\\S+) on ((pts\\/\\d+)|(tty\\d+))",
                        "<6>main: 2010-08-19 localhost foo23: load test 23 on tty1" },
                { "1997", "<6>main: 2010-08-19 localhost foo23: load test 23 on tty1" } });
    }

    /**
     * Instantiates a new syslog regex test.
     *
     * @param matchPattern
     *            the match pattern
     * @param logMessage
     *            the log message
     */
    public SyslogRegexTest(final String matchPattern, final String logMessage) {
        m_matchPattern = matchPattern;
        m_logMessage = logMessage;
        System.err.println("=== " + m_matchPattern + " ===");
    }

    /**
     * Test regex.
     */
    @Test
    @Ignore
    public void testRegex() {
        String logMessage = m_logMessage;
        String matchPattern = m_matchPattern;
        tryPattern(logMessage, matchPattern);
    }

    /**
     * Try pattern.
     *
     * @param logMessage
     *            the log message
     * @param matchPattern
     *            the match pattern
     */
    private void tryPattern(String logMessage, String matchPattern) {
        Pattern pattern = Pattern.compile(matchPattern, Pattern.MULTILINE);
        long start, end;
        boolean matches = false;

        start = System.currentTimeMillis();
        for (int i = 0; i < m_count; i++) {
            Matcher m = pattern.matcher(logMessage);
            matches = m.matches();
        }
        end = System.currentTimeMillis();
        printSpeed("matches = " + matches, start, end);

        start = System.currentTimeMillis();
        for (int i = 0; i < m_count; i++) {
            Matcher m = pattern.matcher(logMessage);
            matches = m.find();
        }
        end = System.currentTimeMillis();
        printSpeed("find = " + matches, start, end);

        pattern = Pattern.compile(".*" + m_matchPattern + ".*");
        start = System.currentTimeMillis();
        for (int i = 0; i < m_count; i++) {
            Matcher m = pattern.matcher(logMessage);
            matches = m.matches();
        }
        end = System.currentTimeMillis();
        printSpeed("matches (.* at beginning and end) = " + matches, start, end);

        start = System.currentTimeMillis();
        for (int i = 0; i < m_count; i++) {
            Matcher m = pattern.matcher(logMessage);
            matches = m.find();
        }
        end = System.currentTimeMillis();
        printSpeed("find (.* at beginning and end) = " + matches, start, end);

        pattern = Pattern.compile("^.*" + m_matchPattern + ".*$");
        start = System.currentTimeMillis();
        for (int i = 0; i < m_count; i++) {
            Matcher m = pattern.matcher(logMessage);
            matches = m.matches();
        }
        end = System.currentTimeMillis();
        printSpeed("matches (^.* at beginning, .*$ at end) = " + matches, start, end);

        start = System.currentTimeMillis();
        for (int i = 0; i < m_count; i++) {
            Matcher m = pattern.matcher(logMessage);
            matches = m.find();
        }
        end = System.currentTimeMillis();
        printSpeed("find (^.* at beginning, .*$ at end) = " + matches, start, end);
    }

    /**
     * Prints the speed.
     *
     * @param message
     *            the message
     * @param start
     *            the start
     * @param end
     *            the end
     */
    private void printSpeed(final String message, long start, long end) {
        System.err.println(String.format("%s: total time: %d, number per second: %8.4f", message, (end - start),
                                         (m_count * 1000.0 / (end - start))));
    }

}
