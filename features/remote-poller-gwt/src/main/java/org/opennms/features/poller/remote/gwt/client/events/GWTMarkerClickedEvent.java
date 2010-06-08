package org.opennms.features.poller.remote.gwt.client.events;


import org.opennms.features.poller.remote.gwt.client.map.MarkerState;

import com.google.gwt.event.shared.GwtEvent;

public class GWTMarkerClickedEvent extends GwtEvent<GWTMarkerClickedEventHandler> {
    
    public final static Type<GWTMarkerClickedEventHandler> TYPE = new Type<GWTMarkerClickedEventHandler>();
    
    private MarkerState m_marker;
    
    public GWTMarkerClickedEvent(MarkerState marker) {
        setMarker(marker);
    }

    @Override
    protected void dispatch(GWTMarkerClickedEventHandler handler) {
        handler.onGWTMarkerClicked(this);
        
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<GWTMarkerClickedEventHandler> getAssociatedType() {
        return TYPE;
    }

    private void setMarker(MarkerState marker) {
        m_marker = marker;
    }

    public MarkerState getMarkerState() {
        return m_marker;
    }

}
