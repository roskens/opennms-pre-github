/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.syslogd;

/**
 * The Enum SyslogSeverity.
 */
public enum SyslogSeverity {

    /** The emergency. */
    EMERGENCY(0, "system is unusable"),
 /** The alert. */
 ALERT(1, "action must be taken immediately"),
 /** The critical. */
 CRITICAL(2, "critical conditions"),
 /** The error. */
 ERROR(
            3, "error conditions"),
 /** The warning. */
 WARNING(4, "warning conditions"),
 /** The notice. */
 NOTICE(5, "normal but significant condition"),
 /** The informational. */
 INFORMATIONAL(
            6, "informational messages"),
 /** The debug. */
 DEBUG(7, "debug-level messages"),
 /** The all. */
 ALL(8, "all levels"),
 /** The unknown. */
 UNKNOWN(99, "unknown");

    /** The Constant MASK. */
    public static final int MASK = 0x0007;

    /** The m_severity. */
    private final int m_severity;

    /** The m_name. */
    private final String m_name;

    /** The m_description. */
    private final String m_description;

    /**
     * Instantiates a new syslog severity.
     *
     * @param severity
     *            the severity
     * @param description
     *            the description
     */
    SyslogSeverity(final int severity, final String description) {
        m_severity = severity;
        m_name = (name().substring(0, 1) + name().substring(1).toLowerCase()).intern();
        m_description = description.intern();
    }

    /**
     * Gets the severity number.
     *
     * @return the severity number
     */
    public int getSeverityNumber() {
        return m_severity;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return m_description;
    }

    /**
     * Gets the priority.
     *
     * @param facility
     *            the facility
     * @return the priority
     */
    public int getPriority(final SyslogFacility facility) {
        if (facility == null) {
            return 0 | m_severity;
        }
        return (facility.getFacilityNumber() & SyslogFacility.MASK) | m_severity;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return m_name;
    }

    /**
     * Gets the severity.
     *
     * @param severity
     *            the severity
     * @return the severity
     */
    public static SyslogSeverity getSeverity(final int severity) {
        final SyslogSeverity[] severities = SyslogSeverity.values();
        if (severities.length < severity) {
            return null;
        }
        return severities[severity];
    }

    /**
     * Gets the severity for code.
     *
     * @param code
     *            the code
     * @return the severity for code
     */
    public static SyslogSeverity getSeverityForCode(final int code) {
        return getSeverity(code & MASK);
    }
}
