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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class provides a {@link BackupSource} for the filesystem.
 *
 * @author Christian Pape
 */
public class FileSystemBackup implements BackupSource {
    /**
     * the filesystem base path
     */
    private String m_fileSystemBasePath;
    /**
     * the directories for backup
     */
    private Set<String> m_directories = new TreeSet<String>();

    /**
     * Constructor for instantiating new objects of this class.
     *
     * @param fileSystemBasePath base filesystem path
     * @param directories        the directories for backup
     */
    public FileSystemBackup(String fileSystemBasePath, String[] directories) {
        this.m_fileSystemBasePath = fileSystemBasePath;
        this.m_directories.addAll(Arrays.asList(directories));
    }

    /**
     * Constructor for instantiating new objects of this class.
     *
     * @param fileSystemBasePath base filesystem path
     * @param directories        the directories for backup
     */
    public FileSystemBackup(String fileSystemBasePath, Set<String> directories) {
        this.m_fileSystemBasePath = fileSystemBasePath;
        this.m_directories.addAll(directories);
    }

    /**
     * Returns the relative path of a file.
     *
     * @param baseDirectory the base directory
     * @param filePath      the file path
     * @return the relative path
     */
    private String getRelativePath(File baseDirectory, File filePath) {
        return baseDirectory.toURI().relativize(filePath.toURI()).getPath();
    }

    /**
     * Recursively searches for files in filesystem.
     *
     * @param path the path
     * @return a {@link Set} of filenames
     */
    private Set<String> searchFiles(String path) {
        Set<String> files = new TreeSet<String>();

        File directory = new File(path);

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                files.addAll(searchFiles(file.getAbsolutePath()));
            } else {
                files.add(getRelativePath(new File(m_fileSystemBasePath), file));
            }
        }

        return files;
    }

    /**
     * Returns the {@link Set} of full files filenames.
     *
     * @return the filenames
     */
    public Set<String> fullFiles() {
        Set<String> files = new TreeSet<String>();

        for (String directory : m_directories) {
            files.addAll(searchFiles(m_fileSystemBasePath + "/" + directory));
        }

        return files;
    }

    /**
     * Checks whether a full file with the given filename exists.
     *
     * @param filename the filename
     * @return true if existing, false otherwise
     */
    public boolean containsFullFile(String filename) {
        File file = new File(m_fileSystemBasePath + "/" + filename);
        return file.exists();
    }

    /**
     * Returns an {@link InputStream} for a file specified by filename.
     *
     * @param filename the filename
     * @return an {@link InputStream} instance
     */
    public InputStream getInputStreamForFullFile(String filename) {
        try {
            return new FileInputStream(new File(m_fileSystemBasePath + "/" + filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
