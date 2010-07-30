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
package org.opennms.netmgt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.5.0
 */
@Entity
@Table(schema = "public", name = "group_members")
@Proxy(lazy = false)
public class OnmsGroupMembers {

    private Long id;
    private OnmsGroup group;
    private OnmsUser user;

    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_members_seq")
    @SequenceGenerator(name = "group_members_seq", sequenceName = "group_members_id_seq")
    public Long getId() {
        return id;
    }

    
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    public OnmsUser getUser() {
        return user;
    }

    
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    public OnmsGroup getGroup() {
        return group;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public void setUser(OnmsUser authenticationByUsername) {
        this.user = authenticationByUsername;
    }

    
    public void setGroup(OnmsGroup groupsByGroupId) {
        this.group = groupsByGroupId;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OnmsGroupMembers that = (OnmsGroupMembers) o;

        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;

        return true;
    }

    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

