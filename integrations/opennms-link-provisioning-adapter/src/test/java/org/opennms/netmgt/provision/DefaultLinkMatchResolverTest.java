package org.opennms.netmgt.provision;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Properties;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.netmgt.dao.db.OpenNMSConfigurationExecutionListener;
import org.opennms.test.mock.MockLogAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
    OpenNMSConfigurationExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class
})
@ContextConfiguration(locations={
        "classpath*:/META-INF/opennms/provisiond-extensions.xml",
        "classpath:/linkTestContext.xml"
})

@Ignore
public class DefaultLinkMatchResolverTest {
    @Autowired
    private DefaultLinkMatchResolverImpl m_resolver;

    @Before
    public void setUp() {
        Properties props = new Properties();
        props.setProperty("log4j.logger.org.opennms", "DEBUG");
        MockLogAppender.setupLogging(props);
    }
    
    @Test
    public void testSimpleMatch() {
        assertEquals("nc-ral0002-to-ral0001-dwave", m_resolver.getAssociatedEndPoint("nc-ral0001-to-ral0002-dwave"));
    }
    
    @Test
    public void testMultiplePatterns() {
        assertEquals("middle-was-bar", m_resolver.getAssociatedEndPoint("foo-bar-baz"));
        assertEquals("middle-was-now", m_resolver.getAssociatedEndPoint("before-now-after"));
        assertNull(m_resolver.getAssociatedEndPoint("after-wasn't-before"));
    }
    
    @Test
    public void testPatternsFromConfig() {
        assertEquals("middle-was-bar", m_resolver.getAssociatedEndPoint("foo-bar-baz"));
        assertEquals("middle-was-now", m_resolver.getAssociatedEndPoint("before-now-after"));
        assertNull(m_resolver.getAssociatedEndPoint("after-wasn't-before"));
    }
}
