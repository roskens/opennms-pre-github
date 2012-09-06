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

package org.opennms.netmgt.config.vmware.cim;

//---------------------------------/
//- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * VMware Cim object groups
 *
 * @version $Revision$ $Date$
 */
@XmlRootElement(name = "snmp-vmware-cim-groups")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("all")
public class VmwareCimGroups implements java.io.Serializable {


    //--------------------------/
    //- Class/Member Variables -/
    //--------------------------/

    /**
     * A VMware Cim Object Group
     */
    @XmlElement(name = "vmware-cim-group")
    private java.util.List<org.opennms.netmgt.config.vmware.cim.VmwareCimGroup> _vmwareCimGroupList;


    //----------------/
    //- Constructors -/
    //----------------/

    public VmwareCimGroups() {
        super();
        this._vmwareCimGroupList = new java.util.ArrayList<org.opennms.netmgt.config.vmware.cim.VmwareCimGroup>();
    }


    //-----------/
    //- Methods -/
    //-----------/

    /**
     * @param vVmwareCimGroup
     * @throws java.lang.IndexOutOfBoundsException
     *          if the index
     *          given is outside the bounds of the collection
     */
    public void addVmwareCimGroup(
            final org.opennms.netmgt.config.vmware.cim.VmwareCimGroup vVmwareCimGroup)
            throws java.lang.IndexOutOfBoundsException {
        this._vmwareCimGroupList.add(vVmwareCimGroup);
    }

    /**
     * @param index
     * @param vVmwareCimGroup
     * @throws java.lang.IndexOutOfBoundsException
     *          if the index
     *          given is outside the bounds of the collection
     */
    public void addVmwareCimGroup(
            final int index,
            final org.opennms.netmgt.config.vmware.cim.VmwareCimGroup vVmwareCimGroup)
            throws java.lang.IndexOutOfBoundsException {
        this._vmwareCimGroupList.add(index, vVmwareCimGroup);
    }

    /**
     * Method enumerateVmwareCimGroup.
     *
     * @return an Enumeration over all possible elements of this
     *         collection
     */
    public java.util.Enumeration<org.opennms.netmgt.config.vmware.cim.VmwareCimGroup> enumerateVmwareCimGroup(
    ) {
        return java.util.Collections.enumeration(this._vmwareCimGroupList);
    }

