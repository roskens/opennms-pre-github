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

package org.opennms.netmgt.provision;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.OpenNMSJUnit4ClassRunner;
import org.opennms.core.test.db.annotations.JUnitTemporaryDatabase;
import org.opennms.core.utils.BeanUtils;
import org.opennms.test.JUnitConfigurationEnvironment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Test the user stories/use cases associated with the Link Adapter.
 *
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 */
@RunWith(OpenNMSJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-dao.xml",
        "classpath:/META-INF/opennms/applicationContext-minimal-conf.xml" })
@JUnitConfigurationEnvironment
@JUnitTemporaryDatabase
public class SimplerProvisioningAdapterTest implements InitializingBean {

    /** The name. */
    public static String NAME = "MyProvisioningAdapter";

    /** The add latch. */
    CountDownLatch addLatch = new CountDownLatch(1);

    /** The delete latch. */
    CountDownLatch deleteLatch = new CountDownLatch(1);

    /** The update latch. */
    CountDownLatch updateLatch = new CountDownLatch(1);

    /** The config change latch. */
    CountDownLatch configChangeLatch = new CountDownLatch(1);

    // From applicationContext-dao.xml
    /** The m_tx template. */
    @Autowired
    private TransactionTemplate m_txTemplate;

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        BeanUtils.assertAutowiring(this);
    }

    /**
     * The Class MyProvisioningAdapter.
     */
    class MyProvisioningAdapter extends SimplerQueuedProvisioningAdapter {

        /**
         * Instantiates a new my provisioning adapter.
         */
        public MyProvisioningAdapter() {
            super(NAME);
            setTemplate(m_txTemplate);
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.SimplerQueuedProvisioningAdapter#doAddNode(int)
         */
        @Override
        public void doAddNode(int nodeid) {
            addLatch.countDown();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.SimplerQueuedProvisioningAdapter#doDeleteNode(int)
         */
        @Override
        public void doDeleteNode(int nodeid) {
            deleteLatch.countDown();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.SimplerQueuedProvisioningAdapter#doNotifyConfigChange(int)
         */
        @Override
        public void doNotifyConfigChange(int nodeid) {
            configChangeLatch.countDown();
        }

        /* (non-Javadoc)
         * @see org.opennms.netmgt.provision.SimplerQueuedProvisioningAdapter#doUpdateNode(int)
         */
        @Override
        public void doUpdateNode(int nodeid) {
            updateLatch.countDown();
        }

    }

    /** The m_adapter. */
    private MyProvisioningAdapter m_adapter;

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        m_adapter = new MyProvisioningAdapter();
        m_adapter.init();
    }

    /**
     * Dwo get name.
     */
    @Test
    public void dwoGetName() {
        assertEquals(NAME, m_adapter.getName());
    }

    /**
     * Dwo add node calls do add node.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void dwoAddNodeCallsDoAddNode() throws InterruptedException {

        m_adapter.addNode(1);
        assertTrue(addLatch.await(2, TimeUnit.SECONDS));

    }

    /**
     * Dwo delete node calls do delete node.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void dwoDeleteNodeCallsDoDeleteNode() throws InterruptedException {

        m_adapter.deleteNode(1);
        assertTrue(deleteLatch.await(2, TimeUnit.SECONDS));

    }

    /**
     * Dwo update node calls do update node.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void dwoUpdateNodeCallsDoUpdateNode() throws InterruptedException {

        m_adapter.updateNode(1);
        assertTrue(updateLatch.await(2, TimeUnit.SECONDS));

    }

    /**
     * Dwo notify config change calls do notify config change.
     *
     * @throws InterruptedException
     *             the interrupted exception
     */
    @Test
    public void dwoNotifyConfigChangeCallsDoNotifyConfigChange() throws InterruptedException {

        m_adapter.nodeConfigChanged(1);
        assertTrue(configChangeLatch.await(2, TimeUnit.SECONDS));

    }
}
