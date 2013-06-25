package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.data.Container;
import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.opennms.features.vaadin.dashboard.config.DashletSelector;
import org.opennms.features.vaadin.dashboard.model.SandwichBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardConfigView extends HorizontalLayout implements TabSheet.CloseHandler {
    private TabSheet.Tab overviewTab;
    private DashboardTabSheet tabSheet;
    private DashletSelector dashletSelector;
    private DashboardOverview dashboardOverview;
    private Map<SandwichBoard, TabSheet.Tab> sandwichBoardModelEditorMap = new HashMap<SandwichBoard, TabSheet.Tab>();

    public DashboardConfigView(DashletSelector dashletSelector) {
        this.dashletSelector = dashletSelector;

        setSizeFull();

        tabSheet = new DashboardTabSheet() {
            @Override
            protected void addNewTabComponent() {
                DashboardConfigView.this.addNewTabComponent();
            }
        };

        tabSheet.setSizeFull();

        dashboardOverview = new DashboardOverview(this);

        overviewTab = tabSheet.addTab(dashboardOverview, "Overview");

        overviewTab.setClosable(false);

        tabSheet.setSelectedTab(overviewTab);

        tabSheet.setCloseHandler(this);

        addComponent(tabSheet);

        DashboardProvider.getInstance().getBeanContainer().addItemSetChangeListener(new Container.ItemSetChangeListener() {
            public void containerItemSetChange(Container.ItemSetChangeEvent itemSetChangeEvent) {
                List<SandwichBoard> sandwichBoardsToRemove = new ArrayList<SandwichBoard>();
                List<TabSheet.Tab> tabsToRemove = new ArrayList<TabSheet.Tab>();
                for (Map.Entry<SandwichBoard, TabSheet.Tab> entry : sandwichBoardModelEditorMap.entrySet()) {
                    SandwichBoardModelEditor sandwichBoardModelEditor = (SandwichBoardModelEditor) entry.getValue().getComponent();
                    if (!DashboardProvider.getInstance().containsDashboard(sandwichBoardModelEditor.getSandwichBoard())) {
                        DashboardConfigView.this.dashletSelector.removeServiceListChangedListener(sandwichBoardModelEditor);
                        sandwichBoardsToRemove.add(sandwichBoardModelEditor.getSandwichBoard());
                        tabsToRemove.add(entry.getValue());
                    }
                }
                for (TabSheet.Tab tab : tabsToRemove) {
                    tabSheet.removeTab(tab);
                }
                for (SandwichBoard sandwichBoard : sandwichBoardsToRemove) {
                    sandwichBoardModelEditorMap.remove(sandwichBoard);
                }
            }
        });
    }

    public void openSandwichBoard(SandwichBoard sandwichBoard) {
        if (sandwichBoardModelEditorMap.containsKey(sandwichBoard)) {
            tabSheet.setSelectedTab(sandwichBoardModelEditorMap.get(sandwichBoard));
        } else {
            SandwichBoardModelEditor sandwichBoardModelEditor = new SandwichBoardModelEditor(dashletSelector, sandwichBoard);

            TabSheet.Tab tab = tabSheet.addTab(sandwichBoardModelEditor, sandwichBoard.getTitle(), null);
            sandwichBoardModelEditor.setTab(tab);
            tab.setClosable(true);

            sandwichBoardModelEditorMap.put(sandwichBoard, tab);

            tabSheet.setSelectedTab(tab);
        }
    }

    protected void addNewTabComponent() {
        final Window window = new Window("Edit Dashboard");

        window.setModal(true);
        window.setClosable(false);
        window.setResizable(false);

        getUI().addWindow(window);

        window.setContent(new VerticalLayout() {
            TextField name = new TextField("Dashboard Name");

            {
                addComponent(new FormLayout() {
                    {
                        setSizeUndefined();
                        setMargin(true);

                        String newName = "Untitled";
                        int i = 1;
                        if (DashboardProvider.getInstance().containsDashboard(newName)) {
                            do {
                                i++;
                                newName = "Untitled #" + i;
                            } while (DashboardProvider.getInstance().containsDashboard(newName));
                        }
                        name.setValue(newName);
                        addComponent(name);
                        name.focus();
                        name.selectAll();

                        name.addValidator(new AbstractStringValidator("Title must be unique") {
                            @Override
                            protected boolean isValidValue(String s) {
                                return (!DashboardProvider.getInstance().containsDashboard(s) && !"".equals(s));
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
                                    SandwichBoard model = new SandwichBoard();
                                    model.setTitle(name.getValue());

                                    DashboardProvider.getInstance().addDashboard(model);
                                    DashboardProvider.getInstance().save();

                                    SandwichBoardModelEditor sandwichBoardModelEditor = new SandwichBoardModelEditor(dashletSelector, model);
                                    TabSheet.Tab tab = tabSheet.addTab(sandwichBoardModelEditor, model.getTitle());
                                    sandwichBoardModelEditor.setTab(tab);
                                    sandwichBoardModelEditorMap.put(model, tab);
                                    tab.setClosable(true);
                                    tabSheet.setSelectedTab(tab);
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

    public void onTabClose(final TabSheet tabsheet, final Component tabContent) {
        tabsheet.removeComponent(tabContent);
        sandwichBoardModelEditorMap.remove(((SandwichBoardModelEditor) tabContent).getSandwichBoard());
    }
}
