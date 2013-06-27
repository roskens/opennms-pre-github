/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.NoResultException;
import javax.servlet.ServletContext;

import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.core.criteria.restrictions.Restriction;
import org.opennms.core.criteria.restrictions.RestrictionVisitor;
import org.opennms.core.criteria.restrictions.Restrictions;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.InetAddressComparator;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.dao.CategoryDao;

import org.opennms.netmgt.dao.DataLinkInterfaceDao;
import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.dao.IpRouteInterfaceDao;
import org.opennms.netmgt.dao.MonitoredServiceDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.ServiceTypeDao;
import org.opennms.netmgt.dao.SnmpInterfaceDao;
import org.opennms.netmgt.dao.StpInterfaceDao;
import org.opennms.netmgt.dao.StpNodeDao;
import org.opennms.netmgt.dao.VlanDao;

import org.opennms.netmgt.model.DataLinkInterface;
import org.opennms.netmgt.model.OnmsArpInterface;
import org.opennms.netmgt.model.OnmsArpInterface.StatusType;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsIpRouteInterface;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsRestrictions;
import org.opennms.netmgt.model.OnmsServiceType;
import org.opennms.netmgt.model.OnmsSnmpInterface;
import org.opennms.netmgt.model.OnmsStpInterface;
import org.opennms.netmgt.model.OnmsStpNode;
import org.opennms.netmgt.model.OnmsVlan;
import org.opennms.netmgt.model.PrimaryType;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The source for all network element business objects (nodes, interfaces,
 * services). Encapsulates all lookup functionality for the network element
 * business objects in one place.
 *
 * @author <A HREF="larry@opennms.org">Larry Karnowski </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 */
@Transactional(readOnly=true)
public class NetworkElementFactory implements InitializingBean, NetworkElementFactoryInterface {
    
    @Autowired
    private NodeDao m_nodeDao;
    
    @Autowired
    private IpInterfaceDao m_ipInterfaceDao;
    
    @Autowired
    private SnmpInterfaceDao m_snmpInterfaceDao;
    
    @Autowired
    private DataLinkInterfaceDao m_dataLinkInterfaceDao;

    @Autowired
    private IpRouteInterfaceDao m_ipRouteInterfaceDao;

    @Autowired
    private StpNodeDao m_stpNodeDao;
    
    @Autowired
    private StpInterfaceDao m_stpInterfaceDao;
    
    @Autowired
    private VlanDao m_vlanDao;
    
    @Autowired
    private MonitoredServiceDao m_monSvcDao;
    
    @Autowired
    private ServiceTypeDao m_serviceTypeDao;
    
    @Autowired
    private CategoryDao m_categoryDao;
    
	@Autowired
	private PlatformTransactionManager m_transactionManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    public static NetworkElementFactoryInterface getInstance(ServletContext servletContext) {
        return getInstance(WebApplicationContextUtils.getWebApplicationContext(servletContext));    
    }

    public static NetworkElementFactoryInterface getInstance(ApplicationContext appContext) {
    	return appContext.getBean(NetworkElementFactoryInterface.class);
    }

    private static final Comparator<Interface> INTERFACE_COMPARATOR = new InterfaceComparator();

    public static class InterfaceComparator implements Comparator<Interface> {
        @Override
        public int compare(Interface o1, Interface o2) {

            // Sort by IP first if the IPs are non-0.0.0.0
            if (!"0.0.0.0".equals(o1.getIpAddress()) && !"0.0.0.0".equals(o2.getIpAddress())) {
                return new InetAddressComparator().compare(InetAddressUtils.addr(o1.getIpAddress()), InetAddressUtils.addr(o2.getIpAddress()));
            } else {
                // Sort IPs that are non-0.0.0.0 so they are first
                if (!"0.0.0.0".equals(o1.getIpAddress())) {
                    return -1;
                } else if (!"0.0.0.0".equals(o2.getIpAddress())) {
                    return 1;
                }
            }
            return 0;
        }
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getNodeLabel(int)
	 */
    @Override
    public String getNodeLabel(int nodeId) {
        OnmsNode onmsNode = getNode(nodeId);
        if(onmsNode != null)
            return onmsNode.getLabel();
        return null;
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getIpPrimaryAddress(int)
	 */
    @Override
    public String getIpPrimaryAddress(int nodeId) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsIpInterface.class);
        criteriaBuilder.join("node","node");
        criteriaBuilder.and(
                Restrictions.eq("node.id", nodeId),
                Restrictions.ne("isSnmpPrimary", PrimaryType.PRIMARY));
        
