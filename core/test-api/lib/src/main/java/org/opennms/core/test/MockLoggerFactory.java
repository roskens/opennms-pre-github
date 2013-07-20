/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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
package org.opennms.core.test;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * A factory for creating MockLogger objects.
 */
public class MockLoggerFactory implements ILoggerFactory {

    /** The Constant INSTANCE. */
    static final MockLoggerFactory INSTANCE = new MockLoggerFactory();

    /** The s_appender. */
    static MockLogAppender s_appender = MockLogAppender.getInstance();

    /** The m_logger map. */
    final Map<String, Logger> m_loggerMap;

    /**
     * Instantiates a new mock logger factory.
     */
    public MockLoggerFactory() {
        m_loggerMap = new HashMap<String, Logger>();
    }

    /**
     * Sets the appender.
     *
     * @param appender
     *            the new appender
     */
    public static void setAppender(final MockLogAppender appender) {
        s_appender = appender;
    }

    /**
     * Return an appropriate {@link MockLogger} instance by name.
     *
     * @param name
     *            the name
     * @return the logger
     */
    public Logger getLogger(final String name) {
        if (s_appender == null) {
            System.err.println("WARNING: getLogger(" + name + ") called, but MockLogAppender hasn't been set up yet!");
        }
        Logger slogger = null;
        // protect against concurrent access of the loggerMap
        synchronized (this) {
            slogger = (Logger) m_loggerMap.get(name);
            if (slogger == null) {
                slogger = new MockLogger(name);
                m_loggerMap.put(name, slogger);
            }
        }
        return slogger;
    }
}
