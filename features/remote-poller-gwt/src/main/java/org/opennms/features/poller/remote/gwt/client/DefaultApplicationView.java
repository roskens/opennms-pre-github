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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.opennms.features.poller.remote.gwt.client.FilterPanel.StatusSelectionChangedEvent;
import org.opennms.features.poller.remote.gwt.client.location.LocationInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class DefaultApplicationView.
 */
public class DefaultApplicationView implements ApplicationView, ResizeHandler {

    /**
     * The Interface Binder.
     */
    interface Binder extends UiBinder<DockLayoutPanel, DefaultApplicationView> {
    }

    /** The Constant BINDER. */
    private static final Binder BINDER = GWT.create(Binder.class);

    /**
     * The Interface LinkStyles.
     */
    interface LinkStyles extends CssResource {

        /**
         * Active link.
         *
         * @return the string
         */
        String activeLink();
    }

    /** The location panel. */
    @UiField
    protected LocationPanel locationPanel;

    /** The main panel. */
    @UiField
    protected DockLayoutPanel mainPanel;

    /** The split panel. */
    @UiField
    protected SplitLayoutPanel splitPanel;

    /** The location link. */
    @UiField
    protected Hyperlink locationLink;

    /** The application link. */
    @UiField
    protected Hyperlink applicationLink;

    /** The update timestamp. */
    @UiField
    protected Label updateTimestamp;

    /** The link styles. */
    @UiField
    protected LinkStyles linkStyles;

    /** The statuses panel. */
    @UiField
    protected HorizontalPanel statusesPanel;

    /** The status down. */
    @UiField
    protected CheckBox statusDown;

    /** The status disconnected. */
    @UiField
    protected CheckBox statusDisconnected;

    /** The status marginal. */
    @UiField
    protected CheckBox statusMarginal;

    /** The status up. */
    @UiField
    protected CheckBox statusUp;

    /** The status stopped. */
    @UiField
    protected CheckBox statusStopped;

    /** The status unknown. */
    @UiField
    protected CheckBox statusUnknown;

    /** The m_map panel. */
    private final MapPanel m_mapPanel;

    /** The m_event bus. */
    private final HandlerManager m_eventBus;

    /** The m_presenter. */
    private Application m_presenter;

    /** The Constant UPDATE_TIMESTAMP_FORMAT. */
    static final DateTimeFormat UPDATE_TIMESTAMP_FORMAT = DateTimeFormat.getMediumDateTimeFormat();

    /**
     * Instantiates a new default application view.
     *
     * @param presenter
     *            the presenter
     * @param eventBus
     *            the event bus
     * @param mapPanel
     *            the map panel
     */
    public DefaultApplicationView(Application presenter, HandlerManager eventBus, MapPanel mapPanel) {
        m_presenter = presenter;
        m_eventBus = eventBus;
        m_mapPanel = mapPanel;
        BINDER.createAndBindUi(this);

        locationPanel.setEventBus(eventBus);
        setupWindow();

    }

