package org.opennms.netmgt.dao;

import org.opennms.netmgt.config.reportd.ReportdConfiguration;
import org.opennms.netmgt.config.vmware.vijava.VmwareCollection;
import org.opennms.netmgt.config.vmware.vijava.VmwareDatacollectionConfig;
import org.opennms.netmgt.model.RrdRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 02.05.12
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public interface VmwareDatacollectionConfigDao {

    VmwareDatacollectionConfig getConfig();

    VmwareCollection getVmwareCollection(String collectionName);

    RrdRepository getRrdRepository(String collectionName);

    List<String> getRRAList(String cName);

    String getRrdPath();
}