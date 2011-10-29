/*******************************************************************************
 * This file is part of OpenNMS(R). Copyright (C) 2009-2011 The OpenNMS Group,
 * Inc. OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc. OpenNMS(R)
 * is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. OpenNMS(R) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details. You should have received a copy of the GNU General Public
 * License along with OpenNMS(R). If not, see: http://www.gnu.org/licenses/
 * For more information contact: OpenNMS(R) Licensing <license@opennms.org>
 * http://www.opennms.org/ http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.reporting.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinitionRepository;
import org.opennms.netmgt.reporting.repository.definition.RepositoryTyp;
import org.opennms.netmgt.reporting.repository.definition.disk.DiskReportDefinitionRepository;

/**
 * Implementation of the report service
 * 
 * @author thargor
 */
public class DefaultReportService implements ReportService {

    // FIXME thargor: version of jasper has to be configured
    private static final String JASPER_VERSION = "3.7.6";
    // FIXME thargor: should be configured for reportservice, so all report
    // get stored
    private static final String REPORT_STORE_PATH = "/tmp/reports/";

	// FIXME: Add Spring dependency injection
    private ReportDefinitionRepository m_diskRDRepository = new DiskReportDefinitionRepository();
    
	// FIXME: Add Spring dependency injection
    private ReportDefinitionRepository m_connectRDRepository = new DiskReportDefinitionRepository();

    /**
     * Returns repo-instance for the report repo-typ
     * 
     * @param typ
     * @return
     */
    private ReportDefinitionRepository getRepo(RepositoryTyp typ) {
        if (RepositoryTyp.CONNECT == typ) {
            return m_connectRDRepository;
        } else if (RepositoryTyp.DISK == typ) {
            return m_diskRDRepository;
        } else {
            throw new IllegalStateException("unhandled repository typ: ["
                    + typ + "]");
        }
    }

    @Override
    public ReportDefinition getReportDefinition(String reportName) {
        Collection<ReportDefinition> definitions = getReportDefinitions();
        if (definitions != null) {
            for (ReportDefinition def : definitions) {
                if (def.getName().equals(reportName))
                    return def;
            }
        }

        return null;
    }

    @Override
    public String generateReport(ReportDefinition reportDef,
            ReportFormat format, Map<String, String> params, Connection connection)
            throws ReportRunException {

        ReportDefinitionRepository repo = getRepo(reportDef.getRepositoryTyp());
        InputStream reportTemplate = null;
        String exceptionMsg = "error on report-generation template:["
                + reportDef.getId() + "]";
        try {
            reportTemplate = repo.getReportTemplate(reportDef.getId(),
                                                    JASPER_VERSION);
            if (reportTemplate != null) {
                JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                                                                       params,
                                                                       connection);
                return saveReport(reportDef, jasperPrint, format);
            } else {
                throw new ReportRunException(exceptionMsg, null);
            }
        } catch (IOException e) {
            throw new ReportRunException(exceptionMsg, e);
        } catch (JRException e) {
            throw new ReportRunException(exceptionMsg, e);
        } finally {
            if (reportTemplate != null) {
                try {
                    reportTemplate.close();
                } catch (IOException e) {
                    LogUtils.errorf(this,
                                    "closing template inputstrean failed");
                }
            }
        }
    }

    /**
     * Save the report to a shared directory. The filename is build from the
     * name of the report-definition, the current time and the format of the
     * report.
     * 
     * @param jasperPrint
     * @param format
     * @return path to the stored resource
     * @throws JRException
     */
    private String saveReport(ReportDefinition reportDef,
            JasperPrint jasperPrint, ReportFormat format) throws JRException {

        // generate path
        String destFileName = generateReportName(REPORT_STORE_PATH,
                                                 reportDef.getTemplateName(),
                                                 format.name());

        // export according to format
        String reportName = null;
        switch (format) {
        case pdf:
            JasperExportManager.exportReportToPdfFile(jasperPrint,
                                                      destFileName);
            break;
        case html:
            JasperExportManager.exportReportToHtmlFile(jasperPrint,
                                                       destFileName);
            break;
        case xml:
            JasperExportManager.exportReportToXmlFile(jasperPrint,
                                                      destFileName, true);
            break;
        case csv:
            JRCsvExporter exporter = new JRCsvExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT,
                                  jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                                  destFileName);
            exporter.exportReport();
            break;
        default:
            throw new IllegalStateException("unhandled format " + format);
        }

        return reportName;
    }

    @Override
    public Collection<ReportDefinition> getReportDefinitions() {

        Collection<ReportDefinition> result = new ArrayList<ReportDefinition>();
        for (RepositoryTyp typ : RepositoryTyp.values()) {
            result.addAll(getRepo(typ).getAllReportDefinitions());
        }

        return result;
    }

    /**
     * Generates a path to store the report result to, containing the report
     * name, the current date and time and the format as suffix
     * 
     * @param reportDirectory
     * @param reportName
     * @param reportFormat
     * @return
     */
    private String generateReportName(String reportDirectory,
            String reportName, String reportFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return reportDirectory + reportName + sdf.format(new Date()) + "."
                + reportFormat;
    }

}
