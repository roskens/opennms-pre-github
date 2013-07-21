/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.map.db.datasources;

/**
 * The interface DataSource provide a way to get data named like the input.
 *
 * @author mmigliore
 * @author <a href="mailto:antonio@opennms.it">Antonio Russo</a>
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @author <a href="mailto:antonio@opennms.it">Antonio Russo</a>
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @version $Id: $
 * @since 1.8.1
 */
public interface DataSourceInterface {

    /**
     * Gets the status of the element with id in input using params in input.
     *
     * @param id
     *            a {@link java.lang.Object} object.
     * @return the status of velem, -1 if no status is found for velem
     */
    public String getStatus(Object id);

    /**
     * Gets the severity of the element with id in input using params in input.
     *
     * @param id
     *            a {@link java.lang.Object} object.
     * @return the severity of velem, -1 if no severity is found for velem
     */
    public String getSeverity(Object id);

    /**
     * Gets the availability of the element with id in input using params in
     * input.
     *
     * @param id
     *            a {@link java.lang.Object} object.
     * @return the availability of velem, -1 if no availability is found for
     *         velem
     */
    public double getAvailability(Object id);

}
