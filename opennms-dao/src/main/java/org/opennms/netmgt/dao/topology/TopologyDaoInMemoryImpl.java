package org.opennms.netmgt.dao.topology;

import java.util.ArrayList;
import java.util.List;

import org.opennms.netmgt.dao.TopologyDao;
import org.opennms.netmgt.model.topology.TopologyElement;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.Link;

public class TopologyDaoInMemoryImpl implements TopologyDao {

	private List<TopologyElement> m_elements;	
	
    public TopologyDaoInMemoryImpl() {
    	m_elements = new ArrayList<TopologyElement>();
    }

	protected TopologyElement updateElementIdentifiers(TopologyElement elementUpdated, TopologyElement elementToUpdate) {
		
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
		for (TopologyElement e: m_elements) {
			if (e.equals(endpoint.getTopologyElement())) {
				endpoint.setTopologyElement(updateElementIdentifiers(endpoint.getTopologyElement(),e));
				if (e.hasEndPoint(endpoint)) {
					EndPoint ep = e.getEndPoint(endpoint);
					ep.update(endpoint);
				} else {
					e.addEndPoint(endpoint);
				}
				return;
			}
		}
		m_elements.add(endpoint.getTopologyElement());
	}

	@Override
	public void delete(TopologyElement element) {
		m_elements.remove(element);
	}

	@Override
	public void delete(Link link) {
		for (TopologyElement e: m_elements) {
			for (EndPoint ep: e.getEndpoints()) {
				if (ep.hasLink() && (ep.equals(link.getA()) || ep.equals(link.getB()))) {
					ep.setLink(null);
				}
			}
		}
	}

	@Override
	public void delete(EndPoint endpoint) {
		for (TopologyElement e: m_elements) {
			if (e == null)
				continue;
			List<EndPoint> newendpoints = new ArrayList<EndPoint>(); 
			for (EndPoint ep: e.getEndpoints()) {
				if (ep.equals(endpoint)) {
					continue;
				}
				newendpoints.add(ep);
			}
			e.setEndpoints(newendpoints);
		}
	}

	@Override
	public List<TopologyElement> getTopology() {
		return m_elements;
	}

	@Override
	public void delete(ElementIdentifier elementidentifier) {
		for (TopologyElement e: m_elements) {
			if (e.hasElementIdentifier(elementidentifier)) {
				e.removeElementIdentifier(elementidentifier);
			}
		}
	}

	@Override
	public TopologyElement get(ElementIdentifier elementIdentifier) {
		for (TopologyElement e: m_elements) {
			if (e.hasElementIdentifier(elementIdentifier)) {
				return e;
			}
		}
		return null;
	}

	@Override
	public EndPoint get(EndPoint endpoint) {
		for (TopologyElement e: m_elements) {
			if (e.hasEndPoint(endpoint)) {
				return e.getEndPoint(endpoint);
			}
		}
		return null;
	}
}
