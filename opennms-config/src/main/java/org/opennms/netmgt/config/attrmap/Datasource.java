/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.opennms.netmgt.config.attrmap;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.Validator;

import org.opennms.core.xml.ValidateUsing;
import org.xml.sax.ContentHandler;

/**
 * Virtual datasource
 *
 * @version $Revision$ $Date$
 */
@XmlRootElement(name="datasource", namespace="http://xmlns.opennms.org/xsd/config/attrmap")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("attrmap.xsd")
public class Datasource implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * virtual datasource
     */
    @XmlAttribute(name="ds-name")
    private String _dsName;

    /**
     * datasource type (snmp | response)
     */
    @XmlAttribute(name="ds-type")
    private String _dsType;

    /**
     * real nodeid or ip address
     */
    @XmlAttribute(name="real-node")
    private String _realNode;

    /**
     * real node's datasource or real ip address's datasource
     */
    @XmlAttribute(name="real-datasource")
    private String _realDatasource;


    public Datasource() {
        super();
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

        if (obj instanceof Datasource) {

            Datasource temp = (Datasource)obj;
            if (this._dsName != null) {
                if (temp._dsName == null) return false;
                else if (!(this._dsName.equals(temp._dsName)))
                    return false;
            }
            else if (temp._dsName != null)
                return false;
            if (this._dsType != null) {
                if (temp._dsType == null) return false;
                else if (!(this._dsType.equals(temp._dsType)))
                    return false;
            }
            else if (temp._dsType != null)
                return false;
            if (this._realNode != null) {
                if (temp._realNode == null) return false;
                else if (!(this._realNode.equals(temp._realNode)))
                    return false;
            }
            else if (temp._realNode != null)
                return false;
            if (this._realDatasource != null) {
                if (temp._realDatasource == null) return false;
                else if (!(this._realDatasource.equals(temp._realDatasource)))
                    return false;
            }
            else if (temp._realDatasource != null)
                return false;
            return true;
        }
        return false;
    }

    /**
     * Returns the value of field 'dsName'. The field 'dsName' has
     * the following description: virtual datasource
     *
     * @return the value of field 'DsName'.
     */
    public String getDsName() {
        return _dsName == null ? "" : _dsName;
    }

    /**
     * Returns the value of field 'dsType'. The field 'dsType' has
     * the following description: datasource type (snmp | response)
     *
     * @return the value of field 'DsType'.
     */
    public String getDsType() {
        return _dsType == null ? "" : _dsType;
    }

    /**
     * Returns the value of field 'realDatasource'. The field
     * 'realDatasource' has the following description: real node's
     * datasource or real ip address's datasource
     *
     * @return the value of field 'RealDatasource'.
     */
    public String getRealDatasource() {
        return _realDatasource == null ? "" : _realDatasource;
    }

    /**
     * Returns the value of field 'realNode'. The field 'realNode'
     * has the following description: real nodeid or ip address
     *
     * @return the value of field 'RealNode'.
     */
    public String getRealNode() {
        return _realNode == null ? "" : _realNode;
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
        if (_dsName != null) {
           result = 37 * result + _dsName.hashCode();
        }
        if (_dsType != null) {
           result = 37 * result + _dsType.hashCode();
        }
        if (_realNode != null) {
           result = 37 * result + _realNode.hashCode();
        }
        if (_realDatasource != null) {
           result = 37 * result + _realDatasource.hashCode();
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
     * Sets the value of field 'dsName'. The field 'dsName' has the
     * following description: virtual datasource
     *
     * @param dsName the value of field 'dsName'.
     */
    public void setDsName(final String dsName) {
        this._dsName = dsName;
    }

    /**
     * Sets the value of field 'dsType'. The field 'dsType' has the
     * following description: datasource type (snmp | response)
     *
     * @param dsType the value of field 'dsType'.
     */
    public void setDsType(final String dsType) {
        this._dsType = dsType;
    }

    /**
     * Sets the value of field 'realDatasource'. The field
     * 'realDatasource' has the following description: real node's
     * datasource or real ip address's datasource
     *
     * @param realDatasource the value of field 'realDatasource'.
     */
    public void setRealDatasource(final String realDatasource) {
        this._realDatasource = realDatasource;
    }

    /**
     * Sets the value of field 'realNode'. The field 'realNode' has
     * the following description: real nodeid or ip address
     *
     * @param realNode the value of field 'realNode'.
     */
    public void setRealNode(final String realNode) {
        this._realNode = realNode;
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
     * org.opennms.netmgt.config.attrmap.Datasource
     */
    @Deprecated
    public static Datasource unmarshal(final Reader reader) throws MarshalException, ValidationException {
        return (Datasource) Unmarshaller.unmarshal(Datasource.class, reader);
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
