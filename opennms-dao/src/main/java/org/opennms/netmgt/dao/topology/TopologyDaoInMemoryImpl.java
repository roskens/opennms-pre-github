package org.opennms.netmgt.dao.topology;

import java.util.ArrayList;
import java.util.List;

import org.opennms.netmgt.dao.TopologyDao;
import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.Link;

public class TopologyDaoInMemoryImpl implements TopologyDao {

	private List<Element> m_elements;	
	private List<EndPoint> m_withoutelementendpoints;	
		
    public TopologyDaoInMemoryImpl() {
    	m_elements = new ArrayList<Element>();
    	m_withoutelementendpoints = new ArrayList<EndPoint>();
    }

	protected Element updateElementIdentifiers(Element elementUpdated, Element elementToUpdate) {
		
		for (ElementIdentifier elementIdentifierUpdated: elementUpdated.getElementIdentifiers()) {
			if (elementToUpdate.hasElementIdentifier(elementIdentifierUpdated)) {
				for (ElementIdentifier elementIdentifierToUpdate: elementToUpdate.getElementIdentifiers()) {
					if (elementIdentifierToUpdate.equals(elementIdentifierUpdated)) {
						elementIdentifierToUpdate.update(elementIdentifierUpdated);
						break;
					}
				}
			} else {
				elementToUpdate.addElementIdentifier(elementIdentifierUpdated);
			}
		}
		return elementToUpdate;
	}

	@Override
	public void saveOrUpdate(Link link) {
		saveOrUpdate(link.getA());
		saveOrUpdate(link.getB());
	}

	@Override
	public void saveOrUpdate(EndPoint endpoint) {
		if (endpoint.hasElement())
			saveOrUpdateWithElement(endpoint);
		else if (endpoint.hasLink()) 
			saveOrUpdateWithLink(endpoint);
	}
	
	protected void saveOrUpdateWithLink(EndPoint endpoint) {
		for (Element e : m_elements) {
			for (EndPoint ep: e.getEndpoints()) {
				if (ep.equals(endpoint)) {
					ep.update(endpoint);
					return;
				}
			}
		}
		m_withoutelementendpoints.add(endpoint);
	}
	
	protected void saveOrUpdateWithElement(EndPoint endpoint) {
		for (Element e: m_elements) {
			if (e.equals(endpoint.getElement())) {
				endpoint.setElement(updateElementIdentifiers(endpoint.getElement(),e));
				if (e.hasEndPoint(endpoint)) {
					EndPoint ep = e.getEndPoint(endpoint);
					ep.update(endpoint);
				} else {
					for (EndPoint withoutelementendpoint: m_withoutelementendpoints) {
						if (withoutelementendpoint.equals(endpoint)) {
							withoutelementendpoint.update(endpoint);
							withoutelementendpoint.setElement(e);
							e.addEndPoint(withoutelementendpoint);
							m_withoutelementendpoints.remove(withoutelementendpoint);
							return;
						}
					}
					e.addEndPoint(endpoint);
				}
				return;
			}
		}
		m_elements.add(endpoint.getElement());
	}

	@Override
	public void delete(Element element) {
		m_elements.remove(element);
	}

	@Override
	public void delete(Link link) {
		for (Element e: m_elements) {
			for (EndPoint ep: e.getEndpoints()) {
				if (ep.hasLink() && (ep.equals(link.getA()) || ep.equals(link.getB()))) {
					ep.setLink(null);
					break;
				}
			}
		}
		
		for (EndPoint ep: m_withoutelementendpoints) {
			if (ep.hasLink() && (ep.equals(link.getA()) || ep.equals(link.getB()))) {
				ep.setLink(null);
			}
		}

	}

	@Override
	public void delete(EndPoint endpoint) {
		if (endpoint.hasLink())
			delete(endpoint.getLink());
		for (Element e: m_elements) {
			List<EndPoint> newendpoints = new ArrayList<EndPoint>(); 
			for (EndPoint ep: e.getEndpoints()) {
				if (ep.equals(endpoint)) {
					continue;
				}
				newendpoints.add(ep);
			}
			e.setEndpoints(newendpoints);
		}
		List<EndPoint> newendpoints = new ArrayList<EndPoint>(); 
		for (EndPoint ep: m_withoutelementendpoints) {
			if (endpoint.equals(ep)) {
				continue;
			}
			newendpoints.add(ep);
		}
		m_withoutelementendpoints = newendpoints;
	}

	@Override
	public List<Element> getTopology() {
		return m_elements;
	}

}
