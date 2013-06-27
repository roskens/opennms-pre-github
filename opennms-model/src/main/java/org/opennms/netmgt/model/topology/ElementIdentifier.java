package org.opennms.netmgt.model.topology;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="identifierType")
public abstract class ElementIdentifier extends Pollable {

	private TopologyElement m_element;
	
	String m_id;

	@Id
	public String getId() {
		return m_id;
	}

	protected void setId(String id) {
		m_id = id;
	}

	public ElementIdentifier(Integer sourceNode) {
		super(sourceNode);
	}
		
	public void setTopologyElement(TopologyElement element) {
		m_element = element;
	}
	
    @ManyToOne(optional=false, fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name="element_id")
	public TopologyElement getTopologyElement() {
		return m_element;
	}

	public boolean equals (Object o) {
		if (o instanceof ElementIdentifier) {
				return equals((ElementIdentifier)o);
		}
		return false;
	}

	public abstract boolean equals(ElementIdentifier elementIdentifier);
	
	public void update(ElementIdentifier elementidentifier) {
		if (!equals(elementidentifier))
			return;
		m_lastPoll = elementidentifier.getLastPoll();
	}
	
	public abstract String displayElementidentifierType();
	
}
