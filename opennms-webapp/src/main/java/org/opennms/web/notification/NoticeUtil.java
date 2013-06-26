/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.web.notification;

import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.opennms.core.criteria.Alias;
import org.opennms.core.utils.WebSecurityUtils;
import org.opennms.web.element.NetworkElementFactory;
import org.opennms.web.filter.Filter;
import org.opennms.web.filter.NotNullFilter;
import org.opennms.web.filter.NullFilter;
import org.opennms.web.filter.notification.AcknowledgedByFilter;
import org.opennms.web.filter.notification.InterfaceFilter;
import org.opennms.web.filter.notification.NegativeNodeFilter;
import org.opennms.web.filter.notification.NodeFilter;
import org.opennms.web.filter.notification.NotificationIdFilter;
import org.opennms.web.filter.notification.ResponderFilter;
import org.opennms.web.filter.notification.ServiceFilter;
import org.opennms.web.filter.notification.UserFilter;
import org.opennms.netmgt.model.OnmsNotification;
import org.opennms.web.filter.SearchParameter;

/**
 * <p>Abstract NoticeUtil class.</p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public abstract class NoticeUtil extends Object {

    public static SearchParameter getSearchParameter(Filter[] filters, AcknowledgeType ackType) {
        return getSearchParameter(filters, null, ackType, null, null);
    }

    public static SearchParameter getSearchParameter(Filter[] filters, SortStyle sortStyle, AcknowledgeType ackType) {
        return getSearchParameter(filters, sortStyle, ackType, null, null);
    }

    public static SearchParameter getSearchParameter(Filter[] filters, SortStyle sortStyle, AcknowledgeType ackType, Integer limit, Integer offset) {
        SearchParameter parameter = new SearchParameter(OnmsNotification.class, filters, sortStyle == null ? null : sortStyle.toSortRule(), null, null);
        parameter.addAlias("node", "node", Alias.JoinType.LEFT_JOIN);
        parameter.addAlias("serviceType", "serviceType", Alias.JoinType.LEFT_JOIN);

        if (AcknowledgeType.ACKNOWLEDGED.equals(ackType)) parameter.addFilter(new NotNullFilter("answeredBy"));
        if (AcknowledgeType.UNACKNOWLEDGED.equals(ackType)) parameter.addFilter(new NullFilter("answeredBy"));

        return parameter;
    }

    /**
     * <p>getFilter</p>
     *
     * @param filterString a {@link java.lang.String} object.
     * @return a org$opennms$web$filter$Filter object.
     */
    public static Filter getFilter(String filterString, ServletContext servletContext) {
        Filter filter = null;

        StringTokenizer tokens = new StringTokenizer(filterString, "=");
        String type = tokens.nextToken();
        String value = tokens.nextToken();

        if (type.equals(AcknowledgedByFilter.TYPE)) {
            filter = new AcknowledgedByFilter(value);
        } else if (type.equals(InterfaceFilter.TYPE)) {
            filter = new InterfaceFilter(value);
        } else if (type.equals(NodeFilter.TYPE)) {
            filter = new NodeFilter(getId(value));
        } else if(type.equals(NegativeNodeFilter.TYPE)) {
        	filter = new NegativeNodeFilter(getId(value), getNodeLabel(servletContext, value));
        } else if (type.equals(NotificationIdFilter.TYPE)) {
            filter = new NotificationIdFilter(getId(value));
        } else if (type.equals(ResponderFilter.TYPE)) {
            filter = new ResponderFilter(value);
        } else if (type.equals(ServiceFilter.TYPE)) {
            filter = new ServiceFilter(getId(value), getServiceName(servletContext, value));
        } else if (type.equals(UserFilter.TYPE)) {
            filter = new UserFilter(value);
        }

        return filter;
    }

    private static String getNodeLabel(ServletContext context, String value) {
        return NetworkElementFactory.getInstance(context).getNodeLabel(getId(value));
    }

    private static String getServiceName(ServletContext context, String value) {
        return NetworkElementFactory.getInstance(context).getServiceNameFromId(getId(value));
    }

    private static int getId(String value) {
        return WebSecurityUtils.safeParseInt(value);
    }

}
