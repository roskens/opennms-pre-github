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

package org.opennms.features.jmxconfiggenerator.graphs;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Report.
 *
 * @author Simon Walter <simon.walter@hp-factory.de>
 * @author Markus Neumann <markus@opennms.com>
 */
public class Report {

    /** The id. */
    private String id;

    /** The name. */
    private String name;

    /** The title. */
    private String title;

    /** The vertical label. */
    private String verticalLabel;

    /** The graphs. */
    private List<Graph> graphs = new ArrayList<Graph>();

    /**
     * Instantiates a new report.
     *
     * @param id
     *            the id
     * @param name
     *            the name
     * @param title
     *            the title
     * @param verticalLabel
     *            the vertical label
     */
    public Report(String id, String name, String title, String verticalLabel) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.verticalLabel = verticalLabel;
    }

    /**
     * Gets the graph resources.
     *
     * @return the graph resources
     */
    public String getGraphResources() {
        String result = "";
        for (Graph graph : graphs) {
            result = result.concat(graph.getResourceName() + ", ");
        }
        if (result.length() > 2) {
            result = result.substring(0, (result.length() - 2));
        }
        return result;
    }

    /**
     * Gets the graphs.
     *
     * @return the graphs
     */
    public List<Graph> getGraphs() {
        return graphs;
    }

    /**
     * Sets the graphs.
     *
     * @param graphs
     *            the new graphs
     */
    public void setGraphs(List<Graph> graphs) {
        this.graphs = graphs;
    }

    /**
     * Adds the graph.
     *
     * @param graph
     *            the graph
     */
    public void addGraph(Graph graph) {
        this.graphs.add(graph);
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *            the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the vertical label.
     *
     * @return the vertical label
     */
    public String getVerticalLabel() {
        return verticalLabel;
    }

    /**
     * Sets the vertical label.
     *
     * @param verticalLabel
     *            the new vertical label
     */
    public void setVerticalLabel(String verticalLabel) {
        this.verticalLabel = verticalLabel;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Report{" + "id=" + id + ", name=" + name + ", title=" + title + ", verticalLabel=" + verticalLabel
                + ", graphs=" + graphs + '}';
    }
}
