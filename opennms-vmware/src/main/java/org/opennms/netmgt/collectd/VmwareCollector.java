package org.opennms.netmgt.collectd;

import com.vmware.vim25.mo.ManagedEntity;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.BeanUtils;
import org.opennms.core.utils.LogUtils;
import org.opennms.core.utils.ParameterMap;
import org.opennms.netmgt.collectd.vmware.VmwareViJavaAccess;
import org.opennms.netmgt.collectd.vmware.vijava.*;
import org.opennms.netmgt.config.DataSourceFactory;
import org.opennms.netmgt.config.collector.AttributeGroupType;
import org.opennms.netmgt.config.collector.CollectionSet;
import org.opennms.netmgt.config.vmware.vijava.*;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.VmwareDatacollectionConfigDao;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.RrdRepository;
import org.opennms.netmgt.model.events.EventProxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.Map.Entry;

import static junit.framework.Assert.assertNotNull;

public class VmwareCollector implements ServiceCollector {

    // the node dao object for retrieving assets
    private NodeDao m_nodeDao = null;

    // the config dao
    private VmwareDatacollectionConfigDao m_vmwareDatacollectionConfigDao;

    public void initialize(Map<String, String> parameters) throws CollectionInitializationException {

        if (m_nodeDao == null)
            m_nodeDao = BeanUtils.getBean("daoContext", "nodeDao", NodeDao.class);

        assertNotNull("Node dao should be a non-null value.", m_nodeDao);

        if (m_vmwareDatacollectionConfigDao == null)
            m_vmwareDatacollectionConfigDao = BeanUtils.getBean("daoContext", "vmwareDatacollectionConfigDao", VmwareDatacollectionConfigDao.class);

        assertNotNull("vmwareDatacollectionConfigDao should be a non-null value.", m_vmwareDatacollectionConfigDao);

        initDatabaseConnectionFactory();
        initializeRrdRepository();
    }


    private void initializeRrdRepository() {
        LogUtils.debugf(this, "initializeRrdRepository: Initializing RRD repo from WmiCollector...");
        initializeRrdDirs();
    }

