package org.opennms.netmgt.model.entopology;

import java.util.Set;

public abstract class ElementIdentifier extends Pollable {

	private Set<EndPoint> m_endpoints;
	
	String m_id;

	public String getId() {
		return m_id;
	}

	protected void setId(String id) {
		m_id = id;
	}

	public ElementIdentifier(Integer sourceNode) {
		super(sourceNode);
	}
		
	public void setEndPoints(Set<EndPoint> endpoints) {
		m_endpoints = endpoints;
	}
	
	public Set<EndPoint> getEndPoints() {
		return m_endpoints;
	}
	
	public void addEndPoint(EndPoint endpoint) {
		m_endpoints.add(endpoint);
	}

	public abstract String displayElementidentifierType();
	
}
