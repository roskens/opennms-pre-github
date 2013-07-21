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

package org.opennms.netmgt.collectd.jmxhelper;

import javax.management.openmbean.CompositeData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class JmxTest.
 *
 * @author Markus Neumann <Markus@OpenNMS.org>
 */
public class JmxTest implements JmxTestMBean {

    /** The log. */
    @SuppressWarnings("unused")
    private static Logger LOG = LoggerFactory.getLogger(JmxTest.class);

    /** The name. */
    String name = "Jmx Test";

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.jmxhelper.JmxTestMBean#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.jmxhelper.JmxTestMBean#getStringNull()
     */
    @Override
    public String getStringNull() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.jmxhelper.JmxTestMBean#getA()
     */
    @Override
    public int getA() {
        return 1;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.jmxhelper.JmxTestMBean#getB()
     */
    @Override
    public int getB() {
        return 2;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.jmxhelper.JmxTestMBean#getC()
     */
    @Override
    public int getC() {
        return 3;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.jmxhelper.JmxTestMBean#getD()
     */
    @Override
    public int getD() {
        return 4;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.jmxhelper.JmxTestMBean#getIntegerNull()
     */
    @Override
    public Integer getIntegerNull() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.collectd.jmxhelper.JmxTestMBean#getCompositeData()
     */
    @Override
    public CompositeData getCompositeData() {
        return null;
    }
}
