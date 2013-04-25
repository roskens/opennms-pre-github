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
	
	public EndPoint() {
		super();
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
	 * The current ifindex of the endpoint
	 * could be null
	 */
	private Integer m_ifIndex;

	/**
	 * The current ifName of the endPoint
	 * could be null
	 */
	private String m_ifName;
	
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

	public Element getDevice() {
		return m_device;
	}

	public void setDevice(Element device) {
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
