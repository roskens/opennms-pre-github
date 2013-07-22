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

package org.opennms.netmgt.config;

import java.beans.PropertyEditorSupport;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.db.DataSourceFactory;
import org.opennms.core.utils.ConfigFileConstants;
import org.opennms.core.utils.MatchTable;
import org.opennms.core.utils.PropertiesUtils;
import org.opennms.core.utils.SingleResultQuerier;
import org.opennms.core.xml.CastorUtils;
import org.opennms.netmgt.config.translator.Assignment;
import org.opennms.netmgt.config.translator.EventTranslationSpec;
import org.opennms.netmgt.config.translator.EventTranslatorConfiguration;
import org.opennms.netmgt.config.translator.Mapping;
import org.opennms.netmgt.config.translator.Translation;
import org.opennms.netmgt.config.translator.Value;
import org.opennms.netmgt.eventd.datablock.EventUtil;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Parm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * This is the singleton class used to load the configuration from the
 * passive-status-configuration.xml. This provides convenience methods to get
 * the configured
 * categories and their information, add/delete categories from category groups.
 * <strong>Note: </strong>Users of this class should make sure the
 * <em>init()</em> is called before calling any other method to ensure the
 * config is loaded before accessing other convenience methods.
 *
 * @author <a href="mailto:david@opennms.org">David Hustace </a>
 * @author <a href="http://www.opennms.org/">OpenNMS </a>
 */
public final class EventTranslatorConfigFactory implements EventTranslatorConfig {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(EventTranslatorConfigFactory.class);

    /** The singleton instance of this factory. */
    private static EventTranslatorConfig m_singleton = null;

    /** The config class loaded from the config file. */
    private EventTranslatorConfiguration m_config;

    /** The m_translation specs. */
    private List<TranslationSpec> m_translationSpecs;

    /**
     * This member is set to true if the configuration file has been loaded.
     */
    private static boolean m_loaded = false;

    /** connection factory for use with sql-value. */
    private DataSource m_dbConnFactory = null;

