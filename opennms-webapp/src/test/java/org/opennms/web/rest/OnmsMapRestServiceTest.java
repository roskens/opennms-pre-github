package org.opennms.web.rest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.ws.rs.core.MediaType;

public class OnmsMapRestServiceTest  extends AbstractSpringJerseyRestTestCase {
    @Test
    public void testGetMap() {

    }    
    private void sendPost(String url, String xml) throws Exception {
        sendData(POST, MediaType.APPLICATION_XML, url, xml);
    }

    private void sendPut(String url, String formData) throws Exception {
        sendData(PUT, MediaType.APPLICATION_FORM_URLENCODED, url, formData);
    }

    private void sendData(String requestType, String contentType, String url, String data) throws Exception {
        MockHttpServletRequest request = createRequest(requestType, url);
        request.setContentType(contentType);
        request.setContent(data.getBytes());
        MockHttpServletResponse response = createResponse();
        dispatch(request, response);
        assertEquals(200, response.getStatus());
    }

    private String sendRequest(String requestType, String url, int spectedStatus) throws Exception {
        MockHttpServletRequest request = createRequest(requestType, url);
        MockHttpServletResponse response = createResponse();
        dispatch(request, response);
        assertEquals(spectedStatus, response.getStatus());
        String xml = response.getContentAsString();
        if (xml != null)
            System.err.println(xml);
        return xml;
    }

    private void createMap() throws Exception {
        String node = "<map>" +

        "<label>TestMachine</label>" +
        "<labelSource>H</labelSource>" +
        "<sysContact>The Owner</sysContact>" +
        "<sysDescription>" +
        "Darwin TestMachine 9.4.0 Darwin Kernel Version 9.4.0: Mon Jun  9 19:30:53 PDT 2008; root:xnu-1228.5.20~1/RELEASE_I386 i386" +
        "</sysDescription>" +
        "<sysLocation>DevJam</sysLocation>" +
        "<sysName>TestMachine</sysName>" +
        "<sysObjectId>.1.3.6.1.4.1.8072.3.2.255</sysObjectId>" +
        "<type>A</type>" +
                
        "</map>";
        sendPost("/nodes", node);
    }
}
