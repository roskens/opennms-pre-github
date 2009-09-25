package org.opennms.netmgt.provision;

public interface NodeLinkService {
    
    public String getNodeLabel(int nodeId);
    public void createLink(int pointOne, int pointTwo);
    public Integer getNodeId(String endPoint);
    public void updateLinkStatus(int nodeId, int parentNodeId, String status);
    
}
