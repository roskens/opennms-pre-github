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
 * archive
 * 
 * @version $Revision$ $Date$
 */

@XmlRootElement(name = "archive")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("all")
public class Archive implements Serializable {
    private static final long serialVersionUID = 0L;

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * Field _cf.
     */
    @XmlAttribute(name="cf")
    private String _cf;

    /**
     * Field _xff.
     */
    @XmlAttribute(name="xff")
    private Double _xff;

    /**
     * Field _steps.
     */
    @XmlAttribute(name="steps")
    private Integer _steps;

    /**
     * Field _rows.
     */
    @XmlAttribute(name="rows")
    private Integer _rows;

    // ----------------/
    // - Constructors -/
    // ----------------/

    public Archive() {
        super();
    }

    // -----------/
    // - Methods -/
    // -----------/

    /**
     */
    public void deleteRows() {
        this._rows = null;
    }

    /**
     */
    public void deleteSteps() {
        this._steps = null;
    }

    /**
     */
    public void deleteXff() {
        this._xff = null;
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

        if (obj instanceof Archive) {

            Archive temp = (Archive) obj;
            if (this._cf != null) {
                if (temp._cf == null)
                    return false;
                else if (!(this._cf.equals(temp._cf)))
                    return false;
            } else if (temp._cf != null)
                return false;
            if (_xff != null) {
                if (temp._xff == null)
                    return false;
                else if (!(_xff.equals(temp._xff)))
                    return false;
            } else if (temp._xff != null)
                return false;
            if (_steps != null) {
                if (temp._steps == null)
                    return false;
                else if (!(_steps.equals(temp._steps)))
                    return false;
            } else if (temp._steps != null)
                return false;
            if (_rows != null) {
                if (temp._rows == null)
                    return false;
                else if (!(_rows.equals(temp._rows)))
                    return false;
            } else if (temp._rows != null)
                return false;
            return true;
        }
        return false;
    }

    /**
     * Returns the value of field 'cf'.
     * 
     * @return the value of field 'Cf'.
     */
    public String getCf() {
        return this._cf;
    }

    /**
     * Returns the value of field 'rows'.
     * 
     * @return the value of field 'Rows'.
     */
    public int getRows() {
        return this._rows;
    }

    /**
     * Returns the value of field 'steps'.
     * 
     * @return the value of field 'Steps'.
     */
    public int getSteps() {
        return this._steps;
    }

    /**
     * Returns the value of field 'xff'.
     * 
     * @return the value of field 'Xff'.
     */
    public double getXff() {
        return this._xff;
    }

    /**
     * Method hasRows.
     * 
     * @return true if at least one Rows has been added
     */
    public boolean hasRows() {
        return this._rows != null;
    }

    /**
     * Method hasSteps.
     * 
     * @return true if at least one Steps has been added
     */
    public boolean hasSteps() {
        return this._steps != null;
    }

    /**
     * Method hasXff.
     * 
     * @return true if at least one Xff has been added
     */
    public boolean hasXff() {
        return this._xff != null;
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
        if (_cf != null) {
            result = 37 * result + _cf.hashCode();
        }
        tmp = java.lang.Double.doubleToLongBits(_xff);
        result = 37 * result + (int) (tmp ^ (tmp >>> 32));
        result = 37 * result + _steps;
        result = 37 * result + _rows;

        return result;
    }

    /**
     * Sets the value of field 'cf'.
     * 
     * @param cf
     *            the value of field 'cf'.
     */
    public void setCf(final String cf) {
        this._cf = cf;
    }

    /**
     * Sets the value of field 'rows'.
     * 
     * @param rows
     *            the value of field 'rows'.
     */
    public void setRows(final Integer rows) {
        this._rows = rows;
    }

    /**
     * Sets the value of field 'steps'.
     * 
     * @param steps
     *            the value of field 'steps'.
     */
    public void setSteps(final Integer steps) {
        this._steps = steps;
    }

    /**
     * Sets the value of field 'xff'.
     * 
     * @param xff
     *            the value of field 'xff'.
     */
    public void setXff(final Double xff) {
        this._xff = xff;
    }
}
