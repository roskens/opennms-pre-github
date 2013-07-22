/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

/**
 * <p>AdminApplicationService interface.</p>
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @version $Id: $
 * @since 1.8.1
 */
package org.opennms.web.svclayer;

import java.util.List;

import org.opennms.netmgt.model.OnmsApplication;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.web.svclayer.support.DefaultAdminApplicationService.ApplicationAndMemberServices;
import org.opennms.web.svclayer.support.DefaultAdminApplicationService.EditModel;
import org.opennms.web.svclayer.support.DefaultAdminApplicationService.ServiceEditModel;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Interface AdminApplicationService.
 */
@Transactional(readOnly = true)
public interface AdminApplicationService {

    /**
     * <p>
     * getApplication
     * </p>
     * .
     *
     * @param applicationIdString
     *            a {@link java.lang.String} object.
     * @return a
     *         {@link org.opennms.web.svclayer.support.DefaultAdminApplicationService.ApplicationAndMemberServices}
     *         object.
     */
    ApplicationAndMemberServices getApplication(String applicationIdString);

    /**
     * <p>
     * findAllMonitoredServices
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    List<OnmsMonitoredService> findAllMonitoredServices();

    /**
     * <p>
     * findApplicationAndAllMonitoredServices
     * </p>
     * .
     *
     * @param applicationIdString
     *            a {@link java.lang.String} object.
     * @return a
     *         {@link org.opennms.web.svclayer.support.DefaultAdminApplicationService.EditModel}
     *         object.
     */
    EditModel findApplicationAndAllMonitoredServices(String applicationIdString);

    /**
     * <p>
     * performEdit
     * </p>
     * .
     *
     * @param editAction
     *            a {@link java.lang.String} object.
     * @param editAction2
     *            a {@link java.lang.String} object.
     * @param toAdd
     *            an array of {@link java.lang.String} objects.
     * @param toDelete
     *            an array of {@link java.lang.String} objects.
     */
    @Transactional(readOnly = false) void performEdit(String editAction, String editAction2, String[] toAdd, String[] toDelete);

    /**
     * <p>
     * addNewApplication
     * </p>
     * .
     *
     * @param name
     *            a {@link java.lang.String} object.
     * @return a {@link org.opennms.netmgt.model.OnmsApplication} object.
     */
    @Transactional(readOnly = false) OnmsApplication addNewApplication(String name);

    /**
     * <p>
     * findAllApplications
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    List<OnmsApplication> findAllApplications();

    /**
     * <p>
     * removeApplication
     * </p>
     * .
     *
     * @param applicationIdString
     *            a {@link java.lang.String} object.
     */
    @Transactional(readOnly = false) void removeApplication(String applicationIdString);

    /**
     * <p>
     * findByMonitoredService
     * </p>
     * .
     *
     * @param id
     *            a int.
     * @return a {@link java.util.List} object.
     */
    List<OnmsApplication> findByMonitoredService(int id);

    /**
     * <p>
     * performServiceEdit
     * </p>
     * .
     *
     * @param ifServiceIdString
     *            a {@link java.lang.String} object.
     * @param editAction
     *            a {@link java.lang.String} object.
     * @param toAdd
     *            an array of {@link java.lang.String} objects.
     * @param toDelete
     *            an array of {@link java.lang.String} objects.
     */
    @Transactional(readOnly = false) void performServiceEdit(String ifServiceIdString, String editAction, String[] toAdd, String[] toDelete);

    /**
     * <p>
     * findServiceApplications
     * </p>
     * .
     *
     * @param ifServiceIdString
     *            a {@link java.lang.String} object.
     * @return a
     *         {@link org.opennms.web.svclayer.support.DefaultAdminApplicationService.ServiceEditModel}
     *         object.
     */
    ServiceEditModel findServiceApplications(String ifServiceIdString);

}
