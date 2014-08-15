/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUNumber ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R). If not, see:
 * http://www.gnu.org/licenses/
 *
 * For more information contact:
 * OpenNMS(R) Licensing <license@opennms.org>
 * http://www.opennms.org/
 * http://www.opennms.com/
 *******************************************************************************/
package org.opennms.netmgt.collection.support;

import org.joda.time.DateTime;
import org.opennms.netmgt.collection.api.Resource;
import org.opennms.netmgt.collection.api.Sample;

/**
 *
 * @author roskens
 * @param <Number>
 */
public abstract class AbstractSample<Number> implements Sample<Number> {

    private final DateTime m_timestamp;
    private final Resource m_resource;
    private final String m_name;
    private final Number m_value;

    public AbstractSample(final DateTime timestamp, final Resource resource, final String name, final Number value) {
        m_timestamp = timestamp;
        m_resource = resource;
        m_name = name;
        m_value = value;
    }

    public AbstractSample(final long timestamp, final Resource resource, final String name, final Number value) {
        this(new DateTime(timestamp), resource, name, value);
    }

    @Override
    public DateTime getTimestamp() {
        return m_timestamp;
    }

    @Override
    public Resource getResource() {
        return m_resource;
    }

    @Override
    public String getName() {
        return m_name;
    }

    @Override
    public Number getValue() {
        return m_value;
    }

    @Override
    public String getValueAsString() {
        return m_value.toString();
    }

    @Override
    public String resolvePath() {
        return getResource().resolvePath() + "." + escape(getName());
    }

    private String escape(String in) {
        return in.replaceAll("(?<!\\\\)\\.", "\\\\.");
    }

    private String unescape(String in) {
        return in.replaceAll("\\\\.", ".");
    }
}