    /**
     * Overrides the java.lang.Object.equals method.
     *
     * @param obj
     * @return true if the objects are equal.
     */
    @Override()
    public boolean equals(
            final java.lang.Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof VmwareCimGroups) {

            VmwareCimGroups temp = (VmwareCimGroups) obj;
            if (this._vmwareCimGroupList != null) {
                if (temp._vmwareCimGroupList == null) return false;
                else if (!(this._vmwareCimGroupList.equals(temp._vmwareCimGroupList)))
                    return false;
            } else if (temp._vmwareCimGroupList != null)
                return false;
            return true;
        }
        return false;
    }

    /**
     * Method getVmwareCimGroup.
     *
     * @param index
     * @return the value of the
     *         org.opennms.netmgt.config.vmware.cim.VmwareCimGroup at the
     *         given index
     * @throws java.lang.IndexOutOfBoundsException
     *          if the index
     *          given is outside the bounds of the collection
     */
    public org.opennms.netmgt.config.vmware.cim.VmwareCimGroup getVmwareCimGroup(
            final int index)
            throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._vmwareCimGroupList.size()) {
            throw new IndexOutOfBoundsException("getVmwareCimGroup: Index value '" + index + "' not in range [0.." + (this._vmwareCimGroupList.size() - 1) + "]");
        }

        return (org.opennms.netmgt.config.vmware.cim.VmwareCimGroup) _vmwareCimGroupList.get(index);
    }

    /**
     * Method getVmwareCimGroup.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call.
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     *
     * @return this collection as an Array
     */
    public org.opennms.netmgt.config.vmware.cim.VmwareCimGroup[] getVmwareCimGroup(
    ) {
        org.opennms.netmgt.config.vmware.cim.VmwareCimGroup[] array = new org.opennms.netmgt.config.vmware.cim.VmwareCimGroup[0];
        return (org.opennms.netmgt.config.vmware.cim.VmwareCimGroup[]) this._vmwareCimGroupList.toArray(array);
    }

    /**
     * Method getVmwareCimGroupCollection.Returns a reference to
     * '_vmwareCimGroupList'. No type checking is performed on any
     * modifications to the Vector.
     *
     * @return a reference to the Vector backing this class
     */
    public java.util.List<org.opennms.netmgt.config.vmware.cim.VmwareCimGroup> getVmwareCimGroupCollection(
    ) {
        return this._vmwareCimGroupList;
    }

    /**
     * Method getVmwareCimGroupCount.
     *
     * @return the size of this collection
     */
    public int getVmwareCimGroupCount(
    ) {
        return this._vmwareCimGroupList.size();
    }

    /**
     * Overrides the java.lang.Object.hashCode method.
     * <p/>
     * The following steps came from <b>Effective Java Programming
     * Language Guide</b> by Joshua Bloch, Chapter 3
     *
     * @return a hash code value for the object.
     */
    public int hashCode(
    ) {
        int result = 17;

        long tmp;
        if (_vmwareCimGroupList != null) {
            result = 37 * result + _vmwareCimGroupList.hashCode();
        }

        return result;
    }

    /**
     * Method isValid.
     *
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * Method iterateVmwareCimGroup.
     *
     * @return an Iterator over all possible elements in this
     *         collection
     */
    public java.util.Iterator<org.opennms.netmgt.config.vmware.cim.VmwareCimGroup> iterateVmwareCimGroup(
    ) {
        return this._vmwareCimGroupList.iterator();
    }

    /**
     * @param out
     * @throws org.exolab.castor.xml.MarshalException
     *          if object is
     *          null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException
     *          if this
     *          object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     *                             marshaling
     * @throws org.exolab.castor.xml.ValidationException
     *                             if this
     *                             object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException
     *                             if object is
     *                             null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
            throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllVmwareCimGroup(
    ) {
        this._vmwareCimGroupList.clear();
    }

    /**
     * Method removeVmwareCimGroup.
     *
     * @param vVmwareCimGroup
     * @return true if the object was removed from the collection.
     */
    public boolean removeVmwareCimGroup(
            final org.opennms.netmgt.config.vmware.cim.VmwareCimGroup vVmwareCimGroup) {
        boolean removed = _vmwareCimGroupList.remove(vVmwareCimGroup);
        return removed;
    }

    /**
     * Method removeVmwareCimGroupAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public org.opennms.netmgt.config.vmware.cim.VmwareCimGroup removeVmwareCimGroupAt(
            final int index) {
        java.lang.Object obj = this._vmwareCimGroupList.remove(index);
        return (org.opennms.netmgt.config.vmware.cim.VmwareCimGroup) obj;
    }

    /**
     * @param index
     * @param vVmwareCimGroup
     * @throws java.lang.IndexOutOfBoundsException
     *          if the index
     *          given is outside the bounds of the collection
     */
    public void setVmwareCimGroup(
            final int index,
            final org.opennms.netmgt.config.vmware.cim.VmwareCimGroup vVmwareCimGroup)
            throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._vmwareCimGroupList.size()) {
            throw new IndexOutOfBoundsException("setVmwareCimGroup: Index value '" + index + "' not in range [0.." + (this._vmwareCimGroupList.size() - 1) + "]");
        }

        this._vmwareCimGroupList.set(index, vVmwareCimGroup);
    }

    /**
     * @param vVmwareCimGroupArray
     */
    public void setVmwareCimGroup(
            final org.opennms.netmgt.config.vmware.cim.VmwareCimGroup[] vVmwareCimGroupArray) {
        //-- copy array
        _vmwareCimGroupList.clear();

        for (int i = 0; i < vVmwareCimGroupArray.length; i++) {
            this._vmwareCimGroupList.add(vVmwareCimGroupArray[i]);
        }
    }

    /**
     * Sets the value of '_vmwareCimGroupList' by copying the given
     * Vector. All elements will be checked for type safety.
     *
     * @param vVmwareCimGroupList the Vector to copy.
     */
    public void setVmwareCimGroup(
            final java.util.List<org.opennms.netmgt.config.vmware.cim.VmwareCimGroup> vVmwareCimGroupList) {
        // copy vector
        this._vmwareCimGroupList.clear();

        this._vmwareCimGroupList.addAll(vVmwareCimGroupList);
    }

    /**
     * Sets the value of '_vmwareCimGroupList' by setting it to the
     * given Vector. No type checking is performed.
     *
     * @param vmwareCimGroupList the Vector to set.
     * @deprecated
     */
    public void setVmwareCimGroupCollection(
            final java.util.List<org.opennms.netmgt.config.vmware.cim.VmwareCimGroup> vmwareCimGroupList) {
        this._vmwareCimGroupList = vmwareCimGroupList;
    }

    /**
     * Method unmarshal.
     *
     * @param reader
     * @return the unmarshaled
     *         org.opennms.netmgt.config.vmware.cim.VmwareCimGroups
     * @throws org.exolab.castor.xml.MarshalException
     *          if object is
     *          null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException
     *          if this
     *          object is an invalid instance according to the schema
     */
    public static org.opennms.netmgt.config.vmware.cim.VmwareCimGroups unmarshal(
            final java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.opennms.netmgt.config.vmware.cim.VmwareCimGroups) Unmarshaller.unmarshal(org.opennms.netmgt.config.vmware.cim.VmwareCimGroups.class, reader);
    }

    /**
     * @throws org.exolab.castor.xml.ValidationException
     *          if this
     *          object is an invalid instance according to the schema
     */
    public void validate(
    )
            throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
