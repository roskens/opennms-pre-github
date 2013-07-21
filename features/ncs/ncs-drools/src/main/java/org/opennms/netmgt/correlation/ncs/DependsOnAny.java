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

/**
 * The Class DependsOnAny.
 */
public class DependsOnAny {

    /** The m_component. */
    private Component m_component;

    /** The m_sub components. */
    private List<Component> m_subComponents;

    /**
     * Instantiates a new depends on any.
     */
    public DependsOnAny() {
    }

    /**
     * Instantiates a new depends on any.
     *
     * @param component
     *            the component
     * @param subComponents
     *            the sub components
     */
    public DependsOnAny(Component component, List<Component> subComponents) {
        m_component = component;
        m_subComponents = subComponents;
    }

    /**
     * Gets the component.
     *
     * @return the component
     */
    public Component getComponent() {
        return m_component;
    }

    /**
     * Sets the component.
     *
     * @param component
     *            the new component
     */
    public void setComponent(Component component) {
        m_component = component;
    }

    /**
     * Gets the sub components.
     *
     * @return the sub components
     */
    public List<Component> getSubComponents() {
        return m_subComponents;
    }

    /**
     * Sets the sub components.
     *
     * @param subComponents
     *            the new sub components
     */
    public void setSubComponents(List<Component> subComponents) {
        m_subComponents = subComponents;
    }

}
