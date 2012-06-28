/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.collectd.vmware;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import com.vmware.vim25.mo.util.MorUtil;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.BeanUtils;
import org.opennms.netmgt.collectd.vmware.vijava.VmwarePerformanceValues;
import org.opennms.netmgt.dao.VmwareConfigDao;
import org.sblim.wbem.cim.*;
import org.sblim.wbem.client.CIMClient;
import org.sblim.wbem.client.PasswordCredential;
import org.sblim.wbem.client.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * The Class VmwareViJavaAccess
 * <p/>
 * This class provides all the functionality to query Vmware infrastructure components.
 *
 * @author Christian Pape <Christian.Pape@informatik.hs-fulda.de>
 */
public class VmwareViJavaAccess {

    /**
     * logging for VMware library VI Java
     */
    private final Logger logger = LoggerFactory.getLogger("OpenNMS.VMware." + VmwareViJavaAccess.class.getName());

    /**
     * the config dao
     */
    private VmwareConfigDao m_vmwareConfigDao;

    private String m_hostname;
    private String m_username;
    private String m_password;

    private ServiceInstance m_serviceInstance = null;

    private PerformanceManager m_performanceManager = null;

    private Map<Integer, PerfCounterInfo> m_perfCounterInfoMap = null;

    private Map<HostSystem, HostServiceTicket> m_hostServiceTickets = new HashMap<HostSystem, HostServiceTicket>();

    private Map<HostSystem, String> m_hostSystemCimUrls = new HashMap<HostSystem, String>();

    /**
     * Constructor for creating a instance for a given server and credentials.
     *
     * @param hostname the vCenter's hostname
     * @param username the username
     * @param password the password
     */
    public VmwareViJavaAccess(String hostname, String username, String password) {
        this.m_hostname = hostname;
        this.m_username = username;
        this.m_password = password;
    }

    /**
     * Constructor for creating a instance for a given server. Checks whether credentials
     * are available in the Vmware config file.
     *
     * @param hostname the vCenter's hostname
     * @throws MarshalException
     * @throws ValidationException
     * @throws IOException
     */
    public VmwareViJavaAccess(String hostname) throws MarshalException, ValidationException, IOException {
        if (m_vmwareConfigDao == null)
            m_vmwareConfigDao = BeanUtils.getBean("daoContext", "vmwareConfigDao", VmwareConfigDao.class);

        if (m_vmwareConfigDao == null) {
            logger.error("vmwareConfigDao should be a non-null value.");
        }

        this.m_hostname = hostname;
        this.m_username = m_vmwareConfigDao.getServerMap().get(m_hostname).getUsername();
        this.m_password = m_vmwareConfigDao.getServerMap().get(m_hostname).getPassword();
    }

    /**
     * Connects to the server.
     *
     * @throws MalformedURLException
     * @throws RemoteException
     */
    public void connect() throws MalformedURLException, RemoteException {
        relax();

        m_serviceInstance = new ServiceInstance(new URL("https://" + m_hostname + "/sdk"), m_username, m_password);
    }

    /**
     * Disconnects from the server.
     */
    public void disconnect() {
        m_serviceInstance.getServerConnection().logout();
    }

    /**
     * This method is used to "relax" the policies concerning self-signed certificates.
     */
    private void relax() {

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public boolean isServerTrusted(
                    java.security.cert.X509Certificate[] certs) {
                return true;
            }

            public boolean isClientTrusted(
                    java.security.cert.X509Certificate[] certs) {
                return true;
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType)
                    throws java.security.cert.CertificateException {
                return;
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType)
                    throws java.security.cert.CertificateException {
                return;
            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } catch (Exception exception) {
            logger.warn("Error setting relaxed SSL policy", exception);
        }

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
    }

    /**
     * Retrieves the performance manager for this instance.
     *
     * @return the performance manager
     */
    private PerformanceManager getPerformanceManager() {
        if (m_performanceManager == null)
            m_performanceManager = m_serviceInstance.getPerformanceManager();

        return m_performanceManager;
    }

