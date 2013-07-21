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

package org.opennms.netmgt.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.ConfigFileConstants;

/**
 * <p>
 * UserFactory class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
public class UserFactory extends UserManager {

    /** The static singleton instance of the UserFactory. */
    private static UserManager instance;

    /**
     * File path of users.xml
     */
    protected File usersFile;

    /** Boolean indicating if the init() method has been called. */
    private static boolean initialized = false;

    /** The m_users conf file. */
    private File m_usersConfFile;

    /** The m_last modified. */
    private long m_lastModified;

    /** The m_file size. */
    private long m_fileSize;

    /**
     * Initializes the factory.
     *
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public UserFactory() throws MarshalException, ValidationException, FileNotFoundException, IOException {
        super(GroupFactory.getInstance());
        reload();
    }

    /**
     * <p>
     * init
     * </p>
     * .
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public static synchronized void init() throws IOException, FileNotFoundException, MarshalException,
            ValidationException {

        if (instance == null || !initialized) {
            GroupFactory.init();
            instance = new UserFactory();
            initialized = true;
        }

    }

    /**
     * Singleton static call to get the only instance that should exist for the
     * UserFactory.
     *
     * @return the single user factory instance
     */
    public static synchronized UserManager getInstance() {
        return instance;
    }

    /**
     * <p>
     * Setter for the field <code>instance</code>.
     * </p>
     *
     * @param mgr
     *            a {@link org.opennms.netmgt.config.UserManager} object.
     */
    public static synchronized void setInstance(UserManager mgr) {
        initialized = true;
        instance = mgr;
    }

    /**
     * <p>
     * reload
     * </p>
     * .
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    public void reload() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        // Form the complete filename for the config file
        //
        m_usersConfFile = ConfigFileConstants.getFile(ConfigFileConstants.USERS_CONF_FILE_NAME);

        InputStream configIn = new FileInputStream(m_usersConfFile);
        m_lastModified = m_usersConfFile.lastModified();
        m_fileSize = m_usersConfFile.length();

        parseXML(configIn);

        initialized = true;

    }

    /** {@inheritDoc} */
    @Override
    protected void saveXML(String writerString) throws IOException {
        if (writerString != null) {
            Writer fileWriter = new OutputStreamWriter(new FileOutputStream(m_usersConfFile), "UTF-8");
            fileWriter.write(writerString);
            fileWriter.flush();
            fileWriter.close();
        }
    }

    /**
     * <p>
     * isUpdateNeeded
     * </p>
     * .
     *
     * @return a boolean.
     */
    @Override
    public boolean isUpdateNeeded() {
        if (m_usersConfFile == null) {
            return true;
        } else {
            // Check to see if the file size has changed
            if (m_fileSize != m_usersConfFile.length()) {
                return true;
                // Check to see if the timestamp has changed
            } else if (m_lastModified != m_usersConfFile.lastModified()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * <p>
     * update
     * </p>
     * .
     *
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws FileNotFoundException
     *             the file not found exception
     * @throws MarshalException
     *             the marshal exception
     * @throws ValidationException
     *             the validation exception
     */
    @Override
    public void doUpdate() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        if (isUpdateNeeded()) {
            reload();
        }
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
}
