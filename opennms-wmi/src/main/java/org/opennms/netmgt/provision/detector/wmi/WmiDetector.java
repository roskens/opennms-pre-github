/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.provision.detector.wmi;

import java.net.InetAddress;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.config.WmiPeerFactory;
import org.opennms.netmgt.config.wmi.WmiAgentConfig;
import org.opennms.netmgt.provision.support.SyncAbstractDetector;
import org.opennms.protocols.wmi.WmiException;
import org.opennms.protocols.wmi.WmiManager;
import org.opennms.protocols.wmi.WmiParams;
import org.opennms.protocols.wmi.WmiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The Class WmiDetector.
 */
@Component
@Scope("prototype")
public class WmiDetector extends SyncAbstractDetector {

    /** The Constant LOG. */
    public static final Logger LOG = LoggerFactory.getLogger(WmiDetector.class);

    /** The Constant PROTOCOL_NAME. */
    private static final String PROTOCOL_NAME = "WMI";

    /** The Constant DEFAULT_WMI_CLASS. */
    private static final String DEFAULT_WMI_CLASS = "Win32_ComputerSystem";

    /** The Constant DEFAULT_WMI_OBJECT. */
    private static final String DEFAULT_WMI_OBJECT = "Status";

    /** The Constant DEFAULT_WMI_COMP_VAL. */
    private static final String DEFAULT_WMI_COMP_VAL = "OK";

    /** The Constant DEFAULT_WMI_MATCH_TYPE. */
    private static final String DEFAULT_WMI_MATCH_TYPE = "all";

    /** The Constant DEFAULT_WMI_COMP_OP. */
    private static final String DEFAULT_WMI_COMP_OP = "EQ";

    /** The Constant DEFAULT_WMI_WQL. */
    private static final String DEFAULT_WMI_WQL = "NOTSET";

    /** The m_match type. */
    private String m_matchType;

    /** The m_comp val. */
    private String m_compVal;

    /** The m_comp op. */
    private String m_compOp;

    /** The m_wmi class. */
    private String m_wmiClass;

    /** The m_wmi object. */
    private String m_wmiObject;

    /** The m_wmi wql str. */
    private String m_wmiWqlStr;

    /** The m_username. */
    private String m_username;

    /** The m_password. */
    private String m_password;

    /** The m_domain. */
    private String m_domain;

