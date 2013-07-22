/**
 * *****************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013 The OpenNMS Group, Inc. OpenNMS(R) is Copyright (C)
 * 1999-2013 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OpenNMS(R). If not, see: http://www.gnu.org/licenses/
 *
 * For more information contact: OpenNMS(R) Licensing <license@opennms.org>
 * http://www.opennms.org/ http://www.opennms.com/
 * *****************************************************************************
 */
package org.opennms.features.jmxconfiggenerator.webui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

import org.opennms.features.jmxconfiggenerator.Starter;
import org.opennms.features.jmxconfiggenerator.graphs.GraphConfigGenerator;
import org.opennms.features.jmxconfiggenerator.graphs.JmxConfigReader;
import org.opennms.features.jmxconfiggenerator.graphs.Report;
import org.opennms.features.jmxconfiggenerator.jmxconfig.JmxDatacollectionConfiggenerator;
import org.opennms.features.jmxconfiggenerator.webui.data.ModelChangeListener;
import org.opennms.features.jmxconfiggenerator.webui.data.ServiceConfig;
import org.opennms.features.jmxconfiggenerator.webui.data.UiModel;
import org.opennms.features.jmxconfiggenerator.webui.ui.ConfigForm;
import org.opennms.features.jmxconfiggenerator.webui.ui.ConfigResultView;
import org.opennms.features.jmxconfiggenerator.webui.ui.HeaderPanel;
import org.opennms.features.jmxconfiggenerator.webui.ui.IntroductionView;
import org.opennms.features.jmxconfiggenerator.webui.ui.ModelChangeRegistry;
import org.opennms.features.jmxconfiggenerator.webui.ui.ProgressWindow;
import org.opennms.features.jmxconfiggenerator.webui.ui.UiState;
import org.opennms.features.jmxconfiggenerator.webui.ui.mbeans.MBeansView;
import org.opennms.xmlns.xsd.config.jmx_datacollection.JmxDatacollectionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * The Class JmxConfigGeneratorApplication.
 */
@Theme(Config.STYLE_NAME)
@SuppressWarnings("serial")
public class JmxConfigGeneratorApplication extends UI implements ModelChangeListener<UiModel> {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(JmxConfigGeneratorApplication.class);

    /**
     * The Header panel which holds the steps which are necessary to complete
     * the configuration for a new service to get collected.
     */
    private HeaderPanel headerPanel;

    /**
     * The "content" panel which shows the view for the currently selected step
     * of the configuration process.
     */
    private Panel contentPanel;

    /** The progress window. */
    private ProgressWindow progressWindow;

    /** The model. */
    private UiModel model = new UiModel();

    /** The model change registry. */
    private ModelChangeRegistry modelChangeRegistry = new ModelChangeRegistry();

    /** The view cache. */
    private Map<UiState, Component> viewCache = new HashMap<UiState, Component>();

    /* (non-Javadoc)
     * @see com.vaadin.ui.UI#init(com.vaadin.server.VaadinRequest)
     */
    @Override
    protected void init(VaadinRequest request) {
        initHeaderPanel();
        initContentPanel();
        initMainWindow();

        registerListener(UiModel.class, this);
        updateView(UiState.IntroductionView);
    }

    /**
     * Inits the header panel.
     */
    private void initHeaderPanel() {
        headerPanel = new HeaderPanel();
        registerListener(UiState.class, headerPanel);
    }

    // the Main panel holds all views such as Config view, mbeans view, etc.
    /**
     * Inits the content panel.
     */
    private void initContentPanel() {
        contentPanel = new Panel();
        contentPanel.setContent(new VerticalLayout());
        contentPanel.getContent().setSizeFull();
        contentPanel.setSizeFull();
    }

    /**
     * Creates the main window and adds the header, main and button panels to
     * it.
     */
    private void initMainWindow() {
        Window window = new Window("JmxConfigGenerator GUI Tool");
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(headerPanel);
        layout.addComponent(contentPanel);
        // content Panel should use most of the space :)
        layout.setExpandRatio(contentPanel, 1);
        window.setContent(layout);
        window.getContent().setSizeFull();
        window.setSizeFull();
        addWindow(window);
    }

    /**
     * Sets the content panel component.
     *
     * @param c
     *            the new content panel component
     */
    private void setContentPanelComponent(Component c) {
        contentPanel.setContent(c);
    }

    /**
     * Update view.
     *
     * @param uiState
     *            the ui state
     */
    public void updateView(UiState uiState) {
        switch (uiState) {
        case IntroductionView:
        case ServiceConfigurationView:
        case MbeansView:
        case ResultView:
            setContentPanelComponent(getView(uiState));
            notifyObservers(UiModel.class, model);
            break;
        case MbeansDetection:
            showProgressWindow(uiState.getDescription() + ".\n\n This may take a while ...");
            new DetectMBeansWorkerThread().start();
            break;
        case ResultConfigGeneration:
            showProgressWindow(uiState.getDescription() + ".\n\n This may take a while ...");
            new CreateOutputWorkerThread().start();
            break;
        }
        notifyObservers(UiState.class, uiState);
    }

