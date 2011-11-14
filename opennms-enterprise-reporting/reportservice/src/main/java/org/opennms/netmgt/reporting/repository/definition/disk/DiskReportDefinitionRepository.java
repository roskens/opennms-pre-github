package org.opennms.netmgt.reporting.repository.definition.disk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinitionRepository;
import org.opennms.netmgt.reporting.repository.disk.dao.DiskReportRepositoryConfigDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides access to the report-templates from the local opennms installation.
 * 
 * @author thargor
 */
public class DiskReportDefinitionRepository implements
		ReportDefinitionRepository {
    
    private static final Logger log = LoggerFactory.getLogger(DiskReportDefinitionRepository.class);

	private static final String TEMPLATE_DIR = System
			.getProperty("opennms.home")
			+ File.separator
			+ "etc"
			+ File.separator + "report-templates" + File.separator;

	private static final String TEMPLATE_SUFFIX = ".jrxml";

	private DiskReportRepositoryConfigDao m_diskReportRepositoryConfigDao;

    public DiskReportDefinitionRepository(){
    }

    private List<ReportDefinition> getReportDefs() {
        List<ReportDefinition> resultList = new ArrayList<ReportDefinition>();
        if (m_diskReportRepositoryConfigDao != null) {
            Object temp = m_diskReportRepositoryConfigDao.getReportDefinitionList();
            if (temp != null) {
                resultList = (List<ReportDefinition>) temp;
                return resultList;
            } else {
                log.warn("get ReportDefinitions from DiskReportRepositoryConfigDao causes problems [{}]", temp);
                return resultList;
            }
        } else {
            log.warn("DiskReportRepositoryConfigDao is null");
            return resultList;
        }
    }

	public Collection<ReportDefinition> getAllReportDefinitions() {

		for (ReportDefinition rd : getReportDefs()) {
			rd.setEngineVersions(getVersions(rd));
		}

		return getReportDefs();
	}

	public ReportDefinition getReportDefinition(String name) {

		for (ReportDefinition rd : getReportDefs()) {
			if (rd.getTemplateName().equals(name)) {
				getVersions(rd);
				return rd;
			}
		}

		return null;
	}

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
						+ reportDefinition.getTemplateName() + TEMPLATE_SUFFIX);

				return FileUtils.openInputStream(file);
			}
		}

		return null;
	}

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
							rd.getTemplateName() + TEMPLATE_SUFFIX);
					if (templateFile.exists())
						versions.add(versionDir.getName());
				}
			}
		} else {
		    log.warn("report template directory can't be read [{}]", TEMPLATE_DIR);
		}

		File templateFile = new File(rootDir, rd.getTemplateName()
				+ TEMPLATE_SUFFIX);
		if (templateFile.exists() && templateFile.canRead())
			versions.add(VERSION_NONE);

		return versions;
	}

	public DiskReportRepositoryConfigDao getDiskReportRepositoryConfigDao() {
		return m_diskReportRepositoryConfigDao;
	}

	public void setDiskReportRepositoryConfigDao(
            DiskReportRepositoryConfigDao diskReportRepositoryConfigDao) {
		this.m_diskReportRepositoryConfigDao = diskReportRepositoryConfigDao;
	}

}
