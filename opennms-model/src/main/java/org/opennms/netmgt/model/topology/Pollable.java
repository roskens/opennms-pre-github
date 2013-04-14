package org.opennms.netmgt.model.topology;

import java.util.Date;

public abstract class Pollable {

	Date m_lastPoll;

	public Date getLastPoll() {
		return m_lastPoll;
	}

	public void setLastPoll(Date lastPoll) {
		m_lastPoll = lastPoll;
	}
	
	public Pollable (Date now) {
		m_lastPoll = now;
	}
}
