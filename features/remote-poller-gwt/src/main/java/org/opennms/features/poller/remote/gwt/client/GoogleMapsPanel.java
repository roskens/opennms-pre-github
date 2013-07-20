/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.features.poller.remote.gwt.client;

import static org.opennms.features.poller.remote.gwt.client.utils.GoogleMapsUtils.toGWTBounds;
import static org.opennms.features.poller.remote.gwt.client.utils.GoogleMapsUtils.toLatLng;
import static org.opennms.features.poller.remote.gwt.client.utils.GoogleMapsUtils.toLatLngBounds;

import java.util.HashMap;
import java.util.Map;

import org.opennms.features.poller.remote.gwt.client.events.GWTMarkerClickedEvent;
import org.opennms.features.poller.remote.gwt.client.events.MapPanelBoundsChangedEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.InfoWindowCloseClickHandler;
import com.google.gwt.maps.client.event.MapMoveEndHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * GoogleMapsPanel class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public class GoogleMapsPanel extends Composite implements MapPanel {

    /** The ui binder. */
    private static GoogleMapsPanelUiBinder uiBinder = GWT.create(GoogleMapsPanelUiBinder.class);

    /**
     * The Interface GoogleMapsPanelUiBinder.
     */
    interface GoogleMapsPanelUiBinder extends UiBinder<Widget, GoogleMapsPanel> {
    }

    /** The m_map widget. */
    @UiField
    MapWidget m_mapWidget;

    /** The m_markers. */
    private Map<String, Marker> m_markers = new HashMap<String, Marker>();

    /** The m_marker states. */
    private Map<String, GWTMarkerState> m_markerStates = new HashMap<String, GWTMarkerState>();

    /** The m_event bus. */
    private HandlerManager m_eventBus;

    /**
     * <p>
     * Constructor for GoogleMapsPanel.
     * </p>
     *
     * @param eventBus
     *            a {@link com.google.gwt.event.shared.HandlerManager} object.
     */
    public GoogleMapsPanel(final HandlerManager eventBus) {
        m_eventBus = eventBus;
        initWidget(uiBinder.createAndBindUi(this));

        initializeMapPanel();

        m_mapWidget.addMapMoveEndHandler(new MapMoveEndHandler() {
            @Override
            public void onMoveEnd(MapMoveEndEvent event) {
                m_eventBus.fireEvent(new MapPanelBoundsChangedEvent(getBounds()));
            }
        });
    }

    /**
     * Gets the map widget.
     *
     * @return the map widget
     */
    private MapWidget getMapWidget() {
        return m_mapWidget;
    }

    /** {@inheritDoc} */
    @Override
    public void showLocationDetails(final String name, final String htmlTitle, final String htmlContent) {
        final Marker m = m_markers.get(name);

        getMapWidget().savePosition();
        getMapWidget().setCenter(m.getLatLng());
        if (m != null) {
            final VerticalPanel panel = new VerticalPanel();
            panel.add(new Label(htmlTitle));
            panel.add(new HTML(htmlContent));
            getMapWidget().getInfoWindow().open(m.getLatLng(), new InfoWindowContent(panel.toString()));
            getMapWidget().getInfoWindow().addInfoWindowCloseClickHandler(new InfoWindowCloseClickHandler() {
                @Override
                public void onCloseClick(InfoWindowCloseClickEvent event) {
                    getMapWidget().returnToSavedPosition();
                }
            });
        }
    }

    /**
     * <p>
     * getBounds
     * </p>
     * .
     *
     * @return a {@link org.opennms.features.poller.remote.gwt.client.GWTBounds}
     *         object.
     */
    @Override
    public GWTBounds getBounds() {
        return toGWTBounds(getMapWidget().getBounds());
    }

    /** {@inheritDoc} */
    @Override
    public void setBounds(GWTBounds b) {
        LatLngBounds bounds = toLatLngBounds(b);
        getMapWidget().setCenter(bounds.getCenter(), getMapWidget().getBoundsZoomLevel(bounds));
    }

    /**
     * Initialize map panel.
     */
    private void initializeMapPanel() {
        getMapWidget().setSize("100%", "100%");
        getMapWidget().setUIToDefault();
        getMapWidget().addControl(new LargeMapControl());
        getMapWidget().setScrollWheelZoomEnabled(true);

        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(final ResizeEvent resizeEvent) {
                if (getMapWidget() != null) {
                    getMapWidget().checkResizeAndCenter();
                }
            }
        });
    }

    /**
     * Adds the overlay.
     *
     * @param newMarker
     *            the new marker
     */
    private void addOverlay(final Marker newMarker) {
        getMapWidget().addOverlay(newMarker);
    }

    /**
     * Creates the marker.
     *
     * @param marker
     *            the marker
     * @return the marker
     */
    private Marker createMarker(final GWTMarkerState marker) {
        final Icon icon = Icon.newInstance();
        icon.setIconSize(Size.newInstance(32, 32));
        icon.setIconAnchor(Point.newInstance(16, 32));
        String markerImageURL = marker.getImageURL();
        icon.setImageURL(markerImageURL);

        final MarkerOptions markerOptions = MarkerOptions.newInstance();
        markerOptions.setAutoPan(true);
        markerOptions.setClickable(true);
        markerOptions.setTitle(marker.getName());
        markerOptions.setIcon(icon);

        Marker m = new Marker(toLatLng(marker.getLatLng()), markerOptions);
        m.setVisible(marker.isVisible());
        m.addMarkerClickHandler(new DefaultMarkerClickHandler(marker));
        return m;
    }

    /** {@inheritDoc} */
    @Override
    public void placeMarker(final GWTMarkerState marker) {
        m_markerStates.put(marker.getName(), marker);

        Marker m = m_markers.get(marker.getName());
        if (m == null) {
            m = createMarker(marker);
            m_markers.put(marker.getName(), m);
            addOverlay(m);
        }
        updateMarkerFromState(m, marker);
    }

    /**
     * Update marker from state.
     *
     * @param m
     *            the m
     * @param marker
     *            the marker
     */
    private void updateMarkerFromState(Marker m, GWTMarkerState marker) {
        m.setImage(marker.getImageURL());
        m.setVisible(marker.isVisible());
    }

    /**
     * The Class DefaultMarkerClickHandler.
     */
    private final class DefaultMarkerClickHandler implements MarkerClickHandler {

        /** The m_marker. */
        private final GWTMarkerState m_marker;

        /**
         * Instantiates a new default marker click handler.
         *
         * @param marker
         *            the marker
         */
        DefaultMarkerClickHandler(GWTMarkerState marker) {
            m_marker = marker;
        }

        /* (non-Javadoc)
         * @see com.google.gwt.maps.client.event.MarkerClickHandler#onClick(com.google.gwt.maps.client.event.MarkerClickHandler.MarkerClickEvent)
         */
        @Override
        public void onClick(final MarkerClickEvent mke) {
            // showLocationDetails(m_marker);
            m_eventBus.fireEvent(new GWTMarkerClickedEvent(m_marker));
        }
    }

    /**
     * <p>
     * getWidget
     * </p>
     * .
     *
     * @return a {@link com.google.gwt.user.client.ui.Widget} object.
     */
    @Override
    public Widget getWidget() {
        return this;
    }

}
