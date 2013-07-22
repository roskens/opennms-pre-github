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
package org.opennms.netmgt.ticketer.remedy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * DefaultremedyConfigDao class.
 * </p>
 *
 * @author ranger
 * @version $Id: $
 */
public class DefaultRemedyConfigDao {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRemedyConfigDao.class);

    /** The m_config. */
    Configuration m_config = null;

    /**
     * Retrieves the properties defined in the remedy.properties file.
     *
     * @return a <code>java.util.Properties</code> object containing remedy
     *         plugin defined properties
     */

    private Configuration getProperties() {
        if (m_config != null) {
            return m_config;
        }
        String propsFile = new String(System.getProperty("opennms.home") + "/etc/remedy.properties");

        LOG.debug("loading properties from: {}", propsFile);

        Configuration config = null;

        try {
            config = new PropertiesConfiguration(propsFile);
        } catch (final ConfigurationException e) {
            LOG.debug("Unable to load properties from {}", propsFile, e);
        }
        m_config = config;
        return config;

    }

    /**
     * <p>
     * getUserName
     * </p>
     * .
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUserName() {
        return getProperties().getString("remedy.username");
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    String getPassword() {
        return getProperties().getString("remedy.password");
    }

    /**
     * Gets the authentication.
     *
     * @return the authentication
     */
    String getAuthentication() {
        return getProperties().getString("remedy.authentication");
    }

    /**
     * Gets the locale.
     *
     * @return the locale
     */
    String getLocale() {
        return getProperties().getString("remedy.locale");
    }

    /**
     * Gets the time zone.
     *
     * @return the time zone
     */
    String getTimeZone() {
        return getProperties().getString("remedy.timezone");
    }

    /**
     * Gets the end point.
     *
     * @return the end point
     */
    String getEndPoint() {
        return getProperties().getString("remedy.endpoint");
    }

    /**
     * Gets the port name.
     *
     * @return the port name
     */
    String getPortName() {
        return getProperties().getString("remedy.portname");
    }

    /**
     * Gets the creates the end point.
     *
     * @return the creates the end point
     */
    String getCreateEndPoint() {
        return getProperties().getString("remedy.createendpoint");
    }

    /**
     * Gets the creates the port name.
     *
     * @return the creates the port name
     */
    String getCreatePortName() {
        return getProperties().getString("remedy.createportname");
    }

    /**
     * Gets the target groups.
     *
     * @return the target groups
     */
    List<String> getTargetGroups() {
        List<String> targetGroups = new ArrayList<String>();
        if (getProperties().containsKey("remedy.targetgroups")) {
            for (String group : getProperties().getString("remedy.targetgroups").split(":")) {
                targetGroups.add(group);
            }
        }
        return targetGroups;
    }

    /**
     * Gets the assigned group.
     *
     * @return the assigned group
     */
    String getAssignedGroup() {
        return getProperties().getString("remedy.assignedgroup");
    }

    /**
     * Gets the assigned group.
     *
     * @param targetGroup
     *            the target group
     * @return the assigned group
     */
    String getAssignedGroup(String targetGroup) {
        if (getProperties().containsKey("remedy.assignedgroup." + targetGroup)) {
            return getProperties().getString("remedy.assignedgroup." + targetGroup);
        }
        return getAssignedGroup();
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    String getFirstName() {
        return getProperties().getString("remedy.firstname");
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    String getLastName() {
        return getProperties().getString("remedy.lastname");
    }

    /**
     * Gets the service ci.
     *
     * @return the service ci
     */
    String getServiceCI() {
        return getProperties().getString("remedy.serviceCI");
    }

    /**
     * Gets the service ci recon id.
     *
     * @return the service ci recon id
     */
    String getServiceCIReconID() {
        return getProperties().getString("remedy.serviceCIReconID");
    }

    /**
     * Gets the assigned support company.
     *
     * @return the assigned support company
     */
    String getAssignedSupportCompany() {
        return getProperties().getString("remedy.assignedsupportcompany");
    }

    /**
     * Gets the assigned support company.
     *
     * @param targetGroup
     *            the target group
     * @return the assigned support company
     */
    String getAssignedSupportCompany(String targetGroup) {
        if (getProperties().containsKey("remedy.assignedsupportcompany." + targetGroup)) {
            return getProperties().getString("remedy.assignedsupportcompany." + targetGroup);
        }
        return getAssignedSupportCompany();
    }

    /**
     * Gets the assigned support organization.
     *
     * @return the assigned support organization
     */
    String getAssignedSupportOrganization() {
        return getProperties().getString("remedy.assignedsupportorganization");
    }

    /**
     * Gets the assigned support organization.
     *
     * @param targetGroup
     *            the target group
     * @return the assigned support organization
     */
    String getAssignedSupportOrganization(String targetGroup) {
        if (getProperties().containsKey("remedy.assignedsupportorganization." + targetGroup)) {
            return getProperties().getString("remedy.assignedsupportorganization." + targetGroup);
        }
        return getAssignedSupportOrganization();
    }

    /**
     * Gets the categorizationtier1.
     *
     * @return the categorizationtier1
     */
    String getCategorizationtier1() {
        return getProperties().getString("remedy.categorizationtier1");
    }

    /**
     * Gets the categorizationtier2.
     *
     * @return the categorizationtier2
     */
    String getCategorizationtier2() {
        return getProperties().getString("remedy.categorizationtier2");
    }

    /**
     * Gets the categorizationtier3.
     *
     * @return the categorizationtier3
     */
    String getCategorizationtier3() {
        return getProperties().getString("remedy.categorizationtier3");
    }

    /**
     * Gets the service type.
     *
     * @return the service type
     */
    String getServiceType() {
        return getProperties().getString("remedy.serviceType");
    }

    /**
     * Gets the reported source.
     *
     * @return the reported source
     */
    String getReportedSource() {
        return getProperties().getString("remedy.reportedSource");
    }

    /**
     * Gets the impact.
     *
     * @return the impact
     */
    String getImpact() {
        return getProperties().getString("remedy.impact");
    }

    /**
     * Gets the urgency.
     *
     * @return the urgency
     */
    String getUrgency() {
        return getProperties().getString("remedy.urgency");
    }

    /**
     * Gets the resolution.
     *
     * @return the resolution
     */
    String getResolution() {
        return getProperties().getString("remedy.resolution");
    }

    /**
     * Gets the re open status reason.
     *
     * @return the re open status reason
     */
    String getReOpenStatusReason() {
        return getProperties().getString("remedy.reason.reopen");
    }

    /**
     * Gets the resolved status reason.
     *
     * @return the resolved status reason
     */
    String getResolvedStatusReason() {
        return getProperties().getString("remedy.reason.resolved");
    }

    /**
     * Gets the cancelled status reason.
     *
     * @return the cancelled status reason
     */
    String getCancelledStatusReason() {
        return getProperties().getString("remedy.reason.cancelled");
    }
}
