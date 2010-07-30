package org.opennms.acl.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opennms.acl.SpringFactory;
import org.opennms.acl.conf.dbunit.DBAuthority;
import org.opennms.acl.conf.dbunit.DBGroup;
import org.opennms.acl.conf.dbunit.DBUser;

public class UserFactoryTest {

    @BeforeClass
    public static void setUp() throws Exception {
        factory = (AclUserFactory) SpringFactory.getXmlWebApplicationContext().getBean("aclUserFactory");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        factory = null;
    }

    @Before
    public void prepareDb() {
        dbGroup.prepareDb();
        dbUser.prepareDb();
        dbAuth.prepareDb();
    }

    @After
    public void cleanDb() {
        dbAuth.cleanDb();
        dbUser.cleanDb();
        dbGroup.cleanDb();
    }

    @Test
    public void getUserByIDWithAuthorities() {

        assertTrue(factory.getAclUserByUsername("max").getUsername().equals("max"));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getUserDisabledByIDWithAuthorities() {

        factory.getAclUserByUsername(null);
    }

    @Test
    public void getUserByIDWithOutAuthorities() {

        assertTrue(factory.getAclUserByUsername("pippo").getUsername().equals("pippo"));
    }

    @Test
    public void getUserByUsernameWithAuthorities() {

        assertNotNull(factory.getAclUserByUsername("max"));
        assertTrue(factory.getAclUserByUsername("max").getUsername().equals("max"));
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void getUserDisabldeByUsernameWithAuthorities() {

        factory.getAclUserByUsername(null);
    }

    private DBUser dbUser = new DBUser();
    private DBAuthority dbAuth = new DBAuthority();
    private DBGroup dbGroup = new DBGroup();
    private static AclUserFactory factory;
}
