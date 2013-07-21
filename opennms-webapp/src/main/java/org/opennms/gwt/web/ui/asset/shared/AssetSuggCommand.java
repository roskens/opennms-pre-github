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

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSetSuggestBox;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class AssetSuggCommand.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a> Command object
 *         to transfer suggestions for {@link FieldSetSuggestBox}es to ui.
 *         Contains ordered treesets for each suggestion list.
 */
public class AssetSuggCommand implements IsSerializable {

    /** The m_additionalhardware. */
    private Set<String> m_additionalhardware;

    /** The m_address1. */
    private Set<String> m_address1;

    /** The m_address2. */
    private Set<String> m_address2;

    /** The m_admin. */
    private Set<String> m_admin;

    /** The m_building. */
    private Set<String> m_building;

    /** The m_category. */
    private Set<String> m_category;

    /** The m_circuit id. */
    private Set<String> m_circuitId;

    /** The m_city. */
    private Set<String> m_city;

    /** The m_country. */
    private Set<String> m_country;

    /** The m_cpu. */
    private Set<String> m_cpu;

    /** The m_department. */
    private Set<String> m_department;

    /** The m_description. */
    private Set<String> m_description;

    /** The m_display category. */
    private Set<String> m_displayCategory;

    /** The m_division. */
    private Set<String> m_division;

    /** The m_floor. */
    private Set<String> m_floor;

    /** The m_hdd1. */
    private Set<String> m_hdd1;

    /** The m_hdd2. */
    private Set<String> m_hdd2;

    /** The m_hdd3. */
    private Set<String> m_hdd3;

    /** The m_hdd4. */
    private Set<String> m_hdd4;

    /** The m_hdd5. */
    private Set<String> m_hdd5;

    /** The m_hdd6. */
    private Set<String> m_hdd6;

    /** The m_inputpower. */
    private Set<String> m_inputpower;

    /** The m_lease. */
    private Set<String> m_lease;

    /** The m_maintcontract. */
    private Set<String> m_maintcontract;

    /** The m_manufacturer. */
    private Set<String> m_manufacturer;

    /** The m_model number. */
    private Set<String> m_modelNumber;

    /** The m_notify category. */
    private Set<String> m_notifyCategory;

    /** The m_numpowersupplies. */
    private Set<String> m_numpowersupplies;

    /** The m_operating system. */
    private Set<String> m_operatingSystem;

    /** The m_poller category. */
    private Set<String> m_pollerCategory;

    /** The m_rack. */
    private Set<String> m_rack;

    /** The m_ram. */
    private Set<String> m_ram;

    /** The m_region. */
    private Set<String> m_region;

    /** The m_room. */
    private Set<String> m_room;

    /** The m_snmpcommunity. */
    private Set<String> m_snmpcommunity;

    /** The m_state. */
    private Set<String> m_state;

    /** The m_storagectrl. */
    private Set<String> m_storagectrl;

    /** The m_support phone. */
    private Set<String> m_supportPhone;

    /** The m_threshold category. */
    private Set<String> m_thresholdCategory;

    /** The m_vendor. */
    private Set<String> m_vendor;

    /** The m_vendor fax. */
    private Set<String> m_vendorFax;

    /** The m_vendor phone. */
    private Set<String> m_vendorPhone;

    /** The m_zip. */
    private Set<String> m_zip;

    /** VMware managed Object ID. */
    private Set<String> m_vmwareManagedObjectId;

    /** VMware managed entity Type (virtualMachine | hostSystem). */
    private Set<String> m_vmwareManagedEntityType;

    /** VMware management Server. */
    private Set<String> m_vmwareManagementServer;

    /** VMware topology info. */
    private Set<String> m_vmwareTopologyInfo;

    /** VMware managed entity state. */
    private Set<String> m_vmwareState;

