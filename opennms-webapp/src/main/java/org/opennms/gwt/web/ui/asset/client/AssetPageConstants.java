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

package org.opennms.gwt.web.ui.asset.client;

/**
 * The Interface AssetPageConstants.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 *         Basic static string i18n mechanism by GWT. Just add:
 *         DefaultStringValue("English default String")
 *         Key("Key to map value to the translated property files") String
 *         myI18nString() method to get the i18n string
 */
public interface AssetPageConstants extends com.google.gwt.i18n.client.Constants {

    /**
     * Additionalhardware.
     *
     * @return the string
     */
    @DefaultStringValue("Additional hardware")
    @Key("additionalhardware")
    String additionalhardware();

    /**
     * Additionalhardware help.
     *
     * @return the string
     */
    @DefaultStringValue("Additional hardware")
    @Key("additionalhardwareHelp")
    String additionalhardwareHelp();

    /**
     * Address1.
     *
     * @return the string
     */
    @DefaultStringValue("Address 1")
    @Key("address1")
    String address1();

    /**
     * Address1 help.
     *
     * @return the string
     */
    @DefaultStringValue("Street address of this device (For technician dispatch)")
    @Key("address1Help")
    String address1Help();

    /**
     * Longitude.
     *
     * @return the string
     */
    @DefaultStringValue("Longitude")
    @Key("longitude")
    String longitude();

    /**
     * Longitude help.
     *
     * @return the string
     */
    @DefaultStringValue("Geo Longitude")
    @Key("longitudeHelp")
    String longitudeHelp();

    /**
     * Latitude.
     *
     * @return the string
     */
    @DefaultStringValue("Latitude")
    @Key("latitude")
    String latitude();

    /**
     * Latitude help.
     *
     * @return the string
     */
    @DefaultStringValue("Geo Latitude")
    @Key("latitudeHelp")
    String latitudeHelp();

    /**
     * Address2.
     *
     * @return the string
     */
    @DefaultStringValue("Address 2")
    @Key("address2")
    String address2();

    /**
     * Address2 help.
     *
     * @return the string
     */
    @DefaultStringValue("Continuation of address")
    @Key("address2Help")
    String address2Help();

    /**
     * Admin.
     *
     * @return the string
     */
    @DefaultStringValue("Admin")
    @Key("admin")
    String admin();

    /**
     * Admin help.
     *
     * @return the string
     */
    @DefaultStringValue("Admin contact at the given location")
    @Key("adminHelp")
    String adminHelp();

    /**
     * Asset number.
     *
     * @return the string
     */
    @DefaultStringValue("Asset Number")
    @Key("assetNumber")
    String assetNumber();

    /**
     * Asset number help.
     *
     * @return the string
     */
    @DefaultStringValue("This field should be used if the organization employs asset tags for inventory purposes")
    @Key("assetNumberHelp")
    String assetNumberHelp();

    /**
     * Asset page not valid dont save.
     *
     * @return the string
     */
    @DefaultStringValue("Error saving: One or more fields contains an invalid value. Please correct your input and try again. ")
    @Key("assetPageNotValidDontSave")
    String assetPageNotValidDontSave();

    /* Authentication */
    /**
     * Authentication header.
     *
     * @return the string
     */
    @DefaultStringValue("Authentication")
    @Key("authenticationHeader")
    String authenticationHeader();

    /**
     * Auto enable.
     *
     * @return the string
     */
    @DefaultStringValue("AutoEnable")
    @Key("autoEnable")
    String autoEnable();

    /**
     * Auto enable help.
     *
     * @return the string
     */
    @DefaultStringValue("Whether the provided authentication user goes directly into 'enable' mode on this node upon login.")
    @Key("autoEnableHelp")
    String autoEnableHelp();

    /**
     * Building.
     *
     * @return the string
     */
    @DefaultStringValue("Building")
    @Key("building")
    String building();

    /**
     * Building help.
     *
     * @return the string
     */
    @DefaultStringValue("If this address is part of a complex/campus, this field allows additional granularity.")
    @Key("buildingHelp")
    String buildingHelp();

    /**
     * Category.
     *
     * @return the string
     */
    @DefaultStringValue("Category")
    @Key("category")
    String category();

