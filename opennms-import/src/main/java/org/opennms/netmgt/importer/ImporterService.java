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

package org.opennms.netmgt.importer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.opennms.core.logging.Logging;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.daemon.SpringServiceDaemon;
import org.opennms.netmgt.importer.operations.AbstractSaveOrUpdateOperation;
import org.opennms.netmgt.importer.operations.ImportOperation;
import org.opennms.netmgt.importer.operations.ImportOperationsManager;
import org.opennms.netmgt.importer.operations.ImportStatistics;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventIpcManager;
import org.opennms.netmgt.model.events.EventListener;
import org.opennms.netmgt.model.events.EventUtils;
import org.opennms.netmgt.xml.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * <p>
 * ImporterService class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
public class ImporterService extends BaseImporter implements SpringServiceDaemon, DisposableBean, EventListener {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(ImporterService.class);

    /** Constant <code>NAME="ModelImporter"</code>. */
    public static final String NAME = "model-importer";

    /** The m_import resource. */
    private volatile Resource m_importResource;

    /** The m_event manager. */
    private volatile EventIpcManager m_eventManager;

    /** The m_stats. */
    private volatile ImporterStats m_stats;

    /**
     * <p>
     * doImport
     * </p>
     * .
     */
    public void doImport() {
        doImport(null);
    }

    /**
     * Begins importing from resource specified in model-importer.properties
     * file or
     * in event parameter: url. Import Resources are managed with a "key" called
     * "foreignSource" specified in the XML retreived by the resource and can be
     * overridden
     * as a parameter of an event.
     *
     * @param event
     *            the event
     */
    private void doImport(Event event) {
        Resource resource = null;
        try {
            m_stats = new ImporterStats();
            resource = ((event != null && getEventUrl(event) != null) ? new UrlResource(getEventUrl(event))
                : m_importResource);
            sendImportStarted(resource);
            importModelFromResource(resource, m_stats, event);
            LOG.info("Finished Importing: {}", m_stats);
            sendImportSuccessful(m_stats, resource);
        } catch (IOException e) {
            String msg = "IOException importing " + resource;
            LOG.error(msg, e);
            sendImportFailed(msg + ": " + e.getMessage(), resource);
        } catch (ModelImportException e) {
            String msg = "Error parsing import data from " + resource;
            LOG.error(msg, e);
            sendImportFailed(msg + ": " + e.getMessage(), resource);
        }
    }

    /**
     * Gets the event url.
     *
     * @param event
     *            the event
     * @return the event url
     */
    private String getEventUrl(Event event) {
        return EventUtils.getParm(event, EventConstants.PARM_URL);
    }

    /**
     * <p>
     * getStats
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getStats() {
        return (m_stats == null ? "No Stats Availabile" : m_stats.toString());
    }

    /**
     * Send import successful.
     *
     * @param stats
     *            the stats
     * @param resource
     *            the resource
     */
    private void sendImportSuccessful(ImporterStats stats, Resource resource) {
        EventBuilder builder = new EventBuilder(EventConstants.IMPORT_SUCCESSFUL_UEI, NAME);
        builder.addParam(EventConstants.PARM_IMPORT_RESOURCE, resource.toString());
        builder.addParam(EventConstants.PARM_IMPORT_STATS, stats.toString());
        m_eventManager.sendNow(builder.getEvent());
    }

    /**
     * Send import failed.
     *
     * @param msg
     *            the msg
     * @param resource
     *            the resource
     */
    private void sendImportFailed(String msg, Resource resource) {
        EventBuilder builder = new EventBuilder(EventConstants.IMPORT_FAILED_UEI, NAME);
        builder.addParam(EventConstants.PARM_IMPORT_RESOURCE, resource.toString());
        builder.addParam(EventConstants.PARM_FAILURE_MESSAGE, msg);
        m_eventManager.sendNow(builder.getEvent());
    }

    /**
     * Send import started.
     *
     * @param resource
     *            the resource
     */
    private void sendImportStarted(Resource resource) {
        EventBuilder builder = new EventBuilder(EventConstants.IMPORT_STARTED_UEI, NAME);
        builder.addParam(EventConstants.PARM_IMPORT_RESOURCE, resource.toString());
        m_eventManager.sendNow(builder.getEvent());
    }

    /**
     * <p>
     * setImportResource
     * </p>
     * .
     *
     * @param resource
     *            a {@link org.springframework.core.io.Resource} object.
     */
    public void setImportResource(Resource resource) {
        m_importResource = resource;
    }

    /**
     * <p>
     * getEventManager
     * </p>
     * .
     *
     * @return a {@link org.opennms.netmgt.model.events.EventIpcManager} object.
     */
    public EventIpcManager getEventManager() {
        return m_eventManager;
    }

    /**
     * <p>
     * setEventManager
     * </p>
     * .
     *
     * @param eventManager
     *            a {@link org.opennms.netmgt.model.events.EventIpcManager}
     *            object.
     */
    public void setEventManager(EventIpcManager eventManager) {
        m_eventManager = eventManager;
    }

