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
 * The Class SharedEdge.
 */
public class SharedEdge {

    /** The m_key. */
    private String m_key;

    /** The m_source key. */
    private String m_sourceKey;

    /** The m_target key. */
    private String m_targetKey;

    /** The m_selected. */
    private boolean m_selected;

    /** The m_style name. */
    private String m_styleName;

    /** The m_tooltip text. */
    private String m_tooltipText;

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
     * Sets the source key.
     *
     * @param sourceKey
     *            the new source key
     */
    public void setSourceKey(String sourceKey) {
        m_sourceKey = sourceKey;
    }

    /**
     * Sets the target key.
     *
     * @param targetKey
     *            the new target key
     */
    public void setTargetKey(String targetKey) {
        m_targetKey = targetKey;
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
     * Sets the css class.
     *
     * @param styleName
     *            the new css class
     */
    public void setCssClass(String styleName) {
        m_styleName = styleName;
    }

    /**
     * Gets the css class.
     *
     * @return the css class
     */
    public String getCssClass() {
        return m_styleName;
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
     * Gets the style name.
     *
     * @return the style name
     */
    public String getStyleName() {
        return m_styleName;
    }

    /**
     * Sets the style name.
     *
     * @param styleName
     *            the new style name
     */
    public void setStyleName(String styleName) {
        m_styleName = styleName;
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
     * Gets the source key.
     *
     * @return the source key
     */
    public String getSourceKey() {
        return m_sourceKey;
    }

    /**
     * Gets the target key.
     *
     * @return the target key
     */
    public String getTargetKey() {
        return m_targetKey;
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
     * Gets the tooltip text.
     *
     * @return the tooltip text
     */
    public String getTooltipText() {
        return m_tooltipText;
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
