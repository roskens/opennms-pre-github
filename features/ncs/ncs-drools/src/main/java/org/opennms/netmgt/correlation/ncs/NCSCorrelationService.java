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

package org.opennms.netmgt.correlation.ncs;

import java.util.List;

import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Parm;
import org.opennms.netmgt.xml.event.Value;

/**
 * The Interface NCSCorrelationService.
 */
public interface NCSCorrelationService {

    /**
     * The Class AttrParmMap.
     */
    public static class AttrParmMap {

        /** The m_attribute name. */
        String m_attributeName;

        /** The m_paramter index. */
        int m_paramterIndex;

        /**
         * Instantiates a new attr parm map.
         *
         * @param attributeName
         *            the attribute name
         * @param parameterIndex
         *            the parameter index
         */
        public AttrParmMap(String attributeName, int parameterIndex) {
            m_attributeName = attributeName;
            m_paramterIndex = parameterIndex;
        }

        /**
         * Matches.
         *
         * @param component
         *            the component
         * @param e
         *            the e
         * @return true, if successful
         */
        public boolean matches(NCSComponent component, Event e) {
            if (!component.getAttributes().containsKey(m_attributeName))
                return false;
            List<Parm> parms = e.getParmCollection();
            if (m_paramterIndex > parms.size())
                return false;

            Parm parm = parms.get(m_paramterIndex - 1);
            Value val = parm.getValue();
            if (val == null)
                return false;

            String attrVal = component.getAttributes().get(m_attributeName);
            String eventVal = val.getContent();

            return attrVal == null ? eventVal == null : attrVal.equals(eventVal);

        }

    }

    /**
     * Find components that depend on.
     *
     * @param componentId
     *            the component id
     * @return the list
     */
    List<NCSComponent> findComponentsThatDependOn(Long componentId);

    /**
     * Find sub components.
     *
     * @param componentId
     *            the component id
     * @return the list
     */
    List<NCSComponent> findSubComponents(Long componentId);

    /**
     * Find components by node id and attr parm maps.
     *
     * @param e
     *            the e
     * @param parameterMap
     *            the parameter map
     * @return the list
     */
    List<NCSComponent> findComponentsByNodeIdAndAttrParmMaps(Event e, AttrParmMap... parameterMap);

    /**
     * Find components by node id and event parameters.
     *
     * @param e
     *            the e
     * @param parameterNames
     *            the parameter names
     * @return the list
     */
    List<NCSComponent> findComponentsByNodeIdAndEventParameters(Event e, String... parameterNames);

}
