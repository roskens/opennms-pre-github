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
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Proxy;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.5.0
 */
@Entity
@Table(name = "authentication")
@Proxy(lazy = false)
public class OnmsUser implements OnmsUserView {

    private String username;
    private String password;
    private boolean enabled;
    private String fullname;
    private String newPassword;
    private String confirmNewPassword;
	private String comments;
    private String email;
    private String numsvc;
    private String numpin;
    private String txtsvc;
    private String txtpin;

    private Set<OnmsAuthority> authorities;
    private Set<Integer> items;
    private Collection<OnmsGroupMembers> groups;
    boolean isNew;

    public OnmsUser() {
        authorities = new HashSet<OnmsAuthority>();
        items = new HashSet<Integer>();
        groups = new HashSet<OnmsGroupMembers>();
    }

    public OnmsUser(boolean isNew) {
        authorities = new HashSet<OnmsAuthority>();
        items = new HashSet<Integer>();
        groups = new HashSet<OnmsGroupMembers>();
        this.isNew = isNew;
    }

    @Transient
    public boolean getNew() {
        return isNew;
    }

    @Transient
    public Set<Integer> getItems() {
        return items;
    }

    @Transient
    public Set<OnmsAuthority> getAuthorities() {
        return authorities;
    }

    @Column(name = "enabled")
    public boolean getEnabled() {
        return enabled;
    }

    @Column(name = "fullname")
    public String getFullname() {
        return fullname;
    }

    @Column(name = "comments")
    public String getComments() {
        return comments;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @Column(name = "numsvc")
    public String getNumsvc() {
        return numsvc;
    }

    @Column(name = "numpin")
    public String getNumpin() {
        return numpin;
    }

    @Column(name = "txtsvc")
    public String getTxtsvc() {
        return txtsvc;
    }

    @Column(name = "txtpin")
    public String getTxtpin() {
        return txtpin;
    }

    @Transient
    public String getNewPassword() {
        return newPassword;
    }

    @Transient
    public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

    @Id
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    @OneToMany(mappedBy = "user")
    public Collection<OnmsGroupMembers> getGroups() {
        return groups;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumsvc(String numsvc) {
        this.numsvc = numsvc;
    }

    public void setNumpin(String numpin) {
        this.numpin = numpin;
    }

    public void setTxtsvc(String txtsvc) {
        this.txtsvc = txtsvc;
    }

    public void setTxtpin(String txtpin) {
        this.txtpin = txtpin;
    }

    public void setGroups(Collection<OnmsGroupMembers> groupMembersByUsername) {
        this.groups = groupMembersByUsername;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

    public void setAuthorities(Set<OnmsAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setItems(Set<Integer> items) {
        this.items = items;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setName(String name) {
        this.fullname = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        OnmsUser that = (OnmsUser) o;

        if (enabled == that.enabled)
            return false;
        if (fullname != null ? !fullname.equals(that.fullname)
                            : that.fullname != null)
            return false;
        if (password != null ? !password.equals(that.password)
                            : that.password != null)
            return false;
        if (username != null ? !username.equals(that.username)
                            : that.username != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 33 * result + (password != null ? password.hashCode() : 0);
        result = 33 * result + (fullname != null ? fullname.hashCode() : 0);
        return result;
    }

}
