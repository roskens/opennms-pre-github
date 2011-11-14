package org.opennms.netmgt.connect.reports.remote.api;

import java.io.InputStream;
import java.util.Collection;

import org.opennms.netmgt.connect.reports.remote.api.model.ReportDefinitionSDO;

/**
 * Service suppling report-template for
 * {@link ConnectReportDefinitionRepository}.
 * 
 * @author thargor
 */
public interface RemoteReportDefinitionRepository {

    /**
     * List all reports
     * 
     * @return
     */
    Collection<ReportDefinitionSDO> getAllReportDefinitions();

    /**
     * List all reports available for the supplied credentials
     * 
     * @return
     */
    Collection<ReportDefinitionSDO> getAvailableReportDefinitions();

    /**
     * Get the template of the report.
     * 
     * @param id
     *            id of the report
     * @return
     */
    InputStream getReportTemplate(Integer id, String version);

    /**
     * @param id
     * @return
     */
    ReportDefinitionSDO getReportDefinition(Integer id);

}
