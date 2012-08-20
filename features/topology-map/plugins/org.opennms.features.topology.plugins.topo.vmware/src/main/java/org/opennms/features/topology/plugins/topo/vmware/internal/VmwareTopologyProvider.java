package org.opennms.features.topology.plugins.topo.vmware.internal;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import org.opennms.features.topology.api.TopologyProvider;
import org.opennms.features.topology.plugins.topo.vmware.internal.operations.VmwareConstants;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsNode;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.*;

public class VmwareTopologyProvider implements TopologyProvider {

    private interface VmwareEntityTypeHandler {
        public void handleEntity(String managedObjectId, String managedEntityType, String managementServer, VmwareVertex entityVertex, VmwareTopologyEntity vmwareTopologyEntity, HashMap<String, ArrayList<VmwareTopologyEntity>> vmwareTopologyEntities);
    }

    private class VmwareTopologyEntity {
        private String id, name, type;

        public VmwareTopologyEntity(String id, String type, String name) {
            this.id = id;
            this.type = type;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    private HashMap<String, VmwareEntityTypeHandler> m_entityHandlers = new HashMap<String, VmwareEntityTypeHandler>();

    NodeDao m_nodeDao;

    private VmwareVertexContainer m_vertexContainer;
    private BeanContainer<String, VmwareEdge> m_edgeContainer;
    private int m_counter = 0;
    private int m_edgeCounter = 0;
    private int m_groupCounter = 0;
    private boolean m_generated = false;

    public VmwareTopologyProvider() {
        m_vertexContainer = new VmwareVertexContainer();
        m_edgeContainer = new BeanContainer<String, VmwareEdge>(VmwareEdge.class);
        m_edgeContainer.setBeanIdProperty("id");

        resetContainer();

        addEntityHandler("datacenter", new VmwareEntityTypeHandler() {
            public void handleEntity(String managedObjectId, String managedEntityType, String managementServer, VmwareVertex entityVertex, VmwareTopologyEntity vmwareTopologyEntity, HashMap<String, ArrayList<VmwareTopologyEntity>> vmwareTopologyEntities) {
                // create the datacenter if it does not exist already
                VmwareGroup datacenterVertex = addEntityVertexGroup(managementServer + "/" + vmwareTopologyEntity.getId(), vmwareTopologyEntity.getName(), "DATACENTER_ICON");

                // if this entity is a virtual machine, do nothing
                if ("VirtualMachine".equals(managedEntityType)) {
                    return;
                } else {
                    // if cluster entries exist, do nothing
                    if (vmwareTopologyEntities.containsKey("cluster")) {
                        return;
                    } else {
                        // set the entity's parent to this datacenter vertex
                        entityVertex.setParent(datacenterVertex);
                    }
                }
            }
        });

        addEntityHandler("cluster", new VmwareEntityTypeHandler() {
            public void handleEntity(String managedObjectId, String managedEntityType, String managementServer, VmwareVertex entityVertex, VmwareTopologyEntity vmwareTopologyEntity, HashMap<String, ArrayList<VmwareTopologyEntity>> vmwareTopologyEntities) {
                // create cluster if it does not exist already
                VmwareGroup clusterVertex = addEntityVertexGroup(managementServer + "/" + vmwareTopologyEntity.getId(), vmwareTopologyEntity.getName(), "CLUSTER_ICON");

                // check for the datacenter entries
                ArrayList<VmwareTopologyEntity> datacenterEntities = vmwareTopologyEntities.get("datacenter");

                for (VmwareTopologyEntity datacenterEntity : datacenterEntities) {
                    // add them if they do not exist already
                    VmwareGroup datacenterVertex = addEntityVertexGroup(managementServer + "/" + datacenterEntity.getId(), datacenterEntity.getName(), "DATACENTER_ICON");

                    // and set the parent of the cluster to the datacenter
                    clusterVertex.setParent(datacenterVertex);
                }

                // this entity's parent is the cluster vertex
                entityVertex.setParent(clusterVertex);
            }
        });

        addEntityHandler("host", new VmwareEntityTypeHandler() {
            public void handleEntity(String managedObjectId, String managedEntityType, String managementServer, VmwareVertex entityVertex, VmwareTopologyEntity vmwareTopologyEntity, HashMap<String, ArrayList<VmwareTopologyEntity>> vmwareTopologyEntities) {
                // - a host entity does only exists on virtual machines
                // - host systems have no direct relationship to other host systems, they have possibly the same parent entity (datacenter, cluster, folder)

                // the host system must exist already, because host systems vertices are created first

                // so, if entity is a virtual machine connect it to the host system
                if ("VirtualMachine".equals(managedEntityType)) {
                    connectVertices(managementServer + "/" + managedObjectId + "->" + managementServer + "/" + vmwareTopologyEntity.getId(), managementServer + "/" + managedObjectId, managementServer + "/" + vmwareTopologyEntity.getId());

                    ArrayList<VmwareTopologyEntity> hostEntities = vmwareTopologyEntities.get("host");

                    if (hostEntities != null) {
                        for (VmwareTopologyEntity hostEntity : hostEntities) {
                            entityVertex.setParent((VmwareGroup) getRequiredVertex(managementServer + "/" + hostEntity.getId()));
                        }
                    }

                }
            }
        });

        addEntityHandler("network", new VmwareEntityTypeHandler() {
            public void handleEntity(String managedObjectId, String managedEntityType, String managementServer, VmwareVertex entityVertex, VmwareTopologyEntity vmwareTopologyEntity, HashMap<String, ArrayList<VmwareTopologyEntity>> vmwareTopologyEntities) {
                // create the network, if it does not exist already
                VmwareVertex networkVertex = addEntityVertex(managementServer + "/" + vmwareTopologyEntity.getId(), vmwareTopologyEntity.getName(), "NETWORK_ICON");

                ArrayList<VmwareTopologyEntity> entities = vmwareTopologyEntities.get("cluster");

                if (entities == null)
                    entities = vmwareTopologyEntities.get("datacenter");

                if (entities != null) {
                    for (VmwareTopologyEntity entity : entities) {
                        networkVertex.setParent((VmwareGroup) getRequiredVertex(managementServer + "/" + entity.getId()));
                    }
                }

                // connect it to this vertex
                connectVertices(managementServer + "/" + managedObjectId + "->" + vmwareTopologyEntity.getId(), managementServer + "/" + managedObjectId, managementServer + "/" + vmwareTopologyEntity.getId());
            }
        });

        addEntityHandler("datastore", new VmwareEntityTypeHandler() {
            public void handleEntity(String managedObjectId, String managedEntityType, String managementServer, VmwareVertex entityVertex, VmwareTopologyEntity vmwareTopologyEntity, HashMap<String, ArrayList<VmwareTopologyEntity>> vmwareTopologyEntities) {
                // create the network, if it does not exist already
                VmwareVertex datastoreVertex = addEntityVertex(managementServer + "/" + vmwareTopologyEntity.getId(), vmwareTopologyEntity.getName(), "NETWORK_ICON");

                ArrayList<VmwareTopologyEntity> entities = vmwareTopologyEntities.get("cluster");

                if (entities == null)
                    entities = vmwareTopologyEntities.get("datacenter");

                if (entities != null) {
                    for (VmwareTopologyEntity entity : entities) {
                        datastoreVertex.setParent((VmwareGroup) getRequiredVertex(managementServer + "/" + entity.getId()));
                    }
                }

                // connect it to this vertex
                connectVertices(managementServer + "/" + managedObjectId + "->" + vmwareTopologyEntity.getId(), managementServer + "/" + managedObjectId, managementServer + "/" + vmwareTopologyEntity.getId());
            }
        });
    }

    public void generateNew() {
        m_generated = true;

        // reset container
        resetContainer();

        // get all host systems
        List<OnmsNode> hostSystems = m_nodeDao.findAllByVarCharAssetColumn("vmwareManagedEntityType", "HostSystem");

        if (hostSystems.size() == 0) {
            System.err.println("No host systems with defined VMware assets fields found!");
        } else {
            for (OnmsNode hostSystem : hostSystems) {

                String vmwareManagementServer = hostSystem.getAssetRecord().getVmwareManagementServer().trim();
                String vmwareManagedObjectId = hostSystem.getAssetRecord().getVmwareManagedObjectId().trim();
                String vmwareTopologyInfo = hostSystem.getAssetRecord().getVmwareTopologyInfo().trim();
                String vmwareState = hostSystem.getAssetRecord().getVmwareState().trim();

                String primaryInterface = "unknown";

                // get the primary interface ip address

                if (hostSystem.getPrimaryInterface() != null) {
                    primaryInterface = hostSystem.getPrimaryInterface().getIpHostName();
                }

                VmwareVertex hostSystemVertex = addHostSystemVertex(vmwareManagementServer + "/" + vmwareManagedObjectId, hostSystem.getLabel(), primaryInterface, hostSystem.getId(), vmwareState);

                handleEntities(vmwareManagedObjectId, "HostSystem", vmwareManagementServer, vmwareTopologyInfo, hostSystemVertex);
            }
        }

        // get all host systems
        List<OnmsNode> virtualMachines = m_nodeDao.findAllByVarCharAssetColumn("vmwareManagedEntityType", "VirtualMachine");

        if (virtualMachines.size() == 0) {
            System.err.println("No virtual machines with defined VMware assets fields found!");
        } else {
            for (OnmsNode virtualMachine : virtualMachines) {

                String vmwareManagementServer = virtualMachine.getAssetRecord().getVmwareManagementServer().trim();
                String vmwareManagedObjectId = virtualMachine.getAssetRecord().getVmwareManagedObjectId().trim();
                String vmwareTopologyInfo = virtualMachine.getAssetRecord().getVmwareTopologyInfo().trim();
                String vmwareState = virtualMachine.getAssetRecord().getVmwareState().trim();

                String primaryInterface = "unknown";

                // get the primary interface ip address

                if (virtualMachine.getPrimaryInterface() != null) {
                    primaryInterface = virtualMachine.getPrimaryInterface().getIpHostName();
                }

                VmwareVertex virtualMachineVertex = addVirtualMachineVertex(vmwareManagementServer + "/" + vmwareManagedObjectId, virtualMachine.getLabel(), primaryInterface, virtualMachine.getId(), vmwareState);

                handleEntities(vmwareManagedObjectId, "VirtualMachine", vmwareManagementServer, vmwareTopologyInfo, virtualMachineVertex);
            }
        }

        for (String id : m_vertexContainer.getItemIds()) {
            debug(id);
        }
    }

    public void prepareGroupTree(String managedObjectId, String managedEntityType, String managementServer, HashMap<String, ArrayList<VmwareTopologyEntity>> vmwareTopologyEntities) {

        ArrayList<VmwareTopologyEntity> datacenterEntities = vmwareTopologyEntities.get("datacenter");

        for (VmwareTopologyEntity datacenterEntity : datacenterEntities) {
            addEntityVertexGroup(managementServer + "/" + datacenterEntity.getId(), datacenterEntity.getName(), "DATACENTER_ICON");
        }

        ArrayList<VmwareTopologyEntity> clusterEntities = vmwareTopologyEntities.get("cluster");

        for (VmwareTopologyEntity clusterEntity : clusterEntities) {
            addEntityVertexGroup(managementServer + "/" + clusterEntity.getId(), clusterEntity.getName(), "CLUSTER_ICON");
        }
    }

    HashMap<String, ArrayList<VmwareTopologyEntity>> prepareEntities(String entitiesString) {
        HashMap<String, ArrayList<VmwareTopologyEntity>> vmwareTopologyEntities = new HashMap<String, ArrayList<VmwareTopologyEntity>>();

        String entitites[] = entitiesString.split("[, ]+");

        for (String entityAndName : entitites) {
            String splitBySlash[] = entityAndName.split("/");
            String entityId = splitBySlash[0];

            String entityName = new String(splitBySlash[1]);

            String entityType = entityId.split("-")[0];

            if (!vmwareTopologyEntities.containsKey(entityType)) {
                vmwareTopologyEntities.put(entityType, new ArrayList<VmwareTopologyEntity>());
            }

            vmwareTopologyEntities.get(entityType).add(new VmwareTopologyEntity(entityId, entityType, entityName));
        }

        return vmwareTopologyEntities;
    }

    public void handleEntities(String managedObjectId, String managedEntityType, String managementServer, String entitiesString, VmwareVertex vmwareVertex) {
        HashMap<String, ArrayList<VmwareTopologyEntity>> vmwareTopologyEntities = prepareEntities(entitiesString);

        for (String type : vmwareTopologyEntities.keySet()) {
            ArrayList<VmwareTopologyEntity> entities = vmwareTopologyEntities.get(type);

            for (VmwareTopologyEntity vmwareTopologyEntity : entities) {

                VmwareEntityTypeHandler vmwareEntityTypeHandler = m_entityHandlers.get(vmwareTopologyEntity.getType());
                if (vmwareEntityTypeHandler == null) {
                    System.err.println("A handler for EntityType '" + vmwareTopologyEntity.getType() + "' not implemented.");
                } else {
                    vmwareEntityTypeHandler.handleEntity(managedObjectId, managedEntityType, managementServer, vmwareVertex, vmwareTopologyEntity, vmwareTopologyEntities);
                }
            }
        }
    }

    public void addEntityHandler(String entityType, VmwareEntityTypeHandler vmwareEntityTypeHandler) {
        m_entityHandlers.put(entityType, vmwareEntityTypeHandler);
    }

    public NodeDao getNodeDao() {
        return m_nodeDao;
    }

    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    public boolean isGenerated() {
        return m_generated;
    }

    public void debug(String id) {

        VmwareVertex vmwareVertex = getRequiredVertex(id);

        System.err.println("-+- id: " + vmwareVertex.getId());
        System.err.println(" |- hashCode: " + vmwareVertex.hashCode());
        System.err.println(" |- label: " + vmwareVertex.getLabel());
        System.err.println(" |- ip: " + vmwareVertex.getIpAddr());
        System.err.println(" |- nodeId: " + vmwareVertex.getNodeID());

        for (VmwareEdge vmwareEdge : vmwareVertex.getEdges()) {
            String edgeTo = vmwareEdge.getTarget().getId();
            if (id.equals(edgeTo))
                edgeTo = vmwareEdge.getSource().getId();
            System.err.println(" |- edgeTo: " + edgeTo);
        }
        System.err.println(" '- parent: " + (vmwareVertex.getParent() == null ? null : vmwareVertex.getParent().getId()));
    }

    public VmwareGroup addDatacenterGroup(String groupId, String groupName) {
        if (!m_vertexContainer.containsId(groupId)) {
            addGroup(groupId, "DATACENTER_ICON", groupName);
        }
        return (VmwareGroup) getRequiredVertex(groupId);
    }

    public VmwareVertex addNetworkVertex(String vertexId, String vertexName) {
        if (!m_vertexContainer.containsId(vertexId)) {
            addVertex(vertexId, 50, 50, "NETWORK_ICON", vertexName, "", -1);
        }
        return getRequiredVertex(vertexId);
    }

    public VmwareVertex addEntityVertex(String vertexId, String vertexName, String iconId) {
        if (!m_vertexContainer.containsId(vertexId)) {
            addVertex(vertexId, 50, 50, iconId, vertexName, "", -1);
        }
        return getRequiredVertex(vertexId);
    }

    public VmwareGroup addEntityVertexGroup(String vertexId, String vertexName, String iconId) {
        if (!m_vertexContainer.containsId(vertexId)) {
            addGroup(vertexId, iconId, vertexName);
        }
        return (VmwareGroup) getRequiredVertex(vertexId);
    }

    public VmwareVertex addDatastoreVertex(String vertexId, String vertexName) {
        if (!m_vertexContainer.containsId(vertexId)) {
            addVertex(vertexId, 50, 50, "DATASTORE_ICON", vertexName, "", -1);
        }
        return getRequiredVertex(vertexId);
    }

    public VmwareVertex addVirtualMachineVertex(String vertexId, String vertexName, String primaryInterface, int id, String powerState) {
        String icon = "VIRTUALMACHINE_ICON_UNKNOWN";

        if ("poweredOn".equals(powerState))
            icon = "VIRTUALMACHINE_ICON_ON";
        if ("poweredOff".equals(powerState))
            icon = "VIRTUALMACHINE_ICON_OFF";
        if ("suspended".equals(powerState))
            icon = "VIRTUALMACHINE_ICON_SUSPENDED";

        addVertex(vertexId, 50, 50, icon, vertexName, primaryInterface, id);

        return getRequiredVertex(vertexId);
    }

    public VmwareVertex addHostSystemVertex(String vertexId, String vertexName, String primaryInterface, int id, String powerState) {
        String icon = "HOSTSYSTEM_ICON_UNKOWN";

        if ("poweredOn".equals(powerState))
            icon = "HOSTSYSTEM_ICON_ON";
        if ("poweredOff".equals(powerState))
            icon = "HOSTSYSTEM_ICON_OFF";
        if ("standBy".equals(powerState))
            icon = "HOSTSYSTEM_ICON_STANDBY";

        addVertex(vertexId, 50, 50, icon, vertexName, primaryInterface, id);

        return getRequiredVertex(vertexId);
    }

    /*
        public void addHostSystem(OnmsNode hostSystem) {
            // getting data for nodes

            String vmwareManagementServer = hostSystem.getAssetRecord().getVmwareManagementServer().trim();
            String vmwareNetworks = hostSystem.getAssetRecord().getVmwareNetworks().trim();
            String vmwareDatastores = hostSystem.getAssetRecord().getVmwareDatastores().trim();
            String vmwareManagedObjectId = hostSystem.getAssetRecord().getVmwareManagedObjectId().trim();
            String vmwarePowerState = hostSystem.getAssetRecord().getVmwareRuntimeInformation();

            VmwareGroup datacenterVertex = addDatacenterGroup(vmwareManagementServer, "Datacenter (" + vmwareManagementServer + ")");

            String primaryInterface = "unknown";

            // get the primary interface ip address

            if (hostSystem.getPrimaryInterface() != null) {
                primaryInterface = hostSystem.getPrimaryInterface().getIpHostName();
            }

            VmwareVertex hostSystemVertex = addHostSystemVertex(vmwareManagementServer + "/" + vmwareManagedObjectId, hostSystem.getLabel(), primaryInterface, hostSystem.getId(), vmwarePowerState);

            // set the parent vertex
            hostSystemVertex.setParent(datacenterVertex);

            for (String network : vmwareNetworks.split("[, ]+")) {
                VmwareVertex networkVertex = addNetworkVertex(vmwareManagementServer + "/" + network, network);
                networkVertex.setParent(datacenterVertex);
                connectVertices(vmwareManagementServer + "/" + vmwareManagedObjectId + "->" + network, vmwareManagementServer + "/" + vmwareManagedObjectId, vmwareManagementServer + "/" + network);
            }
            for (String datastore : vmwareDatastores.split("[, ]+")) {
                VmwareVertex datastoreVertex = addDatastoreVertex(vmwareManagementServer + "/" + datastore, datastore);
                datastoreVertex.setParent(datacenterVertex);
                connectVertices(vmwareManagementServer + "/" + vmwareManagedObjectId + "->" + datastore, vmwareManagementServer + "/" + vmwareManagedObjectId, vmwareManagementServer + "/" + datastore);
            }
        }

        public void addVirtualMachine(OnmsNode virtualMachine) {
            // getting data for nodes

            String vmwareManagementServer = virtualMachine.getAssetRecord().getVmwareManagementServer().trim();
            String vmwareRuntimeInformation = virtualMachine.getAssetRecord().getVmwareRuntimeInformation().trim();
            String vmwareManagedObjectId = virtualMachine.getAssetRecord().getVmwareManagedObjectId().trim();

            String splittedData[] = vmwareRuntimeInformation.split("[, ]+");

            String vmwareHostSystem = splittedData[0];
            String vmwarePowerState = splittedData[1];

            VmwareGroup datacenterVertex = addDatacenterGroup(vmwareManagementServer, "Datacenter (" + vmwareManagementServer + ")");

            String primaryInterface = "unknown";

            // get the primary interface ip address

            if (virtualMachine.getPrimaryInterface() != null) {
                primaryInterface = virtualMachine.getPrimaryInterface().getIpHostName();
            }

            // add a vertex for the virtual machine
            VmwareVertex virtualMachineVertex = addVirtualMachineVertex(vmwareManagementServer + "/" + vmwareManagedObjectId, virtualMachine.getLabel(), primaryInterface, virtualMachine.getId(), vmwarePowerState);

            // and set the parent vertex
            virtualMachineVertex.setParent(datacenterVertex);

            // connect the virtual machine to the host system
            connectVertices(vmwareManagementServer + "/" + vmwareManagedObjectId + "->" + vmwareManagementServer + "/" + vmwareHostSystem, vmwareManagementServer + "/" + vmwareManagedObjectId, vmwareManagementServer + "/" + vmwareHostSystem);
        }

        public void generate() {
            m_generated = true;

            // reset container
            resetContainer();

            // get all host systems
            List<OnmsNode> hostSystems = m_nodeDao.findAllByVarCharAssetColumn("vmwareManagedEntityType", "HostSystem");

            if (hostSystems.size() == 0) {
                System.err.println("No host systems with defined VMware assets fields found!");
            } else {
                for (OnmsNode hostSystem : hostSystems) {
                    addHostSystem(hostSystem);
                }
            }

            // get all virtual machines
            List<OnmsNode> virtualMachines = m_nodeDao.findAllByVarCharAssetColumn("vmwareManagedEntityType", "VirtualMachine");

            if (virtualMachines.size() == 0) {
                System.err.println("No virtual machines with defined VMware assets fields found!");
            } else {
                for (OnmsNode virtualMachine : virtualMachines) {
                    addVirtualMachine(virtualMachine);
                }
            }

            for (String id : m_vertexContainer.getItemIds()) {
                debug(id);
            }
        }
    */
    public VmwareVertexContainer getVertexContainer() {
        return m_vertexContainer;
    }

    public BeanContainer<String, VmwareEdge> getEdgeContainer() {
        return m_edgeContainer;
    }

    public Collection<?> getVertexIds() {
        return m_vertexContainer.getItemIds();
    }

    public Collection<?> getEdgeIds() {
        return m_edgeContainer.getItemIds();
    }

    public Item getVertexItem(Object vertexId) {
        return m_vertexContainer.getItem(vertexId);
    }

    public Item getEdgeItem(Object edgeId) {
        return m_edgeContainer.getItem(edgeId);
    }

    public Collection<?> getEndPointIdsForEdge(Object edgeId) {

        VmwareEdge edge = getRequiredEdge(edgeId);

        List<Object> endPoints = new ArrayList<Object>(2);

        endPoints.add(edge.getSource().getId());
        endPoints.add(edge.getTarget().getId());

        return endPoints;
    }

    public Collection<?> getEdgeIdsForVertex(Object vertexId) {

        VmwareVertex vertex = getRequiredVertex(vertexId);

        List<Object> edges = new ArrayList<Object>(vertex.getEdges().size());

        for (VmwareEdge e : vertex.getEdges()) {

            Object edgeId = e.getId();

            edges.add(edgeId);

        }

        return edges;

    }

    private Item addVertex(String id, int x, int y, String icon, String label, String ipAddr, int nodeID) {
        if (m_vertexContainer.containsId(id)) {
            throw new IllegalArgumentException("A vertex or group with id " + id + " already exists!");
        }
        System.err.println("Adding a vertex: " + id);
        VmwareVertex vertex = new VmwareLeafVertex(id, x, y);

        vertex.setIconKey(icon);
        vertex.setIcon(VmwareConstants.ICONS.get(icon));

        vertex.setLabel(label);
        vertex.setIpAddr(ipAddr);
        vertex.setNodeID(nodeID);

        return m_vertexContainer.addBean(vertex);
    }

    private Item addGroup(String groupId, String icon, String label) {
        if (m_vertexContainer.containsId(groupId)) {
            throw new IllegalArgumentException("A vertex or group with id " + groupId + " already exists!");
        }
        System.err.println("Adding a group: " + groupId);
        VmwareVertex vertex = new VmwareGroup(groupId);

        vertex.setIconKey(icon);
        vertex.setIcon(VmwareConstants.ICONS.get(icon));

        vertex.setLabel(label);

        return m_vertexContainer.addBean(vertex);
    }

    private void connectVertices(String id, Object sourceVertextId, Object targetVertextId) {
        VmwareVertex source = getRequiredVertex(sourceVertextId);
        VmwareVertex target = getRequiredVertex(targetVertextId);

        VmwareEdge edge = new VmwareEdge(id, source, target);

        m_edgeContainer.addBean(edge);
    }

    public void removeVertex(Object vertexId) {

        VmwareVertex vertex = getVertex(vertexId, false);
        if (vertex == null) return;

        m_vertexContainer.removeItem(vertexId);

        for (VmwareEdge e : vertex.getEdges()) {
            m_edgeContainer.removeItem(e.getId());
        }
    }

    public VmwareVertex getRequiredVertex(Object vertexId) {
        return getVertex(vertexId, true);
    }

    public VmwareVertex getVertex(Object vertexId, boolean required) {
        BeanItem<VmwareVertex> item = m_vertexContainer.getItem(vertexId);
        if (required && item == null) {
            throw new IllegalArgumentException("required vertex " + vertexId + " not found.");
        }

        return item == null ? null : item.getBean();
    }

    private VmwareEdge getRequiredEdge(Object edgeId) {
        return getEdge(edgeId, true);
    }

    private VmwareEdge getEdge(Object edgeId, boolean required) {
        BeanItem<VmwareEdge> item = m_edgeContainer.getItem(edgeId);
        if (required && item == null) {
            throw new IllegalArgumentException("required edge " + edgeId + " not found.");
        }

        return item == null ? null : item.getBean();
    }


    @XmlRootElement(name = "graph")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class SimpleGraph {

        @XmlElements({
                @XmlElement(name = "vertex", type = VmwareLeafVertex.class),
                @XmlElement(name = "group", type = VmwareGroup.class)
        })
        List<VmwareVertex> m_vertices = new ArrayList<VmwareVertex>();

        @XmlElement(name = "edge")
        List<VmwareEdge> m_edges = new ArrayList<VmwareEdge>();

        @SuppressWarnings("unused")
        public SimpleGraph() {
        }

        public SimpleGraph(List<VmwareVertex> vertices, List<VmwareEdge> edges) {
            m_vertices = vertices;
            m_edges = edges;
        }

    }

    public void save(String filename) {
        List<VmwareVertex> vertices = getBeans(m_vertexContainer);
        List<VmwareEdge> edges = getBeans(m_edgeContainer);

        SimpleGraph graph = new SimpleGraph(vertices, edges);

        JAXB.marshal(graph, new File(filename));
    }

    public void load(String filename) {
        /*
        SimpleGraph graph = JAXB.unmarshal(new File(filename), SimpleGraph.class);
        
        m_vertexContainer.removeAllItems();
        m_vertexContainer.addAll(graph.m_vertices);
        
        m_edgeContainer.removeAllItems();
        m_edgeContainer.addAll(graph.m_edges);
        */
        generateNew();
    }

    private <T> List<T> getBeans(BeanContainer<?, T> container) {
        Collection<?> itemIds = container.getItemIds();
        List<T> beans = new ArrayList<T>(itemIds.size());

        for (Object itemId : itemIds) {
            beans.add(container.getItem(itemId).getBean());
        }

        return beans;
    }

    public String getNextVertexId() {
        return "v" + m_counter++;
    }

    public String getNextEdgeId() {
        return "e" + m_edgeCounter++;
    }

    public String getNextGroupId() {
        return "g" + m_groupCounter++;
    }

    public void resetContainer() {
        getVertexContainer().removeAllItems();
        getEdgeContainer().removeAllItems();

        m_counter = 0;
        m_edgeCounter = 0;
    }

    public Collection<?> getPropertyIds() {
        return Collections.EMPTY_LIST;
    }

    public Property getProperty(String propertyId) {
        return null;
    }


    public Object addVertex(int x, int y, String icon) {
        System.err.println("Adding vertex in VmwareTopologyProvider with icon: " + icon);
        String nextVertexId = getNextVertexId();
//        addVertex(nextVertexId, x, y, icon, "Vertex " + nextVertexId, "127.0.0.1", -1);
        /*
         * Passing a nodeID of -1 will disable the Events/Alarms, Node Info, and
         * Resource Graphs windows in the context menus
         */
        addVertex(nextVertexId, x, y, icon, "Vertex " + nextVertexId, "64.146.64.214", -1);
        return nextVertexId;
    }

    //@Override
    public void setParent(Object vertexId, Object parentId) {
        m_vertexContainer.setParent(vertexId, parentId);
    }

    public Object connectVertices(Object sourceVertextId, Object targetVertextId) {
        String nextEdgeId = getNextEdgeId();
        connectVertices(nextEdgeId, sourceVertextId, targetVertextId);
        return nextEdgeId;
    }

    //@Override
    public Object addGroup(String groupIcon) {
        String nextGroupId = getNextGroupId();
        addGroup(nextGroupId, groupIcon, "Group " + nextGroupId);
        return nextGroupId;
    }

    //@Override
    public boolean containsVertexId(Object vertexId) {
        return m_vertexContainer.containsId(vertexId);
    }

}