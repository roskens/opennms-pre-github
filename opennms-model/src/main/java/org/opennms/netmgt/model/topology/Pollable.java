package org.opennms.netmgt.model.topology;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

@MappedSuperclass
public abstract class Pollable {

	Integer m_id;
	Date m_lastPoll;
	Integer m_sourceNode;
	
	public Pollable (Integer sourceNode) {
		m_lastPoll = new Date();
		m_sourceNode = sourceNode;
	}
	
	
	@Id
    @SequenceGenerator(name="opennmsSequence", sequenceName="opennmsNxtId")
    @GeneratedValue(generator="opennmsSequence")
	public Integer getId() {
		return m_id;
	}



	public void setId(Integer id) {
		m_id = id;
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
