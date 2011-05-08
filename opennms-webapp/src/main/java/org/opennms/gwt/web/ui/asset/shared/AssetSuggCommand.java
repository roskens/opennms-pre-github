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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * 
 */
public class AssetSuggCommand implements IsSerializable {

	private ArrayList<String> m_additionalhardware;

	private ArrayList<String> m_address1;

	private ArrayList<String> m_address2;

	private ArrayList<String> m_admin;

	private ArrayList<String> m_building;

	private ArrayList<String> m_category;

	private ArrayList<String> m_circuitId;

	private ArrayList<String> m_city;

	private ArrayList<String> m_cpu;

	private ArrayList<String> m_department;

	private ArrayList<String> m_description;

	private ArrayList<String> m_displayCategory;

	private ArrayList<String> m_division;

	private ArrayList<String> m_floor;

	private ArrayList<String> m_hdd1;

	private ArrayList<String> m_hdd2;

	private ArrayList<String> m_hdd3;

	private ArrayList<String> m_hdd4;

	private ArrayList<String> m_hdd5;

	private ArrayList<String> m_hdd6;

	private ArrayList<String> m_inputpower;

	private ArrayList<String> m_lease;

	private ArrayList<String> m_maintContractNumber;

	private ArrayList<String> m_manufacturer;

	private ArrayList<String> m_modelNumber;

	private ArrayList<String> m_notifyCategory;

	private ArrayList<String> m_numpowersupplies;

	private ArrayList<String> m_operatingSystem;

	private ArrayList<String> m_pollerCategory;

	private ArrayList<String> m_rack;

	private ArrayList<String> m_ram;

	private ArrayList<String> m_region;

	private ArrayList<String> m_room;

	private ArrayList<String> m_snmpcommunity;

	private ArrayList<String> m_state;

	private ArrayList<String> m_storagectrl;

	private ArrayList<String> m_supportPhone;

	private ArrayList<String> m_thresholdCategory;

	private ArrayList<String> m_vendor;

	private ArrayList<String> m_vendorFax;

	private ArrayList<String> m_vendorPhone;

	private ArrayList<String> m_zip;

	public AssetSuggCommand() {
		this.m_additionalhardware = new ArrayList<String>();
		this.m_address1 = new ArrayList<String>();
		this.m_address2 = new ArrayList<String>();
		this.m_admin = new ArrayList<String>();
		this.m_building = new ArrayList<String>();
		this.m_category = new ArrayList<String>();
		this.m_circuitId = new ArrayList<String>();
		this.m_city = new ArrayList<String>();
		this.m_cpu = new ArrayList<String>();
		this.m_department = new ArrayList<String>();
		this.m_description = new ArrayList<String>();
		this.m_displayCategory = new ArrayList<String>();
		this.m_division = new ArrayList<String>();
		this.m_floor = new ArrayList<String>();
		this.m_hdd1 = new ArrayList<String>();
		this.m_hdd2 = new ArrayList<String>();
		this.m_hdd3 = new ArrayList<String>();
		this.m_hdd4 = new ArrayList<String>();
		this.m_hdd5 = new ArrayList<String>();
		this.m_hdd6 = new ArrayList<String>();
		this.m_inputpower = new ArrayList<String>();
		this.m_lease = new ArrayList<String>();
		this.m_maintContractNumber = new ArrayList<String>();
		this.m_manufacturer = new ArrayList<String>();
		this.m_modelNumber = new ArrayList<String>();
		this.m_notifyCategory = new ArrayList<String>();
		this.m_numpowersupplies = new ArrayList<String>();
		this.m_operatingSystem = new ArrayList<String>();
		this.m_pollerCategory = new ArrayList<String>();
		this.m_rack = new ArrayList<String>();
		this.m_ram = new ArrayList<String>();
		this.m_region = new ArrayList<String>();
		this.m_room = new ArrayList<String>();
		this.m_snmpcommunity = new ArrayList<String>();
		this.m_state = new ArrayList<String>();
		this.m_storagectrl = new ArrayList<String>();
		this.m_supportPhone = new ArrayList<String>();
		this.m_thresholdCategory = new ArrayList<String>();
		this.m_vendor = new ArrayList<String>();
		this.m_vendorFax = new ArrayList<String>();
		this.m_vendorPhone = new ArrayList<String>();
		this.m_zip = new ArrayList<String>();
	}

