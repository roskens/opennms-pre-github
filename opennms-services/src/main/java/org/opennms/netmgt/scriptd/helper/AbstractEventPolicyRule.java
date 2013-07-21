/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.scriptd.helper;

import org.opennms.netmgt.xml.event.Event;

/**
 * The Class AbstractEventPolicyRule.
 */
public abstract class AbstractEventPolicyRule implements EventPolicyRule {

    /** The forward. */
    private boolean forward;

    /* (non-Javadoc)
     * @see org.opennms.netmgt.scriptd.helper.EventPolicyRule#addForwardRule(org.opennms.netmgt.scriptd.helper.EventMatch)
     */
    @Override
    public void addForwardRule(EventMatch match) {
        m_filter.add(match);
        m_forwardes.add(Boolean.TRUE);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.scriptd.helper.EventPolicyRule#addDropRule(org.opennms.netmgt.scriptd.helper.EventMatch)
     */
    @Override
    public void addDropRule(EventMatch match) {
        m_filter.add(match);
        m_forwardes.add(Boolean.FALSE);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.scriptd.helper.EventPolicyRule#filter(org.opennms.netmgt.xml.event.Event)
     */
    @Override
    public Event filter(Event event) {
        forward = true;
        int count = 0;
        for (EventMatch filter : m_filter) {
            if (filter.match(event)) {
                forward = m_forwardes.get(count).booleanValue();
                break;
            }
            count++;
        }
        if (forward)
            return expand(event);
        return null;
    }

    /**
     * Expand.
     *
     * @param event
     *            the event
     * @return the event
     */
    protected abstract Event expand(Event event);

}
