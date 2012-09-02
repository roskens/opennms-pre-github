/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.opennms.netmgt.rrd.cassandra.config;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * datasource
 * 
 * @version $Revision$ $Date$
 */

@XmlRootElement(name = "datasource")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("all")
public class Datasource implements Serializable {
    private static final long serialVersionUID = 0L;

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * Field _name.
     */
    @XmlAttribute(name = "name")
    private String _name;

    /**
     * Field _type.
     */
    @XmlAttribute(name = "type")
    private String _type;

    /**
     * Field _heartbeat.
     */
    @XmlAttribute(name = "heartbeat")
    private Long _heartbeat;

    /**
     * Field _min.
     */
    @XmlAttribute(name = "min")
    private String _min;

    /**
     * Field _max.
     */
    @XmlAttribute(name = "max")
    private String _max;

    // ----------------/
    // - Constructors -/
    // ----------------/

    public Datasource() {
        super();
    }

    // -----------/
    // - Methods -/
    // -----------/

    /**
     */
    public void deleteHeartbeat() {
        _heartbeat = null;
    }

    /**
     * Overrides the java.lang.Object.equals method.
     * 
     * @param obj
     * @return true if the objects are equal.
     */
    @Override()
    public boolean equals(final java.lang.Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof Datasource) {

            Datasource temp = (Datasource) obj;
            if (_name != null) {
                if (temp._name == null)
                    return false;
                else if (!(_name.equals(temp._name)))
                    return false;
            } else if (temp._name != null)
                return false;
            if (_type != null) {
                if (temp._type == null)
                    return false;
                else if (!(_type.equals(temp._type)))
                    return false;
            } else if (temp._type != null)
                return false;
            if (_heartbeat != null) {
                if (temp._heartbeat == null)
                    return false;
                else if (!(_heartbeat.equals(temp._heartbeat)))
                    return false;
            } else if (temp._heartbeat != null)
                return false;
            if (_min != null) {
                if (temp._min == null)
                    return false;
                else if (!(_min.equals(temp._min)))
                    return false;
            } else if (temp._min != null)
                return false;
            if (_max != null) {
                if (temp._max == null)
                    return false;
                else if (!(_max.equals(temp._max)))
                    return false;
            } else if (temp._max != null)
                return false;
            return true;
        }
        return false;
    }

    /**
     * Returns the value of field 'heartbeat'.
     * 
     * @return the value of field 'Heartbeat'.
     */
    public Long getHeartbeat() {
        return _heartbeat == null ? 0 : _heartbeat;
    }

    /**
     * Returns the value of field 'max'.
     * 
     * @return the value of field 'Max'.
     */
    public String getMax() {
        return _max == null ? "U" : _max;
    }

    /**
     * Returns the value of field 'min'.
     * 
     * @return the value of field 'Min'.
     */
    public String getMin() {
        return _min == null ? "U" : _min;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public String getType() {
        return _type;
    }

    /**
     * Method hasHeartbeat.
     * 
     * @return true if at least one Heartbeat has been added
     */
    public boolean hasHeartbeat() {
        return _heartbeat != null;
    }

    /**
     * Overrides the java.lang.Object.hashCode method.
     * <p>
     * The following steps came from <b>Effective Java Programming Language Guide</b> by Joshua Bloch, Chapter 3
     * 
     * @return a hash code value for the object.
     */
    public int hashCode() {
        int result = 17;

        long tmp;
        if (_name != null) {
            result = 37 * result + _name.hashCode();
        }
        if (_type != null) {
            result = 37 * result + _type.hashCode();
        }
        if (_heartbeat != null) {
            result = 37 * result + _heartbeat.hashCode();
        }
        if (_min != null) {
            result = 37 * result + _min.hashCode();
        }
        if (_max != null) {
            result = 37 * result + _max.hashCode();
        }

        return result;
    }

    /**
     * Sets the value of field 'heartbeat'.
     * 
     * @param heartbeat
     *            the value of field 'heartbeat'.
     */
    public void setHeartbeat(final Long heartbeat) {
        _heartbeat = heartbeat;
    }

    /**
     * Sets the value of field 'max'.
     * 
     * @param max
     *            the value of field 'max'.
     */
    public void setMax(final String max) {
        _max = max;
    }

    /**
     * Sets the value of field 'min'.
     * 
     * @param min
     *            the value of field 'min'.
     */
    public void setMin(final String min) {
        _min = min;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name
     *            the value of field 'name'.
     */
    public void setName(final String name) {
        _name = name;
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type
     *            the value of field 'type'.
     */
    public void setType(final String type) {
        _type = type;
    }
}