    /**
     * Creates the view.
     *
     * @param uiState
     *            the ui state
     * @param app
     *            the app
     * @return the component
     */
    private Component createView(UiState uiState, JmxConfigGeneratorApplication app) {
        Component component = null;
        switch (uiState) {
        case IntroductionView:
            component = new IntroductionView(app);
            break;
        case ServiceConfigurationView:
            component = new ConfigForm(app);
            registerListener(UiModel.class, (ModelChangeListener) component);
            break;
        case MbeansView:
            component = new MBeansView(app);
            registerListener(UiModel.class, (ModelChangeListener) component);
            break;
        case ResultView:
            component = new ConfigResultView(app);
            registerListener(UiModel.class, (ModelChangeListener) component);
            break;
        }
        return component;
    }

    /**
     * Gets the view.
     *
     * @param uiState
     *            the ui state
     * @return the view
     */
    private Component getView(UiState uiState) {
        if (viewCache.get(uiState) == null) {
            Component component = createView(uiState, JmxConfigGeneratorApplication.this);
            if (component == null) {
                return null; // no "real" view
            }
            viewCache.put(uiState, component);
        }
        return viewCache.get(uiState);
    }

    /**
     * Gets the progress window.
     *
     * @return the progress window
     */
    private ProgressWindow getProgressWindow() {
        if (progressWindow == null) {
            progressWindow = new ProgressWindow();
        }
        return progressWindow;
    }

    /**
     * Show progress window.
     *
     * @param label
     *            the label
     */
    public void showProgressWindow(String label) {
        getProgressWindow().setLabelText(label);
        addWindow(getProgressWindow());
    }

    /**
     * Register listener.
     *
     * @param aClass
     *            the a class
     * @param listener
     *            the listener
     */
    private void registerListener(Class<?> aClass, ModelChangeListener<?> listener) {
        modelChangeRegistry.registerListener(aClass, listener);
    }

    /**
     * Notify observers.
     *
     * @param aClass
     *            the a class
     * @param object
     *            the object
     */
    private void notifyObservers(Class<?> aClass, Object object) {
        modelChangeRegistry.notifyObservers(aClass, object);
    }

    /**
     * The Class DetectMBeansWorkerThread.
     */
    private class DetectMBeansWorkerThread extends Thread {

        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            try {
                ServiceConfig config = ((BeanItem<ServiceConfig>) ((ConfigForm) getView(UiState.ServiceConfigurationView)).getItemDataSource()).getBean();

                // TODO loading of the dictionary should not be done via the
                // Starter class and not in a static way!
                JmxDatacollectionConfiggenerator jmxConfigGenerator = new JmxDatacollectionConfiggenerator();
                JMXServiceURL jmxServiceURL = jmxConfigGenerator.getJmxServiceURL(config.isJmxmp(), config.getHost(),
                                                                                  config.getPort());
                JMXConnector connector = jmxConfigGenerator.getJmxConnector(config.getUser(), config.getPassword(),
                                                                            jmxServiceURL);
                JmxDatacollectionConfig generateJmxConfigModel = jmxConfigGenerator.generateJmxConfigModel(connector.getMBeanServerConnection(),
                                                                                                           "anyservice",
                                                                                                           !config.isSkipDefaultVM(),
                                                                                                           config.isRunWritableMBeans(),
                                                                                                           Starter.loadInternalDictionary());
                connector.close();

                model.setRawModel(generateJmxConfigModel);

                updateView(UiState.MbeansView);
                removeWindow(getProgressWindow());
            } catch (MalformedURLException ex) {
                handleError(ex);
            } catch (IOException ex) {
                handleError(ex);
            } catch (SecurityException ex) {
                handleError(ex);
            }
        }

        /**
         * Handle error.
         *
         * @param ex
         *            the ex
         */
        private void handleError(Exception ex) {
            // TODO logging?
            Notification.show("Connection error",
                              "An error occured during connection jmx service. Please verify connection settings.<br/><br/>"
                                      + ex.getMessage(), Type.ERROR_MESSAGE);
            removeWindow(getProgressWindow());
        }
    }

    /**
     * The Class CreateOutputWorkerThread.
     */
    private class CreateOutputWorkerThread extends Thread {

        /* (non-Javadoc)
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            if (model == null) {
                return;
            }

            // create snmp-graph.properties
            try {
                GraphConfigGenerator graphConfigGenerator = new GraphConfigGenerator();
                Collection<Report> reports = new JmxConfigReader().generateReportsByJmxDatacollectionConfig(model.getOutputConfig());
                model.setSnmpGraphProperties(graphConfigGenerator.generateSnmpGraph(reports));
            } catch (IOException ex) {
                model.setSnmpGraphProperties(ex.getMessage()); // TODO handle
                                                               // Errors in UI
                LOG.error("SNMP Graph-Properties couldn't be created.", ex);
            }

            model.updateOutput();
            updateView(UiState.ResultView);
            removeWindow(getProgressWindow());
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.jmxconfiggenerator.webui.data.ModelChangeListener#modelChanged(java.lang.Object)
     */
    @Override
    public void modelChanged(UiModel newModel) {
        if (model != newModel) {
            model = newModel;
        }
    }

}
