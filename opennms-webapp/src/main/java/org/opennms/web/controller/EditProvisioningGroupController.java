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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.opennms.netmgt.model.PrimaryType;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.web.svclayer.ManualProvisioningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * <p>
 * EditProvisioningGroupController class.
 * </p>
 */
public class EditProvisioningGroupController extends SimpleFormController {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(EditProvisioningGroupController.class);

    /**
     * The Class TreeCommand.
     */
    public static class TreeCommand {

        /** The m_form path. */
        private String m_formPath;

        /** The m_action. */
        private String m_action;

        /** The m_form data. */
        private Requisition m_formData;

        /** The m_current node. */
        private String m_currentNode;

        /** The m_group name. */
        private String m_groupName = "hardcoded";

        /**
         * Gets the action.
         *
         * @return the action
         */
        public final String getAction() {
            return m_action;
        }

        /**
         * Sets the action.
         *
         * @param action
         *            the new action
         */
        public final void setAction(final String action) {
            m_action = action;
        }

        /**
         * Gets the group name.
         *
         * @return the group name
         */
        public final String getGroupName() {
            return m_groupName;
        }

        /**
         * Sets the group name.
         *
         * @param groupName
         *            the new group name
         */
        public final void setGroupName(final String groupName) {
            m_groupName = groupName;
        }

        /**
         * Gets the form data.
         *
         * @return the form data
         */
        public final Requisition getFormData() {
            return m_formData;
        }

        /**
         * Sets the form data.
         *
         * @param importData
         *            the new form data
         */
        public final void setFormData(final Requisition importData) {
            m_formData = importData;
        }

        /**
         * Gets the form path.
         *
         * @return the form path
         */
        public final String getFormPath() {
            return m_formPath;
        }

        /**
         * Sets the form path.
         *
         * @param target
         *            the new form path
         */
        public final void setFormPath(final String target) {
            m_formPath = target;
        }

        /**
         * Gets the current node.
         *
         * @return the current node
         */
        public final String getCurrentNode() {
            return m_currentNode;
        }

        /**
         * Sets the current node.
         *
         * @param node
         *            the new current node
         */
        public final void setCurrentNode(final String node) {
            m_currentNode = node;
        }

        /**
         * Gets the data path.
         *
         * @return the data path
         */
        public final String getDataPath() {
            // added nodeEditForm. to the formData. because somehow we are
            // getting that attached a prefix as well.
            return m_formPath.substring("nodeEditForm.formData.".length());
        }

        /**
         * Sets the data path.
         *
         * @param path
         *            the new data path
         */
        public final void setDataPath(final String path) {
            // added nodeEditForm. to the formData. because somehow we are
            // getting that attached a prefix as well.
            m_formPath = "nodeEditForm.formData." + path;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString() {
            return new ToStringBuilder(this).append("action", getAction()).append("currentNode", getCurrentNode()).append("dataPath",
                                                                                                                          getDataPath()).append("formData",
                                                                                                                                                getFormData()).append("formPath",
                                                                                                                                                                      getFormPath()).append("groupName",
                                                                                                                                                                                            getGroupName()).toString();
        }
    }

    /** The m_provisioning service. */
    private ManualProvisioningService m_provisioningService;

    /**
     * <p>
     * setProvisioningService
     * </p>
     * .
     *
     * @param provisioningService
     *            a {@link org.opennms.web.svclayer.ManualProvisioningService}
     *            object.
     */
    public final void setProvisioningService(final ManualProvisioningService provisioningService) {
        m_provisioningService = provisioningService;
    }

    /** {@inheritDoc} */
    @Override
    protected final ModelAndView handleRequestInternal(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        return super.handleRequestInternal(request, response);
    }

    /** {@inheritDoc} */
    @Override
    protected final ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response,
            final Object command, final BindException errors) throws Exception {
        TreeCommand treeCmd = (TreeCommand) command;
        String action = treeCmd.getAction();
        if (action == null) {
            return doShow(request, response, treeCmd, errors);
        } else if ("toggleFreeForm".equalsIgnoreCase(action)) {
            Boolean isFreeForm = (Boolean) request.getSession().getAttribute("freeFormEditing");
            if (isFreeForm == null) {
                isFreeForm = false;
            }
            request.getSession().setAttribute("freeFormEditing", !isFreeForm);
            return doShow(request, response, treeCmd, errors);
        } else if ("addNode".equalsIgnoreCase(action)) {
            return doAddNode(request, response, treeCmd, errors);
        } else if ("addInterface".equalsIgnoreCase(action)) {
            return doAddInterface(request, response, treeCmd, errors);
        } else if ("addService".equalsIgnoreCase(action)) {
            return doAddService(request, response, treeCmd, errors);
        } else if ("addCategory".equalsIgnoreCase(action)) {
            return doAddCategory(request, response, treeCmd, errors);
        } else if ("addAssetField".equalsIgnoreCase(action)) {
            return doAddAssetField(request, response, treeCmd, errors);
        } else if ("save".equalsIgnoreCase(action)) {
            return doSave(request, response, treeCmd, errors);
        } else if ("edit".equalsIgnoreCase(action)) {
            return doEdit(request, response, treeCmd, errors);
        } else if ("cancel".equalsIgnoreCase(action)) {
            return doCancel(request, response, treeCmd, errors);
        } else if ("delete".equalsIgnoreCase(action)) {
            return doDelete(request, response, treeCmd, errors);
        } else if ("import".equalsIgnoreCase(action)) {
            return doImport(request, response, treeCmd, errors);
        } else if ("done".equalsIgnoreCase(action)) {
            return done(request, response, treeCmd, errors);
        } else {
            errors.reject("Unrecognized action: " + action);
            return showForm(request, response, errors);
        }

    }

