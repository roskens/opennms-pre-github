package org.opennms.features.topology.plugins.topo.vmware.internal.operations;

import java.util.HashMap;

public class VmwareConstants {
    public static final String CENTER_VERTEX_ID = "center";
    public static final String ROOT_GROUP_ID = "Network";
    public static final String GROUP_ICON = "VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/group.png";
    public static final String SERVER_ICON = "VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/server.png";
    public static final String SWITCH_ICON = "VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/srx100.png";

    public static HashMap<String, String> ICONS;

    static {
        ICONS = new HashMap<String, String>();
        ICONS.put("NETWORK_ICON", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-network.png");
        ICONS.put("HOSTSYSTEM_ICON_ON", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-hostsystem-on.png");
        ICONS.put("HOSTSYSTEM_ICON_OFF", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-hostsystem-off.png");
        ICONS.put("HOSTSYSTEM_ICON_STANDBY", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-hostsystem-standby.png");
        ICONS.put("HOSTSYSTEM_ICON_UNKOWN", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-hostsystem-unkown.png");
        ICONS.put("VIRTUALMACHINE_ICON_ON", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-virtualmachine-on.png");
        ICONS.put("VIRTUALMACHINE_ICON_OFF", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-virtualmachine-off.png");
        ICONS.put("VIRTUALMACHINE_ICON_SUSPENDED", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-virtualmachine-suspended.png");
        ICONS.put("VIRTUALMACHINE_ICON_UNKNOWN", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-virtualmachine-unknown.png");
        ICONS.put("DATASTORE_ICON", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-datastore.png");
        ICONS.put("DATACENTER_ICON", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-datacenter.png");
    }


}