	public ArrayList<String> getAdditionalhardware() {
		return this.m_additionalhardware;
	}

	public ArrayList<String> getAddress1() {
		return this.m_address1;
	}

	public ArrayList<String> getAddress2() {
		return this.m_address2;
	}

	public ArrayList<String> getAdmin() {
		return this.m_admin;
	}

	public ArrayList<String> getBuilding() {
		return this.m_building;
	}

	public ArrayList<String> getCategory() {
		return this.m_category;
	}

	public ArrayList<String> getCircuitId() {
		return this.m_circuitId;
	}

	public ArrayList<String> getCity() {
		return this.m_city;
	}

	public ArrayList<String> getCpu() {
		return this.m_cpu;
	}

	public ArrayList<String> getDepartment() {
		return this.m_department;
	}

	public ArrayList<String> getDescription() {
		return this.m_description;
	}

	public ArrayList<String> getDisplayCategory() {
		return this.m_displayCategory;
	}

	public ArrayList<String> getDivision() {
		return this.m_division;
	}

	public ArrayList<String> getFloor() {
		return this.m_floor;
	}

	public ArrayList<String> getHdd1() {
		return this.m_hdd1;
	}

	public ArrayList<String> getHdd2() {
		return this.m_hdd2;
	}

	public ArrayList<String> getHdd3() {
		return this.m_hdd3;
	}

	public ArrayList<String> getHdd4() {
		return this.m_hdd4;
	}

	public ArrayList<String> getHdd5() {
		return this.m_hdd5;
	}

	public ArrayList<String> getHdd6() {
		return this.m_hdd6;
	}

	public ArrayList<String> getInputpower() {
		return this.m_inputpower;
	}

	public ArrayList<String> getLease() {
		return this.m_lease;
	}

	public ArrayList<String> getMaintContractNumber() {
		return this.m_maintContractNumber;
	}

	public ArrayList<String> getManufacturer() {
		return this.m_manufacturer;
	}

	public ArrayList<String> getModelNumber() {
		return this.m_modelNumber;
	}

	public ArrayList<String> getNotifyCategory() {
		return this.m_notifyCategory;
	}

	public ArrayList<String> getNumpowersupplies() {
		return this.m_numpowersupplies;
	}

	public ArrayList<String> getOperatingSystem() {
		return this.m_operatingSystem;
	}

	public ArrayList<String> getPollerCategory() {
		return this.m_pollerCategory;
	}

	public ArrayList<String> getRack() {
		return this.m_rack;
	}

	public ArrayList<String> getRam() {
		return this.m_ram;
	}

	public ArrayList<String> getRegion() {
		return this.m_region;
	}

	public ArrayList<String> getRoom() {
		return this.m_room;
	}

	public ArrayList<String> getSnmpcommunity() {
		return this.m_snmpcommunity;
	}

	public ArrayList<String> getState() {
		return this.m_state;
	}

	public ArrayList<String> getStoragectrl() {
		return this.m_storagectrl;
	}

	public ArrayList<String> getSupportPhone() {
		return this.m_supportPhone;
	}

	public ArrayList<String> getThresholdCategory() {
		return this.m_thresholdCategory;
	}

	public ArrayList<String> getVendor() {
		return this.m_vendor;
	}

	public ArrayList<String> getVendorFax() {
		return this.m_vendorFax;
	}

	public ArrayList<String> getVendorPhone() {
		return this.m_vendorPhone;
	}

