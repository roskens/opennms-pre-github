/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.enlinkd;

import static org.opennms.core.utils.InetAddressUtils.str;


import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.linkd.snmp.LldpLocalGroup;
import org.opennms.netmgt.linkd.snmp.LldpRemTable;
import org.opennms.netmgt.snmp.SnmpAgentConfig;
import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpWalker;

/**
 * This class is designed to collect the necessary SNMP information from the
 * target address and store the collected information. When the class is
 * initially constructed no information is collected. The SNMP Session
 * creating and collection occurs in the main run method of the instance. This
 * allows the collection to occur in a thread if necessary.
 */
public final class LldpLinkdNodeDiscovery extends AbstractLinkdNodeDiscovery {

    public LldpLocalGroup m_lldpLocalGroup;
    public LldpRemTable m_lldpRemTable;
    
    /**
     * Constructs a new SNMP collector for a node using the passed interface
     * as the collection point. The collection does not occur until the
     * <code>run</code> method is invoked.
     * 
     * @param nodeid
     * @param config
     *            The SnmpPeer object to collect from.
     */
    public LldpLinkdNodeDiscovery(final EnhancedLinkd linkd, final int nodeid,
            final SnmpAgentConfig config) {
    	super(linkd, nodeid, config);
    }

    
    boolean hasLldpLocalGroup() {
        return (m_lldpLocalGroup != null && !m_lldpLocalGroup.failed() && m_lldpLocalGroup.getLldpLocChassisid() != null);
    }

    LldpLocalGroup getLldpLocalGroup() {
        return m_lldpLocalGroup;
    }

    protected void runCollection() {


        final String hostAddress = str(m_address);


        m_lldpLocalGroup = new LldpLocalGroup(m_address);

		LogUtils.debugf(this, "run: collecting : %s", m_agentConfig);

        SnmpWalker walker =  SnmpUtils.createWalker(m_agentConfig, "lldpLocalGroup", m_lldpLocalGroup);

        walker.start();

        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.infof(this,
                        "run:Aborting node scan : Agent timed out while scanning the lldpLocalGroup table");
            }  else if (walker.failed()) {
            	LogUtils.infof(this,
                        "run:Aborting node scan : Agent failed while scanning the lldpLocalGroup table: %s", walker.getErrorMessage());
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: collection interrupted, exiting");
            return;
        }
        if (!this.hasLldpLocalGroup()) {
            LogUtils.infof(this,
                           "run: failed to collect lldpLocalGroup for %s",
                           hostAddress);
            return;
        }
        
        m_lldpRemTable = new LldpRemTable(m_address);
        
//        m_linkd.updateNodeSnmpCollection(this);
        // clean memory
        // first make everything clean
        m_lldpLocalGroup = null;
        m_lldpRemTable = null;
    }


	@Override
	public String getInfo() {
        return "ReadyRunnable LldpLinkNodeDiscovery" + " ip=" + str(getTarget())
                + " port=" + getPort() + " community=" + getReadCommunity()
                + " package=" + getPackageName();
	}

}
