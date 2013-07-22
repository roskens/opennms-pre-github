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

package org.opennms.features.reporting.repository.global;

import java.io.InputStream;
import java.util.List;

import org.opennms.features.reporting.model.basicreport.BasicReportDefinition;
import org.opennms.features.reporting.repository.ReportRepository;

/**
 * The Interface GlobalReportRepository.
 */
public interface GlobalReportRepository {

    /**
     * Gets the all reports.
     *
     * @return the all reports
     */
    List<BasicReportDefinition> getAllReports();

    /**
     * Gets the all online reports.
     *
     * @return the all online reports
     */
    List<BasicReportDefinition> getAllOnlineReports();

    /**
     * Gets the reports.
     *
     * @param repoId
     *            the repo id
     * @return the reports
     */
    List<BasicReportDefinition> getReports(String repoId);

    /**
     * Gets the online reports.
     *
     * @param repoId
     *            the repo id
     * @return the online reports
     */
    List<BasicReportDefinition> getOnlineReports(String repoId);

    /**
     * Gets the report service.
     *
     * @param reportId
     *            the report id
     * @return the report service
     */
    String getReportService(String reportId);

    /**
     * Gets the display name.
     *
     * @param reportId
     *            the report id
     * @return the display name
     */
    String getDisplayName(String reportId);

    /**
     * Gets the engine.
     *
     * @param reportId
     *            the report id
     * @return the engine
     */
    String getEngine(String reportId);

    /**
     * Gets the template stream.
     *
     * @param reportId
     *            the report id
     * @return the template stream
     */
    InputStream getTemplateStream(String reportId);

    /**
     * Gets the repository list.
     *
     * @return the repository list
     */
    List<ReportRepository> getRepositoryList();

    /**
     * Adds the report repository.
     *
     * @param repository
     *            the repository
     */
    void addReportRepository(ReportRepository repository);

    /**
     * Gets the repository by id.
     *
     * @param repoId
     *            the repo id
     * @return the repository by id
     */
    ReportRepository getRepositoryById(String repoId);

    /**
     * Reload configuration files.
     */
    void reloadConfigurationFiles();
}