    /**
     * Category help.
     *
     * @return the string
     */
    @DefaultStringValue("This column is to be used to specify what category of Network Element this device would fall into (E.G. WAN Router, Firewall, Etc.)")
    @Key("categoryHelp")
    String categoryHelp();

    /**
     * Circuit id.
     *
     * @return the string
     */
    @DefaultStringValue("Circuit ID")
    @Key("circuitId")
    String circuitId();

    /**
     * Circuit id help.
     *
     * @return the string
     */
    @DefaultStringValue("This field should contain the Circuit ID of the ISP/Carrier's designation to which this equipment terminates a connection. For DSL a phone number (or whatever identifying charistic of the LEC should be populated here) to this device's Voice or Data egress.")
    @Key("circuitIdHelp")
    String circuitIdHelp();

    /**
     * City.
     *
     * @return the string
     */
    @DefaultStringValue("City")
    @Key("city")
    String city();

    /**
     * City help.
     *
     * @return the string
     */
    @DefaultStringValue("Continuation of address")
    @Key("cityHelp")
    String cityHelp();

    /**
     * Comment.
     *
     * @return the string
     */
    @DefaultStringValue("Comment")
    @Key("comment")
    String comment();

    /**
     * Comment help.
     *
     * @return the string
     */
    @DefaultStringValue("Comments for this asset")
    @Key("commentHelp")
    String commentHelp();

    /* Comments */
    /**
     * Comments header.
     *
     * @return the string
     */
    @DefaultStringValue("Comments")
    @Key("commentsHeader")
    String commentsHeader();

    /* Configuration Categories */
    /**
     * Configuration cat header.
     *
     * @return the string
     */
    @DefaultStringValue("Configuration Categories")
    @Key("configurationCatHeader")
    String configurationCatHeader();

    /**
     * Connection.
     *
     * @return the string
     */
    @DefaultStringValue("Connection")
    @Key("connection")
    String connection();

    /**
     * Connection help.
     *
     * @return the string
     */
    @DefaultStringValue("Connection")
    @Key("connectionHelp")
    String connectionHelp();

    /**
     * Contract expires.
     *
     * @return the string
     */
    @DefaultStringValue("Contract Expires")
    @Key("contractExpires")
    String contractExpires();

    /**
     * Contract expires help.
     *
     * @return the string
     */
    @DefaultStringValue("Date when maintenance contract expires")
    @Key("contractExpiresHelp")
    String contractExpiresHelp();

    /**
     * Cpu.
     *
     * @return the string
     */
    @DefaultStringValue("CPU")
    @Key("cpu")
    String cpu();

    /**
     * Cpu help.
     *
     * @return the string
     */
    @DefaultStringValue("Type of CPU in this node")
    @Key("cpuHelp")
    String cpuHelp();

    /**
     * Custom.
     *
     * @return the string
     */
    @DefaultStringValue("Custom")
    @Key("custom")
    String custom();

    /**
     * Customer contract.
     *
     * @return the string
     */
    @DefaultStringValue("Customer contract")
    @Key("customerContract")
    String customerContract();

    /**
     * Customer contract exp.
     *
     * @return the string
     */
    @DefaultStringValue("Customer contract expires")
    @Key("customerContractExp")
    String customerContractExp();

    /**
     * Customer contract exp help.
     *
     * @return the string
     */
    @DefaultStringValue("Customer contract expires")
    @Key("customerContractExpHelp")
    String customerContractExpHelp();

    /**
     * Customer contract help.
     *
     * @return the string
     */
    @DefaultStringValue("Customer contract")
    @Key("customerContractHelp")
    String customerContractHelp();

    /* Customer */
    /**
     * Customer header.
     *
     * @return the string
     */
    @DefaultStringValue("Customer")
    @Key("customerHeader")
    String customerHeader();

    /**
     * Customer mail.
     *
     * @return the string
     */
    @DefaultStringValue("Customer mail")
    @Key("customerMail")
    String customerMail();

    /**
     * Customer mail help.
     *
     * @return the string
     */
    @DefaultStringValue("Customer mail")
    @Key("customerMailHelp")
    String customerMailHelp();

    /**
     * Customer name.
     *
     * @return the string
     */
    @DefaultStringValue("Customer name")
    @Key("customerName")
    String customerName();

