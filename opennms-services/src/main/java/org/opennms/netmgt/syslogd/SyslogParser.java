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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SyslogParser.
 */
public class SyslogParser {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(SyslogParser.class);

    /** The m_pattern. */
    private static Pattern m_pattern = Pattern.compile("^.*$");

    /** The m_matcher. */
    private Matcher m_matcher = null;

    /** The m_text. */
    private final String m_text;

    /** The m_found. */
    private Boolean m_found = null;

    /** The m_matched. */
    private Boolean m_matched = null;

    /** The m_trace enabled. */
    private boolean m_traceEnabled = false;

    /**
     * Instantiates a new syslog parser.
     *
     * @param text
     *            the text
     */
    protected SyslogParser(final String text) {
        m_text = text;
        m_traceEnabled = LOG.isTraceEnabled();
    }

    /**
     * Find.
     *
     * @return true, if successful
     */
    public boolean find() {
        if (m_found == null) {
            getMatcher().reset();
            m_found = getMatcher().find();
        }
        return m_found;
    }

    /**
     * Matches.
     *
     * @return true, if successful
     */
    public boolean matches() {
        if (m_matched == null) {
            getMatcher().reset();
            m_matched = getMatcher().matches();
        }
        return m_matched;
    }

    /**
     * Matched.
     *
     * @return the boolean
     */
    protected Boolean matched() {
        return m_matched;
    }

    /**
     * Gets the text.
     *
     * @return the text
     */
    protected String getText() {
        return m_text;
    }

    /**
     * Trace enabled.
     *
     * @return true, if successful
     */
    protected boolean traceEnabled() {
        return m_traceEnabled;
    }

    /* override this to return your own class */
    /**
     * Gets the parser.
     *
     * @param text
     *            the text
     * @return the parser
     * @throws SyslogParserException
     *             the syslog parser exception
     */
    public static SyslogParser getParser(final String text) throws SyslogParserException {
        throw new UnsupportedOperationException("You must implement getParser() in your subclass!");
    }

    /* override this to get your custom pattern */
    /**
     * Gets the pattern.
     *
     * @return the pattern
     */
    protected Pattern getPattern() {
        return m_pattern;
    }

    /* override this to parse data from the matcher */
    /**
     * Parses the.
     *
     * @return the syslog message
     * @throws SyslogParserException
     *             the syslog parser exception
     */
    public SyslogMessage parse() throws SyslogParserException {
        final SyslogMessage message = new SyslogMessage();
        message.setMessage(getMatcher().group().trim());
        return message;
    }

    /**
     * Gets the matcher.
     *
     * @return the matcher
     */
    protected Matcher getMatcher() {
        if (m_matcher == null) {
            m_matcher = getPattern().matcher(m_text);
        }
        return m_matcher;
    }

    /**
     * Parses the date.
     *
     * @param dateString
     *            the date string
     * @return the date
     */
    protected Date parseDate(final String dateString) {
        Date date;
        try {
            final DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = df.parse(dateString);
        } catch (final Exception e) {
            try {
                final DateFormat df = new SimpleDateFormat("MMM d HH:mm:ss", Locale.ROOT);
                df.setTimeZone(TimeZone.getTimeZone("UTC"));

                // Ugh, what's a non-lame way of forcing it to parse to
                // "this year"?
                date = df.parse(dateString);
                final Calendar c = df.getCalendar();
                c.setTime(date);
                c.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
                date = c.getTime();
            } catch (final Exception e2) {
                LOG.debug("Unable to parse date '{}'", dateString, e2);
                date = null;
            }
        }
        return date;
    }

}