        List<OnmsIpInterface> ifaces = m_ipInterfaceDao.findMatching(criteriaBuilder.toCriteria());
        
        if(ifaces.size() > 0) {
            OnmsIpInterface iface = ifaces.get(0);
            return InetAddressUtils.str(iface.getIpAddress());
        }else{
            return null;
        }
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getNode(int)
	 */
    @Override
    public OnmsNode getNode(int nodeId) {
        try {
            return m_nodeDao.get(nodeId);
        } catch (NoResultException e) {
            return null;
        }

    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getAllNodes()
	 */
    @Override
    public List<OnmsNode> getAllNodes() {
        OnmsCriteria criteria =  new OnmsCriteria(OnmsNode.class);

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsNode.class);
        criteriaBuilder.or(
                Restrictions.isNull("type"),
                Restrictions.ne("type", "D"));
        criteriaBuilder.orderBy("label").asc();
        
        return m_nodeDao.findMatching(criteria);
    }
    
    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getNodesWithIpLike(java.lang.String)
	 */
    @Override
    public List<OnmsNode> getNodesWithIpLike(String iplike) {
        if(iplike == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }
        OnmsCriteria nodeCrit = new OnmsCriteria(OnmsNode.class, "node");
        nodeCrit.createCriteria("ipInterfaces", "iface")
            .add(OnmsRestrictions.ipLike(iplike))
            .add(Restrictions.ne("isManaged", "D"));
        nodeCrit.add(Restrictions.ne("type", "D"));
        nodeCrit.addOrder(Order.asc("label"));
        nodeCrit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        
        return m_nodeDao.findMatching(nodeCrit);
    }

    @Override
    public Integer getIfIndex(int ipinterfaceid) {
        return getIfIndex(m_ipInterfaceDao.get(ipinterfaceid));
    }
    
    @Override
    public Integer getIfIndex(int nodeID, String ipaddr) {
        return getIfIndex(m_ipInterfaceDao.get(m_nodeDao.get(nodeID), ipaddr));
    }

    private Integer getIfIndex(OnmsIpInterface ipinterface) {
        if (ipinterface != null && ipinterface.getIfIndex() != null)
            return ipinterface.getIfIndex();
        return -1;        
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getInterface(int)
	 */
    @Override
    public Interface getInterface(int ipInterfaceId) {
        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsIpInterface.class);
        criteriaBuilder.eq("id", ipInterfaceId);
        criteriaBuilder.fetch("snmpInterface");
        
        List<OnmsIpInterface> ifaces = m_ipInterfaceDao.findMatching(criteriaBuilder.toCriteria());
        
        if(ifaces.size() > 0) {
            return new Interface(ifaces.get(0));
        }
        
        return null;
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getInterface(int, java.lang.String)
	 */
    @Override
    public Interface getInterface(int nodeId, String ipAddress) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsIpInterface.class);
        criteriaBuilder.join("node", "node");
        criteriaBuilder.eq("node.id", nodeId);
        criteriaBuilder.eq("ipAddress", InetAddressUtils.addr(ipAddress));
        criteriaBuilder.fetch("snmpInterface");

        List<OnmsIpInterface> ifaces = m_ipInterfaceDao.findMatching(criteriaBuilder.toCriteria());
        return ifaces.size() > 0 ? new Interface(ifaces.get(0)) : null;
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getSnmpInterface(int, int)
	 */
    @Override
    public Interface getSnmpInterface(int nodeId, int ifIndex) {
        return new Interface(m_snmpInterfaceDao.findByNodeIdAndIfIndex(nodeId, ifIndex));
    }
    

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getInterfacesWithIpAddress(java.lang.String)
	 */
    @Override
    public Interface[] getInterfacesWithIpAddress(String ipAddress) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsIpInterface.class);
        criteriaBuilder.eq("ipAddress", InetAddressUtils.addr(ipAddress));
        criteriaBuilder.fetch("snmpInterface");

