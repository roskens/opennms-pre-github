/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/
package org.opennms.features.backup.api.helpers;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.opennms.features.backup.api.rest.RestBackupSet;
import org.opennms.features.backup.api.rest.RestZipBackupContents;

/**
 * This helper class provides some rest functionality for querying a remote backup server.
 *
 * @author Christian Pape
 */
public class RestClient {

    /**
     * Constructs the REST client.
     *
     * @param username the username to be used
     * @param password the password to be used
     * @return the REST client
     */
    private static Client getClient(String username, String password) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(username, password));
        return client;
    }

    /**
     * Returns the {@link RestBackupSet} instance for a given customer system.
     *
     * @param location   the remote server location
     * @param username   the username
     * @param password   the password
     * @param customerId the customer's id
     * @param system     the system's id
     * @return the {@link RestBackupSet} instance
     */
    public static RestBackupSet getBackupSet(String location, String username, String password, String customerId, String system) {
        Client client = getClient(username, password);
        WebResource webResource = client.resource(location + "/backupSet/" + customerId + "/" + system);
        return webResource.header("Accept", "application/xml").get(new GenericType<RestBackupSet>() {
        });
    }

    /**
     * Returns the {@link RestZipBackupContents} instance for a backup with the given timestamp.
     *
     * @param location   the remote server location
     * @param username   the username
     * @param password   the password
     * @param customerId the customer's id
     * @param system     the system's id
     * @param timestamp  the backup timestamp
     * @return the {@link RestZipBackupContents} instance
     */
    public static RestZipBackupContents getZipBackupContents(String location, String username, String password, String customerId, String system, String timestamp) {
        Client client = getClient(username, password);
        WebResource webResource = client.resource(location + "/zipBackupContents/" + customerId + "/" + system + "/" + timestamp);
        return webResource.header("Accept", "application/xml").get(new GenericType<RestZipBackupContents>() {
        });
    }
}
