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

package org.opennms.features.jmxconfiggenerator.webui.data;

import org.opennms.xmlns.xsd.config.jmx_datacollection.Attrib;
import org.opennms.xmlns.xsd.config.jmx_datacollection.CompAttrib;

/**
 * Meta interface to address all properties of an Attrib bean in vaadin
 * framework. In this way we do not need use strings!
 *
 * @author Markus von Rüden
 * @see org.opennms.xmlns.xsd.config.jmx_datacollection.Attrib
 */
public interface MetaAttribItem {

    /**
     * The Enum AttribType.
     */
    public static enum AttribType {

        /** The counter. */
        counter,
 /** The gauge. */
 gauge;

        /**
         * Value of.
         *
         * @param object
         *            the object
         * @return the attrib type
         */
        public static AttribType valueOf(Object object) {
            return gauge;
        }

        /**
         * Value of.
         *
         * @param attrib
         *            the attrib
         * @return the attrib type
         */
        public static AttribType valueOf(Attrib attrib) {
            return attrib == null ? gauge : valueOf(attrib.getType());
        }

        /**
         * Value of.
         *
         * @param attrib
         *            the attrib
         * @return the attrib type
         */
        public static AttribType valueOf(CompAttrib attrib) {
            return attrib == null ? gauge : valueOf(attrib.getType());
        }
    }

    /** The name. */
    String NAME = "name";

    /** The selected. */
    String SELECTED = "selected";

    /** The alias. */
    String ALIAS = "alias";

    /** The type. */
    String TYPE = "type";
}
