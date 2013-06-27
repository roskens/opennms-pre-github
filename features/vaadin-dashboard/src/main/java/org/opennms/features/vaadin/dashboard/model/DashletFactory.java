package org.opennms.features.vaadin.dashboard.model;

import java.util.Map;
import java.util.Set;

public interface DashletFactory {
    public abstract Dashlet newDashletInstance(DashletSpec dashletSpec);

    public String getName();

    public Map<String, String> getRequiredParameters();
}
