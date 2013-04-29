package org.opennms.netmgt.model.topology;

import java.util.Date;

public abstract class Pollable {

	Date m_lastPoll;
	Integer m_sourceNode;
	
	public Pollable (Integer sourceNode) {
		m_lastPoll = new Date();
		m_sourceNode = sourceNode;
	}

	public Integer getSourceNode() {
		return m_sourceNode;
	}

	public void setSourceNode(Integer sourceNode) {
		m_sourceNode = sourceNode;
	}

	public Date getLastPoll() {
		return m_lastPoll;
	}

	public void setLastPoll(Date lastPoll) {
		m_lastPoll = lastPoll;
	}
	

}
