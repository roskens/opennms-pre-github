package org.opennms.netmgt.model.topology;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * This class represents a physical link between 2 network end points
 * such as an Ethernet connection or a virtual link between 2 end points
 * such as an IP address connection to a subnetwork.  Can also be used
 * to represent a network service between to service end points.
 *  
 * @author antonio
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Link extends Pollable {
	

    public static final String INPUT_LINK_DISPLAY        = "user";
    public static final String LLDP_LINK_DISPLAY         = "lldp" ;
    public static final String CDP_LINK_DISPLAY          = "cdp" ;
    public static final String OSPF_LINK_DISPLAY         = "ospf" ;
    public static final String STP_LINK_DISPLAY          = "spanning-tree" ;
    public static final String DOT1DTPFDB_LINK_DISPLAY   = "dot1d-bridge-forwarding-table" ;
    public static final String DOT1QTPFDB_LINK_DISPLAY   = "dot1q-bridge-forwarding-table" ;
    public static final String PSEUDOBRIDGE_LINK_DISPLAY = "pseudo-bridge" ;
    public static final String PSEUDOMAC_LINK_DISPLAY    = "pseudo-mac" ;


	private EndPoint m_a;
	
	private EndPoint m_b;
		
	private Integer m_id;
	
	public Link(EndPoint a, EndPoint b, Integer sourceNode) {
		super(sourceNode);
		a.setLink(this);
		b.setLink(this);
		setA(a);
		setB(b);
	}
	
	@Id
    @SequenceGenerator(name="opennmsSequence", sequenceName="opennmsNxtId")
    @GeneratedValue(generator="opennmsSequence")
	public int getId() {
		return m_id;
	}

	public void setId(int id) {
		m_id = id;
	}

    @OneToOne(mappedBy="link", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	public EndPoint getA() {
		return m_a;
	}

	public void setA(EndPoint a) {
		this.m_a = a;
	}

    @OneToOne(mappedBy="link", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	public EndPoint getB() {
		return m_b;
	}

	public void setB(EndPoint b) {
		this.m_b = b;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Link) {
			Link a = (Link)o;
			if ((a.getA().equals(getA()) && a.getB().equals(getB()))
					|| (a.getA().equals(getB()) && a.getB().equals(getA())))
				return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("A", getA())
		.append("B", getB())
		.append("lastPoll", m_lastPoll)
		.append("sourceNode", m_sourceNode)
		.toString();
	}
	
	public abstract String displayLinkType();
	
}
