package org.opennms.features.backup.api.helpers;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UploadHelper {

    public static boolean upload(String location, String username, String password, String filename) {
        return upload(location, username, password, filename, new HashMap<String, String>());
    }

    public static boolean upload(String location, String username, String password, String filename, Map<String, String> parameters) {
        boolean uploadStatus = false;
        PostMethod filePost = null;
        HttpClient client = null;
        try {
            //create new post method, and set parameters
            filePost = new PostMethod(location + "/upload");
            filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);

            FilePartSource targetFile = new FilePartSource(new File(filename));

            List<Part> parts = new LinkedList<Part>();

            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                parts.add(new StringPart(entry.getKey(), entry.getValue()));
            }

            parts.add(new FilePart("data", targetFile));

            filePost.setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[]{}), filePost.getParams()));

            String authStr = username + ":" + password;
            String authEncoded = Base64.encodeBase64String(authStr.getBytes());

            filePost.addRequestHeader("Authorization", "Basic " + authEncoded);

            client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

            int status = client.executeMethod(filePost);

            // HttpStatus.getStatusText(status)

            uploadStatus = (status == HttpStatus.SC_OK);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            filePost = null;
            client = null;
        }

        return uploadStatus;
    }
}
