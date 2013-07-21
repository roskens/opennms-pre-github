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

package org.opennms.features.topology.app.internal.gwt.client.d3;

/**
 * The Class SimpleTransition.
 */
public class SimpleTransition extends D3Behavior {

    /** The m_duration. */
    private int m_duration;

    /** The m_delay. */
    private int m_delay;

    /** The m_property. */
    private String m_property;

    /** The m_value. */
    private double m_value;

    /**
     * Instantiates a new simple transition.
     *
     * @param property
     *            the property
     * @param value
     *            the value
     * @param duration
     *            the duration
     * @param delay
     *            the delay
     */
    public SimpleTransition(String property, int value, int duration, int delay) {
        m_duration = duration;
        m_delay = delay;
        m_property = property;
        m_value = value;
    }

    /**
     * Instantiates a new simple transition.
     *
     * @param property
     *            the property
     * @param d
     *            the d
     * @param duration
     *            the duration
     * @param delay
     *            the delay
     */
    public SimpleTransition(String property, double d, int duration, int delay) {

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.app.internal.gwt.client.d3.D3Behavior#run(org.opennms.features.topology.app.internal.gwt.client.d3.D3)
     */
    @Override
    public D3 run(D3 selection) {
        return selection.transition().duration(m_duration).delay(m_delay).attr(m_property, m_value);
    }

}
