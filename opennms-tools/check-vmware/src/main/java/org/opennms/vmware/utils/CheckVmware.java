package org.opennms.vmware.utils;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;
import com.vmware.vim25.mo.util.MorUtil;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class CheckVmware {
  // the vmware performance counters
  private static Map<Integer, PerfCounterInfo> perfCounterInfoMap = new HashMap<Integer, PerfCounterInfo>();

  public static void queryPerformanceCounters(String hostname, String username, String password, String objectId) throws Exception {
    ServiceInstance serviceInstance = new ServiceInstance(new URL("https://" + hostname + "/sdk"), username, password, true);

    PerformanceManager performanceManager = serviceInstance.getPerformanceManager();

    ManagedObjectReference managedObjectReference = new ManagedObjectReference();

    managedObjectReference.setType("ManagedEntity");
    managedObjectReference.setVal(objectId);

    ManagedEntity managedEntity = MorUtil.createExactManagedEntity(serviceInstance.getServerConnection(), managedObjectReference);

    int refreshRate = performanceManager.queryPerfProviderSummary(managedEntity).getRefreshRate();

    PerfMetricId[] perfMetricIds = performanceManager.queryAvailablePerfMetric(managedEntity, null, null, refreshRate);

    PerfQuerySpec perfQuerySpec = new PerfQuerySpec();
    perfQuerySpec.setEntity(managedEntity.getMOR());
    perfQuerySpec.setMaxSample(new Integer(1));
    perfQuerySpec.setIntervalId(refreshRate);

    PerfEntityMetricBase[] perfEntityMetricBases = performanceManager.queryPerf(new PerfQuerySpec[] { perfQuerySpec });

    if (perfEntityMetricBases != null) {
      buildPerfCounterMap(performanceManager);

      for (int i = 0; i < perfEntityMetricBases.length; i++) {
        PerfMetricSeries[] perfMetricSeries = ((PerfEntityMetric) perfEntityMetricBases[i]).getValue();

        for (int j = 0; perfMetricSeries != null && j < perfMetricSeries.length; j++) {

          if (perfMetricSeries[j] instanceof PerfMetricIntSeries) {
            long[] longs = ((PerfMetricIntSeries) perfMetricSeries[j]).getValue();

            if (longs.length == 1) {

              PerfCounterInfo perfCounterInfo = perfCounterInfoMap.get(perfMetricSeries[j].getId().getCounterId());
              String instance = perfMetricSeries[j].getId().getInstance();
              String name = getHumanReadableName(perfCounterInfo);

              if (instance != null && !"".equals(instance))
                System.out.println(name + "[" + instance + "]=" + longs[0]);
              else
                System.out.println(name+"=" + longs[0]);
            }
          }
        }
      }
    }
  }

  public static void queryAvailablePerformanceCounters(String hostname, String username, String password) throws Exception {
    ServiceInstance serviceInstance = new ServiceInstance(new URL("https://" + hostname + "/sdk"), username, password, true);
    PerformanceManager performanceManager = serviceInstance.getPerformanceManager();

    PerfCounterInfo[] pcis = performanceManager.getPerfCounter();

    for (int i = 0; pcis != null && i < pcis.length; i++) {
      String perfCounter = pcis[i].getGroupInfo().getKey() + "." + pcis[i].getNameInfo().getKey() + "." + pcis[i].getRollupType();
      System.out.print("name=" + perfCounter);
      System.out.print(", key=" + pcis[i].getKey());
      System.out.print(", level=" + pcis[i].getLevel());
      System.out.print(", type=" + pcis[i].getStatsType());
      System.out.println(", unit=" + pcis[i].getUnitInfo().getKey());
    }

    serviceInstance.getServerConnection().logout();
  }

  public static void listManagedObjects(String hostname, String username, String password) throws Exception {
    ServiceInstance serviceInstance = new ServiceInstance(new URL("https://" + hostname + "/sdk"), username, password, true);

    ManagedEntity[] managedEntitites;

    managedEntitites = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities("HostSystem");

    if (managedEntitites != null) {
      for (ManagedEntity managedEntity : managedEntitites) {
        HostSystem hostSystem = (HostSystem) managedEntity;

        System.out.print("objectId=" + hostSystem.getMOR().getVal());
        System.out.print(", name=" + hostSystem.getName());

        for (HostVirtualNic hostVirtualNic : hostSystem.getHostNetworkSystem().getNetworkInfo().getConsoleVnic()) {
          System.out.print(", ipAddress=" + hostVirtualNic.getSpec().getIp().getIpAddress());
        }
        System.out.println();
      }
    }

    managedEntitites = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities("VirtualMachine");

    if (managedEntitites != null) {
      for (ManagedEntity managedEntity : managedEntitites) {
        VirtualMachine virtualMachine = (VirtualMachine) managedEntity;

        System.out.print("objectId=" + virtualMachine.getMOR().getVal());
        System.out.print(", name=" + virtualMachine.getName());

        LinkedHashSet<String> ipAddresses = new LinkedHashSet<String>();

        // add the Ip address reported by VMware tools, this should be primary
        ipAddresses.add(virtualMachine.getGuest().getIpAddress());

        // if possible, iterate over all virtual networks networks and add
        // interface Ip addresses
        if (virtualMachine.getGuest().getNet() != null) {
          for (GuestNicInfo guestNicInfo : virtualMachine.getGuest().getNet()) {
            if (guestNicInfo.getIpAddress() != null) {
              for (String ipAddress : guestNicInfo.getIpAddress())
                ipAddresses.add(ipAddress);
            }
          }
        }

        for (String ipAddress : ipAddresses) {
          System.out.print(", ipAddress=" + ipAddress);
        }

        System.out.println(", template=" + (virtualMachine.getConfig().isTemplate() ? "yes" : "no"));
      }
    }

    serviceInstance.getServerConnection().logout();
  }

  private static void buildPerfCounterMap(PerformanceManager performanceManager) {
    PerfCounterInfo[] perfCounterInfos = performanceManager.getPerfCounter();

    for (PerfCounterInfo perfCounterInfo : perfCounterInfos) {
      perfCounterInfoMap.put(perfCounterInfo.getKey(), perfCounterInfo);
    }
  }

  private static String getHumanReadableName(PerfCounterInfo perfCounterInfo) {
    return perfCounterInfo.getGroupInfo().getKey() + "." + perfCounterInfo.getNameInfo().getKey() + "." + perfCounterInfo.getRollupType().toString();
  }

  public static void main(String args[]) throws Exception {
    String hostname = null;
    String username = null;
    String password = null;
    String command = null;
    String objectId = null;

    command = (args.length > 0 ? args[0] : null);
    hostname = (args.length > 1 ? args[1] : null);
    username = (args.length > 2 ? args[2] : null);
    password = (args.length > 3 ? args[3] : null);
    objectId = (args.length > 4 ? args[4] : null);

    if ("list".equals(command) && hostname != null && username != null && password != null) {
      listManagedObjects(hostname, username, password);
    } else {
      if ("query".equals(command) && hostname != null && username != null && password != null && objectId != null) {
        queryPerformanceCounters(hostname, username, password, objectId);
      } else {
        if ("info".equals(command) && hostname != null && username != null && password != null) {
          queryAvailablePerformanceCounters(hostname, username, password);
        } else {
          System.out.println("Usage: CheckVmware (list|query|info) <hostname> <username> <password> [objectId]\n");
          System.out.println("CheckVmware list <hostname> <username> <password>");
          System.out.println(" - lists all HostSystem and VirtualMachine objects\n");
          System.out.println("CheckVmware info <hostname> <username> <password>");
          System.out.println(" - display information about available perfomance counters\n");
          System.out.println("CheckVmware query <hostname> <username> <password> <objectId>");
          System.out.println(" - queries perfomance counters of the object identified by objectId\n");
        }
      }
    }
  }
}