    /**
     * Customer name help.
     *
     * @return the string
     */
    @DefaultStringValue("Customer name")
    @Key("customerNameHelp")
    String customerNameHelp();

    /**
     * Customer number.
     *
     * @return the string
     */
    @DefaultStringValue("Customer number")
    @Key("customerNumber")
    String customerNumber();

    /**
     * Customer number help.
     *
     * @return the string
     */
    @DefaultStringValue("Customer number")
    @Key("customerNumberHelp")
    String customerNumberHelp();

    /**
     * Customer phone.
     *
     * @return the string
     */
    @DefaultStringValue("Customer phone")
    @Key("customerPhone")
    String customerPhone();

    /**
     * Customer phone help.
     *
     * @return the string
     */
    @DefaultStringValue("Customer phone")
    @Key("customerPhoneHelp")
    String customerPhoneHelp();

    /* Custom */
    /**
     * Custom header.
     *
     * @return the string
     */
    @DefaultStringValue("Custom")
    @Key("customHeader")
    String customHeader();

    /**
     * Custom help.
     *
     * @return the string
     */
    @DefaultStringValue("Custom")
    @Key("customHelp")
    String customHelp();

    /**
     * Date installed.
     *
     * @return the string
     */
    @DefaultStringValue("Date Installed")
    @Key("dateInstalled")
    String dateInstalled();

    /**
     * Date installed help.
     *
     * @return the string
     */
    @DefaultStringValue("A handy dandy place to keep the date this equipment went into service, just in case the bean counters or your boss ask you for any particulars on this device")
    @Key("dateInstalledHelp")
    String dateInstalledHelp();

    /**
     * Department.
     *
     * @return the string
     */
    @DefaultStringValue("Department")
    @Key("department")
    String department();

    /**
     * Department help.
     *
     * @return the string
     */
    @DefaultStringValue("More of the above, but more simple (E.G. Accounting, Collections, IT, Etc.)")
    @Key("departmentHelp")
    String departmentHelp();

    /**
     * Description.
     *
     * @return the string
     */
    @DefaultStringValue("Description")
    @Key("description")
    String description();

    /**
     * Description help.
     *
     * @return the string
     */
    @DefaultStringValue("Description of the device's purpose (E.G. Core P2P Router, Egress Internet Router, Etc.)")
    @Key("descriptionHelp")
    String descriptionHelp();

    /**
     * Display cat.
     *
     * @return the string
     */
    @DefaultStringValue("Display Category")
    @Key("displayCategory")
    String displayCat();

    /**
     * Display cat help.
     *
     * @return the string
     */
    @DefaultStringValue("This column is to be used to specify what category of Network Element this device would fall into (E.G. WAN Router, Firewall, Etc.).")
    @Key("displayCategoryHelp")
    String displayCatHelp();

    /**
     * Division.
     *
     * @return the string
     */
    @DefaultStringValue("Division")
    @Key("division")
    String division();

    /**
     * Division help.
     *
     * @return the string
     */
    @DefaultStringValue("Standard corporate mumbo jumbo for the bean counters getting ever more granular on where money gets spent. Populate as you fee fit, or as dictated.")
    @Key("divisionHelp")
    String divisionHelp();

    /**
     * Enable password.
     *
     * @return the string
     */
    @DefaultStringValue("Enable Password")
    @Key("enablePassword")
    String enablePassword();

    /**
     * Enable password help.
     *
     * @return the string
     */
    @DefaultStringValue("Enable Password: used only if AutoEnable is not set to 'A'")
    @Key("enablePasswordHelp")
    String enablePasswordHelp();

    /**
     * Error fatching asset data.
     *
     * @return the string
     */
    @DefaultStringValue("Error fetching asset data for node with ID: ")
    @Key("errorFatchingAssetData")
    String errorFatchingAssetData();

    /**
     * Error fetching asset sugg data.
     *
     * @return the string
     */
    @DefaultStringValue("Error fetching asset suggestion data for node ID: ")
    @Key("errorFetchingAssetSuggData")
    String errorFetchingAssetSuggData();

    /**
     * Error saving asset data.
     *
     * @return the string
     */
    @DefaultStringValue("Error saving asset data for node ID: ")
    @Key("errorSavingAssetData")
    String errorSavingAssetData();

