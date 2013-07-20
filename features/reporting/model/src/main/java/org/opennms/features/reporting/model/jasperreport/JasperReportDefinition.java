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

package org.opennms.features.reporting.model.jasperreport;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * The Interface JasperReportDefinition.
 */
public interface JasperReportDefinition {

    /**
     * Gets the id.
     *
     * @return the id
     */
    @XmlAttribute(name = "id")
    public abstract String getId();

    /**
     * Gets the template.
     *
     * @return the template
     */
    @XmlAttribute(name = "template")
    public abstract String getTemplate();

    /**
     * Gets the engine.
     *
     * @return the engine
     */
    @XmlAttribute(name = "engine")
    public abstract String getEngine();

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public abstract void setId(String id);

    /**
     * Sets the template.
     *
     * @param template
     *            the new template
     */
    public abstract void setTemplate(String template);

    /**
     * Sets the engine.
     *
     * @param engine
     *            the new engine
     */
    public abstract void setEngine(String engine);

}
