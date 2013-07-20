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

package org.opennms.features.vaadin.nodemaps.internal.gwt.client;

import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.discotools.gwt.leaflet.client.marker.Marker;
import org.discotools.gwt.leaflet.client.types.LatLng;

import com.google.gwt.core.client.JsArrayString;

/**
 * The Class NodeMarker.
 */
public class NodeMarker extends Marker {

    /**
     * The Class Property.
     */
    public static class Property {

        /** The Constant CATEGORIES. */
        public static final String CATEGORIES = "categories";

        /** The Constant NODE_ID. */
        public static final String NODE_ID = "nodeId";

        /** The Constant NODE_LABEL. */
        public static final String NODE_LABEL = "nodeLabel";

        /** The Constant FOREIGN_SOURCE. */
        public static final String FOREIGN_SOURCE = "foreignSource";

        /** The Constant FOREIGN_ID. */
        public static final String FOREIGN_ID = "foreignId";

        /** The Constant IP_ADDRESS. */
        public static final String IP_ADDRESS = "ipAddress";

        /** The Constant SEVERITY_LABEL. */
        public static final String SEVERITY_LABEL = "severityLabel";

        /** The Constant DESCRIPTION. */
        public static final String DESCRIPTION = "description";

        /** The Constant MAINTCONTRACT. */
        public static final String MAINTCONTRACT = "maintcontract";

        /** The Constant SEVERITY. */
        public static final String SEVERITY = "severity";

        /** The Constant UNACKED_COUNT. */
        public static final String UNACKED_COUNT = "unackedCount";
    }

    /**
     * Instantiates a new node marker.
     *
     * @param latLng
     *            the lat lng
     */
    public NodeMarker(final LatLng latLng) {
        super(latLng, new Options());
    }

    /**
     * Instantiates a new node marker.
     *
     * @param element
     *            the element
     */
    public NodeMarker(final JSObject element) {
        super(element);
    }

    /**
     * Put property.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public void putProperty(final String key, final String value) {
        getJSObject().setProperty(key, value);
    }

    /**
     * Gets the property.
     *
     * @param key
     *            the key
     * @return the property
     */
    public String getProperty(final String key) {
        return getJSObject().getPropertyAsString(key);
    }

    /**
     * Gets the text property names.
     *
     * @return the text property names
     */
    public String[] getTextPropertyNames() {
        final JsArrayString nativeNames = getNativePropertyNames(getJSObject());
        final String[] names = new String[nativeNames.length()];
        for (int i = 0; i < nativeNames.length(); i++) {
            names[i] = nativeNames.get(i);
        }
        return names;
    }

    /**
     * Gets the native property names.
     *
     * @param self
     *            the self
     * @return the native property names
     */
    private native JsArrayString getNativePropertyNames(final JSObject self) /*-{
                                                                             var props = [];
                                                                             for (var prop in self) {
                                                                             if (self.hasOwnProperty(prop) && typeof self[prop] === 'string') {
                                                                             props.push(prop);
                                                                             }
                                                                             }
                                                                             return props;
                                                                             }-*/;

    /**
     * Gets the categories.
     *
     * @return the categories
     */
    public JsArrayString getCategories() {
        final JSObject property = getJSObject().getProperty(Property.CATEGORIES);
        if (property == null) {
            return JsArrayString.createArray().cast();
        } else {
            return property.cast();
        }
    }

    /**
     * Gets the categories as string.
     *
     * @return the categories as string
     */
    public String getCategoriesAsString() {
        final StringBuilder catBuilder = new StringBuilder();
        final JsArrayString categories = getCategories();
        if (categories.length() > 0) {
            if (categories.length() == 1) {
                catBuilder.append("Category: ");
            } else {
                catBuilder.append("Categories: ");
            }
            for (int i = 0; i < categories.length(); i++) {
                catBuilder.append(categories.get(i));
                if (i != (categories.length() - 1)) {
                    catBuilder.append(", ");
                }
            }
        }
        return catBuilder.toString();
    }

    /**
     * Sets the categories.
     *
     * @param categories
     *            the new categories
     */
    public void setCategories(final String[] categories) {
        final JsArrayString array = JsArrayString.createArray().cast();
        for (final String category : categories) {
            array.push(category);
        }
        final JSObject jsObject = array.cast();
        getJSObject().setProperty(Property.CATEGORIES, jsObject);
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public Integer getNodeId() {
        final String id = getProperty(Property.NODE_ID);
        return id == null ? null : Integer.valueOf(id);
    }

    /**
     * Gets the node label.
     *
     * @return the node label
     */
    public String getNodeLabel() {
        return getProperty(Property.NODE_LABEL);
    }

    /**
     * Gets the foreign source.
     *
     * @return the foreign source
     */
    public String getForeignSource() {
        return getProperty(Property.FOREIGN_SOURCE);
    }

    /**
     * Gets the foreign id.
     *
     * @return the foreign id
     */
    public String getForeignId() {
        return getProperty(Property.FOREIGN_ID);
    }

    /**
     * Gets the ip address.
     *
     * @return the ip address
     */
    public String getIpAddress() {
        return getProperty(Property.IP_ADDRESS);
    }

    /**
     * Gets the severity label.
     *
     * @return the severity label
     */
    public String getSeverityLabel() {
        return getProperty(Property.SEVERITY_LABEL);
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return getProperty(Property.DESCRIPTION);
    }

    /**
     * Gets the maint contract.
     *
     * @return the maint contract
     */
    public String getMaintContract() {
        return getProperty(Property.MAINTCONTRACT);
    }

    /**
     * Gets the severity.
     *
     * @return the severity
     */
    public int getSeverity() {
        final String severity = getProperty(Property.SEVERITY);
        return severity == null ? 0 : Integer.valueOf(severity);
    }

    /**
     * Gets the unacked count.
     *
     * @return the unacked count
     */
    public Integer getUnackedCount() {
        final String count = getProperty(Property.UNACKED_COUNT);
        return count == null ? 0 : Integer.valueOf(count);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "Feature[lat=" + getLatLng().lat() + ",lon=" + getLatLng().lng() + ",label=" + getNodeLabel() + "]";
    }

    /**
     * To search result.
     *
     * @return the jS object
     */
    public JSObject toSearchResult() {
        return SearchResult.create(getNodeLabel(), getLatLng());
    }

}
