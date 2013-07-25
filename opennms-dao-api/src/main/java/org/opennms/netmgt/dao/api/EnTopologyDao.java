package org.opennms.netmgt.dao.api;

import org.opennms.netmgt.model.entopology.ElementIdentifier;
import org.opennms.netmgt.model.entopology.EndPoint;
import org.opennms.netmgt.model.entopology.Link;

public interface EnTopologyDao {
	
	public void saveOrUpdate(Link link);
	
	public void saveOrUpdate(EndPoint endpoint);
	
	public void delete(ElementIdentifier elementidentifier);

	public void delete(Link link);
	
	public void delete(EndPoint endpoint);
		
}
