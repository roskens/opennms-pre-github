package org.opennms.features.topology.plugins.ncs;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.opennms.core.criteria.Criteria;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.*;

public class MockNodeDao implements NodeDao {

    @Override
    public void lock() {
       
    }


    @Override
    public void flush() {
       

    }

    @Override
    public void clear() {
       

    }

    @Override
    public int countAll() {
       
        return 0;
    }

    @Override
    public void delete(OnmsNode entity) {
       

    }

    @Override
    public void delete(Integer key) {
       

    }

    @Override
    public List<OnmsNode> findMatching(Criteria criteria) {
       
        return null;
    }

    @Override
    public List<OnmsNode> findMatching(OnmsCriteria criteria) {
       
        return null;
    }

    @Override
    public int countMatching(Criteria onmsCrit) {
       
        return 0;
    }

    @Override
    public int countMatching(OnmsCriteria onmsCrit) {
       
        return 0;
    }

    @Override
    public List<OnmsNode> findAll(int limit, int offset) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OnmsNode get(Integer id) {
       
        return null;
    }

    @Override
    public OnmsNode load(Integer id) {
       
        return null;
    }

    @Override
    public void save(OnmsNode entity) {
       

    }

    @Override
    public void saveOrUpdate(OnmsNode entity) {
       

    }

    @Override
    public void update(OnmsNode entity) {
       

    }

    @Override
    public OnmsNode get(String lookupCriteria) {
       
        return null;
    }

    @Override
    public String getLabelForId(Integer id) {
       
        return null;
    }

    @Override
    public List<OnmsNode> findByLabel(String label) {
       
        return null;
    }

    @Override
    public List<OnmsNode> findNodes(OnmsDistPoller dp) {
       
        return null;
    }

    @Override
    public OnmsNode getHierarchy(Integer id) {
       
        return null;
    }

    @Override
    public Map<String, Integer> getForeignIdToNodeIdMap(String foreignSource) {
       
        return null;
    }

    @Override
    public List<OnmsNode> findAllByVarCharAssetColumn(String columnName,
            String columnValue) {
       
        return null;
    }

    @Override
    public List<OnmsNode> findAllByVarCharAssetColumnCategoryList(
            String columnName, String columnValue,
            Collection<OnmsCategory> categories) {
       
        return null;
    }

    @Override
    public List<OnmsNode> findByCategory(OnmsCategory category) {
       
        return null;
    }

    @Override
    public List<OnmsNode> findAllByCategoryList(
            Collection<OnmsCategory> categories) {
       
        return null;
    }

    @Override
    public List<OnmsNode> findAllByCategoryLists(
            Collection<OnmsCategory> rowCatNames,
            Collection<OnmsCategory> colCatNames) {
       
        return null;
    }

    @Override
    public List<OnmsNode> findAll() {
       
        return null;
    }

    @Override
    public List<OnmsNode> findByForeignSource(String foreignSource) {
       
        return null;
    }

    @Override
    public OnmsNode findByForeignId(String foreignSource, String foreignId) {
        OnmsNode node = new OnmsNode();
        node.setId((int)(Math.random()* 100));
        node.setLabel("node-label");
        return node;
    }

    @Override
    public int getNodeCountForForeignSource(String groupName) {
       
        return 0;
    }

    @Override
    public List<OnmsNode> findAllProvisionedNodes() {
       
        return null;
    }

    @Override
    public List<OnmsIpInterface> findObsoleteIpInterfaces(Integer nodeId,
            Date scanStamp) {
       
        return null;
    }

    @Override
    public void deleteObsoleteInterfaces(Integer nodeId, Date scanStamp) {
       

    }

    @Override
    public void updateNodeScanStamp(Integer nodeId, Date scanStamp) {
       

    }

    @Override
    public Collection<Integer> getNodeIds() {
       
        return null;
    }

    @Override
    public List<OnmsNode> findByForeignSourceAndIpAddress(String foreignSource,
            String ipAddress) {
       
        return null;
    }

    @Override
    public SurveillanceStatus findSurveillanceStatusByCategoryLists(
            Collection<OnmsCategory> rowCategories,
            Collection<OnmsCategory> columnCategories) {
       
        return null;
    }

    @Override
    public List<OnmsNode> findBySysName(String sysName) {
        return null;
    }

    @Override
    public List<OnmsNode> findByTypeAndIsSnmpPrimary(String type, PrimaryType snmpPrimaryType) {
        return null;
    }

    @Override
    public List<OnmsNode> findByTypeAndIsSnmpPrimaryAndNodeId(String type, PrimaryType snmpPrimaryType, int nodeid) {
        return null;
    }
}
