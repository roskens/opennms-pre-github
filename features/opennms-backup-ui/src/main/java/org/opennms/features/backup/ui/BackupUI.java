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
package org.opennms.features.backup.ui;

import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.opennms.features.backup.api.client.BackupJob;
import org.opennms.features.backup.api.client.BackupProgress;
import org.opennms.features.backup.api.client.BackupService;
import org.opennms.features.backup.api.helpers.RestClient;
import org.opennms.features.backup.api.rest.RestBackupSet;
import org.opennms.features.backup.api.rest.RestZipBackup;
import org.opennms.features.backup.api.rest.RestZipBackupContents;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The wallboard application's "main" class
 *
 * @author Christian Pape
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
@SuppressWarnings("serial")
@Title("OpenNMS Backup")
public class BackupUI extends UI {

    private Table table;
    private TreeTable treeTable;
    private TextArea logTextArea;
    private VerticalLayout rightLayout, leftLayout, taskLayout;
    private BeanItemContainer<RestZipBackup> beanItemContainer;
    private HierarchicalContainer hierarchicalContainer;
    private BackupService backupService;
    private ProgressBar progressIndicator;


    private Map<String, TaskEntry> taskEntries = new LinkedHashMap<String, TaskEntry>();

    public void updateTable() {
        beanItemContainer.removeAllItems();

        RestBackupSet backupSet = RestClient.getBackupSet(backupService.getBackupUrl() + "/rest", backupService.getUsername(), backupService.getPassword(), backupService.getCustomerId(), backupService.getSystemId());

        for (RestZipBackup restZipBackup : backupSet.getZipBackups()) {
            beanItemContainer.addItem(restZipBackup);
        }
    }

    public void addRecursive(String filename) {
        System.out.println("'" + filename + "'");

        if ("".equals(filename)) {
            if (!hierarchicalContainer.containsId("/")) {
                Item item = hierarchicalContainer.addItem("/");
                item.getItemProperty("filename").setValue("/");
            }
        } else {

            String rest, name;

            if (filename.lastIndexOf("/") != -1) {
                rest = filename.substring(0, filename.lastIndexOf("/"));
                name = filename.substring(filename.lastIndexOf("/") + 1);
            } else {
                rest = "";
                name = filename;
            }

            if (!hierarchicalContainer.containsId(filename)) {
                hierarchicalContainer.addItem(filename).getItemProperty("filename").setValue(name);
            }

            addRecursive(rest);

            if ("".equals(rest)) {
                hierarchicalContainer.setParent(filename, "/");
            } else {
                hierarchicalContainer.setParent(filename, rest);
            }

        }
    }

    public void addLogEntry(String message) {
        logTextArea.getUI().getSession().lock();
        try {
            String newContent = logTextArea.getValue() + message + "\n";
            logTextArea.setValue(newContent);
            logTextArea.setCursorPosition(newContent.length());
        } finally {
            logTextArea.getUI().getSession().unlock();
        }
    }

    public void updateTree(String timestamp) {
        hierarchicalContainer.removeAllItems();

        if (timestamp == null) {
            Item item = hierarchicalContainer.addItem("?");
            item.getItemProperty("filename").setValue("No Backup selected");
            hierarchicalContainer.setChildrenAllowed("?", false);
        } else {
            RestZipBackupContents restZipBackupContents = RestClient.getZipBackupContents(backupService.getBackupUrl() + "/rest", backupService.getUsername(), backupService.getPassword(), backupService.getCustomerId(), backupService.getSystemId(), timestamp);

            for (String filename : restZipBackupContents.getFiles()) {
                addRecursive(filename);
                hierarchicalContainer.setChildrenAllowed(filename, false);
            }

            for (Object itemId : treeTable.getItemIds()) {
                treeTable.setCollapsed(itemId, false);
            }
        }
    }

    public void clearTaskEntries() {
        taskLayout.removeAllComponents();
        taskEntries.clear();
    }

