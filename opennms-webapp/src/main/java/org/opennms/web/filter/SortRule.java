package org.opennms.web.filter;


public class SortRule {
    private final boolean desc;
    private final String beanProperty;

    public SortRule(String beanProperty, boolean desc) {
        this.desc = desc;
        this.beanProperty = beanProperty;
    }

    public String getBeanProperty() {
        return beanProperty;
    }

    public boolean isAsc() {
        return !desc;
    }
}
