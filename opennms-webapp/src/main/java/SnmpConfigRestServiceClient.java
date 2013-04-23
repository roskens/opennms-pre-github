import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBException;

import org.apache.commons.httpclient.HttpException;
import org.opennms.web.snmpinfo.SnmpInfo;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class SnmpConfigRestServiceClient {

	private static int getPort(int defaultPort) {
		String port = System.getenv("JERSEY_HTTP_PORT");
		if (null != port) {
			try {
				return Integer.parseInt(port);
			} catch (NumberFormatException e) {
			}
		}
		return defaultPort;
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost/").port(getPort(8980)).path("opennms/rest/").build(null);
	}

	public static final URI BASE_URI = getBaseURI();

	private WebResource service;

	public SnmpConfigRestServiceClient() {
	}
	
	private Client getClient() {
		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		client.addFilter(new HTTPBasicAuthFilter("admin", "admin"));
		return client;
	}

	public WebResource.Builder createWebResourceBuilder(URI url) {
		service = getClient().resource(url);
		MediaType[] mediaTypes = new MediaType[] { MediaType.valueOf(MediaType.APPLICATION_XML) };
		WebResource.Builder builder = service.accept(mediaTypes);
		return builder;
	}

	public SnmpInfo getSnmpInfo(String ipAddress) {			
		WebResource.Builder builder = createWebResourceBuilder(UriBuilder.fromUri("http://localhost/")
				.port(getPort(8980)).path("opennms/rest/snmpConfig/" + ipAddress).build((Object[]) null));
		GenericType<SnmpInfo> genericType = new GenericType<SnmpInfo>() {
		};
		return builder.get(genericType);
	}

	public void setSnmpInfo(String ipAddress, SnmpInfo snmpInfo) {
		try {
			service = getClient().resource(UriBuilder.fromUri("http://localhost/").port(getPort(8980))
					.path("opennms/rest/snmpConfig/" + ipAddress).build((Object[]) null));
			service.type("application/xml").put(snmpInfo);
		} catch (UniformInterfaceException e) {
			if (e.getResponse().getStatus() == 401) {
				throw new IllegalStateException(e);
			} else if (e.getResponse().getStatus() == 403) {
				throw new IllegalStateException(e);
			} else {
				throw e;
			}
		}
	}

	public static void main(String[] args) throws HttpException, IOException, JAXBException {
		SnmpConfigRestServiceClient wiu = new SnmpConfigRestServiceClient();
		System.out.println(wiu.getSnmpInfo("1.1.1.1"));
	}
}
