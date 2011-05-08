//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
// OpenNMS Licensing       <license@opennms.org>
//     http://www.opennms.org/
//     http://www.opennms.com/
//
package org.opennms.netmgt.model;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.style.ToStringCreator;

/**
 * Represents the asset information for a node.
 *
 * @hibernate.class table="assets"
 * @author ranger
 * @version $Id: $
 */
@XmlRootElement(name = "assetRecord")
@Entity
@Table(name="assets")
public class OnmsAssetRecord implements Serializable {

    /** Constant <code>AUTOENABLED="A"</code> */
    public static final String AUTOENABLED = "A";
    
    /** Constant <code>RSH_CONNECTION="rsh"</code> */
    public static final String RSH_CONNECTION = "rsh";

    //public enum Autoenable {AUTOENABLED};
    
    private static final long serialVersionUID = 509128305684814487L;

    /** Constant <code>SSH_CONNECTION="ssh"</code> */
    public static final String SSH_CONNECTION = "ssh";

    /** Constant <code>TELNET_CONNECTION="telnet"</code> */
    public static final String TELNET_CONNECTION = "telnet";
    
    //public enum AssetConnections {TELNET_CONNECTION,SSH_CONNECTION,RSH_CONNECTION};

    /** identifier field */
    private String m_additionalhardware;
    
    /** identifier field */
    private String m_address1;

    /** identifier field */
    private String m_address2;

    /** identifier field */
    private String m_admin;

    /** identifier field */
    private String m_assetNumber;

    /** identifier field */
    private String m_autoenable;

    /** identifier field */
    private String m_building;

    /** identifier field */
    private String m_category = "Unspecified";

    /** identifier field */
    private String m_circuitId;

    /** identifier field */
    private String m_city;

    /** identifier field */
    private String m_comment;

    /** identifier field */
    private String m_connection;

    /** identifier field */
    private String m_cpu;

    /** identifier field */
    private String m_dateInstalled;

    /** identifier field */
    private String m_department;

    /** identifier field */
    private String m_description;

    /** identifier field */
    private String m_displayCategory;

    /** identifier field */
    private String m_division;

    /** identifier field */
    private String m_enable;

    /** identifier field */
    private String m_floor;

    /** identifier field */
    private String m_hdd1;

    /** identifier field */
    private String m_hdd2;

    /** identifier field */
    private String m_hdd3;

    /** identifier field */
    private String m_hdd4;

    /** identifier field */
    private String m_hdd5;

    /** identifier field */
    private String m_hdd6;

    private Integer m_id;

    /** identifier field */
    private String m_inputpower;

    /** identifier field */
    private String m_lastModifiedBy = "";

    /** identifier field */
    private Date m_lastModifiedDate = new Date();

    /** identifier field */
    private String m_lease;

    /** identifier field */
    private String m_leaseExpires;

    /** identifier field */
    private String m_maintContractExpiration;

    /** identifier field */
    private String m_maintContract;

    private String m_managedObjectInstance;

    private String m_managedObjectType;

    /** identifier field */
    private String m_manufacturer;

    /** identifier field */
    private String m_modelNumber;

    /** persistent field */
    private OnmsNode m_node;

    /** identifier field */
    private String m_notifyCategory;

    /** identifier field */
    private String m_numpowersupplies;

    /** identifier field */
    private String m_operatingSystem;

    /** identifier field */
    private String m_password;

    /** identifier field */
    private String m_pollerCategory;

    /** identifier field */
    private String m_port;

    /** identifier field */
    private String m_rack;

    /** identifier field */
    private String m_ram;

    /** identifier field */
    private String m_region;

    /** identifier field */
    private String m_room;

    /** identifier field */
    private String m_serialNumber;

    /** identifier field */
    private String m_slot;

    /** identifier field */
    private String m_snmpcommunity;

    /** identifier field */
    private String m_state;

    /** identifier field */
    private String m_storagectrl;

    /** identifier field */
    private String m_supportPhone;

    /** identifier field */
    private String m_thresholdCategory;

    /** identifier field */
    private String m_username;

    /** identifier field */
    private String m_vendor;

    /** identifier field */
    private String m_vendorAssetNumber;

    /** identifier field */
    private String m_vendorFax;

    /** identifier field */
    private String m_vendorPhone;

    /** identifier field */
    private String m_zip;

