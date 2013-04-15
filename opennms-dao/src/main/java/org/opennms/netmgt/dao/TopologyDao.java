package org.opennms.netmgt.dao;

import java.util.List;

import org.opennms.netmgt.model.topology.Element;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.Link;

public interface TopologyDao {
	
	public void saveOrUpdate(Link link);
	
	public void saveOrUpdate(EndPoint endpoint);
	
	public void delete(Element element);
	
	public void delete(Link link);
	
	public void delete(EndPoint endpoint);
	
}
