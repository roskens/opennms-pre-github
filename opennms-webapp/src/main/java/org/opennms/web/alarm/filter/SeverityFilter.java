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

package org.opennms.web.alarm.filter;

import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.web.filter.EqualsFilter;
import org.opennms.web.filter.SQLType;

/**
 * Encapsulates severity filtering functionality.
 */
public class SeverityFilter extends EqualsFilter<OnmsSeverity> {

    /** The Constant TYPE. */
    public static final String TYPE = "severity";

    /**
     * Instantiates a new severity filter.
     *
     * @param severity
     *            the severity
     */
    public SeverityFilter(final OnmsSeverity severity) {
        super(TYPE, SQLType.SEVERITY, "ALARMS.SEVERITY", "severity", severity);
    }

    /* (non-Javadoc)
     * @see org.opennms.web.filter.OneArgFilter#getTextDescription()
     */
    @Override
    public String getTextDescription() {
        return (TYPE + " is " + getValue().getLabel());
    }

    /* (non-Javadoc)
     * @see org.opennms.web.filter.BaseFilter#toString()
     */
    @Override
    public String toString() {
        return ("<AlarmFactory.SeverityFilter: " + this.getDescription() + ">");
    }

    /**
     * Gets the severity.
     *
     * @return the severity
     */
    public int getSeverity() {
        return getValue().getId();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return (this.toString().equals(obj.toString()));
    }
}
