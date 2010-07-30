// ============================================================================
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
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307,
// USA.
//
// The author can be contacted at the following email address:
//
// Massimiliano Dessi
// desmax74@yahoo.it
//
//
// -----------------------------------------------------------------------------
// OpenNMS Network Management System is Copyright by The OpenNMS Group, Inc.
// ============================================================================
package org.opennms.acl.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.opennms.acl.service.AuthorityService;
import org.opennms.acl.service.GroupService;
import org.opennms.netmgt.Pager;
import org.opennms.netmgt.model.OnmsAuthority;
import org.opennms.netmgt.model.OnmsGroup;
import org.opennms.netmgt.model.OnmsGroupView;

/**
 * This entity is an ACL group.
 *
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
public class GroupFacade implements Serializable {

    public GroupFacade(OnmsGroup group, AuthorityService authorityService,
            GroupService groupService) {
        this.group = group;
        this.authorityService = authorityService;
        this.groupService = groupService;
    }

    public Set<OnmsAuthority> getAuthorities() {
        return group.getAuthorities();
    }


    /**
     * Return a paginated list of anemic group
     *
     * @param pager
     * @return
     */
    public Set<OnmsGroup> getGroups(Pager pager) {
        return groupService.getGroups(pager);
    }

    /**
     * Return a read only Group
     *
     * @return authority
     */
    public OnmsGroupView getGroupView() {
        return group;
    }

    /**
     * @return hasAuthorities
     */
    public boolean hasAuthorities() {
        return group.getAuthorities().size() > 0;
    }

    /**
     * @return hasUsers
     */
    public boolean hasUser() {
        return groupService.hasUsers(group.getId());
    }

    /**
     * Save the internal state of the Group
     */
    public void save() {
        groupService.save(group);
    }

    /**
     * Overwrite the authorities assigned to this Group
     */
    public void setNewAuthorities(Set<Integer> items) {
        if (items.size() > 0) {
            Set<OnmsAuthority> auths = new HashSet<OnmsAuthority>();
            for (Integer item : items) {
                auths.add(authorityService.getAuthority(item));
            }
            group.setAuthorities(auths);
        }else{
            group.setAuthorities(new HashSet<OnmsAuthority>());
        }
    }

    /**
     * Remove this Group
     */
    public void remove() {
        if(group.getEliminable()){
            groupService.removeGroup(group.getId());
        }
    }

    /**
     * Return the human readable description of this Group
     *
     * @return description
     */
    public String getName() {
        return group.getName();
    }

    /**
     * Group unique identifier
     *
     * @return group's identifier
     */
    public Integer getId() {
        return group.getId();
    }

    /**
     * @return the authorities that the group doesn't have
     */
    public Set<OnmsAuthority> getFreeAuthorities() {
        return authorityService.getFreeAuthoritiesForGroup();
    }

    private OnmsGroup group;
    private AuthorityService authorityService;
    private GroupService groupService;
    private static final long serialVersionUID = 5L;
}
