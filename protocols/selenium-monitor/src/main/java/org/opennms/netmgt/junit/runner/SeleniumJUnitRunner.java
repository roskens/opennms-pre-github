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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

/**
 * The Class SeleniumJUnitRunner.
 */
public class SeleniumJUnitRunner extends Suite {

    /**
     * The Interface BaseUrl.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface BaseUrl {

        /**
         * Url.
         *
         * @return the string
         */
        String url();
    }

    /**
     * The Interface TimeoutInSeconds.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface TimeoutInSeconds {

        /**
         * Timeout.
         *
         * @return the int
         */
        int timeout();
    }

    /** The m_runners. */
    private final List<Runner> m_runners = new ArrayList<Runner>();

    /**
     * Instantiates a new selenium j unit runner.
     *
     * @param testClass
     *            the test class
     * @throws InitializationError
     *             the initialization error
     */
    public SeleniumJUnitRunner(Class<?> testClass) throws InitializationError {
        super(testClass, Collections.<Runner> emptyList());
        m_runners.add(new TestClassRunnerForSelenium(getTestClass().getJavaClass(), getBaseUrlAnnotation(testClass),
                                                     getTimeoutAnnotation(testClass)));
    }

    /**
     * Gets the timeout annotation.
     *
     * @param testClass
     *            the test class
     * @return the timeout annotation
     */
    private int getTimeoutAnnotation(Class<?> testClass) {
        SeleniumJUnitRunner.TimeoutInSeconds timeout = testClass.getAnnotation(SeleniumJUnitRunner.TimeoutInSeconds.class);
        if (timeout == null) {
            return 3;
        } else {
            return timeout.timeout();
        }
    }

    /**
     * Gets the base url annotation.
     *
     * @param klass
     *            the klass
     * @return the base url annotation
     */
    private String getBaseUrlAnnotation(Class<?> klass) {
        SeleniumJUnitRunner.BaseUrl baseUrl = klass.getAnnotation(SeleniumJUnitRunner.BaseUrl.class);
        if (baseUrl == null) {
            return "";
        } else {
            return baseUrl.url();
        }
    }

    /* (non-Javadoc)
     * @see org.junit.runners.Suite#getChildren()
     */
    @Override
    protected List<Runner> getChildren() {
        return m_runners;
    }
}
