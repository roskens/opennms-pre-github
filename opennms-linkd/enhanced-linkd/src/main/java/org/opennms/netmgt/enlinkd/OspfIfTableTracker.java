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

package org.opennms.netmgt.enlinkd;

import static org.opennms.core.utils.InetAddressUtils.str;

import java.net.InetAddress;
import java.util.List;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.LogUtils;

import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.NodeElementIdentifier;
import org.opennms.netmgt.model.topology.OspfElementIdentifier;
import org.opennms.netmgt.model.topology.OspfEndPoint;
import org.opennms.netmgt.model.topology.OspfLink;
import org.opennms.netmgt.snmp.RowCallback;
import org.opennms.netmgt.snmp.SnmpInstId;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpRowResult;
import org.opennms.netmgt.snmp.TableTracker;

public class OspfIfTableTracker extends TableTracker {

    public static final SnmpObjId OSPF_IF_TABLE_ENTRY  = SnmpObjId.get(".1.3.6.1.2.1.14.7.1"); // start of table (GETNEXT)
    
    public final static SnmpObjId OSPF_IF_IPADDRESS    = SnmpObjId.get(".1.3.6.1.2.1.14.7.1.1");
    public final static SnmpObjId OSPF_ADDRESS_LESS_IF = SnmpObjId.get(".1.3.6.1.2.1.14.7.1.2");

    public static final SnmpObjId[] s_ospfiftable_elemList = new SnmpObjId[] {
        
        /**
         *  "The IP address of this OSPF interface."
        */
        OSPF_IF_IPADDRESS,
        
        /**
         * "For the purpose of easing  the  instancing  of
         * addressed   and  addressless  interfaces;  This
         * variable takes the value 0 on  interfaces  with
         * IP  Addresses,  and  the corresponding value of
         * ifIndex for interfaces having no IP Address."
         * 
         */
        OSPF_ADDRESS_LESS_IF
        
    };
    
    class OspfIfRow extends SnmpRowResult {

    	public OspfIfRow(int columnCount, SnmpInstId instance) {
			super(columnCount, instance);
            LogUtils.debugf(this, "column count = %d, instance = %s", columnCount, instance);
		}
    	
    	public InetAddress getOspfIpAddress() {
	        return getValue(OSPF_IF_IPADDRESS).toInetAddress();
	    }
	    
	    public Integer getOspfAddressLessIf() {
	        return getValue(OSPF_ADDRESS_LESS_IF).toInt();
	    }
	    
	    public OspfEndPoint getEndPoint(Integer sourceNode) {
	    	return new OspfEndPoint(getOspfIpAddress(), getOspfAddressLessIf(),sourceNode);
	    }

		public OspfLink getLink(
				OspfElementIdentifier ospfGenralElementIdentifier,
				NodeElementIdentifier nodeElementIdentifier,
				IpAddrTableGetter ipAddrTableGetter,
				List<OspfEndPoint> nbrEndPoints) {
			Element elementA = new Element();
			elementA.addElementIdentifier(nodeElementIdentifier);
			elementA.addElementIdentifier(ospfGenralElementIdentifier);
		
            LogUtils.infof(this, "processOspfIfRow: row ospf ip address: %s", str(getOspfIpAddress()));
            LogUtils.infof(this, "processOspfIfRow: row ospf address less ifindex: %s", getOspfAddressLessIf());

			OspfEndPoint endPointA = ipAddrTableGetter.get(getEndPoint(nodeElementIdentifier.getNodeid()));
			OspfEndPoint endPointB = null;
			for (OspfEndPoint ospfEndPoint : nbrEndPoints) {
				if (InetAddressUtils.getNetwork(ospfEndPoint.getOspfIpAddr(),
						endPointA.getOspfIpMask()).equals(
						InetAddressUtils.getNetwork(endPointA.getOspfIpAddr(),
								endPointA.getOspfIpMask()))) {
					endPointB = ospfEndPoint;
					LogUtils.infof(this,
							"processOspfIfRow: link nbr address found: %s",
							str(endPointB.getOspfIpAddr()));
					endPointB.setOspfIpMask(endPointA.getOspfIpMask());
					return new OspfLink(endPointA, endPointA,
							nodeElementIdentifier.getNodeid());
				}
			}
			
			return null;
		}

    }

    public OspfIfTableTracker() {
        super(s_ospfiftable_elemList);
    }

    public OspfIfTableTracker(final RowCallback rowProcessor) {
    	super(rowProcessor,s_ospfiftable_elemList);
    }

    /** {@inheritDoc} */
    @Override
    public SnmpRowResult createRowResult(final int columnCount, final SnmpInstId instance) {
        return new OspfIfRow(columnCount, instance);
    }

    /** {@inheritDoc} */
    @Override
    public void rowCompleted(final SnmpRowResult row) {
        processOspfIfRow((OspfIfRow)row);
    }

    /**
     * <p>processOspfIfRow</p>
     *
     * @param row a {@link org.opennms.netmgt.enlinkd.OspfIfTableTracker.OspfIfRow} object.
     */
    public void processOspfIfRow(final OspfIfRow row) {
    }


}
