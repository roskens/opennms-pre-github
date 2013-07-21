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

package org.opennms.web.map.db;

/**
 * <p>
 * LinkInfo class.
 * </p>
 *
 * @author <a href="mailto:antonio@opennms.it">Antonio Russo</a>
 * @version $Id: $
 * @since 1.8.1
 */
public class LinkInfo {

    /** The id. */
    int id;

    /** The nodeid. */
    int nodeid;

    /** The ifindex. */
    int ifindex;

    /** The nodeparentid. */
    int nodeparentid;

    /** The parentifindex. */
    int parentifindex;

    /** The linktypeid. */
    int linktypeid;

    /** The snmpiftype. */
    int snmpiftype;

    /** The snmpifspeed. */
    long snmpifspeed;

    /** The snmpifoperstatus. */
    int snmpifoperstatus;

    /** The snmpifadminstatus. */
    int snmpifadminstatus;

    /** The status. */
    String status;

    /**
     * Instantiates a new link info.
     *
     * @param id
     *            the id
     * @param nodeid
     *            the nodeid
     * @param ifindex
     *            the ifindex
     * @param nodeparentid
     *            the nodeparentid
     * @param parentifindex
     *            the parentifindex
     * @param snmpiftype
     *            the snmpiftype
     * @param snmpifspeed
     *            the snmpifspeed
     * @param snmpifoperstatus
     *            the snmpifoperstatus
     * @param snmpifadminstatus
     *            the snmpifadminstatus
     * @param status
     *            the status
     * @param linktypeid
     *            the linktypeid
     */
    LinkInfo(int id, int nodeid, int ifindex, int nodeparentid, int parentifindex, int snmpiftype, long snmpifspeed,
            int snmpifoperstatus, int snmpifadminstatus, String status, int linktypeid) {
        super();
        this.id = id;
        this.nodeid = nodeid;
        this.ifindex = ifindex;
        this.nodeparentid = nodeparentid;
        this.parentifindex = parentifindex;
        this.snmpiftype = snmpiftype;
        this.snmpifspeed = snmpifspeed;
        this.snmpifoperstatus = snmpifoperstatus;
        this.snmpifadminstatus = snmpifadminstatus;
        this.status = status;
        this.linktypeid = linktypeid;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LinkInfo) {
            LinkInfo ol = (LinkInfo) obj;
            return (ol.id == this.id);
        }
        return false;
    }

    /**
     * <p>
     * hashCode
     * </p>
     * .
     *
     * @return a int.
     */
    @Override
    public int hashCode() {
        return this.id;
    }

}