    /**
     * Private constructor.
     *
     * @param configFile
     *            the config file
     * @param dbConnFactory
     *            the db conn factory
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    private EventTranslatorConfigFactory(String configFile, DataSource dbConnFactory) throws IOException,
            MarshalException, ValidationException {
        InputStream stream = null;
        try {
            stream = new FileInputStream(configFile);
            unmarshall(stream, dbConnFactory);
        } finally {
            if (stream != null) {
                IOUtils.closeQuietly(stream);
            }
        }
    }

    /**
     * <p>
     * Constructor for EventTranslatorConfigFactory.
     * </p>
     *
     * @param rdr
     *            a {@link java.io.Reader} object.
     * @param dbConnFactory
     *            a {@link javax.sql.DataSource} object.
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public EventTranslatorConfigFactory(InputStream rdr, DataSource dbConnFactory) throws MarshalException,
            ValidationException {
        unmarshall(rdr, dbConnFactory);
    }

    /**
     * Unmarshall.
     *
     * @param stream
     *            the stream
     * @param dbConnFactory
     *            the db conn factory
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    private synchronized void unmarshall(InputStream stream, DataSource dbConnFactory) throws MarshalException,
            ValidationException {
        m_config = CastorUtils.unmarshal(EventTranslatorConfiguration.class, stream);
        m_dbConnFactory = dbConnFactory;
    }

    /**
     * Unmarshall.
     *
     * @param stream
     *            the stream
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    private synchronized void unmarshall(InputStream stream) throws MarshalException, ValidationException {
        unmarshall(stream, null);
    }

    /**
     * Simply marshals the config without messing with the singletons.
     *
     * @throws Exception
     *             the exception
     */
    @Override
    public void update() throws Exception {

        synchronized (this) {

            File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.TRANSLATOR_CONFIG_FILE_NAME);
            InputStream stream = null;

            try {
                stream = new FileInputStream(cfgFile);
                unmarshall(stream);
            } finally {
                if (stream != null) {
                    IOUtils.closeQuietly(stream);
                }
            }

        }
    }

    /**
     * Load the config from the default config file and create the singleton
     * instance of this factory.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws SQLException
     *             the sQL exception
     * @throws PropertyVetoException
     *             the property veto exception
     */
    public static synchronized void init() throws IOException, MarshalException, ValidationException,
            ClassNotFoundException, SQLException, PropertyVetoException {
        if (m_loaded) {
            // init already called - return
            // to reload, reload() will need to be called
            return;
        }

        DataSourceFactory.init();

        File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.TRANSLATOR_CONFIG_FILE_NAME);

        m_singleton = new EventTranslatorConfigFactory(cfgFile.getPath(), DataSourceFactory.getInstance());

        m_loaded = true;
    }

    /**
     * Reload the config from the default config file.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws SQLException
     *             the sQL exception
     * @throws PropertyVetoException
     *             the property veto exception
     */
    public static synchronized void reload() throws IOException, MarshalException, ValidationException,
            ClassNotFoundException, SQLException, PropertyVetoException {
        m_singleton = null;
        m_loaded = false;

        init();
    }

    /**
     * Return the singleton instance of this factory.
     *
     * @return The current factory instance.
     */
    public static synchronized EventTranslatorConfig getInstance() {
        if (!m_loaded) {
            throw new IllegalStateException("getInstance: The factory has not been initialized");
        }

        return m_singleton;
    }

    /**
     * <p>
     * setInstance
     * </p>
     * .
     *
     * @param singleton
     *            a {@link org.opennms.netmgt.config.EventTranslatorConfig}
     *            object.
     */
    public static void setInstance(EventTranslatorConfig singleton) {
        m_singleton = singleton;
        m_loaded = true;
    }

    /**
     * Return the PassiveStatus configuration.
     *
     * @return the PassiveStatus configuration
     */
    private synchronized EventTranslatorConfiguration getConfig() {
        return m_config;
    }

    /*
     * (non-Javadoc)
     * @see org.opennms.netmgt.config.PassiveStatusConfig#getUEIList()
     */
    /**
     * <p>
     * getUEIList
     * </p>
     * .
     *
     * @return a {@link java.util.List} object.
     */
    @Override
    public List<String> getUEIList() {
        return getTranslationUEIs();
    }

    /**
     * Gets the translation ue is.
     *
     * @return the translation ue is
     */
    private List<String> getTranslationUEIs() {
        Translation translation = getConfig().getTranslation();
        if (translation == null) {
            return Collections.emptyList();
        }

        List<String> ueis = new ArrayList<String>();
        for (EventTranslationSpec event : translation.getEventTranslationSpecCollection()) {
            ueis.add(event.getUei());
        }
        return ueis;
    }

    /**
     * The Class TranslationFailedException.
     */
    static class TranslationFailedException extends RuntimeException {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -7219413891842193464L;

        /**
         * Instantiates a new translation failed exception.
         *
         * @param msg
         *            the msg
         */
        TranslationFailedException(String msg) {
            super(msg);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isTranslationEvent(Event e) {
        for (TranslationSpec spec : getTranslationSpecs()) {
            if (spec.matches(e)) {
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public List<Event> translateEvent(Event e) {
        ArrayList<Event> events = new ArrayList<Event>();
        for (TranslationSpec spec : getTranslationSpecs()) {
            events.addAll(spec.translate(e));
        }
        return events;
    }

    /**
     * Gets the translation specs.
     *
     * @return the translation specs
     */
    private List<TranslationSpec> getTranslationSpecs() {
        if (m_translationSpecs == null) {
            m_translationSpecs = constructTranslationSpecs();
        }

        return m_translationSpecs;
    }

    /**
     * Construct translation specs.
     *
     * @return the list
     */
    private List<TranslationSpec> constructTranslationSpecs() {
        List<TranslationSpec> specs = new ArrayList<TranslationSpec>();
        for (EventTranslationSpec eventTrans : m_config.getTranslation().getEventTranslationSpecCollection()) {
            specs.add(new TranslationSpec(eventTrans));
        }
        return specs;
    }

    /**
     * The Class TranslationSpec.
     */
    class TranslationSpec {

        /** The m_spec. */
        private EventTranslationSpec m_spec;

        /** The m_translation mappings. */
        private List<TranslationMapping> m_translationMappings;

        /**
         * Instantiates a new translation spec.
         *
         * @param spec
         *            the spec
         */
        TranslationSpec(EventTranslationSpec spec) {
            m_spec = spec;
            m_translationMappings = null; // lazy init
        }

        /**
         * Translate.
         *
         * @param e
         *            the e
         * @return the list
         */
        public List<Event> translate(Event e) {
            // short circuit here is the uei doesn't match
            if (!ueiMatches(e)) {
                return Collections.emptyList();
            }

            // uei matches now go thru the mappings
            ArrayList<Event> events = new ArrayList<Event>();
            for (TranslationMapping mapping : getTranslationMappings()) {
                Event translatedEvent = mapping.translate(e);
                if (translatedEvent != null) {
                    events.add(translatedEvent);
                }
            }

            return events;
        }

        /**
         * Gets the uei.
         *
         * @return the uei
         */
        String getUei() {
            return m_spec.getUei();
        }

        /**
         * Gets the event translation spec.
         *
         * @return the event translation spec
         */
        public EventTranslationSpec getEventTranslationSpec() {
            return m_spec;
        }

        /**
         * Construct translation mappings.
         *
         * @return the list
         */
        private List<TranslationMapping> constructTranslationMappings() {
            if (m_spec.getMappings() == null) {
                return Collections.emptyList();
            }

            List<Mapping> mappings = m_spec.getMappings().getMappingCollection();

            List<TranslationMapping> transMaps = new ArrayList<TranslationMapping>(mappings.size());
            for (Mapping mapping : mappings) {
                TranslationMapping transMap = new TranslationMapping(mapping);
                transMaps.add(transMap);
            }

            return Collections.unmodifiableList(transMaps);
        }

        /**
         * Gets the translation mappings.
         *
         * @return the translation mappings
         */
        List<TranslationMapping> getTranslationMappings() {
            if (m_translationMappings == null) {
                m_translationMappings = constructTranslationMappings();
            }
            return Collections.unmodifiableList(m_translationMappings);
        }

        /**
         * Matches.
         *
         * @param e
         *            the e
         * @return true, if successful
         */
        boolean matches(Event e) {
            // short circuit if the eui doesn't match
            if (!ueiMatches(e)) {
                LOG.debug("TransSpec.matches: No match comparing spec UEI: {} with event UEI: {}", e.getUei(),
                          m_spec.getUei());
                return false;
            }

            // uei matches to go thru the mappings
            LOG.debug("TransSpec.matches: checking mappings for spec.");
            for (TranslationMapping transMap : getTranslationMappings()) {
                if (transMap.matches(e)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Uei matches.
         *
         * @param e
         *            the e
         * @return true, if successful
         */
        private boolean ueiMatches(Event e) {
            return e.getUei().equals(m_spec.getUei()) || m_spec.getUei().endsWith("/")
                    && e.getUei().startsWith(m_spec.getUei());
        }

    }

    /**
     * The Class TranslationMapping.
     */
    class TranslationMapping {

        /** The m_mapping. */
        Mapping m_mapping;

        /** The m_assignments. */
        List<AssignmentSpec> m_assignments;

        /**
         * Instantiates a new translation mapping.
         *
         * @param mapping
         *            the mapping
         */
        TranslationMapping(Mapping mapping) {
            m_mapping = mapping;
            m_assignments = null; // lazy init
        }

        /**
         * Translate.
         *
         * @param srcEvent
         *            the src event
         * @return the event
         */
        public Event translate(Event srcEvent) {
            // if the event doesn't match the mapping then don't apply the
            // translation
            if (!matches(srcEvent)) {
                return null;
            }

            Event targetEvent = cloneEvent(srcEvent);
            for (AssignmentSpec assignSpec : getAssignmentSpecs()) {
                assignSpec.apply(srcEvent, targetEvent);
            }

            targetEvent.setSource(TRANSLATOR_NAME);
            return targetEvent;
        }

        /**
         * Clone event.
         *
         * @param srcEvent
         *            the src event
         * @return the event
         */
        private Event cloneEvent(Event srcEvent) {
            Event clonedEvent = EventUtil.cloneEvent(srcEvent);
            /*
             * since alarmData and severity are computed based on translated
             * information in
             * eventd using the data from eventconf, we unset it here to eventd
             * can reset to the proper new settings.
             */
            clonedEvent.setAlarmData(null);
            clonedEvent.setSeverity(null);
            /*
             * the reasoning for alarmData and severity also applies to
             * description (see NMS-4038).
             */
            clonedEvent.setDescr(null);
            return clonedEvent;
        }

        /**
         * Gets the mapping.
         *
         * @return the mapping
         */
        public Mapping getMapping() {
            return m_mapping;
        }

        /**
         * Gets the assignment specs.
         *
         * @return the assignment specs
         */
        private List<AssignmentSpec> getAssignmentSpecs() {
            if (m_assignments == null) {
                m_assignments = constructAssignmentSpecs();
            }
            return m_assignments;
        }

        /**
         * Construct assignment specs.
         *
         * @return the list
         */
        private List<AssignmentSpec> constructAssignmentSpecs() {
            Mapping mapping = getMapping();
            List<AssignmentSpec> assignments = new ArrayList<AssignmentSpec>();
            for (Assignment assign : mapping.getAssignmentCollection()) {
                AssignmentSpec assignSpec = ("parameter".equals(assign.getType()) ? (AssignmentSpec) new ParameterAssignmentSpec(
                                                                                                                                 assign)
                    : (AssignmentSpec) new FieldAssignmentSpec(assign));
                assignments.add(assignSpec);
            }
            return assignments;
        }

        /**
         * Assignments match.
         *
         * @param e
         *            the e
         * @return true, if successful
         */
        private boolean assignmentsMatch(Event e) {
            AssignmentSpec assignSpec = null;
            for (Iterator<AssignmentSpec> it = getAssignmentSpecs().iterator(); it.hasNext();) {
                assignSpec = it.next();
                if (!assignSpec.matches(e)) {
                    LOG.debug("TranslationMapping.assignmentsMatch: assignmentSpec: {} doesn't match.",
                              assignSpec.getAttributeName());
                    return false;
                }
            }
            LOG.debug("TranslationMapping.assignmentsMatch: assignmentSpec: {} matches!", assignSpec.getAttributeName());
            return true;
        }

        /**
         * Matches.
         *
         * @param e
         *            the e
         * @return true, if successful
         */
        boolean matches(Event e) {
            return assignmentsMatch(e);
        }
    }

    /**
     * The Class AssignmentSpec.
     */
    abstract class AssignmentSpec {

        /** The m_assignment. */
        private Assignment m_assignment;

        /** The m_value spec. */
        private ValueSpec m_valueSpec;

        /**
         * Instantiates a new assignment spec.
         *
         * @param assignment
         *            the assignment
         */
        AssignmentSpec(Assignment assignment) {
            m_assignment = assignment;
            m_valueSpec = null; // lazy init
        }

        /**
         * Apply.
         *
         * @param srcEvent
         *            the src event
         * @param targetEvent
         *            the target event
         */
        public void apply(Event srcEvent, Event targetEvent) {
            setValue(targetEvent, getValueSpec().getResult(srcEvent));
        }

        /**
         * Gets the assignment.
         *
         * @return the assignment
         */
        private Assignment getAssignment() {
            return m_assignment;
        }

        /**
         * Gets the attribute name.
         *
         * @return the attribute name
         */
        protected String getAttributeName() {
            return getAssignment().getName();
        }

        /**
         * Construct value spec.
         *
         * @return the value spec
         */
        private ValueSpec constructValueSpec() {
            Value val = getAssignment().getValue();

            return EventTranslatorConfigFactory.this.getValueSpec(val);
        }

        /**
         * Sets the value.
         *
         * @param targetEvent
         *            the target event
         * @param value
         *            the value
         */
        protected abstract void setValue(Event targetEvent, String value);

        /**
         * Gets the value spec.
         *
         * @return the value spec
         */
        private ValueSpec getValueSpec() {
            if (m_valueSpec == null) {
                m_valueSpec = constructValueSpec();
            }
            return m_valueSpec;
        }

        /**
         * Matches.
         *
         * @param e
         *            the e
         * @return true, if successful
         */
        boolean matches(Event e) {
            return getValueSpec().matches(e);
        }
    }

    /**
     * The Class FieldAssignmentSpec.
     */
    class FieldAssignmentSpec extends AssignmentSpec {

        /**
         * Instantiates a new field assignment spec.
         *
         * @param field
         *            the field
         */
        FieldAssignmentSpec(Assignment field) {
            super(field);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.AssignmentSpec#setValue(org.opennms.netmgt.xml.event.Event, java.lang.String)
         */
        @Override
        protected void setValue(Event targetEvent, String value) {
            try {
                BeanWrapper bean = PropertyAccessorFactory.forBeanPropertyAccess(targetEvent);
                bean.setPropertyValue(getAttributeName(), value);
            } catch (FatalBeanException e) {
                LOG.error("Unable to set value for attribute {}to value {} Exception: {}", e, getAttributeName(), value);
                throw new TranslationFailedException("Unable to set value for attribute " + getAttributeName()
                        + " to value " + value);
            }
        }

    }

    /**
     * The Class ParameterAssignmentSpec.
     */
    class ParameterAssignmentSpec extends AssignmentSpec {

        /**
         * Instantiates a new parameter assignment spec.
         *
         * @param assign
         *            the assign
         */
        ParameterAssignmentSpec(Assignment assign) {
            super(assign);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.AssignmentSpec#setValue(org.opennms.netmgt.xml.event.Event, java.lang.String)
         */
        @Override
        protected void setValue(Event targetEvent, String value) {
            if (value == null) {
                LOG.debug("Value of parameter is null setting to blank");
                value = "";
            }

            for (final Parm parm : targetEvent.getParmCollection()) {
                if (parm.getParmName().equals(getAttributeName())) {
                    org.opennms.netmgt.xml.event.Value val = parm.getValue();
                    if (val == null) {
                        val = new org.opennms.netmgt.xml.event.Value();
                        parm.setValue(val);
                    }
                    LOG.debug("Overriding value of parameter {}. Setting it to {}", value, getAttributeName());
                    val.setContent(value);
                    return;
                }
            }

            // if we got here then we didn't find the existing parameter
            Parm newParm = new Parm();
            newParm.setParmName(getAttributeName());
            org.opennms.netmgt.xml.event.Value val = new org.opennms.netmgt.xml.event.Value();
            newParm.setValue(val);
            LOG.debug("Setting value of parameter {} to {}", value, getAttributeName());
            val.setContent(value);
            targetEvent.addParm(newParm);
        }
    }

    /**
     * Gets the value spec.
     *
     * @param val
     *            the val
     * @return the value spec
     */
    ValueSpec getValueSpec(Value val) {
        if ("field".equals(val.getType())) {
            return new FieldValueSpec(val);
        } else if ("parameter".equals(val.getType())) {
            return new ParameterValueSpec(val);
        } else if ("constant".equals(val.getType())) {
            return new ConstantValueSpec(val);
        } else if ("sql".equals(val.getType())) {
            return new SqlValueSpec(val);
        } else {
            return new ValueSpecUnspecified();
        }
    }

    /**
     * The Class ValueSpec.
     */
    abstract class ValueSpec {

        /**
         * Matches.
         *
         * @param e
         *            the e
         * @return true, if successful
         */
        public abstract boolean matches(Event e);

        /**
         * Gets the result.
         *
         * @param srcEvent
         *            the src event
         * @return the result
         */
        public abstract String getResult(Event srcEvent);
    }

    /**
     * The Class ConstantValueSpec.
     */
    class ConstantValueSpec extends ValueSpec {

        /** The m_constant. */
        Value m_constant;

        /**
         * Instantiates a new constant value spec.
         *
         * @param constant
         *            the constant
         */
        public ConstantValueSpec(Value constant) {
            m_constant = constant;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.ValueSpec#matches(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public boolean matches(Event e) {
            if (m_constant.getMatches() != null) {
                LOG.warn("ConstantValueSpec.matches: matches not allowed for constant value.");
                throw new IllegalStateException("Illegal to use matches with constant type values");
            }
            return true;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.ValueSpec#getResult(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public String getResult(Event srcEvent) {
            return m_constant.getResult();
        }

    }

    /**
     * The Class ValueSpecUnspecified.
     */
    class ValueSpecUnspecified extends ValueSpec {

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.ValueSpec#matches(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public boolean matches(Event e) {
            // TODO: this should probably throw an exception since it makes no
            // sense
            return true;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.ValueSpec#getResult(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public String getResult(Event srcEvent) {
            return "value unspecified";
        }

    }

    /**
     * The Class SqlValueSpec.
     */
    class SqlValueSpec extends ValueSpec {

        /** The m_val. */
        Value m_val;

        /** The m_nested values. */
        List<ValueSpec> m_nestedValues;

        /**
         * Instantiates a new sql value spec.
         *
         * @param val
         *            the val
         */
        public SqlValueSpec(Value val) {
            m_val = val;
            m_nestedValues = null; // lazy init
        }

        /**
         * Gets the nested values.
         *
         * @return the nested values
         */
        public List<ValueSpec> getNestedValues() {
            if (m_nestedValues == null) {
                m_nestedValues = constructNestedValues();
            }
            return m_nestedValues;
        }

        /**
         * Construct nested values.
         *
         * @return the list
         */
        private List<ValueSpec> constructNestedValues() {
            List<ValueSpec> nestedValues = new ArrayList<ValueSpec>();
            for (Value val : m_val.getValueCollection()) {
                nestedValues.add(EventTranslatorConfigFactory.this.getValueSpec(val));
            }
            return nestedValues;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.ValueSpec#matches(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public boolean matches(Event e) {
            for (ValueSpec nestedVal : getNestedValues()) {
                if (!nestedVal.matches(e)) {
                    return false;
                }
            }

            Query query = createQuery(e);
            int rowCount = query.execute();

            if (rowCount < 1) {
                LOG.info("No results found for query {}. No match.", query.reproduceStatement());
                return false;
            }

            return true;
        }

        /**
         * The Class Query.
         */
        private class Query {

            /** The m_querier. */
            SingleResultQuerier m_querier;

            /** The m_args. */
            Object[] m_args;

            /**
             * Instantiates a new query.
             *
             * @param querier
             *            the querier
             * @param args
             *            the args
             */
            Query(SingleResultQuerier querier, Object[] args) {
                m_querier = querier;
                m_args = args;
            }

            /**
             * Gets the row count.
             *
             * @return the row count
             */
            public int getRowCount() {
                return m_querier.getCount();
            }

            /**
             * Execute.
             *
             * @return the int
             */
            public int execute() {
                m_querier.execute(m_args);
                return getRowCount();
            }

            /**
             * Reproduce statement.
             *
             * @return the string
             */
            public String reproduceStatement() {
                return m_querier.reproduceStatement(m_args);
            }

            /**
             * Gets the result.
             *
             * @return the result
             */
            public Object getResult() {
                return m_querier.getResult();
            }

        }

        /**
         * Creates the query.
         *
         * @param srcEvent
         *            the src event
         * @return the query
         */
        public Query createQuery(Event srcEvent) {
            Object[] args = new Object[getNestedValues().size()];
            SingleResultQuerier querier = new SingleResultQuerier(m_dbConnFactory, m_val.getResult());
            for (int i = 0; i < args.length; i++) {
                args[i] = (getNestedValues().get(i)).getResult(srcEvent);
            }

            return new Query(querier, args);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.ValueSpec#getResult(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public String getResult(Event srcEvent) {
            Query query = createQuery(srcEvent);
            query.execute();
            if (query.getRowCount() < 1) {
                LOG.info("No results found for query {}. Returning null", query.reproduceStatement());
                return null;
            } else {
                Object result = query.getResult();
                LOG.debug("getResult: result of single result querier is: {}", result);
                if (result != null) {
                    return result.toString();
                } else {
                    return null;
                }
            }
        }

    }

    /**
     * The Class AttributeValueSpec.
     */
    abstract class AttributeValueSpec extends ValueSpec {

        /** The m_val. */
        Value m_val;

        /**
         * Instantiates a new attribute value spec.
         *
         * @param val
         *            the val
         */
        AttributeValueSpec(Value val) {
            m_val = val;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.ValueSpec#matches(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public boolean matches(Event e) {

            String attributeValue = getAttributeValue(e);
            if (attributeValue == null) {
                LOG.debug("AttributeValueSpec.matches: Event attributeValue doesn't match because attributeValue itself is null");
                return false;
            }

            if (m_val.getMatches() == null) {
                LOG.debug("AttributeValueSpec.matches: Event attributeValue: {} matches because pattern is null",
                          attributeValue);
                return true;
            }

            Pattern p = Pattern.compile(m_val.getMatches());
            Matcher m = p.matcher(attributeValue);

            LOG.debug("AttributeValueSpec.matches: Event attributeValue: {} {} pattern: {}", attributeValue,
                      (m.matches() ? "matches" : "doesn't match"), m_val.getMatches());
            if (m.matches()) {
                return true;
            } else {
                return false;
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.ValueSpec#getResult(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public String getResult(Event srcEvent) {
            if (m_val.getMatches() == null) {
                return m_val.getResult();
            }

            String attributeValue = getAttributeValue(srcEvent);

            if (attributeValue == null) {
                throw new TranslationFailedException("failed to match null against '" + m_val.getMatches()
                        + "' for attribute " + getAttributeName());
            }

            Pattern p = Pattern.compile(m_val.getMatches());
            final Matcher m = p.matcher(attributeValue);
            if (!m.matches()) {
                throw new TranslationFailedException("failed to match " + attributeValue + " against '"
                        + m_val.getMatches() + "' for attribute " + getAttributeName());
            }

            MatchTable matches = new MatchTable(m);

            return PropertiesUtils.substitute(m_val.getResult(), matches);
        }

        /**
         * Gets the attribute name.
         *
         * @return the attribute name
         */
        public String getAttributeName() {
            return m_val.getName();
        }

        /**
         * Gets the attribute value.
         *
         * @param e
         *            the e
         * @return the attribute value
         */
        public abstract String getAttributeValue(Event e);
    }

    // XXX: This is here because Spring converting to a String appears
    // to be broken. It if probably a Hack and we probably need to have
    // a better way to access the Spring property editors and convert
    // to a string more correctly.
    /**
     * The Class StringPropertyEditor.
     */
    class StringPropertyEditor extends PropertyEditorSupport {

        /* (non-Javadoc)
         * @see java.beans.PropertyEditorSupport#setValue(java.lang.Object)
         */
        @Override
        public void setValue(Object value) {
            if (value == null || value instanceof String) {
                super.setValue(value);
            } else {
                super.setValue(value.toString());
            }
        }

        /* (non-Javadoc)
         * @see java.beans.PropertyEditorSupport#getAsText()
         */
        @Override
        public String getAsText() {
            return (String) super.getValue();
        }

        /* (non-Javadoc)
         * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
         */
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            super.setValue(text);
        }

    }

    /**
     * The Class FieldValueSpec.
     */
    class FieldValueSpec extends AttributeValueSpec {

        /**
         * Instantiates a new field value spec.
         *
         * @param val
         *            the val
         */
        public FieldValueSpec(Value val) {
            super(val);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.AttributeValueSpec#getAttributeValue(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public String getAttributeValue(Event e) {
            try {
                BeanWrapper bean = getBeanWrapper(e);

                return (String) bean.convertIfNecessary(bean.getPropertyValue(getAttributeName()), String.class);
            } catch (FatalBeanException ex) {
                LOG.error("Property {} does not exist on Event", ex, getAttributeName());
                throw new TranslationFailedException("Property " + getAttributeName() + " does not exist on Event");
            }
        }

        /**
         * Gets the bean wrapper.
         *
         * @param e
         *            the e
         * @return the bean wrapper
         */
        private BeanWrapper getBeanWrapper(Event e) {
            BeanWrapper bean = PropertyAccessorFactory.forBeanPropertyAccess(e);
            bean.registerCustomEditor(String.class, new StringPropertyEditor());
            return bean;
        }
    }

    /**
     * The Class ParameterValueSpec.
     */
    class ParameterValueSpec extends AttributeValueSpec {

        /**
         * Instantiates a new parameter value spec.
         *
         * @param val
         *            the val
         */
        ParameterValueSpec(Value val) {
            super(val);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.EventTranslatorConfigFactory.AttributeValueSpec#getAttributeValue(org.opennms.netmgt.xml.event.Event)
         */
        @Override
        public String getAttributeValue(Event e) {

            String attrName = getAttributeName();
            for (Parm parm : e.getParmCollection()) {

                if (parm.getParmName().equals(attrName)) {
                    LOG.debug("getAttributeValue: eventParm name: '{} equals translation parameter name: ' {}",
                              attrName, parm.getParmName());
                    return (parm.getValue() == null ? "" : parm.getValue().getContent());
                }

                String trimmedAttrName = StringUtils.removeStart(attrName, "~");

                if (attrName.startsWith("~") && (parm.getParmName().matches(trimmedAttrName))) {
                    LOG.debug("getAttributeValue: eventParm name: '{} matches translation parameter name expression: ' {}",
                              trimmedAttrName, parm.getParmName());
                    return (parm.getValue() == null ? "" : parm.getValue().getContent());
                }
            }
            return null;
        }
    }

}
