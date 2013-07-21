/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.svclayer.support;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.StatisticsReport;
import org.opennms.web.controller.statisticsReports.PrettyOnmsResource;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;

/**
 * Model object for web statistics reports.
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @version $Id: $
 * @since 1.8.1
 */
public class StatisticsReportModel {

    /**
     * The Class Datum.
     */
    public static class Datum implements Comparable<Datum> {

        /** The m_value. */
        private Double m_value;

        /** The m_resource. */
        private OnmsResource m_resource;

        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public final int compareTo(final Datum o) {
            return m_value.compareTo(o.getValue());
        }

        /**
         * Gets the resource.
         *
         * @return the resource
         */
        public final OnmsResource getResource() {
            return m_resource;
        }

        /**
         * Gets the pretty resource.
         *
         * @return the pretty resource
         */
        public final OnmsResource getPrettyResource() {
            return new PrettyOnmsResource(m_resource);
        }

        /**
         * Sets the resource.
         *
         * @param resource
         *            the new resource
         */
        public final void setResource(final OnmsResource resource) {
            m_resource = resource;
        }

        /**
         * Gets the resource parent label.
         *
         * @return the resource parent label
         */
        public final String getResourceParentLabel() {
            Assert.notNull(m_resource, "the resource must be set before calling this method");

            StringBuffer buffer = new StringBuffer();

            OnmsResource parent = m_resource.getParent();
            while (parent != null) {
                if (buffer.length() > 0) {
                    buffer.append("<br/>");
                }
                buffer.append(parent.getResourceType().getLabel());
                buffer.append(": ");
                buffer.append(parent.getLabel());

                parent = parent.getParent();
            }

            return buffer.toString();
        }

        /**
         * Gets the resource parents reversed.
         *
         * @return the resource parents reversed
         */
        public final List<OnmsResource> getResourceParentsReversed() {
            if (m_resource == null) {
                return new ArrayList<OnmsResource>(0);
            }

            List<OnmsResource> resources = new ArrayList<OnmsResource>();

            OnmsResource parent = m_resource.getParent();
            while (parent != null) {
                resources.add(0, parent);
                parent = parent.getParent();
            }

            return resources;
        }

        /**
         * Gets the pretty resource parents reversed.
         *
         * @return the pretty resource parents reversed
         */
        public final List<OnmsResource> getPrettyResourceParentsReversed() {
            if (m_resource == null) {
                return new ArrayList<OnmsResource>(0);
            }

            List<OnmsResource> resources = new ArrayList<OnmsResource>();

            OnmsResource parent = new PrettyOnmsResource(m_resource.getParent());
            while (parent != null) {
                resources.add(0, new PrettyOnmsResource(parent));
                parent = parent.getParent();
            }

            return resources;
        }

        /**
         * Gets the value.
         *
         * @return the value
         */
        public final Double getValue() {
            return m_value;
        }

        /**
         * Sets the value.
         *
         * @param value
         *            the new value
         */
        public final void setValue(final Double value) {
            m_value = value;
        }
    }

    /** The m_errors. */
    private BindException m_errors;

    /** The m_report. */
    private StatisticsReport m_report;

    /** The m_data. */
    private SortedSet<Datum> m_data = new TreeSet<Datum>();

    /**
     * <p>
     * getData
     * </p>
     * .
     *
     * @return a {@link java.util.SortedSet} object.
     */
    public final SortedSet<Datum> getData() {
        return m_data;
    }

    /**
     * <p>
     * setData
     * </p>
     * .
     *
     * @param data
     *            a {@link java.util.SortedSet} object.
     */
    public final void setData(final SortedSet<StatisticsReportModel.Datum> data) {
        m_data = data;
    }

    /**
     * <p>
     * addData
     * </p>
     * .
     *
     * @param datum
     *            a
     *            {@link org.opennms.web.svclayer.support.StatisticsReportModel.Datum}
     *            object.
     */
    public final void addData(final Datum datum) {
        m_data.add(datum);
    }

    /**
     * <p>
     * getErrors
     * </p>
     * .
     *
     * @return a {@link org.springframework.validation.BindException} object.
     */
    public final BindException getErrors() {
        return m_errors;
    }

    /**
     * <p>
     * setErrors
     * </p>
     * .
     *
     * @param errors
     *            a {@link org.springframework.validation.BindException} object.
     */
    public final void setErrors(final BindException errors) {
        m_errors = errors;
    }

    /**
     * <p>
     * getReport
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.model.StatisticsReport} object.
     */
    public final StatisticsReport getReport() {
        return m_report;
    }

    /**
     * <p>
     * setReport
     * </p>
     * .
     *
     * @param report
     *            a {@link org.opennms.netmgt.model.StatisticsReport} object.
     */
    public final void setReport(final StatisticsReport report) {
        m_report = report;
    }

}
