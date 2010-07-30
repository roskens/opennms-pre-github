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

import java.util.HashSet;
import java.util.Set;

import org.opennms.netmgt.Pager;
import org.opennms.netmgt.dao.AuthorityDao;
import org.opennms.netmgt.dao.GroupDao;
import org.opennms.netmgt.model.OnmsAuthority;
import org.opennms.netmgt.model.OnmsGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
@Service("groupService")
public class GroupServiceImpl implements GroupService {

    @Transactional(readOnly=true)
    public Set<OnmsGroup> getUserGroupsWithAutorities(String username) {
        return groupRepository.getUserGroupsWithAutorities(username);
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=DataAccessException.class)
    public void deleteUserGroups(String username) {
        groupRepository.deleteUserGroups(username);
    }

    @Transactional(readOnly=true)
    public Set<OnmsGroup> getFreeGroups(String username) {
        return groupRepository.getFreeGroups(username);
    }

    @Transactional(readOnly=true)
    public OnmsGroup getGroup(Integer id) {
        return groupRepository.getGroup(id);
    }

    @Transactional(readOnly=true)
    public Set<OnmsGroup> getGroups(Pager pager) {
        return groupRepository.getGroups(pager);
    }

    @Transactional(readOnly=true)
    public Set<OnmsGroup> getUserGroups(String username) {
        return groupRepository.getUserGroups(username);
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=DataAccessException.class)
    public void removeGroup(Integer id) {
        groupRepository.removeGroup(id);
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=DataAccessException.class)
    public void save(OnmsGroup group) {
    	if(!group.isNew()){
    		boolean eliminable = groupRepository.getGroup(group.getId()).getEliminable();
    		group.setEliminable(eliminable);
    	}
        authorityRepository.removeGroupFromAuthorities(group.getId());
        if(group.getAuthorities().size() > 0){
            authorityRepository.saveAuthorities(group.getId(), getIdAuthorities(group));
        }
        groupRepository.save(group);
    }



    @Transactional(readOnly= true)
    public OnmsGroup getGroupByName(String name) {
        return groupRepository.getGroupByName(name);
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=DataAccessException.class)
    public void saveGroups(String username, Set<Integer> groups) {
        groupRepository.saveGroups(username, groups);
    }

    @Transactional(readOnly=true)
    public Integer getTotalItemsNumber() {
        return groupRepository.getGroupsNumber();
    }

    @Transactional(readOnly=true)
    public Boolean hasUsers(Integer id) {
        return groupRepository.hasUsers(id);
    }

    private Set<Integer> getIdAuthorities(OnmsGroup group) {
        Set<Integer> ids = new HashSet<Integer>();
        for (OnmsAuthority authority : group.getAuthorities()) {
            ids.add(authority.getId());
        }
        return ids;
    }

    @Autowired
    private GroupDao groupRepository;
    @Autowired
    private AuthorityDao authorityRepository;

}