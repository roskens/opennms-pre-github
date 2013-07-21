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

/**
 * The Class MockServiceDaemon.
 */
public class MockServiceDaemon implements MockServiceDaemonMBean {

    /** The start called. */
    private boolean startCalled = false;

    /** The status str. */
    private String statusStr = "UNDEFINED";

    /** The name. */
    private String name;

    /**
     * Instantiates a new mock service daemon.
     *
     * @param name
     *            the name
     */
    public MockServiceDaemon(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.model.ServiceDaemon#getStatusText()
     */
    @Override
    public String getStatusText() {
        // TODO Auto-generated method stub
        return statusStr;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.fiber.PausableFiber#pause()
     */
    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.core.fiber.PausableFiber#resume()
     */
    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.opennms.core.fiber.Fiber#getName()
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return name;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.fiber.Fiber#getStatus()
     */
    @Override
    public int getStatus() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.fiber.Fiber#start()
     */
    @Override
    public void start() {
        // TODO Auto-generated method stub
        startCalled = true;
        statusStr = "Started";
    }

    /**
     * Gets the start called.
     *
     * @return the start called
     */
    public boolean getStartCalled() {
        return startCalled;
    }

    /* (non-Javadoc)
     * @see org.opennms.core.fiber.Fiber#stop()
     */
    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

}
