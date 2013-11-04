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
package org.opennms.features.backup.api.server;


import org.apache.commons.io.FileUtils;
import org.badiff.FileDiffs;
import org.badiff.imp.FileDiff;
import org.opennms.features.backup.api.model.ZipBackup;
import org.opennms.features.backup.api.rest.RestBackupSet;
import org.opennms.features.backup.api.rest.RestZipBackup;
import org.opennms.features.backup.api.rest.RestZipBackupContents;

import javax.xml.bind.JAXB;
import java.io.*;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a set of backup files.
 *
 * @author Christian Pape
 */
public class BackupSet {
    /**
     * the base directory
     */
    private String m_baseDirectory;
    /**
     * the {@link Set} of zip-files
     */
    private TreeSet<ZipBackup> m_zipBackupFiles = null;

    /**
     * Constructor for instatiating new objects of this class.
     *
     * @param baseDirectory the base directory
     */
    public BackupSet(String baseDirectory) {
        this.m_baseDirectory = baseDirectory;

        searchBackupFiles();
    }

    /**
     * This method searches for backup files.
     */
    private void searchBackupFiles() {
        m_zipBackupFiles = new TreeSet<ZipBackup>();

        File directory = new File(m_baseDirectory);

        if (!directory.exists()) {
            return;
        }

        for (File file : directory.listFiles()) {
            if (!file.isDirectory()) {
                if (file.getName().matches("backup\\.[0-9]+\\.zip")) {
                    ZipBackup zipBackup = new ZipBackup(file.getAbsolutePath());
                    m_zipBackupFiles.add(zipBackup);
                }
            }
        }
    }

    /**
     * Returns a {@link ZipBackup} instance for a given timestamp.
     *
     * @param timestamp the timestamp
     * @return the {@link ZipBackup} instance
     */
    public ZipBackup getZipBackup(long timestamp) {
        for (ZipBackup zipBackup : m_zipBackupFiles) {
            if (zipBackup.getTimestamp() == timestamp) {
                return zipBackup;
            }
        }
        return null;
    }

    /**
     * Returns a {@link ZipBackup} instance for a given timestamp.
     *
     * @param timestampString the timestamp
     * @return the {@link ZipBackup} instance
     */
    public ZipBackup getZipBackup(String timestampString) {
        long timestamp = Long.valueOf(timestampString);

        for (ZipBackup zipBackup : m_zipBackupFiles) {
            if (zipBackup.getTimestamp() == timestamp) {
                return zipBackup;
            }
        }
        return null;
    }

    /**
     * Returns the {@link Set} of {@link ZipBackup} instances this backup set consists of.
     *
     * @return the {@link Set} of {@link ZipBackup} instances
     */
    public Set<ZipBackup> getZipBackupFiles() {
        return m_zipBackupFiles;
    }

