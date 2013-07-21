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
package org.opennms.netmgt.linkd.nb;

import org.opennms.netmgt.model.NetworkBuilder;

/**
 * The Class Nms4930NetworkBuilder.
 */
public class Nms4930NetworkBuilder extends LinkdNetworkBuilder {

    /**
     * Builds the network4005.
     */
    @SuppressWarnings("deprecation")
    public void buildNetwork4005() {
        NetworkBuilder nb = getNetworkBuilder();

        nb.addNode("cisco1").setForeignSource("linkd").setForeignId("cisco1").setSysObjectId(".1.3.6.1.4.1.9.1.122").setType("A");
        nb.addInterface("10.1.1.2").setIsSnmpPrimary("P").setIsManaged("M").addSnmpInterface(3).setIfType(6).setCollectionEnabled(true).setIfSpeed(100000000).setPhysAddr("c2007db90010");
        nb.addInterface("10.1.2.1").setIsSnmpPrimary("S").setIsManaged("M").addSnmpInterface(1).setIfType(6).setCollectionEnabled(true).setIfSpeed(100000000).setPhysAddr("c2007db90000");
        nb.addInterface("10.1.3.1").setIsSnmpPrimary("S").setIsManaged("M").addSnmpInterface(2).setIfType(6).setCollectionEnabled(true).setIfSpeed(100000000).setPhysAddr("c2007db90001");
        m_nodeDao.save(nb.getCurrentNode());

        nb.addNode("cisco2").setForeignSource("linkd").setForeignId("cisco2").setSysObjectId(".1.3.6.1.4.1.9.1.122").setType("A");
        nb.addInterface("10.1.2.2").setIsSnmpPrimary("P").setIsManaged("M").addSnmpInterface(1).setIfType(6).setCollectionEnabled(true).setIfSpeed(100000000).setPhysAddr("c2017db90000");
        nb.addInterface("10.1.5.1").setIsSnmpPrimary("S").setIsManaged("M").addSnmpInterface(2).setIfType(6).setCollectionEnabled(true).setIfSpeed(100000000).setPhysAddr("c2017db90001");
        m_nodeDao.save(nb.getCurrentNode());

        m_nodeDao.flush();

    }
}
