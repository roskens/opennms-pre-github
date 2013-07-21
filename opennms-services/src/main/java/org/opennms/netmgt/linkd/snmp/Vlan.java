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

package org.opennms.netmgt.linkd.snmp;

import org.opennms.netmgt.model.OnmsVlan;
import org.opennms.netmgt.model.OnmsVlan.VlanStatus;
import org.opennms.netmgt.model.OnmsVlan.VlanType;
import org.opennms.netmgt.snmp.SnmpResult;

/**
 * <p>
 * VlanCollectorEntry interface.
 * </p>
 *
 * @author antonio
 * @version $Id: $
 */
public abstract class Vlan extends SnmpStore {

    /** The has vlan index. */
    private boolean hasVlanIndex = false;

    /** The vlan index. */
    private Integer vlanIndex = null;

    /** The Constant VLAN_INDEX. */
    public static final String VLAN_INDEX = "vlanIndex";

    /** The Constant VLAN_NAME. */
    public static final String VLAN_NAME = "vlanName";

    /** The Constant VLAN_STATUS. */
    public static final String VLAN_STATUS = "vlanStatus";

    /** The Constant VLAN_TYPE. */
    public static final String VLAN_TYPE = "vlanType";

    /**
     * Instantiates a new vlan.
     *
     * @param list
     *            the list
     */
    public Vlan(NamedSnmpVar[] list) {
        super(list);
    }

    /** {@inheritDoc} */
    @Override
    public void storeResult(SnmpResult res) {
        if (!hasVlanIndex && !hasVlanIndexOid()) {
            vlanIndex = res.getInstance().getLastSubId();
            hasVlanIndex = true;
        }
        super.storeResult(res);
    }

    /**
     * Checks for vlan index oid.
     *
     * @return true, if successful
     */
    protected abstract boolean hasVlanIndexOid();

    /**
     * Gets the vlan index.
     *
     * @return the vlan index
     */
    public Integer getVlanIndex() {
        if (hasVlanIndex)
            return vlanIndex;
        return getInt32(VLAN_INDEX);
    }

    /**
     * Gets the vlan name.
     *
     * @return the vlan name
     */
    public String getVlanName() {
        return getDisplayString(VLAN_NAME);
    }

    /**
     * Gets the vlan status.
     *
     * @return the vlan status
     */
    public abstract VlanStatus getVlanStatus();

    /**
     * Gets the vlan type.
     *
     * @return the vlan type
     */
    public abstract VlanType getVlanType();

    /**
     * Gets the onms vlan.
     *
     * @return the onms vlan
     */
    public OnmsVlan getOnmsVlan() {
        return new OnmsVlan(getVlanIndex(), getVlanName(), getVlanStatus(), getVlanType());
    }

}
