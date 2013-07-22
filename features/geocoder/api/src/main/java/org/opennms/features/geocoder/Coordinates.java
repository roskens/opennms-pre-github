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
package org.opennms.features.geocoder;

import java.io.Serializable;

/**
 * The Class Coordinates.
 */
public class Coordinates implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2079876989978267336L;

    /** The Constant BAD_COORDINATES. */
    public static final String BAD_COORDINATES = Integer.MIN_VALUE + "," + Integer.MIN_VALUE;

    /** The m_longitude. */
    private float m_longitude;

    /** The m_latitude. */
    private float m_latitude;

    /**
     * Instantiates a new coordinates.
     */
    public Coordinates() {
    }

    /**
     * Instantiates a new coordinates.
     *
     * @param lonLat
     *            the lon lat
     * @throws GeocoderException
     *             the geocoder exception
     */
    public Coordinates(final String lonLat) throws GeocoderException {
        setCoordinates(lonLat);
    }

    /**
     * Instantiates a new coordinates.
     *
     * @param longitude
     *            the longitude
     * @param latitude
     *            the latitude
     */
    public Coordinates(final float longitude, final float latitude) {
        setCoordinates(longitude, latitude);
    }

    /**
     * Split comma separated floats.
     *
     * @param coordinateString
     *            the coordinate string
     * @return the float[]
     * @throws GeocoderException
     *             the geocoder exception
     */
    public static Float[] splitCommaSeparatedFloats(final String coordinateString) throws GeocoderException {
        final String[] separated = coordinateString.split(",");
        final Float[] coordinates;
        try {
            coordinates = new Float[] { Float.valueOf(separated[0]), Float.valueOf(separated[1]) };
        } catch (final NumberFormatException e) {
            throw new GeocoderException("Failed to parse coordinate string '" + coordinateString + "'", e);
        }
        return coordinates;
    }

    /**
     * Sets the coordinates.
     *
     * @param lonLat
     *            the new coordinates
     * @throws GeocoderException
     *             the geocoder exception
     */
    protected void setCoordinates(final String lonLat) throws GeocoderException {
        if (lonLat == null) {
            throw new GeocoderException("Attempt to initialize a Coordinate with a null lon/lat string!");
        }

        final Float[] coordinates = splitCommaSeparatedFloats(lonLat);
        m_longitude = coordinates[0].floatValue();
        m_latitude = coordinates[1].floatValue();
    }

    /**
     * Sets the coordinates.
     *
     * @param longitude
     *            the longitude
     * @param latitude
     *            the latitude
     */
    protected void setCoordinates(final float longitude, final float latitude) {
        m_longitude = longitude;
        m_latitude = latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public float getLongitude() {
        return m_longitude;
    }

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public float getLatitude() {
        return m_latitude;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return m_longitude + "," + m_latitude;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(m_longitude);
        result = prime * result + Float.floatToIntBits(m_latitude);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Coordinates)) {
            return false;
        }
        final Coordinates other = (Coordinates) obj;
        if (Float.floatToIntBits(m_longitude) != Float.floatToIntBits(other.m_longitude)) {
            return false;
        }
        if (Float.floatToIntBits(m_latitude) != Float.floatToIntBits(other.m_latitude)) {
            return false;
        }
        return true;
    }

}
