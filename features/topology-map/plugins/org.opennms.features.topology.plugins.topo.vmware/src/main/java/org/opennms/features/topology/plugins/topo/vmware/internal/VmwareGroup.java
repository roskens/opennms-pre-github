package org.opennms.features.topology.plugins.topo.vmware.internal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="group")
public class VmwareGroup extends VmwareVertex {

	List<VmwareVertex> m_members = new ArrayList<VmwareVertex>();
	
	
	public VmwareGroup() {}
	
	public VmwareGroup(String id) {
		super(id);
	}

	@Override
	public boolean isLeaf() {
		return false;
	}
	
	@XmlIDREF
	public List<VmwareVertex> getMembers() {
		return m_members;
	}
	
	public void addMember(VmwareVertex v) {
		m_members.add(v);
	}
	
	public void removeMember(VmwareVertex v) {
		m_members.remove(v);
	}

}
