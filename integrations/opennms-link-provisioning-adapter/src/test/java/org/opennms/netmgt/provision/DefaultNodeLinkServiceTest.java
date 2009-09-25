package org.opennms.netmgt.provision;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.netmgt.dao.DatabasePopulator;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.db.JUnitTemporaryDatabase;
import org.opennms.netmgt.dao.db.OpenNMSConfigurationExecutionListener;
import org.opennms.netmgt.dao.db.TemporaryDatabaseExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;


@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
    OpenNMSConfigurationExecutionListener.class,
    TemporaryDatabaseExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class
})
@ContextConfiguration(locations={
        "classpath:/META-INF/opennms/applicationContext-dao.xml",
        "classpath:/META-INF/opennms/applicationContext-databasePopulator.xml",
        "classpath:/META-INF/opennms/provisiond-extensions.xml",
        "classpath:/META-INF/opennms/linkAdapterTestContext.xml",
        "classpath:/linkTestContext.xml"
})
@JUnitTemporaryDatabase()
@Ignore
public class DefaultNodeLinkServiceTest {
    
    @Autowired 
    DatabasePopulator m_dbPopulator;
    
    @Autowired
    NodeDao m_nodeDao;
    
    
    @Autowired
    JdbcTemplate m_jdbcTemplate;
    
    @Autowired
    NodeLinkService m_nodeLinkService;
    
    @Before
    public void setup(){

    }
    
    @Test
    public void dwoNotNull(){
        assertNotNull(m_dbPopulator);
        assertNotNull(m_nodeDao);
        assertNotNull(m_jdbcTemplate);
    }
    
    @Ignore
    @Test
    public void dwoTestGetNodeLabel(){
        m_nodeLinkService = new DefaultNodeLinkService();
        String nodeLabel = m_nodeLinkService.getNodeLabel(1);
        
        assertNotNull(nodeLabel);
    }
    
    @Test
    public void dwoTestGetNodeId(){
        
    }
    
    @Test
    public void dwoTestCreateLink(){
        
    }
    
}
