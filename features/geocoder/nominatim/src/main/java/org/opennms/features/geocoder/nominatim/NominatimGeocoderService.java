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
package org.opennms.features.geocoder.nominatim;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import net.simon04.jelementtree.ElementTree;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.opennms.features.geocoder.Coordinates;
import org.opennms.features.geocoder.GeocoderException;
import org.opennms.features.geocoder.GeocoderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class NominatimGeocoderService.
 */
public class NominatimGeocoderService implements GeocoderService {

    /** The Constant GEOCODE_URL. */
    private static final String GEOCODE_URL = "http://open.mapquestapi.com/nominatim/v1/search?format=xml";

    /** The Constant m_httpClient. */
    private static final HttpClient m_httpClient = new DefaultHttpClient();

    /** The m_email address. */
    private String m_emailAddress;

    /** The m_referer. */
    private String m_referer;

    /** The m_log. */
    private Logger m_log = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new nominatim geocoder service.
     */
    public NominatimGeocoderService() {
    }

    /**
     * On init.
     */
    public void onInit() {
        if (m_emailAddress == null || "".equals(m_emailAddress)) {
            throw new UnsupportedOperationException("You must specify an email address for the Nominatim geocoder!");
        }
    }

    /* (non-Javadoc)
     * @see org.opennms.features.geocoder.GeocoderService#getCoordinates(java.lang.String)
     */
    @Override
    public Coordinates getCoordinates(final String address) throws GeocoderException {
        final HttpUriRequest method = new HttpGet(getUrl(address));
        method.addHeader("User-Agent", "OpenNMS-NominatimGeocoderService/1.0");
        if (m_referer != null && !"".equals(m_referer)) {
            method.addHeader("Referer", m_referer);
        }

        InputStream responseStream = null;
        try {
            responseStream = m_httpClient.execute(method).getEntity().getContent();
            final ElementTree tree = ElementTree.fromStream(responseStream);
            if (tree == null) {
                throw new GeocoderException(
                                            "an error occurred connecting to the Nominatim geocoding service (no XML tree was found)");
            }

            final List<ElementTree> places = tree.findAll("//place");
            if (places.size() > 1) {
                m_log.warn("More than one location returned for query: {}", address);
            } else if (places.size() == 0) {
                throw new GeocoderException("Nominatim returned an OK status code, but no places");
            }
            final ElementTree place = places.get(0);

            final Float longitude = Float.valueOf(place.getAttribute("lon"));
            final Float latitude = Float.valueOf(place.getAttribute("lat"));
            return new Coordinates(longitude, latitude);
        } catch (final GeocoderException e) {
            throw e;
        } catch (final Throwable e) {
            throw new GeocoderException("unable to get lon/lat from Nominatim", e);
        } finally {
            IOUtils.closeQuietly(responseStream);
        }
    }

    /**
     * Gets the url.
     *
     * @param geolocation
     *            the geolocation
     * @return the url
     * @throws GeocoderException
     *             the geocoder exception
     */
    private String getUrl(final String geolocation) throws GeocoderException {
        try {
            return GEOCODE_URL + "&q=" + URLEncoder.encode(geolocation, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new GeocoderException("unable to URL-encode query string", e);
        }
    }

    /**
     * Gets the email address.
     *
     * @return the email address
     */
    public String getEmailAddress() {
        return m_emailAddress;
    }

    /**
     * Sets the email address.
     *
     * @param emailAddress
     *            the new email address
     */
    public void setEmailAddress(final String emailAddress) {
        m_emailAddress = emailAddress;
    }

    /**
     * Gets the referer.
     *
     * @return the referer
     */
    public String getReferer() {
        return m_referer;
    }

    /**
     * Sets the referer.
     *
     * @param referer
     *            the new referer
     */
    public void setReferer(final String referer) {
        m_referer = referer;
    }
}
