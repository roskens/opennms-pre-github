package org.opennms.netmgt.collectd.vmware.vijava;

import java.io.File;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.collectd.CollectionAgent;
import org.opennms.netmgt.model.RrdRepository;

public class VmwareMultiInstanceCollectionResource extends VmwareCollectionResource {
    private String m_inst;
    private String m_name;

    public VmwareMultiInstanceCollectionResource(final CollectionAgent agent, final String instance, final String name) {
        super(agent);
        m_inst = instance;
        m_name = name;
    }

    @Override
    public File getResourceDir(RrdRepository repository) {
        final File rrdBaseDir = repository.getRrdBaseDir();
        final File nodeDir = new File(rrdBaseDir, String.valueOf(m_agent.getNodeId()));
        final File typeDir = new File(nodeDir, m_name);
        final File instDir = new File(typeDir, m_inst.replaceAll("/","_").replaceAll("\\s+", "_").replaceAll(":", "_").replaceAll("\\\\", "_").replaceAll("[\\[\\]]", "_"));
        LogUtils.debugf(this, "getResourceDir: %s", instDir);
        return instDir;
    }

    public String getResourceTypeName() {
        return m_name;
    }

    public String getInstance() {
        return m_inst;
    }

    public String toString() {
        return "Node[" + m_agent.getNodeId() + "]/type["+ m_name+"]/instance[" + m_inst +"]";
    }
}
