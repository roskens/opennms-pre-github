/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.jasper.jrobin;

import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.opennms.netmgt.jasper.jrobin.RrdXportCmd.XPort;

/**
 * The Class JRobinDataSource.
 */
public class JRobinDataSource implements JRDataSource {

    /** The m_current row. */
    private int m_currentRow = -1;

    /** The m_timestamps. */
    private long[] m_timestamps;

    /** The m_step. */
    private long m_step;

    /** The m_xports. */
    private List<XPort> m_xports;

    /**
     * Instantiates a new j robin data source.
     *
     * @param step
     *            the step
     * @param timestamps
     *            the timestamps
     * @param xports
     *            the xports
     */
    public JRobinDataSource(long step, long[] timestamps, List<XPort> xports) {
        m_step = step;
        m_timestamps = timestamps;
        m_xports = xports;
    }

    /* (non-Javadoc)
     * @see net.sf.jasperreports.engine.JRDataSource#getFieldValue(net.sf.jasperreports.engine.JRField)
     */
    @Override
    public Object getFieldValue(JRField field) throws JRException {
        Object computeFieldValue = computeFieldValue(field);
        return computeFieldValue;
    }

    /**
     * Compute field value.
     *
     * @param field
     *            the field
     * @return the object
     */
    private Object computeFieldValue(JRField field) {
        if ("Timestamp".equalsIgnoreCase(getColumnName(field))) {
            return new Date(m_timestamps[m_currentRow] * 1000L);
        } else if ("Step".equalsIgnoreCase(getColumnName(field))) {
            return m_step;
        }
        XPort xport = findXPortForField(getColumnName(field));
        return xport == null ? null : Double.valueOf(xport.values[m_currentRow]);
    }

    /**
     * Gets the column name.
     *
     * @param field
     *            the field
     * @return the column name
     */
    private String getColumnName(JRField field) {
        return field.getDescription() == null || field.getDescription().trim().equals("") ? field.getName()
            : field.getDescription();
    }

    /**
     * Find x port for field.
     *
     * @param description
     *            the description
     * @return the x port
     */
    private XPort findXPortForField(String description) {
        for (XPort xport : m_xports) {
            if (xport.legend.equalsIgnoreCase(description)) {
                return xport;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see net.sf.jasperreports.engine.JRDataSource#next()
     */
    @Override
    public boolean next() throws JRException {
        m_currentRow++;
        return m_currentRow < m_timestamps.length;
    }

}
