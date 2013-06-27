/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.filter.notification;

import org.opennms.web.filter.NotEqualOrNullFilter;

/**
 * Encapsulates all node filtering functionality.
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public class NegativeNodeFilter extends NotEqualOrNullFilter<Integer> {
    /** Constant <code>TYPE="nodenot"</code> */
    public static final String TYPE = "nodenot";
    private final String m_nodeName;

    /**
     * <p>Constructor for NegativeNodeFilter.</p>
     *
     * @param nodeId a int.
     */
    public NegativeNodeFilter(int nodeId, String nodeName) {
        super(TYPE, "node.id", nodeId);
        m_nodeName = nodeName;
    }

    /**
     * <p>getTextDescription</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getTextDescription() {
        
//        String nodeName = NetworkElementFactory.getInstance(m_servletContext).getNodeLabel(getValue());
        String nodeName = m_nodeName;
        
        if(nodeName == null) {
            nodeName = Integer.toString(getValue());
        }

        return ("node is not " + nodeName);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        return (this.toString().equals(obj.toString()));
    }
}
