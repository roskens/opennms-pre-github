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

package org.opennms.features.reporting.model.basicreport;

/**
 * The Interface BasicReportDefinition.
 */
public interface BasicReportDefinition {

    /**
     * Gets the description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    String getDisplayName();

    /**
     * Gets the id.
     *
     * @return the id
     */
    String getId();

    /**
     * Gets the repository id.
     *
     * @return the repository id
     */
    String getRepositoryId();

    /**
     * Gets the online.
     *
     * @return the online
     */
    boolean getOnline();

    /**
     * Gets the report service.
     *
     * @return the report service
     */
    String getReportService();

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    void setId(String id);

    /**
     * Sets the display name.
     *
     * @param displayName
     *            the new display name
     */
    void setDisplayName(String displayName);

    /**
     * Sets the report service.
     *
     * @param reportService
     *            the new report service
     */
    void setReportService(String reportService);

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    void setDescription(String description);

    /**
     * Sets the online.
     *
     * @param online
     *            the new online
     */
    void setOnline(boolean online);

    /**
     * Gets the allow access.
     *
     * @return the allow access
     */
    boolean getAllowAccess();

    /**
     * Sets the allow access.
     *
     * @param allowAccess
     *            the new allow access
     */
    void setAllowAccess(boolean allowAccess);
}
