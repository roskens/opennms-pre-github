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

import java.net.InetAddress;

import static org.opennms.core.utils.InetAddressUtils.getIpAddressByHexString;

import org.opennms.core.utils.LogUtils;

import org.opennms.netmgt.model.topology.CdpElementIdentifier;
import org.opennms.netmgt.model.topology.CdpElementIdentifier.CiscoNetworkProtocolType;
import org.opennms.netmgt.model.topology.CdpEndPoint;
import org.opennms.netmgt.model.topology.CdpLink;
import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.NodeElementIdentifier;
import org.opennms.netmgt.snmp.RowCallback;
import org.opennms.netmgt.snmp.SnmpInstId;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpRowResult;
import org.opennms.netmgt.snmp.TableTracker;

public class CdpCacheTableTracker extends TableTracker {

	public static final SnmpObjId CDP_CACHE_TABLE_ENTRY = SnmpObjId.get(".1.3.6.1.4.1.9.9.23.1.2.1.1"); // start of table (GETNEXT)

	public final static SnmpObjId CDP_ADDRESS_TYPE      = SnmpObjId.get(".1.3.6.1.4.1.9.9.23.1.2.1.1.3");
	public final static SnmpObjId CDP_ADDRESS           = SnmpObjId.get(".1.3.6.1.4.1.9.9.23.1.2.1.1.4");
	public final static SnmpObjId CDP_DEVICEID          = SnmpObjId.get(".1.3.6.1.4.1.9.9.23.1.2.1.1.6");
	public final static SnmpObjId CDP_DEVICEPORT        = SnmpObjId.get(".1.3.6.1.4.1.9.9.23.1.2.1.1.7");
                                                           
	/**
	 * <P>The keys that will be supported by default from the 
	 * TreeMap base class. Each of the elements in the list
	 * are an instance of the dot1dbasetable. Objects
	 * in this list should be used by multiple instances of
	 * this class.</P>
	 */
	public static final SnmpObjId[] cdpCache_elemList = new SnmpObjId[] {

		/**
		 * <P>An indication of the type of address contained in the
		 *  corresponding instance of cdpCacheAddress.</P>
		 */
		CDP_ADDRESS_TYPE,

		/**
		 * <P>The (first) network-layer address of the device's
		 *  SNMP-agent as reported in the Address TLV of the most recently
		 *  received CDP message. For example, if the corresponding
		 *  instance of cacheAddressType had the value 'ip(1)', then
		 *  this object would be an IP-address.</P>
		 */
		CDP_ADDRESS, 

		/**
		 * <P>The Device-ID string as reported in the most recent CDP
		 *  message. The zero-length string indicates no Device-ID
		 *  field (TLV) was reported in the most recent CDP
		 *  message.</P>
		 */
		CDP_DEVICEID,

		/**
		 * <P>The Port-ID string as reported in the most recent CDP
		 *  message. This will typically be the value of the ifName
		 *  object (e.g., 'Ethernet0'). The zero-length string
		 *  indicates no Port-ID field (TLV) was reported in the
		 *  most recent CDP message.</P>
		 */
		CDP_DEVICEPORT
	};

    class CdpCacheRow extends SnmpRowResult {
		
    	public CdpCacheRow(int columnCount, SnmpInstId instance) {
			super(columnCount, instance);
            LogUtils.debugf(this, "column count = %d, instance = %s", columnCount, instance);
		}

    	/**
    	 *  "Normally, the ifIndex value of the local interface.
         *   For 802.3 Repeaters for which the repeater ports do not
         *   have ifIndex values assigned, this value is a unique
         *   value for the port, and greater than any ifIndex value
         *   supported by the repeater; the specific port number in
         *   this case, is given by the corresponding value of
         *   cdpInterfacePort."
    	 * 
    	 */
    	
    	public Integer getCdpCacheIfIndex() {
			return getInstance().getSubIdAt(getInstance().length()-2);
    	}
	
		/**
		 * <p>getCdpCacheAddressType</p>
		 *
		 * @return a int.
		 */
		public Integer getCdpCacheAddressType() {
		    return getValue(CDP_ADDRESS_TYPE).toInt();
		}
	
