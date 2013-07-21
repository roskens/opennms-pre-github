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

package org.opennms.netmgt.mock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.opennms.netmgt.collectd.SnmpCollector;
import org.opennms.netmgt.config.DataCollectionConfigDao;
import org.opennms.netmgt.config.MibObject;
import org.opennms.netmgt.config.datacollection.DatacollectionConfig;
import org.opennms.netmgt.config.datacollection.ResourceType;
import org.opennms.netmgt.model.RrdRepository;

/**
 * The Class MockDataCollectionConfig.
 */
public class MockDataCollectionConfig implements DataCollectionConfigDao {

    /** The Constant initalMibObjects. */
    public static final String[][] initalMibObjects = { { "sysLocation", ".1.3.6.1.2.1.1.6", "0", "string" },

    { "sysName", ".1.3.6.1.2.1.1.5", "0", "string" },

    { "sysContact", ".1.3.6.1.2.1.1.4", "0", "string" },

    { "sysUptime", ".1.3.6.1.2.1.1.3", "0", "timeTicks" },

    { "sysOid", ".1.3.6.1.2.1.1.2", "0", "string" },

    { "sysDescr", ".1.3.6.1.2.1.1.1", "0", "string" },

    { "ifNumber", ".1.3.6.1.2.1.2.1", "0", "integer" },

    { "ifInDiscards", ".1.3.6.1.2.1.2.2.1.13", "ifIndex", "counter" },

    { "ifOutErrors", ".1.3.6.1.2.1.2.2.1.20", "ifIndex", "counter" },

    { "ifInErrors", ".1.3.6.1.2.1.2.2.1.14", "ifIndex", "counter" },

    { "ifOutOctets", ".1.3.6.1.2.1.2.2.1.16", "ifIndex", "counter" },

    { "ifInOctets", ".1.3.6.1.2.1.2.2.1.10", "ifIndex", "counter" },

    { "ifSpeed", ".1.3.6.1.2.1.2.2.1.5", "ifIndex", "gauge" },

    };

    /** The m_attr list. */
    private List<MibObject> m_attrList;

    /** The m_attr map. */
    private Map<String, MibObject> m_attrMap;

    /**
     * Instantiates a new mock data collection config.
     */
    public MockDataCollectionConfig() {
        setAttrList(new ArrayList<MibObject>());
        setAttrMap(new TreeMap<String, MibObject>());
        addInitialAttributeTypes();
    }

    /**
     * Sets the attr list.
     *
     * @param attrList
     *            the new attr list
     */
    public void setAttrList(List<MibObject> attrList) {
        m_attrList = attrList;
    }

    /**
     * Gets the attr list.
     *
     * @return the attr list
     */
    public List<MibObject> getAttrList() {
        return m_attrList;
    }

    /**
     * Sets the attr map.
     *
     * @param attrMap
     *            the attr map
     */
    public void setAttrMap(Map<String, MibObject> attrMap) {
        m_attrMap = attrMap;
    }

    /**
     * Gets the attr map.
     *
     * @return the attr map
     */
    public Map<String, MibObject> getAttrMap() {
        return m_attrMap;
    }

    /**
     * Creates the mib object.
     *
     * @param alias
     *            the alias
     * @param oid
     *            the oid
     * @param instance
     *            the instance
     * @param type
     *            the type
     * @return the mib object
     */
    private MibObject createMibObject(String alias, String oid, String instance, String type) {
        MibObject mibObj = new MibObject();
        mibObj.setGroupName("test");
        mibObj.setAlias(alias);
        mibObj.setOid(oid);
        mibObj.setType(type);
        mibObj.setInstance(instance);
        mibObj.setGroupName("ifIndex".equals(instance) ? "interface" : "node");
        mibObj.setGroupIfType("ifIndex".equals(instance) ? "all" : "ignored");
        return mibObj;
    }

    /**
     * Creates the attribute type.
     *
     * @param alias
     *            the alias
     * @param oid
     *            the oid
     * @param instance
     *            the instance
     * @param type
     *            the type
     * @return the mib object
     */
    public MibObject createAttributeType(String alias, String oid, String instance, String type) {
        return createMibObject(alias, oid, instance, type);
    }

