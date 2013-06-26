package org.opennms.web.filter;

import org.opennms.core.criteria.Alias;
import org.opennms.core.criteria.Criteria;
import org.opennms.core.criteria.CriteriaBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchParameter {

    private List<Filter> m_filter;
    private SortRule m_sortRule;  //= SortStyle.LASTEVENTTIME; // TODO MVR JPA add default SortStyle
    private Integer m_limit;
    private Integer m_offset;
    private List<Alias> m_alias = new ArrayList<Alias>();
    private boolean count;
    private Class<?> m_class;

    public SearchParameter(Class<?> clazz, Filter... filter) {
        this(clazz, filter, null, null, null);
    }

    public SearchParameter(Class<?> clazz, Filter[] filter, SortRule sortRule, Integer limit, Integer offset) {
        m_filter = filter == null ? new ArrayList<Filter>() : Arrays.asList(filter);
        m_sortRule = sortRule;
        m_limit = limit;
        m_offset = offset;
    }

    public SearchParameter addFilter(Filter filter) {
        m_filter.add(filter);
        return this;
    }

    public SearchParameter addAlias(String aliasPath, String alias, Alias.JoinType join) {
        m_alias.add(new Alias(aliasPath, alias, join));
        return this;
    }

    public SearchParameter setCount(boolean count) {
        this.count = count;
        return this;
    }

    public Criteria toCriteria() {
        CriteriaBuilder builder = new CriteriaBuilder(m_class);
        if (count) builder.count();
        if (m_limit != null) builder.limit(m_limit);
        if (m_offset != null) builder.offset(m_offset);
        if (m_sortRule != null) builder.orderBy(m_sortRule.getBeanProperty(), m_sortRule.isAsc());

        Criteria criteria = builder.toCriteria();
        criteria.setAliases(m_alias);
        criteria.setRestrictions(Whatever.convert(m_filter));
        return criteria;
    }
}
