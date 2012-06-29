package org.opennms.core.utils.url;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericURLConnection extends URLConnection {

    protected GenericURLConnection(URL url) {
        super(url);
    }

    protected String getUsername(URL url) {
        String userInfo = url.getUserInfo();
        if (userInfo != null) {
            if (userInfo.contains(":")) {
                String[] userPass = userInfo.split(":");
                return userPass[0];
            } else {
                return userInfo;
            }
        } else {
            return null;
        }
    }

    protected String getPassword(URL url) {
        String userInfo = url.getUserInfo();
        if (userInfo != null) {
            if (userInfo.contains(":")) {
                String[] userPass = userInfo.split(":");
                return userPass[1];
            } else {
                return userInfo;
            }
        } else {
            return null;
        }
    }

    protected Map<String, String> getQueryArgs(URL url) {
        HashMap<String, String> hashMap = new HashMap<String, String>();

        String queryString = url.getQuery();

        if (queryString != null) {

            try {
                queryString = URLDecoder.decode(queryString, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String[] queryArgs = queryString.split("[&;]");

            for (String queryArg : queryArgs) {

                String key = queryArg;
                String value = "";

                if (queryArg.contains("=")) {
                    String[] keyValue = queryArg.split("=");

                    key = keyValue[0];
                    value = keyValue[1];

                }

                if (!"".equals(key))
                    hashMap.put(key, value);
            }
        }
        return hashMap;
    }
}
