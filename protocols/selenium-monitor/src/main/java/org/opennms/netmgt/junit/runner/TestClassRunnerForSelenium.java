/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.junit.runner;

import java.util.List;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * The Class TestClassRunnerForSelenium.
 */
public class TestClassRunnerForSelenium extends BlockJUnit4ClassRunner {

    /** The m_timeout. */
    private int m_timeout;

    /** The m_base url. */
    private String m_baseUrl;

    /**
     * Instantiates a new test class runner for selenium.
     *
     * @param type
     *            the type
     * @param baseUrl
     *            the base url
     * @param timeoutInSeconds
     *            the timeout in seconds
     * @throws InitializationError
     *             the initialization error
     */
    TestClassRunnerForSelenium(Class<?> type, String baseUrl, int timeoutInSeconds) throws InitializationError {
        super(type);
        setBaseUrl(baseUrl);
        setTimeout(timeoutInSeconds);
    }

    /* (non-Javadoc)
     * @see org.junit.runners.BlockJUnit4ClassRunner#createTest()
     */
    @Override
    public Object createTest() throws Exception {
        return getTestClass().getOnlyConstructor().newInstance(getBaseUrl(), getTimeout());
    }

    /* (non-Javadoc)
     * @see org.junit.runners.BlockJUnit4ClassRunner#validateConstructor(java.util.List)
     */
    @Override
    protected void validateConstructor(List<Throwable> errors) {
        validateOnlyOneConstructor(errors);
    }

    /**
     * Gets the timeout.
     *
     * @return the timeout
     */
    public int getTimeout() {
        return m_timeout;
    }

    /**
     * Sets the timeout.
     *
     * @param timeout
     *            the new timeout
     */
    public void setTimeout(int timeout) {
        m_timeout = timeout;
    }

    /**
     * Gets the base url.
     *
     * @return the base url
     */
    public String getBaseUrl() {
        return m_baseUrl;
    }

    /**
     * Sets the base url.
     *
     * @param baseUrl
     *            the new base url
     */
    public void setBaseUrl(String baseUrl) {
        m_baseUrl = baseUrl;
    }

}
