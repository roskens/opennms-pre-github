package org.opennms.netmgt.collectd.vmware;

import com.vmware.vim25.ElementDescription;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

import com.vmware.vim25.mo.util.MorUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.opennms.netmgt.collectd.vmware.vijava.VmwarePerformanceValues;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceInstance.class, PerformanceManager.class, VmwareViJavaAccess.class, MorUtil.class, PerfProviderSummary.class})
public class VmwareViJavaAccessTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetPerfCounterInfoMap() {
        VmwareViJavaAccess vmwareViJavaAccessMock = createPartialMock(VmwareViJavaAccess.class, new String[]{"getPerformanceManager"});

        PerformanceManager mockPerformanceManager = createMock(PerformanceManager.class);

        try {
            expectPrivate(vmwareViJavaAccessMock, "getPerformanceManager").andReturn(mockPerformanceManager);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        PerfCounterInfo[] perfCounterInfos = new PerfCounterInfo[3];

        for (int i = 0; i < 3; i++) {
            PerfCounterInfo perfCounterInfo = new PerfCounterInfo();
            perfCounterInfo.setKey(i);
            perfCounterInfos[i] = perfCounterInfo;
        }

        expect(mockPerformanceManager.getPerfCounter()).andReturn(perfCounterInfos);

        replayAll();

        Map<Integer, PerfCounterInfo> perfCounterInfoMap = vmwareViJavaAccessMock.getPerfCounterInfoMap();

        Assert.assertEquals(perfCounterInfoMap.size(), 3);

        for (int i : perfCounterInfoMap.keySet()) {
            Assert.assertEquals(perfCounterInfoMap.get(i), perfCounterInfos[i]);
        }

        verifyAll();
    }

