/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.opennms.core.criteria.Criteria;
import org.opennms.core.criteria.restrictions.Restrictions;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.dao.EventDao;
import org.opennms.netmgt.model.OnmsEvent;
import org.opennms.netmgt.model.OnmsSeverity;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>DaoWebEventRepository class.</p>
 * 
 * @deprecated Move all of these methods into the {@link EventDao}. This class just
 * delegates straight to it anyway.
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public class DaoWebEventRepository implements WebEventRepository, InitializingBean {
    
    @Autowired
    EventDao m_eventDao;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    private Event mapOnmsEventToEvent(OnmsEvent onmsEvent){
        log().debug("Mapping OnmsEvent to WebEvent for event with database id " + onmsEvent.getId());
        Event event = new Event();
        event.acknowledgeTime = onmsEvent.getEventAckTime();
        event.acknowledgeUser = onmsEvent.getEventAckUser();
        event.alarmId = onmsEvent.getAlarm() != null ? onmsEvent.getAlarm().getId() : 0;
        event.autoAction = onmsEvent.getEventAutoAction();
        event.createTime = onmsEvent.getEventCreateTime();
        event.description = onmsEvent.getEventDescr();
        event.dpName = onmsEvent.getDistPoller() != null ? onmsEvent.getDistPoller().getName() : "";
        event.eventDisplay = Boolean.valueOf(onmsEvent.getEventDisplay().equals("Y"));
        event.forward = onmsEvent.getEventForward();
        event.host = onmsEvent.getEventHost();
        event.id = onmsEvent.getId();
        event.ipAddr = onmsEvent.getIpAddr() == null ? null : InetAddressUtils.toIpAddrString(onmsEvent.getIpAddr());
        event.logGroup = onmsEvent.getEventLogGroup();
        event.logMessage = onmsEvent.getEventLogMsg();
        event.mouseOverText = onmsEvent.getEventMouseOverText();
        event.nodeLabel = getNodeLabelFromNode(onmsEvent);
        log().debug("Found NodeLabel for mapped event:" + event.getNodeLabel());
        event.nodeID = getNodeIdFromNode(onmsEvent);
        log().debug("Found NodeId for mapped event:" + event.getNodeId());
        event.notification = onmsEvent.getEventNotification();
        event.operatorAction = onmsEvent.getEventOperAction();
        event.operatorActionMenuText = onmsEvent.getEventOperActionMenuText();
        event.operatorInstruction = onmsEvent.getEventOperInstruct();
        event.parms = onmsEvent.getEventParms();
        event.serviceID = onmsEvent.getServiceType() != null ? onmsEvent.getServiceType().getId() : 0;
        event.serviceName = onmsEvent.getServiceType() != null ? onmsEvent.getServiceType().getName() : "";
        event.severity = OnmsSeverity.get(onmsEvent.getEventSeverity());
        event.snmp = onmsEvent.getEventSnmp();
        event.snmphost = onmsEvent.getEventSnmpHost();
        event.time = onmsEvent.getEventTime();
        event.troubleTicket = onmsEvent.getEventTTicket();
        event.troubleTicketState = onmsEvent.getEventTTicketState();
        event.uei = onmsEvent.getEventUei();
        return event;
    }

    private Integer getNodeIdFromNode(OnmsEvent onmsEvent) {
        try {
            return onmsEvent.getNode() != null ? onmsEvent.getNode().getId() : 0;            
        } catch (org.hibernate.ObjectNotFoundException e) {
            log().debug("No node found in database for event with id: " + onmsEvent.getId());
            return 0;
        }
    }
    
    private String getNodeLabelFromNode(OnmsEvent onmsEvent) {
        try {
            return onmsEvent.getNode() != null ? onmsEvent.getNode().getLabel() : "";                    
        } catch (org.hibernate.ObjectNotFoundException e) {
            log().debug("No node found in database for event with id: " + onmsEvent.getId());
            return "";
        } 
    }
    
    /**
     * <p>acknowledgeAll</p>
     *
     * @param user a {@link java.lang.String} object.
     * @param timestamp a java$util$Date object.
     */
    @Transactional
    @Override
    public void acknowledgeAll(String user, Date timestamp) {
        acknowledgeMatchingEvents(user, timestamp, EventUtil.getSearchParameter().toCriteria());
    }
    
    /** {@inheritDoc} */
    @Transactional
    @Override
    public void acknowledgeMatchingEvents(String user, Date timestamp, Criteria criteria) {
        List<OnmsEvent> events = m_eventDao.findMatching(criteria);
        
        Iterator<OnmsEvent> eventsIt = events.iterator();
        while(eventsIt.hasNext()){
            OnmsEvent event = eventsIt.next();
            event.setEventAckUser(user);
            event.setEventAckTime(timestamp);
            m_eventDao.update(event);
        }
    }
    
    /** {@inheritDoc} */
    @Transactional
    @Override
    public int countMatchingEvents(Criteria criteria) {
        return m_eventDao.countMatching(criteria);
    }
    
    /** {@inheritDoc} */
    @Transactional
    @Override
    public int[] countMatchingEventsBySeverity(Criteria criteria) {
        //OnmsCriteria crit = getOnmsCriteria(criteria).setProjection(Projections.groupProperty("severityId"));

        int[] eventCounts = new int[8];
        eventCounts[OnmsSeverity.CLEARED.getId()] = m_eventDao.countMatching(criteria.addRestriction(Restrictions.eq("eventSeverity", OnmsSeverity.CLEARED.getId())));
        eventCounts[OnmsSeverity.CRITICAL.getId()] = m_eventDao.countMatching(criteria.addRestriction(Restrictions.eq("eventSeverity", OnmsSeverity.CRITICAL.getId())));
        eventCounts[OnmsSeverity.INDETERMINATE.getId()] = m_eventDao.countMatching(criteria.addRestriction(Restrictions.eq("eventSeverity", OnmsSeverity.INDETERMINATE.getId())));
        eventCounts[OnmsSeverity.MAJOR.getId()] = m_eventDao.countMatching(criteria.addRestriction(Restrictions.eq("eventSeverity", OnmsSeverity.MAJOR.getId())));
        eventCounts[OnmsSeverity.MINOR.getId()] = m_eventDao.countMatching(criteria.addRestriction(Restrictions.eq("eventSeverity", OnmsSeverity.MINOR.getId())));
        eventCounts[OnmsSeverity.NORMAL.getId()] = m_eventDao.countMatching(criteria.addRestriction(Restrictions.eq("eventSeverity", OnmsSeverity.NORMAL.getId())));
        eventCounts[OnmsSeverity.WARNING.getId()] = m_eventDao.countMatching(criteria.addRestriction(Restrictions.eq("eventSeverity", OnmsSeverity.WARNING.getId())));
        return eventCounts;
    }
    
    /** {@inheritDoc} */
    @Transactional
    @Override
    public Event getEvent(int eventId) {
        return mapOnmsEventToEvent(m_eventDao.get(eventId));
    }
    
    /** {@inheritDoc} */
    @Transactional
    @Override
    public Event[] getMatchingEvents(Criteria criteria) {
        List<Event> events = new ArrayList<Event>();
        log().debug("getMatchingEvents: try to get events for Criteria: " + criteria.toString());
        List<OnmsEvent> onmsEvents = m_eventDao.findMatching(criteria);

        log().debug("getMatchingEvents: found " + onmsEvents.size() + " events");

        if(onmsEvents.size() > 0){
            Iterator<OnmsEvent> eventIt = onmsEvents.iterator();
            
            while(eventIt.hasNext()){
                events.add(mapOnmsEventToEvent(eventIt.next()));
            }   
        }
        
        return events.toArray(new Event[0]);
    }
    
    /**
     * <p>unacknowledgeAll</p>
     */
    @Transactional
    @Override
    public void unacknowledgeAll() {
        unacknowledgeMatchingEvents(EventUtil.getSearchParameter().toCriteria());
    }
    
    /** {@inheritDoc} */
    @Transactional
    @Override
    public void unacknowledgeMatchingEvents(Criteria criteria) {
        List<OnmsEvent> events = m_eventDao.findMatching(criteria);
        
        for(OnmsEvent event : events) {
            event.setEventAckUser(null);
            event.setEventAckTime(null);
            m_eventDao.update(event);
        }
    }
    
    private static ThreadCategory log() {
        return ThreadCategory.getInstance(DaoWebEventRepository.class);
    }


}
