/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R). If not, see:
 * http://www.gnu.org/licenses/
 *
 * For more information contact:
 * OpenNMS(R) Licensing <license@opennms.org>
 * http://www.opennms.org/
 * http://www.opennms.com/
 *******************************************************************************/
package org.opennms.netmgt.collection.api;

/**
 *
 * @author roskens
 */
public enum CollectionStatus {
    UNKNOWN(0), SUCCESS(1), FAILED(2), INITALIZED(3), COLLECTING(4);
    private final int m_status;

    CollectionStatus(int status) {
        m_status = status;
    }

    public int getValue() {
        return m_status;
    }

    public static CollectionStatus toStatus(int status) {
        for (CollectionStatus s : CollectionStatus.values()) {
            if (status == s.getValue()) {
                return s;
            }
        }
        return UNKNOWN;
    }
}
