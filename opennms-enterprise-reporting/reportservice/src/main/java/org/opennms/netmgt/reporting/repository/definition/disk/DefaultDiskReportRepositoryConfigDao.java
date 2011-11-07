package org.opennms.netmgt.reporting.repository.definition.disk;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

public class DefaultDiskReportRepositoryConfigDao implements
        DiskReportRepositoryConfigDao {

	private static final String REPORT_DISK_REPOSITORY_XML = System
			.getProperty("opennms.home")
			+ File.separator
			+ "etc"
			+ File.separator + "report-disk-repository.xml";

	private DiskReportRepositoryConfig m_diskReportDefinitionConfig = new DiskReportRepositoryConfig();

	public List<ReportDefinition> getReportDefinitionList() {
		try {
			JAXBContext context = JAXBContext
					.newInstance(DiskReportRepositoryConfig.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			m_diskReportDefinitionConfig = (DiskReportRepositoryConfig) unmarshaller
					.unmarshal(new FileReader(REPORT_DISK_REPOSITORY_XML));
		} catch (Exception e) {
            //TODO Tak: Do Logging not jokes
            LogUtils.warnf(this, "Funny Log [%s]", e);
			e.printStackTrace();
		}

		return m_diskReportDefinitionConfig.getReportDefinitions();
	}
}
