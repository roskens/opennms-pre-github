//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.                                                            
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//    
// For more information contact: 
//   OpenNMS Licensing       <license@opennms.org>
//   http://www.opennms.org/
//   http://www.opennms.com/
//
// Tab Size = 8

package org.opennms.netmgt.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.netmgt.dao.AssetRecordDao;
import org.opennms.netmgt.model.OnmsAssetRecord;

/**
 * <p>
 * AssetRecordDaoHibernate class.
 * </p>
 * 
 * @author ranger
 * @version $Id: $
 */
public class AssetRecordDaoHibernate extends
		AbstractDaoHibernate<OnmsAssetRecord, Integer> implements
		AssetRecordDao {

	/**
	 * <p>
	 * Constructor for AssetRecordDaoHibernate.
	 * </p>
	 */
	public AssetRecordDaoHibernate() {
		super(OnmsAssetRecord.class);
	}

	/**
	 * <p>
	 * findByNodeId
	 * </p>
	 * 
	 * @param id
	 *            a {@link java.lang.Integer} object.
	 * @return a {@link org.opennms.netmgt.model.OnmsAssetRecord} object.
	 */
	public OnmsAssetRecord findByNodeId(Integer id) {
		return (OnmsAssetRecord) findUnique(
				"from OnmsAssetRecord rec where rec.nodeId = ?", id);
	}

	/**
	 * <p>
	 * findImportedAssetNumbersToNodeIds
	 * </p>
	 * 
	 * @param foreignSource
	 *            a {@link java.lang.String} object.
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, Integer> findImportedAssetNumbersToNodeIds(
			String foreignSource) {

		@SuppressWarnings("unchecked")
		List<Object[]> assetNumbers = getHibernateTemplate()
				.find("select a.node.id, a.assetNumber from OnmsAssetRecord a where a.assetNumber like '"
						+ foreignSource + "%'");

		Map<String, Integer> assetNumberMap = new HashMap<String, Integer>();
		for (Object[] an : assetNumbers) {
			assetNumberMap.put((String) an[1], (Integer) an[0]);
		}
		return assetNumberMap;
	}

	@Override
	public List<String> getDistinctAdditionalhardware() {
		String hql = "select distinct a.additionalhardware from OnmsAssetRecord a order by a.additionalhardware asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctAddress1() {
		String hql = "select distinct a.address1 from OnmsAssetRecord a order by a.address1 asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctAddress2() {
		String hql = "select distinct a.address2 from OnmsAssetRecord a order by a.address2 asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctAdmin() {
		String hql = "select distinct a.admin from OnmsAssetRecord a order by a.admin asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctAssetCategory() {
		String hql = "select distinct a.category from OnmsAssetRecord a order by a.category asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctBuilding() {
		String hql = "select distinct a.building from OnmsAssetRecord a order by a.building asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctCategory() {
		String hql = "select distinct a.category from OnmsAssetRecord a order by a.category asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctCircuitId() {
		String hql = "select distinct a.circuitId from OnmsAssetRecord a order by a.circuitId asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctCity() {
		String hql = "select distinct a.city from OnmsAssetRecord a order by a.city asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctDepartment() {
		String hql = "select distinct a.department from OnmsAssetRecord a order by a.department asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctDescription() {
		String hql = "select distinct a.description from OnmsAssetRecord a order by a.description asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctDisplayCategory() {
		String hql = "select distinct a.displayCategory from OnmsAssetRecord a order by a.displayCategory asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctDivision() {
		String hql = "select distinct a.division from OnmsAssetRecord a order by a.division asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctFloor() {
		String hql = "select distinct a.floor from OnmsAssetRecord a order by a.floor asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctInputpower() {
		String hql = "select distinct a.inputpower from OnmsAssetRecord a order by a.inputpower asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctLease() {
		String hql = "select distinct a.lease from OnmsAssetRecord a order by a.lease asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctMaintContract() {
		String hql = "select distinct a.maintContractNumber from OnmsAssetRecord a order by a.maintContractNumber asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctManufacturer() {
		// TODO Auto-generated method stub
		String hql = "select distinct a.manufacturer from OnmsAssetRecord a order by a.manufacturer asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctModelNumber() {
		String hql = "select distinct a.modelNumber from OnmsAssetRecord a order by a.modelNumber asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctNotificationCategory() {
		String hql = "select distinct a.notifyCategory from OnmsAssetRecord a order by a.notifyCategory asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctOs() {
		String hql = "select distinct a.operatingSystem from OnmsAssetRecord a order by a.operatingSystem asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctPollerCategory() {
		String hql = "select distinct a.pollerCategory from OnmsAssetRecord a order by a.pollerCategory asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctRack() {
		String hql = "select distinct a.rack from OnmsAssetRecord a order by a.rack asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctRegion() {
		String hql = "select distinct a.region from OnmsAssetRecord a order by a.region asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctRoom() {
		String hql = "select distinct a.room from OnmsAssetRecord a order by a.room asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctSnmpcommunity() {
		String hql = "select distinct a.snmpcommunity from OnmsAssetRecord a order by a.snmpcommunity asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctState() {
		String hql = "select distinct a.state from OnmsAssetRecord a order by a.state asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctStoragectrl() {
		String hql = "select distinct a.storagectrl from OnmsAssetRecord a order by a.storagectrl asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctSupportPhone() {
		String hql = "select distinct a.supportPhone from OnmsAssetRecord a order by a.supportPhone asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctThresholdCategory() {
		String hql = "select distinct a.thresholdCategory from OnmsAssetRecord a order by a.thresholdCategory asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctVendor() {
		String hql = "select distinct a.vendor from OnmsAssetRecord a order by a.vendor asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctVendorAsset() {
		String hql = "select distinct a.vendor from OnmsAssetRecord a order by a.vendor asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctVendorFax() {
		String hql = "select distinct a.vendorFax from OnmsAssetRecord a order by a.vendorFax asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctVendorPhone() {
		String hql = "select distinct a.vendorPhone from OnmsAssetRecord a order by a.vendorPhone asc";
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<String> getDistinctZip() {
		String hql = "select distinct a.zip from OnmsAssetRecord a order by a.zip asc";
		return getHibernateTemplate().find(hql);
	}
}
