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

package org.opennms.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opennms.netmgt.provision.persist.ForeignSourceService;
import org.opennms.netmgt.provision.persist.foreignsource.ForeignSource;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.web.svclayer.ManualProvisioningService;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

public class ProvisioningGroupsController extends SimpleFormController {

    private ManualProvisioningService m_provisioningService;

    private ForeignSourceService m_foreignSourceService;

    /**
     * <p>
     * setProvisioningService
     * </p>
     *
     * @param provisioningService
     *            a {@link org.opennms.web.svclayer.ManualProvisioningService}
     *            object.
     */
    public final void setProvisioningService(final ManualProvisioningService provisioningService) {
        m_provisioningService = provisioningService;
    }

    /**
     * <p>
     * setForeignSourceService
     * </p>
     *
     * @param fss
     *            a
     *            {@link org.opennms.netmgt.provision.persist.ForeignSourceService}
     *            object.
     */
    public final void setForeignSourceService(final ForeignSourceService fss) {
        m_foreignSourceService = fss;
    }

    public static class GroupAction {
        private String m_groupName;

        private String m_action = "show";

        private String m_actionTarget;

        public final String getAction() {
            return m_action;
        }

        public final void setAction(final String action) {
            m_action = action;
        }

        public final String getGroupName() {
            return m_groupName;
        }

        public final void setGroupName(final String groupName) {
            m_groupName = groupName;
        }

        public final String getActionTarget() {
            return m_actionTarget;
        }

        public final void setActionTarget(final String target) {
            m_actionTarget = target;
        }
    }

    /**
     * <p>
     * Constructor for ProvisioningGroupsController.
     * </p>
     */
    public ProvisioningGroupsController() {
        setCommandClass(GroupAction.class);
    }

    /** {@inheritDoc} */
    @Override
    protected final ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response,
            final Object cmd, final BindException errors) throws Exception {
        GroupAction command = (GroupAction) cmd;
        String action = command.getAction();

        if (action == null || "show".equalsIgnoreCase(action)) {
            return doShow(request, response, command, errors);
        } else if ("addGroup".equalsIgnoreCase(action)) {
            return doAddGroup(request, response, command, errors);
        } else if ("deleteNodes".equalsIgnoreCase(action)) {
            return doDeleteNodes(request, response, command, errors);
        } else if ("import".equalsIgnoreCase(action)) {
            return doImport(request, response, command, errors);
        } else if ("deleteGroup".equalsIgnoreCase(action)) {
            return doDeleteGroup(request, response, command, errors);
        } else if ("cloneForeignSource".equalsIgnoreCase(action)) {
            return doCloneForeignSource(request, response, command, errors);
        } else if ("resetDefaultForeignSource".equalsIgnoreCase(action)) {
            return doResetDefaultForeignSource(request, response, command, errors);
        } else {
            errors.reject("Unrecognized action: " + action);
            return super.onSubmit(request, response, command, errors);
        }

    }

    private ModelAndView doShow(final HttpServletRequest request, final HttpServletResponse response,
            final GroupAction command, final BindException errors) throws Exception {
        return showForm(request, response, errors);
    }

    private ModelAndView doDeleteGroup(final HttpServletRequest request, final HttpServletResponse response,
            final GroupAction command, final BindException errors) throws Exception {
        m_provisioningService.deleteProvisioningGroup(command.getGroupName());
        m_foreignSourceService.deleteForeignSource(command.getGroupName());
        return showForm(request, response, errors);
    }

    private ModelAndView doImport(final HttpServletRequest request, final HttpServletResponse response,
            final GroupAction command, final BindException errors) throws Exception {
        m_provisioningService.importProvisioningGroup(command.getGroupName());
        Thread.sleep(500);
        return showForm(request, response, errors);
    }

    private ModelAndView doDeleteNodes(final HttpServletRequest request, final HttpServletResponse response,
            final GroupAction command, final BindException errors) throws Exception {
        m_provisioningService.deleteAllNodes(command.getGroupName());
        return showForm(request, response, errors);
    }

    private ModelAndView doAddGroup(final HttpServletRequest request, final HttpServletResponse response,
            final GroupAction command, final BindException errors) throws Exception {
        String groupName = command.getGroupName();
        if (groupName.equals("default") || groupName.equals("")) {
            return showForm(request, response, errors);
        } else {
            m_provisioningService.createProvisioningGroup(command.getGroupName());
        }
        return showForm(request, response, errors);
    }

    private ModelAndView doCloneForeignSource(final HttpServletRequest request, final HttpServletResponse response,
            final GroupAction command, final BindException errors) throws Exception {
        m_foreignSourceService.cloneForeignSource(command.getGroupName(), command.getActionTarget());
        return showForm(request, response, errors);
    }

    private ModelAndView doResetDefaultForeignSource(final HttpServletRequest request,
            final HttpServletResponse response, final GroupAction command, final BindException errors) throws Exception {
        m_foreignSourceService.deleteForeignSource("default");
        return showForm(request, response, errors);
    }

    /** {@inheritDoc} */
    @Override
    protected final Map<String, Object> referenceData(final HttpServletRequest request) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();

        Set<String> names = new TreeSet<String>();
        Map<String, Requisition> groups = new TreeMap<String, Requisition>();
        Map<String, ForeignSource> foreignSources = new TreeMap<String, ForeignSource>();

        for (Requisition mi : m_provisioningService.getAllGroups()) {
            if (mi != null) {
                names.add(mi.getForeignSource());
                groups.put(mi.getForeignSource(), mi);
            }
        }
        for (ForeignSource fs : m_foreignSourceService.getAllForeignSources()) {
            if (!fs.isDefault()) {
                names.add(fs.getName());
                foreignSources.put(fs.getName(), fs);
            }
        }

        refData.put("foreignSourceNames", names);
        refData.put("groups", groups);
        refData.put("foreignSources", foreignSources);
        refData.put("dbNodeCounts", m_provisioningService.getGroupDbNodeCounts());

        return refData;
    }

}
