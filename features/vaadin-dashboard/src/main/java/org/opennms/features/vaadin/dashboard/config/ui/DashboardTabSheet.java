package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet;

public abstract class DashboardTabSheet extends TabSheet {
    private CssLayout plusTab;
    private Component lastTab;

    protected abstract void addNewTabComponent();

    public DashboardTabSheet() {
        plusTab = new CssLayout();
        plusTab.setCaption("+");
        addTab(plusTab).setClosable(false);
        addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {

            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                Component selectedTab = getSelectedTab();
                if (selectedTab.getCaption() != null && selectedTab.getCaption().equals("+")) {
                    setSelectedTab((lastTab != null ? lastTab : getComponentIterator().next()));
                    addNewTabComponent();
                } else {
                    lastTab = selectedTab;
                }
            }
        });
    }

    @Override
    public TabSheet.Tab addTab(Component c, String caption, Resource icon) {
        removeComponent(plusTab);
        TabSheet.Tab tab = super.addTab(c, caption, icon);
        super.addTab(plusTab, plusTab.getCaption(), null);
        return tab;
    }
}
