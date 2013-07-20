/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.core.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * ContainerTask
 * @author brozow
 *
 * TODO derive directly from Task
 */
/**
 * <p>
 * Abstract ContainerTask class.
 * </p>
 *
 * @param <T>
 *            the generic type
 * @author ranger
 * @version $Id: $
 */
public abstract class ContainerTask<T extends ContainerTask<?>> extends Task {

    /**
     * TaskTrigger.
     *
     * @author brozow
     */
    private final class TaskTrigger extends Task {

        /**
         * Instantiates a new task trigger.
         *
         * @param coordinator
         *            the coordinator
         * @param parent
         *            the parent
         */
        public TaskTrigger(DefaultTaskCoordinator coordinator, ContainerTask<?> parent) {
            super(coordinator, parent);
        }

        /* (non-Javadoc)
         * @see org.opennms.core.tasks.Task#completeSubmit()
         */
        @Override
        protected void completeSubmit() {
            getCoordinator().markTaskAsCompleted(TaskTrigger.this);
        }

        /* (non-Javadoc)
         * @see org.opennms.core.tasks.Task#toString()
         */
        @Override
        public String toString() {
            return "Trigger For " + ContainerTask.this;
        }
    }

    /** The m_trigger task. */
    protected final Task m_triggerTask;

    /** The m_children. */
    private final List<Task> m_children = Collections.synchronizedList(new ArrayList<Task>());

    /** The m_builder. */
    private final TaskBuilder<T> m_builder;

    /**
     * <p>
     * Constructor for ContainerTask.
     * </p>
     *
     * @param coordinator
     *            a {@link org.opennms.core.tasks.DefaultTaskCoordinator}
     *            object.
     * @param parent
     *            a {@link org.opennms.core.tasks.ContainerTask} object.
     */
    public ContainerTask(DefaultTaskCoordinator coordinator, ContainerTask<?> parent) {
        super(coordinator, parent);
        m_builder = createBuilder();
        m_triggerTask = new TaskTrigger(coordinator, this);

    }

    /**
     * Creates the builder.
     *
     * @return the task builder
     */
    @SuppressWarnings("unchecked")
    private TaskBuilder<T> createBuilder() {
        return new TaskBuilder<T>((T) this);
    }

    /**
     * <p>
     * getBuilder
     * </p>
     * .
     *
     * @return a {@link org.opennms.core.tasks.TaskBuilder} object.
     */
    public TaskBuilder<T> getBuilder() {
        return m_builder;
    }

    /** {@inheritDoc} */
    @Override
    public void addPrerequisite(Task task) {
        super.addPrerequisite(task);
        m_triggerTask.addPrerequisite(task);
    }

    /** The m_child. */
    AtomicInteger m_child = new AtomicInteger(0);

    /** {@inheritDoc} */
    @Override
    public void preSchedule() {
        m_triggerTask.schedule();
        List<Task> children;
        synchronized (m_children) {
            children = new ArrayList<Task>(m_children);
            m_children.clear();
        }

        for (Task task : children) {
            task.schedule();
        }
    }

    /**
     * <p>
     * add
     * </p>
     * .
     *
     * @param task
     *            a {@link org.opennms.core.tasks.Task} object.
     */
    public void add(Task task) {

        super.addPrerequisite(task);
        addChildDependencies(task);

        boolean scheduleChild;
        synchronized (m_children) {
            scheduleChild = isScheduled();
            if (!scheduleChild) {
                m_children.add(task);
            }
        }

        if (scheduleChild) {
            task.schedule();
        }

    }

    /**
     * <p>
     * add
     * </p>
     * .
     *
     * @param runInBatch
     *            a {@link org.opennms.core.tasks.RunInBatch} object.
     */
    public void add(RunInBatch runInBatch) {
        getBuilder().add(runInBatch);
    }

    /**
     * <p>
     * add
     * </p>
     * .
     *
     * @param needsContainer
     *            a {@link org.opennms.core.tasks.NeedsContainer} object.
     */
    public void add(NeedsContainer needsContainer) {
        getBuilder().add(needsContainer);
    }

