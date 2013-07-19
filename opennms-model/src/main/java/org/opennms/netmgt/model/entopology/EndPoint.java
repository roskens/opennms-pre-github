package org.opennms.netmgt.model.entopology;

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
	private ElementIdentifier m_elementidentifier;

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
	
	String m_id;

	public String getId() {
		return m_id;
	}

	protected void setId(String id) {
		m_id = id;
	}


	public ElementIdentifier getElementIdentifier() {
		return m_elementidentifier;
	}

	public void setElementIdentifier(ElementIdentifier device) {
		device.addEndPoint(this);
		m_elementidentifier = device;
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
		return m_elementidentifier != null;
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
	
}
