/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2013 The OpenNMS Group, Inc.
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

import java.util.Arrays;

/**
 * The Enum UiState.
 */
public enum UiState {

    /** The Introduction view. */
    IntroductionView("Introduction", true),
 /** The Service configuration view. */
 ServiceConfigurationView("Service Configuration", true),
 /** The Mbeans detection. */
 MbeansDetection(
            "Determine MBeans information", false),
 /** The Mbeans view. */
 MbeansView("MBeans Configuration", true),
 /** The Result config generation. */
 ResultConfigGeneration(
            "Generate OpenNMS Configuration snippets", false),
 /** The Result view. */
 ResultView("OpenNMS Configuration", true);

    /** The description. */
    private final String description;

    /** The ui. */
    private boolean ui;

    /**
     * Instantiates a new ui state.
     *
     * @param description
     *            the description
     * @param ui
     *            the ui
     */
    private UiState(String description, boolean ui) {
        this.description = description;
        this.ui = ui;
    }

    /**
     * Checks for ui.
     *
     * @return true, if successful
     */
    boolean hasUi() {
        return ui;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Checks for previous.
     *
     * @return true, if successful
     */
    public boolean hasPrevious() {
        return !isFirst();
    }

    /**
     * Checks for next.
     *
     * @return true, if successful
     */
    public boolean hasNext() {
        return !isLast();
    }

    /**
     * Checks if is first.
     *
     * @return true, if is first
     */
    private boolean isFirst() {
        return UiState.values()[0].equals(this);
    }

    /**
     * Checks if is last.
     *
     * @return true, if is last
     */
    private boolean isLast() {
        return UiState.values()[UiState.values().length - 1].equals(this);
    }

    /**
     * Gets the previous.
     *
     * @return the previous
     */
    public UiState getPrevious() {
        if (hasPrevious()) {
            int currentIndex = Arrays.asList(UiState.values()).indexOf(this);
            return UiState.values()[currentIndex - 1];
        }
        return null; // no previous element
    }

    /**
     * Gets the next.
     *
     * @return the next
     */
    public UiState getNext() {
        if (hasNext()) {
            int currentIndex = Arrays.asList(UiState.values()).indexOf(this);
            return UiState.values()[currentIndex + 1];
        }
        return null; // no next element
    }
}
