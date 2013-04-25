package org.opennms.netmgt.model.topology;

import java.net.InetAddress;

import org.apache.commons.lang.builder.ToStringBuilder;
import static org.opennms.core.utils.InetAddressUtils.str;

public final class InetElementIdentifier extends ElementIdentifier {

	private final InetAddress m_inet; 

	public InetElementIdentifier(InetAddress inet) {
		super(ElementIdentifierType.INET);
		m_inet = inet;
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
			.append("lastPoll", m_lastPoll)
			.toString();
	}

}
