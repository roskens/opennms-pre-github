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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class RemoteRepositoryConfig.
 */
@XmlRootElement(name = "remoteRepositoryConfig")
public class RemoteRepositoryConfig {

    /** The m_repository list. */
    private List<RemoteRepositoryDefinition> m_repositoryList = new ArrayList<RemoteRepositoryDefinition>();

    /** The jasper reports version. */
    private String jasperReportsVersion;

    /**
     * Gets the repository list.
     *
     * @return the repository list
     */
    @XmlElement(name = "remoteRepository")
    public List<RemoteRepositoryDefinition> getRepositoryList() {
        return m_repositoryList;
    }

    /**
     * Sets the repository list.
     *
     * @param repositoryList
     *            the new repository list
     */
    public void setRepositoryList(List<RemoteRepositoryDefinition> repositoryList) {
        this.m_repositoryList = repositoryList;
    }

    /**
     * Gets the jasper reports version.
     *
     * @return the jasper reports version
     */
    public String getJasperReportsVersion() {
        return jasperReportsVersion;
    }

    /**
     * Sets the jasper reports version.
     *
     * @param jasperReportsVersion
     *            the new jasper reports version
     */
    public void setJasperReportsVersion(String jasperReportsVersion) {
        this.jasperReportsVersion = jasperReportsVersion;
    }

}