    /**
     * This method retrieves the performance counters available.
     *
     * @return a map of performance counters
     */
    public Map<Integer, PerfCounterInfo> getPerfCounterInfoMap() {
        if (m_perfCounterInfoMap == null) {
            m_perfCounterInfoMap = new HashMap<Integer, PerfCounterInfo>();

            PerfCounterInfo[] perfCounterInfos = getPerformanceManager().getPerfCounter();

            for (PerfCounterInfo perfCounterInfo : perfCounterInfos) {
                m_perfCounterInfoMap.put(perfCounterInfo.getKey(), perfCounterInfo);
            }
        }
        return m_perfCounterInfoMap;
    }

    /**
     * Returns a managed entitiy for a given managed object Id.
     *
     * @param managedObjectId the managed object Id
     * @return the managed entity
     */
    public ManagedEntity getManagedEntityByManagedObjectId(String managedObjectId) {
        ManagedObjectReference managedObjectReference = new ManagedObjectReference();

        managedObjectReference.setType("ManagedEntity");
        managedObjectReference.setVal(managedObjectId);

        ManagedEntity managedEntity = MorUtil.createExactManagedEntity(m_serviceInstance.getServerConnection(), managedObjectReference);

        return managedEntity;
    }

    /**
     * Returns a virtual machine by a given managed object Id.
     *
     * @param managedObjectId the managed object Id
     * @return the virtual machine object
     */
    public VirtualMachine getVirtualMachineByManagedObjectId(String managedObjectId) {
        ManagedObjectReference managedObjectReference = new ManagedObjectReference();

        managedObjectReference.setType("VirtualMachine");
        managedObjectReference.setVal(managedObjectId);

        VirtualMachine virtualMachine = (VirtualMachine) MorUtil.createExactManagedEntity(m_serviceInstance.getServerConnection(), managedObjectReference);

        return virtualMachine;
    }

    /**
     * Returns a host system by a given managed object Id.
     *
     * @param managedObjectId the managed object Id
     * @return the host system object
     */
    public HostSystem getHostSystemByManagedObjectId(String managedObjectId) {
        ManagedObjectReference managedObjectReference = new ManagedObjectReference();

        managedObjectReference.setType("HostSystem");
        managedObjectReference.setVal(managedObjectId);

        HostSystem hostSystem = (HostSystem) MorUtil.createExactManagedEntity(m_serviceInstance.getServerConnection(), managedObjectReference);

        return hostSystem;
    }

    /**
     * Generates a human-readable name for a performance counter.
     *
     * @param perfCounterInfo the perfomance counter info object
     * @return a string-representation of the performance counter's name
     */
    private String getHumanReadableName(PerfCounterInfo perfCounterInfo) {
        return perfCounterInfo.getGroupInfo().getKey() + "." + perfCounterInfo.getNameInfo().getKey() + "." + perfCounterInfo.getRollupType().toString();
    }

    /**
     * This method queries performance values for a given managed entity.
     *
     * @param managedEntity the managed entity to query
     * @return the perfomance values
     * @throws RemoteException
     */
    public VmwarePerformanceValues queryPerformanceValues(ManagedEntity managedEntity) throws RemoteException {

        VmwarePerformanceValues vmwarePerformanceValues = new VmwarePerformanceValues();

        int refreshRate = getPerformanceManager().queryPerfProviderSummary(managedEntity).getRefreshRate();

        PerfQuerySpec perfQuerySpec = new PerfQuerySpec();
        perfQuerySpec.setEntity(managedEntity.getMOR());
        perfQuerySpec.setMaxSample(new
                Integer(1)
        );
        perfQuerySpec.setIntervalId(refreshRate);

        PerfEntityMetricBase[] perfEntityMetricBases = getPerformanceManager().queryPerf(new PerfQuerySpec[]{perfQuerySpec});

        if (perfEntityMetricBases != null) {
            for (int i = 0; i < perfEntityMetricBases.length; i++) {
                PerfMetricSeries[] perfMetricSeries = ((PerfEntityMetric) perfEntityMetricBases[i]).getValue();

                for (int j = 0; perfMetricSeries != null && j < perfMetricSeries.length; j++) {

                    if (perfMetricSeries[j] instanceof PerfMetricIntSeries) {
                        long[] longs = ((PerfMetricIntSeries) perfMetricSeries[j]).getValue();

                        if (longs.length == 1) {

                            PerfCounterInfo perfCounterInfo = getPerfCounterInfoMap().get(perfMetricSeries[j].getId().getCounterId());
                            String instance = perfMetricSeries[j].getId().getInstance();
                            String name = getHumanReadableName(perfCounterInfo);

                            if (instance != null && !"".equals(instance))
                                vmwarePerformanceValues.addValue(name, instance, longs[0]);
                            else
                                vmwarePerformanceValues.addValue(name, longs[0]);
                        }
                    }
                }
            }
        }

        return vmwarePerformanceValues;
    }

