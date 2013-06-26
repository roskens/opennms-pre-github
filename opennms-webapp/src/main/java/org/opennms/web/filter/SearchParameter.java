package org.opennms.web.filter;

import org.opennms.core.criteria.Alias;
import org.opennms.core.criteria.Criteria;
import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.netmgt.dao.AlarmDao;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.web.filter.alarm.SortStyle;

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

    public SearchParameter(Filter... filter) {
        this(filter, null, null, null);
    }

    public SearchParameter(Filter[] filter, SortRule sortRule, Integer limit, Integer offset) {
        m_filter = Arrays.asList(filter);
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

    public OnmsCriteria toCriteria() {
        CriteriaBuilder builder = new CriteriaBuilder(getEntityClass());
        for(Alias eachAlias : m_alias) {
        }
        Criteria criteria = builder.toCriteria();
        criteria.setAl

        builder.toCriteria().set()


    }
}
