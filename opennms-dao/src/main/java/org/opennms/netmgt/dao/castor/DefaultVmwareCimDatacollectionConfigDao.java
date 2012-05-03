package org.opennms.netmgt.dao.castor;

import org.opennms.netmgt.config.vmware.cim.VmwareCimCollection;
import org.opennms.netmgt.config.vmware.cim.VmwareCimDatacollectionConfig;
import org.opennms.netmgt.dao.VmwareCimDatacollectionConfigDao;
import org.opennms.netmgt.model.RrdRepository;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 02.05.12
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */
public class DefaultVmwareCimDatacollectionConfigDao extends AbstractCastorConfigDao<VmwareCimDatacollectionConfig, VmwareCimDatacollectionConfig> implements VmwareCimDatacollectionConfigDao {

    public DefaultVmwareCimDatacollectionConfigDao() {
        super(VmwareCimDatacollectionConfig.class, "Vmware Cim Data Collection Configuration");
    }

    public VmwareCimDatacollectionConfig getConfig() {
        return getContainer().getObject();
    }

    public VmwareCimDatacollectionConfig translateConfig(VmwareCimDatacollectionConfig castorConfig) {
        return castorConfig;
    }

    public VmwareCimCollection getVmwareCimCollection(String collectionName) {
        VmwareCimCollection[] collections = getConfig().getVmwareCimCollection();
        VmwareCimCollection collection = null;
        for (VmwareCimCollection coll : collections) {
            if (coll.getName().equalsIgnoreCase(collectionName)) {
                collection = coll;
                break;
            }
        }
        if (collection == null) {
            throw new IllegalArgumentException("getVmwareCimCollection: collection name: "
                    + collectionName + " specified in collectd configuration not found in Vmware collection configuration.");
        }
        return collection;
    }

    public RrdRepository getRrdRepository(String collectionName) {
        RrdRepository repo = new RrdRepository();
        repo.setRrdBaseDir(new File(getRrdPath()));
        repo.setRraList(getRRAList(collectionName));
        repo.setStep(getStep(collectionName));
        repo.setHeartBeat((2 * getStep(collectionName)));
        return repo;
    }

    public int getStep(String cName) {
        VmwareCimCollection collection = getVmwareCimCollection(cName);
        if (collection != null)
            return collection.getRrd().getStep();
        else
            return -1;
    }

    public List<String> getRRAList(String cName) {
        VmwareCimCollection collection = getVmwareCimCollection(cName);
        if (collection != null)
            return collection.getRrd().getRraCollection();
        else
            return null;

    }

    public String getRrdPath() {
        String rrdPath = getConfig().getRrdRepository();
        if (rrdPath == null) {
            throw new RuntimeException("Configuration error, failed to "
                    + "retrieve path to RRD repository.");
        }

        /*
        * TODO: make a path utils class that has the below in it strip the
        * File.separator char off of the end of the path.
        */
        if (rrdPath.endsWith(File.separator)) {
            rrdPath = rrdPath.substring(0, (rrdPath.length() - File.separator.length()));
        }

        return rrdPath;
    }
}
