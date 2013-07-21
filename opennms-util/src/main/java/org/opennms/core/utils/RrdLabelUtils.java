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

package org.opennms.core.utils;

/**
 * The Class RrdLabelUtils.
 */
public abstract class RrdLabelUtils {

    /**
     * Compute name for rrd.
     *
     * @param ifname
     *            the ifname
     * @param ifdescr
     *            the ifdescr
     * @return the string
     */
    public static String computeNameForRRD(String ifname, String ifdescr) {
        String label = null;
        if (ifname != null && !ifname.equals("")) {
            label = AlphaNumeric.parseAndReplace(ifname, '_');
        } else if (ifdescr != null && !ifdescr.equals("")) {
            label = AlphaNumeric.parseAndReplace(ifdescr, '_');
        }
        return label;

    }

    /**
     * Compute phys addr for rrd.
     *
     * @param physaddr
     *            the physaddr
     * @return the string
     */
    public static String computePhysAddrForRRD(String physaddr) {
        String physAddrForRRD = null;

        if (physaddr != null && !physaddr.equals("")) {
            String parsedPhysAddr = AlphaNumeric.parseAndTrim(physaddr);
            if (parsedPhysAddr.length() == 12) {
                physAddrForRRD = parsedPhysAddr;
            }
        }

        return physAddrForRRD;

    }

    /**
     * Compute label for rrd.
     *
     * @param ifname
     *            the ifname
     * @param ifdescr
     *            the ifdescr
     * @param physaddr
     *            the physaddr
     * @return the string
     */
    public static String computeLabelForRRD(String ifname, String ifdescr, String physaddr) {
        String name = computeNameForRRD(ifname, ifdescr);
        String physAddrForRRD = computePhysAddrForRRD(physaddr);
        return (physAddrForRRD == null ? name : name + '-' + physAddrForRRD);
    }
}