	public ArrayList<String> getZip() {
		return this.m_zip;
	}

	public void setAdditionalhardware(ArrayList<String> additionalhardware) {
		this.m_additionalhardware = additionalhardware;
	}

	public void setAddress1(ArrayList<String> address1) {
		this.m_address1 = address1;
	}

	public void setAddress2(ArrayList<String> address2) {
		this.m_address2 = address2;
	}

	public void setAdmin(ArrayList<String> admin) {
		this.m_admin = admin;
	}

	public void setBuilding(ArrayList<String> building) {
		this.m_building = building;
	}

	public void setCategory(ArrayList<String> category) {
		this.m_category = category;
	}

	public void setCircuitId(ArrayList<String> circuitId) {
		this.m_circuitId = circuitId;
	}

	public void setCity(ArrayList<String> city) {
		this.m_city = city;
	}

	public void setCpu(ArrayList<String> cpu) {
		this.m_cpu = cpu;
	}

	public void setDepartment(ArrayList<String> department) {
		this.m_department = department;
	}

	public void setDescription(ArrayList<String> description) {
		this.m_description = description;
	}

	public void setDisplayCategory(ArrayList<String> displayCategory) {
		this.m_displayCategory = displayCategory;
	}

	public void setDivision(ArrayList<String> division) {
		this.m_division = division;
	}

	public void setFloor(ArrayList<String> floor) {
		this.m_floor = floor;
	}

	public void setHdd1(ArrayList<String> hdd1) {
		this.m_hdd1 = hdd1;
	}

	public void setHdd2(ArrayList<String> hdd2) {
		this.m_hdd2 = hdd2;
	}

	public void setHdd3(ArrayList<String> hdd3) {
		this.m_hdd3 = hdd3;
	}

	public void setHdd4(ArrayList<String> hdd4) {
		this.m_hdd4 = hdd4;
	}

	public void setHdd5(ArrayList<String> hdd5) {
		this.m_hdd5 = hdd5;
	}

	public void setHdd6(ArrayList<String> hdd6) {
		this.m_hdd6 = hdd6;
	}

	public void setInputpower(ArrayList<String> inputpower) {
		this.m_inputpower = inputpower;
	}

	public void setLease(ArrayList<String> lease) {
		this.m_lease = lease;
	}

	public void setMaintContractNumber(ArrayList<String> maintContractNumber) {
		this.m_maintContractNumber = maintContractNumber;
	}

	public void setManufacturer(ArrayList<String> manufacturer) {
		this.m_manufacturer = manufacturer;
	}

	public void setModelNumber(ArrayList<String> modelNumber) {
		this.m_modelNumber = modelNumber;
	}

	public void setNotifyCategory(ArrayList<String> notifyCategory) {
		this.m_notifyCategory = notifyCategory;
	}

	public void setNumpowersupplies(ArrayList<String> numpowersupplies) {
		this.m_numpowersupplies = numpowersupplies;
	}

	public void setOperatingSystem(ArrayList<String> operatingSystem) {
		this.m_operatingSystem = operatingSystem;
	}

	public void setPollerCategory(ArrayList<String> pollerCategory) {
		this.m_pollerCategory = pollerCategory;
	}

	public void setRack(ArrayList<String> rack) {
		this.m_rack = rack;
	}

	public void setRam(ArrayList<String> ram) {
		this.m_ram = ram;
	}

	public void setRegion(ArrayList<String> region) {
		this.m_region = region;
	}

	public void setRoom(ArrayList<String> room) {
		this.m_room = room;
	}

	public void setSnmpcommunity(ArrayList<String> snmpcommunity) {
		this.m_snmpcommunity = snmpcommunity;
	}

	public void setState(ArrayList<String> state) {
		this.m_state = state;
	}

	public void setStoragectrl(ArrayList<String> storagectrl) {
		this.m_storagectrl = storagectrl;
	}

