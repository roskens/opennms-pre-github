package org.opennms.netmgt.reporting.repository.definition.connect;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.connect.reports.remote.api.RemoteReportDefinitionRepository;
import org.opennms.netmgt.connect.reports.remote.api.model.ReportDefinitionSDO;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

public class ConnectReportDefinitionRepositoryTest {

	ConnectReportDefinitionRepository repo = new ConnectReportDefinitionRepository();
    ConnectReportRepositoryConfigDao mockDao;
	RemoteReportDefinitionRepository mockRemoteRepo;
	
	@Before
	public void setUp() throws Exception {
		mockDao = createMock(ConnectReportRepositoryConfigDao.class);
		mockRemoteRepo = createMock(RemoteReportDefinitionRepository.class);
		repo.setRemoteReportRepository(mockRemoteRepo);
		repo.setConnectReportRepositoryConfigDao(mockDao);
	}

	@Test
	public final void connectReportingNotActive() {
		expect(mockDao.getReportingActive()).andReturn(Boolean.FALSE);
		replay(mockDao);
		List<ReportDefinition> expList = new ArrayList<ReportDefinition>();
		List<ReportDefinition> resultList = (List<ReportDefinition>) repo.getAllReportDefinitions();
		assertEquals(0, resultList.size());
		assertEquals(expList, resultList);
		verify(mockDao);
	}
	
	@Test
	public final void connectReportingActive() {
		
		List<ReportDefinitionSDO> mockList = new ArrayList<ReportDefinitionSDO>();
		ReportDefinitionSDO repDef1 = new ReportDefinitionSDO();
		repDef1.setId(1);
		repDef1.setName("RemoteReport");
		repDef1.setDescription("First Mocked Remote Report");
		mockList.add(repDef1);
		
		reset(mockDao);
		expect(mockDao.getReportingActive()).andReturn(Boolean.TRUE);
		replay(mockDao);
		
		expect(mockRemoteRepo.getAvailableReportDefinitions()).andReturn(mockList);
		replay(mockRemoteRepo);
		
		List<ReportDefinition> resultList = (List<ReportDefinition>) repo.getAllReportDefinitions();
		assertEquals(1, resultList.size());
		assertEquals("RemoteReport", resultList.get(0).getName());
		verify(mockDao);
	}
}
