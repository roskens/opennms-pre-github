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

package org.opennms.netmgt.dao.hibernate;

import org.opennms.netmgt.dao.AssetRecordDao;
import org.opennms.netmgt.model.OnmsAssetRecord;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetRecordDaoHibernate extends AbstractDaoHibernate<OnmsAssetRecord, Integer> implements AssetRecordDao {

    public AssetRecordDaoHibernate() {
        super(OnmsAssetRecord.class);
    }

    @Override
    public OnmsAssetRecord findByNodeId(Integer id) {
        return  findUnique("from OnmsAssetRecord rec where rec.node.id = ?", id);
    }

    @Override
    public Map<String, Integer> findImportedAssetNumbersToNodeIds(String foreignSource) {
        List<Object[]> assetNumbers = getJpaTemplate().find("select a.node.id, a.assetNumber from OnmsAssetRecord a where a.assetNumber like '" + foreignSource + "%'");
        Map<String, Integer> assetNumberMap = new HashMap<String, Integer>();
        for (Object[] an : assetNumbers) {
            assetNumberMap.put((String) an[1], (Integer) an[0]);
        }
        return Collections.unmodifiableMap(assetNumberMap);
    }

    @Override
    // TODO MVR JPA build test for this
    public List<OnmsAssetRecord> getDistinctProperties() {
        return getJpaTemplate().getEntityManager().createQuery(
                getJpaTemplate().getEntityManager().getCriteriaBuilder()
                .createQuery(OnmsAssetRecord.class)
                .distinct(true)).getResultList();
    }
}
