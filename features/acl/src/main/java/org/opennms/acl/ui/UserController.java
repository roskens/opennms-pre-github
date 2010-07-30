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
package org.opennms.acl.ui;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.opennms.acl.domain.GenericUser;
import org.opennms.acl.exception.UserNotfoundException;
import org.opennms.acl.service.UserService;
import org.opennms.acl.ui.util.WebUtils;
import org.opennms.acl.util.Constants;
import org.opennms.netmgt.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * User Controller
 *
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since 1.9.0
 */
@Controller
public class UserController {

    @RequestMapping("/user.authorities.page")
    public ModelAndView authorities(HttpServletRequest req) throws Exception {
        GenericUser user = WebUtils.getUser(req);
        return new ModelAndView("acl/user/authorities", Constants.UI_USER, user.getUserView());
    }

    @RequestMapping("/user.list.page")
    public ModelAndView list(HttpServletRequest req) throws Exception {
        Pager pager = WebUtils.getPager(req, userService.getTotalItemsNumber(), 15);
        return new ModelAndView("acl/user/list", Constants.UI_USERS, userService.getUsers(pager));
    }

    @RequestMapping("/user.disable.page")
    public ModelAndView disable(@RequestParam("sid") String username, HttpServletRequest req) throws Exception {
        userService.disableUser(username);
        Pager pager = WebUtils.getPager(req, userService.getTotalItemsNumber(), 15);
        return new ModelAndView("acl/user/list", Constants.UI_USERS, userService.getUsers(pager));
    }

    @RequestMapping("/user.enable.page")
    public ModelAndView enable(@RequestParam("sid") String username, HttpServletRequest req) throws Exception {
        userService.enableUser(username);
        Pager pager = WebUtils.getPager(req, userService.getTotalItemsNumber(), 15);
        return new ModelAndView("acl/user/list", Constants.UI_USERS, userService.getUsers(pager));
    }

    @RequestMapping("/user.detail.page")
    public ModelAndView detail(HttpServletRequest req) throws Exception {
        GenericUser user = WebUtils.getUser(req);
        if (user != null) {
            ModelAndView mav = new ModelAndView("acl/user/detail");
            mav.addObject(Constants.UI_USER, user.getUserView());
            mav.addObject(Constants.UI_USER_GROUPS, user.getGroups());
            mav.addObject(Constants.GROUPS, user.getFreeGroups());
            return mav;
        } else {
            throw new UserNotfoundException("id not found");
        }
    }

    @RequestMapping("/user.selection.page")
    public ModelAndView selection(@RequestParam("includedHidden") String ids, HttpServletRequest req) throws Exception {
        GenericUser user = WebUtils.getUser(req);
        if (user != null && ids != null && ids.length() > 0) {
            user.setNewGroups(WebUtils.extractIdGrantedAuthorityFromString(ids, Constants.COMMA));
        } else {
            user.setNewGroups(new HashSet<Integer>());
        }
        user.save();
        return new ModelAndView(new StringBuilder(Constants.REDIRECT_USER_AUTHORITIES).append("?").append(Constants.USER_SID).append("=").append(user.getUsername()).toString());
    }

    @Autowired
    private UserService userService;
}
