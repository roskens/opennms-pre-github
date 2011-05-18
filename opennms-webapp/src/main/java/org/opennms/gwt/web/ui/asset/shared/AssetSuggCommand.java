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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 * 
 */
public class AssetSuggCommand implements IsSerializable {
	
	private Set<String> m_additionalhardware;

	private Set<String> m_address1;

	private Set<String> m_address2;

	private Set<String> m_admin;

	private Set<String> m_building;

	private Set<String> m_category;

	private Set<String> m_circuitId;

	private Set<String> m_city;

	private Set<String> m_cpu;

	private Set<String> m_department;

	private Set<String> m_description;

	private Set<String> m_displayCategory;

	private Set<String> m_division;

	private Set<String> m_floor;

	private Set<String> m_hdd1;

	private Set<String> m_hdd2;

	private Set<String> m_hdd3;

	private Set<String> m_hdd4;

	private Set<String> m_hdd5;

	private Set<String> m_hdd6;

	private Set<String> m_inputpower;

	private Set<String> m_lease;

	private Set<String> m_maintContract;

	private Set<String> m_manufacturer;

	private Set<String> m_modelNumber;

	private Set<String> m_notifyCategory;

	private Set<String> m_numpowersupplies;

	private Set<String> m_operatingSystem;

	private Set<String> m_pollerCategory;

	private Set<String> m_rack;

	private Set<String> m_ram;

	private Set<String> m_region;

	private Set<String> m_room;

	private Set<String> m_snmpcommunity;

	private Set<String> m_state;

	private Set<String> m_storagectrl;

	private Set<String> m_supportPhone;

	private Set<String> m_thresholdCategory;

	private Set<String> m_vendor;

	private Set<String> m_vendorFax;

	private Set<String> m_vendorPhone;

	private Set<String> m_zip;

	public AssetSuggCommand() {
		this.m_additionalhardware = new HashSet<String>();
		this.m_address1 = new HashSet<String>();
		this.m_address2 = new HashSet<String>();
		this.m_admin = new HashSet<String>();
		this.m_building = new HashSet<String>();
		this.m_category = new HashSet<String>();
		this.m_circuitId = new HashSet<String>();
		this.m_city = new HashSet<String>();
		this.m_cpu = new HashSet<String>();
		this.m_department = new HashSet<String>();
		this.m_description = new HashSet<String>();
		this.m_displayCategory = new HashSet<String>();
		this.m_division = new HashSet<String>();
		this.m_floor = new HashSet<String>();
		this.m_hdd1 = new HashSet<String>();
		this.m_hdd2 = new HashSet<String>();
		this.m_hdd3 = new HashSet<String>();
		this.m_hdd4 = new HashSet<String>();
		this.m_hdd5 = new HashSet<String>();
		this.m_hdd6 = new HashSet<String>();
		this.m_inputpower = new HashSet<String>();
		this.m_lease = new HashSet<String>();
		this.m_maintContract = new HashSet<String>();
		this.m_manufacturer = new HashSet<String>();
		this.m_modelNumber = new HashSet<String>();
		this.m_notifyCategory = new HashSet<String>();
		this.m_numpowersupplies = new HashSet<String>();
		this.m_operatingSystem = new HashSet<String>();
		this.m_pollerCategory = new HashSet<String>();
		this.m_rack = new HashSet<String>();
		this.m_ram = new HashSet<String>();
		this.m_region = new HashSet<String>();
		this.m_room = new HashSet<String>();
		this.m_snmpcommunity = new HashSet<String>();
		this.m_state = new HashSet<String>();
		this.m_storagectrl = new HashSet<String>();
		this.m_supportPhone = new HashSet<String>();
		this.m_thresholdCategory = new HashSet<String>();
		this.m_vendor = new HashSet<String>();
		this.m_vendorFax = new HashSet<String>();
		this.m_vendorPhone = new HashSet<String>();
		this.m_zip = new HashSet<String>();
	}

	public Collection<String> getAdditionalhardware() {
		return this.m_additionalhardware;
	}

	public Collection<String> getAddress1() {
		return this.m_address1;
	}

	public Collection<String> getAddress2() {
		return this.m_address2;
	}

	public Collection<String> getAdmin() {
		return this.m_admin;
	}

	public Collection<String> getBuilding() {
		return this.m_building;
	}

	public Collection<String> getCategory() {
		return this.m_category;
	}

	public Collection<String> getCircuitId() {
		return this.m_circuitId;
	}

