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

package org.opennms.netmgt.ncs.persistence;

import org.opennms.netmgt.model.events.EventProxy;
import org.opennms.netmgt.model.ncs.NCSComponent;
import org.opennms.netmgt.ncs.rest.NCSRestService.ComponentList;

/**
 * The Interface NCSComponentService.
 */
public interface NCSComponentService {

    /**
     * Sets the event proxy.
     *
     * @param eventProxy
     *            the new event proxy
     * @throws Exception
     *             the exception
     */
    public void setEventProxy(EventProxy eventProxy) throws Exception;

    /**
     * Gets the component.
     *
     * @param type
     *            the type
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @return the component
     */
    public NCSComponent getComponent(String type, String foreignSource, String foreignId);

    /**
     * Find components with attribute.
     *
     * @param string
     *            the string
     * @param string2
     *            the string2
     * @return the component list
     */
    public ComponentList findComponentsWithAttribute(String string, String string2);

    /**
     * Adds the or update components.
     *
     * @param component
     *            the component
     * @param deleteOrphans
     *            the delete orphans
     * @return the nCS component
     */
    public NCSComponent addOrUpdateComponents(NCSComponent component, boolean deleteOrphans);

    /**
     * Adds the subcomponent.
     *
     * @param type
     *            the type
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param subComponent
     *            the sub component
     * @param deleteOrphans
     *            the delete orphans
     * @return the nCS component
     */
    public NCSComponent addSubcomponent(String type, String foreignSource, String foreignId, NCSComponent subComponent,
            boolean deleteOrphans);

    /**
     * Delete component.
     *
     * @param type
     *            the type
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @param deleteOrphans
     *            the delete orphans
     */
    public void deleteComponent(String type, String foreignSource, String foreignId, boolean deleteOrphans);
}
