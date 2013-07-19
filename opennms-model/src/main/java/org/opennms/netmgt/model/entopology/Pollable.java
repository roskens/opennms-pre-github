package org.opennms.netmgt.model.entopology;

import java.util.Date;
import java.util.Set;

public abstract class Pollable {

	protected Date m_lastPoll;
	protected Set<Integer> m_sourceNodes;
	
	public Pollable (Integer sourceNode) {
		m_lastPoll = new Date();
		m_sourceNodes.add(sourceNode);
	}
	
	public Set<Integer> getSourceNode() {
		return m_sourceNodes;
	}

	public void setSourceNodes(Set<Integer> sourceNodes) {
		m_sourceNodes = sourceNodes;
	}

	public Date getLastPoll() {
		return m_lastPoll;
	}

	public void setLastPoll(Date lastPoll) {
		m_lastPoll = lastPoll;
	}
	
}