    /**
     * Instantiates a new asset sugg command.
     */
    public AssetSuggCommand() {
        m_additionalhardware = new TreeSet<String>();
        m_address1 = new TreeSet<String>();
        m_address2 = new TreeSet<String>();
        m_admin = new TreeSet<String>();
        m_building = new TreeSet<String>();
        m_category = new TreeSet<String>();
        m_circuitId = new TreeSet<String>();
        m_city = new TreeSet<String>();
        m_cpu = new TreeSet<String>();
        m_country = new TreeSet<String>();
        m_department = new TreeSet<String>();
        m_description = new TreeSet<String>();
        m_displayCategory = new TreeSet<String>();
        m_division = new TreeSet<String>();
        m_floor = new TreeSet<String>();
        m_hdd1 = new TreeSet<String>();
        m_hdd2 = new TreeSet<String>();
        m_hdd3 = new TreeSet<String>();
        m_hdd4 = new TreeSet<String>();
        m_hdd5 = new TreeSet<String>();
        m_hdd6 = new TreeSet<String>();
        m_inputpower = new TreeSet<String>();
        m_lease = new TreeSet<String>();
        m_maintcontract = new TreeSet<String>();
        m_manufacturer = new TreeSet<String>();
        m_modelNumber = new TreeSet<String>();
        m_notifyCategory = new TreeSet<String>();
        m_numpowersupplies = new TreeSet<String>();
        m_operatingSystem = new TreeSet<String>();
        m_pollerCategory = new TreeSet<String>();
        m_rack = new TreeSet<String>();
        m_ram = new TreeSet<String>();
        m_region = new TreeSet<String>();
        m_room = new TreeSet<String>();
        m_snmpcommunity = new TreeSet<String>();
        m_state = new TreeSet<String>();
        m_storagectrl = new TreeSet<String>();
        m_supportPhone = new TreeSet<String>();
        m_thresholdCategory = new TreeSet<String>();
        m_vendor = new TreeSet<String>();
        m_vendorFax = new TreeSet<String>();
        m_vendorPhone = new TreeSet<String>();
        m_zip = new TreeSet<String>();
        m_vmwareManagedObjectId = new TreeSet<String>();
        m_vmwareManagedEntityType = new TreeSet<String>();
        m_vmwareManagementServer = new TreeSet<String>();
        m_vmwareTopologyInfo = new TreeSet<String>();
        m_vmwareState = new TreeSet<String>();
        initUnchangedEntry();
    }

    /**
     * Adds the additionalhardware.
     *
     * @param additionalhardware
     *            the additionalhardware
     */
    public void addAdditionalhardware(String additionalhardware) {
        if ((additionalhardware != null) && !"".equals(additionalhardware)) {
            m_additionalhardware.add(additionalhardware);
        }
    }

    /**
     * Adds the address1.
     *
     * @param address1
     *            the address1
     */
    public void addAddress1(String address1) {
        if ((address1 != null) && !"".equals(address1)) {
            m_address1.add(address1);
        }
    }

    /**
     * Adds the address2.
     *
     * @param address2
     *            the address2
     */
    public void addAddress2(String address2) {
        if ((address2 != null) && !"".equals(address2)) {
            m_address2.add(address2);
        }
    }

    /**
     * Adds the admin.
     *
     * @param admin
     *            the admin
     */
    public void addAdmin(String admin) {
        if ((admin != null) && !"".equals(admin)) {
            m_admin.add(admin);
        }
    }

    /**
     * Adds the building.
     *
     * @param building
     *            the building
     */
    public void addBuilding(String building) {
        if ((building != null) && !"".equals(building)) {
            m_building.add(building);
        }
    }

    /**
     * Adds the category.
     *
     * @param category
     *            the category
     */
    public void addCategory(String category) {
        if ((category != null) && !"".equals(category)) {
            m_category.add(category);
        }
    }

    /**
     * Adds the circuit id.
     *
     * @param circuitId
     *            the circuit id
     */
    public void addCircuitId(String circuitId) {
        if ((circuitId != null) && !"".equals(circuitId)) {
            m_circuitId.add(circuitId);
        }
    }