    /** {@inheritDoc} */
    @Override
    protected ImportOperationsManager createImportOperationsManager(Map<String, Integer> assetNumbersToNodes,
            ImportStatistics stats) {
        ImportOperationsManager opsMgr = super.createImportOperationsManager(assetNumbersToNodes, stats);
        opsMgr.setEventMgr(m_eventManager);
        return opsMgr;
    }

    /**
     * <p>
     * afterPropertiesSet
     * </p>
     * .
     *
     * @throws Exception
     *             the exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        m_eventManager.addEventListener(this, EventConstants.RELOAD_IMPORT_UEI);
    }

    /**
     * <p>
     * destroy
     * </p>
     * .
     *
     * @throws Exception
     *             the exception
     */
    @Override
    public void destroy() throws Exception {
        m_eventManager.removeEventListener(this, EventConstants.RELOAD_IMPORT_UEI);

    }

    /**
     * <p>
     * getName
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /** {@inheritDoc} */
    @Override
    public void onEvent(Event e) {

        Map mdc = Logging.getCopyOfContextMap();
        try {
            Logging.putPrefix(NAME);

            if (!EventConstants.RELOAD_IMPORT_UEI.equals(e.getUei())) {
                return;
            }
            doImport(e);
        } finally {
            Logging.setContextMap(mdc);
        }
    }

    /**
     * The Class ImporterStats.
     */
    public class ImporterStats implements ImportStatistics {

        /** The m_import duration. */
        private Duration m_importDuration = new Duration("Importing");

        /** The m_audit duration. */
        private Duration m_auditDuration = new Duration("Auditing");

        /** The m_loading duration. */
        private Duration m_loadingDuration = new Duration("Loading");

        /** The m_processing duration. */
        private Duration m_processingDuration = new Duration("Processing");

        /** The m_preprocessing duration. */
        private Duration m_preprocessingDuration = new Duration("Scanning");

        /** The m_relate duration. */
        private Duration m_relateDuration = new Duration("Relating");

        /** The m_preprocessing effort. */
        private WorkEffort m_preprocessingEffort = new WorkEffort("Scan Effort");

        /** The m_processing effort. */
        private WorkEffort m_processingEffort = new WorkEffort("Write Effort");

        /** The m_event effort. */
        private WorkEffort m_eventEffort = new WorkEffort("Event Sending Effort");

        /** The m_delete count. */
        private int m_deleteCount;

        /** The m_insert count. */
        private int m_insertCount;

        /** The m_update count. */
        private int m_updateCount;

