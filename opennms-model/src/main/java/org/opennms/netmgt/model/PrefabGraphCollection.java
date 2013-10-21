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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>PrefabGraphCollection class.</p>
 *
 * @author Christian Pape
 */
@XmlRootElement(name = "prefabgraphs")
public class PrefabGraphCollection extends LinkedList<PrefabGraph> {

    private static final long serialVersionUID = 4731486422555152257L;

    /**
     * <p>Constructor for OnmsCategoryCollection.</p>
     */
    public PrefabGraphCollection() {
        super();
    }

    /**
     * <p>Constructor for OnmsCategoryCollection.</p>
     *
     * @param c a {@link java.util.Collection} object.
     */
    public PrefabGraphCollection(Collection<? extends PrefabGraph> c) {
        super(c);
    }

    /**
     * <p>Constructor for OnmsCategoryCollection.</p>
     *
     * @param c a {@link java.util.Collection} object.
     */
    public PrefabGraphCollection(PrefabGraph[] c) {
        for (PrefabGraph prefabGraph : c) {
            add(prefabGraph);
        }
    }

    /**
     * <p>getPrefabGraphs</p>
     *
     * @return a {@link java.util.List} object.
     */
    @XmlElement(name = "prefabgraph")
    public List<PrefabGraph> getPrefabGraphs() {
        return this;
    }

    /**
     * <p>setPrefabGraphs</p>
     *
     * @param prefabGraphs a {@link java.util.List} object.
     */
    public void setPrefabGraphs(List<PrefabGraph> prefabGraphs) {
        if (prefabGraphs == this) {
            return;
        }
        clear();
        addAll(prefabGraphs);
    }

    /**
     * <p>getCount</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    @XmlAttribute(name = "count")
    public Integer getCount() {
        return this.size();
    }

    // make JaxbUtils happy (for a getter there must be a setter)
    public void setCount(Integer count) {
        ; // no implementation, because it is calculated
    }
}
