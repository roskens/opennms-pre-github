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
package org.opennms.features.vaadin.dashboard.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class represents an abstract factory for instantiating {@link Dashlet} objects.
 *
 * @author Christian Pape
 */
public abstract class AbstractDashletFactory implements DashletFactory {
    /**
     * The name of the provided {@link Dashlet}
     */
    protected String m_name;
    /**
     * A map holding the required parameters for the {@link Dashlet}
     */
    protected Map<String, String> m_requiredParameters = new TreeMap<String, String>();

    /**
     * Constructor for instantiating a new factory with the given name.
     *
     * @param name the name to use
     */
    public AbstractDashletFactory(String name) {
        m_name = name;
    }

    /**
     * Add a required parameter for this factory.
     *
     * @param key          the key to use
     * @param defaultValue the default value for this parameter
     */
    protected void addRequiredParameter(String key, String defaultValue) {
        m_requiredParameters.put(key, defaultValue);
    }

    /**
     * Returns the name of the {@link Dashlet} instances this object provides.
     *
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Returns the {@link Map} with the required parameters and default values.
     *
     * @return the {@link Map} holding the requires parameters
     */
    public Map<String, String> getRequiredParameters() {
        return m_requiredParameters;
    }

    /**
     * This methos sets the required parameters {@link Map}.
     *
     * @param requiredParameters the parameter {@link Map} to be set
     */
    public void setRequiredParameters(Map<String, String> requiredParameters) {
        m_requiredParameters = requiredParameters;
    }
}