	public void setSupportPhone(ArrayList<String> supportPhone) {
		this.m_supportPhone = supportPhone;
	}

	public void setThresholdCategory(ArrayList<String> thresholdCategory) {
		this.m_thresholdCategory = thresholdCategory;
	}

	public void setVendor(ArrayList<String> vendor) {
		this.m_vendor = vendor;
	}

	public void setVendorFax(ArrayList<String> vendorFax) {
		this.m_vendorFax = vendorFax;
	}

	public void setVendorPhone(ArrayList<String> vendorPhone) {
		this.m_vendorPhone = vendorPhone;
	}

	public void setZip(ArrayList<String> zip) {
		this.m_zip = zip;
	}

	public void addAdditionalhardware(String additionalhardware) {
		if (additionalhardware != null)
			this.m_address1.add(additionalhardware);
	}

	public void addAddress1(String address1) {
		if (address1 != null)
			this.m_address1.add(address1);
	}

	public void addAddress2(String address2) {
		if (address2 != null)
			this.m_address2.add(address2);
	}

	public void addAdmin(String admin) {
		if (admin != null)
			this.m_admin.add(admin);
	}

	public void addBuilding(String building) {
		if (building != null)
			this.m_building.add(building);
	}

	public void addCategory(String category) {
		if (category != null)
			this.m_category.add(category);
	}

	public void addCircuitId(String circuitId) {
		if (circuitId != null)
			this.m_circuitId.add(circuitId);
	}

	public void addCity(String city) {
		if (city != null)
			this.m_city.add(city);
	}

	public void addCpu(String cpu) {
		if (cpu != null)
			this.m_cpu.add(cpu);
	}

	public void addDepartment(String department) {
		if (department != null)
			this.m_department.add(department);
	}

	public void addDescription(String description) {
		if (description != null)
			this.m_description.add(description);
	}

	public void addDisplayCategory(String displayCategory) {
		if (displayCategory != null)
			this.m_displayCategory.add(displayCategory);
	}

	public void addDivision(String division) {
		if (division != null)
			this.m_division.add(division);
	}

	public void addFloor(String floor) {
		if (floor != null)
			this.m_floor.add(floor);
	}

	public void addHdd1(String hdd1) {
		if (hdd1 != null)
			this.m_hdd1.add(hdd1);
	}

	public void addHdd2(String hdd2) {
		if (hdd2 != null)
			this.m_hdd2.add(hdd2);
	}

	public void addHdd3(String hdd3) {
		if (hdd3 != null)
			this.m_hdd3.add(hdd3);
	}

	public void addHdd4(String hdd4) {
		if (hdd4 != null)
			this.m_hdd4.add(hdd4);
	}

	public void addHdd5(String hdd5) {
		if (hdd5 != null)
			this.m_hdd5.add(hdd5);
	}

	public void addHdd6(String hdd6) {
		if (hdd6 != null)
			this.m_hdd6.add(hdd6);
	}

	public void addInputpower(String inputpower) {
		if (inputpower != null)
			this.m_inputpower.add(inputpower);
	}

	public void addLease(String lease) {
		if (lease != null)
			this.m_lease.add(lease);
	}

	public void addMaintContractNumber(String maintContractNumber) {
		if (maintContractNumber != null)
			this.m_maintContractNumber.add(maintContractNumber);
	}

	public void addManufacturer(String manufacturer) {
		if (manufacturer != null)
			this.m_manufacturer.add(manufacturer);
	}

	public void addModelNumber(String modelNumber) {
		if (modelNumber != null)
			this.m_modelNumber.add(modelNumber);
	}

	public void addNotifyCategory(String notifyCategory) {
		if (notifyCategory != null)
			this.m_notifyCategory.add(notifyCategory);
	}

	public void addNumpowersupplies(String numpowersupplies) {
		if (numpowersupplies != null)
			this.m_numpowersupplies.add(numpowersupplies);
	}

