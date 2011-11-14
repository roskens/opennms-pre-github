package org.opennms.netmgt.reporting.repository.definition.connect;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ConnectReportConfiguration")
public class ConnectReportRepositoryConfig {

    private UriInfo m_uri;

    private String m_apiKey;

    private String m_credentials;

    private Boolean m_reportingActive;

    @XmlElement(name = "serverUri")
    public UriInfo getConnectServerURI() {
        return this.m_uri;
    }

    public void setConnectServerURI(UriInfo uri) {
        this.m_uri = uri;
    }

    @XmlElement(name = "apiKey")
    public String getConnectApiKey() {
        return m_apiKey;
    }

    public void setApiKey(String apiKey) {
        this.m_apiKey = apiKey;
    }

    @XmlElement(name = "credentials")
    public String getCredentials() {
        return m_credentials;
    }

    public void setCredentials(String credentials) {
        this.m_credentials = credentials;
    }

    @XmlElement(name = "reportingActive")
    public Boolean getReportingActive() {
        return m_reportingActive;
    }

    public void setReportingActive(Boolean reportingActive) {
        this.m_reportingActive = reportingActive;
    }

	@Override
	public String toString() {
		String result = "DefaultConnectRepositoryConfigDao [opennmsConnectConfiguration=";

        result = "Connect server URI: " + getConnectServerURI() + "; Connect API key: " + getConnectApiKey() + "; " + "Connect REST credentials: " + getCredentials();

		return result;
	}
}
