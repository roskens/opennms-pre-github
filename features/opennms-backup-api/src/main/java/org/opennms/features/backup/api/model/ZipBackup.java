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

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This class represents a single backup file.
 *
 * @author Christian Pape
 */
public class ZipBackup implements BackupTarget, Comparable<ZipBackup> {
    /**
     * the zip's filename
     */
    private String m_zipFilename;
    /**
     * the backup timestamp
     */
    private long m_timestamp;

    /**
     * Constructor for instantiating new objects of this class.
     *
     * @param zipFilename the zip's filename
     */
    public ZipBackup(String zipFilename) {
        this.m_zipFilename = zipFilename;
        this.m_timestamp = Long.valueOf(this.m_zipFilename.split("\\.")[1]);
    }

    @Override
    public int compareTo(ZipBackup zipBackup) {
        if (getTimestamp() < zipBackup.getTimestamp()) {
            return -1;
        }
        if (getTimestamp() > zipBackup.getTimestamp()) {
            return +1;
        }
        return 0;
    }

    /**
     * Returns the filename of this backup.
     *
     * @return the filename
     */
    public String getFilename() {
        return m_zipFilename;
    }

    /**
     * Returns the timestamp of this backup.
     *
     * @returnthe timestamp
     */
    public long getTimestamp() {
        return m_timestamp;
    }

    /**
     * Returns the date of this backup instance.
     *
     * @return the {@link Date} instance
     */
    public Date getDate() {
        Date date = new Date();
        date.setTime(m_timestamp);
        return date;
    }

    /**
     * Returns a {@link Set} of filenames of diff files.
     *
     * @return the set of diff files
     */
    public Set<String> diffFiles() {
        Set<String> files = new TreeSet<String>();

        try {
            ZipFile zipFile = new ZipFile(m_zipFilename);

            Enumeration zipEntries = zipFile.entries();

            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();

                if (zipEntry.getName().startsWith("diff/")) {
                    files.add(zipEntry.getName().substring(5));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

    /**
     * Returns a {@link Set} of filenames of full files.
     *
     * @return the set of full files
     */
    public Set<String> fullFiles() {
        Set<String> files = new TreeSet<String>();

        try {
            ZipFile zipFile = new ZipFile(m_zipFilename);

            Enumeration zipEntries = zipFile.entries();

            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();

                if (zipEntry.getName().startsWith("full/")) {
                    files.add(zipEntry.getName().substring(5));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

    /**
     * Checks whether this backup contains the full file specified by the given filename.
     *
     * @param filename the filename
     * @return true if existing, false otherwise
     */
    public boolean containsFullFile(String filename) {
        try {
            ZipFile zipFile = new ZipFile(m_zipFilename);
            ZipEntry zipEntry = zipFile.getEntry("full/" + filename);

            return (zipEntry != null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Checks whether this backup contains the diff file specified by the given filename.
     *
     * @param filename the filename
     * @return true if existing, false otherwise
     */
    public boolean containsDiffFile(String filename) {
        try {
            ZipFile zipFile = new ZipFile(m_zipFilename);
            ZipEntry zipEntry = zipFile.getEntry("diff/" + filename);

            return (zipEntry != null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Returns an {@link InputStream} instance for a given full file.
     *
     * @param filename the filename
     * @return the {@link InputStream} instance
     */
    public InputStream getInputStreamForFullFile(String filename) {
        try {
            ZipFile zipFile = new ZipFile(m_zipFilename);
            ZipEntry zipEntry = zipFile.getEntry("full/" + filename);

            if (zipEntry != null) {
                return zipFile.getInputStream(zipEntry);
            } else {
                System.out.println(filename + " not found in " + m_zipFilename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns an {@link InputStream} instance for a given diff file.
     *
     * @param filename the filename
     * @return the {@link InputStream} instance
     */
    public InputStream getInputStreamForDiffFile(String filename) {
        try {
            ZipFile zipFile = new ZipFile(m_zipFilename);
            ZipEntry zipEntry = zipFile.getEntry("diff/" + filename);

            if (zipEntry != null) {
                return zipFile.getInputStream(zipEntry);
            } else {
                System.out.println(filename + " not found in " + m_zipFilename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
