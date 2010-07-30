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
package org.opennms.netmgt.dao;

import java.util.Set;


import org.opennms.netmgt.Pager;
import org.opennms.netmgt.model.OnmsGroup;

/**
 * Contract to insert/update/read/delete the groups
 *
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
public interface GroupDao {

    /**
     * Save a GroupDTO
     *
     * @param group
     */
    public void save(OnmsGroup group);

    /**
     * Retrieve a group by id
     *
     * @param id
     * @return group
     */
    public OnmsGroup getGroup(Integer id);

    /**
     * Retrieve a group by name
     *
     * @param name
     * @return group
     */
    public OnmsGroup getGroupByName(String name);

    /**
     * Remove a group by id
     *
     * @param id
     */
    public void removeGroup(Integer id);

    /**
     * @param pager
     * @return paginated list of groups
     */
    public Set<OnmsGroup> getGroups(Pager pager);

    /**
     * @return list of all authorities
     */
    public Set<OnmsGroup> getGroups();

    /**
     * @return numbers of authorities present in the system
     */
    public Integer getGroupsNumber();

    /**
     * @param username
     * @return the list of user's authorities by username
     */
    public Set<OnmsGroup> getUserGroups(String username);

    /**
     * @param username
     * @return the list of user's authorities by username
     */
    public Set<OnmsGroup> getUserGroupsWithAutorities(String username);

    /**
     * @param username
     * @return the list of authorities that user doesn't have
     */
    public Set<OnmsGroup> getFreeGroups(String username);

    /**
     * Save a list of authorities for a given user
     *
     * @param username
     * @param groups
     */
    public void saveGroups(String username, Set<Integer> groups);

    /**
     * Delete all user's authorities
     *
     * @param username
     * @return the result of the operation
     */
    public void deleteUserGroups(String username);


    public boolean hasUsers(Integer id);

}