		/**
		 * <p>getCdpCacheAddress</p>
		 *
		 * @return a {@link java.lang.String} object.
		 */
		public String getCdpCacheAddress() {
		    return getValue(CDP_ADDRESS).toHexString();
		}
	
		/**
		 * <p>getCdpCacheIpv4Address</p>
		 *
		 * @return a {@link java.lang.String} object.
		 */
		public InetAddress getCdpCacheIpv4Address() {
	            return getIpAddressByHexString(getCdpCacheAddress());	    
		}
				
		/**
		 * <p>getCdpCacheDeviceId</p>
		 *
		 * @return a {@link java.lang.String} object.
		 */
		public String getCdpCacheDeviceId() {
			return getValue(CDP_DEVICEID).toDisplayString();
		}
		
		/**
		 * <p>getCdpCacheDevicePort</p>
		 *
		 * @return a {@link java.lang.String} object.
		 */
		public String getCdpCacheDevicePort() {
			return 	getValue(CDP_DEVICEPORT).toDisplayString();
		}
		
		public CdpElementIdentifier getCdpCacheElementIdentifier() {
			return new CdpElementIdentifier(getCdpCacheDeviceId(),getCdpCacheAddress(),CiscoNetworkProtocolType.get(getCdpCacheAddressType()));
		}
		
		public CdpEndPoint getCdpCacheEndPoint() {
			return new CdpEndPoint(getCdpCacheDevicePort());
		}
		
	    public CdpLink getLink(CdpElementIdentifier cdpIdentifier, NodeElementIdentifier nodeIdentifier, CdpInterfacePortNameGetter cdpInterfacePortNameGetter) {
            Element deviceA = new Element();
            deviceA.addElementIdentifier(nodeIdentifier);
            deviceA.addElementIdentifier(cdpIdentifier);
            LogUtils.infof(this, "processCdpCacheRow: row count: %d", getColumnCount());
            LogUtils.infof(this, "processCdpCacheRow: row cdpCacheIfindex: %d",  getCdpCacheIfIndex());

            CdpEndPoint endPointA = cdpInterfacePortNameGetter.get(getCdpCacheIfIndex());
            deviceA.addEndPoint(endPointA);
    		endPointA.setElement(deviceA);
            LogUtils.infof(this, "processCdpCacheRow: row local port: %s", endPointA.getCdpCacheDevicePort());
    		
    		Element deviceB = new Element();
            CdpElementIdentifier cdpCacheElementIdentifier = getCdpCacheElementIdentifier();
            LogUtils.infof(this, "processCdpCacheRow: row cdp cache identifier: %s", cdpCacheElementIdentifier);
            deviceB.addElementIdentifier(cdpCacheElementIdentifier);
    		
    		CdpEndPoint endPointB = getCdpCacheEndPoint();
    		deviceB.addEndPoint(endPointB);
    		endPointB.setElement(deviceB);
            LogUtils.infof(this, "processCdpCacheRow: row cdp cache device port: %s", endPointB.getCdpCacheDevicePort());
    		
    		CdpLink link = new CdpLink(endPointA, endPointB);
    		endPointA.setLink(link);
    		endPointB.setLink(link);
    		return link;
	    }

    }

	/**
	 * <p>Constructor for CdpCacheTableEntry.</p>
	 */
	public CdpCacheTableTracker() {
		super(cdpCache_elemList);
	}

	/**
	 * <p>Constructor for CdpCacheTableEntry.</p>
	 */
	public CdpCacheTableTracker(RowCallback rowProcessor) {
		super(rowProcessor,cdpCache_elemList);
	}

    /** {@inheritDoc} */
    @Override
    public SnmpRowResult createRowResult(final int columnCount, final SnmpInstId instance) {
        return new CdpCacheRow(columnCount, instance);
    }

    /** {@inheritDoc} */
    @Override
    public void rowCompleted(final SnmpRowResult row) {
        processCdpCacheRow((CdpCacheRow)row);
    }

    /**
     * <p>processcdpCacheRow</p>
     *
     * @param row a {@link org.opennms.netmgt.enlinkd.CdpCacheTableTracker.CdpCacheRow} object.
     */
    public void processCdpCacheRow(final CdpCacheRow row) {
    }

}
