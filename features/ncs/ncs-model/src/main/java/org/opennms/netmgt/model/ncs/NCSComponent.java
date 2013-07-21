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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.MapKey;

/**
 * The Class NCSComponent.
 */
@Entity
@Table(name = "ncscomponent")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement(name = "component")
@XmlAccessorType(XmlAccessType.FIELD)
public class NCSComponent {

    /**
     * The Enum DependencyRequirements.
     */
    public enum DependencyRequirements {

        /** The any. */
        ANY,
 /** The all. */
 ALL
    };

    /**
     * The Class NodeIdentification.
     */
    @XmlRootElement(name = "node")
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class NodeIdentification {

        /** The m_foreign source. */
        @XmlAttribute(name = "foreignSource", required = true)
        private String m_foreignSource;

        /** The m_foreign id. */
        @XmlAttribute(name = "foreignId", required = true)
        private String m_foreignId;

        /**
         * Instantiates a new node identification.
         */
        public NodeIdentification() {
        }

        /**
         * Instantiates a new node identification.
         *
         * @param nodeForeignSource
         *            the node foreign source
         * @param nodeForeignId
         *            the node foreign id
         */
        public NodeIdentification(String nodeForeignSource, String nodeForeignId) {
            m_foreignSource = nodeForeignSource;
            m_foreignId = nodeForeignId;
        }

        /**
         * Gets the foreign source.
         *
         * @return the foreign source
         */
        @Column(name = "nodeForeignSource")
        public String getForeignSource() {
            return m_foreignSource;
        }

        /**
         * Sets the foreign source.
         *
         * @param foreignSource
         *            the new foreign source
         */
        public void setForeignSource(String foreignSource) {
            m_foreignSource = foreignSource;
        }

        /**
         * Gets the foreign id.
         *
         * @return the foreign id
         */
        @Column(name = "nodeForeignId")
        public String getForeignId() {
            return m_foreignId;
        }

        /**
         * Sets the foreign id.
         *
         * @param foreignId
         *            the new foreign id
         */
        public void setForeignId(String foreignId) {
            m_foreignId = foreignId;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((m_foreignId == null) ? 0 : m_foreignId.hashCode());
            result = prime * result + ((m_foreignSource == null) ? 0 : m_foreignSource.hashCode());
            return result;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            NodeIdentification other = (NodeIdentification) obj;
            if (m_foreignId == null) {
                if (other.m_foreignId != null)
                    return false;
            } else if (!m_foreignId.equals(other.m_foreignId))
                return false;
            if (m_foreignSource == null) {
                if (other.m_foreignSource != null)
                    return false;
            } else if (!m_foreignSource.equals(other.m_foreignSource))
                return false;
            return true;
        }

    }

    /** The m_id. */
    @XmlElement(name = "id")
    private Long m_id;

    /** The m_version. */
    @XmlTransient
    private Integer m_version;

    /** The m_foreign source. */
    @XmlAttribute(name = "foreignSource", required = true)
    private String m_foreignSource;

    /** The m_foreign id. */
    @XmlAttribute(name = "foreignId", required = true)
    private String m_foreignId;

    /** The m_type. */
    @XmlAttribute(name = "type", required = true)
    private String m_type;

    /** The m_name. */
    @XmlElement(name = "name")
    private String m_name;

    /** The m_node identification. */
    @XmlElement(name = "node")
    private NodeIdentification m_nodeIdentification;

    /** The m_up event uei. */
    @XmlElement(name = "upEventUei")
    private String m_upEventUei;

    /** The m_down event uei. */
    @XmlElement(name = "downEventUei")
    private String m_downEventUei;

    /** The m_dependencies required. */
    @XmlElement(name = "dependenciesRequired", required = false, defaultValue = "ALL")
    private DependencyRequirements m_dependenciesRequired;

    /** The m_attributes. */
    @XmlElement(name = "attributes", required = false)
    @XmlJavaTypeAdapter(JAXBMapAdapter.class)
    private Map<String, String> m_attributes = new LinkedHashMap<String, String>();

    /** The m_subcomponents. */
    @XmlElement(name = "component")
    private Set<NCSComponent> m_subcomponents = new LinkedHashSet<NCSComponent>();

    /** The m_parents. */
    @XmlTransient
    private Set<NCSComponent> m_parents = new LinkedHashSet<NCSComponent>();

    /**
     * Instantiates a new nCS component.
     *
     * @param type
     *            the type
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     */
    public NCSComponent(final String type, final String foreignSource, final String foreignId) {
        this();
        m_type = type;
        m_foreignSource = foreignSource;
        m_foreignId = foreignId;
    }

