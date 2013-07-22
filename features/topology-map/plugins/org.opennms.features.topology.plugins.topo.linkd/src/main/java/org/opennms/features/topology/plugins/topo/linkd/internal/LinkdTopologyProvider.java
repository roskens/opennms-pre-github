/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
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

package org.opennms.features.topology.plugins.topo.linkd.internal;

import java.io.File;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.opennms.features.topology.api.topo.AbstractEdge;
import org.opennms.features.topology.api.topo.AbstractTopologyProvider;
import org.opennms.features.topology.api.topo.AbstractVertex;
import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.GraphProvider;
import org.opennms.features.topology.api.topo.SimpleLeafVertex;
import org.opennms.features.topology.api.topo.Vertex;
import org.opennms.features.topology.api.topo.WrappedEdge;
import org.opennms.features.topology.api.topo.WrappedGraph;
import org.opennms.features.topology.api.topo.WrappedGroup;
import org.opennms.features.topology.api.topo.WrappedLeafVertex;
import org.opennms.features.topology.api.topo.WrappedVertex;
import org.opennms.netmgt.dao.api.DataLinkInterfaceDao;
import org.opennms.netmgt.dao.api.IpInterfaceDao;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.dao.api.SnmpInterfaceDao;
import org.opennms.netmgt.model.DataLinkInterface;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsSnmpInterface;
import org.slf4j.LoggerFactory;

/**
 * The Class LinkdTopologyProvider.
 */
public class LinkdTopologyProvider extends AbstractTopologyProvider implements GraphProvider {

    /**
     * The Class LinkStateMachine.
     */
    private class LinkStateMachine {

        /** The m_up state. */
        LinkState m_upState;

        /** The m_down state. */
        LinkState m_downState;

        /** The m_unknown state. */
        LinkState m_unknownState;

        /** The m_state. */
        LinkState m_state;

        /**
         * Instantiates a new link state machine.
         */
        public LinkStateMachine() {
            m_upState = new LinkUpState(this);
            m_downState = new LinkDownState(this);
            m_unknownState = new LinkUnknownState(this);
            m_state = m_upState;
        }

        /**
         * Sets the parent interfaces.
         *
         * @param sourceInterface
         *            the source interface
         * @param targetInterface
         *            the target interface
         */
        public void setParentInterfaces(OnmsSnmpInterface sourceInterface, OnmsSnmpInterface targetInterface) {
            m_state.setParentInterfaces(sourceInterface, targetInterface);
        }

        /**
         * Gets the link status.
         *
         * @return the link status
         */
        public String getLinkStatus() {
            return m_state.getLinkStatus();
        }

        /**
         * Gets the up state.
         *
         * @return the up state
         */
        public LinkState getUpState() {
            return m_upState;
        }

        /**
         * Gets the down state.
         *
         * @return the down state
         */
        public LinkState getDownState() {
            return m_downState;
        }

        /**
         * Gets the unknown state.
         *
         * @return the unknown state
         */
        public LinkState getUnknownState() {
            return m_unknownState;
        }

        /**
         * Sets the state.
         *
         * @param state
         *            the new state
         */
        public void setState(LinkState state) {
            m_state = state;
        }
    }

    /**
     * The Interface LinkState.
     */
    private interface LinkState {

        /**
         * Sets the parent interfaces.
         *
         * @param sourceInterface
         *            the source interface
         * @param targetInterface
         *            the target interface
         */
        void setParentInterfaces(OnmsSnmpInterface sourceInterface, OnmsSnmpInterface targetInterface);

        /**
         * Gets the link status.
         *
         * @return the link status
         */
        String getLinkStatus();
    }

    /**
     * The Class AbstractLinkState.
     */
    private abstract class AbstractLinkState implements LinkState {

        /** The m_link state machine. */
        private LinkStateMachine m_linkStateMachine;

        /**
         * Instantiates a new abstract link state.
         *
         * @param linkStateMachine
         *            the link state machine
         */
        public AbstractLinkState(LinkStateMachine linkStateMachine) {
            m_linkStateMachine = linkStateMachine;
        }

        /**
         * Gets the link state machine.
         *
         * @return the link state machine
         */
        protected LinkStateMachine getLinkStateMachine() {
            return m_linkStateMachine;
        }
    }

    /**
     * The Class LinkUpState.
     */
    private class LinkUpState extends AbstractLinkState {

