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
package org.opennms.features.vaadin.nodemaps.internal.gwt.client.ui.controls.search;

import com.google.gwt.user.client.ui.TextBox;

/**
 * The Class MockTextBox.
 */
public final class MockTextBox extends TextBox {

    /** The m_value. */
    String m_value = "";

    /**
     * Instantiates a new mock text box.
     */
    public MockTextBox() {
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.TextBoxBase#getValue()
     */
    @Override
    public String getValue() {
        return m_value;
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.ValueBoxBase#setValue(java.lang.Object)
     */
    @Override
    public void setValue(final String value) {
        m_value = value;
    }
}