    /**
     * Define attribute type.
     *
     * @param alias
     *            the alias
     * @param oid
     *            the oid
     * @param instance
     *            the instance
     * @param type
     *            the type
     * @return the mib object
     */
    public MibObject defineAttributeType(String alias, String oid, String instance, String type) {
        MibObject mibObj = createAttributeType(alias, oid, instance, type);
        getAttrMap().put(mibObj.getAlias(), mibObj);
        getAttrMap().put(mibObj.getOid(), mibObj);
        return mibObj;
    }

    /**
     * Adds the initial attribute types.
     */
    public void addInitialAttributeTypes() {
        for (int i = 0; i < MockDataCollectionConfig.initalMibObjects.length; i++) {
            String[] mibData = MockDataCollectionConfig.initalMibObjects[i];
            defineAttributeType(mibData[0], mibData[1], mibData[2], mibData[3]);

        }
    }

    /**
     * Gets the attribute type.
     *
     * @param alias
     *            the alias
     * @param oid
     *            the oid
     * @param inst
     *            the inst
     * @param type
     *            the type
     * @return the attribute type
     */
    public MibObject getAttributeType(String alias, String oid, String inst, String type) {
        MibObject attrType = getAttributeType(alias);
        if (attrType != null)
            return attrType;
        return defineAttributeType(alias, oid, inst, type);

    }

    /**
     * Gets the attribute type.
     *
     * @param aliasOrOid
     *            the alias or oid
     * @return the attribute type
     */
    public MibObject getAttributeType(String aliasOrOid) {
        return getAttrMap().get(aliasOrOid);
    }

    /**
     * Adds the attribute type.
     *
     * @param alias
     *            the alias
     * @param oid
     *            the oid
     * @param inst
     *            the inst
     * @param type
     *            the type
     */
    public void addAttributeType(String alias, String oid, String inst, String type) {
        MibObject attrType = getAttributeType(alias, oid, inst, type);
        getAttrList().add(attrType);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.DataCollectionConfigDao#getRRAList(java.lang.String)
     */
    @Override
    public List<String> getRRAList(String collectionName) {
        return new ArrayList<String>(0);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.DataCollectionConfigDao#getRrdPath()
     */
    @Override
    public String getRrdPath() {
        return "/tmp";
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.DataCollectionConfigDao#getSnmpStorageFlag(java.lang.String)
     */
    @Override
    public String getSnmpStorageFlag(String collectionName) {
        return SnmpCollector.SNMP_STORAGE_PRIMARY;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.DataCollectionConfigDao#getStep(java.lang.String)
     */
    @Override
    public int getStep(String collectionName) {
        return 300;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.DataCollectionConfigDao#getMibObjectList(java.lang.String, java.lang.String, java.lang.String, int)
     */
    @Override
    public List<MibObject> getMibObjectList(String cName, String aSysoid, String anAddress, int ifType) {
        return getAttrList();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.DataCollectionConfigDao#getConfiguredResourceTypes()
     */
    @Override
    public Map<String, ResourceType> getConfiguredResourceTypes() {
        return new TreeMap<String, ResourceType>();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.DataCollectionConfigDao#getRrdRepository(java.lang.String)
     */
    @Override
    public RrdRepository getRrdRepository(String collectionName) {
        RrdRepository repo = new RrdRepository();
        repo.setRrdBaseDir(new File(getRrdPath()));
        repo.setRraList(getRRAList(collectionName));
        repo.setStep(getStep(collectionName));
        repo.setHeartBeat((2 * getStep(collectionName)));
        return repo;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.DataCollectionConfigDao#getRootDataCollection()
     */
    @Override
    public DatacollectionConfig getRootDataCollection() {
        return new DatacollectionConfig();
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.DataCollectionConfigDao#getAvailableDataCollectionGroups()
     */
    @Override
    public List<String> getAvailableDataCollectionGroups() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.DataCollectionConfigDao#getAvailableSystemDefs()
     */
    @Override
    public List<String> getAvailableSystemDefs() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.DataCollectionConfigDao#getAvailableMibGroups()
     */
    @Override
    public List<String> getAvailableMibGroups() {
        return null;
    }

}