    /**
     * Done.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView done(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {
        return new ModelAndView(getSuccessView());
    }

    /**
     * Do show.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView doShow(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {
        return showForm(request, response, errors);
    }

    /**
     * Do cancel.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView doCancel(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {

        Requisition formData = m_provisioningService.getProvisioningGroup(treeCmd.getGroupName());
        treeCmd.setFormData(formData);

        treeCmd.setCurrentNode("");

        return showForm(request, response, errors);
    }

    /**
     * Do import.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView doImport(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {

        m_provisioningService.importProvisioningGroup(treeCmd.getGroupName());
        return super.showForm(request, response, errors);

    }

    /**
     * Do delete.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView doDelete(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {

        Requisition formData = m_provisioningService.deletePath(treeCmd.getGroupName(), treeCmd.getDataPath());
        treeCmd.setFormData(formData);

        return showForm(request, response, errors);
    }

    /**
     * Do add category.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView doAddCategory(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {

        Requisition formData = m_provisioningService.addCategoryToNode(treeCmd.getGroupName(), treeCmd.getDataPath(),
                                                                       "New Category");
        treeCmd.setFormData(formData);

        treeCmd.setCurrentNode(treeCmd.getFormPath() + ".category[0]");

        return showForm(request, response, errors);
    }

    /**
     * Do add asset field.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView doAddAssetField(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {
        Requisition formData = m_provisioningService.addAssetFieldToNode(treeCmd.getGroupName(), treeCmd.getDataPath(),
                                                                         "key", "value");
        treeCmd.setFormData(formData);

        treeCmd.setCurrentNode(treeCmd.getFormPath() + ".asset[0]");

        return showForm(request, response, errors);
    }

    /**
     * Do edit.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView doEdit(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {

        treeCmd.setCurrentNode(treeCmd.getFormPath());

        return showForm(request, response, errors);
    }

    /**
     * Do save.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView doSave(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {
        try {
            LOG.debug("treeCmd = {}", treeCmd);
            treeCmd.getFormData().validate();
            final Requisition formData = m_provisioningService.saveProvisioningGroup(treeCmd.getGroupName(),
                                                                                     treeCmd.getFormData());
            treeCmd.setFormData(formData);
            treeCmd.setCurrentNode("");
        } catch (final Throwable t) {
            errors.reject(t.getMessage());
        }
        return showForm(request, response, errors);
    }

    /**
     * Do add service.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView doAddService(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {

        Requisition formData = m_provisioningService.addServiceToInterface(treeCmd.getGroupName(),
                                                                           treeCmd.getDataPath(), "SVC");
        treeCmd.setFormData(formData);

        treeCmd.setCurrentNode(treeCmd.getFormPath() + ".monitoredService[0]");

        return showForm(request, response, errors);
    }

    /**
     * Do add interface.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView doAddInterface(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {

        Requisition formData = m_provisioningService.addInterfaceToNode(treeCmd.getGroupName(), treeCmd.getDataPath(),
                                                                        "");
        treeCmd.setFormData(formData);

        treeCmd.setCurrentNode(treeCmd.getFormPath() + ".interface[0]");

        return showForm(request, response, errors);
    }

    /**
     * Do add node.
     *
     * @param request
     *            the request
     * @param response
     *            the response
     * @param treeCmd
     *            the tree cmd
     * @param errors
     *            the errors
     * @return the model and view
     * @throws Exception
     *             the exception
     */
    private ModelAndView doAddNode(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {

        treeCmd.setFormData(m_provisioningService.addNewNodeToGroup(treeCmd.getGroupName(), "New Node"));

        treeCmd.setCurrentNode(treeCmd.getFormPath() + ".node[0]");

        return showForm(request, response, errors);
    }

    /** {@inheritDoc} */
    @Override
    protected final Object formBackingObject(final HttpServletRequest request) throws Exception {
        TreeCommand formCommand = new TreeCommand();
        initializeTreeCommand(request, formCommand);
        return formCommand;
    }

    /**
     * Initialize tree command.
     *
     * @param request
     *            the request
     * @param formCommand
     *            the form command
     * @throws Exception
     *             the exception
     */
    private void initializeTreeCommand(final HttpServletRequest request, final TreeCommand formCommand)
            throws Exception {
        String groupName = request.getParameter("groupName");
        if (groupName == null) {
            throw new IllegalArgumentException("groupName required");
        }

        Requisition formData = m_provisioningService.getProvisioningGroup(groupName);
        if (formData == null) {
            formData = m_provisioningService.createProvisioningGroup(groupName);
        }

        formCommand.setFormData(formData);
    }

    /** {@inheritDoc} */
    @Override
    protected final Map<String, Collection<String>> referenceData(final HttpServletRequest request) throws Exception {
        Map<String, Collection<String>> map = new HashMap<String, Collection<String>>();

        // Fetch the list of possible values out of the Castor enumeration
        List<String> choices = new ArrayList<String>();
        for (PrimaryType type : PrimaryType.getAllTypes()) {
            choices.add(type.getCode());
        }
        map.put("snmpPrimaryChoices", choices);

        String groupName = request.getParameter("groupName");
        if (groupName != null) {
            List<String> services = new ArrayList<String>(m_provisioningService.getServiceTypeNames(groupName));
            Collections.sort(services);
            map.put("services", services);
        }

        List<String> categories = new ArrayList<String>(m_provisioningService.getNodeCategoryNames());
        Collections.sort(categories);
        List<String> assetFields = new ArrayList<String>(m_provisioningService.getAssetFieldNames());
        Collections.sort(assetFields);
        map.put("categories", categories);
        map.put("assetFields", assetFields);

        return map;
    }
}
