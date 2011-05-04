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
 * 
 */
public class AssetSuggCommand implements IsSerializable {

	private ArrayList<String> additionalhardware;

	private ArrayList<String> address1;

	private ArrayList<String> address2;

	private ArrayList<String> admin;

	private ArrayList<String> building;

	private ArrayList<String> category;

	private ArrayList<String> circuitId;

	private ArrayList<String> city;

	private ArrayList<String> cpu;

	private ArrayList<String> department;

	private ArrayList<String> description;

	private ArrayList<String> displayCategory;

	private ArrayList<String> division;

	private ArrayList<String> floor;

	private ArrayList<String> hdd1;

	private ArrayList<String> hdd2;

	private ArrayList<String> hdd3;

	private ArrayList<String> hdd4;

	private ArrayList<String> hdd5;

	private ArrayList<String> hdd6;

	private ArrayList<String> inputpower;

	private ArrayList<String> lease;

	private ArrayList<String> maintContractNumber;

	private ArrayList<String> manufacturer;

	private ArrayList<String> modelNumber;

	private ArrayList<String> notifyCategory;

	private ArrayList<String> numpowersupplies;

	private ArrayList<String> operatingSystem;

	private ArrayList<String> pollerCategory;

	private ArrayList<String> rack;

	private ArrayList<String> ram;

	private ArrayList<String> region;

	private ArrayList<String> room;

	private ArrayList<String> Snmpcommunity;

	private ArrayList<String> state;

	private ArrayList<String> storagectrl;

	private ArrayList<String> supportPhone;

	private ArrayList<String> thresholdCategory;

	private ArrayList<String> vendor;

	private ArrayList<String> vendorFax;

	private ArrayList<String> vendorPhone;

	private ArrayList<String> zip;

	public ArrayList<String> getAdditionalhardware() {
		return additionalhardware;
	}

	public ArrayList<String> getAddress1() {
		return address1;
	}

	public ArrayList<String> getAddress2() {
		return address2;
	}

	public ArrayList<String> getAdmin() {
		return admin;
	}

	public ArrayList<String> getBuilding() {
		return building;
	}

	public ArrayList<String> getCategory() {
		return category;
	}

	public ArrayList<String> getCircuitId() {
		return circuitId;
	}

	public ArrayList<String> getCity() {
		return city;
	}

	public ArrayList<String> getCpu() {
		return cpu;
	}

	public ArrayList<String> getDepartment() {
		return department;
	}

	public ArrayList<String> getDescription() {
		return description;
	}

	public ArrayList<String> getDisplayCategory() {
		return displayCategory;
	}

	public ArrayList<String> getDivision() {
		return division;
	}

	public ArrayList<String> getFloor() {
		return floor;
	}

	public ArrayList<String> getHdd1() {
		return hdd1;
	}

	public ArrayList<String> getHdd2() {
		return hdd2;
	}

	public ArrayList<String> getHdd3() {
		return hdd3;
	}

	public ArrayList<String> getHdd4() {
		return hdd4;
	}

	public ArrayList<String> getHdd5() {
		return hdd5;
	}

	public ArrayList<String> getHdd6() {
		return hdd6;
	}

	public ArrayList<String> getInputpower() {
		return inputpower;
	}

	public ArrayList<String> getLease() {
		return lease;
	}

	public ArrayList<String> getMaintContractNumber() {
		return maintContractNumber;
	}

	public ArrayList<String> getManufacturer() {
		return manufacturer;
	}

	public ArrayList<String> getModelNumber() {
		return modelNumber;
	}

	public ArrayList<String> getNotifyCategory() {
		return notifyCategory;
	}

	public ArrayList<String> getNumpowersupplies() {
		return numpowersupplies;
	}

	public ArrayList<String> getOperatingSystem() {
		return operatingSystem;
	}

	public ArrayList<String> getPollerCategory() {
		return pollerCategory;
	}

	public ArrayList<String> getRack() {
		return rack;
	}

	public ArrayList<String> getRam() {
		return ram;
	}

	public ArrayList<String> getRegion() {
		return region;
	}

	public ArrayList<String> getRoom() {
		return room;
	}

	public ArrayList<String> getSnmpcommunity() {
		return Snmpcommunity;
	}

	public ArrayList<String> getState() {
		return state;
	}

	public ArrayList<String> getStoragectrl() {
		return storagectrl;
	}

	public ArrayList<String> getSupportPhone() {
		return supportPhone;
	}

	public ArrayList<String> getThresholdCategory() {
		return thresholdCategory;
	}

	public ArrayList<String> getVendor() {
		return vendor;
	}

	public ArrayList<String> getVendorFax() {
		return vendorFax;
	}

	public ArrayList<String> getVendorPhone() {
		return vendorPhone;
	}

	public ArrayList<String> getZip() {
		return zip;
	}

	public void setAdditionalhardware(ArrayList<String> additionalhardware) {
		this.additionalhardware = additionalhardware;
	}

	public void setAddress1(ArrayList<String> address1) {
		this.address1 = address1;
	}

	public void setAddress2(ArrayList<String> address2) {
		this.address2 = address2;
	}

	public void setAdmin(ArrayList<String> admin) {
		this.admin = admin;
	}

	public void setBuilding(ArrayList<String> building) {
		this.building = building;
	}

