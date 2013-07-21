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
package org.opennms.features.topology.app.internal.gwt.client;

/**
 * The Class SharedVertex.
 */
public class SharedVertex {

    /** The m_key. */
    private String m_key;

    /** The m_initial x. */
    private int m_initialX;

    /** The m_initial y. */
    private int m_initialY;

    /** The m_x. */
    private int m_x;

    /** The m_y. */
    private int m_y;

    /** The m_selected. */
    private boolean m_selected;

    /** The m_status. */
    private String m_status;

    /** The m_icon url. */
    private String m_iconUrl;

    /** The m_label. */
    private String m_label;

    /** The m_tooltip text. */
    private String m_tooltipText;

    /** The m_status count. */
    private String m_statusCount = "0";

    /**
     * Gets the status count.
     *
     * @return the statusCount
     */
    public String getStatusCount() {
        return m_statusCount;
    }

    /**
     * Sets the status count.
     *
     * @param statusCount
     *            the statusCount to set
     */
    public void setStatusCount(String statusCount) {
        this.m_statusCount = statusCount;
    }

    /**
     * Sets the key.
     *
     * @param key
     *            the new key
     */
    public void setKey(String key) {
        m_key = key;
    }

    /**
     * Sets the initial x.
     *
     * @param x
     *            the new initial x
     */
    public void setInitialX(int x) {
        m_initialX = x;
    }

    /**
     * Sets the initial y.
     *
     * @param y
     *            the new initial y
     */
    public void setInitialY(int y) {
        m_initialY = y;
    }

    /**
     * Sets the x.
     *
     * @param x
     *            the new x
     */
    public void setX(int x) {
        m_x = x;
    }

    /**
     * Sets the y.
     *
     * @param y
     *            the new y
     */
    public void setY(int y) {
        m_y = y;
    }

    /**
     * Sets the selected.
     *
     * @param selected
     *            the new selected
     */
    public void setSelected(boolean selected) {
        m_selected = selected;
    }

    /**
     * Sets the icon url.
     *
     * @param iconUrl
     *            the new icon url
     */
    public void setIconUrl(String iconUrl) {
        m_iconUrl = iconUrl;
    }

    /**
     * Sets the label.
     *
     * @param label
     *            the new label
     */
    public void setLabel(String label) {
        m_label = label;
    }

    /**
     * Sets the tooltip text.
     *
     * @param tooltipText
     *            the new tooltip text
     */
    public void setTooltipText(String tooltipText) {
        m_tooltipText = tooltipText;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return m_status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    public void setStatus(String status) {
        m_status = status;
    }

    /**
     * Gets the tooltip text.
     *
     * @return the tooltip text
     */
    public String getTooltipText() {
        return m_tooltipText;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return m_key;
    }

    /**
     * Gets the initial x.
     *
     * @return the initial x
     */
    public int getInitialX() {
        return m_initialX;
    }

    /**
     * Gets the initial y.
     *
     * @return the initial y
     */
    public int getInitialY() {
        return m_initialY;
    }

    /**
     * Gets the x.
     *
     * @return the x
     */
    public int getX() {
        return m_x;
    }

    /**
     * Gets the y.
     *
     * @return the y
     */
    public int getY() {
        return m_y;
    }

    /**
     * Checks if is selected.
     *
     * @return true, if is selected
     */
    public boolean isSelected() {
        return m_selected;
    }

    /**
     * Gets the icon url.
     *
     * @return the icon url
     */
    public String getIconUrl() {
        return m_iconUrl;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return m_label;
    }

    /**
     * Gets the selected.
     *
     * @return the selected
     */
    public boolean getSelected() {
        return m_selected;
    }

}
