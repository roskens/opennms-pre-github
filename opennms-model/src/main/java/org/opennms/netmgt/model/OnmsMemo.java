/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * Generic memo for any element inside OpenNMS
 * </p>
 * .
 *
 * @author <a href="mailto:Markus@OpenNMS.com">Markus Neumann</a>
 */
@XmlRootElement(name = "memo")
@Entity
@Table(name = "memos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "Memo")
public class OnmsMemo implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7272348439687562161L;

    /** The m_id. */
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "memoSequence", sequenceName = "memoNxtId")
    @GeneratedValue(generator = "memoSequence")
    @XmlAttribute(name = "id")
    private Integer m_id;

    /** The m_body. */
    @Column(name = "body")
    private String m_body;

    /** The m_author. */
    @Column(name = "author")
    private String m_author;

    /** The m_updated. */
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date m_updated;

    /** The m_created. */
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date m_created;

    /**
     * Pre update.
     */
    @PreUpdate
    private void preUpdate() {
        m_updated = new Date();
    }

    /**
     * Pre persist.
     */
    @PrePersist
    private void prePersist() {
        m_created = new Date();
    }

    /**
     * Gets the body.
     *
     * @return the body
     */
    public String getBody() {
        return m_body;
    }

    /**
     * Sets the body.
     *
     * @param body
     *            the new body
     */
    public void setBody(String body) {
        this.m_body = body;
    }

    /**
     * Gets the created.
     *
     * @return the created
     */
    public Date getCreated() {
        return m_created;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return m_id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(final Integer id) {
        m_id = id;
    }

    /**
     * Gets the updated.
     *
     * @return the updated
     */
    public Date getUpdated() {
        return m_updated;
    }

    /**
     * Sets the created.
     *
     * @param created
     *            the new created
     */
    public void setCreated(Date created) {
        this.m_created = created;
    }

    /**
     * Sets the updated.
     *
     * @param updated
     *            the new updated
     */
    public void setUpdated(Date updated) {
        this.m_updated = updated;
    }

    /**
     * Gets the author.
     *
     * @return the author
     */
    public String getAuthor() {
        return m_author;
    }

    /**
     * Sets the author.
     *
     * @param author
     *            the new author
     */
    public void setAuthor(String author) {
        this.m_author = author;
    }
}
