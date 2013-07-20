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

import org.slf4j.spi.LocationAwareLogger;

/**
 * The Enum Level.
 */
public enum Level {

    /** The trace. */
    TRACE(LocationAwareLogger.TRACE_INT),
 /** The debug. */
 DEBUG(LocationAwareLogger.DEBUG_INT),
 /** The info. */
 INFO(LocationAwareLogger.INFO_INT),
 /** The warn. */
 WARN(
            LocationAwareLogger.WARN_INT),
 /** The error. */
 ERROR(LocationAwareLogger.ERROR_INT),
 /** The fatal. */
 FATAL(
            LocationAwareLogger.ERROR_INT + 10);

    /** The m_code. */
    private int m_code;

    /**
     * Instantiates a new level.
     *
     * @param code
     *            the code
     */
    private Level(final int code) {
        m_code = code;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public int getCode() {
        return m_code;
    }

    /**
     * From code.
     *
     * @param code
     *            the code
     * @return the level
     */
    public static Level fromCode(final int code) {
        for (final Level l : Level.values()) {
            if (l.getCode() == code) {
                return l;
            }
        }
        return null;
    }

    /**
     * Gt.
     *
     * @param level
     *            the level
     * @return true, if successful
     */
    public boolean gt(final Level level) {
        return getCode() > level.getCode();
    }

    /**
     * Ge.
     *
     * @param level
     *            the level
     * @return true, if successful
     */
    public boolean ge(final Level level) {
        return getCode() >= level.getCode();
    }

    /**
     * Lt.
     *
     * @param level
     *            the level
     * @return true, if successful
     */
    public boolean lt(final Level level) {
        return getCode() < level.getCode();
    }

    /**
     * Le.
     *
     * @param level
     *            the level
     * @return true, if successful
     */
    public boolean le(final Level level) {
        return getCode() <= level.getCode();
    }

    /**
     * Eq.
     *
     * @param level
     *            the level
     * @return true, if successful
     */
    public boolean eq(final Level level) {
        return getCode() == level.getCode();
    }
}
