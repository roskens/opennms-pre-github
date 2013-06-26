package org.opennms.web.filter;


public class NullFilter extends BaseFilter {
    public NullFilter(String propertyName) {
        super("NULLFILTER", propertyName);
    }

    @Override
    public String getValueString() {
        return null;  // TODO MVR JPA implement me
    }

    @Override
    public String getTextDescription() {
        return null;   // TODO MVR JPA implement me
    }
}
