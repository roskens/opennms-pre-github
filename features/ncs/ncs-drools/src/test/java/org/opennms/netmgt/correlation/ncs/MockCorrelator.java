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

package org.opennms.netmgt.correlation.ncs;

import java.util.LinkedList;
import java.util.List;

import org.opennms.netmgt.correlation.CorrelationEngine;
import org.opennms.netmgt.correlation.CorrelationEngineRegistrar;

/**
 * The Class MockCorrelator.
 */
public class MockCorrelator implements CorrelationEngineRegistrar {

    /** The m_engines. */
    List<CorrelationEngine> m_engines = new LinkedList<CorrelationEngine>();

    /* (non-Javadoc)
     * @see org.opennms.netmgt.correlation.CorrelationEngineRegistrar#addCorrelationEngine(org.opennms.netmgt.correlation.CorrelationEngine)
     */
    @Override
    public void addCorrelationEngine(CorrelationEngine engine) {
        m_engines.add(engine);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.correlation.CorrelationEngineRegistrar#addCorrelationEngines(org.opennms.netmgt.correlation.CorrelationEngine[])
     */
    @Override
    public void addCorrelationEngines(CorrelationEngine... engines) {
        for (CorrelationEngine engine : engines) {
            m_engines.add(engine);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.correlation.CorrelationEngineRegistrar#findEngineByName(java.lang.String)
     */
    @Override
    public CorrelationEngine findEngineByName(String name) {
        for (CorrelationEngine engine : m_engines) {
            if (name.equals(engine.getName())) {
                return engine;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.correlation.CorrelationEngineRegistrar#getEngines()
     */
    @Override
    public List<CorrelationEngine> getEngines() {
        return m_engines;
    }

}
