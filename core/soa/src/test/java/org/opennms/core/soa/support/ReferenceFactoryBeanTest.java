/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.core.soa.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.soa.ServiceRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ReferenceFactoryBeanTest.
 *
 * @author brozow
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ReferenceFactoryBeanTest implements InitializingBean {

    /** The service registry. */
    @Autowired
    ServiceRegistry serviceRegistry;

    /** The hello. */
    @Autowired
    Hello hello;

    /** The goodbye. */
    @Autowired
    Goodbye goodbye;

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        assertNotNull(serviceRegistry);
        assertNotNull(hello);
        assertNotNull(goodbye);
    }

    /**
     * Test find reference.
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    @DirtiesContext
    public void testFindReference() throws IOException {

        MyProvider provider = new MyProvider();

        serviceRegistry.register(provider, Hello.class, Goodbye.class);

        assertEquals(0, provider.helloSaid());

        hello.sayHello();

        assertEquals(1, provider.helloSaid());

        assertEquals(0, provider.goodbyeSaid());

        goodbye.sayGoodbye();

        assertEquals(1, provider.goodbyeSaid());

    }

}
