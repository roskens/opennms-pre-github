package org.opennms.netmgt.collectd;

import java.net.InetAddress;
import java.util.Set;

import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.snmp.SnmpAgentConfig;

public class MockCollectionAgent implements CollectionAgent {
    
    private int nodeid;
    private String ipaddress;
    
    public MockCollectionAgent(int nodeid, String ipaddress) {
        super();
        this.nodeid = nodeid;
        this.ipaddress = ipaddress;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public InetAddress getAddress() {
        return InetAddressUtils.addr(getHostAddress());
    }

    @Override
    public <V> V getAttribute(String property) {
        return null;
    }

    @Override
    public Object setAttribute(String property, Object value) {
        return null;
    }

    @Override
    public String getSnmpInterfaceLabel(int ifIndex) {
        return null;
    }

    @Override
    public String getHostAddress() {
        return ipaddress;
    }

    @Override
    public void setSavedIfCount(int ifCount) {
    }

    @Override
    public int getSavedIfCount() {
        return 0;
    }

    @Override
    public int getNodeId() {
        return nodeid;
    }

    @Override
    public String getSysObjectId() {
        return null;
    }

    @Override
    public void validateAgent() throws CollectionInitializationException {
    }

    @Override
    public SnmpAgentConfig getAgentConfig() {
        return null;
    }

    @Override
    public Set<IfInfo> getSnmpInterfaceInfo(IfResourceType type) {
        return null;
    }

    @Override
    public InetAddress getInetAddress() {
        return getAddress();
    }

    @Override
    public long getSavedSysUpTime() {
        return 0;
    }

    @Override
    public void setSavedSysUpTime(long sysUpTime) {
    }    
}
