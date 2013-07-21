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

package org.opennms.netmgt.collectd;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

import org.opennms.core.db.DataSourceFactory;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.ParameterMap;
import org.opennms.netmgt.config.BeanInfo;
import org.opennms.netmgt.config.JMXDataCollectionConfigFactory;
import org.opennms.netmgt.config.collectd.jmx.Attrib;
import org.opennms.netmgt.config.collector.AttributeGroupType;
import org.opennms.netmgt.config.collector.CollectionAttribute;
import org.opennms.netmgt.config.collector.CollectionAttributeType;
import org.opennms.netmgt.config.collector.CollectionResource;
import org.opennms.netmgt.config.collector.CollectionSet;
import org.opennms.netmgt.config.collector.CollectionSetVisitor;
import org.opennms.netmgt.config.collector.Persister;
import org.opennms.netmgt.config.collector.ServiceParameters;
import org.opennms.netmgt.model.RrdRepository;
import org.opennms.netmgt.model.events.EventProxy;
import org.opennms.protocols.jmx.connectors.ConnectionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class performs the collection and storage of data. The derived class
 * manages the connection and configuration. The SNMPCollector class was used
 * as the starting point for this class so anyone familiar with it should be
 * able to easily understand it.
 * <p>
 * The jmx-datacollection-config.xml defines a list of MBeans and attributes
 * that may be monitored. This class retrieves the list of MBeans for the
 * specified service name (currently jboss and jsr160) and queries the remote
 * server for the attributes. The values are then stored in RRD files.
 * </p>
 * <p>
 * Two types of MBeans may be specified in the jmx-datacollection-config.xml
 * file. Standard MBeans which consist of and ObjectName and their attributes,
 * and WildCard MBeans which performs a query to retrieve MBeans based on a
 * criteria. The current implementation looks like: jboss:a=b,c=d,* Future
 * versions may permit enhanced queries. In either case multiple MBeans may be
 * returned and these MBeans would then be queried to obtain their attributes.
 * There are some important issues then using the wild card approach:
 * </p>
 * <p>
 * <ol>
 * <li>Since multiple MBeans will have the same attribute name there needs to be
 * a way to differentiate them. To handle this situation you need to specify
 * which field in the ObjectName should be used. This is defined as the
 * key-field.</li>
 * <li>The version of RRD that is used is limited to 19 characters. If this
 * limit is exceeded then the data will not be saved. The name is defined as:
 * keyField_attributeName.rrd Since the keyfield is defined in the Object Name
 * and may be too long, you may define an alias for it. The key-alias parameter
 * permit you to define a list of names to be substituted. Only exact matches
 * are handled. An example is:
 * <code>key-alias="this-name-is-long|thisIsNot,name-way-2-long,goodName"</code>
 * </li>
 * <li>If there are keyfields that you want to exclude (exact matches) you may
 * use a comma separated list like: <code>exclude="name1,name2,name3"</code></li>
 * <li>Unlike the Standard MBeans there is no way (currently) to pre-define
 * graphs for them in the snmp-graph.properties file. The only way you can
 * create graphs is to create a custom graph in the Report section. The wild
 * card approach needs to be carefully considered before using it but it can cut
 * down on the amount of work necessary to define what to save.</li>
 * </ol>
 * </p>
 *
 * @author <a href="mailto:mike@opennms.org">Mike Jamison</a>
 * @author <a href="http://www.opennms.org/">OpenNMS</a>
 */
public abstract class JMXCollector implements ServiceCollector {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(JMXCollector.class);

    /**
     * Interface attribute key used to store the map of IfInfo objects which
     * hold data about each interface on a particular node.
     */
    static String IF_MAP_KEY = "org.opennms.netmgt.collectd.JBossCollector.ifMap";

    /**
     * RRD data source name max length.
     */
    private static final int MAX_DS_NAME_LENGTH = 19;

    /**
     * In some circumstances there may be many instances of a given service
     * but running on different ports. Rather than using the port as the
     * identifier users may define a more meaningful name.
     */
    private boolean useFriendlyName = false;