    /**
     * Instantiates a new nCS component.
     */
    public NCSComponent() {
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    public Long getId() {
        return m_id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(Long id) {
        m_id = id;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    @Version
    public Integer getVersion() {
        return m_version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the new version
     */
    public void setVersion(Integer version) {
        m_version = version;
    }

    /**
     * Gets the foreign source.
     *
     * @return the foreign source
     */
    public String getForeignSource() {
        return m_foreignSource;
    }

    /**
     * Sets the foreign source.
     *
     * @param foreignSource
     *            the new foreign source
     */
    public void setForeignSource(String foreignSource) {
        m_foreignSource = foreignSource;
    }

    /**
     * Gets the foreign id.
     *
     * @return the foreign id
     */
    public String getForeignId() {
        return m_foreignId;
    }

    /**
     * Sets the foreign id.
     *
     * @param foreignId
     *            the new foreign id
     */
    public void setForeignId(String foreignId) {
        m_foreignId = foreignId;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return m_type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(String type) {
        m_type = type;
    }

    /**
     * Gets the node identification.
     *
     * @return the node identification
     */
    public NodeIdentification getNodeIdentification() {
        return m_nodeIdentification;
    }

    /**
     * Sets the node identification.
     *
     * @param nodeIdentification
     *            the new node identification
     */
    public void setNodeIdentification(NodeIdentification nodeIdentification) {
        m_nodeIdentification = nodeIdentification;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * Gets the up event uei.
     *
     * @return the up event uei
     */
    public String getUpEventUei() {
        return m_upEventUei;
    }

    /**
     * Sets the up event uei.
     *
     * @param upEventUei
     *            the new up event uei
     */
    public void setUpEventUei(String upEventUei) {
        m_upEventUei = upEventUei;
    }

    /**
     * Gets the down event uei.
     *
     * @return the down event uei
     */
    public String getDownEventUei() {
        return m_downEventUei;
    }

    /**
     * Sets the down event uei.
     *
     * @param downEventUei
     *            the new down event uei
     */
    public void setDownEventUei(String downEventUei) {
        m_downEventUei = downEventUei;
    }

    /**
     * Gets the dependencies required.
     *
     * @return the dependencies required
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "depsRequired")
    public DependencyRequirements getDependenciesRequired() {
        return m_dependenciesRequired;
    }

    /**
     * Sets the dependencies required.
     *
     * @param dependenciesRequired
     *            the new dependencies required
     */
    public void setDependenciesRequired(DependencyRequirements dependenciesRequired) {
        m_dependenciesRequired = dependenciesRequired;
    }

    /**
     * Gets the parent components.
     *
     * @return the parent components
     */
    @ManyToMany
    @JoinTable(name = "subcomponents", joinColumns = { @JoinColumn(name = "subcomponent_id") }, inverseJoinColumns = { @JoinColumn(name = "component_id") })
    public Set<NCSComponent> getParentComponents() {
        return m_parents;
    }

    /**
     * Sets the parent components.
     *
     * @param parents
     *            the new parent components
     */
    public void setParentComponents(final Set<NCSComponent> parents) {
        m_parents = parents;
    }

    /**
     * Gets the subcomponents.
     *
     * @return the subcomponents
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subcomponents", joinColumns = { @JoinColumn(name = "component_id") }, inverseJoinColumns = { @JoinColumn(name = "subcomponent_id") })
    public Set<NCSComponent> getSubcomponents() {
        return m_subcomponents;
    }

    /**
     * Sets the subcomponents.
     *
     * @param subComponents
     *            the new subcomponents
     */
    public void setSubcomponents(Set<NCSComponent> subComponents) {
        m_subcomponents = subComponents;
    }

    /**
     * Adds the subcomponent.
     *
     * @param subComponent
     *            the sub component
     */
    public void addSubcomponent(NCSComponent subComponent) {
        getSubcomponents().add(subComponent);
    }

    /**
     * Removes the subcomponent.
     *
     * @param subComponent
     *            the sub component
     */
    public void removeSubcomponent(NCSComponent subComponent) {
        getSubcomponents().remove(subComponent);
    }

    /**
     * Gets the subcomponent.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @return the subcomponent
     */
    public NCSComponent getSubcomponent(String foreignSource, String foreignId) {
        for (NCSComponent subcomponent : getSubcomponents()) {
            if (subcomponent.hasIdentity(foreignSource, foreignId)) {
                return subcomponent;
            }
        }
        return null;
    }

    /**
     * Checks for identity.
     *
     * @param foreignSource
     *            the foreign source
     * @param foreignId
     *            the foreign id
     * @return true, if successful
     */
    public boolean hasIdentity(String foreignSource, String foreignId) {
        return m_foreignSource.equals(foreignSource) && m_foreignId.equals(foreignId);
    }

    /**
     * Gets the attributes.
     *
     * @return the attributes
     */
    @CollectionOfElements
    @JoinTable(name = "ncs_attributes")
    @MapKey(columns = @Column(name = "key"))
    @Column(name = "value", nullable = false)
    public Map<String, String> getAttributes() {
        return m_attributes;
    }

    /**
     * Sets the attributes.
     *
     * @param attributes
     *            the attributes
     */
    public void setAttributes(Map<String, String> attributes) {
        m_attributes = attributes;
    }

    /**
     * Sets the attribute.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     */
    public void setAttribute(String key, String value) {
        m_attributes.put(key, value);
    }

    /**
     * Removes the attribute.
     *
     * @param key
     *            the key
     * @return the string
     */
    public String removeAttribute(String key) {
        return m_attributes.remove(key);
    }

    /**
     * Visit.
     *
     * @param visitor
     *            the visitor
     */
    public void visit(NCSComponentVisitor visitor) {
        // visit this component
        visitor.visitComponent(this);

        // visit subcomponents
        for (NCSComponent subcomponent : getSubcomponents()) {
            subcomponent.visit(visitor);
        }

        // complete visiting this component
        visitor.completeComponent(this);
    }
}
