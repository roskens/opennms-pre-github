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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * An ElementIdentifier identifies a physical device with endpoints, like a
 * switch, a router, a host.
 * 
 * @author thargor
 */
// FIXME should we call this device or just element, identifier is just confusing?
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discriminator")
@Table(name="topoElementIdentifier")
public abstract class ElementIdentifier implements Pollable {

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

	@OneToMany(mappedBy="m_elementIdentifier")
	protected Set<EndPoint> m_endpoints = new HashSet<EndPoint>();
    
    public ElementIdentifier() {
    }
	   
	public ElementIdentifier(String id) {
	    this.m_id = id;
    }
    
    public String getId() {
		return m_id;
	}

    protected void setId(String id) {
		m_id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<EndPoint> getEndPoints() {
		return m_endpoints;
	}
	
	public void addEndPoint(EndPoint endpoint) {
		m_endpoints.add(endpoint);
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
        ElementIdentifier other = (ElementIdentifier) obj;
        if (m_id == null) {
            if (other.m_id != null)
                return false;
        } else if (!m_id.equals(other.m_id))
            return false;
        return true;
    }
	
}