    /**
     * Interface attribute key used to store a JMXNodeInfo object which holds
     * data about the node being polled.
     */
    static String NODE_INFO_KEY = "org.opennms.netmgt.collectd.JMXCollector.nodeInfo";

    /** The service name is provided by the derived class. */
    private String serviceName = null;

    /**
     * <p>
     * Returns the name of the service that the plug-in collects ("JMX").
     * </p>
     *
     * @return The service that the plug-in collects.
     */
    public String serviceName() {
        return serviceName.toUpperCase();
    }

    /**
     * <p>
     * Setter for the field <code>serviceName</code>.
     * </p>
     *
     * @param name
     *            a {@link java.lang.String} object.
     */
    public void setServiceName(String name) {
        serviceName = name;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Initialize the service collector.
     * </p>
     * <p>
     * During initialization the JMX collector: - Initializes various
     * configuration factories. - Verifies access to the database - Verifies
     * access to RRD file repository - Verifies access to JNI RRD shared library
     * - Determines if JMX to be stored for only the node's primary interface or
     * for all interfaces.
     * </p>
     *
     * @exception RuntimeException
     *                Thrown if an unrecoverable error occurs that prevents
     *                the plug-in from functioning.
     */
    @Override
    public void initialize(Map<String, String> parameters) {
        // Initialize the JMXDataCollectionConfigFactory
        try {
            // XXX was reload(), which isn't test-friendly
            JMXDataCollectionConfigFactory.init();
        } catch (Throwable e) {
            LOG.error("initialize: Failed to load data collection configuration", e);
            throw new UndeclaredThrowableException(e);
        }

        // Make sure we can connect to the database
        java.sql.Connection ctest = null;
        try {
            DataSourceFactory.init();
            ctest = DataSourceFactory.getInstance().getConnection();
        } catch (final Exception e) {
            LOG.error("initialize: failed to get a database connection", e);
            throw new UndeclaredThrowableException(e);
        } finally {
            if (ctest != null) {
                try {
                    ctest.close();
                } catch (final Throwable t) {
                    LOG.debug("initialize: an exception occured while closing the JDBC connection");
                }
            }
        }

        // Save local reference to singleton instance

        LOG.debug("initialize: successfully instantiated JNI interface to RRD.");
    }

    /**
     * Responsible for freeing up any resources held by the collector.
     */
    @Override
    public void release() {
        // Nothing to release...
    }

    /**
     * {@inheritDoc} Responsible for performing all necessary initialization for
     * the
     * specified interface in preparation for data collection.
     */
    @Override
    public void initialize(CollectionAgent agent, Map<String, Object> parameters) {
        InetAddress ipAddr = agent.getAddress();
        int nodeID = agent.getNodeId();

        // Retrieve the name of the JMX data collector
        String collectionName = ParameterMap.getKeyedString(parameters, "collection", serviceName);

        final String hostAddress = InetAddressUtils.str(ipAddr);
        LOG.debug("initialize: InetAddress={}, collectionName={}", hostAddress, collectionName);

        JMXNodeInfo nodeInfo = new JMXNodeInfo(nodeID);
        LOG.debug("nodeInfo: {} {} {}", hostAddress, nodeID, agent);

        /*
         * Retrieve list of MBean objects to be collected from the
         * remote agent which are to be stored in the node-level RRD file.
         * These objects pertain to the node itself not any individual
         * interfaces.
         */
        Map<String, List<Attrib>> attrMap = JMXDataCollectionConfigFactory.getInstance().getAttributeMap(collectionName,
                                                                                                         serviceName,
                                                                                                         hostAddress);
        nodeInfo.setAttributeMap(attrMap);

        Map<String, JMXDataSource> dsList = buildDataSourceList(collectionName, attrMap);
        nodeInfo.setDsMap(dsList);
        nodeInfo.setMBeans(JMXDataCollectionConfigFactory.getInstance().getMBeanInfo(collectionName));

        // Add the JMXNodeInfo object as an attribute of the interface
        agent.setAttribute(NODE_INFO_KEY, nodeInfo);
        agent.setAttribute("collectionName", collectionName);

    }

    /**
     * {@inheritDoc} Responsible for releasing any resources associated with the
     * specified
     * interface.
     */
    @Override
    public void release(CollectionAgent agent) {
        // Nothing to release...
    }

    /**
     * <p>
     * getMBeanServerConnection
     * </p>
     * .
     *
     * @param map
     *            a {@link java.util.Map} object.
     * @param address
     *            a {@link java.net.InetAddress} object.
     * @return a {@link org.opennms.protocols.jmx.connectors.ConnectionWrapper}
     *         object.
     */
    public abstract ConnectionWrapper getMBeanServerConnection(Map<String, Object> map, InetAddress address);

    /**
     * {@inheritDoc} Perform data collection.
     */
    @Override
    public CollectionSet collect(CollectionAgent agent, EventProxy eproxy, Map<String, Object> map) {
        InetAddress ipaddr = agent.getAddress();
        JMXNodeInfo nodeInfo = agent.getAttribute(NODE_INFO_KEY);
        Map<String, BeanInfo> mbeans = nodeInfo.getMBeans();
        String collDir = serviceName;

        String port = ParameterMap.getKeyedString(map, "port", null);
        String friendlyName = ParameterMap.getKeyedString(map, "friendly-name", port);
        if (useFriendlyName) {
            collDir = friendlyName;
        }

        JMXCollectionSet collectionSet = new JMXCollectionSet(agent, collDir);
        collectionSet.setCollectionTimestamp(new Date());
        JMXCollectionResource collectionResource = collectionSet.getResource();

        ConnectionWrapper connection = null;

        LOG.debug("collecting {} on node ID {}", InetAddressUtils.str(ipaddr), nodeInfo.getNodeId());

        try {
            connection = getMBeanServerConnection(map, ipaddr);

            if (connection == null) {
                return collectionSet;
            }

            MBeanServerConnection mbeanServer = connection.getMBeanServer();

            int retry = ParameterMap.getKeyedInteger(map, "retry", 3);
            for (int attempts = 0; attempts <= retry; attempts++) {
                try {
                    /*
                     * Iterate over the mbeans, for each object name perform a
                     * getAttributes, the update the RRD.
                     */

                    for (Iterator<BeanInfo> iter = mbeans.values().iterator(); iter.hasNext();) {
                        BeanInfo beanInfo = iter.next();
                        String objectName = beanInfo.getObjectName();
                        String excludeList = beanInfo.getExcludes();
                        // All JMX collected values are per node
                        AttributeGroupType attribGroupType = new AttributeGroupType(fixGroupName(objectName), "all");

                        List<String> attribNames = beanInfo.getAttributeNames();
                        List<String> compAttribNames = beanInfo.getCompositeAttributeNames();

                        for (String compAttribName : compAttribNames) {
                            if (attribNames.contains(compAttribName)) {
                                attribNames.remove(compAttribName);
                                String[] ac = compAttribName.split("\\|", -1);
                                String attrName = ac[0];
                                if (!attribNames.contains(attrName)) {
                                    attribNames.add(attrName);
                                }
                            }
                        }
                        // LOG.debug(" JMXCollector: processed the following attributes: {}",
                        // attribNames);
                        // LOG.debug(" JMXCollector: processed the following Composite Attributes: {}",
                        // compAttribNames);

                        String[] attrNames = attribNames.toArray(new String[attribNames.size()]);

                        if (objectName.indexOf("*") == -1) {
                            LOG.debug("{} Collector - getAttributes: {}, # attributes: {}, # composite attribute members: {}",
                                      serviceName, objectName, attrNames.length, compAttribNames.size());
                            try {
                                ObjectName oName = new ObjectName(objectName);
                                if (mbeanServer.isRegistered(oName)) {
                                    AttributeList attrList = mbeanServer.getAttributes(oName, attrNames);
                                    Map<String, JMXDataSource> dsMap = nodeInfo.getDsMap();
                                    for (Object attribute : attrList) {
                                        List<String> compositeMemberKeys = new ArrayList<String>();
                                        Boolean isComposite = false;
                                        Attribute attrib = (Attribute) attribute;
                                        for (String compAttrName : compAttribNames) {
                                            String[] attribKeys = compAttrName.split("\\|", -1);
                                            if (attrib.getName().equals(attribKeys[0])) {
                                                compositeMemberKeys.add(attribKeys[1]);
                                                isComposite = true;
                                            }
                                        }
                                        if (isComposite) {
                                            try {
                                                CompositeData cd = (CompositeData) attrib.getValue();
                                                for (String key : compositeMemberKeys) {
                                                    /*
                                                     * value = cd.get(key);
                                                     * log.debug(
                                                     * " JMXCollector - got CompositeData: "
                                                     * +
                                                     * objectName + "|" +
                                                     * attrib.getName() + "|" +
                                                     * key + " |-> " +
                                                     * cd.get(key).toString());
                                                     */
                                                    JMXDataSource ds = dsMap.get(objectName + "|" + attrib.getName()
                                                            + "|" + key);
                                                    JMXCollectionAttributeType attribType = new JMXCollectionAttributeType(
                                                                                                                           ds,
                                                                                                                           null,
                                                                                                                           null,
                                                                                                                           attribGroupType);
                                                    collectionResource.setAttributeValue(attribType,
                                                                                         cd.get(key).toString());
                                                }
                                            } catch (final ClassCastException cce) {
                                                LOG.debug("{} Collection - getAttributes (try CompositeData) - ERROR: Failed to cast attribute value to type CompositeData!",
                                                          serviceName, cce);
                                            }
                                        } else {
                                            // this is a normal attribute, so
                                            // fallback to default handler
                                            JMXDataSource ds = dsMap.get(objectName + "|" + attrib.getName());
                                            JMXCollectionAttributeType attribType = new JMXCollectionAttributeType(
                                                                                                                   ds,
                                                                                                                   null,
                                                                                                                   null,
                                                                                                                   attribGroupType);
                                            collectionResource.setAttributeValue(attribType,
                                                                                 attrib.getValue().toString());
                                        }
                                    }
                                }
                            } catch (final InstanceNotFoundException e) {
                                LOG.error("Unable to retrieve attributes from {}", objectName, e);
                            }
                        } else {
                            /*
                             * This section is for ObjectNames that use the
                             * '*' wildcard
                             */
                            Set<ObjectName> mbeanSet = getObjectNames(mbeanServer, objectName);
                            for (Iterator<ObjectName> objectNameIter = mbeanSet.iterator(); objectNameIter.hasNext();) {
                                ObjectName oName = objectNameIter.next();
                                LOG.debug("{} Collector - getAttributesWC: {}, # attributes: {}, alias: {}",
                                          serviceName, oName, attrNames.length, beanInfo.getKeyAlias());

                                try {
                                    if (excludeList == null) {
                                        // the exclude list doesn't apply
                                        if (mbeanServer.isRegistered(oName)) {
                                            AttributeList attrList = mbeanServer.getAttributes(oName, attrNames);
                                            Map<String, JMXDataSource> dsMap = nodeInfo.getDsMap();

                                            for (Object attribute : attrList) {
                                                Attribute attrib = (Attribute) attribute;
                                                JMXDataSource ds = dsMap.get(objectName + "|" + attrib.getName());
                                                JMXCollectionAttributeType attribType = new JMXCollectionAttributeType(
                                                                                                                       ds,
                                                                                                                       oName.getKeyProperty(beanInfo.getKeyField()),
                                                                                                                       beanInfo.getKeyAlias(),
                                                                                                                       attribGroupType);

                                                collectionResource.setAttributeValue(attribType,
                                                                                     attrib.getValue().toString());
                                            }

                                        }
                                    } else {
                                        /*
                                         * filter out calls if the key field
                                         * matches an entry in the exclude
                                         * list
                                         */
                                        String keyName = oName.getKeyProperty(beanInfo.getKeyField());
                                        boolean found = false;
                                        StringTokenizer st = new StringTokenizer(excludeList, ",");
                                        while (st.hasMoreTokens()) {
                                            if (keyName.equals(st.nextToken())) {
                                                found = true;
                                                break;
                                            }
                                        }
                                        if (!found) {
                                            if (mbeanServer.isRegistered(oName)) {
                                                AttributeList attrList = mbeanServer.getAttributes(oName, attrNames);
                                                Map<String, JMXDataSource> dsMap = nodeInfo.getDsMap();

                                                for (Object attribute : attrList) {
                                                    Attribute attrib = (Attribute) attribute;
                                                    JMXDataSource ds = dsMap.get(objectName + "|" + attrib.getName());
                                                    JMXCollectionAttributeType attribType = new JMXCollectionAttributeType(
                                                                                                                           ds,
                                                                                                                           oName.getKeyProperty(beanInfo.getKeyField()),
                                                                                                                           beanInfo.getKeyAlias(),
                                                                                                                           attribGroupType);

                                                    collectionResource.setAttributeValue(attribType,
                                                                                         attrib.getValue().toString());
                                                }
                                            }
                                        }
                                    }
                                } catch (final InstanceNotFoundException e) {
                                    LOG.error("Error retrieving attributes for {}", oName, e);
                                }
                            }
                        }
                    }
                    break;
                } catch (final Exception e) {
                    LOG.debug("{} Collector.collect: IOException while collecting address: {}", serviceName,
                              agent.getAddress(), e);
                }
            }
        } catch (final Exception e) {
            LOG.error("Error getting MBeanServer", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        collectionSet.setStatus(ServiceCollector.COLLECTION_SUCCEEDED);
        return collectionSet;
    }

    /**
     * Gets the object names.
     *
     * @param mbeanServer
     *            the mbean server
     * @param objectName
     *            the object name
     * @return the object names
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MalformedObjectNameException
     *             the malformed object name exception
     */
    private Set<ObjectName> getObjectNames(MBeanServerConnection mbeanServer, String objectName) throws IOException,
            MalformedObjectNameException {
        return mbeanServer.queryNames(new ObjectName(objectName), null);
    }

    /**
     * This method removes characters from an object name that are
     * potentially illegal in a file or directory name, returning a
     * name that is appropriate for use with the storeByGroup persistence
     * method.
     *
     * @param objectName
     *            the object name
     * @return the string
     */
    private String fixGroupName(String objectName) {
        if (objectName == null) {
            return "NULL";
        }
        return objectName.replaceAll("[.:=,]", "_");
    }

    /*
     * This method strips out the illegal character '/' and attempts to keep
     * the length of the key plus ds name to 19 or less characters. The slash
     * character cannot be in the name since it is an illegal character in
     * file names.
     */
    /**
     * Fix key.
     *
     * @param key
     *            the key
     * @param attrName
     *            the attr name
     * @param substitutions
     *            the substitutions
     * @return the string
     */
    private String fixKey(String key, String attrName, String substitutions) {
        String newKey = key;
        if (key.startsWith(File.separator)) {
            newKey = key.substring(1);
        }
        if (substitutions != null && substitutions.length() > 0) {
            StringTokenizer st = new StringTokenizer(substitutions, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                int index = token.indexOf("|");
                if (newKey.equals(token.substring(0, index))) {
                    newKey = token.substring(index + 1);
                }
            }
        }
        return newKey;
    }

    /**
     * <p>
     * getRRDValue_isthis_used_
     * </p>
     * .
     *
     * @param ds
     *            the ds
     * @param collectorEntry
     *            a {@link org.opennms.netmgt.collectd.JMXCollectorEntry}
     *            object.
     * @return a {@link java.lang.String} object.
     * @throws IllegalArgumentException
     *             the illegal argument exception
     */
    public String getRRDValue_isthis_used_(JMXDataSource ds, JMXCollectorEntry collectorEntry)
            throws IllegalArgumentException {

        LOG.debug("getRRDValue: {}", ds.getName());

        // Make sure we have an actual object id value.
        if (ds.getOid() == null) {
            return null;
        }

        return collectorEntry.get(collectorEntry + "|" + ds.getOid());
    }

    /**
     * This method is responsible for building a list of RRDDataSource objects
     * from the provided list of MBeanObject objects.
     *
     * @param collectionName
     *            Collection name
     * @param attributeMap
     *            the attribute map
     * @return list of RRDDataSource objects
     */
    protected Map<String, JMXDataSource> buildDataSourceList(String collectionName,
            Map<String, List<Attrib>> attributeMap) {
        LOG.debug("buildDataSourceList - ***");

        /*
         * Retrieve the RRD expansion data source list which contains all
         * the expansion data source's. Use this list as a basis
         * for building a data source list for the current interface.
         */
        HashMap<String, JMXDataSource> dsList = new HashMap<String, JMXDataSource>();

        /*
         * Loop through the MBean object list to be collected for this
         * interface and add a corresponding RRD data source object. In this
         * manner each interface will have RRD files create which reflect only
         * the data sources pertinent to it.
         */

        LOG.debug("attributeMap size: {}", attributeMap.size());
        Iterator<String> objNameIter = attributeMap.keySet().iterator();
        while (objNameIter.hasNext()) {
            String objectName = objNameIter.next().toString();
            List<Attrib> list = attributeMap.get(objectName);

            LOG.debug("ObjectName: {}, Attributes: {}", objectName, list.size());

            Iterator<Attrib> iter = list.iterator();
            while (iter.hasNext()) {
                Attrib attr = iter.next();
                JMXDataSource ds = null;

                /*
                 * Verify that this object has an appropriate "integer" data
                 * type which can be stored in an RRD database file (must map to
                 * one of the supported RRD data source types: COUNTER or
                 * GAUGE).
                 */
                String ds_type = JMXDataSource.mapType(attr.getType());
                if (ds_type != null) {
                    /*
                     * Passed!! Create new data source instance for this MBean
                     * object.
                     * Assign heartbeat using formula (2 * step) and hard code
                     * min & max values to "U" ("unknown").
                     */
                    ds = new JMXDataSource();
                    ds.setHeartbeat(2 * JMXDataCollectionConfigFactory.getInstance().getStep(collectionName));
                    // For completeness, adding a minval option to the variable.
                    String ds_minval = attr.getMinval();
                    if (ds_minval == null) {
                        ds_minval = "U";
                    }
                    ds.setMax(ds_minval);

                    /*
                     * In order to handle counter wraps, we need to set a max
                     * value for the variable.
                     */
                    String ds_maxval = attr.getMaxval();
                    if (ds_maxval == null) {
                        ds_maxval = "U";
                    }

                    ds.setMax(ds_maxval);
                    ds.setInstance(collectionName);

                    /*
                     * Truncate MBean object name/alias if it exceeds 19 char
                     * max for RRD data source names.
                     */
                    String ds_name = attr.getAlias();
                    if (ds_name.length() > MAX_DS_NAME_LENGTH) {
                        LOG.warn("buildDataSourceList: alias '{}' exceeds 19 char maximum for RRD data source names, truncating.",
                                 attr.getAlias());
                        char[] temp = ds_name.toCharArray();
                        ds_name = String.copyValueOf(temp, 0, MAX_DS_NAME_LENGTH);
                    }
                    ds.setName(ds_name);

                    // Map MBean object data type to RRD data type
                    ds.setType(ds_type);

                    /*
                     * Assign the data source object identifier and instance
                     * ds.setName(attr.getName());
                     */
                    ds.setOid(attr.getName());

                    LOG.debug("buildDataSourceList: ds_name: {} ds_oid: {}.{} ds_max: {} ds_min: {}", ds.getName(),
                              ds.getOid(), ds.getInstance(), ds.getMax(), ds.getMin());

                    // Add the new data source to the list
                    dsList.put(objectName + "|" + attr.getName(), ds);
                } else {
                    LOG.warn("buildDataSourceList: Data type '{}' not supported.  Only integer-type data may be stored in RRD.  MBean object '{}' will not be mapped to RRD data source.",
                             attr.getType(), attr.getAlias());
                }
            }
        }

        return dsList;
    }

    /**
     * <p>
     * Setter for the field <code>useFriendlyName</code>.
     * </p>
     *
     * @param useFriendlyName
     *            a boolean.
     */
    public void setUseFriendlyName(boolean useFriendlyName) {
        this.useFriendlyName = useFriendlyName;
    }

    /**
     * The Class JMXCollectionAttributeType.
     */
    class JMXCollectionAttributeType implements CollectionAttributeType {

        /** The m_data source. */
        JMXDataSource m_dataSource;

        /** The m_group type. */
        AttributeGroupType m_groupType;

        /** The m_name. */
        String m_name;

        /**
         * Instantiates a new jMX collection attribute type.
         *
         * @param dataSource
         *            the data source
         * @param key
         *            the key
         * @param substitutions
         *            the substitutions
         * @param groupType
         *            the group type
         */
        protected JMXCollectionAttributeType(JMXDataSource dataSource, String key, String substitutions,
                AttributeGroupType groupType) {
            m_groupType = groupType;
            m_dataSource = dataSource;
            m_name = createName(key, substitutions);
        }

        /**
         * Creates the name.
         *
         * @param key
         *            the key
         * @param substitutions
         *            the substitutions
         * @return the string
         */
        private String createName(String key, String substitutions) {
            String name = m_dataSource.getName();
            if (key != null && !key.equals("")) {
                name = fixKey(key, m_dataSource.getName(), substitutions) + "_" + name;
            }
            return name;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.CollectionAttributeType#getGroupType()
         */
        @Override
        public AttributeGroupType getGroupType() {
            return m_groupType;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.CollectionAttributeType#storeAttribute(org.opennms.netmgt.config.collector.CollectionAttribute, org.opennms.netmgt.config.collector.Persister)
         */
        @Override
        public void storeAttribute(CollectionAttribute attribute, Persister persister) {
            // Only numeric data comes back from JMX in data collection
            persister.persistNumericAttribute(attribute);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.AttributeDefinition#getName()
         */
        @Override
        public String getName() {
            return m_name;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.AttributeDefinition#getType()
         */
        @Override
        public String getType() {
            return m_dataSource.getType();
        }

    }

    /**
     * The Class JMXCollectionAttribute.
     */
    class JMXCollectionAttribute extends AbstractCollectionAttribute implements CollectionAttribute {

        /** The m_alias. */
        String m_alias;

        /** The m_value. */
        String m_value;

        /** The m_resource. */
        JMXCollectionResource m_resource;

        /** The m_attrib type. */
        CollectionAttributeType m_attribType;

        /**
         * Instantiates a new jMX collection attribute.
         *
         * @param resource
         *            the resource
         * @param attribType
         *            the attrib type
         * @param alias
         *            the alias
         * @param value
         *            the value
         */
        JMXCollectionAttribute(JMXCollectionResource resource, CollectionAttributeType attribType, String alias,
                String value) {
            super();
            m_resource = resource;
            m_attribType = attribType;
            m_alias = alias;
            m_value = value;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.collectd.AbstractCollectionAttribute#getAttributeType()
         */
        @Override
        public CollectionAttributeType getAttributeType() {
            return m_attribType;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.collectd.AbstractCollectionAttribute#getName()
         */
        @Override
        public String getName() {
            return m_alias;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.collectd.AbstractCollectionAttribute#getNumericValue()
         */
        @Override
        public String getNumericValue() {
            return m_value;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.collectd.AbstractCollectionAttribute#getResource()
         */
        @Override
        public CollectionResource getResource() {
            return m_resource;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.collectd.AbstractCollectionAttribute#getStringValue()
         */
        @Override
        public String getStringValue() {
            return m_value;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.collectd.AbstractCollectionAttribute#shouldPersist(org.opennms.netmgt.config.collector.ServiceParameters)
         */
        @Override
        public boolean shouldPersist(ServiceParameters params) {
            return true;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.CollectionAttribute#getType()
         */
        @Override
        public String getType() {
            return m_attribType.getType();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "alias " + m_alias + ", value " + m_value + ", resource " + m_resource + ", attributeType "
                    + m_attribType;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.CollectionAttribute#getMetricIdentifier()
         */
        @Override
        public String getMetricIdentifier() {
            String metricId = m_attribType.getGroupType().getName();
            metricId = metricId.replace("_type_", ":type=");
            metricId = metricId.replace("_", ".");
            metricId = metricId.concat(".");
            metricId = metricId.concat(getName());
            return "JMX_".concat(metricId);

        }

    }

    /**
     * The Class JMXCollectionResource.
     */
    class JMXCollectionResource extends AbstractCollectionResource {

        /** The m_resource name. */
        String m_resourceName;

        /** The m_node id. */
        private int m_nodeId;

        /**
         * Instantiates a new jMX collection resource.
         *
         * @param agent
         *            the agent
         * @param resourceName
         *            the resource name
         */
        JMXCollectionResource(CollectionAgent agent, String resourceName) {
            super(agent);
            m_resourceName = resourceName;
            m_nodeId = agent.getNodeId();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "node[" + m_nodeId + ']';
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.collectd.AbstractCollectionResource#getType()
         */
        @Override
        public int getType() {
            return -1; // Is this correct?
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.collectd.AbstractCollectionResource#rescanNeeded()
         */
        @Override
        public boolean rescanNeeded() {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.collectd.AbstractCollectionResource#shouldPersist(org.opennms.netmgt.config.collector.ServiceParameters)
         */
        @Override
        public boolean shouldPersist(ServiceParameters params) {
            return true;
        }

        /**
         * Sets the attribute value.
         *
         * @param type
         *            the type
         * @param value
         *            the value
         */
        public void setAttributeValue(CollectionAttributeType type, String value) {
            JMXCollectionAttribute attr = new JMXCollectionAttribute(this, type, type.getName(), value);
            addAttribute(attr);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.collectd.AbstractCollectionResource#getResourceDir(org.opennms.netmgt.model.RrdRepository)
         */
        @Override
        public File getResourceDir(RrdRepository repository) {
            return new File(repository.getRrdBaseDir(), getParent() + File.separator + m_resourceName);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.CollectionResource#getResourceTypeName()
         */
        @Override
        public String getResourceTypeName() {
            return "node"; // All node resources for JMX; nothing of interface
                           // or "indexed resource" type
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.CollectionResource#getInstance()
         */
        @Override
        public String getInstance() {
            return null; // For node type resources, use the default instance
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.CollectionResource#getParent()
         */
        @Override
        public String getParent() {
            return m_agent.getStorageDir().toString();
        }
    }

    /**
     * The Class JMXCollectionSet.
     */
    class JMXCollectionSet implements CollectionSet {

        /** The m_status. */
        private int m_status;

        /** The m_timestamp. */
        private Date m_timestamp;

        /** The m_collection resource. */
        private JMXCollectionResource m_collectionResource;

        /**
         * Instantiates a new jMX collection set.
         *
         * @param agent
         *            the agent
         * @param resourceName
         *            the resource name
         */
        JMXCollectionSet(CollectionAgent agent, String resourceName) {
            m_status = ServiceCollector.COLLECTION_FAILED;
            m_collectionResource = new JMXCollectionResource(agent, resourceName);
        }

        /**
         * Gets the resource.
         *
         * @return the resource
         */
        public JMXCollectionResource getResource() {
            return m_collectionResource;
        }

        /**
         * Sets the status.
         *
         * @param status
         *            the new status
         */
        public void setStatus(int status) {
            m_status = status;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.CollectionSet#getStatus()
         */
        @Override
        public int getStatus() {
            return m_status;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.CollectionSet#visit(org.opennms.netmgt.config.collector.CollectionSetVisitor)
         */
        @Override
        public void visit(CollectionSetVisitor visitor) {
            visitor.visitCollectionSet(this);
            m_collectionResource.visit(visitor);
            visitor.completeCollectionSet(this);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.CollectionSet#ignorePersist()
         */
        @Override
        public boolean ignorePersist() {
            return false;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.config.collector.CollectionSet#getCollectionTimestamp()
         */
        @Override
        public Date getCollectionTimestamp() {
            return m_timestamp;
        }

        /**
         * Sets the collection timestamp.
         *
         * @param timestamp
         *            the new collection timestamp
         */
        public void setCollectionTimestamp(Date timestamp) {
            this.m_timestamp = timestamp;
        }

    }

    /** {@inheritDoc} */
    @Override
    public RrdRepository getRrdRepository(String collectionName) {
        return JMXDataCollectionConfigFactory.getInstance().getRrdRepository(collectionName);
    }

}
