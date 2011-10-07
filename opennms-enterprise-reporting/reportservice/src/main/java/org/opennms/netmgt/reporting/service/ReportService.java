package org.opennms.netmgt.reporting.service;

import java.sql.Connection;
import java.util.Collection;
import java.util.Map;

import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

/**
 * <p>
 * ReportService interface.
 * </p>
 * 
 * @author thargor
 */
public interface ReportService {
    
    public enum ReportFormat { html, pdf, csv, xml };

    /**
     * Generates a report and returns a filesystem-path to the resulting
     * resource.
     * 
     * @param reportDef
     * @param format
     * @param parameters
     * @return
     * @throws ReportRunException 
     */
    String generateReport(ReportDefinition reportDef, ReportFormat format, Map<String,String> parameters, Connection connection) throws ReportRunException;

    /**
     * Returns a list of all available report definitions
     * 
     * @return
     */
    Collection<ReportDefinition> getReportDefinitions();

    /**
     * @param reportName
     */
    ReportDefinition getReportDefinition(String reportName);
    
//    Collection<Report> listReports(Integer reportDefId, [ReportFormat format]);
//    public class Report {
//        private ReportDefinition m_repotDefinition;
//        private Date m_created;
//        private String m_path;
//    }
}
