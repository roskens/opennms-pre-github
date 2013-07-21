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

package org.opennms.netmgt.config;

/* wrapper object to deal with old castor resources */
/**
 * The Class Time.
 */
public class Time {

    /** The m_id. */
    private String m_id;

    /** The m_day. */
    private String m_day;

    /** The m_begins. */
    private String m_begins;

    /** The m_ends. */
    private String m_ends;

    /**
     * Instantiates a new time.
     */
    public Time() {
    }

    /**
     * Instantiates a new time.
     *
     * @param id
     *            the id
     * @param day
     *            the day
     * @param begins
     *            the begins
     * @param ends
     *            the ends
     */
    public Time(final String id, final String day, final String begins, final String ends) {
        m_id = id;
        m_day = day;
        m_begins = begins;
        m_ends = ends;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return m_id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(final String id) {
        m_id = id;
    }

    /**
     * Gets the day.
     *
     * @return the day
     */
    public String getDay() {
        return m_day;
    }

    /**
     * Sets the day.
     *
     * @param day
     *            the new day
     */
    public void setDay(final String day) {
        m_day = day;
    }

    /**
     * Gets the begins.
     *
     * @return the begins
     */
    public String getBegins() {
        return m_begins;
    }

    /**
     * Sets the begins.
     *
     * @param begins
     *            the new begins
     */
    public void setBegins(final String begins) {
        m_begins = begins;
    }

    /**
     * Gets the ends.
     *
     * @return the ends
     */
    public String getEnds() {
        return m_ends;
    }

    /**
     * Sets the ends.
     *
     * @param ends
     *            the new ends
     */
    public void setEnds(final String ends) {
        m_ends = ends;
    }
}
