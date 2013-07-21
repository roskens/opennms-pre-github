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

package org.opennms.gwt.web.ui.asset.client.view;

import java.util.ArrayList;

import org.opennms.gwt.web.ui.asset.client.AssetPageConstants;
import org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter;
import org.opennms.gwt.web.ui.asset.client.tools.DisclosurePanelCookie;
import org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSet;
import org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSetDateBox;
import org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSetListBox;
import org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSetPasswordBox;
import org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSetSuggestBox;
import org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSetTextArea;
import org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSetTextBox;
import org.opennms.gwt.web.ui.asset.client.tools.fieldsets.FieldSetTextDisplay;
import org.opennms.gwt.web.ui.asset.client.tools.validation.StringAsIntegerValidator;
import org.opennms.gwt.web.ui.asset.client.tools.validation.StringBasicValidator;
import org.opennms.gwt.web.ui.asset.shared.AssetCommand;
import org.opennms.gwt.web.ui.asset.shared.AssetSuggCommand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class AssetNodePageImpl.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 *         AssetNodePage java part. Corresponding ui-binder xml
 *         {@link AssetNodePage.ui.xml}.
 *         Most parts are mapping code to fill the ui {@link FieldSets} from the
 *         command objects {@link AssetCommand} {@link AssetSuggCommand} and
 *         back.
 *         Adding some validators to {@link FieldSets}.
 *         Mapping code may be replaced by implementing GWT Editor framework.
 */
public class AssetNodePageImpl extends Composite implements AssetPagePresenter.Display {

    /**
     * Recommended GWT MVP and UI-Binder design.
     */
    @UiTemplate("AssetNodePage.ui.xml")
    interface AssetNodePageUiBinder extends UiBinder<Widget, AssetNodePageImpl> {
    }

    /** The ui binder. */
    private static AssetNodePageUiBinder uiBinder = GWT.create(AssetNodePageUiBinder.class);

    /** The con. */
    private AssetPageConstants con = GWT.create(AssetPageConstants.class);

    /** The m_asset. */
    AssetCommand m_asset;

    /** The node info label. */
    @UiField
    Label nodeInfoLabel;

    /** The node info link. */
    @UiField
    Anchor nodeInfoLink;

    /** The main panel. */
    @UiField
    VerticalPanel mainPanel;

    /** The l info top. */
    @UiField
    Label lInfoTop;

    /** The l info bottom. */
    @UiField
    Label lInfoBottom;

    /** The snmp disc panel. */
    @UiField
    DisclosurePanelCookie snmpDiscPanel;

    /** The s system id. */
    @UiField
    FieldSetTextDisplay sSystemId;

    /** The s system name. */
    @UiField
    FieldSetTextDisplay sSystemName;

    /** The s system location. */
    @UiField
    FieldSetTextDisplay sSystemLocation;

    /** The s system contact. */
    @UiField
    FieldSetTextDisplay sSystemContact;

    /** The s system description. */
    @UiField
    FieldSetTextDisplay sSystemDescription;

    /** The s display cat. */
    @UiField
    FieldSetSuggestBox sDisplayCat;

    /** The s notification cat. */
    @UiField
    FieldSetSuggestBox sNotificationCat;

    /** The s poller cat. */
    @UiField
    FieldSetSuggestBox sPollerCat;

    /** The s threshold cat. */
    @UiField
    FieldSetSuggestBox sThresholdCat;

    /** The s description. */
    @UiField
    FieldSetSuggestBox sDescription;

    /** The s asset category. */
    @UiField
    FieldSetSuggestBox sAssetCategory;

    /** The s manufacturer. */
    @UiField
    FieldSetSuggestBox sManufacturer;

    /** The s model number. */
    @UiField
    FieldSetSuggestBox sModelNumber;

    /** The s serial number. */
    @UiField
    FieldSetTextBox sSerialNumber;

    /** The s asset number. */
    @UiField
    FieldSetTextBox sAssetNumber;

    /** The s operating system. */
    @UiField
    FieldSetSuggestBox sOperatingSystem;

    /** The s date installed. */
    @UiField
    FieldSetDateBox sDateInstalled;

