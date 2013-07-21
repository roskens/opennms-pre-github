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

package org.opennms.netmgt.snmp.mock;

/**
 * The Class ResponsePdu.
 */
public class ResponsePdu extends TestPdu {

    // we only use the errors related to reading since we don't support writing
    // yet
    /** The Constant NO_ERR. */
    public static final int NO_ERR = 0;

    /** The Constant TOO_BIG_ERR. */
    public static final int TOO_BIG_ERR = 1;

    /** The Constant NO_SUCH_NAME_ERR. */
    public static final int NO_SUCH_NAME_ERR = 2;

    /** The Constant GEN_ERR. */
    public static final int GEN_ERR = 5;

    /** The m_error status. */
    private int m_errorStatus;

    /** The m_error index. */
    private int m_errorIndex;

    /**
     * Instantiates a new response pdu.
     */
    public ResponsePdu() {
        super();
    }

    /**
     * Gets the error status.
     *
     * @return the error status
     */
    public int getErrorStatus() {
        return m_errorStatus;
    }

    /**
     * Sets the error status.
     *
     * @param errorStatus
     *            the new error status
     */
    void setErrorStatus(int errorStatus) {
        m_errorStatus = errorStatus;
    }

    /**
     * Gets the error index.
     *
     * @return the error index
     */
    public int getErrorIndex() {
        return m_errorIndex;
    }

    /**
     * Sets the error index.
     *
     * @param errorIndex
     *            the new error index
     */
    void setErrorIndex(int errorIndex) {
        m_errorIndex = errorIndex;
    }

}
