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
package org.opennms.web.springframework.security;

import org.opennms.netmgt.model.OnmsUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * The Class OnmsAuthenticationToken.
 */
final class OnmsAuthenticationToken extends AbstractAuthenticationToken {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5896244818836123481L;

    /** The m_user. */
    private final OnmsUser m_user;

    /**
     * Instantiates a new onms authentication token.
     *
     * @param user
     *            the user
     */
    OnmsAuthenticationToken(final OnmsUser user) {
        super(user.getAuthorities());
        m_user = user;
        setAuthenticated(true);
    }

    /**
     * This should always be a UserDetails. Java-Spec allows this,
     * spring can handle it and it's easier for us this way.
     *
     * @return the principal
     */
    @Override
    public Object getPrincipal() {
        return m_user;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.core.Authentication#getCredentials()
     */
    @Override
    public Object getCredentials() {
        return m_user.getPassword();
    }
}
