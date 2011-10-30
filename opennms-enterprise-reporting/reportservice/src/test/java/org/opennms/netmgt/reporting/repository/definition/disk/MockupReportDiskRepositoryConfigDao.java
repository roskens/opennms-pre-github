package org.opennms.netmgt.reporting.repository.definition.disk;

import java.util.ArrayList;
import java.util.List;

import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

public class MockupReportDiskRepositoryConfigDao implements
		ReportDiskRepositoryConfigDao {

	@Override
	public List<ReportDefinition> getReportDefinitions() {
		List<ReportDefinition> reportDefs = new ArrayList<ReportDefinition>();
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
		return reportDefs;
	}
}