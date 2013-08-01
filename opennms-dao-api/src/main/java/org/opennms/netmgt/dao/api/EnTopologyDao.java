package org.opennms.netmgt.dao.api;

import org.opennms.netmgt.model.entopology.EndPoint;

public interface EnTopologyDao {
	
	public EndPoint get(Integer id);
	
	public void saveOrUpdate(EndPoint endpoint);
		
	public void delete(EndPoint endpoint);
		
}
