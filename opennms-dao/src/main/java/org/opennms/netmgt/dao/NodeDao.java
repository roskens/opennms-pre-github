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

package org.opennms.netmgt.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.opennms.netmgt.model.*;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeDao extends OnmsDao<OnmsNode, Integer> {
	
    OnmsNode get(String lookupCriteria);

    String getLabelForId(Integer id);
    
    List<OnmsNode> findByLabel(String label);
    
    List<OnmsNode> findNodes(OnmsDistPoller dp);
    OnmsNode getHierarchy(Integer id);
    
    Map<String, Integer> getForeignIdToNodeIdMap(String foreignSource);
    
    List<OnmsNode> findAllByVarCharAssetColumn(String columnName, String columnValue);
    
    List<OnmsNode> findAllByVarCharAssetColumnCategoryList(String columnName, String columnValue,
            Collection<OnmsCategory> categories);
    
    List<OnmsNode> findByCategory(OnmsCategory category);
    
    List<OnmsNode> findAllByCategoryList(Collection<OnmsCategory> categories);

    List<OnmsNode> findAllByCategoryLists(Collection<OnmsCategory> rowCatNames, Collection<OnmsCategory> colCatNames);
    
    @Override
    List<OnmsNode> findAll();

    List<OnmsNode> findByForeignSource(String foreignSource);
    
    OnmsNode findByForeignId(String foreignSource, String foreignId);

    int getNodeCountForForeignSource(String groupName);
    
    List<OnmsNode> findAllProvisionedNodes();
    
    List<OnmsIpInterface> findObsoleteIpInterfaces(Integer nodeId, Date scanStamp);

    void deleteObsoleteInterfaces(Integer nodeId, Date scanStamp);

    void updateNodeScanStamp(Integer nodeId, Date scanStamp);

    Collection<Integer> getNodeIds();

    List<OnmsNode> findByForeignSourceAndIpAddress(String foreignSource, String ipAddress);

    SurveillanceStatus findSurveillanceStatusByCategoryLists(Collection<OnmsCategory> rowCategories, Collection<OnmsCategory> columnCategories);

    List<OnmsNode> findBySysName(String sysName);

    List<OnmsNode> findByTypeAndIsSnmpPrimary(String type, PrimaryType snmpPrimaryType);

    List<OnmsNode> findByTypeAndIsSnmpPrimaryAndNodeId(String type, PrimaryType snmpPrimaryType, int nodeid);
}
