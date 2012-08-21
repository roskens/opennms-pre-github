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
        ICONS.put("CLUSTER_ICON", "/opennms/osgi/VAADIN/widgetsets/org.opennms.features.topology.widgetset.gwt.TopologyWidgetset/topologywidget/images/vmware-cluster.png");
    }


}
