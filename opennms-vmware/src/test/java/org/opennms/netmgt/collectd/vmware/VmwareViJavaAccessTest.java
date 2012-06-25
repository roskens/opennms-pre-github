package org.opennms.netmgt.collectd.vmware;

import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;
import java.util.Map;


import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceInstance.class, PerformanceManager.class})
public class VmwareViJavaAccessTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetPerfCounterInfoMap() {
        String xyz = createMock(String.class);
        ServiceInstance mockServiceInstance = createMock(ServiceInstance.class);
        PerformanceManager mockPerformanceManager = createMock(PerformanceManager.class);

        try {
            expectNew(ServiceInstance.class, new Class<?>[]{URL.class, String.class, String.class}, new URL("https://hostname/sdk"), "username", "password").andReturn(mockServiceInstance);
            expectNew(PerformanceManager.class).andReturn(mockPerformanceManager);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        PerfCounterInfo[] perfCounterInfos = new PerfCounterInfo[3];

        perfCounterInfos[0] = new PerfCounterInfo();
        perfCounterInfos[1] = new PerfCounterInfo();
        perfCounterInfos[2] = new PerfCounterInfo();

        expect(mockPerformanceManager.getPerfCounter()).andReturn(perfCounterInfos);

        VmwareViJavaAccess vmwareViJavaAccess = new VmwareViJavaAccess("hostname", "username", "password");

        Map<Integer, PerfCounterInfo> perfCounterInfoMap = vmwareViJavaAccess.getPerfCounterInfoMap();

        for (int i : perfCounterInfoMap.keySet()) {
            System.out.println(i + ". " + perfCounterInfoMap.get(i));
        }
    }
}
