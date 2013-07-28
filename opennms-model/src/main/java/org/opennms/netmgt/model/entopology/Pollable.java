package org.opennms.netmgt.model.entopology;

import java.util.Date;
import java.util.Set;

/**
 * Interface for elements of a toplogy.
 * <p>An element of a topology can be an endpoint, a link or //FIXME Elmentidentifier.</p>
 * <p>ONMS querys for informations about the topology via different protocols.
 * For each of this protocols exists a concrete implementation of the endpoint, link and elemntIdentifier</p>
 * <p>To build a topology ONMS queries its nodes about their view of the network.
 * Since a node can only see directly connected parts of the network
 * the combination of all the views of all the nodes is the resulting topology.</p>
 * <p>To build the topology ONMS has to merge all the views to one consistent data-set.
 * If node A sees an elemtent X and node 2 sees it too, the element has to be updated and not added again.</p>
 * <p>To detect elements that are no longer part of the topology ONMS keeps track of the nodes it ws seen from and the last time it was seen</p>
 */
// FIXME there are already PollableX in onms, we should find another name: TopologyElement?
public interface Pollable {

    /**
     * Returns the list of node-ids of OnmsNodes this elements was detected from.
     * @return
     */
	Set<Integer> getSourceNodes();

	void setSourceNodes(Set<Integer> sourceNodes);

    /**
     * The last time this element was detected by onms
     * @return
     */
	Date getLastPoll();

	void setLastPoll(Date lastPoll);
	
}
