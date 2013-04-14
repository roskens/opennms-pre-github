package org.opennms.netmgt.dao;

import java.util.List;

import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.ElementIdentifier.ElementIdentifierType;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.Link;
import org.opennms.netmgt.model.topology.Link.LinkType;

public interface TopologyDao {
	
	public void saveOrUpdate(Element element);

	public void saveOrUpdate(Link link);
	
	public void saveOrUpdate(EndPoint endpoint);
	
	public void delete(Element element);
	
	public void delete(Link link);
	
	public void delete(EndPoint endpoint);
	
	public Element get(ElementIdentifier elementIdentifier);
	
	public List<Element> get(ElementIdentifierType type);
	
	public List<Link> get(LinkType type);

}