    /**
     * Adds the city.
     *
     * @param city
     *            the city
     */
    public void addCity(String city) {
        if ((city != null) && !"".equals(city)) {
            m_city.add(city);
        }
    }

    /**
     * Adds the cpu.
     *
     * @param cpu
     *            the cpu
     */
    public void addCpu(String cpu) {
        if ((cpu != null) && !"".equals(cpu)) {
            m_cpu.add(cpu);
        }
    }

    /**
     * Adds the country.
     *
     * @param country
     *            the country
     */
    public void addCountry(String country) {
        if ((country != null) && !"".equals(country)) {
            m_country.add(country);
        }
    }

    /**
     * Adds the department.
     *
     * @param department
     *            the department
     */
    public void addDepartment(String department) {
        if ((department != null) && !"".equals(department)) {
            m_department.add(department);
        }
    }

    /**
     * Adds the description.
     *
     * @param description
     *            the description
     */
    public void addDescription(String description) {
        if ((description != null) && !"".equals(description)) {
            m_description.add(description);
        }
    }

    /**
     * Adds the display category.
     *
     * @param displayCategory
     *            the display category
     */
    public void addDisplayCategory(String displayCategory) {
        if ((displayCategory != null) && !"".equals(displayCategory)) {
            m_displayCategory.add(displayCategory);
        }
    }

    /**
     * Adds the division.
     *
     * @param division
     *            the division
     */
    public void addDivision(String division) {
        if ((division != null) && !"".equals(division)) {
            m_division.add(division);
        }
    }

    /**
     * Adds the floor.
     *
     * @param floor
     *            the floor
     */
    public void addFloor(String floor) {
        if ((floor != null) && !"".equals(floor)) {
            m_floor.add(floor);
        }
    }

    /**
     * Adds the hdd1.
     *
     * @param hdd1
     *            the hdd1
     */
    public void addHdd1(String hdd1) {
        if ((hdd1 != null) && !"".equals(hdd1)) {
            m_hdd1.add(hdd1);
        }
    }

    /**
     * Adds the hdd2.
     *
     * @param hdd2
     *            the hdd2
     */
    public void addHdd2(String hdd2) {
        if ((hdd2 != null) && !"".equals(hdd2)) {
            m_hdd2.add(hdd2);
        }
    }

    /**
     * Adds the hdd3.
     *
     * @param hdd3
     *            the hdd3
     */
    public void addHdd3(String hdd3) {
        if ((hdd3 != null) && !"".equals(hdd3)) {
            m_hdd3.add(hdd3);
        }
    }

    /**
     * Adds the hdd4.
     *
     * @param hdd4
     *            the hdd4
     */
    public void addHdd4(String hdd4) {
        if ((hdd4 != null) && !"".equals(hdd4)) {
            m_hdd4.add(hdd4);
        }
    }

    /**
     * Adds the hdd5.
     *
     * @param hdd5
     *            the hdd5
     */
    public void addHdd5(String hdd5) {
        if ((hdd5 != null) && !"".equals(hdd5)) {
            m_hdd5.add(hdd5);
        }
    }

    /**
     * Adds the hdd6.
     *
     * @param hdd6
     *            the hdd6
     */
    public void addHdd6(String hdd6) {
        if ((hdd6 != null) && !"".equals(hdd6)) {
            m_hdd6.add(hdd6);
        }
    }

    /**
     * Adds the inputpower.
     *
     * @param inputpower
     *            the inputpower
     */
    public void addInputpower(String inputpower) {
        if ((inputpower != null) && !"".equals(inputpower)) {
            m_inputpower.add(inputpower);
        }
    }

    /**
     * Adds the lease.
     *
     * @param lease
     *            the lease
     */
    public void addLease(String lease) {
        if ((lease != null) && !"".equals(lease)) {
            m_lease.add(lease);
        }
    }

