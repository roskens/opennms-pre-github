/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */



package org.opennms.netmgt.config.attrmap;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.Validator;

import org.opennms.core.xml.ValidateUsing;
import org.xml.sax.ContentHandler;

/**
 * This is the top-level element for attrmap.xml.
 *
 *
 * @version $Revision$ $Date$
 */

@XmlRootElement(name="attrmap")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("attrmap.xsd")
public class Attrmap implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Virtual file names
     *
     */
    @XmlElement(name="datasource")
    private List<Datasource> _datasourceList;


    public Attrmap() {
        super();
        this._datasourceList = new ArrayList<Datasource>();
    }


    /**
     *
     *
     * @param vDatasource
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDatasource(final Datasource vDatasource) throws IndexOutOfBoundsException {
        this._datasourceList.add(vDatasource);
    }

    /**
     *
     *
     * @param index
     * @param vDatasource
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDatasource( final int index, final Datasource vDatasource) throws IndexOutOfBoundsException {
        this._datasourceList.add(index, vDatasource);
    }

    /**
     * Method enumerateDatasource.
     *
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public Enumeration<Datasource> enumerateDatasource() {
        return Collections.enumeration(this._datasourceList);
    }

    /**
     * Overrides the java.lang.Object.equals method.
     *
     * @param obj
     * @return true if the objects are equal.
     */
    @Override()
    public boolean equals(final Object obj) {
        if ( this == obj )
            return true;

        if (obj instanceof Attrmap) {

            Attrmap temp = (Attrmap)obj;
            if (this._datasourceList != null) {
                if (temp._datasourceList == null) return false;
                else if (!(this._datasourceList.equals(temp._datasourceList)))
                    return false;
            }
            else if (temp._datasourceList != null)
                return false;
            return true;
        }
        return false;
    }

    /**
     * Method getDatasource.
     *
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.opennms.netmgt.config.attrmap.Datasource at the given
     * index
     */
    public Datasource getDatasource(final int index) throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._datasourceList.size()) {
            throw new IndexOutOfBoundsException("getDatasource: Index value '" + index + "' not in range [0.." + (this._datasourceList.size() - 1) + "]");
        }

        return _datasourceList.get(index);
    }

    /**
     * Method getDatasource.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public Datasource[] getDatasource() {
        Datasource[] array = new Datasource[0];
        return this._datasourceList.toArray(array);
    }

    /**
     * Method getDatasourceCollection.Returns a reference to
     * '_datasourceList'. No type checking is performed on any
     * modifications to the Vector.
     *
     * @return a reference to the Vector backing this class
     */
    public List<Datasource> getDatasourceCollection() {
        return this._datasourceList;
    }

    /**
     * Method getDatasourceCount.
     *
     * @return the size of this collection
     */
    public int getDatasourceCount() {
        return this._datasourceList.size();
    }

    /**
     * Overrides the java.lang.Object.hashCode method.
     * <p>
     * The following steps came from <b>Effective Java Programming
     * Language Guide</b> by Joshua Bloch, Chapter 3
     *
     * @return a hash code value for the object.
     */
    public int hashCode() {
        int result = 17;

        long tmp;
        if (_datasourceList != null) {
           result = 37 * result + _datasourceList.hashCode();
        }

        return result;
    }

    /**
     * Method isValid.
     *
     * @return true if this object is valid according to the schema
     */
    @Deprecated
    public boolean isValid() {
        try {
            validate();
        } catch (ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * Method iterateDatasource.
     *
     * @return an Iterator over all possible elements in this
     * collection
     */
    public Iterator<Datasource> iterateDatasource() {
        return this._datasourceList.iterator();
    }

    /**
     *
     *
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    @Deprecated
    public void marshal(final Writer out) throws MarshalException, ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     *
     *
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    @Deprecated
    public void marshal(final ContentHandler handler) throws IOException, MarshalException, ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllDatasource() {
        this._datasourceList.clear();
    }

    /**
     * Method removeDatasource.
     *
     * @param vDatasource
     * @return true if the object was removed from the collection.
     */
    public boolean removeDatasource(final Datasource vDatasource) {
        boolean removed = _datasourceList.remove(vDatasource);
        return removed;
    }

    /**
     * Method removeDatasourceAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public Datasource removeDatasourceAt(final int index) {
        return this._datasourceList.remove(index);
    }

    /**
     *
     *
     * @param index
     * @param vDatasource
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDatasource(final int index, final Datasource vDatasource) throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._datasourceList.size()) {
            throw new IndexOutOfBoundsException("setDatasource: Index value '" + index + "' not in range [0.." + (this._datasourceList.size() - 1) + "]");
        }

        this._datasourceList.set(index, vDatasource);
    }

    /**
     *
     *
     * @param vDatasourceArray
     */
    public void setDatasource(final Datasource[] vDatasourceArray) {
        //-- copy array
        _datasourceList.clear();

        for (int i = 0; i < vDatasourceArray.length; i++) {
                this._datasourceList.add(vDatasourceArray[i]);
        }
    }

    /**
     * Sets the value of '_datasourceList' by copying the given
     * Vector. All elements will be checked for type safety.
     *
     * @param vDatasourceList the Vector to copy.
     */
    public void setDatasource(final List<Datasource> vDatasourceList) {
        // copy vector
        this._datasourceList.clear();

        this._datasourceList.addAll(vDatasourceList);
    }

    /**
     * Sets the value of '_datasourceList' by setting it to the
     * given Vector. No type checking is performed.
     * @deprecated
     *
     * @param datasourceList the Vector to set.
     */
    public void setDatasourceCollection(final List<Datasource> datasourceList) {
        this._datasourceList = datasourceList;
    }

    /**
     * Method unmarshal.
     *
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.opennms.netmgt.config.attrmap.Attrmap
     */
    @Deprecated
    public static Attrmap unmarshal(final java.io.Reader reader) throws MarshalException, ValidationException {
        return (Attrmap) Unmarshaller.unmarshal(Attrmap.class, reader);
    }

    /**
     *
     *
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    @Deprecated
    public void validate() throws ValidationException {
        new Validator().validate(this);
    }

}
