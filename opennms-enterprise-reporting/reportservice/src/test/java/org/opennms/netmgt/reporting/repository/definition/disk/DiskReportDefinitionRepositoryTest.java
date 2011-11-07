package org.opennms.netmgt.reporting.repository.definition.disk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DiskReportDefinitionRepositoryTest {

    private DiskReportDefinitionRepository diskRepo;
    private DiskReportRepositoryConfigDao mockDao;
    private List<ReportDefinition> mockReportList;
    private ReportDefinition mockReportDefinition;
    @Before
    public void setUp() {
        mockDao = createMock("mockDao", DiskReportRepositoryConfigDao.class);
        mockReportList = createMock("mockReportList", List.class);
        mockReportDefinition = new ReportDefinition();

        // EasyMock just supports creatMock of interfaces in this version?
        //mockReportDefinition = createMock("mockReportDefinition", ReportDefinition.class);

        diskRepo = new DiskReportDefinitionRepository();
        diskRepo.setDiskReportRepositoryConfigDao(mockDao);
    }

    @After
    public void cleanUp() {
        reset(mockDao);
    }

    @Test
    public final void testGetAllReportDefinitionsIfconfigReturnsNull() {
        expect(mockDao.getReportDefinitionList()).andReturn(null);
        expect(mockDao.getReportDefinitionList()).andReturn(null);
        replay(mockDao);
        Collection<ReportDefinition> results;
        results = diskRepo.getAllReportDefinitions();
        assertEquals("number of reports found", 0, results.size());
        verify(mockDao);
	}

    @Test
    public final void testGetAllReportDefinitionsIfconfigIsNull() {
        diskRepo.setDiskReportRepositoryConfigDao(null);
        Collection<ReportDefinition> results;
        results = diskRepo.getAllReportDefinitions();
        assertEquals("number of reports found", 0, results.size());
	}

    @Test
    public final void testGetAllReportDefinitions() {
        List<ReportDefinition> reportList = new ArrayList<ReportDefinition>();
        mockReportDefinition.setName("TestName");
        reportList.add(mockReportDefinition);

        expect(mockDao.getReportDefinitionList()).andReturn(reportList);
        expect(mockDao.getReportDefinitionList()).andReturn(reportList);
        replay(mockDao);

        List<ReportDefinition> results;
        results = (List<ReportDefinition>)diskRepo.getAllReportDefinitions();
        assertEquals("number of reports found", 1, results.size());
        assertEquals("TestName", results.get(0).getName());
        verify(mockDao);
    }
}
