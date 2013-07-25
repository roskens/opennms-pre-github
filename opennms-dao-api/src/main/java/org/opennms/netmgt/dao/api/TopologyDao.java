package org.opennms.netmgt.dao.api;

import java.util.List;

import org.opennms.netmgt.model.topology.TopologyElement;
import org.opennms.netmgt.model.topology.ElementIdentifier;
import org.opennms.netmgt.model.topology.EndPoint;
import org.opennms.netmgt.model.topology.Link;

public interface TopologyDao {
	
	public void saveOrUpdate(Link link);
	
	public void saveOrUpdate(EndPoint endpoint);
	
	public void delete(ElementIdentifier elementidentifier);

	public void delete(TopologyElement element);
	
	public void delete(Link link);
	
	public void delete(EndPoint endpoint);

	public TopologyElement get(ElementIdentifier elementIdentifier);

	public EndPoint get(EndPoint endpoint);

	public List<TopologyElement> getTopology();
		
}
