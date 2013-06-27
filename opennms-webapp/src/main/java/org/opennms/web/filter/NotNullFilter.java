package org.opennms.web.filter;

public class NotNullFilter extends BaseFilter {
    public NotNullFilter(String propertyName) {
        super("NOTNULLFILTER", propertyName);
    }

    @Override
    public String getValueString() {
        return null;  // TODO MVR JPA implement me
    }

    @Override
    public String getTextDescription() {
        return null; // TODO MVR JPA implement me
    }
}