    /**
     * Adds the maintcontract.
     *
     * @param maintcontract
     *            the maintcontract
     */
    public void addMaintcontract(String maintcontract) {
        if ((maintcontract != null) && !"".equals(maintcontract)) {
            m_maintcontract.add(maintcontract);
        }
    }

    /**
     * Adds the manufacturer.
     *
     * @param manufacturer
     *            the manufacturer
     */
    public void addManufacturer(String manufacturer) {
        if ((manufacturer != null) && !"".equals(manufacturer)) {
            m_manufacturer.add(manufacturer);
        }
    }

    /**
     * Adds the model number.
     *
     * @param modelNumber
     *            the model number
     */
    public void addModelNumber(String modelNumber) {
        if ((modelNumber != null) && !"".equals(modelNumber)) {
            m_modelNumber.add(modelNumber);
        }
    }

    /**
     * Adds the notify category.
     *
     * @param notifyCategory
     *            the notify category
     */
    public void addNotifyCategory(String notifyCategory) {
        if ((notifyCategory != null) && !"".equals(notifyCategory)) {
            m_notifyCategory.add(notifyCategory);
        }
    }

    /**
     * Adds the numpowersupplies.
     *
     * @param numpowersupplies
     *            the numpowersupplies
     */
    public void addNumpowersupplies(String numpowersupplies) {
        if ((numpowersupplies != null) && !"".equals(numpowersupplies)) {
            m_numpowersupplies.add(numpowersupplies);
        }
    }

    /**
     * Adds the operating system.
     *
     * @param operatingSystem
     *            the operating system
     */
    public void addOperatingSystem(String operatingSystem) {
        if ((operatingSystem != null) && !"".equals(operatingSystem)) {
            m_operatingSystem.add(operatingSystem);
        }
    }

    /**
     * Adds the poller category.
     *
     * @param pollerCategory
     *            the poller category
     */
    public void addPollerCategory(String pollerCategory) {
        if ((pollerCategory != null) && !"".equals(pollerCategory)) {
            m_pollerCategory.add(pollerCategory);
        }
    }

    /**
     * Adds the rack.
     *
     * @param rack
     *            the rack
     */
    public void addRack(String rack) {
        if ((rack != null) && !"".equals(rack)) {
            m_rack.add(rack);
        }
    }

    /**
     * Adds the ram.
     *
     * @param ram
     *            the ram
     */
    public void addRam(String ram) {
        if ((ram != null) && !"".equals(ram)) {
            m_ram.add(ram);
        }
    }

    /**
     * Adds the region.
     *
     * @param region
     *            the region
     */
    public void addRegion(String region) {
        if ((region != null) && !"".equals(region)) {
            m_region.add(region);
        }
    }

    /**
     * Adds the room.
     *
     * @param room
     *            the room
     */
    public void addRoom(String room) {
        if ((room != null) && !"".equals(room)) {
            m_room.add(room);
        }
    }

    /**
     * Adds the snmpcommunity.
     *
     * @param snmpcommunity
     *            the snmpcommunity
     */
    public void addSnmpcommunity(String snmpcommunity) {
        if ((snmpcommunity != null) && !"".equals(snmpcommunity)) {
            m_snmpcommunity.add(snmpcommunity);
        }
    }

    /**
     * Adds the state.
     *
     * @param state
     *            the state
     */
    public void addState(String state) {
        if ((state != null) && !"".equals(state)) {
            m_state.add(state);
        }
    }

    /**
     * Adds the storagectrl.
     *
     * @param storagectrl
     *            the storagectrl
     */
    public void addStoragectrl(String storagectrl) {
        if ((storagectrl != null) && !"".equals(storagectrl)) {
            m_storagectrl.add(storagectrl);
        }
    }

    /**
     * Adds the support phone.
     *
     * @param supportPhone
     *            the support phone
     */
    public void addSupportPhone(String supportPhone) {
        if ((supportPhone != null) && !"".equals(supportPhone)) {
            m_supportPhone.add(supportPhone);
        }
    }

