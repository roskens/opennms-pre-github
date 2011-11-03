package org.opennms.netmgt.reporting.repository.definition.disk;

import java.util.List;

import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

public interface ReportDiskRepositoryConfigDao {

	/**
	 * <p>getReportDefinitions</p>
	 * 
	 * @return a {@link java.util.List} object.
	 */
	List<ReportDefinition> getReportDefinitionList();
}
