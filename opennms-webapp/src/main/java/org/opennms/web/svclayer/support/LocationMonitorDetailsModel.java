/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.svclayer.support;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.Errors;

/**
 * <p>
 * LocationMonitorDetailsModel class.
 * </p>
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @version $Id: $
 * @since 1.8.1
 */
public class LocationMonitorDetailsModel {

    /** The m_errors. */
    private Errors m_errors;

    /** The m_title. */
    private MessageSourceResolvable m_title;

    /** The m_main details. */
    private Map<MessageSourceResolvable, MessageSourceResolvable> m_mainDetails;

    /** The m_additional details title. */
    private MessageSourceResolvable m_additionalDetailsTitle;

    /** The m_additional details. */
    private Map<MessageSourceResolvable, MessageSourceResolvable> m_additionalDetails;

    /**
     * <p>
     * Constructor for LocationMonitorDetailsModel.
     * </p>
     */
    public LocationMonitorDetailsModel() {
    }

    /**
     * <p>
     * getAdditionalDetails
     * </p>
     * .
     *
     * @return a {@link java.util.Map} object.
     */
    public final Map<MessageSourceResolvable, MessageSourceResolvable> getAdditionalDetails() {
        return m_additionalDetails;
    }

    /**
     * <p>
     * setAdditionalDetails
     * </p>
     * .
     *
     * @param additionalDetails
     *            a {@link java.util.Map} object.
     */
    public final void setAdditionalDetails(final Map<MessageSourceResolvable, MessageSourceResolvable> additionalDetails) {
        m_additionalDetails = additionalDetails;
    }

    /**
     * <p>
     * addAdditionalDetail
     * </p>
     * .
     *
     * @param key
     *            a {@link org.springframework.context.MessageSourceResolvable}
     *            object.
     * @param value
     *            a {@link org.springframework.context.MessageSourceResolvable}
     *            object.
     */
    public final void addAdditionalDetail(final MessageSourceResolvable key, final MessageSourceResolvable value) {
        if (m_additionalDetails == null) {
            m_additionalDetails = new LinkedHashMap<MessageSourceResolvable, MessageSourceResolvable>();
        }
        m_additionalDetails.put(key, value);
    }

    /**
     * <p>
     * getAdditionalDetailsTitle
     * </p>
     * .
     *
     * @return a {@link org.springframework.context.MessageSourceResolvable}
     *         object.
     */
    public final MessageSourceResolvable getAdditionalDetailsTitle() {
        return m_additionalDetailsTitle;
    }

    /**
     * <p>
     * setAdditionalDetailsTitle
     * </p>
     * .
     *
     * @param additionalDetailsTitle
     *            a {@link org.springframework.context.MessageSourceResolvable}
     *            object.
     */
    public final void setAdditionalDetailsTitle(final MessageSourceResolvable additionalDetailsTitle) {
        m_additionalDetailsTitle = additionalDetailsTitle;
    }

    /**
     * <p>
     * getMainDetails
     * </p>
     * .
     *
     * @return a {@link java.util.Map} object.
     */
    public final Map<MessageSourceResolvable, MessageSourceResolvable> getMainDetails() {
        return m_mainDetails;
    }

    /**
     * <p>
     * setMainDetails
     * </p>
     * .
     *
     * @param mainDetails
     *            a {@link java.util.Map} object.
     */
    public final void setMainDetails(final Map<MessageSourceResolvable, MessageSourceResolvable> mainDetails) {
        m_mainDetails = mainDetails;
    }

    /**
     * <p>
     * addMainDetail
     * </p>
     * .
     *
     * @param key
     *            a {@link org.springframework.context.MessageSourceResolvable}
     *            object.
     * @param value
     *            a {@link org.springframework.context.MessageSourceResolvable}
     *            object.
     */
    public final void addMainDetail(final MessageSourceResolvable key, final MessageSourceResolvable value) {
        if (m_mainDetails == null) {
            m_mainDetails = new LinkedHashMap<MessageSourceResolvable, MessageSourceResolvable>();
        }
        m_mainDetails.put(key, value);
    }

    /**
     * <p>
     * getTitle
     * </p>
     * .
     *
     * @return a {@link org.springframework.context.MessageSourceResolvable}
     *         object.
     */
    public final MessageSourceResolvable getTitle() {
        return m_title;
    }

    /**
     * <p>
     * setTitle
     * </p>
     * .
     *
     * @param title
     *            a {@link org.springframework.context.MessageSourceResolvable}
     *            object.
     */
    public final void setTitle(final MessageSourceResolvable title) {
        m_title = title;
    }

    /**
     * <p>
     * getErrors
     * </p>
     * .
     *
     * @return a {@link org.springframework.validation.Errors} object.
     */
    public final Errors getErrors() {
        return m_errors;
    }

    /**
     * <p>
     * setErrors
     * </p>
     * .
     *
     * @param errors
     *            a {@link org.springframework.validation.Errors} object.
     */
    public final void setErrors(final Errors errors) {
        m_errors = errors;
    }
}
