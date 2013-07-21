/**
 * *****************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc. OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with OpenNMS(R). If not, see:
 * http://www.gnu.org/licenses/
 *
 * For more information contact: OpenNMS(R) Licensing <license@opennms.org> http://www.opennms.org/ http://www.opennms.com/
 ******************************************************************************
 */
package org.opennms.nrtg.web.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opennms.netmgt.model.PrefabGraph;

/**
 * The Class NrtHelper.
 *
 * @author Markus@OpenNMS.org
 */
public class NrtHelper {

    /** The Constant RRD_KEYWORDS. */
    public static final List<String> RRD_KEYWORDS = Arrays.asList("--", "DEF", "CDEF", "LINE", "GPRINT");

    /**
     * Clean up rrd graph string for web ui.
     *
     * @param prefabGraph
     *            the prefab graph
     * @param externalPropertyAttributes
     *            the external property attributes
     * @param stringPropertyAttributes
     *            the string property attributes
     * @return the string
     */
    public String cleanUpRrdGraphStringForWebUi(final PrefabGraph prefabGraph,
            final Map<String, String> externalPropertyAttributes, final Map<String, String> stringPropertyAttributes) {
        String graphString = prefabGraph.getCommand();

        // Overwrite height and width by cinematic ration 1x2.40
        graphString = "--height=400 " + graphString;
        graphString = "--width=960 " + graphString;

        if (!graphString.contains("--slope-mode")) {
            graphString = "--slope-mode " + graphString;
        }
        if (!graphString.contains("--watermark")) {
            graphString = "--watermark=\"NRTG Alpha 1.0\" " + graphString;
        }

        // Escaping colons in rrd-strings rrd in javascript in java...
        graphString = graphString.replace("\\:", "\\\\\\\\:");
        graphString = graphString.replace("\\n", "\\\\\\\\n");

        // Escaping quotes in javascript in java
        graphString = graphString.replace("\"", "\\\\\"");

        for (final String key : externalPropertyAttributes.keySet()) {
            graphString = graphString.replace("{" + key + "}", externalPropertyAttributes.get(key));
        }

        for (final String key : stringPropertyAttributes.keySet()) {
            graphString = graphString.replace("{" + key + "}", stringPropertyAttributes.get(key));
        }

        return graphString;
    }

    /**
     * Generate js mapping object.
     *
     * @param rrdCommand
     *            the rrd command
     * @param rrdGraphAttributesToMetricIds
     *            the rrd graph attributes to metric ids
     * @return the string
     */
    public String generateJsMappingObject(String rrdCommand, final Map<String, String> rrdGraphAttributesToMetricIds) {
        final StringBuilder stringBuilder = new StringBuilder();
        final String command = rrdCommand;

        final Pattern pattern = Pattern.compile("DEF:.*?=(\\{.*?\\}):(.*?):");
        final Matcher matcher = pattern.matcher(command);

        final Map<String, String> rrdFileMapping = new HashMap<String, String>();
        while (matcher.find()) {
            rrdFileMapping.put(matcher.group(2), matcher.group(1));
        }

        for (final Map.Entry<String, String> entry : rrdGraphAttributesToMetricIds.entrySet()) {
            final String row = String.format("'%s': '%s:%s', \n", entry.getValue(), rrdFileMapping.get(entry.getKey()),
                                             entry.getKey());
            stringBuilder.append(row);
        }

        return stringBuilder.toString().substring(0, stringBuilder.toString().length() - ", \n".length());
    }
}
