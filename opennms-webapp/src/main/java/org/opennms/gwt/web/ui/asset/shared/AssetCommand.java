/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.gwt.web.ui.asset.shared;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class AssetCommand.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a> <br/>
 *         Command object for asset pages. Contains fields from OnmsAsset and
 *         additional values needed to show complete ui.
 */
public class AssetCommand implements IsSerializable {

    /** The m_additionalhardware. */
    private String m_additionalhardware;

    /** The m_address1. */
    private String m_address1;

    /** The m_address2. */
    private String m_address2;

    /** The m_admin. */
    private String m_admin;

    /** The m_allow modify. */
    private boolean m_allowModify;

    /** The m_asset number. */
    private String m_assetNumber;

    /** The m_autoenable. */
    private String m_autoenable;

    /** The m_autoenable options. */
    private ArrayList<String> m_autoenableOptions;

    /** The m_building. */
    private String m_building;

    /** The m_category. */
    private String m_category;

    /** The m_circuit id. */
    private String m_circuitId;

    /** The m_city. */
    private String m_city;

    /** The m_comment. */
    private String m_comment;

    /** The m_connection. */
    private String m_connection;

    /** The m_connection options. */
    private ArrayList<String> m_connectionOptions;

    /** The m_longitude. */
    private Float m_longitude;

    /** The m_latitude. */
    private Float m_latitude;

    /** The m_country. */
    private String m_country;

    /** The m_cpu. */
    private String m_cpu;

    /** The m_date installed. */
    private String m_dateInstalled;

    /** The m_department. */
    private String m_department;

    /** The m_description. */
    private String m_description;

    /** The m_display category. */
    private String m_displayCategory;

    /** The m_division. */
    private String m_division;

    /** The m_enable. */
    private String m_enable;

    /** The m_floor. */
    private String m_floor;

    /** The m_hdd1. */
    private String m_hdd1;

    /** The m_hdd2. */
    private String m_hdd2;

    /** The m_hdd3. */
    private String m_hdd3;

    /** The m_hdd4. */
    private String m_hdd4;

    /** The m_hdd5. */
    private String m_hdd5;

    /** The m_hdd6. */
    private String m_hdd6;

    /** The m_id. */
    private Integer m_id;

    /** The m_inputpower. */
    private String m_inputpower;

    /** The m_last modified by. */
    private String m_lastModifiedBy;

    /** The m_last modified date. */
    private Date m_lastModifiedDate;

    /** The m_lease. */
    private String m_lease;

    /** The m_lease expires. */
    private String m_leaseExpires;

    /** The m_logged in user. */
    private String m_loggedInUser;

    /** The m_maintcontract. */
    private String m_maintcontract;

    /** The m_maint contract expiration. */
    private String m_maintContractExpiration;

    /** The m_manufacturer. */
    private String m_manufacturer;

    /** The m_model number. */
    private String m_modelNumber;

    /** The m_next node id. */
    private Integer m_nextNodeId;

    /** The m_node id. */
    private String m_nodeId;

    /** The m_node label. */
    private String m_nodeLabel;

    /** The m_notify category. */
    private String m_notifyCategory;

    /** The m_numpowersupplies. */
    private String m_numpowersupplies;

    /** The m_operating system. */
    private String m_operatingSystem;

    /** The m_password. */
    private String m_password;

    /** The m_poller category. */
    private String m_pollerCategory;

    /** The m_port. */
    private String m_port;

    /** The m_previous node id. */
    private Integer m_previousNodeId;

    /** The m_rack. */
    private String m_rack;

    /** The m_rackunitheight. */
    private String m_rackunitheight;

    /** The m_ram. */
    private String m_ram;

    /** The m_region. */
    private String m_region;

    /** The m_room. */
    private String m_room;

    /** The m_serial number. */
    private String m_serialNumber;

    /** The m_slot. */
    private String m_slot;

    /** The m_snmpcommunity. */
    private String m_snmpcommunity;

    /** The m_snmp sys contact. */
    private String m_snmpSysContact;

    /** The m_snmp sys description. */
    private String m_snmpSysDescription;

    /** The m_snmp sys location. */
    private String m_snmpSysLocation;

    /** The m_snmp sys name. */
    private String m_snmpSysName;

    /** The m_snmp sys object id. */
    private String m_snmpSysObjectId;

    /** The m_state. */
    private String m_state;

    /** The m_storagectrl. */
    private String m_storagectrl;

    /** The m_support phone. */
    private String m_supportPhone;

