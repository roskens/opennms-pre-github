package org.opennms.features.vaadin.dashboard.dashlets;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;

public class UlfDashlet extends Image implements Dashlet {

    public UlfDashlet(DashletSpec dashletSpec) {
        super(null, new ThemeResource("img/ulf.png"));
        setCaption(getName());
        setSizeFull();
    }

    public boolean allowAdvance() {
        return true;
    }

    public String getName() {
        return "Ulf";
    }

    @Override
    public boolean isBoosted() {
        return false;
    }
}
