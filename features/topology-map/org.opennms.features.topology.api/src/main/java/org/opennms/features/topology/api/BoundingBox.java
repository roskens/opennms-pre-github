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

package org.opennms.features.topology.api;

/**
 * The Class BoundingBox.
 */
public class BoundingBox {

    /** The m_left. */
    private int m_left = Integer.MAX_VALUE;

    /** The m_top. */
    private int m_top = Integer.MAX_VALUE;

    /** The m_right. */
    private int m_right = Integer.MIN_VALUE;

    /** The m_bottom. */
    private int m_bottom = Integer.MIN_VALUE;

    /**
     * Instantiates a new bounding box.
     */
    public BoundingBox() {

    }

    /**
     * Instantiates a new bounding box.
     *
     * @param x
     *            the x
     * @param y
     *            the y
     * @param width
     *            the width
     * @param height
     *            the height
     */
    public BoundingBox(int x, int y, int width, int height) {
        m_left = x;
        m_top = y;
        m_right = x + width;
        m_bottom = y + height;
    }

    /**
     * Instantiates a new bounding box.
     *
     * @param center
     *            the center
     * @param width
     *            the width
     * @param height
     *            the height
     */
    public BoundingBox(Point center, int width, int height) {
        m_left = center.getX() - width / 2;
        m_top = center.getY() - height / 2;
        m_right = m_left + width;
        m_bottom = m_top + height;
    }

    /**
     * Gets the x.
     *
     * @return the x
     */
    public int getX() {
        return m_left;
    }

    /**
     * Gets the y.
     *
     * @return the y
     */
    public int getY() {
        return m_top;
    }

    /**
     * Gets the width.
     *
     * @return the width
     */
    public int getWidth() {
        return m_right - m_left;
    }

    /**
     * Gets the height.
     *
     * @return the height
     */
    public int getHeight() {
        return m_bottom - m_top;
    }

    /**
     * Adds the point.
     *
     * @param location
     *            the location
     */
    public void addPoint(Point location) {
        m_left = Math.min(m_left, location.getX());
        m_right = Math.max(m_right, location.getX());
        m_top = Math.min(m_top, location.getY());
        m_bottom = Math.max(m_bottom, location.getY());
    }

    /**
     * Gets the center.
     *
     * @return the center
     */
    public Point getCenter() {
        Point p = new Point(getX() + (getWidth() / 2), getY() + (getHeight() / 2));
        return p;
    }

    /**
     * Sets the center.
     *
     * @param center
     *            the new center
     */
    public void setCenter(Point center) {
        m_left = center.getX() - getWidth() / 2;
        m_top = center.getY() - getHeight() / 2;
    }

    /**
     * Compute with aspect ratio.
     *
     * @param R
     *            the r
     * @return the bounding box
     */
    public BoundingBox computeWithAspectRatio(double R) {
        double r = getAspectRatio();
        int width = (int) (r < R ? Math.round(getHeight() * R) : getWidth());
        int height = (int) (r < R ? getHeight() : Math.round(getWidth() / R));
        Point center = getCenter();
        int x = center.getX() - width / 2;
        int y = center.getY() - height / 2;
        return new BoundingBox(x, y, width, height);
    }

    /**
     * Gets the aspect ratio.
     *
     * @return the aspect ratio
     */
    private double getAspectRatio() {
        return getHeight() == 0 ? 0 : (double) getWidth() / (double) getHeight();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "x: " + getX() + " y: " + getY() + " width: " + getWidth() + " height: " + getHeight();
    }

    /**
     * Fragment.
     *
     * @return the string
     */
    public String fragment() {
        return "(" + getX() + "," + getY() + "," + getWidth() + "," + getHeight() + ")";
    }

    /**
     * Adds the boundingbox.
     *
     * @param box
     *            the box
     */
    public void addBoundingbox(BoundingBox box) {
        m_left = Math.min(m_left, box.m_left);
        m_right = Math.max(m_right, box.m_right);
        m_top = Math.min(m_top, box.m_top);
        m_bottom = Math.max(m_bottom, box.m_bottom);

    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + m_bottom;
        result = prime * result + m_left;
        result = prime * result + m_right;
        result = prime * result + m_top;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoundingBox other = (BoundingBox) obj;
        if (m_bottom != other.m_bottom)
            return false;
        if (m_left != other.m_left)
            return false;
        if (m_right != other.m_right)
            return false;
        if (m_top != other.m_top)
            return false;
        return true;
    }

}
