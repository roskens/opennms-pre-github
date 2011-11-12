package org.opennms.netmgt.reporting.repository.connect.dao;

import org.opennms.netmgt.reporting.repository.definition.connect.ConnectReportRepositoryConfig;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;
import java.io.File;
import java.io.FileReader;

public class DefaultConnectReportRepositoryConfigDao implements ConnectReportRepositoryConfigDao {
	private static final String CONNECT_REPORT_REPOSITORY_XML = System
			.getProperty("opennms.home")
			+ File.separator
			+ "etc"
			+ File.separator + "connect-report-repository.xml";

    private ConnectReportRepositoryConfig m_connectReportRepositoryConfig;

    public DefaultConnectReportRepositoryConfigDao() {
        this.m_connectReportRepositoryConfig = new ConnectReportRepositoryConfig();
    }

    @XmlTransient
    private void unmarshallConfig() {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(DefaultConnectReportRepositoryConfigDao.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            this.m_connectReportRepositoryConfig = (ConnectReportRepositoryConfig) unmarshaller
                    .unmarshal(new FileReader(CONNECT_REPORT_REPOSITORY_XML));
        } catch (Exception e) {
            // TODO indigo: error handling
            e.printStackTrace();
        }
    }

    @Override
    public UriInfo getConnectServerURI() {
        unmarshallConfig();
        return this.m_connectReportRepositoryConfig.getConnectServerURI();
    }

    @Override
    public String getConnectApiKey() {
        unmarshallConfig();
        return this.m_connectReportRepositoryConfig.getConnectApiKey();
    }

    @Override
    public String getCredentials() {
        unmarshallConfig();
        return this.m_connectReportRepositoryConfig.getCredentials();
    }

    @Override
    public Boolean getReportingActive() {
        unmarshallConfig();
        return this.m_connectReportRepositoryConfig.getReportingActive();
    }
}
