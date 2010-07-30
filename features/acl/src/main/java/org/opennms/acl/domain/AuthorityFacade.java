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
import java.util.HashSet;
import java.util.Set;

import org.opennms.acl.service.AclCategoryService;
import org.opennms.acl.service.AuthorityService;
import org.opennms.netmgt.Pager;
import org.opennms.netmgt.model.OnmsAuthority;
import org.opennms.netmgt.model.OnmsAuthorityView;
import org.opennms.netmgt.model.OnmsCategory;

/**
 * This entity class represents an Authority (permission/authority/group/category/other...)
 *
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
public class AuthorityFacade implements Serializable {

    /**
     * Constructor used by AuthorityFactory
     *
     * @param authority
     * @param authorityService
     * @param aclItemService
     */
    public AuthorityFacade(OnmsAuthority authority, AuthorityService authorityService, AclCategoryService aclItemService) {
        this.authority = authority;
        this.authorityService = authorityService;
        this.aclItemService = aclItemService;
    }

    /**
     * Return a paginated list of anemic authorities
     *
     * @param pager
     * @return
     */
    public Set<OnmsAuthority> getAuthorities(Pager pager) {
        return authorityService.getAuthorities(pager);
    }

    /**
     * Return a read only Autority
     *
     * @return authority
     */
    public OnmsAuthorityView getAuthorityView() {
        return authority;
    }

    /**
     * @return hasItems
     */
    public boolean hasItems() {
        return !authority.getItems().isEmpty();
    }

    /**
     * Save the internal state of the Authority
     */
    public void save() {
        authorityService.save(authority);
    }

    /**
     * Overwrite the items assigned to this Autority
     */
    public void setNewItems(Set<Integer> items) {
        authority.setItems(items);
        aclItemService.deleteAuthority(authority.getName());
        aclItemService.addIfAbsentAuthority(authority);
    }

    /**
     * Remove this Autority
     */
    public void remove() {
        if(authority.getEliminable()){
            authorityService.removeAuthority(authority.getId());
            aclItemService.deleteAuthority(authority.getName());
        }
    }

    /**
     * Return the human readable description of this Authority
     *
     * @return description
     */
    public String getDescription() {
        return authority.getDescription();
    }

    /**
     * Return the eliminable properties of the authorities
     *
     * @return description
     */
    public boolean getEliminable() {
        return authority.getEliminable();
    }

    /**
     * Authority unique identifier
     *
     * @return authority's identifier
     */
    public Integer getId() {
        return authority.getId();
    }

    /**
     * Return a list of all items manageable by authorities
     *
     * @return all items
     */
    public Set<OnmsCategory> getAllItems() {
        return aclItemService.getItems();
    }

    /**
     * Return the items that the authority does not have
     * @return
     */
    public Set<OnmsCategory> getFreeItems() {
        return aclItemService.getFreeItems(getItems());
    }

    /**
     * Return a list of all items managed by this Authority
     *
     * @return authority items
     */
    public Set<Integer> getItems() {
    	if(!hasItems() && authority.getCategories().size() >0){
    		Set<Integer> items = new HashSet<Integer>();
    		for (OnmsCategory category : authority.getCategories()) {
				items.add(category.getId());
			}
    		authority.setItems(items);
    	}
        return authority.getItems();

    }

    public Set<OnmsCategory> getCategories(){
        return authority.getCategories();
    }

    /**
     * Return the name of the Authority
     *
     * @return name
     */
    public String getName() {
        return authority.getName();
    }

    private OnmsAuthority authority;
    private AuthorityService authorityService;
    private AclCategoryService aclItemService;
    private static final long serialVersionUID = 1L;
}
