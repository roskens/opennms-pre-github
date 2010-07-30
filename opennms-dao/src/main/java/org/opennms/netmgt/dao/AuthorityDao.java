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
import org.opennms.netmgt.model.OnmsAuthority;

/**
 * Contract to insert/update/read/delete the authorities
 *
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
public interface AuthorityDao {

    /**
     * Save an authorityDTO
     *
     * @param authority
     */
    public void save(OnmsAuthority authority);

    /**
     * Retrieve an authority by id
     *
     * @param id
     * @return authority
     */
    public OnmsAuthority getAuthority(Integer id);

    /**
     * Retrieve an authority by name
     *
     * @param name
     * @return authority
     */
    public OnmsAuthority getAuthorityByName(String name);

    /**
     * Remove an authority by id
     *
     * @param id
     */
    public void removeAuthority(Integer id);

    /**
     * @param pager
     * @return paginated list of authorities
     */
    public Set<OnmsAuthority> getAuthorities(Pager pager);

    /**
     * @return list of all authorities
     */
    public Set<OnmsAuthority> getAuthorities();

    /**
     * @return numbers of authorities present in the system
     */
    public Integer getAuthoritiesNumber();

    /**
     * @param username
     * @return the list of user's authorities by username
     */
    public Set<OnmsAuthority> getUserAuthorities(String username);

    /**
     * @param username
     * @return the list of authorities that user doesn't have
     */
    public Set<OnmsAuthority> getFreeAuthorities(String username);

    /**
     * @param username
     * @return the list of authorities that Group doesn't have
     */
    public Set<OnmsAuthority> getFreeAuthoritiesForGroup();

    /**
     * Save a list of authorities for a given group
     *
     * @param username
     * @param authorities
     */
    public void saveAuthorities(Integer id, Set<Integer> authorities);

    /**
     * Delete all user's groups
     *
     * @param username
     * @return the result of the operation
     */
    public void deleteUserGroups(String username);

    /**
     * @param id
     * @return
     */
    public Set<Integer> getIdItemsAuthority(Integer id);

    /**
     *
     * @param id
     * @return
     */
    public Set<OnmsAuthority> getGroupAuthorities(Integer id);

    /**
     *
     * @param id
     */
    public void removeGroupFromAuthorities(Integer id);

}

