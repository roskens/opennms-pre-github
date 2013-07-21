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

package org.opennms.netmgt.jasper.resource;

/**
 * The Class ResourceQueryCommandParser.
 */
public class ResourceQueryCommandParser {

    /**
     * Instantiates a new resource query command parser.
     */
    public ResourceQueryCommandParser() {
    }

    /** The m_current query. */
    private ResourceQuery m_currentQuery;

    /** The delimeter. */
    private static String DELIMETER = ",";

    /**
     * Sets the current query.
     *
     * @param q
     *            the new current query
     */
    private void setCurrentQuery(ResourceQuery q) {
        m_currentQuery = q;
    }

    /**
     * Gets the current query.
     *
     * @return the current query
     */
    private ResourceQuery getCurrentQuery() {
        return m_currentQuery;
    }

    /**
     * Parses the query command.
     *
     * @param queryCommand
     *            the query command
     * @return the resource query
     */
    public ResourceQuery parseQueryCommand(String queryCommand) {
        ResourceQuery retVal = new ResourceQuery();
        setCurrentQuery(retVal);

        String command = queryCommand.trim();

        String[] cmdArray = command.split("--");

        for (String cmd : cmdArray) {
            processCommand(cmd.trim());
        }

        return retVal;
    }

    /**
     * Process command.
     *
     * @param command
     *            the command
     */
    private void processCommand(String command) {

        if (command.toLowerCase().contains("rrddir")) {
            processRrdDir(command);
        } else if (command.toLowerCase().contains("nodeid")) {
            processNodeId(command);
        } else if (command.toLowerCase().contains("resourcetype")) {
            processResourceName(command);
        } else if (command.toLowerCase().contains("dsname")) {
            processFilters(command);
        } else if (command.toLowerCase().contains("string")) {
            processStringProperties(command);
        }
    }

    /**
     * Process string properties.
     *
     * @param command
     *            the command
     */
    private void processStringProperties(String command) {
        String value = command.substring(command.toLowerCase().indexOf("string") + "string".length(), command.length());
        String[] strProperties = value.trim().split(DELIMETER);

        getCurrentQuery().setStringProperties(strProperties);
    }

    /**
     * Process filters.
     *
     * @param command
     *            the command
     */
    private void processFilters(String command) {
        String value = command.substring(command.toLowerCase().indexOf("dsname") + "dsname".length(), command.length());
        String[] strFilters = value.trim().split(DELIMETER);

        getCurrentQuery().setFilters(strFilters);
    }

    /**
     * Process resource name.
     *
     * @param command
     *            the command
     */
    private void processResourceName(String command) {
        String value = command.substring(command.toLowerCase().indexOf("resourcetype") + "resourcetype".length(),
                                         command.length());
        getCurrentQuery().setResourceName(value.trim());
    }

    /**
     * Process node id.
     *
     * @param command
     *            the command
     */
    private void processNodeId(String command) {
        String value = command.substring(command.toLowerCase().indexOf("nodeid") + "nodeid".length(), command.length());
        getCurrentQuery().setNodeId(value.trim());
    }

    /**
     * Process rrd dir.
     *
     * @param command
     *            the command
     */
    private void processRrdDir(String command) {
        String value = command.substring(command.toLowerCase().indexOf("rrddir") + "rrdDir".length(), command.length());
        getCurrentQuery().setRrdDir(value.trim());
    }

}
