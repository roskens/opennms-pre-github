/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
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

package org.opennms.gwt.web.ui.asset.client;

import java.util.Date;

import com.google.gwt.i18n.client.Messages;

/**
 * The Interface AssetPageMessages.
 *
 * @author <a href="mailto:MarkusNeumannMarkus@gmail.com">Markus Neumann</a>
 *         Basic static string i18n mechanism by GWT for messages with embedded
 *         content. Just add:
 *         DefaultMessage("[''{0}''] English [''{1}''] default String") ''{0}'',
 *         ''{1}'', ... is the template to insert parameters
 *         Key("Key to map value to the translated property files")
 *         String myI18nString([String mySuperString], [Date niceDate]) method
 *         to get
 *         the i18n string with the embedded string
 */

public interface AssetPageMessages extends Messages {

    /**
     * Invalid symbol.
     *
     * @param symbol
     *            the symbol
     * @return the string
     */
    @DefaultMessage("''{0}'' is not a valid symbol.")
    String invalidSymbol(String symbol);

    /**
     * Last update.
     *
     * @param timestamp
     *            the timestamp
     * @return the string
     */
    @DefaultMessage("Last update: {0,date,medium} {0,time,medium}")
    String lastUpdate(Date timestamp);
}
