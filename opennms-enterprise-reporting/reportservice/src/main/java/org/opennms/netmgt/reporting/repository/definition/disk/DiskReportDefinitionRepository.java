package org.opennms.netmgt.reporting.repository.definition.disk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinitionRepository;

/**
 * Provides access to the report-templates from the local opennms
 * installation.
 * 
 * @author thargor
 */
public class DiskReportDefinitionRepository implements
        ReportDefinitionRepository {

    // FIXME thargor: DiskReportRepositoryConfig template directory
    //System.getProperty("opennms.home")
    private static final String TEMPLATE_DIR = "/tmp" 
            + File.separator
            + "etc"
            + File.separator
            + "report-templates"
            + File.separator;

    // FIXME thargor: DiskReportRepositoryConfig, list of reports and their
    // descriptions
    private static final List<ReportDefinition> reportDefs = new ArrayList<ReportDefinition>();

    private static final String TEMPLATE_SUFFIX = ".jrxml";

    static {
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setId(1);
        reportDefinition.setName("Trivial Report");
        reportDefinition.setTemplateName("trivial-report");
        reportDefinition.setDescription("for testing purpose...");
        reportDefs.add(reportDefinition);

        reportDefinition = new ReportDefinition();
        reportDefinition.setId(2);
        reportDefinition.setName("Not Subscribed Report");
        reportDefinition.setTemplateName("trivial-report");
        reportDefinition.setDescription("for testing purpose...");
        reportDefs.add(reportDefinition);
    }

    private List<ReportDefinition> getReportDefs() {
        return reportDefs;
    }

    @Override
    public Collection<ReportDefinition> getAllReportDefinitions() {

        for (ReportDefinition rd : getReportDefs()) {
            rd.setEngineVersions(getVersions(rd));
        }

        return getReportDefs();
    }

    @Override
    public ReportDefinition getReportDefinition(String name) {

        for (ReportDefinition rd : getReportDefs()) {
            if (rd.getTemplateName().equals(name)) {
                getVersions(rd);
                return rd;
            }
        }

        return null;
    }

    @Override
    public InputStream getReportTemplate(Integer id, String version)
            throws IOException {

        ReportDefinition reportDefinition = getReportDefinition(id);
        if (reportDefinition != null) {
            if (reportDefinition.getEngineVersions().contains(version)) {
                String versionPath;
                if (VERSION_NONE.equals(version))
                    versionPath = "";
                else {
                    versionPath = version + File.separator;
                }
                File file = new File(TEMPLATE_DIR + versionPath
                        + reportDefinition.getTemplateName()
                        + TEMPLATE_SUFFIX);

                return FileUtils.openInputStream(file);
            }
        }

        return null;
    }

    @Override
    public ReportDefinition getReportDefinition(Integer id) {
        for (ReportDefinition rd : getReportDefs()) {
            if (rd.getId().equals(id)) {
                getVersions(rd);
                return rd;
            }
        }
        return null;
    }

    /**
     * @param rd
     * @return
     */
    private List<String> getVersions(ReportDefinition rd) {

        List<String> versions = new ArrayList<String>();
        File rootDir = new File(TEMPLATE_DIR);
        if (rootDir.exists() && rootDir.isDirectory() && rootDir.canRead()) {
            for (File versionDir : rootDir.listFiles()) {
                if (versionDir.isDirectory() && versionDir.canRead()) {
                    File templateFile = new File(versionDir,
                                                 rd.getTemplateName()
                                                         + TEMPLATE_SUFFIX);
                    if (templateFile.exists())
                        versions.add(versionDir.getName());
                }
            }
        } else {
            LogUtils.warnf(this,
                           "report template directory can't be read [%s]",
                           TEMPLATE_DIR);
        }

        File templateFile = new File(rootDir, rd.getTemplateName() + TEMPLATE_SUFFIX);
        if (templateFile.exists() && templateFile.canRead())
            versions.add(VERSION_NONE);

        return versions;
    }
}
