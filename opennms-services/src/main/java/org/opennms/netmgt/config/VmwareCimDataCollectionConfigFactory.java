package org.opennms.netmgt.config;

import org.apache.commons.io.IOUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.ConfigFileConstants;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.core.xml.CastorUtils;
import org.opennms.netmgt.config.vmware.cim.VmwareCimCollection;
import org.opennms.netmgt.config.vmware.cim.VmwareCimDatacollectionConfig;
import org.opennms.netmgt.model.RrdRepository;

import java.io.*;
import java.util.List;

public class VmwareCimDataCollectionConfigFactory {

    private static VmwareCimDataCollectionConfigFactory m_instance;

    private static boolean m_loadedFromFile = false;

    protected boolean initialized = false;

    protected static long m_lastModified;

    private static VmwareCimDatacollectionConfig m_config;

    public VmwareCimDataCollectionConfigFactory(String configFile) throws MarshalException, ValidationException, IOException {
        InputStream is = null;

        try {
            is = new FileInputStream(configFile);
            initialize(is);
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
        }
    }

    public VmwareCimDataCollectionConfigFactory(InputStream is) throws MarshalException, ValidationException {
        initialize(is);
    }

    private void initialize(InputStream stream) throws MarshalException, ValidationException {
        log().debug("initialize: initializing Vmware cim collection config factory.");
        m_config = CastorUtils.unmarshal(VmwareCimDatacollectionConfig.class, stream);
    }

    public static synchronized void init() throws IOException, FileNotFoundException, MarshalException, ValidationException {

        if (m_instance == null) {
            File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.VMWARE_CIM_COLLECTION_CONFIG_FILE_NAME);
            m_instance = new VmwareCimDataCollectionConfigFactory(cfgFile.getPath());
            m_lastModified = cfgFile.lastModified();
            m_loadedFromFile = true;
        }
    }

    public static synchronized VmwareCimDataCollectionConfigFactory getInstance() {

        if (m_instance == null) {
            throw new IllegalStateException("You must call VmwareCimCollectionConfigFactory.init() before calling getInstance().");
        }
        return m_instance;
    }

    public static synchronized void setInstance(VmwareCimDataCollectionConfigFactory instance) {
        m_instance = instance;
        m_loadedFromFile = false;
    }

    public synchronized void reload() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        m_instance = null;
        init();
    }

    protected void updateFromFile() throws IOException, MarshalException, ValidationException {
        if (m_loadedFromFile) {
            File surveillanceViewsFile = ConfigFileConstants.getFile(ConfigFileConstants.VMWARE_CIM_COLLECTION_CONFIG_FILE_NAME);
            if (m_lastModified != surveillanceViewsFile.lastModified()) {
                this.reload();
            }
        }
    }

    public synchronized static VmwareCimDatacollectionConfig getConfig() {
        return m_config;
    }

    public synchronized static void setConfig(VmwareCimDatacollectionConfig m_config) {
        VmwareCimDataCollectionConfigFactory.m_config = m_config;
    }

    private ThreadCategory log() {
        return ThreadCategory.getInstance();
    }

    public VmwareCimCollection getVmwareCimCollection(String collectionName) {
        VmwareCimCollection[] collections = m_config.getVmwareCimCollection();
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
        String rrdPath = m_config.getRrdRepository();
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

