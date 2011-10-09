package org.opennms.netmgt.reporting.repository.definition.disk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.repository.definition.RepositoryTyp;

public class DiskReportDefinitionRepositoryTest {

    private DiskReportDefinitionRepository diskRepo = new DiskReportDefinitionRepository(); 
        
    @Test
    public final void testGetAllReportDefinitions() {
        ArrayList<ReportDefinition> reportDefinisions = (ArrayList<ReportDefinition>) diskRepo.getAllReportDefinitions();
        
        assertEquals("number of reports found", 2, reportDefinisions.size());
        
        ReportDefinition report = reportDefinisions.get(0);
        
        assertEquals("check report name" , "Trivial Report", report.getName());
        assertEquals("check description", "for testing purpose...", report.getDescription());
        assertEquals("check repositoryTyp", RepositoryTyp.DISK, report.getRepositoryTyp());
        
        report = reportDefinisions.get(1);
        assertEquals("check report name", "Not Subscribed Report", report.getName());
        assertEquals("check description", "for testing purpose...", report.getDescription());
        assertEquals("check repositoryTyp", RepositoryTyp.DISK, report.getRepositoryTyp());
        
//FIXME Tak: first setup configuration bases repository        
//        try {
//            diskRepo.getReportTemplate(report.getId(), "");
//            BufferedReader reader = new BufferedReader( new InputStreamReader(diskRepo.getReportTemplate(report.getId(), "Version??")));
//            reader.read();
//        } catch (IOException e) {
//            fail("Exception during getReportTemplate");
//            e.printStackTrace();
//        }
        
    }

    @Test
    public final void testGetReportDefinitionString() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testGetReportTemplate() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testGetReportDefinitionInteger() {
        fail("Not yet implemented"); // TODO
    }

}