    /** The m_threshold category. */
    private String m_thresholdCategory;

    /** The m_username. */
    private String m_username;

    /** The m_vendor. */
    private String m_vendor;

    /** The m_vendor asset number. */
    private String m_vendorAssetNumber;

    /** The m_vendor fax. */
    private String m_vendorFax;

    /** The m_vendor phone. */
    private String m_vendorPhone;

    /** The m_zip. */
    private String m_zip;

    /** The m_vmware managed object id. */
    private String m_vmwareManagedObjectId;

    /** The m_vmware managed entity type. */
    private String m_vmwareManagedEntityType;

    /** The m_vmware management server. */
    private String m_vmwareManagementServer;

    /** The m_vmware topology info. */
    private String m_vmwareTopologyInfo;

    /** The m_vmware state. */
    private String m_vmwareState;

    /**
     * Instantiates a new asset command.
     */
    public AssetCommand() {
        m_autoenableOptions = new ArrayList<String>();
        m_connectionOptions = new ArrayList<String>();
        m_lastModifiedDate = new Date();
    }

    // --- Getter ---

    /**
     * Gets the additionalhardware.
     *
     * @return the additionalhardware
     */
    public String getAdditionalhardware() {
        return m_additionalhardware;
    }

    /**
     * Gets the address1.
     *
     * @return the address1
     */
    public String getAddress1() {
        return m_address1;
    }

    /**
     * Gets the address2.
     *
     * @return the address2
     */
    public String getAddress2() {
        return m_address2;
    }

    /**
     * Gets the admin.
     *
     * @return the admin
     */
    public String getAdmin() {
        return m_admin;
    }

    /**
     * Gets the allow modify.
     *
     * @return the allow modify
     */
    public boolean getAllowModify() {
        return m_allowModify;
    }

    /**
     * Gets the asset number.
     *
     * @return the asset number
     */
    public String getAssetNumber() {
        return m_assetNumber;
    }

    /**
     * Gets the autoenable.
     *
     * @return the autoenable
     */
    public String getAutoenable() {
        return m_autoenable;
    }

    /**
     * Gets the autoenable options.
     *
     * @return the autoenable options
     */
    public ArrayList<String> getAutoenableOptions() {
        return m_autoenableOptions;
    }

    /**
     * Gets the building.
     *
     * @return the building
     */
    public String getBuilding() {
        return m_building;
    }

    /**
     * Gets the category.
     *
     * @return the category
     */
    public String getCategory() {
        return m_category;
    }

