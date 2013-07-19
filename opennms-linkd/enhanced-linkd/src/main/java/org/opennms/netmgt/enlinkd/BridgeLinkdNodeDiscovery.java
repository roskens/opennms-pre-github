/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.enlinkd;

import static org.opennms.core.utils.InetAddressUtils.str;
import static org.opennms.core.utils.InetAddressUtils.isValidBridgeAddress;
import static org.opennms.core.utils.InetAddressUtils.getBridgeAddressFromBridgeId;
import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getPseudoMacLink;
import static org.opennms.netmgt.enlinkd.PseudoBridgeHelper.getPseudoBridgeLink;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.netmgt.model.OnmsStpNode.BridgeBaseType;
import org.opennms.netmgt.model.topology.BridgeDot1dTpFdbLink;
import org.opennms.netmgt.model.topology.BridgeDot1qTpFdbLink;
import org.opennms.netmgt.model.topology.BridgeElementIdentifier;
import org.opennms.netmgt.model.topology.BridgeEndPoint;
import org.opennms.netmgt.model.topology.BridgeStpLink;
import org.opennms.netmgt.model.topology.Link;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;
import org.opennms.netmgt.model.topology.NodeElementIdentifier;

import org.opennms.netmgt.snmp.SnmpUtils;
import org.opennms.netmgt.snmp.SnmpWalker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is designed to collect the necessary SNMP information from the
 * target address and store the collected information. When the class is
 * initially constructed no information is collected. The SNMP Session creating
 * and collection occurs in the main run method of the instance. This allows the
 * collection to occur in a thread if necessary.
 */
public final class BridgeLinkdNodeDiscovery extends AbstractLinkdNodeDiscovery {
    private static final Logger LOG = LoggerFactory.getLogger(BridgeLinkdNodeDiscovery.class);

	public final static String CISCO_ENTERPRISE_OID = ".1.3.6.1.4.1.9";

	List<Integer> m_stpPorts = new ArrayList<Integer>();
	List<Integer> m_backbonePorts = new ArrayList<Integer>();
	private boolean m_failed = false;
	Map<Integer, Link> m_parsedPort = new HashMap<Integer, Link>();

	public List<Integer> getBackBonePorts() {
		return m_backbonePorts;
	}

	public void addBackBonePort(Integer bridgePort) {
		m_backbonePorts.add(bridgePort);
	}

	public Map<Integer, Link> getParsedPort() {
		return m_parsedPort;
	}

	public void addParsedPort(Integer bridgePort, Link link) {
		m_parsedPort.put(bridgePort, link);
	}

	public void removeParsedPort(Integer bridgePort) {
		m_parsedPort.remove(bridgePort);
	}

	public boolean isFailed() {
		return m_failed;
	}

	public void setFailed(boolean failed) {
		m_failed = failed;
	}

	/**
	 * Constructs a new SNMP collector for Bridge Node Discovery. The collection
	 * does not occur until the <code>run</code> method is invoked.
	 * 
	 * @param EnhancedLinkd
	 *            linkd
	 * @param LinkableNode
	 *            node
	 */
	public BridgeLinkdNodeDiscovery(final EnhancedLinkd linkd,
			final LinkableNode node) {
		super(linkd, node);
	}

	protected void runCollection() {

		final Date now = new Date();

		LOG.debug("run: collecting: {}", getPeer());
		walkBridge();
		if (isFailed())
			return;

		if (m_node.getSysoid().startsWith(CISCO_ENTERPRISE_OID)) {
			walkCiscoVtpVlan();
			if (isFailed())
				return;
		}

		// These are the standalone learned mac address
		// suitable for real links
		for (Link link : m_parsedPort.values()) {
			if (link instanceof BridgeDot1dTpFdbLink)
				m_linkd.getQueryManager().store((BridgeDot1dTpFdbLink) link);
			else if (link instanceof BridgeDot1qTpFdbLink)
				m_linkd.getQueryManager().store((BridgeDot1qTpFdbLink) link);
		}

		m_backbonePorts.clear();
		m_failed = false;
		m_parsedPort.clear();
		m_stpPorts.clear();

		m_linkd.getQueryManager().reconcileBridge(getNodeId(), now);

	}

