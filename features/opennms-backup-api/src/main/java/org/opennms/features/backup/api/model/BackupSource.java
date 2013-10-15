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
package org.opennms.features.backup.api.model;

import java.io.InputStream;
import java.util.Set;

/**
 * This interface describes the source of a backup operation.
 *
 * @author Christian Pape
 */
public interface BackupSource {
    /**
     * Returns the {@link Set} of full files
     *
     * @return the set of full files
     */
    public Set<String> fullFiles();

    /**
     * Checks whether this source contains the full file with the given name.
     *
     * @param filename the filename
     * @return true if exists, false otherwise
     */
    public boolean containsFullFile(String filename);

    /**
     * Returns an {@link InputStream} for a given file.
     *
     * @param filename the filename
     * @return the {@link InputStream}
     */
    public InputStream getInputStreamForFullFile(String filename);
}
