package org.opennms.netmgt.config;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.*;
import org.opennms.core.xml.CastorUtils;
import org.opennms.netmgt.config.vmware.VmwareConfig;
import org.opennms.netmgt.config.vmware.VmwareServer;
import org.springframework.core.io.FileSystemResource;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class VmwareConfigFactory {
    /**
     * The singleton instance of this factory
     */
    private static VmwareConfigFactory m_singleton = null;

    /**
     * The server map
     */
    private Map<String, VmwareServer> m_vmwareServerMap = new HashMap<String, VmwareServer>();

    /**
     * The config class loaded from the config file
     */
    private static VmwareConfig m_config;

    private static boolean m_loaded = false;

    private VmwareConfigFactory(String configFile) throws IOException, MarshalException, ValidationException {
        m_config = CastorUtils.unmarshal(VmwareConfig.class, new FileSystemResource(configFile));

        for (VmwareServer vmwareServer : m_config.getVmwareServer()) {
            m_vmwareServerMap.put(vmwareServer.getHostname(), vmwareServer);
            LogUtils.debugf(this, "Added vmware-server entry for '" + vmwareServer.getUsername() + "@" + vmwareServer.getHostname() + "'");
        }
    }

    public static VmwareConfigFactory getInstance() throws IOException, MarshalException, ValidationException {
        if (m_singleton == null) {
            File cfgFile = ConfigFileConstants.getFile(ConfigFileConstants.VMWARE_CONFIG_FILE_NAME);

            log().debug("init: config file path: " + cfgFile.getPath());

            m_singleton = new VmwareConfigFactory(cfgFile.getPath());
        }

        return m_singleton;
    }

    private static ThreadCategory log() {
        return ThreadCategory.getInstance(VmwareConfigFactory.class);
    }

    public Map<String, VmwareServer> getServerMap() throws IOException, MarshalException, ValidationException {
        return m_vmwareServerMap;
    }

    public VmwareConfig getConfig() {
        return m_config;
    }
}
