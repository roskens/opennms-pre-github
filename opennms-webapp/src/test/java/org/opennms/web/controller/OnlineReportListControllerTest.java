package org.opennms.web.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

public class OnlineReportListControllerTest {
	
	private OnlineReportListController m_onlineReportListController = new OnlineReportListController();

	@Test
	public void onlineReportListControllerNotNull () {
		assertNotNull(m_onlineReportListController);
		assertNotNull(m_onlineReportListController.getReportService());
		assertNotNull(m_onlineReportListController.getReportService().getReportDefinitions());
	}
	
	@Test
	public void onlineReportListControllerListReportDefinitions () {
		assertEquals(4, m_onlineReportListController.getReportService().getReportDefinitions().size());
		assertEquals("Trivial Report", ((ReportDefinition)m_onlineReportListController.getReportService().getReportDefinitions().toArray()[0]).getName());
	}
}
