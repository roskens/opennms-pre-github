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
package org.opennms.features.topology.app.internal.gwt.client.svg;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The Class SVGLength.
 */
public class SVGLength extends JavaScriptObject {

    /** The Constant SVG_LENGTHTYPE_PX. */
    public static final int SVG_LENGTHTYPE_PX = 5;

    /**
     * Instantiates a new sVG length.
     */
    protected SVGLength() {

    }

    /**
     * Gets the unit type.
     *
     * @return the unit type
     */
    public final native int getUnitType() /*-{
                                          return this.unitType;
                                          }-*/;

    /**
     * Gets the value in specified units.
     *
     * @return the value in specified units
     */
    public final native int getValueInSpecifiedUnits() /*-{
                                                       return this.valueInSpecifiedUnits;
                                                       }-*/;

    /**
     * Sets the new value specified units.
     *
     * @param unitType
     *            the unit type
     * @param valueInSpecifiedUnits
     *            the value in specified units
     */
    public final native void setNewValueSpecifiedUnits(int unitType, int valueInSpecifiedUnits) /*-{
                                                                                                this.newValueSpecifiedUnits(unitType, valueInSpecifiedUnits);
                                                                                                }-*/;

    /**
     * Convert to specified units.
     *
     * @param unitType
     *            the unit type
     */
    public final native void convertToSpecifiedUnits(int unitType) /*-{
                                                                   $wnd.console.log("calling: " + convertToSpecifiedUnits + " with type: " + unitType);
                                                                   this.convertToSpecifiedUnits(unitType);
                                                                   }-*/;

}
