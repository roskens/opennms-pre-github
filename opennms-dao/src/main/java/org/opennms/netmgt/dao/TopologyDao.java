package org.opennms.netmgt.dao;

import java.util.List;

import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.Link;

public interface TopologyDao {
	
	public void saveOrUpdate(Link link);
	
	public void saveOrUpdate(EndPoint endpoint);
	
	public void delete(ElementIdentifier elementidentifier);

	public void delete(Element element);
	
	public void delete(Link link);
	
	public void delete(EndPoint endpoint);

	public Element get(ElementIdentifier elementIdentifier);

	public EndPoint get(EndPoint endpoint);

	public List<Element> getTopology();
	
}
