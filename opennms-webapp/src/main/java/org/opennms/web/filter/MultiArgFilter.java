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

package org.opennms.web.filter;

import java.util.Arrays;
import java.util.List;

/**
 * TwoArgFilter
 *
 * @author brozow
 * @version $Id: $
 * @since 1.8.1
 */
public abstract class MultiArgFilter<T> extends BaseFilter<T> {

    private T[] m_values;
    
    public MultiArgFilter(String filterType, String propertyName, T... values) {
        super(filterType, propertyName);
        m_values = values;
    }
    
    /**
     * <p>getValues</p>
     *
     * @return an array of T objects.
     */
    public T[] getValues() {
        return m_values;
    }
    
    /**
     * <p>getValuesAsList</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<T> getValuesAsList() {
        return Arrays.asList(m_values);
    }
    

    /** {@inheritDoc} */
    @Override
    final public String getValueString() {
        StringBuilder buf = new StringBuilder();
        for(int i = 0; i < m_values.length; i++) {
            if (i != 0) {
                buf.append(',');
            }
            buf.append(ValueStringRenderer.toString(m_values[i]));
        }
        return buf.toString();
    }
}
