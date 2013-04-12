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
import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.LldpEndPoint;
import org.opennms.netmgt.model.topology.LldpLink;
import org.opennms.netmgt.model.topology.NodeElementIdentifier;
import org.opennms.netmgt.snmp.CollectionTracker;

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
    
    /**
     * Constructs a new SNMP collector for a node using the passed interface
     * as the collection point. The collection does not occur until the
     * <code>run</code> method is invoked.
     * 
     * @param nodeid
     * @param config
     *            The SnmpPeer object to collect from.
     */
    public LldpLinkdNodeDiscovery(final EnhancedLinkd linkd, final LinkableNode node, String pkgname) {
    	super(linkd, node, pkgname);
    }

    protected void runCollection() {

        String trackerName = "lldpLocalGroup";

        LldpLocalGroup lldpLocalGroup = new LldpLocalGroup(getTarget());

		LogUtils.debugf(this, "run: collecting : %s", getPeer());

        SnmpWalker walker =  SnmpUtils.createWalker(getPeer(), trackerName, lldpLocalGroup);

        walker.start();

        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.infof(this,
                        "run:Aborting node scan : Agent timed out while scanning the %s table", trackerName);
            }  else if (walker.failed()) {
            	LogUtils.infof(this,
                        "run:Aborting node scan : Agent failed while scanning the %s table: %s", trackerName,walker.getErrorMessage());
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: collection interrupted, exiting");
            return;
        }
        
        final Element deviceA = new Element();
        deviceA.addElementIdentifier(new NodeElementIdentifier(getNodeId()));
        deviceA.addElementIdentifier(lldpLocalGroup.getElementIdentifier());

		final LldpLocPortGetter lldpLocPort = new LldpLocPortGetter(getPeer());
        trackerName = "lldpRemTable";
        LldpRemTableTracker m_lldpRemTable = new LldpRemTableTracker() {
            
        	public void processLldpRemRow(final LldpRemRow row) {        		
        		LldpEndPoint endPointA = lldpLocPort.get(row.getLldpRemLocalPortNum());
        		endPointA.setDevice(deviceA);
        	    final Element deviceB = new Element();
        		deviceB.addElementIdentifier(row.getRemElementIdentifier());
        		LldpEndPoint endPointB = row.getRemEndPoint();
        		endPointB.setDevice(deviceB);
        		LldpLink link = new LldpLink(endPointA, endPointB);
        		endPointA.setLink(link);
        		endPointB.setLink(link);
        		m_linkd.getQueryManager().store(link);
            }
        };

        CollectionTracker[] tracker = new CollectionTracker[0];
        tracker = new CollectionTracker[] {m_lldpRemTable};

        walker = SnmpUtils.createWalker(getPeer(), trackerName, tracker);
        walker.start();
        
        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.infof(this,
                        "run:Aborting node scan : Agent timed out while scanning the %s table", trackerName);
            }  else if (walker.failed()) {
            	LogUtils.infof(this,
                        "run:Aborting node scan : Agent failed while scanning the %s table: %s", trackerName,walker.getErrorMessage());
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: collection interrupted, exiting");
            return;
        }
    }

	@Override
	public String getInfo() {
        return "ReadyRunnable LldpLinkNodeDiscovery" + " ip=" + str(getTarget())
                + " port=" + getPort() + " community=" + getReadCommunity()
                + " package=" + getPackageName();
	}

}
