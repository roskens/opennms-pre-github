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
 *
 * From the original copyright headers:
 *
 * Copyright (c) 2009+ desmax74
 * Copyright (c) 2009+ The OpenNMS Group, Inc.
 *
 * This program was developed and is maintained by Rocco RIONERO
 * ("the author") and is subject to dual-copyright according to
 * the terms set in "The OpenNMS Project Contributor Agreement".
 *
 * The author can be contacted at the following email address:
 *
 *     Massimiliano Dess&igrave;
 *     desmax74@yahoo.it
 *******************************************************************************/

package org.opennms.acl.conf;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Config.
 */
public class Config {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    /**
     * Gets the db driver.
     *
     * @return the db driver
     */
    public String getDbDriver() {
        return dbDriver;
    }

    /**
     * Gets the db user.
     *
     * @return the db user
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * Gets the db pass.
     *
     * @return the db pass
     */
    public String getDbPass() {
        return dbPass;
    }

    /**
     * Gets the db url.
     *
     * @return the db url
     */
    public String getDbUrl() {
        return dbUrl;
    }

    /**
     * Instantiates a new config.
     */
    public Config() {
        Properties props = new Properties();
        try {
            props.load(Config.class.getResourceAsStream("/org/opennms/acl/conf/config.properties"));
        } catch (final IOException e) {
            LOG.warn("Unable to get configuration resource.", e);
        }

        dbDriver = props.getProperty("jdbc.driver");
        dbUrl = props.getProperty("jdbc.url");
        dbUser = props.getProperty("jdbc.username");
        dbPass = props.getProperty("jdbc.password");
    }

    /** The db driver. */
    private String dbDriver;

    /** The db user. */
    private String dbUser;

    /** The db pass. */
    private String dbPass;

    /** The db url. */
    private String dbUrl;
}
