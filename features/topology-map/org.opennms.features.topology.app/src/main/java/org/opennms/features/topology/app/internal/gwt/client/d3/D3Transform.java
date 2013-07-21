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

package org.opennms.features.topology.app.internal.gwt.client.d3;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;

/**
 * The Class D3Transform.
 */
public class D3Transform extends JavaScriptObject {

    /**
     * Instantiates a new d3 transform.
     */
    protected D3Transform() {
    };

    /**
     * Gets the translate.
     *
     * @return the translate
     */
    public final native JsArrayInteger getTranslate() /*-{
                                                      return this.translate;
                                                      }-*/;

    /**
     * Gets the x.
     *
     * @return the x
     */
    public final native int getX() /*-{
                                   if(this.translate != "undefined"){
                                   return this.translate[0];
                                   }
                                   return -1;
                                   }-*/;

    /**
     * Gets the y.
     *
     * @return the y
     */
    public final native int getY() /*-{
                                   if(this.translate != "undefined"){
                                   return this.translate[1];
                                   }
                                   return -1;
                                   }-*/;

    /**
     * Gets the scale.
     *
     * @return the scale
     */
    public final native JsArrayNumber getScale() /*-{
                                                 return this.scale;
                                                 }-*/;

    /**
     * Gets the scale x.
     *
     * @return the scale x
     */
    public final native double getScaleX() /*-{
                                           return this.scale[0];
                                           }-*/;

    /**
     * Gets the scale y.
     *
     * @return the scale y
     */
    public final native double getScaleY() /*-{
                                           return this.scale[1];
                                           }-*/;

    /**
     * Gets the rotate.
     *
     * @return the rotate
     */
    public final native double getRotate() /*-{
                                           return this.rotate;
                                           }-*/;

    /**
     * Gets the skew.
     *
     * @return the skew
     */
    public final native double getSkew() /*-{
                                         return this.skew;
                                         }-*/;

}