    private void initializeRrdDirs() {
        final File f = new File(m_vmwareDatacollectionConfigDao.getRrdPath());
        if (!f.isDirectory()) {
            if (!f.mkdirs()) {
                throw new RuntimeException("Unable to create RRD file repository.  Path doesn't already exist and could not make directory: " + m_vmwareDatacollectionConfigDao.getRrdPath());
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

    public CollectionSet collect2(CollectionAgent agent, EventProxy eproxy, Map<String, Object> parameters) throws CollectionException {

        String collectionName = ParameterMap.getKeyedString(parameters, "collection", ParameterMap.getKeyedString(parameters, "vmware-collection", null));

        final VmwareCollection collection = m_vmwareDatacollectionConfigDao.getVmwareCollection(collectionName);

        String vmwareManagementServer = (String) parameters.get("vmwareManagementServer");
        String vmwareManagedObjectId = (String) parameters.get("vmwareManagedObjectId");

        if (vmwareManagementServer == null || vmwareManagedObjectId == null) {
            return null;
        } else {
            if ("".equals(vmwareManagementServer) || "".equals(vmwareManagedObjectId))
                return null;
        }

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
            // Building attribute/value map per instance.
            Map<String, Map<Attrib, String>> instanceValues = new HashMap<String, Map<Attrib, String>>();
            for (final Attrib attrib : vmwareGroup.getAttrib()) {
                if (vmwarePerformanceValues.hasInstances(attrib.getName())) {
                    Set<String> instances = vmwarePerformanceValues.getInstances(attrib.getName());
                    for (String instance : instances) {
                        if (!instanceValues.containsKey(instance)) {
                            instanceValues.put(instance, new HashMap<Attrib, String>());
                        }
                        instanceValues.get(instance).put(attrib, String.valueOf(vmwarePerformanceValues.getValue(attrib.getName(), instance)));
                    }
                } else {
                    if (!instanceValues.containsKey("__single")) {
                        instanceValues.put("__single", new HashMap<Attrib, String>());
                    }
                    instanceValues.get("__single").put(attrib, String.valueOf(vmwarePerformanceValues.getValue(attrib.getName())));
                }
            }
            // Adding instance names as string attribute.
            for (String instance : instanceValues.keySet()) {
                if (!instance.equals("__single")) {
                    final Attrib attrib = new Attrib();
                    attrib.setName(vmwareGroup.getResourceType() + "Name");
                    attrib.setAlias(vmwareGroup.getResourceType() + "Name");
                    attrib.setType("String");
                    instanceValues.get(instance).put(attrib, instance);
                }
            }

            final AttributeGroupType attribGroupType = new AttributeGroupType(vmwareGroup.getName(), "all");
            for (Entry<String, Map<Attrib, String>> instance : instanceValues.entrySet()) {
                VmwareCollectionResource vmwareCollectionResource = null;
                if (instance.getKey().equals("__single")) {
                    vmwareCollectionResource = new VmwareSingleInstanceCollectionResource(agent);
                } else {
                    vmwareCollectionResource = new VmwareMultiInstanceCollectionResource(agent, instance.getKey(), vmwareGroup.getResourceType());
                }
                for (Entry<Attrib, String> entry : instance.getValue().entrySet()) {
                    LogUtils.debugf(this, "Storing instance value %s[%s]='%s' for node %d", entry.getKey().getName(), instance.getKey(), entry.getValue(), agent.getNodeId());
                    final VmwareCollectionAttributeType attribType = new VmwareCollectionAttributeType(entry.getKey(), attribGroupType);
                    vmwareCollectionResource.setAttributeValue(attribType, entry.getValue());
                }
                collectionSet.getResources().add(vmwareCollectionResource);
            }
        }

        collectionSet.setStatus(ServiceCollector.COLLECTION_SUCCEEDED);

        vmwareViJavaAccess.disconnect();

        return collectionSet;
    }

    public CollectionSet collect(CollectionAgent agent, EventProxy eproxy, Map<String, Object> parameters) throws CollectionException {

        String collectionName = ParameterMap.getKeyedString(parameters, "collection", ParameterMap.getKeyedString(parameters, "vmware-collection", null));

        final VmwareCollection collection = m_vmwareDatacollectionConfigDao.getVmwareCollection(collectionName);

        String vmwareManagementServer = (String) parameters.get("vmwareManagementServer");
        String vmwareManagedObjectId = (String) parameters.get("vmwareManagedObjectId");

        if (vmwareManagementServer == null || vmwareManagedObjectId == null) {
            return null;
        } else {
            if ("".equals(vmwareManagementServer) || "".equals(vmwareManagedObjectId))
                return null;
        }

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
            final AttributeGroupType attribGroupType = new AttributeGroupType(vmwareGroup.getName(), "all");

            if ("node".equalsIgnoreCase(vmwareGroup.getName())) {
                // single instance value

                VmwareCollectionResource vmwareCollectionResource = new VmwareSingleInstanceCollectionResource(agent);

                for (Attrib attrib : vmwareGroup.getAttrib()) {

                    if (vmwarePerformanceValues.hasInstances(attrib.getName())) {
                        // warning
                        LogUtils.warnf(this, "Warning! Found multi instance value '%s' defined as single instance attribute for node %d", attrib.getName(), agent.getNodeId());
                    } else {
                        final VmwareCollectionAttributeType attribType = new VmwareCollectionAttributeType(attrib, attribGroupType);
                        LogUtils.debugf(this, "Storing single instance value %s='%s' for node %d", attrib.getName(), String.valueOf(vmwarePerformanceValues.getValue(attrib.getName())), agent.getNodeId());
                        vmwareCollectionResource.setAttributeValue(attribType, String.valueOf(vmwarePerformanceValues.getValue(attrib.getName())));
                    }
                }

                collectionSet.getResources().add(vmwareCollectionResource);
            } else {
                // multi instance value

                Set<String> instanceSet = null;

                HashMap<String, VmwareMultiInstanceCollectionResource> resources = new HashMap<String, VmwareMultiInstanceCollectionResource>();

                for (Attrib attrib : vmwareGroup.getAttrib()) {
                    if (!vmwarePerformanceValues.hasInstances(attrib.getName())) {
                        // warning
                        LogUtils.warnf(this, "Warning! Found single instance value '%s' defined as multi instance attribute for node %d", attrib.getName(), agent.getNodeId());
                    } else {

                        instanceSet = vmwarePerformanceValues.getInstances(attrib.getName());

                        for (String instance : instanceSet) {
                            if (!resources.containsKey(instance))
                                resources.put(instance, new VmwareMultiInstanceCollectionResource(agent, instance, vmwareGroup.getResourceType()));

                            final VmwareCollectionAttributeType attribType = new VmwareCollectionAttributeType(attrib, attribGroupType);
                            LogUtils.debugf(this, "Storing multi instance value %s[%s]='%s' for node %d", attrib.getName(), instance, String.valueOf(vmwarePerformanceValues.getValue(attrib.getName(), instance)), agent.getNodeId());
                            resources.get(instance).setAttributeValue(attribType, String.valueOf(vmwarePerformanceValues.getValue(attrib.getName(), instance)));
                        }
                    }
                }

                if (instanceSet != null) {
                    final Attrib attrib = new Attrib();
                    attrib.setName(vmwareGroup.getResourceType() + "Name");
                    attrib.setAlias(vmwareGroup.getResourceType() + "Name");
                    attrib.setType("String");

                    for (String instance : instanceSet) {
                        final VmwareCollectionAttributeType attribType = new VmwareCollectionAttributeType(attrib, attribGroupType);
                        LogUtils.debugf(this, "Storing multi instance value %s[%s]='%s' for node %d", attrib.getName(), instance, instance, agent.getNodeId());
                        resources.get(instance).setAttributeValue(attribType, instance);
                    }

                    for (String instance : resources.keySet()) {
                        collectionSet.getResources().add(resources.get(instance));
                    }
                }
            }
        }

        collectionSet.setStatus(ServiceCollector.COLLECTION_SUCCEEDED);

        vmwareViJavaAccess.disconnect();

        return collectionSet;
    }

    public RrdRepository getRrdRepository(final String collectionName) {
        return m_vmwareDatacollectionConfigDao.getRrdRepository(collectionName);
    }

    public void setNodeDao(NodeDao nodeDao) {
        m_nodeDao = nodeDao;
    }

    public void setVmwareDatacollectionConfigDao(VmwareDatacollectionConfigDao vmwareDatacollectionConfigDao) {
        this.m_vmwareDatacollectionConfigDao = vmwareDatacollectionConfigDao;
    }

}
