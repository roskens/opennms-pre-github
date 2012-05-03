package org.opennms.netmgt.collectd.vmware;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import com.vmware.vim25.mo.util.MorUtil;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.EmptyKeyRelaxedTrustSSLContext;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.collectd.vmware.vijava.VmwarePerformanceValues;
import org.opennms.netmgt.dao.VmwareDatacollectionConfigDao;
import org.sblim.wbem.cim.CIMException;
import org.sblim.wbem.cim.CIMNameSpace;
import org.sblim.wbem.cim.CIMObject;
import org.sblim.wbem.cim.CIMObjectPath;
import org.sblim.wbem.client.CIMClient;
import org.sblim.wbem.client.PasswordCredential;
import org.sblim.wbem.client.UserPrincipal;
import org.opennms.netmgt.dao.VmwareConfigDao;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: 23.01.12
 * Time: 08:37
 * To change this template use File | Settings | File Templates.
 */
public class VmwareViJavaAccess {
    /*

       private static String baseUnitsDesc[] = {"Unknown", "Other", "Degrees C", "Degrees F", "Degrees K", "Volts", "Amps", "Watts", "Joules", "Coulombs", "VA", "Nits", "Lumens",
               "Lux", "Candelas", "kPa", "PSI", "Newtons", "CFM", "RPM", "Hertz", "Seconds", "Minutes", "Hours", "Days", "Weeks", "Mils", "Inches", "Feet", "Cubic Inches", "Cubic Feet",
               "Meters", "Cubic Centimeters", "Cubic Meters", "Liters", "Fluid Ounces", "Radians", "Steradians", "Revolutions", "Cycles", "Gravities", "Ounces", "Pounds", "Foot-Pounds",
               "Ounce-Inches", "Gauss", "Gilberts", "Henries", "Farads", "Ohms", "Siemens", "Moles", "Becquerels", "PPM (parts/million)", "Decibels", "DbA", "DbC", "Grays", "Sieverts",
               "Color Temperature Degrees K", "Bits", "Bytes", "Words (data)", "DoubleWords", "QuadWords", "Percentage", "Pascals"};

       private static String sensorTypesDesc[] = {"Unknown", "Other", "Temperature", "Voltage", "Current", "Tachometer", "Counter", "Switch", "Lock", "Humidity", "Smoke Detection",
               "Presence", "Air Flow", "Power Consumption", "Power Production", "Pressure", "DMTF Reserved", "Vendor Reserved"};

       private static String supportedThresholdsDesc[] = {"LowerThresholdNonCritical", "UpperThresholdNonCritical", "LowerThresholdCritical", "UpperThresholdCritical",
               "LowerThresholdFatal", "UpperThresholdFatal"};

       private static String rateUnitsDesc[] = {"None", "Per MicroSecond", "Per MilliSecond", "Per Second", "Per Minute", "Per Hour", "Per Day", "Per Week", "Per Month", "Per Year"};


    */

    // the config dao
    private VmwareConfigDao m_vmwareConfigDao;

    private String m_hostname;

    private String m_username;

    private String m_password;

    private ServiceInstance m_serviceInstance = null;

    private PerformanceManager m_performanceManager = null;

    private Map<Integer, PerfCounterInfo> m_perfCounterInfoMap = null;

    private Map<HostSystem, HostServiceTicket> m_hostServiceTickets = new HashMap<HostSystem, HostServiceTicket>();

    private Map<HostSystem, String> m_hostSystemCimUrls = new HashMap<HostSystem, String>();

    public VmwareViJavaAccess(String hostname, String username, String password) {
        this.m_hostname = hostname;
        this.m_username = username;
        this.m_password = password;
    }

    public VmwareViJavaAccess(String hostname) throws MarshalException, ValidationException, IOException {
        if (m_vmwareConfigDao == null)
            m_vmwareConfigDao = BeanUtils.getBean("daoContext", "vmwareConfigDao", VmwareConfigDao.class);

        assertNotNull("vmwareConfigDao should be a non-null value.", m_vmwareConfigDao);

        this.m_hostname = hostname;
        this.m_username = m_vmwareConfigDao.getServerMap().get(m_hostname).getUsername();
        this.m_password = m_vmwareConfigDao.getServerMap().get(m_hostname).getPassword();
    }

