package org.opennms.netmgt.reporting.repository.disk.dao;

import java.util.List;

import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

public interface DiskReportRepositoryConfigDao {

	/**
	 * <p>getReportDefinitions</p>
	 * 
	 * @return a {@link java.util.List} object.
	 */
	List<ReportDefinition> getReportDefinitionList();
}
