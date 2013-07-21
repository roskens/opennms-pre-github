/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.protocols;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.core.utils.TimeoutTracker;
import org.opennms.netmgt.protocols.ssh.Ssh;

/**
 * The Class SshTest.
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:ranger@opennms.org">Ben Reed</a>
 */
public class SshTest extends TestCase {

    /** The good. */
    InetAddress good;

    /** The Constant bad. */
    private static final InetAddress bad = InetAddressUtils.UNPINGABLE_ADDRESS;

    /** The Constant GOOD_HOST. */
    private static final String GOOD_HOST = "localhost";

    /** The Constant PORT. */
    private static final int PORT = 22;

    /** The Constant TIMEOUT. */
    private static final int TIMEOUT = 2000;

    /** The tt. */
    private TimeoutTracker tt;

    /** The ssh. */
    Ssh ssh;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("retries", "0");
        parameters.put("port", "22");
        parameters.put("timeout", Integer.toString(TIMEOUT));

        tt = new TimeoutTracker(parameters, 0, TIMEOUT);
        ssh = new Ssh();
        ssh.setPort(PORT);
        ssh.setTimeout(TIMEOUT);

        good = InetAddressUtils.addr(GOOD_HOST);
    }

    /**
     * Test ssh good.
     *
     * @throws Exception
     *             the exception
     */
    public void testSshGood() throws Exception {
        ssh.setAddress(good);
        assertTrue(ssh.poll(tt).isAvailable());
    }

    /**
     * Test ssh bad.
     *
     * @throws Exception
     *             the exception
     */
    public void testSshBad() throws Exception {
        Date start = new Date();
        ssh.setAddress(bad);
        assertFalse(ssh.poll(tt).isAvailable());
        Date end = new Date();

        // give it 2.5 seconds to time out
        assertTrue(end.getTime() - start.getTime() < 2500);
    }

}
