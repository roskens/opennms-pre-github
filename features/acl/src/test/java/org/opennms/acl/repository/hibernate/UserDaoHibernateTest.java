package org.opennms.acl.repository.hibernate;

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
import org.opennms.acl.conf.dbunit.DbGroupMemeber;
import org.opennms.netmgt.Pager;
import org.opennms.netmgt.dao.UserDao;
import org.opennms.netmgt.model.OnmsUser;

public class UserDaoHibernateTest {

    @BeforeClass
    public static void setUp() throws Exception {
        repo = (UserDao) SpringFactory.getXmlWebApplicationContext().getBean("userRepository",UserDao.class);
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
    public void save() {
        OnmsUser user = new OnmsUser();
        user.setEnabled(true);
        user.setPassword("paperoga");
        user.setUsername("paperoga");
        user.setName("paperoga");
        repo.save(user);
    }
    
    @Test
    public void disableUser() {
        repo.disableUser("max");
    }
    
    @Test
    public void getDisabledUsers() {
        assertTrue(repo.getDisabledUsers(new Pager(0, 1, 15)).size() == 1);
    }
    
    @Test
    public void getEnabledUsers() {
        assertTrue(repo.getEnabledUsers(new Pager(0, 1, 15)).size() == 3);
    }
    
    @Test
    public void getUsersNumber() {
        assertTrue(repo.getUsersNumber() == 4);
    }
    
    @Test
    public void getUser() {
        OnmsUser user = repo.getUser("max");
        assertTrue(user.getUsername().equals("max"));
        assertTrue(user.getEnabled() == true);
        assertTrue(user.getPassword().equals("0706025b2bbcec1ed8d64822f4eccd96314938d0"));
    }
    
    @Test
    public void getUserWithAuthorities() {
        OnmsUser user = repo.getUserWithAuthorities("max");
        assertTrue(user.getUsername().equals("max"));
        assertTrue(user.getEnabled() == true);
        assertTrue(user.getAuthorities().size() == 5);
 
    }
    
    private DBUser dbUser = new DBUser();
    private DBAuthority dbAuth = new DBAuthority();
    private DBGroup dbGroup = new DBGroup();
    private DbGroupMemeber dbGroupMember = new DbGroupMemeber();
    private static UserDao repo;
}
