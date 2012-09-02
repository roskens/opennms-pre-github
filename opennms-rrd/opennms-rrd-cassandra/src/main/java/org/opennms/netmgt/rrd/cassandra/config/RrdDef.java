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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * rrd_def
 * 
 * @version $Revision$ $Date$
 */

@XmlRootElement(name = "rrd_def")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("all")
public class RrdDef implements Serializable {
    private static final long serialVersionUID = 0L;

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * Field _step.
     */
    @XmlAttribute(name = "step")
    private Long _step;

    /**
     * datasource
     */
    @XmlElement(name = "datasource")
    private java.util.List<org.opennms.netmgt.rrd.cassandra.config.Datasource> _datasourceList;

    /**
     * archive
     */
    @XmlElement(name = "archive")
    private java.util.List<org.opennms.netmgt.rrd.cassandra.config.Archive> _archiveList;

    // ----------------/
    // - Constructors -/
    // ----------------/

    public RrdDef() {
        super();
        this._datasourceList = new java.util.ArrayList<org.opennms.netmgt.rrd.cassandra.config.Datasource>();
        this._archiveList = new java.util.ArrayList<org.opennms.netmgt.rrd.cassandra.config.Archive>();
    }

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * @param vArchive
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index
     *             given is outside the bounds of the collection
     */
    public void addArchive(final org.opennms.netmgt.rrd.cassandra.config.Archive vArchive)
            throws java.lang.IndexOutOfBoundsException {
        this._archiveList.add(vArchive);
    }

    /**
     * @param index
     * @param vArchive
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index
     *             given is outside the bounds of the collection
     */
    public void addArchive(final int index, final org.opennms.netmgt.rrd.cassandra.config.Archive vArchive)
            throws java.lang.IndexOutOfBoundsException {
        this._archiveList.add(index, vArchive);
    }

    /**
     * @param vDatasource
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index
     *             given is outside the bounds of the collection
     */
    public void addDatasource(final org.opennms.netmgt.rrd.cassandra.config.Datasource vDatasource)
            throws java.lang.IndexOutOfBoundsException {
        this._datasourceList.add(vDatasource);
    }

    /**
     * @param index
     * @param vDatasource
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index
     *             given is outside the bounds of the collection
     */
    public void addDatasource(final int index, final org.opennms.netmgt.rrd.cassandra.config.Datasource vDatasource)
            throws java.lang.IndexOutOfBoundsException {
        this._datasourceList.add(index, vDatasource);
    }

    /**
     */
    public void deleteStep() {
        _step = null;
    }

    /**
     * Method enumerateArchive.
     * 
     * @return an Enumeration over all possible elements of this
     *         collection
     */
    public java.util.Enumeration<org.opennms.netmgt.rrd.cassandra.config.Archive> enumerateArchive() {
        return java.util.Collections.enumeration(this._archiveList);
    }

    /**
     * Method enumerateDatasource.
     * 
     * @return an Enumeration over all possible elements of this
     *         collection
     */
    public java.util.Enumeration<org.opennms.netmgt.rrd.cassandra.config.Datasource> enumerateDatasource() {
        return java.util.Collections.enumeration(this._datasourceList);
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

        if (obj instanceof RrdDef) {

            RrdDef temp = (RrdDef) obj;
            if (this._step != null) {
                if (temp._step == null)
                    return false;
                else if (!(this._step.equals(temp._step)))
                    return false;
            } else if (temp._step != null)
                return false;
            if (this._datasourceList != null) {
                if (temp._datasourceList == null)
                    return false;
                else if (!(this._datasourceList.equals(temp._datasourceList)))
                    return false;
            } else if (temp._datasourceList != null)
                return false;
            if (this._archiveList != null) {
                if (temp._archiveList == null)
                    return false;
                else if (!(this._archiveList.equals(temp._archiveList)))
                    return false;
            } else if (temp._archiveList != null)
                return false;
            return true;
        }
        return false;
    }

