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

package org.opennms.features.jmxconfiggenerator.webui.ui;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Window;

/**
 * A modal "PopUp" which shows some text to the user and a "sandglass" (ok it is
 * a turning circle.. but you get my point ;)).
 *
 * @author Markus von Rüden
 */
public class ProgressWindow extends Window {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The progress. */
    private ProgressIndicator progress = new ProgressIndicator();

    /** The layout. */
    private HorizontalLayout layout = new HorizontalLayout();

    /** The label. */
    private Label label = new Label("calculating some stuff");

    /**
     * Instantiates a new progress window.
     */
    public ProgressWindow() {
        setCaption("processing...");
        setModal(true);
        setClosable(false);
        setWidth(400, UNITS_PIXELS);
        setHeight(200, UNITS_PIXELS);
        progress.setIndeterminate(true);
        layout.addComponent(progress);
        layout.setSpacing(true);
        layout.addComponent(label);
        setContent(layout);
        center();
    }

    /**
     * Sets the label text.
     *
     * @param label
     *            the new label text
     */
    public void setLabelText(String label) {
        this.label.setValue(label);
    }
}
