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

package org.opennms.netmgt.collectd;

import com.vmware.vim25.mo.HostSystem;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.LogUtils;
import org.opennms.core.utils.ParameterMap;
import org.opennms.netmgt.collectd.vmware.VmwareViJavaAccess;
import org.opennms.netmgt.collectd.vmware.cim.VmwareCimCollectionAttributeType;
import org.opennms.netmgt.collectd.vmware.cim.VmwareCimCollectionResource;
import org.opennms.netmgt.collectd.vmware.cim.VmwareCimCollectionSet;
import org.opennms.netmgt.collectd.vmware.cim.VmwareCimMultiInstanceCollectionResource;
import org.opennms.netmgt.config.DataSourceFactory;
import org.opennms.netmgt.config.collector.AttributeGroupType;
import org.opennms.netmgt.config.collector.CollectionSet;
import org.opennms.netmgt.config.vmware.cim.*;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.VmwareCimDatacollectionConfigDao;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.RrdRepository;
import org.opennms.netmgt.model.events.EventProxy;
import org.sblim.wbem.cim.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.*;

import static junit.framework.Assert.assertNotNull;

public class VmwareCimCollector implements ServiceCollector {

    /**
     * the attribute groups
     */
    private HashMap<String, AttributeGroupType> m_groupTypeList = new HashMap<String, AttributeGroupType>();

    /**
     * the attribute types
     */
    private HashMap<String, VmwareCimCollectionAttributeType> m_attribTypeList = new HashMap<String, VmwareCimCollectionAttributeType>();

    /**
     * the node dao object for retrieving assets
     */
    private NodeDao m_nodeDao = null;

    /**
     * the config dao
     */
    VmwareCimDatacollectionConfigDao m_vmwareCimDatacollectionConfigDao;

    /**
     * Initializes this instance with a given parameter map.
     *
     * @param parameters the parameter map to use
     * @throws CollectionInitializationException
     *
     */
    public void initialize(Map<String, String> parameters) throws CollectionInitializationException {
        if (m_nodeDao == null)
            m_nodeDao = BeanUtils.getBean("daoContext", "nodeDao", NodeDao.class);

        assertNotNull("Node dao should be a non-null value.", m_nodeDao);

        if (m_vmwareCimDatacollectionConfigDao == null)
            m_vmwareCimDatacollectionConfigDao = BeanUtils.getBean("daoContext", "vmwareCimDatacollectionConfigDao", VmwareCimDatacollectionConfigDao.class);

        assertNotNull("vmwareCimDatacollectionConfigDao should be a non-null value.", m_vmwareCimDatacollectionConfigDao);

        initDatabaseConnectionFactory();
        initializeRrdRepository();
    }

    /**
     * Initializes the Rrd repository.
     */
    private void initializeRrdRepository() {
        LogUtils.debugf(this, "initializeRrdRepository: Initializing RRD repo from WmiCollector...");
        initializeRrdDirs();
    }

    /**
     * Initializes the Rrd directories.
     */
    private void initializeRrdDirs() {
        final File f = new File(m_vmwareCimDatacollectionConfigDao.getRrdPath());
        if (!f.isDirectory()) {
            if (!f.mkdirs()) {
                throw new RuntimeException("Unable to create RRD file repository.  Path doesn't already exist and could not make directory: " + m_vmwareCimDatacollectionConfigDao.getRrdPath());
            }
        }
    }

