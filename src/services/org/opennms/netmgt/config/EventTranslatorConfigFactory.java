//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2002-2003 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.                                                            
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//       
// For more information contact: 
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//
// Tab Size = 8
//

package org.opennms.netmgt.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Category;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.PropertiesUtils;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.ConfigFileConstants;
import org.opennms.netmgt.config.translator.Assignment;
import org.opennms.netmgt.config.translator.EventTranslationSpec;
import org.opennms.netmgt.config.translator.EventTranslatorConfiguration;
import org.opennms.netmgt.config.translator.Mapping;
import org.opennms.netmgt.config.translator.Value;
import org.opennms.netmgt.eventd.EventUtil;
import org.opennms.netmgt.utils.SingleResultQuerier;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Parm;
import org.opennms.netmgt.xml.event.Parms;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.FatalBeanException;

/**
 * This is the singleton class used to load the configuration from the
 * passive-status-configuration.xml. This provides convenience methods to get the configured
 * categories and their information, add/delete categories from category groups.
 * 
 * <strong>Note: </strong>Users of this class should make sure the
 * <em>init()</em> is called before calling any other method to ensure the
 * config is loaded before accessing other convenience methods.
 * 
 * @author <a href="mailto:david@opennms.org">David Hustace </a>
 * @author <a href="http://www.opennms.org/">OpenNMS </a>
 */
public final class EventTranslatorConfigFactory implements EventTranslatorConfig {
    /**
     * The singleton instance of this factory
     */
    private static EventTranslatorConfig m_singleton = null;

    /**
     * The config class loaded from the config file
     */
    private EventTranslatorConfiguration m_config;

	private List m_translationSpecs;
	
    /**
     * This member is set to true if the configuration file has been loaded.
     */
    private static boolean m_loaded = false;

    /**
     * connection factory for use with sql-value
     */
	private DbConnectionFactory m_dbConnFactory;

    
    /**
     * Private constructor
     * 
     * @exception java.io.IOException
     *                Thrown if the specified config file cannot be read
     * @exception org.exolab.castor.xml.MarshalException
     *                Thrown if the file does not conform to the schema.
     * @exception org.exolab.castor.xml.ValidationException
     *                Thrown if the contents do not match the required schema.
     * 
     */
    private EventTranslatorConfigFactory(String configFile, DbConnectionFactory dbConnFactory) throws IOException, MarshalException, ValidationException {
        Reader rdr = new InputStreamReader(new FileInputStream(configFile));
        marshallReader(rdr, dbConnFactory);
        rdr.close();
    }
    
    public EventTranslatorConfigFactory(Reader rdr, DbConnectionFactory dbConnFactory) throws MarshalException, ValidationException {
        marshallReader(rdr, dbConnFactory);
    }
    
    private synchronized void marshallReader(Reader rdr, DbConnectionFactory dbConnFactory) throws MarshalException, ValidationException {
        m_config = (EventTranslatorConfiguration) Unmarshaller.unmarshal(EventTranslatorConfiguration.class, rdr);
        m_dbConnFactory = dbConnFactory;
    }

    /**
     * Load the config from the default config file and create the singleton
     * instance of this factory.
     * 
     * @exception java.io.IOException
     *                Thrown if the specified config file cannot be read
     * @exception org.exolab.castor.xml.MarshalException
     *                Thrown if the file does not conform to the schema.
     * @exception org.exolab.castor.xml.ValidationException
     *                Thrown if the contents do not match the required schema.
     * @throws ClassNotFoundException 
     */
    public static synchronized void init() throws IOException, MarshalException, ValidationException, ClassNotFoundException {
        if (m_loaded) {
            // init already called - return
            // to reload, reload() will need to be called
            return;
        }
        
        DatabaseConnectionFactory.init();

        File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.PASSIVE_CONFIG_FILE_NAME);

        m_singleton = new EventTranslatorConfigFactory(cfgFile.getPath(), DatabaseConnectionFactory.getInstance());

