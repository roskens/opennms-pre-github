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

/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.opennms.netmgt.xml.event;

//---------------------------------/
//- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The trouble ticket info with state on/off determining if
 * action is taken on the trouble ticket.
 *
 * @version $Revision$ $Date$
 */

@XmlRootElement(name = "tticket")
@XmlAccessorType(XmlAccessType.FIELD)
// @ValidateUsing("event.xsd")
public class Tticket implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -691077894886561643L;

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** internal content storage. */
    @XmlValue
    private java.lang.String _content = "";

    /**
     * Field _state.
     */
    @XmlAttribute(name = "state")
    private java.lang.String _state = "on";

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Instantiates a new tticket.
     */
    public Tticket() {
        super();
        setContent("");
        setState("on");
    }

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     *
     * @return the value of field 'Content'.
     */
    public java.lang.String getContent() {
        return this._content;
    }

    /**
     * Returns the value of field 'state'.
     *
     * @return the value of field 'State'.
     */
    public java.lang.String getState() {
        return this._state;
    }

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     *
     * @param content
     *            the value of field 'content'.
     */
    public void setContent(final java.lang.String content) {
        this._content = content;
    }

    /**
     * Sets the value of field 'state'.
     *
     * @param state
     *            the value of field 'state'.
     */
    public void setState(final java.lang.String state) {
        this._state = state;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("content", _content).append("state", _state).toString();
    }
}
