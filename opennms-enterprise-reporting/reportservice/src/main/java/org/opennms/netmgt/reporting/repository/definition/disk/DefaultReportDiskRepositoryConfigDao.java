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
public class DefaultReportDiskRepositoryConfigDao implements
		ReportDiskRepositoryConfigDao {

	private static final String REPORT_DISK_REPOSITORY_XML = System
			.getProperty("opennms.home")
			+ File.separator
			+ "etc"
			+ File.separator + "report-disk-repository.xml";

	private List<ReportDefinition> reportDefs = new ArrayList<ReportDefinition>();

	@XmlElement(name = "ReportDefinition")
	public List<ReportDefinition> getReportDefinitions() {
		return reportDefs;
	}

	@Override
	@XmlTransient
	public List<ReportDefinition> getReportDefinitionList() {
		DefaultReportDiskRepositoryConfigDao reportRepositoryConfigDao = null;
		try {
			JAXBContext context = JAXBContext
					.newInstance(DefaultReportDiskRepositoryConfigDao.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			reportRepositoryConfigDao = (DefaultReportDiskRepositoryConfigDao) unmarshaller
					.unmarshal(new FileReader(REPORT_DISK_REPOSITORY_XML));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != reportRepositoryConfigDao) {
			reportDefs = reportRepositoryConfigDao.getReportDefinitions();
		} else {
			reportDefs = null;
		}
		return reportDefs;
	}

	@Override
	public String toString() {
		String result = "DefaultDiskRepositoryConfigDao [reportDefinitions=";

		for (ReportDefinition reportDefinition : reportDefs) {
			result = result.concat(reportDefinition.toString());
		}
		result = result.concat("]");

		return result;
	}
}
