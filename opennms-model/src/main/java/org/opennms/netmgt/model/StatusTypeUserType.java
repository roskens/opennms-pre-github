/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2012 The OpenNMS Group, Inc.
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.opennms.netmgt.model.OnmsArpInterface.StatusType;

/**
 * The Class StatusTypeUserType.
 */
public class StatusTypeUserType implements UserType {

    /** The Constant SQL_TYPES. */
    private static final int[] SQL_TYPES = new int[] { java.sql.Types.CHAR };

    /**
     * A public default constructor is required by Hibernate.
     */
    public StatusTypeUserType() {
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
     */
    @Override
    public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    /**
     * Since {@link java.net.InetAddress} is immutable, we just return the
     * original
     * value without copying it.
     *
     * @param value
     *            the value
     * @return the object
     * @throws HibernateException
     *             the hibernate exception
     */
    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        if (value == null) {
            return null;
        } else if (value instanceof StatusType) {
            return value;
        } else {
            throw new IllegalArgumentException("Unexpected type that is mapped with " + this.getClass().getSimpleName()
                    + ": " + value.getClass().getName());
        }
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
     */
    @Override
    public Serializable disassemble(final Object value) throws HibernateException {
        return (Serializable) deepCopy(value);
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean equals(final Object x, final Object y) throws HibernateException {
        if (x == null || y == null)
            return false;

        if (!(x instanceof StatusType) || !(y instanceof StatusType))
            return false;

        return ((StatusType) x).getCharCode() == ((StatusType) y).getCharCode();
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
     */
    @Override
    public int hashCode(final Object x) throws HibernateException {
        return x.hashCode();
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#isMutable()
     */
    @Override
    public boolean isMutable() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
     */
    @Override
    public Object nullSafeGet(final ResultSet rs, final String[] names, final Object owner) throws HibernateException,
            SQLException {
        return StatusType.get(Hibernate.CHARACTER.nullSafeGet(rs, names[0]).toString());
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
     */
    @Override
    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
            SQLException {
        if (value == null) {
            Hibernate.CHARACTER.nullSafeSet(st, null, index);
        } else if (value instanceof StatusType) {
            Hibernate.CHARACTER.nullSafeSet(st, new Character(((StatusType) value).getCharCode()), index);
        } else if (value instanceof String) {
            try {
                Hibernate.CHARACTER.nullSafeSet(st, new Character(StatusType.get((String) value).getCharCode()), index);
            } catch (final IllegalArgumentException e) {
                Hibernate.CHARACTER.nullSafeSet(st, new Character(((String) value).charAt(0)), index);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
        return original;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#returnedClass()
     */
    @Override
    public Class<StatusType> returnedClass() {
        return StatusType.class;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

}
