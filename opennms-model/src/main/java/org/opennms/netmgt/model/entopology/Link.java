package org.opennms.netmgt.model.entopology;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a physical link between 2 network end points
 * such as an Ethernet connection or a virtual link between 2 end points
 * such as an IP address connection to a subnetwork.  Can also be used
 * to represent a network service between to service end points.
 *  
 * @author antonio
 *
 */
// FIXME this should go into its own table
public abstract class Link implements Pollable {
	
    // FIXME this should be an enum?
    public static final String INPUT_LINK_DISPLAY        = "user";
    public static final String LLDP_LINK_DISPLAY         = "lldp" ;
    public static final String CDP_LINK_DISPLAY          = "cdp" ;
    public static final String OSPF_LINK_DISPLAY         = "ospf" ;
    public static final String STP_LINK_DISPLAY          = "spanning-tree" ;
    public static final String DOT1DTPFDB_LINK_DISPLAY   = "dot1d-bridge-forwarding-table" ;
    public static final String DOT1QTPFDB_LINK_DISPLAY   = "dot1q-bridge-forwarding-table" ;
    public static final String PSEUDOBRIDGE_LINK_DISPLAY = "pseudo-bridge" ;
    public static final String PSEUDOMAC_LINK_DISPLAY    = "pseudo-mac" ;


    private Integer m_id;
    
	private Set<EndPoint> m_endpoints = new HashSet<EndPoint>(2);
	
    private Set<Integer> m_sourceNodes;
    private Date m_lastPoll;
	
	public Link(EndPoint a, EndPoint b, Integer sourceNode) {
		
		a.setLink(this);
		b.setLink(this);
		m_endpoints.add(a);
		m_endpoints.add(b);
	}
	
	public int getId() {
		return m_id;
	}

	public void setId(int id) {
		m_id = id;
	}

	public Set<EndPoint> getEndpoints() {
		return m_endpoints;
	}

	public void setEndpoints(Set<EndPoint> endpoints) {
		m_endpoints = endpoints;
	}

	public abstract String displayLinkType();

    @Override
    public Set<Integer> getSourceNodes() {
        return m_sourceNodes;
    }

    @Override
    public void setSourceNodes(Set<Integer> sourceNodes) {
        this.m_sourceNodes = sourceNodes;
    }

    @Override
    public Date getLastPoll() {
        return m_lastPoll;
    }

    @Override
    public void setLastPoll(Date lastPoll) {
        this.m_lastPoll = lastPoll;
    }
	
}
