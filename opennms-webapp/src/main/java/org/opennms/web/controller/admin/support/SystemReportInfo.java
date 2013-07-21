/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.controller.admin.support;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.systemreport.SystemReportFormatter;
import org.opennms.systemreport.SystemReportPlugin;

/**
 * The Class SystemReportInfo.
 */
public class SystemReportInfo implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4803853030354121419L;

    /** The m_plugins. */
    private final List<SystemReportPlugin> m_plugins;

    /** The m_formatters. */
    private final List<SystemReportFormatter> m_formatters;

    /**
     * Instantiates a new system report info.
     *
     * @param formatters
     *            the formatters
     * @param plugins
     *            the plugins
     */
    public SystemReportInfo(final List<SystemReportFormatter> formatters, final List<SystemReportPlugin> plugins) {
        m_formatters = formatters;
        m_plugins = plugins;
    }

    /**
     * Gets the formatter.
     *
     * @return the formatter
     */
    public SystemReportFormatter getFormatter() {
        if (m_formatters != null && m_formatters.size() > 0) {
            return m_formatters.get(0);
        }
        return null;
    }

    /**
     * Gets the formatters.
     *
     * @return the formatters
     */
    public List<SystemReportFormatter> getFormatters() {
        return m_formatters;
    }

    /**
     * Gets the plugins.
     *
     * @return the plugins
     */
    public List<SystemReportPlugin> getPlugins() {
        return m_plugins;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("formatters", m_formatters).append("plugins", m_plugins).toString();
    }
}
