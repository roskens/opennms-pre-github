package org.opennms.netmgt.dao.api;

import org.opennms.netmgt.model.entopology.ElementIdentifier;
import org.opennms.netmgt.model.entopology.EndPoint;

public interface EnTopologyDao {
	
	public void saveOrUpdate(EndPoint endpoint);
	
	public void delete(ElementIdentifier elementidentifier);
	
	public void delete(EndPoint endpoint);
		
}