    /**
     * <p>
     * getTriggerTask
     * </p>
     * .
     *
     * @return a {@link org.opennms.core.tasks.Task} object.
     */
    protected Task getTriggerTask() {
        return m_triggerTask;
    }

    // private void setPreferredExecutorOfChild(Task task) {
    // if (task instanceof ContainerTask) {
    // ContainerTask container = (ContainerTask)task;
    // if (container.getChildPreferredExecutor().equals(DEFAULT_EXECUTOR)) {
    // container.setPreferredExecutor(getChildPreferredExecutor());
    // }
    // } else if (task instanceof SyncTask){
    // SyncTask syncTask = (SyncTask)task;
    // if (syncTask.getPreferredExecutor().equals(DEFAULT_EXECUTOR)) {
    // syncTask.setPreferredExecutor(getChildPreferredExecutor());
    // }
    // }
    // }

    /** {@inheritDoc} */
    @Override
    protected void completeSubmit() {
        getCoordinator().markTaskAsCompleted(this);
    }

    /**
     * <p>
     * add
     * </p>
     * .
     *
     * @param runnable
     *            a {@link java.lang.Runnable} object.
     * @return a {@link org.opennms.core.tasks.SyncTask} object.
     */
    public SyncTask add(Runnable runnable) {
        SyncTask task = createTask(runnable);
        add(task);
        return task;
    }

    /**
     * <p>
     * add
     * </p>
     * .
     *
     * @param runnable
     *            a {@link java.lang.Runnable} object.
     * @param schedulingHint
     *            a {@link java.lang.String} object.
     * @return a {@link org.opennms.core.tasks.SyncTask} object.
     */
    public SyncTask add(Runnable runnable, String schedulingHint) {
        SyncTask task = createTask(runnable, schedulingHint);
        add(task);
        return task;
    }

    /**
     * <p>
     * add
     * </p>
     * .
     *
     * @param <S>
     *            a S object.
     * @param async
     *            a {@link org.opennms.core.tasks.Async} object.
     * @param cb
     *            a {@link org.opennms.core.tasks.Callback} object.
     * @return a {@link org.opennms.core.tasks.AsyncTask} object.
     */
    public <S> AsyncTask<S> add(Async<S> async, Callback<S> cb) {
        AsyncTask<S> task = createTask(async, cb);
        add(task);
        return task;
    }

    /**
     * <p>
     * addSequence
     * </p>
     * .
     *
     * @param tasks
     *            a {@link java.lang.Runnable} object.
     * @return a {@link org.opennms.core.tasks.SequenceTask} object.
     */
    @Deprecated
    public SequenceTask addSequence(Runnable... tasks) {
        return getCoordinator().createSequence(this, tasks);
    }

    /**
     * Creates the task.
     *
     * @param runnable
     *            the runnable
     * @return the sync task
     */
    private SyncTask createTask(Runnable runnable) {
        return getCoordinator().createTask(this, runnable);
    }

    /**
     * Creates the task.
     *
     * @param runnable
     *            the runnable
     * @param schedulingHint
     *            the scheduling hint
     * @return the sync task
     */
    private SyncTask createTask(Runnable runnable, String schedulingHint) {
        return getCoordinator().createTask(this, runnable, schedulingHint);
    }

    /**
     * Creates the task.
     *
     * @param <S>
     *            the generic type
     * @param async
     *            the async
     * @param cb
     *            the cb
     * @return the async task
     */
    private <S> AsyncTask<S> createTask(Async<S> async, Callback<S> cb) {
        return getCoordinator().createTask(this, async, cb);
    }

    /**
     * <p>
     * addChildDependencies
     * </p>
     * .
     *
     * @param child
     *            a {@link org.opennms.core.tasks.Task} object.
     */
    protected void addChildDependencies(Task child) {
        child.addPrerequisite(m_triggerTask);
    }
}
