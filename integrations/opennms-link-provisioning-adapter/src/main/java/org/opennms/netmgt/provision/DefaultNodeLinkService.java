package org.opennms.netmgt.provision;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Transient;

import org.hibernate.criterion.Restrictions;
import org.opennms.netmgt.dao.DataLinkInterfaceDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.DataLinkInterface;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsNode;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

public class DefaultNodeLinkService implements NodeLinkService {
    
    @Autowired
    NodeDao m_nodeDao;
    
    @Autowired
    DataLinkInterfaceDao m_dataLinkDao;
    
    @Transactional
    public void createLink(int pointOne, int pointTwo) {
        OnmsNode parentNode = m_nodeDao.get(pointOne);
        Assert.notNull(parentNode, "node with id: " + pointOne + " does not exist");
        
        OnmsNode node = m_nodeDao.get(pointTwo);
        Assert.notNull(node, "node with id: " + pointTwo + " does not exist");
        
        OnmsCriteria criteria = new OnmsCriteria(DataLinkInterface.class);
        criteria.add(Restrictions.eq("nodeId", pointTwo));
        criteria.add(Restrictions.eq("nodeParentId", pointOne));
        
        Collection<DataLinkInterface> dataLinkInterface = m_dataLinkDao.findMatching(criteria);
        
        if(dataLinkInterface.size() <= 0){
            DataLinkInterface dataLink = new DataLinkInterface();
            dataLink.setNodeId(pointTwo);
            dataLink.setNodeParentId(pointOne);
            dataLink.setIfIndex(getPrimaryIfIndexForNode(node));
            dataLink.setParentIfIndex(getPrimaryIfIndexForNode(parentNode));
            dataLink.setStatus("A");
            dataLink.setLastPollTime(new Date());
            
            m_dataLinkDao.save(dataLink);
            m_dataLinkDao.flush();
        }
        
    }
    
    private int getPrimaryIfIndexForNode(OnmsNode node) {
        if(node.getPrimaryInterface() != null && node.getPrimaryInterface().getIfIndex() != null){
            return node.getPrimaryInterface().getIfIndex();
        }else{
            return -1;
        }
    }

    public Integer getNodeId(String endPoint) {
        Collection<OnmsNode> nodes = m_nodeDao.findByLabel(endPoint);
        
        if(nodes.size() > 0){
            return nodes.iterator().next().getId();
        }
        return null;
    }

    public String getNodeLabel(int nodeId) {
        OnmsNode node = m_nodeDao.get(nodeId);
        if(node != null){
            return node.getLabel(); 
        }
        return null;
    }

    public void updateLinkStatus(int nodeId, int parentNodeId, String status) {
        OnmsCriteria criteria = new OnmsCriteria(DataLinkInterface.class);
        criteria.add(Restrictions.eq("nodeId", nodeId));
        criteria.add(Restrictions.eq("nodeParentId", parentNodeId));
        
        Collection<DataLinkInterface> dataLinkInterface = m_dataLinkDao.findMatching(criteria);
        
        if(dataLinkInterface.size() > 0){
            DataLinkInterface dataLink = dataLinkInterface.iterator().next();
            dataLink.setStatus(status);
            
            m_dataLinkDao.update(dataLink);
            m_dataLinkDao.flush();
        }
    }

}
