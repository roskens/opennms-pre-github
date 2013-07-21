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
 * The Interface MapViewManager.
 */
public interface MapViewManager {

    /**
     * Adds the listener.
     *
     * @param listener
     *            the listener
     */
    public void addListener(MapViewManagerListener listener);

    /**
     * Removes the listener.
     *
     * @param listener
     *            the listener
     */
    public void removeListener(MapViewManagerListener listener);

    /**
     * Sets the map bounds.
     *
     * @param boundingBox
     *            the new map bounds
     */
    public void setMapBounds(BoundingBox boundingBox);

    /**
     * Sets the bounding box.
     *
     * @param boundingBox
     *            the new bounding box
     */
    public void setBoundingBox(BoundingBox boundingBox);

    /**
     * Sets the view port.
     *
     * @param width
     *            the width
     * @param height
     *            the height
     */
    public void setViewPort(int width, int height);

    /**
     * Gets the view port aspect ratio.
     *
     * @return the view port aspect ratio
     */
    public double getViewPortAspectRatio();

    /**
     * Sets the center.
     *
     * @param point
     *            the new center
     */
    public void setCenter(Point point);

    /**
     * Zoom to point.
     *
     * @param scale
     *            the scale
     * @param center
     *            the center
     */
    public void zoomToPoint(double scale, Point center);

    /**
     * Gets the current bounding box.
     *
     * @return the current bounding box
     */
    public BoundingBox getCurrentBoundingBox();

    /**
     * Gets the scale.
     *
     * @return the scale
     */
    public double getScale();

    /**
     * Sets the scale.
     *
     * @param scale
     *            the new scale
     */
    public void setScale(double scale);

    /**
     * Gets the view port height.
     *
     * @return the view port height
     */
    public int getViewPortHeight();

    /**
     * Gets the view port width.
     *
     * @return the view port width
     */
    public int getViewPortWidth();
}
