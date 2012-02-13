package org.opennms.netmgt.poller.monitors;

import com.vmware.vim25.mo.HostSystem;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.collectd.vmware.VmwareViJavaAccess;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.PollStatus;
import org.opennms.netmgt.poller.MonitoredService;
import org.sblim.wbem.cim.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static junit.framework.Assert.assertNotNull;

public class VmwareCimMonitor extends AbstractServiceMonitor {

    // the node dao object for retrieving assets
    private NodeDao m_nodeDao = null;

    // healthStates map
    private static HashMap<Integer, String> m_healthStates;

    static {
        m_healthStates = new HashMap<Integer, String>();

        m_healthStates.put(0, "Unkown");
        m_healthStates.put(5, "OK");
        m_healthStates.put(10, "Degraded/Warning");
        m_healthStates.put(15, "Minor failure");
        m_healthStates.put(20, "Major failure");
        m_healthStates.put(25, "Critical failure");
        m_healthStates.put(30, "Non-recoverable error");
    }

    public void initialize(Map<String, Object> parameters) {
        m_nodeDao = BeanUtils.getBean("daoContext", "nodeDao", NodeDao.class);
        assertNotNull("Node dao should be a non-null value.", m_nodeDao);
    }

    private String getPropertyOfCimObject(CIMObject cimObject, String propertyName) {
        if (cimObject == null) {
            return null;
        } else {
            CIMProperty cimProperty = cimObject.getProperty(propertyName);
            if (cimProperty == null) {
                return null;
            } else {
                CIMValue cimValue = cimProperty.getValue();
                if (cimValue == null) {
                    return null;
                } else {
                    Object object = cimValue.getValue();
                    if (object == null) {
                        return null;
                    } else {
                        return object.toString();
                    }
                }
            }
        }
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

        HostSystem hostSystem = vmwareViJavaAccess.getHostSystemByManagedObjectId(vmwareManagedObjectId);

        String powerState = hostSystem.getSummary().runtime.getPowerState().toString();

        if ("poweredOn".equals(powerState)) {
            Vector<CIMObject> cimObjects = null;
            try {
                cimObjects = vmwareViJavaAccess.queryCimObjects(hostSystem, "CIM_NumericSensor");
            } catch (RemoteException e) {
                LogUtils.warnf(this, "Error retrieving cim values from host system '" + vmwareManagedObjectId + "'", e.getMessage());

                vmwareViJavaAccess.disconnect();

                return PollStatus.unavailable("Error retrieving cim values from host system '" + vmwareManagedObjectId + "'");
            } catch (CIMException e) {
                LogUtils.warnf(this, "Error retrieving cim values from host system '" + vmwareManagedObjectId + "'", e.getMessage());

                vmwareViJavaAccess.disconnect();

                return PollStatus.unavailable("Error retrieving cim values from host system '" + vmwareManagedObjectId + "'");
            }

            boolean success = true;
            String reason = "VMware CIM query returned: ";

            for (CIMObject cimObject : cimObjects) {
                String healthState = getPropertyOfCimObject(cimObject, "HealthState");
                String cimObjectName = getPropertyOfCimObject(cimObject, "Name");

                if (healthState != null) {
                    int healthStateInt = Integer.valueOf(healthState).intValue();

                    if (healthStateInt != 5) {

                        if (!success)
                            reason += ", ";

                        success = false;
                        reason += cimObjectName + " ";

                        if (m_healthStates.containsKey(healthStateInt))
                            reason += "(" + m_healthStates.get(healthStateInt) + ")";
                        else
                            reason += "(" + healthStateInt + ")";
                    }
                }

            }
            if (success) {
                serviceStatus = PollStatus.available();
            } else {
                serviceStatus = PollStatus.unavailable(reason);
            }
        } else {
            serviceStatus = PollStatus.unresponsive("Host system's power state is '" + hostSystem.getSummary().runtime.getPowerState() + "'");
        }

        vmwareViJavaAccess.disconnect();

        return serviceStatus;
    }

    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

}
