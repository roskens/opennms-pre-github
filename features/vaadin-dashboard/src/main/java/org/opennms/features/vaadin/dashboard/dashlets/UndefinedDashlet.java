package org.opennms.features.vaadin.dashboard.dashlets;

import com.vaadin.ui.TextField;
import org.opennms.features.vaadin.dashboard.model.Sandwich;
import org.opennms.features.vaadin.dashboard.model.SandwichSpec;

public class UndefinedDashlet extends TextField implements Sandwich {
    public UndefinedDashlet() {
    }

    public boolean allowAdvance() {
        return false;
    }

    public String getName() {
        return "Undefined";
    }

}
