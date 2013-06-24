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

package org.opennms.netmgt.dao.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.dao.AcknowledgmentDao;
import org.opennms.netmgt.model.AckType;
import org.opennms.netmgt.model.Acknowledgeable;
import org.opennms.netmgt.model.OnmsAcknowledgment;
import org.opennms.netmgt.model.OnmsAlarm;
import org.opennms.netmgt.model.OnmsNotification;
import org.springframework.transaction.annotation.Transactional;

public class AcknowledgmentDaoHibernate extends AbstractDaoHibernate<OnmsAcknowledgment, Integer> implements AcknowledgmentDao {

    public AcknowledgmentDaoHibernate() {
        super(OnmsAcknowledgment.class);
    }

    /** {@inheritDoc} */
    @Override
    public List<Acknowledgeable> findAcknowledgables(final OnmsAcknowledgment ack) {
        List<Acknowledgeable> ackables = new ArrayList<Acknowledgeable>();
        if (ack == null || ack.getAckType() == null) {
            return ackables;
        }

        if (ack.getAckType().equals(AckType.ALARM)) {
            final OnmsAlarm alarm = findAlarm(ack);
            if (alarm != null && alarm.getAckId() != null) {
                ackables.add(alarm);
                List<OnmsNotification> notifs = findRelatedNotifications(alarm);
                if (notifs != null) {
                    for (OnmsNotification notif : notifs) {
                        if (notif.getAckId() != null) {
                            ackables.add(notif);
                        }
                    }
                }
            }
        } else if (ack.getAckType().equals(AckType.NOTIFICATION)) {
            final OnmsNotification notif = findNotification(ack);
            if (notif != null && notif.getAckId() != null) {
                ackables.add(notif);
                    if (notif.getEvent() != null) {
                        final OnmsAlarm alarm = notif.getEvent().getAlarm();
                        if (alarm != null) {
                            ackables.add(alarm);
                        }
                    }
            }
        }
        return ackables;
    }
    
    private List<OnmsNotification> findRelatedNotifications(final OnmsAlarm alarm) {
        final String hql = "from OnmsNotification as n where n.event.alarm = ?";
        return findObjects(OnmsNotification.class, hql, alarm);
    }

    private OnmsAlarm findAlarm(final OnmsAcknowledgment ack) {
        if (ack != null) {
            return getJpaTemplate().find(OnmsAlarm.class, ack.getRefId());
        }
        return null;
    }

    private OnmsNotification findNotification(final OnmsAcknowledgment ack) {
        if (ack != null) {
            return getJpaTemplate().find(OnmsNotification.class, ack.getRefId());
        }
        return null;
    }

    /** {@inheritDoc} */
    @Transactional(readOnly=false)
    @Override
    public void processAcks(Collection<OnmsAcknowledgment> acks) {
        LogUtils.infof(this, "processAcks: Processing "+acks.size()+" acknowledgements...");
        for (OnmsAcknowledgment ack : acks) {
            processAck(ack);
        }
    }

    /** {@inheritDoc} */
    @Transactional(readOnly=false)
    @Override
    public void processAck(OnmsAcknowledgment ack) {
        LogUtils.infof(this,"processAck: Searching DB for acknowledgables for ack: "+ack);
        List<Acknowledgeable> ackables = findAcknowledgables(ack);
        
        if (ackables == null || ackables.size() < 1) {
            LogUtils.debugf(this,"processAck: No acknowledgables found.");
            throw new IllegalStateException("No acknowlegables in the database for ack: "+ack);
        }

        LogUtils.debugf(this,"processAck: Found "+ackables.size()+". Acknowledging...");
        
        Iterator<Acknowledgeable> it = ackables.iterator();
        while (it.hasNext()) {
            try {
                Acknowledgeable ackable = it.next();

                switch (ack.getAckAction()) {
                    case ACKNOWLEDGE:
                        LogUtils.debugf(this,"processAck: Acknowledging ackable: "+ackable+"...");
                        ackable.acknowledge(ack.getAckUser());
                        LogUtils.debugf(this,"processAck: Acknowledged ackable: "+ackable);
                        break;
                    case UNACKNOWLEDGE:
                        LogUtils.debugf(this,"processAck: Unacknowledging ackable: "+ackable+"...");
                        ackable.unacknowledge(ack.getAckUser());
                        LogUtils.debugf(this,"processAck: Unacknowledged ackable: "+ackable);
                        break;
                    case CLEAR:
                        LogUtils.debugf(this,"processAck: Clearing ackable: "+ackable+"...");
                        ackable.clear(ack.getAckUser());
                        LogUtils.debugf(this,"processAck: Cleared ackable: "+ackable);
                        break;
                    case ESCALATE:
                        LogUtils.debugf(this,"processAck: Escalating ackable: "+ackable+"...");
                        ackable.escalate(ack.getAckUser());
                        LogUtils.debugf(this,"processAck: Escalated ackable: "+ackable);
                        break;
                }
                getJpaTemplate().merge(ackable);
                save(ack);
                flush();
            } catch (Throwable t) {
                LogUtils.errorf(this, "processAck: exception while processing: "+ack+"; "+t, t);
            }
        }
        LogUtils.infof(this,"processAck: Found and processed acknowledgables for the acknowledgement: "+ack);
    }
}
