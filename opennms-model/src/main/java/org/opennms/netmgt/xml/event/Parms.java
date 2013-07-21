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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The varbinds from the trap.
 *
 * @version $Revision$ $Date$
 */

@XmlRootElement(name = "parms")
@XmlAccessorType(XmlAccessType.FIELD)
@Deprecated
// @ValidateUsing("event.xsd")
public class Parms implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1361348948961582446L;

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** A varbind from the trap. */
    @XmlElement(name = "parm", required = true)
    private java.util.List<org.opennms.netmgt.xml.event.Parm> _parmList;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Instantiates a new parms.
     */
    public Parms() {
        super();
        this._parmList = new java.util.ArrayList<org.opennms.netmgt.xml.event.Parm>();
    }

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Adds the parm.
     *
     * @param vParm
     *            the v parm
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addParm(final org.opennms.netmgt.xml.event.Parm vParm) throws java.lang.IndexOutOfBoundsException {
        this._parmList.add(vParm);
    }

    /**
     * Adds the parm.
     *
     * @param index
     *            the index
     * @param vParm
     *            the v parm
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addParm(final int index, final org.opennms.netmgt.xml.event.Parm vParm)
            throws java.lang.IndexOutOfBoundsException {
        this._parmList.add(index, vParm);
    }

    /**
     * Method enumerateParm.
     *
     * @return an Enumeration over all possible elements of this
     *         collection
     */
    public java.util.Enumeration<org.opennms.netmgt.xml.event.Parm> enumerateParm() {
        return java.util.Collections.enumeration(this._parmList);
    }

    /**
     * Method getParm.
     *
     * @param index
     *            the index
     * @return the value of the org.opennms.netmgt.xml.event.Parm
     *         at the given index
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public org.opennms.netmgt.xml.event.Parm getParm(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._parmList.size()) {
            throw new IndexOutOfBoundsException("getParm: Index value '" + index + "' not in range [0.."
                    + (this._parmList.size() - 1) + "]");
        }

        return (org.opennms.netmgt.xml.event.Parm) _parmList.get(index);
    }

    /**
     * Method getParm.Returns the contents of the collection in an
     * Array.
     * <p>
     * Note: Just in case the collection contents are changing in another
     * thread, we pass a 0-length Array of the correct type into the API call.
     * This way we <i>know</i> that the Array returned is of exactly the correct
     * length.
     *
     * @return this collection as an Array
     */
    public org.opennms.netmgt.xml.event.Parm[] getParm() {
        org.opennms.netmgt.xml.event.Parm[] array = new org.opennms.netmgt.xml.event.Parm[0];
        return (org.opennms.netmgt.xml.event.Parm[]) this._parmList.toArray(array);
    }

    /**
     * Method getParmCollection.Returns a reference to '_parmList'.
     * No type checking is performed on any modifications to the
     * Vector.
     *
     * @return a reference to the Vector backing this class
     * @deprecated This entire class has been deprecated. Use
     *             Event.getParmCollection instead
     */
    public java.util.List<org.opennms.netmgt.xml.event.Parm> getParmCollection() {
        return this._parmList;
    }

    /**
     * Method getParmCount.
     *
     * @return the size of this collection
     */
    public int getParmCount() {
        return this._parmList.size();
    }

    /**
     * Method iterateParm.
     *
     * @return an Iterator over all possible elements in this
     *         collection
     */
    public java.util.Iterator<org.opennms.netmgt.xml.event.Parm> iterateParm() {
        return this._parmList.iterator();
    }

    /**
     * Removes the all parm.
     */
    public void removeAllParm() {
        this._parmList.clear();
    }

    /**
     * Method removeParm.
     *
     * @param vParm
     *            the v parm
     * @return true if the object was removed from the collection.
     */
    public boolean removeParm(final org.opennms.netmgt.xml.event.Parm vParm) {
        boolean removed = _parmList.remove(vParm);
        return removed;
    }

    /**
     * Method removeParmAt.
     *
     * @param index
     *            the index
     * @return the element removed from the collection
     */
    public org.opennms.netmgt.xml.event.Parm removeParmAt(final int index) {
        java.lang.Object obj = this._parmList.remove(index);
        return (org.opennms.netmgt.xml.event.Parm) obj;
    }

    /**
     * Sets the parm.
     *
     * @param index
     *            the index
     * @param vParm
     *            the v parm
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setParm(final int index, final org.opennms.netmgt.xml.event.Parm vParm)
            throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._parmList.size()) {
            throw new IndexOutOfBoundsException("setParm: Index value '" + index + "' not in range [0.."
                    + (this._parmList.size() - 1) + "]");
        }

        this._parmList.set(index, vParm);
    }

    /**
     * Sets the parm.
     *
     * @param vParmArray
     *            the new parm
     */
    public void setParm(final org.opennms.netmgt.xml.event.Parm[] vParmArray) {
        // -- copy array
        _parmList.clear();

        for (int i = 0; i < vParmArray.length; i++) {
            this._parmList.add(vParmArray[i]);
        }
    }

    /**
     * Sets the value of '_parmList' by copying the given Vector.
     * All elements will be checked for type safety.
     *
     * @param vParmList
     *            the Vector to copy.
     */
    public void setParm(final java.util.List<org.opennms.netmgt.xml.event.Parm> vParmList) {
        // copy vector
        this._parmList.clear();

        this._parmList.addAll(vParmList);
    }

    /**
     * Sets the value of '_parmList' by setting it to the given
     * Vector. No type checking is performed.
     *
     * @param parmList
     *            the Vector to set.
     * @deprecated
     */
    public void setParmCollection(final java.util.List<org.opennms.netmgt.xml.event.Parm> parmList) {
        this._parmList = parmList;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("parm", _parmList).toString();
    }
}
