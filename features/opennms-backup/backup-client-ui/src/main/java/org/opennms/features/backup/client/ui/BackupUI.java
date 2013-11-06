/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
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
package org.opennms.features.backup.client.ui;

import com.opennms.saas.endpoint.backup.api.model.BackupConfig;
import com.opennms.saas.endpoint.backup.api.model.BackupInfo;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.opennms.features.backup.client.api.BackupClient;
import org.opennms.features.backup.client.api.LocalBackupConfig;

import java.util.List;

/**
 * The wallboard application's "main" class
 *
 * @author Christian Pape
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
@SuppressWarnings("serial")
@Title("OpenNMS Backup")
@Theme("runo")
public class BackupUI extends UI {
    /**
     * the backup service instance
     */
    private BackupClient m_backupClient;

    private final TreeTable leftBackupsTable = new TreeTable();
    private final TreeTable rightBackupsTable = new TreeTable();

    @Override
    protected void init(VaadinRequest request) {
        /**
         * Initalizing the layout components
         */
        Panel topPanel = new Panel();
        topPanel.setSizeFull();
        topPanel.setCaption("Backup overview");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(true);
        verticalLayout.setSizeFull();
        verticalLayout.setMargin(true);

        Panel leftPanel = new Panel();
        leftPanel.setCaption("Local Backups");
        leftPanel.setSizeFull();

        leftBackupsTable.setSizeFull();
        leftBackupsTable.setSelectable(true);
        leftBackupsTable.setMultiSelect(false);
        leftBackupsTable.setNewItemsAllowed(false);

        HorizontalLayout leftButtonLayout = new HorizontalLayout();

        Button leftDeleteBtn = new Button("Delete");
        Button leftRefreshBtn = new Button("Refresh");
        Button leftBackupBtn = new Button("Backup");

        leftButtonLayout.addComponent(leftDeleteBtn);
        leftButtonLayout.addComponent(leftRefreshBtn);
        leftButtonLayout.addComponent(leftBackupBtn);

        VerticalLayout leftVerticalLayout = new VerticalLayout();
        leftVerticalLayout.setMargin(true);

        leftVerticalLayout.setSizeFull();
        leftVerticalLayout.addComponent(leftBackupsTable);
        leftVerticalLayout.addComponent(leftButtonLayout);
        leftVerticalLayout.setExpandRatio(leftBackupsTable, 1.0f);

        leftPanel.setContent(leftVerticalLayout);

        Panel rightPanel = new Panel();
        rightPanel.setCaption("Remote Backups");
        rightPanel.setSizeFull();

        rightBackupsTable.setSizeFull();
        rightBackupsTable.setSelectable(true);
        rightBackupsTable.setMultiSelect(false);
        rightBackupsTable.setNewItemsAllowed(false);

        HorizontalLayout rightButtonLayout = new HorizontalLayout();

        Button rightDeleteBtn = new Button("Delete");
        Button rightRefreshBtn = new Button("Refresh");
        Button rightBackupBtn = new Button("Download");

        rightButtonLayout.addComponent(rightDeleteBtn);
        rightButtonLayout.addComponent(rightRefreshBtn);
        rightButtonLayout.addComponent(rightBackupBtn);

        VerticalLayout rightVerticalLayout = new VerticalLayout();
        rightVerticalLayout.setMargin(true);

        rightVerticalLayout.setSizeFull();
        rightVerticalLayout.addComponent(rightBackupsTable);
        rightVerticalLayout.addComponent(rightButtonLayout);
        rightVerticalLayout.setExpandRatio(rightBackupsTable, 1.0f);

        rightPanel.setContent(rightVerticalLayout);

        HorizontalLayout horizontalLayout = new HorizontalLayout(leftPanel, rightPanel);
        horizontalLayout.setSizeFull();

        verticalLayout.addComponent(topPanel);
        verticalLayout.addComponent(horizontalLayout);

        verticalLayout.setExpandRatio(topPanel, 0.3f);
        verticalLayout.setExpandRatio(horizontalLayout, 0.6f);
/*
        updateRightTable();
        updateLeftTable();

        rightRefreshBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
            }
        });

        rightBackupBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
            }
        });
*/
        setContent(verticalLayout);
    }

    public void updateLeftTable() {
    }

    public void updateRightTable() {
        LocalBackupConfig localBackupConfig = new LocalBackupConfig();
        localBackupConfig.setKeyId("8062629b-d401-426c-b7b3-77d51eb52e65");
        localBackupConfig.setBackupServiceLocation("http://localhost:8080/backup-war/endpoint/backups");
        localBackupConfig.setLocalBackupDirectory("/Users/chris/Desktop/backup");
        localBackupConfig.setBaseDirectory("/opt/opennms");
        localBackupConfig.setPgDumpLocation("/Library/PostgreSQL/9.2/bin/pg_dump");
        localBackupConfig.setMaxConcurrentUploads(4);

        m_backupClient = new BackupClient(localBackupConfig);
        BackupConfig backupConfig = m_backupClient.lookupBackupConfig();
        List<BackupInfo> backupinfoList = m_backupClient.list(backupConfig);

        rightBackupsTable.removeAllItems();

        for (BackupInfo backupInfo : backupinfoList) {
            rightBackupsTable.addItem(backupInfo.getId());
            rightBackupsTable.setItemCaption(backupInfo.getId(), backupInfo.getFileInfo().getCreated().toString());
        }

    }

    public void updateLeftTavble() {

    }

    /**
     * Returns the associated BackupService.
     *
     * @return the BackupService instance
     */
    public BackupClient getBackupClient() {
        return m_backupClient;
    }

    public void setBackupClient(BackupClient backupClient) {
        this.m_backupClient = backupClient;

        updateRightTable();
    }
}