    /**
     * On down clicked.
     *
     * @param event
     *            the event
     */
    @UiHandler("statusDown")
    public void onDownClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.DOWN, getStatusDown().getValue()));
    }

    /**
     * On disconnected clicked.
     *
     * @param event
     *            the event
     */
    @UiHandler("statusDisconnected")
    public void onDisconnectedClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.DISCONNECTED, getStatusDisconnected().getValue()));
    }

    /**
     * On marginal clicked.
     *
     * @param event
     *            the event
     */
    @UiHandler("statusMarginal")
    public void onMarginalClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.MARGINAL, getStatusMarginal().getValue()));
    }

    /**
     * On up clicked.
     *
     * @param event
     *            the event
     */
    @UiHandler("statusUp")
    public void onUpClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.UP, getStatusUp().getValue()));
    }

    /**
     * On stopped clicked.
     *
     * @param event
     *            the event
     */
    @UiHandler("statusStopped")
    public void onStoppedClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.STOPPED, getStatusStopped().getValue()));
    }

    /**
     * On unknown clicked.
     *
     * @param event
     *            the event
     */
    @UiHandler("statusUnknown")
    public void onUnknownClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.UNKNOWN, getStatusUnknown().getValue()));
    }

    /**
     * Gets the event bus.
     *
     * @return the event bus
     */
    private HandlerManager getEventBus() {
        return m_eventBus;
    }

    /**
     * Gets the main panel.
     *
     * @return the main panel
     */
    private DockLayoutPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Gets the split panel.
     *
     * @return the split panel
     */
    private SplitLayoutPanel getSplitPanel() {
        return splitPanel;
    }

    /**
     * Gets the statuses panel.
     *
     * @return the statuses panel
     */
    private HorizontalPanel getStatusesPanel() {
        return statusesPanel;
    }

    /**
     * Gets the status down.
     *
     * @return the status down
     */
    private CheckBox getStatusDown() {
        return statusDown;
    }

    /**
     * Gets the status disconnected.
     *
     * @return the status disconnected
     */
    private CheckBox getStatusDisconnected() {
        return statusDisconnected;
    }

    /**
     * Gets the status marginal.
     *
     * @return the status marginal
     */
    private CheckBox getStatusMarginal() {
        return statusMarginal;
    }

    /**
     * Gets the status up.
     *
     * @return the status up
     */
    private CheckBox getStatusUp() {
        return statusUp;
    }

    /**
     * Gets the status stopped.
     *
     * @return the status stopped
     */
    private CheckBox getStatusStopped() {
        return statusStopped;
    }

    /**
     * Gets the status unknown.
     *
     * @return the status unknown
     */
    private CheckBox getStatusUnknown() {
        return statusUnknown;
    }

    /**
     * Gets the location panel.
     *
     * @return the location panel
     */
    private LocationPanel getLocationPanel() {
        return locationPanel;
    }

    /**
     * Gets the location link.
     *
     * @return the location link
     */
    private Hyperlink getLocationLink() {
        return locationLink;
    }

    /**
     * Gets the application link.
     *
     * @return the application link
     */
    private Hyperlink getApplicationLink() {
        return applicationLink;
    }

    /**
     * Gets the link styles.
     *
     * @return the link styles
     */
    private LinkStyles getLinkStyles() {
        return linkStyles;
    }

    /**
     * Gets the update timestamp.
     *
     * @return the update timestamp
     */
    private Label getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Gets the presenter.
     *
     * @return the presenter
     */
    private Application getPresenter() {
        return m_presenter;
    }

    /**
     * <p>
     * onApplicationClick
     * </p>
     * .
     *
     * @param event
     *            a {@link com.google.gwt.event.dom.client.ClickEvent} object.
     */
    @UiHandler("applicationLink")
    public void onApplicationClick(ClickEvent event) {
        if (getApplicationLink().getStyleName().contains(getLinkStyles().activeLink())) {
            // This link is already selected, do nothing
        } else {
            getPresenter().onApplicationViewSelected();
            getApplicationLink().addStyleName(getLinkStyles().activeLink());
            getLocationLink().removeStyleName(getLinkStyles().activeLink());
            getLocationPanel().showApplicationList();
            getLocationPanel().showApplicationFilters(true);
            getLocationPanel().resizeDockPanel();
        }
    }

    /**
     * <p>
     * onLocationClick
     * </p>
     * .
     *
     * @param event
     *            a {@link com.google.gwt.event.dom.client.ClickEvent} object.
     */
    @UiHandler("locationLink")
    public void onLocationClick(ClickEvent event) {
        if (getLocationLink().getStyleName().contains(getLinkStyles().activeLink())) {
            // This link is already selected, do nothing
        } else {
            getPresenter().onLocationViewSelected();
            getLocationLink().addStyleName(getLinkStyles().activeLink());
            getApplicationLink().removeStyleName(getLinkStyles().activeLink());
            getLocationPanel().showLocationList();
            getLocationPanel().showApplicationFilters(false);
            getLocationPanel().resizeDockPanel();
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * org.opennms.features.poller.remote.gwt.client.ApplicationView#updateTimestamp
     * ()
     */
    @Override
    public void updateTimestamp() {
        getUpdateTimestamp().setText("Last update: " + UPDATE_TIMESTAMP_FORMAT.format(new Date()));
    }

    /**
     * Gets the app height.
     *
     * @return the app height
     */
    private Integer getAppHeight() {
        final com.google.gwt.user.client.Element e = getMainPanel().getElement();
        int extraHeight = e.getAbsoluteTop();
        return Window.getClientHeight() - extraHeight;
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#
     * getSelectedStatuses()
     */
    @Override
    public Set<Status> getSelectedStatuses() {

        Set<Status> statuses = new HashSet<Status>();
        for (final Widget w : getStatusesPanel()) {
            if (w instanceof CheckBox) {
                final CheckBox cb = (CheckBox) w;
                if (cb.getValue()) {
                    statuses.add(Status.valueOf(cb.getFormValue()));
                }
            }
        }
        return statuses;
    }

    /**
     * Setup window.
     */
    private void setupWindow() {
        Window.setTitle("OpenNMS - Remote Monitor");
        Window.enableScrolling(false);
        Window.setMargin("0px");
        Window.addResizeHandler(this);
    }

    /*
     * (non-Javadoc)
     * @see
     * org.opennms.features.poller.remote.gwt.client.ApplicationView#initialize
     * ()
     */
    @Override
    public void initialize() {
        getSplitPanel().add(getMapPanel().getWidget());
        getSplitPanel().setWidgetMinSize(getLocationPanel(), 255);
        getMainPanel().setSize("100%", "100%");
        RootPanel.get("map").add(getMainPanel());

        updateTimestamp();
        onLocationClick(null);

        onResize(null);
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#
     * updateSelectedApplications(java.util.Set)
     */
    @Override
    public void updateSelectedApplications(Set<ApplicationInfo> applications) {
        getLocationPanel().updateSelectedApplications(applications);
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#
     * updateLocationList(java.util.ArrayList)
     */
    @Override
    public void updateLocationList(ArrayList<LocationInfo> locationsForLocationPanel) {
        getLocationPanel().updateLocationList(locationsForLocationPanel);
    }

    /*
     * (non-Javadoc)
     * @see
     * org.opennms.features.poller.remote.gwt.client.ApplicationView#setSelectedTag
     * (java.lang.String, java.util.List)
     */
    @Override
    public void setSelectedTag(String selectedTag, List<String> allTags) {
        getLocationPanel().clearTagPanel();
        getLocationPanel().addAllTags(allTags);
        getLocationPanel().selectTag(selectedTag);
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#
     * updateApplicationList(java.util.ArrayList)
     */
    @Override
    public void updateApplicationList(ArrayList<ApplicationInfo> applications) {
        getLocationPanel().updateApplicationList(applications);
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#
     * updateApplicationNames(java.util.TreeSet)
     */
    @Override
    public void updateApplicationNames(TreeSet<String> allApplicationNames) {
        getLocationPanel().updateApplicationNames(allApplicationNames);
    }

    /**
     * Gets the map panel.
     *
     * @return the map panel
     */
    private MapPanel getMapPanel() {
        return m_mapPanel;
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#
     * fitMapToLocations
     * (org.opennms.features.poller.remote.gwt.client.GWTBounds)
     */
    @Override
    public void fitMapToLocations(GWTBounds locationBounds) {
        if (getMapPanel() instanceof SmartMapFit) {
            ((SmartMapFit) getMapPanel()).fitToBounds();
        } else {
            // TODO: Zoom in to visible locations on startup

            getMapPanel().setBounds(locationBounds);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * org.opennms.features.poller.remote.gwt.client.ApplicationView#getMapBounds
     * ()
     */
    @Override
    public GWTBounds getMapBounds() {
        return getMapPanel().getBounds();
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#
     * showLocationDetails(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void showLocationDetails(final String locationName, String htmlTitle, String htmlContent) {
        getMapPanel().showLocationDetails(locationName, htmlTitle, htmlContent);
    }

    /*
     * (non-Javadoc)
     * @see
     * org.opennms.features.poller.remote.gwt.client.ApplicationView#placeMarker
     * (org.opennms.features.poller.remote.gwt.client.GWTMarkerState)
     */
    @Override
    public void placeMarker(final GWTMarkerState markerState) {
        getMapPanel().placeMarker(markerState);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.poller.remote.gwt.client.ApplicationView#setStatusMessage(java.lang.String)
     */
    @Override
    public void setStatusMessage(String statusMessage) {
        // getUpdateTimestamp().setText(statusMessage);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.event.logical.shared.ResizeHandler#onResize(com.google.gwt.event.logical.shared.ResizeEvent)
     */
    @Override
    public void onResize(ResizeEvent event) {
        getMainPanel().setHeight(getAppHeight().toString());
    }
}
