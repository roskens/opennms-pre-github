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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Proxy;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.5.0
 */
@Entity
@Table(schema = "public", name = "groups")
@Proxy(lazy = false)
public class OnmsGroup implements OnmsGroupView {

    private String name;
    private Integer id = 0;
    private Collection<OnmsGroupMembers> groupMembers;
    private Set<OnmsAuthority> authorities;
    private boolean eliminable = true;
    private static final Integer defaultId = 1;

    public OnmsGroup() {
        groupMembers = new HashSet<OnmsGroupMembers>();
        authorities = new HashSet<OnmsAuthority>();
    }

    public static final Integer getDefaultId(){
    	return defaultId;
    }

    @Transient
    public boolean isNew(){
    	return id == 0;
    }

    @OneToMany(mappedBy = "group")
    @LazyCollection(LazyCollectionOption.FALSE)
    public Set<OnmsAuthority> getAuthorities() {
        return authorities;
    }

    @Column(name = "group_name")
    public String getName() {
        return name;
    }

    @Column(name = "eliminable")
    public Boolean getEliminable() {
        return eliminable;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "groups_seq")
    @SequenceGenerator(name = "groups_seq", sequenceName = "groups_id_seq")
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    @OneToMany(mappedBy = "group")
    @LazyCollection(LazyCollectionOption.FALSE)
    public Collection<OnmsGroupMembers> getGroupMembers() {
        return groupMembers;
    }

    @Transient
    public boolean getEmptyUsers() {
        return groupMembers.size() > 0;
    }

    public void setGroupMembers(Collection<OnmsGroupMembers> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void setEliminable(boolean eliminable) {
        this.eliminable = eliminable;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAuthorities(Collection<OnmsAuthority> authorities) {
        this.authorities = (Set<OnmsAuthority>) authorities;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OnmsGroup groups = (OnmsGroup) o;

        if (name != null ? !name.equals(groups.name) : groups.name != null)
            return false;
        if (id != null ? !id.equals(groups.id) : groups.id != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 32 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

}
