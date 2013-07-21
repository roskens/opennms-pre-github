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

/**
 * The Class Graph.
 *
 * @author Simon Walter <simon.walter@hp-factory.de>
 * @author Markus Neumann <markus@opennms.com>
 */
public class Graph {

    /** The id. */
    private String id;

    /** The description. */
    private String description;

    /** The resource name. */
    private String resourceName;

    /** The colore a. */
    private String coloreA;

    /** The colore b. */
    private String coloreB;

    /** The colore c. */
    private String coloreC;

    /**
     * Instantiates a new graph.
     *
     * @param id
     *            the id
     * @param description
     *            the description
     * @param resourceName
     *            the resource name
     * @param coloreA
     *            the colore a
     * @param coloreB
     *            the colore b
     * @param coloreC
     *            the colore c
     */
    public Graph(String id, String description, String resourceName, String coloreA, String coloreB, String coloreC) {
        this.id = id;
        this.description = description;
        this.resourceName = resourceName;
        this.coloreA = coloreA;
        this.coloreB = coloreB;
        this.coloreC = coloreC;
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
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the resource name.
     *
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Sets the resource name.
     *
     * @param resourceName
     *            the new resource name
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * Gets the colore a.
     *
     * @return the colore a
     */
    public String getColoreA() {
        return coloreA;
    }

    /**
     * Sets the colore a.
     *
     * @param coloreA
     *            the new colore a
     */
    public void setColoreA(String coloreA) {
        this.coloreA = coloreA;
    }

    /**
     * Gets the colore b.
     *
     * @return the colore b
     */
    public String getColoreB() {
        return coloreB;
    }

    /**
     * Sets the colore b.
     *
     * @param coloreB
     *            the new colore b
     */
    public void setColoreB(String coloreB) {
        this.coloreB = coloreB;
    }

    /**
     * Gets the colore c.
     *
     * @return the colore c
     */
    public String getColoreC() {
        return coloreC;
    }

    /**
     * Sets the colore c.
     *
     * @param coloreC
     *            the new colore c
     */
    public void setColoreC(String coloreC) {
        this.coloreC = coloreC;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Graph{" + "id=" + id + ", description=" + description + ", resourceName=" + resourceName + ", coloreA="
                + coloreA + ", coloreB=" + coloreB + ", coloreC=" + coloreC + '}';
    }
}