    /**
     * Fax.
     *
     * @return the string
     */
    @DefaultStringValue("Fax")
    @Key("fax")
    String fax();

    /**
     * Fax help.
     *
     * @return the string
     */
    @DefaultStringValue("Fax number of the above vendor")
    @Key("faxHelp")
    String faxHelp();

    /**
     * Floor.
     *
     * @return the string
     */
    @DefaultStringValue("Floor")
    @Key("floor")
    String floor();

    /**
     * Floor help.
     *
     * @return the string
     */
    @DefaultStringValue("Floor on which this node is located, for technician dispatch.")
    @Key("floorHelp")
    String floorHelp();

    /* Hardware */
    /**
     * Hardware header.
     *
     * @return the string
     */
    @DefaultStringValue("Hardware")
    @Key("hardwareHeader")
    String hardwareHeader();

    /**
     * Hdd.
     *
     * @return the string
     */
    @DefaultStringValue("HDD")
    @Key("hdd")
    String hdd();

    /**
     * Hdd help.
     *
     * @return the string
     */
    @DefaultStringValue("Hard disk drive information")
    @Key("hddHelp")
    String hddHelp();

    /* Identification */
    /**
     * Identification header.
     *
     * @return the string
     */
    @DefaultStringValue("Identification")
    @Key("identificationHeader")
    String identificationHeader();

    /**
     * Info asset.
     *
     * @return the string
     */
    @DefaultStringValue("Asset Info of Node: ")
    @Key("infoAsset")
    String infoAsset();

    /**
     * Info asset loging.
     *
     * @return the string
     */
    @DefaultStringValue("Loading Asset Info of Node: ")
    @Key("infoAssetLoging")
    String infoAssetLoging();

    /**
     * Info asset restting.
     *
     * @return the string
     */
    @DefaultStringValue("Resetting Asset Info of Node: ")
    @Key("infoAssetRestting")
    String infoAssetRestting();

    /**
     * Info asset saved.
     *
     * @return the string
     */
    @DefaultStringValue("Saved Asset Info of Node: ")
    @Key("infoAssetSaved")
    String infoAssetSaved();

    /**
     * Info asset saving.
     *
     * @return the string
     */
    @DefaultStringValue("Saving Asset Info of Node: ")
    @Key("infoAssetSaving")
    String infoAssetSaving();

    /**
     * Inputpower.
     *
     * @return the string
     */
    @DefaultStringValue("Inputpower")
    @Key("inputpower")
    String inputpower();

    /**
     * Inputpower help.
     *
     * @return the string
     */
    @DefaultStringValue("Input power type")
    @Key("inputpowerHelp")
    String inputpowerHelp();

    /**
     * Last modified.
     *
     * @return the string
     */
    @DefaultStringValue("Last Modified: ")
    @Key("lastModified")
    String lastModified();

    /**
     * Lease.
     *
     * @return the string
     */
    @DefaultStringValue("Lease")
    @Key("lease")
    String lease();

    /**
     * Lease expires.
     *
     * @return the string
     */
    @DefaultStringValue("Lease Expires")
    @Key("leaseExpires")
    String leaseExpires();

    /**
     * Lease expires help.
     *
     * @return the string
     */
    @DefaultStringValue("If all goes according to plan, this should be a date after you’ve got new equipment commissioned to take over for the service this equipment provides")
    @Key("leaseExpiresHelp")
    String leaseExpiresHelp();

    /**
     * Lease help.
     *
     * @return the string
     */
    @DefaultStringValue("A nice spot to populate the name of the leasing company or lease ID for this equipment")
    @Key("leaseHelp")
    String leaseHelp();

    /**
     * Legend green.
     *
     * @return the string
     */
    @DefaultStringValue("Changed value, valid")
    @Key("legendGreen")
    String legendGreen();

    /**
     * Legend grey.
     *
     * @return the string
     */
    @DefaultStringValue("Already saved value")
    @Key("legendGrey")
    String legendGrey();

    /**
     * Legend headline.
     *
     * @return the string
     */
    @DefaultStringValue("legend")
    @Key("legendHeadline")
    String legendHeadline();

