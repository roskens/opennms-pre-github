package org.opennms.netmgt.reporting.repository.definition.disk;

import java.util.ArrayList;
import java.util.List;

import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.repository.definition.RepositoryTyp;

//FIXME Tak: Move Mockup into src/test/java. +
// It's her to be reachable during build time. 
// It's used as hard reference at the moment.
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