package org.opennms.netmgt.reporting.repository.definition;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.opennms.netmgt.reporting.service.ReportService;

/**
 * Components providing report-templates for {@link ReportService} must
 * implement this interface.
 * 
 * @author thargor
 */
public interface ReportDefinitionRepository {

    /**
     * Constants for reports without a version
     */
    public static final String VERSION_NONE = "none";

    /**
     * List all available reports
     * 
     * @return
     */
    Collection<ReportDefinition> getAllReportDefinitions();

    /**
     * Get the definition of a report by its name
     * 
     * @param name
     *            name of the report
     * @return
     */
    ReportDefinition getReportDefinition(String name);

    /**
     * Get the definition of a report by its name
     * 
     * @param id
     *            id of the report
     * @return
     */
    ReportDefinition getReportDefinition(Integer id);

    /**
     * Get the template of the report.
     * 
     * @param id
     *            id of the report
     * @param engineVersion
     *            of the engine the template is requested for
     * @return
     * @throws IOException 
     */
    InputStream getReportTemplate(Integer id, String engineVersion) throws IOException;

}