    /** The s region. */
    @UiField
    FieldSetSuggestBox sRegion;

    /** The s division. */
    @UiField
    FieldSetSuggestBox sDivision;

    /** The s department. */
    @UiField
    FieldSetSuggestBox sDepartment;

    /** The s address1. */
    @UiField
    FieldSetSuggestBox sAddress1;

    /** The s address2. */
    @UiField
    FieldSetSuggestBox sAddress2;

    /** The s city. */
    @UiField
    FieldSetSuggestBox sCity;

    /** The s state. */
    @UiField
    FieldSetSuggestBox sState;

    /** The s zip. */
    @UiField
    FieldSetSuggestBox sZip;

    /** The s country. */
    @UiField
    FieldSetSuggestBox sCountry;

    /** The s longitude. */
    @UiField
    FieldSetTextBox sLongitude;

    /** The s latitude. */
    @UiField
    FieldSetTextBox sLatitude;

    /** The s building. */
    @UiField
    FieldSetSuggestBox sBuilding;

    /** The s floor. */
    @UiField
    FieldSetSuggestBox sFloor;

    /** The s room. */
    @UiField
    FieldSetSuggestBox sRoom;

    /** The s rack. */
    @UiField
    FieldSetSuggestBox sRack;

    /** The s slot. */
    @UiField
    FieldSetTextBox sSlot;

    /** The s rack unit hight. */
    @UiField
    FieldSetTextBox sRackUnitHight;

    /** The s port. */
    @UiField
    FieldSetTextBox sPort;

    /** The s circuit id. */
    @UiField
    FieldSetSuggestBox sCircuitId;

    /** The s admin. */
    @UiField
    FieldSetSuggestBox sAdmin;

    /** The s vendor name. */
    @UiField
    FieldSetSuggestBox sVendorName;

    /** The s phone. */
    @UiField
    FieldSetSuggestBox sPhone;

    /** The s fax. */
    @UiField
    FieldSetSuggestBox sFax;

    /** The s lease. */
    @UiField
    FieldSetSuggestBox sLease;

    /** The s lease expires. */
    @UiField
    FieldSetDateBox sLeaseExpires;

    /** The s vendor asset. */
    @UiField
    FieldSetTextBox sVendorAsset;

    /** The s maint contract. */
    @UiField
    FieldSetSuggestBox sMaintContract;

    /** The s contract expires. */
    @UiField
    FieldSetDateBox sContractExpires;

    /** The s maint phone. */
    @UiField
    FieldSetSuggestBox sMaintPhone;

    /** The s user name. */
    @UiField
    FieldSetTextBox sUserName;

    /** The s password. */
    @UiField
    FieldSetPasswordBox sPassword;

    /** The s enable password. */
    @UiField
    FieldSetPasswordBox sEnablePassword;

    /** The s connection. */
    @UiField
    FieldSetListBox sConnection;

    /** The s auto enable. */
    @UiField
    FieldSetListBox sAutoEnable;

    /** The s snmpcommunity. */
    @UiField
    FieldSetSuggestBox sSnmpcommunity;

    /** The s cpu. */
    @UiField
    FieldSetSuggestBox sCpu;

    /** The s ram. */
    @UiField
    FieldSetSuggestBox sRam;

    /** The s storagectrl. */
    @UiField
    FieldSetSuggestBox sStoragectrl;

    /** The s additionalhardware. */
    @UiField
    FieldSetSuggestBox sAdditionalhardware;

    /** The s numpowersupplies. */
    @UiField
    FieldSetSuggestBox sNumpowersupplies;

    /** The s inputpower. */
    @UiField
    FieldSetSuggestBox sInputpower;

    /** The s hdd1. */
    @UiField
    FieldSetSuggestBox sHdd1;

    /** The s hdd2. */
    @UiField
    FieldSetSuggestBox sHdd2;

    /** The s hdd3. */
    @UiField
    FieldSetSuggestBox sHdd3;

    /** The s hdd4. */
    @UiField
    FieldSetSuggestBox sHdd4;

    /** The s hdd5. */
    @UiField
    FieldSetSuggestBox sHdd5;

    /** The s hdd6. */
    @UiField
    FieldSetSuggestBox sHdd6;

