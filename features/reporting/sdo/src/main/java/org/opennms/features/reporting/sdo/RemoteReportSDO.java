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

package org.opennms.features.reporting.sdo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class RemoteReportSDO.
 */
@XmlRootElement(name = "remoteReportSDO")
public class RemoteReportSDO {

    /** The m_id. */
    private String m_id;

    /** The m_engine. */
    private String m_engine;

    /** The m_template. */
    private String m_template;

    /** The m_description. */
    private String m_description;

    /** The m_display name. */
    private String m_displayName;

    /** The m_report service. */
    private String m_reportService;

    /** The m_allow access. */
    private boolean m_allowAccess;

    /** The m_online. */
    private boolean m_online;

    /** The m_subreport. */
    private boolean m_subreport;

    /**
     * Gets the description.
     *
     * @return the description
     */
    @XmlElement(name = "description")
    public String getDescription() {
        return m_description;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    @XmlElement(name = "display-name")
    public String getDisplayName() {
        return m_displayName;
    }

    /**
     * Gets the engine.
     *
     * @return the engine
     */
    @XmlElement(name = "engine")
    public String getEngine() {
        return m_engine;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @XmlElement(name = "id")
    public String getId() {
        return m_id;
    }

    /**
     * Gets the report service.
     *
     * @return the report service
     */
    @XmlElement(name = "report-service")
    public String getReportService() {
        return m_reportService;
    }

    /**
     * Gets the template.
     *
     * @return the template
     */
    @XmlElement(name = "template")
    public String getTemplate() {
        return m_template;
    }

    /**
     * Gets the online.
     *
     * @return the online
     */
    @XmlElement(name = "online")
    public boolean getOnline() {
        return m_online;
    }

    /**
     * Gets the subreport.
     *
     * @return the subreport
     */
    @XmlElement(name = "subreport")
    public boolean getSubreport() {
        return m_subreport;
    }

    /**
     * Gets the allow access.
     *
     * @return the allow access
     */
    @XmlElement(name = "allow-access")
    public boolean getAllowAccess() {
        return m_allowAccess;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(String description) {
        m_description = description;
    }

    /**
     * Sets the display name.
     *
     * @param displayName
     *            the new display name
     */
    public void setDisplayName(String displayName) {
        m_displayName = displayName;
    }

    /**
     * Sets the engine.
     *
     * @param engine
     *            the new engine
     */
    public void setEngine(String engine) {
        m_engine = engine;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(String id) {
        m_id = id;
    }

    /**
     * Sets the online.
     *
     * @param online
     *            the new online
     */
    public void setOnline(boolean online) {
        m_online = online;
    }

    /**
     * Sets the subreport.
     *
     * @param subreport
     *            the new subreport
     */
    public void setSubreport(boolean subreport) {
        m_subreport = subreport;
    }

    /**
     * Sets the allow access.
     *
     * @param allowAccess
     *            the new allow access
     */
    public void setAllowAccess(boolean allowAccess) {
        m_allowAccess = allowAccess;
    }

    /**
     * Sets the report service.
     *
     * @param reportService
     *            the new report service
     */
    public void setReportService(String reportService) {
        m_reportService = reportService;
    }

    /**
     * Sets the template.
     *
     * @param template
     *            the new template
     */
    public void setTemplate(String template) {
        m_template = template;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RemoteReportSDO [m_id=" + m_id + ", m_engine=" + m_engine + ", m_template=" + m_template
                + ", m_description=" + m_description + ", m_displayName=" + m_displayName + ", m_reportService="
                + m_reportService + ", m_allowAccess=" + m_allowAccess + ", m_online=" + m_online + "]";
    }
}
