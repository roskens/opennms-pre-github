package org.opennms.netmgt.reporting.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.opennms.core.utils.LogUtils;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.config.DataSourceFactory;
import org.opennms.netmgt.config.reportd.Parameter;
import org.opennms.netmgt.config.reportd.Report;
import org.opennms.netmgt.daemon.SpringServiceDaemon;
import org.opennms.netmgt.dao.ReportdConfigurationDao;
import org.opennms.netmgt.model.events.EventBuilder;
import org.opennms.netmgt.model.events.EventForwarder;
import org.opennms.netmgt.model.events.annotations.EventHandler;
import org.opennms.netmgt.model.events.annotations.EventListener;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.service.ReportService.ReportFormat;
import org.opennms.netmgt.xml.event.Event;
import org.opennms.netmgt.xml.event.Parm;
import org.springframework.util.Assert;

/**
 * <p>
 * Reportd is a standalone daemon to generate reports.
 * </p>
 * <p>
 * The daemon can be triggered by events over the event-bus:
 * <ol>
 * <li>{@link EventConstants#REPORTD_RUN_REPORT}: generate a report</li>
 * <li>{@link EventConstants#RELOAD_DAEMON_CONFIG_UEI}: reload the scheduler
 * configuration.</li>
 * </ol>
 * </p>
 * <p>The daemon starts a quartz scheduler to generate scheduled reports, which are stored on disk and send by mail.</p>
 * 
 * @see ReportService
 * @see ReportScheduler
 * @author ranger
 * @author thargor
 * @version $Id: $
 */
@EventListener(name = "Reportd:EventListener")
// FIXME thargor: multithreading by Event and Scheduler?
public class Reportd implements SpringServiceDaemon {

    /** Constant <code>NAME="Reportd"</code> */
    public static final String NAME = "Reportd";

    private volatile EventForwarder m_eventForwarder;
    private ReportScheduler m_reportScheduler;
    private ReportService m_reportService;
    private ReportDeliveryService m_reportDeliveryService;
    private ReportdConfigurationDao m_reportConfigurationDao;

    /**
     * <p>
     * Starts the daemon and the scheduler
     * </p>
     * 
     * @throws java.lang.Exception
     *             if any.
     */
    @Override
    public void start() throws Exception {
        m_reportScheduler.start();
    }

    /**
     * <p>
     * afterPropertiesSet
     * </p>
     * 
     * @throws java.lang.Exception
     *             if any.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(m_eventForwarder, "No Event Forwarder Set");
        Assert.notNull(m_reportScheduler, "No Report Scheduler Set");
        Assert.notNull(m_reportService, "No Report service set");
        Assert.notNull(m_reportDeliveryService, "No Delivery service set");
        Assert.notNull(m_reportConfigurationDao,
                       "NoConfiguration DAO Defined");
    }

    /**
     * <p>
     * runReport
     * </p>
     * 
     * @param reportName
     *            a {@link java.lang.String} object.
     */
    public void runReport(String reportName) {
        LogUtils.infof(this, "Running report by name: (%s).", reportName);
        Report report = m_reportConfigurationDao.getReport(reportName);
        if (report != null) {
            runReport(report);
        } else {
            createAndSendReportingEvent(EventConstants.REPORT_RUN_FAILED_UEI,
                                        reportName, "undefined report");
        }
    }

