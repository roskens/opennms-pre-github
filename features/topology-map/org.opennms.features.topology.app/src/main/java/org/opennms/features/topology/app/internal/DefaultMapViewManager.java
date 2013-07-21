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
package org.opennms.features.topology.app.internal;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.opennms.features.topology.api.BoundingBox;
import org.opennms.features.topology.api.MapViewManager;
import org.opennms.features.topology.api.MapViewManagerListener;
import org.opennms.features.topology.api.Point;

/**
 * The Class DefaultMapViewManager.
 */
public class DefaultMapViewManager implements MapViewManager {

    /** The m_map bounds. */
    private BoundingBox m_mapBounds = new BoundingBox(0, 0, 100, 100);

    /** The m_view port width. */
    private int m_viewPortWidth = 100;

    /** The m_view port height. */
    private int m_viewPortHeight = 100;

    /** The m_scale. */
    private double m_scale = 0.0;

    /** The m_center. */
    private Point m_center = new Point(0, 0);

    /** The m_listeners. */
    private Set<MapViewManagerListener> m_listeners = new CopyOnWriteArraySet<MapViewManagerListener>();

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#addListener(org.opennms.features.topology.api.MapViewManagerListener)
     */
    @Override
    public void addListener(MapViewManagerListener listener) {
        m_listeners.add(listener);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#removeListener(org.opennms.features.topology.api.MapViewManagerListener)
     */
    @Override
    public void removeListener(MapViewManagerListener listener) {
        m_listeners.remove(listener);
    }

    /**
     * Fire update.
     */
    private void fireUpdate() {
        for (MapViewManagerListener listener : m_listeners) {
            listener.boundingBoxChanged(this);
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#setMapBounds(org.opennms.features.topology.api.BoundingBox)
     */
    @Override
    public void setMapBounds(BoundingBox boundingBox) {
        m_mapBounds = boundingBox;
        m_center = m_mapBounds.getCenter();

        fireUpdate();
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#setViewPort(int, int)
     */
    @Override
    public void setViewPort(int width, int height) {
        int oldWidth = m_viewPortWidth;
        int oldHeight = m_viewPortHeight;

        m_viewPortWidth = width;
        m_viewPortHeight = height;

        if (oldWidth != m_viewPortWidth || oldHeight != m_viewPortHeight) {
            fireUpdate();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#getViewPortAspectRatio()
     */
    @Override
    public double getViewPortAspectRatio() {
        return m_viewPortWidth < 0 ? -1 : m_viewPortWidth / (double) m_viewPortHeight;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#setCenter(org.opennms.features.topology.api.Point)
     */
    @Override
    public void setCenter(Point point) {
        Point oldCenter = m_center;
        m_center = point;
        if (!oldCenter.equals(m_center)) {
            fireUpdate();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#zoomToPoint(double, org.opennms.features.topology.api.Point)
     */
    @Override
    public void zoomToPoint(double scale, Point center) {
        double oldScale = m_scale;
        m_scale = scale;
        m_scale = Math.min(1.0, m_scale);
        m_scale = Math.max(0.0, m_scale);
        m_scale = ((int) Math.round(m_scale * 10)) / 10.0;
        Point oldCenter = m_center;
        m_center = center;

        if (m_scale != oldScale || !oldCenter.equals(m_center)) {
            fireUpdate();
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#getCurrentBoundingBox()
     */
    @Override
    public BoundingBox getCurrentBoundingBox() {
        if (m_viewPortWidth < 0 || m_mapBounds == null) {
            // return m_mapBounds;
            // throw new
            // IllegalStateException("View port and maps bounds must be set");
        }
        BoundingBox mPrime = m_mapBounds.computeWithAspectRatio(getViewPortAspectRatio());
        int width = (int) (Math.pow(mPrime.getWidth(), 1 - m_scale) * Math.pow(m_viewPortWidth / 2, m_scale));
        int height = (int) (Math.pow(mPrime.getHeight(), 1 - m_scale) * Math.pow(m_viewPortHeight / 2, m_scale));

        return new BoundingBox(m_center, width, height);
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#getScale()
     */
    @Override
    public double getScale() {
        return m_scale;

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#setScale(double)
     */
    @Override
    public void setScale(double scale) {
        double oldScale = m_scale;
        m_scale = scale;
        m_scale = Math.min(1.0, m_scale);
        m_scale = Math.max(0.0, m_scale);
        if (oldScale != m_scale) {
            fireUpdate();
        }

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#setBoundingBox(org.opennms.features.topology.api.BoundingBox)
     */
    @Override
    public void setBoundingBox(BoundingBox boundingBox) {
        BoundingBox oldBoundingBox = getCurrentBoundingBox();
        BoundingBox bbPrime = boundingBox.computeWithAspectRatio(getViewPortAspectRatio());
        BoundingBox mPrime = m_mapBounds.computeWithAspectRatio(getViewPortAspectRatio());
        double oldScale = m_scale;
        m_scale = Math.log(bbPrime.getWidth() / (double) mPrime.getWidth())
                / Math.log((m_viewPortWidth / 2.0) / (double) mPrime.getWidth());
        m_scale = Math.min(1.0, m_scale);
        m_scale = Math.max(0.0, m_scale);
        m_scale = (int) (Math.round(m_scale * 10)) / 10.0;

        Point oldCenter = m_center;
        m_center = boundingBox.getCenter();

        BoundingBox newBoundingBox = getCurrentBoundingBox();
        if (!oldCenter.equals(m_center) || oldScale != m_scale || !oldBoundingBox.equals(newBoundingBox)) {
            fireUpdate();
        }

    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#getViewPortHeight()
     */
    @Override
    public int getViewPortHeight() {
        return m_viewPortHeight;
    }

    /* (non-Javadoc)
     * @see org.opennms.features.topology.api.MapViewManager#getViewPortWidth()
     */
    @Override
    public int getViewPortWidth() {
        return m_viewPortWidth;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Map bounds [ " + m_mapBounds + "]  || view [ width: " + getViewPortWidth() + " :: height: "
                + getViewPortHeight() + " ] || currentBoundingBox: [ " + getCurrentBoundingBox() + " ]" + "  scale: "
                + getScale() + " || centerPoint: [ " + m_center + " ]";
    }
}
