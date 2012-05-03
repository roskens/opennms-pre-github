package org.opennms.netmgt.dao;

import org.opennms.netmgt.config.vmware.VmwareConfig;
import org.opennms.netmgt.config.vmware.VmwareServer;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 02.05.12
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public interface VmwareConfigDao {

    VmwareConfig getConfig();

    Map<String, VmwareServer> getServerMap();
}
