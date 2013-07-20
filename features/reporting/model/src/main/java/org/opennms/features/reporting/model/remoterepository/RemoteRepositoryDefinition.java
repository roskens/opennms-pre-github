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

package org.opennms.features.reporting.model.remoterepository;

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class RemoteRepositoryDefinition.
 */
@XmlRootElement(name = "remoteRepository")
public class RemoteRepositoryDefinition {

    /** The m_repository active. */
    private Boolean m_repositoryActive;

    /** The URI. */
    private URI m_URI;

    /** The m_login user. */
    private String m_loginUser;

    /** The m_login repo password. */
    private String m_loginRepoPassword;

    /** The m_repository name. */
    private String m_repositoryName;

    /** The m_repository description. */
    private String m_repositoryDescription;

    /** The m_repository management url. */
    private String m_repositoryManagementURL;

    /** The m_repository id. */
    private String m_repositoryId;

    /**
     * Gets the login repo password.
     *
     * @return the login repo password
     */
    public String getLoginRepoPassword() {
        return m_loginRepoPassword;
    }

    /**
     * Gets the login user.
     *
     * @return the login user
     */
    public String getLoginUser() {
        return m_loginUser;
    }

    /**
     * Gets the repository description.
     *
     * @return the repository description
     */
    public String getRepositoryDescription() {
        return m_repositoryDescription;
    }

    /**
     * Gets the repository id.
     *
     * @return the repository id
     */
    public String getRepositoryId() {
        return m_repositoryId;
    }

    /**
     * Gets the repository management url.
     *
     * @return the repository management url
     */
    public String getRepositoryManagementURL() {
        return m_repositoryManagementURL;
    }

    /**
     * Gets the repository name.
     *
     * @return the repository name
     */
    public String getRepositoryName() {
        return m_repositoryName;
    }

    /**
     * Gets the uri.
     *
     * @return the uri
     */
    public URI getURI() {
        return m_URI;
    }

    /**
     * Checks if is repository active.
     *
     * @return the boolean
     */
    public Boolean isRepositoryActive() {
        return m_repositoryActive;
    }

    /**
     * Sets the login repo password.
     *
     * @param loginRepoPassword
     *            the new login repo password
     */
    @XmlElement(name = "login-password")
    public void setLoginRepoPassword(String loginRepoPassword) {
        m_loginRepoPassword = loginRepoPassword;
    }

    /**
     * Sets the login user.
     *
     * @param loginUser
     *            the new login user
     */
    @XmlElement(name = "login-user")
    public void setLoginUser(String loginUser) {
        m_loginUser = loginUser;
    }

    /**
     * Sets the repository active.
     *
     * @param repositoryActive
     *            the new repository active
     */
    @XmlElement(name = "active")
    public void setRepositoryActive(Boolean repositoryActive) {
        m_repositoryActive = repositoryActive;
    }

    /**
     * Sets the repository description.
     *
     * @param repositoryDescription
     *            the new repository description
     */
    @XmlElement(name = "description")
    public void setRepositoryDescription(String repositoryDescription) {
        m_repositoryDescription = repositoryDescription;
    }

    /**
     * Sets the repository id.
     *
     * @param repositoryId
     *            the new repository id
     */
    @XmlElement(name = "id")
    public void setRepositoryId(String repositoryId) {
        m_repositoryId = repositoryId;
    }

    /**
     * Sets the repository management url.
     *
     * @param repositoryManagementURL
     *            the new repository management url
     */
    @XmlElement(name = "management-url")
    public void setRepositoryManagementURL(String repositoryManagementURL) {
        m_repositoryManagementURL = repositoryManagementURL;
    }

    /**
     * Sets the repository name.
     *
     * @param repositoryName
     *            the new repository name
     */
    @XmlElement(name = "name")
    public void setRepositoryName(String repositoryName) {
        m_repositoryName = repositoryName;
    }

    /**
     * Sets the uri.
     *
     * @param uri
     *            the new uri
     */
    @XmlElement(name = "uri")
    public void setURI(URI uri) {
        m_URI = uri;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RemoteRepositoryConfig [m_repositoryActive=" + m_repositoryActive + ", m_URI=" + m_URI
                + ", m_loginUser=" + m_loginUser + ", m_loginRepoPassword=" + m_loginRepoPassword
                + ", m_repositoryName=" + m_repositoryName + ", m_repositoryDescription=" + m_repositoryDescription
                + ", m_repositoryManagementURL=" + m_repositoryManagementURL + ", m_repositoryId=" + m_repositoryId
                + "]";
    }
}
