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

import org.opennms.netmgt.model.OnmsCategory;


/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.5.0
 */
@Entity
@Table(schema = "public", name = "authorities_categories")
public class OnmsAuthoritiesCategories {

    private Integer authorityId;
    private OnmsAuthority authority;
    private Integer id;
    private Integer categoryId;
    private OnmsCategory category;


    @Column(name = "authority_id")
    public Integer getAuthorityId() {
        return authorityId;
    }


    @Column(name = "category_id")
    public Integer getCategoryId() {
        return categoryId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authorities_categories_seq")
    @SequenceGenerator(name = "authorities_categories_seq", sequenceName = "authorities_categories_id_seq")
    @Column(name = "id")
    public Integer getId() {
        return id;
    }


    @ManyToOne
    @JoinColumn(name = "authority_id", referencedColumnName = "id", nullable = false,insertable=false, updatable=false)
    public OnmsAuthority getAuthority() {
        return authority;
    }


    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "categoryid", nullable = false,insertable=false, updatable=false)
    public OnmsCategory getCategory() {
        return category;
    }

  /*  @Autowired()
    public void setCategory(@Qualifier("categoryDao")OnmsCategory category) {
        this.category = category;
    }*/

    public void setCategory(OnmsCategory category) {
        this.category = category;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }


    public void setAuthority(OnmsAuthority authority) {
        this.authority = authority;
    }


    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OnmsAuthoritiesCategories that = (OnmsAuthoritiesCategories) o;

        if (authorityId != null ? !authorityId.equals(that.authorityId)
                               : that.authorityId != null)
            return false;
        if (categoryId != null ? !categoryId.equals(that.categoryId)
                              : that.categoryId != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;

        return true;
    }


    @Override
    public int hashCode() {
        int result = authorityId != null ? authorityId.hashCode() : 0;
        result = 34 * result + (id != null ? id.hashCode() : 0);
        result = 34 * result
                + (categoryId != null ? categoryId.hashCode() : 0);
        return result;
    }
}
