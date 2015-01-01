/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2002-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.vmmgr;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringBoard implements SpringBoardMBean {

    private Path m_contextDir;
    private FileSystemXmlApplicationContext m_context;

    /**
     * <p>Getter for the field <code>contextDir</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getContextDir() {
        return (m_contextDir == null ? null : m_contextDir.toString());
    }

    /** {@inheritDoc} */
    @Override
    public void setContextDir(String contextDir) {
        m_contextDir = Paths.get(contextDir);
    }

    /**
     * <p>start</p>
     */
    @Override
    public void start() {
        String appContext = System.getProperty("opennms.appcontext", "opennms-appContext.xml");
        Path contextFile = m_contextDir.resolve(appContext);
        System.err.println(contextFile);
        m_context = new FileSystemXmlApplicationContext(contextFile.toString());
    }

    /**
     * <p>status</p>
     *
     * @return a {@link java.util.List} object.
     */
    @Override
    public List<String> status() {
        return Collections.singletonList(m_context.toString());
    }

    /**
     * <p>stop</p>
     */
    @Override
    public void stop() {
        m_context.close();
    }


}
