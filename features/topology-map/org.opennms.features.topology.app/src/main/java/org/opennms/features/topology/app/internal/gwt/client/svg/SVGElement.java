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

import com.google.gwt.dom.client.Element;

/**
 * The Class SVGElement.
 */
public class SVGElement extends Element {

    /**
     * Instantiates a new sVG element.
     */
    protected SVGElement() {
    }

    /**
     * Wrap element.
     *
     * @param svg
     *            the svg
     * @return the sVG element
     */
    public static final native SVGElement wrapElement(Element svg) /*-{
                                                                   return elem;
                                                                   }-*/;

    /**
     * Creates the svg matrix.
     *
     * @return the sVG matrix
     */
    public final native SVGMatrix createSVGMatrix() /*-{
                                                    return this.createSVGMatrix();
                                                    }-*/;

    /**
     * Gets the ctm.
     *
     * @return the ctm
     */
    public final native SVGMatrix getCTM() /*-{
                                           return this.getCTM();
                                           }-*/;

    /**
     * Creates the svg point.
     *
     * @return the sVG point
     */
    public final native SVGPoint createSVGPoint() /*-{
                                                  return this.createSVGPoint();
                                                  }-*/;

    /**
     * Sets the x.
     *
     * @param x
     *            the new x
     */
    public final native void setX(int x) /*-{
                                         this.x = x;
                                         }-*/;

    /**
     * Sets the y.
     *
     * @param y
     *            the new y
     */
    public final native void setY(int y) /*-{
                                         this.y = y;
                                         }-*/;

    /**
     * Gets the b box.
     *
     * @return the b box
     */
    public final native SVGRect getBBox() /*-{
                                          return this.getBBox();
                                          }-*/;

    /**
     * Gets the bounding client rect.
     *
     * @return the bounding client rect
     */
    public final native ClientRect getBoundingClientRect() /*-{
                                                           return this.getBoundingClientRect();
                                                           }-*/;

    /**
     * Inverse.
     *
     * @return the sVG matrix
     */
    public final native SVGMatrix inverse() /*-{
                                            return this.inverse();
                                            }-*/;

    /**
     * Gets the width.
     *
     * @return the width
     */
    public final native SVGAnimatedLength getWidth() /*-{
                                                     return this.width;
                                                     }-*/;

    /**
     * Gets the height.
     *
     * @return the height
     */
    public final native SVGAnimatedLength getHeight() /*-{
                                                      return this.height;
                                                      }-*/;

    /**
     * Creates the svg length.
     *
     * @return the sVG length
     */
    public final native SVGLength createSVGLength() /*-{
                                                    return this.createSVGLength();
                                                    }-*/;

    /**
     * Gets the screen ctm.
     *
     * @return the screen ctm
     */
    public final native SVGMatrix getScreenCTM() /*-{
                                                 return this.getScreenCTM();
                                                 }-*/;

}
