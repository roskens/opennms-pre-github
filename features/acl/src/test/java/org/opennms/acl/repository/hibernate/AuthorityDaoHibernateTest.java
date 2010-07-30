package org.opennms.acl.repository.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opennms.acl.SpringFactory;
import org.opennms.acl.conf.dbunit.DBAuthority;
import org.opennms.acl.conf.dbunit.DBGroup;
import org.opennms.acl.conf.dbunit.DBUser;
import org.opennms.acl.conf.dbunit.DbGroupMemeber;
import org.opennms.netmgt.Pager;
import org.opennms.netmgt.dao.AuthorityDao;
import org.opennms.netmgt.model.OnmsAuthority;

public class AuthorityDaoHibernateTest {

    @BeforeClass
    public static void setUp() throws Exception {
        repo = (AuthorityDao) SpringFactory.getXmlWebApplicationContext().getBean("authorityRepository",AuthorityDao.class);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        repo = null;
    }

    @Before
    public void prepareDb() {
        dbUser.prepareDb();
        dbGroup.prepareDb();
        dbGroupMember.prepareDb();
        dbAuth.prepareDb();
    }

    @After
    public void cleanDb() {
        dbAuth.cleanDb();
        dbGroupMember.cleanDb();
        dbGroup.cleanDb();
        dbUser.cleanDb();
    }

    @Test
    public void getAllAuthorities() {
        assertTrue(repo.getAuthorities().size() == 16);
    }

    @Test
    public void getAuthority() {
        OnmsAuthority authority = repo.getAuthority(19);
        assertNotNull(authority);
        assertTrue(authority.getName().equals("quattro"));
        authority = repo.getAuthority(30);
        assertTrue(authority.getName().equals("quindici"));
    }

    @Test
    public void getAuthoritiesNumber() {
        assertTrue(repo.getAuthorities().size() == 16);
    }

    @Test
    public void removeAuthority() {
        repo.removeAuthority(19);
    }

    @Test
    public void getFreeAuthoritiesForGroup() {
        assertTrue(repo.getFreeAuthoritiesForGroup().size() == 4);
    }

    @Test
    public void getFreeAuthorities() {
        assertTrue(repo.getFreeAuthorities("paperone").size() == 16);
        assertTrue(repo.getFreeAuthorities("max").size() == 11);
    }

    @Test
    public void getUserAuthorities() {
        assertTrue(repo.getFreeAuthorities("paperone").size() == 16);
        assertTrue(repo.getUserAuthorities("max").size() == 5);
    }

    @Test
    public void saveAuthorities() {
        Set<Integer> authorities = new HashSet<Integer>();
        authorities.add(14);
        authorities.add(16);
        repo.saveAuthorities(1, authorities);
    }

    @Test
    public void deleteUserGroups() {
        repo.deleteUserGroups("max");
    }

    @Test
    public void removeAuthorityFromGroups() {
        repo.removeGroupFromAuthorities(14);
    }

    @Test
    public void getAuthoritiesPaging() {
        Pager pager = new Pager(0, 1, 15);
        assertTrue(repo.getAuthorities(pager).size() == 15);
        pager = new Pager(1, 1, 15);
        assertTrue(repo.getAuthorities(pager).size() == 1);
    }

    @Test
    public void getAuthoritiesGroup() {
        assertTrue(repo.getGroupAuthorities(1).size() == 4);
        assertTrue(repo.getGroupAuthorities(2).size() == 1);
    }

    @Test
    public void saveAuthority() {
        OnmsAuthority auth = new OnmsAuthority();
        auth.setName("testAuth");
        repo.save(auth);
    }

    private DBUser dbUser = new DBUser();
    private DBAuthority dbAuth = new DBAuthority();
    private DBGroup dbGroup = new DBGroup();
    private DbGroupMemeber dbGroupMember = new DbGroupMemeber();
    private static AuthorityDao repo;
}
