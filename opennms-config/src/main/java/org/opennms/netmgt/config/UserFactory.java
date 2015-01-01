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

package org.opennms.netmgt.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.ConfigFileConstants;

/**
 * <p>UserFactory class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class UserFactory extends UserManager {
    /**
     * The static singleton instance of the UserFactory
     */
    private static UserManager instance;

    /**
     * Boolean indicating if the init() method has been called
     */
    private static boolean initialized = false;

    /**
     *
     */
    private Path m_usersConfFile;

    /**
     *
     */
    private FileTime m_lastModified = null;

    /**
     *
     */
    private long m_fileSize;

    /**
     * Initializes the factory
     *
     * @throws java.io.IOException if any.
     * @throws java.io.FileNotFoundException if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     * @throws org.exolab.castor.xml.MarshalException if any.
     */
    public UserFactory() throws MarshalException, ValidationException, FileNotFoundException, IOException {
        super(GroupFactory.getInstance());
        reload();
    }

    /**
     * <p>init</p>
     *
     * @throws java.io.IOException if any.
     * @throws java.io.FileNotFoundException if any.
     * @throws org.exolab.castor.xml.MarshalException if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    public static synchronized void init() throws IOException, FileNotFoundException, MarshalException, ValidationException {

        if (instance == null || !initialized) {
            GroupFactory.init();
            instance = new UserFactory();
            initialized = true;
        }

    }

    /**
     * Singleton static call to get the only instance that should exist for the
     * UserFactory
     *
     * @return the single user factory instance
     */
    public static synchronized UserManager getInstance() {
        return instance;
    }

    /**
     * <p>Setter for the field <code>instance</code>.</p>
     *
     * @param mgr a {@link org.opennms.netmgt.config.UserManager} object.
     */
    public static synchronized void setInstance(UserManager mgr) {
        initialized = true;
        instance = mgr;
    }

    /**
     * <p>reload</p>
     *
     * @throws java.io.IOException if any.
     * @throws java.io.FileNotFoundException if any.
     * @throws org.exolab.castor.xml.MarshalException if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    public void reload() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        // Form the complete filename for the config file
        //
        m_usersConfFile = ConfigFileConstants.getFile(ConfigFileConstants.USERS_CONF_FILE_NAME);

        try (InputStream configIn = Files.newInputStream(m_usersConfFile);) {
            m_lastModified = Files.getLastModifiedTime(m_usersConfFile);
            m_fileSize = Files.size(m_usersConfFile);

            parseXML(configIn);

            initialized = true;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void saveXML(String writerString) throws IOException {
        if (writerString != null) {
            try (Writer fileWriter = Files.newBufferedWriter(m_usersConfFile, Charset.forName("UTF-8"));) {
                fileWriter.write(writerString);
                fileWriter.flush();
            }
        }
    }

    /**
     * <p>isUpdateNeeded</p>
     *
     * @return a boolean.
     */
    @Override
    public boolean isUpdateNeeded() {
        if (m_usersConfFile == null) {
            return true;
        }
        try {
            // Check to see if the file size has changed
            if (m_fileSize != Files.size(m_usersConfFile)) {
                return true;
            // Check to see if the timestamp has changed
            } else if (m_lastModified == null || !m_lastModified.equals(Files.getLastModifiedTime(m_usersConfFile))) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return true;
        }

    }

    /**
     * <p>update</p>
     *
     * @throws java.io.IOException if any.
     * @throws java.io.FileNotFoundException if any.
     * @throws org.exolab.castor.xml.MarshalException if any.
     * @throws org.exolab.castor.xml.ValidationException if any.
     */
    @Override
    public void doUpdate() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        if (isUpdateNeeded()) {
            reload();
        }
    }

    @Override
    public FileTime getLastModified() {
        return m_lastModified;
    }

    @Override
    public long getFileSize() {
        return m_fileSize;
    }
}
