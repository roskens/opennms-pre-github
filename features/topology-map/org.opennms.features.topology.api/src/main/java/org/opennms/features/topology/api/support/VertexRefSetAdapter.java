/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013 The OpenNMS Group, Inc.
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

package org.opennms.features.topology.api.support;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.opennms.features.topology.api.topo.AbstractVertexRef;
import org.opennms.features.topology.api.topo.VertexRef;

/**
 * The Class VertexRefSetAdapter.
 */
public class VertexRefSetAdapter extends XmlAdapter<VertexRefSetAdapter.VertexRefSet, Set<VertexRef>> {

    /**
     * The Class VertexRefSet.
     */
    public static final class VertexRefSet {

        /** The entry. */
        public Set<VertexRefEntry> entry = new HashSet<VertexRefEntry>(0);
    }

    /**
     * The Class VertexRefEntry.
     */
    public static final class VertexRefEntry {

        /** The namespace. */
        @XmlAttribute
        public String namespace;

        /** The id. */
        @XmlAttribute
        public String id;

        /** The label. */
        @XmlAttribute
        public String label;
    }

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public VertexRefSetAdapter.VertexRefSet marshal(Set<VertexRef> v) throws Exception {
        if (v == null) {
            return null;
        } else {
            VertexRefSet retval = new VertexRefSet();
            for (VertexRef key : v) {
                VertexRefEntry entry = new VertexRefEntry();
                entry.namespace = key.getNamespace();
                entry.id = key.getId();
                entry.label = key.getLabel();
                retval.entry.add(entry);
            }
            return retval;
        }
    }

    /* (non-Javadoc)
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public Set<VertexRef> unmarshal(VertexRefSetAdapter.VertexRefSet v) throws Exception {
        if (v == null) {
            return null;
        } else {
            Set<VertexRef> retval = new HashSet<VertexRef>();
            for (VertexRefEntry entry : v.entry) {
                VertexRef ref = new AbstractVertexRef(entry.namespace, entry.id, entry.label);
                retval.add(ref);
            }
            return retval;
        }
    }
}
