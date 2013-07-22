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

package org.opennms.netmgt.config.categories;

import java.util.concurrent.locks.Lock;

/**
 * <p>
 * CatFactory interface.
 * </p>
 *
 * @author jsartin
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 * @version $Id: $
 */
public interface CatFactory {

    /**
     * <p>
     * getConfig
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.config.categories.Catinfo} object.
     */
    Catinfo getConfig();

    /**
     * <p>
     * getCategory
     * </p>
     * .
     *
     * @param name
     *            a {@link java.lang.String} object.
     * @return a {@link org.opennms.netmgt.config.categories.Category} object.
     */
    Category getCategory(String name);

    /**
     * <p>
     * getEffectiveRule
     * </p>
     * .
     *
     * @param catLabel
     *            a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    String getEffectiveRule(String catLabel);

    /**
     * <p>
     * getNormal
     * </p>
     * .
     *
     * @param catlabel
     *            a {@link java.lang.String} object.
     * @return a double.
     */
    double getNormal(String catlabel);

    /**
     * <p>
     * getWarning
     * </p>
     * .
     *
     * @param catlabel
     *            a {@link java.lang.String} object.
     * @return a double.
     */
    double getWarning(String catlabel);

    /**
     * Gets the read lock.
     *
     * @return the read lock
     */
    Lock getReadLock();

    /**
     * Gets the write lock.
     *
     * @return the write lock
     */
    Lock getWriteLock();

}