	private void walkCiscoVtpVlan() {
		String trackerName = "vtpVersion";
		final CiscoVtpStatus vtpStatus = new CiscoVtpStatus();
		SnmpWalker walker = SnmpUtils.createWalker(getPeer(), trackerName,
				vtpStatus);
		walker.start();

		try {
			walker.waitFor();
			if (walker.timedOut()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent timed out while scanning the {} table",
						trackerName);
				setFailed(true);
				return;
			} else if (walker.failed()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent failed while scanning the {} table: {}",
						trackerName, walker.getErrorMessage());
				setFailed(true);
				return;
			}
		} catch (final InterruptedException e) {
			LOG.error("run: Bridge Linkd node collection interrupted, exiting", e);
			setFailed(true);
			return;
		}

		if (vtpStatus.getVtpVersion() == null) {
			LOG.info("cisco vtp mib not supported, on: {}",
					str(getPeer().getAddress()));
			return;
		}
		LOG.info("cisco vtp mib supported, on: {}", str(getPeer()
				.getAddress()));
		LOG.info("walking cisco vtp, on: {}", str(getPeer()
				.getAddress()));

		trackerName = "ciscoVtpVlan";
		final CiscoVtpVlanTableTracker ciscoVtpVlanTableTracker = new CiscoVtpVlanTableTracker() {
			@Override
			public void processCiscoVtpVlanRow(final CiscoVtpVlanRow row) {
				if (row.isTypeEthernet() && row.isStatusOperational()) {
					String community = getPeer().getReadCommunity();
					Integer vlanindex = row.getVlanIndex();
					LOG.debug("run: cisco vlan collection setting peer community: {} with VLAN {}",
							community, vlanindex);
					if (vlanindex != 1) {
						getPeer().setReadCommunity(community + "@" + vlanindex);
						walkBridge();
						getPeer().setReadCommunity(community);
					}
				}
			}
		};
		walker = SnmpUtils.createWalker(getPeer(), trackerName,
				ciscoVtpVlanTableTracker);
		walker.start();

		try {
			walker.waitFor();
			if (walker.timedOut()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent timed out while scanning the {} table",
						trackerName);
				setFailed(true);
				return;
			} else if (walker.failed()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent failed while scanning the {} table: {}",
						trackerName, walker.getErrorMessage());
				setFailed(true);
				return;
			}
		} catch (final InterruptedException e) {
			LOG.error("run: Bridge Linkd node collection interrupted, exiting",e);
			setFailed(true);
			return;
		}
	}

	protected void walkBridge() {
		String trackerName = "dot1dbase";
		final Dot1dBase dot1dbase = new Dot1dBase();
		SnmpWalker walker = SnmpUtils.createWalker(getPeer(), trackerName,
				dot1dbase);
		walker.start();

		try {
			walker.waitFor();
			if (walker.timedOut()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent timed out while scanning the {} table",
						trackerName);
				setFailed(true);
				return;
			} else if (walker.failed()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent failed while scanning the {} table: {}",
						trackerName, walker.getErrorMessage());
				setFailed(true);
				return;
			}
		} catch (final InterruptedException e) {
			LOG.error("run: Bridge Linkd node collection interrupted, exiting",e);
			setFailed(true);
			return;
		}

		if (dot1dbase.getBridgeAddress() == null) {
			LOG.info("bridge mib not supported on: {}",
					str(getPeer().getAddress()));
			return;
		}

		if (isValidBridgeAddress(dot1dbase.getBridgeAddress())) {
			LOG.info("bridge not supported, base address identifier {} is not valid on: {}",
					dot1dbase.getBridgeAddress(), str(getPeer().getAddress()));
			return;
		}

		if (dot1dbase.getNumberOfPorts() == 0) {
			LOG.info("bridge {} has 0 port active, on: {}",
					dot1dbase.getBridgeAddress(), str(getPeer().getAddress()));
			return;
		}
		LOG.info("bridge {} has is if type {}, on: {}", dot1dbase
				.getBridgeAddress(),
				BridgeBaseType.getBridgeBaseTypeString(dot1dbase
						.getBridgeType().getIntCode()), str(getPeer()
						.getAddress()));

		if (dot1dbase.getBridgeType().equals(BridgeBaseType.UNKNOWN)) {
			LOG.info("{}: unknown type bridge, on: {}",
					dot1dbase.getBridgeAddress(), str(getPeer().getAddress()));
			return;
		} else if (dot1dbase.getBridgeType().equals(
				BridgeBaseType.SOURCEROUTE_ONLY)) {
			LOG.info("{}: source route only type bridge, on: {}",
					dot1dbase.getBridgeAddress(), str(getPeer().getAddress()));
			return;
		}

		final BridgeElementIdentifier bridgeElementIdentifier = dot1dbase
				.getElementIdentifier(getNodeId());
		LOG.info("found local bridge identifier : {}",
				bridgeElementIdentifier);

		final NodeElementIdentifier nodeElementIdentifier = new NodeElementIdentifier(
				getNodeId());
		LOG.info("found node identifier for node: {}",
				nodeElementIdentifier);

		walkSpanningTree(nodeElementIdentifier, bridgeElementIdentifier);
		if (isFailed())
			return;

		walkDot1DTpFdp(nodeElementIdentifier, bridgeElementIdentifier);
		if (isFailed())
			return;

		if (getParsedPort().isEmpty() && getBackBonePorts().isEmpty()
				&& !m_node.getSysoid().startsWith(CISCO_ENTERPRISE_OID))
			walkDot1QTpFdp(nodeElementIdentifier, bridgeElementIdentifier);
	}

	private void walkDot1DTpFdp(
			final NodeElementIdentifier nodeElementIdentifier,
			final BridgeElementIdentifier bridgeElementIdentifier) {
		String trackerName = "dot1dTbFdbPortTable";

		Dot1dTpFdbTableTracker stpPortTableTracker = new Dot1dTpFdbTableTracker() {

			@Override
			public void processDot1dTpFdbRow(final Dot1dTpFdbRow row) {
				Integer bridgePort = row.getDot1dTpFdbPort();
				if (getStpPorts().contains(bridgePort))
					return;
				BridgeDot1dTpFdbLink link = row.getLink(nodeElementIdentifier,
						bridgeElementIdentifier);
				if (link == null)
					return;
				// if backbone add as pseudo link
				if (getBackBonePorts().contains(bridgePort)) {
					m_linkd.getQueryManager().store(
							getPseudoMacLink((BridgeEndPoint)link.getA(), (MacAddrEndPoint)link.getB()));
					return;
				}
				// if at least one parsed this is the second then is a backbone
				// port
				// so add the port to the backbone and save the three links...as
				// pseudo device
				if (getParsedPort().containsKey(bridgePort)) {
					m_linkd.getQueryManager().store(
							getPseudoBridgeLink((BridgeEndPoint)link.getA()));
					m_linkd.getQueryManager().store(
							getPseudoMacLink((BridgeEndPoint) link.getA(),
									(MacAddrEndPoint)getParsedPort().get(bridgePort).getB()));
					m_linkd.getQueryManager().store(
							getPseudoMacLink((BridgeEndPoint)link.getA(), (MacAddrEndPoint)link.getB()));
					removeParsedPort(bridgePort);
					addBackBonePort(bridgePort);
					return;
				}
				// first port occurrence save properly to check if there are
				// other problem
				addParsedPort(bridgePort, link);
			}
		};
		SnmpWalker walker = SnmpUtils.createWalker(getPeer(), trackerName,
				stpPortTableTracker);
		walker.start();

		try {
			walker.waitFor();
			if (walker.timedOut()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent timed out while scanning the {} table",
						trackerName);
				setFailed(true);
				return;
			} else if (walker.failed()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent failed while scanning the {} table: {}",
						trackerName, walker.getErrorMessage());
				setFailed(true);
				return;
			}
		} catch (final InterruptedException e) {
			LOG.error("run: Bridge Linkd node collection interrupted, exiting",e);
			setFailed(true);
			return;
		}
	}

	private void walkDot1QTpFdp(
			final NodeElementIdentifier nodeElementIdentifier,
			final BridgeElementIdentifier bridgeElementIdentifier) {

		String trackerName = "dot1qTbFdbPortTable";

		Dot1qTpFdbTableTracker dot1qTpFdbTableTracker = new Dot1qTpFdbTableTracker() {

			@Override
			public void processDot1qTpFdbRow(final Dot1qTpFdbRow row) {
				Integer bridgePort = row.getDot1qTpFdbPort();
				if (getStpPorts().contains(bridgePort))
					return;
				BridgeDot1qTpFdbLink link = row.getLink(nodeElementIdentifier,
						bridgeElementIdentifier);
				if (link == null)
					return;
				// if backbone add as pseudo link
				if (getBackBonePorts().contains(bridgePort)) {
					m_linkd.getQueryManager().store(
							getPseudoMacLink((BridgeEndPoint) link.getA(),
									(MacAddrEndPoint) link.getB()));
					return;
				}
				// if at least one parsed this is the second then is a backbone
				// port
				// so add the port to the backbone and save the three links...as
				// pseudo device
				if (getParsedPort().containsKey(bridgePort)) {
					m_linkd.getQueryManager().store(
							getPseudoBridgeLink(
									(BridgeEndPoint) link.getA()));
					m_linkd.getQueryManager().store(
							getPseudoMacLink(
									(BridgeEndPoint) link.getA(),
									(MacAddrEndPoint) getParsedPort().get(
											bridgePort).getB()));
					m_linkd.getQueryManager().store(
							getPseudoMacLink(
									(BridgeEndPoint) link.getA(),
									(MacAddrEndPoint) link.getB()));
					removeParsedPort(bridgePort);
					addBackBonePort(bridgePort);
					return;
				}
				// first port occurrence save properly to check if there are
				// other problem
				addParsedPort(bridgePort, link);
			}

		};
		SnmpWalker walker = SnmpUtils.createWalker(getPeer(), trackerName,
				dot1qTpFdbTableTracker);
		walker.start();

		try {
			walker.waitFor();
			if (walker.timedOut()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent timed out while scanning the {} table",
						trackerName);
				setFailed(true);
			} else if (walker.failed()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent failed while scanning the {} table: {}",
						trackerName, walker.getErrorMessage());
				setFailed(true);
			}
		} catch (final InterruptedException e) {
			LOG.error("run: Bridge Linkd node collection interrupted, exiting",e);
			setFailed(true);
		}
	}

	private void walkSpanningTree(
			final NodeElementIdentifier nodeElementIdentifier,
			final BridgeElementIdentifier bridgeElementIdentifier) {

		final List<Integer> backbonestpbports = new ArrayList<Integer>();

		String trackerName = "dot1dStp";
		final Dot1dStp dot1dstp = new Dot1dStp();

		SnmpWalker walker = SnmpUtils.createWalker(getPeer(), trackerName,
				dot1dstp);
		walker.start();

		try {
			walker.waitFor();
			if (walker.timedOut()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent timed out while scanning the {} table",
						trackerName);
				setFailed(true);
				return;
			} else if (walker.failed()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent failed while scanning the {} table: {}",
						trackerName, walker.getErrorMessage());
				setFailed(true);
				return;
			}
		} catch (final InterruptedException e) {
			LOG.error("run: Bridge Linkd node collection interrupted, exiting",e);
			setFailed(true);
			return;
		}

		if (dot1dstp.getStpDesignatedRoot() == null) {
			LOG.info("spanning tree not supported on: {}",
					str(getPeer().getAddress()));
			return;
		} else if (!isValidBridgeAddress(dot1dstp.getStpDesignatedRoot())) {
			LOG.info("spanning tree not supported, designated root {} is not valid on: {}",
					dot1dstp.getStpDesignatedRoot(),
					str(getPeer().getAddress()));
			return;
		} else if (dot1dstp.getStpProtocolSpecification() != 3) {
			LOG.info("ieee8021d spanning tree not supported on bridge {}, on: {}",
					dot1dstp.getStpDesignatedRoot(),
					str(getPeer().getAddress()));
			return;
		} else if (bridgeElementIdentifier.getBridgeAddress().equals(
				getBridgeAddressFromBridgeId(dot1dstp.getStpDesignatedRoot()))) {
			LOG.info("designated root of spanning tree is itself on bridge {}, on: {}",
					dot1dstp.getStpDesignatedRoot(),
					str(getPeer().getAddress()));
			return;
		}

		trackerName = "dot1dStpPortTable";

		Dot1dStpPortTableTracker stpPortTableTracker = new Dot1dStpPortTableTracker() {
			@Override
			public void processDot1dStpPortRow(final Dot1dStpPortRow row) {
				BridgeStpLink link = row.getLink(nodeElementIdentifier,
						bridgeElementIdentifier);
				if (link != null) {
					backbonestpbports.add(row.getDot1dStpPort());
					m_linkd.getQueryManager().store(link);
				}
			}

		};

		walker = SnmpUtils.createWalker(getPeer(), trackerName,
				stpPortTableTracker);
		walker.start();

		try {
			walker.waitFor();
			if (walker.timedOut()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent timed out while scanning the {} table",
						trackerName);
				setFailed(true);
			} else if (walker.failed()) {
				LOG.info("run:Aborting Bridge Linkd node scan : Agent failed while scanning the {} table: {}",
						trackerName, walker.getErrorMessage());
				setFailed(true);
			}
		} catch (final InterruptedException e) {
			LOG.error("run: Bridge Linkd node collection interrupted, exiting",e);
			setFailed(true);
		}
		setStpPorts(backbonestpbports);
	}

	public List<Integer> getStpPorts() {
		return m_stpPorts;
	}

	public void setStpPorts(List<Integer> stpPorts) {
		m_stpPorts = stpPorts;
	}

	@Override
	public String getInfo() {
		return "ReadyRunnable BridgeLinkNodeDiscovery" + " ip="
				+ str(getTarget()) + " port=" + getPort() + " community="
				+ getReadCommunity() + " package=" + getPackageName();
	}

	@Override
	public String getName() {
		return "BridgeLinkDiscovery";
	}

	  /**
     * <p>
     * isReady
     * </p>
     * 
     * @return a boolean.
     */
	@Override
    public boolean isReady() {
        return m_linkd.getQueryManager().ready();
    }

}
