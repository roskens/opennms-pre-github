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

import java.util.Date;
import java.util.List;

import org.opennms.gwt.web.ui.asset.client.AssetService;
import org.opennms.gwt.web.ui.asset.shared.AssetCommand;
import org.opennms.gwt.web.ui.asset.shared.AssetSuggCommand;
import org.opennms.netmgt.dao.AssetRecordDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsAssetRecord;
import org.opennms.netmgt.model.OnmsNode;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * 
 */
@Transactional(readOnly = false)
public class AssetServiceImpl extends RemoteServiceServlet implements
		AssetService {

	private static final long serialVersionUID = 3847574674959207209L;

	private AssetRecordDao m_assetRecordDao;

	private NodeDao m_nodeDao;

	private OnmsNode m_onmsNode;

	private OnmsAssetRecord m_onmsAssetRecord;

	@Override
	public AssetCommand getAssetByNodeId(int nodeId) {
		AssetCommand assetCommand = new AssetCommand();
		this.m_onmsNode = this.m_nodeDao.get(nodeId);
		this.m_onmsAssetRecord = this.m_onmsNode.getAssetRecord();

		// copy all assetRecord properties to assetCommand for gui
		BeanUtils.copyProperties(this.m_onmsAssetRecord, assetCommand);

		// set node specific properties for the asset node page
		assetCommand.setSnmpSysContact(this.m_onmsNode.getSysContact());
		assetCommand.setSnmpSysDescription(this.m_onmsNode.getSysDescription());
		assetCommand.setSnmpSysLocation(this.m_onmsNode.getSysLocation());
		assetCommand.setSnmpSysName(this.m_onmsNode.getSysName());
		assetCommand.setSnmpSysObjectId(this.m_onmsNode.getSysObjectId());
		
		assetCommand.setNodeId(this.m_onmsNode.getNodeId());
		assetCommand.setNodeLabel(this.m_onmsNode.getLabel());

		// set user from web ui session
		assetCommand.setLoggedInUser("admin");
		this.m_onmsAssetRecord.setNode(this.m_onmsNode);
		return assetCommand;
	}

	@Override
	public AssetSuggCommand getAssetSuggestions() {
		AssetSuggCommand suggestion = new AssetSuggCommand();
		List<OnmsAssetRecord> distinctAssetProperties = this.m_assetRecordDao
				.getDistinctProperties();

		// TODO: a lot of mapping code, better using Hibernates
		// ResultToBeanMapper
		for (OnmsAssetRecord asset : distinctAssetProperties) {
			suggestion.addAdditionalhardware(asset.getAdditionalhardware());
			suggestion.addAddress1(asset.getAddress1());
			suggestion.addAddress2(asset.getAddress2());
			suggestion.addAdmin(asset.getAdmin());
			suggestion.addBuilding(asset.getBuilding());
			suggestion.addCategory(asset.getCategory());
			suggestion.addCircuitId(asset.getCircuitId());
			suggestion.addCity(asset.getCity());
			suggestion.addCpu(asset.getCpu());
			suggestion.addDepartment(asset.getDepartment());
			suggestion.addDescription(asset.getDescription());
			suggestion.addDisplayCategory(asset.getDisplayCategory());
			suggestion.addDivision(asset.getDivision());
			suggestion.addFloor(asset.getFloor());
			suggestion.addHdd1(asset.getHdd1());
			suggestion.addHdd2(asset.getHdd2());
			suggestion.addHdd3(asset.getHdd3());
			suggestion.addHdd4(asset.getHdd4());
			suggestion.addHdd5(asset.getHdd5());
			suggestion.addHdd6(asset.getHdd6());
			suggestion.addInputpower(asset.getInputpower());
			suggestion.addLease(asset.getLease());
			suggestion.addMaintContractNumber(asset.getMaintcontract());
			suggestion.addManufacturer(asset.getManufacturer());
			suggestion.addModelNumber(asset.getModelNumber());
			suggestion.addNotifyCategory(asset.getNotifyCategory());
			suggestion.addNumpowersupplies(asset.getNumpowersupplies());
			suggestion.addOperatingSystem(asset.getOperatingSystem());
			suggestion.addPollerCategory(asset.getPollerCategory());
			suggestion.addRack(asset.getRack());
			suggestion.addRam(asset.getRam());
			suggestion.addRegion(asset.getRegion());
			suggestion.addRoom(asset.getRoom());
			suggestion.addSnmpcommunity(asset.getSnmpcommunity());
			suggestion.addState(asset.getState());
			suggestion.addStoragectrl(asset.getStoragectrl());
			suggestion.addSupportPhone(asset.getSupportPhone());
			suggestion.addThresholdCategory(asset.getThresholdCategory());
			suggestion.addVendor(asset.getVendor());
			suggestion.addVendorFax(asset.getVendorFax());
			suggestion.addVendorPhone(asset.getVendorPhone());
			suggestion.addZip(asset.getZip());
		}
		return suggestion;
	}

	@Override
	public Boolean saveOrUpdateAssetByNodeId(int nodeId,
			AssetCommand assetCommand) {
		Boolean isSaved = false;
		this.m_onmsNode = this.m_nodeDao.get(nodeId);
		this.m_onmsAssetRecord = this.m_onmsNode.getAssetRecord();

		BeanUtils.copyProperties(assetCommand, this.m_onmsAssetRecord);

		this.m_onmsAssetRecord.setLastModifiedBy("admin");

		this.m_onmsAssetRecord.setLastModifiedDate(new Date());
		this.m_onmsAssetRecord.setNode(this.m_onmsNode);

		try {
			this.m_assetRecordDao.saveOrUpdate(this.m_onmsAssetRecord);
			isSaved = true;
		} catch (Exception e) {
			// TODO: Catch exception and show error in web user interface
			isSaved = false;
			e.printStackTrace();
		}

		return isSaved;
	}

	/**
	 * <p>
	 * getUsername
	 * </p>
	 * 
	 * @return a {@link java.lang.String} object.
	 */
	// protected String getUsername() {
	// /*
	// * This should never be null, as the strategy should create a
	// * SecurityContext if one doesn't exist, but let's check anyway.
	// */
	// SecurityContext context = SecurityContextHolder.getContext();
	// Assert.state(context != null,
	// "No security context found when calling SecurityContextHolder.getContext()");
	//
	// Authentication auth = context.getAuthentication();
	// Assert.state(auth != null,
	// "No Authentication object found when calling getAuthentication on our SecurityContext object");
	//
	// Object obj = auth.getPrincipal();
	// Assert.state(obj != null,
	// "No principal object found when calling getPrinticpal on our Authentication object");
	//
	//
	// if (obj instanceof UserDetails) {
	// return ((UserDetails)obj).getUsername();
	// } else {
	// return obj.toString();
	// }
	// }
	//

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