    /**
     * Legend red.
     *
     * @return the string
     */
    @DefaultStringValue("Changed value with error, can't be saved")
    @Key("legendRed")
    String legendRed();

    /**
     * Legend yellow.
     *
     * @return the string
     */
    @DefaultStringValue("Changed value with warning, save possible")
    @Key("legendYellow")
    String legendYellow();

    /* Location */
    /**
     * Location header.
     *
     * @return the string
     */
    @DefaultStringValue("Location")
    @Key("locationHeader")
    String locationHeader();

    /**
     * Maint contract.
     *
     * @return the string
     */
    @DefaultStringValue("Maint Contract Number")
    @Key("maintContract")
    String maintContract();

    /**
     * Maint contract help.
     *
     * @return the string
     */
    @DefaultStringValue("Number / ID of maintenance contract")
    @Key("maintContractHelp")
    String maintContractHelp();

    /**
     * Maint phone.
     *
     * @return the string
     */
    @DefaultStringValue("Maint Phone")
    @Key("maintPhone")
    String maintPhone();

    /**
     * Maint phone help.
     *
     * @return the string
     */
    @DefaultStringValue("Phone number for technical operational support for the device in question (Think Helpdesk, Phone Company, ISP NOC, Etc.)")
    @Key("maintPhoneHelp")
    String maintPhoneHelp();

    /**
     * Manufacturer.
     *
     * @return the string
     */
    @DefaultStringValue("Manufacturer")
    @Key("manufacturer")
    String manufacturer();

    /**
     * Manufacturer help.
     *
     * @return the string
     */
    @DefaultStringValue("Manufacturer -Self explanatory")
    @Key("manufacturerHelp")
    String manufacturerHelp();

    /**
     * Model number.
     *
     * @return the string
     */
    @DefaultStringValue("Model Number")
    @Key("modelNumber")
    String modelNumber();

    /**
     * Model number help.
     *
     * @return the string
     */
    @DefaultStringValue("Model number of the device (E.G. Cisco 3845, Oki B4400, Etc.)")
    @Key("modelNumberHelp")
    String modelNumberHelp();

    /**
     * Node id label.
     *
     * @return the string
     */
    @DefaultStringValue("Node ID: ")
    @Key("nodeIdLabel")
    String nodeIdLabel();

    /**
     * Node info link.
     *
     * @return the string
     */
    @DefaultStringValue("General Information")
    @Key("nodeInfoLink")
    String nodeInfoLink();

    /**
     * Node param not valid int.
     *
     * @return the string
     */
    @DefaultStringValue("Parameter node is not an parseable Node ID: ")
    @Key("nodeParamNotValidInt")
    String nodeParamNotValidInt();

    /**
     * Notification cat.
     *
     * @return the string
     */
    @DefaultStringValue("Notification Category")
    @Key("notificationCategory")
    String notificationCat();

    /**
     * Notification cat help.
     *
     * @return the string
     */
    @DefaultStringValue("This could be something like 'serverAdmin' or 'networkAdmin' to be used in filter rules for directing notifications.")
    @Key("notificationCategoryHelp")
    String notificationCatHelp();

    /**
     * Numpowersupplies.
     *
     * @return the string
     */
    @DefaultStringValue("Number of power supplies")
    @Key("numpowersupplies")
    String numpowersupplies();

    /**
     * Numpowersupplies help.
     *
     * @return the string
     */
    @DefaultStringValue("Number of power supplies")
    @Key("numpowersuppliesHelp")
    String numpowersuppliesHelp();

    /**
     * Operating system.
     *
     * @return the string
     */
    @DefaultStringValue("Operating System")
    @Key("operatingSystem")
    String operatingSystem();

    /**
     * Operating system help.
     *
     * @return the string
     */
    @DefaultStringValue("Self explanatory")
    @Key("operatingSystemHelp")
    String operatingSystemHelp();

    /**
     * Password.
     *
     * @return the string
     */
    @DefaultStringValue("Password")
    @Key("password")
    String password();

    /**
     * Password help.
     *
     * @return the string
     */
    @DefaultStringValue("Password")
    @Key("passwordHelp")
    String passwordHelp();

    /**
     * Phone.
     *
     * @return the string
     */
    @DefaultStringValue("Phone")
    @Key("phone")
    String phone();

