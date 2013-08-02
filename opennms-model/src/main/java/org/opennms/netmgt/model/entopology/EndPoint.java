/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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
package org.opennms.netmgt.model.entopology;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * An endpoint is a destination in the network such as a physical port on a
 * device, an IP address or a tcp port. The type of the endpoint depends on
 * the protocol the information for the topology comes from.
 * 
 * @author Antonio
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discriminator")
@Table(name="topoendpoint")
public abstract class EndPoint<EI extends ElementIdentifier, LE extends EndPoint<EI, LE>> implements Pollable {

    /**
     * Primary Key for these type of entities. Each implementation builds it's on String from it's attriubtes.
     */
    // We can't use a technical (long) id here. We never do lookups on it, we
    // always need to find the entity with a specific set of attributes.
    // @EmbeddedId doesn't work with Inheritance because the supertype is an
    // entity it needs an @Id, and @EmbeddedId together with @Id is not
    // possible. @EmbeddedId also has to be concrete Class not a gernic Type
    // or abstract class and it need columns.
    @Id
    @Column(name="id")
    protected String m_id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="lastpoll")
    protected Date m_lastPoll = new Date();
    
    @Column(name="sourcenode")
    protected Integer m_sourceNode;

    /**
     * The Element to which the End Point 
     * belongs
     *  
     */
    @ManyToOne(cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="elementidentifier")
    private ElementIdentifier m_elementIdentifier;

    /**
     * An endpoint can have a link to another endpoint.
     * 
     */
    @OneToOne(cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="linkedendpoint")
    private EndPoint m_linkedEndpoint;


	// FIXME are all the if properties duplicated information from onms-interface? do we really really need them here?
    /**
     * The ifindex of the endpoint
     * could be null
     */
 // FIXME
    @Transient
    private Integer m_ifIndex;

    /**
     * The  ifName of the endPoint
     * could be null
     */
 // FIXME
    @Transient
    private String m_ifName;
    /**
     * The ifDescr of the endPoint
     * could be null
     */
 // FIXME
    @Transient
    private String m_ifDescr;
    
    /**
     * The ifAlias of the endPoint
     * could be null
     */
 // FIXME
    @Transient
    private String m_ifAlias;
    
    public EndPoint() {
    }
    
	public EndPoint(String id) {
	    this.m_id = id;
	}


	public String getId() {
		return m_id;
	}

	protected void setId(String id) {
		m_id = id;
	}


	public EI getElementIdentifier() {
	    return (EI) m_elementIdentifier;
	}

	public void setElementIdentifier(EI device) {
        if (m_elementIdentifier != null) {
            m_elementIdentifier.getEndPoints().remove(this);
        }
        m_elementIdentifier = device;
        if (m_elementIdentifier != null) {
            m_elementIdentifier.getEndPoints().add(this);
        }
	}

    public LE getLinkedEndpoint() {
        return (LE) m_linkedEndpoint;
    }


    public void setLinkedEndpoint(LE linkedEndpoint) {
        if(m_linkedEndpoint != null) {
            EndPoint tmp = m_linkedEndpoint;
            m_linkedEndpoint = null; // set it null to avoid recursion
            tmp.setLinkedEndpoint(null);
        }
        m_linkedEndpoint = linkedEndpoint;
        if(m_linkedEndpoint != null && m_linkedEndpoint.getLinkedEndpoint() == null) {
            m_linkedEndpoint.setLinkedEndpoint(this);
        }
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

    @Override
    public Integer getSourceNode() {
        return m_sourceNode;
    }

    @Override
    public Date getLastPoll() {
        return m_lastPoll;
    }
    
    @Override
    public void updatePollable(Date lastPoll, Integer node) {
        this.m_lastPoll = lastPoll;
        this.m_sourceNode = node;
    }

    /**
     * The simplings define there primary key, we want it to match with hashcode and equals
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
        return result;
    }

    /**
     * The simplings define there primary key, we want it to match with hashcode and equals
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EndPoint other = (EndPoint) obj;
        if (m_id == null) {
            if (other.m_id != null)
                return false;
        } else if (!m_id.equals(other.m_id))
            return false;
        return true;
    }
}
