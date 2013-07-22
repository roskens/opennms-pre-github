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

package org.opennms.netmgt.model.ncs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * The Class JAXBMapAdapter.
 */
@XmlTransient
public class JAXBMapAdapter extends XmlAdapter<JAXBMapAdapter.JAXBMap, Map<String, String>> {

    /**
     * The Class JAXBMap.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "attributes")
    public static class JAXBMap {

        /** The a. */
        @XmlElement(name = "attribute", required = true)
        private final List<JAXBMapEntry> a = new ArrayList<JAXBMapEntry>();

        /**
         * Gets the a.
         *
         * @return the a
         */
        public List<JAXBMapEntry> getA() {
            return this.a;
        }
    }

    /**
     * The Class JAXBMapEntry.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "attribute")
    public static class JAXBMapEntry {

        /** The key. */
        @XmlElement(name = "key", required = true)
        private final String key;

        /** The value. */
        @XmlElement(name = "value", required = true)
        private final String value;

        /**
         * Instantiates a new jAXB map entry.
         *
         * @param key
         *            the key
         * @param value
         *            the value
         */
        public JAXBMapEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Instantiates a new jAXB map entry.
         */
        public JAXBMapEntry() {
            this.key = null;
            this.value = null;
        }

        /**
         * Gets the key.
         *
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * Gets the value.
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }
    }

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public JAXBMap marshal(Map<String, String> v) throws Exception {
        if (v.isEmpty()) {
            return null;
        }
        JAXBMap myMap = new JAXBMap();
        List<JAXBMapEntry> aList = myMap.getA();
        for (Map.Entry<String, String> e : v.entrySet()) {
            aList.add(new JAXBMapEntry(e.getKey(), e.getValue()));
        }
        return myMap;
    }

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public Map<String, String> unmarshal(JAXBMap v) throws Exception {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (JAXBMapEntry e : v.getA()) {
            map.put(e.getKey(), e.getValue());
        }
        return map;
    }
}
