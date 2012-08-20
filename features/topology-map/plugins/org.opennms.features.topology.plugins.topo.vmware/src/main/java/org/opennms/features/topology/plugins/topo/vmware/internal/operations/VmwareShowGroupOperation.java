package org.opennms.features.topology.plugins.topo.vmware.internal.operations;

import java.util.List;

import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.Operation;
import org.opennms.features.topology.api.OperationContext;
import org.opennms.features.topology.plugins.topo.vmware.internal.VmwareVertex;
import org.opennms.features.topology.plugins.topo.vmware.internal.VmwareTopologyProvider;


public class VmwareShowGroupOperation implements Operation {


    VmwareTopologyProvider m_topologyProvider;

    public VmwareShowGroupOperation(VmwareTopologyProvider topologyProvider) {
        m_topologyProvider = topologyProvider;
    }

    @Override
    public Undoer execute(List<Object> targets, OperationContext operationContext) {
        if (targets.size() != 1)
            return null;

        GraphContainer graphContainer = operationContext.getGraphContainer();

        for (Object key : targets) {
            Object vertexId = graphContainer.getVertexItemIdForVertexKey(key);

            VmwareVertex vmwareVertex = m_topologyProvider.getVertex(vertexId, false);

            if (vmwareVertex != null) {
                if (!vmwareVertex.isLeaf()) {
                    m_topologyProvider.setParent(vmwareVertex.getId(), VmwareConstants.ROOT_GROUP_ID);
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public boolean display(List<Object> targets, OperationContext operationContext) {
        return true;
    }

    @Override
    public boolean enabled(List<Object> targets, OperationContext operationContext) {
        if (targets.size() != 1)
            return false;

        GraphContainer graphContainer = operationContext.getGraphContainer();

        for (Object key : targets) {
            Object vertexId = graphContainer.getVertexItemIdForVertexKey(key);

            VmwareVertex vmwareVertex = m_topologyProvider.getVertex(vertexId, false);

            if (vmwareVertex != null) {
                if (!vmwareVertex.isLeaf()) {
                    m_topologyProvider.setParent(vmwareVertex.getId(), VmwareConstants.ROOT_GROUP_ID);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getId() {
        return null;
    }
}