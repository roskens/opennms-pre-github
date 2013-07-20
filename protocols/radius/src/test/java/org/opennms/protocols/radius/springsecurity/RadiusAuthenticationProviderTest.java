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

package org.opennms.protocols.radius.springsecurity;

import java.io.IOException;

import net.jradius.client.auth.CHAPAuthenticator;
import net.jradius.client.auth.EAPMD5Authenticator;
import net.jradius.client.auth.EAPMSCHAPv2Authenticator;
import net.jradius.client.auth.MSCHAPv1Authenticator;
import net.jradius.client.auth.MSCHAPv2Authenticator;
import net.jradius.client.auth.PAPAuthenticator;
import net.jradius.client.auth.RadiusAuthenticator;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * The Class RadiusAuthenticationProviderTest.
 */
public class RadiusAuthenticationProviderTest {

    /** The m_radius server. */
    private String m_radiusServer = "127.0.0.1";

    /** The m_shared secret. */
    private String m_sharedSecret = "testing123";

    /** The m_principal. */
    private Object m_principal = "test";

    /** The m_username. */
    private final String m_username = "test";

    /** The m_credentials. */
    private Object m_credentials = "opennms";

    /**
     * Test retrieve user default.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @Ignore("Need to have a RADIUS server running on localhost")
    public void testRetrieveUserDefault() throws IOException {
        RadiusAuthenticationProvider provider = new RadiusAuthenticationProvider(m_radiusServer, m_sharedSecret);
        RadiusAuthenticator authTypeClass = null;

        provider.setAuthTypeClass(authTypeClass);
        provider.setRolesAttribute("Unknown-VSAttribute(5813:1)");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(m_principal, m_credentials);
        provider.retrieveUser(m_username, token);
    }

    /**
     * Test retrieve user pap.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @Ignore("Need to have a RADIUS server running on localhost")
    public void testRetrieveUserPap() throws IOException {
        RadiusAuthenticationProvider provider = new RadiusAuthenticationProvider(m_radiusServer, m_sharedSecret);
        RadiusAuthenticator authTypeClass = new PAPAuthenticator();

        provider.setAuthTypeClass(authTypeClass);
        provider.setRolesAttribute("Unknown-VSAttribute(5813:1)");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(m_principal, m_credentials);
        provider.retrieveUser(m_username, token);
    }

    /**
     * Test retrieve user chap.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @Ignore("Need to have a RADIUS server running on localhost")
    public void testRetrieveUserChap() throws IOException {
        RadiusAuthenticationProvider provider = new RadiusAuthenticationProvider(m_radiusServer, m_sharedSecret);
        RadiusAuthenticator authTypeClass = new CHAPAuthenticator();

        provider.setAuthTypeClass(authTypeClass);
        provider.setRolesAttribute("Unknown-VSAttribute(5813:1)");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(m_principal, m_credentials);
        provider.retrieveUser(m_username, token);
    }

    /**
     * This test will use null as the authenticator value, which will default to
     * using
     * PAP authentication.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @Ignore("Need to have a RADIUS server running on localhost")
    public void testRetrieveUserMultipleTimesDefault() throws IOException {
        doTestRetrieveUserMultipleTimes(null);
    }

    /**
     * Test retrieve user multiple times pap.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @Ignore("Need to have a RADIUS server running on localhost")
    public void testRetrieveUserMultipleTimesPAP() throws IOException {
        doTestRetrieveUserMultipleTimes(new PAPAuthenticator());
    }

    /**
     * Test retrieve user multiple times chap.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @Ignore("Need to have a RADIUS server running on localhost")
    public void testRetrieveUserMultipleTimesCHAP() throws IOException {
        doTestRetrieveUserMultipleTimes(new CHAPAuthenticator());
    }

    /**
     * Test retrieve user multiple times eapm d5.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @Ignore("Need to have a RADIUS server running on localhost")
    public void testRetrieveUserMultipleTimesEAPMD5() throws IOException {
        doTestRetrieveUserMultipleTimes(new EAPMD5Authenticator());
    }

    /**
     * Test retrieve user multiple times eapmscha pv2.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @Ignore("Need to have a RADIUS server running on localhost")
    public void testRetrieveUserMultipleTimesEAPMSCHAPv2() throws IOException {
        doTestRetrieveUserMultipleTimes(new EAPMSCHAPv2Authenticator());
    }

    /**
     * Test retrieve user multiple times mscha pv1.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @Ignore("Need to have a RADIUS server running on localhost")
    public void testRetrieveUserMultipleTimesMSCHAPv1() throws IOException {
        doTestRetrieveUserMultipleTimes(new MSCHAPv1Authenticator());
    }

    /**
     * Test retrieve user multiple times mscha pv2.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @Ignore("Need to have a RADIUS server running on localhost")
    public void testRetrieveUserMultipleTimesMSCHAPv2() throws IOException {
        doTestRetrieveUserMultipleTimes(new MSCHAPv2Authenticator());
    }

    /**
     * Do test retrieve user multiple times.
     *
     * @param authenticator
     *            the authenticator
     */
    public void doTestRetrieveUserMultipleTimes(RadiusAuthenticator authenticator) {
        RadiusAuthenticationProvider provider = new RadiusAuthenticationProvider(m_radiusServer, m_sharedSecret);
        RadiusAuthenticator authTypeClass = authenticator;

        provider.setAuthTypeClass(authTypeClass);
        provider.setRolesAttribute("Unknown-VSAttribute(5813:1)");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(m_principal, m_credentials);
        provider.retrieveUser(m_username, token);
        provider.retrieveUser(m_username, token);
        provider.retrieveUser(m_username, token);
        provider.retrieveUser(m_username, token);
        provider.retrieveUser(m_username, token);
        provider.retrieveUser(m_username, token);
    }
}
