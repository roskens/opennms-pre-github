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

package org.opennms.netmgt.config;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.test.MockLogAppender;
import org.springframework.core.io.ByteArrayResource;

public class PollOutagesConfigManagerTest extends TestCase {

    private PollOutagesConfigManager m_manager;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(PollOutagesConfigManagerTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        MockLogAppender.setupLogging();

        final String xml = "<?xml version=\"1.0\"?>\n" +
                "<outages>\n" +
                "   <outage name=\"one\" type=\"weekly\">\n" +
                "       <time day=\"sunday\" begins=\"12:30:00\" ends=\"12:45:00\"/>\n" +
                "       <time day=\"sunday\" begins=\"13:30:00\" ends=\"14:45:00\"/>\n" +
                "       <time day=\"monday\" begins=\"13:30:00\" ends=\"14:45:00\"/>\n" +
                "       <time day=\"tuesday\" begins=\"13:00:00\" ends=\"14:45:00\"/>\n" +
                "       <interface address=\"192.168.0.1\"/>\n" +
                "       <interface address=\"192.168.0.36\"/>\n" +
                "       <interface address=\"192.168.0.38\"/>\n" +
                "   </outage>\n" +
                "\n" +
                "   <outage name=\"two\" type=\"monthly\">\n" +
                "       <time day=\"1\" begins=\"23:30:00\" ends=\"23:45:00\"/>\n" +
                "       <time day=\"15\" begins=\"21:30:00\" ends=\"21:45:00\"/>\n" +
                "       <time day=\"15\" begins=\"23:30:00\" ends=\"23:45:00\"/>\n" +
                "       <interface address=\"192.168.100.254\"/>\n" +
                "       <interface address=\"192.168.101.254\"/>\n" +
                "       <interface address=\"192.168.102.254\"/>\n" +
                "       <interface address=\"192.168.103.254\"/>\n" +
                "       <interface address=\"192.168.104.254\"/>\n" +
                "       <interface address=\"192.168.105.254\"/>\n" +
                "       <interface address=\"192.168.106.254\"/>\n" +
                "       <interface address=\"192.168.107.254\"/>\n" +
                "   </outage>\n" +
                "\n" +
                "   <outage name=\"three\" type=\"specific\">\n" +
                "       <time begins=\"21-Feb-2005 05:30:00\" ends=\"21-Feb-2005 15:00:00\"/>\n" +
                "       <interface address=\"192.168.0.1\"/>\n" +
                "   </outage>\n" +
                "</outages>\n";

        m_manager = new PollOutagesConfigManager() {
            @Override
            public void update() throws IOException, MarshalException, ValidationException {}
        };

        m_manager.setConfigResource(new ByteArrayResource(xml.getBytes()));
        m_manager.afterPropertiesSet();
    }

    @Override
    protected void tearDown() throws Exception {
        MockLogAppender.assertNoWarningsOrGreater();
    }

    private long getTime(final String timeString) throws ParseException {
        final Date date = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(timeString);
        return date.getTime();

    }

    public void testIsTimeInOutageWeekly() throws Exception {

        assertTrue(m_manager.isTimeInOutage(getTime("21-FEB-2005 14:00:00"), "one"));
        assertFalse(m_manager.isTimeInOutage(getTime("21-FEB-2005 14:00:00"), "two"));
        assertTrue(m_manager.isTimeInOutage(getTime("21-FEB-2005 14:00:00"), "three"));

        assertTrue(m_manager.isTimeInOutage(getTime("15-FEB-2005 14:00:00"), "one"));
        assertFalse(m_manager.isTimeInOutage(getTime("15-FEB-2005 14:00:00"), "two"));
        assertFalse(m_manager.isTimeInOutage(getTime("15-FEB-2005 14:00:00"), "three"));

        assertFalse(m_manager.isTimeInOutage(getTime("15-FEB-2005 23:37:00"), "one"));
        assertTrue(m_manager.isTimeInOutage(getTime("15-FEB-2005 23:37:00"), "two"));
        assertFalse(m_manager.isTimeInOutage(getTime("15-FEB-2005 23:37:00"), "three"));

        assertFalse(m_manager.isTimeInOutage(getTime("21-FEB-2005 16:00:00"), "one"));
        assertFalse(m_manager.isTimeInOutage(getTime("21-FEB-2005 16:00:00"), "two"));
        assertFalse(m_manager.isTimeInOutage(getTime("21-FEB-2005 16:00:00"), "three"));


    }

