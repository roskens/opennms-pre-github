package org.opennms.netmgt.linkd;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.dao.FilterDao;
import org.opennms.netmgt.filter.FilterParseException;
import org.opennms.netmgt.model.EntityVisitor;

public class MockFilterDao implements FilterDao {

    private List<InetAddress> m_activeAddresses = Collections.singletonList(InetAddressUtils.addr("192.168.1.10"));

    @Override
    public void walkMatchingNodes(final String rule, final EntityVisitor visitor) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public SortedMap<Integer, String> getNodeMap(final String rule) throws FilterParseException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public Map<InetAddress, Set<String>> getIPAddressServiceMap(final String rule) throws FilterParseException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public List<InetAddress> getActiveIPAddressList(final String rule) throws FilterParseException {
        return m_activeAddresses;
    }

    @Override
    public List<InetAddress> getIPAddressList(final String rule) throws FilterParseException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public boolean isValid(final String addr, final String rule) throws FilterParseException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public boolean isRuleMatching(final String rule) throws FilterParseException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Override
    public void validateRule(final String rule) throws FilterParseException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

}
