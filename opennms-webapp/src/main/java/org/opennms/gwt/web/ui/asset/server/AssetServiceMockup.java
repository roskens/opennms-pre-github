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

package org.opennms.gwt.web.ui.asset.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.opennms.gwt.web.ui.asset.client.AssetService;
import org.opennms.gwt.web.ui.asset.shared.AssetCommand;
import org.opennms.gwt.web.ui.asset.shared.AssetSuggCommand;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 * 
 */
public class AssetServiceMockup extends RemoteServiceServlet implements AssetService {

	private static final long serialVersionUID = 386558445935186134L;

	private AssetCommand asset = new AssetCommand();
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private ArrayList<String> maunfacturers = new ArrayList<String>();
	
	public AssetServiceMockup() {
		maunfacturers.add("Atari");
		maunfacturers.add("Bell-Labs");
		maunfacturers.add("Comodore");
		maunfacturers.add("Dell");
		setData();
	}

	public AssetCommand getAssetByNodeId(int nodeId) throws Exception {
		if (nodeId == 666) {
			throw new NullPointerException("just testing error-case: nodeid 666");
		}

		return asset;
	}

	public Boolean saveOrUpdateAssetByNodeId(int nodeId, AssetCommand asset) {
		if (nodeId == 999) {
			return false;
		}

		this.asset = asset;
		asset.setLastModifiedDate(new Date());
		asset.setLastModifiedBy("admin");
		maunfacturers.add(this.asset.getManufacturer());
		return true;
	}

	public AssetCommand setData() {
		asset.setLoggedInUser("admin");
		saveDataSNMP();
		saveDataConfigCategories();
		saveDataIdentification();
		saveDataLocation();
		saveDataVendor();
		saveDataAuthentication();
		saveDataHardware();
		saveDataComments();
		asset.setLastModifiedBy("admin");
		asset.setLastModifiedDate(new Date());
		return asset;
	}

	private void saveDataSNMP() {
		asset.setSnmpSysObjectId(".1.3.6.1.4.1.8072.3.2.10");
		//asset.setSnmpSysObjectId("");
		asset.setSnmpSysName("tak-ThinkPad-X200s");
		asset.setSnmpSysLocation("Sitting on the Dock of the Bay");
		asset.setSnmpSysContact("Me <me@example.org>");
		asset.setSnmpSysDescription("Linux tak-ThinkPad-X200s 2.6.35-28-generic #50-Ubuntu SMP Fri Mar 18 18:42:20 UTC 2011 x86_64");
	}

	private void saveDataConfigCategories() {
		asset.setDisplayCategory("DisplayCategory");
		asset.setNotifyCategory("NotifyCategory");
		asset.setPollerCategory("PollerCategory");
		asset.setThresholdCategory("ThresholdCategory");
	}

	private void saveDataIdentification() {
		asset.setDescription("Description");
		asset.setCategory("Category");
		asset.setManufacturer("Manufacturer");
		asset.setModelNumber("ModelNumber");
		asset.setSerialNumber("SerialNumber");
		asset.setAssetNumber("AssetNumber");
		asset.setOperatingSystem("OperatingSystem");
		asset.setDateInstalled(formatter.format(new Date()));
	}

	private void saveDataLocation() {
		asset.setRegion("Region");
		asset.setDivision("Division");
		asset.setDepartment("Department");
		asset.setAddress1("Address1");
		asset.setAddress2("Address2");
		asset.setCity("City");
		asset.setState("State");
		asset.setZip("Zip");
		asset.setBuilding("Building");
		asset.setFloor("Floor");
		asset.setRoom("Room");
		asset.setRack("Rack");
		asset.setSlot("Slot");
		asset.setPort("Port");
		asset.setCircuitId("CircuitId");
	}

	private void saveDataVendor() {
		asset.setVendor("Vendor");
		asset.setVendorPhone("VendorPhone");
		asset.setVendorFax("VendorFax");
		asset.setLease("Lease");
		// asset.setLeaseExpires(formatter.format(new Date()));
		asset.setLeaseExpires("FooDate");
		asset.setVendorAssetNumber("VendorAssetNumber");
		asset.setMaintContractNumber("MaintContractNumber");
		asset.setMaintContractExpiration(formatter.format(new Date()));
		asset.setSupportPhone("SupportPhone");
	}

	private void saveDataAuthentication() {
		asset.setUsername("Username");
		asset.setPassword("Password");
		asset.setEnable("Enable");

		asset.setConnection("");
		ArrayList<String> connectionOptions = new ArrayList<String>();
		connectionOptions.add("");
		connectionOptions.add("telnet");
		connectionOptions.add("ssh");
		connectionOptions.add("rsh");
		asset.setConnectionOptions(connectionOptions);

		asset.setAutoenable("");
		ArrayList<String> autoenableOptions = new ArrayList<String>();
		autoenableOptions.add("");
		autoenableOptions.add("A");
		asset.setAutoenableOptions(autoenableOptions);

	}

	private void saveDataHardware() {
		asset.setCpu("Intel Centrino2");
		asset.setRam("8GB DDR3");
		asset.setStoragectrl("SATA");
		asset.setAdditionalhardware("Rocket-Tower");
		asset.setNumpowersupplies("1");
		asset.setInputpower("2400 Watt");

		asset.setHdd1("for Comics");
		asset.setHdd2("for Musik");
		asset.setHdd3("for Games");
		asset.setHdd4("for Programs");
		asset.setHdd5("for Chaos");
		asset.setHdd6("for failing");
	}

	private void saveDataComments() {
		asset.setComment("Es soll manchen Dichter geben, der muss dichten, um zu leben.Ist das immer so? Mitnichten,manche leben um zu dichten.");
	}

	public AssetSuggCommand getAssetSuggestions() {
		AssetSuggCommand assetSugg = new AssetSuggCommand();

		assetSugg.setManufacturer(maunfacturers);

		ArrayList<String> descriptions = new ArrayList<String>();
		descriptions.add("001");
		descriptions.add("002");
		descriptions.add("003");
		descriptions.add("004");
		assetSugg.setDescription(descriptions);

		ArrayList<String> categories = new ArrayList<String>();
		categories.add("allo");
		categories.add("aallo");
		categories.add("ballo");
		categories.add("callo");
		assetSugg.setCategory(categories);

		ArrayList<String> cpus = new ArrayList<String>();
		cpus.add("AMD");
		cpus.add("ARM");
		cpus.add("INTEL");
		cpus.add("MOTOROLA");
		assetSugg.setCpu(cpus);

		ArrayList<String> additionalhardwares = new ArrayList<String>();
		additionalhardwares.add("Laser-Canon");
		additionalhardwares.add("Magic-Door");
		additionalhardwares.add("Blackhole-Port");
		assetSugg.setAdditionalhardware(additionalhardwares);

		ArrayList<String> admins = new ArrayList<String>();
		admins.add("Super Mario");
		admins.add("Medium Mario");
		admins.add("Bad Mario");
		admins.add("Pure Mario");
		assetSugg.setAdmin(admins);

		ArrayList<String> snmpcom = new ArrayList<String>();
		snmpcom.add("public");
		snmpcom.add("not so public");
		snmpcom.add("private");
		assetSugg.setSnmpcommunity(snmpcom);
		
		ArrayList<String> custom1com = new ArrayList<String>();
		custom1com.add("Testsystem");
		custom1com.add("Productionsystem");
		custom1com.add("Not in use");

		return assetSugg;
	}
}