    public void addTaskEntry(String title) {
        taskEntries.put(title, new TaskEntry(title));
    }

    public void setTaskEntryState(String title, TaskEntry.TaskState state) {
        TaskEntry taskEntry = taskEntries.get(title);
        if (taskEntry == null) {
            System.out.println(title);
        } else {
            taskEntry.setState(state);
        }
    }

    public void setProgress(float f) {
        if (progressIndicator != null) {
            progressIndicator.getUI().getSession().lock();
            try {
                progressIndicator.setValue(f);
            } finally {
                progressIndicator.getUI().getSession().unlock();
            }
        }
    }

    public void createTaskList(List<String> stringList) {
        clearTaskEntries();

        for (String string : stringList) {
            addTaskEntry(string);
        }

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth(60, Unit.PERCENTAGE);

        for (Map.Entry<String, TaskEntry> entry : taskEntries.entrySet()) {
            verticalLayout.addComponent(entry.getValue().getHorizontalLayout());
        }

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPollingInterval(200);
        progressIndicator.setImmediate(true);
        progressIndicator.setIndeterminate(false);
        progressIndicator.setEnabled(true);
        progressIndicator.setVisible(true);
        progressIndicator.setWidth(100, Unit.PERCENTAGE);
        verticalLayout.addComponent(progressIndicator);

        this.progressIndicator = progressIndicator;

        taskLayout.addComponent(verticalLayout);
        taskLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setMargin(true);
        Panel leftPanel = new Panel();
        leftLayout = new VerticalLayout();
        leftLayout.setMargin(true);
        leftLayout.setSizeFull();
        leftPanel.setContent(leftLayout);
        leftPanel.setSizeFull();

        Panel rightPanel = new Panel();
        rightLayout = new VerticalLayout();
        rightLayout.setMargin(true);
        rightLayout.setSizeFull();
        rightPanel.setContent(rightLayout);
        rightPanel.setSizeFull();

        Panel taskPanel = new Panel();
        taskLayout = new VerticalLayout();
        taskLayout.setSizeFull();
        taskPanel.setContent(taskLayout);
        taskPanel.setSizeFull();

        logTextArea = new TextArea();
        logTextArea.setSizeFull();

        Panel logPanel = new Panel();
        logPanel.setSizeFull();
        logPanel.setContent(logTextArea);

        HorizontalLayout upperLayout = new HorizontalLayout();
        upperLayout.addComponent(leftPanel);
        upperLayout.addComponent(rightPanel);
        upperLayout.setSizeFull();

        VerticalLayout lowerLayout = new VerticalLayout();
        lowerLayout.addComponent(taskPanel);
        lowerLayout.addComponent(logPanel);
        lowerLayout.setSizeFull();

        rootLayout.addComponent(upperLayout);
        rootLayout.addComponent(lowerLayout);

        leftPanel.setCaption("Available backups");
        rightPanel.setCaption("Files");
        taskPanel.setCaption("Tasks");
        logPanel.setCaption("Log");

        rootLayout.setSizeFull();
        rootLayout.setSpacing(true);

        // backupService = BackupService.getInstance();

        table = new Table();
        table.setSelectable(true);
        table.setMultiSelect(false);
        table.setNewItemsAllowed(false);

        beanItemContainer = new BeanItemContainer<RestZipBackup>(RestZipBackup.class);

        table.setContainerDataSource(beanItemContainer);
        table.setSizeFull();
        table.addGeneratedColumn("date", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table table, final Object itemId, Object columnId) {
                Item item = table.getItem(itemId);
                Date date = new Date();
                date.setTime(Long.valueOf(item.getItemProperty("timestamp").getValue().toString()));
                return date;
            }
        });

        leftLayout.addComponent(table);

        treeTable = new TreeTable();
        treeTable.setSizeFull();
        treeTable.setMultiSelect(true);
        treeTable.setNewItemsAllowed(false);
        treeTable.setSelectable(true);

        hierarchicalContainer = new HierarchicalContainer();
        hierarchicalContainer.addContainerProperty("filename", String.class, "");

        treeTable.setItemCaptionPropertyId("filename");
        treeTable.setContainerDataSource(hierarchicalContainer);

        updateTree(null);

        rightLayout.addComponent(treeTable);

        updateTable();

        table.setVisibleColumns(new Object[]{"date", "timestamp"});

        table.setImmediate(true);

        table.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {

                Object id = valueChangeEvent.getProperty().getValue();

                if (id == null) {
                    updateTree(null);
                } else {
                    updateTree(table.getItem(id).getItemProperty("timestamp").getValue().toString());
                }

            }
        });


        final Button button = new Button("Test");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                BackupJob backupJob = backupService.createBackupJob();

                createTaskList(backupJob.getTaskNames());

                backupJob.execute();
            }
        });

        if (backupService.isBackupJobRunning()) {
            button.setEnabled(false);

            BackupJob backupJob = backupService.getBackupJob();

            createTaskList(backupJob.getTaskNames());

            for (String taskName : backupJob.getTaskNames()) {
                if (backupJob.isTaskPending(taskName)) {
                    setTaskEntryState(taskName, TaskEntry.TaskState.PENDING);
                } else {
                    if (backupJob.isTaskRunning(taskName)) {
                        setTaskEntryState(taskName, TaskEntry.TaskState.RUNNING);
                    } else {
                        if (backupJob.isTaskComplete(taskName)) {
                            setTaskEntryState(taskName, TaskEntry.TaskState.COMPLETE);
                        } else {
                            setTaskEntryState(taskName, TaskEntry.TaskState.FAILED);
                        }
                    }
                }
            }
        }

        backupService.setBackupProgress(new BackupProgress() {
            public void backupBegin(long timestamp, BackupJob backupJob) {
                button.setEnabled(false);
                Date date = new Date();
                date.setTime(timestamp);
                addLogEntry("Backup job " + timestamp + " (" + date + ") started");
            }

            public void backupEnd(long timestamp, BackupJob backupJob) {
                button.setEnabled(true);

                Date date = new Date();
                date.setTime(timestamp);
                addLogEntry("Backup job " + timestamp + " (" + date + ") finished");
            }

            public void backupFailed(long timestamp, BackupJob backupJob) {
                button.setEnabled(true);

                Date date = new Date();
                date.setTime(timestamp);
                addLogEntry("Backup job " + timestamp + " (" + date + ") failed");
            }

            public void phaseBegin(String title) {
                addLogEntry("Phase " + title + " start");
                setTaskEntryState(title, TaskEntry.TaskState.RUNNING.RUNNING);
                setProgress(0.0f);
            }

            public void phaseEnd(String title) {
                addLogEntry("Phase " + title + " finished");
                setTaskEntryState(title, TaskEntry.TaskState.COMPLETE);
                setProgress(1.0f);
            }

            public void phaseFailed(String title) {
                addLogEntry("Phase " + title + " failed");
                setTaskEntryState(title, TaskEntry.TaskState.FAILED);
                setProgress(1.0f);
            }

            public void filesProcessed(int filesProcessed, int filesTotal) {
                if (filesProcessed % 100 == 0 || filesProcessed == filesTotal) {
                    addLogEntry(filesProcessed + "/" + filesTotal + " processed");
                }
                setProgress((float) filesProcessed / (float) filesTotal);
            }
        });

        rightLayout.addComponent(button);


        setContent(rootLayout);
    }

    /**
     * Returns the associated BackupService.
     *
     * @return the BackupService instance
     */
    public BackupService getBackupService() {
        return backupService;
    }

    /**
     * Sets the BackupService to be used.
     *
     * @param backupService the BackupService instance
     */
    public void setBackupService(BackupService backupService) {
        this.backupService = backupService;
    }

}
