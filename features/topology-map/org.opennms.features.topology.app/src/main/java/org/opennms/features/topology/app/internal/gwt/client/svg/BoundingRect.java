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

package org.opennms.features.topology.app.internal.gwt.client.svg;

import com.google.gwt.touch.client.Point;

/**
 * The Class BoundingRect.
 */
public class BoundingRect {

    /** The m_top left. */
    Point m_topLeft;

    /** The m_bottom right. */
    Point m_bottomRight;

    /**
     * Gets the center x.
     *
     * @return the center x
     */
    public double getCenterX() {
        return (m_topLeft.getX() + m_bottomRight.getX()) / 2;
    }

    /**
     * Gets the center y.
     *
     * @return the center y
     */
    public double getCenterY() {
        return (m_topLeft.getY() + m_bottomRight.getY()) / 2;
    }

    /**
     * Gets the width.
     *
     * @return the width
     */
    public double getWidth() {
        double width = m_bottomRight.getX() - m_topLeft.getX() + 100;

        return width;
    }

    /**
     * Gets the height.
     *
     * @return the height
     */
    public double getHeight() {
        return m_bottomRight.getY() - m_topLeft.getY() + 100;
    }

    /**
     * Adds the point.
     *
     * @param point
     *            the point
     */
    public void addPoint(Point point) {
        if (m_topLeft == null) {
            m_topLeft = point;
        } else {
            m_topLeft = new Point(Math.min(m_topLeft.getX(), point.getX()), Math.min(m_topLeft.getY(), point.getY()));
        }

        if (m_bottomRight == null) {
            m_bottomRight = point;
        } else {
            m_bottomRight = new Point(Math.max(m_bottomRight.getX(), point.getX()), Math.max(m_bottomRight.getY(),
                                                                                             point.getY()));
        }
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    public boolean isEmpty() {
        return m_bottomRight == null && m_topLeft == null;
    }

}
