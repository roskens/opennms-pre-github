package org.opennms.features.vaadin.dashboard.dashlets;

import org.opennms.features.vaadin.dashboard.model.AbstractDashletFactory;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;

public class MapDashletFactory extends AbstractDashletFactory {
    public MapDashletFactory() {
        super("Map");
    }

    public Dashlet newDashletInstance(DashletSpec dashletSpec) {
        return new MapDashlet(dashletSpec);
    }
}