        /** The m_event count. */
        private int m_eventCount;

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#beginProcessingOps()
         */
        @Override
        public void beginProcessingOps() {
            m_processingDuration.start();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#finishProcessingOps()
         */
        @Override
        public void finishProcessingOps() {
            m_processingDuration.end();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#beginPreprocessingOps()
         */
        @Override
        public void beginPreprocessingOps() {
            m_preprocessingDuration.start();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#finishPreprocessingOps()
         */
        @Override
        public void finishPreprocessingOps() {
            m_preprocessingDuration.end();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#beginPreprocessing(org.opennms.netmgt.importer.operations.ImportOperation)
         */
        @Override
        public void beginPreprocessing(ImportOperation oper) {
            if (oper instanceof AbstractSaveOrUpdateOperation) {
                m_preprocessingEffort.begin();
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#finishPreprocessing(org.opennms.netmgt.importer.operations.ImportOperation)
         */
        @Override
        public void finishPreprocessing(ImportOperation oper) {
            if (oper instanceof AbstractSaveOrUpdateOperation) {
                m_preprocessingEffort.end();
            }
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#beginPersisting(org.opennms.netmgt.importer.operations.ImportOperation)
         */
        @Override
        public void beginPersisting(ImportOperation oper) {
            m_processingEffort.begin();

        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#finishPersisting(org.opennms.netmgt.importer.operations.ImportOperation)
         */
        @Override
        public void finishPersisting(ImportOperation oper) {
            m_processingEffort.end();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#beginSendingEvents(org.opennms.netmgt.importer.operations.ImportOperation, java.util.List)
         */
        @Override
        public void beginSendingEvents(ImportOperation oper, List<Event> events) {
            if (events != null) {
                m_eventCount += events.size();
            }
            m_eventEffort.begin();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#finishSendingEvents(org.opennms.netmgt.importer.operations.ImportOperation, java.util.List)
         */
        @Override
        public void finishSendingEvents(ImportOperation oper, List<Event> events) {
            m_eventEffort.end();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#beginLoadingResource(org.springframework.core.io.Resource)
         */
        @Override
        public void beginLoadingResource(Resource resource) {
            m_loadingDuration.setName("Loading Resource: " + resource);
            m_loadingDuration.start();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#finishLoadingResource(org.springframework.core.io.Resource)
         */
        @Override
        public void finishLoadingResource(Resource resource) {
            m_loadingDuration.end();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#beginImporting()
         */
        @Override
        public void beginImporting() {
            m_importDuration.start();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#finishImporting()
         */
        @Override
        public void finishImporting() {
            m_importDuration.end();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#beginAuditNodes()
         */
        @Override
        public void beginAuditNodes() {
            m_auditDuration.start();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#finishAuditNodes()
         */
        @Override
        public void finishAuditNodes() {
            m_auditDuration.end();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#setDeleteCount(int)
         */
        @Override
        public void setDeleteCount(int deleteCount) {
            m_deleteCount = deleteCount;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#setInsertCount(int)
         */
        @Override
        public void setInsertCount(int insertCount) {
            m_insertCount = insertCount;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#setUpdateCount(int)
         */
        @Override
        public void setUpdateCount(int updateCount) {
            m_updateCount = updateCount;
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#beginRelateNodes()
         */
        @Override
        public void beginRelateNodes() {
            m_relateDuration.start();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.importer.operations.ImportStatistics#finishRelateNodes()
         */
        @Override
        public void finishRelateNodes() {
            m_relateDuration.end();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuffer stats = new StringBuffer();
            stats.append("Deletes: ").append(m_deleteCount).append(' ');
            stats.append("Updates: ").append(m_updateCount).append(' ');
            stats.append("Inserts: ").append(m_insertCount).append('\n');
            stats.append(m_importDuration).append(' ');
            stats.append(m_loadingDuration).append(' ');
            stats.append(m_auditDuration).append('\n');
            stats.append(m_preprocessingDuration).append(' ');
            stats.append(m_processingDuration).append(' ');
            stats.append(m_relateDuration).append(' ');
            stats.append(m_preprocessingEffort).append(' ');
            stats.append(m_processingEffort).append(' ');
            stats.append(m_eventEffort).append(' ');
            if (m_eventCount > 0) {
                stats.append("Avg ").append((double) m_eventEffort.getTotalTime() / (double) m_eventCount).append(" ms per event");
            }

            return stats.toString();
        }

    }

    /**
     * The Class Duration.
     */
    public class Duration {

        /** The m_name. */
        private String m_name = null;

        /** The m_start. */
        private long m_start = -1L;

        /** The m_end. */
        private long m_end = -1L;

        /**
         * Instantiates a new duration.
         */
        public Duration() {
            this(null);
        }

        /**
         * Instantiates a new duration.
         *
         * @param name
         *            the name
         */
        public Duration(String name) {
            m_name = name;
        }

        /**
         * Sets the name.
         *
         * @param name
         *            the new name
         */
        public void setName(String name) {
            m_name = name;
        }

        /**
         * Start.
         */
        public void start() {
            m_start = System.currentTimeMillis();
        }

        /**
         * End.
         */
        public void end() {
            m_end = System.currentTimeMillis();
        }

        /**
         * Gets the length.
         *
         * @return the length
         */
        public long getLength() {
            if (m_start == -1L) {
                return 0L;
            }
            long end = (m_end == -1L ? System.currentTimeMillis() : m_end);
            return end - m_start;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return (m_name == null ? "" : m_name + ": ") + (m_start == -1L ? "has not begun" : elapsedTime());
        }

        /**
         * Elapsed time.
         *
         * @return the string
         */
        private String elapsedTime() {

            long duration = getLength();

            long hours = duration / 3600000L;
            duration = duration % 3600000L;
            long mins = duration / 60000L;
            duration = duration % 60000L;
            long secs = duration / 1000L;
            long millis = duration % 1000L;

            StringBuffer elapsed = new StringBuffer();
            if (hours > 0) {
                elapsed.append(hours).append("h ");
            }
            if (mins > 0) {
                elapsed.append(mins).append("m ");
            }
            if (secs > 0) {
                elapsed.append(secs).append("s ");
            }
            if (millis > 0) {
                elapsed.append(millis).append("ms");
            }

            return elapsed.toString();

        }

    }

    /**
     * The Class WorkEffort.
     */
    public class WorkEffort {

        /** The m_name. */
        private String m_name;

        /** The m_total time. */
        private long m_totalTime;

        /** The m_section count. */
        private long m_sectionCount;

        /** The m_pending section. */
        private ThreadLocal<Duration> m_pendingSection = new ThreadLocal<Duration>();

        /**
         * Instantiates a new work effort.
         *
         * @param name
         *            the name
         */
        public WorkEffort(String name) {
            m_name = name;
        }

        /**
         * Begin.
         */
        public void begin() {
            Duration pending = new Duration();
            pending.start();
            m_pendingSection.set(pending);
        }

        /**
         * End.
         */
        public void end() {
            Duration pending = m_pendingSection.get();
            m_sectionCount++;
            m_totalTime += pending.getLength();
        }

        /**
         * Gets the total time.
         *
         * @return the total time
         */
        public long getTotalTime() {
            return m_totalTime;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("Total ").append(m_name).append(": ");
            buf.append((double) m_totalTime / (double) 1000L).append(" thread-seconds");
            if (m_sectionCount > 0) {
                buf.append(" Avg ").append(m_name).append(": ");
                buf.append((double) m_totalTime / (double) m_sectionCount).append(" ms per node");
            }
            return buf.toString();
        }

    }

    /**
     * <p>
     * start
     * </p>
     * .
     *
     * @throws Exception
     *             the exception
     */
    @Override
    public void start() throws Exception {
        // nothing to do -- we're event-driven
    }
}
