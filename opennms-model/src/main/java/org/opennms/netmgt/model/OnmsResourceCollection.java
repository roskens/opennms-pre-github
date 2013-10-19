/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2008-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>OnmsResourceCollection class.</p>
 *
 * @author Christian Pape
 */
@XmlRootElement(name = "resources")
public class OnmsResourceCollection extends LinkedList<OnmsResource> {

    private static final long serialVersionUID = 4731486422555152257L;

    /**
     * <p>Constructor for OnmsResourceCollection.</p>
     */
    public OnmsResourceCollection() {
        super();
    }

    /**
     * <p>Constructor for OnmsResourceCollection.</p>
     *
     * @param c a {@link java.util.Collection} object.
     */
    public OnmsResourceCollection(Collection<? extends OnmsResource> c) {
        super(c);
    }

    /**
     * <p>getResources</p>
     *
     * @return a {@link java.util.List} object.
     */
    @XmlElement(name = "resource")
    public List<OnmsResource> getResources() {
        return this;
    }

    /**
     * <p>setResources</p>
     *
     * @param onmsResources a {@link java.util.List} object.
     */
    public void setResources(List<OnmsResource> onmsResources) {
        if (onmsResources == this) return;
        clear();
        addAll(onmsResources);
    }

    /**
     * <p>getCount</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    @XmlAttribute(name="count")
    public Integer getCount() {
        return this.size();
    }

    // make JaxbUtils happy (for a getter there must be a setter)
    public void setCount(Integer count) {
        ; // no implementation, because it is calculated
    }
}
