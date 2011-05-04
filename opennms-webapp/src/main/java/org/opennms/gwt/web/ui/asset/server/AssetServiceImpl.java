/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2011 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *      OpenNMS Licensing       <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 */

package org.opennms.gwt.web.ui.asset.server;

import java.util.ArrayList;
import java.util.Date;

import org.opennms.gwt.web.ui.asset.client.AssetService;
import org.opennms.gwt.web.ui.asset.shared.AssetCommand;
import org.opennms.gwt.web.ui.asset.shared.AssetSuggCommand;
import org.opennms.netmgt.dao.AssetRecordDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsAssetRecord;
import org.opennms.netmgt.model.OnmsNode;
import org.springframework.beans.BeanUtils;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * 
 */
@Transactional(readOnly = false)
public class AssetServiceImpl extends RemoteServiceServlet implements AssetService {

    private static final long serialVersionUID = 3847574674959207209L;

    private AssetRecordDao m_assetRecordDao;

    private NodeDao m_nodeDao;

    private OnmsNode m_onmsNode;

    private OnmsAssetRecord m_onmsAssetRecord;

    private AssetSuggCommand m_assetSuggCommand;
    
    private AssetCommand m_assetCommand;
    
    public AssetServiceImpl() {
    	this.m_assetCommand = new AssetCommand();
    	this.m_assetSuggCommand = new AssetSuggCommand();
    }
    
    @Override
    public AssetCommand getAssetByNodeId(int nodeId) {
        this.m_onmsNode = this.m_nodeDao.get(nodeId);
        this.m_onmsAssetRecord = this.m_onmsNode.getAssetRecord();
        
        // copy all assetRecord properties to assetCommand for gui
        BeanUtils.copyProperties(this.m_onmsAssetRecord, this.m_assetCommand);
        
        // set node specific properties for the asset node page
        this.m_assetCommand.setSnmpSysContact(this.m_onmsNode.getSysContact());
        this.m_assetCommand.setSnmpSysDescription(this.m_onmsNode.getSysDescription());
        this.m_assetCommand.setSnmpSysLocation(this.m_onmsNode.getSysLocation());
        this.m_assetCommand.setSnmpSysName(this.m_onmsNode.getSysName());
        this.m_assetCommand.setSnmpSysObjectId(this.m_onmsNode.getSysObjectId());
        
        // set user from web ui session
        this.m_assetCommand.setLoggedInUser(getUsername());
        this.m_onmsAssetRecord.setNode(this.m_onmsNode);
        return this.m_assetCommand;
    }

