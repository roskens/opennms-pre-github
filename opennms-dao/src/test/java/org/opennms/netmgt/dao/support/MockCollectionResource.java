/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao.support;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.opennms.core.utils.TimeKeeper;
import org.opennms.netmgt.config.collector.CollectionAttribute;
import org.opennms.netmgt.config.collector.CollectionAttributeType;
import org.opennms.netmgt.config.collector.CollectionResource;
import org.opennms.netmgt.config.collector.CollectionSetVisitor;
import org.opennms.netmgt.config.collector.Persister;
import org.opennms.netmgt.config.collector.ServiceParameters;
import org.opennms.netmgt.model.RrdRepository;

/**
 * MockCollectionResource.
 *
 * @author <a href="mail:agalue@opennms.org">Alejandro Galue</a>
 */
public class MockCollectionResource implements CollectionResource {

    /** The parent. */
    private String parent;

    /** The instance. */
    private String instance;

    /** The type. */
    private String type;

    /** The attributes. */
    private Map<String, String> attributes = new HashMap<String, String>();

    /**
     * Instantiates a new mock collection resource.
     *
     * @param parent
     *            the parent
     * @param instance
     *            the instance
     * @param type
     *            the type
     */
    public MockCollectionResource(String parent, String instance, String type) {
        this.parent = parent;
        this.instance = instance;
        this.type = type;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.ResourceIdentifier#getOwnerName()
     */
    @Override
    public String getOwnerName() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.ResourceIdentifier#getResourceDir(org.opennms.netmgt.model.RrdRepository)
     */
    @Override
    public File getResourceDir(RrdRepository repository) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionResource#shouldPersist(org.opennms.netmgt.config.collector.ServiceParameters)
     */
    @Override
    public boolean shouldPersist(ServiceParameters params) {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionResource#rescanNeeded()
     */
    @Override
    public boolean rescanNeeded() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionResource#visit(org.opennms.netmgt.config.collector.CollectionSetVisitor)
     */
    @Override
    public void visit(CollectionSetVisitor visitor) {
        for (Entry<String, String> entry : attributes.entrySet()) {
            final CollectionResource resource = this;
            final String attrName = entry.getKey();
            final String attrValue = entry.getValue();
            CollectionAttribute attribute = new CollectionAttribute() {
                @Override
                public CollectionResource getResource() {
                    return resource;
                }

                @Override
                public String getStringValue() {
                    return attrValue;
                }

                @Override
                public String getNumericValue() {
                    return attrValue;
                }

                @Override
                public String getName() {
                    return attrName;
                }

                @Override
                public void storeAttribute(Persister persister) {
                }

                @Override
                public boolean shouldPersist(ServiceParameters params) {
                    return true;
                }

                @Override
                public CollectionAttributeType getAttributeType() {
                    return null;
                }

                @Override
                public void visit(CollectionSetVisitor visitor) {
                }

                @Override
                public String getType() {
                    return "string";
                }

                @Override
                public String getMetricIdentifier() {
                    return "MOCK_" + getName();
                }
            };
            visitor.visitAttribute(attribute);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionResource#getType()
     */
    @Override
    public int getType() {
        return 0;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionResource#getResourceTypeName()
     */
    @Override
    public String getResourceTypeName() {
        return type;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionResource#getParent()
     */
    @Override
    public String getParent() {
        return parent;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionResource#getInstance()
     */
    @Override
    public String getInstance() {
        return instance;
    }

    /**
     * Sets the instance.
     *
     * @param instance
     *            the new instance
     */
    public void setInstance(String instance) {
        this.instance = instance;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionResource#getLabel()
     */
    @Override
    public String getLabel() {
        return null;
    }

    /**
     * Gets the attribtue map.
     *
     * @return the attribtue map
     */
    public Map<String, String> getAttribtueMap() {
        return attributes;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.collector.CollectionResource#getTimeKeeper()
     */
    @Override
    public TimeKeeper getTimeKeeper() {
        return null;
    }

}
