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
package org.opennms.features.vaadin.nodemaps.internal.gwt.client.ui.controls.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.discotools.gwt.leaflet.client.controls.Control;
import org.discotools.gwt.leaflet.client.jsobject.JSObject;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.NodeMarker;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.SearchConsumer;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.SearchOptions;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.DomEvent;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.DomEventCallback;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.event.SearchEventCallback;
import org.opennms.features.vaadin.nodemaps.internal.gwt.client.ui.MarkerContainer;

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * The Class SearchControl.
 */
public class SearchControl extends Control {

    /** The logger. */
    Logger logger = Logger.getLogger(getClass().getName());

    /** The Constant m_labels. */
    private static final HashMap<String, String> m_labels;
    static {
        m_labels = new HashMap<String, String>();
        m_labels.put("nodeid", "Node&nbsp;ID");
        m_labels.put("description", "Description");
        m_labels.put("ipaddress", "IP&nbsp;Address");
        m_labels.put("maintcontract", "Maint.&nbsp;Contract");
        m_labels.put("foreignsource", "Foreign&nbsp;Source");
        m_labels.put("foreignid", "Foreign&nbsp;ID");
    }

    /** The m_container. */
    private HTMLPanel m_container;

    /** The m_input box. */
    private SearchTextBox m_inputBox;

    /** The m_history wrapper. */
    private HistoryWrapper m_historyWrapper;

    /** The m_submit icon. */
    private HTML m_submitIcon;

    /** The m_search consumer. */
    private SearchConsumer m_searchConsumer;

    /** The m_marker container. */
    private MarkerContainer m_markerContainer;

    /** The m_change callback. */
    private SearchEventCallback m_changeCallback;

    /** The m_auto complete. */
    private CellList<NodeMarker> m_autoComplete;

    /** The m_state manager. */
    private SearchStateManager m_stateManager;

    /** The m_selection model. */
    private SingleSelectionModel<NodeMarker> m_selectionModel;

    /** The m_updated. */
    private Set<Widget> m_updated = new HashSet<Widget>();

    /**
     * Instantiates a new search control.
     *
     * @param searchConsumer
     *            the search consumer
     * @param markerContainer
     *            the marker container
     */
    public SearchControl(final SearchConsumer searchConsumer, final MarkerContainer markerContainer) {
        this(searchConsumer, markerContainer, new SearchOptions());
    }

    /**
     * Instantiates a new search control.
     *
     * @param searchConsumer
     *            the search consumer
     * @param markerContainer
     *            the marker container
     * @param options
     *            the options
     */
    public SearchControl(final SearchConsumer searchConsumer, final MarkerContainer markerContainer,
            final SearchOptions options) {
        super(JSObject.createJSObject());
        setJSObject(SearchControlImpl.create(this, options.getJSObject()));
        logger.log(Level.INFO, "new SearchControl()");
        m_searchConsumer = searchConsumer;
        m_markerContainer = markerContainer;
        m_selectionModel = new SingleSelectionModel<NodeMarker>();

        m_historyWrapper = new HistoryWrapper();

        initializeContainerWidget();
        initializeInputWidget();
        initializeSubmitWidget();
        initializeCellAutocompleteWidget();
        initializeSearchStateManager();
    }

    /**
     * Focus.
     */
    public void focus() {
        m_inputBox.setFocus(true);
    }

    /**
     * Do on add.
     *
     * @param map
     *            the map
     * @return the element
     */
    public Element doOnAdd(final JavaScriptObject map) {
        logger.log(Level.INFO, "onAdd() called");

        m_container.add(m_inputBox);
        m_container.add(m_submitIcon);
        m_container.add(m_autoComplete);

        return m_container.getElement();
    }

    /**
     * Do on remove.
     *
     * @param map
     *            the map
     * @return the search control
     */
    public SearchControl doOnRemove(final JavaScriptObject map) {
        logger.log(Level.INFO, "onRemove() called");
        if (m_changeCallback != null) {
            DomEvent.removeListener(m_changeCallback);
        }
        return this;
    }

