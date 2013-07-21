/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2013 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2013 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.detector.bsf.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.bsf.util.IOUtils;
import org.opennms.netmgt.provision.detector.bsf.request.BSFRequest;
import org.opennms.netmgt.provision.detector.bsf.response.BSFResponse;
import org.opennms.netmgt.provision.support.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * BSFClient class.
 * </p>
 *
 * @author Alejandro Galue <agalue@opennms.org>
 * @version $Id: $
 */
public class BSFClient implements Client<BSFRequest, BSFResponse> {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(BSFClient.class);

    /** The m_service name. */
    private String m_serviceName;

    /** The m_file name. */
    private String m_fileName;

    /** The m_lang class. */
    private String m_langClass;

    /** The m_bsf engine. */
    private String m_bsfEngine;

    /** The m_file extensions. */
    private String[] m_fileExtensions = {};

    /** The m_run type. */
    private String m_runType = "eval";

    /** The m_results. */
    private HashMap<String, String> m_results;

    /**
     * <p>
     * close
     * </p>
     * .
     */
    @Override
    public void close() {
    }

    /** {@inheritDoc} */
    @Override
    public void connect(final InetAddress address, final int port, final int timeout) throws IOException, Exception {
        m_results = new HashMap<String, String>();
        BSFManager bsfManager = new BSFManager();
        File file = new File(m_fileName);
        Map<String, Object> map = getParametersMap();
        try {

            if (m_langClass == null) {
                m_langClass = BSFManager.getLangFromFilename(m_fileName);
            }
            if (m_bsfEngine != null && m_langClass != null && m_fileExtensions.length > 0) {
                BSFManager.registerScriptingEngine(m_langClass, m_bsfEngine, m_fileExtensions);
            }

            if (file.exists() && file.canRead()) {
                String code = IOUtils.getStringFromReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

                // Declare some beans that can be used inside the script
                bsfManager.declareBean("map", map, Map.class);
                bsfManager.declareBean("ip_addr", address.getHostAddress(), String.class);
                // TODO: I'm not sure how to deal with it on detectors. Is the
                // node exists before running detectors? If so, I need NodeDao
                // here.
                // bsfManager.declareBean("node_id",svc.getNodeId(),int.class);
                // bsfManager.declareBean("node_label", svc.getNodeLabel(),
                // String.class);
                bsfManager.declareBean("svc_name", m_serviceName, String.class);
                bsfManager.declareBean("results", m_results, HashMap.class);

                for (final Entry<String, Object> entry : map.entrySet()) {
                    bsfManager.declareBean(entry.getKey(), entry.getValue(), String.class);
                }

                LOG.info("Executing {} for {}", m_langClass, file.getAbsoluteFile());
                if ("eval".equals(m_runType)) {
                    m_results.put("status", bsfManager.eval(m_langClass, "BSFDetector", 0, 0, code).toString());
                } else if ("exec".equals(m_runType)) {
                    bsfManager.exec(m_langClass, "BSFDetector", 0, 0, code);
                } else {
                    LOG.warn("Invalid run-type parameter value '{}' for service '{}'. Only 'eval' and 'exec' are supported.",
                             m_runType, m_serviceName);
                    throw new RuntimeException("Invalid run-type '" + m_runType + "'");
                }

                if ("exec".equals(m_runType) && !m_results.containsKey("status")) {
                    LOG.warn("The exec script '{}' for service '{}' never put a 'status' entry in the 'results' bean. Exec scripts should put this entry with a value of 'OK' for up.",
                             m_fileName, m_serviceName);
                }
            } else {
                LOG.warn("Cannot locate or read BSF script file '{}'. Marking service '{}' down.", m_fileName,
                         m_serviceName);
            }

        } catch (BSFException e) {
            m_results.clear();
            LOG.warn("BSFDetector poll for service '{}' failed with BSFException: {}", m_serviceName, e.getMessage(), e);
        } catch (FileNotFoundException e) {
            m_results.clear();
            LOG.warn("Could not find BSF script file '{}'. Marking service '{}' down.", m_fileName, m_serviceName);
        } catch (IOException e) {
            m_results.clear();
            LOG.warn("BSFDetector poll for service '{}' failed with IOException: {}", m_serviceName, e.getMessage(), e);
        } catch (Throwable e) {
            m_results.clear();
            LOG.warn("BSFDetector poll for service '{}' failed with unexpected throwable: {}", m_serviceName,
                     e.getMessage(), e);
        } finally {
            bsfManager.terminate();
        }
    }