    /**
     * Queries a host system for Cim data.
     *
     * @param hostSystem the host system to query
     * @param cimClass   the class of Cim objects to retrieve
     * @return the list of Cim objects
     * @throws RemoteException
     * @throws CIMException
     */
    public Vector<CIMObject> queryCimObjects(HostSystem hostSystem, String cimClass) throws RemoteException, CIMException {
        Vector<CIMObject> cimObjects = new Vector<CIMObject>();

        if (!m_hostServiceTickets.containsKey(hostSystem))
            m_hostServiceTickets.put(hostSystem, hostSystem.acquireCimServicesTicket());

        HostServiceTicket hostServiceTicket = m_hostServiceTickets.get(hostSystem);

        if (!m_hostSystemCimUrls.containsKey(hostSystem)) {
            String ipAddress = null;

            HostNetworkSystem hostNetworkSystem = hostSystem.getHostNetworkSystem();

            if (hostNetworkSystem != null) {
                HostNetworkInfo hostNetworkInfo = hostNetworkSystem.getNetworkInfo();
                if (hostNetworkInfo != null) {

                    HostVirtualNic[] hostVirtualNics = hostNetworkInfo.getConsoleVnic();
                    if (hostVirtualNics != null) {
                        for (HostVirtualNic hostVirtualNic : hostVirtualNics) {
                            ipAddress = hostVirtualNic.getSpec().getIp().getIpAddress();
                        }
                    }

                    hostVirtualNics = hostNetworkInfo.getVnic();
                    if (ipAddress == null && hostVirtualNics != null) {
                        for (HostVirtualNic hostVirtualNic : hostVirtualNics) {
                            ipAddress = hostVirtualNic.getSpec().getIp().getIpAddress();
                        }
                    }
                }
            }

            if (ipAddress == null) {
                logger.warn("Cannot determine ip address for host system '{}'", hostSystem.getMOR().getVal());
                return cimObjects;
            }

            m_hostSystemCimUrls.put(hostSystem, "https://" + ipAddress + ":5989");
        }

        String cimAgentAddress = m_hostSystemCimUrls.get(hostSystem);

        String namespace = "root/cimv2";

        UserPrincipal userPr = new UserPrincipal(hostServiceTicket.getSessionId());
        PasswordCredential pwCred = new PasswordCredential(hostServiceTicket.getSessionId().toCharArray());

        CIMNameSpace ns = new CIMNameSpace(cimAgentAddress, namespace);
        CIMClient cimClient = new CIMClient(ns, userPr, pwCred);

        // very important to query esx5 hosts
        cimClient.useMPost(false);

        CIMObjectPath rpCOP = new CIMObjectPath(cimClass);

        Enumeration rpEnm = cimClient.enumerateInstances(rpCOP);

        while (rpEnm.hasMoreElements()) {
            CIMObject rp = (CIMObject) rpEnm.nextElement();

            cimObjects.add(rp);
        }

        return cimObjects;
    }

    /**
     * Searches for a managed entity by a given type.
     *
     * @param type the type string to search for
     * @return the list of managed entities found
     * @throws RemoteException
     */
    public ManagedEntity[] searchManagedEntities(String type) throws RemoteException {
        return (new InventoryNavigator(m_serviceInstance.getRootFolder())).searchManagedEntities(type);
    }

    /**
     * Returns the value of a given cim object and property.
     *
     * @param cimObject    the Cim object
     * @param propertyName the property's name
     * @return the value
     */
    public String getPropertyOfCimObject(CIMObject cimObject, String propertyName) {
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
}
