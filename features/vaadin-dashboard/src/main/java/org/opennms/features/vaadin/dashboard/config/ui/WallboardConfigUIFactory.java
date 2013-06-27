package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.ui.UI;
import org.ops4j.pax.vaadin.AbstractApplicationFactory;
import org.osgi.service.blueprint.container.BlueprintContainer;

public class WallboardConfigUIFactory extends AbstractApplicationFactory {
    private final BlueprintContainer m_blueprintContainer;
    private final String m_beanName;

    public WallboardConfigUIFactory(BlueprintContainer container, String beanName) {
        m_blueprintContainer = container;
        m_beanName = beanName;
    }

    @Override
    public UI getUI() {
        return (UI) m_blueprintContainer.getComponentInstance(m_beanName);
    }

    @Override
    public Class<? extends UI> getUIClass() {
        return WallboardConfigUI.class;
    }
}
