/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.ncs.northbounder.transfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Tests Marshaling of North bound Alarm
 * FIXME: This is just a stub for getting started. Needs lots of work.
 *
 * @author <a mailto:brozow@opennms.org>Matt Brozowski</a>
 * @author <a mailto:david@opennms.org>David Hustace</a>
 */
public class MarshallTest {

    /** The m_marshaller. */
    private Marshaller m_marshaller;

    /** The m_unmarshaller. */
    private Unmarshaller m_unmarshaller;

    /**
     * Sets the up.
     *
     * @throws JAXBException
     *             the jAXB exception
     */
    @Before
    public void setUp() throws JAXBException {

        // Create a Marshaller
        JAXBContext context = JAXBContext.newInstance(ServiceAlarmNotification.class);
        m_marshaller = context.createMarshaller();
        m_marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        m_unmarshaller = context.createUnmarshaller();

    }

    /**
     * Marshall to ut f8.
     *
     * @param <T>
     *            the generic type
     * @param t
     *            the t
     * @return the byte[]
     * @throws JAXBException
     *             the jAXB exception
     */
    private <T> byte[] marshallToUTF8(T t) throws JAXBException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // marshall the output
        m_marshaller.marshal(t, out);

        // verify its matches the expected results
        byte[] utf8 = out.toByteArray();

        return utf8;
    }

    /**
     * Unmarshall from ut f8.
     *
     * @param <T>
     *            the generic type
     * @param utf8
     *            the utf8
     * @param expected
     *            the expected
     * @return the t
     * @throws JAXBException
     *             the jAXB exception
     */
    private <T> T unmarshallFromUTF8(byte[] utf8, Class<T> expected) throws JAXBException {
        Source source = new StreamSource(new ByteArrayInputStream(utf8));
        return m_unmarshaller.unmarshal(source, expected).getValue();
    }

    /**
     * Test marshall.
     *
     * @throws JAXBException
     *             the jAXB exception
     * @throws UnsupportedEncodingException
     *             the unsupported encoding exception
     * @throws SAXException
     *             the sAX exception
     */
    @Test
    public void testMarshall() throws JAXBException, UnsupportedEncodingException, SAXException {

        String expectedXML = "" + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<ServiceAlarmNotification xmlns=\"http://junosspace.juniper.net/monitoring\">\n"
                + "    <ServiceAlarm>\n" + "        <Id>Id</Id>\n" + "        <Name>Name</Name>\n"
                + "        <Status>Status</Status>\n" + "    </ServiceAlarm>\n" + "    <ServiceAlarm>\n"
                + "        <Id>Id</Id>\n" + "        <Name>Name</Name>\n" + "        <Status>Status</Status>\n"
                + "    </ServiceAlarm>\n" + "    <ServiceAlarm>\n" + "        <Id>Id</Id>\n"
                + "        <Name>Name</Name>\n" + "        <Status>Status</Status>\n" + "    </ServiceAlarm>\n"
                + "    <ServiceAlarm>\n" + "        <Id>Id</Id>\n" + "        <Name>Name</Name>\n"
                + "        <Status>Status</Status>\n" + "    </ServiceAlarm>\n" + "</ServiceAlarmNotification>\n" + "";

        List<ServiceAlarm> svcAlarms = new ArrayList<ServiceAlarm>();
        for (int i = 0; i < 4; i++) {
            svcAlarms.add(new ServiceAlarm("Id", "Name", "Status"));
        }

        ServiceAlarmNotification notification = new ServiceAlarmNotification();
        notification.setServiceAlarms(svcAlarms);

        byte[] utf8 = marshallToUTF8(notification);

        String result = new String(utf8, "UTF-8");
        assertEquals(expectedXML, result);

        System.err.println(result);

        ServiceAlarmNotification read = unmarshallFromUTF8(utf8, ServiceAlarmNotification.class);
        assertNotNull(read);

        String roundTrip = new String(marshallToUTF8(read));
        assertEquals(expectedXML, roundTrip);
    }

}
