package org.opennms.netmgt.model.topology;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * This class is a container of end points.  In the network, this
 * can be either a physical or virtual node/device/subnetwork/etc.
 */
@Entity
@Table(name="topologyElement")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Element {
	
	private int m_id;

	private List<EndPoint> m_endpoints;

	private List<ElementIdentifier> m_identifiers;

	public Element() {
		m_endpoints = new ArrayList<EndPoint>();
		m_identifiers = new ArrayList<ElementIdentifier>();
	}
	
	@Id
    @SequenceGenerator(name="opennmsSequence", sequenceName="opennmsNxtId")
    @GeneratedValue(generator="opennmsSequence")
	public int getId() {
		return m_id;
	}

	public void setId(int m_id) {
		this.m_id = m_id;
	}

	@OneToMany
	public List<EndPoint> getEndpoints() {
		return m_endpoints;
	}

	public void setEndpoints(List<EndPoint> endpoints) {
		m_endpoints = endpoints;
	}
	
	@OneToMany
	public List<ElementIdentifier> getElementIdentifiers() {
		return m_identifiers;
	}

	public void setElementIdentifiers(List<ElementIdentifier> identifiers) {
		m_identifiers = identifiers;
	}

	public boolean hasElementIdentifier(ElementIdentifier elementidentifier) {
		return m_identifiers.contains(elementidentifier);
	}
	
	public void removeElementIdentifier(ElementIdentifier elementidentifier) {
		m_identifiers.remove(elementidentifier);
	}
	
	public void addElementIdentifier(ElementIdentifier elementidentifier) {
		m_identifiers.add(elementidentifier);
	}

	public boolean hasEndPoint(EndPoint endPoint) {
		return m_endpoints.contains(endPoint);
	}
	
	public void removeEndPoint(EndPoint endPoint) {
		m_endpoints.remove(endPoint);
	}

	public EndPoint getEndPoint(EndPoint endPoint) {
		for (EndPoint e: m_endpoints) {
			if (endPoint.equals(e)) {
				return e;
			}
		}
		return null;
	}

	public void addEndPoint(EndPoint endPoint) {
		endPoint.setElement(this);
		m_endpoints.add(endPoint);
	}
	
	public boolean equals(Object o) {
		if ( o instanceof Element) {
			Element e = (Element) o;
			if (e.getElementIdentifiers().size() == 0 && 
					getElementIdentifiers().size() == 0)
					return false;
			for (ElementIdentifier localElementIdentifier : getElementIdentifiers()) {
				for (ElementIdentifier oe: e.getElementIdentifiers()) {
					if (oe.equals(localElementIdentifier))
						return true;
				}
			}
		}
		return false;
	}
	
	public String toString() {
		ToStringBuilder tosb = new ToStringBuilder(this);
		for (ElementIdentifier ei: m_identifiers) {
			tosb.append("\nElementIdentifier",ei);
		}
		for (EndPoint ep: m_endpoints) {
			tosb.append("\nEndPoint",ep);
		}
		return tosb.toString();
	}
}
