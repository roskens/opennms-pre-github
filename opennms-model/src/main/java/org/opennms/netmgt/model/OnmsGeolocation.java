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
package org.opennms.netmgt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The Class OnmsGeolocation.
 */
@Embeddable
public class OnmsGeolocation implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3859935145186027524L;

    /**
     * Instantiates a new onms geolocation.
     */
    public OnmsGeolocation() {
    }

    /** The m_address1. */
    private String m_address1;

    /** The m_address2. */
    private String m_address2;

    /** The m_city. */
    private String m_city;

    /** The m_state. */
    private String m_state;

    /** The m_zip. */
    private String m_zip;

    /** The m_country. */
    private String m_country;

    /** The m_longitude. */
    private Float m_longitude;

    /** The m_latitude. */
    private Float m_latitude;

    /**
     * --# address1 : Address of geographical location of asset, line 1.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name = "address1", length = 256)
    public String getAddress1() {
        return m_address1;
    }

    /**
     * <p>
     * setAddress1
     * </p>
     * .
     *
     * @param address1
     *            a {@link java.lang.String} object.
     */
    public void setAddress1(String address1) {
        m_address1 = address1;
    }

    /**
     * --# address2 : Address of geographical location of asset, line 2.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name = "address2", length = 256)
    public String getAddress2() {
        return m_address2;
    }

    /**
     * <p>
     * setAddress2
     * </p>
     * .
     *
     * @param address2
     *            a {@link java.lang.String} object.
     */
    public void setAddress2(String address2) {
        m_address2 = address2;
    }

    /**
     * --# city : The city where this asset resides.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name = "city", length = 64)
    public String getCity() {
        return m_city;
    }

    /**
     * <p>
     * setCity
     * </p>
     * .
     *
     * @param city
     *            a {@link java.lang.String} object.
     */
    public void setCity(String city) {
        m_city = city;
    }

    /**
     * --# state : The state where this asset resides.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name = "state", length = 64)
    public String getState() {
        return m_state;
    }

    /**
     * <p>
     * setState
     * </p>
     * .
     *
     * @param state
     *            a {@link java.lang.String} object.
     */
    public void setState(String state) {
        m_state = state;
    }

    /**
     * --# zip : The zip code where this asset resides.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name = "zip", length = 64)
    public String getZip() {
        return m_zip;
    }

    /**
     * <p>
     * setZip
     * </p>
     * .
     *
     * @param zip
     *            a {@link java.lang.String} object.
     */
    public void setZip(String zip) {
        m_zip = zip;
    }

    /**
     * --# country : The country where this asset resides.
     *
     * @return a {@link java.lang.String} object.
     */
    @Column(name = "country", length = 64)
    public String getCountry() {
        return m_country;
    }

    /**
     * <p>
     * setCountry
     * </p>
     * .
     *
     * @param country
     *            a {@link java.lang.String} object.
     */
    public void setCountry(String country) {
        m_country = country;
    }

    /**
     * The longitude coordinate of this node.
     *
     * @return the longitude
     */
    @Column(name = "longitude")
    public Float getLongitude() {
        return m_longitude;
    }

    /**
     * Sets the longitude.
     *
     * @param longitude
     *            the new longitude
     */
    public void setLongitude(final Float longitude) {
        m_longitude = longitude;
    }

    /**
     * The latitude coordinate of this node.
     *
     * @return the latitude
     */
    @Column(name = "latitude")
    public Float getLatitude() {
        return m_latitude;
    }

    /**
     * Sets the latitude.
     *
     * @param latitude
     *            the new latitude
     */
    public void setLatitude(final Float latitude) {
        m_latitude = latitude;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OnmsGeolocation[" + this.asAddressString() + "]";
    }

    /**
     * As address string.
     *
     * @return the string
     */
    public String asAddressString() {
        final StringBuffer sb = new StringBuffer();

        if (this.getAddress1() != null) {
            sb.append(this.getAddress1());
            if (this.getAddress2() != null) {
                sb.append(" ").append(this.getAddress2());
            }
        }

        if (this.getCity() != null) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(this.getCity());
        }
        if (this.getState() != null) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(this.getState());
        }
        if (this.getZip() != null) {
            if (this.getState() != null) {
                sb.append(" ");
            } else if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(this.getZip());
        }
        if (this.getCountry() != null) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(this.getCountry());
        }

        return sb.toString();
    }

}
