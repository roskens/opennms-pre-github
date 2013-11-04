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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This helper class provides methods for uploading the backup file to a remote location.
 *
 * @author Christian Pape
 */
public class UploadHelper {

    /**
     * Uploads a file to a remote location.
     *
     * @param location the remote server location
     * @param username the username
     * @param password the password
     * @param filename the filename to be uploaded
     * @return true on success, false otherwise
     */
    public static boolean upload(String location, String username, String password, String filename) throws Exception {
        return upload(location, username, password, filename, new HashMap<String, String>());
    }

    /**
     * Uploads a file to a remote location with additional parameters.
     *
     * @param location   the remote server location
     * @param username   the username
     * @param password   the password
     * @param filename   the filename to be uploaded
     * @param parameters additional paramaters {@link Map}
     * @return true on success, false otherwise
     * @throws IOException
     */
    public static boolean upload(String location, String username, String password, String filename, Map<String, String> parameters) throws IOException {
        boolean uploadStatus = false;

        PostMethod filePost;
        HttpClient client;

        /**
         * Create new post method
         */
        filePost = new PostMethod(location + "/upload");
        filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);

        FilePartSource targetFile = new FilePartSource(new File(filename));

        List<Part> parts = new LinkedList<Part>();

        /**
         * Add the parameters
         */
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            parts.add(new StringPart(entry.getKey(), entry.getValue()));
        }

        /**
         * ...and the file
         */
        parts.add(new FilePart("data", targetFile));

        filePost.setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[]{}), filePost.getParams()));

        /**
         * do the authentication stuff
         */
        String authStr = username + ":" + password;
        String authEncoded = Base64.encodeBase64String(authStr.getBytes());

        filePost.addRequestHeader("Authorization", "Basic " + authEncoded);

        client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

        int status = client.executeMethod(filePost);

        uploadStatus = (status == HttpStatus.SC_OK);

        return uploadStatus;
    }
}
