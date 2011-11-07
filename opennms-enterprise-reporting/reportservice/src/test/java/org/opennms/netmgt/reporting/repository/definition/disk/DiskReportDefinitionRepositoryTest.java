package org.opennms.netmgt.reporting.repository.definition.disk;

import org.junit.Test;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.repository.definition.RepositoryTyp;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DiskReportDefinitionRepositoryTest {

    private DiskReportDefinitionRepository diskRepo = new DiskReportDefinitionRepository(); 
        
    @Test
    public final void testGetAllReportDefinitions() {
    	
    	diskRepo.setM_diskReportRepositoryConfigDao(new MockupDiskReportRepositoryConfigDao());
        List<ReportDefinition> reportDefinisions = (ArrayList<ReportDefinition>) diskRepo.getAllReportDefinitions();
        
        assertEquals("number of reports found", 3, reportDefinisions.size());
        
        ReportDefinition report = reportDefinisions.get(0);
        
        assertEquals("check report name" , "Trivial Report", report.getName());
        assertEquals("check description", "for testing purpose...", report.getDescription());
        assertEquals("check repositoryTyp", RepositoryTyp.DISK, report.getRepositoryTyp());
        assertEquals("check that no versions are set", 0, report.getEngineVersions().size());
        
        report = reportDefinisions.get(1);
        assertEquals("check report name", "Not Subscribed Report", report.getName());
        assertEquals("check description", "for testing purpose...", report.getDescription());
        assertEquals("check repositoryTyp", RepositoryTyp.DISK, report.getRepositoryTyp());
        assertEquals("check that no versions are set", 0, report.getEngineVersions().size());
	}
}
