package org.opennms.netmgt.model.entopology;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * An endpoint is a destination in the network such as a physical port on a
 * device, an IP address or a tcp port. The type of the endpoint depends on
 * the protocol the information for the topology comes from.
 * 
 * @author Antonio
 */
// FIXME this should go into its own table
@MappedSuperclass
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="Discriminator")
public abstract class EndPoint implements Pollable {

	//FIXME change to enum
    public static final String INPUT_LINK_DISPLAY        = "user";
    public static final String LLDP_LINK_DISPLAY         = "lldp" ;
    public static final String CDP_LINK_DISPLAY          = "cdp" ;
    public static final String OSPF_LINK_DISPLAY         = "ospf" ;
    public static final String STP_LINK_DISPLAY          = "spanning-tree" ;
    public static final String DOT1DTPFDB_LINK_DISPLAY   = "dot1d-bridge-forwarding-table" ;
    public static final String DOT1QTPFDB_LINK_DISPLAY   = "dot1q-bridge-forwarding-table" ;
    public static final String PSEUDOBRIDGE_LINK_DISPLAY = "pseudo-bridge" ;
    public static final String PSEUDOMAC_LINK_DISPLAY    = "pseudo-mac" ;

    @Id
    protected Long m_id;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date m_lastPoll;
    
    // FIXME
    @Transient
    protected Set<Integer> m_sourceNodes;

    /**
     * The Element to which the End Point 
     * belongs
     *  
     */
    @ManyToOne
    private ElementIdentifier m_elementidentifier;

    /**
     * An endpoint can have a link to another endpoint.
     * 
     */
    @OneToOne
    private EndPoint m_linkedEndpoint;

    private Integer m_nodeId;

    public Integer getNodeId() {
		return m_nodeId;
	}


	public void setNodeId(Integer nodeId) {
		m_nodeId = nodeId;
	}

	// FIXME are all the if* properties duplicated information form onms-interface?
    /**
     * The ifindex of the endpoint
     * could be null
     */
    @Column(name="InterfaceIndex")
    private Integer m_ifIndex;

    /**
     * The  ifName of the endPoint
     * could be null
     */
    @Column(name="InterfaceName")
    private String m_ifName;
    /**
     * The ifDescr of the endPoint
     * could be null
     */
    @Column(name="InterfaceDescription")
    private String m_ifDescr;
    
    /**
     * The ifAlias of the endPoint
     * could be null
     */
    @Column(name="InterfaceAlias")
    private String m_ifAlias;
    
    
	public EndPoint(Integer sourceNode) {
		m_sourceNodes.add(sourceNode);
	}


	public Long getId() {
		return m_id;
	}

	protected void setId(Long id) {
		m_id = id;
	}


	public ElementIdentifier getElementIdentifier() {
		return m_elementidentifier;
	}

	public void setElementIdentifier(ElementIdentifier device) {
		m_elementidentifier = device;
	}
	

    public EndPoint getLinkedEndpoint() {
        return m_linkedEndpoint;
    }


    public void setLinkedEndpoint(EndPoint linkedEndpoint) {
        m_linkedEndpoint = linkedEndpoint;
    }

	public String getIfDescr() {
		return m_ifDescr;
	}

	public void setIfDescr(String ifDescr) {
		m_ifDescr = ifDescr;
	}

	public String getIfAlias() {
		return m_ifAlias;
	}

	public void setIfAlias(String ifAlias) {
		m_ifAlias = ifAlias;
	}
	
	public Integer getIfIndex() {
		return m_ifIndex;
	}

	public void setIfIndex(Integer ifIndex) {
		m_ifIndex = ifIndex;
	}

	public String getIfName() {
		return m_ifName;
	}

	public void setIfName(String ifName) {
		m_ifName = ifName;
	}

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
	
	public abstract String displayLinkType();

}
