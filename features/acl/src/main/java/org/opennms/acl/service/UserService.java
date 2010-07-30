//============================================================================
//
// Copyright (c) 2009+ Massimiliano Dessi (desmax74)
// Copyright (c) 2009+ The OpenNMS Group, Inc.
// All rights reserved everywhere.
//
// This program was developed and is maintained by Massimiliano Dessi
// ("the author") and is subject to dual-copyright according to
// the terms set in "The OpenNMS Project Contributor Agreement".
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
// USA.
//
// The author can be contacted at the following email address:
//
//       Massimiliano Dessi
//       desmax74@yahoo.it
//
//
//-----------------------------------------------------------------------------
// OpenNMS Network Management System is Copyright by The OpenNMS Group, Inc.
//============================================================================
package org.opennms.acl.service;

import java.util.List;

import org.opennms.netmgt.Pager;
import org.opennms.netmgt.model.OnmsUser;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
public interface UserService extends PagerService {

    /**
     * Retrieve all users (enabled/disabled)
     *
     * @param pager
     * @return
     */
    public List<OnmsUser> getUsers(Pager pager);

    /**
     * Retrieve the enabled users
     *
     * @param pager
     * @return
     */
    public List<OnmsUser> getEnabledUsers(Pager pager);

    /**
     * Retrieve the disabled users
     *
     * @param pager
     * @return
     */
    public List<OnmsUser> getDisabledUsers(Pager pager);


    /**
     * disable a user
     *
     * @param id
     */
    public void disableUser(String username);

    /**
     * enable a user
     *
     * @param id
     */
    public void enableUser(String username);

    /**
     * retrieve a user
     *
     * @param id
     * @return a {@link UserView}
     */
    public OnmsUser getUser(String username);


    /**
     * retrieve a user with their authorities
     *
     * @param username
     * @return {@link UserAuthoritiesDTO} with their roles
     */
    public OnmsUser getUserWithAuthorities(String username);


    /**
     * @return user's number
     */
    public Integer getUsersNumber();

    /**
     * Method only for admin, insert a user or change the user password
     *
     * @param user
     */
    public void saveUserAndAuthorities(OnmsUser user);

    /**
     * Method only for admin, insert a user or change the user password
     *
     * @param user
     */
    public void saveUser(OnmsUser user);

}
