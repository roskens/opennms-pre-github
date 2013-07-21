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

package org.opennms.netmgt.model.ncs;

import org.opennms.netmgt.model.ncs.NCSComponent.DependencyRequirements;
import org.opennms.netmgt.model.ncs.NCSComponent.NodeIdentification;

/**
 * The Class NCSBuilder.
 */
public class NCSBuilder {

    /** The m_parent. */
    private final NCSBuilder m_parent;

    /** The m_component. */
    private final NCSComponent m_component;

    /**
     * Instantiates a new nCS builder.
     *
     * @param type
     *            the type
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     */
    public NCSBuilder(String type, String foreignSource, String foreignId) {
        this(null, new NCSComponent(type, foreignSource, foreignId));
    }

    /**
     * Instantiates a new nCS builder.
     *
     * @param parent
     *            the parent
     * @param component
     *            the component
     */
    public NCSBuilder(NCSBuilder parent, NCSComponent component) {
        m_parent = parent;
        m_component = component;
    }

    /**
     * Sets the foreign source.
     *
     * @param foreignSource
     *            the foreign source
     * @return the nCS builder
     */
    public NCSBuilder setForeignSource(String foreignSource) {
        m_component.setForeignSource(foreignSource);
        return this;
    }

    /**
     * Sets the foreign id.
     *
     * @param foreignId
     *            the foreign id
     * @return the nCS builder
     */
    public NCSBuilder setForeignId(String foreignId) {
        m_component.setForeignId(foreignId);
        return this;
    }

    /**
     * Sets the node identity.
     *
     * @param nodeForeignSource
     *            the node foreign source
     * @param nodeForeignId
     *            the node foreign id
     * @return the nCS builder
     */
    public NCSBuilder setNodeIdentity(String nodeForeignSource, String nodeForeignId) {
        m_component.setNodeIdentification(new NodeIdentification(nodeForeignSource, nodeForeignId));
        return this;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the type
     * @return the nCS builder
     */
    public NCSBuilder setType(String type) {
        m_component.setType(type);
        return this;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the name
     * @return the nCS builder
     */
    public NCSBuilder setName(String name) {
        m_component.setName(name);
        return this;
    }

    /**
     * Sets the up event uei.
     *
     * @param upEventUei
     *            the up event uei
     * @return the nCS builder
     */
    public NCSBuilder setUpEventUei(String upEventUei) {
        m_component.setUpEventUei(upEventUei);
        return this;
    }

    /**
     * Sets the down event uei.
     *
     * @param downEventUei
     *            the down event uei
     * @return the nCS builder
     */
    public NCSBuilder setDownEventUei(String downEventUei) {
        m_component.setDownEventUei(downEventUei);
        return this;
    }

    /**
     * Sets the attribute.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @return the nCS builder
     */
    public NCSBuilder setAttribute(String key, String value) {
        m_component.setAttribute(key, value);
        return this;
    }

    /**
     * Sets the dependencies required.
     *
     * @param requirements
     *            the requirements
     * @return the nCS builder
     */
    public NCSBuilder setDependenciesRequired(DependencyRequirements requirements) {
        m_component.setDependenciesRequired(requirements);
        return this;
    }

    /**
     * Push component.
     *
     * @param type
     *            the type
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @return the nCS builder
     */
    public NCSBuilder pushComponent(String type, String foreignSource, String foreignId) {
        NCSComponent sub = new NCSComponent(type, foreignSource, foreignId);
        m_component.addSubcomponent(sub);
        return new NCSBuilder(this, sub);
    }

    /**
     * Pop component.
     *
     * @return the nCS builder
     */
    public NCSBuilder popComponent() {
        return m_parent;
    }

    /**
     * Gets the.
     *
     * @return the nCS component
     */
    public NCSComponent get() {
        return m_component;
    }

}
