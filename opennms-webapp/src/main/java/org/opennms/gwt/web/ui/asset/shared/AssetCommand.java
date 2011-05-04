/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2011 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *      OpenNMS Licensing       <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 */

package org.opennms.gwt.web.ui.asset.shared;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public class AssetCommand implements IsSerializable {
	
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
	private ArrayList<String> m_autoenableOptions;

	/** identifier field */
	private String m_building;

	/** identifier field */
	private String m_category;

	/** identifier field */
	private String m_circuitId;

	/** identifier field */
	private String m_city;

	/** identifier field */
	private String m_comment;

	/** identifier field */
	private String m_connection;
    
    /** identifier field */
	private ArrayList<String> m_connectionOptions;

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

	/** identifier field */
	private Integer m_id;

	/** identifier field */
	private String m_inputpower;

	/** identifier field */
	private String m_lastModifiedBy;

    /** identifier field */
    private Date m_lastModifiedDate;

	/** identifier field */
	private String m_lease;

	/** identifier field */
	private String m_leaseExpires;

	/** identifier field */
	private String m_loggedInUser;

	/** identifier field */
	private String m_maintContractExpiration;

	/** identifier field */
	private String m_maintContractNumber;

	/** identifier field */
	private String m_manufacturer;

	/** identifier field */
	private String m_modelNumber;

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
	private String m_snmpSysContact;
	
	/** identifier field */	
	private String m_snmpSysDescription;

	/** identifier field */	
	private String m_snmpSysLocation;
	
	/** identifier field */	
	private String m_snmpSysName;
	
	/** identifier field */	
	private String m_snmpSysObjectId;

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

    public AssetCommand () {
    	this.m_autoenableOptions = new ArrayList<String>();
    	this.m_connectionOptions = new ArrayList<String>();
    	this.m_lastModifiedDate = new Date();
    }

	public String getAdditionalhardware() {
		return m_additionalhardware;
	}

	public String getAddress1() {
		return m_address1;
	}

	public String getAddress2() {
		return m_address2;
	}

	public String getAdmin() {
		return m_admin;
	}

	public String getAssetNumber() {
		return m_assetNumber;
	}

	public String getAutoenable() {
		return m_autoenable;
	}

	public ArrayList<String> getAutoenableOptions () {
		return m_autoenableOptions;
	}

	public String getBuilding() {
		return m_building;
	}

	public String getCategory() {
		return m_category;
	}

	public String getCircuitId() {
		return m_circuitId;
	}

	public String getCity() {
		return m_city;
	}

	public String getComment() {
		return m_comment;
	}

	public String getConnection() {
		return m_connection;
	}

	public ArrayList<String> getConnectionOptions () {
		return m_connectionOptions;
	}
	
	public String getCpu() {
		return m_cpu;
	}
	
	public String getDateInstalled() {
		return m_dateInstalled;
	}

	public String getDepartment() {
		return m_department;
	}

	public String getDescription() {
		return m_description;
	}

	public String getDisplayCategory() {
		return m_displayCategory;
	}

	public String getDivision() {
		return m_division;
	}

	public String getEnable() {
		return m_enable;
	}

	public String getFloor() {
		return m_floor;
	}

	public String getHdd1() {
		return m_hdd1;
	}

	public String getHdd2() {
		return m_hdd2;
	}

	public String getHdd3() {
		return m_hdd3;
	}

	public String getHdd4() {
		return m_hdd4;
	}

	public String getHdd5() {
		return m_hdd5;
	}

	public String getHdd6() {
		return m_hdd6;
	}
	
	public Integer getId() {
		return m_id;
	}

	public String getInputpower() {
		return m_inputpower;
	}
	
	public String getLastModifiedBy() {
		return m_lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return m_lastModifiedDate;
	}

	public String getLease() {
		return m_lease;
	}

	public String getLeaseExpires() {
		return m_leaseExpires;
	}

	public String getLoggedInUser() {
		return m_loggedInUser;
	}

	public String getMaintContractExpiration() {
		return m_maintContractExpiration;
	}

	public String getMaintContractNumber() {
		return m_maintContractNumber;
	}

	public String getManufacturer() {
		return m_manufacturer;
	}

	public String getModelNumber() {
		return m_modelNumber;
	}

	public String getNotifyCategory() {
		return m_notifyCategory;
	}

	public String getNumpowersupplies() {
		return m_numpowersupplies;
	}

	public String getOperatingSystem() {
		return m_operatingSystem;
	}

	public String getPassword() {
		return m_password;
	}

	public String getPollerCategory() {
		return m_pollerCategory;
	}

	public String getPort() {
		return m_port;
	}

	public String getRack() {
		return m_rack;
	}

	public String getRam() {
		return m_ram;
	}

	public String getRegion() {
		return m_region;
	}

	public String getRoom() {
		return m_room;
	}

	public String getSerialNumber() {
		return m_serialNumber;
	}

	public String getSlot() {
		return m_slot;
	}

	public String getSnmpcommunity() {
		return m_snmpcommunity;
	}

	public String getSnmpSysContact() {
		return m_snmpSysContact;
	}

	public String getSnmpSysDescription() {
		return m_snmpSysDescription;
	}

	public String getSnmpSysLocation() {
		return m_snmpSysLocation;
	}

	public String getSnmpSysName() {
		return m_snmpSysName;
	}

	public String getSnmpSysObjectId() {
		return m_snmpSysObjectId;
	}

	public String getState() {
		return m_state;
	}

	public String getStoragectrl() {
		return m_storagectrl;
	}

	public String getSupportPhone() {
		return m_supportPhone;
	}

	public String getThresholdCategory() {
		return m_thresholdCategory;
	}

	public String getUsername() {
		return m_username;
	}

	public String getVendor() {
		return m_vendor;
	}

	public String getVendorAssetNumber() {
		return m_vendorAssetNumber;
	}

	public String getVendorFax() {
		return m_vendorFax;
	}

	public String getVendorPhone() {
		return m_vendorPhone;
	}

	public String getZip() {
		return m_zip;
	}

	public void setAdditionalhardware(String additionalhardware) {
		this.m_additionalhardware = additionalhardware;
	}

	public void setAddress1(String address1) {
		this.m_address1 = address1;
	}

	public void setAddress2(String address2) {
		this.m_address2 = address2;
	}

	public void setAdmin(String admin) {
		this.m_admin = admin;
	}

	public void setAssetNumber(String assetNumber) {
		this.m_assetNumber = assetNumber;
	}

	public void setAutoenable(String autoenable) {
		this.m_autoenable = autoenable;
	}

	public void setAutoenableOptions (ArrayList<String> autoenableOptions) {
		m_autoenableOptions = autoenableOptions;
	}

	public void setBuilding(String building) {
		this.m_building = building;
	}

	public void setCategory(String category) {
		this.m_category = category;
	}

	public void setCircuitId(String circuitId) {
		this.m_circuitId = circuitId;
	}

	public void setCity(String city) {
		this.m_city = city;
	}

	public void setComment(String comment) {
		this.m_comment = comment;
	}

	public void setConnection(String connection) {
		this.m_connection = connection;
	}

	public void setConnectionOptions(ArrayList<String> connectionOptions) {
		m_connectionOptions = connectionOptions;
	}

	public void setCpu(String cpu) {
		this.m_cpu = cpu;
	}

	public void setDateInstalled(String dateInstalled) {
		this.m_dateInstalled = dateInstalled;
	}

	public void setDepartment(String department) {
		this.m_department = department;
	}

	public void setDescription(String description) {
		this.m_description = description;
	}

	public void setDisplayCategory(String displayCategory) {
		this.m_displayCategory = displayCategory;
	}

	public void setDivision(String division) {
		this.m_division = division;
	}

	public void setEnable(String enable) {
		this.m_enable = enable;
	}

	public void setFloor(String floor) {
		this.m_floor = floor;
	}

	public void setHdd1(String hdd1) {
		this.m_hdd1 = hdd1;
	}

	public void setHdd2(String hdd2) {
		this.m_hdd2 = hdd2;
	}

	public void setHdd3(String hdd3) {
		this.m_hdd3 = hdd3;
	}

	public void setHdd4(String hdd4) {
		this.m_hdd4 = hdd4;
	}

	public void setHdd5(String hdd5) {
		this.m_hdd5 = hdd5;
	}

	public void setHdd6(String hdd6) {
		this.m_hdd6 = hdd6;
	}

	public void setId(Integer id) {
		this.m_id = id;
	}

	public void setInputpower(String inputpower) {
		this.m_inputpower = inputpower;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.m_lastModifiedBy = lastModifiedBy;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.m_lastModifiedDate = lastModifiedDate;
	}

	public void setLease(String lease) {
		this.m_lease = lease;
	}

	public void setLeaseExpires(String leaseExpires) {
		this.m_leaseExpires = leaseExpires;
	}

	public void setLoggedInUser(String m_loggedInUser) {
		this.m_loggedInUser = m_loggedInUser;
	}

	public void setMaintContractExpiration(String maintContractExpiration) {
		this.m_maintContractExpiration = maintContractExpiration;
	}

	public void setMaintContractNumber(String maintContractNumber) {
		this.m_maintContractNumber = maintContractNumber;
	}

	public void setManufacturer(String manufacturer) {
		this.m_manufacturer = manufacturer;
	}

	public void setModelNumber(String modelNumber) {
		this.m_modelNumber = modelNumber;
	}

	public void setNotifyCategory(String notifyCategory) {
		this.m_notifyCategory = notifyCategory;
	}

	public void setNumpowersupplies(String numpowersupplies) {
		this.m_numpowersupplies = numpowersupplies;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.m_operatingSystem = operatingSystem;
	}

	public void setPassword(String password) {
		this.m_password = password;
	}

	public void setPollerCategory(String pollerCategory) {
		this.m_pollerCategory = pollerCategory;
	}

	public void setPort(String port) {
		this.m_port = port;
	}

	public void setRack(String rack) {
		this.m_rack = rack;
	}

	public void setRam(String ram) {
		this.m_ram = ram;
	}

	public void setRegion(String region) {
		this.m_region = region;
	}

	public void setRoom(String room) {
		this.m_room = room;
	}

	public void setSerialNumber(String serialNumber) {
		this.m_serialNumber = serialNumber;
	}

	public void setSlot(String slot) {
		this.m_slot = slot;
	}

	public void setSnmpcommunity(String snmpcommunity) {
		this.m_snmpcommunity = snmpcommunity;
	}

	public void setSnmpSysContact(String snmpSysContact) {
		this.m_snmpSysContact = snmpSysContact;
	}

	public void setSnmpSysDescription(String snmpSysDescription) {
		this.m_snmpSysDescription = snmpSysDescription;
	}

	public void setSnmpSysLocation(String snmpSysLocation) {
		this.m_snmpSysLocation = snmpSysLocation;
	}

	public void setSnmpSysName(String snmpSysName) {
		this.m_snmpSysName = snmpSysName;
	}

	public void setSnmpSysObjectId(String snmpSysObjectId) {
		this.m_snmpSysObjectId = snmpSysObjectId;
	}

	public void setState(String state) {
		this.m_state = state;
	}

	public void setStoragectrl(String storagectrl) {
		this.m_storagectrl = storagectrl;
	}

	public void setSupportPhone(String supportPhone) {
		this.m_supportPhone = supportPhone;
	}

	public void setThresholdCategory(String thresholdCategory) {
		this.m_thresholdCategory = thresholdCategory;
	}

	public void setUsername(String username) {
		this.m_username = username;
	}

	public void setVendor(String vendor) {
		this.m_vendor = vendor;
	}

	public void setVendorAssetNumber(String vendorAssetNumber) {
		this.m_vendorAssetNumber = vendorAssetNumber;
	}

	public void setVendorFax(String vendorFax) {
		this.m_vendorFax = vendorFax;
	}

	public void setVendorPhone(String vendorPhone) {
		this.m_vendorPhone = vendorPhone;
	}

	public void setZip(String zip) {
		this.m_zip = zip;
	}

	@Override
	public String toString() {
		return "AssetCommand [m_additionalhardware=" + m_additionalhardware + ", m_address1=" + m_address1
				+ ", m_address2=" + m_address2 + ", m_admin=" + m_admin + ", m_assetNumber=" + m_assetNumber
				+ ", m_autoenable=" + m_autoenable + ", m_autoenableOptions=" + m_autoenableOptions + ", m_building="
				+ m_building + ", m_category=" + m_category + ", m_circuitId=" + m_circuitId + ", m_city=" + m_city
				+ ", m_comment=" + m_comment + ", m_connection=" + m_connection + ", m_connectionOptions="
				+ m_connectionOptions + ", m_cpu=" + m_cpu + ", m_dateInstalled=" + m_dateInstalled + ", m_department="
				+ m_department + ", m_description=" + m_description + ", m_displayCategory=" + m_displayCategory
				+ ", m_division=" + m_division + ", m_enable=" + m_enable + ", m_floor=" + m_floor + ", m_hdd1="
				+ m_hdd1 + ", m_hdd2=" + m_hdd2 + ", m_hdd3=" + m_hdd3 + ", m_hdd4=" + m_hdd4 + ", m_hdd5=" + m_hdd5
				+ ", m_hdd6=" + m_hdd6 + ", m_id=" + m_id + ", m_inputpower=" + m_inputpower + ", m_lastModifiedBy="
				+ m_lastModifiedBy + ", m_lastModifiedDate=" + m_lastModifiedDate + ", m_lease=" + m_lease
				+ ", m_leaseExpires=" + m_leaseExpires + ", m_maintContractExpiration=" + m_maintContractExpiration
				+ ", m_maintContractNumber=" + m_maintContractNumber + ", m_manufacturer=" + m_manufacturer
				+ ", m_modelNumber=" + m_modelNumber + ", m_notifyCategory=" + m_notifyCategory
				+ ", m_numpowersupplies=" + m_numpowersupplies + ", m_operatingSystem=" + m_operatingSystem
				+ ", m_password=" + m_password + ", m_pollerCategory=" + m_pollerCategory + ", m_port=" + m_port
				+ ", m_rack=" + m_rack + ", m_ram=" + m_ram + ", m_region=" + m_region + ", m_room=" + m_room
				+ ", m_serialNumber=" + m_serialNumber + ", m_slot=" + m_slot + ", m_snmpcommunity=" + m_snmpcommunity
				+ ", m_snmpSysContact=" + m_snmpSysContact + ", m_snmpSysDescription=" + m_snmpSysDescription
				+ ", m_snmpSysLocation=" + m_snmpSysLocation + ", m_snmpSysName=" + m_snmpSysName
				+ ", m_snmpSysObjectId=" + m_snmpSysObjectId + ", m_state=" + m_state + ", m_storagectrl="
				+ m_storagectrl + ", m_supportPhone=" + m_supportPhone + ", m_thresholdCategory=" + m_thresholdCategory
				+ ", m_username=" + m_username + ", m_vendor=" + m_vendor + ", m_vendorAssetNumber="
				+ m_vendorAssetNumber + ", m_vendorFax=" + m_vendorFax + ", m_vendorPhone=" + m_vendorPhone
				+ ", m_zip=" + m_zip + "]";
	}	
}