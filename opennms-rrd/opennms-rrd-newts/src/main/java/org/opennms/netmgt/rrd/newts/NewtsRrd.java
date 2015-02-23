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
public class NewtsRrd {

    private static final Logger LOG = LoggerFactory.getLogger(NewtsRrd.class);

    private final Resource m_resource;
    private final int m_step;
    private final PropertiesConfiguration m_properties;

    /**
     * Creates a new {@link NewtsResource} instance with for the supplied
     * file name. The file is expected to be a properties file
     *
     * @param fileName
     *          the file name
     */
    public NewtsRrd(final String fileName) {
        m_resource = new Resource(fileName);
        try {
            final File file = new File(fileName);
            LOG.debug("NewtsResource: filename={}", fileName);
            m_properties = new PropertiesConfiguration(file);
            m_step = m_properties.getInt("step", 300);
        } catch (ConfigurationException ex) {
            LOG.debug("confiiguration error", ex);
            throw new IllegalArgumentException("invalid newts resource file: " + fileName);
        }
    }

    /**
     * Returns primary RRD time step
     *
     * @return Primary time step in seconds
     */
    public int getStep() {
        return m_step;
    }

    /**
     * Returns the Newts {@link Resource} instance.
     * @return
     */
    public Resource getResource() {
        return m_resource;
    }

    /**
     * Returns the nth {@link NewtsMetric} instance.
     *
     * @param n
     * @return
     *      a {@link NewtsMetric} instance.
     */
    public NewtsMetric getMetric(int n) {
        if (!m_properties.containsKey("ds." + n + ".name")) {
            throw new IllegalArgumentException("properties does not contain key ds." + n + ".name");
        }
        String name = m_properties.getString("ds." + n + ".name");
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name is null");
        }
        if (!m_properties.containsKey("ds." + n + ".type")) {
            throw new IllegalArgumentException("properties does not contain key ds." + n + ".type");
        }
        String type = m_properties.getString("ds." + n + ".type");
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("type is null");
        }
        return new NewtsMetric(name, type);
    }

    /**
     * Returns the {@link NewtsMetric} instance for the given metric name.
     *
     * @param metric
     * @return
     *      a {@link NewtsMetric} instance.
     */
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