        m_loaded = true;
    }

    /**
     * Reload the config from the default config file
     * 
     * @exception java.io.IOException
     *                Thrown if the specified config file cannot be read/loaded
     * @exception org.exolab.castor.xml.MarshalException
     *                Thrown if the file does not conform to the schema.
     * @exception org.exolab.castor.xml.ValidationException
     *                Thrown if the contents do not match the required schema.
     * @throws ClassNotFoundException 
     */
    public static synchronized void reload() throws IOException, MarshalException, ValidationException, ClassNotFoundException {
        m_singleton = null;
        m_loaded = false;

        init();
    }

    /**
     * Return the singleton instance of this factory.
     * 
     * @return The current factory instance.
     * 
     * @throws java.lang.IllegalStateException
     *             Thrown if the factory has not yet been initialized.
     */
    public static synchronized EventTranslatorConfig getInstance() {
        if (!m_loaded)
            throw new IllegalStateException("getInstance: The factory has not been initialized");

        return m_singleton;
    }

	public static void setInstance(EventTranslatorConfig singleton) {
		m_singleton=singleton;
		m_loaded=true;
	}
	
	private Category log() {
		return ThreadCategory.getInstance(EventTranslatorConfig.class);
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
     *  (non-Javadoc)
     * @see org.opennms.netmgt.config.PassiveStatusConfig#getUEIList()
     */
    public List getUEIList() {
    		return getTranslationUEIs();
    }

    private List getTranslationUEIs() {
		List translatedEvents = getConfig().getTranslation().getEventTranslationSpecCollection();
		List ueis = new ArrayList();
		for (Iterator it = translatedEvents.iterator(); it.hasNext();) {
			EventTranslationSpec event = (EventTranslationSpec) it.next();
			ueis.add(event.getUei());
		}
		return ueis;
	}
    
	static class TranslationFailedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		TranslationFailedException(String msg) {
    			super(msg);
    		}
    }

    public boolean isTranslationEvent(Event e) {
    	
		List specs = getTranslationSpecs();
		for (Iterator it = specs.iterator(); it.hasNext();) {
			TranslationSpec spec = (TranslationSpec) it.next();
			if (spec.matches(e))
				return true;
		}
		return false;
    }
    
	public List translateEvent(Event e) {
		ArrayList events = new ArrayList();
		for (Iterator it = getTranslationSpecs().iterator(); it.hasNext();) {
			TranslationSpec spec = (TranslationSpec) it.next();
			events.addAll(spec.translate(e));
		}
		return events;
	}
	
	private List getTranslationSpecs() {
		if (m_translationSpecs == null)
			m_translationSpecs = constructTranslationSpecs();
		
		return m_translationSpecs;
	}

	private List constructTranslationSpecs() {
		List specs = new ArrayList();
		for (Iterator it = m_config.getTranslation().getEventTranslationSpecCollection().iterator(); it.hasNext();) {
			EventTranslationSpec eventTrans = (EventTranslationSpec) it.next();
			specs.add(new TranslationSpec(eventTrans));
		}
		return specs;
	}
	
	class TranslationSpec {
		private EventTranslationSpec m_spec;
		private List m_translationMappings;
		TranslationSpec(EventTranslationSpec spec) {
			m_spec = spec;
			m_translationMappings = null; // lazy init
		}
		public List translate(Event e) {
			// short circuit here is the uei doesn't match
			if (!ueiMatches(e)) return Collections.EMPTY_LIST;
			
			// uei matches now go thru the mappings
			ArrayList events = new ArrayList();
			for (Iterator it = getTranslationMappings().iterator(); it.hasNext();) {
				TranslationMapping mapping = (TranslationMapping) it.next();
				Event translatedEvent = mapping.translate(e);
				if (translatedEvent != null)
					events.add(translatedEvent);
			}
			
			return events;
		}
		String getUei() { return m_spec.getUei(); }
		public EventTranslationSpec getEventTranslationSpec() {
			return m_spec;
		}
		
		private List constructTranslationMappings() {
			if (m_spec.getMappings() == null) return Collections.EMPTY_LIST;

			List mappings = m_spec.getMappings().getMappingCollection();
			
			List transMaps = new ArrayList(mappings.size());
			for (Iterator it = mappings.iterator(); it.hasNext();) {
				Mapping mapping = (Mapping) it.next();
				TranslationMapping transMap = new TranslationMapping(mapping);
				transMaps.add(transMap);
			}
			
			return transMaps;
		}
		
		List getTranslationMappings() {
			if (m_translationMappings == null)
				m_translationMappings = constructTranslationMappings();
			return m_translationMappings;
		}
		boolean matches(Event e) {
			// short circuit if the eui doesn't match
			if (!ueiMatches(e)) return false;
			
			// uei matches to go thru the mappings
			List transMaps = getTranslationMappings();
			for (Iterator it = transMaps.iterator(); it.hasNext();) {
				TranslationMapping transMap = (TranslationMapping) it.next();
				if (transMap.matches(e))
					return true;
			}
			return false;
		}
		
		private boolean ueiMatches(Event e) {
			return m_spec.getUei().equals(e.getUei());
		}
		
		
	}
	
	class TranslationMapping {
		Mapping m_mapping;
		List m_assignments;
		TranslationMapping(Mapping mapping) { 
			m_mapping = mapping;
			m_assignments = null; // lazy init
		}
		
		public Event translate(Event srcEvent) {
			// if the event doesn't match the mapping then don't apply the translation
			if (!matches(srcEvent)) return null;
			
			Event targetEvent = EventUtil.cloneEvent(srcEvent);
			
			for (Iterator it = getAssignmentSpecs().iterator(); it.hasNext();) {
				AssignmentSpec assignSpec = (AssignmentSpec) it.next();
				assignSpec.apply(srcEvent, targetEvent);
			}
			
			targetEvent.setSource(TRANSLATOR_NAME);
			return targetEvent;
		}

		public Mapping getMapping() {
			return m_mapping;
		}
		
		private List getAssignmentSpecs() {
			if (m_assignments == null)
				m_assignments = constructAssignmentSpecs();
			return m_assignments;
		}
		
		private List constructAssignmentSpecs() {
			Mapping mapping = getMapping();
			List assignments = new ArrayList();
			for (Iterator iter = mapping.getAssignmentCollection().iterator(); iter.hasNext();) {
				Assignment assign = (Assignment) iter.next();
				
				AssignmentSpec assignSpec = 
					("parameter".equals(assign.getType()) ? 
							(AssignmentSpec)new ParameterAssignmentSpec(assign) :
							(AssignmentSpec)new FieldAssignmentSpec(assign)
							);
				assignments.add(assignSpec);
			}
			return assignments;
		}
		
		private boolean assignmentsMatch(Event e) {
			for (Iterator it = getAssignmentSpecs().iterator(); it.hasNext();) {
				AssignmentSpec assignSpec = (AssignmentSpec) it.next();
			
				if (!assignSpec.matches(e))
					return false;
			}	
			return true;
		}
		boolean matches(Event e) {
			return assignmentsMatch(e);
		}
	}
	
	abstract class AssignmentSpec {
		private Assignment m_assignment;
		private ValueSpec m_valueSpec;
		AssignmentSpec(Assignment assignment) {
			m_assignment = assignment; 
			m_valueSpec = null; // lazy init
		}
		
		public void apply(Event srcEvent, Event targetEvent) {
			setValue(targetEvent, getValueSpec().getResult(srcEvent));
		}
		
		private Assignment getAssignment() { return m_assignment; }
		
		protected String getAttributeName() { return getAssignment().getName(); }

		private ValueSpec constructValueSpec() {
			Value val = getAssignment().getValue();
			
			
			return EventTranslatorConfigFactory.this.getValueSpec(val);
		}

		protected abstract void setValue(Event targetEvent, String value);
		
		private ValueSpec getValueSpec() {
			if (m_valueSpec == null)
				m_valueSpec = constructValueSpec();
			return m_valueSpec;
		}
		boolean matches(Event e) {
			return getValueSpec().matches(e);
		}
	}
	
	class FieldAssignmentSpec extends AssignmentSpec {
		FieldAssignmentSpec(Assignment field) { super(field); }
		
		protected void setValue(Event targetEvent, String value) {
			try {
				BeanWrapperImpl bean = new BeanWrapperImpl(targetEvent);
				bean.setPropertyValue(getAttributeName(), value);
			} catch(FatalBeanException e) {
				log().error("Unable to set value for attribute "+getAttributeName()+"to value "+value);
				throw new TranslationFailedException("Unable to set value for attribute "+getAttributeName()+"to value "+value);
			}
		}
		
	}
	
	class ParameterAssignmentSpec extends AssignmentSpec {
		ParameterAssignmentSpec(Assignment assign) {
			super(assign);
		}

		protected void setValue(Event targetEvent, String value) {
			Parms parms = targetEvent.getParms();
			if (parms == null) {
				parms = new Parms();
				targetEvent.setParms(parms);
			}
				
			for (Iterator it = parms.getParmCollection().iterator(); it.hasNext();) {
				Parm parm = (Parm) it.next();
				if (parm.getParmName().equals(getAttributeName())) {
					org.opennms.netmgt.xml.event.Value val = parm.getValue();
					if (val == null) {
						val = new org.opennms.netmgt.xml.event.Value();
						parm.setValue(val);
					}
					log().debug("Overriding value of parameter "+getAttributeName()+". Setting it to "+value);
					val.setContent(value);
					return;
				}
			}
			
			// if we got here then we didn't find the existing parm
			Parm newParm = new Parm();
			parms.addParm(newParm);
			newParm.setParmName(getAttributeName());
			org.opennms.netmgt.xml.event.Value val = new org.opennms.netmgt.xml.event.Value();
			newParm.setValue(val);
			log().debug("Setting value of parameter "+getAttributeName()+" to "+value);
			val.setContent(value);
			
			
		}
	}
	
	ValueSpec getValueSpec(Value val) {
		if ("field".equals(val.getType()))
			return new FieldValueSpec(val);
		else if ("parameter".equals(val.getType()))
			return new ParameterValueSpec(val);
		else if ("constant".equals(val.getType()))
			return new ConstantValueSpec(val);
		else if ("sql".equals(val.getType()))
			return new SqlValueSpec(val);
		else
			return new ValueSpecUnspecified();
	}
	

	abstract class ValueSpec {

		public abstract boolean matches(Event e);
		
		public abstract String getResult(Event srcEvent);
	}
	
	class ConstantValueSpec extends ValueSpec {
		
		Value m_constant;

		public ConstantValueSpec(Value constant) {
			m_constant = constant;
		}
		

		public boolean matches(Event e) {
			if (m_constant.getMatches() != null)
				throw new IllegalStateException("Illegal to use matches with constant type values");
			return true;
		}


		public String getResult(Event srcEvent) {
			return m_constant.getResult();
		}

	}

	class ValueSpecUnspecified extends ValueSpec {
		
		public boolean matches(Event e) {
			// TODO: this should probably throw an exception since it makes no sense
			return true;
		}

		public String getResult(Event srcEvent) {
			return "value unspecified";
		}

	}
	
	class SqlValueSpec extends ValueSpec {
		Value m_val;
		List m_nestedValues;
		public SqlValueSpec(Value val) {
			m_val = val;
			m_nestedValues = null; // lazy init
		}
		
		public List getNestedValues() {
			if (m_nestedValues == null)
				m_nestedValues = constructNestedValues();
			return m_nestedValues;
		}

		private List constructNestedValues() {
			List nestedValues = new ArrayList();
			for (Iterator it = m_val.getValueCollection().iterator(); it.hasNext();) {
				Value val = (Value) it.next();
				nestedValues.add(EventTranslatorConfigFactory.this.getValueSpec(val));
			}
			return nestedValues;
		}

		public boolean matches(Event e) {
			for (Iterator it = getNestedValues().iterator(); it.hasNext();) {
				ValueSpec nestedVal = (ValueSpec) it.next();
				if (!nestedVal.matches(e))
					return false;
			}
			return true;
		}

		public String getResult(Event srcEvent) {
			SingleResultQuerier querier = new SingleResultQuerier(m_dbConnFactory, m_val.getResult());
			Object[] args = new Object[getNestedValues().size()];
			for (int i = 0; i < args.length; i++) {
				args[i] = ((ValueSpec)getNestedValues().get(i)).getResult(srcEvent);
			}
			querier.execute(args);
			if (querier.getCount() < 1) {
				log().warn("No results found for query "+querier.reproduceStatement(args)+". Returning null");
				return null;
			}
			else
				return querier.getResult().toString();
		}
		
	}

	abstract class AttributeValueSpec extends ValueSpec {
		Value m_val;
		AttributeValueSpec(Value val) { m_val = val; }

		public boolean matches(Event e) {
			
			String attributeValue = getAttributeValue(e);
			if (attributeValue == null) return false;

			if (m_val.getMatches() == null) return true;

			Pattern p = Pattern.compile(m_val.getMatches());
			Matcher m = p.matcher(attributeValue);
			
			return m.matches();
		}

		public String getResult(Event srcEvent) {
			if (m_val.getMatches() == null) return m_val.getResult();

			String attributeValue = getAttributeValue(srcEvent);

			if (attributeValue == null) {
				throw new TranslationFailedException("failed to match null against '"+m_val.getMatches()+"' for attribute "+getAttributeName());
			}

			Pattern p = Pattern.compile(m_val.getMatches());
			final Matcher m = p.matcher(attributeValue);
			if (!m.matches())
				throw new TranslationFailedException("failed to match "+attributeValue+" against '"+m_val.getMatches()+"' for attribute "+getAttributeName());
			
			class MatchTable implements PropertiesUtils.SymbolTable {

				public String getSymbolValue(String symbol) {
					try {
						int groupNum = Integer.parseInt(symbol);
						if (groupNum > m.groupCount())
							return null;
						return m.group(groupNum);
					} catch (NumberFormatException e) {
						return null;
					}
				}
				
			};
			MatchTable matches = new MatchTable();

			return PropertiesUtils.substitute(m_val.getResult(), matches);
		}
		
		public String getAttributeName() { return m_val.getName(); }


		abstract public String getAttributeValue(Event e);
	}
	
	class FieldValueSpec extends AttributeValueSpec {
		public FieldValueSpec(Value val) {
			super(val);
		}

		public String getAttributeValue(Event e) {
			try {
				BeanWrapperImpl bean = new BeanWrapperImpl(e);
				return (String)bean.doTypeConversionIfNecessary(bean.getPropertyValue(getAttributeName()), String.class);
			} catch (FatalBeanException ex) {
				log().error("Property "+getAttributeName()+" does not exist on Event", ex);
				throw new TranslationFailedException("Property "+getAttributeName()+" does not exist on Event");
			}
		}
	}
	
	class ParameterValueSpec extends AttributeValueSpec {
		ParameterValueSpec(Value val) { super(val); }
		
		public String getAttributeValue(Event e) {
			
			String attrName = getAttributeName();
			Parms parms = e.getParms();
			if (parms == null) return null;
			
			for (Iterator it = parms.getParmCollection().iterator(); it.hasNext();) {
				Parm parm = (Parm) it.next();
				if (parm.getParmName().equals(attrName))
					return (parm.getValue() == null ? "" : parm.getValue().getContent());
					
			}
			return null;
		}
	}
	


}
