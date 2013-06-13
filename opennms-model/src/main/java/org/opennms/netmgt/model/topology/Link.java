package org.opennms.netmgt.model.topology;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

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
	
		@Embeddable
		public final static class LinkType extends AbstractType 
		implements Serializable {

			private static final long serialVersionUID = 7220152765747623134L;

			public static final int LINK_TYPE_INPUT         = 0;
			public static final int LINK_TYPE_LLDP          = 1;
			public static final int LINK_TYPE_CDP           = 2;
			public static final int LINK_TYPE_OSPF          = 3;
			public static final int LINK_TYPE_DOT1D_STP     = 4;
			public static final int LINK_TYPE_DOT1D_TP_FDB  = 5;
			public static final int LINK_TYPE_DOT1Q_TP_FDB  = 6;
			public static final int LINK_TYPE_PSEUDO_BRIDGE = 7;
			public static final int LINK_TYPE_PSEUDO_MAC = 8;
			
			public static LinkType INPUT         = new LinkType(LINK_TYPE_INPUT);
			public static LinkType LLDP          = new LinkType(LINK_TYPE_LLDP);
			public static LinkType CDP           = new LinkType(LINK_TYPE_CDP);
			public static LinkType OSPF          = new LinkType(LINK_TYPE_OSPF);
			public static LinkType DOT1D_STP     = new LinkType(LINK_TYPE_DOT1D_STP);
			public static LinkType DOT1D_TP_FDB  = new LinkType(LINK_TYPE_DOT1D_TP_FDB);
			public static LinkType DOT1Q_TP_FDB  = new LinkType(LINK_TYPE_DOT1Q_TP_FDB);
			public static LinkType PSEUDO_BRIDGE = new LinkType(LINK_TYPE_PSEUDO_BRIDGE);
			public static LinkType PSEUDO_MAC    = new LinkType(LINK_TYPE_PSEUDO_MAC);

			public LinkType(Integer linkType) {
				super(linkType);
			}
		    protected static final Map<Integer, String> s_typeMap = new HashMap<Integer, String>();
	
	        static {
	        	s_typeMap.put(0, "input" );
	        	s_typeMap.put(1, "lldp" );
	        	s_typeMap.put(2, "cdp" );
	        	s_typeMap.put(3, "ospf" );
	        	s_typeMap.put(4, "spanning-tree" );
	        	s_typeMap.put(5, "dot1d-bridge-forwarding-table" );
	        	s_typeMap.put(6, "dot1q-bridge-forwarding-table" );
	        	s_typeMap.put(7, "pseudo-bridge" );
	        	s_typeMap.put(8, "pseudo-mac" );
	        }

	        /**
	         * <p>ElementIdentifierTypeString</p>
	         *
	         * @return a {@link java.lang.String} object.
	         */
	        /**
	         */
	        public static String getTypeString(Integer code) {
	            if (s_typeMap.containsKey(code))
	                    return s_typeMap.get( code);
	            return null;
	        }


	        @Override
	        public boolean equals(Object o) {
	            if (o instanceof LinkType) {
	                return m_type.intValue() == ((LinkType)o).m_type.intValue();
	            }
	            return false;
	        }

	        public static LinkType get(Integer code) {
	            if (code == null)
	                throw new IllegalArgumentException("Cannot create LinkType from null code");
	            switch (code) {
	            case LINK_TYPE_INPUT: 		  return INPUT;
	            case LINK_TYPE_LLDP: 		  return LLDP;
	            case LINK_TYPE_CDP: 		  return CDP;
	            case LINK_TYPE_OSPF: 		  return OSPF;
	            case LINK_TYPE_DOT1D_STP:     return DOT1D_STP;
	            case LINK_TYPE_DOT1D_TP_FDB:  return DOT1D_TP_FDB;
	            case LINK_TYPE_DOT1Q_TP_FDB:  return DOT1Q_TP_FDB;
	            case LINK_TYPE_PSEUDO_BRIDGE: return PSEUDO_BRIDGE;
	            case LINK_TYPE_PSEUDO_MAC:    return PSEUDO_MAC;
	            default:
	                throw new IllegalArgumentException("Cannot create LinkType from code "+code);
	            }
	        }		
		}

	private EndPoint m_a;
	
	private EndPoint m_b;
	
	private LinkType m_linkType;
	
	public Link(EndPoint a, EndPoint b, LinkType linkType, Integer sourceNode) {
		super(sourceNode);
		m_linkType=linkType;
		setA(a);
		setB(b);
	}
	
	@OneToOne
	public EndPoint getA() {
		return m_a;
	}

	public void setA(EndPoint a) {
		a.setLink(this);
		this.m_a = a;
	}

	@OneToOne
	public EndPoint getB() {
		return m_b;
	}

	public void setB(EndPoint b) {
		b.setLink(this);
		this.m_b = b;
	}
	
	public LinkType getLinkType() {
		return m_linkType;
	}
	
	public void setLinkType(LinkType linkType) {
		m_linkType = linkType;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Link) {
			Link a = (Link)o;
			if (getLinkType().equals((a.getLinkType()))) {
				if ((a.getA().equals(getA()) && a.getB().equals(getB()))
						|| (a.getA().equals(getB()) && a.getB().equals(getA())))
					return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("A", getA())
		.append("B", getB())
		.append("type", getLinkType())
		.append("lastPoll", m_lastPoll)
		.append("sourceNode", m_sourceNode)
		.toString();
	}
}
