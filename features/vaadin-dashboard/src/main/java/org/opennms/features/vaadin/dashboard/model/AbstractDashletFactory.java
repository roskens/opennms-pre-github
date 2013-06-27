package org.opennms.features.vaadin.dashboard.model;

import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractDashletFactory implements DashletFactory {
    protected String name;
    protected Map<String, String> requiredParameters = new TreeMap<String, String>();

    public AbstractDashletFactory(String name) {
        this.name = name;
    }

    protected void addRequiredParameter(String key, String defaultValue) {
        requiredParameters.put(key, defaultValue);
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getRequiredParameters() {
        return requiredParameters;
    }
}
