package org.opennms.vaadin.applicationstack.view;

import com.vaadin.ui.UI;
import org.ops4j.pax.vaadin.AbstractApplicationFactory;
import org.osgi.service.blueprint.container.BlueprintContainer;

/**
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
public class ApplicationStackUIFactory extends AbstractApplicationFactory {
    private final BlueprintContainer m_blueprintContainer;
    private final String m_beanName;

    public ApplicationStackUIFactory(BlueprintContainer container, String beanName) {
        m_blueprintContainer = container;
        m_beanName = beanName;
    }


    @Override
    public UI getUI() {
        return (UI) m_blueprintContainer.getComponentInstance(m_beanName);
    }

    @Override
    public Class<? extends UI> getUIClass() {
        return ApplicationStackUI.class;
    }
}
