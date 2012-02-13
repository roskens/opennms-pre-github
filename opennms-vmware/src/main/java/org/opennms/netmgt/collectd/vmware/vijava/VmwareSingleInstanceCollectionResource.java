package org.opennms.netmgt.collectd.vmware.vijava;

import org.opennms.netmgt.collectd.CollectionAgent;

public class VmwareSingleInstanceCollectionResource extends VmwareCollectionResource {

    public VmwareSingleInstanceCollectionResource(final CollectionAgent agent) {
        super(agent);
    }

    public String getResourceTypeName() {
        return "node";
    }

    public String getInstance() {
        return null;
    }

    public String toString() {
        return "Node[" + m_agent.getNodeId() + "]/type[node]";
    }

}
