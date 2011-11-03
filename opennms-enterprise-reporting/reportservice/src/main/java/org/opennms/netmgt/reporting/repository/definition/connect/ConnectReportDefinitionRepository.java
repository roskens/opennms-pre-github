package org.opennms.netmgt.reporting.repository.definition.connect;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.opennms.netmgt.connect.reports.remote.api.RemoteReportDefinitionRepository;
import org.opennms.netmgt.connect.reports.remote.api.model.ReportDefinitionSDO;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinitionRepository;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

/**
 * Provides access to the report-templates from the opennms connect server.
 * It's the client for {@link DefaultRemoteReportDefinitionRepository}
 * 
 * @author thargor
 */
public class ConnectReportDefinitionRepository implements
        ReportDefinitionRepository {

    // FIXME thargor: ConnectConfig
    private static final String LOGIN = "okay";
    private static final String PASSWORD = "okay";

    // FIXME thargor: ConnectReportRepositoryConfig
    private static final String URL = "http://localhost/opennms/rest/connect/reports";
    private static final Boolean active = true;

    // FIXME thargor: Inject Webservice stub
    private RemoteReportDefinitionRepository m_remoteReportRepository;

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ReportDefinition> getAllReportDefinitions() {
        Collection<ReportDefinition> result = new ArrayList<ReportDefinition>();

        if (Boolean.TRUE.equals(active)) {
//            result = ReportDefinitionSDOMapper.fromCollection(m_remoteReportRepository.getAvailableReportDefinitions());
            
            DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
            config.getState().setCredentials(null, null, -1, LOGIN, PASSWORD);
            ApacheHttpClient c = ApacheHttpClient.create(config);
            WebResource r = c.resource(URL);
            Collection<?> restResult = r.get(Collection.class);
            result = ReportDefinitionSDOMapper.fromCollection((Collection<ReportDefinitionSDO>) restResult);
            
        } else {
            logNotActive();
        }

        return result;
    }

    @Override
    public ReportDefinition getReportDefinition(String name) {
        throw new IllegalStateException(
                                        "requesting reports by name is not supported by remote repository!");
    }

    @Override
    public InputStream getReportTemplate(Integer id, String version) {
        if (Boolean.TRUE.equals(active)) {
            return m_remoteReportRepository.getReportTemplate(id, version);
        } else {
            logNotActive();
            return null;
        }

    }

    @Override
    public ReportDefinition getReportDefinition(Integer id) {
        if (Boolean.TRUE.equals(active)) {
            return ReportDefinitionSDOMapper.fromSDO(m_remoteReportRepository.getReportDefinition(id));
        } else {
            logNotActive();
            return null;
        }
    }

    private void logNotActive() {
//        LogUtils.infof(this, "ConnectReportDefinitionRepository not active");
    }
}