    /**
     * Adds the threshold category.
     *
     * @param thresholdCategory
     *            the threshold category
     */
    public void addThresholdCategory(String thresholdCategory) {
        if ((thresholdCategory != null) && !"".equals(thresholdCategory)) {
            m_thresholdCategory.add(thresholdCategory);
        }
    }

    /**
     * Adds the vendor.
     *
     * @param vendor
     *            the vendor
     */
    public void addVendor(String vendor) {
        if ((vendor != null) && !"".equals(vendor)) {
            m_vendor.add(vendor);
        }
    }

    /**
     * Adds the vendor fax.
     *
     * @param vendorFax
     *            the vendor fax
     */
    public void addVendorFax(String vendorFax) {
        if ((vendorFax != null) && !"".equals(vendorFax)) {
            m_vendorFax.add(vendorFax);
        }
    }

    /**
     * Adds the vendor phone.
     *
     * @param vendorPhone
     *            the vendor phone
     */
    public void addVendorPhone(String vendorPhone) {
        if ((vendorPhone != null) && !"".equals(vendorPhone)) {
            m_vendorPhone.add(vendorPhone);
        }
    }

    /**
     * Adds the zip.
     *
     * @param zip
     *            the zip
     */
    public void addZip(String zip) {
        if ((zip != null) && !"".equals(zip)) {
            m_zip.add(zip);
        }
    }

    /**
     * Adds the vmware managed object id.
     *
     * @param vmwareManagedObjectId
     *            the vmware managed object id
     */
    public void addVmwareManagedObjectId(String vmwareManagedObjectId) {
        if ((vmwareManagedObjectId != null) && !"".equals(vmwareManagedObjectId)) {
            m_vmwareManagedObjectId.add(vmwareManagedObjectId);
        }
    }

    /**
     * Adds the vmware managed entity type.
     *
     * @param vmwareManagedEntityType
     *            the vmware managed entity type
     */
    public void addVmwareManagedEntityType(String vmwareManagedEntityType) {
        if ((vmwareManagedEntityType != null) && !"".equals(vmwareManagedEntityType)) {
            m_vmwareManagedEntityType.add(vmwareManagedEntityType);
        }
    }

    /**
     * Adds the vmware management server.
     *
     * @param vmwareManagementServer
     *            the vmware management server
     */
    public void addVmwareManagementServer(String vmwareManagementServer) {
        if ((vmwareManagementServer != null) && !"".equals(vmwareManagementServer)) {
            m_vmwareManagementServer.add(vmwareManagementServer);
        }
    }

    /**
     * Adds the vmware topology info.
     *
     * @param vmwareTopologyInfo
     *            the vmware topology info
     */
    public void addVmwareTopologyInfo(String vmwareTopologyInfo) {
        if ((vmwareTopologyInfo != null) && !"".equals(vmwareTopologyInfo)) {
            m_vmwareTopologyInfo.add(vmwareTopologyInfo);
        }
    }

    /**
     * Adds the vmware state.
     *
     * @param vmwareState
     *            the vmware state
     */
    public void addVmwareState(String vmwareState) {
        if ((vmwareState != null) && !"".equals(vmwareState)) {
            m_vmwareState.add(vmwareState);
        }
    }

    /**
     * Gets the additionalhardware.
     *
     * @return the additionalhardware
     */
    public Collection<String> getAdditionalhardware() {
        return m_additionalhardware;
    }

    /**
     * Gets the address1.
     *
     * @return the address1
     */
    public Collection<String> getAddress1() {
        return m_address1;
    }

    /**
     * Gets the address2.
     *
     * @return the address2
     */
    public Collection<String> getAddress2() {
        return m_address2;
    }

    /**
     * Gets the admin.
     *
     * @return the admin
     */
    public Collection<String> getAdmin() {
        return m_admin;
    }

    /**
     * Gets the building.
     *
     * @return the building
     */
    public Collection<String> getBuilding() {
        return m_building;
    }

