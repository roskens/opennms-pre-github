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
 * The Enum SyslogFacility.
 */
public enum SyslogFacility {

    /** The kernel. */
    KERNEL(0, "kernel messages"),
 /** The user. */
 USER(1, "user-level messages"),
 /** The mail. */
 MAIL(2, "mail system"),
 /** The system. */
 SYSTEM(3, "system daemons"),
 /** The auth. */
 AUTH(
            4, "security/authorization messages"),
 /** The syslog. */
 SYSLOG(5, "messages generated internally by syslogd"),
 /** The lpd. */
 LPD(6,
            "line printer subsystem"),
 /** The news. */
 NEWS(7, "network news subsystem"),
 /** The uucp. */
 UUCP(8, "UUCP subsystem"),
 /** The clock. */
 CLOCK(9,
            "clock daemon"),
 /** The authpriv. */
 AUTHPRIV(10, "privileged security/authorization messages"),
 /** The ftp. */
 FTP(11, "FTP daemon"),
 /** The ntp. */
 NTP(12,
            "NTP subsystem"),
 /** The audit. */
 AUDIT(13, "log audit"),
 /** The alert. */
 ALERT(14, "log alert"),
 /** The cron. */
 CRON(15, "cron daemon"),
 /** The LOCA l0. */
 LOCAL0(16,
            "local use 0"),
 /** The LOCA l1. */
 LOCAL1(17, "local use 1"),
 /** The LOCA l2. */
 LOCAL2(18, "local use 2"),
 /** The LOCA l3. */
 LOCAL3(19, "local use 3"),
 /** The LOCA l4. */
 LOCAL4(20,
            "local use 4"),
 /** The LOCA l5. */
 LOCAL5(21, "local use 5"),
 /** The LOCA l6. */
 LOCAL6(22, "local use 6"),
 /** The LOCA l7. */
 LOCAL7(23, "local use 7"),
 /** The unknown. */
 UNKNOWN(
            99, "unknown");

    /** The Constant MASK. */
    public static final int MASK = 0x03F8;

    /** The m_facility. */
    private final int m_facility;

    /** The m_name. */
    private final String m_name;

    /** The m_description. */
    private final String m_description;

    /**
     * Instantiates a new syslog facility.
     *
     * @param fac
     *            the fac
     * @param description
     *            the description
     */
    SyslogFacility(final int fac, final String description) {
        m_facility = fac;
        m_name = name().toLowerCase().intern();
        m_description = description.intern();
    }

    /**
     * Gets the facility number.
     *
     * @return the facility number
     */
    public int getFacilityNumber() {
        return m_facility;
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
     * @param severity
     *            the severity
     * @return the priority
     */
    public int getPriority(final SyslogSeverity severity) {
        if (severity == null) {
            return m_facility & MASK;
        }
        return ((m_facility & MASK) | severity.getSeverityNumber());
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return m_name;
    }

    /**
     * Gets the facility.
     *
     * @param fac
     *            the fac
     * @return the facility
     */
    public static SyslogFacility getFacility(final int fac) {
        final SyslogFacility[] facilities = SyslogFacility.values();
        if (facilities.length < fac) {
            return null;
        }
        return facilities[fac];
    }

    /**
     * Gets the facility for code.
     *
     * @param code
     *            the code
     * @return the facility for code
     */
    public static SyslogFacility getFacilityForCode(final int code) {
        return getFacility((code & MASK) >> 3);
    }
}
