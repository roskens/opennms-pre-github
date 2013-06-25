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

package org.opennms.web.alarm;

import java.util.HashMap;
import java.util.Map;

import org.opennms.netmgt.dao.AlarmDao;
import org.springframework.util.Assert;

public enum AcknowledgeType {
    ACKNOWLEDGED("ack", AlarmDao.AcknowledgeFlag.acknowledge),
    UNACKNOWLEDGED("unack", AlarmDao.AcknowledgeFlag.unacknowlege),
    BOTH("both", AlarmDao.AcknowledgeFlag.both);

    /** Constant <code>s_ackTypesString</code> */
    private static final Map<String, AcknowledgeType> s_ackTypesString;
    
    static {
        s_ackTypesString = new HashMap<String, AcknowledgeType>();
        for (AcknowledgeType ackType : AcknowledgeType.values()) {
            s_ackTypesString.put(ackType.getShortName(), ackType);
        }
    }

    private final AlarmDao.AcknowledgeFlag m_flag;

    private String m_shortName;

    private AcknowledgeType(String shortName, AlarmDao.AcknowledgeFlag flag) {
        m_shortName = shortName;
        m_flag = flag;
    }

    public AlarmDao.AcknowledgeFlag toFlag() {
        return m_flag;
    }

    @Override
    public String toString() {
        return "AcknowledgeType." + getName();
    }

    public String getName() {
        return name();
    }

    public String getShortName() {
        return m_shortName;
    }
    
    public static AcknowledgeType getAcknowledgeType(String ackTypeString) {
        Assert.notNull(ackTypeString, "Cannot take null parameters.");
        return s_ackTypesString.get(ackTypeString.toLowerCase());
    }
}