    /**
     * <p>
     * runReport
     * </p>
     * 
     * @param report
     *            a {@link org.opennms.netmgt.config.reportd.Report} object.
     */
    public void runReport(Report report) {
        String originalName = ThreadCategory.getPrefix();
        Connection connection = null;
        try {
            ThreadCategory.setPrefix(NAME);
            LogUtils.debugf(this, NAME + " -- running job %s",
                            report.getReportName());

            // get the report definition
            ReportDefinition reportDefinition = m_reportService.getReportDefinition(report.getReportName());
            if (reportDefinition != null) {

                // get DB Connection
                connection = DataSourceFactory.getDataSource().getConnection();
                
                // generate and store the report
                String generateReport = m_reportService.generateReport(reportDefinition,
                                                                       convertReportFormat(report.getReportFormat()),
                                                                       convertParameterListToMap(report.getParameterCollection()), connection);
                LogUtils.debugf(this, NAME + " -- delivering report %s",
                                report.getReportName());

                // deliver the report by mail
                m_reportDeliveryService.deliverReport(report, generateReport);
                LogUtils.debugf(this, NAME + " -- done running job %s",
                                report.getReportName());

            } else {
                throw new ReportRunException("no report-definition found: ["
                        + report.getReportName(), null);
            }
        } catch (ReportRunException e) {
            createAndSendReportingEvent(EventConstants.REPORT_RUN_FAILED_UEI,
                                        report.getReportName(),
                                        e.getMessage());
        } catch (ReportDeliveryException e) {
            createAndSendReportingEvent(EventConstants.REPORT_DELIVERY_FAILED_UEI,
                                        report.getReportName(),
                                        e.getMessage());
        } catch (SQLException e) {
            createAndSendReportingEvent(EventConstants.REPORT_DELIVERY_FAILED_UEI,
                                        report.getReportName(),
                                        e.getMessage());
        } finally {
            ThreadCategory.setPrefix(originalName);
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LogUtils.errorf(this, e, "error closing db connection after report generation");
                }
            }
        }
    }

    /**
     * Converts the String representing a ReportFormat
     * 
     * @param reportFormat
     * @return
     */
    private ReportFormat convertReportFormat(String reportFormat) {

        for (ReportFormat formatEnum : ReportFormat.values()) {
            if (formatEnum.name().equalsIgnoreCase(reportFormat))
                return formatEnum;
        }

        throw new IllegalArgumentException("unkown report format: ["
                + reportFormat + "]");
    }

    /**
     * Converts a list of parameters to a map
     * 
     * @param parameterCollection
     * @return
     */
    private Map<String, String> convertParameterListToMap(
            List<Parameter> parameterCollection) {

        Map<String, String> result = new HashMap<String, String>();
        if (parameterCollection != null) {
            for (Parameter p : parameterCollection) {
                result.put(p.getName(), p.getValue());
            }
        }
        return result;
    }

    /**
     * <p>
     * createAndSendReportingEvent
     * 
     * @param uei
     *            the UEI of the event to send
     * @param reportName
     *            the name of the report in question
     * @param reason
     *            an explanation of why this event was sent
     */
    private void createAndSendReportingEvent(String uei, String reportName,
            String reason) {
        LogUtils.debugf(this,
                        "Crafting reporting event with UEI '%s' for report '%s' with reason '%s'",
                        uei, reportName, reason);

        EventBuilder bldr = new EventBuilder(uei, NAME);
        if (StringUtils.isNotBlank(reportName))
            bldr.addParam(EventConstants.PARM_REPORT_NAME, reportName);
        if (StringUtils.isNotBlank(reason))
            bldr.addParam(EventConstants.PARM_REASON, reason);
        getEventForwarder().sendNow(bldr.getEvent());
    }

    /**
     * <p>
     * handleRunReportEvent
     * </p>
     * 
     * @param e
     *            a {@link org.opennms.netmgt.xml.event.Event} object.
     */
    @EventHandler(uei = EventConstants.REPORTD_RUN_REPORT)
    public void handleRunReportEvent(Event e) {
        String reportName = null;

        // extract event parameters
        for (Parm parm : e.getParmCollection()) {
            if (EventConstants.PARM_REPORT_NAME.equals(parm.getParmName())) {
                reportName = parm.getValue().getContent();
            } else {
                LogUtils.infof(this, "Unknown Event Constant: %s",
                               parm.getParmName());
            }
        }

        if (StringUtils.isNotBlank(reportName)) {
            LogUtils.debugf(this, "running report %s", reportName);
            runReport(reportName);
        } else {
            LogUtils.errorf(this,
                            "Can not run report -- reportName not specified");

        }
    }

    /**
     * <p>
     * Handel a reload configuration event
     * </p>
     * 
     * @param e
     *            a {@link org.opennms.netmgt.xml.event.Event} object.
     */
    @EventHandler(uei = EventConstants.RELOAD_DAEMON_CONFIG_UEI)
    public void handleReloadConfigEvent(Event e) {

        if (isReloadConfigEventTarget(e)) {
            LogUtils.infof(this,
                           "handleReloadConfigEvent: reloading configuration...");

            try {

                LogUtils.debugf(this,
                                "handleReloadConfigEvent: lock acquired, unscheduling current reports...");

                m_reportScheduler.rebuildReportSchedule();

                LogUtils.debugf(this,
                                "handleReloadConfigEvent: reports rescheduled.");

                createAndSendReportingEvent(EventConstants.RELOAD_DAEMON_CONFIG_SUCCESSFUL_UEI,
                                            null, null);
            } catch (Throwable ex) {

                LogUtils.errorf(this,
                                ex,
                                "handleReloadConfigurationEvent: Error reloading configuration: %s",
                                ex.getMessage());
                createAndSendReportingEvent(EventConstants.RELOAD_DAEMON_CONFIG_FAILED_UEI,
                                            null,
                                            ex.getLocalizedMessage().substring(1,
                                                                               128));

            }

            LogUtils.infof(this,
                           "handleReloadConfigEvent: configuration reloaded.");
        }

    }

    private boolean isReloadConfigEventTarget(Event event) {
        boolean isTarget = false;

        List<Parm> parmCollection = event.getParmCollection();

        for (Parm parm : parmCollection) {
            if (EventConstants.PARM_DAEMON_NAME.equals(parm.getParmName())
                    && NAME.equalsIgnoreCase(parm.getValue().getContent())) {
                isTarget = true;
                break;
            }
        }

        LogUtils.debugf(this, "isReloadConfigEventTarget: " + NAME
                + " was target of reload event: " + isTarget);
        return isTarget;
    }

    /**
     * <p>
     * setEventForwarder
     * </p>
     * 
     * @param eventForwarder
     *            a {@link org.opennms.netmgt.model.events.EventForwarder}
     *            object.
     */
    public void setEventForwarder(EventForwarder eventForwarder) {
        m_eventForwarder = eventForwarder;
    }

    /**
     * <p>
     * getEventForwarder
     * </p>
     * 
     * @return a {@link org.opennms.netmgt.model.events.EventForwarder}
     *         object.
     */
    public EventForwarder getEventForwarder() {
        return m_eventForwarder;
    }

    /**
     * <p>
     * setReportScheduler
     * </p>
     * 
     * @param reportScheduler
     *            a
     *            {@link org.opennms.netmgt.reporting.service.ReportScheduler}
     *            object.
     */
    public void setReportScheduler(ReportScheduler reportScheduler) {
        m_reportScheduler = reportScheduler;
    }

    /**
     * <p>
     * getReportScheduler
     * </p>
     * 
     * @return a {@link org.opennms.netmgt.reporting.service.ReportScheduler}
     *         object.
     */
    public ReportScheduler getReportScheduler() {
        return m_reportScheduler;
    }

    /**
     * <p>
     * getReportService
     * </p>
     * 
     * @return a {@link org.opennms.netmgt.reporting.service.ReportService}
     *         object.
     */
    public ReportService getReportService() {
        return m_reportService;
    }

    /**
     * <p>
     * setReportService
     * </p>
     * 
     * @param reportService
     *            a {@link org.opennms.netmgt.reporting.service.ReportService}
     *            object.
     */
    public void setReportService(ReportService reportService) {
        m_reportService = reportService;
    }

    /**
     * <p>
     * getReportDeliveryService
     * </p>
     * 
     * @return a
     *         {@link org.opennms.netmgt.reporting.service.ReportDeliveryService}
     *         object.
     */
    public ReportDeliveryService getReportDeliveryService() {
        return m_reportDeliveryService;
    }

    /**
     * <p>
     * setReportDeliveryService
     * </p>
     * 
     * @param reportDeliveryService
     *            a
     *            {@link org.opennms.netmgt.reporting.service.ReportDeliveryService}
     *            object.
     */
    public void setReportDeliveryService(
            ReportDeliveryService reportDeliveryService) {
        m_reportDeliveryService = reportDeliveryService;
    }

    /**
     * <p>
     * getReportdConfigurationDao
     * </p>
     * 
     * @return a {@link org.opennms.netmgt.dao.ReportdConfigurationDao}
     *         object.
     */
    public ReportdConfigurationDao getReportdConfigurationDao() {
        return m_reportConfigurationDao;
    }

    /**
     * <p>
     * setReportdConfigurationDao
     * </p>
     * 
     * @param reportConfigurationDao
     *            a {@link org.opennms.netmgt.dao.ReportdConfigurationDao}
     *            object.
     */
    public void setReportdConfigurationDao(
            ReportdConfigurationDao reportConfigurationDao) {
        m_reportConfigurationDao = reportConfigurationDao;
    }

}
