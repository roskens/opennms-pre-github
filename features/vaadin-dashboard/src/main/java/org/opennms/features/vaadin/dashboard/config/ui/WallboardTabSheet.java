package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;

public abstract class WallboardTabSheet extends TabSheet {
    private CssLayout m_plusTab;
    private Component m_lastTab;

    protected abstract void addNewTabComponent();

    public WallboardTabSheet() {
        m_plusTab = new CssLayout();
        m_plusTab.setCaption("+");
        addTab(m_plusTab).setClosable(false);
        addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {

            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                Component selectedTab = getSelectedTab();
                if (selectedTab.getCaption() != null && selectedTab.getCaption().equals("+")) {
                    setSelectedTab((m_lastTab != null ? m_lastTab : getComponentIterator().next()));
                    addNewTabComponent();
                } else {
                    m_lastTab = selectedTab;
                }
            }
        });
    }

    @Override
    public TabSheet.Tab addTab(Component c, String caption, Resource icon) {
        removeComponent(m_plusTab);
        TabSheet.Tab tab = super.addTab(c, caption, icon);
        super.addTab(m_plusTab, m_plusTab.getCaption(), null);
        return tab;
    }
}
