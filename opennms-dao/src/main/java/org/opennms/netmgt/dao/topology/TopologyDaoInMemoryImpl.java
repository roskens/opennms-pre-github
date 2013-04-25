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
		
    public TopologyDaoInMemoryImpl() {
    	m_elements = new ArrayList<Element>();
    }

	protected Element updateElementIdentifier(Element element, Element e) {
		List<ElementIdentifier> eitoberemoved = new ArrayList<ElementIdentifier>();
		for (ElementIdentifier ei: e.getElementIdentifiers()) {
			if (element.hasElementIdentifier(ei)) {
				eitoberemoved.add(ei);
			}
		}
		for (ElementIdentifier remove: eitoberemoved) {
			e.removeElementIdentifier(remove);
		}
		for (ElementIdentifier add: element.getElementIdentifiers()) {
			e.addElementIdentifier(add);
		}
		return e;
	}

	@Override
	public void saveOrUpdate(Link link) {
		saveOrUpdate(link.getA());
		saveOrUpdate(link.getB());
	}

	@Override
	public void saveOrUpdate(EndPoint endpoint) {
		for (Element e: m_elements) {
			if (e.equals(endpoint.getDevice())) {
				endpoint.setDevice(updateElementIdentifier(endpoint.getDevice(),e));
				if (e.hasEndPoint(endpoint)) {
					EndPoint ep = e.getEndPoint(endpoint);
					ep.update(endpoint);
				}
				else
					e.addEndPoint(endpoint);
				return;
			}
		}
		m_elements.add(endpoint.getDevice());
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
	}

	@Override
	public List<Element> getTopology() {
		return m_elements;
	}

}
