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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.model.events.EventUtils;
import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.model.ncs.NCSComponentRepository;
import org.opennms.netmgt.xml.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class DefaultNCSCorrelationService.
 */
@Transactional
public class DefaultNCSCorrelationService implements NCSCorrelationService {

    /** The m_component repo. */
    @Autowired
    NCSComponentRepository m_componentRepo;

    /** The m_node dao. */
    @Autowired
    NodeDao m_nodeDao;

    /* (non-Javadoc)
     * @see org.opennms.netmgt.correlation.ncs.NCSCorrelationService#findComponentsThatDependOn(java.lang.Long)
     */
    @Override
    public List<NCSComponent> findComponentsThatDependOn(Long componentId) {

        NCSComponent comp = m_componentRepo.get(componentId);

        List<NCSComponent> parents = m_componentRepo.findComponentsThatDependOn(comp);

        for (NCSComponent parent : parents) {
            m_componentRepo.initialize(parent);

        }

        return parents;

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.correlation.ncs.NCSCorrelationService#findComponentsByNodeIdAndEventParameters(org.opennms.netmgt.xml.event.Event, java.lang.String[])
     */
    @Override
    public List<NCSComponent> findComponentsByNodeIdAndEventParameters(Event e, String... parameterNames) {

        assert e.getNodeid() != null;
        assert e.getNodeid() != 0;

        List<NCSComponent> components = m_componentRepo.findComponentsByNodeId(e.getNodeid().intValue());

        List<NCSComponent> matching = new LinkedList<NCSComponent>();
        for (NCSComponent component : components) {
            if (matches(component, e, parameterNames)) {
                matching.add(component);
            }
        }

        return matching;
    }

    /**
     * Matches.
     *
     * @param component
     *            the component
     * @param e
     *            the e
     * @param parameters
     *            the parameters
     * @return true, if successful
     */
    private boolean matches(NCSComponent component, Event e, String... parameters) {

        for (String key : parameters) {
            if (!component.getAttributes().containsKey(key)) {
                return false;
            }

            String val = component.getAttributes().get(key);

            if (!val.equals(EventUtils.getParm(e, key))) {
                return false;
            }
        }

        return true;

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.correlation.ncs.NCSCorrelationService#findSubComponents(java.lang.Long)
     */
    @Override
    public List<NCSComponent> findSubComponents(Long componentId) {

        NCSComponent comp = m_componentRepo.get(componentId);

        Set<NCSComponent> subcomponents = comp.getSubcomponents();

        for (NCSComponent subcomponent : subcomponents) {
            m_componentRepo.initialize(subcomponent);

        }

        return new ArrayList<NCSComponent>(subcomponents);

    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.correlation.ncs.NCSCorrelationService#findComponentsByNodeIdAndAttrParmMaps(org.opennms.netmgt.xml.event.Event, org.opennms.netmgt.correlation.ncs.NCSCorrelationService.AttrParmMap[])
     */
    @Override
    public List<NCSComponent> findComponentsByNodeIdAndAttrParmMaps(Event e, AttrParmMap... parameterMap) {
        assert e.getNodeid() != null;
        assert e.getNodeid() != 0;

        List<NCSComponent> components = m_componentRepo.findComponentsByNodeId(e.getNodeid().intValue());

        List<NCSComponent> matching = new LinkedList<NCSComponent>();
        for (NCSComponent component : components) {
            if (matches(component, e, parameterMap)) {
                matching.add(component);
            }
        }

        return matching;
    }

    /**
     * Matches.
     *
     * @param component
     *            the component
     * @param e
     *            the e
     * @param parameterMap
     *            the parameter map
     * @return true, if successful
     */
    private boolean matches(NCSComponent component, Event e, AttrParmMap[] parameterMap) {
        for (AttrParmMap map : parameterMap) {
            if (!map.matches(component, e)) {
                return false;
            }
        }

        return true;
    }

}
