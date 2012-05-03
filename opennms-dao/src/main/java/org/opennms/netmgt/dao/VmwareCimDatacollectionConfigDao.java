package org.opennms.netmgt.dao;

import org.opennms.netmgt.config.vmware.cim.VmwareCimDatacollectionConfig;
import org.opennms.netmgt.config.vmware.cim.VmwareCimCollection;
import org.opennms.netmgt.model.RrdRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 02.05.12
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public interface VmwareCimDatacollectionConfigDao {

    VmwareCimDatacollectionConfig getConfig();

    VmwareCimCollection getVmwareCimCollection(String collectionName);

    RrdRepository getRrdRepository(String collectionName);

    List<String> getRRAList(String cName);

    String getRrdPath();
}
