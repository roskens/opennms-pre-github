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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/* wrapper object to deal with old castor resources */
/**
 * The Class BasicSchedule.
 */
public class BasicSchedule {

    /** The m_name. */
    private String m_name;

    /** The m_type. */
    private String m_type;

    /** The m_times. */
    private List<Time> m_times = new ArrayList<Time>();

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        m_name = name;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return m_type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(final String type) {
        m_type = type;
    }

    /**
     * Sets the time collection.
     *
     * @param times
     *            the new time collection
     */
    public void setTimeCollection(final Collection<Time> times) {
        synchronized (m_times) {
            if (m_times == times)
                return;
            m_times.clear();
            m_times.addAll(times);
        }
    }

    /**
     * Gets the time collection.
     *
     * @return the time collection
     */
    public Collection<Time> getTimeCollection() {
        synchronized (m_times) {
            return m_times;
        }
    }

    /**
     * Enumerate time.
     *
     * @return the enumeration
     */
    public Enumeration<Time> enumerateTime() {
        synchronized (m_times) {
            return Collections.enumeration(m_times);
        }
    }

    /**
     * Gets the time count.
     *
     * @return the time count
     */
    public int getTimeCount() {
        synchronized (m_times) {
            return m_times.size();
        }
    }

    /**
     * Gets the time.
     *
     * @param index
     *            the index
     * @return the time
     */
    public Time getTime(final int index) {
        synchronized (m_times) {
            return m_times.get(index);
        }
    }
}
