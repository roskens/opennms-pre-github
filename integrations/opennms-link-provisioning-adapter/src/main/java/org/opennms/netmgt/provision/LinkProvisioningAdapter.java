/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2009 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * Modifications:
 * 
 * Created: September 24, 2009
 *
 * Copyright (C) 2009 The OpenNMS Group, Inc.  All rights reserved.
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
package org.opennms.netmgt.provision;


import org.apache.log4j.Category;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.events.annotations.EventHandler;
import org.opennms.netmgt.model.events.annotations.EventListener;
import org.opennms.netmgt.xml.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;


/**
 * This adapter automatically creates links between nodes based on an expression applied
 * to the node label (hostname)
 * 
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 *
 */
@EventListener(name="LinkProvisioningAdapter")
public class LinkProvisioningAdapter extends SimplerQueuedProvisioningAdapter {

    private static final String ADAPTER_NAME = "LinkAdapter";
    
    @Autowired
    private LinkMatchResolver m_linkMatchResolver;
    
    @Autowired
    private NodeLinkService m_nodeLinkService;
    
    public LinkProvisioningAdapter() {
        super(ADAPTER_NAME);
    }
    
    @Override
    public void init() {
        super.init();
        Assert.notNull(m_nodeLinkService, "nodeLinkService must not be null");
        Assert.notNull(m_linkMatchResolver, "linkMatchResolver must not be null");
    }

    public void doAddNode(int nodeId1) {
        String nodeLabel1 = m_nodeLinkService.getNodeLabel(nodeId1);
        String nodeLabel2 = m_linkMatchResolver.getAssociatedEndPoint(nodeLabel1);

        Integer nodeId2 = m_nodeLinkService.getNodeId(nodeLabel2);
        if(nodeId2 != null){
            if(nodeLabel1.compareTo(nodeLabel2) < 0){
                m_nodeLinkService.createLink(nodeId1, nodeId2);
            }else{
                m_nodeLinkService.createLink(nodeId2, nodeId1);
            }
        }
        
    }
    
    public void doUpdateNode(int nodeid) {
        
    }
    
    public void doDeleteNode(int nodeid) {
        
    }
    
    public void doNotifyConfigChange(int nodeid) {
        
    }
    
    @EventHandler(uei=EventConstants.DATA_LINK_FAILED_EVENT_UEI)
    public void dataLinkFailed(Event event){
        if(event.getUei().equals("uei.opennms.org/internal/linkd/dataLinkFailed")){
            int nodeId = m_nodeLinkService.getNodeId(event.getSource());
            int parentNodeId =  m_nodeLinkService.getNodeId(m_linkMatchResolver.getAssociatedEndPoint(event.getSource()));
            m_nodeLinkService.updateLinkStatus(nodeId, parentNodeId, "B");
        }
    }
    
    @EventHandler(uei=EventConstants.DATA_LINK_RESTORED_EVENT_UEI)
    public void dataLinkRestored(Event event){
        if(event.getUei().equals("uei.opennms.org/internal/linkd/dataLinkRestored")){
            int nodeId = m_nodeLinkService.getNodeId(event.getSource());
            int parentNodeId = m_nodeLinkService.getNodeId(m_linkMatchResolver.getAssociatedEndPoint(event.getSource()));
            m_nodeLinkService.updateLinkStatus(nodeId, parentNodeId, "G");
        }
    }
    
    
    private static Category log() {
        return ThreadCategory.getInstance(LinkProvisioningAdapter.class);
    }

    public void setLinkMatchResolver(LinkMatchResolver linkMatchResolver) {
        m_linkMatchResolver = linkMatchResolver;
    }

    public LinkMatchResolver getLinkMatchResolver() {
        return m_linkMatchResolver;
    }

    public void setNodeLinkService(NodeLinkService nodeLinkService) {
        m_nodeLinkService = nodeLinkService;
    }
    

}
