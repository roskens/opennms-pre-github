/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.Duration;
import org.opennms.core.utils.PropertyPath;
import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.netmgt.model.OnmsSeverityEditor;
import org.opennms.netmgt.model.PrimaryType;
import org.opennms.netmgt.model.PrimaryTypeEditor;
import org.opennms.netmgt.provision.persist.ForeignSourceService;
import org.opennms.netmgt.provision.persist.StringIntervalPropertyEditor;
import org.opennms.netmgt.provision.persist.StringXmlCalendarPropertyEditor;
import org.opennms.netmgt.provision.persist.foreignsource.ForeignSource;
import org.opennms.netmgt.provision.persist.foreignsource.PluginConfig;
import org.opennms.web.rest.support.InetAddressTypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * The Class EditForeignSourceController.
 */
@Controller
/**
 * <p>EditForeignSourceController class.</p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
@SuppressWarnings("deprecation")
public class EditForeignSourceController extends SimpleFormController {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(EditForeignSourceController.class);

    /** The m_foreign source service. */
    private ForeignSourceService m_foreignSourceService;

    /** The Constant m_pluginParameters. */
    private static final Map<String, Set<String>> m_pluginParameters = new HashMap<String, Set<String>>();

    /**
     * <p>
     * setForeignSourceService
     * </p>
     * .
     *
     * @param fss
     *            a
     *            {@link org.opennms.netmgt.provision.persist.ForeignSourceService}
     *            object.
     */
    public final void setForeignSourceService(final ForeignSourceService fss) {
        m_foreignSourceService = fss;
    }

    /**
     * The Class TreeCommand.
     */
    public static class TreeCommand {

        /** The m_form path. */
        private String m_formPath;

        /** The m_action. */
        private String m_action;

        /** The m_form data. */
        private ForeignSource m_formData;

        /** The m_current node. */
        private String m_currentNode;

        /** The m_foreign source name. */
        private String m_foreignSourceName = "hardcoded";

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
         * Gets the foreign source name.
         *
         * @return the foreign source name
         */
        public final String getForeignSourceName() {
            return m_foreignSourceName;
        }

        /**
         * Sets the foreign source name.
         *
         * @param foreignSourceName
         *            the new foreign source name
         */
        public final void setForeignSourceName(final String foreignSourceName) {
            m_foreignSourceName = foreignSourceName;
        }

        /**
         * Gets the form data.
         *
         * @return the form data
         */
        public final ForeignSource getFormData() {
            return m_formData;
        }

        /**
         * Sets the form data.
         *
         * @param importData
         *            the new form data
         */
        public final void setFormData(final ForeignSource importData) {
            m_formData = importData;
        }

        /**
         * Gets the default form path.
         *
         * @return the default form path
         */
        public final String getDefaultFormPath() {
            return "foreignSourceEditForm.formData";
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
            return m_formPath.substring("foreignSourceEditForm.formData.".length());
        }

        /**
         * Sets the data path.
         *
         * @param path
         *            the new data path
         */
        public final void setDataPath(final String path) {
            m_formPath = "foreignSourceEditForm.formData." + path;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString() {
            return new ToStringBuilder(this).append("foreign source", m_foreignSourceName).append("form path",
                                                                                                  m_formPath).append("action",
                                                                                                                     m_action).append("form data",
                                                                                                                                      m_formData).append("current node",
                                                                                                                                                         m_currentNode).toString();
        }
    }

    /** {@inheritDoc} */
    @Override
    protected final void initBinder(final HttpServletRequest req, final ServletRequestDataBinder binder)
            throws Exception {
        binder.registerCustomEditor(Duration.class, new StringIntervalPropertyEditor());
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
        LOG.debug("treeCmd = {}", treeCmd);
        String action = treeCmd.getAction();
        if (action == null) {
            return doShow(request, response, treeCmd, errors);
        } else if ("addDetector".equalsIgnoreCase(action)) {
            return doAddDetector(request, response, treeCmd, errors);
        } else if ("addPolicy".equalsIgnoreCase(action)) {
            return doAddPolicy(request, response, treeCmd, errors);
        } else if ("addParameter".equalsIgnoreCase(action)) {
            return doAddParameter(request, response, treeCmd, errors);
        } else if ("save".equalsIgnoreCase(action)) {
            return doSave(request, response, treeCmd, errors);
        } else if ("edit".equalsIgnoreCase(action)) {
            return doEdit(request, response, treeCmd, errors);
        } else if ("cancel".equalsIgnoreCase(action)) {
            return doCancel(request, response, treeCmd, errors);
        } else if ("delete".equalsIgnoreCase(action)) {
            return doDelete(request, response, treeCmd, errors);
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
        m_foreignSourceService.saveForeignSource(treeCmd.getForeignSourceName(), treeCmd.getFormData());
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
     * Do add detector.
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
    private ModelAndView doAddDetector(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {
        ForeignSource fs = m_foreignSourceService.addDetectorToForeignSource(treeCmd.getForeignSourceName(),
                                                                             "New Detector");
        treeCmd.setFormData(fs);
        treeCmd.setCurrentNode(treeCmd.getFormPath() + ".detectors[" + (fs.getDetectors().size() - 1) + "]");
        return showForm(request, response, errors);
    }

    /**
     * Do add policy.
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
    private ModelAndView doAddPolicy(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {
        ForeignSource fs = m_foreignSourceService.addPolicyToForeignSource(treeCmd.getForeignSourceName(), "New Policy");
        treeCmd.setFormData(fs);
        treeCmd.setCurrentNode(treeCmd.getFormPath() + ".policies[" + (fs.getPolicies().size() - 1) + "]");
        return showForm(request, response, errors);
    }

    /**
     * Do add parameter.
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
    private ModelAndView doAddParameter(final HttpServletRequest request, final HttpServletResponse response,
            final TreeCommand treeCmd, final BindException errors) throws Exception {
        ForeignSource fs = m_foreignSourceService.addParameter(treeCmd.getForeignSourceName(), treeCmd.getDataPath());
        treeCmd.setFormData(fs);
        PropertyPath path = new PropertyPath(treeCmd.getDataPath());
        PluginConfig obj = (PluginConfig) path.getValue(fs);
        int numParameters = (obj.getParameters().size() - 1);
        treeCmd.setCurrentNode(treeCmd.getFormPath() + ".parameters[" + numParameters + "]");
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
        ForeignSource fs = m_foreignSourceService.saveForeignSource(treeCmd.getForeignSourceName(),
                                                                    treeCmd.getFormData());
        treeCmd.setFormData(fs);
        treeCmd.setCurrentNode("");
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
        ForeignSource fs = m_foreignSourceService.getForeignSource(treeCmd.getForeignSourceName());
        treeCmd.setFormData(fs);
        treeCmd.setCurrentNode("");
        return showForm(request, response, errors);
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

        ForeignSource fs = m_foreignSourceService.deletePath(treeCmd.getForeignSourceName(), treeCmd.getDataPath());
        treeCmd.setFormData(fs);
        return showForm(request, response, errors);
    }

    /** {@inheritDoc} */
    @Override
    protected final Object formBackingObject(final HttpServletRequest request) throws Exception {
        TreeCommand formCommand = new TreeCommand();
        String foreignSourceName = request.getParameter("foreignSourceName");
        if (foreignSourceName == null) {
            throw new IllegalArgumentException("foreignSourceName required");
        }
        ForeignSource fs = m_foreignSourceService.getForeignSource(foreignSourceName);
        formCommand.setFormData(fs);
        return formCommand;
    }

    /** {@inheritDoc} */
    @Override
    protected final Map<String, Object> referenceData(final HttpServletRequest request) throws Exception {
        final Map<String, Object> map = new HashMap<String, Object>();
        int classFieldWidth = 20;
        int valueFieldWidth = 20;

        final Map<String, Set<String>> classParameters = new TreeMap<String, Set<String>>();

        final Map<String, String> detectorTypes = m_foreignSourceService.getDetectorTypes();
        map.put("detectorTypes", detectorTypes);
        for (String key : detectorTypes.keySet()) {
            classParameters.put(key, getParametersForClass(key));
            classFieldWidth = Math.max(classFieldWidth, key.length());
        }

        final Map<String, String> policyTypes = m_foreignSourceService.getPolicyTypes();
        map.put("policyTypes", policyTypes);
        for (String key : policyTypes.keySet()) {
            classParameters.put(key, getParametersForClass(key));
            classFieldWidth = Math.max(classFieldWidth, key.length());
        }

        map.put("pluginInfo", m_foreignSourceService.getWrappers());
        map.put("classFieldWidth", classFieldWidth);
        map.put("valueFieldWidth", valueFieldWidth);

        return map;
    }

    /**
     * Gets the parameters for class.
     *
     * @param clazz
     *            the clazz
     * @return the parameters for class
     */
    private Set<String> getParametersForClass(final String clazz) {
        if (m_pluginParameters.containsKey(clazz)) {
            return m_pluginParameters.get(clazz);
        }
        Set<String> parameters = new TreeSet<String>();
        try {
            final BeanWrapper wrapper = new BeanWrapperImpl(Class.forName(clazz));
            wrapper.registerCustomEditor(XMLGregorianCalendar.class, new StringXmlCalendarPropertyEditor());
            wrapper.registerCustomEditor(java.net.InetAddress.class, new InetAddressTypeEditor());
            wrapper.registerCustomEditor(OnmsSeverity.class, new OnmsSeverityEditor());
            wrapper.registerCustomEditor(PrimaryType.class, new PrimaryTypeEditor());
            for (final PropertyDescriptor pd : wrapper.getPropertyDescriptors()) {
                if (!"class".equals(pd.getName())) {
                    parameters.add(pd.getName());
                }
            }
            m_pluginParameters.put(clazz, parameters);
            return parameters;
        } catch (ClassNotFoundException e) {
            LOG.warn("unable to wrap class {}", clazz, e);
        }
        return null;
    }

}
