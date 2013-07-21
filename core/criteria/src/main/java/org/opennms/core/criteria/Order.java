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

package org.opennms.core.criteria;

/**
 * The Class Order.
 */
public class Order {

    /**
     * The Interface OrderVisitor.
     */
    public static interface OrderVisitor {

        /**
         * Visit attribute.
         *
         * @param attribute
         *            the attribute
         */
        public void visitAttribute(final String attribute);

        /**
         * Visit ascending.
         *
         * @param ascending
         *            the ascending
         */
        public void visitAscending(final boolean ascending);
    }

    /** The m_attribute. */
    private final String m_attribute;

    /** The m_ascending. */
    private final boolean m_ascending;

    /**
     * Instantiates a new order.
     *
     * @param attribute
     *            the attribute
     * @param ascending
     *            the ascending
     */
    public Order(final String attribute, boolean ascending) {
        m_attribute = attribute;
        m_ascending = ascending;
    }

    /**
     * Visit.
     *
     * @param visitor
     *            the visitor
     */
    public void visit(final OrderVisitor visitor) {
        visitor.visitAttribute(getAttribute());
        visitor.visitAscending(asc());
    }

    /**
     * Gets the attribute.
     *
     * @return the attribute
     */
    public String getAttribute() {
        return m_attribute;
    }

    /**
     * Asc.
     *
     * @return true, if successful
     */
    public boolean asc() {
        return m_ascending;
    }

    /**
     * Desc.
     *
     * @return true, if successful
     */
    public boolean desc() {
        return !m_ascending;
    }

    /**
     * Asc.
     *
     * @param attribute
     *            the attribute
     * @return the order
     */
    public static Order asc(final String attribute) {
        return new Order(attribute, true);
    }

    /**
     * Desc.
     *
     * @param attribute
     *            the attribute
     * @return the order
     */
    public static Order desc(final String attribute) {
        return new Order(attribute, false);
    }

    /*
     * we don't include m_ascending since a single order attribute should only
     * be used once
     */
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        // result = prime * result + (m_ascending ? 1231 : 1237);
        result = prime * result + ((m_attribute == null) ? 0 : m_attribute.hashCode());
        return result;
    }

    /*
     * we don't include m_ascending since a single order attribute should only
     * be used once
     */
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Order))
            return false;
        final Order other = (Order) obj;
        // if (m_ascending != other.m_ascending) return false;
        if (m_attribute == null) {
            if (other.m_attribute != null)
                return false;
        } else if (!m_attribute.equals(other.m_attribute)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Order [attribute=" + m_attribute + ", ascending=" + m_ascending + "]";
    }

}