    /**
     * Instantiates a new wmi detector.
     */
    public WmiDetector() {
        super(PROTOCOL_NAME, 0);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.support.AbstractDetector#onInit()
     */
    @Override
    protected void onInit() {
        setMatchType(getMatchType() != null ? getMatchType() : DEFAULT_WMI_MATCH_TYPE);
        setCompVal(getCompVal() != null ? getCompVal() : DEFAULT_WMI_COMP_VAL);
        setCompOp(getCompOp() != null ? getCompOp() : DEFAULT_WMI_COMP_OP);
        setWmiClass(getWmiClass() != null ? getWmiClass() : DEFAULT_WMI_CLASS);
        setWmiObject(getWmiObject() != null ? getWmiObject() : DEFAULT_WMI_OBJECT);
        setWmiWqlStr(getWmiWqlStr() != null ? getWmiWqlStr() : DEFAULT_WMI_WQL);
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.support.SyncAbstractDetector#isServiceDetected(java.net.InetAddress)
     */
    @Override
    public boolean isServiceDetected(final InetAddress address) {
        WmiParams clientParams = null;

        if (getWmiWqlStr().equals(DEFAULT_WMI_WQL)) {
            // Create the check parameters holder.
            clientParams = new WmiParams(WmiParams.WMI_OPERATION_INSTANCEOF, getCompVal(), getCompOp(), getWmiClass(),
                                         getWmiObject());
        } else {
            // Define the WQL Query.
            clientParams = new WmiParams(WmiParams.WMI_OPERATION_WQL, getCompVal(), getCompOp(), getWmiWqlStr(),
                                         getWmiObject());
        }

        // Use WMI credentials from configuration files, and override values
        // with the detector parameters if they exists.
        final WmiAgentConfig agentConfig = WmiPeerFactory.getInstance().getAgentConfig(address);
        if (getUsername() != null)
            agentConfig.setUsername(getUsername());
        if (getPassword() != null)
            agentConfig.setPassword(getPassword());
        if (getDomain() != null)
            agentConfig.setDomain(getDomain());
        if (getRetries() > 0)
            agentConfig.setRetries(getRetries());
        if (getTimeout() > 0)
            agentConfig.setTimeout(getTimeout());

        // Perform the operation specified in the parameters.
        WmiResult result = isServer(address, agentConfig.getUsername(), agentConfig.getPassword(),
                                    agentConfig.getDomain(), getMatchType(), agentConfig.getRetries(),
                                    agentConfig.getTimeout(), clientParams);

        // Only fail on critical and unknown returns.
        return (result != null && result.getResultCode() != WmiResult.RES_STATE_CRIT && result.getResultCode() != WmiResult.RES_STATE_UNKNOWN);
    }

    /**
     * Checks if is server.
     *
     * @param host
     *            the host
     * @param user
     *            the user
     * @param pass
     *            the pass
     * @param domain
     *            the domain
     * @param matchType
     *            the match type
     * @param retries
     *            the retries
     * @param timeout
     *            the timeout
     * @param params
     *            the params
     * @return the wmi result
     */
    private WmiResult isServer(InetAddress host, String user, String pass, String domain, String matchType,
            int retries, int timeout, WmiParams params) {
        boolean isAServer = false;

        WmiResult result = null;
        for (int attempts = 0; attempts <= retries && !isAServer; attempts++) {
            WmiManager mgr = null;
            try {
                mgr = new WmiManager(InetAddressUtils.str(host), user, pass, domain, matchType);

                // Connect to the WMI server.
                mgr.init();

                // Perform the operation specified in the parameters.
                result = mgr.performOp(params);
                if (params.getWmiOperation().equals(WmiParams.WMI_OPERATION_WQL)) {
                    LOG.debug("WmiPlugin: {} : {}", params.getWql(),
                              WmiResult.convertStateToString(result.getResultCode()));
                } else {
                    LOG.debug("WmiPlugin: \\\\{}\\{} : {}", params.getWmiClass(), params.getWmiObject(),
                              WmiResult.convertStateToString(result.getResultCode()));
                }

                isAServer = true;
            } catch (WmiException e) {
                StringBuffer message = new StringBuffer();
                message.append("WmiPlugin: Check failed... : ");
                message.append(e.getMessage());
                message.append(" : ");
                message.append((e.getCause() == null ? "" : e.getCause().getMessage()));
                LOG.info(message.toString());
                isAServer = false;
            } finally {
                if (mgr != null) {
                    try {
                        mgr.close();
                    } catch (WmiException e) {
                        LOG.warn("an error occurred closing the WMI Manager", e);
                    }
                }
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.opennms.netmgt.provision.support.AbstractDetector#dispose()
     */
    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    /**
     * Sets the match type.
     *
     * @param matchType
     *            the new match type
     */
    public void setMatchType(String matchType) {
        m_matchType = matchType;
    }

    /**
     * Gets the match type.
     *
     * @return the match type
     */
    public String getMatchType() {
        return m_matchType;
    }

    /**
     * Sets the comp val.
     *
     * @param compVal
     *            the new comp val
     */
    public void setCompVal(String compVal) {
        m_compVal = compVal;
    }

    /**
     * Gets the comp val.
     *
     * @return the comp val
     */
    public String getCompVal() {
        return m_compVal;
    }

    /**
     * Sets the comp op.
     *
     * @param compOp
     *            the new comp op
     */
    public void setCompOp(String compOp) {
        m_compOp = compOp;
    }

    /**
     * Gets the comp op.
     *
     * @return the comp op
     */
    public String getCompOp() {
        return m_compOp;
    }

    /**
     * Sets the wmi class.
     *
     * @param wmiClass
     *            the new wmi class
     */
    public void setWmiClass(String wmiClass) {
        m_wmiClass = wmiClass;
    }

    /**
     * Gets the wmi class.
     *
     * @return the wmi class
     */
    public String getWmiClass() {
        return m_wmiClass;
    }

    /**
     * Sets the wmi object.
     *
     * @param wmiObject
     *            the new wmi object
     */
    public void setWmiObject(String wmiObject) {
        m_wmiObject = wmiObject;
    }

    /**
     * Gets the wmi object.
     *
     * @return the wmi object
     */
    public String getWmiObject() {
        return m_wmiObject;
    }

    /**
     * Sets the wmi wql str.
     *
     * @param wmiWqlStr
     *            the new wmi wql str
     */
    public void setWmiWqlStr(String wmiWqlStr) {
        m_wmiWqlStr = wmiWqlStr;
    }

    /**
     * Gets the wmi wql str.
     *
     * @return the wmi wql str
     */
    public String getWmiWqlStr() {
        return m_wmiWqlStr;
    }

    /**
     * Sets the username.
     *
     * @param username
     *            the new username
     */
    public void setUsername(String username) {
        m_username = username;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return m_username;
    }

    /**
     * Sets the password.
     *
     * @param password
     *            the new password
     */
    public void setPassword(String password) {
        m_password = password;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return m_password;
    }

    /**
     * Sets the domain.
     *
     * @param domain
     *            the new domain
     */
    public void setDomain(String domain) {
        m_domain = domain;
    }

    /**
     * Gets the domain.
     *
     * @return the domain
     */
    public String getDomain() {
        return m_domain;
    }

}
