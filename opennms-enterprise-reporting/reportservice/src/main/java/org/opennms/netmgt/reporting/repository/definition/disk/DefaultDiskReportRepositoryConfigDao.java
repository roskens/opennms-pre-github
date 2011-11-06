package org.opennms.netmgt.reporting.repository.definition.disk;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

@XmlRootElement(name = "ReportDefinitions")
public class DefaultDiskReportRepositoryConfigDao implements
        DiskReportRepositoryConfigDao {

	private static final String REPORT_DISK_REPOSITORY_XML = System
			.getProperty("opennms.home")
			+ File.separator
			+ "etc"
			+ File.separator + "report-disk-repository.xml";

	private List<ReportDefinition> m_reportDefs = new ArrayList<ReportDefinition>();

	@XmlElement(name = "ReportDefinition")
	public List<ReportDefinition> getReportDefinitions() {
		return m_reportDefs;
	}

	@Override
	@XmlTransient
	public List<ReportDefinition> getReportDefinitionList() {
        // TODO indigo: We need a model
		DefaultDiskReportRepositoryConfigDao reportRepositoryConfigDao = null;
		try {
			JAXBContext context = JAXBContext
					.newInstance(DefaultDiskReportRepositoryConfigDao.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			reportRepositoryConfigDao = (DefaultDiskReportRepositoryConfigDao) unmarshaller
					.unmarshal(new FileReader(REPORT_DISK_REPOSITORY_XML));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != reportRepositoryConfigDao) {
			m_reportDefs = reportRepositoryConfigDao.getReportDefinitions();
		} else {
			m_reportDefs = null;
		}
		return m_reportDefs;
	}

	@Override
	public String toString() {
		String result = "DefaultDiskRepositoryConfigDao [reportDefinitions=";

		for (ReportDefinition reportDefinition : m_reportDefs) {
			result = result.concat(reportDefinition.toString());
		}
		result = result.concat("]");

		return result;
	}
}
