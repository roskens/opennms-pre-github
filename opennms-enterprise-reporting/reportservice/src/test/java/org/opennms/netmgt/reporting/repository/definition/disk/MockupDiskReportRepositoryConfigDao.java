package org.opennms.netmgt.reporting.repository.definition.disk;

import java.util.ArrayList;
import java.util.List;

import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.repository.definition.RepositoryTyp;

public class MockupDiskReportRepositoryConfigDao implements
        DiskReportRepositoryConfigDao {

	@Override
	public List<ReportDefinition> getReportDefinitionList() {
		List<ReportDefinition> reportDefs = new ArrayList<ReportDefinition>();
		ReportDefinition reportDefinition = new ReportDefinition();
		reportDefinition.setId(1);
		reportDefinition.setName("Trivial Report");
		reportDefinition.setTemplateName("trivial-report");
		reportDefinition.setDescription("for testing purpose...");
		reportDefinition.setRepositoryTyp(RepositoryTyp.DISK);
		reportDefs.add(reportDefinition);

		reportDefinition = new ReportDefinition();
		reportDefinition.setId(2);
		reportDefinition.setName("Not Subscribed Report");
		reportDefinition.setTemplateName("trivial-report");
		reportDefinition.setDescription("for testing purpose...");
		reportDefinition.setRepositoryTyp(RepositoryTyp.DISK);
		reportDefs.add(reportDefinition);

		reportDefinition = new ReportDefinition();
		reportDefinition.setId(3);
		reportDefinition.setName("So damn subscribed");
		reportDefinition.setTemplateName("fancy-shmancy-report");
		reportDefinition.setDescription("for getting the world purpose...");
		reportDefinition.setRepositoryTyp(RepositoryTyp.DISK);
		reportDefs.add(reportDefinition);
		return reportDefs;
	}
}