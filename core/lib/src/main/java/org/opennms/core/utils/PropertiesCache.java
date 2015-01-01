/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Caches properties files in order to improve performance.
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @version $Id: $
 */
public class PropertiesCache {

	private static final Logger LOG = LoggerFactory.getLogger(PropertiesCache.class);

    public static final String CHECK_LAST_MODIFY_STRING = "org.opennms.utils.propertiesCache.enableCheckFileModified";

    private static class PropertiesHolder {
        private Properties m_properties;
        private final Path m_path;
        private final Lock lock = new ReentrantLock();
        private FileTime m_lastModify = FileTime.fromMillis(0L);
        private boolean m_checkLastModify = Boolean.getBoolean(CHECK_LAST_MODIFY_STRING);

        PropertiesHolder(Path file) {
            m_path = file;
            m_properties = null;
        }

        private Properties read() throws IOException {
            if (!Files.isReadable(m_path)) {
                return null;
            }

            try (InputStream in = Files.newInputStream(m_path);) {
                Properties prop = new Properties();
                prop.load(in);
                if (m_checkLastModify) {
                    m_lastModify = (FileTime) Files.getAttribute(m_path, "basic:lastModifiedTime");
                }
                return prop;
            }
        }

        private void write() throws IOException {
            if (!Files.exists(m_path.getParent())) {
                Path parent = Files.createDirectories(m_path.getParent());
                if (!Files.exists(parent)) {
                    LOG.warn("Could not make directory: {}", parent);
            	}
            }
            try (OutputStream out = Files.newOutputStream(m_path);) {
                m_properties.store(out, null);
            }
        }

        public Properties get() throws IOException {
            lock.lock();
            try {
                if (m_properties == null) {
                    readWithDefault(new Properties());
                } else {
                    if (m_checkLastModify && Files.isReadable(m_path) && !m_lastModify.equals((FileTime) Files.getAttribute(m_path, "basic:lastModifiedTime"))) {
                        m_properties = read();
                    }
                }
                return m_properties;
            } finally {
                lock.unlock();
            }
        }

        private void readWithDefault(Properties deflt) throws IOException {
            // this is
            if (deflt == null && !Files.isReadable(m_path)) {
                // nothing to load so m_properties remains null no writing necessary
                // just return to avoid getting the write lock
                return;
            }

            if (m_properties == null) {
                m_properties = read();
                if (m_properties == null) {
                    m_properties = deflt;
                }
            }
        }

        public void put(Properties properties) throws IOException {
            lock.lock();
            try {
                m_properties = properties;
                write();
            } finally {
                lock.unlock();
            }
        }

        public void update(Map<String, String> props) throws IOException {
            if (props == null) return;
            lock.lock();
            try {
                boolean save = false;
                for(Entry<String, String> e : props.entrySet()) {
                    if (!e.getValue().equals(get().get(e.getKey()))) {
                        get().put(e.getKey(), e.getValue());
                        save = true;
                    }
                }
                if (save) {
                    write();
                }
            } finally {
                lock.unlock();
            }
        }

        public void setProperty(String key, String value) throws IOException {
            lock.lock();
            try {
                // first we do get to make sure the properties are loaded
                get();
                if (!value.equals(get().get(key))) {
                    get().put(key, value);
                    write();
                }
            } finally {
                lock.unlock();
            }
        }

        public Properties find() throws IOException {
            lock.lock();
            try {
                if (m_properties == null) {
                    readWithDefault(null);
                }
                return m_properties;
            } finally {
                lock.unlock();
            }
        }

        public String getProperty(String key) throws IOException {
            lock.lock();
            try {
                return get().getProperty(key);
            } finally {
                lock.unlock();
            }

        }

    }


    private Map<String, PropertiesHolder> m_cache = new TreeMap<String, PropertiesHolder>();

    private PropertiesHolder getHolder(Path propFile) throws IOException {
        String key = propFile.normalize().toString();
        synchronized (m_cache) {
            PropertiesHolder holder = m_cache.get(key);
            if (holder == null) {
                holder = new PropertiesHolder(propFile);
                m_cache.put(key, holder);
            }
            return holder;
        }
    }

    /**
     * <p>clear</p>
     */
    public void clear() {
        synchronized (m_cache) {
            m_cache.clear();
        }
    }

    /**
     * Get the current properties object from the cache loading it in memory
     *
     * @param propFile a {@link java.io.File} object.
     * @throws java.io.IOException if any.
     * @return a {@link java.util.Properties} object.
     */
    public Properties getProperties(Path propFile) throws IOException {
        return getHolder(propFile).get();
    }

    /**
     * <p>findProperties</p>
     *
     * @param propFile a {@link java.io.File} object.
     * @return a {@link java.util.Properties} object.
     * @throws java.io.IOException if any.
     */
    public Properties findProperties(Path propFile) throws IOException {
        return getHolder(propFile).find();
    }
    /**
     * <p>saveProperties</p>
     *
     * @param propFile a {@link java.io.File} object.
     * @param properties a {@link java.util.Properties} object.
     * @throws java.io.IOException if any.
     */
    public void saveProperties(final Path propFile, final Properties properties) throws IOException {
        getHolder(propFile).put(properties);
    }

    public void saveProperties(final Path propFile, final Map<String, String> attributeMappings) throws IOException {
        if (attributeMappings == null) return;
        final Properties properties = new Properties();
        properties.putAll(attributeMappings);
        saveProperties(propFile, properties);
    }

    /**
     * <p>updateProperties</p>
     *
     * @param propFile a {@link java.io.File} object.
     * @param props a {@link java.util.Map} object.
     * @throws java.io.IOException if any.
     */
    public void updateProperties(Path propFile, Map<String, String> props) throws IOException {
        getHolder(propFile).update(props);
    }

    /**
     * <p>setProperty</p>
     *
     * @param propFile a {@link java.io.File} object.
     * @param key a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public void setProperty(Path propFile, String key, String value) throws IOException {
        getHolder(propFile).setProperty(key, value);
    }

    /**
     * <p>getProperty</p>
     *
     * @param propFile a {@link java.io.File} object.
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public String getProperty(Path propFile, String key) throws IOException {
        return getHolder(propFile).getProperty(key);
    }

}
