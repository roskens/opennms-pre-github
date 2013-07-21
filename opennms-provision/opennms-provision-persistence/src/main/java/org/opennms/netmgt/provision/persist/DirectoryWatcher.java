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
package org.opennms.netmgt.provision.persist;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.opennms.core.utils.FileReloadCallback;
import org.opennms.core.utils.FileReloadContainer;

/**
 * The Class DirectoryWatcher.
 *
 * @param <T>
 *            the generic type
 */
public class DirectoryWatcher<T> {

    /** The m_directory. */
    private File m_directory;

    /** The m_loader. */
    private FileReloadCallback<T> m_loader;

    /** The m_contents. */
    private ConcurrentHashMap<String, FileReloadContainer<T>> m_contents = new ConcurrentHashMap<String, FileReloadContainer<T>>();

    /** The m_file names. */
    private AtomicReference<Set<String>> m_fileNames = new AtomicReference<Set<String>>();

    /**
     * Instantiates a new directory watcher.
     *
     * @param directory
     *            the directory
     * @param loader
     *            the loader
     */
    public DirectoryWatcher(File directory, FileReloadCallback<T> loader) {
        m_directory = directory;
        m_directory.mkdirs();
        m_loader = loader;
    }

    /**
     * Gets the file names.
     *
     * @return the file names
     */
    public Set<String> getFileNames() {
        return checkFileChanges();
    }

    /**
     * Check file changes.
     *
     * @return the sets the
     */
    private Set<String> checkFileChanges() {
        while (true) {
            LinkedHashSet<String> fileNames = new LinkedHashSet<String>(Arrays.asList(m_directory.list()));
            Set<String> oldFileNames = m_fileNames.get();
            if (fileNames.equals(oldFileNames)) {
                return fileNames;
            } else if (m_fileNames.compareAndSet(oldFileNames, fileNames)) {
                m_contents.clear();
                return fileNames;
            }
        }
    }

    /**
     * Gets the base names with extension.
     *
     * @param extension
     *            the extension
     * @return the base names with extension
     */
    public Set<String> getBaseNamesWithExtension(String extension) {
        Set<String> fileNames = checkFileChanges();
        Set<String> basenames = new LinkedHashSet<String>();
        for (String fileName : fileNames) {
            if (fileName.endsWith(extension)) {
                String basename = fileName.substring(0, fileName.length() - extension.length());
                basenames.add(basename);
            }
        }
        return basenames;
    }

    /**
     * Gets the contents.
     *
     * @param fileName
     *            the file name
     * @return the contents
     * @throws FileNotFoundException
     *             the file not found exception
     */
    public T getContents(String fileName) throws FileNotFoundException {
        checkFileChanges();

        File file = new File(m_directory, fileName);

        if (file.exists()) {
            FileReloadContainer<T> newContainer = new FileReloadContainer<T>(file, m_loader);
            newContainer.setReloadCheckInterval(0);
            FileReloadContainer<T> container = m_contents.putIfAbsent(file.getName(), newContainer);
            if (container == null) {
                container = newContainer;
            }
            return container.getObject();
        } else {
            m_contents.remove(fileName);
            throw new FileNotFoundException("there is no file " + fileName + " in directory " + m_directory);
        }

    }

}
