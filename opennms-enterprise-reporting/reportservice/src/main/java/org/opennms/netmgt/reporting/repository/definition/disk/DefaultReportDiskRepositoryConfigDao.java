package org.opennms.netmgt.reporting.repository.definition.disk;

import java.util.ArrayList;
import java.util.List;

import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;


public class DefaultReportDiskRepositoryConfigDao implements
		ReportDiskRepositoryConfigDao {
	
//  TODO Tak: read real xml config report-disk-repository.xml
//	private static final String REPORT_DISK_REPOSITORY_XML = System
//			.getProperty("opennms.home")
//			+ File.separator
//			+ "etc"
//			+ File.separator + "report-disk-repository.xml";
	
	@Override
	public List<ReportDefinition> getReportDefinitions() {
		//TODO Tak: Implement me!
		List<ReportDefinition> reportDefs = new ArrayList<ReportDefinition>();
		return reportDefs;
	}
}