package org.opennms.netmgt.reporting.repository.definition.connect;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.connect.reports.remote.api.RemoteReportDefinitionRepository;
import org.opennms.netmgt.connect.reports.remote.api.model.ReportDefinitionSDO;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinitionRepository;

/**
 * Provides access to the report-templates from the opennms connect server.
 * It's the client for @ {link DefaultRemoteReportDefinitionRepository}
 * 
 * @author thargor
 */
public class ConnectReportDefinitionRepository implements
        ReportDefinitionRepository {

    private ConnectReportRepositoryConfigDao m_connectReportRepositoryConfigDao;

    // TODO thargor: ConnectReportRepositoryConfig
    private static final String URL = "http://localhost/opennms/rest/connect/reports";
    
    // TODO thargor: Inject Webservice stub
    private RemoteReportDefinitionRepository m_remoteReportRepository;

    public ConnectReportDefinitionRepository () {
    }

    @SuppressWarnings("unchecked")
    public Collection<ReportDefinition> getAllReportDefinitions() {
        Collection<ReportDefinition> result = new ArrayList<ReportDefinition>();

        if (m_connectReportRepositoryConfigDao != null) {
            if (Boolean.TRUE.equals(m_connectReportRepositoryConfigDao.getReportingActive())) {
                if (m_remoteReportRepository != null) {

                    Collection<ReportDefinitionSDO> sdoResult = new ArrayList<ReportDefinitionSDO>();
                    Object temp = m_remoteReportRepository.getAvailableReportDefinitions();
                    if (temp != null) {
                        sdoResult.addAll((Collection<ReportDefinitionSDO>) temp);
                    }
                    result = ReportDefinitionSDOMapper.fromCollection(sdoResult);

//TODO Tak: move that code into a remoteReportRepository implementation?
//            DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
//            config.getState().setCredentials(null, null, -1, configDao.getUserName(), configDao.getConnectReportPassword());
//            ApacheHttpClient c = ApacheHttpClient.create(config);
//            WebResource r = c.resource(URL);
//            Collection<?> restResult = r.get(Collection.class);
//            result = ReportDefinitionSDOMapper.fromCollection((Collection<ReportDefinitionSDO>) restResult);
                } else {
                    logRemoteReportRepositoryProblem();
                }
            } else {
                logNotActive();
            }
        } else {
            logConfigProblem();
        }


        return result;
    }

    public ReportDefinition getReportDefinition(String name) {
        throw new IllegalStateException(
                                        "requesting reports by name is not supported by remote repository!");
    }

    public InputStream getReportTemplate(Integer id, String version) {
        if (m_connectReportRepositoryConfigDao != null) {
            if (Boolean.TRUE.equals(m_connectReportRepositoryConfigDao.getReportingActive())) {
                if (m_remoteReportRepository != null) {
                    return m_remoteReportRepository.getReportTemplate(id, version);
                } else {
                    logRemoteReportRepositoryProblem();
                    return null;
                }
            } else {
                logConfigProblem();
                return null;
            }
        } else {
            logNotActive();
            return null;
        }
    }

    public ReportDefinition getReportDefinition(Integer id) {
         if (m_connectReportRepositoryConfigDao != null) {
            if (Boolean.TRUE.equals(m_connectReportRepositoryConfigDao.getReportingActive())) {
                if (m_remoteReportRepository != null) {
            return ReportDefinitionSDOMapper.fromSDO(m_remoteReportRepository.getReportDefinition(id));
                     } else {
                    logRemoteReportRepositoryProblem();
                    return null;
                }
            } else {
                logConfigProblem();
                return null;
            }
        } else {
            logNotActive();
            return null;
        }
    }

    private void logNotActive() {
        LogUtils.infof(this, "ConnectReportDefinitionRepository not active");
    }

    private void logConfigProblem() {
        LogUtils.infof(this, "ConnectReportRepositoryConfig problem. Config is null");
    }

    private void logRemoteReportRepositoryProblem() {
        LogUtils.infof(this, "RemoteReportRepository problem. Repository is null");
    }

	public ConnectReportRepositoryConfigDao getConnectReportRepositoryConfigDao() {
		return m_connectReportRepositoryConfigDao;
	}

	public void setConnectReportRepositoryConfigDao(ConnectReportRepositoryConfigDao connectReportRepositoryConfigDao) {
		this.m_connectReportRepositoryConfigDao = connectReportRepositoryConfigDao;
	}

	public RemoteReportDefinitionRepository getRemoteReportRepository() {
		return m_remoteReportRepository;
	}

	public void setRemoteReportRepository(
			RemoteReportDefinitionRepository remoteReportRepository) {
		this.m_remoteReportRepository = remoteReportRepository;
	}
}