    /**
     * Method getArchive.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index
     *             given is outside the bounds of the collection
     * @return the value of the
     *         org.opennms.netmgt.rrd.cassandra.config.Archive at the given
     *         index
     */
    public org.opennms.netmgt.rrd.cassandra.config.Archive getArchive(final int index)
            throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._archiveList.size()) {
            throw new IndexOutOfBoundsException("getArchive: Index value '" + index + "' not in range [0.."
                    + (this._archiveList.size() - 1) + "]");
        }

        return (org.opennms.netmgt.rrd.cassandra.config.Archive) _archiveList.get(index);
    }

    /**
     * Method getArchive.Returns the contents of the collection in
     * an Array.
     * <p>
     * Note: Just in case the collection contents are changing in another thread, we pass a 0-length Array of the correct type
     * into the API call. This way we <i>know</i> that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.opennms.netmgt.rrd.cassandra.config.Archive[] getArchive() {
        org.opennms.netmgt.rrd.cassandra.config.Archive[] array = new org.opennms.netmgt.rrd.cassandra.config.Archive[0];
        return (org.opennms.netmgt.rrd.cassandra.config.Archive[]) this._archiveList.toArray(array);
    }

    /**
     * Method getArchiveCollection.Returns a reference to
     * '_archiveList'. No type checking is performed on any
     * modifications to the Vector.
     * 
     * @return a reference to the Vector backing this class
     */
    public java.util.List<org.opennms.netmgt.rrd.cassandra.config.Archive> getArchiveCollection() {
        return this._archiveList;
    }

    /**
     * Method getArchiveCount.
     * 
     * @return the size of this collection
     */
    public int getArchiveCount() {
        return this._archiveList.size();
    }

    /**
     * Method getDatasource.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index
     *             given is outside the bounds of the collection
     * @return the value of the
     *         org.opennms.netmgt.rrd.cassandra.config.Datasource at the
     *         given index
     */
    public org.opennms.netmgt.rrd.cassandra.config.Datasource getDatasource(final int index)
            throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._datasourceList.size()) {
            throw new IndexOutOfBoundsException("getDatasource: Index value '" + index + "' not in range [0.."
                    + (this._datasourceList.size() - 1) + "]");
        }

        return (org.opennms.netmgt.rrd.cassandra.config.Datasource) _datasourceList.get(index);
    }

    /**
     * Method getDatasource.Returns the contents of the collection
     * in an Array.
     * <p>
     * Note: Just in case the collection contents are changing in another thread, we pass a 0-length Array of the correct type
     * into the API call. This way we <i>know</i> that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.opennms.netmgt.rrd.cassandra.config.Datasource[] getDatasource() {
        org.opennms.netmgt.rrd.cassandra.config.Datasource[] array = new org.opennms.netmgt.rrd.cassandra.config.Datasource[0];
        return (org.opennms.netmgt.rrd.cassandra.config.Datasource[]) this._datasourceList.toArray(array);
    }

    /**
     * Method getDatasourceCollection.Returns a reference to
     * '_datasourceList'. No type checking is performed on any
     * modifications to the Vector.
     * 
     * @return a reference to the Vector backing this class
     */
    public java.util.List<org.opennms.netmgt.rrd.cassandra.config.Datasource> getDatasourceCollection() {
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
     * Returns the value of field 'step'.
     * 
     * @return the value of field 'Step'.
     */
    public Long getStep() {
        return _step == null ? 0 : _step;
    }

    /**
     * Method hasStep.
     * 
     * @return true if at least one Step has been added
     */
    public boolean hasStep() {
        return _step != null;
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

        if (_step != null) {
            result = 37 * result + _step.hashCode();
        }
        if (_datasourceList != null) {
            result = 37 * result + _datasourceList.hashCode();
        }
        if (_archiveList != null) {
            result = 37 * result + _archiveList.hashCode();
        }

        return result;
    }

    /**
     * Method iterateArchive.
     * 
     * @return an Iterator over all possible elements in this
     *         collection
     */
    public java.util.Iterator<org.opennms.netmgt.rrd.cassandra.config.Archive> iterateArchive() {
        return this._archiveList.iterator();
    }

    /**
     * Method iterateDatasource.
     * 
     * @return an Iterator over all possible elements in this
     *         collection
     */
    public java.util.Iterator<org.opennms.netmgt.rrd.cassandra.config.Datasource> iterateDatasource() {
        return this._datasourceList.iterator();
    }

    /**
     */
    public void removeAllArchive() {
        this._archiveList.clear();
    }

    /**
     */
    public void removeAllDatasource() {
        this._datasourceList.clear();
    }

    /**
     * Method removeArchive.
     * 
     * @param vArchive
     * @return true if the object was removed from the collection.
     */
    public boolean removeArchive(final org.opennms.netmgt.rrd.cassandra.config.Archive vArchive) {
        boolean removed = _archiveList.remove(vArchive);
        return removed;
    }

    /**
     * Method removeArchiveAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.opennms.netmgt.rrd.cassandra.config.Archive removeArchiveAt(final int index) {
        java.lang.Object obj = this._archiveList.remove(index);
        return (org.opennms.netmgt.rrd.cassandra.config.Archive) obj;
    }

    /**
     * Method removeDatasource.
     * 
     * @param vDatasource
     * @return true if the object was removed from the collection.
     */
    public boolean removeDatasource(final org.opennms.netmgt.rrd.cassandra.config.Datasource vDatasource) {
        boolean removed = _datasourceList.remove(vDatasource);
        return removed;
    }

    /**
     * Method removeDatasourceAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.opennms.netmgt.rrd.cassandra.config.Datasource removeDatasourceAt(final int index) {
        java.lang.Object obj = this._datasourceList.remove(index);
        return (org.opennms.netmgt.rrd.cassandra.config.Datasource) obj;
    }

    /**
     * @param index
     * @param vArchive
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index
     *             given is outside the bounds of the collection
     */
    public void setArchive(final int index, final org.opennms.netmgt.rrd.cassandra.config.Archive vArchive)
            throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._archiveList.size()) {
            throw new IndexOutOfBoundsException("setArchive: Index value '" + index + "' not in range [0.."
                    + (this._archiveList.size() - 1) + "]");
        }

        this._archiveList.set(index, vArchive);
    }

    /**
     * @param vArchiveArray
     */
    public void setArchive(final org.opennms.netmgt.rrd.cassandra.config.Archive[] vArchiveArray) {
        // -- copy array
        _archiveList.clear();

        for (int i = 0; i < vArchiveArray.length; i++) {
            this._archiveList.add(vArchiveArray[i]);
        }
    }

    /**
     * Sets the value of '_archiveList' by copying the given
     * Vector. All elements will be checked for type safety.
     * 
     * @param vArchiveList
     *            the Vector to copy.
     */
    public void setArchive(final java.util.List<org.opennms.netmgt.rrd.cassandra.config.Archive> vArchiveList) {
        // copy vector
        this._archiveList.clear();

        this._archiveList.addAll(vArchiveList);
    }

    /**
     * Sets the value of '_archiveList' by setting it to the given
     * Vector. No type checking is performed.
     * 
     * @deprecated
     * @param archiveList
     *            the Vector to set.
     */
    public void setArchiveCollection(final java.util.List<org.opennms.netmgt.rrd.cassandra.config.Archive> archiveList) {
        this._archiveList = archiveList;
    }

    /**
     * @param index
     * @param vDatasource
     * @throws java.lang.IndexOutOfBoundsException
     *             if the index
     *             given is outside the bounds of the collection
     */
    public void setDatasource(final int index, final org.opennms.netmgt.rrd.cassandra.config.Datasource vDatasource)
            throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._datasourceList.size()) {
            throw new IndexOutOfBoundsException("setDatasource: Index value '" + index + "' not in range [0.."
                    + (this._datasourceList.size() - 1) + "]");
        }

        this._datasourceList.set(index, vDatasource);
    }

    /**
     * @param vDatasourceArray
     */
    public void setDatasource(final org.opennms.netmgt.rrd.cassandra.config.Datasource[] vDatasourceArray) {
        // -- copy array
        _datasourceList.clear();

        for (int i = 0; i < vDatasourceArray.length; i++) {
            this._datasourceList.add(vDatasourceArray[i]);
        }
    }

    /**
     * Sets the value of '_datasourceList' by copying the given
     * Vector. All elements will be checked for type safety.
     * 
     * @param vDatasourceList
     *            the Vector to copy.
     */
    public void setDatasource(final java.util.List<org.opennms.netmgt.rrd.cassandra.config.Datasource> vDatasourceList) {
        // copy vector
        this._datasourceList.clear();

        this._datasourceList.addAll(vDatasourceList);
    }

    /**
     * Sets the value of '_datasourceList' by setting it to the
     * given Vector. No type checking is performed.
     * 
     * @deprecated
     * @param datasourceList
     *            the Vector to set.
     */
    public void setDatasourceCollection(final java.util.List<org.opennms.netmgt.rrd.cassandra.config.Datasource> datasourceList) {
        this._datasourceList = datasourceList;
    }

    /**
     * Sets the value of field 'step'.
     * 
     * @param step
     *            the value of field 'step'.
     */
    public void setStep(final Long step) {
        _step = step;
    }
}
