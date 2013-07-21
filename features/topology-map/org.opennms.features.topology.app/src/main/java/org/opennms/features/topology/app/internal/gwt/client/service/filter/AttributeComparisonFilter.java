/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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
package org.opennms.features.topology.app.internal.gwt.client.service.filter;

import java.util.Map;

/**
 * The Class AttributeComparisonFilter.
 */
public abstract class AttributeComparisonFilter extends AbstractFilter {

    /** The m_attribute. */
    private String m_attribute;

    /**
     * Instantiates a new attribute comparison filter.
     *
     * @param attribute
     *            the attribute
     */
    protected AttributeComparisonFilter(String attribute) {
        m_attribute = attribute;
    }

    /**
     * Gets the attribute.
     *
     * @return the attribute
     */
    protected String getAttribute() {
        return m_attribute;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.service.filter.AbstractFilter#match(java.util.Map)
     */
    @Override
    public boolean match(Map<String, String> properties) {
        if (properties == null || !properties.containsKey(m_attribute)) {
            return false;
        } else {
            return valueMatches(properties.get(m_attribute));
        }
    }

    /**
     * Value matches.
     *
     * @param value
     *            the value
     * @return true, if successful
     */
    protected abstract boolean valueMatches(String value);

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.service.filter.AbstractFilter#toString()
     */
    @Override
    public abstract String toString();

}
