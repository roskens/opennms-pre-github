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

package org.opennms.features.topology.api;

/**
 * The Interface HistoryManager.
 */
public interface HistoryManager {

    /**
     * Apply history.
     *
     * @param userId
     *            the user id
     * @param fragmentId
     *            the fragment id
     * @param container
     *            the container
     */
    public void applyHistory(String userId, String fragmentId, GraphContainer container);

    /**
     * Gets the history for user.
     *
     * @param userId
     *            the user id
     * @return the history for user
     */
    public String getHistoryForUser(String userId);

    /**
     * Creates the.
     *
     * @param userId
     *            the user id
     * @param container
     *            the container
     * @return the string
     */
    public String create(String userId, GraphContainer container);

    /**
     * On bind.
     *
     * @param operation
     *            the operation
     */
    void onBind(HistoryOperation operation);

    /**
     * On unbind.
     *
     * @param operation
     *            the operation
     */
    void onUnbind(HistoryOperation operation);

}
