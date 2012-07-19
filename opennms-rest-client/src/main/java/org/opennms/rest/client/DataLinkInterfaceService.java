package org.opennms.rest.client;

import org.opennms.rest.model.ClientDataLinkInterface;
import org.opennms.rest.model.ClientDataLinkInterfaceList;

public interface DataLinkInterfaceService {
    
    public ClientDataLinkInterfaceList getAll();
    
    public ClientDataLinkInterface get(Integer id);

}
