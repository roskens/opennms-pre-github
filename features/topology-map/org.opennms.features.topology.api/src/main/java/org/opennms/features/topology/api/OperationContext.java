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

package org.opennms.features.topology.api;

import com.vaadin.ui.UI;

/**
 * The Interface OperationContext.
 */
public interface OperationContext {

    /**
     * The Enum DisplayLocation.
     */
    enum DisplayLocation {

        /** The menubar. */
        MENUBAR,
 /** The contextmenu. */
 CONTEXTMENU
    };

    /**
     * Gets the main window.
     *
     * @return the main window
     */
    UI getMainWindow();

    /**
     * Gets the graph container.
     *
     * @return the graph container
     */
    GraphContainer getGraphContainer();

    /**
     * Gets the display location.
     *
     * @return the display location
     */
    DisplayLocation getDisplayLocation();

    /**
     * Checks if is checked.
     *
     * @return true, if is checked
     */
    boolean isChecked();
}
