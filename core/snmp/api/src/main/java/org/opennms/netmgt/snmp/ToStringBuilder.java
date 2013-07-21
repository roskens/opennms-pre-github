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
package org.opennms.netmgt.snmp;

/**
 * The Class ToStringBuilder.
 */
class ToStringBuilder {

    /** The buf. */
    final StringBuilder buf;

    /** The first. */
    boolean first = true;

    /** The finished. */
    boolean finished = false;

    /**
     * Instantiates a new to string builder.
     *
     * @param o
     *            the o
     */
    public ToStringBuilder(Object o) {
        buf = new StringBuilder(512);

        buf.append(prettyClassName(o.getClass()));
        buf.append('@');
        buf.append(String.format("%x", System.identityHashCode(o)));
        buf.append('[');

    }

    /**
     * Append.
     *
     * @param label
     *            the label
     * @param value
     *            the value
     * @return the to string builder
     */
    public ToStringBuilder append(String label, String value) {
        assertNotFinished();
        if (!first) {
            buf.append(", ");
        } else {
            first = false;
        }
        buf.append(label).append('=').append(value);
        return this;
    }

    /**
     * Append.
     *
     * @param label
     *            the label
     * @param value
     *            the value
     * @return the to string builder
     */
    public ToStringBuilder append(String label, Object value) {
        return append(label, value == null ? null : value.toString());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (!finished) {
            buf.append(']');
            finished = true;
        }

        return buf.toString();
    }

    /**
     * Assert not finished.
     */
    private void assertNotFinished() {
        if (finished) {
            throw new IllegalStateException("This builder has already been completed by calling toString");
        }
    }

    /**
     * Pretty class name.
     *
     * @param c
     *            the c
     * @return the string
     */
    private String prettyClassName(Class<?> c) {
        String className = c.getName();
        int lastDot = className.lastIndexOf('.');
        return lastDot > 0 ? className.substring(lastDot + 1) : className;
    }

}