    public void connect() throws MalformedURLException, RemoteException {
        relax();

        m_serviceInstance = new ServiceInstance(new URL("https://" + m_hostname + "/sdk"), m_username, m_password);
    }

    public void disconnect() {
        m_serviceInstance.getServerConnection().logout();
    }

    private void relax() {
        /*
        DefaultHttpClient client = new DefaultHttpClient();

        final SchemeRegistry registry = client.getConnectionManager().getSchemeRegistry();
        final Scheme https = registry.getScheme("https");

        try {
            final org.apache.http.conn.ssl.SSLSocketFactory factory = new org.apache.http.conn.ssl.SSLSocketFactory(SSLContext.getInstance(EmptyKeyRelaxedTrustSSLContext.ALGORITHM), org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            final Scheme lenient = new Scheme(https.getName(), https.getDefaultPort(), factory);
            registry.register(lenient);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            LogUtils.warnf(this, "Error setting relaxed https policy'" + noSuchAlgorithmException.getMessage() + "'");
        }

        */

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
            LogUtils.warnf(this, "Error setting relaxed SSL policy", exception);
        }

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });

    }

    private PerformanceManager getPerformanceManager() {
        if (m_performanceManager == null)
            m_performanceManager = m_serviceInstance.getPerformanceManager();

        return m_performanceManager;
    }

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

    public ManagedEntity getManagedEntityByManagedObjectId(String managedObjectId) {
        ManagedObjectReference managedObjectReference = new ManagedObjectReference();

        managedObjectReference.setType("ManagedEntity");
        managedObjectReference.setVal(managedObjectId);

        ManagedEntity managedEntity = MorUtil.createExactManagedEntity(m_serviceInstance.getServerConnection(), managedObjectReference);

        return managedEntity;
    }

    public VirtualMachine getVirtualMachineByManagedObjectId(String managedObjectId) {
        ManagedObjectReference managedObjectReference = new ManagedObjectReference();

        managedObjectReference.setType("VirtualMachine");
        managedObjectReference.setVal(managedObjectId);

        VirtualMachine virtualMachine = (VirtualMachine) MorUtil.createExactManagedEntity(m_serviceInstance.getServerConnection(), managedObjectReference);

        return virtualMachine;
    }

    public HostSystem getHostSystemByManagedObjectId(String managedObjectId) {
        ManagedObjectReference managedObjectReference = new ManagedObjectReference();

        managedObjectReference.setType("HostSystem");
        managedObjectReference.setVal(managedObjectId);

        HostSystem hostSystem = (HostSystem) MorUtil.createExactManagedEntity(m_serviceInstance.getServerConnection(), managedObjectReference);

        return hostSystem;
    }

    private String getHumanReadableName(PerfCounterInfo perfCounterInfo) {
        return perfCounterInfo.getGroupInfo().getKey() + "." + perfCounterInfo.getNameInfo().getKey() + "." + perfCounterInfo.getRollupType().toString();
    }

    public VmwarePerformanceValues queryPerformanceValues(ManagedEntity managedEntity) throws RemoteException {

        VmwarePerformanceValues vmwarePerformanceValues = new VmwarePerformanceValues();

        int refreshRate = getPerformanceManager().queryPerfProviderSummary(managedEntity).getRefreshRate();

        PerfMetricId[] perfMetricIds = getPerformanceManager().queryAvailablePerfMetric(managedEntity, null, null, refreshRate);

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
                LogUtils.warnf(this, "Cannot determine ip address for host system " + hostSystem.getMOR().getVal());
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

    public ManagedEntity[] searchManagedEntities(String type) throws RemoteException {
        return (new InventoryNavigator(m_serviceInstance.getRootFolder())).searchManagedEntities(type);
    }
}
