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
 * The Interface RestrictionVisitor.
 */
public interface RestrictionVisitor {

    /**
     * Visit null.
     *
     * @param restriction
     *            the restriction
     */
    void visitNull(final NullRestriction restriction);

    /**
     * Visit null complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitNullComplete(final NullRestriction restriction);

    /**
     * Visit not null.
     *
     * @param restriction
     *            the restriction
     */
    void visitNotNull(final NotNullRestriction restriction);

    /**
     * Visit not null complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitNotNullComplete(final NotNullRestriction restriction);

    /**
     * Visit eq.
     *
     * @param restriction
     *            the restriction
     */
    void visitEq(final EqRestriction restriction);

    /**
     * Visit eq complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitEqComplete(final EqRestriction restriction);

    /**
     * Visit ne.
     *
     * @param restriction
     *            the restriction
     */
    void visitNe(final NeRestriction restriction);

    /**
     * Visit ne complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitNeComplete(final NeRestriction restriction);

    /**
     * Visit gt.
     *
     * @param restriction
     *            the restriction
     */
    void visitGt(final GtRestriction restriction);

    /**
     * Visit gt complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitGtComplete(final GtRestriction restriction);

    /**
     * Visit ge.
     *
     * @param restriction
     *            the restriction
     */
    void visitGe(final GeRestriction restriction);

    /**
     * Visit ge complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitGeComplete(final GeRestriction restriction);

    /**
     * Visit lt.
     *
     * @param restriction
     *            the restriction
     */
    void visitLt(final LtRestriction restriction);

    /**
     * Visit lt complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitLtComplete(final LtRestriction restriction);

    /**
     * Visit le.
     *
     * @param restriction
     *            the restriction
     */
    void visitLe(final LeRestriction restriction);

    /**
     * Visit le complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitLeComplete(final LeRestriction restriction);

    /**
     * Visit all.
     *
     * @param restriction
     *            the restriction
     */
    void visitAll(final AllRestriction restriction);

    /**
     * Visit all complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitAllComplete(final AllRestriction restriction);

    /**
     * Visit any.
     *
     * @param restriction
     *            the restriction
     */
    void visitAny(final AnyRestriction restriction);

    /**
     * Visit any complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitAnyComplete(final AnyRestriction restriction);

    /**
     * Visit like.
     *
     * @param restriction
     *            the restriction
     */
    void visitLike(final LikeRestriction restriction);

    /**
     * Visit like complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitLikeComplete(final LikeRestriction restriction);

    /**
     * Visit ilike.
     *
     * @param restriction
     *            the restriction
     */
    void visitIlike(final IlikeRestriction restriction);

    /**
     * Visit ilike complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitIlikeComplete(final IlikeRestriction restriction);

    /**
     * Visit in.
     *
     * @param restriction
     *            the restriction
     */
    void visitIn(final InRestriction restriction);

    /**
     * Visit in complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitInComplete(final InRestriction restriction);

    /**
     * Visit not.
     *
     * @param restriction
     *            the restriction
     */
    void visitNot(final NotRestriction restriction);

    /**
     * Visit not complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitNotComplete(final NotRestriction restriction);

    /**
     * Visit between.
     *
     * @param restriction
     *            the restriction
     */
    void visitBetween(final BetweenRestriction restriction);

    /**
     * Visit between complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitBetweenComplete(final BetweenRestriction restriction);

    /**
     * Visit sql.
     *
     * @param restriction
     *            the restriction
     */
    void visitSql(final SqlRestriction restriction);

    /**
     * Visit sql complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitSqlComplete(final SqlRestriction restriction);

    /**
     * Visit iplike.
     *
     * @param restriction
     *            the restriction
     */
    void visitIplike(final IplikeRestriction restriction);

    /**
     * Visit iplike complete.
     *
     * @param restriction
     *            the restriction
     */
    void visitIplikeComplete(final IplikeRestriction restriction);
}
