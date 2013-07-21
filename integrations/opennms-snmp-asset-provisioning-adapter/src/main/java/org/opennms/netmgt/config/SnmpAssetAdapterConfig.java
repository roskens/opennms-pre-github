/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.config;

import java.net.InetAddress;
import java.util.concurrent.locks.Lock;

import org.opennms.netmgt.config.snmpAsset.adapter.AssetField;

/**
 * The Interface SnmpAssetAdapterConfig.
 */
public interface SnmpAssetAdapterConfig {

    /**
     * Gets the asset fields for address.
     *
     * @param address
     *            the address
     * @param sysoid
     *            the sysoid
     * @return the asset fields for address
     */
    public AssetField[] getAssetFieldsForAddress(InetAddress address, String sysoid);

    /**
     * Update.
     *
     * @throws Exception
     *             the exception
     */
    void update() throws Exception;

    /**
     * Gets the read lock.
     *
     * @return the read lock
     */
    public Lock getReadLock();

    /**
     * Gets the write lock.
     *
     * @return the write lock
     */
    public Lock getWriteLock();
}