    /**
     * Refresh.
     */
    public void refresh() {
        final List<NodeMarker> markers = m_markerContainer.getMarkers();
        m_autoComplete.setRowData(markers);
    }

    /**
     * Update autocomplete style.
     *
     * @param widget
     *            the widget
     */
    protected void updateAutocompleteStyle(final Widget widget) {
        // we only need to do this once
        if (m_updated.contains(widget)) {
            return;
        }

        final Style style = widget.getElement().getStyle();
        style.setPosition(Position.ABSOLUTE);
        final int left = 5;
        final int top = m_container.getOffsetHeight() + 5;
        style.setLeft(left, Unit.PX);
        style.setTop(top, Unit.PX);
        DomEvent.stopEventPropagation(widget);
        m_updated.add(widget);
    }

    /**
     * Initialize search state manager.
     */
    private void initializeSearchStateManager() {
        m_stateManager = new SearchStateManager(m_inputBox, m_historyWrapper) {
            @Override
            public void refresh() {
                m_searchConsumer.setSearchString(m_inputBox.getValue());
                // it's the search consumer's job to trigger an update in any UI
                // elements
                m_searchConsumer.refresh();

                final List<NodeMarker> markers = m_markerContainer.getMarkers();
                final NodeMarker selected = m_selectionModel.getSelectedObject();
                final NodeMarker firstMarker = markers.size() > 0 ? markers.get(0) : null;
                if (selected == null) {
                    if (firstMarker != null) {
                        m_selectionModel.setSelected(firstMarker, true);
                    }
                } else {
                    if (!markers.contains(selected)) {
                        if (firstMarker != null) {
                            m_selectionModel.setSelected(firstMarker, true);
                        } else {
                            m_selectionModel.setSelected(selected, false);
                        }
                    }
                }
            }

            @Override
            public void clearSearchInput() {
                m_inputBox.setValue("");
            }

            @Override
            public void focusAutocomplete() {
                m_autoComplete.setFocus(true);
                if (m_selectionModel.getSelectedObject() == null) {
                    final List<NodeMarker> markers = m_markerContainer.getMarkers();
                    if (markers.size() > 0) {
                        m_selectionModel.setSelected(markers.get(0), true);
                    }
                }
            }

            @Override
            public void showAutocomplete() {
                final List<NodeMarker> markers = m_markerContainer.getMarkers();
                if (markers.size() > 0) {
                    m_selectionModel.setSelected(markers.get(0), true);
                }
                m_autoComplete.setVisible(true);
                updateAutocompleteStyle(m_autoComplete);
            }

            @Override
            public void hideAutocomplete() {
                m_autoComplete.setVisible(false);
            }

            @Override
            public void entrySelected() {
                final NodeMarker selected = m_selectionModel.getSelectedObject();
                if (selected != null) {
                    m_inputBox.setValue("nodeLabel=" + selected.getNodeLabel());
                }
            }

            @Override
            public void focusInput() {
                m_inputBox.setFocus(true);
            }

        };
    }

    /**
     * Initialize container widget.
     */
    private void initializeContainerWidget() {
        m_container = new HTMLPanel("<div class\"leaflet-control-search\"></div>");
        m_container.addStyleName("leaflet-control");
    }

    /**
     * Initialize input widget.
     */
    private void initializeInputWidget() {
        m_inputBox = new SearchTextBox();
        m_inputBox.addStyleName("search-input");
        m_inputBox.getElement().setAttribute("placeholder", "Search...");
        m_inputBox.getElement().setAttribute("type", "search");
        m_inputBox.setMaxLength(40);
        m_inputBox.setVisibleLength(40);
        m_inputBox.setValue(m_searchConsumer.getSearchString());

        DomEvent.stopEventPropagation(m_inputBox);

        m_changeCallback = new SearchEventCallback(new String[] { "keydown", "change", "cut", "paste", "search" },
                                                   m_inputBox, m_searchConsumer) {
            @Override
            protected void onEvent(final NativeEvent event) {
                m_stateManager.handleInputEvent(event);
            }
        };
        DomEvent.addListener(m_changeCallback);
    }