    /**
     * default constructor
     */
    public OnmsAssetRecord() {
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        
        if (this == obj) {
            return true;
        }
        
        if (obj == null || obj.getClass() != this.getClass()) {
            throw new IllegalArgumentException("the Operation Object passed is either null or of the wrong class");
        }

        OnmsAssetRecord cmpAsset = (OnmsAssetRecord)obj;
        
        Integer newNodeId = cmpAsset.getNode().getId();
        
        if (newNodeId == null) {
            return false;
        }
        
        if (m_node.getId().equals(cmpAsset.getNode().getId())) {
            equals = true;
        }
        
        return equals;
        
    }
    
    /**
     * <p>getAdditionalhardware</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="additionalhardware", length=64)
    public String getAdditionalhardware() {
        return m_additionalhardware;
    }

    /**
     *--# address1         : Address of geographical location of asset, line 1.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="address1", length=256)
    public String getAddress1() {
        return m_address1;
    }

    /**
     *--# address2         : Address of geographical location of asset, line 2.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="address2", length=256)
    public String getAddress2() {
        return m_address2;
    }



    /**
     * <p>getAdmin</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="admin", length=32)
    public String getAdmin() {
        return m_admin;
    }

    /**
     *--# assetNumber      : A business-specified asset number.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="assetNumber", length=64)
    public String getAssetNumber() {
        return m_assetNumber;
    }

    /**
     * <p>getAutoenable</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="autoenable", length=1)
    public String getAutoenable() {
        return m_autoenable;
    }

    /**
     *--# building         : The building where this asset resides.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="building", length=64)
    public String getBuilding() {
        return m_building;
    }

    /**
     *--# category         : A broad idea of what this asset does (examples are
     *--#                    desktop, printer, server, infrastructure, etc.).
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="category", length=64)
    public String getCategory() {
        return m_category;
    }

    /**
     *--# circuitId        : The electrical/network circuit this asset connects to.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="circuitId", length=64)
    public String getCircuitId() {
        return m_circuitId;
    }

    /**
     *--# city             : The city where this asset resides.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="city", length=64)
    public String getCity() {
        return m_city;
    }

    /**
     * <p>getComment</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="comment", length=1024)
    public String getComment() {
        return m_comment;
    }

    /**
     * <p>getConnection</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="connection", length=32)
    public String getConnection() {
        return m_connection;
    }

    /**
     * <p>getCpu</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="cpu", length=32)
    public String getCpu() {
        return m_cpu;
    }

    /**
     *--# dateInstalled    : The date the asset was installed.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="dateInstalled", length=64)
    public String getDateInstalled() {
        return m_dateInstalled;
    }

    /**
     *--# department       : The department this asset belongs to.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="department", length=64)
    public String getDepartment() {
        return m_department;
    }

    /**
     *--# description      : A free-form description.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="description", length=128)
    public String getDescription() {
        return m_description;
    }

    /**
     * <p>getDisplayCategory</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="displayCategory", length=64)
    public String getDisplayCategory() {
        return m_displayCategory;
    }

    /**
     *--# division         : A broad geographical or organizational area.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="division", length=64)
    public String getDivision() {
        return m_division;
    }

    /**
     * <p>getEnable</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="enable", length=32)
    public String getEnable() {
        return m_enable;
    }

    /**
     *--# floor            : The floor of the building where this asset resides.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="floor", length=64)
    public String getFloor() {
        return m_floor;
    }

    /**
     * <p>getHdd1</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="hdd1", length=32)
    public String getHdd1() {
        return m_hdd1;
    }

    /**
     * <p>getHdd2</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="hdd2", length=32)
    public String getHdd2() {
        return m_hdd2;
    }

    /**
     * <p>getHdd3</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="hdd3", length=32)
    public String getHdd3() {
        return m_hdd3;
    }

    /**
     * <p>getHdd4</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="hdd4", length=32)
    public String getHdd4() {
        return m_hdd4;
    }

    /**
     * <p>getHdd5</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="hdd5", length=32)
    public String getHdd5() {
        return m_hdd5;
    }

    /**
     * <p>getHdd6</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="hdd6", length=32)
    public String getHdd6() {
        return m_hdd6;
    }

    /**
     * <p>getId</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    @Id
    @SequenceGenerator(name="opennmsSequence", sequenceName="opennmsNxtId")
    @GeneratedValue(generator="opennmsSequence")    
    public Integer getId() {
        return m_id;
    }

    /**
     * <p>getInputpower</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="inputpower", length=6)
    public String getInputpower() {
        return m_inputpower;
    }

    /**
     *--# userLastModified : The last user who modified this record.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="userLastModified", length=20)
    public String getLastModifiedBy() {
        return m_lastModifiedBy;
    }

    /**
     *--# lastModifiedDate : The last time this record was modified.
     *
     * @return a {@link java.util.Date} object.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastModifiedDate")
    public Date getLastModifiedDate() {
        return m_lastModifiedDate;
    }

    /**
     *--# lease            : The lease number of this asset.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="lease", length=64)
    public String getLease() {
        return m_lease;
    }

    /**
     *--# leaseExpires     : The date the lease expires for this asset.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="leaseExpires", length=64)
    public String getLeaseExpires() {
        return m_leaseExpires;
    }

    /**
     * <p>getMaintContractExpiration</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="maintContractExpires", length=64)
    public String getMaintContractExpiration() {
        return m_maintContractExpiration;
    }

    /**
     *--# maintContract    : The maintenance contract number for this asset.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="maintContract", length=64)
    public String getMaintcontract() {
        return m_maintContract;
    }

    /**
     * <p>getManagedObjectInstance</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="managedObjectInstance", length=512)
    public String getManagedObjectInstance() {
        return m_managedObjectInstance;
    }

    /**
     * <p>getManagedObjectType</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="managedObjectType", length=512)
    public String getManagedObjectType() {
        return m_managedObjectType;
    }

    /**
     *--# manufacturer     : Name of the manufacturer of this asset.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="manufacturer", length=64)
    public String getManufacturer() {
        return m_manufacturer;
    }

    /**
     *--# modelNumber      : The model number of this asset.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="modelNumber", length=64)
    public String getModelNumber() {
        return m_modelNumber;
    }

    /**
     * The node this asset information belongs to.
     *
     * @return a {@link org.opennms.netmgt.model.OnmsNode} object.
     */
    @XmlIDREF
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="nodeId")
    public OnmsNode getNode() {
        return m_node;
    }

    /**
     * <p>getNotifyCategory</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="notifyCategory", length=64)
    public String getNotifyCategory() {
        return m_notifyCategory;
    }

    /**
     * <p>getNumpowersupplies</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="numpowersupplies", length=1)
    public String getNumpowersupplies() {
        return m_numpowersupplies;
    }

    /**
     *--# operatingSystem  : The operating system, if any.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="operatingSystem", length=64)
    public String getOperatingSystem() {
        return m_operatingSystem;
    }

    /**
     * <p>getPassword</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="password", length=32)
    public String getPassword() {
        return m_password;
    }

    /**
     * <p>getPollerCategory</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="pollerCategory", length=64)
    public String getPollerCategory() {
        return m_pollerCategory;
    }

    /**
     *--# port             : For servers, the port in the slot it is installed in.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="port", length=64)
    public String getPort() {
        return m_port;
    }

    /**
     *--# rack             : For servers, the rack it is installed in.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="rack", length=64)
    public String getRack() {
        return m_rack;
    }

    /**
     * <p>getRam</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="ram", length=10)
    public String getRam() {
        return m_ram;
    }

    /**
     *--# region           : A broad geographical or organizational area.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="region", length=64)
    public String getRegion() {
        return m_region;
    }

    /**
     *--# room             : The room where this asset resides.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="room", length=64)
    public String getRoom() {
        return m_room;
    }

    /**
     *--# serialNumber     : The serial number of this asset.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="serialNumber", length=64)
    public String getSerialNumber() {
        return m_serialNumber;
    }

    /**
     *--# slot             : For servers, the slot in the rack it is installed in.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="slot", length=64)
    public String getSlot() {
        return m_slot;
    }

    /**
     * <p>getSnmpcommunity</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="snmpcommunity", length=32)
    public String getSnmpcommunity() {
        return m_snmpcommunity;
    }

    /**
     *--# state            : The state where this asset resides.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="state", length=64)
    public String getState() {
        return m_state;
    }

    /**
     * <p>getStoragectrl</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="storagectrl", length=32)
    public String getStoragectrl() {
        return m_storagectrl;
    }

    /**
     *--# supportPhone     : A support phone number for this asset.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="supportPhone", length=64)
    public String getSupportPhone() {
        return m_supportPhone;
    }

    /**
     * <p>getThresholdCategory</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="thresholdCategory", length=64)
    public String getThresholdCategory() {
        return m_thresholdCategory;
    }

    /**
     * <p>getUsername</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="username", length=32)
    public String getUsername() {
        return m_username;
    }

    /**
     *--# vendor           : Vendor from whom this asset was purchased.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="vendor", length=64)
    public String getVendor() {
        return m_vendor;
    }

    /**
     * <p>getVendorAssetNumber</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="vendorAssetNumber", length=64)
    public String getVendorAssetNumber() {
        return m_vendorAssetNumber;
    }

    /**
     *--# vendorFax        : A fax number for the vendor.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="vendorFax", length=64)
    public String getVendorFax() {
        return m_vendorFax;
    }

    /**
     *--# vendorPhone      : A contact number for the vendor.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="vendorPhone", length=64)
    public String getVendorPhone() {
        return m_vendorPhone;
    }

    /**
     *--# zip              : The zip code where this asset resides.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name="zip", length=64)
    public String getZip() {
        return m_zip;
    }

    /**
     * Used to merge the contents of one asset record to another.  If equals implementation
     * returns false, the merge is aborted.
     *
     * @param newRecord a {@link org.opennms.netmgt.model.OnmsAssetRecord} object.
     */
    public void mergeRecord(OnmsAssetRecord newRecord) {
        
        if (!this.equals(newRecord)) {
            return;
        }
        
        //this works because all asset properties are strings
        //if the model dependencies ever change to not include spring, this will break
        BeanWrapper currentBean = new BeanWrapperImpl(this);
        BeanWrapper newBean = new BeanWrapperImpl(newRecord);
        PropertyDescriptor[] pds = newBean.getPropertyDescriptors();
        
        for (PropertyDescriptor pd : pds) {
            String propertyName = pd.getName();
            
            if (propertyName.equals("class")) {
                continue;
            }
            
            // This should never fail since both of these objects are of the same type
            if (newBean.getPropertyValue(propertyName) != null) {
                currentBean.setPropertyValue(propertyName, newBean.getPropertyValue(propertyName));
            }
        }
    }

    /**
     * <p>setAdditionalhardware</p>
     *
     * @param additionalhardware a {@link java.lang.String} object.
     */
    public void setAdditionalhardware(String additionalhardware) {
            m_additionalhardware = additionalhardware;
    }

    /**
     * <p>setAddress1</p>
     *
     * @param address1 a {@link java.lang.String} object.
     */
    public void setAddress1(String address1) {
        m_address1 = address1;
    }

    /**
     * <p>setAddress2</p>
     *
     * @param address2 a {@link java.lang.String} object.
     */
    public void setAddress2(String address2) {
        m_address2 = address2;
    }

    /**
     * <p>setAdmin</p>
     *
     * @param admin a {@link java.lang.String} object.
     */
    public void setAdmin(String admin) {
            m_admin = admin;
    }

    /**
     * <p>setAssetNumber</p>
     *
     * @param assetnumber a {@link java.lang.String} object.
     */
    public void setAssetNumber(String assetnumber) {
        m_assetNumber = assetnumber;
    }

    /**
     * <p>setAutoenable</p>
     *
     * @param autoenable a {@link java.lang.String} object.
     */
    public void setAutoenable(String autoenable) {
            m_autoenable = autoenable;
    }

    /**
     * <p>setBuilding</p>
     *
     * @param building a {@link java.lang.String} object.
     */
    public void setBuilding(String building) {
        m_building = building;
    }

    /**
     * <p>setCategory</p>
     *
     * @param category a {@link java.lang.String} object.
     */
    public void setCategory(String category) {
        m_category = category;
    }

    /**
     * <p>setCircuitId</p>
     *
     * @param circuitid a {@link java.lang.String} object.
     */
    public void setCircuitId(String circuitid) {
        m_circuitId = circuitid;
    }

    /**
     * <p>setCity</p>
     *
     * @param city a {@link java.lang.String} object.
     */
    public void setCity(String city) {
        m_city = city;
    }

    /**
     * <p>setComment</p>
     *
     * @param comment a {@link java.lang.String} object.
     */
    public void setComment(String comment) {
        m_comment = comment;
    }

    /**
     * <p>setConnection</p>
     *
     * @param connection a {@link java.lang.String} object.
     */
    public void setConnection(String connection) {
        if (connection == null) {
            m_connection = connection;            
        } else {
            if (connection.equalsIgnoreCase(TELNET_CONNECTION))
                m_connection = TELNET_CONNECTION;
            else if (connection.equalsIgnoreCase(SSH_CONNECTION))
                m_connection = SSH_CONNECTION;
            else if (connection.equalsIgnoreCase(RSH_CONNECTION))
                m_connection = RSH_CONNECTION;
            else
                m_connection = connection;
        }
    }

    /**
     * <p>setCpu</p>
     *
     * @param cpu a {@link java.lang.String} object.
     */
    public void setCpu(String cpu) {
            m_cpu = cpu;
    }

    /**
     * <p>setDateInstalled</p>
     *
     * @param dateinstalled a {@link java.lang.String} object.
     */
    public void setDateInstalled(String dateinstalled) {
        m_dateInstalled = dateinstalled;
    }

    /**
     * <p>setDepartment</p>
     *
     * @param department a {@link java.lang.String} object.
     */
    public void setDepartment(String department) {
        m_department = department;
    }

    /**
     * <p>setDescription</p>
     *
     * @param description a {@link java.lang.String} object.
     */
    public void setDescription(String description) {
        m_description = description;
    }

    /**
     * <p>setDisplayCategory</p>
     *
     * @param displaycategory a {@link java.lang.String} object.
     */
    public void setDisplayCategory(String displaycategory) {
        m_displayCategory = displaycategory;
    }

    /**
     * <p>setDivision</p>
     *
     * @param division a {@link java.lang.String} object.
     */
    public void setDivision(String division) {
        m_division = division;
    }
    
    /**
     * <p>setEnable</p>
     *
     * @param enable a {@link java.lang.String} object.
     */
    public void setEnable(String enable) {
        m_enable = enable;
    }
    
    /**
     * <p>setFloor</p>
     *
     * @param floor a {@link java.lang.String} object.
     */
    public void setFloor(String floor) {
        m_floor = floor;
    }
    
    /**
     * <p>setHdd1</p>
     *
     * @param hdd1 a {@link java.lang.String} object.
     */
    public void setHdd1(String hdd1) {
            m_hdd1 = hdd1;
    }
    
    /**
     * <p>setHdd2</p>
     *
     * @param hdd2 a {@link java.lang.String} object.
     */
    public void setHdd2(String hdd2) {
            m_hdd2 = hdd2;
    }
    

    /**
     * <p>setHdd3</p>
     *
     * @param hdd3 a {@link java.lang.String} object.
     */
    public void setHdd3(String hdd3) {
            m_hdd3 = hdd3;
    }

    /**
     * <p>setHdd4</p>
     *
     * @param hdd4 a {@link java.lang.String} object.
     */
    public void setHdd4(String hdd4) {
            m_hdd4 = hdd4;
    }

    /**
     * <p>setHdd5</p>
     *
     * @param hdd5 a {@link java.lang.String} object.
     */
    public void setHdd5(String hdd5) {
            m_hdd5 = hdd5;
    }

    /**
     * <p>setHdd6</p>
     *
     * @param hdd6 a {@link java.lang.String} object.
     */
    public void setHdd6(String hdd6) {
            m_hdd6 = hdd6;
    }

    /**
     * <p>setId</p>
     *
     * @param id a {@link java.lang.Integer} object.
     */
    protected void setId(Integer id) {
        m_id = id;
    }

    /**
     * <p>setInputpower</p>
     *
     * @param inputpower a {@link java.lang.String} object.
     */
    public void setInputpower(String inputpower) {
            m_inputpower = inputpower;
    }

    /**
     * <p>setLastModifiedBy</p>
     *
     * @param userlastmodified a {@link java.lang.String} object.
     */
    public void setLastModifiedBy(String userlastmodified) {
        m_lastModifiedBy = userlastmodified;
    }

    /**
     * <p>setLastModifiedDate</p>
     *
     * @param lastmodifieddate a {@link java.util.Date} object.
     */
    public void setLastModifiedDate(Date lastmodifieddate) {
        m_lastModifiedDate = lastmodifieddate;
    }

    /**
     * <p>setLease</p>
     *
     * @param lease a {@link java.lang.String} object.
     */
    public void setLease(String lease) {
        m_lease = lease;
    }

    /**
     * <p>setLeaseExpires</p>
     *
     * @param leaseexpires a {@link java.lang.String} object.
     */
    public void setLeaseExpires(String leaseexpires) {
        m_leaseExpires = leaseexpires;
    }

    /**
     * <p>setMaintContractExpiration</p>
     *
     * @param maintcontractexpires a {@link java.lang.String} object.
     */
    public void setMaintContractExpiration(String maintcontractexpires) {
        m_maintContractExpiration = maintcontractexpires;
    }

    /**
     * <p>setMaintContract</p>
     *
     * @param maintcontract a {@link java.lang.String} object.
     */
    public void setMaintcontract(String maintcontract) {
        m_maintContract = maintcontract;
    }

    /**
     * <p>setManagedObjectInstance</p>
     *
     * @param moi a {@link java.lang.String} object.
     */
    public void setManagedObjectInstance(String moi) {
        m_managedObjectInstance = moi;
    }

    /**
     * <p>setManagedObjectType</p>
     *
     * @param mot a {@link java.lang.String} object.
     */
    public void setManagedObjectType(String mot) {
        m_managedObjectType = mot;
    }

    /**
     * <p>setManufacturer</p>
     *
     * @param manufacturer a {@link java.lang.String} object.
     */
    public void setManufacturer(String manufacturer) {
        m_manufacturer = manufacturer;
    }

    /**
     * <p>setModelNumber</p>
     *
     * @param modelnumber a {@link java.lang.String} object.
     */
    public void setModelNumber(String modelnumber) {
        m_modelNumber = modelnumber;
    }

    /**
     * Set the node associated with the asset record
     *
     * @param node a {@link org.opennms.netmgt.model.OnmsNode} object.
     */
    public void setNode(OnmsNode node) {
        m_node = node;
    }

    /**
     * <p>setNotifyCategory</p>
     *
     * @param notifycategory a {@link java.lang.String} object.
     */
    public void setNotifyCategory(String notifycategory) {
        m_notifyCategory = notifycategory;
    }

    /**
     * <p>setNumpowersupplies</p>
     *
     * @param numpowersupplies a {@link java.lang.String} object.
     */
    public void setNumpowersupplies(String numpowersupplies) {
            m_numpowersupplies = numpowersupplies;
    }

    /**
     * <p>setOperatingSystem</p>
     *
     * @param operatingsystem a {@link java.lang.String} object.
     */
    public void setOperatingSystem(String operatingsystem) {
        m_operatingSystem = operatingsystem;
    }

    /**
     * <p>setPassword</p>
     *
     * @param password a {@link java.lang.String} object.
     */
    public void setPassword(String password) {
        m_password = password;
    }

    /**
     * <p>setPollerCategory</p>
     *
     * @param pollercategory a {@link java.lang.String} object.
     */
    public void setPollerCategory(String pollercategory) {
        m_pollerCategory = pollercategory;
    }

    /**
     * <p>setPort</p>
     *
     * @param port a {@link java.lang.String} object.
     */
    public void setPort(String port) {
        m_port = port;
    }

    /**
     * <p>setRack</p>
     *
     * @param rack a {@link java.lang.String} object.
     */
    public void setRack(String rack) {
        m_rack = rack;
    }

    /**
     * <p>setRam</p>
     *
     * @param ram a {@link java.lang.String} object.
     */
    public void setRam(String ram) {
            m_ram = ram;
    }

    /**
     * <p>setRegion</p>
     *
     * @param region a {@link java.lang.String} object.
     */
    public void setRegion(String region) {
        m_region = region;
    }

    /**
     * <p>setRoom</p>
     *
     * @param room a {@link java.lang.String} object.
     */
    public void setRoom(String room) {
        m_room = room;
    }

    /**
     * <p>setSerialNumber</p>
     *
     * @param serialnumber a {@link java.lang.String} object.
     */
    public void setSerialNumber(String serialnumber) {
        m_serialNumber = serialnumber;
    }

    /**
     * <p>setSlot</p>
     *
     * @param slot a {@link java.lang.String} object.
     */
    public void setSlot(String slot) {
        m_slot = slot;
    }

    /**
     * <p>setSnmpcommunity</p>
     *
     * @param snmpcommunity a {@link java.lang.String} object.
     */
    public void setSnmpcommunity(String snmpcommunity) {
            m_snmpcommunity = snmpcommunity;
    }

    /**
     * <p>setState</p>
     *
     * @param state a {@link java.lang.String} object.
     */
    public void setState(String state) {
        m_state = state;
    }

    /**
     * <p>setStoragectrl</p>
     *
     * @param storagectrl a {@link java.lang.String} object.
     */
    public void setStoragectrl(String storagectrl) {
            m_storagectrl = storagectrl;
    }

    /**
     * <p>setSupportPhone</p>
     *
     * @param supportphone a {@link java.lang.String} object.
     */
    public void setSupportPhone(String supportphone) {
        m_supportPhone = supportphone;
    }

    /**
     * <p>setThresholdCategory</p>
     *
     * @param thresholdcategory a {@link java.lang.String} object.
     */
    public void setThresholdCategory(String thresholdcategory) {
        m_thresholdCategory = thresholdcategory;
    }

    /**
     * <p>setUsername</p>
     *
     * @param username a {@link java.lang.String} object.
     */
    public void setUsername(String username) {
        m_username = username;
    }

    /**
     * <p>setVendor</p>
     *
     * @param vendor a {@link java.lang.String} object.
     */
    public void setVendor(String vendor) {
        m_vendor = vendor;
    }

    /**
     * <p>setVendorAssetNumber</p>
     *
     * @param vendorassetnumber a {@link java.lang.String} object.
     */
    public void setVendorAssetNumber(String vendorassetnumber) {
        m_vendorAssetNumber = vendorassetnumber;
    }

    /**
     * <p>setVendorFax</p>
     *
     * @param vendorfax a {@link java.lang.String} object.
     */
    public void setVendorFax(String vendorfax) {
        m_vendorFax = vendorfax;
    }

    /**
     * <p>setVendorPhone</p>
     *
     * @param vendorphone a {@link java.lang.String} object.
     */
    public void setVendorPhone(String vendorphone) {
        m_vendorPhone = vendorphone;
    }

    /**
     * <p>setZip</p>
     *
     * @param zip a {@link java.lang.String} object.
     */
    public void setZip(String zip) {
        m_zip = zip;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringCreator(this)
            .append("category", getCategory())
            .append("manufacturer", getManufacturer())
            .append("vendor", getVendor())
            .append("modelnumber", getModelNumber())
            .append("serialnumber", getSerialNumber())
            .append("description", getDescription())
            .append("circuitid", getCircuitId())
            .append("assetnumber", getAssetNumber())
            .append("operatingsystem", getOperatingSystem())
            .append("rack", getRack())
            .append("slot", getSlot())
            .append("port", getPort())
            .append("region", getRegion())
            .append("division", getDivision())
            .append("department", getDepartment())
            .append("address1", getAddress1())
            .append("address2", getAddress2())
            .append("city", getCity())
            .append("state", getState())
            .append("zip", getZip())
            .append("building", getBuilding())
            .append("floor", getFloor())
            .append("room", getRoom())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("enable",getEnable())
            .append("autoenable",getAutoenable())
            .append("connection", getConnection())
            .append("vendorphone", getVendorPhone())
            .append("vendorfax", getVendorFax())
            .append("vendorassetnumber", getVendorAssetNumber())
            .append("userlastmodified", getLastModifiedBy())
            .append("lastmodifieddate", getLastModifiedDate())
            .append("dateinstalled", getDateInstalled())
            .append("lease", getLease())
            .append("leaseexpires", getLeaseExpires())
            .append("supportphone", getSupportPhone())
            .append("maintcontract", getMaintcontract())
            .append("maintcontractexpires", getMaintContractExpiration())
            .append("displaycategory", getDisplayCategory())
            .append("notifycategory", getNotifyCategory())
            .append("pollercategory", getPollerCategory())
            .append("thresholdcategory", getThresholdCategory())
            .append("comment", getComment())
            .append("cpu", getCpu())
            .append("ram", getRam())
            .append("storagectrl", getStoragectrl())
            .append("hdd1", getHdd1())
            .append("hdd2", getHdd2())
            .append("hdd3", getHdd3())
            .append("hdd4", getHdd4())
            .append("hdd5", getHdd5())
            .append("hdd6", getHdd6())
            .append("numpowersupplies", getNumpowersupplies())
            .append("inputpower", getInputpower())
            .append("additionalhardware", getAdditionalhardware())
            .append("admin", getAdmin())
            .append("snmpcommunity", getSnmpcommunity())
            .toString();
    }
}
