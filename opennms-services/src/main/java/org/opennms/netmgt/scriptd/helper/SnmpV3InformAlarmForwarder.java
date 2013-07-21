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

import java.net.UnknownHostException;

import org.opennms.netmgt.xml.event.Event;

/**
 * The Class SnmpV3InformAlarmForwarder.
 */
public class SnmpV3InformAlarmForwarder extends SnmpTrapForwarderHelper implements EventForwarder {

    /**
     * Instantiates a new snmp v3 inform alarm forwarder.
     *
     * @param ip
     *            the ip
     * @param port
     *            the port
     * @param timeout
     *            the timeout
     * @param retries
     *            the retries
     * @param securityLevel
     *            the security level
     * @param securityname
     *            the securityname
     * @param authPassPhrase
     *            the auth pass phrase
     * @param authProtocol
     *            the auth protocol
     * @param privPassPhrase
     *            the priv pass phrase
     * @param privprotocol
     *            the privprotocol
     * @param snmpTrapHelper
     *            the snmp trap helper
     */
    public SnmpV3InformAlarmForwarder(String ip, int port, int timeout, int retries, int securityLevel,
            String securityname, String authPassPhrase, String authProtocol, String privPassPhrase,
            String privprotocol, SnmpTrapHelper snmpTrapHelper) {
        super(ip, port, securityLevel, securityname, authPassPhrase, authProtocol, privPassPhrase, privprotocol,
              timeout, retries, snmpTrapHelper);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.scriptd.helper.EventForwarder#flushEvent(org.opennms.netmgt.xml.event.Event)
     */
    @Override
    public void flushEvent(Event event) {
        event = super.filter(event);
        if (event != null) {
            try {
                sendV3AlarmInform(event, false);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SnmpTrapHelperException e) {
                e.printStackTrace();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.scriptd.helper.EventForwarder#flushSyncEvent(org.opennms.netmgt.xml.event.Event)
     */
    @Override
    public void flushSyncEvent(Event event) {
        event = super.filter(event);
        if (event != null) {
            try {
                sendV3AlarmInform(event, true);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SnmpTrapHelperException e) {
                e.printStackTrace();
            }
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.scriptd.helper.EventForwarder#sendStartSync()
     */
    @Override
    public void sendStartSync() {
        super.sendV3StartSyncInform();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.scriptd.helper.EventForwarder#sendEndSync()
     */
    @Override
    public void sendEndSync() {
        super.sendV3EndSyncInform();

    }
}
