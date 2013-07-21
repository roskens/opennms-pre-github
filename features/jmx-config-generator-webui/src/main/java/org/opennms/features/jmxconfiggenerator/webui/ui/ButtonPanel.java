/**
 * *****************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013 The OpenNMS Group, Inc. OpenNMS(R) is Copyright (C)
 * 1999-2013 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OpenNMS(R). If not, see: http://www.gnu.org/licenses/
 *
 * For more information contact: OpenNMS(R) Licensing <license@opennms.org>
 * http://www.opennms.org/ http://www.opennms.com/
 * *****************************************************************************
 */
package org.opennms.features.jmxconfiggenerator.webui.ui;

import org.opennms.features.jmxconfiggenerator.webui.data.ModelChangeListener;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

/**
 * The Class ButtonPanel.
 */
public class ButtonPanel extends HorizontalLayout implements ModelChangeListener<UiState> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The next. */
    private final Button next;

    /** The previous. */
    private final Button previous;

    /**
     * Instantiates a new button panel.
     *
     * @param listener
     *            the listener
     */
    public ButtonPanel(ClickListener listener) {
        next = UIHelper.createButton("next", IconProvider.BUTTON_NEXT, listener);
        previous = UIHelper.createButton("previous", IconProvider.BUTTON_PREVIOUS, listener);
        // help = UIHelper.createButton("help", IconProvider.BUTTON_INFO, this);

        addComponent(previous);
        // addComponent(help);
        addComponent(next);
    }

    /**
     * Gets the next.
     *
     * @return the next
     */
    public Button getNext() {
        return next;
    }

    /**
     * Gets the previous.
     *
     * @return the previous
     */
    public Button getPrevious() {
        return previous;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.data.ModelChangeListener#modelChanged(java.lang.Object)
     */
    @Override
    public void modelChanged(UiState newModel) {
        // help.setVisible(false); // TODO enable/disable help dynamically
        previous.setVisible(newModel.hasPrevious());
        next.setVisible(newModel.hasNext());
    }
}
