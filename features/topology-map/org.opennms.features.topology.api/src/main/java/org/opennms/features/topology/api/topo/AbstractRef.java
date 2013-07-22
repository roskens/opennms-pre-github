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

package org.opennms.features.topology.api.topo;

/**
 * The Class AbstractRef.
 */
public class AbstractRef implements Ref {

    /** The m_namespace. */
    private final String m_namespace;

    /** The m_id. */
    private final String m_id;

    /** The m_label. */
    private String m_label;

    /**
     * Instantiates a new abstract ref.
     *
     * @param namespace
     *            the namespace
     * @param id
     *            the id
     * @param label
     *            the label
     */
    protected AbstractRef(String namespace, String id, String label) {
        m_namespace = namespace;
        m_id = id;
        m_label = label;
    }

    /**
     * Instantiates a new abstract ref.
     *
     * @param ref
     *            the ref
     */
    protected AbstractRef(Ref ref) {
        this(ref.getNamespace(), ref.getId(), ref.getLabel());
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Ref#getId()
     */
    @Override
    public final String getId() {
        return m_id;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Ref#getNamespace()
     */
    @Override
    public final String getNamespace() {
        return m_namespace;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.Ref#getLabel()
     */
    @Override
    public final String getLabel() {
        return m_label;
    }

    /**
     * Sets the label.
     *
     * @param label
     *            the new label
     */
    public final void setLabel(String label) {
        m_label = label;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getNamespace() == null) ? 0 : getNamespace().hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Ref)) {
            return false;
        }

        Ref ref = (Ref) obj;

        return getNamespace().equals(ref.getNamespace()) && getId().equals(ref.getId());

    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Ref o) {
        if (this.equals(o)) {
            return 0;
        } else {
            // Order by namespace, then ID
            if (this.getNamespace().equals(o.getNamespace())) {
                if (this.getId().equals(o.getId())) {
                    // Shouldn't happen because equals() should return true
                    throw new IllegalStateException("equals() was inaccurate in " + this.getClass().getName());
                } else {
                    return this.getId().compareTo(o.getId());
                }
            } else {
                return this.getNamespace().compareTo(o.getNamespace());
            }
        }
    }
}
