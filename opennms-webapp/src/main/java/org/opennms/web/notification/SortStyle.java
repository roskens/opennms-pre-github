package org.opennms.web.notification;

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
    RESPONDER("responder","answeredBy",true),
    PAGETIME("pagetime","pageTime",true),
    RESPONDTIME("respondtime","respondTime",true),
    NODE("node","node.label", true),
    INTERFACE("interface", "node.label", true),
    SERVICE("service","serviceType.name",true),
    ID("id", "notifyId", true),
    REVERSE_RESPONDER("rev_responder", "answeredBy", false),
    REVERSE_PAGETIME("rev_pagetime", "pageTime", false),
    REVERSE_RESPONDTIME("rev_respondtime","respondTime",false),
    REVERSE_NODE("rev_node", "node.label", false),
    REVERSE_INTERFACE("rev_interface", "node.label", false),
    REVERSE_SERVICE("rev_service", "serviceType.name", false),
    REVERSE_ID("rev_id", "notifyId", false);

    /** Constant <code>DEFAULT_SORT_STYLE</code> */
    public static final SortStyle DEFAULT_SORT_STYLE = SortStyle.ID;

    private static final Map<String, SortStyle> m_sortStylesString;
    private boolean m_desc;
    private String m_dbColumn;

    private String m_shortName;

    static {
        m_sortStylesString = new HashMap<String, SortStyle>();
        for (SortStyle sortStyle : SortStyle.values()) {
            m_sortStylesString.put(sortStyle.getShortName(), sortStyle);

        }
    }

    SortStyle(String shortName, String dbColumn, boolean desc) {
        m_shortName = shortName;
        m_dbColumn = dbColumn;
        m_desc = desc;
    }

    /**
     * <p>toString</p>
     *
     * @return a {@link String} object.
     */
    @Override
    public String toString() {
        return ("SortStyle." + getName());
    }

    /**
     * <p>getName</p>
     *
     * @return a {@link String} object.
     */
    public String getName() {
        return name();
    }

    /**
     * <p>getShortName</p>
     *
     * @return a {@link String} object.
     */
    public String getShortName() {
        return m_shortName;
    }

    /**
     * <p>getSortStyle</p>
     *
     * @param sortStyleString a {@link String} object.
     * @return a {@link SortStyle} object.
     */
    public static SortStyle getSortStyle(String sortStyleString) {
        Assert.notNull(sortStyleString, "Cannot take null parameters.");

        return m_sortStylesString.get(sortStyleString.toLowerCase());
    }

   public SortRule toSortRule() {
       return new SortRule(m_dbColumn, m_desc);
   }

}
