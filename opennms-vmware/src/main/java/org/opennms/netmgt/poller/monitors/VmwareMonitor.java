package org.opennms.netmgt.poller.monitors;

import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.VirtualMachine;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.collectd.vmware.VmwareViJavaAccess;
import org.opennms.netmgt.config.vmware.VmwareServer;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.MonitoredService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;

public class VmwareMonitor extends AbstractServiceMonitor {

    // map for accessing the vmware-server parameters
    private Map<String, VmwareServer> vmwareServerMap = null;

    // the node dao object for retrieving assets
    private NodeDao m_nodeDao = null;

    public void initialize(Map<String, Object> parameters) {
        m_nodeDao = BeanUtils.getBean("daoContext", "nodeDao", NodeDao.class);

        assertNotNull("Node dao should be a non-null value.", m_nodeDao);
    }

    public PollStatus poll(MonitoredService svc, Map<String, Object> parameters) {
        OnmsNode onmsNode = m_nodeDao.get(svc.getNodeId());

        // retrieve the assets and
        String vmwareManagementServer = onmsNode.getAssetRecord().getVmwareManagementServer();
        String vmwareManagedEntityType = onmsNode.getAssetRecord().getVmwareManagedEntityType();
        String vmwareManagedObjectId = onmsNode.getAssetRecord().getVmwareManagedObjectId();

        PollStatus serviceStatus = PollStatus.unknown();

        VmwareViJavaAccess vmwareViJavaAccess = null;

        try {
            vmwareViJavaAccess = new VmwareViJavaAccess(vmwareManagementServer);
        } catch (MarshalException e) {
            LogUtils.warnf(this, "Error initialising VMware connection to '" + vmwareManagementServer + "': " + e.getMessage());
            return PollStatus.unavailable("Error initialising VMware connection to '" + vmwareManagementServer + "'");
        } catch (ValidationException e) {
            LogUtils.warnf(this, "Error initialising VMware connection to '" + vmwareManagementServer + "': " + e.getMessage());
            return PollStatus.unavailable("Error initialising VMware connection to '" + vmwareManagementServer + "'");
        } catch (IOException e) {
            LogUtils.warnf(this, "Error initialising VMware connection to '" + vmwareManagementServer + "': " + e.getMessage());
            return PollStatus.unavailable("Error initialising VMware connection to '" + vmwareManagementServer + "'");
        }

        try {
            vmwareViJavaAccess.connect();
        } catch (MalformedURLException e) {
            LogUtils.warnf(this, "Error connecting VMware management server '" + vmwareManagementServer + "': " + e.getMessage());
            return PollStatus.unavailable("Error connecting VMware management server '" + vmwareManagementServer + "'");
        } catch (RemoteException e) {
            LogUtils.warnf(this, "Error connecting VMware management server '" + vmwareManagementServer + "': " + e.getMessage());
            return PollStatus.unavailable("Error connecting VMware management server '" + vmwareManagementServer + "'");
        }

        String powerState = null;

        if ("HostSystem".equals(vmwareManagedEntityType)) {
            HostSystem hostSystem = vmwareViJavaAccess.getHostSystemByManagedObjectId(vmwareManagedObjectId);
            powerState = hostSystem.getSummary().runtime.getPowerState().toString();

        } else {
            if ("VirtualMachine".equals(vmwareManagedEntityType)) {
                VirtualMachine virtualMachine = vmwareViJavaAccess.getVirtualMachineByManagedObjectId(vmwareManagedObjectId);
                powerState = virtualMachine.getSummary().runtime.getPowerState().toString();
            } else {
                LogUtils.warnf(this, "Error getting '" + vmwareManagedEntityType + "' for '" + vmwareManagedObjectId + "'");

                vmwareViJavaAccess.disconnect();

                return serviceStatus;
            }
        }

        if (powerState == null)
            powerState = "unknown";

        if ("poweredOn".equals(powerState)) {
            serviceStatus = PollStatus.available();
        } else {
            serviceStatus = PollStatus.unavailable("The system's state is '" + powerState + "'");
        }

        vmwareViJavaAccess.disconnect();

        return serviceStatus;
    }

    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

}
