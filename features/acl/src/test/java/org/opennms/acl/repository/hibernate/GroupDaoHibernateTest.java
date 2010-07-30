package org.opennms.acl.repository.hibernate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
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
import org.opennms.netmgt.dao.GroupDao;
import org.opennms.netmgt.model.OnmsGroup;

public class GroupDaoHibernateTest {


    @BeforeClass
    public static void setUp() throws Exception {
        repo = (GroupDao) SpringFactory.getXmlWebApplicationContext().getBean("groupRepository", GroupDao.class);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        repo = null;
    }

    @Before
    public void prepareDb() {
        dbUser.prepareDb();
        dbGroup.prepareDb();
        dbAuth.prepareDb();
        dbGroupMember.prepareDb();
    }

    @After
    public void cleanDb() {
        dbGroupMember.cleanDb();
        dbAuth.cleanDb();
        dbGroup.cleanDb();
        dbUser.cleanDb();
    }

    @Test
    public void saveGroups() {
        dbGroupMember.cleanDb();

        Set<Integer> groups = new HashSet<Integer>();
        groups.add(1);
        groups.add(2);
        repo.saveGroups("paperone", groups);
    }



    @Test
    public void saveGroup() {
        OnmsGroup group = new OnmsGroup();
        group.setName("GROUP_USER");
        repo.save(group);
    }

    @Test
    public void getGroup() {
        assertNotNull(repo.getGroup(1));
    }

    @Test
    public void getGroupsPaging() {
        Pager pager = new Pager(0, 3, 15);
        assertTrue(repo.getGroups().size() == 4);
        assertTrue(repo.getGroups(pager).size() == 3);
    }

    @Test
    public void getGroups() {
        assertTrue(repo.getGroups().size() == 4);
    }

   @Test
    public void getGroupNumber() {
        assertNotNull(repo.getGroupsNumber() == 3);
    }

    @Test
    public void getFreeGroups() {
        assertNotNull(repo.getFreeGroups("paperone"));
        assertTrue(repo.getFreeGroups("paperone").size() == 3);
        assertTrue(repo.getFreeGroups("max").size() == 2);
    }

    @Test
    public void getUserGroups() {
        assertTrue(repo.getUserGroups("paperone").size() == 0);
        assertTrue(repo.getUserGroups("max").size() == 2);
    }

    @Test
    public void deleteUserGroups() {
        assertTrue(repo.getUserGroups("max").size() == 2);
        repo.deleteUserGroups("max");
        assertTrue(repo.getUserGroups("max").size() == 0);
    }

    @Test
    public void getUserGroupsAuthorities() {
        Set<OnmsGroup> groups = repo.getUserGroupsWithAutorities("max");
        assertTrue(groups.size() == 2);
        Iterator<OnmsGroup> iterator = groups.iterator();
        while(iterator.hasNext()){
            OnmsGroup group = iterator.next();
            if(group.getName().equals("GROUP_ADMIN"))
               assertTrue(group.getAuthorities().size() == 1);
            if(group.getName().equals("GROUP_USER"))
                assertTrue(group.getAuthorities().size() == 1);
        }
    }

    @Test
    public void deleteGroup() {
        repo.removeGroup(3);
    }

    @Test
    public void hasUser() {
         assertTrue(repo.hasUsers(1));
         assertTrue(repo.hasUsers(2));
         assertTrue(!repo.hasUsers(3));
    }

    private DBAuthority dbAuth = new DBAuthority();
    private DBUser dbUser = new DBUser();
    private DBGroup dbGroup = new DBGroup();
    private DbGroupMemeber dbGroupMember = new DbGroupMemeber();
    private static GroupDao repo;
}
