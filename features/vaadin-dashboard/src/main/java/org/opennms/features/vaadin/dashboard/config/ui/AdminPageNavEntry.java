package org.opennms.features.vaadin.dashboard.config.ui;

import org.opennms.web.navigate.PageNavEntry;

public class AdminPageNavEntry implements PageNavEntry {

    private String m_name;
    private String m_url;

    @Override
    public String getName() {
        return m_name;
    }

    public void setName(final String name) {
        this.m_name = name;
    }

    @Override
    public String getUrl() {
        return m_url;
    }

    public void setUrl(final String url) {
        this.m_url = url;
    }
}