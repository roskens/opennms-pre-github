package org.opennms.netmgt.model.topology;

import java.net.InetAddress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import static org.opennms.core.utils.InetAddressUtils.str;

@Entity
@DiscriminatorValue("INET")
public final class InetElementIdentifier extends ElementIdentifier {

	private InetAddress m_inet;

	public InetElementIdentifier(InetAddress inet,Integer sourceNode) {
		super(ElementIdentifierType.INET,sourceNode);
		m_inet = inet;
	}

    @Type(type="org.opennms.netmgt.model.InetAddressUserType")
    public InetAddress getInet() {
		return m_inet;
	}

	public void setInet(InetAddress inet) {
		m_inet = inet;
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
			.append("sourceNode", m_sourceNode)
			.toString();
	}
}
