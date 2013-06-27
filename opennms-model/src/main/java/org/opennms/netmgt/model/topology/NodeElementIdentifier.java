package org.opennms.netmgt.model.topology;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@DiscriminatorValue("NODE")
public final class NodeElementIdentifier extends ElementIdentifier {

	public final static String NODE_IDENTIFIER_DISPLAY = "OpenNMS node";
	private Integer m_nodeid; 

	public NodeElementIdentifier(Integer nodeid) {
		super(nodeid);
		m_nodeid = Integer.valueOf(nodeid);
		setId(m_nodeid.toString());
	}

	public Integer getNodeid() {
		return m_nodeid;
	}

	public void setNodeid(Integer nodeid) {
		m_nodeid = nodeid;
	}

	@Override
	public boolean equals(ElementIdentifier elementIdentifier) {
		if (elementIdentifier instanceof NodeElementIdentifier) {
			NodeElementIdentifier nodeElementIdentifier = (NodeElementIdentifier)elementIdentifier;
			if (m_nodeid.intValue() == nodeElementIdentifier.getNodeid().intValue()) {
				return true;
			}
		}
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
			.append("sourceNode", m_sourceNode)
			.toString();
	}

	@Override
	public String displayElementidentifierType() {
		return NODE_IDENTIFIER_DISPLAY;
	}

}
