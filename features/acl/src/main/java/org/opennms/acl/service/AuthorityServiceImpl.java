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

import org.opennms.netmgt.Pager;
import org.opennms.netmgt.dao.AuthorityDao;
import org.opennms.netmgt.model.OnmsAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
@Service("authorityService")
public class AuthorityServiceImpl implements AuthorityService {

    @Transactional(readOnly=true)
    public OnmsAuthority getAuthorityByName(String name) {
        return authorityRepository.getAuthorityByName(name);
    }

    @Transactional(readOnly=true)
    public Set<OnmsAuthority> getGroupAuthorities(Integer id) {
        return authorityRepository.getGroupAuthorities(id);
    }

    @Transactional(readOnly=true)
    public Set<OnmsAuthority> getFreeAuthoritiesForGroup() {
        return authorityRepository.getFreeAuthoritiesForGroup();
    }

    @Transactional(readOnly=true)
    public Set<OnmsAuthority> getAuthorities() {
        return authorityRepository.getAuthorities();
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=DataAccessException.class)
    public void insertGroupAuthorities(Integer id, Set<Integer> authorities) {
        authorityRepository.removeGroupFromAuthorities(id);
        authorityRepository.saveAuthorities(id, authorities);
    }

    @Transactional(readOnly=true)
    public Set<OnmsAuthority> getFreeAuthorities(String username) {
        return authorityRepository.getFreeAuthorities(username);
    }

    @Transactional(readOnly=true)
    public Set<OnmsAuthority> getUserAuthorities(String username) {
        return authorityRepository.getUserAuthorities(username);
    }

    public Integer getTotalItemsNumber() {
        return getAuthoritiesNumber();
    }

    @Transactional(readOnly=true)
    public OnmsAuthority getAuthority(Integer id) {
        return authorityRepository.getAuthority(id);
    }

    @Transactional(readOnly=true)
    public Set<OnmsAuthority> getAuthorities(Pager pager) {
        return authorityRepository.getAuthorities(pager);
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=DataAccessException.class)
    public void removeAuthority(Integer id) {
        authorityRepository.removeAuthority(id);
    }

    @Transactional(propagation=Propagation.REQUIRED, rollbackFor=DataAccessException.class)
    public void save(OnmsAuthority authority) {
        authorityRepository.save(authority);
    }

    @Transactional(readOnly=true)
    public Integer getAuthoritiesNumber() {
        return authorityRepository.getAuthoritiesNumber();
    }

    public Set<Integer> getIdItemsAuthority(Integer id) {
        return authorityRepository.getIdItemsAuthority(id);
    }

    @Autowired
    public void setAuthorityRepository(AuthorityDao authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    private AuthorityDao authorityRepository;
}