/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao.filter.notification;

import org.opennms.netmgt.dao.filter.EqualsFilter;

/**
 * Encapsulates all service filtering functionality.
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public class ServiceFilter extends EqualsFilter<Integer> {
    /** Constant <code>TYPE="service"</code> */
    public static final String TYPE = "service";
    private final String m_serviceName;

    /**
     * <p>Constructor for ServiceFilter.</p>
     *
     * @param serviceId a int.
     */
    public ServiceFilter(int serviceId, String serviceName) {
        super(TYPE, "serviceType.id", serviceId);
        m_serviceName = serviceName;
    }
    
    @Override
    public String getTextDescription() {
//        String serviceName = NetworkElementFactory.getInstance(servletContext).getServiceNameFromId(getValue());
        return (TYPE + "=" + m_serviceName);
    }

    @Override
    public String toString() {
        return ("<WebNotificationRepository.ServiceFilter: " + this.getDescription() + ">");
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        return (this.toString().equals(obj.toString()));
    }
}
