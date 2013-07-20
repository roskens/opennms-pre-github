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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.opennms.systemreport.SystemReportFormatter;
import org.opennms.systemreport.SystemReportPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * The Class FullTextSystemReportFormatter.
 */
public class FullTextSystemReportFormatter extends AbstractSystemReportFormatter implements SystemReportFormatter {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(FullTextSystemReportFormatter.class);

    /* (non-Javadoc)
     * @see org.opennms.systemreport.formatters.AbstractSystemReportFormatter#getName()
     */
    @Override
    public String getName() {
        return "full";
    }

    /* (non-Javadoc)
     * @see org.opennms.systemreport.formatters.AbstractSystemReportFormatter#getDescription()
     */
    @Override
    public String getDescription() {
        return "Human-readable text (full output)";
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
        final OutputStream out = getOutputStream();

        try {
            out.write(String.format("= %s: %s =\n\n", plugin.getName(), plugin.getDescription()).getBytes());

            for (final Map.Entry<String, Resource> entry : plugin.getEntries().entrySet()) {
                final Resource value = entry.getValue();

                out.write(String.format("== %s ==\n\n", entry.getKey()).getBytes());

                final InputStream is = value.getInputStream();
                int bytes;
                byte[] buffer = new byte[1024];

                while ((bytes = is.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes);
                }
                is.close();

                out.write("\n\n".getBytes());
            }

        } catch (final Exception e) {
            LOG.info("unable to write", e);
        }
    }

}
