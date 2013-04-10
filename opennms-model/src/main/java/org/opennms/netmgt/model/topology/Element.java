package org.opennms.netmgt.model.topology;

import java.util.List;


/**
 * This class is a container of end points.  In the network, this
 * can be either a physical or virtual node/device/subnetwork/etc.
 */
public class Element {

	private Integer m_id;
	
	private List<EndPoint> m_endpoints;

	private List<ElementIdentifier> m_identifiers;

	public List<EndPoint> getEndpoints() {
		return m_endpoints;
	}

	public void setEndpoints(List<EndPoint> endpoints) {
		m_endpoints = endpoints;
	}
	
	public List<ElementIdentifier> getElementIdentifiers() {
		return m_identifiers;
	}

	public void addElementIdentifier(ElementIdentifier elementidentifier) {
		m_identifiers.add(elementidentifier);
	}

	public void setId(Integer id) {
		m_id = id;
	}
	
	public Integer getId() {
		return m_id;
	}

}
