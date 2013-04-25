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


import org.opennms.core.utils.LogUtils;

import org.opennms.netmgt.linkd.snmp.NamedSnmpVar;
import org.opennms.netmgt.linkd.snmp.SnmpStore;

import org.opennms.netmgt.snmp.AggregateTracker;
import org.opennms.netmgt.snmp.SnmpResult;

public final class Dot1dStp extends AggregateTracker {
    
	/** Constant <code>STP_PROTOCOL_SPEC="dot1dStpProtocolSpecification"</code> */
	public final static String STP_PROTOCOL_SPEC = "dot1dStpProtocolSpecification";

	/** Constant <code>STP_DESIGNATED_ROOT="dot1dStpDesignatedRoot"</code> */
	public final static String STP_DESIGNATED_ROOT = "dot1dStpDesignatedRoot";

	private static final NamedSnmpVar[] ms_elemList = new NamedSnmpVar[] {
		/**
		 * <P>An indication of what version of the Spanning
		 *  Tree Protocol is being run. The value
		 *  'decLb100(2)' indicates the DEC LANbridge 100
		 *  Spanning Tree protocol. IEEE 802.1d
		 *  implementations will return 'ieee8021d(3)'. If
		 *  future versions of the IEEE Spanning Tree Protocol
		 *  are released that are incompatible with the
		 *  current version a new value will be defined.</P>
		 */
		new NamedSnmpVar(NamedSnmpVar.SNMPINT32, STP_PROTOCOL_SPEC, ".1.3.6.1.2.1.17.2.1"),

		/**
		 * <P>The bridge identifier of the root of the spanning
		 *  tree as determined by the Spanning Tree Protocol
		 *  as executed by this node. This value is used as
		 *  the Root Identifier parameter in all Configuration
		 *  Bridge PDUs originated by this node.</P>
		 */
		new NamedSnmpVar(NamedSnmpVar.SNMPOCTETSTRING, STP_DESIGNATED_ROOT, ".1.3.6.1.2.1.17.2.5"),
	
	};

    private SnmpStore m_store;

	/**
	 * <P>The class constructor is used to initialize the collector
	 * and send out the initial SNMP packet requesting data. The
	 * data is then received and store by the object. When all the
	 * data has been collected the passed signaler object is <EM>notified</em>
	 * using the notifyAll() method.</P>
	 *
	 * @param address a {@link java.net.InetAddress} object.
	 */
	public Dot1dStp() {
        super(NamedSnmpVar.getTrackersFor(ms_elemList));
        m_store = new SnmpStore(ms_elemList); 
    }
    
    /** {@inheritDoc} */
    protected void storeResult(SnmpResult res) {
        m_store.storeResult(res);
    }

    /** {@inheritDoc} */
    protected void reportGenErr(String msg) {
        LogUtils.warnf(this, "Error retrieving dot1dStp: %s", msg);
    }

    /** {@inheritDoc} */
    protected void reportNoSuchNameErr(String msg) {
        LogUtils.infof(this, "Error retrieving dot1dStp: %s", msg);
    }

    /**
     * <p>getStpProtocolSpecification</p>
     *
     * @return a int.
     */
    public Integer getStpProtocolSpecification(){
    	return m_store.getValue(STP_PROTOCOL_SPEC).toInt();
    }
	

    /**
     * <p>getStpDesignatedRoot</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getStpDesignatedRoot(){
    	return m_store.getHexString(STP_DESIGNATED_ROOT);
    }
    
}
