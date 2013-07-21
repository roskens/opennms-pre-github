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

import org.opennms.core.criteria.Criteria.CriteriaVisitor;
import org.opennms.core.criteria.restrictions.Restriction;

/**
 * The Class AbstractCriteriaVisitor.
 */
public class AbstractCriteriaVisitor implements CriteriaVisitor {

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitClass(java.lang.Class)
     */
    @Override
    public void visitClass(final Class<?> clazz) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitOrder(org.opennms.core.criteria.Order)
     */
    @Override
    public void visitOrder(final Order order) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitOrdersFinished()
     */
    @Override
    public void visitOrdersFinished() {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitAlias(org.opennms.core.criteria.Alias)
     */
    @Override
    public void visitAlias(final Alias alias) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitAliasesFinished()
     */
    @Override
    public void visitAliasesFinished() {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitFetch(org.opennms.core.criteria.Fetch)
     */
    @Override
    public void visitFetch(final Fetch fetch) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitFetchesFinished()
     */
    @Override
    public void visitFetchesFinished() {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitRestriction(org.opennms.core.criteria.restrictions.Restriction)
     */
    @Override
    public void visitRestriction(final Restriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitRestrictionsFinished()
     */
    @Override
    public void visitRestrictionsFinished() {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitDistinct(boolean)
     */
    @Override
    public void visitDistinct(final boolean distinct) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitLimit(java.lang.Integer)
     */
    @Override
    public void visitLimit(final Integer limit) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.Criteria.CriteriaVisitor#visitOffset(java.lang.Integer)
     */
    @Override
    public void visitOffset(final Integer offset) {
    }

}
