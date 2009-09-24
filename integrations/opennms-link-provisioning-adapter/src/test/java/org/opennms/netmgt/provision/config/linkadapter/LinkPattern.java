package org.opennms.netmgt.provision.config.linkadapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkPattern {
    private Pattern m_pattern;
    private String m_template;

    public LinkPattern(String pattern, String template) {
        m_pattern = Pattern.compile(pattern, Pattern.CANON_EQ | Pattern.DOTALL);
        m_template = template;
    }

    public Matcher getMatcher(String endPoint) {
        return m_pattern.matcher(endPoint);
    }

    public String resolveTemplate(String endPoint) {
        Matcher m = getMatcher(endPoint);

        if (m.matches()) {
            return m.replaceAll(m_template);
        }

        return null;
    }

}
