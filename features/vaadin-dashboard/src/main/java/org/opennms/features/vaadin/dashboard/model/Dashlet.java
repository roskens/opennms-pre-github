package org.opennms.features.vaadin.dashboard.model;

import com.vaadin.ui.Component;

/**
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
public interface Dashlet extends Component {
    public String getName();

    public boolean isBoosted();
}
