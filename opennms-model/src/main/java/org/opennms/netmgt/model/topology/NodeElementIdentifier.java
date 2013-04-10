package org.opennms.netmgt.model.topology;

public final class NodeElementIdentifier extends ElementIdentifier {

	private Integer m_nodeid; 

	public NodeElementIdentifier(String nodeid) {
		super(nodeid);
		m_nodeid = Integer.valueOf(nodeid);
		setType(ElementIdentifierType.ONMSNODE);
	}

	public Integer getNodeid() {
		return m_nodeid;
	}

	public void setNodeid(Integer nodeid) {
		m_nodeid = nodeid;
	}
	
}