	public Collection<String> getCity() {
		return this.m_city;
	}

	public Collection<String> getCpu() {
		return this.m_cpu;
	}

	public Collection<String> getDepartment() {
		return this.m_department;
	}

	public Collection<String> getDescription() {
		return this.m_description;
	}

	public Collection<String> getDisplayCategory() {
		return this.m_displayCategory;
	}

	public Collection<String> getDivision() {
		return this.m_division;
	}

	public Collection<String> getFloor() {
		return this.m_floor;
	}

	public Collection<String> getHdd1() {
		return this.m_hdd1;
	}

	public Collection<String> getHdd2() {
		return this.m_hdd2;
	}

	public Collection<String> getHdd3() {
		return this.m_hdd3;
	}

	public Collection<String> getHdd4() {
		return this.m_hdd4;
	}

	public Collection<String> getHdd5() {
		return this.m_hdd5;
	}

	public Collection<String> getHdd6() {
		return this.m_hdd6;
	}

	public Collection<String> getInputpower() {
		return this.m_inputpower;
	}

	public Collection<String> getLease() {
		return this.m_lease;
	}

	public Collection<String> getMaintContract() {
		return this.m_maintContract;
	}

	public Collection<String> getManufacturer() {
		return this.m_manufacturer;
	}

	public Collection<String> getModelNumber() {
		return this.m_modelNumber;
	}

	public Collection<String> getNotifyCategory() {
		return this.m_notifyCategory;
	}

	public Collection<String> getNumpowersupplies() {
		return this.m_numpowersupplies;
	}

	public Collection<String> getOperatingSystem() {
		return this.m_operatingSystem;
	}

	public Collection<String> getPollerCategory() {
		return this.m_pollerCategory;
	}

	public Collection<String> getRack() {
		return this.m_rack;
	}

	public Collection<String> getRam() {
		return this.m_ram;
	}

	public Collection<String> getRegion() {
		return this.m_region;
	}

	public Collection<String> getRoom() {
		return this.m_room;
	}

	public Collection<String> getSnmpcommunity() {
		return this.m_snmpcommunity;
	}

	public Collection<String> getState() {
		return this.m_state;
	}

	public Collection<String> getStoragectrl() {
		return this.m_storagectrl;
	}

	public Collection<String> getSupportPhone() {
		return this.m_supportPhone;
	}

	public Collection<String> getThresholdCategory() {
		return this.m_thresholdCategory;
	}

	public Collection<String> getVendor() {
		return this.m_vendor;
	}

	public Collection<String> getVendorFax() {
		return this.m_vendorFax;
	}

	public Collection<String> getVendorPhone() {
		return this.m_vendorPhone;
	}

	public Collection<String> getZip() {
		return this.m_zip;
	}

	public void addAdditionalhardware(String additionalhardware) {
		if (additionalhardware != null && !"".equals(additionalhardware))
			this.m_additionalhardware.add(additionalhardware);
	}

	public void addAddress1(String address1) {
		if (address1 != null && !"".equals(address1))
			this.m_address1.add(address1);
	}

	public void addAddress2(String address2) {
		if (address2 != null && !"".equals(address2))
			this.m_address2.add(address2);
	}

	public void addAdmin(String admin) {
		if (admin != null && !"".equals(admin))
			this.m_admin.add(admin);
	}

	public void addBuilding(String building) {
		if (building != null && !"".equals(building))
			this.m_building.add(building);
	}

	public void addCategory(String category) {
		if (category != null && !"".equals(category))
			this.m_category.add(category);
	}

	public void addCircuitId(String circuitId) {
		if (circuitId != null && !"".equals(circuitId))
			this.m_circuitId.add(circuitId);
	}

	public void addCity(String city) {
		if (city != null && !"".equals(city))
			this.m_city.add(city);
	}

	public void addCpu(String cpu) {
		if (cpu != null && !"".equals(cpu))
			this.m_cpu.add(cpu);
	}

	public void addDepartment(String department) {
		if (department != null && !"".equals(department))
			this.m_department.add(department);
	}

	public void addDescription(String description) {
		if (description != null && !"".equals(description))
			this.m_description.add(description);
	}

	public void addDisplayCategory(String displayCategory) {
		if (displayCategory != null && !"".equals(displayCategory))
			this.m_displayCategory.add(displayCategory);
	}

	public void addDivision(String division) {
		if (division != null && !"".equals(division))
			this.m_division.add(division);
	}

