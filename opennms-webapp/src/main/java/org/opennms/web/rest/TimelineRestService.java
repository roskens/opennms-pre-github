/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.web.rest;

import com.sun.jersey.spi.resource.PerRequest;
import java.awt.Color;
import java.awt.Font;
import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.core.criteria.restrictions.Restrictions;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.dao.api.OutageDao;
import org.opennms.netmgt.model.OnmsOutage;
import org.opennms.netmgt.model.OnmsOutageCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.opennms.netmgt.dao.api.NodeDao;
import org.opennms.netmgt.model.OnmsNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@PerRequest
@Scope("prototype")
@Path("timeline")
public class TimelineRestService extends OnmsRestService {

    private static final Logger LOG = LoggerFactory.getLogger(TimelineRestService.class);

    private static class TimescaleDescriptor {
        /**
         * The divisor to use for calculating the number of labels
         */
        private final int m_divisor;
        /**
         * The calendar field type to be used
         */
        private final int m_type;
        /**
         * The calendar field types to be zeroed
         */
        private final int[] m_typesToZero;
        /**
         * The increment for the calendar field
         */
        private final int m_increment;
        /**
         * The date format to be used
         */
        private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        private static final SimpleDateFormat SIMPLE_TIME_FORMAT = new SimpleDateFormat("HH:mm");

        /**
         * Red color
         */
        private static final Color ONMS_RED = Color.decode("#CC0000");
        /**
         * Green color
         */
        private static final Color ONMS_GREEN = Color.decode("#6F9F3A");
        /**
         * Green color
         */
        private static final Color ONMS_GRAY = Color.decode("#999999");

        /**
         * Constructor for an instance.
         *
         * @param divisor     the divisor to be used
         * @param type        the calendar field type
         * @param increment   the increment for the calendar field type
         * @param typesToZero the calendar field types to be zeroed
         */
        public TimescaleDescriptor(int divisor, int type, int increment, int... typesToZero) {
            this.m_divisor = divisor;
            this.m_type = type;
            this.m_increment = increment;
            this.m_typesToZero = typesToZero;
        }

        public int getDivisor() {
            return m_divisor;
        }

        public int getType() {
            return m_type;
        }

        public int[] getTypesToZero() {
            return m_typesToZero;
        }

        public int getIncrement() {
            return m_increment;
        }

        /**
         * Checks whether this instance matches the required number of labels.
         *
         * @param delta the delta used
         * @param num   the number of labels
         * @return true, if matches, false otherwise
         */
        public boolean match(long delta, int num) {
            return (delta / m_divisor < num);
        }

        /**
         * Draws the header an a given graphics context.
         *
         * @param graphics2D the graphics context to be used
         * @param delta      the delta
         * @param start      the start value
         * @param width      the width of the header
         */
        public void drawHeader(Graphics2D graphics2D, long delta, long start, int width) {
            Calendar calendar = GregorianCalendar.getInstance();

            calendar.setTimeInMillis(start * 1000);

            for (int typeToZero : getTypesToZero()) {
                calendar.set(typeToZero, calendar.getActualMinimum(typeToZero));
            }

            calendar.add(getType(), -getIncrement());

            graphics2D.setColor(Color.BLACK);

            while (calendar.getTimeInMillis() / 1000 - getDivisor() < (start + delta)) {
                int n = (int) ((calendar.getTimeInMillis() / 1000 - start) / (delta / width));
                graphics2D.setColor(ONMS_GRAY);
                graphics2D.drawLine(n, 16, n, 19);
                graphics2D.drawLine(n, 0, n, 4);
                String d;
                if (getDivisor() <= 3600 * 24) {
                    d = SIMPLE_TIME_FORMAT.format(calendar.getTime());
                } else {
                    d = SIMPLE_DATE_FORMAT.format(calendar.getTime());
                }
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawString(d, n - graphics2D.getFontMetrics().stringWidth(d) / 2, 15);
                calendar.add(getType(), getIncrement());
            }
        }

