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

package org.opennms.netmgt.rrd.newts;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.jrobin.graph.RrdGraph;
import org.opennms.netmgt.rrd.RrdException;
import org.opennms.netmgt.rrd.RrdGraphDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Container for details from a JRobin RRD graph.  Stores the same details
 * as RrdGraphDetails, in addition to the JRobin RrdGraph object itself and
 * the graph command String used to generate the graph.  We keep the graph
 * command string around so we can generate a detailed error if
 * getInputStream() is called, but no graph was produced.
 *
 * Direct copy of JRobinRrdGraphDetails.
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @version $Id: $
 */
public class NewtsRrdGraphDetails implements RrdGraphDetails {
    private static final Logger LOG = LoggerFactory.getLogger(NewtsRrdGraphDetails.class);

    private final RrdGraph m_rrdGraph;
    private final String m_graphCommand;

    /**
     * <p>Constructor for NewtsRrdGraphDetails.</p>
     *
     * @param rrdGraph a {@link org.jrobin.graph.RrdGraph} object.
     * @param graphCommand a {@link java.lang.String} object.
     */
    public NewtsRrdGraphDetails(RrdGraph rrdGraph, String graphCommand) {
        m_rrdGraph = rrdGraph;
        m_graphCommand = graphCommand;
    }

    /**
     * <p>getRrdGraph</p>
     *
     * @return a {@link org.jrobin.graph.RrdGraph} object.
     */
    public RrdGraph getRrdGraph() {
        return m_rrdGraph;
    }

    /**
     * <p>getGraphCommand</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getGraphCommand() {
        return m_graphCommand;
    }

    /**
     * <p>getInputStream</p>
     *
     * @return a {@link java.io.InputStream} object.
     * @throws org.opennms.netmgt.rrd.RrdException if any.
     */
    @Override
    public InputStream getInputStream() throws RrdException {
        assertGraphProduced();

        return new ByteArrayInputStream(m_rrdGraph.getRrdGraphInfo().getBytes());
    }

    /**
     * <p>getPrintLines</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    @Override
    public String[] getPrintLines() {
        return m_rrdGraph.getRrdGraphInfo().getPrintLines();
    }

    /**
     * <p>getHeight</p>
     *
     * @return a int.
     * @throws org.opennms.netmgt.rrd.RrdException if any.
     */
    @Override
    public int getHeight() throws RrdException {
        assertGraphProduced();

        return m_rrdGraph.getRrdGraphInfo().getHeight();
    }

    /**
     * <p>getWidth</p>
     *
     * @return a int.
     * @throws org.opennms.netmgt.rrd.RrdException if any.
     */
    @Override
    public int getWidth() throws RrdException {
        assertGraphProduced();

        return m_rrdGraph.getRrdGraphInfo().getWidth();
    }

    private void assertGraphProduced() throws RrdException {
        if (m_rrdGraph.getRrdGraphInfo().getBytes() == null) {
            String message = "no graph was produced by JRobin for command '" + getGraphCommand() + "'.  Does the command have any drawing commands (e.g.: LINE1, LINE2, LINE3, AREA, STACK, GPRINT)?";
            LOG.error(message);
            throw new RrdException(message);
        }
    }
}