	public void addOperatingSystem(String operatingSystem) {
		if (operatingSystem != null)
			this.m_operatingSystem.add(operatingSystem);
	}

	public void addPollerCategory(String pollerCategory) {
		if (pollerCategory != null)
			this.m_pollerCategory.add(pollerCategory);
	}

	public void addRack(String rack) {
		if (rack != null)
			this.m_rack.add(rack);
	}

	public void addRam(String ram) {
		if (ram != null)
			this.m_ram.add(ram);
	}

	public void addRegion(String region) {
		if (region != null)
			this.m_region.add(region);
	}

	public void addRoom(String room) {
		if (room != null)
			this.m_room.add(room);
	}

	public void addSnmpcommunity(String snmpcommunity) {
		if (snmpcommunity != null)
			this.m_snmpcommunity.add(snmpcommunity);
	}

	public void addState(String state) {
		if (state != null)
			this.m_state.add(state);
	}

	public void addStoragectrl(String storagectrl) {
		if (storagectrl != null)
			this.m_storagectrl.add(storagectrl);
	}

	public void addSupportPhone(String supportPhone) {
		if (supportPhone != null)
			this.m_supportPhone.add(supportPhone);
	}

	public void addThresholdCategory(String thresholdCategory) {
		if (thresholdCategory != null)
			this.m_thresholdCategory.add(thresholdCategory);
	}

	public void addVendor(String vendor) {
		if (vendor != null)
			this.m_vendor.add(vendor);
	}

	public void addVendorFax(String vendorFax) {
		if (vendorFax != null)
			this.m_vendorFax.add(vendorFax);
	}

	public void addVendorPhone(String vendorPhone) {
		if (vendorPhone != null)
			this.m_vendorPhone.add(vendorPhone);
	}

	public void addZip(String zip) {
		if (zip != null)
			this.m_zip.add(zip);
	}

	@Override
	public String toString() {
		return "AssetSuggCommand [m_additionalhardware=" + m_additionalhardware
				+ ", m_address1=" + m_address1 + ", m_address2=" + m_address2
				+ ", m_admin=" + m_admin + ", m_building=" + m_building
				+ ", m_category=" + m_category + ", m_circuitId=" + m_circuitId
				+ ", m_city=" + m_city + ", m_cpu=" + m_cpu + ", m_department="
				+ m_department + ", m_description=" + m_description
				+ ", m_displayCategory=" + m_displayCategory + ", m_division="
				+ m_division + ", m_floor=" + m_floor + ", m_hdd1=" + m_hdd1
				+ ", m_hdd2=" + m_hdd2 + ", m_hdd3=" + m_hdd3 + ", m_hdd4="
				+ m_hdd4 + ", m_hdd5=" + m_hdd5 + ", m_hdd6=" + m_hdd6
				+ ", m_inputpower=" + m_inputpower + ", m_lease=" + m_lease
				+ ", m_maintContractNumber=" + m_maintContractNumber
				+ ", m_manufacturer=" + m_manufacturer + ", m_modelNumber="
				+ m_modelNumber + ", m_notifyCategory=" + m_notifyCategory
				+ ", m_numpowersupplies=" + m_numpowersupplies
				+ ", m_operatingSystem=" + m_operatingSystem
				+ ", m_pollerCategory=" + m_pollerCategory + ", m_rack="
				+ m_rack + ", m_ram=" + m_ram + ", m_region=" + m_region
				+ ", m_room=" + m_room + ", m_snmpcommunity=" + m_snmpcommunity
				+ ", m_state=" + m_state + ", m_storagectrl=" + m_storagectrl
				+ ", m_supportPhone=" + m_supportPhone
				+ ", m_thresholdCategory=" + m_thresholdCategory
				+ ", m_vendor=" + m_vendor + ", m_vendorFax=" + m_vendorFax
				+ ", m_vendorPhone=" + m_vendorPhone + ", m_zip=" + m_zip + "]";
	}
}