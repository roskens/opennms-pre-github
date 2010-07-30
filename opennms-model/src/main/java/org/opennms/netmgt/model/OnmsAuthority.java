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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
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
@Table(name = "authorities")
@Proxy(lazy = false)
public class OnmsAuthority implements OnmsAuthorityView {

    private OnmsGroup group;
    private String name;
    private Integer id = 0;
    private String description;
    private boolean eliminable = true;
    private Set<Integer> items;
    private Collection<OnmsAuthoritiesCategories> authoritiesCategories;
    private Set<OnmsCategory> categories;

    public OnmsAuthority() {
        items = Collections.EMPTY_SET;
        authoritiesCategories = new HashSet<OnmsAuthoritiesCategories>();
        categories = new HashSet<OnmsCategory>();
    }

    @Transient
    public boolean isNew(){
    	return id == 0;
    }

    @Transient
    public Set<Integer> getItems() {
        return items;
    }


    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authorities_seq")
    @SequenceGenerator(name = "authorities_seq", sequenceName = "authorities_id_seq")
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }


    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    public OnmsGroup getGroup() {
        return group;
    }

    @Transient
    public Collection<OnmsAuthoritiesCategories> getAuthoritiesCategories() {
        return authoritiesCategories;
    }


    @OneToMany
    @JoinTable(name="authorities_categories", joinColumns = @JoinColumn( name="authority_id"), inverseJoinColumns = @JoinColumn( name="category_id"))
    @LazyCollection(LazyCollectionOption.FALSE)
    public Set<OnmsCategory> getCategories() {
        return categories;
    }

    @Column(name = "eliminable")
    public boolean getEliminable() {
        return eliminable;
    }

    public void setEliminable(boolean eliminable) {
        this.eliminable = eliminable;
    }

    public void setCategories(Set<OnmsCategory> categories) {
        this.categories = categories;
    }

    public void setItems(Set<Integer> items) {
        this.items = items;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGroup(OnmsGroup group) {
        this.group = group;
    }

    public void setAuthoritiesCategories(
            Collection<OnmsAuthoritiesCategories> authoritiesCategories) {
        this.authoritiesCategories = authoritiesCategories;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OnmsAuthority that = (OnmsAuthority) o;

        if (description != null ? !description.equals(that.description)
                               : that.description != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result
                + (description != null ? description.hashCode() : 0);
        return result;
    }

}