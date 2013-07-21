/**
 *
 */
package org.opennms.features.topology.plugins.ncs;

import org.opennms.features.topology.api.GraphContainer;

/**
 * This listener responds to events from to {@link TopologyComponent} that
 * indicate that the user has selected a vertex or edge in the graph.
 *
 * @see SelectionEvent
 */
public interface SelectionListener {

    /**
     * On selection update.
     *
     * @param graphContainer
     *            the graph container
     */
    public void onSelectionUpdate(GraphContainer graphContainer);
}
