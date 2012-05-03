package org.opennms.netmgt.dao.castor;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.config.vmware.VmwareConfig;
import org.opennms.netmgt.config.vmware.VmwareServer;
import org.opennms.netmgt.dao.VmwareConfigDao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 02.05.12
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */
public class DefaultVmwareConfigDao extends AbstractCastorConfigDao<VmwareConfig, VmwareConfig> implements VmwareConfigDao {

    public DefaultVmwareConfigDao() {
        super(VmwareConfig.class, "Vmware Configuration");
    }

    public VmwareConfig getConfig() {
        return getContainer().getObject();
    }

    public VmwareConfig translateConfig(VmwareConfig castorConfig) {
        return castorConfig;
    }

    public Map<String, VmwareServer> getServerMap() {
        HashMap<String, VmwareServer> vmwareServerMap = new HashMap<String, VmwareServer>();

        for (VmwareServer vmwareServer : getConfig().getVmwareServer()) {
            vmwareServerMap.put(vmwareServer.getHostname(), vmwareServer);
        }
        return vmwareServerMap;
    }
}
