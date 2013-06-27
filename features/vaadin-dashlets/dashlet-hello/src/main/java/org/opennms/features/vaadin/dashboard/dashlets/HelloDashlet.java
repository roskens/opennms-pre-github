package org.opennms.features.vaadin.dashboard.dashlets;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;

public class HelloDashlet extends VerticalLayout implements Dashlet {
    public static int instanceCounter = 1;

    public HelloDashlet(DashletSpec dashletSpec) {
        setCaption(getName());

        if (dashletSpec.getParameters().containsKey("TEXT")) {
            addComponent(new Label(dashletSpec.getParameters().get("TEXT") + " #" + instanceCounter));
        }

        instanceCounter++;
    }

    public boolean allowAdvance() {
        return true;
    }

    public String getName() {
        return "Hello";
    }

    @Override
    public boolean isBoosted() {
        return false;
    }

}
