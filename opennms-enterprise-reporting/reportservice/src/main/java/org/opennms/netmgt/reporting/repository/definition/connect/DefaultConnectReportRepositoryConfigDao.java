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

    private DefaultConnectReportRepositoryConfigDao m_defaultConnectReportRepositoryConfigDao;

    @XmlElement(name = "serverUri")
    public UriInfo getConnectServerURI() {
        unmarshallConfig();
        return m_defaultConnectReportRepositoryConfigDao.getConnectServerURI();
    }

    @XmlElement(name = "apiKey")
    public String getConnectApiKey() {
        unmarshallConfig();
        return m_defaultConnectReportRepositoryConfigDao.getConnectApiKey();
    }

    @XmlElement(name = "credentials")
    public String getCredentials() {
        unmarshallConfig();
        return m_defaultConnectReportRepositoryConfigDao.getCredentials();
    }

    @XmlElement(name = "reportingActive")
    public Boolean getReportingActive() {
        return m_defaultConnectReportRepositoryConfigDao.getReportingActive();
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
            m_defaultConnectReportRepositoryConfigDao = (DefaultConnectReportRepositoryConfigDao) unmarshaller
                    .unmarshal(new FileReader(CONNECT_REPORT_REPOSITORY_XML));
        } catch (Exception e) {
            // TODO indigo: error handling
            e.printStackTrace();
        }
    }
}
