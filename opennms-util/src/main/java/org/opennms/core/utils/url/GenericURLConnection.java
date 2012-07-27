/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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

package org.opennms.core.utils.url;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Extending the URL connection with functionality to get username, password and the query map.
 *
 * @author <a href="mailto:christian.pape@informatik.hs-fulda.de">Christian Pape</a>
 * @author <a href="mailto:ronny@opennms.org">Ronny Trommer</a>
 */
public abstract class GenericURLConnection extends URLConnection {

    protected GenericURLConnection(URL url) {
        super(url);
    }

    /**
     * <p>getUsername</p>
     * <p/>
     * Get username from given URL.
     *
     * @param url URL with credentials as {@java.net.URL} object.
     * @return username as {@java.lang.String} object.
     */
    protected String getUsername(URL url) {
        String userInfo = url.getUserInfo();
        if (userInfo != null) {
            if (userInfo.contains(":")) {
                // user info given with user:pass, return user
                String[] userPass = userInfo.split(":");
                return userPass[0];
            } else {
                // user info without pass
                return userInfo;
            }
        } else {
            // no user info available
            return null;
        }
    }

    /**
     * <p>getPassword</p>
     * <p/>
     * Get password from given URL.
     *
     * @param url URL with credentials as {@java.lang.URL} object.
     * @return password in plaintext as {@java.lang.String} object.
     */
    protected String getPassword(URL url) {
        String userInfo = url.getUserInfo();
        if (userInfo != null) {
            if (userInfo.contains(":")) {
                // user info given with user:pass, return pass
                String[] userPass = userInfo.split(":");
                return userPass[1];
            } else {
                // user info without pass, returns the username
                return userInfo;
            }
        } else {
            // no user infor available
            return null;
        }
    }

    /**
     * <p>getQueryArgs</p>
     * <p/>
     * Get the URL query arguments as Map.
     *
     * @param url URL with query arguments as {@java.net.URL} object.
     * @return query arguments as key value {@java.util.Map} object.
     */
    protected Map<String, String> getQueryArgs(URL url) {
        // initialize
        HashMap<String, String> hashMap = new HashMap<String, String>();

        // get the whole query string
        String queryString = url.getQuery();

        if (queryString != null) {
            // query string is available
            try {
                // decode query string as UTF-8
                queryString = URLDecoder.decode(queryString, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // Parse string and tokenize by &
            String[] queryArgs = queryString.split("[&;]");

            // loop to every argument and assign key, values from query string in a HashMap
            for (String queryArg : queryArgs) {

                String key = queryArg;
                String value = "";

                // extract key and value from query string
                if (queryArg.contains("=")) {
                    String[] keyValue = queryArg.split("=");

                    key = keyValue[0];
                    value = keyValue[1];
                }

                // assign to Map
                if (!"".equals(key)) {
                    hashMap.put(key, value);
                }
            }
        }
        return hashMap;
    }
}
