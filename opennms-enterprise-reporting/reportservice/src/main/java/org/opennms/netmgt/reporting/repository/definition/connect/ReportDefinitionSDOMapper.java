package org.opennms.netmgt.reporting.repository.definition.connect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.opennms.netmgt.connect.reports.remote.api.model.ReportDefinitionSDO;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

/**
 * @author thargor
 */
public class ReportDefinitionSDOMapper {

    /**
     * @param reportDef
     * @return
     */
    public static ReportDefinitionSDO toSDO(ReportDefinition reportDef) {

        ReportDefinitionSDO result = new ReportDefinitionSDO();

        result.setDescription(reportDef.getDescription());
        result.setId(reportDef.getId());
        result.setName(reportDef.getName());
        result.setEngineVersions(reportDef.getEngineVersions());

        return result;
    }

    public static Collection<ReportDefinitionSDO> toCollection(
            Collection<ReportDefinition> list) {
        List<ReportDefinitionSDO> result = new ArrayList<ReportDefinitionSDO>();
        for (ReportDefinition reportDef : list) {
            result.add(ReportDefinitionSDOMapper.toSDO(reportDef));
        }

        return result;
    }

    /**
     * @param allReportDefinitions
     * @return
     */
    public static Collection<ReportDefinition> fromCollection(
            Collection<ReportDefinitionSDO> list) {
        Collection<ReportDefinition> result = new ArrayList<ReportDefinition>();
        for (ReportDefinitionSDO reportDef : list) {
            result.add(ReportDefinitionSDOMapper.fromSDO(reportDef));
        }

        return result;
    }

    /**
     * @param reportDef
     * @return
     */
    public static ReportDefinition fromSDO(ReportDefinitionSDO reportDef) {
        ReportDefinition result = new ReportDefinition();
        result.setDescription(reportDef.getDescription());
        result.setId(reportDef.getId());
        result.setName(reportDef.getName());
        result.setEngineVersions(reportDef.getEngineVersions());
        return result;
    }

}
