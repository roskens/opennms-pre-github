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

package org.opennms.nrtg.api.model;

/**
 * Undefined model for a structure on top of collection jobs.
 *
 * @author Markus Neumann
 */
public class CollectionTask {

    /** The count. */
    private Integer count;

    /** The meta job. */
    private String metaJob;

    /**
     * Instantiates a new collection task.
     *
     * @param count
     *            the count
     * @param metaJob
     *            the meta job
     */
    public CollectionTask(Integer count, String metaJob) {
        this.count = count;
        this.metaJob = metaJob;
    }

    /**
     * Gets the count.
     *
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Gets the meta job.
     *
     * @return the meta job
     */
    public String getMetaJob() {
        return metaJob;
    }

}
