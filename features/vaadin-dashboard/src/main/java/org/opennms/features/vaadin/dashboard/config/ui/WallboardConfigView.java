package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.data.Container;
import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.opennms.features.vaadin.dashboard.config.DashletSelector;
import org.opennms.features.vaadin.dashboard.model.DashletFactory;
import org.opennms.features.vaadin.dashboard.model.Wallboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallboardConfigView extends HorizontalLayout implements TabSheet.CloseHandler, DashletSelector.ServiceListChangedListener {
    private TabSheet.Tab m_overviewTab;
    private WallboardTabSheet m_tabSheet;
    private DashletSelector m_dashletSelector;
    private WallboardOverview m_dashboardOverview;
    private Map<Wallboard, TabSheet.Tab> m_wallboardEditorMap = new HashMap<Wallboard, TabSheet.Tab>();

    public WallboardConfigView(DashletSelector dashletSelector) {
        m_dashletSelector = dashletSelector;

        setSizeFull();

        m_tabSheet = new WallboardTabSheet() {
            @Override
            protected void addNewTabComponent() {
                WallboardConfigView.this.addNewTabComponent();
            }
        };

        m_tabSheet.setSizeFull();

        m_dashboardOverview = new WallboardOverview(this);

        m_overviewTab = m_tabSheet.addTab(m_dashboardOverview, "Overview");

        m_overviewTab.setClosable(false);

        m_tabSheet.setSelectedTab(m_overviewTab);
        m_tabSheet.setCloseHandler(this);

        addComponent(m_tabSheet);

        dashletSelector.addServiceListChangedListener(this);

        WallboardProvider.getInstance().getBeanContainer().addItemSetChangeListener(new Container.ItemSetChangeListener() {
            public void containerItemSetChange(Container.ItemSetChangeEvent itemSetChangeEvent) {
                List<Wallboard> wallboardsToRemove = new ArrayList<Wallboard>();
                List<TabSheet.Tab> tabsToRemove = new ArrayList<TabSheet.Tab>();
                for (Map.Entry<Wallboard, TabSheet.Tab> entry : m_wallboardEditorMap.entrySet()) {
                    WallboardEditor wallboardEditor = (WallboardEditor) entry.getValue().getComponent();
                    if (!WallboardProvider.getInstance().containsWallboard(wallboardEditor.getWallboard())) {
                        wallboardsToRemove.add(wallboardEditor.getWallboard());
                        tabsToRemove.add(entry.getValue());
                    }
                }
                for (TabSheet.Tab tab : tabsToRemove) {
                    m_tabSheet.removeTab(tab);
                }
                for (Wallboard wallboard : wallboardsToRemove) {
                    m_wallboardEditorMap.remove(wallboard);
                }
            }
        });
    }

    public void openWallboardEditor(Wallboard wallboard) {
        if (m_wallboardEditorMap.containsKey(wallboard)) {
            m_tabSheet.setSelectedTab(m_wallboardEditorMap.get(wallboard));
        } else {
            WallboardEditor wallboardEditor = new WallboardEditor(m_dashletSelector, wallboard);

            TabSheet.Tab tab = m_tabSheet.addTab(wallboardEditor, wallboard.getTitle(), null);
            wallboardEditor.setTab(tab);
            tab.setClosable(true);

            m_wallboardEditorMap.put(wallboard, tab);

            m_tabSheet.setSelectedTab(tab);
        }
    }

    protected void addNewTabComponent() {
        final Window window = new Window("New Wallboard");

        window.setModal(true);
        window.setClosable(false);
        window.setResizable(false);

        getUI().addWindow(window);

        window.setContent(new VerticalLayout() {
            TextField name = new TextField("Wallboard Name");

            {
                addComponent(new FormLayout() {
                    {
                        setSizeUndefined();
                        setMargin(true);

                        String newName = "Untitled";
                        int i = 1;
                        if (WallboardProvider.getInstance().containsWallboard(newName)) {
                            do {
                                i++;
                                newName = "Untitled #" + i;
                            } while (WallboardProvider.getInstance().containsWallboard(newName));
                        }
                        name.setValue(newName);
                        addComponent(name);
                        name.focus();
                        name.selectAll();

                        name.addValidator(new AbstractStringValidator("Title must be unique") {
                            @Override
                            protected boolean isValidValue(String s) {
                                return (!WallboardProvider.getInstance().containsWallboard(s) && !"".equals(s));
                            }
                        });
                    }
                });

                addComponent(new HorizontalLayout() {
                    {
                        setMargin(true);
                        setSpacing(true);
                        setWidth("100%");

                        Button cancel = new Button("Cancel");
                        cancel.addClickListener(new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                window.close();
                            }
                        });

                        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);
                        addComponent(cancel);
                        setExpandRatio(cancel, 1);
                        setComponentAlignment(cancel, Alignment.TOP_RIGHT);

                        Button ok = new Button("Save");

                        ok.addClickListener(new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                if (name.isValid()) {
                                    Wallboard wallboard = new Wallboard();
                                    wallboard.setTitle(name.getValue());

                                    WallboardProvider.getInstance().addWallboard(wallboard);
                                    WallboardProvider.getInstance().save();

                                    WallboardEditor wallboardEditor = new WallboardEditor(m_dashletSelector, wallboard);
                                    TabSheet.Tab tab = m_tabSheet.addTab(wallboardEditor, wallboard.getTitle());

                                    wallboardEditor.setTab(tab);

                                    m_wallboardEditorMap.put(wallboard, tab);

                                    tab.setClosable(true);

                                    m_tabSheet.setSelectedTab(tab);

                                    window.close();
                                }
                            }
                        });

                        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER, null);

                        addComponent(ok);
                    }
                });
            }
        });
    }

    public void serviceListChanged(List<DashletFactory> factoryList) {
        for (Map.Entry<Wallboard, TabSheet.Tab> entry : m_wallboardEditorMap.entrySet()) {
            WallboardEditor wallboardEditor = (WallboardEditor) entry.getValue().getComponent();
            wallboardEditor.updateServiceList(factoryList);
        }

        ((WallboardConfigUI) getUI()).notifyMessage("Configuration change", "Dashlet list modified");
    }


    public void onTabClose(final TabSheet tabsheet, final Component tabContent) {
        tabsheet.removeComponent(tabContent);
        m_wallboardEditorMap.remove(((WallboardEditor) tabContent).getWallboard());
    }
}
