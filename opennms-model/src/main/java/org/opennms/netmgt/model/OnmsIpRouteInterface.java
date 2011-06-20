package org.opennms.netmgt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "ipRouteInterface")
@Entity
@Table(name="ipRouteInterface", uniqueConstraints = {@UniqueConstraint(columnNames={"nodeId", "routeDest"})})
public class OnmsIpRouteInterface {

	private Integer m_nodeId;
	private OnmsNode m_node;
	private String m_routeDest;
	private String m_routeMask;
	private String m_routeNextHop;
	private Integer m_routeIfIndex;
	private Integer m_routeMetric1;
	private Integer m_routeMetric2;
	private Integer m_routeMetric3;
	private Integer m_routeMetric4;
	private Integer m_routeMetric5;
	private Integer m_routeType;
	private Integer m_routeProto;
	private Character m_status;
	private Date m_lastPollTime;

    // transient, see getNode()/setNode() below
    @Transient
    @XmlTransient
	public Integer getNodeId() {
		return m_nodeId;
	}
	
	public void setNodeId(final Integer nodeId) {
		m_nodeId = nodeId;
	}
	
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="nodeId")
    @XmlElement(name="nodeId")
    @XmlIDREF
    public OnmsNode getNode() {
        return m_node;
    }

    public void setNode(OnmsNode node) {
        m_node = node;
        m_nodeId = node == null? null : node.getId();
    }

    @XmlElement
    @Column(nullable=false, length=16)
	public String getRouteDest() {
		return m_routeDest;
	}

	public void setRouteDest(String routeDest) {
		m_routeDest = routeDest;
	}

    @XmlElement
    @Column(nullable=false, length=16)
	public String getRouteMask() {
		return m_routeMask;
	}

	public void setRouteMask(String routeMask) {
		m_routeMask = routeMask;
	}

    @XmlElement
    @Column(nullable=false, length=16)
	public String getRouteNextHop() {
		return m_routeNextHop;
	}

	public void setRouteNextHop(String routeNextHop) {
		m_routeNextHop = routeNextHop;
	}

    @XmlElement
    @Column(nullable=false)
	public Integer getRouteIfIndex() {
		return m_routeIfIndex;
	}

	public void setRouteIfIndex(Integer routeIfIndex) {
		m_routeIfIndex = routeIfIndex;
	}

    @XmlElement
    @Column
	public Integer getRouteMetric1() {
		return m_routeMetric1;
	}

	public void setRouteMetric1(Integer routeMetric1) {
		m_routeMetric1 = routeMetric1;
	}

    @XmlElement
    @Column
	public Integer getRouteMetric2() {
		return m_routeMetric2;
	}

	public void setRouteMetric2(Integer routeMetric2) {
		m_routeMetric2 = routeMetric2;
	}

    @XmlElement
    @Column
	public Integer getRouteMetric3() {
		return m_routeMetric3;
	}

	public void setRouteMetric3(Integer routeMetric3) {
		m_routeMetric3 = routeMetric3;
	}

    @XmlElement
    @Column
	public Integer getRouteMetric4() {
		return m_routeMetric4;
	}

	public void setRouteMetric4(Integer routeMetric4) {
		m_routeMetric4 = routeMetric4;
	}

    @XmlElement
    @Column
	public Integer getRouteMetric5() {
		return m_routeMetric5;
	}

	public void setRouteMetric5(Integer routeMetric5) {
		m_routeMetric5 = routeMetric5;
	}

    @XmlElement
    @Column
	public Integer getRouteType() {
		return m_routeType;
	}

	public void setRouteType(Integer routeType) {
		m_routeType = routeType;
	}

    @XmlElement
    @Column
	public Integer getRouteProto() {
		return m_routeProto;
	}

	public void setRouteProto(Integer routeProto) {
		m_routeProto = routeProto;
	}

    @XmlElement
    @Column(nullable=false)
	public Character getStatus() {
		return m_status;
	}

	public void setStatus(final Character status) {
		m_status = status;
	}

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    @XmlElement
	public Date getLastPollTime() {
		return m_lastPollTime;
	}

	public void setLastPollTime(Date lastPollTime) {
		m_lastPollTime = lastPollTime;
	}


}
