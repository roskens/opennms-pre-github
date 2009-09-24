package org.opennms.netmgt.provision;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.opennms.netmgt.provision.config.linkadapter.LinkPattern;

public class DefaultLinkMatchResolverTest {
    private DefaultLinkMatchResolverImpl m_resolver;

    @Before
    public void setUp() {
        m_resolver = new DefaultLinkMatchResolverImpl();
    }
    
    @Test
    public void testSimpleMatch() {
        m_resolver.addPattern(new LinkPattern("([a-z]{2})-([a-z]{3})([0-9]{4})-to-([a-z]{3})([0-9]{4})-dwave", "$1-$4$5-to-$2$3-dwave"));
        assertEquals("nc-ral0002-to-ral0001-dwave", m_resolver.getAssociatedEndPoint("nc-ral0001-to-ral0002-dwave"));
    }
    
    @Test
    public void testMultiplePatterns() {
        m_resolver.addPattern(new LinkPattern("before-(.*?)-after", "middle-was-$1"));
        m_resolver.addPattern(new LinkPattern("foo-(.*?)-baz", "middle-was-$1"));
        
        assertEquals("middle-was-bar", m_resolver.getAssociatedEndPoint("foo-bar-baz"));
        assertEquals("middle-was-now", m_resolver.getAssociatedEndPoint("before-now-after"));
        assertNull(m_resolver.getAssociatedEndPoint("after-wasn't-before"));
    }
}