    /**
     * Gets the category.
     *
     * @return the category
     */
    public Collection<String> getCategory() {
        return m_category;
    }

    /**
     * Gets the circuit id.
     *
     * @return the circuit id
     */
    public Collection<String> getCircuitId() {
        return m_circuitId;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public Collection<String> getCity() {
        return m_city;
    }

    /**
     * Gets the country.
     *
     * @return the country
     */
    public Collection<String> getCountry() {
        return m_country;
    }

    /**
     * Gets the cpu.
     *
     * @return the cpu
     */
    public Collection<String> getCpu() {
        return m_cpu;
    }

    /**
     * Gets the department.
     *
     * @return the department
     */
    public Collection<String> getDepartment() {
        return m_department;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public Collection<String> getDescription() {
        return m_description;
    }

    /**
     * Gets the display category.
     *
     * @return the display category
     */
    public Collection<String> getDisplayCategory() {
        return m_displayCategory;
    }

    /**
     * Gets the division.
     *
     * @return the division
     */
    public Collection<String> getDivision() {
        return m_division;
    }

    /**
     * Gets the floor.
     *
     * @return the floor
     */
    public Collection<String> getFloor() {
        return m_floor;
    }

    /**
     * Gets the hdd1.
     *
     * @return the hdd1
     */
    public Collection<String> getHdd1() {
        return m_hdd1;
    }

    /**
     * Gets the hdd2.
     *
     * @return the hdd2
     */
    public Collection<String> getHdd2() {
        return m_hdd2;
    }

    /**
     * Gets the hdd3.
     *
     * @return the hdd3
     */
    public Collection<String> getHdd3() {
        return m_hdd3;
    }

    /**
     * Gets the hdd4.
     *
     * @return the hdd4
     */
    public Collection<String> getHdd4() {
        return m_hdd4;
    }

    /**
     * Gets the hdd5.
     *
     * @return the hdd5
     */
    public Collection<String> getHdd5() {
        return m_hdd5;
    }

    /**
     * Gets the hdd6.
     *
     * @return the hdd6
     */
    public Collection<String> getHdd6() {
        return m_hdd6;
    }

    /**
     * Gets the inputpower.
     *
     * @return the inputpower
     */
    public Collection<String> getInputpower() {
        return m_inputpower;
    }

    /**
     * Gets the lease.
     *
     * @return the lease
     */
    public Collection<String> getLease() {
        return m_lease;
    }

    /**
     * Gets the maintcontract.
     *
     * @return the maintcontract
     */
    public Collection<String> getMaintcontract() {
        return m_maintcontract;
    }

    /**
     * Gets the manufacturer.
     *
     * @return the manufacturer
     */
    public Collection<String> getManufacturer() {
        return m_manufacturer;
    }

    /**
     * Gets the model number.
     *
     * @return the model number
     */
    public Collection<String> getModelNumber() {
        return m_modelNumber;
    }

    /**
     * Gets the notify category.
     *
     * @return the notify category
     */
    public Collection<String> getNotifyCategory() {
        return m_notifyCategory;
    }

    /**
     * Gets the numpowersupplies.
     *
     * @return the numpowersupplies
     */
    public Collection<String> getNumpowersupplies() {
        return m_numpowersupplies;
    }

    /**
     * Gets the operating system.
     *
     * @return the operating system
     */
    public Collection<String> getOperatingSystem() {
        return m_operatingSystem;
    }

    /**
     * Gets the poller category.
     *
     * @return the poller category
     */
    public Collection<String> getPollerCategory() {
        return m_pollerCategory;
    }

    /**
     * Gets the rack.
     *
     * @return the rack
     */
    public Collection<String> getRack() {
        return m_rack;
    }

    /**
     * Gets the ram.
     *
     * @return the ram
     */
    public Collection<String> getRam() {
        return m_ram;
    }

    /**
     * Gets the region.
     *
     * @return the region
     */
    public Collection<String> getRegion() {
        return m_region;
    }

    /**
     * Gets the room.
     *
     * @return the room
     */
    public Collection<String> getRoom() {
        return m_room;
    }

    /**
     * Gets the snmpcommunity.
     *
     * @return the snmpcommunity
     */
    public Collection<String> getSnmpcommunity() {
        return m_snmpcommunity;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public Collection<String> getState() {
        return m_state;
    }

    /**
     * Gets the storagectrl.
     *
     * @return the storagectrl
     */
    public Collection<String> getStoragectrl() {
        return m_storagectrl;
    }

    /**
     * Gets the support phone.
     *
     * @return the support phone
     */
    public Collection<String> getSupportPhone() {
        return m_supportPhone;
    }

    /**
     * Gets the threshold category.
     *
     * @return the threshold category
     */
    public Collection<String> getThresholdCategory() {
        return m_thresholdCategory;
    }

    /**
     * Gets the vendor.
     *
     * @return the vendor
     */
    public Collection<String> getVendor() {
        return m_vendor;
    }

    /**
     * Gets the vendor fax.
     *
     * @return the vendor fax
     */
    public Collection<String> getVendorFax() {
        return m_vendorFax;
    }

    /**
     * Gets the vendor phone.
     *
     * @return the vendor phone
     */
    public Collection<String> getVendorPhone() {
        return m_vendorPhone;
    }

    /**
     * Gets the zip.
     *
     * @return the zip
     */
    public Collection<String> getZip() {
        return m_zip;
    }

    /**
     * Gets the vmware managed object id.
     *
     * @return the vmware managed object id
     */
    public Collection<String> getVmwareManagedObjectId() {
        return m_vmwareManagedObjectId;
    }

    /**
     * Gets the vmware managed entity type.
     *
     * @return the vmware managed entity type
     */
    public Collection<String> getVmwareManagedEntityType() {
        return m_vmwareManagedEntityType;
    }

    /**
     * Gets the vmware management server.
     *
     * @return the vmware management server
     */
    public Collection<String> getVmwareManagementServer() {
        return m_vmwareManagementServer;
    }

    /**
     * Gets the vmware topology info.
     *
     * @return the vmware topology info
     */
    public Collection<String> getVmwareTopologyInfo() {
        return m_vmwareTopologyInfo;
    }

    /**
     * Gets the vmware state.
     *
     * @return the vmware state
     */
    public Collection<String> getVmwareState() {
        return m_vmwareState;
    }

    /**
     * Inits the unchanged entry.
     */
    private void initUnchangedEntry() {
        m_additionalhardware.add("");
        m_address1.add("");
        m_address2.add("");
        m_admin.add("");
        m_building.add("");
        m_category.add("");
        m_circuitId.add("");
        m_city.add("");
        m_cpu.add("");
        m_country.add("");
        m_department.add("");
        m_description.add("");
        m_displayCategory.add("");
        m_division.add("");
        m_floor.add("");
        m_hdd1.add("");
        m_hdd2.add("");
        m_hdd3.add("");
        m_hdd4.add("");
        m_hdd5.add("");
        m_hdd6.add("");
        m_inputpower.add("");
        m_lease.add("");
        m_maintcontract.add("");
        m_manufacturer.add("");
        m_modelNumber.add("");
        m_notifyCategory.add("");
        m_numpowersupplies.add("");
        m_operatingSystem.add("");
        m_pollerCategory.add("");
        m_rack.add("");
        m_ram.add("");
        m_region.add("");
        m_room.add("");
        m_snmpcommunity.add("");
        m_state.add("");
        m_storagectrl.add("");
        m_supportPhone.add("");
        m_thresholdCategory.add("");
        m_vendor.add("");
        m_vendorFax.add("");
        m_vendorPhone.add("");
        m_zip.add("");
        m_vmwareManagedObjectId.add("");
        m_vmwareManagedEntityType.add("");
        m_vmwareManagementServer.add("");
        m_vmwareTopologyInfo.add("");
        m_vmwareState.add("");
    }
}
