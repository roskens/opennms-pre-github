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

package org.opennms.features.topology.api.topo;

import com.vaadin.data.Item;

/**
 * The Interface Edge.
 */
public interface Edge extends EdgeRef {

    /**
     * Gets the key.
     *
     * @return the key
     * @deprecated Use namespace/id tuple
     */
    String getKey();

    /**
     * Gets the item.
     *
     * @return the item
     */
    Item getItem();

    /**
     * Gets the source.
     *
     * @return the source
     */
    Connector getSource();

    /**
     * Gets the target.
     *
     * @return the target
     */
    Connector getTarget();

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Ref#getLabel()
     */
    @Override
    String getLabel();

    /**
     * Gets the tooltip text.
     *
     * @return the tooltip text
     */
    String getTooltipText();

    /**
     * Sets the tooltip text.
     *
     * @param tooltipText
     *            the new tooltip text
     */
    void setTooltipText(String tooltipText);

    /**
     * Gets the style name.
     *
     * @return the style name
     */
    String getStyleName();

    /**
     * Sets the style name.
     *
     * @param styleName
     *            the new style name
     */
    void setStyleName(String styleName);

}
