package org.opennms.netmgt.model.entopology;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * An ElementIdentifier identifies a physical device with endpoints, like a
 * switch, a router, a host.
 * 
 * @author thargor
 */
// FIXME should we call this device?
@MappedSuperclass
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Discriminator")
public abstract class ElementIdentifier implements Pollable {
    
    @Id
	protected Long m_id;

    @Temporal(TemporalType.TIMESTAMP)
	protected Date m_lastPoll = new Date();

    // FIXME
    @Transient
	protected Set<Integer> m_sourceNodes = new HashSet<Integer>();

	@OneToMany
	protected Set<EndPoint> m_endpoints;
    
	public ElementIdentifier(Integer sourceNode) {
        m_sourceNodes.add(sourceNode);
    }

    
    public Long getId() {
		return m_id;
	}

	protected void setId(Long id) {
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
