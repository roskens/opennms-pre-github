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
 *<P>The QbridgeDot1dTpFdbTableEntry class is designed to hold all the MIB-II
 * information for one entry in the MIB II dot1dBridge.dot1dTp.dot1dTpFdbTable.
 * The table effectively contains a list of these entries, each entry having information
 * about bridge forwarding table.</P>
 *
 * <P>This object is used by the Dot1dTpFdbTable to hold information
 * single entries in the table. See the Dot1dTpFdbTable documentation
 * form more information.</P>
 *
 * @author <A HREF="mailto:rssntn67@yahoo.it">Antonio</A>
 * @see Dot1dTpFdbTable
 * @see <A HREF="http://www.ietf.org/rfc/rfc1213.txt">RFC1213</A>
 * @version $Id: $
 */
public final class QBridgeDot1dTpFdbTableTracker extends TableTracker {
	// Lookup strings for specific table entries
	//

	/** Constant <code>FDB_ADDRESS="dot1dTpFdbAddress"</code> */
	public final static SnmpObjId QBRIDGE_FDB_ADDRESS = SnmpObjId.get(".1.3.6.1.2.1.17.7.1.2.2.1.1");


	/** Constant <code>FDB_PORT="dot1dTpFdbPort"</code> */
	public final static SnmpObjId QBRIDGE_FDB_PORT    = SnmpObjId.get(".1.3.6.1.2.1.17.7.1.2.2.1.2");

	/** Constant <code>FDB_STATUS="dot1dTpFdbStatus"</code> */
	public final static SnmpObjId QBRIDGE_FDB_STATUS  = SnmpObjId.get(".1.3.6.1.2.1.17.7.1.2.2.1.3");

	/**
	 * <P>The keys that will be supported by default from the 
	 * TreeMap base class. Each of the elements in the list
	 * are an instance of the Dot1dTpFbTable. Objects
	 * in this list should be used by multiple instances of
	 * this class.</P>
	 */
	public static SnmpObjId[] ms_elemList = new SnmpObjId[] {
	    /**
         * A unicast MAC address for which the bridge has
         * forwarding and/or filtering information.
         */
        QBRIDGE_FDB_ADDRESS,

        /**
         * Either the value '0', or the port number of the
         * port on which a frame having a source address
         * equal to the value of the corresponding instance
         * of dot1dTpFdbAddress has been seen. A value of
         * '0' indicates that the port number has not been
         * learned but that the bridge does have some
         * forwarding/filtering information about this
         * address (e.g. in the dot1dStaticTable).
         * Implementors are encouraged to assign the port
         * value to this object whenever it is learned even
         * for addresses for which the corresponding value of
         * dot1dTpFdbStatus is not learned(3).
         */
        QBRIDGE_FDB_PORT,
        /**
         * The status of this entry. The meanings of the
         * values are:
         * other(1) : none of the following. This would
         * include the case where some other
         * MIB object (not the corresponding
         * instance of dot1dTpFdbPort, nor an
         * entry in the dot1dStaticTable) is
         * being used to determine if and how
         * frames addressed to the value of
         * the corresponding instance of
         * dot1dTpFdbAddress are being
         * forwarded.
         * invalid(2) : this entry is not longer valid
         * (e.g., it was learned but has since
         * aged-out), but has not yet been
         * flushed from the table.
         * learned(3) : the value of the corresponding
         * instance of dot1dTpFdbPort was
         * learned, and is being used.
         * self(4) : the value of the corresponding
         * instance of dot1dTpFdbAddress
         * represents one of the bridge's
         * addresses. The corresponding
         * instance of dot1dTpFdbPort
         * indicates which of the bridge's
         * ports has this address.
         * mgmt(5) : the value of the corresponding
         * instance of dot1dTpFdbAddress is
         * also the value of an existing
         * instance of dot1dStaticAddress.
         */
        QBRIDGE_FDB_STATUS
	};


	class QBridgeDot1dTpFdbRow extends SnmpRowResult {

		public QBridgeDot1dTpFdbRow(int columnCount, SnmpInstId instance) {
			super(columnCount, instance);
		}

		/**
		 * <p>getQBridgeDot1dTpFdbAddress</p>
		 *
		 * @return a {@link java.lang.String} object.
		 */
		public String getQBridgeDot1dTpFdbAddress() {
			return getValue(QBRIDGE_FDB_ADDRESS).toHexString();
		}
	
		/**
		 * <p>getQBridgeDot1dTpFdbPort</p>
		 *
		 * @return a int.
		 */
		public Integer getQBridgeDot1dTpFdbPort() {
			return getValue(QBRIDGE_FDB_PORT).toInt();
		}
	
		/**
		 * <p>getQBridgeDot1dTpFdbStatus</p>
		 *
		 * @return a int.
		 */
		public Integer getQBridgeDot1dTpFdbStatus() {
			return getValue(QBRIDGE_FDB_STATUS).toInt();
		}
	}	
	/**
	 * <p>Constructor for QBridgeDot1dTpFdbTableEntry.</p>
	 */
	public QBridgeDot1dTpFdbTableTracker() {
		super(ms_elemList);
	}

	public QBridgeDot1dTpFdbTableTracker(RowCallback rowProcessor) {
		super(rowProcessor, ms_elemList);
	}

    /** {@inheritDoc} */
    @Override
    public SnmpRowResult createRowResult(final int columnCount, final SnmpInstId instance) {
        return new QBridgeDot1dTpFdbRow(columnCount, instance);
    }

    /** {@inheritDoc} */
    @Override
    public void rowCompleted(final SnmpRowResult row) {
        processQBridgeDot1dTpFdbRow((QBridgeDot1dTpFdbRow)row);
    }

    /**
     * <p>processIpNetToMediaRow</p>
     *
     * @param row a {@link org.opennms.netmgt.enlinkd.QBridgeDot1dTpFdbTableTracker.QBridgeDot1dTpFdbRow} object.
     */
    public void processQBridgeDot1dTpFdbRow(final QBridgeDot1dTpFdbRow row) {
    }
	


}
