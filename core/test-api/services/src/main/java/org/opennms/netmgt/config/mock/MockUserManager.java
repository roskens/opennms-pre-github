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

package org.opennms.netmgt.config.mock;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.config.GroupManager;
import org.opennms.netmgt.config.UserManager;

/**
 * The Class MockUserManager.
 */
public class MockUserManager extends UserManager {

    /** The m_xml string. */
    String m_xmlString;

    /** The update needed. */
    boolean updateNeeded = true;

    /** The m_last modified. */
    private long m_lastModified;

    /** The m_file size. */
    private long m_fileSize;

    /**
     * Instantiates a new mock user manager.
     *
     * @param groupManager
     *            the group manager
     * @param xmlString
     *            the xml string
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public MockUserManager(GroupManager groupManager, String xmlString) throws MarshalException, ValidationException {
        super(groupManager);
        m_xmlString = xmlString;
        parseXML();
    }

    /**
     * Parses the xml.
     *
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    private void parseXML() throws MarshalException, ValidationException {
        InputStream in = new ByteArrayInputStream(m_xmlString.getBytes());
        parseXML(in);
        updateNeeded = false;
        m_lastModified = System.currentTimeMillis();
        m_fileSize = m_xmlString.getBytes().length;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.UserManager#saveXML(java.lang.String)
     */
    @Override
    protected void saveXML(String writerString) throws IOException {
        m_xmlString = writerString;
        updateNeeded = true;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.UserManager#doUpdate()
     */
    @Override
    protected void doUpdate() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        if (updateNeeded) {
            parseXML();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.UserManager#isUpdateNeeded()
     */
    @Override
    public boolean isUpdateNeeded() {
        return updateNeeded;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.UserManager#getLastModified()
     */
    @Override
    public long getLastModified() {
        return m_lastModified;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.UserManager#getFileSize()
     */
    @Override
    public long getFileSize() {
        return m_fileSize;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.config.UserManager#reload()
     */
    @Override
    public void reload() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        parseXML();
    }

}
