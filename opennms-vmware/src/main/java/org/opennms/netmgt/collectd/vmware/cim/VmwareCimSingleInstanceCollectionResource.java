package org.opennms.netmgt.collectd.vmware.cim;

import org.opennms.netmgt.collectd.CollectionAgent;

public class VmwareCimSingleInstanceCollectionResource extends VmwareCimCollectionResource {

    public VmwareCimSingleInstanceCollectionResource(final CollectionAgent agent) {
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
