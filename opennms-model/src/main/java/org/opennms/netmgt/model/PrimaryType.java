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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * The Class PrimaryType.
 */
@Embeddable
public class PrimaryType implements Comparable<PrimaryType>, Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -647348487361201657L;

    /** The Constant s_order. */
    private static final char[] s_order = { 'N', 'S', 'P' };

    /** The m_coll type. */
    private char m_collType;

    /**
     * Instantiates a new primary type.
     */
    protected PrimaryType() {
        this('N');
    }

    /**
     * Instantiates a new primary type.
     *
     * @param collType
     *            the coll type
     */
    PrimaryType(final char collType) {
        m_collType = collType;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    @Transient
    public String getCode() {
        return String.valueOf(m_collType);
    }

    /**
     * Gets the char code.
     *
     * @return the char code
     */
    @Column(name = "isSnmpPrimary")
    public char getCharCode() {
        return m_collType;
    }

    /**
     * Sets the char code.
     *
     * @param collType
     *            the new char code
     */
    public void setCharCode(final char collType) {
        m_collType = collType;
    }

    /**
     * Hibernate objects should not have any specific hashCode() implementation
     * since it should always give the same object for the same row anyways.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o) {
        if (o instanceof PrimaryType) {
            return this.compareTo((PrimaryType) o) == 0;
        } else
            return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final PrimaryType collType) {
        return getIndex(m_collType) - getIndex(collType.m_collType);
    }

    /**
     * Gets the index.
     *
     * @param code
     *            the code
     * @return the index
     */
    private static int getIndex(final char code) {
        for (int i = 0; i < s_order.length; i++) {
            if (s_order[i] == code) {
                return i;
            }
        }
        throw new IllegalArgumentException("illegal collType code '" + code + "'");
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.valueOf(m_collType);
    }

    /**
     * Checks if is less than.
     *
     * @param collType
     *            the coll type
     * @return true, if is less than
     */
    public boolean isLessThan(final PrimaryType collType) {
        return compareTo(collType) < 0;
    }

    /**
     * Checks if is greater than.
     *
     * @param collType
     *            the coll type
     * @return true, if is greater than
     */
    public boolean isGreaterThan(final PrimaryType collType) {
        return compareTo(collType) > 0;
    }

    /**
     * Max.
     *
     * @param collType
     *            the coll type
     * @return the primary type
     */
    public PrimaryType max(final PrimaryType collType) {
        return this.isLessThan(collType) ? collType : this;
    }

    /**
     * Min.
     *
     * @param collType
     *            the coll type
     * @return the primary type
     */
    public PrimaryType min(final PrimaryType collType) {
        return this.isLessThan(collType) ? this : collType;
    }

    /**
     * Gets the.
     *
     * @param code
     *            the code
     * @return the primary type
     */
    public static PrimaryType get(final char code) {
        switch (code) {
        case 'P':
            return PRIMARY;
        case 'S':
            return SECONDARY;
        case 'N':
            return NOT_ELIGIBLE;
        default:
            throw new IllegalArgumentException("Cannot create collType from code " + code);
        }
    }

    /**
     * Gets the.
     *
     * @param code
     *            the code
     * @return the primary type
     */
    public static PrimaryType get(final String code) {
        if (code == null) {
            return NOT_ELIGIBLE;
        }
        final String codeText = code.trim();
        if (codeText.length() < 1) {
            return NOT_ELIGIBLE;
        } else if (codeText.length() > 1) {
            throw new IllegalArgumentException("Cannot convert string '" + codeText + "' to a collType");
        } else {
            return get(codeText.charAt(0));
        }
    }

    /**
     * Gets the all types.
     *
     * @return the all types
     */
    public static List<PrimaryType> getAllTypes() {
        final List<PrimaryType> types = new ArrayList<PrimaryType>();
        for (final char c : s_order) {
            types.add(PrimaryType.get(c));
        }
        return types;
    }

    /** The Constant PRIMARY. */
    public static final PrimaryType PRIMARY = new PrimaryType('P');

    /** The Constant SECONDARY. */
    public static final PrimaryType SECONDARY = new PrimaryType('S');

    /** The Constant NOT_ELIGIBLE. */
    public static final PrimaryType NOT_ELIGIBLE = new PrimaryType('N');
}
