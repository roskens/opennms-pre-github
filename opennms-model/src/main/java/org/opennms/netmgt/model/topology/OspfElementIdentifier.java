package org.opennms.netmgt.model.topology;

import java.net.InetAddress;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import static org.opennms.core.utils.InetAddressUtils.str;

@Entity
@DiscriminatorValue("OSPF")
public final class OspfElementIdentifier extends ElementIdentifier {

	private InetAddress m_ospfRouterId; 

	public OspfElementIdentifier(InetAddress ospfRouterid,Integer sourceNode) {
		super(ElementIdentifierType.OSPF,sourceNode);
		m_ospfRouterId= ospfRouterid;
	}

    @Type(type="org.opennms.netmgt.model.InetAddressUserType")
	public InetAddress getOspfRouterId() {
		return m_ospfRouterId;
	}

	public void setOspfRouterId(InetAddress ospfRouterId) {
		m_ospfRouterId = ospfRouterId;
	}

	/**
	 * <p>toString</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("ospfRouterId", str(m_ospfRouterId))
			.append("lastPoll", m_lastPoll)
			.append("sourceNode", m_sourceNode)
			.toString();
	}

}