        /**
         * Draws vertical lines on a given graphics context.
         *
         * @param graphics2D the graphics context
         * @param delta      the delta
         * @param start      the start value
         * @param width      the width of the graphic
         */
        public void drawLine(Graphics2D graphics2D, long delta, long start, int width) {
            Calendar calendar = GregorianCalendar.getInstance();

            calendar.setTimeInMillis(start * 1000);

            for (int typeToZero : getTypesToZero()) {
                calendar.set(typeToZero, calendar.getActualMinimum(typeToZero));
            }

            calendar.add(getType(), -getIncrement());

            graphics2D.setColor(ONMS_GRAY);

            while (calendar.getTimeInMillis() / 1000 - getDivisor() < (start + delta)) {
                int n = (int) ((calendar.getTimeInMillis() / 1000 - start) / (delta / width));
                graphics2D.drawLine(n, 0, n, 19);
                calendar.add(getType(), getIncrement());
            }
        }

        /**
         * Draws an event on a given graphics context.
         *
         * @param graphics2D the graphics context
         * @param delta      the delta
         * @param start      the start value
         * @param width      the width of the graphic
         * @param onmsOutage the outage to be drawn
         * @return true, if no resolved yet
         */
        public boolean drawEvent(Graphics2D graphics2D, long delta, long start, int width, OnmsOutage onmsOutage) throws IOException {
            long p1 = onmsOutage.getServiceLostEvent().getEventCreateTime().getTime() / 1000;
            long p2 = start + delta;

            if (onmsOutage.getServiceRegainedEvent() != null) {
                p2 = onmsOutage.getServiceRegainedEvent().getEventCreateTime().getTime() / 1000;
            }

            graphics2D.setColor(ONMS_RED);
            int n1 = (int) ((p1 - start) / (delta / width));
            int n2 = (int) ((p2 - start) / (delta / width));
            graphics2D.fillRect(n1, 2, (n2 - n1 > 0 ? n2 - n1 : 1), 16);

            return onmsOutage.getServiceRegainedEvent() == null;
        }

        /**
         * Draws a solid green bar of a given length.
         *
         * @param graphics2D the graphics context
         * @param width      the width of the graphic
         */
        public void drawGreen(Graphics2D graphics2D, int width) {
            graphics2D.setColor(ONMS_GREEN);
            graphics2D.fillRect(0, 2, width, 16);
        }

        /**
         * Computes the number of labels to be used for the timeline
         *
         * @param graphics2D the graphics context
         * @param delta      the delta
         * @param width      the width of the timeline header
         * @return the number of labels
         */
        public static int computeNumberOfLabels(Graphics2D graphics2D, int delta, int width) {
            if (delta <= 3600 * 24) {
                return width / graphics2D.getFontMetrics().stringWidth("XX:XX");
            } else {
                return width / graphics2D.getFontMetrics().stringWidth("XXXX-XX-XX XX:XX");
            }
        }

        /**
         * Returns the HTML map entry for a given outage instance.
         *
         * @param graphics2D the graphics context
         * @param delta      the delta
         * @param start      the start value
         * @param width      the width of the graphic
         * @param onmsOutage the outage to be used
         * @return the HTML map entry
         */
        public String getMapEntry(Graphics2D graphics2D, long delta, long start, int width, OnmsOutage onmsOutage) {
            long p1 = onmsOutage.getServiceLostEvent().getEventCreateTime().getTime() / 1000;
            long p2 = start + delta;

            if (onmsOutage.getServiceRegainedEvent() != null) {
                p2 = onmsOutage.getServiceRegainedEvent().getEventCreateTime().getTime() / 1000;
            }

            graphics2D.setColor(ONMS_RED);
            int n1 = (int) ((p1 - start) / (delta / width));
            int n2 = (int) ((p2 - start) / (delta / width));
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append("<area shape=\"rect\" coords=\"")
              .append(n1)
              .append(",2,")
              .append(n2)
              .append(",18\" ")
              .append("href=\"/opennms/outage/detail.htm?id=")
              .append(onmsOutage.getId())
              .append("\" alt=\"Id ")
              .append(onmsOutage.getId())
              .append("\" title=\"")
              .append(onmsOutage.getServiceLostEvent().getEventCreateTime())
              .append("\">");
            return stringBuffer.toString();
        }
    }

