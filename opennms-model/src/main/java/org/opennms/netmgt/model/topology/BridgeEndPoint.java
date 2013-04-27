package org.opennms.netmgt.model.topology;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BridgeEndPoint extends EndPoint {

	public BridgeEndPoint(Integer bridgePortId) {
		super();
		m_bridgePort = bridgePortId;
	}

	private final Integer m_bridgePort;

	
	public Integer getBridgePort() {
		return m_bridgePort;
	}
		
	@Override
	public boolean equals(EndPoint endPoint) {
		if (endPoint instanceof BridgeEndPoint) {
			if ((getElement() != null && endPoint.getElement() != null && getElement().equals(endPoint.getElement())) 
					|| (getElement() == null && endPoint.getElement() == null)) 
			return m_bridgePort.equals(((BridgeEndPoint)endPoint).getBridgePort());
		}
		return false;
	}
	
	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("m_bridgePort", m_bridgePort)
			.append("lastPoll", m_lastPoll)
			.toString();
	}

	@Override
	public void update(EndPoint endpoint) {
		if (!equals(endpoint)) {
			return;
		}
		m_lastPoll = endpoint.getLastPoll();
		if (endpoint.hasLink()) {
			Link link = endpoint.getLink(); 
			if (equals(link.getA())) 
				link.setA(this);
			else if (equals(link.getB())) 
				link.setB(this);
			setLink(link);
		}
	}

}
