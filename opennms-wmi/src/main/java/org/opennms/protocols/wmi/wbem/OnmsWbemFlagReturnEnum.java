/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
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

package org.opennms.protocols.wmi.wbem;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: CE136452
 * Date: Oct 21, 2008
 * Time: 6:45:47 AM
 * To change this template use File | Settings | File Templates.
 *
 * @author ranger
 * @version $Id: $
 */
public enum OnmsWbemFlagReturnEnum {

    /** The wbem flag forward only. */
    wbemFlagForwardOnly(32, "wbemFlagForwardOnly"), /** The wbem flag bidirectional. */
 wbemFlagBidirectional(0, "wbemFlagBidirectional"), /** The wbem flag return immediately. */
 wbemFlagReturnImmediately(
            16, "wbemFlagReturnImmediately"),
 /** The wbem flag return when complete. */
 wbemFlagReturnWhenComplete(0, "wbemFlagReturnWhenComplete"),
 /** The wbem query flag prototype. */
 wbemQueryFlagPrototype(
            2, "wbemQueryFlagPrototype"),
 /** The wbem flag use amended qualifiers. */
 wbemFlagUseAmendedQualifiers(131072, "wbemFlagUseAmendedQualifiers");

    /** Constant <code>lookup</code>. */
    private static final Map<Integer, OnmsWbemFlagReturnEnum> lookup = new HashMap<Integer, OnmsWbemFlagReturnEnum>();

    /** The return flag value. */
    private int returnFlagValue;

    /** The return flag name. */
    private String returnFlagName;

    static {
        for (final OnmsWbemFlagReturnEnum s : EnumSet.allOf(OnmsWbemFlagReturnEnum.class)) {
            lookup.put(s.getReturnFlagValue(), s);
        }
    }

    /**
     * Instantiates a new onms wbem flag return enum.
     *
     * @param returnFlagValue
     *            the return flag value
     * @param returnFlagName
     *            the return flag name
     */
    OnmsWbemFlagReturnEnum(final int returnFlagValue, final String returnFlagName) {
        this.returnFlagValue = returnFlagValue;
        this.returnFlagName = returnFlagName;
    }

    /**
     * <p>
     * Getter for the field <code>returnFlagValue</code>.
     * </p>
     *
     * @return a int.
     */
    public int getReturnFlagValue() {
        return returnFlagValue;
    }

    /**
     * <p>
     * Getter for the field <code>returnFlagName</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getReturnFlagName() {
        return returnFlagName;
    }

    /**
     * <p>
     * get
     * </p>
     * .
     *
     * @param returnFlagValue
     *            a int.
     * @return a {@link org.opennms.protocols.wmi.wbem.OnmsWbemFlagReturnEnum}
     *         object.
     */
    public static OnmsWbemFlagReturnEnum get(final int returnFlagValue) {
        return lookup.get(returnFlagValue);
    }
}
