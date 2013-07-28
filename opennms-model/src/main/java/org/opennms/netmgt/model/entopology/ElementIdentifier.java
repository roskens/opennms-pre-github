package org.opennms.netmgt.model.entopology;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 
 * @author thargor
 */
// FIXME isn't this just an element?
// FIXME this should go into its own table
public abstract class ElementIdentifier implements Pollable {
    
    // FIXME id should always be a int/long
	protected String m_id;

	protected Date m_lastPoll = new Date();

	protected Set<Integer> m_sourceNodes = new HashSet<Integer>();

	protected Set<EndPoint> m_endpoints;
    
	public ElementIdentifier(Integer sourceNode) {
        m_sourceNodes.add(sourceNode);
    }

    
    public String getId() {
		return m_id;
	}

	protected void setId(String id) {
		m_id = id;
	}
		
	public void setEndPoints(Set<EndPoint> endpoints) {
		m_endpoints = endpoints;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<EndPoint> getEndPoints() {
		return m_endpoints;
	}
	
	public void addEndPoint(EndPoint endpoint) {
		m_endpoints.add(endpoint);
	}

	public abstract String displayElementidentifierType();

    @Override
    public Set<Integer> getSourceNodes() {
        return m_sourceNodes;
    }

    @Override
    public void setSourceNodes(Set<Integer> sourceNodes) {
        m_sourceNodes = sourceNodes;
    }

    @Override
    public Date getLastPoll() {
        return m_lastPoll;
    }

    @Override
    public void setLastPoll(Date lastPoll) {
        m_lastPoll = lastPoll;
    }
	
}