    /**
     * Phone help.
     *
     * @return the string
     */
    @DefaultStringValue("Phone number of vendor that services (or provides service to) this equipment (E.G. ISP, PBX Vendor, Phone company, etc.)")
    @Key("phoneHelp")
    String phoneHelp();

    /**
     * Poller cat.
     *
     * @return the string
     */
    @DefaultStringValue("Poller Category")
    @Key("pollerCategory")
    String pollerCat();

    /**
     * Poller cat help.
     *
     * @return the string
     */
    @DefaultStringValue("This is to be used in filter rules to define devices in a particular poller package.")
    @Key("pollerCategoryHelp")
    String pollerCatHelp();

    /**
     * Port.
     *
     * @return the string
     */
    @DefaultStringValue("Port")
    @Key("port")
    String port();

    /**
     * Port help.
     *
     * @return the string
     */
    @DefaultStringValue("Port on a given card or device being monitored")
    @Key("portHelp")
    String portHelp();

    /**
     * Rack.
     *
     * @return the string
     */
    @DefaultStringValue("Rack")
    @Key("rack")
    String rack();

    /**
     * Rack help.
     *
     * @return the string
     */
    @DefaultStringValue("This field should be used to designate the rack in specific that this piece of equipment is located in at a given location (E.G. Server3; Network5; Isle C-Bay5;105.12, Etc.) preferably by using both Bay & Isle coordinates")
    @Key("rackHelp")
    String rackHelp();

    /**
     * Rack unit height.
     *
     * @return the string
     */
    @DefaultStringValue("Rack unit height")
    @Key("rackUnitHeight")
    String rackUnitHeight();

    /**
     * Rack unit height help.
     *
     * @return the string
     */
    @DefaultStringValue("Rack unit height of node: 1, 2, 3, ...")
    @Key("rackUnitHeightHelp")
    String rackUnitHeightHelp();

    /**
     * Ram.
     *
     * @return the string
     */
    @DefaultStringValue("RAM")
    @Key("ram")
    String ram();

    /**
     * Ram help.
     *
     * @return the string
     */
    @DefaultStringValue("RAM")
    @Key("ramHelp")
    String ramHelp();

    /**
     * Region.
     *
     * @return the string
     */
    @DefaultStringValue("Region")
    @Key("region")
    String region();

    /**
     * Region help.
     *
     * @return the string
     */
    @DefaultStringValue("On a geographically or otherwise determined regional basis")
    @Key("regionHelp")
    String regionHelp();

    /**
     * Reset button.
     *
     * @return the string
     */
    @DefaultStringValue("Reset")
    @Key("resetButton")
    String resetButton();

    /**
     * Room.
     *
     * @return the string
     */
    @DefaultStringValue("Room")
    @Key("room")
    String room();

    /**
     * Room help.
     *
     * @return the string
     */
    @DefaultStringValue("Room number where this node is located, for technician dispatch")
    @Key("roomHelp")
    String roomHelp();

    /* Submit */
    /**
     * Save button.
     *
     * @return the string
     */
    @DefaultStringValue("Save")
    @Key("saveButton")
    String saveButton();

    /**
     * Serial number.
     *
     * @return the string
     */
    @DefaultStringValue("Serial Number")
    @Key("serialNumber")
    String serialNumber();

    /**
     * Serial number help.
     *
     * @return the string
     */
    @DefaultStringValue("Self explanatory")
    @Key("serialNumberHelp")
    String serialNumberHelp();

    /**
     * Slot.
     *
     * @return the string
     */
    @DefaultStringValue("Slot")
    @Key("slot")
    String slot();

    /**
     * Slot help.
     *
     * @return the string
     */
    @DefaultStringValue("This field should be used to designate what slot in a chassis/shelf this node occupies")
    @Key("slotHelp")
    String slotHelp();

    /**
     * Snmpcommunity.
     *
     * @return the string
     */
    @DefaultStringValue("SNMP community")
    @Key("snmpcommunity")
    String snmpcommunity();

    /**
     * Snmpcommunity help.
     *
     * @return the string
     */
    @DefaultStringValue("SNMP community string")
    @Key("snmpcommunityHelp")
    String snmpcommunityHelp();

