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

package org.opennms.netmgt.xml.eventconf;

//---------------------------------/
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.Validator;
import org.opennms.core.xml.ValidateUsing;
import org.xml.sax.ContentHandler;

/**
 * The Class Event.
 */
@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("eventconf.xsd")
@XmlType(propOrder = {})
public class Event implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 565808183599950549L;

    /** The Constant EMPTY_VARBINDSDECODE_ARRAY. */
    private static final Varbindsdecode[] EMPTY_VARBINDSDECODE_ARRAY = new Varbindsdecode[0];

    /** The Constant EMPTY_SCRIPT_ARRAY. */
    private static final Script[] EMPTY_SCRIPT_ARRAY = new Script[0];

    /** The Constant EMPTY_OPERACTION_ARRAY. */
    private static final Operaction[] EMPTY_OPERACTION_ARRAY = new Operaction[0];

    /** The Constant EMPTY_STRING_ARRAY. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** The Constant EMPTY_FORWARD_ARRAY. */
    private static final Forward[] EMPTY_FORWARD_ARRAY = new Forward[0];

    /** The Constant EMPTY_AUTOACTION_ARRAY. */
    private static final Autoaction[] EMPTY_AUTOACTION_ARRAY = new Autoaction[0];

    /** The event mask which helps to uniquely identify an event. */
    @XmlElement(name = "mask", required = false)
    private Mask m_mask;

    /** The Universal Event Identifier. */
    // @NotNull
    @XmlElement(name = "uei", required = true)
    private String m_uei;

    /** A human readable name used to identify an event in the web ui. */
    // @NotNull
    @XmlElement(name = "event-label", required = true)
    private String m_eventLabel;

    /** The SNMP information from the trap. */
    @XmlElement(name = "snmp", required = false)
    private Snmp m_snmp;

    /** The event description. */
    // @NotNull
    @XmlElement(name = "descr", required = true)
    private String m_descr;

    /** The event logmsg. */
    // @NotNull
    @XmlElement(name = "logmsg", required = true)
    private Logmsg m_logmsg;

    /** The event severity. */
    // @NotNull
    @XmlElement(name = "severity", required = true)
    private String m_severity;

    /** The event correlation information. */
    @XmlElement(name = "correlation", required = false)
    private Correlation m_correlation;

    /** The operator instruction for this event. */
    @XmlElement(name = "operinstruct", required = false)
    private String m_operinstruct;

    /** The automatic action to occur when this event occurs. */
    // @Size(min=0)
    @XmlElement(name = "autoaction", required = false)
    private List<Autoaction> m_autoactions = new ArrayList<Autoaction>();

    /** The varbind decoding tag used to decode value into a string. */
    // @Size(min=0)
    @XmlElement(name = "varbindsdecode", required = false)
    private List<Varbindsdecode> m_varbindsdecodes = new ArrayList<Varbindsdecode>();

    /** The operator action to be taken when this event occurs. */
    // @Size(min=0)
    @XmlElement(name = "operaction", required = false)
    private List<Operaction> m_operactions = new ArrayList<Operaction>();

    /** The autoacknowledge information for the user. */
    @XmlElement(name = "autoacknowledge", required = false)
    private Autoacknowledge m_autoacknowledge;

    /** A logical group with which to associate this event. */
    // @Size(min=0)
    @XmlElement(name = "loggroup", required = false)
    private List<String> m_loggroups = new ArrayList<String>();

    /** The trouble ticket info. */
    @XmlElement(name = "tticket", required = false)
    private Tticket m_tticket;

    /** The forwarding information for this event. */
    // @Size(min=0)
    @XmlElement(name = "forward", required = false)
    private List<Forward> m_forwards = new ArrayList<Forward>();

    /** The script information for this event. */
    // @Size(min=0)
    @XmlElement(name = "script", required = false)
    private List<Script> m_scripts = new ArrayList<Script>();

    /**
     * The text to be displayed on a 'mouseOver' event
     * when this event is displayed in the event browser.
     */
    @XmlElement(name = "mouseovertext", required = false)
    private String m_mouseovertext;

    /**
     * Data used to create an event.
     */
    @XmlElement(name = "alarm-data", required = false)
    private AlarmData m_alarmData;

    /** The m_filters. */
    @XmlElement(name = "filters", required = false)
    private Filters m_filters;

    /** The m_matcher. */
    @XmlTransient
    private EventMatcher m_matcher;

    /**
     * Adds the autoaction.
     *
     * @param autoaction
     *            the autoaction
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addAutoaction(final Autoaction autoaction) throws IndexOutOfBoundsException {
        m_autoactions.add(autoaction);
    }

    /**
     * Adds the autoaction.
     *
     * @param index
     *            the index
     * @param autoaction
     *            the autoaction
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addAutoaction(final int index, final Autoaction autoaction) throws IndexOutOfBoundsException {
        m_autoactions.add(index, autoaction);
    }

    /**
     * Adds the forward.
     *
     * @param forward
     *            the forward
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addForward(final Forward forward) throws IndexOutOfBoundsException {
        m_forwards.add(forward);
    }

    /**
     * Adds the forward.
     *
     * @param index
     *            the index
     * @param forward
     *            the forward
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addForward(final int index, final Forward forward) throws IndexOutOfBoundsException {
        m_forwards.add(index, forward);
    }

    /**
     * Adds the loggroup.
     *
     * @param loggroup
     *            the loggroup
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addLoggroup(final String loggroup) throws IndexOutOfBoundsException {
        m_loggroups.add(loggroup.intern());
    }

    /**
     * Adds the loggroup.
     *
     * @param index
     *            the index
     * @param loggroup
     *            the loggroup
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addLoggroup(final int index, final String loggroup) throws IndexOutOfBoundsException {
        m_loggroups.add(index, loggroup.intern());
    }

    /**
     * Adds the operaction.
     *
     * @param operaction
     *            the operaction
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addOperaction(final Operaction operaction) throws IndexOutOfBoundsException {
        m_operactions.add(operaction);
    }

    /**
     * Adds the operaction.
     *
     * @param index
     *            the index
     * @param operaction
     *            the operaction
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addOperaction(final int index, final Operaction operaction) throws IndexOutOfBoundsException {
        m_operactions.add(index, operaction);
    }

    /**
     * Adds the script.
     *
     * @param script
     *            the script
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addScript(final Script script) throws IndexOutOfBoundsException {
        m_scripts.add(script);
    }

    /**
     * Adds the script.
     *
     * @param index
     *            the index
     * @param script
     *            the script
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addScript(final int index, final Script script) throws IndexOutOfBoundsException {
        m_scripts.add(index, script);
    }

    /**
     * Adds the varbindsdecode.
     *
     * @param varbindsdecode
     *            the varbindsdecode
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addVarbindsdecode(final Varbindsdecode varbindsdecode) throws IndexOutOfBoundsException {
        m_varbindsdecodes.add(varbindsdecode);
    }

    /**
     * Adds the varbindsdecode.
     *
     * @param index
     *            the index
     * @param varbindsdecode
     *            the varbindsdecode
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addVarbindsdecode(final int index, final Varbindsdecode varbindsdecode)
            throws IndexOutOfBoundsException {
        m_varbindsdecodes.add(index, varbindsdecode);
    }

    /**
     * Enumerate autoaction.
     *
     * @return the enumeration
     */
    public Enumeration<Autoaction> enumerateAutoaction() {
        return Collections.enumeration(m_autoactions);
    }

    /**
     * Enumerate forward.
     *
     * @return the enumeration
     */
    public Enumeration<Forward> enumerateForward() {
        return Collections.enumeration(m_forwards);
    }

    /**
     * Enumerate loggroup.
     *
     * @return the enumeration
     */
    public Enumeration<String> enumerateLoggroup() {
        return Collections.enumeration(m_loggroups);
    }

    /**
     * Enumerate operaction.
     *
     * @return the enumeration
     */
    public Enumeration<Operaction> enumerateOperaction() {
        return Collections.enumeration(m_operactions);
    }

    /**
     * Enumerate script.
     *
     * @return the enumeration
     */
    public Enumeration<Script> enumerateScript() {
        return Collections.enumeration(m_scripts);
    }

    /**
     * Enumerate varbindsdecode.
     *
     * @return the enumeration
     */
    public Enumeration<Varbindsdecode> enumerateVarbindsdecode() {
        return Collections.enumeration(m_varbindsdecodes);
    }

    /**
     * Gets the alarm data.
     *
     * @return the alarm data
     */
    public AlarmData getAlarmData() {
        return m_alarmData;
    }

    /**
     * Gets the autoacknowledge.
     *
     * @return the autoacknowledge
     */
    public Autoacknowledge getAutoacknowledge() {
        return m_autoacknowledge;
    }

    /**
     * Gets the autoaction.
     *
     * @param index
     *            the index
     * @return the autoaction
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public Autoaction getAutoaction(final int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_autoactions.size()) {
            throw new IndexOutOfBoundsException("getAutoaction: Index value '" + index + "' not in range [0.."
                    + (m_autoactions.size() - 1) + "]");
        }

        return m_autoactions.get(index);
    }

    /**
     * Gets the autoaction.
     *
     * @return the autoaction
     */
    public Autoaction[] getAutoaction() {
        return m_autoactions.toArray(EMPTY_AUTOACTION_ARRAY);
    }

    /**
     * Gets the autoaction collection.
     *
     * @return the autoaction collection
     */
    public List<Autoaction> getAutoactionCollection() {
        return m_autoactions;
    }

    /**
     * Gets the autoaction count.
     *
     * @return the autoaction count
     */
    public int getAutoactionCount() {
        return m_autoactions.size();
    }

    /**
     * Gets the correlation.
     *
     * @return the correlation
     */
    public Correlation getCorrelation() {
        return m_correlation;
    }

    /**
     * Gets the descr.
     *
     * @return the descr
     */
    public String getDescr() {
        return m_descr;
    }

    /**
     * Gets the event label.
     *
     * @return the event label
     */
    public String getEventLabel() {
        return m_eventLabel;
    }

    /**
     * Gets the forward.
     *
     * @param index
     *            the index
     * @return the forward
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public Forward getForward(final int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_forwards.size()) {
            throw new IndexOutOfBoundsException("getForward: Index value '" + index + "' not in range [0.."
                    + (m_forwards.size() - 1) + "]");
        }
        return m_forwards.get(index);
    }

    /**
     * Gets the filters.
     *
     * @return the filters
     */
    public Filters getFilters() {
        return m_filters;
    }

    /**
     * Gets the forward.
     *
     * @return the forward
     */
    public Forward[] getForward() {
        return m_forwards.toArray(EMPTY_FORWARD_ARRAY);
    }

    /**
     * Gets the forward collection.
     *
     * @return the forward collection
     */
    public List<Forward> getForwardCollection() {
        return m_forwards;
    }

    /**
     * Gets the forward count.
     *
     * @return the forward count
     */
    public int getForwardCount() {
        return m_forwards.size();
    }

    /**
     * Gets the loggroup.
     *
     * @param index
     *            the index
     * @return the loggroup
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public String getLoggroup(final int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_loggroups.size()) {
            throw new IndexOutOfBoundsException("getLoggroup: Index value '" + index + "' not in range [0.."
                    + (m_loggroups.size() - 1) + "]");
        }
        return m_loggroups.get(index);
    }

    /**
     * Gets the loggroup.
     *
     * @return the loggroup
     */
    public String[] getLoggroup() {
        return m_loggroups.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Gets the loggroup collection.
     *
     * @return the loggroup collection
     */
    public List<String> getLoggroupCollection() {
        return m_loggroups;
    }

    /**
     * Gets the loggroup count.
     *
     * @return the loggroup count
     */
    public int getLoggroupCount() {
        return m_loggroups.size();
    }

    /**
     * Gets the logmsg.
     *
     * @return the logmsg
     */
    public Logmsg getLogmsg() {
        return m_logmsg;
    }

    /**
     * Gets the mask.
     *
     * @return the mask
     */
    public Mask getMask() {
        return m_mask;
    }

    /**
     * Gets the mouseovertext.
     *
     * @return the mouseovertext
     */
    public String getMouseovertext() {
        return m_mouseovertext;
    }

    /**
     * Gets the operaction.
     *
     * @param index
     *            the index
     * @return the operaction
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public Operaction getOperaction(final int index) throws IndexOutOfBoundsException {
        return m_operactions.get(index);
    }

    /**
     * Gets the operaction.
     *
     * @return the operaction
     */
    public Operaction[] getOperaction() {
        return m_operactions.toArray(EMPTY_OPERACTION_ARRAY);
    }

    /**
     * Gets the operaction collection.
     *
     * @return the operaction collection
     */
    public List<Operaction> getOperactionCollection() {
        return m_operactions;
    }

    /**
     * Gets the operaction count.
     *
     * @return the operaction count
     */
    public int getOperactionCount() {
        return m_operactions.size();
    }

    /**
     * Gets the operinstruct.
     *
     * @return the operinstruct
     */
    public String getOperinstruct() {
        return m_operinstruct;
    }

    /**
     * Gets the script.
     *
     * @param index
     *            the index
     * @return the script
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public Script getScript(final int index) throws IndexOutOfBoundsException {
        return m_scripts.get(index);
    }

    /**
     * Gets the script.
     *
     * @return the script
     */
    public Script[] getScript() {
        return m_scripts.toArray(EMPTY_SCRIPT_ARRAY);
    }

    /**
     * Gets the script collection.
     *
     * @return the script collection
     */
    public List<Script> getScriptCollection() {
        return m_scripts;
    }

    /**
     * Gets the script count.
     *
     * @return the script count
     */
    public int getScriptCount() {
        return m_scripts.size();
    }

    /**
     * Gets the severity.
     *
     * @return the severity
     */
    public String getSeverity() {
        return m_severity;
    }

    /**
     * Gets the snmp.
     *
     * @return the snmp
     */
    public Snmp getSnmp() {
        return m_snmp;
    }

    /**
     * Gets the tticket.
     *
     * @return the tticket
     */
    public Tticket getTticket() {
        return m_tticket;
    }

    /**
     * Gets the uei.
     *
     * @return the uei
     */
    public String getUei() {
        return m_uei;
    }

    /**
     * Gets the varbindsdecode.
     *
     * @param index
     *            the index
     * @return the varbindsdecode
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public Varbindsdecode getVarbindsdecode(final int index) throws IndexOutOfBoundsException {
        return m_varbindsdecodes.get(index);
    }

    /**
     * Gets the varbindsdecode.
     *
     * @return the varbindsdecode
     */
    public Varbindsdecode[] getVarbindsdecode() {
        return m_varbindsdecodes.toArray(EMPTY_VARBINDSDECODE_ARRAY);
    }

    /**
     * Gets the varbindsdecode collection.
     *
     * @return the varbindsdecode collection
     */
    public List<Varbindsdecode> getVarbindsdecodeCollection() {
        return m_varbindsdecodes;
    }

    /**
     * Gets the varbindsdecode count.
     *
     * @return the varbindsdecode count
     */
    public int getVarbindsdecodeCount() {
        return m_varbindsdecodes.size();
    }

    /**
     * Checks if is valid.
     *
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
        try {
            validate();
        } catch (final ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * Iterate autoaction.
     *
     * @return the iterator
     */
    public Iterator<Autoaction> iterateAutoaction() {
        return m_autoactions.iterator();
    }

    /**
     * Iterate forward.
     *
     * @return the iterator
     */
    public Iterator<Forward> iterateForward() {
        return m_forwards.iterator();
    }

    /**
     * Iterate loggroup.
     *
     * @return the iterator
     */
    public Iterator<String> iterateLoggroup() {
        return m_loggroups.iterator();
    }

    /**
     * Iterate operaction.
     *
     * @return the iterator
     */
    public Iterator<Operaction> iterateOperaction() {
        return m_operactions.iterator();
    }

    /**
     * Iterate script.
     *
     * @return the iterator
     */
    public Iterator<Script> iterateScript() {
        return m_scripts.iterator();
    }

    /**
     * Iterate varbindsdecode.
     *
     * @return the iterator
     */
    public Iterator<Varbindsdecode> iterateVarbindsdecode() {
        return m_varbindsdecodes.iterator();
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
    public void marshal(final Writer out) throws MarshalException, ValidationException {
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
    public void marshal(final ContentHandler handler) throws IOException, MarshalException, ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Removes the all autoaction.
     */
    public void removeAllAutoaction() {
        m_autoactions.clear();
    }

    /**
     * Removes the all forward.
     */
    public void removeAllForward() {
        m_forwards.clear();
    }

    /**
     * Removes the all loggroup.
     */
    public void removeAllLoggroup() {
        m_loggroups.clear();
    }

    /**
     * Removes the all operaction.
     */
    public void removeAllOperaction() {
        m_operactions.clear();
    }

    /**
     * Removes the all script.
     */
    public void removeAllScript() {
        m_scripts.clear();
    }

    /**
     * Removes the all varbindsdecode.
     */
    public void removeAllVarbindsdecode() {
        m_varbindsdecodes.clear();
    }

    /**
     * Removes the autoaction.
     *
     * @param autoaction
     *            the autoaction
     * @return true, if successful
     */
    public boolean removeAutoaction(final Autoaction autoaction) {
        return m_autoactions.remove(autoaction);
    }

    /**
     * Removes the autoaction at.
     *
     * @param index
     *            the index
     * @return the autoaction
     */
    public Autoaction removeAutoactionAt(final int index) {
        return m_autoactions.remove(index);
    }

    /**
     * Removes the forward.
     *
     * @param forward
     *            the forward
     * @return true, if successful
     */
    public boolean removeForward(final Forward forward) {
        return m_forwards.remove(forward);
    }

    /**
     * Removes the forward at.
     *
     * @param index
     *            the index
     * @return the forward
     */
    public Forward removeForwardAt(final int index) {
        return m_forwards.remove(index);
    }

    /**
     * Removes the loggroup.
     *
     * @param loggroup
     *            the loggroup
     * @return true, if successful
     */
    public boolean removeLoggroup(final String loggroup) {
        return m_loggroups.remove(loggroup);
    }

    /**
     * Removes the loggroup at.
     *
     * @param index
     *            the index
     * @return the string
     */
    public String removeLoggroupAt(final int index) {
        return m_loggroups.remove(index);
    }

    /**
     * Removes the operaction.
     *
     * @param operaction
     *            the operaction
     * @return true, if successful
     */
    public boolean removeOperaction(final Operaction operaction) {
        return m_operactions.remove(operaction);
    }

    /**
     * Removes the operaction at.
     *
     * @param index
     *            the index
     * @return the operaction
     */
    public Operaction removeOperactionAt(final int index) {
        return m_operactions.remove(index);
    }

    /**
     * Removes the script.
     *
     * @param script
     *            the script
     * @return true, if successful
     */
    public boolean removeScript(final Script script) {
        return m_scripts.remove(script);
    }

    /**
     * Removes the script at.
     *
     * @param index
     *            the index
     * @return the script
     */
    public Script removeScriptAt(final int index) {
        return m_scripts.remove(index);
    }

    /**
     * Removes the varbindsdecode.
     *
     * @param decode
     *            the decode
     * @return true, if successful
     */
    public boolean removeVarbindsdecode(final Varbindsdecode decode) {
        return m_varbindsdecodes.remove(decode);
    }

    /**
     * Removes the varbindsdecode at.
     *
     * @param index
     *            the index
     * @return the varbindsdecode
     */
    public Varbindsdecode removeVarbindsdecodeAt(final int index) {
        return m_varbindsdecodes.remove(index);
    }

    /**
     * Sets the alarm data.
     *
     * @param alarmData
     *            the new alarm data
     */
    public void setAlarmData(final AlarmData alarmData) {
        m_alarmData = alarmData;
    }

    /**
     * Sets the autoacknowledge.
     *
     * @param autoacknowledge
     *            the new autoacknowledge
     */
    public void setAutoacknowledge(final Autoacknowledge autoacknowledge) {
        m_autoacknowledge = autoacknowledge;
    }

    /**
     * Sets the autoaction.
     *
     * @param index
     *            the index
     * @param autoaction
     *            the autoaction
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setAutoaction(final int index, final Autoaction autoaction) throws IndexOutOfBoundsException {
        m_autoactions.set(index, autoaction);
    }

    /**
     * Sets the autoaction.
     *
     * @param autoactions
     *            the new autoaction
     */
    public void setAutoaction(final Autoaction[] autoactions) {
        m_autoactions.clear();
        for (final Autoaction act : autoactions) {
            m_autoactions.add(act);
        }
    }

    /**
     * Sets the autoaction.
     *
     * @param autoactions
     *            the new autoaction
     */
    public void setAutoaction(final List<Autoaction> autoactions) {
        if (m_autoactions == autoactions)
            return;
        m_autoactions.clear();
        m_autoactions.addAll(autoactions);
    }

    /**
     * Sets the autoaction collection.
     *
     * @param autoactions
     *            the new autoaction collection
     */
    public void setAutoactionCollection(final List<Autoaction> autoactions) {
        setAutoaction(autoactions);
    }

    /**
     * Sets the correlation.
     *
     * @param correlation
     *            the new correlation
     */
    public void setCorrelation(final Correlation correlation) {
        m_correlation = correlation;
    }

    /**
     * Sets the descr.
     *
     * @param descr
     *            the new descr
     */
    public void setDescr(final String descr) {
        m_descr = descr.intern();
    }

    /**
     * Sets the event label.
     *
     * @param eventLabel
     *            the new event label
     */
    public void setEventLabel(final String eventLabel) {
        m_eventLabel = eventLabel.intern();
    }

    /**
     * Sets the filters.
     *
     * @param filters
     *            the new filters
     */
    public void setFilters(final Filters filters) {
        m_filters = filters;
    }

    /**
     * Sets the forward.
     *
     * @param index
     *            the index
     * @param forward
     *            the forward
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setForward(final int index, final Forward forward) throws IndexOutOfBoundsException {
        m_forwards.set(index, forward);
    }

    /**
     * Sets the forward.
     *
     * @param forwards
     *            the new forward
     */
    public void setForward(final Forward[] forwards) {
        m_forwards.clear();
        for (final Forward forward : forwards) {
            m_forwards.add(forward);
        }
    }

    /**
     * Sets the forward.
     *
     * @param forwards
     *            the new forward
     */
    public void setForward(final List<Forward> forwards) {
        if (m_forwards == forwards)
            return;
        m_forwards.clear();
        m_forwards.addAll(forwards);
    }

    /**
     * Sets the forward collection.
     *
     * @param forwards
     *            the new forward collection
     */
    public void setForwardCollection(final List<Forward> forwards) {
        setForward(forwards);
    }

    /**
     * Sets the loggroup.
     *
     * @param index
     *            the index
     * @param loggroup
     *            the loggroup
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setLoggroup(final int index, final String loggroup) throws IndexOutOfBoundsException {
        m_loggroups.set(index, loggroup.intern());
    }

    /**
     * Sets the loggroup.
     *
     * @param loggroups
     *            the new loggroup
     */
    public void setLoggroup(final String[] loggroups) {
        m_loggroups.clear();
        for (final String group : loggroups) {
            m_loggroups.add(group.intern());
        }
    }

    /**
     * Sets the loggroup.
     *
     * @param loggroups
     *            the new loggroup
     */
    public void setLoggroup(final List<String> loggroups) {
        if (m_loggroups == loggroups)
            return;
        m_loggroups.clear();
        m_loggroups.addAll(loggroups);
    }

    /**
     * Sets the loggroup collection.
     *
     * @param loggroups
     *            the new loggroup collection
     */
    public void setLoggroupCollection(final List<String> loggroups) {
        setLoggroup(loggroups);
    }

    /**
     * Sets the logmsg.
     *
     * @param logmsg
     *            the new logmsg
     */
    public void setLogmsg(final Logmsg logmsg) {
        m_logmsg = logmsg;
    }

    /**
     * Sets the mask.
     *
     * @param mask
     *            the new mask
     */
    public void setMask(final Mask mask) {
        m_mask = mask;
    }

    /**
     * Sets the mouseovertext.
     *
     * @param mouseovertext
     *            the new mouseovertext
     */
    public void setMouseovertext(final String mouseovertext) {
        m_mouseovertext = mouseovertext.intern();
    }

    /**
     * Sets the operaction.
     *
     * @param index
     *            the index
     * @param operaction
     *            the operaction
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setOperaction(final int index, final Operaction operaction) throws IndexOutOfBoundsException {
        m_operactions.set(index, operaction);
    }

    /**
     * Sets the operaction.
     *
     * @param operactions
     *            the new operaction
     */
    public void setOperaction(final Operaction[] operactions) {
        m_operactions.clear();
        for (final Operaction action : operactions) {
            m_operactions.add(action);
        }
    }

    /**
     * Sets the operaction.
     *
     * @param operactions
     *            the new operaction
     */
    public void setOperaction(final List<Operaction> operactions) {
        if (m_operactions == operactions)
            return;
        m_operactions.clear();
        m_operactions.addAll(operactions);
    }

    /**
     * Sets the operaction collection.
     *
     * @param operactions
     *            the new operaction collection
     */
    public void setOperactionCollection(final List<Operaction> operactions) {
        setOperaction(operactions);
    }

    /**
     * Sets the operinstruct.
     *
     * @param operinstruct
     *            the new operinstruct
     */
    public void setOperinstruct(final String operinstruct) {
        m_operinstruct = operinstruct.intern();
    }

    /**
     * Sets the script.
     *
     * @param index
     *            the index
     * @param script
     *            the script
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setScript(final int index, final Script script) throws IndexOutOfBoundsException {
        m_scripts.set(index, script);
    }

    /**
     * Sets the script.
     *
     * @param scripts
     *            the new script
     */
    public void setScript(final Script[] scripts) {
        m_scripts.clear();
        for (final Script script : scripts) {
            m_scripts.add(script);
        }
    }

    /**
     * Sets the script.
     *
     * @param scripts
     *            the new script
     */
    public void setScript(final List<Script> scripts) {
        if (m_scripts == scripts)
            return;
        m_scripts.clear();
        m_scripts.addAll(scripts);
    }

    /**
     * Sets the script collection.
     *
     * @param scripts
     *            the new script collection
     */
    public void setScriptCollection(final List<Script> scripts) {
        setScript(scripts);
    }

    /**
     * Sets the severity.
     *
     * @param severity
     *            the new severity
     */
    public void setSeverity(final String severity) {
        m_severity = severity.intern();
    }

    /**
     * Sets the snmp.
     *
     * @param snmp
     *            the new snmp
     */
    public void setSnmp(final Snmp snmp) {
        m_snmp = snmp;
    }

    /**
     * Sets the tticket.
     *
     * @param tticket
     *            the new tticket
     */
    public void setTticket(final Tticket tticket) {
        m_tticket = tticket;
    }

    /**
     * Sets the uei.
     *
     * @param uei
     *            the new uei
     */
    public void setUei(final String uei) {
        m_uei = uei.intern();
    }

    /**
     * Sets the varbindsdecode.
     *
     * @param index
     *            the index
     * @param decode
     *            the decode
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setVarbindsdecode(final int index, final Varbindsdecode decode) throws IndexOutOfBoundsException {
        m_varbindsdecodes.set(index, decode);
    }

    /**
     * Sets the varbindsdecode.
     *
     * @param decodes
     *            the new varbindsdecode
     */
    public void setVarbindsdecode(final Varbindsdecode[] decodes) {
        m_varbindsdecodes.clear();
        for (final Varbindsdecode decode : decodes) {
            m_varbindsdecodes.add(decode);
        }
    }

    /**
     * Sets the varbindsdecode.
     *
     * @param decodes
     *            the new varbindsdecode
     */
    public void setVarbindsdecode(final List<Varbindsdecode> decodes) {
        if (m_varbindsdecodes == decodes)
            return;
        m_varbindsdecodes.clear();
        m_varbindsdecodes.addAll(decodes);
    }

    /**
     * Sets the varbindsdecode collection.
     *
     * @param decodes
     *            the new varbindsdecode collection
     */
    public void setVarbindsdecodeCollection(final List<Varbindsdecode> decodes) {
        setVarbindsdecode(decodes);
    }

    /**
     * Unmarshal.
     *
     * @param reader
     *            the reader
     * @return the event
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public static Event unmarshal(final Reader reader) throws MarshalException, ValidationException {
        return (Event) Unmarshaller.unmarshal(Event.class, reader);
    }

    /**
     * Validate.
     *
     * @throws ValidationException
     *             the validation exception
     */
    public void validate() throws ValidationException {
        new Validator().validate(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_alarmData == null) ? 0 : m_alarmData.hashCode());
        result = prime * result + ((m_autoacknowledge == null) ? 0 : m_autoacknowledge.hashCode());
        result = prime * result + ((m_autoactions == null) ? 0 : m_autoactions.hashCode());
        result = prime * result + ((m_correlation == null) ? 0 : m_correlation.hashCode());
        result = prime * result + ((m_descr == null) ? 0 : m_descr.hashCode());
        result = prime * result + ((m_eventLabel == null) ? 0 : m_eventLabel.hashCode());
        result = prime * result + ((m_filters == null) ? 0 : m_filters.hashCode());
        result = prime * result + ((m_forwards == null) ? 0 : m_forwards.hashCode());
        result = prime * result + ((m_loggroups == null) ? 0 : m_loggroups.hashCode());
        result = prime * result + ((m_logmsg == null) ? 0 : m_logmsg.hashCode());
        result = prime * result + ((m_mask == null) ? 0 : m_mask.hashCode());
        result = prime * result + ((m_mouseovertext == null) ? 0 : m_mouseovertext.hashCode());
        result = prime * result + ((m_operactions == null) ? 0 : m_operactions.hashCode());
        result = prime * result + ((m_operinstruct == null) ? 0 : m_operinstruct.hashCode());
        result = prime * result + ((m_scripts == null) ? 0 : m_scripts.hashCode());
        result = prime * result + ((m_severity == null) ? 0 : m_severity.hashCode());
        result = prime * result + ((m_snmp == null) ? 0 : m_snmp.hashCode());
        result = prime * result + ((m_tticket == null) ? 0 : m_tticket.hashCode());
        result = prime * result + ((m_uei == null) ? 0 : m_uei.hashCode());
        result = prime * result + ((m_varbindsdecodes == null) ? 0 : m_varbindsdecodes.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Event))
            return false;
        final Event other = (Event) obj;
        if (m_alarmData == null) {
            if (other.m_alarmData != null)
                return false;
        } else if (!m_alarmData.equals(other.m_alarmData)) {
            return false;
        }
        if (m_autoacknowledge == null) {
            if (other.m_autoacknowledge != null)
                return false;
        } else if (!m_autoacknowledge.equals(other.m_autoacknowledge)) {
            return false;
        }
        if (m_autoactions == null) {
            if (other.m_autoactions != null)
                return false;
        } else if (!m_autoactions.equals(other.m_autoactions)) {
            return false;
        }
        if (m_correlation == null) {
            if (other.m_correlation != null)
                return false;
        } else if (!m_correlation.equals(other.m_correlation)) {
            return false;
        }
        if (m_descr == null) {
            if (other.m_descr != null)
                return false;
        } else if (!m_descr.equals(other.m_descr)) {
            return false;
        }
        if (m_eventLabel == null) {
            if (other.m_eventLabel != null)
                return false;
        } else if (!m_eventLabel.equals(other.m_eventLabel)) {
            return false;
        }
        if (m_filters == null) {
            if (other.m_filters != null)
                return false;
        } else if (!m_filters.equals(other.m_filters)) {
            return false;
        }
        if (m_forwards == null) {
            if (other.m_forwards != null)
                return false;
        } else if (!m_forwards.equals(other.m_forwards)) {
            return false;
        }
        if (m_loggroups == null) {
            if (other.m_loggroups != null)
                return false;
        } else if (!m_loggroups.equals(other.m_loggroups)) {
            return false;
        }
        if (m_logmsg == null) {
            if (other.m_logmsg != null)
                return false;
        } else if (!m_logmsg.equals(other.m_logmsg)) {
            return false;
        }
        if (m_mask == null) {
            if (other.m_mask != null)
                return false;
        } else if (!m_mask.equals(other.m_mask)) {
            return false;
        }
        if (m_mouseovertext == null) {
            if (other.m_mouseovertext != null)
                return false;
        } else if (!m_mouseovertext.equals(other.m_mouseovertext)) {
            return false;
        }
        if (m_operactions == null) {
            if (other.m_operactions != null)
                return false;
        } else if (!m_operactions.equals(other.m_operactions)) {
            return false;
        }
        if (m_operinstruct == null) {
            if (other.m_operinstruct != null)
                return false;
        } else if (!m_operinstruct.equals(other.m_operinstruct)) {
            return false;
        }
        if (m_scripts == null) {
            if (other.m_scripts != null)
                return false;
        } else if (!m_scripts.equals(other.m_scripts)) {
            return false;
        }
        if (m_severity == null) {
            if (other.m_severity != null)
                return false;
        } else if (!m_severity.equals(other.m_severity)) {
            return false;
        }
        if (m_snmp == null) {
            if (other.m_snmp != null)
                return false;
        } else if (!m_snmp.equals(other.m_snmp)) {
            return false;
        }
        if (m_tticket == null) {
            if (other.m_tticket != null)
                return false;
        } else if (!m_tticket.equals(other.m_tticket)) {
            return false;
        }
        if (m_uei == null) {
            if (other.m_uei != null)
                return false;
        } else if (!m_uei.equals(other.m_uei)) {
            return false;
        }
        if (m_varbindsdecodes == null) {
            if (other.m_varbindsdecodes != null)
                return false;
        } else if (!m_varbindsdecodes.equals(other.m_varbindsdecodes)) {
            return false;
        }
        return true;
    }

    /**
     * Construct matcher.
     *
     * @return the event matcher
     */
    private EventMatcher constructMatcher() {
        if (m_mask == null || m_mask.getMaskelementCount() <= 0) {
            return m_uei == null ? EventMatchers.falseMatcher() : EventMatchers.ueiMatcher(m_uei);
        } else {
            return m_mask.constructMatcher();
        }
    }

    /**
     * Matches.
     *
     * @param matchingEvent
     *            the matching event
     * @return true, if successful
     */
    public boolean matches(org.opennms.netmgt.xml.event.Event matchingEvent) {
        // System.err.println("Attempting to match " + m_matcher);
        return m_matcher.matches(matchingEvent);
    }

    /**
     * Initialize.
     */
    public void initialize() {
        m_matcher = constructMatcher();
    }

    /**
     * Gets the mask element values.
     *
     * @param mename
     *            the mename
     * @return the mask element values
     */
    public List<String> getMaskElementValues(String mename) {
        return m_mask == null ? null : m_mask.getMaskElementValues(mename);
    }

}