    /**
     * The static list of timescales
     */
    public static final ArrayList<TimescaleDescriptor> TIMESCALE_DESCRIPTORS = new ArrayList<TimescaleDescriptor>();

    /**
     * Initialization of the timescale list
     */
    static {
        TIMESCALE_DESCRIPTORS.add(new TimescaleDescriptor(60 * 1, Calendar.MINUTE, 1, Calendar.SECOND));
        TIMESCALE_DESCRIPTORS.add(new TimescaleDescriptor(60 * 2, Calendar.MINUTE, 2, Calendar.SECOND));
        TIMESCALE_DESCRIPTORS.add(new TimescaleDescriptor(60 * 3, Calendar.MINUTE, 3, Calendar.SECOND));
        TIMESCALE_DESCRIPTORS.add(new TimescaleDescriptor(60 * 4, Calendar.MINUTE, 4, Calendar.SECOND));
        TIMESCALE_DESCRIPTORS.add(new TimescaleDescriptor(60 * 5, Calendar.MINUTE, 5, Calendar.SECOND));
        TIMESCALE_DESCRIPTORS.add(new TimescaleDescriptor(60 * 10, Calendar.MINUTE, 10, Calendar.SECOND));
        TIMESCALE_DESCRIPTORS.add(new TimescaleDescriptor(60 * 30, Calendar.MINUTE, 30, Calendar.SECOND));

        for (int i = 1; i <= 10; i++) {
            TIMESCALE_DESCRIPTORS.add(new TimescaleDescriptor(3600 * i, Calendar.HOUR, i, Calendar.SECOND, Calendar.MINUTE));
        }

        for (int i = 1; i <= 10; i++) {
            TIMESCALE_DESCRIPTORS.add(new TimescaleDescriptor(3600 * 24 * i, Calendar.DAY_OF_MONTH, i, Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR));
        }

        for (int i = 1; i <= 10; i++) {
            TIMESCALE_DESCRIPTORS.add(new TimescaleDescriptor(3600 * 24 * 30 * i, Calendar.MONTH, i, Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR, Calendar.DAY_OF_MONTH));
        }

        for (int i = 1; i <= 10; i++) {
            TIMESCALE_DESCRIPTORS.add(new TimescaleDescriptor(3600 * 24 * 360 * i, Calendar.YEAR, i, Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR, Calendar.DAY_OF_MONTH, Calendar.MONTH));
        }
    }

    @Autowired
    private OutageDao m_outageDao;

    @Autowired
    private NodeDao m_nodeDao;

    @Context
    UriInfo m_uriInfo;

    @Context
    SecurityContext m_securityContext;

    @Context
    ServletContext m_servletContext;

