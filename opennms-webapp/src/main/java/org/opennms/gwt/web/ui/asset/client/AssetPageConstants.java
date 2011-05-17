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

package org.opennms.gwt.web.ui.asset.client;
/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public interface AssetPageConstants extends com.google.gwt.i18n.client.Constants {
	
	@DefaultStringValue("Node Id: ")
	@Key("nodeIdLabel")
	String nodeIdLabel();
	
	@DefaultStringValue("General Information")
	@Key("nodeInfoLink")
	String nodeInfoLink();
	
	/* SNMP Labels */
	@DefaultStringValue("SNMP Info")
	@Key("snmpHeader")
	String snmpHeader();

	@DefaultStringValue("System Id")
	@Key("systemId")
	String systemId();

	@DefaultStringValue("System Name")
	@Key("systemName")
	String systemName();

	@DefaultStringValue("System Location")
	@Key("systemLocation")
	String systemLocation();

	@DefaultStringValue("System Contact")
	@Key("systemContact")
	String systemContact();

	@DefaultStringValue("System Description")
	@Key("systemDescription")
	String systemDescription();

	/* Configuration Categories */
	@DefaultStringValue("Configuration Categories")
	@Key("configurationCatHeader")
	String configurationCatHeader();

	@DefaultStringValue("Display Category")
	@Key("displayCategory")
	String displayCat();

	@DefaultStringValue("Notification Category")
	@Key("notificationCategory")
	String notificationCat();

	@DefaultStringValue("Poller Category")
	@Key("pollerCategory")
	String pollerCat();

	@DefaultStringValue("Threshold Category")
	@Key("thresholdCategory")
	String thresholdCat();

	@DefaultStringValue("This column is to be used to specify what category of Network Element this device would fall into (E.G. Wan Router, Firewall, Etc.)")
	@Key("displayCategoryHelp")
	String displayCatHelp();

	@DefaultStringValue("This could be something like 'serverAdmin' or 'networkAdmin' to be used for directing notifications.")
	@Key("notificationCategoryHelp")
	String notificationCatHelp();

	@DefaultStringValue("This is to be used to define devices in a particular poller package.")
	@Key("pollerCategoryHelp")
	String pollerCatHelp();

	@DefaultStringValue("This is to be used to define devices in a particular thresholding package.")
	@Key("thresholdCategoryHelp")
	String thresholdCatHelp();
	
	/* Identification */
	@DefaultStringValue("Identification")
	@Key("identificationHeader")
	String identificationHeader();

	@DefaultStringValue("Description")
	@Key("description")
	String description();

	@DefaultStringValue("Category")
	@Key("category")
	String category();

	@DefaultStringValue("Manufacturer")
	@Key("manufacturer")
	String manufacturer();

	@DefaultStringValue("Model Number")
	@Key("modelNumber")
	String modelNumber();

	@DefaultStringValue("Serial Number")
	@Key("serialNumber")
	String serialNumber();

	@DefaultStringValue("Asset Number")
	@Key("assetNumber")
	String assetNumber();

	@DefaultStringValue("Date Installed")
	@Key("dateInstalled")
	String dateInstalled();

	@DefaultStringValue("Operating System")
	@Key("operatingSystem")
	String operatingSystem();

	@DefaultStringValue("Description of the device's purpose (E.G. Core P2P Router, Egress Internet Router, Etc.)")
	@Key("descriptionHelp")
	String descriptionHelp();

	@DefaultStringValue("This column is to be used to specify what category of Network Element this device would fall into (E.G. Wan Router, Firewall, Etc.)")
	@Key("categoryHelp")
	String categoryHelp();

	@DefaultStringValue("Manufacturer -Self explanatory")
	@Key("manufacturerHelp")
	String manufacturerHelp();

	@DefaultStringValue("Model number of the device (E.G. Cisco 3845, Oki B4400, Etc.)")
	@Key("modelNumberHelp")
	String modelNumberHelp();

	@DefaultStringValue("Self explanatory")
	@Key("serialNumberHelp")
	String serialNumberHelp();

	@DefaultStringValue("This field should be used if the organization employs asset tags for inventory purposes")
	@Key("assetNumberHelp")
	String assetNumberHelp();

	@DefaultStringValue("Handy dandy place to keep the date this equipment went into service on just incase the bean counters, or your boss ask you for any particulars on this device")
	@Key("dateInstalledHelp")
	String dateInstalledHelp();

	@DefaultStringValue("Self explanatory")
	@Key("operatingSystemHelp")
	String operatingSystemHelp();
	
	/* Location */
	@DefaultStringValue("Location")
	@Key("locationHeader")
	String locationHeader();

	@DefaultStringValue("Region")
	@Key("region")
	String region();

	@DefaultStringValue("Division")
	@Key("division")
	String division();

	@DefaultStringValue("Department")
	@Key("department")
	String department();

	@DefaultStringValue("Address 1")
	@Key("address1")
	String address1();

	@DefaultStringValue("Address 2")
	@Key("address2")
	String address2();

	@DefaultStringValue("City")
	@Key("city")
	String city();

	@DefaultStringValue("State")
	@Key("state")
	String state();

	@DefaultStringValue("ZIP")
	@Key("zip")
	String zip();

	@DefaultStringValue("Building")
	@Key("building")
	String building();

	@DefaultStringValue("Floor")
	@Key("floor")
	String floor();

	@DefaultStringValue("Room")
	@Key("room")
	String room();

	@DefaultStringValue("Rack")
	@Key("rack")
	String rack();

	@DefaultStringValue("Rack unit height")
	@Key("rackUnitHeight")
	String rackUnitHeight();

	@DefaultStringValue("Slot")
	@Key("slot")
	String slot();

	@DefaultStringValue("Port")
	@Key("port")
	String port();

	@DefaultStringValue("CircuitId")
	@Key("circuitId")
	String circuitId();
	
	@DefaultStringValue("Admin")
	@Key("admin")
	String admin();

	@DefaultStringValue("On a geographically or otherwise determined regional basis")
	@Key("regionHelp")
	String regionHelp();

	@DefaultStringValue("Standard corporate mumbo jumbo for the bean counters getting more & more granular on where money gets spent, populate as you fee fit, or as dictated to")
	@Key("divisionHelp")
	String divisionHelp();

	@DefaultStringValue("More of the above, but more simple (E.G. Accounting, Collections, IT, Etc.)")
	@Key("departmentHelp")
	String departmentHelp();

	@DefaultStringValue("Street address of this device (This as if you were in a remote maintenance center & had to dispatch someone here)")
	@Key("address1Help")
	String address1Help();

	@DefaultStringValue("Continuation of address")
	@Key("address2Help")
	String address2Help();

	@DefaultStringValue("Continuation of address")
	@Key("cityHelp")
	String cityHelp();

	@DefaultStringValue("Continuation of address")
	@Key("stateHelp")
	String stateHelp();

	@DefaultStringValue("Zip -Zoning ImproveMent code, Yep -you guessed it…   more of the same")
	@Key("zipHelp")
	String zipHelp();

	@DefaultStringValue("If this address is part of a complex/campus this field allows additional granularity")
	@Key("buildingHelp")
	String buildingHelp();

	@DefaultStringValue("Were still in the dispatching frame of mind here")
	@Key("floorHelp")
	String floorHelp();

	@DefaultStringValue("Yes, were dialing the location down more after we had dialed it up a piece")
	@Key("roomHelp")
	String roomHelp();

	@DefaultStringValue("This field should be used to designate the rack in specific that this piece of equipment is located in at a given location (E.G. Server3; Network5; Isle C-Bay5;105.12, Etc.) preferably by using both Bay & Isle coordinates")
	@Key("rackHelp")
	String rackHelp();

	@DefaultStringValue("Rack unit height blockt in Rack by Node")
	@Key("rackUnitHeightHelp")
	String rackUnitHeightHelp();

	@DefaultStringValue("This field should be used to designate what slot in a chassis/shelf is being surveiled by this database entry (I feel as though this should be indicative of vertical elevation from the base of the rack as well)")
	@Key("slotHelp")
	String slotHelp();

	@DefaultStringValue("Port on a given card or device being surveiled")
	@Key("portHelp")
	String portHelp();

	@DefaultStringValue("This field should be the Circuit ID of the ISP/Carrier's designation that this equipment terminates a connection to, if DSL a Phone Number (or whatever identifying charistic of the LEC should be populated here) to this device's Voice or Data egress")
	@Key("circuitIdHelp")
	String circuitIdHelp();
	
	@DefaultStringValue("Admin of the given location")
	@Key("adminHelp")
	String adminHelp();
	
	/* Vendor */
	@DefaultStringValue("Vendor")
	@Key("vendorHeader")
	String vendorHeader();

	@DefaultStringValue("Name")
	@Key("vendorName")
	String vendorName();

	@DefaultStringValue("Phone")
	@Key("phone")
	String phone();

	@DefaultStringValue("Fax")
	@Key("fax")
	String fax();

	@DefaultStringValue("Lease")
	@Key("lease")
	String lease();

	@DefaultStringValue("Lease Expires")
	@Key("leaseExpires")
	String leaseExpires();

	@DefaultStringValue("Vendor Asset")
	@Key("vendorAsset")
	String vendorAsset();

	@DefaultStringValue("Maint Contract")
	@Key("maintContract")
	String maintContract();

	@DefaultStringValue("Contract Expires")
	@Key("contractExpires")
	String contractExpires();

	@DefaultStringValue("Maint Phone")
	@Key("maintPhone")
	String maintPhone();
	
	@DefaultStringValue("Vendor who provides service for this device (If applicable, E.G. ISP, Local PBX Maintenance vendor, etc.)")
	@Key("vendorNameHelp")
	String vendorNameHelp();

	@DefaultStringValue("Phone number of vendor that services (or provides service to) this equipment (E.G. ISP, PBX Vendor, Phone company, etc.)")
	@Key("phoneHelp")
	String phoneHelp();

	@DefaultStringValue("Fax number of the above vendor")
	@Key("faxHelp")
	String faxHelp();

	@DefaultStringValue("Im assuming that this would be a nice spot to populate the name of the leasing company for this equipment")
	@Key("leaseHelp")
	String leaseHelp();

	@DefaultStringValue("LeaseExpires -If all goes according to plan, this should be a date after you’ve got new equipment commissioned to take over for the service this equipment provides")
	@Key("leaseExpiresHelp")
	String leaseExpiresHelp();

	@DefaultStringValue("If the vendor that supplies this equipment uses a asset tag of their own, populate that data here")
	@Key("vendorAssetHelp")
	String vendorAssetHelp();

	@DefaultStringValue("MaintContract -Type of maintenance contract (E.G. Platinum Plus, Smartnet, Etc.)")
	@Key("maintContractHelp")
	String maintContractHelp();

	@DefaultStringValue("Date when maintenance contract expires")
	@Key("contractExpiresHelp")
	String contractExpiresHelp();

	@DefaultStringValue("Phone number for technical operational support for the device in question (Think Helpdesk, Phone Company, ISP NOC, Etc.)")
	@Key("maintPhoneHelp")
	String maintPhoneHelp();
	
	/* Authentication */
	@DefaultStringValue("Authentication")
	@Key("authenticationHeader")
	String authenticationHeader();

	@DefaultStringValue("Username")
	@Key("username")
	String username();

	@DefaultStringValue("Password")
	@Key("password")
	String password();

	@DefaultStringValue("Enable Password")
	@Key("enablePassword")
	String enablePassword();

	@DefaultStringValue("Connection")
	@Key("connection")
	String connection();

	@DefaultStringValue("AutoEnable")
	@Key("autoEnable")
	String autoEnable();
	
	@DefaultStringValue("SNMP community")
	@Key("snmpcommunity")
	String snmpcommunity();

	@DefaultStringValue("Username")
	@Key("usernameHelp")
	String usernameHelp();

	@DefaultStringValue("Password")
	@Key("passwordHelp")
	String passwordHelp();

	@DefaultStringValue("Enable Password")
	@Key("enablePasswordHelp")
	String enablePasswordHelp();

	@DefaultStringValue("Connection")
	@Key("connectionHelp")
	String connectionHelp();

	@DefaultStringValue("AutoEnable")
	@Key("autoEnableHelp")
	String autoEnableHelp();
	
	@DefaultStringValue("SNMP community")
	@Key("snmpcommunityHelp")
	String snmpcommunityHelp();
	
	/* Comments */
	@DefaultStringValue("Comments")
	@Key("commentsHeader")
	String commentsHeader();

	@DefaultStringValue("Comment")
	@Key("comment")
	String comment();
	
	@DefaultStringValue("Comments")
	@Key("commentHelp")
	String commentHelp();
	
	/* Submit */
	@DefaultStringValue("Save")
	@Key("saveButton")
	String saveButton();

	@DefaultStringValue("Reset")
	@Key("resetButton")
	String resetButton();
	
	/* Hardware */
	@DefaultStringValue("Hardware")
	@Key("hardwareHeader")
	String hardwareHeader();

	@DefaultStringValue("Cpu")
	@Key("cpu")
	String cpu();
	
	@DefaultStringValue("Ram")
	@Key("ram")
	String ram();

	@DefaultStringValue("Storage Controller")
	@Key("storagectrl")
	String storagectrl();
	
	@DefaultStringValue("HDD")
	@Key("hdd")
	String hdd();
	
	@DefaultStringValue("Additional hardware")
	@Key("additionalhardware")
	String additionalhardware();
	
	@DefaultStringValue("Number of power supplies")
	@Key("numpowersupplies")
	String numpowersupplies();
	
	@DefaultStringValue("Inputpower")
	@Key("inputpower")
	String inputpower();
	
	@DefaultStringValue("Cpu")
	@Key("cpuHelp")
	String cpuHelp();
	
	@DefaultStringValue("Ram")
	@Key("ramHelp")
	String ramHelp();

	@DefaultStringValue("Storage Controller")
	@Key("storagectrlHelp")
	String storagectrlHelp();
	
	@DefaultStringValue("HDD")
	@Key("hddHelp")
	String hddHelp();
	
	@DefaultStringValue("Additional hardware")
	@Key("additionalhardwareHelp")
	String additionalhardwareHelp();
	
	@DefaultStringValue("Number of power supplies")
	@Key("numpowersuppliesHelp")
	String numpowersuppliesHelp();
	
	@DefaultStringValue("Inputpower")
	@Key("inputpowerHelp")
	String inputpowerHelp();
	
	/* Custom */
	@DefaultStringValue("Custom")
	@Key("customHeader")
	String customHeader();
	
	@DefaultStringValue("Custom")
	@Key("custom")
	String custom();
	
	@DefaultStringValue("Custom")
	@Key("customHelp")
	String customHelp();
	
	/* Customer */
	@DefaultStringValue("Customer")
	@Key("customerHeader")
	String customerHeader();
	
	@DefaultStringValue("Customer name")
	@Key("customerName")
	String customerName();

	@DefaultStringValue("Customer number")
	@Key("customerNumber")
	String customerNumber();

	@DefaultStringValue("Customer phone")
	@Key("customerPhone")
	String customerPhone();

	@DefaultStringValue("Customer mail")
	@Key("customerMail")
	String customerMail();	
	
	@DefaultStringValue("Customer name")
	@Key("customerNameHelp")
	String customerNameHelp();

	@DefaultStringValue("Customer number")
	@Key("customerNumberHelp")
	String customerNumberHelp();

	@DefaultStringValue("Customer phone")
	@Key("customerPhoneHelp")
	String customerPhoneHelp();

	@DefaultStringValue("Customer mail")
	@Key("customerMailHelp")
	String customerMailHelp();
	
	@DefaultStringValue("Customer contract")
	@Key("customerContract")
	String customerContract();	
	
	@DefaultStringValue("Customer contract")
	@Key("customerContractHelp")
	String customerContractHelp();
	
	@DefaultStringValue("Customer contract expires")
	@Key("customerContractExp")
	String customerContractExp();	
	
	@DefaultStringValue("Customer contract expires")
	@Key("customerContractExpHelp")
	String customerContractExpHelp();

	@DefaultStringValue("Last Modified: ")
	@Key("lastModified")
	String lastModified();

	@DefaultStringValue("The text is to long, maximum is: ")
	@Key("stringToLongError")
	String stringToLongError();

	@DefaultStringValue("Can't read given text as date. Please use the Datepicker.")
	@Key("stringNotADate")
	String stringNotADate();

	@DefaultStringValue("Parameter node is not an parseabel NodeId: ")
	@Key("nodeParamNotValidInt")
	String nodeParamNotValidInt();

	@DefaultStringValue("Error fetching asset data for nodeId: ")
	@Key("errorFatchingAssetData")
	String errorFatchingAssetData();

	@DefaultStringValue("Error saveing asset data for nodeId: ")
	@Key("errorSavingAssetData")
	String errorSavingAssetData();

	@DefaultStringValue("Error fetching assetSuggestion data for nodeId: ")
	@Key("errorFetchingAssetSuggData")
	String errorFetchingAssetSuggData();

	@DefaultStringValue("Saving Asset Info of Node: ")
	@Key("infoAssetSaving")
	String infoAssetSaving();

	@DefaultStringValue("Resetting Asset Info of Node: ")
	@Key("infoAssetRestting")
	String infoAssetRestting();

	@DefaultStringValue("Asset Info of Node: ")
	@Key("infoAsset")
	String infoAsset();

	@DefaultStringValue("Loading Asset Info of Node: ")
	@Key("infoAssetLoging")
	String infoAssetLoging();

	@DefaultStringValue("Saved Asset Info of Node: ")
	@Key("infoAssetSaved")
	String infoAssetSaved();

	@DefaultStringValue("Sorry, can't save. Still Errors on fileds. Please fix your input. ")
	@Key("assetPageNotValidDontSave")
	String assetPageNotValidDontSave();
}
