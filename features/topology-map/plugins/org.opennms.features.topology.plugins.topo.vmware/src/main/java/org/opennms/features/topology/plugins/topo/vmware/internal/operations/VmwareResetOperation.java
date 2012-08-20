package org.opennms.features.topology.plugins.topo.vmware.internal.operations;

import java.util.List;

import org.opennms.features.topology.api.DisplayState;
import org.opennms.features.topology.api.Operation;
import org.opennms.features.topology.api.OperationContext;
import org.opennms.features.topology.plugins.topo.vmware.internal.VmwareTopologyProvider;


public class VmwareResetOperation implements Operation{
    
    VmwareTopologyProvider m_topologyProvider;
    
    public VmwareResetOperation(VmwareTopologyProvider topologyProvider) {
        m_topologyProvider = topologyProvider;
    }

    @Override
    public Undoer execute(List<Object> targets,
            OperationContext operationContext) {
        /*
        DisplayState graphContainer = operationContext.getGraphContainer();
        Object groupId = m_topologyProvider.addGroup(GROUP_ICON);
        Object vertexId = m_topologyProvider.addVertex(50, 50, SERVER_ICON);
        m_topologyProvider.setParent(vertexId, groupId);
        */

        m_topologyProvider.resetContainer();

        return null;
    }

    @Override
    public boolean display(List<Object> targets, OperationContext operationContext) {
        return true;
    }

    @Override
    public boolean enabled(List<Object> targets, OperationContext operationContext) {
        return true;
    }

    @Override
    public String getId() {
        return null;
    }
}