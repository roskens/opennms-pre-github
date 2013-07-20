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
package org.opennms.features.geocoder.google;

import org.opennms.features.geocoder.Coordinates;
import org.opennms.features.geocoder.GeocoderException;
import org.opennms.features.geocoder.GeocoderService;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;

/**
 * The Class GoogleGeocoderService.
 */
public class GoogleGeocoderService implements GeocoderService {

    /** The m_geocoder. */
    private final Geocoder m_geocoder = new Geocoder();

    /* (non-Javadoc)
     * @see org.opennms.features.geocoder.GeocoderService#getCoordinates(java.lang.String)
     */
    @Override
    public Coordinates getCoordinates(final String address) throws GeocoderException {
        final GeocoderRequest request = new GeocoderRequestBuilder().setAddress(address).setLanguage("en").getGeocoderRequest();
        final GeocodeResponse response = m_geocoder.geocode(request);

        switch (response.getStatus()) {
        case OK:
            return new GoogleCoordinates(response.getResults().get(0));
        case ERROR:
        case INVALID_REQUEST:
        case OVER_QUERY_LIMIT:
        case REQUEST_DENIED:
        case UNKNOWN_ERROR:
        case ZERO_RESULTS:
        default:
            throw new GeocoderException("Failed to get coordinates for " + address
                    + " using Google Geocoder.  Response was: " + response.getStatus().toString());
        }
    }
}
