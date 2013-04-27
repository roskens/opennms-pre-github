package org.opennms.netmgt.model.topology;

import java.net.InetAddress;

import org.apache.commons.lang.builder.ToStringBuilder;
import static org.opennms.core.utils.InetAddressUtils.str;

public final class InetElementIdentifier extends ElementIdentifier {

	private final InetAddress m_inet;
	private final Integer m_sourceIpNetToMediaNode;

	public InetElementIdentifier(InetAddress inet,Integer nodeId) {
		super(ElementIdentifierType.INET);
		m_inet = inet;
		m_sourceIpNetToMediaNode = nodeId;
	}

	public InetAddress getInet() {
		return m_inet;
	}

	@Override
	public boolean equals(ElementIdentifier elementIdentifier) {
		if (elementIdentifier instanceof InetElementIdentifier) 
			return (m_inet.equals(((InetElementIdentifier)elementIdentifier).getInet()));
		return false;
	}
	
	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("inet", str(m_inet))
			.append("sourceNodeid", m_sourceIpNetToMediaNode)
			.append("lastPoll", m_lastPoll)
			.toString();
	}

	public Integer getSourceIpNetToMediaNode() {
		return m_sourceIpNetToMediaNode;
	}

}
