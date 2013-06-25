package org.opennms.features.vaadin.dashboard.dashlets;

import com.vaadin.ui.VerticalLayout;
import org.opennms.features.vaadin.dashboard.model.Sandwich;

public class HelloDashlet extends VerticalLayout implements Sandwich {

    public boolean allowAdvance() {
        return true;
    }

    public String getName() {
        return "Hello";
    }

}