    /**
     * <p>
     * receiveBanner
     * </p>
     * .
     *
     * @return a
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws Exception
     *             the exception
     *             {@link org.opennms.netmgt.provision.detector.bsf.response.BSFResponse}
     *             object.
     */
    @Override
    public BSFResponse receiveBanner() throws IOException, Exception {
        LOG.debug("Results: {}", m_results);
        final BSFResponse response = new BSFResponse(m_results);
        return response;
    }

    /**
     * <p>
     * sendRequest
     * </p>
     * .
     *
     * @param request
     *            a
     * @return a
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws Exception
     *             the exception
     *             {@link org.opennms.netmgt.provision.detector.bsf.request.BSFRequest}
     *             object.
     *             {@link org.opennms.netmgt.provision.detector.bsf.response.BSFResponse}
     *             object.
     */
    @Override
    public BSFResponse sendRequest(final BSFRequest request) throws IOException, Exception {
        return null;
    }

    /**
     * Gets the service name.
     *
     * @return the service name
     */
    public String getServiceName() {
        return m_serviceName;
    }

    /**
     * Sets the service name.
     *
     * @param serviceName
     *            the new service name
     */
    public void setServiceName(String serviceName) {
        this.m_serviceName = serviceName;
    }

    /**
     * Gets the file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return m_fileName;
    }

    /**
     * Sets the file name.
     *
     * @param fileName
     *            the new file name
     */
    public void setFileName(String fileName) {
        this.m_fileName = fileName;
    }

    /**
     * Gets the lang class.
     *
     * @return the lang class
     */
    public String getLangClass() {
        return m_langClass;
    }

    /**
     * Sets the lang class.
     *
     * @param langClass
     *            the new lang class
     */
    public void setLangClass(String langClass) {
        this.m_langClass = langClass;
    }

    /**
     * Gets the bsf engine.
     *
     * @return the bsf engine
     */
    public String getBsfEngine() {
        return m_bsfEngine;
    }

    /**
     * Sets the bsf engine.
     *
     * @param bsfEngine
     *            the new bsf engine
     */
    public void setBsfEngine(String bsfEngine) {
        this.m_bsfEngine = bsfEngine;
    }

    /**
     * Gets the file extensions.
     *
     * @return the file extensions
     */
    public String[] getFileExtensions() {
        return m_fileExtensions;
    }

    /**
     * Sets the file extensions.
     *
     * @param fileExtensions
     *            the new file extensions
     */
    public void setFileExtensions(String[] fileExtensions) {
        this.m_fileExtensions = fileExtensions;
    }

    /**
     * Gets the run type.
     *
     * @return the run type
     */
    public String getRunType() {
        return m_runType;
    }

    /**
     * Sets the run type.
     *
     * @param runType
     *            the new run type
     */
    public void setRunType(String runType) {
        this.m_runType = runType;
    }

    /**
     * Gets the parameters map.
     *
     * @return the parameters map
     */
    private Map<String, Object> getParametersMap() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("file-name", getFileName());
        map.put("lang-class", getLangClass());
        map.put("bsf-engine", getBsfEngine());
        map.put("file-extensions", getFileExtensions());
        map.put("run-type", getRunType());
        return Collections.unmodifiableMap(map);
    }

}
