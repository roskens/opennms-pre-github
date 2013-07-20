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

package org.opennms.core.test.http.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * JUnitHttpServer class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface JUnitHttpServer {

    /**
     * the directory from which to serve test files *.
     *
     * @return the string
     */
    String resource() default "target/test-classes";

    /**
     * the port to listen on *.
     *
     * @return the int
     */
    int port() default 9162;

    /**
     * the list of virtual hosts to respond to, defaults to "localhost" *.
     *
     * @return the string[]
     */
    String[] vhosts() default { "localhost", "127.0.0.1", "::1", "[0000:0000:0000:0000:0000:0000:0000:0001]" };

    /**
     * whether or not to use HTTPS (defaults to HTTP) *.
     *
     * @return true, if successful
     */
    boolean https() default false;

    /**
     * whether or not to use basic auth *.
     *
     * @return true, if successful
     */
    boolean basicAuth() default false;

    /**
     * the basic auth property file (defaults to
     * target/test-classes/realm.properties)
     *
     * @return the string
     */
    String basicAuthFile() default "target/test-classes/realm.properties";

    /**
     * the location of the keystore if using HTTPS (defaults to
     * target/test-classes/JUnitHttpServer.keystore)
     *
     * @return the string
     */
    String keystore() default "target/test-classes/JUnitHttpServer.keystore";

    /**
     * the keystore password *.
     *
     * @return the string
     */
    String keystorePassword() default "opennms";

    /**
     * the key password *.
     *
     * @return the string
     */
    String keyPassword() default "opennms";

    /**
     * zero or more webapps to include, with contexts *.
     *
     * @return the webapp[]
     */
    Webapp[] webapps() default {};

}
