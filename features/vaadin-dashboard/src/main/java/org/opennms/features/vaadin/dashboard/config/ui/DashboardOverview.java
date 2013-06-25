package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.opennms.features.vaadin.dashboard.model.SandwichBoard;


public class DashboardOverview extends VerticalLayout {
    private Table table;
    private DashboardConfigView dashboardConfigView;

    BeanItemContainer<SandwichBoard> beanContainer;

    public DashboardOverview(DashboardConfigView dashboardConfigView) {
        this.dashboardConfigView = dashboardConfigView;

        beanContainer = DashboardProvider.getInstance().getBeanContainer();

        setSizeFull();

        setMargin(true);
        setSpacing(true);

        table = new Table();
        table.setContainerDataSource(beanContainer);
        table.setSizeFull();

        table.addGeneratedColumn("Edit", new Table.ColumnGenerator() {
            public Object generateCell(Table source, final Object itemId, Object columnId) {
                Button button = new Button("Edit");
                button.addClickListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        DashboardOverview.this.dashboardConfigView.openSandwichBoard((SandwichBoard) itemId);
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
                        DashboardProvider.getInstance().removeDashboard((SandwichBoard) itemId);
                    }
                });
                return button;
            }
        });

        addComponent(table);

        table.setVisibleColumns(new Object[]{"title", "Edit", "Remove"});
    }
}
