package org.opennms.netmgt.reporting.repository.definition.connect;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.opennms.core.utils.LogUtils;
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

	private ReportConnectRepositoryConfigDao configDao = new DefaultReportConnectRepositoryConfigDao();
	// FIXME Tak: Inject configDao;
	// private ReportConnectRepositoryConfigDao configDao;
	
    // FIXME thargor: ConnectConfig
    //private static final String LOGIN = "okay";
	//private static final String PASSWORD = "okay";
	//private static final Boolean active = true;
	
    // FIXME thargor: ConnectReportRepositoryConfig
    private static final String URL = "http://localhost/opennms/rest/connect/reports";
    
    // FIXME thargor: Inject Webservice stub
    private RemoteReportDefinitionRepository m_remoteReportRepository;

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ReportDefinition> getAllReportDefinitions() {
        Collection<ReportDefinition> result = new ArrayList<ReportDefinition>();

        if (configDao != null && Boolean.TRUE.equals(configDao.getReportingActive())) {
         
        	result = ReportDefinitionSDOMapper.fromCollection(m_remoteReportRepository.getAvailableReportDefinitions());

//TODO Tak: move that code into a remoteReportRepository implementation?       
//            DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
//            config.getState().setCredentials(null, null, -1, configDao.getUserName(), configDao.getConnectReportPassword());
//            ApacheHttpClient c = ApacheHttpClient.create(config);
//            WebResource r = c.resource(URL);
//            Collection<?> restResult = r.get(Collection.class);
//            result = ReportDefinitionSDOMapper.fromCollection((Collection<ReportDefinitionSDO>) restResult);
            
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
        if (configDao != null && Boolean.TRUE.equals(configDao.getReportingActive())) {
            return m_remoteReportRepository.getReportTemplate(id, version);
        } else {
            logNotActive();
            return null;
        }

    }

    @Override
    public ReportDefinition getReportDefinition(Integer id) {
        if (configDao != null && Boolean.TRUE.equals(configDao.getReportingActive())) {
            return ReportDefinitionSDOMapper.fromSDO(m_remoteReportRepository.getReportDefinition(id));
        } else {
            logNotActive();
            return null;
        }
    }

    private void logNotActive() {
        LogUtils.infof(this, "ConnectReportDefinitionRepository not active");
    }

	public ReportConnectRepositoryConfigDao getConfigDao() {
		return configDao;
	}

	public void setConfigDao(ReportConnectRepositoryConfigDao configDao) {
		this.configDao = configDao;
	}

	public RemoteReportDefinitionRepository getRemoteReportRepository() {
		return m_remoteReportRepository;
	}

	public void setRemoteReportRepository(
			RemoteReportDefinitionRepository remoteReportRepository) {
		this.m_remoteReportRepository = remoteReportRepository;
	}
}
