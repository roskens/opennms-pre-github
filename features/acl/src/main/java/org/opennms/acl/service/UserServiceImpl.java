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
package org.opennms.acl.service;

import java.util.List;

import org.opennms.netmgt.EventConstants;
import org.opennms.netmgt.Pager;
import org.opennms.netmgt.dao.GroupDao;
import org.opennms.netmgt.dao.UserDao;
import org.opennms.netmgt.model.OnmsUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {


    @Transactional(readOnly=true)
    public List<OnmsUser> getUsers(Pager pager) {
        return userRepository.getUsers(pager);
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=DataAccessException.class)
    public void enableUser(String username) {
        userRepository.enableUser(username);

    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=DataAccessException.class)
    public void saveUserAndAuthorities(OnmsUser user) {
        groupRepository.deleteUserGroups(user.getUsername());
        if(user.getItems().size() > 0){
            groupRepository.saveGroups(user.getUsername(), user.getItems());
        }
        userRepository.save(user);
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=DataAccessException.class)
    public void saveUser(OnmsUser user) {
        userRepository.save(user);
    }

    @Transactional(readOnly=true)
    public OnmsUser getUserWithAuthorities(String username) {
        return userRepository.getUserWithAuthorities(username);
    }

    @Transactional(readOnly=true)
    public OnmsUser getUser(String username) {
        return userRepository.getUser(username);
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=DataAccessException.class)
    public void disableUser(String username) {
        if(!username.equals(EventConstants.ADMIN)){
            userRepository.disableUser(username);
        }
    }

    @Transactional(readOnly=true)
    public Integer getUsersNumber() {
        return userRepository.getUsersNumber();
    }

    @Transactional(readOnly=true)
    public Integer getTotalItemsNumber() {
        return userRepository.getUsersNumber();
    }

    @Transactional(readOnly=true)
    public List<OnmsUser> getDisabledUsers(Pager pager) {
        return userRepository.getDisabledUsers(pager);
    }

    @Transactional(readOnly=true)
    public List<OnmsUser> getEnabledUsers(Pager pager) {
        return userRepository.getEnabledUsers(pager);
    }


    @Autowired
    private UserDao userRepository;
    @Autowired
    private GroupDao groupRepository;
}
