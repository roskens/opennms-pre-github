package org.opennms.features.topology.plugins.topo.vmware.internal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="vertex")
public class VmwareLeafVertex extends VmwareVertex {

	public VmwareLeafVertex() {}
	
	public VmwareLeafVertex(String id, int x, int y) {
		super(id, x, y);
	}

	@Override
	public boolean isLeaf() {
		return true;
	}
	

}
