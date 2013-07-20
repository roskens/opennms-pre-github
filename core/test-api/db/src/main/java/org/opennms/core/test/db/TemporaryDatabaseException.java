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
package org.opennms.core.test.db;

/**
 * The Class TemporaryDatabaseException.
 */
public class TemporaryDatabaseException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6365926071506893518L;

    /**
     * Instantiates a new temporary database exception.
     */
    public TemporaryDatabaseException() {
        super();
    }

    /**
     * Instantiates a new temporary database exception.
     *
     * @param message
     *            the message
     */
    public TemporaryDatabaseException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new temporary database exception.
     *
     * @param cause
     *            the cause
     */
    public TemporaryDatabaseException(final Throwable cause) {
        super(cause);
    }

    /**
     * Instantiates a new temporary database exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public TemporaryDatabaseException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
