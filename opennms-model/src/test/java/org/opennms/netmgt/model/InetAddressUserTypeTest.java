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
package org.opennms.netmgt.model;

import static org.junit.Assert.assertNull;

import java.sql.ResultSet;

import org.easymock.EasyMock;
import org.junit.Test;

/**
 * The Class InetAddressUserTypeTest.
 */
public class InetAddressUserTypeTest {

    /**
     * Test inet address type.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void testInetAddressType() throws Exception {
        ResultSet rs = EasyMock.createMock(ResultSet.class);
        EasyMock.expect(rs.getString("ipAddr")).andReturn(null);
        EasyMock.replay(rs);
        final InetAddressUserType userType = new InetAddressUserType();
        final Object result = userType.nullSafeGet(rs, new String[] { "ipAddr" }, null);
        EasyMock.verify(rs);
        assertNull(result);
    }
}
