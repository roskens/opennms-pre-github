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
 * The Class SVGMatrix.
 */
public class SVGMatrix extends JavaScriptObject {

    /**
     * Instantiates a new sVG matrix.
     */
    protected SVGMatrix() {
    };

    /**
     * Translate.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     * @return the sVG matrix
     */
    public final native SVGMatrix translate(double x, double y)/*-{
                                                               return this.translate(x, y);
                                                               }-*/;

    /**
     * Scale.
     *
     * @param newScale
     *            the new scale
     * @return the sVG matrix
     */
    public final native SVGMatrix scale(double newScale) /*-{
                                                         return this.scale(newScale);
                                                         }-*/;

    /**
     * Multiply.
     *
     * @param m
     *            the m
     * @return the sVG matrix
     */
    public final native SVGMatrix multiply(SVGMatrix m) /*-{
                                                        return this.multiply(m);
                                                        }-*/;

    /**
     * Gets the a.
     *
     * @return the a
     */
    public final native double getA() /*-{
                                      return this.a;
                                      }-*/;

    /**
     * Gets the b.
     *
     * @return the b
     */
    public final native double getB() /*-{
                                      return this.b;
                                      }-*/;

    /**
     * Gets the c.
     *
     * @return the c
     */
    public final native double getC() /*-{
                                      return this.c;
                                      }-*/;

    /**
     * Gets the d.
     *
     * @return the d
     */
    public final native double getD() /*-{
                                      return this.d;
                                      }-*/;

    /**
     * Gets the e.
     *
     * @return the e
     */
    public final native double getE() /*-{
                                      return this.e;
                                      }-*/;

    /**
     * Gets the f.
     *
     * @return the f
     */
    public final native double getF() /*-{
                                      return this.f;
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
     * Sets the x.
     *
     * @param clientX
     *            the new x
     */
    public final native void setX(int clientX) /*-{
                                               this.x = clientX;
                                               }-*/;

    /**
     * Sets the y.
     *
     * @param clientY
     *            the new y
     */
    public final native void setY(int clientY) /*-{
                                               this.y = clientY;
                                               }-*/;

    /**
     * Matrix transform.
     *
     * @param matrix
     *            the matrix
     * @return the sVG matrix
     */
    public final native SVGMatrix matrixTransform(SVGMatrix matrix) /*-{
                                                                    return this.matrixTransform(matrix);
                                                                    }-*/;

    /**
     * Gets the x.
     *
     * @return the x
     */
    public final native int getX() /*-{
                                   return this.e;
                                   }-*/;

    /**
     * Gets the y.
     *
     * @return the y
     */
    public final native int getY() /*-{
                                   return this.f;
                                   }-*/;

}
