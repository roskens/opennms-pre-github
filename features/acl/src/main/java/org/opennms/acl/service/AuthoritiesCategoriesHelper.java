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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.opennms.netmgt.model.OnmsAuthority;

/**
 * This class retrieve the categories that an authority can view,
 * All methods are thread safe
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */

class AuthoritiesCategoriesHelper {

    public AuthoritiesCategoriesHelper() {
        authItemsMap = new HashMap<String, Set<Integer>>();
    }

    public AuthoritiesCategoriesHelper(Set<OnmsAuthority> authorities) {
        authItemsMap = new HashMap<String, Set<Integer>>();
        if (authorities != null && authorities.size() > 0) {
            for (OnmsAuthority authority : authorities) {

                if (authority.getItems().size() > 0) {
                    addAuthorityWithCategories(authority);
                }
            }
        }
    }

    public void addIsAbsentAuthorityItems(OnmsAuthority authority) {
        addAuthorityWithCategories(authority);
    }


	public synchronized Boolean deleteAuthority(String authority) {
		return authItemsMap.remove(authority) != null;
	}


	// TODO put in a cache
	public synchronized Set<Integer> getAuthoritiesItems(Set<OnmsAuthority> authorities) {
		Set<Integer> authItems = new HashSet<Integer>();
		for (OnmsAuthority auth : authorities) {
			if (authItemsMap.containsKey(auth.getName())) {
				authItems.addAll(new HashSet<Integer>(authItemsMap.get(auth
						.getName())));
			}
		}
		return authItems;
	}


	public synchronized void addAuthorityWithCategories(OnmsAuthority authority) {

	    if (authItemsMap.containsKey(authority.getName())) {
            Set<Integer> items = authItemsMap.get(authority.getName());
            items.addAll(authority.getItems());
            authItemsMap.put(authority.getName(), items);

        } else {
            authItemsMap.put(authority.getName(), authority.getItems());
        }
	}

	private Map<String, Set<Integer>> authItemsMap;
}
