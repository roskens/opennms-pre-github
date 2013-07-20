/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.features.gwt.combobox.client.common;

import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ColumnDefinition.
 *
 * @param <T>
 *            the generic type
 */
public abstract class ColumnDefinition<T> {

    /**
     * Render.
     *
     * @param t
     *            the t
     * @return the widget
     */
    public abstract Widget render(T t);

    /**
     * Checks if is clickable.
     *
     * @return true, if is clickable
     */
    public boolean isClickable() {
        return true;
    }

    /**
     * Checks if is selectable.
     *
     * @return true, if is selectable
     */
    public boolean isSelectable() {
        return true;
    }
}
