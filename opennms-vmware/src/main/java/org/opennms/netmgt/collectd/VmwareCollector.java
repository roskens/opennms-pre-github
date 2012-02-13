package org.opennms.netmgt.collectd;

import com.vmware.vim25.mo.ManagedEntity;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.LogUtils;
import org.opennms.core.utils.ParameterMap;
import org.opennms.netmgt.collectd.vmware.VmwareViJavaAccess;
import org.opennms.netmgt.collectd.vmware.vijava.*;
import org.opennms.netmgt.config.DataCollectionConfigFactory;
import org.opennms.netmgt.config.DataSourceFactory;
import org.opennms.netmgt.config.VmwareDataCollectionConfigFactory;
import org.opennms.netmgt.config.collector.AttributeGroupType;
import org.opennms.netmgt.config.collector.CollectionSet;
import org.opennms.netmgt.config.vmware.vijava.*;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.RrdRepository;
import org.opennms.netmgt.model.events.EventProxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.*;

import static junit.framework.Assert.assertNotNull;

public class VmwareCollector implements ServiceCollector {

    // the attribute groups
    private HashMap<String, AttributeGroupType> m_groupTypeList = new HashMap<String, AttributeGroupType>();

    // the attribute types
    private HashMap<String, VmwareCollectionAttributeType> m_attribTypeList = new HashMap<String, VmwareCollectionAttributeType>();

    // the node dao object for retrieving assets
    private NodeDao m_nodeDao = null;

    public void initialize(Map<String, String> parameters) throws CollectionInitializationException {

        m_nodeDao = BeanUtils.getBean("daoContext", "nodeDao", NodeDao.class);
        assertNotNull("Node dao should be a non-null value.", m_nodeDao);

        initVmwareCollectionConfig();
        initDatabaseConnectionFactory();
        initializeRrdRepository();
    }

    private void initVmwareCollectionConfig() {
        LogUtils.debugf(this, "initialize: Initializing collector: %s", getClass());
        try {
            VmwareDataCollectionConfigFactory.init();
        } catch (final MarshalException e) {
            LogUtils.errorf(this, e, "initialize: Error marshalling configuration.");
            throw new UndeclaredThrowableException(e);
        } catch (ValidationException e) {
            LogUtils.errorf(this, e, "initialize: Error validating configuration.");
            throw new UndeclaredThrowableException(e);
        } catch (FileNotFoundException e) {
            LogUtils.errorf(this, e, "initialize: Error locating configuration.");
            throw new UndeclaredThrowableException(e);
        } catch (IOException e) {
            LogUtils.errorf(this, e, "initialize: Error reading configuration.");
            throw new UndeclaredThrowableException(e);
        }
    }

    private void initializeRrdRepository() {
        LogUtils.debugf(this, "initializeRrdRepository: Initializing RRD repo from WmiCollector...");
        initializeRrdDirs();
    }

    private void initializeRrdDirs() {
        final File f = new File(VmwareDataCollectionConfigFactory.getInstance().getRrdPath());
        if (!f.isDirectory()) {
            if (!f.mkdirs()) {
                throw new RuntimeException("Unable to create RRD file repository.  Path doesn't already exist and could not make directory: " + DataCollectionConfigFactory.getInstance().getRrdPath());
            }
        }
    }

    private void initDatabaseConnectionFactory() {
        try {
            DataSourceFactory.init();
        } catch (final Exception e) {
            LogUtils.errorf(this, e, "initDatabaseConnectionFactory: Error initializing DataSourceFactory.");
            throw new UndeclaredThrowableException(e);
        }
    }

    private void loadAttributeGroupList(final VmwareCollection collection) {
        for (final VmwareGroup vpm : collection.getVmwareGroups().getVmwareGroup()) {
            final AttributeGroupType attribGroupType1 = new AttributeGroupType(vpm.getName(), "all");
            m_groupTypeList.put(vpm.getName(), attribGroupType1);
        }
    }

    private void loadAttributeTypeList(final VmwareCollection collection) {
        for (final VmwareGroup vpm : collection.getVmwareGroups().getVmwareGroup()) {
            for (final Attrib attrib : vpm.getAttrib()) {
                final AttributeGroupType attribGroupType = m_groupTypeList.get(vpm.getName());
                final VmwareCollectionAttributeType attribType = new VmwareCollectionAttributeType(attrib, attribGroupType);
                m_attribTypeList.put(attrib.getName(), attribType);
            }

            // if the resourceType doesn't equals "node" we need another attribute for storing
            // the instance name - we use the value of resourceType and append "Name"
            if (!"node".equals(vpm.getResourceType())) {
                final AttributeGroupType attribGroupType = m_groupTypeList.get(vpm.getName());

                Attrib attrib = new Attrib();

                attrib.setName(vpm.getResourceType() + "Name");
                attrib.setAlias(vpm.getResourceType() + "Name");
                attrib.setType("String");

                final VmwareCollectionAttributeType attribType = new VmwareCollectionAttributeType(attrib, attribGroupType);

                m_attribTypeList.put(attrib.getName(), attribType);
            }
        }
    }

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

