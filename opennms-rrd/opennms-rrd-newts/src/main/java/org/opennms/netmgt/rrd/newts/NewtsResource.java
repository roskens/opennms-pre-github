/******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 ******************************************************************************/
package org.opennms.netmgt.rrd.newts;

import java.io.File;
import java.util.Map;
import org.opennms.netmgt.rrd.RrdUtils;
import org.opennms.newts.api.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author roskens
 */
public class NewtsResource {

    private static final Logger LOG = LoggerFactory.getLogger(NewtsResource.class);

    private final Resource m_resource;
    private final int m_step;
    private final Map<String, String> m_properties;

    public NewtsResource(final String directory, final String fileName) {
        String resourceName = fileName;
        if (fileName.endsWith(RrdUtils.getExtension())) {
            resourceName = resourceName.substring(0, resourceName.length() - RrdUtils.getExtension().length());
        }
        String rrdFile = directory + File.separator + resourceName;
        //if (!rrdFile.startsWith("/")) { rrdFile = "/" + rrdFile; }
        m_resource = new Resource(rrdFile);
        m_properties = RrdUtils.readMetaDataFile(directory, resourceName + "-newts");
        m_step = Integer.parseInt(m_properties.get("step"));
    }

    public int getStep() {
        return m_step;
    }

    public Resource getResource() {
        return m_resource;
    }

    NewtsMetric getMetric(int i) {
        if (!m_properties.containsKey("ds." + i + ".name")) {
            throw new IllegalArgumentException("properties does not contain key ds." + i + ".name");
        }
        String name = m_properties.get("ds." + i + ".name");
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name is null");
        }
        if (!m_properties.containsKey("ds." + i + ".type")) {
            throw new IllegalArgumentException("properties does not contain key ds." + i + ".type");
        }
        String type = m_properties.get("ds." + i + ".type");
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("type is null");
        }
        return new NewtsMetric(name, type);
    }

    NewtsMetric getMetric(String metric) {
        int i = -1;
        for (Map.Entry<String, String> entry : m_properties.entrySet()) {
            if (metric.equals(entry.getValue())) {
                i = Integer.parseInt(entry.getKey().replaceFirst("^ds\\.", "").replaceAll("\\.name$", ""));
            }
        }
        if (i == -1) {
            throw new IllegalArgumentException("properties does not a contain key that matches '" + metric + "'");
        }
        if (!m_properties.containsKey("ds." + i + ".type")) {
            throw new IllegalArgumentException("properties does not contain key ds." + i + ".type");
        }
        String type = m_properties.get("ds." + i + ".type");
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("type is null");
        }
        return new NewtsMetric(metric, type);
    }
}