    /**
     * Gets the circuit id.
     *
     * @return the circuit id
     */
    public String getCircuitId() {
        return m_circuitId;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() {
        return m_city;
    }

    /**
     * Gets the comment.
     *
     * @return the comment
     */
    public String getComment() {
        return m_comment;
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     */
    public String getConnection() {
        return m_connection;
    }

    /**
     * Gets the connection options.
     *
     * @return the connection options
     */
    public ArrayList<String> getConnectionOptions() {
        return m_connectionOptions;
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public Float getLongitude() {
        return m_longitude;
    }

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public Float getLatitude() {
        return m_latitude;
    }

    /**
     * Gets the country.
     *
     * @return the country
     */
    public String getCountry() {
        return m_country;
    }

    /**
     * Gets the cpu.
     *
     * @return the cpu
     */
    public String getCpu() {
        return m_cpu;
    }

    /**
     * Gets the date installed.
     *
     * @return the date installed
     */
    public String getDateInstalled() {
        return m_dateInstalled;
    }

    /**
     * Gets the department.
     *
     * @return the department
     */
    public String getDepartment() {
        return m_department;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return m_description;
    }

    /**
     * Gets the display category.
     *
     * @return the display category
     */
    public String getDisplayCategory() {
        return m_displayCategory;
    }

    /**
     * Gets the division.
     *
     * @return the division
     */
    public String getDivision() {
        return m_division;
    }

    /**
     * Gets the enable.
     *
     * @return the enable
     */
    public String getEnable() {
        return m_enable;
    }

    /**
     * Gets the floor.
     *
     * @return the floor
     */
    public String getFloor() {
        return m_floor;
    }

    /**
     * Gets the hdd1.
     *
     * @return the hdd1
     */
    public String getHdd1() {
        return m_hdd1;
    }

    /**
     * Gets the hdd2.
     *
     * @return the hdd2
     */
    public String getHdd2() {
        return m_hdd2;
    }

    /**
     * Gets the hdd3.
     *
     * @return the hdd3
     */
    public String getHdd3() {
        return m_hdd3;
    }

    /**
     * Gets the hdd4.
     *
     * @return the hdd4
     */
    public String getHdd4() {
        return m_hdd4;
    }

    /**
     * Gets the hdd5.
     *
     * @return the hdd5
     */
    public String getHdd5() {
        return m_hdd5;
    }

    /**
     * Gets the hdd6.
     *
     * @return the hdd6
     */
    public String getHdd6() {
        return m_hdd6;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return m_id;
    }

    /**
     * Gets the inputpower.
     *
     * @return the inputpower
     */
    public String getInputpower() {
        return m_inputpower;
    }

    /**
     * Gets the last modified by.
     *
     * @return the last modified by
     */
    public String getLastModifiedBy() {
        return m_lastModifiedBy;
    }

    /**
     * Gets the last modified date.
     *
     * @return the last modified date
     */
    public Date getLastModifiedDate() {
        return m_lastModifiedDate;
    }

    /**
     * Gets the lease.
     *
     * @return the lease
     */
    public String getLease() {
        return m_lease;
    }

    /**
     * Gets the lease expires.
     *
     * @return the lease expires
     */
    public String getLeaseExpires() {
        return m_leaseExpires;
    }

    /**
     * Gets the logged in user.
     *
     * @return the logged in user
     */
    public String getLoggedInUser() {
        return m_loggedInUser;
    }

    /**
     * Gets the maintcontract.
     *
     * @return the maintcontract
     */
    public String getMaintcontract() {
        return m_maintcontract;
    }

    /**
     * Gets the maint contract expiration.
     *
     * @return the maint contract expiration
     */
    public String getMaintContractExpiration() {
        return m_maintContractExpiration;
    }

    /**
     * Gets the manufacturer.
     *
     * @return the manufacturer
     */
    public String getManufacturer() {
        return m_manufacturer;
    }

    /**
     * Gets the model number.
     *
     * @return the model number
     */
    public String getModelNumber() {
        return m_modelNumber;
    }

    /**
     * Gets the next node id.
     *
     * @return the next node id
     */
    public Integer getNextNodeId() {
        return m_nextNodeId;
    }

    /**
     * Gets the node id.
     *
     * @return the node id
     */
    public String getNodeId() {
        return m_nodeId;
    }

    /**
     * Gets the node label.
     *
     * @return the node label
     */
    public String getNodeLabel() {
        return m_nodeLabel;
    }

    /**
     * Gets the notify category.
     *
     * @return the notify category
     */
    public String getNotifyCategory() {
        return m_notifyCategory;
    }

    /**
     * Gets the numpowersupplies.
     *
     * @return the numpowersupplies
     */
    public String getNumpowersupplies() {
        return m_numpowersupplies;
    }

    /**
     * Gets the operating system.
     *
     * @return the operating system
     */
    public String getOperatingSystem() {
        return m_operatingSystem;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return m_password;
    }

    /**
     * Gets the poller category.
     *
     * @return the poller category
     */
    public String getPollerCategory() {
        return m_pollerCategory;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public String getPort() {
        return m_port;
    }

    /**
     * Gets the previous node id.
     *
     * @return the previous node id
     */
    public Integer getPreviousNodeId() {
        return m_previousNodeId;
    }

    /**
     * Gets the rack.
     *
     * @return the rack
     */
    public String getRack() {
        return m_rack;
    }

    /**
     * Gets the rackunitheight.
     *
     * @return the rackunitheight
     */
    public String getRackunitheight() {
        return m_rackunitheight;
    }

    /**
     * Gets the ram.
     *
     * @return the ram
     */
    public String getRam() {
        return m_ram;
    }

    /**
     * Gets the region.
     *
     * @return the region
     */
    public String getRegion() {
        return m_region;
    }

    /**
     * Gets the room.
     *
     * @return the room
     */
    public String getRoom() {
        return m_room;
    }

    /**
     * Gets the serial number.
     *
     * @return the serial number
     */
    public String getSerialNumber() {
        return m_serialNumber;
    }

    /**
     * Gets the slot.
     *
     * @return the slot
     */
    public String getSlot() {
        return m_slot;
    }

    /**
     * Gets the snmpcommunity.
     *
     * @return the snmpcommunity
     */
    public String getSnmpcommunity() {
        return m_snmpcommunity;
    }

    /**
     * Gets the snmp sys contact.
     *
     * @return the snmp sys contact
     */
    public String getSnmpSysContact() {
        return m_snmpSysContact;
    }

    /**
     * Gets the snmp sys description.
     *
     * @return the snmp sys description
     */
    public String getSnmpSysDescription() {
        return m_snmpSysDescription;
    }

    /**
     * Gets the snmp sys location.
     *
     * @return the snmp sys location
     */
    public String getSnmpSysLocation() {
        return m_snmpSysLocation;
    }

    /**
     * Gets the snmp sys name.
     *
     * @return the snmp sys name
     */
    public String getSnmpSysName() {
        return m_snmpSysName;
    }

    /**
     * Gets the snmp sys object id.
     *
     * @return the snmp sys object id
     */
    public String getSnmpSysObjectId() {
        return m_snmpSysObjectId;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public String getState() {
        return m_state;
    }

    /**
     * Gets the storagectrl.
     *
     * @return the storagectrl
     */
    public String getStoragectrl() {
        return m_storagectrl;
    }

    /**
     * Gets the support phone.
     *
     * @return the support phone
     */
    public String getSupportPhone() {
        return m_supportPhone;
    }

    /**
     * Gets the threshold category.
     *
     * @return the threshold category
     */
    public String getThresholdCategory() {
        return m_thresholdCategory;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return m_username;
    }

    /**
     * Gets the vendor.
     *
     * @return the vendor
     */
    public String getVendor() {
        return m_vendor;
    }

    /**
     * Gets the vendor asset number.
     *
     * @return the vendor asset number
     */
    public String getVendorAssetNumber() {
        return m_vendorAssetNumber;
    }

    /**
     * Gets the vendor fax.
     *
     * @return the vendor fax
     */
    public String getVendorFax() {
        return m_vendorFax;
    }

    /**
     * Gets the vendor phone.
     *
     * @return the vendor phone
     */
    public String getVendorPhone() {
        return m_vendorPhone;
    }

    /**
     * Gets the zip.
     *
     * @return the zip
     */
    public String getZip() {
        return m_zip;
    }

    /**
     * Gets the vmware managed object id.
     *
     * @return the vmware managed object id
     */
    public String getVmwareManagedObjectId() {
        return m_vmwareManagedObjectId;
    }

    /**
     * Gets the vmware managed entity type.
     *
     * @return the vmware managed entity type
     */
    public String getVmwareManagedEntityType() {
        return m_vmwareManagedEntityType;
    }

    /**
     * Gets the vmware management server.
     *
     * @return the vmware management server
     */
    public String getVmwareManagementServer() {
        return m_vmwareManagementServer;
    }

    /**
     * Gets the vmware topology info.
     *
     * @return the vmware topology info
     */
    public String getVmwareTopologyInfo() {
        return m_vmwareTopologyInfo;
    }

    /**
     * Gets the vmware state.
     *
     * @return the vmware state
     */
    public String getVmwareState() {
        return m_vmwareState;
    }

    // --- Setter ---

    /**
     * Sets the additionalhardware.
     *
     * @param additionalhardware
     *            the new additionalhardware
     */
    public void setAdditionalhardware(String additionalhardware) {
        m_additionalhardware = additionalhardware;
    }

    /**
     * Sets the address1.
     *
     * @param address1
     *            the new address1
     */
    public void setAddress1(String address1) {
        m_address1 = address1;
    }

    /**
     * Sets the address2.
     *
     * @param address2
     *            the new address2
     */
    public void setAddress2(String address2) {
        m_address2 = address2;
    }

    /**
     * Sets the admin.
     *
     * @param admin
     *            the new admin
     */
    public void setAdmin(String admin) {
        m_admin = admin;
    }

    /**
     * Sets the allow modify.
     *
     * @param m_allowModify
     *            the new allow modify
     */
    public void setAllowModify(boolean m_allowModify) {
        this.m_allowModify = m_allowModify;
    }

    /**
     * Sets the asset number.
     *
     * @param assetNumber
     *            the new asset number
     */
    public void setAssetNumber(String assetNumber) {
        m_assetNumber = assetNumber;
    }

    /**
     * Sets the autoenable.
     *
     * @param autoenable
     *            the new autoenable
     */
    public void setAutoenable(String autoenable) {
        m_autoenable = autoenable;
    }

    /**
     * Sets the autoenable options.
     *
     * @param autoenableOptions
     *            the new autoenable options
     */
    public void setAutoenableOptions(ArrayList<String> autoenableOptions) {
        m_autoenableOptions = autoenableOptions;
    }

    /**
     * Sets the building.
     *
     * @param building
     *            the new building
     */
    public void setBuilding(String building) {
        m_building = building;
    }

    /**
     * Sets the category.
     *
     * @param category
     *            the new category
     */
    public void setCategory(String category) {
        m_category = category;
    }

    /**
     * Sets the circuit id.
     *
     * @param circuitId
     *            the new circuit id
     */
    public void setCircuitId(String circuitId) {
        m_circuitId = circuitId;
    }

    /**
     * Sets the city.
     *
     * @param city
     *            the new city
     */
    public void setCity(String city) {
        m_city = city;
    }

    /**
     * Sets the comment.
     *
     * @param comment
     *            the new comment
     */
    public void setComment(String comment) {
        m_comment = comment;
    }

    /**
     * Sets the connection.
     *
     * @param connection
     *            the new connection
     */
    public void setConnection(String connection) {
        m_connection = connection;
    }

    /**
     * Sets the connection options.
     *
     * @param connectionOptions
     *            the new connection options
     */
    public void setConnectionOptions(ArrayList<String> connectionOptions) {
        m_connectionOptions = connectionOptions;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude
     *            the new longitude
     */
    public void setLongitude(Float longitude) {
        m_longitude = longitude;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude
     *            the new latitude
     */
    public void setLatitude(Float latitude) {
        m_latitude = latitude;
    }

    /**
     * Sets the country.
     *
     * @param country
     *            the new country
     */
    public void setCountry(String country) {
        m_country = country;
    }

    /**
     * Sets the cpu.
     *
     * @param cpu
     *            the new cpu
     */
    public void setCpu(String cpu) {
        m_cpu = cpu;
    }

    /**
     * Sets the date installed.
     *
     * @param dateInstalled
     *            the new date installed
     */
    public void setDateInstalled(String dateInstalled) {
        m_dateInstalled = dateInstalled;
    }

    /**
     * Sets the department.
     *
     * @param department
     *            the new department
     */
    public void setDepartment(String department) {
        m_department = department;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(String description) {
        m_description = description;
    }

    /**
     * Sets the display category.
     *
     * @param displayCategory
     *            the new display category
     */
    public void setDisplayCategory(String displayCategory) {
        m_displayCategory = displayCategory;
    }

    /**
     * Sets the division.
     *
     * @param division
     *            the new division
     */
    public void setDivision(String division) {
        m_division = division;
    }

    /**
     * Sets the enable.
     *
     * @param enable
     *            the new enable
     */
    public void setEnable(String enable) {
        m_enable = enable;
    }

    /**
     * Sets the floor.
     *
     * @param floor
     *            the new floor
     */
    public void setFloor(String floor) {
        m_floor = floor;
    }

    /**
     * Sets the hdd1.
     *
     * @param hdd1
     *            the new hdd1
     */
    public void setHdd1(String hdd1) {
        m_hdd1 = hdd1;
    }

    /**
     * Sets the hdd2.
     *
     * @param hdd2
     *            the new hdd2
     */
    public void setHdd2(String hdd2) {
        m_hdd2 = hdd2;
    }

    /**
     * Sets the hdd3.
     *
     * @param hdd3
     *            the new hdd3
     */
    public void setHdd3(String hdd3) {
        m_hdd3 = hdd3;
    }

    /**
     * Sets the hdd4.
     *
     * @param hdd4
     *            the new hdd4
     */
    public void setHdd4(String hdd4) {
        m_hdd4 = hdd4;
    }

    /**
     * Sets the hdd5.
     *
     * @param hdd5
     *            the new hdd5
     */
    public void setHdd5(String hdd5) {
        m_hdd5 = hdd5;
    }

    /**
     * Sets the hdd6.
     *
     * @param hdd6
     *            the new hdd6
     */
    public void setHdd6(String hdd6) {
        m_hdd6 = hdd6;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(Integer id) {
        m_id = id;
    }

    /**
     * Sets the inputpower.
     *
     * @param inputpower
     *            the new inputpower
     */
    public void setInputpower(String inputpower) {
        m_inputpower = inputpower;
    }

    /**
     * Sets the last modified by.
     *
     * @param lastModifiedBy
     *            the new last modified by
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        m_lastModifiedBy = lastModifiedBy;
    }

    /**
     * Sets the last modified date.
     *
     * @param lastModifiedDate
     *            the new last modified date
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        m_lastModifiedDate = lastModifiedDate;
    }

    /**
     * Sets the lease.
     *
     * @param lease
     *            the new lease
     */
    public void setLease(String lease) {
        m_lease = lease;
    }

    /**
     * Sets the lease expires.
     *
     * @param leaseExpires
     *            the new lease expires
     */
    public void setLeaseExpires(String leaseExpires) {
        m_leaseExpires = leaseExpires;
    }

    /**
     * Sets the logged in user.
     *
     * @param m_loggedInUser
     *            the new logged in user
     */
    public void setLoggedInUser(String m_loggedInUser) {
        this.m_loggedInUser = m_loggedInUser;
    }

    /**
     * Sets the maintcontract.
     *
     * @param maintcontract
     *            the new maintcontract
     */
    public void setMaintcontract(String maintcontract) {
        m_maintcontract = maintcontract;
    }

    /**
     * Sets the maint contract expiration.
     *
     * @param maintContractExpiration
     *            the new maint contract expiration
     */
    public void setMaintContractExpiration(String maintContractExpiration) {
        m_maintContractExpiration = maintContractExpiration;
    }

    /**
     * Sets the manufacturer.
     *
     * @param manufacturer
     *            the new manufacturer
     */
    public void setManufacturer(String manufacturer) {
        m_manufacturer = manufacturer;
    }

    /**
     * Sets the model number.
     *
     * @param modelNumber
     *            the new model number
     */
    public void setModelNumber(String modelNumber) {
        m_modelNumber = modelNumber;
    }

    /**
     * Sets the next node id.
     *
     * @param m_nextNodeId
     *            the new next node id
     */
    public void setNextNodeId(Integer m_nextNodeId) {
        this.m_nextNodeId = m_nextNodeId;
    }

    /**
     * Sets the node id.
     *
     * @param m_nodeId
     *            the new node id
     */
    public void setNodeId(String m_nodeId) {
        this.m_nodeId = m_nodeId;
    }

    /**
     * Sets the node label.
     *
     * @param m_nodeLabel
     *            the new node label
     */
    public void setNodeLabel(String m_nodeLabel) {
        this.m_nodeLabel = m_nodeLabel;
    }

    /**
     * Sets the notify category.
     *
     * @param notifyCategory
     *            the new notify category
     */
    public void setNotifyCategory(String notifyCategory) {
        m_notifyCategory = notifyCategory;
    }

    /**
     * Sets the numpowersupplies.
     *
     * @param numpowersupplies
     *            the new numpowersupplies
     */
    public void setNumpowersupplies(String numpowersupplies) {
        m_numpowersupplies = numpowersupplies;
    }

    /**
     * Sets the operating system.
     *
     * @param operatingSystem
     *            the new operating system
     */
    public void setOperatingSystem(String operatingSystem) {
        m_operatingSystem = operatingSystem;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the new password
     */
    public void setPassword(String password) {
        m_password = password;
    }

    /**
     * Sets the poller category.
     *
     * @param pollerCategory
     *            the new poller category
     */
    public void setPollerCategory(String pollerCategory) {
        m_pollerCategory = pollerCategory;
    }

    /**
     * Sets the port.
     *
     * @param port
     *            the new port
     */
    public void setPort(String port) {
        m_port = port;
    }

    /**
     * Sets the previous node id.
     *
     * @param m_previousNodeId
     *            the new previous node id
     */
    public void setPreviousNodeId(Integer m_previousNodeId) {
        this.m_previousNodeId = m_previousNodeId;
    }

    /**
     * Sets the rack.
     *
     * @param rack
     *            the new rack
     */
    public void setRack(String rack) {
        m_rack = rack;
    }

    /**
     * Sets the rackunitheight.
     *
     * @param m_rackunitheight
     *            the new rackunitheight
     */
    public void setRackunitheight(String m_rackunitheight) {
        this.m_rackunitheight = m_rackunitheight;
    }

    /**
     * Sets the ram.
     *
     * @param ram
     *            the new ram
     */
    public void setRam(String ram) {
        m_ram = ram;
    }

    /**
     * Sets the region.
     *
     * @param region
     *            the new region
     */
    public void setRegion(String region) {
        m_region = region;
    }

    /**
     * Sets the room.
     *
     * @param room
     *            the new room
     */
    public void setRoom(String room) {
        m_room = room;
    }

    /**
     * Sets the serial number.
     *
     * @param serialNumber
     *            the new serial number
     */
    public void setSerialNumber(String serialNumber) {
        m_serialNumber = serialNumber;
    }

    /**
     * Sets the slot.
     *
     * @param slot
     *            the new slot
     */
    public void setSlot(String slot) {
        m_slot = slot;
    }

    /**
     * Sets the snmpcommunity.
     *
     * @param snmpcommunity
     *            the new snmpcommunity
     */
    public void setSnmpcommunity(String snmpcommunity) {
        m_snmpcommunity = snmpcommunity;
    }

    /**
     * Sets the snmp sys contact.
     *
     * @param snmpSysContact
     *            the new snmp sys contact
     */
    public void setSnmpSysContact(String snmpSysContact) {
        m_snmpSysContact = snmpSysContact;
    }

    /**
     * Sets the snmp sys description.
     *
     * @param snmpSysDescription
     *            the new snmp sys description
     */
    public void setSnmpSysDescription(String snmpSysDescription) {
        m_snmpSysDescription = snmpSysDescription;
    }

    /**
     * Sets the snmp sys location.
     *
     * @param snmpSysLocation
     *            the new snmp sys location
     */
    public void setSnmpSysLocation(String snmpSysLocation) {
        m_snmpSysLocation = snmpSysLocation;
    }

    /**
     * Sets the snmp sys name.
     *
     * @param snmpSysName
     *            the new snmp sys name
     */
    public void setSnmpSysName(String snmpSysName) {
        m_snmpSysName = snmpSysName;
    }

    /**
     * Sets the snmp sys object id.
     *
     * @param snmpSysObjectId
     *            the new snmp sys object id
     */
    public void setSnmpSysObjectId(String snmpSysObjectId) {
        m_snmpSysObjectId = snmpSysObjectId;
    }

    /**
     * Sets the state.
     *
     * @param state
     *            the new state
     */
    public void setState(String state) {
        m_state = state;
    }

    /**
     * Sets the storagectrl.
     *
     * @param storagectrl
     *            the new storagectrl
     */
    public void setStoragectrl(String storagectrl) {
        m_storagectrl = storagectrl;
    }

    /**
     * Sets the support phone.
     *
     * @param supportPhone
     *            the new support phone
     */
    public void setSupportPhone(String supportPhone) {
        m_supportPhone = supportPhone;
    }

    /**
     * Sets the threshold category.
     *
     * @param thresholdCategory
     *            the new threshold category
     */
    public void setThresholdCategory(String thresholdCategory) {
        m_thresholdCategory = thresholdCategory;
    }

    /**
     * Sets the username.
     *
     * @param username
     *            the new username
     */
    public void setUsername(String username) {
        m_username = username;
    }

    /**
     * Sets the vendor.
     *
     * @param vendor
     *            the new vendor
     */
    public void setVendor(String vendor) {
        m_vendor = vendor;
    }

    /**
     * Sets the vendor asset number.
     *
     * @param vendorAssetNumber
     *            the new vendor asset number
     */
    public void setVendorAssetNumber(String vendorAssetNumber) {
        m_vendorAssetNumber = vendorAssetNumber;
    }

    /**
     * Sets the vendor fax.
     *
     * @param vendorFax
     *            the new vendor fax
     */
    public void setVendorFax(String vendorFax) {
        m_vendorFax = vendorFax;
    }

    /**
     * Sets the vendor phone.
     *
     * @param vendorPhone
     *            the new vendor phone
     */
    public void setVendorPhone(String vendorPhone) {
        m_vendorPhone = vendorPhone;
    }

    /**
     * Sets the zip.
     *
     * @param zip
     *            the new zip
     */
    public void setZip(String zip) {
        m_zip = zip;
    }

    /**
     * Sets the vmware managed object id.
     *
     * @param vmwareManagedObjectId
     *            the new vmware managed object id
     */
    public void setVmwareManagedObjectId(String vmwareManagedObjectId) {
        m_vmwareManagedObjectId = vmwareManagedObjectId;
    }

    /**
     * Sets the vmware managed entity type.
     *
     * @param vmwareManagedEntityType
     *            the new vmware managed entity type
     */
    public void setVmwareManagedEntityType(String vmwareManagedEntityType) {
        m_vmwareManagedEntityType = vmwareManagedEntityType;
    }

    /**
     * Sets the vmware management server.
     *
     * @param vmwareManagementServer
     *            the new vmware management server
     */
    public void setVmwareManagementServer(String vmwareManagementServer) {
        m_vmwareManagementServer = vmwareManagementServer;
    }

    /**
     * Sets the vmware topology info.
     *
     * @param vmwareTopologyInfo
     *            the new vmware topology info
     */
    public void setVmwareTopologyInfo(String vmwareTopologyInfo) {
        m_vmwareTopologyInfo = vmwareTopologyInfo;
    }

    /**
     * Sets the vmware state.
     *
     * @param vmwareState
     *            the new vmware state
     */
    public void setVmwareState(String vmwareState) {
        m_vmwareState = vmwareState;
    }

    // --- nice toString() ---
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AssetCommand [m_additionalhardware=" + m_additionalhardware + ", m_address1=" + m_address1
                + ", m_address2=" + m_address2 + ", m_admin=" + m_admin + ", m_allowModify=" + m_allowModify
                + ", m_assetNumber=" + m_assetNumber + ", m_autoenable=" + m_autoenable + ", m_autoenableOptions="
                + m_autoenableOptions + ", m_building=" + m_building + ", m_category=" + m_category + ", m_circuitId="
                + m_circuitId + ", m_city=" + m_city + ", m_longitude=" + m_longitude + ", m_latitude=" + m_latitude
                + ", m_country=" + m_country + ", m_comment=" + m_comment + ", m_connection=" + m_connection
                + ", m_connectionOptions=" + m_connectionOptions + ", m_cpu=" + m_cpu + ", m_dateInstalled="
                + m_dateInstalled + ", m_department=" + m_department + ", m_description=" + m_description
                + ", m_displayCategory=" + m_displayCategory + ", m_division=" + m_division + ", m_enable=" + m_enable
                + ", m_floor=" + m_floor + ", m_country=" + m_country + ", m_hdd1=" + m_hdd1 + ", m_hdd2=" + m_hdd2
                + ", m_hdd3=" + m_hdd3 + ", m_hdd4=" + m_hdd4 + ", m_hdd5=" + m_hdd5 + ", m_hdd6=" + m_hdd6 + ", m_id="
                + m_id + ", m_inputpower=" + m_inputpower + ", m_lastModifiedBy=" + m_lastModifiedBy
                + ", m_lastModifiedDate=" + m_lastModifiedDate + ", m_lease=" + m_lease + ", m_leaseExpires="
                + m_leaseExpires + ", m_loggedInUser=" + m_loggedInUser + ", m_maintcontract=" + m_maintcontract
                + ", m_maintContractExpiration=" + m_maintContractExpiration + ", m_manufacturer=" + m_manufacturer
                + ", m_modelNumber=" + m_modelNumber + ", m_nextNodeId=" + m_nextNodeId + ", m_nodeId=" + m_nodeId
                + ", m_nodeLabel=" + m_nodeLabel + ", m_notifyCategory=" + m_notifyCategory + ", m_numpowersupplies="
                + m_numpowersupplies + ", m_operatingSystem=" + m_operatingSystem + ", m_password=" + m_password
                + ", m_pollerCategory=" + m_pollerCategory + ", m_port=" + m_port + ", m_previousNodeId="
                + m_previousNodeId + ", m_rack=" + m_rack + ", m_rackunitheight=" + m_rackunitheight + ", m_ram="
                + m_ram + ", m_region=" + m_region + ", m_room=" + m_room + ", m_serialNumber=" + m_serialNumber
                + ", m_slot=" + m_slot + ", m_snmpcommunity=" + m_snmpcommunity + ", m_snmpSysContact="
                + m_snmpSysContact + ", m_snmpSysDescription=" + m_snmpSysDescription + ", m_snmpSysLocation="
                + m_snmpSysLocation + ", m_snmpSysName=" + m_snmpSysName + ", m_snmpSysObjectId=" + m_snmpSysObjectId
                + ", m_state=" + m_state + ", m_storagectrl=" + m_storagectrl + ", m_supportPhone=" + m_supportPhone
                + ", m_thresholdCategory=" + m_thresholdCategory + ", m_username=" + m_username + ", m_vendor="
                + m_vendor + ", m_vendorAssetNumber=" + m_vendorAssetNumber + ", m_vendorFax=" + m_vendorFax
                + ", m_vendorPhone=" + m_vendorPhone + ", m_zip=" + m_zip + ", m_vmwareManagedObjectId="
                + m_vmwareManagedObjectId + ", m_vmwareManagedEntityType=" + m_vmwareManagedEntityType
                + ", m_vmwareManagementServer=" + m_vmwareManagementServer + ", m_vmwareTopologyInfo="
                + m_vmwareTopologyInfo + ", m_vmwareState=" + m_vmwareState + "]";
    }
}
