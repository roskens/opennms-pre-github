package org.opennms.web.alarm;

import org.opennms.web.filter.SortRule;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class to determine sort style of a query.
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public enum SortStyle {
    SEVERITY("severity", "severity", true),
    FIRSTEVENTTIME("firsteventtime", "firstEventTime", true),
    ID("id", "id", true),
    INTERFACE("interface", "ipAddr", true),
    LASTEVENTTIME("lasteventtime", "lastEventTime", true),
    NODE("node", "node.label", true),
    SERVICE("service", "serviceType.name", true),
    POLLER("poller", "distPoller", true),
    COUNT("count", "counter", true),
    ACKUSER("ackuser", "alarmAckUser", false),
    REVERSE_COUNT("rev_count", "counter", false),
    REVERSE_FIRSTEVENTTIME("rev_firsteventtime", "firstEventTime", false),
    REVERSE_SEVERITY("rev_severity", "serverity", false),
    REVERSE_LASTEVENTTIME("rev_lasteventtime", "lastEventTime", false),
    REVERSE_NODE("rev_node", "node.label", false),
    REVERSE_INTERFACE("rev_interface", "ipAddr", false),
    REVERSE_SERVICE("rev_service", "serviceType.name", false),
    REVERSE_POLLER("rev_poller", "distPoller", true),
    REVERSE_ID("rev_id", "id", false),
    REVERSE_ACKUSER("rev_ackuser", "alarmAckUser", true);

    private static final Map<String, SortStyle> m_sortStylesString;

    static {
        m_sortStylesString = new HashMap<String, SortStyle>();
        for (SortStyle sortStyle : SortStyle.values()) {
            m_sortStylesString.put(sortStyle.getShortName(), sortStyle);
        }
    }

    private final String m_shortName;
    private final boolean m_desc;
    private final String m_beanProperty;

    SortStyle(String shortName, final String beanPropertyName, final boolean desc) {
        m_beanProperty = beanPropertyName;
        m_shortName = shortName;
        m_desc = desc;
    }

    @Override
    public String toString() {
        return ("SortStyle." + getName());
    }

    public String getName() {
        return name();
    }

    public String getShortName() {
        return m_shortName;
    }

    public boolean isDesc() {
        return m_desc;
    }

    public String getBeanPropertyName() {
        return m_beanProperty;
    }

    public static SortStyle getSortStyle(String sortStyleString) {
        Assert.notNull(sortStyleString, "Cannot take null parameters.");
        return m_sortStylesString.get(sortStyleString.toLowerCase());
    }

    public SortRule toSortRule() {
        return new SortRule(getBeanPropertyName(), isDesc());
    }
}