    public void testLongListOfOutages() throws Exception {
        final String[] days   = { "sunday",   "sunday",   "tuesday",  "monday",   "friday",   "saturday", "wednesday" };
        final String[] begins = { "12:30:00", "12:45:00", "00:00:00", "00:00:00", "17:00:00", "00:00:00", "00:00:00" };
        final String[] ends   = { "12:45:00", "12:59:59", "08:00:00", "03:00:00", "23:59:59", "23:59:59", "08:00:00" };
        final StringBuffer xml = new StringBuffer();
        final SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");

        xml.append("<?xml version=\"1.0\"?>\n");
        xml.append("<outages>\n");
        for(int i = 0; i < 200; i++) {
            if (i % 5 == 0) {
                if (i % 3 == 0) {
                    xml.append("  <outage name=\""+i+"\" type=\"daily\">\n");
                    for(int j = 1; j < 4; j++) {
                        xml.append("    <time begins=\""+begins[j]+"\" ends=\""+ends[j]+"\"/>\n");
                    }
                    xml.append("    <interface address=\"192.168.0.1\"/>\n");
                    xml.append("    <interface address=\"192.168.0.36\"/>\n");
                    xml.append("    <interface address=\"192.168.0.38\"/>\n");
                    xml.append("  </outage>\n");
                } else if (i % 2 == 0) {
                    xml.append("  <outage name=\""+i+"\" type=\"weekly\">\n");
                    for(int j = 0; j < 7; j++) {
                        xml.append("    <time day=\""+days[j]+"\" begins=\""+begins[j]+"\" ends=\""+ends[j]+"\"/>\n");
                    }
                    xml.append("    <interface address=\"192.168.0.1\"/>\n");
                    xml.append("    <interface address=\"192.168.0.36\"/>\n");
                    xml.append("    <interface address=\"192.168.0.38\"/>\n");
                    xml.append("  </outage>\n");
                } else {
                    xml.append("  <outage name=\""+i+"\" type=\"monthly\">\n");
                    xml.append(
                        "    <time day=\"1\" begins=\"23:30:00\" ends=\"23:45:00\"/>\n" +
                        "    <time day=\"15\" begins=\"21:30:00\" ends=\"21:45:00\"/>\n" +
                        "    <time day=\"15\" begins=\"23:30:00\" ends=\"23:45:00\"/>\n" +
                        "    <interface address=\"192.168.100.254\"/>\n" +
                        "    <interface address=\"192.168.101.254\"/>\n" +
                        "    <interface address=\"192.168.102.254\"/>\n" +
                        "    <interface address=\"192.168.103.254\"/>\n" +
                        "    <interface address=\"192.168.104.254\"/>\n" +
                        "    <interface address=\"192.168.105.254\"/>\n" +
                        "    <interface address=\"192.168.106.254\"/>\n" +
                        "    <interface address=\"192.168.107.254\"/>\n"
                    );
                    xml.append("    <interface address=\"192.168.0.1\"/>\n");
                    xml.append("    <interface address=\"192.168.0.36\"/>\n");
                    xml.append("    <interface address=\"192.168.0.38\"/>\n");
                    xml.append("  </outage>\n");

                }
            } else {
                xml.append("  <outage name=\""+i+"\" type=\"specific\">\n");
                xml.append("    <time begins=\""+(i%10+11)+"-Feb-2005 05:30:00\" ends=\""+(i%10+11)+"-Feb-2005 15:00:00\"/>\n");
                final Random r = new Random(System.currentTimeMillis());
                final int cnt = (r.nextInt(100)%10)+1;

                for(int j = 1 ; j < cnt; j++) {
                    final Calendar b1 = new GregorianCalendar();
                    b1.setTime(fmt.parse("11-Mar-2012 06:00:00"));
                    b1.add(Calendar.WEEK_OF_YEAR, r.nextInt(50));

                    final Calendar b2 = new GregorianCalendar();
                    b2.setTimeInMillis(b1.getTimeInMillis());
                    b2.add(Calendar.HOUR, 4 + r.nextInt(j));

                    xml.append("    <time begins=\""+fmt.format(b1.getTime())+"\" ends=\""+fmt.format(b2.getTime())+"\"/>\n");
                }
                xml.append(
                "    <interface address=\"192.168.0."+(i%200+5)+"\"/>\n" +
                "    <interface address=\"192.168.0."+(i%200+10)+"\"/>\n" +
                "  </outage>\n"
                );
            }
        }
        xml.append("</outages>\n");
        System.out.println(xml.toString());

        final long start_time = System.currentTimeMillis();
        System.out.printf("start: %d\n", start_time);
        m_manager.setConfigResource(new ByteArrayResource(xml.toString().getBytes()));
        m_manager.afterPropertiesSet();
        final long end_time = System.currentTimeMillis();
        System.out.printf("  end: %d\n", end_time);
        System.out.printf(" diff: %d\n", end_time-start_time);

        try {
            // Marshal to a string first, then write the string to the file.
            // This
            // way the original configuration isn't lost if the XML from the
            // marshal is hosed.
            final StringWriter stringWriter = new StringWriter();
            final Marshaller m = new Marshaller();
            m.setWriter(stringWriter);
            m.setProperty("org.exolab.castor.indent", "true");
            m.marshal(m_manager.getConfig(), stringWriter);
            System.out.println(stringWriter.toString());

        } finally {
        }

    }

}
