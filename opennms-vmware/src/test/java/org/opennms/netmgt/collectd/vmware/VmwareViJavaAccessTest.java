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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceInstance.class, PerformanceManager.class, VmwareViJavaAccess.class, MorUtil.class})
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

        verify(ServiceInstance.class, serviceInstanceMock, MorUtil.class);    }

    @Test
    public void testQueryPerformanceValues() throws Exception {

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
