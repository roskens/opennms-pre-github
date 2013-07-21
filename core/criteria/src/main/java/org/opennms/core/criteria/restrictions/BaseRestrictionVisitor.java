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

package org.opennms.core.criteria.restrictions;

/**
 * The Class BaseRestrictionVisitor.
 */
public class BaseRestrictionVisitor implements RestrictionVisitor {

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitNull(org.opennms.core.criteria.restrictions.NullRestriction)
     */
    @Override
    public void visitNull(final NullRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitNullComplete(org.opennms.core.criteria.restrictions.NullRestriction)
     */
    @Override
    public void visitNullComplete(final NullRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitNotNull(org.opennms.core.criteria.restrictions.NotNullRestriction)
     */
    @Override
    public void visitNotNull(final NotNullRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitNotNullComplete(org.opennms.core.criteria.restrictions.NotNullRestriction)
     */
    @Override
    public void visitNotNullComplete(final NotNullRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitEq(org.opennms.core.criteria.restrictions.EqRestriction)
     */
    @Override
    public void visitEq(final EqRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitEqComplete(org.opennms.core.criteria.restrictions.EqRestriction)
     */
    @Override
    public void visitEqComplete(final EqRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitNe(org.opennms.core.criteria.restrictions.NeRestriction)
     */
    @Override
    public void visitNe(final NeRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitNeComplete(org.opennms.core.criteria.restrictions.NeRestriction)
     */
    @Override
    public void visitNeComplete(final NeRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitGt(org.opennms.core.criteria.restrictions.GtRestriction)
     */
    @Override
    public void visitGt(final GtRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitGtComplete(org.opennms.core.criteria.restrictions.GtRestriction)
     */
    @Override
    public void visitGtComplete(final GtRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitGe(org.opennms.core.criteria.restrictions.GeRestriction)
     */
    @Override
    public void visitGe(final GeRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitGeComplete(org.opennms.core.criteria.restrictions.GeRestriction)
     */
    @Override
    public void visitGeComplete(final GeRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitLt(org.opennms.core.criteria.restrictions.LtRestriction)
     */
    @Override
    public void visitLt(final LtRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitLtComplete(org.opennms.core.criteria.restrictions.LtRestriction)
     */
    @Override
    public void visitLtComplete(final LtRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitLe(org.opennms.core.criteria.restrictions.LeRestriction)
     */
    @Override
    public void visitLe(final LeRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitLeComplete(org.opennms.core.criteria.restrictions.LeRestriction)
     */
    @Override
    public void visitLeComplete(final LeRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitAll(org.opennms.core.criteria.restrictions.AllRestriction)
     */
    @Override
    public void visitAll(final AllRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitAllComplete(org.opennms.core.criteria.restrictions.AllRestriction)
     */
    @Override
    public void visitAllComplete(final AllRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitAny(org.opennms.core.criteria.restrictions.AnyRestriction)
     */
    @Override
    public void visitAny(final AnyRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitAnyComplete(org.opennms.core.criteria.restrictions.AnyRestriction)
     */
    @Override
    public void visitAnyComplete(final AnyRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitLike(org.opennms.core.criteria.restrictions.LikeRestriction)
     */
    @Override
    public void visitLike(final LikeRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitLikeComplete(org.opennms.core.criteria.restrictions.LikeRestriction)
     */
    @Override
    public void visitLikeComplete(final LikeRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitIlike(org.opennms.core.criteria.restrictions.IlikeRestriction)
     */
    @Override
    public void visitIlike(final IlikeRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitIlikeComplete(org.opennms.core.criteria.restrictions.IlikeRestriction)
     */
    @Override
    public void visitIlikeComplete(final IlikeRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitIn(org.opennms.core.criteria.restrictions.InRestriction)
     */
    @Override
    public void visitIn(final InRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitInComplete(org.opennms.core.criteria.restrictions.InRestriction)
     */
    @Override
    public void visitInComplete(final InRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitNot(org.opennms.core.criteria.restrictions.NotRestriction)
     */
    @Override
    public void visitNot(final NotRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitNotComplete(org.opennms.core.criteria.restrictions.NotRestriction)
     */
    @Override
    public void visitNotComplete(final NotRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitBetween(org.opennms.core.criteria.restrictions.BetweenRestriction)
     */
    @Override
    public void visitBetween(final BetweenRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitBetweenComplete(org.opennms.core.criteria.restrictions.BetweenRestriction)
     */
    @Override
    public void visitBetweenComplete(final BetweenRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitSql(org.opennms.core.criteria.restrictions.SqlRestriction)
     */
    @Override
    public void visitSql(final SqlRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitSqlComplete(org.opennms.core.criteria.restrictions.SqlRestriction)
     */
    @Override
    public void visitSqlComplete(final SqlRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitIplike(org.opennms.core.criteria.restrictions.IplikeRestriction)
     */
    @Override
    public void visitIplike(final IplikeRestriction restriction) {
    }

    /* (non-Javadoc)
     * @see org.opennms.core.criteria.restrictions.RestrictionVisitor#visitIplikeComplete(org.opennms.core.criteria.restrictions.IplikeRestriction)
     */
    @Override
    public void visitIplikeComplete(final IplikeRestriction restriction) {
    }

}
