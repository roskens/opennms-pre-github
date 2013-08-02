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
package org.opennms.netmgt.model.entopology;

import java.util.Date;

/**
 * Interface for elements of a toplogy.
 * <p>
 * An element of a topology can be an endpoint, a link or //FIXME
 * Elmentidentifier.
 * </p>
 * <p>
 * ONMS querys for informations about the topology via different protocols.
 * For each of this protocols exists a concrete implementation of the
 * endpoint, link and elemntIdentifier
 * </p>
 * <p>
 * To build a topology ONMS queries its nodes about their view of the network.
 * Since a node can only see directly connected parts of the network the
 * combination of all the views of all the nodes is the resulting topology.
 * </p>
 * <p>
 * To build the topology ONMS has to merge all the views to one consistent
 * data-set. If node A sees an elemtent X and node 2 sees it too, the element
 * has to be updated and not added again.
 * </p>
 * <p>
 * To detect elements that are no longer part of the topology ONMS keeps track
 * of the nodes it was seen from and the last time it was seen
 * </p>
 */
// FIXME there are already PollableX in onms, we should find another name: TopologyElement?
public interface Pollable {

    /**
     * Returns the list of node-ids of OnmsNodes this elements was detected
     * from.
     * 
     * @return
     */
    // FIXME does this need to be a list of dates and nodeids? otherwise we
    // delete an element if the last node who has seen it, doesn't see it
    // anymore. which doesn't mean no node can see it, the element would dispear for a short time 
    Integer getSourceNode();

    /**
     * The last time this element was detected by onms
     * 
     * @return
     */
    Date getLastPoll();

    /**
     * Updates the information when and from where this element was last seen.
     * 
     * @param lastPoll
     * @param node
     */
    void updatePollable(Date lastPoll, Integer node);

}
