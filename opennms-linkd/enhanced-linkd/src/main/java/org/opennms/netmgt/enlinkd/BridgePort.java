package org.opennms.netmgt.enlinkd;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BridgePort {
	
	final private String m_bridgeIdentifier;
	
	final private Integer m_bridgePort;
	
	public BridgePort(String bridgeIdentifier, Integer bridgePort) {
		super();
		m_bridgeIdentifier = bridgeIdentifier;
		m_bridgePort = bridgePort;
	}

	public String getBridgeIdentifier() {
		return m_bridgeIdentifier;
	}

	public Integer getBridgePort() {
		return m_bridgePort;
	}
	
	public boolean sameBridge(BridgePort bridgeport) {
		return m_bridgeIdentifier.equals(bridgeport.getBridgeIdentifier());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("bridgeId", m_bridgeIdentifier)
		.append("port", m_bridgePort).toString();
	}
}