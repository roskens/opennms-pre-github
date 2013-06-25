package org.opennms.features.vaadin.dashboard.model;

import com.vaadin.ui.Component;

/**
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
public interface Sandwich extends Component {

    public boolean allowAdvance();

    public String getName();
}
