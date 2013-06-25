package org.opennms.features.vaadin.dashboard.dashlets;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.opennms.features.vaadin.dashboard.model.Sandwich;

public class MapDashlet extends VerticalLayout implements Sandwich {
    public boolean allowAdvance() {
        return true;
    }

    public String getName() {
        return "Map";
    }

}
