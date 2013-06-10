package org.opennms.features.vaadin.nodebrowser;

import com.vaadin.Application;
import com.vaadin.ui.*;
import org.opennms.features.vaadin.nodebrowser.columns.NodeIdColumnGenerator;
import org.opennms.features.vaadin.nodebrowser.columns.NodeLabelColumnGenerator;
import org.opennms.features.vaadin.nodebrowser.columns.NodeStatusColumnGenerator;
import org.opennms.features.vaadin.nodebrowser.search.NodeSearch;
import org.opennms.features.vaadin.nodebrowser.search.SearchStrategy;
import org.opennms.features.vaadin.nodebrowser.search.SqlSearchStrategy;
import org.opennms.features.vaadin.nodebrowser.view.CriteriaBuilderComponent;
import org.opennms.netmgt.dao.NodeDao;
import java.util.List;
import org.opennms.features.vaadin.nodebrowser.model.Criteria;

public class NodeBrowserApplication extends Application implements NodeSearch {
    private NodeDao nodeDao;
    private Table table;
    private SearchStrategy searchStrategy;

    @Override
    public void init() {
        if (nodeDao == null) {
            throw new RuntimeException("nodeDao cannot be null.");
        }

        // setting up the table

        table = new Table();

        table.setImmediate(true);
        table.setSizeFull();

        // setting up the vertical layout

        final VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.setMargin(true);

        CriteriaBuilderComponent criteriaBuilderComponent = new CriteriaBuilderComponent(this, null);

        verticalLayout.addComponent(criteriaBuilderComponent);

        verticalLayout.addComponent(table);
        verticalLayout.setSizeFull();
        verticalLayout.setExpandRatio(table, 1);

        searchStrategy = new SqlSearchStrategy(nodeDao);

        table.setContainerDataSource(searchStrategy.getContainer());

        table.setVisibleColumns(new Object[]{"id", "status", "servicesDown", "servicesUp", "label", "primaryInterface", "categories"});

        final Window mainWindow = new Window("NodeBrowser", verticalLayout);

        table.setSelectable(false);
        table.setMultiSelect(false);

        table.addGeneratedColumn("id", new NodeIdColumnGenerator());
        table.addGeneratedColumn("status", new NodeStatusColumnGenerator());
        table.addGeneratedColumn("label", new NodeLabelColumnGenerator());

        setMainWindow(mainWindow);
    }

    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }

    @Override
    public void search(List<Criteria> criterias) {
        searchStrategy.search(criterias);
    }
}
