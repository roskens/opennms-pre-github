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


import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.model.topology.LldpElementIdentifier;
import org.opennms.netmgt.model.topology.LldpEndPoint;
import org.opennms.netmgt.snmp.RowCallback;
import org.opennms.netmgt.snmp.SnmpInstId;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpRowResult;
import org.opennms.netmgt.snmp.SnmpValue;
import org.opennms.netmgt.snmp.TableTracker;

public class LldpRemTableTracker extends TableTracker {

    public static final SnmpObjId LLDP_REM_TABLE_ENTRY = SnmpObjId.get(".1.0.8802.1.1.2.1.4.1.1"); // start of table (GETNEXT)
    
    
    public final static SnmpObjId LLDP_REM_CHASSISID_SUBTYPE = SnmpObjId.get(LLDP_REM_TABLE_ENTRY,"4");
    public final static SnmpObjId LLDP_REM_CHASSISID         = SnmpObjId.get(LLDP_REM_TABLE_ENTRY,"5");
    public final static SnmpObjId LLDP_REM_PORTID_SUBTYPE    = SnmpObjId.get(LLDP_REM_TABLE_ENTRY,"6");
    public final static SnmpObjId LLDP_REM_PORTID            = SnmpObjId.get(LLDP_REM_TABLE_ENTRY,"7");
    public final static SnmpObjId LLDP_REM_SYSNAME           = SnmpObjId.get(LLDP_REM_TABLE_ENTRY,"9");

    public static final SnmpObjId[] s_lldpremtable_elemList = new SnmpObjId[] {
        
        /**
         *  "The type of encoding used to identify the chassis associated
         *  with the remote system."
         */
        LLDP_REM_CHASSISID_SUBTYPE,
        
        /**
         * "The string value used to identify the chassis component
         * associated with the remote system."
         */
       LLDP_REM_CHASSISID,

        /**
         * "The type of port identifier encoding used in the associated
         * 'lldpRemPortId' object."
         */
        LLDP_REM_PORTID_SUBTYPE,

        /**
         * "The string value used to identify the port component
            associated with the remote system."
         */
        LLDP_REM_PORTID,
        
        /**
         * "The string value used to identify the port component
         * associated with the remote system."
         */
        LLDP_REM_SYSNAME

    };
    

    class LldpRemRow extends SnmpRowResult {

		public LldpRemRow(int columnCount, SnmpInstId instance) {
			super(columnCount, instance);
            LogUtils.debugf(this, "column count = %d, instance = %s", columnCount, instance);
		}
    	
	    public Integer getLldpRemLocalPortNum() {
	    	return getInstance().getLastSubId();
	    }
	    
	    public Integer getLldpRemChassisidSubtype() {
	    	return getValue(LLDP_REM_CHASSISID_SUBTYPE).toInt();
	    }
	    
	    public SnmpValue getLldpRemChassisId() {
	    	return getValue(LLDP_REM_CHASSISID);
	    }
	    
	    public Integer getLldpRemPortidSubtype() {
	    	return getValue(LLDP_REM_PORTID_SUBTYPE).toInt();
	    }

	    public SnmpValue getLldpRemPortid() {
	    	return getValue(LLDP_REM_PORTID);
	    }
	    
	    public String getLldpRemSysname() {
	        return getValue(LLDP_REM_SYSNAME).toDisplayString();
	    }
	    
	    public LldpElementIdentifier getRemElementIdentifier() {
	    	return LldpLocalGroup.getElementIdentifier(getLldpRemChassisId(), getLldpRemSysname(), getLldpRemChassisidSubtype());
	    }
	    
	    public LldpEndPoint getRemEndPoint() {
	    	return LldpLocPortGetter.getEndPoint(getLldpRemPortid(), getLldpRemPortidSubtype());
	    }
    }

    public LldpRemTableTracker() {
        super(s_lldpremtable_elemList);
    }
    
    /**
     * <p>Constructor for IPInterfaceTableTracker.</p>
     *
     * @param rowProcessor a {@link org.opennms.netmgt.snmp.RowCallback} object.
     */
    public LldpRemTableTracker(final RowCallback rowProcessor) {
        super(rowProcessor, s_lldpremtable_elemList);
    }
    
    /** {@inheritDoc} */
    @Override
    public SnmpRowResult createRowResult(final int columnCount, final SnmpInstId instance) {
        return new LldpRemRow(columnCount, instance);
    }

    /** {@inheritDoc} */
    @Override
    public void rowCompleted(final SnmpRowResult row) {
        processLldpRemRow((LldpRemRow)row);
    }

    /**
     * <p>processIPInterfaceRow</p>
     *
     * @param row a {@link org.opennms.netmgt.provision.service.IPAddressTableTracker.IPAddressRow} object.
     */
    public void processLldpRemRow(final LldpRemRow row) {
    }

    


}