	public void setCategory(ArrayList<String> category) {
		this.category = category;
	}

	public void setCircuitId(ArrayList<String> circuitId) {
		this.circuitId = circuitId;
	}

	public void setCity(ArrayList<String> city) {
		this.city = city;
	}

	public void setCpu(ArrayList<String> cpu) {
		this.cpu = cpu;
	}

	public void setDepartment(ArrayList<String> department) {
		this.department = department;
	}

	public void setDescription(ArrayList<String> description) {
		this.description = description;
	}

	public void setDisplayCategory(ArrayList<String> displayCategory) {
		this.displayCategory = displayCategory;
	}

	public void setDivision(ArrayList<String> division) {
		this.division = division;
	}

	public void setFloor(ArrayList<String> floor) {
		this.floor = floor;
	}

	public void setHdd1(ArrayList<String> hdd1) {
		this.hdd1 = hdd1;
	}

	public void setHdd2(ArrayList<String> hdd2) {
		this.hdd2 = hdd2;
	}

	public void setHdd3(ArrayList<String> hdd3) {
		this.hdd3 = hdd3;
	}

	public void setHdd4(ArrayList<String> hdd4) {
		this.hdd4 = hdd4;
	}

	public void setHdd5(ArrayList<String> hdd5) {
		this.hdd5 = hdd5;
	}

	public void setHdd6(ArrayList<String> hdd6) {
		this.hdd6 = hdd6;
	}

	public void setInputpower(ArrayList<String> inputpower) {
		this.inputpower = inputpower;
	}

	public void setLease(ArrayList<String> lease) {
		this.lease = lease;
	}

	public void setMaintContractNumber(ArrayList<String> maintContractNumber) {
		this.maintContractNumber = maintContractNumber;
	}

	public void setManufacturer(ArrayList<String> manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setModelNumber(ArrayList<String> modelNumber) {
		this.modelNumber = modelNumber;
	}

	public void setNotifyCategory(ArrayList<String> notifyCategory) {
		this.notifyCategory = notifyCategory;
	}

	public void setNumpowersupplies(ArrayList<String> numpowersupplies) {
		this.numpowersupplies = numpowersupplies;
	}

	public void setOperatingSystem(ArrayList<String> operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public void setPollerCategory(ArrayList<String> pollerCategory) {
		this.pollerCategory = pollerCategory;
	}

	public void setRack(ArrayList<String> rack) {
		this.rack = rack;
	}

	public void setRam(ArrayList<String> ram) {
		this.ram = ram;
	}

	public void setRegion(ArrayList<String> region) {
		this.region = region;
	}

	public void setRoom(ArrayList<String> room) {
		this.room = room;
	}

	public void setSnmpcommunity(ArrayList<String> snmpcommunity) {
		Snmpcommunity = snmpcommunity;
	}

	public void setState(ArrayList<String> state) {
		this.state = state;
	}

	public void setStoragectrl(ArrayList<String> storagectrl) {
		this.storagectrl = storagectrl;
	}

	public void setSupportPhone(ArrayList<String> supportPhone) {
		this.supportPhone = supportPhone;
	}

	public void setThresholdCategory(ArrayList<String> thresholdCategory) {
		this.thresholdCategory = thresholdCategory;
	}

	public void setVendor(ArrayList<String> vendor) {
		this.vendor = vendor;
	}

	public void setVendorFax(ArrayList<String> vendorFax) {
		this.vendorFax = vendorFax;
	}

	public void setVendorPhone(ArrayList<String> vendorPhone) {
		this.vendorPhone = vendorPhone;
	}

	public void setZip(ArrayList<String> zip) {
		this.zip = zip;
	}

	@Override
	public String toString() {
		return "AssetSuggCommand [additionalhardware=" + additionalhardware + ", address1=" + address1 + ", address2="
				+ address2 + ", admin=" + admin + ", building=" + building + ", category=" + category + ", circuitId="
				+ circuitId + ", city=" + city + ", cpu=" + cpu + ", department=" + department + ", description="
				+ description + ", displayCategory=" + displayCategory + ", division=" + division + ", floor=" + floor
				+ ", hdd1=" + hdd1 + ", hdd2=" + hdd2 + ", hdd3=" + hdd3 + ", hdd4=" + hdd4 + ", hdd5=" + hdd5
				+ ", hdd6=" + hdd6 + ", inputpower=" + inputpower + ", lease=" + lease + ", maintContractNumber="
				+ maintContractNumber + ", manufacturer=" + manufacturer + ", modelNumber=" + modelNumber
				+ ", notifyCategory=" + notifyCategory + ", numpowersupplies=" + numpowersupplies
				+ ", operatingSystem=" + operatingSystem + ", pollerCategory=" + pollerCategory + ", rack=" + rack
				+ ", ram=" + ram + ", region=" + region + ", room=" + room + ", Snmpcommunity=" + Snmpcommunity
				+ ", state=" + state + ", storagectrl=" + storagectrl + ", supportPhone=" + supportPhone
				+ ", thresholdCategory=" + thresholdCategory + ", vendor=" + vendor + ", vendorFax=" + vendorFax
				+ ", vendorPhone=" + vendorPhone + ", zip=" + zip + "]";
	}
}