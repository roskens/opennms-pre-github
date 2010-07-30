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

import java.util.Set;

import org.opennms.netmgt.model.OnmsAuthority;
import org.opennms.netmgt.model.OnmsCategory;

/**
 * Contract to manage acl items
 *
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
public interface AclCategoryService {

	/**
	 * @return all Category
	 */
	public Set<OnmsCategory> getItems();

	/**
	 * delete an item
	 *
	 * @param id
	 */
	public Boolean deleteAuthority(String authority);

    /**
     * add an authority with its items
     *
     * @param authority
     */
    public void addIfAbsentAuthority(OnmsAuthority authority);

	/**
	 * This class retrieve the id of the categories that a GenericUser
	 * can be view
	 *
	 * @param role
	 * @return the set of items'ids permitted
	 */
	public Set<Integer> getAclItems(Set<OnmsAuthority> authorities);

	/**
	 * This class retrieve the Categories that a GenericUser
     * can be view
	 * @param authorityItemsID
	 * @return the set of Categories permitted
	 */
	public Set<OnmsCategory> getFreeItems(Set<Integer> authorityItemsID);

}
