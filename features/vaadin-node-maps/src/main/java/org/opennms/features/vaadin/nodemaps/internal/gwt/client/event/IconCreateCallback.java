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
package org.opennms.features.vaadin.nodemaps.internal.gwt.client.event;

import java.util.List;

import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.discotools.gwt.leaflet.client.jsobject.JSObjectWrapper;
import org.discotools.gwt.leaflet.client.types.DivIcon;
import org.discotools.gwt.leaflet.client.types.DivIconOptions;
import org.discotools.gwt.leaflet.client.types.Point;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.NodeMarker;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.ui.MarkerCluster;

/**
 * The Class IconCreateCallback.
 */
public class IconCreateCallback extends JSObjectWrapper {

    /**
     * Instantiates a new icon create callback.
     *
     * @param jsObject
     *            the js object
     */
    protected IconCreateCallback(final JSObject jsObject) {
        super(jsObject);
    }

    /**
     * Instantiates a new icon create callback.
     */
    public IconCreateCallback() {
        super(JSObject.createJSFunction());
        setJSObject(getCallbackFunction());
    }

    /**
     * Creates the icon.
     *
     * @param cluster
     *            the cluster
     * @return the jS object
     */
    public JSObject createIcon(final MarkerCluster cluster) {
        final DivIconOptions options = new DivIconOptions();
        options.setHtml("<div><span>" + cluster.getChildCount() + "</span></div>");
        options.setIconSize(new Point(40, 40));

        int severity = 0;
        String severityLabel = "Normal";
        for (final NodeMarker marker : (List<NodeMarker>) cluster.getAllChildMarkers()) {
            final int nodeSeverity = marker.getSeverity();
            if (nodeSeverity > severity) {
                severity = nodeSeverity;
                severityLabel = marker.getSeverityLabel();
            }
            if (severity == 7)
                break;
        }

        options.setClassName("marker-cluster marker-cluster-" + severityLabel);

        return new DivIcon(options).getJSObject();
    }

    /**
     * Gets the callback function.
     *
     * @return the callback function
     */
    public final native JSObject getCallbackFunction() /*-{
                                                       var self = this;
                                                       return function(cluster) {
                                                       return self.@org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.IconCreateCallback::createIcon(Lorg/opennms/features/vaadin/nodemaps/internal/gwt/client/ui/MarkerCluster;)(cluster);
                                                       };
                                                       }-*/;
}
