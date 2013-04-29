package org.opennms.netmgt.model.topology;

/**
 * This class represents a destination in the network such as
 * an IP address or a physical port.
 * Also can represent a TCP Port
 * 
 * @author Antonio
 *
 */
public abstract class EndPoint extends Pollable {
	
	public EndPoint(Integer sourceNode) {
		super(sourceNode);
	}

	/** 
	 * The Element to which the End Point 
	 * belongs
	 *  
	 */
	private Element m_device;

	/**
	 * Only one Link for End Point is allowed
	 * 
	 */	
	private Link m_link;

	/**
	 * The ifindex of the endpoint
	 * could be null
	 */
	private Integer m_ifIndex;

	/**
	 * The  ifName of the endPoint
	 * could be null
	 */

	private String m_ifName;
	/**
	 * The ifDescr of the endPoint
	 * could be null
	 */
	private String m_ifDescr;
	
	/**
	 * The ifAlias of the endPoint
	 * could be null
	 */
	private String m_ifAlias;
	

	public Element getElement() {
		return m_device;
	}

	public void setElement(Element device) {
		m_device = device;
	}
	
	public Link getLink() {
		return m_link;
	}

	public void setLink(Link link) {
		m_link = link	;
	}
	
	public boolean hasLink() {
		return m_link != null;
	}

	public boolean hasElement() {
		return m_device != null;
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
	public boolean equals(Object o) {
		if (o instanceof EndPoint) {
			return equals((EndPoint)o);
		}
		return false;
	}
	
	public abstract boolean equals(EndPoint endPoint);

	public abstract void update(EndPoint endpoint);
}
