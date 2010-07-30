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
package org.opennms.acl.ui.validator;

import org.opennms.acl.service.AuthorityService;
import org.opennms.netmgt.model.OnmsAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
@Component("authorityValidator")
public class AuthorityValidator implements Validator {

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return OnmsAuthority.class.isAssignableFrom(clazz);
    }

    public void validate(Object command, Errors err) {
        OnmsAuthority authority = (OnmsAuthority) command;
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "name", "name.required.value", "name is required.");
        //insert
        if(authority.getId() == 0){
            if (null != authorityService.getAuthorityByName(authority.getName())) {
                err.rejectValue("name", "error.name.already.present");
            }
        }else{
            OnmsAuthority old = authorityService.getAuthorityByName(authority.getName());
            if (!old.getName().equals(authority.getName())) {
                err.rejectValue("name", "name.wrong.value");
            }
        }
    }

    @Autowired
    private AuthorityService authorityService;
}