    @Override
    public AssetSuggCommand getAssetSuggestions() {
    	// TODO: a lot of mapping code, better using Hibernates ResultToBeanMapper
    	this.m_assetSuggCommand.setAdditionalhardware((ArrayList<String>) this.m_assetRecordDao.getDistinctAdditionalhardware());
        this.m_assetSuggCommand.setAddress1((ArrayList<String>) this.m_assetRecordDao.getDistinctAddress1());
        this.m_assetSuggCommand.setAddress2((ArrayList<String>) this.m_assetRecordDao.getDistinctAddress2());
        this.m_assetSuggCommand.setAdmin((ArrayList<String>) this.m_assetRecordDao.getDistinctAdmin());
        this.m_assetSuggCommand.setBuilding((ArrayList<String>) this.m_assetRecordDao.getDistinctBuilding());
        this.m_assetSuggCommand.setCategory((ArrayList<String>) this.m_assetRecordDao.getDistinctDisplayCategory());
        this.m_assetSuggCommand.setCircuitId((ArrayList<String>) this.m_assetRecordDao.getDistinctCircuitId());
        this.m_assetSuggCommand.setCity((ArrayList<String>) this.m_assetRecordDao.getDistinctCity());
        this.m_assetSuggCommand.setDepartment((ArrayList<String>) this.m_assetRecordDao.getDistinctDepartment());
        this.m_assetSuggCommand.setDescription((ArrayList<String>) this.m_assetRecordDao.getDistinctDescription());
        this.m_assetSuggCommand.setDisplayCategory((ArrayList<String>) this.m_assetRecordDao.getDistinctDisplayCategory());
        this.m_assetSuggCommand.setDivision((ArrayList<String>) this.m_assetRecordDao.getDistinctDivision());
        this.m_assetSuggCommand.setFloor((ArrayList<String>) this.m_assetRecordDao.getDistinctFloor());
        this.m_assetSuggCommand.setInputpower((ArrayList<String>) this.m_assetRecordDao.getDistinctInputpower());
        this.m_assetSuggCommand.setLease((ArrayList<String>) this.m_assetRecordDao.getDistinctLease());
        this.m_assetSuggCommand.setMaintContractNumber((ArrayList<String>) this.m_assetRecordDao.getDistinctMaintContract());
        this.m_assetSuggCommand.setManufacturer((ArrayList<String>) this.m_assetRecordDao.getDistinctManufacturer());
        this.m_assetSuggCommand.setModelNumber((ArrayList<String>) this.m_assetRecordDao.getDistinctModelNumber());
        this.m_assetSuggCommand.setNotifyCategory((ArrayList<String>) this.m_assetRecordDao.getDistinctNotificationCategory());
        this.m_assetSuggCommand.setOperatingSystem((ArrayList<String>) this.m_assetRecordDao.getDistinctOs());
        this.m_assetSuggCommand.setPollerCategory((ArrayList<String>) this.m_assetRecordDao.getDistinctPollerCategory());
        this.m_assetSuggCommand.setRack((ArrayList<String>) this.m_assetRecordDao.getDistinctRack());
        this.m_assetSuggCommand.setRegion((ArrayList<String>) this.m_assetRecordDao.getDistinctRegion());
        this.m_assetSuggCommand.setRoom((ArrayList<String>) this.m_assetRecordDao.getDistinctRoom());
        this.m_assetSuggCommand.setSnmpcommunity((ArrayList<String>) this.m_assetRecordDao.getDistinctSnmpcommunity());
        this.m_assetSuggCommand.setState((ArrayList<String>) this.m_assetRecordDao.getDistinctState());
        this.m_assetSuggCommand.setStoragectrl((ArrayList<String>) this.m_assetRecordDao.getDistinctStoragectrl());
        this.m_assetSuggCommand.setSupportPhone((ArrayList<String>) this.m_assetRecordDao.getDistinctSupportPhone());
        this.m_assetSuggCommand.setThresholdCategory((ArrayList<String>) this.m_assetRecordDao.getDistinctThresholdCategory());
        this.m_assetSuggCommand.setVendor((ArrayList<String>) this.m_assetRecordDao.getDistinctVendor());
        this.m_assetSuggCommand.setVendorFax((ArrayList<String>) this.m_assetRecordDao.getDistinctVendorFax());
        this.m_assetSuggCommand.setVendorPhone((ArrayList<String>) this.m_assetRecordDao.getDistinctVendorPhone());
        this.m_assetSuggCommand.setZip((ArrayList<String>) this.m_assetRecordDao.getDistinctZip());
        
        return this.m_assetSuggCommand;
    }

    @Override
    public Boolean saveOrUpdateAssetByNodeId(int nodeId, AssetCommand assetCommand) {
    	Boolean isSaved = false;
    	this.m_assetCommand = assetCommand;
        this.m_onmsNode = this.m_nodeDao.get(nodeId);
        this.m_onmsAssetRecord = this.m_onmsNode.getAssetRecord();

        BeanUtils.copyProperties(this.m_assetCommand, this.m_onmsAssetRecord);

        this.m_onmsAssetRecord.setLastModifiedBy(getUsername());
        
        this.m_onmsAssetRecord.setLastModifiedDate(new Date());
        this.m_onmsAssetRecord.setNode(this.m_onmsNode);
        
        try {
        	this.m_assetRecordDao.saveOrUpdate(this.m_onmsAssetRecord);
        	isSaved = true;
        } catch (Exception e) {
        	//TODO: Catch exception and show error in web user interface
        	isSaved = false;
        	e.printStackTrace();
        }
        
        return isSaved;
    }

    /**
     * <p>getUsername</p>
     *
     * @return a {@link java.lang.String} object.
     */
    protected String getUsername() {
        /*
         * This should never be null, as the strategy should create a
         * SecurityContext if one doesn't exist, but let's check anyway.
         */
        SecurityContext context = SecurityContextHolder.getContext();
        Assert.state(context != null, "No security context found when calling SecurityContextHolder.getContext()");
        
        Authentication auth = context.getAuthentication();
        Assert.state(auth != null, "No Authentication object found when calling getAuthentication on our SecurityContext object");
        
        Object obj = auth.getPrincipal();
        Assert.state(obj != null, "No principal object found when calling getPrinticpal on our Authentication object");
        
        
        if (obj instanceof UserDetails) { 
            return ((UserDetails)obj).getUsername(); 
        } else { 
            return obj.toString(); 
        }
    }
    
	public AssetRecordDao getAssetRecordDao() {
		return m_assetRecordDao;
	}

	public void setAssetRecordDao(AssetRecordDao m_assetRecordDao) {
		this.m_assetRecordDao = m_assetRecordDao;
	}

	public NodeDao getNodeDao() {
		return m_nodeDao;
	}

	public void setNodeDao(NodeDao m_nodeDao) {
		this.m_nodeDao = m_nodeDao;
	}
}
