package org.opennms.features.backup.api.helpers;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.opennms.features.backup.api.rest.RestBackupSet;
import org.opennms.features.backup.api.rest.RestZipBackupContents;

public class RestClient {

    private static Client getClient(String username, String password) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("chris", "bla"));
        return client;
    }

    public static RestBackupSet getBackupSet(String location, String username, String password, String customerId, String system) {
        Client client = getClient(username, password);
        WebResource webResource = client.resource(location + "/backupSet/" + customerId + "/" + system);
        return webResource.header("Accept", "application/xml").get(new GenericType<RestBackupSet>() {
        });
    }

    public static RestZipBackupContents getZipBackupContents(String location, String username, String password, String customerId, String system, String timestamp) {
        Client client = getClient(username, password);
        WebResource webResource = client.resource(location + "/zipBackupContents/" + customerId + "/" + system + "/" + timestamp);
        return webResource.header("Accept", "application/xml").get(new GenericType<RestZipBackupContents>() {
        });
    }
}
