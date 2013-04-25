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

import java.util.Date;



import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;

import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpWalker;

/**
 * This class is designed to collect the necessary SNMP information from the
 * target address and store the collected information. When the class is
 * initially constructed no information is collected. The SNMP Session
 * creating and collection occurs in the main run method of the instance. This
 * allows the collection to occur in a thread if necessary.
 */
public final class IpNetToMediaLinkdNodeDiscovery extends AbstractLinkdNodeDiscovery {
    
    /**
     * Constructs a new SNMP collector for a node using the passed interface
     * as the collection point. The collection does not occur until the
     * <code>run</code> method is invoked.
     * 
     * @param nodeid
     * @param config
     *            The SnmpPeer object to collect from.
     */
    public IpNetToMediaLinkdNodeDiscovery(final EnhancedLinkd linkd, final LinkableNode node) {
    	super(linkd, node);
    }

    protected void runCollection() {

    	final Date now = new Date(); 

		LogUtils.debugf(this, "run: collecting : %s", getPeer());


		IpNetToMediaTableTracker ipNetToMediaTableTracker = new IpNetToMediaTableTracker() {
		    public void processIpNetToMediaRow(final IpNetToMediaRow row) {
		    	MacAddrEndPoint macep = row.getEndPoint();
		    	if (macep != null)
		    		m_linkd.getQueryManager().store(macep);
		    }

		};
		
		String trackerName = "ipNetToMedia";
		SnmpWalker walker = SnmpUtils.createWalker(getPeer(), trackerName, ipNetToMediaTableTracker );
        walker.start();
        
        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.infof(this,
                        "run:Aborting IpNetToMedia Linkd node scan : Agent timed out while scanning the %s table", trackerName);
            	return;
            }  else if (walker.failed()) {
            	LogUtils.infof(this,
                        "run:Aborting IpNetToMedia Linkd node scan : Agent failed while scanning the %s table: %s", trackerName,walker.getErrorMessage());
            	return;
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: collection interrupted, exiting");
            return;
        }

        m_linkd.getQueryManager().reconcileIpNetToMedia(getNodeId(), now);
    }

	@Override
	public String getInfo() {
        return "ReadyRunnable IpNetToMediaLinkNodeDiscovery" + " ip=" + str(getTarget())
                + " port=" + getPort() + " community=" + getReadCommunity()
                + " package=" + getPackageName();
	}

	@Override
	public String getName() {
		return "IpNetToMediaLinkDiscovery";
	}

}
