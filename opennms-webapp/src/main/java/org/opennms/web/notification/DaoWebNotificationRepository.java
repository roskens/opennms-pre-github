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

package org.opennms.web.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.dao.AcknowledgmentDao;
import org.opennms.netmgt.dao.NotificationDao;
import org.opennms.netmgt.model.AckAction;
import org.opennms.netmgt.model.OnmsAcknowledgment;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsNotification;
import org.opennms.netmgt.dao.filter.Filter;
import org.opennms.netmgt.dao.filter.notification.NotificationCriteria;
import org.opennms.netmgt.dao.filter.notification.NotificationCriteria.NotificationCriteriaVisitor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>DaoWebNotificationRepository class.</p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public class DaoWebNotificationRepository implements WebNotificationRepository, InitializingBean {
    
    @Autowired
    NotificationDao m_notificationDao;
    
    @Autowired
    AcknowledgmentDao m_ackDao;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }


    private Notification mapOnmsNotificationToNotification(OnmsNotification onmsNotification){
        if(onmsNotification != null){
            Notification notif = new Notification();
            notif.m_eventId = onmsNotification.getEvent() != null ? onmsNotification.getEvent().getId() : 0;
            notif.m_interfaceID = onmsNotification.getIpAddress() == null ? null : InetAddressUtils.toIpAddrString(onmsNotification.getIpAddress());
            notif.m_nodeID = onmsNotification.getNode() != null ? onmsNotification.getNode().getId() : 0;
            notif.m_notifyID = onmsNotification.getNotifyId();
            notif.m_numMsg = onmsNotification.getNumericMsg();
            notif.m_responder = onmsNotification.getAnsweredBy();
            notif.m_serviceId = onmsNotification.getServiceType() != null ? onmsNotification.getServiceType().getId() : 0;
            notif.m_serviceName = onmsNotification.getServiceType() != null ? onmsNotification.getServiceType().getName() : "";
            notif.m_timeReply = onmsNotification.getRespondTime() != null ? onmsNotification.getRespondTime().getTime() : 0;
            notif.m_timeSent = onmsNotification.getPageTime() != null ? onmsNotification.getPageTime().getTime() : 0;
            notif.m_txtMsg = onmsNotification.getTextMsg();
            
            return notif;
        }else{
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Transactional
    @Override
    public void acknowledgeMatchingNotification(String user, Date timestamp, NotificationCriteria criteria) {
        List<OnmsNotification> notifs = m_notificationDao.findMatching(getOnmsCriteria(criteria));
        
        for (OnmsNotification notif : notifs) {
            
            OnmsAcknowledgment ack = new OnmsAcknowledgment(notif, user);
            ack.setAckAction(AckAction.ACKNOWLEDGE);
            ack.setAckTime(timestamp);
            m_ackDao.processAck(ack);
        }
    }
    
    /** {@inheritDoc} */
    @Transactional
    @Override
    public int countMatchingNotifications(NotificationCriteria criteria) {
        return queryForInt(getOnmsCriteria(criteria));
    }

    /** {@inheritDoc} */
    @Transactional
    @Override
    public Notification[] getMatchingNotifications(NotificationCriteria criteria) {
        List<Notification> notifications = new ArrayList<Notification>();
        List<OnmsNotification> onmsNotifs = m_notificationDao.findMatching(getOnmsCriteria(criteria));

        for (OnmsNotification notif : onmsNotifs) {
            notifications.add(mapOnmsNotificationToNotification(notif));
        }
        
        return notifications.toArray(new Notification[0]);
    }
    
    /** {@inheritDoc} */
    @Transactional
    @Override
    public Notification getNotification(int noticeId) {
        return mapOnmsNotificationToNotification(m_notificationDao.get(noticeId));
    }
    
    private int queryForInt(OnmsCriteria onmsCriteria) {
        return m_notificationDao.countMatching(onmsCriteria);
    }

}
