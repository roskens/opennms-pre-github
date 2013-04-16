package org.opennms.netmgt.model.topology;

import org.apache.commons.lang.builder.ToStringBuilder;

public final class NodeElementIdentifier extends ElementIdentifier {

	private final Integer m_nodeid; 

	public NodeElementIdentifier(Integer nodeid) {
		super(ElementIdentifierType.ONMSNODE);
		m_nodeid = Integer.valueOf(nodeid);
	}

	public Integer getNodeid() {
		return m_nodeid;
	}

	@Override
	public boolean equals(ElementIdentifier elementIdentifier) {
		if (elementIdentifier instanceof NodeElementIdentifier) 
			return (m_nodeid == ((NodeElementIdentifier)elementIdentifier).getNodeid());
		return false;
	}
	
	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("nodeid", m_nodeid)
			.append("lastPoll", m_lastPoll)
			.toString();
	}

}
