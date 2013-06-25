package org.opennms.features.vaadin.dashboard.config.ui;

import org.opennms.web.navigate.PageNavEntry;

public class AdminPageNavEntry implements PageNavEntry {

    private String name;
    private String url;

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}