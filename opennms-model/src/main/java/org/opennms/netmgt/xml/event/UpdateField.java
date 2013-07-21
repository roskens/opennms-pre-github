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

package org.opennms.netmgt.xml.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Object used to identify which alarm fields should be updated during Alarm
 * reduction.
 *
 * @author <a href="mailto:david@opennms.org>David Hustace</a>
 */
@XmlRootElement(name = "update-field")
@XmlAccessorType(XmlAccessType.FIELD)
// @ValidateUsing("event.xsd")
public class UpdateField {

    /** The m_field name. */
    @XmlAttribute(name = "field-name", required = true)
    private java.lang.String m_fieldName;

    /** The m_update on reduction. */
    @XmlAttribute(name = "update-on-reduction", required = false)
    private java.lang.Boolean m_updateOnReduction = Boolean.TRUE;

    /**
     * Gets the field name.
     *
     * @return the field name
     */
    public String getFieldName() {
        return m_fieldName;
    }

    /**
     * Sets the field name.
     *
     * @param fieldName
     *            the new field name
     */
    public void setFieldName(String fieldName) {
        m_fieldName = fieldName;
    }

    /**
     * Checks if is update on reduction.
     *
     * @return the boolean
     */
    public Boolean isUpdateOnReduction() {
        return m_updateOnReduction;
    }

    /**
     * Sets the update on reduction.
     *
     * @param update
     *            the new update on reduction
     */
    public void setUpdateOnReduction(Boolean update) {
        m_updateOnReduction = update;
    }
}
