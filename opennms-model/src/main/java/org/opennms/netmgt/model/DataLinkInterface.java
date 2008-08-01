//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
// OpenNMS Licensing       <license@opennms.org>
//     http://www.opennms.org/
//     http://www.opennms.com/
//
/**
 * @author: joed
 * Date  : Jul 31, 2008
 */

package org.opennms.netmgt.model;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

public class DataLinkInterface  implements Serializable {

    private static final long serialVersionUID = -1940209159462647862L;

    @Column(name="nodeid", nullable=false)
    private Integer nodeId;
    @Column(name="ifindex", nullable=false)
    private Integer ifIndex;
    @Column(name="nodeparentid", nullable=false)
    private Integer nodeParentId;
    @Column(name="parentifindex", nullable=false)
    private Integer parentIfIndex;
    @Column(name="status", length=1, nullable=false)
    private String  status;
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="lastpolltime", nullable=false)
    private Date lastPollTime;


    /**
     * Method getNodeId returns the nodeId of this DataLinkInterface object.
     *
     * @return the nodeId (type Integer) of this DataLinkInterface object.
     */
    public Integer getNodeId() {
        return nodeId;
    }

    /**
     * Method getIfIndex returns the ifIndex of this DataLinkInterface object.
     *
     * @return the ifIndex (type Integer) of this DataLinkInterface object.
     */
    public Integer getIfIndex() {
        return ifIndex;
    }

    /**
     * Method getNodeParentId returns the nodeParentId of this DataLinkInterface object.
     *
     * @return the nodeParentId (type Integer) of this DataLinkInterface object.
     */
    public Integer getNodeParentId() {
        return nodeParentId;
    }

    /**
     * Method getParentIfIndex returns the parentIfIndex of this DataLinkInterface object.
     *
     * @return the parentIfIndex (type Integer) of this DataLinkInterface object.
     */
    public Integer getParentIfIndex() {
        return parentIfIndex;
    }

    /**
     * Method getStatus returns the status of this DataLinkInterface object.
     *
     * @return the status (type String) of this DataLinkInterface object.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Method getLastPollTime returns the lastPollTime of this DataLinkInterface object.
     *
     * @return the lastPollTime (type Date) of this DataLinkInterface object.
     */
    public Date getLastPollTime() {
        return lastPollTime;
    }

}
