package org.opennms.netmgt.reporting.repository.definition.connect;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.File;
import java.io.FileReader;

//TODO Tak: split dao and data
@XmlRootElement(name = "ConnectReportConfiguration")
public class DefaultConnectReportRepositoryConfigDao implements ConnectReportRepositoryConfigDao{
	private static final String CONNECT_REPORT_REPOSITORY_XML = System
			.getProperty("opennms.home")
			+ File.separator
			+ "etc"
			+ File.separator + "connect-report-repository.xml";

    private UriInfo m_uriInfo;

    private String m_apiKey;

    private String m_credentials;

    private Boolean m_reportingActive = Boolean.FALSE;

    private ConnectReportRepositoryConfigDao m_connectReportRepositoryConfigDao;

    public DefaultConnectReportRepositoryConfigDao() {
        this.m_connectReportRepositoryConfigDao = new DefaultConnectReportRepositoryConfigDao();
    }

    @XmlElement(name = "serverUri")
    public UriInfo getConnectServerURI() {
        unmarshallConfig();
        return m_connectReportRepositoryConfigDao.getConnectServerURI();
    }

    @XmlElement(name = "apiKey")
    public String getConnectApiKey() {
        unmarshallConfig();
        return m_connectReportRepositoryConfigDao.getConnectApiKey();
    }

    @XmlElement(name = "credentials")
    public String getCredentials() {
        unmarshallConfig();
        return m_connectReportRepositoryConfigDao.getCredentials();
    }

    @XmlElement(name = "reportingActive")
    public Boolean getReportingActive() {
        return m_connectReportRepositoryConfigDao.getReportingActive();
    }

	@Override
	public String toString() {
		String result = "DefaultConnectRepositoryConfigDao [opennmsConnectConfiguration=";

        result = "Connect server URI: " + getConnectServerURI() + "; Connect API key: " + getConnectApiKey() + "; " + "Connect REST credentials: " + getCredentials();

		return result;
	}

    @XmlTransient
    private void unmarshallConfig() {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(DefaultConnectReportRepositoryConfigDao.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            m_connectReportRepositoryConfigDao = (DefaultConnectReportRepositoryConfigDao) unmarshaller
                    .unmarshal(new FileReader(CONNECT_REPORT_REPOSITORY_XML));
        } catch (Exception e) {
            // TODO indigo: error handling
            e.printStackTrace();
        }
    }
}
