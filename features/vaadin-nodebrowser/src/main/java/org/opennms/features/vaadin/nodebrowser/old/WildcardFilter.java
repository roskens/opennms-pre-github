package org.opennms.features.vaadin.nodebrowser.old;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

public class WildcardFilter implements Container.Filter {
    protected String regexpString = "";
    protected Object propertyId;

    public WildcardFilter(Object propertyId, String wildcardString) {
        this.propertyId = propertyId;
        if (wildcardString != null) {
            this.regexpString = wildcardToRegex(wildcardString);
        }

        System.out.println("BMRHGA: " + wildcardString + " for property '" + propertyId + "' -> '" + regexpString + "'");
    }

    public static String wildcardToRegex(String wildcard) {
        StringBuffer s = new StringBuffer(wildcard.length());
        s.append('^');
        for (int i = 0, is = wildcard.length(); i < is; i++) {
            char c = wildcard.charAt(i);
            switch (c) {
                case '*':
                    s.append(".*");
                    break;
                case '?':
                    s.append(".");
                    break;
                case '(':
                case ')':
                case '[':
                case ']':
                case '$':
                case '^':
                case '.':
                case '{':
                case '}':
                case '|':
                case '\\':
                    s.append("\\");
                    s.append(c);
                    break;
                default:
                    s.append(c);
                    break;
            }
        }
        s.append('$');
        return (s.toString());
    }

    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
        if (item != null) {
            if (item.getItemProperty(propertyId) != null) {
                return item.getItemProperty(propertyId).toString().matches(regexpString);
            }
        }
        return false;
    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
        return (this.propertyId.equals(propertyId));
    }
}
