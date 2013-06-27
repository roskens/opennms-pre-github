package org.opennms.features.vaadin.dashboard.dashlets;

import org.opennms.features.vaadin.dashboard.model.AbstractDashletFactory;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;

public class UlfDashletFactory extends AbstractDashletFactory {
    public UlfDashletFactory() {
        super("Ulf");
    }

    public Dashlet newDashletInstance(DashletSpec dashletSpec) {
        return new UlfDashlet(dashletSpec);
    }

}