	public void addFloor(String floor) {
		if (floor != null && !"".equals(floor))
			this.m_floor.add(floor);
	}

	public void addHdd1(String hdd1) {
		if (hdd1 != null && !"".equals(hdd1))
			this.m_hdd1.add(hdd1);
	}

	public void addHdd2(String hdd2) {
		if (hdd2 != null && !"".equals(hdd2))
			this.m_hdd2.add(hdd2);
	}

	public void addHdd3(String hdd3) {
		if (hdd3 != null && !"".equals(hdd3))
			this.m_hdd3.add(hdd3);
	}

	public void addHdd4(String hdd4) {
		if (hdd4 != null && !"".equals(hdd4))
			this.m_hdd4.add(hdd4);
	}

	public void addHdd5(String hdd5) {
		if (hdd5 != null && !"".equals(hdd5))
			this.m_hdd5.add(hdd5);
	}

	public void addHdd6(String hdd6) {
		if (hdd6 != null && !"".equals(hdd6))
			this.m_hdd6.add(hdd6);
	}

	public void addInputpower(String inputpower) {
		if (inputpower != null && !"".equals(inputpower))
			this.m_inputpower.add(inputpower);
	}

	public void addLease(String lease) {
		if (lease != null && !"".equals(lease))
			this.m_lease.add(lease);
	}

	public void addMaintContract(String maintContract) {
		if (maintContract != null && !"".equals(maintContract))
			this.m_maintContract.add(maintContract);
	}

	public void addManufacturer(String manufacturer) {
		if (manufacturer != null && !"".equals(manufacturer))
			this.m_manufacturer.add(manufacturer);
	}

	public void addModelNumber(String modelNumber) {
		if (modelNumber != null && !"".equals(modelNumber))
			this.m_modelNumber.add(modelNumber);
	}

	public void addNotifyCategory(String notifyCategory) {
		if (notifyCategory != null && !"".equals(notifyCategory))
			this.m_notifyCategory.add(notifyCategory);
	}

	public void addNumpowersupplies(String numpowersupplies) {
		if (numpowersupplies != null && !"".equals(numpowersupplies))
			this.m_numpowersupplies.add(numpowersupplies);
	}

	public void addOperatingSystem(String operatingSystem) {
		if (operatingSystem != null && !"".equals(operatingSystem))
			this.m_operatingSystem.add(operatingSystem);
	}

	public void addPollerCategory(String pollerCategory) {
		if (pollerCategory != null && !"".equals(pollerCategory))
			this.m_pollerCategory.add(pollerCategory);
	}

	public void addRack(String rack) {
		if (rack != null && !"".equals(rack))
			this.m_rack.add(rack);
	}

	public void addRam(String ram) {
		if (ram != null && !"".equals(ram))
			this.m_ram.add(ram);
	}

	public void addRegion(String region) {
		if (region != null && !"".equals(region))
			this.m_region.add(region);
	}

	public void addRoom(String room) {
		if (room != null && !"".equals(room))
			this.m_room.add(room);
	}

	public void addSnmpcommunity(String snmpcommunity) {
		if (snmpcommunity != null && !"".equals(snmpcommunity))
			this.m_snmpcommunity.add(snmpcommunity);
	}

	public void addState(String state) {
		if (state != null && !"".equals(state))
			this.m_state.add(state);
	}

	public void addStoragectrl(String storagectrl) {
		if (storagectrl != null && !"".equals(storagectrl))
			this.m_storagectrl.add(storagectrl);
	}

	public void addSupportPhone(String supportPhone) {
		if (supportPhone != null && !"".equals(supportPhone))
			this.m_supportPhone.add(supportPhone);
	}

	public void addThresholdCategory(String thresholdCategory) {
		if (thresholdCategory != null && !"".equals(thresholdCategory))
			this.m_thresholdCategory.add(thresholdCategory);
	}

	public void addVendor(String vendor) {
		if (vendor != null && !"".equals(vendor))
			this.m_vendor.add(vendor);
	}

	public void addVendorFax(String vendorFax) {
		if (vendorFax != null && !"".equals(vendorFax))
			this.m_vendorFax.add(vendorFax);
	}

	public void addVendorPhone(String vendorPhone) {
		if (vendorPhone != null && !"".equals(vendorPhone))
			this.m_vendorPhone.add(vendorPhone);
	}

	public void addZip(String zip) {
		if (zip != null && !"".equals(zip))
			this.m_zip.add(zip);
	}
}