    /**
     * Writes a file.
     *
     * @param inputStream the source {@link InputStream}
     * @param file        the destination file
     * @return true on success, false otherwise
     */
    private boolean writeFile(InputStream inputStream, File file) {
        File parent = file.getParentFile();

        if (!parent.exists() && !parent.mkdirs()) {
            return false;
        }

        try {
            byte[] buffer = new byte[65536];

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int read = 0;

            while ((read = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, read);
            }

            fileOutputStream.close();
            inputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Restores a single file from a {@link ZipBackup} instance.
     *
     * @param zipBackup        the {@link ZipBackup} instance
     * @param restoreDirectory the destination directory
     * @param filename         the filename of the file to be restored
     * @return true on success, false otherwise
     */
    public boolean restoreFile(ZipBackup zipBackup, String restoreDirectory, String filename) {
        if (zipBackup.containsFullFile(filename)) {
            return writeFile(zipBackup.getInputStreamForFullFile(filename), new File(restoreDirectory + "/" + filename));
        } else {
            try {
                TreeSet<ZipBackup> olderBackups = new TreeSet<ZipBackup>(m_zipBackupFiles.headSet(zipBackup, true));

                ZipBackup lastFullBackup = null;

                for (ZipBackup zipBackupEntry : olderBackups) {
                    if (zipBackupEntry.containsFullFile(filename)) {
                        lastFullBackup = zipBackupEntry;
                    }
                }

                if (lastFullBackup == null) {
                    System.out.println("Error no full backup found for file '" + filename + "'");
                    return false;
                }

                SortedSet<ZipBackup> exactBackupSet = olderBackups.tailSet(lastFullBackup, false);

                File origFile = File.createTempFile("tmp", ".orig", new File(restoreDirectory));
                File diffFile = File.createTempFile("tmp", ".diff", new File(restoreDirectory));

                writeFile(lastFullBackup.getInputStreamForFullFile(filename), origFile);

                for (ZipBackup zipBackupEntry : exactBackupSet) {
                    writeFile(zipBackupEntry.getInputStreamForDiffFile(filename), diffFile);
                    File patchedFile = FileDiffs.apply(origFile, new FileDiff(diffFile));

                    FileUtils.deleteQuietly(diffFile);
                    FileUtils.deleteQuietly(origFile);
                    FileUtils.moveFile(patchedFile, origFile);
                }

                File targetFile = new File(restoreDirectory + "/" + filename);
                FileUtils.deleteQuietly(targetFile);
                FileUtils.moveFile(origFile, targetFile);
                FileUtils.deleteQuietly(diffFile);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the most recent {@link ZipBackup} instance.
     *
     * @return the last {@link ZipBackup} instance
     */
    public ZipBackup last() {
        return m_zipBackupFiles.last();
    }

    /**
     * Returns the oldest {@link ZipBackup} instance.
     *
     * @return the first {@link ZipBackup} instance
     */
    public ZipBackup first() {
        return m_zipBackupFiles.first();
    }

    /**
     * Checks whether a file given by filename can be restored with the {@link ZipBackup} instances of this backup set.
     *
     * @param zipBackup the {@link ZipBackup} instance
     * @param filename  the filename of the file to be validated
     * @return true if valid, false otherwise
     */
    public boolean validateFile(ZipBackup zipBackup, String filename) {
        if (zipBackup.containsFullFile(filename)) {
            return true;
        } else {
            TreeSet<ZipBackup> olderBackups = new TreeSet<ZipBackup>(m_zipBackupFiles.headSet(zipBackup, true));

            ZipBackup lastFullBackup = null;

            for (ZipBackup zipBackupEntry : olderBackups) {
                if (zipBackupEntry.containsFullFile(filename)) {
                    lastFullBackup = zipBackupEntry;
                }
            }

            if (lastFullBackup == null) {
                return false;
            }

            SortedSet<ZipBackup> exactBackupSet = olderBackups.tailSet(lastFullBackup, false);

            if (!lastFullBackup.containsFullFile(filename)) {
                return false;
            }

            for (ZipBackup zipBackupEntry : exactBackupSet) {
                if (!zipBackupEntry.containsDiffFile(filename)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Creates a Xml representation of this backup set.
     *
     * @return the Xml {@link String}
     */
    public String xml() {
        RestBackupSet restBackupSet = new RestBackupSet();

        for (ZipBackup zipBackup : m_zipBackupFiles) {
            restBackupSet.getZipBackups().add(new RestZipBackup(zipBackup.getTimestamp()));
        }

        StringWriter stringWriter = new StringWriter();

        JAXB.marshal(restBackupSet, stringWriter);

        return stringWriter.getBuffer().toString();
    }

    /**
     * Returns a Xml representation of the files of a {@link ZipBackup} instance.
     *
     * @param zipBackup the {@link ZipBackup} instance
     * @return the Xml {@link String}
     */
    public String getFilesXml(ZipBackup zipBackup) {
        Set<String> files = getFiles(zipBackup);

        RestZipBackupContents restZipBackupContents = new RestZipBackupContents(zipBackup.getTimestamp(), files);

        StringWriter stringWriter = new StringWriter();

        JAXB.marshal(restZipBackupContents, stringWriter);

        return stringWriter.getBuffer().toString();
    }

    /**
     * Validates all files of a {@link ZipBackup} instance.
     *
     * @param zipBackup the {@link ZipBackup} instance
     * @return true if all files are valid, false otherwise
     */
    public boolean validate(ZipBackup zipBackup) {
        boolean result = true;
        for (String filename : getFiles(zipBackup)) {
            result &= validateFile(zipBackup, filename);
        }
        return result;
    }

    /**
     * Returns a {@link Set} of filenames of an {@link ZipBackup } instance
     *
     * @param zipBackup the {@link ZipBackup} instance
     * @return the set of filenames
     */
    public Set<String> getFiles(ZipBackup zipBackup) {
        Set<String> files = new TreeSet<String>();

        for (String filename : zipBackup.fullFiles()) {
            files.add(filename);
        }

        for (String filename : zipBackup.diffFiles()) {
            files.add(filename);
        }

        return files;
    }
}
