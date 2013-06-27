package org.opennms.features.vaadin.dashboard.dashlets;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;

public class UndefinedDashlet extends VerticalLayout implements Dashlet {

    public UndefinedDashlet(DashletSpec dashletSpec) {
        Label label = new Label("The defined dashlet could not be found!");
        addComponent(label);
        setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        setCaption(getName());
    }

    public String getName() {
        return "Undefined";
    }

    @Override
    public boolean isBoosted() {
        return false;
    }

}