    /**
     * Initialize submit widget.
     */
    private void initializeSubmitWidget() {
        m_submitIcon = new HTML();
        m_submitIcon.addStyleName("search-button");
        m_submitIcon.setTitle("Search locations...");

        DomEvent.stopEventPropagation(m_submitIcon);
        DomEvent.addListener(new DomEventCallback("click", m_submitIcon) {
            @Override
            protected void onEvent(final NativeEvent event) {
                m_inputBox.setFocus(true);
                m_stateManager.handleSearchIconEvent(event);
            }
        });
    }

    /**
     * Initialize cell autocomplete widget.
     */
    private void initializeCellAutocompleteWidget() {
        final AbstractSafeHtmlRenderer<NodeMarker> renderer = new AbstractSafeHtmlRenderer<NodeMarker>() {
            @Override
            public SafeHtml render(final NodeMarker marker) {
                final SafeHtmlBuilder builder = new SafeHtmlBuilder();
                final String searchString = m_searchConsumer.getSearchString().toLowerCase();

                builder.appendHtmlConstant("<div class=\"autocomplete-label\">");
                builder.appendHtmlConstant(marker.getNodeLabel());
                builder.appendHtmlConstant("</div>");
                String additionalSearchInfo = null;
                if (searchString.contains(":") || searchString.contains("=")) {
                    final String searchKey = searchString.replaceAll("[\\:\\=].*$", "").toLowerCase();
                    logger.log(Level.INFO, "searchKey = " + searchKey);

                    if ("category".equals(searchKey) || "categories".equals(searchKey)) {
                        final String categoryString = marker.getCategoriesAsString();
                        if (categoryString.length() > 0) {
                            additionalSearchInfo = categoryString;
                        }
                    }

                    for (final String key : marker.getTextPropertyNames()) {
                        final String lowerKey = key.toLowerCase();
                        if (lowerKey.equals(searchKey) && m_labels.containsKey(lowerKey)) {
                            additionalSearchInfo = m_labels.get(lowerKey) + ": " + marker.getProperty(key);
                            break;
                        }
                    }
                }

                if (additionalSearchInfo != null) {
                    builder.appendHtmlConstant("<div class=\"autocomplete-additional-info\">").appendHtmlConstant(additionalSearchInfo).appendHtmlConstant("</div>");
                }

                return builder.toSafeHtml();
            }
        };

        final AbstractSafeHtmlCell<NodeMarker> cell = new AbstractSafeHtmlCell<NodeMarker>(renderer, "keydown",
                                                                                           "click", "dblclick",
                                                                                           "touchstart") {

            @Override
            public void onBrowserEvent(final Context context, final com.google.gwt.dom.client.Element parent,
                    final NodeMarker value, final NativeEvent event, final ValueUpdater<NodeMarker> valueUpdater) {
                if (m_stateManager.handleAutocompleteEvent(event)) {
                    super.onBrowserEvent(context, parent, value, event, valueUpdater);
                }
            }

            @Override
            protected void render(final Context context, final SafeHtml data, final SafeHtmlBuilder builder) {
                builder.appendHtmlConstant("<div class=\"autocomplete-entry\">");
                if (data != null) {
                    builder.append(data);
                }
                builder.appendHtmlConstant("</div>");
            }
        };

        m_autoComplete = new CellList<NodeMarker>(cell);
        m_autoComplete.setSelectionModel(m_selectionModel);
        m_autoComplete.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
        m_autoComplete.setVisible(false);
        m_autoComplete.addStyleName("search-autocomplete");
    }

    /**
     * The Class HistoryWrapper.
     */
    private class HistoryWrapper implements ValueItem {

        /* (non-Javadoc)
         * @see org.opennms.features.vaadin.nodemaps.internal.gwt.client.ui.controls.search.ValueItem#getValue()
         */
        @Override
        public String getValue() {
            return History.getToken();
        }

        /* (non-Javadoc)
         * @see org.opennms.features.vaadin.nodemaps.internal.gwt.client.ui.controls.search.ValueItem#setValue(java.lang.String)
         */
        @Override
        public void setValue(final String value) {
            History.newItem(value);
        }

    }
}
