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

package org.opennms.netmgt.jasper.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The Class ResourcePathFileTraversal.
 */
public class ResourcePathFileTraversal {

    /** The m_file. */
    private final File m_file;

    /** The m_data source filter list. */
    private List<String> m_dataSourceFilterList = new ArrayList<String>();

    /**
     * Instantiates a new resource path file traversal.
     *
     * @param f
     *            the f
     */
    public ResourcePathFileTraversal(File f) {
        m_file = f;
        if (!m_file.exists()) {
            System.err.println("Directory does not exist: " + f.getAbsolutePath());
        }
    }

    /**
     * Traverse directory.
     *
     * @return the list
     */
    public List<String> traverseDirectory() {
        List<String> paths = new ArrayList<String>();

        addTopLevelIfNecessary(paths);

        traverseDirectory(m_file, paths);
        return paths;
    }

    /**
     * Adds the top level if necessary.
     *
     * @param paths
     *            the paths
     */
    private void addTopLevelIfNecessary(List<String> paths) {
        File[] fList = m_file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                if (f.isFile()) {
                    onDirectory(m_file, paths);
                    break;
                }
            }
        }

    }

    /**
     * Traverse directory.
     *
     * @param f
     *            the f
     * @param dirPaths
     *            the dir paths
     */
    private void traverseDirectory(File f, List<String> dirPaths) {
        if (f.isDirectory()) {

            final File[] children = f.listFiles();

            for (File child : children) {
                if (child.isDirectory()) {
                    onDirectory(child, dirPaths);
                    traverseDirectory(child, dirPaths);
                }

            }
            return;
        }

        onFile(f);
    }

    /**
     * On file.
     *
     * @param f
     *            the f
     */
    private void onFile(File f) {
        System.err.println(f.getName());
    }

    /**
     * On directory.
     *
     * @param f
     *            the f
     * @param dirPaths
     *            the dir paths
     */
    private void onDirectory(File f, List<String> dirPaths) {
        if (System.getProperty("org.opennms.rrd.storeByGroup") != null
                && System.getProperty("org.opennms.rrd.storeByGroup").toLowerCase().equals("true")) {
            try {
                if (validateDataSource(f)) {
                    dirPaths.add(f.getAbsolutePath());
                }
            } catch (IOException ioException) {

            }
        } else {
            if (validateFiles(f)) {
                dirPaths.add(f.getAbsolutePath());
            }
        }

    }

    /**
     * Validate data source.
     *
     * @param f
     *            the f
     * @return true, if successful
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private boolean validateDataSource(File f) throws IOException {
        FilenameFilter dsFilenameFilter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.equals("ds.properties");
            }
        };

        if (f.list(dsFilenameFilter).length > 0) {
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream(f.getAbsolutePath() + "/ds.properties");
            prop.load(fis);
            fis.close();

            for (String datasource : m_dataSourceFilterList) {
                if (prop.get(datasource) == null) {
                    return false;
                }
            }

        }

        return true;
    }

    /**
     * Validate files.
     *
     * @param f
     *            the f
     * @return true, if successful
     */
    private boolean validateFiles(final File f) {
        List<FilenameFilter> filterList = getFilenameFilters();
        for (FilenameFilter filter : filterList) {
            String[] files = f.list(filter);
            if (files.length == 0) {
                return false;
            }
        }

        return true;

    }

    /**
     * Gets the filename filters.
     *
     * @return the filename filters
     */
    private List<FilenameFilter> getFilenameFilters() {
        List<FilenameFilter> filters = new ArrayList<FilenameFilter>();
        for (final String dsName : m_dataSourceFilterList) {
            filters.add(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {

                    return name.contains(dsName);
                }
            });
        }
        return filters;
    }

    /**
     * Adds the datasource filters.
     *
     * @param dsNames
     *            the ds names
     */
    public void addDatasourceFilters(String[] dsNames) {

        if (dsNames != null) {
            for (String dsName : dsNames) {
                m_dataSourceFilterList.add(dsName);
            }
        }

    }

    /**
     * Adds the datasource filter.
     *
     * @param dataSource
     *            the data source
     */
    public void addDatasourceFilter(String dataSource) {
        m_dataSourceFilterList.add(dataSource);
    }

}
