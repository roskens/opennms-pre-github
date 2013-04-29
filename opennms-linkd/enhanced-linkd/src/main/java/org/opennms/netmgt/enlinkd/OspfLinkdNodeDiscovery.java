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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.model.topology.NodeElementIdentifier;
import org.opennms.netmgt.model.topology.OspfElementIdentifier;
import org.opennms.netmgt.model.topology.OspfEndPoint;

import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpWalker;

/**
 * This class is designed to collect the necessary SNMP information from the
 * target address and store the collected information. When the class is
 * initially constructed no information is collected. The SNMP Session
 * creating and collection occurs in the main run method of the instance. This
 * allows the collection to occur in a thread if necessary.
 */
public final class OspfLinkdNodeDiscovery extends AbstractLinkdNodeDiscovery {
    
	/**
	 * Constructs a new SNMP collector for Ospf Node Discovery. 
	 * The collection does not occur until the
     * <code>run</code> method is invoked.
     * 
	 * @param EnhancedLinkd linkd
	 * @param LinkableNode node
	 */
    public OspfLinkdNodeDiscovery(final EnhancedLinkd linkd, final LinkableNode node) {
    	super(linkd, node);
    }

    protected void runCollection() {

    	final Date now = new Date(); 

    	String trackerName = "ospfGeneralGroup";

        final OspfGeneralGroup ospfGeneralGroup = new OspfGeneralGroup();

		LogUtils.debugf(this, "run: collecting : %s", getPeer());

        SnmpWalker walker =  SnmpUtils.createWalker(getPeer(), trackerName, ospfGeneralGroup);

        walker.start();

        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.infof(this,
                        "run:Aborting Ospf Linkd node scan : Agent timed out while scanning the %s table", trackerName);
            	return;
            }  else if (walker.failed()) {
            	LogUtils.infof(this,
                        "run:Aborting Ospf Linkd node scan : Agent failed while scanning the %s table: %s", trackerName,walker.getErrorMessage());
            	return;
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: Ospf Linkd node collection interrupted, exiting");
            return;
        }

        if (ospfGeneralGroup.getOspfRouterId() == null ) {
            LogUtils.infof(this, "ospf mib not supported on: %s", str(getPeer().getAddress()));
            return;
        } 

        if (ospfGeneralGroup.getOspfRouterId().equals(InetAddressUtils.addr("0.0.0.0"))) {
            LogUtils.infof(this, "ospf not supported, ospf identifier 0.0.0.0 is not valid on: %s", str(getPeer().getAddress()));
            return;
        } 

        final OspfElementIdentifier ospfGenralElementIdentifier = ospfGeneralGroup.getElementIdentifier(getNodeId());
        LogUtils.infof(this, "found local ospf identifier : %s", ospfGenralElementIdentifier);

        final NodeElementIdentifier nodeElementIdentifier = new NodeElementIdentifier(getNodeId());
        LogUtils.infof(this, "found node identifier for node: %s", nodeElementIdentifier );

        final List<OspfEndPoint> nbrEndPoints = new ArrayList<OspfEndPoint>();
        trackerName = "ospfNbrTable";
        OspfNbrTableTracker cdpCachospfNbrTableTracker = new OspfNbrTableTracker() {

        	public void processOspfNbrRow(final OspfNbrRow row) {
        		nbrEndPoints.add(row.getEndPoint(getNodeId()));
        	}
        };

        walker = SnmpUtils.createWalker(getPeer(), trackerName, cdpCachospfNbrTableTracker);
        walker.start();
        
        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.infof(this,
                        "run:Aborting Ospf Linkd node scan : Agent timed out while scanning the %s table", trackerName);
            	return;
            }  else if (walker.failed()) {
            	LogUtils.infof(this,
                        "run:Aborting Ospf Linkd node scan : Agent failed while scanning the %s table: %s", trackerName,walker.getErrorMessage());
            	return;
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: collection interrupted, exiting");
            return;
        }

        final IpAddrTableGetter ipAddrTableGetter = new IpAddrTableGetter(getPeer());
        trackerName = "ospfIfTable";
        OspfIfTableTracker ospfIfTableTracker = new OspfIfTableTracker() {

        	public void processOspfIfRow(final OspfIfRow row) {
	    		m_linkd.getQueryManager().store(row.getLink(ospfGenralElementIdentifier, nodeElementIdentifier, ipAddrTableGetter,nbrEndPoints));
        	}

        };

        walker = SnmpUtils.createWalker(getPeer(), trackerName, ospfIfTableTracker);
        walker.start();
        
        try {
            walker.waitFor();
            if (walker.timedOut()) {
            	LogUtils.infof(this,
                        "run:Aborting Ospf Linkd node scan : Agent timed out while scanning the %s table", trackerName);
            	return;
            }  else if (walker.failed()) {
            	LogUtils.infof(this,
                        "run:Aborting Ospf Linkd node scan : Agent failed while scanning the %s table: %s", trackerName,walker.getErrorMessage());
            	return;
            }
        } catch (final InterruptedException e) {
            LogUtils.errorf(this, e, "run: collection interrupted, exiting");
            return;
        }

        m_linkd.getQueryManager().reconcileOspf(getNodeId(),now);
    }

	@Override
	public String getInfo() {
        return "ReadyRunnable OspfLinkNodeDiscovery" + " ip=" + str(getTarget())
                + " port=" + getPort() + " community=" + getReadCommunity()
                + " package=" + getPackageName();
	}

	@Override
	public String getName() {
		return "OspfLinkDiscovery";
	}

}
