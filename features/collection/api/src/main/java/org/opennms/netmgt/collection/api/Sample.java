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

import org.joda.time.DateTime;

/**
 *
 * @author roskens
 * @param <T>
 */
public interface Sample<T> extends CollectionVisitable, Persistable {

    /**
     *
     * @return a {@link Resource} object that this sample is for.
     */
    public Resource getResource();

    /**
     *
     * @return string sample object name
     */
    public String getName();

    /**
     *
     * @return timestamp for the sample
     */
    public DateTime getTimestamp();

    /**
     *
     * @return value for the timestamp
     */
    public T getValue();

    /**
     *
     * @return value as string
     */
    public String getValueAsString();

    /**
     *
     * @return convert sample to fully qualified name.
     */
    public String resolvePath();

    /*
     * XXX: Should there be a method here to unresolve a path?
     * XXX: Should we only allow numeric sample types?
     */
}