    /** The s vmware managed object id. */
    @UiField
    FieldSetSuggestBox sVmwareManagedObjectId;

    /** The s vmware managed entity type. */
    @UiField
    FieldSetSuggestBox sVmwareManagedEntityType;

    /** The s vmware management server. */
    @UiField
    FieldSetSuggestBox sVmwareManagementServer;

    /** The s vmware topology info. */
    @UiField
    FieldSetSuggestBox sVmwareTopologyInfo;

    /** The s vmware state. */
    @UiField
    FieldSetSuggestBox sVmwareState;

    /** The s comment. */
    @UiField
    FieldSetTextArea sComment;

    /** The save button. */
    @UiField
    Button saveButton;

    /** The reset button. */
    @UiField
    Button resetButton;

    /** The last modified. */
    @UiField
    Label lastModified;

    /** The field set list. */
    private ArrayList<FieldSet> fieldSetList = new ArrayList<FieldSet>();

    /**
     * Instantiates a new asset node page impl.
     */
    public AssetNodePageImpl() {
        initWidget(uiBinder.createAndBindUi(this));

        // avoid whitespaces and umlauts at category fields to prevent
        // config-file problems
        sDisplayCat.addWarningValidator(new StringBasicValidator());
        sNotificationCat.addWarningValidator(new StringBasicValidator());
        sThresholdCat.addWarningValidator(new StringBasicValidator());
        sPollerCat.addWarningValidator(new StringBasicValidator());
        sAssetCategory.addWarningValidator(new StringBasicValidator());

        sRackUnitHight.addErrorValidator(new StringAsIntegerValidator());
        sNumpowersupplies.addErrorValidator(new StringAsIntegerValidator());
        sInputpower.addErrorValidator(new StringAsIntegerValidator());
        initUiElementList();
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.ui.Widget#asWidget()
     */
    @Override
    public Widget asWidget() {
        return this;
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter.Display#cleanUp()
     */
    @Override
    public void cleanUp() {
        for (FieldSet fs : fieldSetList) {
            fs.clearChanged();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter.Display#getData()
     */
    @Override
    public AssetCommand getData() {
        saveDataConfigCategories();
        saveDataIdentification();
        saveDataLocation();
        saveDataVendor();
        saveDataAuthentication();
        saveDataHardware();
        saveDataComments();
        saveDataVmware();

        return m_asset;
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter.Display#getResetButton()
     */
    @Override
    public HasClickHandlers getResetButton() {
        return resetButton;
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter.Display#getSaveButton()
     */
    @Override
    public HasClickHandlers getSaveButton() {
        return saveButton;
    }

    /**
     * Set up a list of all {@link FieldSet}s of the ui.
     */
    private void initUiElementList() {

        fieldSetList.add(sSystemId);
        fieldSetList.add(sSystemName);
        fieldSetList.add(sSystemLocation);
        fieldSetList.add(sSystemContact);
        fieldSetList.add(sSystemDescription);

        fieldSetList.add(sDisplayCat);
        fieldSetList.add(sNotificationCat);
        fieldSetList.add(sPollerCat);
        fieldSetList.add(sThresholdCat);

        fieldSetList.add(sDescription);
        fieldSetList.add(sAssetCategory);
        fieldSetList.add(sManufacturer);
        fieldSetList.add(sModelNumber);
        fieldSetList.add(sSerialNumber);
        fieldSetList.add(sAssetNumber);
        fieldSetList.add(sDateInstalled);
        fieldSetList.add(sOperatingSystem);

        fieldSetList.add(sRegion);
        fieldSetList.add(sDivision);
        fieldSetList.add(sDepartment);
        fieldSetList.add(sAddress1);
        fieldSetList.add(sAddress2);
        fieldSetList.add(sCity);
        fieldSetList.add(sState);
        fieldSetList.add(sZip);
        fieldSetList.add(sCountry);
        fieldSetList.add(sLongitude);
        fieldSetList.add(sLatitude);
        fieldSetList.add(sBuilding);
        fieldSetList.add(sFloor);
        fieldSetList.add(sRoom);
        fieldSetList.add(sRack);
        fieldSetList.add(sSlot);
        fieldSetList.add(sRackUnitHight);
        fieldSetList.add(sPort);
        fieldSetList.add(sCircuitId);
        fieldSetList.add(sAdmin);

        fieldSetList.add(sVendorName);
        fieldSetList.add(sPhone);
        fieldSetList.add(sFax);
        fieldSetList.add(sLease);
        fieldSetList.add(sLeaseExpires);
        fieldSetList.add(sVendorAsset);
        fieldSetList.add(sMaintContract);
        fieldSetList.add(sContractExpires);
        fieldSetList.add(sMaintPhone);

        fieldSetList.add(sUserName);
        fieldSetList.add(sPassword);
        fieldSetList.add(sEnablePassword);
        fieldSetList.add(sConnection);
        fieldSetList.add(sAutoEnable);
        fieldSetList.add(sSnmpcommunity);

        fieldSetList.add(sCpu);
        fieldSetList.add(sRam);
        fieldSetList.add(sAdditionalhardware);
        fieldSetList.add(sInputpower);
        fieldSetList.add(sNumpowersupplies);
        fieldSetList.add(sStoragectrl);

        fieldSetList.add(sHdd1);
        fieldSetList.add(sHdd2);
        fieldSetList.add(sHdd3);
        fieldSetList.add(sHdd4);
        fieldSetList.add(sHdd5);
        fieldSetList.add(sHdd6);

        fieldSetList.add(sVmwareManagedObjectId);
        fieldSetList.add(sVmwareManagedEntityType);
        fieldSetList.add(sVmwareManagementServer);

        fieldSetList.add(sVmwareTopologyInfo);
        fieldSetList.add(sVmwareState);

        fieldSetList.add(sComment);
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter.Display#isUiValid()
     */
    @Override
    public boolean isUiValid() {
        for (FieldSet fs : fieldSetList) {
            if (!fs.getError().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Save data authentication.
     */
    private void saveDataAuthentication() {
        m_asset.setUsername(sUserName.getValue());
        m_asset.setPassword(sPassword.getValue());
        m_asset.setEnable(sEnablePassword.getValue());
        m_asset.setConnection(sConnection.getValue());
        m_asset.setAutoenable(sAutoEnable.getValue());
        m_asset.setSnmpcommunity(sSnmpcommunity.getValue());
    }

    /**
     * Save data comments.
     */
    private void saveDataComments() {
        m_asset.setComment(sComment.getValue());
    }

    /**
     * Save data config categories.
     */
    private void saveDataConfigCategories() {
        m_asset.setDisplayCategory(sDisplayCat.getValue());
        m_asset.setNotifyCategory(sNotificationCat.getValue());
        m_asset.setPollerCategory(sPollerCat.getValue());
        m_asset.setThresholdCategory(sThresholdCat.getValue());
    }

    /**
     * Save data hardware.
     */
    private void saveDataHardware() {
        m_asset.setCpu(sCpu.getValue());
        m_asset.setRam(sRam.getValue());
        m_asset.setStoragectrl(sStoragectrl.getValue());
        m_asset.setAdditionalhardware(sAdditionalhardware.getValue());
        m_asset.setNumpowersupplies(sNumpowersupplies.getValue());
        m_asset.setInputpower(sInputpower.getValue());

        m_asset.setHdd1(sHdd1.getValue());
        m_asset.setHdd2(sHdd2.getValue());
        m_asset.setHdd3(sHdd3.getValue());
        m_asset.setHdd4(sHdd4.getValue());
        m_asset.setHdd5(sHdd5.getValue());
        m_asset.setHdd6(sHdd6.getValue());
    }

    /**
     * Save data identification.
     */
    private void saveDataIdentification() {
        m_asset.setDescription(sDescription.getValue());
        m_asset.setCategory(sAssetCategory.getValue());
        m_asset.setManufacturer(sManufacturer.getValue());
        m_asset.setModelNumber(sModelNumber.getValue());
        m_asset.setSerialNumber(sSerialNumber.getValue());
        m_asset.setAssetNumber(sAssetNumber.getValue());
        m_asset.setOperatingSystem(sOperatingSystem.getValue());
        m_asset.setDateInstalled(sDateInstalled.getValue());
    }

    /**
     * Save data location.
     */
    private void saveDataLocation() {
        m_asset.setRegion(sRegion.getValue());
        m_asset.setDivision(sDivision.getValue());
        m_asset.setDepartment(sDepartment.getValue());
        m_asset.setAddress1(sAddress1.getValue());
        m_asset.setAddress2(sAddress2.getValue());
        m_asset.setCity(sCity.getValue());
        m_asset.setState(sState.getValue());
        m_asset.setZip(sZip.getValue());
        m_asset.setCountry(sCountry.getValue());
        m_asset.setLongitude(s2f(sLongitude.getValue()));
        m_asset.setLatitude(s2f(sLatitude.getValue()));
        m_asset.setBuilding(sBuilding.getValue());
        m_asset.setFloor(sFloor.getValue());
        m_asset.setRoom(sRoom.getValue());
        m_asset.setRack(sRack.getValue());
        m_asset.setSlot(sSlot.getValue());
        m_asset.setRackunitheight(sRackUnitHight.getValue());
        m_asset.setPort(sPort.getValue());
        m_asset.setCircuitId(sCircuitId.getValue());
        m_asset.setAdmin(sAdmin.getValue());
    }

    /**
     * Save data vendor.
     */
    private void saveDataVendor() {
        m_asset.setVendor(sVendorName.getValue());
        m_asset.setVendorPhone(sPhone.getValue());
        m_asset.setVendorFax(sFax.getValue());
        m_asset.setLease(sLease.getValue());
        m_asset.setLeaseExpires(sLeaseExpires.getValue());
        m_asset.setVendorAssetNumber(sVendorAsset.getValue());
        m_asset.setMaintcontract(sMaintContract.getValue());
        m_asset.setMaintContractExpiration(sContractExpires.getValue());
        m_asset.setSupportPhone(sMaintPhone.getValue());
    }

    /**
     * Save data vmware.
     */
    private void saveDataVmware() {
        m_asset.setVmwareManagedObjectId(sVmwareManagedObjectId.getValue());
        m_asset.setVmwareManagedEntityType(sVmwareManagedEntityType.getValue());
        m_asset.setVmwareManagementServer(sVmwareManagementServer.getValue());
        m_asset.setVmwareTopologyInfo(sVmwareTopologyInfo.getValue());
        m_asset.setVmwareState(sVmwareState.getValue());
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter.Display#setData(org.opennms.gwt.web.ui.asset.shared.AssetCommand)
     */
    @Override
    public void setData(AssetCommand asset) {
        m_asset = asset;

        nodeInfoLabel.setText(asset.getNodeLabel() + " " + con.nodeIdLabel() + " " + asset.getNodeId());
        nodeInfoLink.setHref("element/node.jsp?node=" + asset.getNodeId());
        nodeInfoLink.setHTML(con.nodeInfoLink());
        setDataSNMP(m_asset);
        setDataConfigCategories(m_asset);
        setDataIdentification(m_asset);
        setDataLocation(m_asset);
        setDataVendor(m_asset);
        setDataAuthentication(m_asset);
        setDataHardware(m_asset);
        setDataComments(m_asset);
        setDataVmware(m_asset);
        DateTimeFormat m_formater = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM);
        lastModified.setText(con.lastModified() + " " + m_formater.format(asset.getLastModifiedDate()) + " | "
                + asset.getLastModifiedBy());
    }

    /**
     * Sets the data authentication.
     *
     * @param asset
     *            the new data authentication
     */
    private void setDataAuthentication(AssetCommand asset) {
        sUserName.setValue(asset.getUsername());
        sPassword.setValue(asset.getPassword());
        sEnablePassword.setValue(asset.getEnable());
        sConnection.setOptions(asset.getConnectionOptions());
        sConnection.setValue(asset.getConnection());
        sAutoEnable.setOptions(asset.getAutoenableOptions());
        sAutoEnable.setValue(asset.getAutoenable());
        sSnmpcommunity.setValue(asset.getSnmpcommunity());
    }

    /**
     * Sets the data comments.
     *
     * @param asset
     *            the new data comments
     */
    private void setDataComments(AssetCommand asset) {
        sComment.setValue(asset.getComment());
    }

    /**
     * Sets the data config categories.
     *
     * @param asset
     *            the new data config categories
     */
    private void setDataConfigCategories(AssetCommand asset) {
        sDisplayCat.setValue(asset.getDisplayCategory());
        sNotificationCat.setValue(asset.getNotifyCategory());
        sPollerCat.setValue(asset.getPollerCategory());
        sThresholdCat.setValue(asset.getThresholdCategory());
    }

    /**
     * Sets the data hardware.
     *
     * @param asset
     *            the new data hardware
     */
    private void setDataHardware(AssetCommand asset) {
        sCpu.setValue(asset.getCpu());
        sRam.setValue(asset.getRam());
        sStoragectrl.setValue(asset.getStoragectrl());
        sAdditionalhardware.setValue(asset.getAdditionalhardware());
        sNumpowersupplies.setValue(asset.getNumpowersupplies());
        sInputpower.setValue(asset.getInputpower());
        sHdd1.setValue(asset.getHdd1());
        sHdd2.setValue(asset.getHdd2());
        sHdd3.setValue(asset.getHdd3());
        sHdd4.setValue(asset.getHdd4());
        sHdd5.setValue(asset.getHdd5());
        sHdd6.setValue(asset.getHdd6());
    }

    /**
     * Sets the data identification.
     *
     * @param asset
     *            the new data identification
     */
    private void setDataIdentification(AssetCommand asset) {
        sDescription.setValue(asset.getDescription());
        sAssetCategory.setValue(asset.getCategory());
        sManufacturer.setValue(asset.getManufacturer());
        sModelNumber.setValue(asset.getModelNumber());
        sSerialNumber.setValue(asset.getSerialNumber());
        sAssetNumber.setValue(asset.getAssetNumber());
        sOperatingSystem.setValue(asset.getOperatingSystem());
        sDateInstalled.setValue(asset.getDateInstalled());
    }

    /**
     * Sets the data location.
     *
     * @param asset
     *            the new data location
     */
    private void setDataLocation(AssetCommand asset) {
        sRegion.setValue(asset.getRegion());
        sDivision.setValue(asset.getDivision());
        sDepartment.setValue(asset.getDepartment());
        sAddress1.setValue(asset.getAddress1());
        sAddress2.setValue(asset.getAddress2());
        sCity.setValue(asset.getCity());
        sState.setValue(asset.getState());
        sZip.setValue(asset.getZip());
        sCountry.setValue(asset.getCountry());
        sLongitude.setValue(f2s(asset.getLongitude()));
        sLatitude.setValue(f2s(asset.getLatitude()));
        sBuilding.setValue(asset.getBuilding());
        sFloor.setValue(asset.getFloor());
        sRoom.setValue(asset.getRoom());
        sRack.setValue(asset.getRack());
        sSlot.setValue(asset.getSlot());
        sRackUnitHight.setValue(asset.getRackunitheight());
        sPort.setValue(asset.getPort());
        sCircuitId.setValue(asset.getCircuitId());
        sAdmin.setValue(asset.getAdmin());
    }

    /**
     * F2s.
     *
     * @param value
     *            the value
     * @return the string
     */
    private String f2s(final Float value) {
        return value == null ? null : value.toString();
    }

    /**
     * S2f.
     *
     * @param value
     *            the value
     * @return the float
     */
    private Float s2f(final String value) {
        if (value != null && !"".equals(value)) {
            try {
                return Float.valueOf(value);
            } catch (final NumberFormatException e) {
                // ignore and return null if it's not a valid float
            }
        }
        return null;
    }

    /**
     * Sets the data snmp.
     *
     * @param asset
     *            the new data snmp
     */
    private void setDataSNMP(AssetCommand asset) {

        if ((asset.getSnmpSysObjectId().equals("")) || (asset.getSnmpSysObjectId() == null)) {
            snmpDiscPanel.setVisible(false);
        } else {
            sSystemId.setValue(asset.getSnmpSysObjectId());
            sSystemName.setValue(asset.getSnmpSysName());
            sSystemLocation.setValue(asset.getSnmpSysLocation());
            sSystemContact.setValue(asset.getSnmpSysContact());
            sSystemDescription.setValue(asset.getSnmpSysDescription());
            snmpDiscPanel.setVisible(true);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter.Display#setDataSugg(org.opennms.gwt.web.ui.asset.shared.AssetSuggCommand)
     */
    @Override
    public void setDataSugg(AssetSuggCommand assetSugg) {
        setDataSuggConfigCategories(assetSugg);
        setDataSuggIdentification(assetSugg);
        setDataSuggLocation(assetSugg);
        setDataSuggVendor(assetSugg);
        setDataSuggAuth(assetSugg);
        setDataSuggHardware(assetSugg);
        setDataSuggVmware(assetSugg);
    }

    /**
     * Sets the data sugg auth.
     *
     * @param assetSugg
     *            the new data sugg auth
     */
    private void setDataSuggAuth(AssetSuggCommand assetSugg) {
        sSnmpcommunity.setSuggestions(assetSugg.getSnmpcommunity());
    }

    /**
     * Sets the data sugg config categories.
     *
     * @param assetSugg
     *            the new data sugg config categories
     */
    private void setDataSuggConfigCategories(AssetSuggCommand assetSugg) {
        sDisplayCat.setSuggestions(assetSugg.getDisplayCategory());
        sNotificationCat.setSuggestions(assetSugg.getNotifyCategory());
        sPollerCat.setSuggestions(assetSugg.getPollerCategory());
        sThresholdCat.setSuggestions(assetSugg.getThresholdCategory());
    }

    /**
     * Sets the data sugg hardware.
     *
     * @param assetSugg
     *            the new data sugg hardware
     */
    private void setDataSuggHardware(AssetSuggCommand assetSugg) {
        sCpu.setSuggestions(assetSugg.getCpu());
        sRam.setSuggestions(assetSugg.getRam());
        sStoragectrl.setSuggestions(assetSugg.getStoragectrl());
        sAdditionalhardware.setSuggestions(assetSugg.getAdditionalhardware());
        sNumpowersupplies.setSuggestions(assetSugg.getNumpowersupplies());
        sInputpower.setSuggestions(assetSugg.getInputpower());
        sHdd1.setSuggestions(assetSugg.getHdd1());
        sHdd2.setSuggestions(assetSugg.getHdd2());
        sHdd3.setSuggestions(assetSugg.getHdd3());
        sHdd4.setSuggestions(assetSugg.getHdd4());
        sHdd5.setSuggestions(assetSugg.getHdd5());
        sHdd6.setSuggestions(assetSugg.getHdd6());
    }

    /**
     * Sets the data sugg identification.
     *
     * @param assetSugg
     *            the new data sugg identification
     */
    private void setDataSuggIdentification(AssetSuggCommand assetSugg) {
        sDescription.setSuggestions(assetSugg.getDescription());
        sAssetCategory.setSuggestions(assetSugg.getCategory());
        sManufacturer.setSuggestions(assetSugg.getManufacturer());
        sModelNumber.setSuggestions(assetSugg.getModelNumber());
        sOperatingSystem.setSuggestions(assetSugg.getOperatingSystem());
    }

    /**
     * Sets the data sugg location.
     *
     * @param assetSugg
     *            the new data sugg location
     */
    private void setDataSuggLocation(AssetSuggCommand assetSugg) {
        sRegion.setSuggestions(assetSugg.getRegion());
        sDivision.setSuggestions(assetSugg.getDivision());
        sDepartment.setSuggestions(assetSugg.getDepartment());
        sAddress1.setSuggestions(assetSugg.getAddress1());
        sAddress2.setSuggestions(assetSugg.getAddress2());
        sCity.setSuggestions(assetSugg.getCity());
        sState.setSuggestions(assetSugg.getState());
        sZip.setSuggestions(assetSugg.getZip());
        sCountry.setSuggestions(assetSugg.getCountry());
        sBuilding.setSuggestions(assetSugg.getBuilding());
        sFloor.setSuggestions(assetSugg.getFloor());
        sRoom.setSuggestions(assetSugg.getRoom());
        sRack.setSuggestions(assetSugg.getRack());
        sCircuitId.setSuggestions(assetSugg.getCircuitId());
        sAdmin.setSuggestions(assetSugg.getAdmin());
    }

    /**
     * Sets the data sugg vendor.
     *
     * @param assetSugg
     *            the new data sugg vendor
     */
    private void setDataSuggVendor(AssetSuggCommand assetSugg) {
        sVendorName.setSuggestions(assetSugg.getVendor());
        sPhone.setSuggestions(assetSugg.getVendorPhone());
        sFax.setSuggestions(assetSugg.getVendorFax());
        sLease.setSuggestions(assetSugg.getLease());
        sMaintContract.setSuggestions(assetSugg.getMaintcontract());
        sMaintPhone.setSuggestions(assetSugg.getSupportPhone());
    }

    /**
     * Sets the data vendor.
     *
     * @param asset
     *            the new data vendor
     */
    private void setDataVendor(AssetCommand asset) {
        sVendorName.setValue(asset.getVendor());
        sPhone.setValue(asset.getVendorPhone());
        sFax.setValue(asset.getVendorFax());
        sLease.setValue(asset.getLease());
        sLeaseExpires.setValue(asset.getLeaseExpires());
        sVendorAsset.setValue(asset.getVendorAssetNumber());
        sMaintContract.setValue(asset.getMaintcontract());
        sContractExpires.setValue(asset.getMaintContractExpiration());
        sMaintPhone.setValue(asset.getSupportPhone());
    }

    /**
     * Sets the data sugg vmware.
     *
     * @param assetSugg
     *            the new data sugg vmware
     */
    private void setDataSuggVmware(AssetSuggCommand assetSugg) {
        sVmwareManagedObjectId.setSuggestions(assetSugg.getVmwareManagedObjectId());
        sVmwareManagedEntityType.setSuggestions(assetSugg.getVmwareManagedEntityType());
        sVmwareManagementServer.setSuggestions(assetSugg.getVmwareManagementServer());
        sVmwareTopologyInfo.setSuggestions(assetSugg.getVmwareTopologyInfo());
        sVmwareState.setSuggestions(assetSugg.getVmwareState());
    }

    /**
     * Sets the data vmware.
     *
     * @param asset
     *            the new data vmware
     */
    private void setDataVmware(AssetCommand asset) {
        sVmwareManagedObjectId.setValue(asset.getVmwareManagedObjectId());
        sVmwareManagedEntityType.setValue(asset.getVmwareManagedEntityType());
        sVmwareManagementServer.setValue(asset.getVmwareManagementServer());
        sVmwareTopologyInfo.setValue(asset.getVmwareTopologyInfo());
        sVmwareState.setValue(asset.getVmwareState());
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter.Display#setEnable(java.lang.Boolean)
     */
    @Override
    public void setEnable(Boolean enabled) {
        for (FieldSet fieldSet : fieldSetList) {
            fieldSet.setEnabled(enabled);
        }
        saveButton.setEnabled(enabled);
        resetButton.setEnabled(enabled);
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter.Display#setError(java.lang.String, java.lang.Throwable)
     */
    @Override
    public void setError(String description, Throwable throwable) {
        String error = "";
        if (throwable != null) {
            error = throwable.toString();
        }
        final DialogBox dialog = new DialogBox();
        dialog.setText(description);
        VerticalPanel panel = new VerticalPanel();
        HTMLPanel html = new HTMLPanel(error);
        html.setStyleName("Message");
        panel.add(html);

        Button ok = new Button("OK");
        SimplePanel buttonPanel = new SimplePanel();
        buttonPanel.setWidget(ok);
        buttonPanel.setStyleName("Button");
        panel.add(buttonPanel);

        dialog.setPopupPosition(Window.getScrollLeft() + 100, Window.getScrollTop() + 100);
        dialog.setWidget(panel);
        ok.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                dialog.hide();
            }
        });

        dialog.show();
    }

    /* (non-Javadoc)
     * @see org.opennms.gwt.web.ui.asset.client.presenter.AssetPagePresenter.Display#setInfo(java.lang.String)
     */
    @Override
    public void setInfo(String info) {
        lInfoTop.setText(info);
        lInfoBottom.setText(info);
    }
}
