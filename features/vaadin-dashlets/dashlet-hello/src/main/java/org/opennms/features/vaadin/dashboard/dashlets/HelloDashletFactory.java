package org.opennms.features.vaadin.dashboard.dashlets;

import org.opennms.features.vaadin.dashboard.model.AbstractDashletFactory;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;

public class HelloDashletFactory extends AbstractDashletFactory {
    public HelloDashletFactory() {
        super("Hello");

        addRequiredParameter("TEXT", "Hello World");
    }

    public Dashlet newDashletInstance(DashletSpec dashletSpec) {
        return new HelloDashlet(dashletSpec);
    }
}
