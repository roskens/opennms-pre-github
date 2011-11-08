package org.opennms.netmgt.reporting.repository.definition.disk;

import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ReportDefinitions")
public class DiskReportRepositoryConfig {

    private List<ReportDefinition> m_reportDefinitions = new ArrayList<ReportDefinition>();

    @XmlElement(name = "ReportDefinition")
    public List<ReportDefinition> getReportDefinitions() {

        return m_reportDefinitions;
    }

    public void setReportDefinitions(List<ReportDefinition> reportDefinitions) {
        this.m_reportDefinitions = reportDefinitions;
    }

   	@Override
	public String toString() {
		String result = "DefaultDiskRepositoryConfigDao [reportDefinitions=";

		for (ReportDefinition reportDefinition : m_reportDefinitions) {
			result = result.concat(reportDefinition.toString());
		}
		result = result.concat("]");

		return result;
	}
}