    @Test
    public void testGetManagedEntityByManagedObjectId() {
        ManagedObjectReference managedObjectReference = new ManagedObjectReference();

        managedObjectReference.setType("ManagedEntity");
        managedObjectReference.setVal("moId");

        ManagedEntity expectedManagedEntity = new ManagedEntity(null, managedObjectReference);

        mockStatic(MorUtil.class);
        expect(MorUtil.createExactManagedEntity(null, managedObjectReference)).andReturn(expectedManagedEntity);

        VmwareViJavaAccess vmwareViJavaAccess = new VmwareViJavaAccess("hostname", "username", "password");

        ServiceInstance serviceInstanceMock = createMock(ServiceInstance.class);

        try {
            expectNew(ServiceInstance.class, new Class<?>[]{URL.class, String.class, String.class}, new URL("https://hostname/sdk"), "username", "password").andReturn(serviceInstanceMock);

            expect(serviceInstanceMock.getServerConnection()).andReturn(null);

            replay(ServiceInstance.class, serviceInstanceMock, MorUtil.class);

            vmwareViJavaAccess.connect();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        ManagedEntity returnedManagedEntity = vmwareViJavaAccess.getManagedEntityByManagedObjectId("moId");

        Assert.assertNotNull(returnedManagedEntity);
        Assert.assertNotNull(expectedManagedEntity);
        Assert.assertEquals(expectedManagedEntity.getMOR().getVal(), returnedManagedEntity.getMOR().getVal());

        verify(ServiceInstance.class, serviceInstanceMock, MorUtil.class);
    }

    @Test
    public void testGetVirtualMachineByManagedObjectId() throws Exception {
        ManagedObjectReference managedObjectReference = new ManagedObjectReference();

        managedObjectReference.setType("VirtualMachine");
        managedObjectReference.setVal("moId");

        VirtualMachine expectedVirtualMachine = new VirtualMachine(null, managedObjectReference);

        mockStatic(MorUtil.class);
        expect(MorUtil.createExactManagedEntity(null, managedObjectReference)).andReturn(expectedVirtualMachine);

        VmwareViJavaAccess vmwareViJavaAccess = new VmwareViJavaAccess("hostname", "username", "password");

        ServiceInstance serviceInstanceMock = createMock(ServiceInstance.class);

        try {
            expectNew(ServiceInstance.class, new Class<?>[]{URL.class, String.class, String.class}, new URL("https://hostname/sdk"), "username", "password").andReturn(serviceInstanceMock);

            expect(serviceInstanceMock.getServerConnection()).andReturn(null);

            replay(ServiceInstance.class, serviceInstanceMock, MorUtil.class);

            vmwareViJavaAccess.connect();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        VirtualMachine returnedVirtualMachine = vmwareViJavaAccess.getVirtualMachineByManagedObjectId("moId");

        Assert.assertNotNull(returnedVirtualMachine);
        Assert.assertNotNull(expectedVirtualMachine);
        Assert.assertEquals(expectedVirtualMachine.getMOR().getVal(), returnedVirtualMachine.getMOR().getVal());

        verify(ServiceInstance.class, serviceInstanceMock, MorUtil.class);
    }

    @Test
    public void testGetHostSystemByManagedObjectId() throws Exception {
        ManagedObjectReference managedObjectReference = new ManagedObjectReference();

        managedObjectReference.setType("HostSystem");
        managedObjectReference.setVal("moId");

        HostSystem expectedHostSystem = new HostSystem(null, managedObjectReference);

        mockStatic(MorUtil.class);
        expect(MorUtil.createExactManagedEntity(null, managedObjectReference)).andReturn(expectedHostSystem);

        VmwareViJavaAccess vmwareViJavaAccess = new VmwareViJavaAccess("hostname", "username", "password");

        ServiceInstance serviceInstanceMock = createMock(ServiceInstance.class);

        try {
            expectNew(ServiceInstance.class, new Class<?>[]{URL.class, String.class, String.class}, new URL("https://hostname/sdk"), "username", "password").andReturn(serviceInstanceMock);

            expect(serviceInstanceMock.getServerConnection()).andReturn(null);

            replay(ServiceInstance.class, serviceInstanceMock, MorUtil.class);

            vmwareViJavaAccess.connect();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

        HostSystem returnedHostSystem = vmwareViJavaAccess.getHostSystemByManagedObjectId("moId");

        Assert.assertNotNull(returnedHostSystem);
        Assert.assertNotNull(expectedHostSystem);
        Assert.assertEquals(expectedHostSystem.getMOR().getVal(), returnedHostSystem.getMOR().getVal());

        verify(ServiceInstance.class, serviceInstanceMock, MorUtil.class);
    }

    @Test
    public void testQueryPerformanceValues() throws Exception {
        VmwareViJavaAccess vmwareViJavaAccessMock = createPartialMock(VmwareViJavaAccess.class, new String[]{"getPerformanceManager"});

        PerformanceManager performanceManagerMock = createMock(PerformanceManager.class);

        PerfProviderSummary perfProviderSummaryMock = createMock(PerfProviderSummary.class);

        ManagedObjectReference managedObjectReference = new ManagedObjectReference();

        managedObjectReference.setType("ManagedEntity");
        managedObjectReference.setVal("moId");

        ManagedEntity managedEntity = new ManagedEntity(null, managedObjectReference) {
            public String toString() {
                return "";
            }
        };

        int refreshRate = 100;

        PerfQuerySpec perfQuerySpec = new PerfQuerySpec();
        perfQuerySpec.setEntity(managedEntity.getMOR());
        perfQuerySpec.setMaxSample(new
                Integer(1)
        );
        perfQuerySpec.setIntervalId(refreshRate);

        int metricCount = 15;
        int instanceCount = 5;

        PerfEntityMetricBase[] perfEntityMetricBases = new PerfEntityMetricBase[metricCount];
        PerfCounterInfo[] perfCounterInfos = new PerfCounterInfo[metricCount];

        for (int i = 0; i < metricCount; i++) {
            ElementDescription groupInfo = new ElementDescription();
            groupInfo.setKey("key" + i);

            ElementDescription nameInfo = new ElementDescription();
            nameInfo.setKey("name" + i);

            perfCounterInfos[i] = new PerfCounterInfo();
            perfCounterInfos[i].setKey(i);
            perfCounterInfos[i].setGroupInfo(groupInfo);
            perfCounterInfos[i].setNameInfo(nameInfo);
            perfCounterInfos[i].setRollupType(PerfSummaryType.average);

            perfEntityMetricBases[i] = new PerfEntityMetric();

            PerfMetricIntSeries[] perfMetricIntSeries;

            if (i % 3 < 2)
                perfMetricIntSeries = new PerfMetricIntSeries[1];
            else
                perfMetricIntSeries = new PerfMetricIntSeries[instanceCount];


            for (int b = 0; b < (i % 3 < 2 ? 1 : instanceCount); b++) {
                PerfMetricId perfMetricId = new PerfMetricId();
                perfMetricId.setCounterId(i);

                if (i % 3 == 0) {
                    perfMetricId.setInstance(null);
                } else {
                    if (i % 3 == 1) {
                        perfMetricId.setInstance("");
                    } else {
                        perfMetricId.setInstance("instance" + b);
                    }
                }

                perfMetricIntSeries[b] = new PerfMetricIntSeries();
                perfMetricIntSeries[b].setValue(new long[]{(long) 123});
                perfMetricIntSeries[b].setId(perfMetricId);
            }

            ((PerfEntityMetric) perfEntityMetricBases[i]).setValue(perfMetricIntSeries);
        }

        expectPrivate(vmwareViJavaAccessMock, "getPerformanceManager").andReturn(performanceManagerMock).anyTimes();
        expect(performanceManagerMock.queryPerfProviderSummary(managedEntity)).andReturn(perfProviderSummaryMock);
        expect(perfProviderSummaryMock.getRefreshRate()).andReturn(refreshRate);
        expect(performanceManagerMock.getPerfCounter()).andReturn(perfCounterInfos).anyTimes();
        expect(performanceManagerMock.queryPerf(anyObject(PerfQuerySpec[].class))).andReturn(perfEntityMetricBases);

        replayAll(performanceManagerMock, perfProviderSummaryMock, vmwareViJavaAccessMock);

        VmwarePerformanceValues vmwarePerformanceValues = vmwareViJavaAccessMock.queryPerformanceValues(managedEntity);

        Assert.assertNotNull(vmwarePerformanceValues);

        for (int i = 0; i < metricCount; i++) {
            PerfCounterInfo perfCounterInfo = perfCounterInfos[i];

            String expectedName = perfCounterInfo.getGroupInfo().getKey() + "." + perfCounterInfo.getNameInfo().getKey() + "." + perfCounterInfo.getRollupType().toString();

            if (vmwarePerformanceValues.hasInstances(expectedName)) {
                Set<String> instances = vmwarePerformanceValues.getInstances(expectedName);

                Assert.assertEquals(instances.size(), ((PerfEntityMetric) perfEntityMetricBases[i]).getValue().length);

                PerfMetricIntSeries[] perfMetricIntSeries = (PerfMetricIntSeries[]) ((PerfEntityMetric) perfEntityMetricBases[i]).getValue();

                for (int b = 0; b < perfMetricIntSeries.length; b++) {
                    Assert.assertTrue(instances.contains(perfMetricIntSeries[b].getId().getInstance()));
                }
            } else {
                Assert.assertEquals(1, ((PerfEntityMetric) perfEntityMetricBases[i]).getValue().length);
            }
        }

        verifyAll();
    }


    @Test
    public void testQueryCimObjects() throws Exception {

    }

    @Test
    public void testSearchManagedEntities() throws Exception {

    }

    @Test
    public void testGetPropertyOfCimObject() throws Exception {

    }
}
