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

import org.opennms.netmgt.snmp.RowCallback;
import org.opennms.netmgt.snmp.SnmpInstId;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpRowResult;
import org.opennms.netmgt.snmp.TableTracker;

/**
 *<P>The Dot1dStpPortTableEntry class is designed to hold all the MIB-II
 * information for one entry in the MIB II dot1dBridge.dot1dStp.dot1dStpPortTable.
 * The table effectively contains a list of these entries, each entry having information
 * about STP Protocol on specific Port.</P>
 *
 * <P>This object is used by the Dot1dStpPortTable to hold information
 * single entries in the table. See the Dot1dStpPortTable documentation
 * form more information.</P>
 *
 * @author <A HREF="mailto:rssntn67@yahoo.it">Antonio</A>
 * @see Dot1dStpPortTable
 * @see <A HREF="http://www.ietf.org/rfc/rfc1213.txt">RFC1213</A>
 * @version $Id: $
 */
public final class Dot1dStpPortTableTracker extends TableTracker {
	// Lookup strings for specific table entries
	//

	public final static SnmpObjId DOT1D_STP_PORT                   = SnmpObjId.get(".1.3.6.1.2.1.17.2.15.1.1");
	public final static SnmpObjId DOT1D_STP_PORT_STATE             = SnmpObjId.get(".1.3.6.1.2.1.17.2.15.1.3");
	public final static SnmpObjId DOT1D_STP_PORT_ENABLE            = SnmpObjId.get(".1.3.6.1.2.1.17.2.15.1.4");
	public final static SnmpObjId DOT1D_STP_PORT_DESIGNATED_BRIDGE = SnmpObjId.get(".1.3.6.1.2.1.17.2.15.1.8");
	public final static SnmpObjId DOT1D_STP_PORT_DESIGNATED_PORT   = SnmpObjId.get(".1.3.6.1.2.1.17.2.15.1.9");

	public static final SnmpObjId[] stpport_elemList = new SnmpObjId[] {
		/**
	     * SYNTAX      Integer32 (1..65535)
         * MAX-ACCESS  read-only
         * STATUS      current
         * DESCRIPTION
         *  "The port number of the port for which this entry
         *  contains Spanning Tree Protocol management information."
         * REFERENCE
         *  "IEEE 802.1D-1998: clause 14.8.2.1.2"		 
         *  
         */
		DOT1D_STP_PORT,
				
		/**
		 *  dot1dStpPortState OBJECT-TYPE
         * SYNTAX      INTEGER {
         *              disabled(1),
         *              blocking(2),
         *              listening(3),
         *              learning(4),
         *              forwarding(5),
         *              broken(6)
         *          }
         * MAX-ACCESS  read-only
         * STATUS      current
         * DESCRIPTION
         * "The port's current state, as defined by application of
         *  the Spanning Tree Protocol.  This state controls what
         *  action a port takes on reception of a frame.  If the
         *  bridge has detected a port that is malfunctioning, it
         *  will place that port into the broken(6) state.  For
         *  ports that are disabled (see dot1dStpPortEnable), this
         *  object will have a value of disabled(1)."
         * REFERENCE
         *  "IEEE 802.1D-1998: clause 8.5.5.2"
         */
		DOT1D_STP_PORT_STATE,
		
		/**
		 *    dot1dStpPortEnable OBJECT-TYPE
         * SYNTAX      INTEGER {
         *              enabled(1),
         *              disabled(2)
         *          }
         * MAX-ACCESS  read-write
         * STATUS      current
         * DESCRIPTION
         *  "The enabled/disabled status of the port."
         * REFERENCE
         *  "IEEE 802.1D-1998: clause 8.5.5.2"
		 */
		DOT1D_STP_PORT_ENABLE,
		
		/**
		 *    dot1dStpPortDesignatedBridge OBJECT-TYPE
         * SYNTAX      BridgeId
         * MAX-ACCESS  read-only
         * STATUS      current
         * DESCRIPTION
         *  "The Bridge Identifier of the bridge that this
         *  port considers to be the Designated Bridge for
         *  this port's segment."
         * REFERENCE
         *  "IEEE 802.1D-1998: clause 8.5.5.6"
		 */
		DOT1D_STP_PORT_DESIGNATED_BRIDGE,
		
		/**
		 * The Port Identifier of the port on the Designated
		 * Bridge for this port's segment.
		 */
		DOT1D_STP_PORT_DESIGNATED_PORT
	};

	class Dot1dStpPortRow extends SnmpRowResult {

		public Dot1dStpPortRow(int columnCount, SnmpInstId instance) {
			super(columnCount, instance);
		}

		/**
		 * <p>getDot1dStpPort</p>
		 *
		 * @return a int.
		 */
		public Integer getDot1dStpPort() {
			return getValue(DOT1D_STP_PORT).toInt();
		}
			
		/**
		 * <p>getDot1dStpPortState</p>
		 *
		 * @return a int.
		 */
		public Integer getDot1dStpPortState() {
			return  getValue(DOT1D_STP_PORT_STATE).toInt();
		}
		
		/**
		 * <p>getDot1dStpPortEnable</p>
		 *
		 * @return a int.
		 */
		public Integer getDot1dStpPortEnable() {
			return getValue(DOT1D_STP_PORT_ENABLE).toInt();
		}
		
		/**
		 * <p>getDot1dStpPortDesignatedBridge</p>
		 *
		 * @return a {@link java.lang.String} object.
		 */
		public String getDot1dStpPortDesignatedBridge() {
			return getValue(DOT1D_STP_PORT_DESIGNATED_BRIDGE).toHexString();
		}
	
		/**
		 * <p>getDot1dStpPortDesignatedPort</p>
		 *
		 * @return a {@link java.lang.String} object.
		 */
		public String getDot1dStpPortDesignatedPort() {
			return getValue(DOT1D_STP_PORT_DESIGNATED_PORT).toHexString();
			
		}
	
	}
	
	public Dot1dStpPortTableTracker() {
		super(stpport_elemList);
	}

	public Dot1dStpPortTableTracker(RowCallback rowProcessor) {
		super(rowProcessor, stpport_elemList);
	}

    /** {@inheritDoc} */
    @Override
    public SnmpRowResult createRowResult(final int columnCount, final SnmpInstId instance) {
        return new Dot1dStpPortRow(columnCount, instance);
    }

    /** {@inheritDoc} */
    @Override
    public void rowCompleted(final SnmpRowResult row) {
        processDot1dStpPortRow((Dot1dStpPortRow)row);
    }

    /**
     * <p>processIpNetToMediaRow</p>
     *
     * @param row a {@link org.opennms.netmgt.enlinkd.Dot1qTpFdbTableTracker.Dot1qTpFdbRow} object.
     */
    public void processDot1dStpPortRow(final Dot1dStpPortRow row) {
    }
	


}
