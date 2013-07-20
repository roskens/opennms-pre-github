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

package org.opennms.features.reporting.model.jasper;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class LocalJasperReport.
 */
@XmlRootElement(name = "report")
public class LocalJasperReport {

    /** The m_id. */
    private String m_id;

    /** The m_template. */
    private String m_template;

    /** The m_engine. */
    private String m_engine;

    /**
     * Gets the id.
     *
     * @return the id
     */
    @XmlAttribute(name = "id")
    public String getId() {
        return m_id;
    }

    /**
     * Gets the template.
     *
     * @return the template
     */
    @XmlAttribute(name = "template")
    public String getTemplate() {
        return m_template;
    }

    /**
     * Gets the engine.
     *
     * @return the engine
     */
    @XmlAttribute(name = "engine")
    public String getEngine() {
        return m_engine;
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
     * Sets the template.
     *
     * @param template
     *            the new template
     */
    public void setTemplate(String template) {
        m_template = template;
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
}
