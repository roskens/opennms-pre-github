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

package org.opennms.netmgt.collectd.vmware.vijava;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The Class VmwarePerformanceValues.
 */
public class VmwarePerformanceValues {

    /** The multi values. */
    private Map<String, Map<String, Long>> multiValues = new HashMap<String, Map<String, Long>>();

    /** The single values. */
    private Map<String, Long> singleValues = new HashMap<String, Long>();

    /**
     * Instantiates a new vmware performance values.
     */
    public VmwarePerformanceValues() {
    }

    /**
     * Adds the value.
     *
     * @param name
     *            the name
     * @param instance
     *            the instance
     * @param value
     *            the value
     */
    public void addValue(String name, String instance, long value) {
        if (!multiValues.containsKey(name)) {
            multiValues.put(name, new HashMap<String, Long>());
        }

        Map<String, Long> map = multiValues.get(name);

        map.put(instance, Long.valueOf(value));
    }

    /**
     * Adds the value.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void addValue(String name, long value) {
        singleValues.put(name, Long.valueOf(value));
    }

    /**
     * Checks for instances.
     *
     * @param name
     *            the name
     * @return true, if successful
     */
    public boolean hasInstances(String name) {
        return (multiValues.containsKey(name));
    }

    /**
     * Checks for single value.
     *
     * @param name
     *            the name
     * @return true, if successful
     */
    public boolean hasSingleValue(String name) {
        return (singleValues.containsKey(name));
    }

    /**
     * Gets the instances.
     *
     * @param name
     *            the name
     * @return the instances
     */
    public Set<String> getInstances(String name) {
        if (multiValues.containsKey(name)) {
            return multiValues.get(name).keySet();
        } else {
            return null;
        }
    }

    /**
     * Gets the value.
     *
     * @param name
     *            the name
     * @return the value
     */
    public Long getValue(String name) {
        return singleValues.get(name);
    }

    /**
     * Gets the value.
     *
     * @param name
     *            the name
     * @param instance
     *            the instance
     * @return the value
     */
    public Long getValue(String name, String instance) {
        if (multiValues.containsKey(name)) {
            return multiValues.get(name).get(instance);
        } else {
            return null;
        }
    }
}