    /* SNMP Labels */
    /**
     * Snmp header.
     *
     * @return the string
     */
    @DefaultStringValue("SNMP Info")
    @Key("snmpHeader")
    String snmpHeader();

    /**
     * State.
     *
     * @return the string
     */
    @DefaultStringValue("State")
    @Key("state")
    String state();

    /**
     * State help.
     *
     * @return the string
     */
    @DefaultStringValue("Continuation of address")
    @Key("stateHelp")
    String stateHelp();

    /**
     * Storagectrl.
     *
     * @return the string
     */
    @DefaultStringValue("Storage Controller")
    @Key("storagectrl")
    String storagectrl();

    /**
     * Storagectrl help.
     *
     * @return the string
     */
    @DefaultStringValue("Storage Controller")
    @Key("storagectrlHelp")
    String storagectrlHelp();

    /**
     * String not a date.
     *
     * @return the string
     */
    @DefaultStringValue("Can't read given text as date. Please use the date picker.")
    @Key("stringNotADate")
    String stringNotADate();

    /**
     * String no valid integer.
     *
     * @return the string
     */
    @DefaultStringValue("Input is not an parseable as Integer: ")
    @Key("stringNoValidInteger")
    String stringNoValidInteger();

    /**
     * String to long error.
     *
     * @return the string
     */
    @DefaultStringValue("The text is too long; maximum length is: ")
    @Key("stringToLongError")
    String stringToLongError();

    /**
     * String basic validation error.
     *
     * @return the string
     */
    @DefaultStringValue("Please just use A-Z a-z 0-9 or - and _ to avoid configuration problems")
    @Key("stringBasicValidationError")
    String stringBasicValidationError();

    /**
     * String contains white spaces error.
     *
     * @return the string
     */
    @DefaultStringValue("The text contains whitespaces; please remove them")
    @Key("stringContainsWhiteSpacesError")
    String stringContainsWhiteSpacesError();

    /**
     * String not matching regexp error.
     *
     * @return the string
     */
    @DefaultStringValue("The text dosen't maches the ergexp; ")
    @Key("stringNotMatchingRegexpError")
    String stringNotMatchingRegexpError();

    /**
     * System contact.
     *
     * @return the string
     */
    @DefaultStringValue("System Contact")
    @Key("systemContact")
    String systemContact();

    /**
     * System contact help.
     *
     * @return the string
     */
    @DefaultStringValue("System Contact, information from SNMP agent")
    @Key("systemContactHelp")
    String systemContactHelp();

    /**
     * System description.
     *
     * @return the string
     */
    @DefaultStringValue("System Description")
    @Key("systemDescription")
    String systemDescription();

    /**
     * System description help.
     *
     * @return the string
     */
    @DefaultStringValue("System Description, information from SNMP agent")
    @Key("systemDescriptionHelp")
    String systemDescriptionHelp();

    /**
     * System id.
     *
     * @return the string
     */
    @DefaultStringValue("System Id")
    @Key("systemId")
    String systemId();

    /**
     * System id help.
     *
     * @return the string
     */
    @DefaultStringValue("System ID, information from SNMP agent")
    @Key("systemIdHelp")
    String systemIdHelp();

    /**
     * System location.
     *
     * @return the string
     */
    @DefaultStringValue("System Location")
    @Key("systemLocation")
    String systemLocation();

    /**
     * System location help.
     *
     * @return the string
     */
    @DefaultStringValue("System Location, information from SNMP agent")
    @Key("systemLocationHelp")
    String systemLocationHelp();

    /**
     * System name.
     *
     * @return the string
     */
    @DefaultStringValue("System Name")
    @Key("systemName")
    String systemName();

    /**
     * System name help.
     *
     * @return the string
     */
    @DefaultStringValue("System Name, information from SNMP agent")
    @Key("systemNameHelp")
    String systemNameHelp();

    /**
     * Threshold cat.
     *
     * @return the string
     */
    @DefaultStringValue("Threshold Category")
    @Key("thresholdCategory")
    String thresholdCat();

    /**
     * Threshold cat help.
     *
     * @return the string
     */
    @DefaultStringValue("This is to be used in filter rules to define devices in a particular thresholding package.")
    @Key("thresholdCategoryHelp")
    String thresholdCatHelp();

