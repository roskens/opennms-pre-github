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

package org.opennms.features.topology.app.internal.support;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.features.topology.api.IconRepository;
import org.slf4j.LoggerFactory;

/**
 * The Class IconRepositoryManager.
 */
public class IconRepositoryManager {

    /**
     * The Class ConfigIconRepository.
     */
    private class ConfigIconRepository implements IconRepository {

        /** The m_icon map. */
        private Map<String, String> m_iconMap = new HashMap<String, String>();

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.IconRepository#contains(java.lang.String)
         */
        @Override
        public boolean contains(String type) {
            return m_iconMap.containsKey(type);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.api.IconRepository#getIconUrl(java.lang.String)
         */
        @Override
        public String getIconUrl(String type) {
            return m_iconMap.get(type);
        }

        /**
         * Adds the icon config.
         *
         * @param key
         *            the key
         * @param url
         *            the url
         */
        public void addIconConfig(String key, String url) {
            if (m_iconMap.containsKey(key)) {
                m_iconMap.remove(key);
            }
            m_iconMap.put(key, url);
        }

    }

    /** The m_icon repos. */
    private List<IconRepository> m_iconRepos = new ArrayList<IconRepository>();

    /** The m_config repo. */
    private ConfigIconRepository m_configRepo = new ConfigIconRepository();

    /**
     * Instantiates a new icon repository manager.
     */
    public IconRepositoryManager() {
        m_iconRepos.add(m_configRepo);
    }

    /**
     * Adds the repository.
     *
     * @param iconRepo
     *            the icon repo
     */
    public void addRepository(IconRepository iconRepo) {
        m_iconRepos.add(iconRepo);
    }

    /**
     * On bind.
     *
     * @param iconRepo
     *            the icon repo
     */
    public synchronized void onBind(IconRepository iconRepo) {
        try {
            addRepository(iconRepo);
        } catch (Throwable e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception during onBind()", e);
        }
    }

    /**
     * On unbind.
     *
     * @param iconRepo
     *            the icon repo
     */
    public synchronized void onUnbind(IconRepository iconRepo) {
        try {
            m_iconRepos.remove(iconRepo);
        } catch (Throwable e) {
            LoggerFactory.getLogger(this.getClass()).warn("Exception during onUnbind()", e);
        }
    }

    /**
     * Lookup icon url for exact key.
     *
     * @param key
     *            the key
     * @return the string
     */
    public String lookupIconUrlForExactKey(String key) {
        for (IconRepository iconRepo : m_iconRepos) {
            if (iconRepo.contains(key)) {
                return iconRepo.getIconUrl(key);
            }
        }
        return null;
    }

    /**
     * Find icon url by key.
     *
     * @param key
     *            the key
     * @return the string
     */
    public String findIconUrlByKey(String key) {

        if (key != null) {
            // if the exact key is configured then use it
            String iconUrl = lookupIconUrlForExactKey(key);
            if (iconUrl != null) {
                return iconUrl;
            }

            //
            int lastColon = key.lastIndexOf(':');
            if ("default".equals(key)) {
                // we got here an no default icon was registered!!
                return "theme://images/server.png";
            } else if (lastColon == -1) {
                // no colons in key so just return 'default' icon
                return findIconUrlByKey("default");
            } else {
                // remove the segment following the last colon
                String newPrefix = key.substring(0, lastColon);
                String suffix = key.substring(lastColon + 1);
                if (!"default".equals(suffix)) {
                    // see if there an icon registered as <prefix>:default
                    return findIconUrlByKey(newPrefix + ":default");
                } else {
                    // if we have tried the :default and got all the way here
                    // just try the prefix
                    return findIconUrlByKey(newPrefix);
                }
            }
        } else {
            return findIconUrlByKey("default");
        }

    }

    /**
     * Update icon config.
     *
     * @param properties
     *            the properties
     */
    public void updateIconConfig(Dictionary<String, ?> properties) {
        Enumeration<String> keys = properties.keys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String url = (String) properties.get(key);
            m_configRepo.addIconConfig(key, url);
        }
    }
}
