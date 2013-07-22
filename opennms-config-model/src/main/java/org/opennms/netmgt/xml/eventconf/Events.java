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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.opennms.core.xml.JaxbUtils;
import org.opennms.core.xml.ValidateUsing;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.util.StringUtils;

/**
 * The Class Events.
 */
@XmlRootElement(name = "events")
@XmlAccessorType(XmlAccessType.FIELD)
@ValidateUsing("eventconf.xsd")
@XmlType(propOrder = {})
public class Events implements Serializable {

    /**
     * The Interface EventCallback.
     *
     * @param <T>
     *            the generic type
     */
    public interface EventCallback<T> {

        /**
         * Process.
         *
         * @param accum
         *            the accum
         * @param event
         *            the event
         * @return the t
         */
        public T process(T accum, Event event);

    }

    /**
     * The Interface EventCriteria.
     */
    public interface EventCriteria {

        /**
         * Matches.
         *
         * @param e
         *            the e
         * @return true, if successful
         */
        public boolean matches(Event e);

    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3725006529763434264L;

    /** The Constant EMPTY_STRING_ARRAY. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** The Constant EMPTY_EVENT_ARRAY. */
    private static final Event[] EMPTY_EVENT_ARRAY = new Event[0];

    /** Global settings for this configuration. */
    @XmlElement(name = "global", required = false)
    private Global m_global;

    // @Size(min=1)
    /** The m_events. */
    @XmlElement(name = "event", required = true)
    private List<Event> m_events = new ArrayList<Event>();

    // @Size(min=0)
    /** The m_event files. */
    @XmlElement(name = "event-file", required = false)
    private List<String> m_eventFiles = new ArrayList<String>();

    /** The m_loaded event files. */
    @XmlTransient
    private Map<String, Events> m_loadedEventFiles = new LinkedHashMap<String, Events>();

    /** The m_partition. */
    @XmlTransient
    private Partition m_partition;

    /** The m_partitioned events. */
    @XmlTransient
    private Map<String, List<Event>> m_partitionedEvents;

    /** The m_null partitioned events. */
    @XmlTransient
    private List<Event> m_nullPartitionedEvents;

    /**
     * Adds the event.
     *
     * @param event
     *            the event
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addEvent(final Event event) throws IndexOutOfBoundsException {
        m_events.add(event);
    }

    /**
     * Adds the event.
     *
     * @param index
     *            the index
     * @param event
     *            the event
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addEvent(final int index, final Event event) throws IndexOutOfBoundsException {
        m_events.add(index, event);
    }

    /**
     * Adds the event file.
     *
     * @param eventFile
     *            the event file
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addEventFile(final String eventFile) throws IndexOutOfBoundsException {
        m_eventFiles.add(eventFile.intern());
    }

    /**
     * Adds the event file.
     *
     * @param index
     *            the index
     * @param eventFile
     *            the event file
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void addEventFile(final int index, final String eventFile) throws IndexOutOfBoundsException {
        m_eventFiles.add(index, eventFile.intern());
    }

    /**
     * Enumerate event.
     *
     * @return the enumeration
     */
    public Enumeration<Event> enumerateEvent() {
        return Collections.enumeration(m_events);
    }

    /**
     * Enumerate event file.
     *
     * @return the enumeration
     */
    public Enumeration<String> enumerateEventFile() {
        return Collections.enumeration(m_eventFiles);
    }

    /**
     * Gets the event.
     *
     * @param index
     *            the index
     * @return the event
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public Event getEvent(final int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_events.size()) {
            throw new IndexOutOfBoundsException("getEvent: Index value '" + index + "' not in range [0.."
                    + (m_events.size() - 1) + "]");
        }
        return m_events.get(index);
    }

    /**
     * Gets the event.
     *
     * @return the event
     */
    public Event[] getEvent() {
        return m_events.toArray(EMPTY_EVENT_ARRAY);
    }

    /**
     * Gets the event collection.
     *
     * @return the event collection
     */
    public List<Event> getEventCollection() {
        return m_events;
    }

    /**
     * Gets the event count.
     *
     * @return the event count
     */
    public int getEventCount() {
        return m_events.size();
    }