    @GET
    @Produces("image/png")
    @Transactional
    @Path("header/{start}/{end}/{width}")
    public Response header(@PathParam("start") final long start, @PathParam("end") final long end, @PathParam("width") final int width) throws IOException {
        int delta = (int) end - (int) start;

        BufferedImage bufferedImage = new BufferedImage(width, 20, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();

        graphics2D.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        graphics2D.setColor(Color.BLACK);

        int numLabels = TimescaleDescriptor.computeNumberOfLabels(graphics2D, delta, width);

        for (TimescaleDescriptor desc : TIMESCALE_DESCRIPTORS) {
            if (desc.match(delta, numLabels)) {
                desc.drawHeader(graphics2D, delta, start, width);
                break;
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageData = baos.toByteArray();

        return Response.ok(imageData).build();
    }

    @GET
    @Produces("text/javascript")
    @Transactional
    @Path("html/{nodeId}/{ipAddress}/{serviceName}/{start}/{end}/{width}")
    public Response html(@PathParam("nodeId") final int nodeId, @PathParam("ipAddress") final String ipAddress, @PathParam("serviceName") final String serviceName, @PathParam("start") final long start, @PathParam("end") final long end, @PathParam("width") final int width) throws IOException {
        int delta = (int) end - (int) start;

        OnmsOutageCollection onmsOutageCollection;

        readLock();
        try {
            final CriteriaBuilder builder = new CriteriaBuilder(OnmsOutage.class);
            builder.eq("node.id", nodeId);
            final Date d = new Date();
            d.setTime(start);

            builder.or(Restrictions.isNull("ifRegainedService"), Restrictions.gt("ifRegainedService", d));
            builder.eq("serviceType.name", serviceName);
            builder.eq("ipInterface.ipAddress", InetAddressUtils.addr(ipAddress));

            builder.alias("monitoredService", "monitoredService");
            builder.alias("monitoredService.ipInterface", "ipInterface");
            builder.alias("monitoredService.ipInterface.node", "node");
            builder.alias("monitoredService.serviceType", "serviceType");

            applyQueryFilters(m_uriInfo.getQueryParameters(), builder);

            builder.orderBy("id").desc();

            onmsOutageCollection = new OnmsOutageCollection(m_outageDao.findMatching(builder.toCriteria()));
        } finally {
            readUnlock();
        }

        BufferedImage bufferedImage = new BufferedImage(width, 20, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();

        graphics2D.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        graphics2D.setColor(Color.BLACK);

        int numLabels = TimescaleDescriptor.computeNumberOfLabels(graphics2D, delta, width);

        StringBuilder htmlBuffer = new StringBuilder();

        htmlBuffer.append("<img src=\"/opennms/rest/timeline/image/");
        htmlBuffer.append(nodeId);
        htmlBuffer.append("/");
        htmlBuffer.append(ipAddress);
        htmlBuffer.append("/");
        htmlBuffer.append(serviceName);
        htmlBuffer.append("/");
        htmlBuffer.append(start);
        htmlBuffer.append("/");
        htmlBuffer.append(end);
        htmlBuffer.append("/");
        htmlBuffer.append(width);
        htmlBuffer.append("\" usemap=\"#");
        htmlBuffer.append(nodeId);
        htmlBuffer.append("-");
        htmlBuffer.append(ipAddress);
        htmlBuffer.append("-");
        htmlBuffer.append(serviceName);
        htmlBuffer.append("\"><map name=\"");
        htmlBuffer.append(nodeId);
        htmlBuffer.append("-");
        htmlBuffer.append(ipAddress);
        htmlBuffer.append("-");
        htmlBuffer.append(serviceName);
        htmlBuffer.append("\">");

        for (TimescaleDescriptor desc : TIMESCALE_DESCRIPTORS) {
            if (desc.match(delta, numLabels)) {
                for (OnmsOutage onmsOutage : onmsOutageCollection) {
                    htmlBuffer.append(desc.getMapEntry(graphics2D, delta, start, width, onmsOutage));
                }
                break;
            }
        }

        htmlBuffer.append("</map>");

        return Response.ok("document.write('" + htmlBuffer.toString() + "');").build();
    }

    @GET
    @Produces("image/png")
    @Transactional
    @Path("image/{nodeId}/{ipAddress}/{serviceName}/{start}/{end}/{width}")
    public Response image(@PathParam("nodeId") final int nodeId, @PathParam("ipAddress") final String ipAddress, @PathParam("serviceName") final String serviceName, @PathParam("start") final long start, @PathParam("end") final long end, @PathParam("width") final int width) throws IOException {
        int delta = (int) end - (int) start;

        OnmsOutageCollection onmsOutageCollection;

        readLock();
        try {
            final CriteriaBuilder builder = new CriteriaBuilder(OnmsOutage.class);
            builder.eq("node.id", nodeId);
            final Date d = new Date();
            d.setTime(start);

            builder.or(Restrictions.isNull("ifRegainedService"), Restrictions.gt("ifRegainedService", d));
            builder.eq("serviceType.name", serviceName);
            builder.eq("ipInterface.ipAddress", InetAddressUtils.addr(ipAddress));

            builder.alias("monitoredService", "monitoredService");
            builder.alias("monitoredService.ipInterface", "ipInterface");
            builder.alias("monitoredService.ipInterface.node", "node");
            builder.alias("monitoredService.serviceType", "serviceType");

            applyQueryFilters(m_uriInfo.getQueryParameters(), builder);

            builder.orderBy("id").desc();

            onmsOutageCollection = new OnmsOutageCollection(m_outageDao.findMatching(builder.toCriteria()));
        } finally {
            readUnlock();
        }

        BufferedImage bufferedImage = new BufferedImage(width, 20, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();

        graphics2D.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        graphics2D.setColor(Color.BLACK);

        int numLabels = TimescaleDescriptor.computeNumberOfLabels(graphics2D, delta, width);

        for (TimescaleDescriptor desc : TIMESCALE_DESCRIPTORS) {
            if (desc.match(delta, numLabels)) {
                desc.drawGreen(graphics2D, width);

                for (OnmsOutage onmsOutage : onmsOutageCollection) {
                    desc.drawEvent(graphics2D, delta, start, width, onmsOutage);
                }

                desc.drawLine(graphics2D, delta, start, width);

                break;
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageData = baos.toByteArray();

        return Response.ok(imageData).build();
    }

    @GET
    @Produces("image/png")
    @Transactional
    @Path("empty/{start}/{end}/{width}")
    public Response empty(@PathParam("start") final long start, @PathParam("end") final long end, @PathParam("width") final int width) throws IOException {
        int delta = (int) end - (int) start;

        BufferedImage bufferedImage = new BufferedImage(width, 20, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();

        graphics2D.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        graphics2D.setColor(Color.BLACK);

        int numLabels = TimescaleDescriptor.computeNumberOfLabels(graphics2D, delta, width);

        for (TimescaleDescriptor desc : TIMESCALE_DESCRIPTORS) {
            if (desc.match(delta, numLabels)) {
                desc.drawLine(graphics2D, delta, start, width);

                break;
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageData = baos.toByteArray();

        return Response.ok(imageData).build();
    }

    @GET
    @Produces("image/png")
    @Transactional
    @Path("dashboard/{start}/{end}/{width}/{height}")
    public Response dashboard(@PathParam("start") final long start, @PathParam("end") final long end, @PathParam("width") final int width, @PathParam("height") final int height) throws IOException {
        LOG.debug("dashboard(start='{}', end='{}', width='{}', height='{}')", start, end, width, height);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();

        graphics2D.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        graphics2D.setColor(Color.decode("#999999"));
        graphics2D.setBackground(Color.BLACK);
        graphics2D.clearRect(0, 0, width, height);

        int delta = (int) end - (int) start;
        int total_pixels = width * height;
        LOG.debug("dashboard: total_pixels={}", total_pixels);

        OnmsOutageCollection onmsOutageCollection;

        readLock();
        try {
            List<OnmsNode> coll = m_nodeDao.findAll();
            int num_nodes = coll.size();
            LOG.debug("dashboard: found {} active nodes in the database.", num_nodes);

            int pixels = calc_square_size(width, height, num_nodes);

            LOG.debug("dashboard: can fill rectangle {}x{} with {} squares of size {}x{}",
              width, height, num_nodes, pixels, pixels
            );

            int i = 0, j = 0;
            int max_x = width / pixels;
            for (OnmsNode node : coll) {
                graphics2D.setColor(node.isDown() ? Color.RED : Color.DARK_GRAY);

                graphics2D.fill3DRect(pixels * i, pixels * j, pixels, pixels, true);
                LOG.debug("dashboard: fillRect(x='{}', y='{}', width='{}', height='{}')",
                  pixels * i, pixels * j, pixels, pixels
                );
                if (i < max_x) {
                    i++;
                }
                if (i == max_x) {
                    i = 0;
                    j++;
                }
            }


        } finally {
            readUnlock();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageData = baos.toByteArray();
        return Response.ok(imageData).build();
    }

    private int calc_square_size(final int x, final int y, final int n) {
        double px = Math.ceil(Math.sqrt(n * x / y));
        double sx, sy;
        if (Math.floor(px * y / x) * px < n) //does not fit, y/(x/px)=px*y/x
        {
            sx = y / Math.ceil(px * y / x);
        } else {
            sx = x / px;
        }
        double py = Math.ceil(Math.sqrt(n * y / x));
        if (Math.floor(py * x / y) * py < n) //does not fit
        {
            sy = x / Math.ceil(x * py / y);
        } else {
            sy = y / py;
        }
        return (int) Math.max(sx, sy);
    }
}