    /**
     * Username.
     *
     * @return the string
     */
    @DefaultStringValue("Username")
    @Key("username")
    String username();

    /**
     * Username help.
     *
     * @return the string
     */
    @DefaultStringValue("Username")
    @Key("usernameHelp")
    String usernameHelp();

    /**
     * Vendor asset.
     *
     * @return the string
     */
    @DefaultStringValue("Vendor Asset")
    @Key("vendorAsset")
    String vendorAsset();

    /**
     * Vendor asset help.
     *
     * @return the string
     */
    @DefaultStringValue("If the vendor that supplies this equipment uses a asset tag of their own, populate that data here")
    @Key("vendorAssetHelp")
    String vendorAssetHelp();

    /* Vendor */
    /**
     * Vendor header.
     *
     * @return the string
     */
    @DefaultStringValue("Vendor")
    @Key("vendorHeader")
    String vendorHeader();

    /**
     * Vendor name.
     *
     * @return the string
     */
    @DefaultStringValue("Name")
    @Key("vendorName")
    String vendorName();

    /**
     * Vendor name help.
     *
     * @return the string
     */
    @DefaultStringValue("Vendor who provides service for this device (If applicable, E.G. ISP, Local PBX Maintenance vendor, etc.)")
    @Key("vendorNameHelp")
    String vendorNameHelp();

    /**
     * Zip.
     *
     * @return the string
     */
    @DefaultStringValue("ZIP")
    @Key("zip")
    String zip();

    /**
     * Zip help.
     *
     * @return the string
     */
    @DefaultStringValue("Postal code (ZIP code)")
    @Key("zipHelp")
    String zipHelp();

    /**
     * Country.
     *
     * @return the string
     */
    @DefaultStringValue("Country")
    @Key("country")
    String country();

    /**
     * Country help.
     *
     * @return the string
     */
    @DefaultStringValue("Country")
    @Key("countryHelp")
    String countryHelp();

    /* VMware asset fields */
    /**
     * Vmware header.
     *
     * @return the string
     */
    @DefaultStringValue("VMware")
    @Key("vmwareHeader")
    String vmwareHeader();

    /**
     * Vmware managed object id.
     *
     * @return the string
     */
    @DefaultStringValue("VMware managed object ID")
    @Key("vmwareManagedObjectId")
    String vmwareManagedObjectId();

    /**
     * Vmware managed object id help.
     *
     * @return the string
     */
    @DefaultStringValue("Internal id in VMware vCenter")
    @Key("vmwareManagedObjectIdHelp")
    String vmwareManagedObjectIdHelp();

    /**
     * Vmware managed entity type.
     *
     * @return the string
     */
    @DefaultStringValue("VMware managed entity type")
    @Key("vmwareManagedEntityType")
    String vmwareManagedEntityType();

    /**
     * Vmware managed entity type help.
     *
     * @return the string
     */
    @DefaultStringValue("Defines a VMware host system or virtual machine")
    @Key("vmwareManagedEntityTypeHelp")
    String vmwareManagedEntityTypeHelp();

    /**
     * Vmware management server.
     *
     * @return the string
     */
    @DefaultStringValue("VMware management server")
    @Key("vmwareManagementServer")
    String vmwareManagementServer();

    /**
     * Vmware management server help.
     *
     * @return the string
     */
    @DefaultStringValue("VMware vCenter host")
    @Key("vmwareManagementServerHelp")
    String vmwareManagementServerHelp();

    /**
     * Vmware topology info.
     *
     * @return the string
     */
    @DefaultStringValue("VMware Topology Info")
    @Key("vmwareTopologyInfo")
    String vmwareTopologyInfo();

    /**
     * Vmware topology info help.
     *
     * @return the string
     */
    @DefaultStringValue("VMware topology information")
    @Key("vmwareTopologyInfoHelp")
    String vmwareTopologyInfoHelp();

    /**
     * Vmware state.
     *
     * @return the string
     */
    @DefaultStringValue("VMware state")
    @Key("vmwareState")
    String vmwareState();

    /**
     * Vmware state help.
     *
     * @return the string
     */
    @DefaultStringValue("VMware managed entity state")
    @Key("vmwareStateHelp")
    String vmwareStateHelp();
}
