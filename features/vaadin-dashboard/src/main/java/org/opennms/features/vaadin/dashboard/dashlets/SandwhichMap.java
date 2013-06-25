package org.opennms.features.vaadin.dashboard.dashlets;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import org.opennms.features.vaadin.dashboard.model.Sandwich;

/**
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
public class SandwhichMap extends Image implements Sandwich {
    public SandwhichMap() {
        super(null, new ThemeResource("img/map.png"));
        setWidth("100%");
    }

    @Override
    public boolean allowAdvance() {
        return true;
    }
}
