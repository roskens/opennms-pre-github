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

package org.opennms.systemreport.formatters;

import java.util.Map;

import org.opennms.systemreport.SystemReportFormatter;
import org.opennms.systemreport.SystemReportPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * The Class TextSystemReportFormatter.
 */
public class TextSystemReportFormatter extends AbstractSystemReportFormatter implements SystemReportFormatter {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(TextSystemReportFormatter.class);

    /* (non-Javadoc)
     * @see org.opennms.systemreport.formatters.AbstractSystemReportFormatter#getName()
     */
    @Override
    public String getName() {
        return "text";
    }

    /* (non-Javadoc)
     * @see org.opennms.systemreport.formatters.AbstractSystemReportFormatter#getDescription()
     */
    @Override
    public String getDescription() {
        return "Simple human-readable indented text";
    }

    /* (non-Javadoc)
     * @see org.opennms.systemreport.SystemReportFormatter#getContentType()
     */
    @Override
    public String getContentType() {
        return "text/plain";
    }

    /* (non-Javadoc)
     * @see org.opennms.systemreport.SystemReportFormatter#getExtension()
     */
    @Override
    public String getExtension() {
        return "txt";
    }

    /* (non-Javadoc)
     * @see org.opennms.systemreport.SystemReportFormatter#canStdout()
     */
    @Override
    public boolean canStdout() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.opennms.systemreport.formatters.AbstractSystemReportFormatter#write(org.opennms.systemreport.SystemReportPlugin)
     */
    @Override
    public void write(final SystemReportPlugin plugin) {
        if (!hasDisplayable(plugin)) {
            return;
        }
        LOG.debug("write({})", plugin.getName());
        try {
            final String title = plugin.getName() + " (" + plugin.getDescription() + "):" + "\n";
            getOutputStream().write(title.getBytes());
            for (final Map.Entry<String, Resource> entry : plugin.getEntries().entrySet()) {
                final Resource value = entry.getValue();
                final boolean displayable = isDisplayable(value);

                final String text;
                if (displayable) {
                    text = "\t" + entry.getKey() + ": " + getResourceText(value) + "\n";
                } else {
                    text = "\t"
                            + entry.getKey()
                            + ": "
                            + (value == null ? "NULL" : value.getClass().getSimpleName()
                                    + " resource is not displayable.  Try using the 'zip' format.") + "\n";
                }
                getOutputStream().write(text.getBytes());
            }
        } catch (Throwable e) {
            LOG.error("Error writing plugin data.", e);
        }
    }
}
