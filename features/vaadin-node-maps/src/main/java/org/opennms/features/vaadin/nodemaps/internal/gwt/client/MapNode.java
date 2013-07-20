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
package org.opennms.features.vaadin.nodemaps.internal.gwt.client;

import java.util.List;

/**
 * The Class MapNode.
 *
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
public class MapNode {

    /** The latitude. */
    private double latitude;

    /** The longitude. */
    private double longitude;

    /** The node id. */
    private String nodeId;

    /** The node label. */
    private String nodeLabel;

    /** The foreign source. */
    private String foreignSource;

    /** The foreign id. */
    private String foreignId;

    /** The description. */
    private String description;

    /** The maintcontract. */
    private String maintcontract;

    /** The ip address. */
    private String ipAddress;

    /** The severity. */
    private String severity;

    /** The severity label. */
    private String severityLabel;

    /** The unacked count. */
    private int unackedCount;

    /** The categories. */
    private List<String> categories;

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude
     *            the new latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude
     *            the new longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * Sets the node id.
     *
     * @param nodeId
     *            the new node id
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * Gets the node label.
     *
     * @return the node label
     */
    public String getNodeLabel() {
        return nodeLabel;
    }

    /**
     * Sets the node label.
     *
     * @param nodeLabel
     *            the new node label
     */
    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }

    /**
     * Gets the foreign source.
     *
     * @return the foreign source
     */
    public String getForeignSource() {
        return foreignSource;
    }

    /**
     * Sets the foreign source.
     *
     * @param foreignSource
     *            the new foreign source
     */
    public void setForeignSource(String foreignSource) {
        this.foreignSource = foreignSource;
    }

    /**
     * Gets the foreign id.
     *
     * @return the foreign id
     */
    public String getForeignId() {
        return foreignId;
    }

    /**
     * Sets the foreign id.
     *
     * @param foreignId
     *            the new foreign id
     */
    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
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
     * Gets the maintcontract.
     *
     * @return the maintcontract
     */
    public String getMaintcontract() {
        return maintcontract;
    }

    /**
     * Sets the maintcontract.
     *
     * @param maintcontract
     *            the new maintcontract
     */
    public void setMaintcontract(String maintcontract) {
        this.maintcontract = maintcontract;
    }

    /**
     * Gets the ip address.
     *
     * @return the ip address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the ip address.
     *
     * @param ipAddress
     *            the new ip address
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Gets the severity.
     *
     * @return the severity
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Sets the severity.
     *
     * @param severity
     *            the new severity
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * Gets the severity label.
     *
     * @return the severity label
     */
    public String getSeverityLabel() {
        return severityLabel;
    }

    /**
     * Sets the severity label.
     *
     * @param severityLabel
     *            the new severity label
     */
    public void setSeverityLabel(String severityLabel) {
        this.severityLabel = severityLabel;
    }

    /**
     * Gets the unacked count.
     *
     * @return the unacked count
     */
    public int getUnackedCount() {
        return unackedCount;
    }

    /**
     * Sets the unacked count.
     *
     * @param unackedCount
     *            the new unacked count
     */
    public void setUnackedCount(int unackedCount) {
        this.unackedCount = unackedCount;
    }

    /**
     * Gets the categories.
     *
     * @return the categories
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * Sets the categories.
     *
     * @param categories
     *            the new categories
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