    /**
     * Gets the event file.
     *
     * @param index
     *            the index
     * @return the event file
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public String getEventFile(final int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_eventFiles.size()) {
            throw new IndexOutOfBoundsException("getEventFile: Index value '" + index + "' not in range [0.."
                    + (m_eventFiles.size() - 1) + "]");
        }
        return m_eventFiles.get(index);
    }

    /**
     * Gets the event file.
     *
     * @return the event file
     */
    public String[] getEventFile() {
        return m_eventFiles.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Gets the event file collection.
     *
     * @return the event file collection
     */
    public List<String> getEventFileCollection() {
        return m_eventFiles;
    }

    /**
     * Gets the event file count.
     *
     * @return the event file count
     */
    public int getEventFileCount() {
        return m_eventFiles.size();
    }

    /**
     * Gets the global.
     *
     * @return the global
     */
    public Global getGlobal() {
        return m_global;
    }

    /**
     * Checks if is valid.
     *
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
        return true;
    }

    /**
     * Iterate event.
     *
     * @return the iterator
     */
    public Iterator<Event> iterateEvent() {
        return m_events.iterator();
    }

    /**
     * Iterate event file.
     *
     * @return the iterator
     */
    public Iterator<String> iterateEventFile() {
        return m_eventFiles.iterator();
    }

    /**
     * Marshal.
     *
     * @param out
     *            the out
     */
    public void marshal(final Writer out) {
        JaxbUtils.marshal(this, out);
    }

    /**
     * Removes the all event.
     */
    public void removeAllEvent() {
        m_events.clear();
    }

    /**
     * Removes the all event file.
     */
    public void removeAllEventFile() {
        m_eventFiles.clear();
    }

    /**
     * Removes the event.
     *
     * @param event
     *            the event
     * @return true, if successful
     */
    public boolean removeEvent(final Event event) {
        return m_events.remove(event);
    }

    /**
     * Removes the event at.
     *
     * @param index
     *            the index
     * @return the event
     */
    public Event removeEventAt(final int index) {
        return m_events.remove(index);
    }

    /**
     * Removes the event file.
     *
     * @param eventFile
     *            the event file
     * @return true, if successful
     */
    public boolean removeEventFile(final String eventFile) {
        return m_eventFiles.remove(eventFile);
    }

    /**
     * Removes the event file at.
     *
     * @param index
     *            the index
     * @return the string
     */
    public String removeEventFileAt(final int index) {
        return m_eventFiles.remove(index);
    }

    /**
     * Sets the event.
     *
     * @param index
     *            the index
     * @param event
     *            the event
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setEvent(final int index, final Event event) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_events.size()) {
            throw new IndexOutOfBoundsException("setEvent: Index value '" + index + "' not in range [0.."
                    + (m_events.size() - 1) + "]");
        }
        m_events.set(index, event);
    }

    /**
     * Sets the event.
     *
     * @param events
     *            the new event
     */
    public void setEvent(final Event[] events) {
        m_events.clear();
        for (final Event event : events) {
            m_events.add(event);
        }
    }

    /**
     * Sets the event.
     *
     * @param events
     *            the new event
     */
    public void setEvent(final List<Event> events) {
        if (m_events == events) {
            return;
        }
        m_events.clear();
        m_events.addAll(events);
    }

    /**
     * Sets the event collection.
     *
     * @param events
     *            the new event collection
     */
    public void setEventCollection(final List<Event> events) {
        setEvent(events);
    }

    /**
     * Sets the event file.
     *
     * @param index
     *            the index
     * @param eventFile
     *            the event file
     * @throws IndexOutOfBoundsException
     *             the index out of bounds exception
     */
    public void setEventFile(final int index, final String eventFile) throws IndexOutOfBoundsException {
        if (index < 0 || index >= m_eventFiles.size()) {
            throw new IndexOutOfBoundsException("setEventFile: Index value '" + index + "' not in range [0.."
                    + (m_eventFiles.size() - 1) + "]");
        }
        m_eventFiles.set(index, eventFile.intern());
    }

    /**
     * Sets the event file.
     *
     * @param eventFiles
     *            the new event file
     */
    public void setEventFile(final String[] eventFiles) {
        m_eventFiles.clear();
        for (final String eventFile : eventFiles) {
            m_eventFiles.add(eventFile.intern());
        }
    }

    /**
     * Sets the event file.
     *
     * @param eventFiles
     *            the new event file
     */
    public void setEventFile(final List<String> eventFiles) {
        if (m_eventFiles == eventFiles) {
            return;
        }
        m_eventFiles.clear();
        m_eventFiles.addAll(eventFiles);
    }

    /**
     * Sets the event file collection.
     *
     * @param eventFiles
     *            the new event file collection
     */
    public void setEventFileCollection(final List<String> eventFiles) {
        setEventFile(eventFiles);
    }

    /**
     * Sets the global.
     *
     * @param global
     *            the new global
     */
    public void setGlobal(final Global global) {
        m_global = global;
    }

    /**
     * Unmarshal.
     *
     * @param reader
     *            the reader
     * @return the events
     */
    public static Events unmarshal(final Reader reader) {
        return JaxbUtils.unmarshal(Events.class, reader);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_eventFiles == null) ? 0 : m_eventFiles.hashCode());
        result = prime * result + ((m_events == null) ? 0 : m_events.hashCode());
        result = prime * result + ((m_global == null) ? 0 : m_global.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Events)) {
            return false;
        }
        final Events other = (Events) obj;
        if (m_eventFiles == null) {
            if (other.m_eventFiles != null) {
                return false;
            }
        } else if (!m_eventFiles.equals(other.m_eventFiles)) {
            return false;
        }
        if (m_events == null) {
            if (other.m_events != null) {
                return false;
            }
        } else if (!m_events.equals(other.m_events)) {
            return false;
        }
        if (m_global == null) {
            if (other.m_global != null) {
                return false;
            }
        } else if (!m_global.equals(other.m_global)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the relative.
     *
     * @param baseRef
     *            the base ref
     * @param relative
     *            the relative
     * @return the relative
     */
    Resource getRelative(Resource baseRef, String relative) {
        try {
            if (relative.startsWith("classpath:")) {
                DefaultResourceLoader loader = new DefaultResourceLoader();
                return loader.getResource(relative);
            } else {
                return baseRef.createRelative(relative);
            }
        } catch (final IOException e) {
            throw new ObjectRetrievalFailureException(
                                                      Resource.class,
                                                      baseRef,
                                                      "Resource location has a relative path, however the configResource does not reference a file, so the relative path cannot be resolved.  The location is: "
                                                              + relative, null);
        }

    }

    /**
     * Load event files.
     *
     * @param configResource
     *            the config resource
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public void loadEventFiles(Resource configResource) throws IOException {
        m_loadedEventFiles.clear();

        for (String eventFile : m_eventFiles) {
            Resource eventResource = getRelative(configResource, eventFile);
            Events events = JaxbUtils.unmarshal(Events.class, eventResource);
            if (events.getEventCount() <= 0) {
                throw new IllegalStateException("Uh oh! An event file " + eventResource.getFile()
                        + " with no events has been laoded!");
            }
            if (events.getGlobal() != null) {
                throw new ObjectRetrievalFailureException(Resource.class, eventResource, "The event resource "
                        + eventResource
                        + " included from the root event configuration file cannot have a 'global' element", null);
            }
            if (events.getEventFileCollection().size() > 0) {
                throw new ObjectRetrievalFailureException(Resource.class, eventResource, "The event resource "
                        + eventResource
                        + " included from the root event configuration file cannot include other configuration files: "
                        + StringUtils.collectionToCommaDelimitedString(events.getEventFileCollection()), null);
            }

            m_loadedEventFiles.put(eventFile, events);
        }
    }

    /**
     * Checks if is secure tag.
     *
     * @param tag
     *            the tag
     * @return true, if is secure tag
     */
    public boolean isSecureTag(String tag) {
        return m_global == null ? false : m_global.isSecureTag(tag);
    }

    /**
     * Partition events.
     *
     * @param partition
     *            the partition
     */
    private void partitionEvents(Partition partition) {
        m_partition = partition;

        m_partitionedEvents = new LinkedHashMap<String, List<Event>>();
        m_nullPartitionedEvents = new ArrayList<Event>();

        for (Event event : m_events) {
            List<String> keys = partition.group(event);
            if (keys == null) {
                m_nullPartitionedEvents.add(event);
            } else {
                for (String key : keys) {
                    List<Event> events = m_partitionedEvents.get(key);
                    if (events == null) {
                        events = new ArrayList<Event>(1);
                        m_partitionedEvents.put(key, events);
                    }
                    events.add(event);
                }
            }
        }

    }

    /**
     * Find first matching event.
     *
     * @param matchingEvent
     *            the matching event
     * @return the event
     */
    public Event findFirstMatchingEvent(org.opennms.netmgt.xml.event.Event matchingEvent) {
        String key = m_partition.group(matchingEvent);
        if (key != null) {
            List<Event> events = m_partitionedEvents.get(key);
            if (events != null) {
                for (Event event : events) {
                    if (event.matches(matchingEvent)) {
                        return event;
                    }
                }
            }
        }

        for (Event event : m_nullPartitionedEvents) {
            if (event.matches(matchingEvent)) {
                return event;
            }
        }

        for (Entry<String, Events> loadedEvents : m_loadedEventFiles.entrySet()) {
            Events subEvents = loadedEvents.getValue();
            Event event = subEvents.findFirstMatchingEvent(matchingEvent);
            if (event != null) {
                return event;
            }
        }

        return null;
    }

    /**
     * Find first matching event.
     *
     * @param criteria
     *            the criteria
     * @return the event
     */
    public Event findFirstMatchingEvent(EventCriteria criteria) {
        for (Event event : m_events) {
            if (criteria.matches(event)) {
                return event;
            }
        }

        for (Entry<String, Events> loadedEvents : m_loadedEventFiles.entrySet()) {
            Events events = loadedEvents.getValue();
            Event result = events.findFirstMatchingEvent(criteria);
            if (result != null) {
                return result;
            }
        }

        return null;

    }

    /**
     * For each event.
     *
     * @param <T>
     *            the generic type
     * @param initial
     *            the initial
     * @param callback
     *            the callback
     * @return the t
     */
    public <T> T forEachEvent(T initial, EventCallback<T> callback) {
        T result = initial;
        for (Event event : m_events) {
            result = callback.process(result, event);
        }

        for (Entry<String, Events> loadedEvents : m_loadedEventFiles.entrySet()) {
            Events events = loadedEvents.getValue();
            result = events.forEachEvent(result, callback);
        }

        return result;
    }

    /**
     * Initialize.
     *
     * @param partition
     *            the partition
     */
    public void initialize(Partition partition) {
        for (Event event : m_events) {
            event.initialize();
        }

        partitionEvents(partition);

        for (Entry<String, Events> loadedEvents : m_loadedEventFiles.entrySet()) {
            Events events = loadedEvents.getValue();
            events.initialize(partition);
        }

    }

    /**
     * Gets the load events by file.
     *
     * @param relativePath
     *            the relative path
     * @return the load events by file
     */
    public Events getLoadEventsByFile(String relativePath) {
        return m_loadedEventFiles.get(relativePath);
    }

    /**
     * Adds the loaded event file.
     *
     * @param relativePath
     *            the relative path
     * @param events
     *            the events
     */
    public void addLoadedEventFile(String relativePath, Events events) {
        m_eventFiles.add(relativePath);
        m_loadedEventFiles.put(relativePath, events);
    }

    /**
     * Removes the loaded event file.
     *
     * @param relativePath
     *            the relative path
     */
    public void removeLoadedEventFile(String relativePath) {
        m_eventFiles.remove(relativePath);
        m_loadedEventFiles.remove(relativePath);
    }

    /**
     * Save events.
     *
     * @param resource
     *            the resource
     */
    public void saveEvents(Resource resource) {
        final StringWriter stringWriter = new StringWriter();
        JaxbUtils.marshal(this, stringWriter);

        if (stringWriter.toString() != null) {
            File file;
            try {
                file = resource.getFile();
            } catch (final IOException e) {
                throw new DataAccessResourceFailureException("Event resource '" + resource
                        + "' is not a file resource and cannot be saved.  Nested exception: " + e, e);
            }

            Writer fileWriter = null;
            try {
                try {
                    fileWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                } catch (final IOException e) {
                    throw new DataAccessResourceFailureException("Event file '" + file
                            + "' could not be opened.  Nested exception: " + e, e);
                }

                try {
                    fileWriter.write(stringWriter.toString());
                } catch (final IOException e) {
                    throw new DataAccessResourceFailureException("Event file '" + file
                            + "' could not be written to.  Nested exception: " + e, e);
                }

                try {
                    fileWriter.close();
                } catch (final IOException e) {
                    throw new DataAccessResourceFailureException("Event file '" + file
                            + "' could not be closed.  Nested exception: " + e, e);
                }
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (Exception e) {
                    }
                }
            }
        }

    }

    /**
     * Save.
     *
     * @param resource
     *            the resource
     */
    public void save(Resource resource) {
        for (Entry<String, Events> entry : m_loadedEventFiles.entrySet()) {
            String eventFile = entry.getKey();
            Events events = entry.getValue();

            Resource eventResource = getRelative(resource, eventFile);
            events.save(eventResource);

        }

        saveEvents(resource);
    }

}
