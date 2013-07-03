/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/
package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import org.opennms.features.vaadin.dashboard.model.Wallboard;
import org.opennms.features.vaadin.dashboard.ui.wallboard.WallboardBody;

/**
 * This class is used to display an brief overview about existing {@link Wallboard} configurations.
 *
 * @author Christian Pape
 */
public class WallboardOverview extends VerticalLayout {
    /**
     * The {@link Table} this component uses to display {@link Wallboard} configurations
     */
    private Table m_table;
    /**
     * The {@link WallboardConfigView} this component belongs to.
     */
    private WallboardConfigView m_wallboardConfigView;
    /**
     * The {@link BeanItemContainer} this component uses for {@link Wallboard} configurations
     */
    BeanItemContainer<Wallboard> m_beanItemContainer;

    /**
     * Constructor for creating new instances.
     *
     * @param wallboardConfigView the {@link WallboardConfigView}
     */
    public WallboardOverview(WallboardConfigView wallboardConfigView) {
        /**
         * Setting the member fields
         */
        this.m_wallboardConfigView = wallboardConfigView;
        m_beanItemContainer = WallboardProvider.getInstance().getBeanContainer();

        /**
         * Setting up the layout component
         */
        setSizeFull();

        setMargin(true);
        setSpacing(true);

        /**
         * Adding the table with the required {@link Table.ColumnGenerator} objects
         */
        m_table = new Table();
        m_table.setContainerDataSource(m_beanItemContainer);
        m_table.setSizeFull();

        m_table.addGeneratedColumn("Edit", new Table.ColumnGenerator() {
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

        m_table.addGeneratedColumn("Remove", new Table.ColumnGenerator() {
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

        m_table.addGeneratedColumn("Preview", new Table.ColumnGenerator() {
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

        m_table.setVisibleColumns(new Object[]{"title", "Edit", "Remove", "Preview"});

        /**
         * Adding the table
         */
        addComponent(m_table);
    }
}