        /**
         * Instantiates a new link up state.
         *
         * @param linkStateMachine
         *            the link state machine
         */
        public LinkUpState(LinkStateMachine linkStateMachine) {
            super(linkStateMachine);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.plugins.topo.linkd.internal.LinkdTopologyProvider.LinkState#setParentInterfaces(org.opennms.netmgt.model.OnmsSnmpInterface, org.opennms.netmgt.model.OnmsSnmpInterface)
         */
        @Override
        public void setParentInterfaces(OnmsSnmpInterface sourceInterface, OnmsSnmpInterface targetInterface) {
            if (sourceInterface != null && sourceInterface.getIfOperStatus() != null) {
                if (sourceInterface.getIfOperStatus() != 1) {
                    getLinkStateMachine().setState(getLinkStateMachine().getDownState());
                }
            }

            if (targetInterface != null && targetInterface.getIfOperStatus() != null) {
                if (targetInterface.getIfOperStatus() != 1) {
                    getLinkStateMachine().setState(getLinkStateMachine().getDownState());
                }
            }

            if (sourceInterface == null && targetInterface == null) {
                getLinkStateMachine().setState(getLinkStateMachine().getUnknownState());
            }

        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.plugins.topo.linkd.internal.LinkdTopologyProvider.LinkState#getLinkStatus()
         */
        @Override
        public String getLinkStatus() {
            return OPER_ADMIN_STATUS[1];
        }

    }

    /**
     * The Class LinkDownState.
     */
    private class LinkDownState extends AbstractLinkState {

        /**
         * Instantiates a new link down state.
         *
         * @param linkStateMachine
         *            the link state machine
         */
        public LinkDownState(LinkStateMachine linkStateMachine) {
            super(linkStateMachine);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.plugins.topo.linkd.internal.LinkdTopologyProvider.LinkState#setParentInterfaces(org.opennms.netmgt.model.OnmsSnmpInterface, org.opennms.netmgt.model.OnmsSnmpInterface)
         */
        @Override
        public void setParentInterfaces(OnmsSnmpInterface sourceInterface, OnmsSnmpInterface targetInterface) {
            if (targetInterface != null && targetInterface.getIfOperStatus() != null) {
                if (sourceInterface != null) {
                    if (sourceInterface.getIfOperStatus() == 1 && targetInterface.getIfOperStatus() == 1) {
                        getLinkStateMachine().setState(getLinkStateMachine().getUpState());
                    }
                }
            } else if (sourceInterface == null) {
                getLinkStateMachine().setState(getLinkStateMachine().getUnknownState());
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.plugins.topo.linkd.internal.LinkdTopologyProvider.LinkState#getLinkStatus()
         */
        @Override
        public String getLinkStatus() {
            return OPER_ADMIN_STATUS[2];
        }

    }

    /**
     * The Class LinkUnknownState.
     */
    private class LinkUnknownState extends AbstractLinkState {

        /**
         * Instantiates a new link unknown state.
         *
         * @param linkStateMachine
         *            the link state machine
         */
        public LinkUnknownState(LinkStateMachine linkStateMachine) {
            super(linkStateMachine);
        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.plugins.topo.linkd.internal.LinkdTopologyProvider.LinkState#setParentInterfaces(org.opennms.netmgt.model.OnmsSnmpInterface, org.opennms.netmgt.model.OnmsSnmpInterface)
         */
        @Override
        public void setParentInterfaces(OnmsSnmpInterface sourceInterface, OnmsSnmpInterface targetInterface) {
            if (targetInterface != null && targetInterface.getIfOperStatus() != null) {
                if (sourceInterface != null) {
                    if (sourceInterface.getIfOperStatus() == 1 && targetInterface.getIfOperStatus() == 1) {
                        getLinkStateMachine().setState(getLinkStateMachine().getUpState());
                    } else {
                        getLinkStateMachine().setState(getLinkStateMachine().getDownState());
                    }
                }
            }

        }

        /* (non-Javadoc)
         * @see org.opennms.features.topology.plugins.topo.linkd.internal.LinkdTopologyProvider.LinkState#getLinkStatus()
         */
        @Override
        public String getLinkStatus() {
            return OPER_ADMIN_STATUS[4];
        }

    }

    /** The Constant TOPOLOGY_NAMESPACE_LINKD. */
    public static final String TOPOLOGY_NAMESPACE_LINKD = "nodes";

    /** The Constant GROUP_ICON_KEY. */
    public static final String GROUP_ICON_KEY = "linkd:group";

    /** The Constant SERVER_ICON_KEY. */
    public static final String SERVER_ICON_KEY = "linkd:system";

    /** The Constant HTML_TOOLTIP_TAG_OPEN. */
    private static final String HTML_TOOLTIP_TAG_OPEN = "<p>";

    /** The Constant HTML_TOOLTIP_TAG_END. */
    private static final String HTML_TOOLTIP_TAG_END = "</p>";

    /**
     * Always print at least one digit after the decimal point,
     * and at most three digits after the decimal point.
     */
    private static final DecimalFormat s_oneDigitAfterDecimal = new DecimalFormat("0.0##");

    /**
     * Print no digits after the decimal point (heh, nor a decimal point).
     */
    private static final DecimalFormat s_noDigitsAfterDecimal = new DecimalFormat("0");

    /**
     * Do not use directly. Call {@link #getNodeStatusMap
     * getInterfaceStatusMap} instead.
     */
    private static final Map<Character, String> m_nodeStatusMap;

    static {
        m_nodeStatusMap = new HashMap<Character, String>();
        m_nodeStatusMap.put('A', "Active");
        m_nodeStatusMap.put(' ', "Unknown");
        m_nodeStatusMap.put('D', "Deleted");
    }

    /** The Constant OPER_ADMIN_STATUS. */
    static final String[] OPER_ADMIN_STATUS = new String[] { "&nbsp;", // 0 (not
                                                                       // supported)
            "Up", // 1
            "Down", // 2
            "Testing", // 3
            "Unknown", // 4
            "Dormant", // 5
            "NotPresent", // 6
            "LowerLayerDown" // 7
    };

    /** The add node without link. */
    private boolean addNodeWithoutLink = false;

    /** The m_data link interface dao. */
    private DataLinkInterfaceDao m_dataLinkInterfaceDao;

    /** The m_node dao. */
    private NodeDao m_nodeDao;

    /** The m_snmp interface dao. */
    private SnmpInterfaceDao m_snmpInterfaceDao;

    /** The m_ip interface dao. */
    private IpInterfaceDao m_ipInterfaceDao;

    /** The m_configuration file. */
    private String m_configurationFile;

    /**
     * Gets the configuration file.
     *
     * @return the configuration file
     */
    public String getConfigurationFile() {
        return m_configurationFile;
    }

    /**
     * Sets the configuration file.
     *
     * @param configurationFile
     *            the new configuration file
     */
    public void setConfigurationFile(String configurationFile) {
        m_configurationFile = configurationFile;
    }

    /**
     * Gets the snmp interface dao.
     *
     * @return the snmp interface dao
     */
    public SnmpInterfaceDao getSnmpInterfaceDao() {
        return m_snmpInterfaceDao;
    }

    /**
     * Sets the snmp interface dao.
     *
     * @param snmpInterfaceDao
     *            the new snmp interface dao
     */
    public void setSnmpInterfaceDao(SnmpInterfaceDao snmpInterfaceDao) {
        m_snmpInterfaceDao = snmpInterfaceDao;
    }

    /**
     * Gets the node dao.
     *
     * @return the node dao
     */
    public NodeDao getNodeDao() {
        return m_nodeDao;
    }

    /**
     * Sets the node dao.
     *
     * @param nodeDao
     *            the new node dao
     */
    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    /**
     * Checks if is adds the node without link.
     *
     * @return true, if is adds the node without link
     */
    public boolean isAddNodeWithoutLink() {
        return addNodeWithoutLink;
    }

    /**
     * Sets the adds the node without link.
     *
     * @param addNodeWithoutLink
     *            the new adds the node without link
     */
    public void setAddNodeWithoutLink(boolean addNodeWithoutLink) {
        this.addNodeWithoutLink = addNodeWithoutLink;
    }

    /**
     * Gets the data link interface dao.
     *
     * @return the data link interface dao
     */
    public DataLinkInterfaceDao getDataLinkInterfaceDao() {
        return m_dataLinkInterfaceDao;
    }

    /**
     * Sets the data link interface dao.
     *
     * @param dataLinkInterfaceDao
     *            the new data link interface dao
     */
    public void setDataLinkInterfaceDao(DataLinkInterfaceDao dataLinkInterfaceDao) {
        m_dataLinkInterfaceDao = dataLinkInterfaceDao;
    }

    /**
     * Used as an init-method in the OSGi blueprint.
     *
     * @throws MalformedURLException
     *             the malformed url exception
     * @throws JAXBException
     *             the jAXB exception
     */
    public void onInit() throws MalformedURLException, JAXBException {
        log("init: loading topology v1.3");
        load(null);
    }

    /**
     * Instantiates a new linkd topology provider.
     */
    public LinkdTopologyProvider() {
        super(TOPOLOGY_NAMESPACE_LINKD);
    }

    /**
     * Gets the graph from file.
     *
     * @param file
     *            the file
     * @return the graph from file
     * @throws JAXBException
     *             the jAXB exception
     * @throws MalformedURLException
     *             the malformed url exception
     */
    private static WrappedGraph getGraphFromFile(File file) throws JAXBException, MalformedURLException {
        JAXBContext jc = JAXBContext.newInstance(WrappedGraph.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (WrappedGraph) u.unmarshal(file.toURI().toURL());
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#refresh()
     */
    @Override
    public void refresh() {
        try {
            load(null);
        } catch (MalformedURLException e) {
            LoggerFactory.getLogger(LinkdTopologyProvider.class).error(e.getMessage(), e);
        } catch (JAXBException e) {
            LoggerFactory.getLogger(LinkdTopologyProvider.class).error(e.getMessage(), e);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#load(java.lang.String)
     */
    @Override
    public void load(String filename) throws MalformedURLException, JAXBException {
        if (filename != null) {
            LoggerFactory.getLogger(LinkdTopologyProvider.class).warn("Filename that was specified for linkd topology will be ignored: "
                                                                              + filename
                                                                              + ", using "
                                                                              + m_configurationFile + " instead");
        }
        log("loadtopology: resetContainer ");
        resetContainer();

        for (DataLinkInterface link : m_dataLinkInterfaceDao.findAll()) {
            log("loadtopology: parsing link: " + link.getDataLinkInterfaceId());

            OnmsNode node = m_nodeDao.get(link.getNode().getId());
            log("loadtopology: found source node: " + node.getLabel());
            String sourceId = node.getNodeId();
            Vertex source = getVertex(getVertexNamespace(), sourceId);
            if (source == null) {
                log("loadtopology: adding source node as vertex: " + node.getLabel());
                source = getVertex(node);
                addVertices(source);
            }

            OnmsNode parentNode = m_nodeDao.get(link.getNodeParentId());
            log("loadtopology: found target node: " + parentNode.getLabel());
            String targetId = parentNode.getNodeId();
            Vertex target = getVertex(getVertexNamespace(), targetId);
            if (target == null) {
                log("loadtopology: adding target as vertex: " + parentNode.getLabel());
                target = getVertex(parentNode);
                addVertices(target);
            }

            // Create a new edge that connects the vertices
            // TODO: Make sure that all properties are set on this object
            AbstractEdge edge = connectVertices(link.getDataLinkInterfaceId(), source, target);
            edge.setTooltipText(getEdgeTooltipText(link, source, target));
        }

        log("loadtopology: adding nodes without links: " + isAddNodeWithoutLink());
        if (isAddNodeWithoutLink()) {
            for (OnmsNode onmsnode : m_nodeDao.findAll()) {
                String nodeId = onmsnode.getNodeId();
                if (getVertex(getVertexNamespace(), nodeId) == null) {
                    log("loadtopology: adding link-less node: " + onmsnode.getLabel());
                    addVertices(getVertex(onmsnode));
                }
            }
        }

        File configFile = new File(m_configurationFile);
        if (configFile.exists() && configFile.canRead()) {
            log("loadtopology: loading topology from configuration file: " + m_configurationFile);
            WrappedGraph graph = getGraphFromFile(configFile);

            // Add all groups to the topology
            for (WrappedVertex eachVertexInFile : graph.m_vertices) {
                if (eachVertexInFile.group) {
                    log("loadtopology: adding group to topology: " + eachVertexInFile.id);
                    if (eachVertexInFile.namespace == null) {
                        eachVertexInFile.namespace = getVertexNamespace();
                        LoggerFactory.getLogger(this.getClass()).warn("Setting namespace on vertex to default: {}",
                                                                      eachVertexInFile);
                    }
                    if (eachVertexInFile.id == null) {
                        LoggerFactory.getLogger(this.getClass()).warn("Invalid vertex unmarshalled from {}: {}",
                                                                      m_configurationFile, eachVertexInFile);
                    }
                    AbstractVertex newGroupVertex = addGroup(eachVertexInFile.id, eachVertexInFile.iconKey,
                                                             eachVertexInFile.label);
                    newGroupVertex.setIpAddress(eachVertexInFile.ipAddr);
                    newGroupVertex.setLocked(eachVertexInFile.locked);
                    if (eachVertexInFile.nodeID != null) {
                        newGroupVertex.setNodeID(eachVertexInFile.nodeID);
                    }
                    newGroupVertex.setParent(eachVertexInFile.parent);
                    newGroupVertex.setSelected(eachVertexInFile.selected);
                    newGroupVertex.setStyleName(eachVertexInFile.styleName);
                    newGroupVertex.setTooltipText(eachVertexInFile.tooltipText);
                    if (eachVertexInFile.x != null) {
                        newGroupVertex.setX(eachVertexInFile.x);
                    }
                    if (eachVertexInFile.y != null) {
                        newGroupVertex.setY(eachVertexInFile.y);
                    }
                }
            }
            for (Vertex vertex : getVertices()) {
                if (vertex.getParent() != null) {
                    log("loadtopology: setting parent of " + vertex + " to " + vertex.getParent());
                    setParent(vertex, vertex.getParent());
                }
            }
            // Add all children to the specific group
            // Attention: We ignore all other attributes, they do not need to be
            // merged!
            for (WrappedVertex eachVertexInFile : graph.m_vertices) {
                if (!eachVertexInFile.group && eachVertexInFile.parent != null) {
                    final Vertex child = getVertex(eachVertexInFile);
                    final Vertex parent = getVertex(eachVertexInFile.parent);
                    if (child == null || parent == null) {
                        continue;
                    }
                    log("loadtopology: setting parent of " + child + " to " + parent);
                    setParent(child, parent);
                }
            }
        } else {
            log("loadtopology: could not load topology configFile:" + m_configurationFile);
        }
        log("Found " + getGroups().size() + " groups");
        log("Found " + getVerticesWithoutGroups().size() + " vertices");
        log("Found " + getEdges().size() + " edges");
    }

    /**
     * Gets the vertex.
     *
     * @param onmsnode
     *            the onmsnode
     * @return the vertex
     */
    private AbstractVertex getVertex(OnmsNode onmsnode) {
        OnmsIpInterface ip = getAddress(onmsnode);
        AbstractVertex vertex = new SimpleLeafVertex(TOPOLOGY_NAMESPACE_LINKD, onmsnode.getNodeId(), 0, 0);
        vertex.setIconKey(getIconName(onmsnode));
        vertex.setLabel(onmsnode.getLabel());
        vertex.setIpAddress(ip == null ? null : ip.getIpAddress().getHostAddress());
        vertex.setNodeID(Integer.parseInt(onmsnode.getNodeId()));
        vertex.setTooltipText(getNodeTooltipText(onmsnode, vertex, ip));
        return vertex;
    }

    /**
     * Gets the address.
     *
     * @param node
     *            the node
     * @return the address
     */
    private OnmsIpInterface getAddress(OnmsNode node) {
        // OnmsIpInterface ip = node.getPrimaryInterface();
        OnmsIpInterface ip = m_ipInterfaceDao.findPrimaryInterfaceByNodeId(node.getId());
        if (ip == null) {
            // for (OnmsIpInterface iterip: node.getIpInterfaces()) {
            for (OnmsIpInterface iterip : m_ipInterfaceDao.findByNodeId(node.getId())) {
                ip = iterip;
                break;
            }
        }
        return ip;
    }

    /**
     * Gets the edge tooltip text.
     *
     * @param link
     *            the link
     * @param source
     *            the source
     * @param target
     *            the target
     * @return the edge tooltip text
     */
    private String getEdgeTooltipText(DataLinkInterface link, Vertex source, Vertex target) {
        StringBuffer tooltipText = new StringBuffer();

        OnmsSnmpInterface sourceInterface = m_snmpInterfaceDao.findByNodeIdAndIfIndex(Integer.parseInt(source.getId()),
                                                                                      link.getIfIndex());
        OnmsSnmpInterface targetInterface = m_snmpInterfaceDao.findByNodeIdAndIfIndex(Integer.parseInt(target.getId()),
                                                                                      link.getParentIfIndex());

        tooltipText.append(HTML_TOOLTIP_TAG_OPEN);
        if (sourceInterface != null && targetInterface != null && sourceInterface.getNetMask() != null
                && !sourceInterface.getNetMask().isLoopbackAddress() && targetInterface.getNetMask() != null
                && !targetInterface.getNetMask().isLoopbackAddress()) {
            tooltipText.append("Type of Link: Layer3/Layer2");
        } else {
            tooltipText.append("Type of Link: Layer2");
        }
        tooltipText.append(HTML_TOOLTIP_TAG_END);

        tooltipText.append(HTML_TOOLTIP_TAG_OPEN);
        tooltipText.append("Name: &lt;endpoint1 " + source.getLabel());
        if (sourceInterface != null) {
            tooltipText.append(":" + sourceInterface.getIfName());
        }
        tooltipText.append(" ---- endpoint2 " + target.getLabel());
        if (targetInterface != null) {
            tooltipText.append(":" + targetInterface.getIfName());
        }
        tooltipText.append("&gt;");
        tooltipText.append(HTML_TOOLTIP_TAG_END);

        LinkStateMachine stateMachine = new LinkStateMachine();
        stateMachine.setParentInterfaces(sourceInterface, targetInterface);
        tooltipText.append(HTML_TOOLTIP_TAG_OPEN);
        tooltipText.append("Link status: " + stateMachine.getLinkStatus());
        tooltipText.append(HTML_TOOLTIP_TAG_END);

        if (targetInterface != null) {
            if (targetInterface.getIfSpeed() != null) {
                tooltipText.append(HTML_TOOLTIP_TAG_OPEN);
                tooltipText.append("Bandwidth: " + getHumanReadableIfSpeed(targetInterface.getIfSpeed()));
                tooltipText.append(HTML_TOOLTIP_TAG_END);
            }
        } else if (sourceInterface != null) {
            if (sourceInterface.getIfSpeed() != null) {
                tooltipText.append(HTML_TOOLTIP_TAG_OPEN);
                tooltipText.append("Bandwidth: " + getHumanReadableIfSpeed(sourceInterface.getIfSpeed()));
                tooltipText.append(HTML_TOOLTIP_TAG_END);
            }
        }

        tooltipText.append(HTML_TOOLTIP_TAG_OPEN);
        tooltipText.append("End Point 1: " + source.getLabel() + ", " + source.getIpAddress());
        tooltipText.append(HTML_TOOLTIP_TAG_END);

        tooltipText.append(HTML_TOOLTIP_TAG_OPEN);
        tooltipText.append("End Point 2: " + target.getLabel() + ", " + target.getIpAddress());
        tooltipText.append(HTML_TOOLTIP_TAG_END);

        log("getEdgeTooltipText\n" + tooltipText);
        return tooltipText.toString();
    }

    /**
     * Gets the node tooltip text.
     *
     * @param node
     *            the node
     * @param vertex
     *            the vertex
     * @param ip
     *            the ip
     * @return the node tooltip text
     */
    private static String getNodeTooltipText(OnmsNode node, AbstractVertex vertex, OnmsIpInterface ip) {
        StringBuffer tooltipText = new StringBuffer();

        /*
         * if (node.getSysDescription() != null &&
         * node.getSysDescription().length() >0) {
         * tooltipText.append(HTML_TOOLTIP_TAG_OPEN);
         * tooltipText.append("Description: " + node.getSysDescription());
         * tooltipText.append(HTML_TOOLTIP_TAG_END);
         * }
         */

        tooltipText.append(HTML_TOOLTIP_TAG_OPEN);
        tooltipText.append("Management IP and Name: " + vertex.getIpAddress() + " (" + vertex.getLabel() + ")");
        tooltipText.append(HTML_TOOLTIP_TAG_END);

        if (node.getSysLocation() != null && node.getSysLocation().length() > 0) {
            tooltipText.append(HTML_TOOLTIP_TAG_OPEN);
            tooltipText.append("Location: " + node.getSysLocation());
            tooltipText.append(HTML_TOOLTIP_TAG_END);
        }

        tooltipText.append(HTML_TOOLTIP_TAG_OPEN);
        tooltipText.append("Status: " + getNodeStatusString(node.getType().charAt(0)));
        if (ip != null && ip.isManaged()) {
            tooltipText.append(" / Managed");
        } else {
            tooltipText.append(" / Unmanaged");
        }
        tooltipText.append(HTML_TOOLTIP_TAG_END);

        log("getNodeTooltipText:\n" + tooltipText);

        return tooltipText.toString();

    }

    /**
     * Gets the icon name.
     *
     * @param node
     *            the node
     * @return the icon name
     */
    public static String getIconName(OnmsNode node) {
        return node.getSysObjectId() == null ? "linkd:system" : "linkd:system:snmp:" + node.getSysObjectId();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.topo.GraphProvider#save()
     */
    @Override
    public void save() {
        List<WrappedVertex> vertices = new ArrayList<WrappedVertex>();
        for (Vertex vertex : getVertices()) {
            if (vertex.isGroup()) {
                vertices.add(new WrappedGroup(vertex));
            } else {
                vertices.add(new WrappedLeafVertex(vertex));
            }
        }
        List<WrappedEdge> edges = new ArrayList<WrappedEdge>();
        for (Edge edge : getEdges()) {
            WrappedEdge newEdge = new WrappedEdge(
                                                  edge,
                                                  new WrappedLeafVertex(
                                                                        m_vertexProvider.getVertex(edge.getSource().getVertex())),
                                                  new WrappedLeafVertex(
                                                                        m_vertexProvider.getVertex(edge.getTarget().getVertex())));
            edges.add(newEdge);
        }

        WrappedGraph graph = new WrappedGraph(getEdgeNamespace(), vertices, edges);

        JAXB.marshal(graph, new File(m_configurationFile));
    }

    /**
     * Gets the if status string.
     *
     * @param ifStatusNum
     *            the if status num
     * @return the if status string
     */
    private static String getIfStatusString(int ifStatusNum) {
        if (ifStatusNum < OPER_ADMIN_STATUS.length) {
            return OPER_ADMIN_STATUS[ifStatusNum];
        } else {
            return "Unknown (" + ifStatusNum + ")";
        }
    }

    /**
     * Return the human-readable name for a interface status character, may be
     * null.
     *
     * @param c
     *            a char.
     * @return a {@link java.lang.String} object.
     */
    private static String getNodeStatusString(char c) {
        return m_nodeStatusMap.get(c);
    }

    /**
     * Method used to convert an integer bits-per-second value to a more
     * readable vale using commonly recognized abbreviation for network
     * interface speeds. Feel free to expand it as necessary to accomodate
     * different values.
     *
     * @param ifSpeed
     *            The bits-per-second value to be converted into a string
     *            description
     * @return A string representation of the speed (&quot;100 Mbps&quot; for
     *         example)
     */
    private static String getHumanReadableIfSpeed(long ifSpeed) {
        DecimalFormat formatter;
        double displaySpeed;
        String units;

        if (ifSpeed >= 1000000000L) {
            if ((ifSpeed % 1000000000L) == 0) {
                formatter = s_noDigitsAfterDecimal;
            } else {
                formatter = s_oneDigitAfterDecimal;
            }
            displaySpeed = ((double) ifSpeed) / 1000000000;
            units = "Gbps";
        } else if (ifSpeed >= 1000000L) {
            if ((ifSpeed % 1000000L) == 0) {
                formatter = s_noDigitsAfterDecimal;
            } else {
                formatter = s_oneDigitAfterDecimal;
            }
            displaySpeed = ((double) ifSpeed) / 1000000;
            units = "Mbps";
        } else if (ifSpeed >= 1000L) {
            if ((ifSpeed % 1000L) == 0) {
                formatter = s_noDigitsAfterDecimal;
            } else {
                formatter = s_oneDigitAfterDecimal;
            }
            displaySpeed = ((double) ifSpeed) / 1000;
            units = "kbps";
        } else {
            formatter = s_noDigitsAfterDecimal;
            displaySpeed = (double) ifSpeed;
            units = "bps";
        }

        return formatter.format(displaySpeed) + " " + units;
    }

    /**
     * Log.
     *
     * @param string
     *            the string
     */
    private static void log(final String string) {
        LoggerFactory.getLogger(LinkdTopologyProvider.class).debug(string);
    }

    /**
     * Gets the ip interface dao.
     *
     * @return the ip interface dao
     */
    public IpInterfaceDao getIpInterfaceDao() {
        return m_ipInterfaceDao;
    }

    /**
     * Sets the ip interface dao.
     *
     * @param ipInterfaceDao
     *            the new ip interface dao
     */
    public void setIpInterfaceDao(IpInterfaceDao ipInterfaceDao) {
        m_ipInterfaceDao = ipInterfaceDao;
    }
}
