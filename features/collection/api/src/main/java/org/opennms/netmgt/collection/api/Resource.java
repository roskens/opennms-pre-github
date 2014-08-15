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
 * XXX: Should resources have attributes? (ex: If this was an OnmsNode, do we have access to foreign source, foreign id, etc.?)
 * <p>
 * @author roskens
 */
public interface Resource {

    /**
     *
     * @return parent {
     * <p>
     * @lin Resource} of this object.
     */
    Resource getParent();

    /**
     *
     * XXX: Should this be a ResourceType class by itself?
     * <p>
     * @return type of resource: {node, interface, etc.}
     */
    String getType();

    /**
     *
     * @return name of resource, unique per type
     */
    String getName();

    /**
     *
     * XXX: we should depend on a system property / configuration option that defines what the resolved name looks like.
     * <p>
     * @return fully qualified name of the resource
     */
    String resolvePath();
}
