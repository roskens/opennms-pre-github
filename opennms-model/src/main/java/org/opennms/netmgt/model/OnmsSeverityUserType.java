/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * The Class OnmsSeverityUserType.
 */
public class OnmsSeverityUserType implements UserType {

    /**
     * A public default constructor is required by Hibernate.
     */
    public OnmsSeverityUserType() {
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
     */
    @Override
    public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
     */
    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        if (value == null) {
            return null;
        } else if (value instanceof OnmsSeverity) {
            // immutable, we can just return the value
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
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        return x.equals(y);
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
        return OnmsSeverity.get(rs.getInt(names[0]));
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
     */
    @Override
    public void nullSafeSet(final PreparedStatement st, final Object value, final int index) throws HibernateException,
            SQLException {
        if (value == null) {
            st.setInt(index, 1);
        } else if (value instanceof OnmsSeverity) {
            st.setInt(index, ((OnmsSeverity) value).getId());
        } else if (value instanceof String) {
            try {
                st.setInt(index, Integer.parseInt((String) value));
            } catch (final IllegalArgumentException e) {
                throw new HibernateException("unable to set severity " + value, e);
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
    public Class<OnmsSeverity> returnedClass() {
        return OnmsSeverity.class;
    }

    /* (non-Javadoc)
     * @see org.hibernate.usertype.UserType#sqlTypes()
     */
    @Override
    public int[] sqlTypes() {
        return new int[] { java.sql.Types.INTEGER };
    }

}
