/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.rest.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * The Class TimeChunker.
 */
public class TimeChunker {

    /**
     * The Class TimeChunk.
     */
    public class TimeChunk {

        /** The m_start date. */
        private Date m_startDate;

        /** The m_end date. */
        private Date m_endDate;

        /**
         * Instantiates a new time chunk.
         *
         * @param startDate
         *            the start date
         * @param endDate
         *            the end date
         */
        public TimeChunk(Date startDate, Date endDate) {
            setStartDate(startDate);
            setEndDate(endDate);
        }

        /**
         * Sets the start date.
         *
         * @param startDate
         *            the new start date
         */
        public void setStartDate(Date startDate) {
            m_startDate = startDate;
        }

        /**
         * Gets the start date.
         *
         * @return the start date
         */
        public Date getStartDate() {
            return m_startDate;
        }

        /**
         * Sets the end date.
         *
         * @param endDate
         *            the new end date
         */
        public void setEndDate(Date endDate) {
            m_endDate = endDate;
        }

        /**
         * Gets the end date.
         *
         * @return the end date
         */
        public Date getEndDate() {
            return m_endDate;
        }

        /**
         * Contains.
         *
         * @param changeTime
         *            the change time
         * @return true, if successful
         */
        public boolean contains(Date changeTime) {
            return !changeTime.before(m_startDate) && !m_endDate.before(changeTime);
        }

    }

    /**
     * The Class Chunks.
     */
    public class Chunks extends Exception {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new chunks.
         *
         * @param message
         *            the message
         */
        public Chunks(String message) {
            super(message);
        }

    }

    /** The Constant MINUTE. */
    public static final int MINUTE = 300000;

    /** The Constant HOURLY. */
    public static final int HOURLY = 3600000;

    /** The Constant DAILY. */
    public static final int DAILY = 86400000;

    /** The m_start date. */
    private Date m_startDate;

    /** The m_end date. */
    private Date m_endDate;

    /** The m_resolution segments. */
    private List<TimeChunk> m_resolutionSegments = new ArrayList<TimeChunk>();

    /** The m_itr. */
    private Iterator<TimeChunk> m_itr;

    /** The m_resolution. */
    private int m_resolution;

    /**
     * Instantiates a new time chunker.
     *
     * @param resolution
     *            the resolution
     * @param startDate
     *            the start date
     * @param endDate
     *            the end date
     */
    public TimeChunker(int resolution, Date startDate, Date endDate) {
        m_startDate = startDate;
        m_endDate = endDate;
        m_resolution = resolution;
        createTimeSegments(m_resolutionSegments, resolution, startDate.getTime(),
                           (endDate.getTime() - startDate.getTime()));
    }

    /**
     * Creates the time segments.
     *
     * @param resolutionSegments
     *            the resolution segments
     * @param resolution
     *            the resolution
     * @param startTime
     *            the start time
     * @param timeInMilliseconds
     *            the time in milliseconds
     */
    private void createTimeSegments(List<TimeChunk> resolutionSegments, int resolution, long startTime,
            long timeInMilliseconds) {
        for (long i = 0; i < timeInMilliseconds; i += resolution) {
            Date startDate = new Date(startTime + i);
            Date endDate = new Date(startTime + i + resolution);
            TimeChunk segment = new TimeChunk(startDate, endDate);
            m_resolutionSegments.add(segment);
        }
        m_itr = m_resolutionSegments.iterator();
    }

    /**
     * Gets the segment count.
     *
     * @return the segment count
     */
    public int getSegmentCount() {
        return m_resolutionSegments.size();
    }

    /**
     * Checks for next.
     *
     * @return true, if successful
     */
    public boolean hasNext() {
        return m_itr.hasNext();
    }

    /**
     * Gets the next segment.
     *
     * @return the next segment
     */
    public TimeChunk getNextSegment() {
        return m_itr.next();
    }

    /**
     * Gets the at.
     *
     * @param index
     *            the index
     * @return the at
     */
    public TimeChunk getAt(int index) {
        return index >= m_resolutionSegments.size() ? null : m_resolutionSegments.get(index);
    }

    /**
     * Gets the index containing.
     *
     * @param timestamp
     *            the timestamp
     * @return the index containing
     */
    public int getIndexContaining(Date timestamp) {
        return (int) (timestamp.getTime() - m_startDate.getTime()) / m_resolution;
    }

    /**
     * Throw chunks.
     *
     * @throws Chunks
     *             the chunks
     */
    public void throwChunks() throws Chunks {
        throw new Chunks("Ewww gross you just threw chunks");
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return m_startDate;
    }

    /**
     * Gets the end date.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return m_endDate;
    }

}
