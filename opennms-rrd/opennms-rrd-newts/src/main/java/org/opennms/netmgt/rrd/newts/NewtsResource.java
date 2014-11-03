/******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2015 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2015 The OpenNMS Group, Inc.
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
import java.util.Iterator;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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
    private final PropertiesConfiguration m_properties;

    public NewtsResource(final String rrdFile) {
        m_resource = new Resource(rrdFile);
        try {
            final File file = new File(rrdFile);
            LOG.debug("NewtsResource: filename={}", rrdFile);
            m_properties = new PropertiesConfiguration(file);
            m_step = m_properties.getInt("step", 300);
        } catch (ConfigurationException ex) {
            LOG.debug("confiiguration error", ex);
            throw new IllegalArgumentException("invalid newts resource file: " + rrdFile);
        }
    }

    public int getStep() {
        return m_step;
    }

    public Resource getResource() {
        return m_resource;
    }

    public NewtsMetric getMetric(int i) {
        if (!m_properties.containsKey("ds." + i + ".name")) {
            throw new IllegalArgumentException("properties does not contain key ds." + i + ".name");
        }
        String name = m_properties.getString("ds." + i + ".name");
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name is null");
        }
        if (!m_properties.containsKey("ds." + i + ".type")) {
            throw new IllegalArgumentException("properties does not contain key ds." + i + ".type");
        }
        String type = m_properties.getString("ds." + i + ".type");
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("type is null");
        }
        return new NewtsMetric(name, type);
    }

    public NewtsMetric getMetric(String metric) {
        int i = -1;
        Iterator<String> it = m_properties.getKeys();
        while (it.hasNext()) {
            String key = it.next();
            if (metric.equals(m_properties.getString(key))) {
                i = Integer.parseInt(key.replaceFirst("^ds\\.", "").replaceAll("\\.name$", ""));
            }
        }
        if (i == -1) {
            throw new IllegalArgumentException("properties does not a contain key that matches '" + metric + "'");
        }
        if (!m_properties.containsKey("ds." + i + ".type")) {
            throw new IllegalArgumentException("properties does not contain key ds." + i + ".type");
        }
        String type = m_properties.getString("ds." + i + ".type");
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("type is null");
        }
        return new NewtsMetric(metric, type);
    }
}
