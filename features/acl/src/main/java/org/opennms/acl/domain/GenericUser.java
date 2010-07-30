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
package org.opennms.acl.domain;

import java.io.Serializable;
import java.util.Set;

import org.opennms.acl.service.GroupService;
import org.opennms.acl.service.UserService;
import org.opennms.netmgt.model.OnmsAuthority;
import org.opennms.netmgt.model.OnmsGroup;
import org.opennms.netmgt.model.OnmsUser;
import org.opennms.netmgt.model.OnmsUserView;
import org.springframework.util.Assert;

/**
 * This entity represent a user managed by Acl application.
 *
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
public class GenericUser implements Serializable {

    /**
     * Constructor
     *
     * @param user
     * @param authorityService
     */
    public GenericUser(OnmsUser user, UserService userService, GroupService groupService) {
        Assert.notNull(user);
        this.user = user;
        this.userService = userService;
        this.groupService = groupService;
    }

    /**
     * Save the user
     */
    public void save() {
        userService.saveUserAndAuthorities(user);
    }

    /**
     * Add a list of groups to this GenericUser
     *
     * @param authorities
     * @return result of the operation
     */
    public void setNewGroups(Set<Integer> items) {
        user.setItems(items);
    }

    /**
     * Return a list of groups that this GenericUser don't have
     *
     * @return free groups
     */
    public Set<OnmsGroup> getFreeGroups() {
        return groupService.getFreeGroups(user.getUsername());
    }

    public Set<OnmsGroup> getGroups() {
        return groupService.getUserGroups(user.getUsername());
    }

    /**
     * Return a read only GenericUser
     *
     * @return
     */
    public OnmsUserView getUserView() {
        return user;
    }


    /**
     * Return the username of this GenericUser
     *
     * @return
     */
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Return a list of authorities of this GenericUser
     *
     * @return
     */
    public Set<OnmsAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    private OnmsUser user;
    private GroupService groupService;
    private UserService userService;
    private static final long serialVersionUID = 1L;
}