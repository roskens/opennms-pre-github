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
package org.opennms.core.logging;

import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.MDC;

/**
 * The Class Logging.
 */
@SuppressWarnings("rawtypes")
public class Logging {

    /** The Constant PREFIX_KEY. */
    public static final String PREFIX_KEY = "prefix";

    /**
     * With prefix.
     *
     * @param <T>
     *            the generic type
     * @param prefix
     *            the prefix
     * @param callable
     *            the callable
     * @return the t
     * @throws Exception
     *             the exception
     */
    public static <T> T withPrefix(final String prefix, final Callable<T> callable) throws Exception {
        final Map mdc = Logging.getCopyOfContextMap();
        try {
            Logging.putPrefix(prefix);
            return callable.call();
        } finally {
            Logging.setContextMap(mdc);
        }

    }

    /**
     * Gets the copy of context map.
     *
     * @return the copy of context map
     */
    public static Map getCopyOfContextMap() {
        return MDC.getCopyOfContextMap();
    }

    /**
     * Sets the context map.
     *
     * @param mdc
     *            the new context map
     */
    public static void setContextMap(final Map mdc) {
        if (mdc == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(mdc);
        }
    }

    /**
     * With prefix.
     *
     * @param prefix
     *            the prefix
     * @param runnable
     *            the runnable
     */
    public static void withPrefix(final String prefix, final Runnable runnable) {
        final Map mdc = Logging.getCopyOfContextMap();
        try {
            Logging.putPrefix(prefix);
            runnable.run();
        } finally {
            Logging.setContextMap(mdc);
        }
    }

    /**
     * Put prefix.
     *
     * @param name
     *            the name
     */
    public static void putPrefix(final String name) {
        MDC.put(PREFIX_KEY, name);
    }

}
