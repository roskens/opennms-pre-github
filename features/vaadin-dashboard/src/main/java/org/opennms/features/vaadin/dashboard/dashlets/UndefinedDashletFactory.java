package org.opennms.features.vaadin.dashboard.dashlets;

import org.opennms.features.vaadin.dashboard.model.AbstractDashletFactory;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;

public class UndefinedDashletFactory extends AbstractDashletFactory {

    public UndefinedDashletFactory() {
        super("Undefined");
    }

    public Dashlet newDashletInstance(DashletSpec dashletSpec) {
        return new UndefinedDashlet(dashletSpec);
    }
}
