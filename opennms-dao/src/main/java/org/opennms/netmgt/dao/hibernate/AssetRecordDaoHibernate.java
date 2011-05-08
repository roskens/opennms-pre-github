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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
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
	public List<OnmsAssetRecord> getDistinctProperties() {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(OnmsAssetRecord.class);
		ProjectionList projList = Projections.projectionList();

		projList.add(Projections.alias(
				Projections.property("additionalhardware"),
				"additionalhardware"));
		projList.add(Projections.alias(Projections.property("address1"),
				"address1"));
		projList.add(Projections.alias(Projections.property("address2"),
				"address2"));
		projList.add(Projections.alias(Projections.property("admin"), "admin"));
		projList.add(Projections.alias(Projections.property("assetNumber"),
				"assetNumber"));
		projList.add(Projections.alias(Projections.property("autoenable"),
				"autoenable"));
		projList.add(Projections.alias(Projections.property("building"),
				"building"));
		projList.add(Projections.alias(Projections.property("category"),
				"category"));
		projList.add(Projections.alias(Projections.property("circuitId"),
				"circuitId"));
		projList.add(Projections.alias(Projections.property("city"), "city"));
		projList.add(Projections.alias(Projections.property("comment"),
				"comment"));
		projList.add(Projections.alias(Projections.property("connection"),
				"connection"));
		projList.add(Projections.alias(Projections.property("cpu"), "cpu"));
		projList.add(Projections.alias(Projections.property("department"),
				"department"));
		projList.add(Projections.alias(Projections.property("description"),
				"description"));
		projList.add(Projections.alias(Projections.property("displayCategory"),
				"displayCategory"));
		projList.add(Projections.alias(Projections.property("division"),
				"division"));
		projList.add(Projections.alias(Projections.property("enable"), "enable"));
		projList.add(Projections.alias(Projections.property("floor"), "floor"));
		projList.add(Projections.alias(Projections.property("hdd1"), "hdd1"));
		projList.add(Projections.alias(Projections.property("hdd2"), "hdd2"));
		projList.add(Projections.alias(Projections.property("hdd3"), "hdd3"));
		projList.add(Projections.alias(Projections.property("hdd4"), "hdd4"));
		projList.add(Projections.alias(Projections.property("hdd5"), "hdd5"));
		projList.add(Projections.alias(Projections.property("hdd6"), "hdd6"));
		projList.add(Projections.alias(Projections.property("inputpower"),
				"inputpower"));
		projList.add(Projections.alias(Projections.property("lease"), "lease"));
		projList.add(Projections.alias(Projections.property("maintcontract"),
				"maintcontract"));
		projList.add(Projections.alias(Projections.property("manufacturer"),
				"manufacturer"));
		projList.add(Projections.alias(Projections.property("modelNumber"),
				"modelNumber"));
		projList.add(Projections.alias(Projections.property("notifyCategory"),
				"notifyCategory"));
		projList.add(Projections.alias(
				Projections.property("numpowersupplies"), "numpowersupplies"));
		projList.add(Projections.alias(Projections.property("operatingSystem"),
				"operatingSystem"));
		projList.add(Projections.alias(Projections.property("pollerCategory"),
				"pollerCategory"));
		projList.add(Projections.alias(Projections.property("port"), "port"));
		projList.add(Projections.alias(Projections.property("rack"), "rack"));
		projList.add(Projections.alias(Projections.property("ram"), "ram"));
		projList.add(Projections.alias(Projections.property("region"), "region"));
		projList.add(Projections.alias(Projections.property("room"), "room"));
		projList.add(Projections.alias(Projections.property("serialNumber"),
				"serialNumber"));
		projList.add(Projections.alias(Projections.property("slot"), "slot"));
		projList.add(Projections.alias(Projections.property("snmpcommunity"),
				"snmpcommunity"));
		projList.add(Projections.alias(Projections.property("state"), "state"));
		projList.add(Projections.alias(Projections.property("storagectrl"),
				"storagectrl"));
		projList.add(Projections.alias(Projections.property("supportPhone"),
				"supportPhone"));
		projList.add(Projections.alias(
				Projections.property("thresholdCategory"), "thresholdCategory"));
		projList.add(Projections.alias(Projections.property("username"),
				"username"));
		projList.add(Projections.alias(Projections.property("vendor"), "vendor"));
		projList.add(Projections.alias(
				Projections.property("vendorAssetNumber"), "vendorAssetNumber"));
		projList.add(Projections.alias(Projections.property("vendorFax"),
				"vendorFax"));
		projList.add(Projections.alias(Projections.property("vendorPhone"),
				"vendorPhone"));
		projList.add(Projections.alias(Projections.property("zip"), "zip"));

		criteria.setProjection(Projections.distinct(projList));
		criteria.setResultTransformer(Transformers
				.aliasToBean(OnmsAssetRecord.class));

		List<OnmsAssetRecord> result = getHibernateTemplate().findByCriteria(
				criteria);
		return result;
	}
}
