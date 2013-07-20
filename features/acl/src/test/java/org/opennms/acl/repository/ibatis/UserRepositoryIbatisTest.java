/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *
 * From the original copyright headers:
 *
 * Copyright (c) 2009+ desmax74
 * Copyright (c) 2009+ The OpenNMS Group, Inc.
 *
 * This program was developed and is maintained by Rocco RIONERO
 * ("the author") and is subject to dual-copyright according to
 * the terms set in "The OpenNMS Project Contributor Agreement".
 *
 * The author can be contacted at the following email address:
 *
 *     Massimiliano Dess&igrave;
 *     desmax74@yahoo.it
 *******************************************************************************/

package org.opennms.acl.repository.ibatis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opennms.acl.SpringFactory;
import org.opennms.acl.conf.dbunit.DBAuthority;
import org.opennms.acl.conf.dbunit.DBGroup;
import org.opennms.acl.conf.dbunit.DBUser;
import org.opennms.acl.conf.dbunit.DbGroupMemeber;
import org.opennms.acl.model.Pager;
import org.opennms.acl.model.UserAuthoritiesDTO;
import org.opennms.acl.model.UserDTO;
import org.opennms.acl.model.UserView;

/**
 * The Class UserRepositoryIbatisTest.
 */
@Ignore("test database is not thread-safe, port to opennms temporary database code")
public class UserRepositoryIbatisTest {

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        repo = (UserRepositoryIbatis) SpringFactory.getXmlWebApplicationContext().getBean("userRepository");
    }

    /**
     * Tear down.
     *
     * @throws Exception
     *             the exception
     */
    @AfterClass
    public static void tearDown() throws Exception {
        repo = null;
    }

    /**
     * Prepare db.
     */
    @Before
    public void prepareDb() {
        dbUser.prepareDb();
        dbGroup.prepareDb();
        dbGroupMember.prepareDb();
        dbAuth.prepareDb();
        // dbAuthoritiesAuth.prepareDb();
    }

    /**
     * Clean db.
     */
    @After
    public void cleanDb() {
        // dbAuthoritiesAuth.cleanDb();
        dbAuth.cleanDb();
        dbGroupMember.cleanDb();
        dbGroup.cleanDb();
        dbUser.cleanDb();
    }

    /**
     * Gets the id user.
     *
     * @return the id user
     */
    @Test
    public void getIdUser() {
        assertTrue(repo.getIdUser("max").toString().equals("1"));
    }

    /**
     * Disable user.
     */
    @Test
    public void disableUser() {
        assertTrue(repo.disableUser("5"));
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    @Test
    public void getUser() {
        UserView user = repo.getUser("1");
        assertTrue(user.getUsername().equals("max"));
        assertTrue(user.isEnabled() == true);
        assertTrue(user.getId() == 1);
    }

    /**
     * Gets the user disabled.
     *
     * @return the user disabled
     */
    @Test
    public void getUserDisabled() {
        assertNull(repo.getUser("8"));
    }

    /**
     * Gets the user with authorities.
     *
     * @return the user with authorities
     */
    @Test
    public void getUserWithAuthorities() {
        UserAuthoritiesDTO user = repo.getUserWithAuthorities("max");
        assertTrue(user.getUsername().equals("max"));
        assertTrue(user.isEnabled() == true);
        assertTrue(user.getId() == 1);
        assertTrue(user.getAuthorities().size() == 5);
        assertFalse(user.isNew());
    }

    /**
     * Gets the user disablde with authorities.
     *
     * @return the user disablde with authorities
     */
    @Test
    public void getUserDisabldeWithAuthorities() {
        UserAuthoritiesDTO user = repo.getUserWithAuthorities("pluto");
        assertNull(user);
    }

    /**
     * Gets the user with out authorities.
     *
     * @return the user with out authorities
     */
    @Test
    public void getUserWithOutAuthorities() {
        UserAuthoritiesDTO user = repo.getUserWithAuthorities("pippo");
        assertTrue(user.getUsername().equals("pippo"));
        assertTrue(user.isEnabled() == true);
        assertTrue(user.getId() == 2);
        assertTrue(user.getAuthorities().size() == 0);
        assertFalse(user.isNew());
    }

    /**
     * Gets the enabled users.
     *
     * @return the enabled users
     */
    @Test
    public void getEnabledUsers() {
        assertTrue(repo.getEnabledUsers(new Pager(0, 1, 15)).size() == 3);
    }

    /**
     * Gets the disabled users.
     *
     * @return the disabled users
     */
    @Test
    public void getDisabledUsers() {
        assertTrue(repo.getDisabledUsers(new Pager(0, 1, 15)).size() == 1);
    }

    /**
     * Gets the users number.
     *
     * @return the users number
     */
    @Test
    public void getUsersNumber() {
        assertTrue(repo.getUsersNumber() == 4);
    }

    /**
     * Insert user.
     */
    @Test
    public void insertUser() {
        UserDTO user = new UserDTO();
        user.setEnabled(true);
        user.setPassword("paperoga");
        user.setUsername("paperoga");
        assertTrue(repo.insertUser(user) > 0);
    }

    /**
     * Update user.
     */
    @Test
    public void updateUser() {
        UserDTO user = new UserDTO();
        user.setEnabled(true);
        user.setId(new Long(2));
        user.setPassword("pluto");
        user.setOldPassword("pippo");
        user.setUsername("pippo");
        assertTrue(repo.updatePassword(user) == 1);
    }

    /** The db user. */
    private DBUser dbUser = new DBUser();

    /** The db auth. */
    private DBAuthority dbAuth = new DBAuthority();

    // private DBAuthoritiesAuth dbAuthoritiesAuth = new DBAuthoritiesAuth();
    /** The db group. */
    private DBGroup dbGroup = new DBGroup();

    /** The db group member. */
    private DbGroupMemeber dbGroupMember = new DbGroupMemeber();

    /** The repo. */
    private static UserRepositoryIbatis repo;
}
