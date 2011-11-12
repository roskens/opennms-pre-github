package org.opennms.netmgt.reporting.repository.connect.dao;

import org.apache.commons.httpclient.URI;
import org.opennms.netmgt.reporting.repository.definition.ReportDefinition;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "opennms-connect")
public interface ConnectReportRepositoryConfigDao {
    public UriInfo getConnectServerURI ();

    public String getConnectApiKey ();

    public String getCredentials ();

    public Boolean getReportingActive();
}
