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

package org.opennms.netmgt.config.nsclient;

//---------------------------------/
//- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Nsclient object groups.
 *
 * @version $Revision$ $Date$
 */

@SuppressWarnings("all")
public class Wpms implements java.io.Serializable {

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /** An NSClient Object Group. */
    private java.util.List<org.opennms.netmgt.config.nsclient.Wpm> _wpmList;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Instantiates a new wpms.
     */
    public Wpms() {
        super();
        this._wpmList = new java.util.ArrayList<org.opennms.netmgt.config.nsclient.Wpm>();
    }

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Adds the wpm.
     *
     * @param vWpm
     *            the v wpm
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addWpm(final org.opennms.netmgt.config.nsclient.Wpm vWpm) throws java.lang.IndexOutOfBoundsException {
        this._wpmList.add(vWpm);
    }

    /**
     * Adds the wpm.
     *
     * @param index
     *            the index
     * @param vWpm
     *            the v wpm
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addWpm(final int index, final org.opennms.netmgt.config.nsclient.Wpm vWpm)
            throws java.lang.IndexOutOfBoundsException {
        this._wpmList.add(index, vWpm);
    }

    /**
     * Method enumerateWpm.
     *
     * @return an Enumeration over all possible elements of this
     *         collection
     */
    public java.util.Enumeration<org.opennms.netmgt.config.nsclient.Wpm> enumerateWpm() {
        return java.util.Collections.enumeration(this._wpmList);
    }

    /**
     * Overrides the java.lang.Object.equals method.
     *
     * @param obj
     *            the obj
     * @return true if the objects are equal.
     */
    @Override()
    public boolean equals(final java.lang.Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Wpms) {

            Wpms temp = (Wpms) obj;
            if (this._wpmList != null) {
                if (temp._wpmList == null)
                    return false;
                else if (!(this._wpmList.equals(temp._wpmList)))
                    return false;
            } else if (temp._wpmList != null)
                return false;
            return true;
        }
        return false;
    }

    /**
     * Method getWpm.
     *
     * @param index
     *            the index
     * @return the value of the
     *         org.opennms.netmgt.config.nsclient.Wpm at the given index
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public org.opennms.netmgt.config.nsclient.Wpm getWpm(final int index) throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._wpmList.size()) {
            throw new IndexOutOfBoundsException("getWpm: Index value '" + index + "' not in range [0.."
                    + (this._wpmList.size() - 1) + "]");
        }

        return (org.opennms.netmgt.config.nsclient.Wpm) _wpmList.get(index);
    }

    /**
     * Method getWpm.Returns the contents of the collection in an
     * Array.
     * <p>
     * Note: Just in case the collection contents are changing in another
     * thread, we pass a 0-length Array of the correct type into the API call.
     * This way we <i>know</i> that the Array returned is of exactly the correct
     * length.
     *
     * @return this collection as an Array
     */
    public org.opennms.netmgt.config.nsclient.Wpm[] getWpm() {
        org.opennms.netmgt.config.nsclient.Wpm[] array = new org.opennms.netmgt.config.nsclient.Wpm[0];
        return (org.opennms.netmgt.config.nsclient.Wpm[]) this._wpmList.toArray(array);
    }

    /**
     * Method getWpmCollection.Returns a reference to '_wpmList'.
     * No type checking is performed on any modifications to the
     * Vector.
     *
     * @return a reference to the Vector backing this class
     */
    public java.util.List<org.opennms.netmgt.config.nsclient.Wpm> getWpmCollection() {
        return this._wpmList;
    }

    /**
     * Method getWpmCount.
     *
     * @return the size of this collection
     */
    public int getWpmCount() {
        return this._wpmList.size();
    }

    /**
     * Overrides the java.lang.Object.hashCode method.
     * <p>
     * The following steps came from <b>Effective Java Programming Language
     * Guide</b> by Joshua Bloch, Chapter 3
     *
     * @return a hash code value for the object.
     */
    @Override
    public int hashCode() {
        int result = 17;

        long tmp;
        if (_wpmList != null) {
            result = 37 * result + _wpmList.hashCode();
        }

        return result;
    }

    /**
     * Method isValid.
     *
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * Method iterateWpm.
     *
     * @return an Iterator over all possible elements in this
     *         collection
     */
    public java.util.Iterator<org.opennms.netmgt.config.nsclient.Wpm> iterateWpm() {
        return this._wpmList.iterator();
    }

    /**
     * Marshal.
     *
     * @param out
     *            the out
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException,
            org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * Marshal.
     *
     * @param handler
     *            the handler
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException,
            org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Removes the all wpm.
     */
    public void removeAllWpm() {
        this._wpmList.clear();
    }

    /**
     * Method removeWpm.
     *
     * @param vWpm
     *            the v wpm
     * @return true if the object was removed from the collection.
     */
    public boolean removeWpm(final org.opennms.netmgt.config.nsclient.Wpm vWpm) {
        boolean removed = _wpmList.remove(vWpm);
        return removed;
    }

    /**
     * Method removeWpmAt.
     *
     * @param index
     *            the index
     * @return the element removed from the collection
     */
    public org.opennms.netmgt.config.nsclient.Wpm removeWpmAt(final int index) {
        java.lang.Object obj = this._wpmList.remove(index);
        return (org.opennms.netmgt.config.nsclient.Wpm) obj;
    }

    /**
     * Sets the wpm.
     *
     * @param index
     *            the index
     * @param vWpm
     *            the v wpm
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setWpm(final int index, final org.opennms.netmgt.config.nsclient.Wpm vWpm)
            throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._wpmList.size()) {
            throw new IndexOutOfBoundsException("setWpm: Index value '" + index + "' not in range [0.."
                    + (this._wpmList.size() - 1) + "]");
        }

        this._wpmList.set(index, vWpm);
    }

    /**
     * Sets the wpm.
     *
     * @param vWpmArray
     *            the new wpm
     */
    public void setWpm(final org.opennms.netmgt.config.nsclient.Wpm[] vWpmArray) {
        // -- copy array
        _wpmList.clear();

        for (int i = 0; i < vWpmArray.length; i++) {
            this._wpmList.add(vWpmArray[i]);
        }
    }

    /**
     * Sets the value of '_wpmList' by copying the given Vector.
     * All elements will be checked for type safety.
     *
     * @param vWpmList
     *            the Vector to copy.
     */
    public void setWpm(final java.util.List<org.opennms.netmgt.config.nsclient.Wpm> vWpmList) {
        // copy vector
        this._wpmList.clear();

        this._wpmList.addAll(vWpmList);
    }

    /**
     * Sets the value of '_wpmList' by setting it to the given
     * Vector. No type checking is performed.
     *
     * @param wpmList
     *            the Vector to set.
     * @deprecated
     */
    public void setWpmCollection(final java.util.List<org.opennms.netmgt.config.nsclient.Wpm> wpmList) {
        this._wpmList = wpmList;
    }

    /**
     * Method unmarshal.
     *
     * @param reader
     *            the reader
     * @return the unmarshaled
     *         org.opennms.netmgt.config.nsclient.Wpms
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public static org.opennms.netmgt.config.nsclient.Wpms unmarshal(final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.opennms.netmgt.config.nsclient.Wpms) Unmarshaller.unmarshal(org.opennms.netmgt.config.nsclient.Wpms.class,
                                                                                reader);
    }

    /**
     * Validate.
     *
     * @throws ValidationException
     *             the validation exception
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
