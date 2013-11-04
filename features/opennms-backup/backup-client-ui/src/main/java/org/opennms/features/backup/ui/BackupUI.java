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
    /**
     * the table of backups
     */
    private Table m_table;
    /**
     * the file listing
     */
    private TreeTable m_treeTable;
    /**
     * the log text area
     */
    private TextArea m_logTextArea;
    /**
     * the layout instances
     */
    private VerticalLayout m_leftLayout, rightLayout, m_taskLayout;
    /**
     * the button layout
     */
    private HorizontalLayout m_rightButtonLayout;
    /**
     * the item container for backups
     */
    private BeanItemContainer<RestZipBackup> m_beanItemContainer;
    /**
     * the hierarchical container for files
     */
    private HierarchicalContainer m_hierarchicalContainer;
    /**
     * the backup service instance
     */
    private BackupService m_backupService;
    /**
     * the progress bar
     */
    private ProgressBar m_progressIndicator;
    /**
     * the task entries for progress visualization
     */
    private Map<String, TaskEntry> m_taskEntries = new LinkedHashMap<String, TaskEntry>();

    @Override
    protected void init(VaadinRequest request) {
        /**
         * Reload the config on init() call
         */
        m_backupService.reloadConfig();

        /**
         * Initalizing the layout components
         */
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setMargin(true);
        Panel leftPanel = new Panel();
        m_leftLayout = new VerticalLayout();
        m_leftLayout.setMargin(true);
        m_leftLayout.setSizeFull();
        leftPanel.setContent(m_leftLayout);
        leftPanel.setSizeFull();

        Panel rightPanel = new Panel();
        rightLayout = new VerticalLayout();
        rightLayout.setMargin(true);
        rightLayout.setSizeFull();
        rightPanel.setContent(rightLayout);
        rightPanel.setSizeFull();

        Panel taskPanel = new Panel();
        m_taskLayout = new VerticalLayout();
        m_taskLayout.setSizeFull();
        taskPanel.setContent(m_taskLayout);
        taskPanel.setSizeFull();

        /**
         * Create the log area
         */
        m_logTextArea = new TextArea();
        m_logTextArea.setSizeFull();

        Panel logPanel = new Panel();
        logPanel.setSizeFull();
        logPanel.setContent(m_logTextArea);

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
/**
 * Create the table
 */
        m_table = new Table();
        m_table.setSelectable(true);
        m_table.setMultiSelect(false);
        m_table.setNewItemsAllowed(false);

        m_beanItemContainer = new BeanItemContainer<RestZipBackup>(RestZipBackup.class);

        m_table.setContainerDataSource(m_beanItemContainer);
        m_table.setSizeFull();
        m_table.addGeneratedColumn("date", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table table, final Object itemId, Object columnId) {
                Item item = table.getItem(itemId);
                Date date = new Date();
                date.setTime(Long.valueOf(item.getItemProperty("timestamp").getValue().toString()));
                return date;
            }
        });

        m_leftLayout.addComponent(m_table);

        m_treeTable = new TreeTable();
        m_treeTable.setSizeFull();
        m_treeTable.setMultiSelect(true);
        m_treeTable.setNewItemsAllowed(false);
        m_treeTable.setSelectable(true);

        m_hierarchicalContainer = new HierarchicalContainer();
        m_hierarchicalContainer.addContainerProperty("filename", String.class, "");

        m_treeTable.setItemCaptionPropertyId("filename");
        m_treeTable.setContainerDataSource(m_hierarchicalContainer);

        updateTree(null);

        rightLayout.addComponent(m_treeTable);

        updateTable();

        m_table.setVisibleColumns(new Object[]{"date", "timestamp"});

        m_table.setImmediate(true);

        m_table.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {

                Object id = valueChangeEvent.getProperty().getValue();

                if (id == null) {
                    updateTree(null);
                } else {
                    updateTree(m_table.getItem(id).getItemProperty("timestamp").getValue().toString());
                }

            }
        });


        final Button button = new Button("Backup now");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                BackupJob backupJob = m_backupService.createBackupJob();

                createTaskList(backupJob.getTaskNames());

                backupJob.execute();
            }
        });

        final Button refreshButton = new Button("Refresh");
        refreshButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                updateTable();
            }
        });

        if (m_backupService.isBackupJobRunning()) {
            button.setEnabled(false);

            BackupJob backupJob = m_backupService.getBackupJob();

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

        /**
         * Creating a {@link BackupProgress} instance
         */
        m_backupService.setBackupProgress(new BackupProgress() {
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

        /**
         * Creating the button layout
         */
        m_rightButtonLayout = new HorizontalLayout();
        m_rightButtonLayout.setSpacing(true);

        m_rightButtonLayout.addComponent(refreshButton);
        m_rightButtonLayout.addComponent(button);

        rightLayout.addComponent(m_rightButtonLayout);

        /**
         * setting the content
         */
        setContent(rootLayout);
    }

    /**
     * Returns the associated BackupService.
     *
     * @return the BackupService instance
     */
    public BackupService getBackupService() {
        return m_backupService;
    }

    /**
     * Sets the BackupService to be used.
     *
     * @param backupService the BackupService instance
     */
    public void setBackupService(BackupService backupService) {
        this.m_backupService = backupService;
    }

    public void updateTable() {
        m_beanItemContainer.removeAllItems();

        RestBackupSet backupSet = RestClient.getBackupSet(m_backupService.getBackupConfig().getBackupUrl() + "/rest", m_backupService.getBackupConfig().getUsername(), m_backupService.getBackupConfig().getPassword(), m_backupService.getBackupConfig().getCustomerId(), m_backupService.getBackupConfig().getSystemId());

        for (RestZipBackup restZipBackup : backupSet.getZipBackups()) {
            m_beanItemContainer.addItem(restZipBackup);
        }
    }

    public void addRecursive(String filename) {
        if ("".equals(filename)) {
            if (!m_hierarchicalContainer.containsId("/")) {
                Item item = m_hierarchicalContainer.addItem("/");
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

            if (!m_hierarchicalContainer.containsId(filename)) {
                m_hierarchicalContainer.addItem(filename).getItemProperty("filename").setValue(name);
            }

            addRecursive(rest);

            if ("".equals(rest)) {
                m_hierarchicalContainer.setParent(filename, "/");
            } else {
                m_hierarchicalContainer.setParent(filename, rest);
            }

        }
    }

    public void addLogEntry(String message) {
        m_logTextArea.getUI().getSession().lock();
        try {
            String newContent = m_logTextArea.getValue() + message + "\n";
            m_logTextArea.setValue(newContent);
            m_logTextArea.setCursorPosition(newContent.length());
        } finally {
            m_logTextArea.getUI().getSession().unlock();
        }
    }

    public void updateTree(String timestamp) {
        m_hierarchicalContainer.removeAllItems();

        if (timestamp == null) {
            Item item = m_hierarchicalContainer.addItem("?");
            item.getItemProperty("filename").setValue("No Backup selected");
            m_hierarchicalContainer.setChildrenAllowed("?", false);
        } else {
            RestZipBackupContents restZipBackupContents = RestClient.getZipBackupContents(m_backupService.getBackupConfig().getBackupUrl() + "/rest", m_backupService.getBackupConfig().getUsername(), m_backupService.getBackupConfig().getPassword(), m_backupService.getBackupConfig().getCustomerId(), m_backupService.getBackupConfig().getSystemId(), timestamp);

            for (String filename : restZipBackupContents.getFiles()) {
                addRecursive(filename);
                m_hierarchicalContainer.setChildrenAllowed(filename, false);
            }
        }
    }

    public void clearTaskEntries() {
        m_taskLayout.removeAllComponents();
        m_taskEntries.clear();
    }

    public void addTaskEntry(String title) {
        m_taskEntries.put(title, new TaskEntry(title));
    }

    public void setTaskEntryState(String title, TaskEntry.TaskState state) {
        TaskEntry taskEntry = m_taskEntries.get(title);
        if (taskEntry == null) {
            System.out.println(title);
        } else {
            taskEntry.setState(state);
        }
    }

    public void setProgress(float f) {
        if (m_progressIndicator != null) {
            m_progressIndicator.getUI().getSession().lock();
            try {
                m_progressIndicator.setValue(f);
            } finally {
                m_progressIndicator.getUI().getSession().unlock();
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

        for (Map.Entry<String, TaskEntry> entry : m_taskEntries.entrySet()) {
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

        this.m_progressIndicator = progressIndicator;

        m_taskLayout.addComponent(verticalLayout);
        m_taskLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }
}
