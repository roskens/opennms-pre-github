/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012-2013 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2013 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.persist;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.opennms.core.xml.JaxbUtils;
import org.opennms.netmgt.provision.persist.foreignsource.ForeignSource;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * The Class RequisitionFileUtils.
 */
public class RequisitionFileUtils {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(RequisitionFileUtils.class);

    /**
     * Creates the path.
     *
     * @param fsPath
     *            the fs path
     * @throws ForeignSourceRepositoryException
     *             the foreign source repository exception
     */
    static void createPath(final File fsPath) throws ForeignSourceRepositoryException {
        if (!fsPath.exists()) {
            if (!fsPath.mkdirs()) {
                throw new ForeignSourceRepositoryException("unable to create directory " + fsPath.getPath());
            }
        }
    }

    /**
     * Encode file name.
     *
     * @param path
     *            the path
     * @param foreignSourceName
     *            the foreign source name
     * @return the file
     */
    static File encodeFileName(final String path, final String foreignSourceName) {
        return new File(path, foreignSourceName + ".xml");
    }

    /**
     * Gets the foreign source from file.
     *
     * @param inputFile
     *            the input file
     * @return the foreign source from file
     * @throws ForeignSourceRepositoryException
     *             the foreign source repository exception
     */
    static ForeignSource getForeignSourceFromFile(final File inputFile) throws ForeignSourceRepositoryException {
        return JaxbUtils.unmarshal(ForeignSource.class, inputFile);
    }

    /**
     * Gets the requisition from file.
     *
     * @param inputFile
     *            the input file
     * @return the requisition from file
     * @throws ForeignSourceRepositoryException
     *             the foreign source repository exception
     */
    static Requisition getRequisitionFromFile(final File inputFile) throws ForeignSourceRepositoryException {
        try {
            return JaxbUtils.unmarshal(Requisition.class, inputFile);
        } catch (final Throwable e) {
            throw new ForeignSourceRepositoryException("unable to unmarshal " + inputFile.getPath(), e);
        }
    }

    /**
     * Gets the output file for foreign source.
     *
     * @param path
     *            the path
     * @param foreignSource
     *            the foreign source
     * @return the output file for foreign source
     */
    static File getOutputFileForForeignSource(final String path, final ForeignSource foreignSource) {
        final File fsPath = new File(path);
        createPath(fsPath);
        return encodeFileName(path, foreignSource.getName());
    }

    /**
     * Gets the output file for requisition.
     *
     * @param path
     *            the path
     * @param requisition
     *            the requisition
     * @return the output file for requisition
     */
    static File getOutputFileForRequisition(final String path, final Requisition requisition) {
        final File reqPath = new File(path);
        createPath(reqPath);
        return encodeFileName(path, requisition.getForeignSource());
    }

    /**
     * Creates the snapshot.
     *
     * @param repository
     *            the repository
     * @param foreignSource
     *            the foreign source
     * @param date
     *            the date
     * @return the file
     */
    public static File createSnapshot(final ForeignSourceRepository repository, final String foreignSource,
            final Date date) {
        final URL url = repository.getRequisitionURL(foreignSource);
        if (url == null) {
            LOG.warn("Unable to get requisition URL for foreign source {}", foreignSource);
            return null;
        }

        final String sourceFileName = url.getFile();
        if (sourceFileName == null) {
            LOG.warn("Trying to create snapshot for {}, but getFile() doesn't return a value", url);
            return null;
        }
        final File sourceFile = new File(sourceFileName);

        if (!sourceFile.exists()) {
            LOG.warn("Trying to create snapshot for {}, but {} does not exist.", url, sourceFileName);
            return null;
        }

        final String targetFileName = sourceFileName + '.' + date.getTime();
        final File targetFile = new File(targetFileName);
        try {
            FileUtils.copyFile(sourceFile, targetFile, true);
            return targetFile;
        } catch (final IOException e) {
            LOG.warn("Failed to copy {} to {}", sourceFileName, targetFileName, e);
        }

        return null;
    }

    /**
     * Find snapshots.
     *
     * @param repository
     *            the repository
     * @param foreignSource
     *            the foreign source
     * @return the list
     */
    public static List<File> findSnapshots(final ForeignSourceRepository repository, final String foreignSource) {
        final List<File> files = new ArrayList<File>();

        URL url = null;
        try {
            url = repository.getRequisitionURL(foreignSource);
        } catch (final ForeignSourceRepositoryException e) {
            LOG.debug("Can't find snapshots for {}, an exception occurred getting the requisition URL!", foreignSource,
                      e);
        }

        if (url != null) {
            final String sourceFileName = url.getFile();
            if (sourceFileName != null) {
                final File sourceFile = new File(sourceFileName);
                final File sourceDirectory = sourceFile.getParentFile();
                for (final File entry : sourceDirectory.listFiles()) {
                    if (isSnapshot(foreignSource, entry)) {
                        files.add(entry);
                    }
                }
            }
        }

        return files;
    }

    /**
     * Checks if is snapshot.
     *
     * @param foreignSource
     *            the foreign source
     * @param entry
     *            the entry
     * @return true, if is snapshot
     */
    private static boolean isSnapshot(final String foreignSource, final File entry) {
        return !entry.isDirectory() && entry.getName().matches(foreignSource + ".xml.\\d+");
    }

    /**
     * Delete resource if snapshot.
     *
     * @param requisition
     *            the requisition
     */
    public static void deleteResourceIfSnapshot(final Requisition requisition) {
        final Resource resource = requisition.getResource();
        if (resource == null) {
            return;
        }

        try {
            final File resourceFile = resource.getFile();
            if (isSnapshot(requisition.getForeignSource(), resourceFile)) {
                if (!resourceFile.delete()) {
                    LOG.debug("Failed to delete {}", resourceFile);
                }
            }
        } catch (final IOException e) {
            LOG.debug("Resource {} can't be turned into a file, skipping snapshot delete detection.", resource, e);
            return;
        }

    }

    /**
     * Delete all snapshots.
     *
     * @param repository
     *            the repository
     */
    public static void deleteAllSnapshots(final ForeignSourceRepository repository) {
        for (final String foreignSource : repository.getActiveForeignSourceNames()) {
            final List<File> snapshots = findSnapshots(repository, foreignSource);
            for (final File snapshot : snapshots) {
                snapshot.delete();
            }
        }
    }

}
