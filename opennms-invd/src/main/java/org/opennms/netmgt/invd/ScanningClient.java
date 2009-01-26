package org.opennms.netmgt.invd;

import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.poller.NetworkInterface;
import org.opennms.netmgt.poller.IPv4NetworkInterface;
import org.opennms.netmgt.model.OnmsIpInterface;

import java.util.Set;
import java.net.InetAddress;

public class ScanningClient extends IPv4NetworkInterface {

    private int m_nodeId = -1;
    private InetAddress m_inetAddress = null;

    private Integer m_ifaceId;
    private IpInterfaceDao m_ifaceDao;

    public Object getAddress() {
        return getInetAddress();
    }

    public ScanningClient(Integer ifaceId, IpInterfaceDao ifaceDao) {
        super(null);
        
        m_ifaceDao = ifaceDao;
        m_ifaceId = ifaceId;

    }

    public String getHostAddress() {
        return getInetAddress().getHostAddress();
    }

    public int getNodeId() {
        if (m_nodeId == -1) {
            m_nodeId = getIpInterface().getNode().getId() == null ? -1 : getIpInterface().getNode().getId().intValue();;
        }
        return m_nodeId;
    }

    public void validateAgent() {
        // Not sure if there is anything to do here.
    }

    public String toString() {
        return "Agent[nodeid = "+getNodeId()+" ipaddr= "+getHostAddress()+']';
    }

    public InetAddress getInetAddress() {
        if (m_inetAddress == null) {
            m_inetAddress = getIpInterface().getInetAddress();
        }
        return m_inetAddress;
    }

    OnmsIpInterface getIpInterface() {
        return m_ifaceDao.load(m_ifaceId);
    }
}