    /**
     * Initializes the database connection factory.
     */
    private void initDatabaseConnectionFactory() {
        try {
            DataSourceFactory.init();
        } catch (final Exception e) {
            LogUtils.errorf(this, e, "initDatabaseConnectionFactory: Error initializing DataSourceFactory.");
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Initializes the attribute group list for a given collection name.
     *
     * @param collection the collection's name
     */
    private void loadAttributeGroupList(final VmwareCimCollection collection) {
        for (final VmwareCimGroup vpm : collection.getVmwareCimGroups().getVmwareCimGroup()) {
            final AttributeGroupType attribGroupType1 = new AttributeGroupType(vpm.getName(), "all");
            m_groupTypeList.put(vpm.getName(), attribGroupType1);
        }
    }

    /**
     * Initializes the attribute type list for a given collection name.
     *
     * @param collection the collection's name
     */
    private void loadAttributeTypeList(final VmwareCimCollection collection) {
        for (final VmwareCimGroup vpm : collection.getVmwareCimGroups().getVmwareCimGroup()) {
            for (final Attrib attrib : vpm.getAttrib()) {
                final AttributeGroupType attribGroupType = m_groupTypeList.get(vpm.getName());
                final VmwareCimCollectionAttributeType attribType = new VmwareCimCollectionAttributeType(attrib, attribGroupType);
                m_attribTypeList.put(attrib.getName(), attribType);
            }
        }
    }

    /**
     * Initializes this instance for a given collection agent and a parameter map.
     *
     * @param agent      the collection agent
     * @param parameters the parameter map
     * @throws CollectionInitializationException
     *
     */
    public void initialize(CollectionAgent agent, Map<String, Object> parameters) throws CollectionInitializationException {
        OnmsNode onmsNode = m_nodeDao.get(agent.getNodeId());

        // retrieve the assets and
        String vmwareManagementServer = onmsNode.getAssetRecord().getVmwareManagementServer();
        String vmwareManagedEntityType = onmsNode.getAssetRecord().getVmwareManagedEntityType();
        String vmwareManagedObjectId = onmsNode.getAssetRecord().getVmwareManagedObjectId();

        parameters.put("vmwareManagementServer", vmwareManagementServer);
        parameters.put("vmwareManagedEntityType", vmwareManagedEntityType);
        parameters.put("vmwareManagedObjectId", vmwareManagedObjectId);
    }

    /**
     * This method is used for cleanup.
     */
    public void release() {
    }

    /**
     * This method is used for cleanup for a given collection agent.
     *
     * @param agent the collection agent
     */
    public void release(CollectionAgent agent) {
    }

    /**
     * This method collect the data for a given collection agent.
     *
     * @param agent      the collection agent
     * @param eproxy     the event proxy
     * @param parameters the parameters map
     * @return the generated collection set
     * @throws CollectionException
     */
    public CollectionSet collect(CollectionAgent agent, EventProxy eproxy, Map<String, Object> parameters) throws CollectionException {
        String collectionName = ParameterMap.getKeyedString(parameters, "collection", ParameterMap.getKeyedString(parameters, "vmware-collection", null));

        final VmwareCimCollection collection = m_vmwareCimDatacollectionConfigDao.getVmwareCimCollection(collectionName);

        String vmwareManagementServer = (String) parameters.get("vmwareManagementServer");
        String vmwareManagedObjectId = (String) parameters.get("vmwareManagedObjectId");

        if (vmwareManagementServer == null || vmwareManagedObjectId == null) {
            return null;
        } else {
            if ("".equals(vmwareManagementServer) || "".equals(vmwareManagedObjectId))
                return null;
        }

        // Load the attribute group types.
        loadAttributeGroupList(collection);

        // Load the attribute types.
        loadAttributeTypeList(collection);

        VmwareCimCollectionSet collectionSet = new VmwareCimCollectionSet(agent);

        collectionSet.setCollectionTimestamp(new Date());

        collectionSet.setStatus(ServiceCollector.COLLECTION_FAILED);

        VmwareViJavaAccess vmwareViJavaAccess = null;

        try {
            vmwareViJavaAccess = new VmwareViJavaAccess(vmwareManagementServer);
        } catch (MarshalException e) {
            LogUtils.warnf(this, "Error initialising VMware connection to '" + vmwareManagementServer + "': " + e.getMessage());
            return collectionSet;
        } catch (ValidationException e) {
            LogUtils.warnf(this, "Error initialising VMware connection to '" + vmwareManagementServer + "': " + e.getMessage());
            return collectionSet;
        } catch (IOException e) {
            LogUtils.warnf(this, "Error initialising VMware connection to '" + vmwareManagementServer + "': " + e.getMessage());
            return collectionSet;
        }

        try {
            vmwareViJavaAccess.connect();
        } catch (MalformedURLException e) {
            LogUtils.warnf(this, "Error connecting VMware management server '" + vmwareManagementServer + "': " + e.getMessage());
            return collectionSet;
        } catch (RemoteException e) {
            LogUtils.warnf(this, "Error connecting VMware management server '" + vmwareManagementServer + "': " + e.getMessage());
            return collectionSet;
        }

        HostSystem hostSystem = vmwareViJavaAccess.getHostSystemByManagedObjectId(vmwareManagedObjectId);

        String powerState = hostSystem.getSummary().runtime.getPowerState().toString();

        LogUtils.debugf(this, "The power state for host system '" + vmwareManagedObjectId + "' is '" + powerState + "'");

        if ("poweredOn".equals(powerState)) {
            HashMap<String, Vector<CIMObject>> cimObjects = new HashMap<String, Vector<CIMObject>>();

            for (final VmwareCimGroup vmwareCimGroup : collection.getVmwareCimGroups().getVmwareCimGroup()) {

                String cimClass = vmwareCimGroup.getCimClass();

                if (!cimObjects.containsKey(cimClass)) {
                    Vector<CIMObject> cimVector = null;
                    try {
                        cimVector = vmwareViJavaAccess.queryCimObjects(hostSystem, cimClass);
                    } catch (RemoteException e) {
                        LogUtils.warnf(this, "Error retrieving cim values from host system '" + vmwareManagedObjectId + "'", e.getMessage());
                        return collectionSet;
                    } catch (CIMException e) {
                        LogUtils.warnf(this, "Error retrieving cim values from host system '" + vmwareManagedObjectId + "'", e.getMessage());
                        return collectionSet;
                    } finally {
                        vmwareViJavaAccess.disconnect();
                    }
                    cimObjects.put(cimClass, cimVector);
                }

                final Vector<CIMObject> cimVector = cimObjects.get(cimClass);

                if (cimVector == null) {
                    LogUtils.warnf(this, "Error getting objects of cim class '" + cimClass + "' from host system '" + vmwareManagedObjectId + "'");

                    continue;
                }

                String keyAttribute = vmwareCimGroup.getKey();
                String attributeValue = vmwareCimGroup.getValue();
                String instanceAttribute = vmwareCimGroup.getInstance();

                for (CIMObject cimObject : cimVector) {
                    String sensorType = vmwareViJavaAccess.getPropertyOfCimObject(cimObject, "SensorType");

                    boolean addObject = false;

                    if (keyAttribute != null && attributeValue != null) {
                        String cimObjectValue = vmwareViJavaAccess.getPropertyOfCimObject(cimObject, keyAttribute);

                        if (attributeValue.equals(cimObjectValue)) {
                            addObject = true;
                        } else {
                            addObject = false;
                        }
                    } else {
                        addObject = true;
                    }

                    if (addObject) {
                        String instance = vmwareViJavaAccess.getPropertyOfCimObject(cimObject, instanceAttribute);
                        VmwareCimCollectionResource vmwareCollectionResource = new VmwareCimMultiInstanceCollectionResource(agent, instance, vmwareCimGroup.getResourceType());

                        for (Attrib attrib : vmwareCimGroup.getAttrib()) {
                            final VmwareCimCollectionAttributeType attribType = m_attribTypeList.get(attrib.getName());

                            vmwareCollectionResource.setAttributeValue(attribType, vmwareViJavaAccess.getPropertyOfCimObject(cimObject, attrib.getName()));

                            LogUtils.debugf(this, "Storing multi instance value " + attrib.getName() + "[" + instance + "]='" + vmwareViJavaAccess.getPropertyOfCimObject(cimObject, attrib.getName()) + "' for node " + agent.getNodeId());

                        }
                        collectionSet.getResources().add(vmwareCollectionResource);
                    }
                }
            }
            collectionSet.setStatus(ServiceCollector.COLLECTION_SUCCEEDED);
        }

        vmwareViJavaAccess.disconnect();

        return collectionSet;
    }


    /**
     * Returns the Rrd repository for this object.
     *
     * @param collectionName the collection's name
     * @return the Rrd repository
     */
    public RrdRepository getRrdRepository(final String collectionName) {
        return m_vmwareCimDatacollectionConfigDao.getRrdRepository(collectionName);
    }

    /**
     * Sets the NodeDao object for this instance.
     *
     * @param nodeDao the NodeDao object to use
     */
    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }
}
