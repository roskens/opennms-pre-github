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
package org.opennms.nrtg.web.internal;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Class ModelAndView.
 */
public class ModelAndView {

    /** The m_view name. */
    private final String m_viewName;

    /** The m_model. */
    private final Map<String, Object> m_model;

    /**
     * Instantiates a new model and view.
     *
     * @param viewName
     *            the view name
     */
    public ModelAndView(String viewName) {
        m_viewName = viewName;
        m_model = new LinkedHashMap<String, Object>();
    }

    /**
     * Adds the object.
     *
     * @param name
     *            the name
     * @param modelObject
     *            the model object
     */
    public void addObject(String name, Object modelObject) {
        m_model.put(name, modelObject);
    }

    /**
     * Gets the view name.
     *
     * @return the view name
     */
    public String getViewName() {
        return m_viewName;
    }

    /**
     * Gets the model.
     *
     * @return the model
     */
    public Map<String, Object> getModel() {
        return m_model;
    }

}