        return getInterfaceArray(m_ipInterfaceDao.findMatching(criteriaBuilder.toCriteria()));
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getActiveInterfacesOnNode(int)
	 */
    @Override
    public Interface[] getActiveInterfacesOnNode(int nodeId) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsIpInterface.class);
        criteriaBuilder.alias("node", "node");
        criteriaBuilder.eq("node.id", nodeId);
        criteriaBuilder.ne("isManaged", "D");

        return getInterfaceArray(m_ipInterfaceDao.findMatching(criteriaBuilder.toCriteria()));
    }

    /*
     * Returns all interfaces, but only includes SNMP data if includeSNMP is true
     * This may be useful for pages that don't need SNMP data and don't want to execute
     * a sub-query per interface!
     *
     * @param includeSNMP a boolean.
     * @return an array of {@link org.opennms.web.element.Interface} objects.
     */
    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getAllInterfaces(boolean)
	 */
    @Override
    public Interface[] getAllInterfaces(boolean includeSnmp) {
        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsIpInterface.class);
        criteriaBuilder.alias("snmpInterface", "snmpInterface");

        if(!includeSnmp) {
            return getInterfaceArray(m_ipInterfaceDao.findMatching(criteriaBuilder.toCriteria()));
        }else {
            return getInterfaceArrayWithSnmpData(m_ipInterfaceDao.findMatching(criteriaBuilder.toCriteria()));
        }
    }


    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getAllManagedIpInterfaces(boolean)
	 */
    @Override
    public Interface[] getAllManagedIpInterfaces(boolean includeSNMP) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsMonitoredService.class);
        criteriaBuilder.alias("snmpInterface", "snmpIface");
        criteriaBuilder.alias("node", "node");
        criteriaBuilder.eq("isManaged", "D");
        criteriaBuilder.eq("ipAddress", InetAddressUtils.addr("0.0.0.0"));
        criteriaBuilder.isNotNull("ipAddress");
        criteriaBuilder.orderBy("ipHostName").orderBy("node.id").orderBy("ipAddress").asc();

        if(!includeSNMP) {
            return getInterfaceArray(m_ipInterfaceDao.findMatching(criteriaBuilder.toCriteria()));
        }else {
            return getInterfaceArrayWithSnmpData(m_ipInterfaceDao.findMatching(criteriaBuilder.toCriteria()));
        }
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getService(int, java.lang.String, int)
	 */
    @Override
    public Service getService(int nodeId, String ipAddress, int serviceId) {
        if (ipAddress == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsMonitoredService.class);
        criteriaBuilder.alias("ipInterface", "ipInterface");
        criteriaBuilder.alias("ipInterface.snmpInterface", "snmpIface");
        criteriaBuilder.alias("ipInterface.node", "node");
        criteriaBuilder.alias("serviceType", "serviceType");
        criteriaBuilder.eq("node.id", nodeId);
        criteriaBuilder.eq("ipInterface.ipAddress", InetAddressUtils.addr(ipAddress));
        criteriaBuilder.eq("serviceType.id", serviceId);

        List<OnmsMonitoredService> monSvcs = m_monSvcDao.findMatching(criteriaBuilder.toCriteria());
        if(monSvcs.size() > 0) {
            return new Service(monSvcs.get(0));
        }else {
            return null;
        }
        
    }
    
    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getService(int)
	 */
    @Override
    public Service getService(int ifServiceId) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsMonitoredService.class);
        criteriaBuilder.alias("ipInterface", "ipInterface");
        criteriaBuilder.alias("ipInterface.snmpInterface", "snmpIface");
        criteriaBuilder.alias("ipInterface.node", "node");
        criteriaBuilder.alias("serviceType", "serviceType");
        criteriaBuilder.eq("id", ifServiceId);
        criteriaBuilder.orderBy("status").asc();

        List<OnmsMonitoredService> monSvcs = m_monSvcDao.findMatching(criteriaBuilder.toCriteria());
        
        if(monSvcs.size() > 0) {
            return new Service(monSvcs.get(0));
        }else {
            return null;
        }
        
        
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getServicesOnInterface(int, java.lang.String)
	 */
    @Override
    public Service[] getServicesOnInterface(int nodeId, String ipAddress) {
        return getServicesOnInterface(nodeId, ipAddress, false);
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getServicesOnInterface(int, java.lang.String, boolean)
	 */
    @Override
    public Service[] getServicesOnInterface(int nodeId, String ipAddress, boolean includeDeletions) {
        if (ipAddress == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsMonitoredService.class);
        criteriaBuilder.join("ipInterface","ipInterface");
        criteriaBuilder.join("ipInterface.node","node");
        criteriaBuilder.and(
                Restrictions.eq("ipInterface.node", nodeId),
                Restrictions.ne("ipInterface.ipAddress", InetAddressUtils.addr(ipAddress)));
        
        if(!includeDeletions) {
            criteriaBuilder.ne("status", "D");
        }
        
        return getServiceArray(m_monSvcDao.findMatching(criteriaBuilder.toCriteria()));
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getServicesOnNode(int)
	 */
    @Override
    public Service[] getServicesOnNode(int nodeId) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsMonitoredService.class);
        criteriaBuilder.alias("ipInterface", "ipInterface");
        criteriaBuilder.alias("ipInterface.snmpInterface", "snmpIface");
        criteriaBuilder.alias("ipInterface.node", "node");
        criteriaBuilder.alias("serviceType", "serviceType");
        criteriaBuilder.eq("node.id", nodeId);

        return getServiceArray(m_monSvcDao.findMatching(criteriaBuilder.toCriteria()));
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getServicesOnNode(int, int)
	 */
    @Override
    public Service[] getServicesOnNode(int nodeId, int serviceId) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsMonitoredService.class);
        criteriaBuilder.alias("ipInterface", "ipInterface");
        criteriaBuilder.alias("ipInterface.snmpInterface", "snmpIface");
        criteriaBuilder.alias("ipInterface.node", "node");
        criteriaBuilder.alias("serviceType", "serviceType");
        criteriaBuilder.eq("node.id", nodeId);
        criteriaBuilder.eq("serviceType.id", serviceId);

        return getServiceArray(m_monSvcDao.findMatching(criteriaBuilder.toCriteria()));
    }

    private static Service[] getServiceArray(List<OnmsMonitoredService> monSvcs) {
        List<Service> svcs = new LinkedList<Service>();
        for(OnmsMonitoredService monSvc : monSvcs) {
            Service service = new Service(monSvc);
            
            svcs.add(service);
        }
        
        
        return svcs.toArray(new Service[svcs.size()]);
    }
    
    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getServiceNameFromId(int)
	 */
    @Override
    public String getServiceNameFromId(int serviceId) {
        OnmsServiceType type = m_serviceTypeDao.get(serviceId);
        return type == null ? null : type.getName();
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getServiceIdFromName(java.lang.String)
	 */
    @Override
    public int getServiceIdFromName(String serviceName) {
        if (serviceName == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }

        OnmsServiceType type = m_serviceTypeDao.findByName(serviceName);
        return type == null ? -1 : type.getId();
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getServiceNameToIdMap()
	 */
    @Override
    public Map<String, Integer> getServiceNameToIdMap(){
        Map<String,Integer> serviceMap = new HashMap<String,Integer>();
        for (OnmsServiceType type : m_serviceTypeDao.findAll()) {
            serviceMap.put(type.getName(), type.getId());
        }
        return serviceMap;
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getAllNodes(int)
	 */
    @Override
    public List<OnmsNode> getAllNodes(int serviceId) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsNode.class);
        criteriaBuilder.alias("ipInterfaces", "ipInterfaces");
        criteriaBuilder.alias("ipInterfaces.monitoredServices", "monSvcs");
        criteriaBuilder.ne("type", "D");
        criteriaBuilder.eq("monSvcs.serviceType.id", serviceId);
        criteriaBuilder.distinct();
        
        return m_nodeDao.findMatching(criteriaBuilder.toCriteria());
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getNodesFromPhysaddr(java.lang.String)
	 */
    @Override
    public List<OnmsNode> getNodesFromPhysaddr(String AtPhysAddr) {
        if (AtPhysAddr == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }
        OnmsCriteria criteria = new OnmsCriteria(OnmsNode.class);
        criteria.createAlias("assetRecord", "assetRecord");
        criteria.createAlias("arpInterfaces", "arpInterfaces");
        criteria.add(Restrictions.ilike("arpInterfaces.physAddr", AtPhysAddr, MatchMode.ANYWHERE));
        criteria.add(Restrictions.ne("arpInterfaces.status", StatusType.DELETED));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        return m_nodeDao.findMatching(criteria);
    }
    

    @Override
    public AtInterface getAtInterface(int nodeId, String ipAddr) {
        return getAtInterfaceForOnmsNode(m_nodeDao.get(nodeId), ipAddr);
    }

    private AtInterface getAtInterfaceForOnmsNode(final OnmsNode onmsNode, final String ipAddr) {
        for (final OnmsArpInterface iface : onmsNode.getArpInterfaces()) {
            final String ifaceAddress = iface.getIpAddress();
            if (ifaceAddress != null && ifaceAddress.equals(ipAddr)) {
                return new AtInterface(iface);
            }
        }
        return null;
    }



    /**
     * <p>getIpRoute</p>
     *
     * @param nodeID a int.
     * @return an array of {@link org.opennms.web.element.IpRouteInterface} objects.
     */
    @Override
    public IpRouteInterface[] getIpRoute(int nodeID) {
        List<OnmsIpRouteInterface> byNode = m_ipRouteInterfaceDao.findByNode(nodeID);
        List<IpRouteInterface> nodes = getIpRouteInterfaceArray(byNode);
        return nodes.toArray(new IpRouteInterface[nodes.size()]);
    }

    private List<IpRouteInterface> getIpRouteInterfaceArray(List<OnmsIpRouteInterface> iproutes ) {
        List<IpRouteInterface> routes = new ArrayList<IpRouteInterface>();
        for (OnmsIpRouteInterface iproute: iproutes) {
            routes.add(new IpRouteInterface(iproute));
        }
        return routes;
    }

    /**
     * <p>isBridgeNode</p>
     *
     * @param nodeID a int.
     * @return a boolean.
     * 
     */
    @Override
    public boolean isBridgeNode(int nodeID) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsStpNode.class);
        criteriaBuilder.join("node","node");
        criteriaBuilder.and(
                Restrictions.eq("node.id", nodeID),
                Restrictions.ne("status", StatusType.DELETED));

        int count = m_stpNodeDao.countMatching(criteriaBuilder.toCriteria());
        return (count > 0);
    }

    /**
     * <p>isRouteInfoNode</p>
     *
     * @param nodeID a int.
     * @return a boolean.
     * 
     */
    @Override
    public boolean isRouteInfoNode(int nodeID) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsIpRouteInterface.class);
        criteriaBuilder.join("node","node");
        criteriaBuilder.and(
                Restrictions.eq("node.id", nodeID),
                Restrictions.ne("status", StatusType.DELETED)).orderBy("ifIndex").asc();

        int count = m_ipRouteInterfaceDao.countMatching(criteriaBuilder.toCriteria());
        //        m_jdbcTemplate.queryForInt("SELECT COUNT(*) FROM IPROUTEINTERFACE WHERE NODEID = ? AND STATUS != 'D'", nodeID);
        return (count > 0);
    }

    /**
     * <p>getLinkedNodeIdOnNode</p>
     *
     * @param nodeID a int.
     * @return a {@link java.util.Set} object.
     * 
     */
    @Override
    public Set<Integer> getLinkedNodeIdOnNode(int nodeID) {
        Set<Integer> nodes = new TreeSet<Integer>();

        for (DataLinkInterface link: m_dataLinkInterfaceDao.findByNodeId(nodeID)) {
            Integer linkedNodeId = link.getNodeParentId();
            if (nodes.contains(linkedNodeId) || link.getStatus().equals(StatusType.DELETED))
                continue;
            nodes.add(linkedNodeId);            
        }

        for (DataLinkInterface link : m_dataLinkInterfaceDao.findByNodeParentId(nodeID)) {
            Integer linkedNodeId = link.getNodeId();
            if (nodes.contains(linkedNodeId) || link.getStatus().equals(StatusType.DELETED))
                continue;
            nodes.add(linkedNodeId);            
        }
        
        // Remove all nulls, TreeSets cannot contain null
        return nodes;
    }
    
    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getDataLinksOnNode(int)
	 */
    @Override
    public List<LinkInterface> getDataLinksOnNode(int nodeId) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(DataLinkInterface.class);
        criteriaBuilder.join("node","node");
        criteriaBuilder.and(
                Restrictions.eq("node.id", nodeId),
                Restrictions.ne("status", StatusType.DELETED)).orderBy("ifIndex").asc();

        List<LinkInterface> ifaces = getDataLinkInterface(m_dataLinkInterfaceDao.findMatching(criteriaBuilder.toCriteria()),nodeId);

        criteriaBuilder = new CriteriaBuilder(DataLinkInterface.class);
        criteriaBuilder.and(
                Restrictions.eq("nodeParentId", nodeId),
                Restrictions.ne("status", StatusType.DELETED));

        ifaces.addAll(getDataLinkInterface(m_dataLinkInterfaceDao.findMatching(criteriaBuilder.toCriteria()),nodeId));
        
        return ifaces;
    	
    }

    @Override
    public List<LinkInterface> getDataLinksOnInterface(int nodeId, String ipAddress){
    	Interface iface = getInterface(nodeId, ipAddress);
    	if (iface != null && Integer.valueOf(iface.getIfIndex()) != null && iface.getIfIndex() > 0) {
    		return getDataLinksOnInterface(nodeId, iface.getIfIndex());    		
    	}
    	return new ArrayList<LinkInterface>();
    }
    
    @Override
    public List<LinkInterface> getDataLinksOnInterface(int id){
    	Interface iface = getInterface(id);
    	if (iface != null && Integer.valueOf(iface.getIfIndex()) != null && iface.getIfIndex() > 0) {
    		return getDataLinksOnInterface(iface.getNodeId(), iface.getIfIndex());    		
    	}
    	return new ArrayList<LinkInterface>();    	
    }


    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getDataLinksOnInterface(int, int)
	 */
    @Override
    public List<LinkInterface> getDataLinksOnInterface(int nodeId, int ifIndex){

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(DataLinkInterface.class);
        criteriaBuilder.join("node","node");
        criteriaBuilder.and(
                Restrictions.eq("node.id", nodeId),
                Restrictions.and(
                        Restrictions.ne("status", StatusType.DELETED),
                        Restrictions.eq("ifIndex", ifIndex)));

        List<LinkInterface> ifaces = getDataLinkInterface(m_dataLinkInterfaceDao.findMatching(criteriaBuilder.toCriteria()),nodeId);

        criteriaBuilder = new CriteriaBuilder(DataLinkInterface.class);
        criteriaBuilder.and(
                Restrictions.eq("nodeParentId", nodeId),
                Restrictions.and(
                        Restrictions.ne("parentIfIndex", ifIndex),
                        Restrictions.eq("status", StatusType.DELETED))).orderBy("parentIfIndex").asc();


        ifaces.addAll(getDataLinkInterface(m_dataLinkInterfaceDao.findMatching(criteriaBuilder.toCriteria()),nodeId));
        
        return ifaces;
    	
    	
    }


    private List<LinkInterface> getDataLinkInterface(List<DataLinkInterface> dlifaces, int nodeId) {
    	List<LinkInterface> lifaces = new ArrayList<LinkInterface>();
    	for (DataLinkInterface dliface: dlifaces) {
    		if (dliface.getNode().getId() == nodeId) {
    			lifaces.add(createLinkInterface(dliface, false));
    		} else if (dliface.getNodeParentId() == nodeId ) {
    			lifaces.add(createLinkInterface(dliface, true));
    		}
    	}
    	return lifaces;
    }
    
    /*
     * Casi d'uso
     * 1) nessuna interfaccia associabile (come rappresentare il link?) 
     * se il nodo ha una sola interfaccia allora va associata anche a quella
     * altrimenti non la associamo
     * 2) node ha ip interface e node parent has SNMP interface
     * 3) node ha una interfaccia SNMP e node parent pure
     * 
     */
    private LinkInterface createLinkInterface(DataLinkInterface dliface, boolean isParent) {

        Integer nodeid = dliface.getNode().getId();
        Integer ifindex = dliface.getIfIndex();

        Integer linkedNodeid = dliface.getNodeParentId();
        Integer linkedIfindex = dliface.getParentIfIndex();

        if (isParent) {
            nodeid = dliface.getNodeParentId();
            ifindex = dliface.getParentIfIndex();
            
            linkedNodeid = dliface.getNode().getId();
            linkedIfindex = dliface.getIfIndex();
        } 
    		
        Interface iface = getInterfaceForLink(nodeid, ifindex);
        Interface linkedIface = getInterfaceForLink(linkedNodeid, linkedIfindex); 
    		
        return new LinkInterface(dliface, isParent, iface, linkedIface);
    }
	
    private Interface getInterfaceForLink(int nodeid, int ifindex) {
        Interface iface = null;
        if (ifindex > 0 ) {
            iface = getSnmpInterface(nodeid, ifindex);

            CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsIpInterface.class);
            criteriaBuilder.and(
                    Restrictions.eq("nodeid", nodeid),
                    Restrictions.ne("ifindex", ifindex));

            List<String> addresses = new ArrayList<String>();
            for (OnmsIpInterface onmsIpInterface : m_ipInterfaceDao.findMatching(criteriaBuilder.toCriteria())) {
                addresses.add(onmsIpInterface.getIpAddress().getHostAddress());
            }

            if (addresses.size() > 0 ) {
            if (iface ==  null) {
                iface = new Interface();
                iface.m_nodeId = nodeid;
                iface.m_ifIndex = ifindex;
            }
            iface.setIpaddresses(addresses);
            } else {
                if (iface != null)
                    iface.setIpaddresses(addresses);
            }
        }
        return iface;
    }
    
    /**
     * <p>getVlansOnNode</p>
     *
     * @param nodeID a int.
     * @return an array of {@link org.opennms.web.element.Vlan} objects.
     * 
     */
    @Override
    public Vlan[] getVlansOnNode(int nodeID) {
        //String sqlQuery = "SELECT * from vlan WHERE status != 'D' AND nodeid = ? order by vlanid;";
        //m_jdbcTemplate.query(sqlQuery, new VlanRowMapper(), nodeID);

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsVlan.class);
        criteriaBuilder.join("node","node");
        criteriaBuilder.and(
                Restrictions.eq("node.id", nodeID),
                Restrictions.ne("status", StatusType.DELETED));

        List<Vlan> vlans = getVlans(m_vlanDao.findMatching(criteriaBuilder.toCriteria()));
        return vlans.toArray(new Vlan[vlans.size()]);
    }

    private List<Vlan> getVlans(List<OnmsVlan> onmsvlans) {
        List<Vlan> vlans = new ArrayList<Vlan>();
        for (OnmsVlan onmsvlan: onmsvlans) {
            vlans.add(new Vlan(onmsvlan));
        }
        return vlans;
    }
    /**
     * <p>getStpInterface</p>
     *
     * @param nodeID a int.
     * @return an array of {@link org.opennms.web.element.StpInterface} objects.
     * 
     */
    @Override
    public StpInterface[] getStpInterface(int nodeID) {

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsStpInterface.class);
        criteriaBuilder.join("node","node");
        criteriaBuilder.and(
                Restrictions.eq("node.id", nodeID),
                Restrictions.ne("status", StatusType.DELETED));

        List<StpInterface> stpinterfaces = new ArrayList<StpInterface>();
    	for (OnmsStpInterface onmsStpInterface: m_stpInterfaceDao.findMatching(criteriaBuilder.toCriteria())) {
    		stpinterfaces.add(getStpInterface(onmsStpInterface));
    	}
    	/*
        String sqlQuery = "SELECT DISTINCT(stpnode.nodeid) AS droot, stpinterfacedb.* FROM "
            + "((SELECT DISTINCT(stpnode.nodeid) AS dbridge, stpinterface.* FROM "
            + "stpinterface LEFT JOIN stpnode ON SUBSTR(stpportdesignatedbridge,5,16) = stpnode.basebridgeaddress " 
            + "AND stpportdesignatedbridge != '0000000000000000'"
            + "WHERE stpinterface.status != 'D' AND stpinterface.nodeid = ?) AS stpinterfacedb "
            + "LEFT JOIN stpnode ON SUBSTR(stpportdesignatedroot, 5, 16) = stpnode.basebridgeaddress) order by stpinterfacedb.stpvlan, stpinterfacedb.ifindex;";
        List<StpInterface> nodes = m_jdbcTemplate.query(sqlQuery, new StpInterfaceRowMapper(), nodeID);
        */
    	
        return stpinterfaces.toArray(new StpInterface[stpinterfaces.size()]);
    }

    /**
     * <p>getStpInterface</p>
     *
     * @param nodeID a int.
     * @param ifindex a int.
     * @return an array of {@link org.opennms.web.element.StpInterface} objects.
     * 
     */
    @Override
    public StpInterface[] getStpInterface(int nodeID, int ifindex) {
    	/*
        String sqlQuery = "SELECT DISTINCT(stpnode.nodeid) AS droot, stpinterfacedb.* FROM "
            + "((SELECT DISTINCT(stpnode.nodeid) AS dbridge, stpinterface.* FROM "
            + "stpinterface LEFT JOIN stpnode ON SUBSTR(stpportdesignatedbridge,5,16) = stpnode.basebridgeaddress "
            + "AND stpportdesignatedbridge != '0000000000000000'"
            + "WHERE stpinterface.status != 'D' AND stpinterface.nodeid = ? AND stpinterface.ifindex = ?) AS stpinterfacedb "
            + "LEFT JOIN stpnode ON SUBSTR(stpportdesignatedroot, 5, 16) = stpnode.basebridgeaddress) order by stpinterfacedb.stpvlan, stpinterfacedb.ifindex;";
        List<StpInterface> nodes = m_jdbcTemplate.query(sqlQuery, new StpInterfaceRowMapper(), nodeID, ifindex);
        */

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsStpNode.class);
        criteriaBuilder.join("node","node");
        criteriaBuilder.and(
                Restrictions.eq("node.id", nodeID),
                Restrictions.and(
                        Restrictions.eq("ifIndex", ifindex),
                        Restrictions.ne("status", StatusType.DELETED)));

        List<StpInterface> stpinterfaces = new ArrayList<StpInterface>();
    	for (OnmsStpInterface onmsStpInterface: m_stpInterfaceDao.findMatching(criteriaBuilder.toCriteria())) {
    		stpinterfaces.add(getStpInterface(onmsStpInterface));
    	}
        return stpinterfaces.toArray(new StpInterface[stpinterfaces.size()]);
    }

    /**
     * <p>getStpNode</p>
     *
     * @param nodeID a int.
     * @return an array of {@link org.opennms.web.element.StpNode} objects.
     * 
     */
    @Override
    public StpNode[] getStpNode(int nodeID) {

                    	/*
        String sqlQuery = "select distinct(e2.nodeid) as stpdesignatedrootnodeid, e1.* from (stpnode e1 left join stpnode e2 on substr(e1.stpdesignatedroot, 5, 16) = e2.basebridgeaddress) where e1.nodeid = ? AND e1.status != 'D' ORDER BY e1.basevlan";
        List<StpNode> nodes = m_jdbcTemplate.query(sqlQuery, new StpNodeRowMapper(), nodeID);
		*/

        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsStpNode.class);
        criteriaBuilder.join("node","node");
        criteriaBuilder.and(
                Restrictions.eq("node.id", nodeID),
                Restrictions.ne("status", StatusType.DELETED));
    	List<StpNode> nodes = new ArrayList<StpNode>();

    	for (OnmsStpNode onmsstpnode: m_stpNodeDao.findMatching(criteriaBuilder.toCriteria())) {
    		nodes.add(getStpNode(onmsstpnode));
    	}
        return nodes.toArray(new StpNode[nodes.size()]);
    }

    private Integer getStpNodeFromStpRootIdentifier(String baseaddress) {
        List<OnmsStpNode> stpnodes = m_stpNodeDao.findByBaseBridgeAddress(baseaddress.substring(5,16));
        if (stpnodes.size() == 1)
        	return stpnodes.get(0).getId();
        return null;
    }

    private StpInterface getStpInterface(OnmsStpInterface onmsStpInterface)  {
        StpInterface stpIf = new StpInterface(onmsStpInterface);
        if (stpIf.get_stpdesignatedbridge() != null) {
        Integer element = getStpNodeFromStpRootIdentifier(stpIf.get_stpdesignatedbridge());
	        if (element != null) {
	            stpIf.setStpBridgeNodeid(element);
	        }
        }
        return stpIf;
    }
    /**
     * This class converts data from the result set into {@link StpNode}
     * objects.
     */
    private StpNode getStpNode(OnmsStpNode node) {
        StpNode stpNode = new StpNode(node);
        if (node.getStpDesignatedRoot() != null) {
        	Integer element = getStpNodeFromStpRootIdentifier(node.getStpDesignatedRoot());
        	if (element != null) {
        		stpNode.m_stprootnodeid = element;
        	}
        }
        return stpNode;
    }

    /* (non-Javadoc)
	 * @see org.opennms.web.element.NetworkElementFactoryInterface#getNodeIdsWithIpLike(java.lang.String)
	 */
    @Override
    public List<Integer> getNodeIdsWithIpLike(String iplike){
        if (iplike == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }
        
        OnmsCriteria nodeCrit = new OnmsCriteria(OnmsNode.class);
        nodeCrit.createCriteria("ipInterfaces", "iface");
        nodeCrit.add(OnmsRestrictions.ipLike(iplike));
        nodeCrit.add(Restrictions.ne("type", "D"));
        nodeCrit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List<Integer> nodeIds = new ArrayList<Integer>();
        List<OnmsNode> nodes = m_nodeDao.findMatching(nodeCrit);
        for(OnmsNode node : nodes) {
            nodeIds.add(node.getId());
        }
        
        return nodeIds;
    }

    
    private Interface[] getInterfaceArray(List<OnmsIpInterface> ipIfaces) {
        List<Interface> intfs = new LinkedList<Interface>();
        for(OnmsIpInterface iface : ipIfaces) {
            intfs.add(new Interface(iface));
        }
        
        Collections.sort(intfs, INTERFACE_COMPARATOR);
        return intfs.toArray(new Interface[intfs.size()]);
    }
    
    private Interface[] getInterfaceArrayWithSnmpData(List<OnmsIpInterface> ipIfaces) {
        List<Interface> intfs = new LinkedList<Interface>();
        for(OnmsIpInterface iface : ipIfaces) {
            Interface intf = new Interface(iface);
            if(iface.getSnmpInterface() != null) {
                OnmsSnmpInterface snmpIface = iface.getSnmpInterface();
                intf.createSnmpInterface(snmpIface);
            }
            intfs.add(intf);
        }
        
        Collections.sort(intfs, INTERFACE_COMPARATOR);
        return intfs.toArray(new Interface[intfs.size()]);
    }    
}
