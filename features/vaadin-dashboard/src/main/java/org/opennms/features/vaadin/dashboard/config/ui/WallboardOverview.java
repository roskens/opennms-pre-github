package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import org.opennms.features.vaadin.dashboard.model.Wallboard;
import org.opennms.features.vaadin.dashboard.ui.wallboard.WallboardBody;


public class WallboardOverview extends VerticalLayout {
    private Table table;
    private WallboardConfigView m_wallboardConfigView;

    BeanItemContainer<Wallboard> m_beanItemContainer;

    public WallboardOverview(WallboardConfigView wallboardConfigView) {
        this.m_wallboardConfigView = wallboardConfigView;

        m_beanItemContainer = WallboardProvider.getInstance().getBeanContainer();

        setSizeFull();

        setMargin(true);
        setSpacing(true);

        table = new Table();
        table.setContainerDataSource(m_beanItemContainer);
        table.setSizeFull();

        table.addGeneratedColumn("Edit", new Table.ColumnGenerator() {
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button button = new Button("Edit");
                button.addClickListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        m_wallboardConfigView.openWallboardEditor((Wallboard) itemId);
                    }
                });
                return button;
            }
        });

        table.addGeneratedColumn("Remove", new Table.ColumnGenerator() {
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button button = new Button("Remove");
                button.addClickListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        WallboardProvider.getInstance().removeWallboard((Wallboard) itemId);
                    }
                });
                return button;
            }
        });

        table.addGeneratedColumn("Preview", new Table.ColumnGenerator() {
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button button = new Button("Preview");
                button.addClickListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        final Window window = new Window("Preview");

                        window.setModal(true);
                        window.setClosable(false);
                        window.setResizable(false);

                        window.setWidth("90%");
                        window.setHeight("90%");

                        getUI().addWindow(window);

                        final WallboardBody wallboardBody = new WallboardBody();

                        window.setContent(new VerticalLayout() {
                            {
                                setMargin(true);
                                setSpacing(true);
                                setSizeFull();

                                addComponent(wallboardBody);
                                setExpandRatio(wallboardBody, 1.0f);
                                addComponent(new HorizontalLayout() {
                                    {
                                        setMargin(true);
                                        setSpacing(true);
                                        setWidth("100%");

                                        Button closeButton = new Button("Close");

                                        addComponent(closeButton);
                                        setComponentAlignment(closeButton, Alignment.MIDDLE_RIGHT);
                                        closeButton.addClickListener(new Button.ClickListener() {
                                            @Override
                                            public void buttonClick(Button.ClickEvent clickEvent) {
                                                window.close();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                        wallboardBody.setDashletSpecs(((Wallboard) itemId).getDashletSpecs());
                    }
                });
                return button;
            }
        });

        addComponent(table);

        table.setVisibleColumns(new Object[]{"title", "Edit", "Remove", "Preview"});
    }
}