    public void release() {
    }

    public void release(CollectionAgent agent) {
    }

    public CollectionSet collect(CollectionAgent agent, EventProxy eproxy, Map<String, Object> parameters) throws CollectionException {

        String collectionName = ParameterMap.getKeyedString(parameters, "collection", ParameterMap.getKeyedString(parameters, "vmware-collection", null));

        final VmwareCollection collection = VmwareDataCollectionConfigFactory.getInstance().getVmwareCollection(collectionName);

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

        VmwareCollectionSet collectionSet = new VmwareCollectionSet(agent);

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

        ManagedEntity managedEntity = vmwareViJavaAccess.getManagedEntityByManagedObjectId(vmwareManagedObjectId);

        VmwarePerformanceValues vmwarePerformanceValues = null;

        try {
            vmwarePerformanceValues = vmwareViJavaAccess.queryPerformanceValues(managedEntity);
        } catch (RemoteException e) {
            LogUtils.warnf(this, "Error retrieving performance values from VMware management server '" + vmwareManagementServer + "' for managed object '" + vmwareManagedObjectId + "'", e.getMessage());

            vmwareViJavaAccess.disconnect();

            return collectionSet;
        }

        for (final VmwareGroup vmwareGroup : collection.getVmwareGroups().getVmwareGroup()) {

            HashSet<String> instanceSet = new HashSet<String>();

            for (final Attrib attrib : vmwareGroup.getAttrib()) {

                final VmwareCollectionAttributeType attribType = m_attribTypeList.get(attrib.getName());

                if (vmwarePerformanceValues.hasInstances(attrib.getName())) {
                    Set<String> instances = vmwarePerformanceValues.getInstances(attrib.getName());
                    for (String instance : instances) {

                        instanceSet.add(instance);

                        VmwareCollectionResource vmwareCollectionResource = new VmwareMultiInstanceCollectionResource(agent, instance, vmwareGroup.getResourceType());
                        vmwareCollectionResource.setAttributeValue(attribType, String.valueOf(vmwarePerformanceValues.getValue(attrib.getName(), instance)));
                        collectionSet.getResources().add(vmwareCollectionResource);

                        LogUtils.debugf(this, "Storing multi instance value " + attrib.getName() + "[" + instance + "]='" + vmwarePerformanceValues.getValue(attrib.getName(), instance) + "' for node " + agent.getNodeId());
                    }
                } else {
                    VmwareCollectionResource vmwareCollectionResource = new VmwareSingleInstanceCollectionResource(agent);
                    vmwareCollectionResource.setAttributeValue(attribType, String.valueOf(vmwarePerformanceValues.getValue(attrib.getName())));
                    collectionSet.getResources().add(vmwareCollectionResource);

                    LogUtils.debugf(this, "Storing single instance value " + attrib.getName() + "='" + vmwarePerformanceValues.getValue(attrib.getName()) + "' for node " + agent.getNodeId());
                }
            }

            if (!"node".equals(vmwareGroup.getResourceType())) {
                VmwareCollectionAttributeType attribType = m_attribTypeList.get(vmwareGroup.getResourceType() + "Name");

                if (attribType != null) {
                    for (String instance : instanceSet) {
                        VmwareCollectionResource vmwareCollectionResource = new VmwareMultiInstanceCollectionResource(agent, instance, vmwareGroup.getResourceType());
                        vmwareCollectionResource.setAttributeValue(attribType, instance);

                        collectionSet.getResources().add(vmwareCollectionResource);

                        LogUtils.debugf(this, "Storing name instance value " + vmwareGroup.getResourceType() + "Name" + "[" + instance + "]='" + instance + "' for node " + agent.getNodeId());
                    }
                } else {
                    LogUtils.warnf(this, "Error storing name instance values: attribType " + vmwareGroup.getResourceType() + "Name not found for node " + agent.getNodeId());
                }
            }
        }

        collectionSet.setStatus(ServiceCollector.COLLECTION_SUCCEEDED);

        vmwareViJavaAccess.disconnect();

        return collectionSet;
    }

    private void initializeRrdDirectory(String path) {
        final File f = new File(path);
        if (!f.isDirectory()) {
            if (!f.mkdirs()) {
                throw new RuntimeException("Unable to create RRD file repository.  Path doesn't already exist and could not make directory: " + DataCollectionConfigFactory.getInstance().getRrdPath());
            }
        }
    }

    public RrdRepository getRrdRepository(final String collectionName) {
        return VmwareDataCollectionConfigFactory.getInstance().getRrdRepository(collectionName);
    }

    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }
}
