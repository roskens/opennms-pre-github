package org.opennms.netmgt.invd;

import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.poller.NetworkInterface;

import java.util.Set;
import java.net.InetAddress;

public class ScanningClient implements NetworkInterface {
    private int m_nodeId = -1;
    private InetAddress m_inetAddress = null;
    private int m_ifIndex = -1;

    private String m_sysObjId = null;

    public int getType() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object getAddress() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object getAttribute(String property) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object setAttribute(String property, Object value) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    ScanningClient(Integer ifaceId, IpInterfaceDao ifaceDao) {

    }


    public String getHostAddress() {
        return "";
    }

    public int getNodeId() {
        return 0;
    }


    public void validateAgent() {

    }

    public String toString() {
        return "";
    }

    public InetAddress getInetAddress() {
              return null;
    }
}
