/**
 * 
 */
package org.opennms.features.topology.plugins.topo.vmware.internal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;


abstract public class VmwareVertex {
    String m_id;
    int m_x;
    int m_y;
    boolean m_selected;
    boolean m_locked = false;
    String m_icon;
    String m_label = "none provided";
    VmwareGroup m_parent = null;
    List<VmwareEdge> m_edges = new ArrayList<VmwareEdge>();
    private String m_ipAddr ="127.0.0.1";
    private int m_nodeID = -1;
    //	private int m_semanticZoomLevel = -1;
    private String m_iconKey;

    public VmwareVertex() {}

    public VmwareVertex(String id) {
        m_id = id;
    }

    public VmwareVertex(String id, int x, int y) {
        m_id = id;
        m_x = x;
        m_y = y;
    }

    @XmlIDREF
    public VmwareGroup getParent() {
        return m_parent;
    }

    public void setParent(VmwareGroup parent) {
        if (m_parent != null) {
            m_parent.removeMember(this);
        }
        m_parent = parent;
        if (m_parent != null) {
            m_parent.addMember(this);
        }
    }

    public boolean isLocked() {
        return m_locked;
    }

    public void setLocked(boolean locked) {
        m_locked = locked;
    }

    public abstract boolean isLeaf();

    public boolean isRoot() {
        return m_parent == null;
    }

    @XmlID
    public String getId() {
        return m_id;
    }

    public void setId(String id) {
        m_id = id;
    }

    public int getX() {
        return m_x;
    }

    public void setX(int x) {
        m_x = x;
    }

    public int getY() {
        return m_y;
    }

    public void setY(int y) {
        m_y = y;
    }

    public boolean isSelected() {
        return m_selected;
    }

    public void setSelected(boolean selected) {
        m_selected = selected;
    }

    public String getIcon() {
        return m_icon;
    }

    public void setIcon(String icon) {
        m_icon = icon;
    }

    public String getLabel() {
        return m_label;
    }

    public void setLabel(String label) {
        m_label = label;
    }

    public String getIpAddr() {
        return m_ipAddr;
    }

    public void setIpAddr(String ipAddr){
        m_ipAddr = ipAddr;
    }

    public int getNodeID() {
        return m_nodeID;
    }

    public void setNodeID(int nodeID) {
        m_nodeID = nodeID;
    }

    @XmlTransient
    public List<VmwareEdge> getEdges() {
        return m_edges;
    }

    void addEdge(VmwareEdge edge) {
        m_edges.add(edge);
    }

    void removeEdge(VmwareEdge edge) {
        m_edges.remove(edge);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VmwareVertex other = (VmwareVertex) obj;
        if (m_id == null) {
            if (other.m_id != null)
                return false;
        } else if (!m_id.equals(other.m_id))
            return false;
        return true;
    }

    public void setIconKey(String iconKey) {
        m_iconKey = iconKey;
    }

    public String getIconKey() {
        return m_iconKey;
    }

//	public int getSemanticZoomLevel() {
//		return m_semanticZoomLevel >= 0
//				? m_semanticZoomLevel
//				: m_parent == null 
//				? 0 
//				: m_parent.getSemanticZoomLevel() + 1;
//	}

//	public VmwareVertex getDisplayVertex(int semanticZoomLevel) {
//		if(getParent() == null || getSemanticZoomLevel() <= semanticZoomLevel) {
//			return this;
//		}else {
//			return getParent().getDisplayVertex(semanticZoomLevel);
//		}
//
//	}


}