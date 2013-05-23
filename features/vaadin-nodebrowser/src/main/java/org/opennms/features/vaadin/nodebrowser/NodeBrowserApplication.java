package org.opennms.features.vaadin.nodebrowser;

import com.vaadin.Application;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import org.opennms.features.vaadin.nodebrowser.columns.NodeIdColumnGenerator;
import org.opennms.features.vaadin.nodebrowser.columns.NodeLabelColumnGenerator;
import org.opennms.features.vaadin.nodebrowser.columns.NodeStatusColumnGenerator;
import org.opennms.netmgt.dao.NodeDao;

import java.util.ArrayList;

public class NodeBrowserApplication extends Application implements NodeSearch {
    private NodeDao nodeDao;
    private Table table;
    private SearchStrategy searchStrategy;
    private ArrayList<SearchCriteria> searchCriterias = new ArrayList<SearchCriteria>();
    private VerticalLayout searchCriteriaList = new VerticalLayout();

    private void updateSearchCriterias() {
        searchCriteriaList.removeAllComponents();
        searchCriteriaList.setMargin(true);

        boolean first = true;

        for (final SearchCriteria searchCriteria : searchCriterias) {
            HorizontalLayout horizontalLayout = searchCriteria.getHorizontalLayout();

            if (first) {
                first = false;
                /*
                final Button searchButton = new Button();
                searchButton.setIcon(new ThemeResource("../runo/icons/16/arrow-right.png"));
                searchButton.addListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        searchStrategy.search(searchCriterias);
                    }
                });
                */
                final Button plusButton = new Button();
                plusButton.setIcon(new ThemeResource("../runo/icons/16/document-add.png"));

                final Button refreshButton = new Button();
                refreshButton.setIcon(new ThemeResource("../runo/icons/16/reload.png"));

                refreshButton.addListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        searchStrategy.refresh();
                    }
                });

                plusButton.addListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        searchCriterias.add(new SearchCriteria(NodeBrowserApplication.this, SearchCriteria.Key.NODE, SearchCriteria.Operator.LIKE, "*"));
                        updateSearchCriterias();
                    }
                });

                //horizontalLayout.addComponent(searchButton);
                horizontalLayout.addComponent(plusButton);
                horizontalLayout.addComponent(refreshButton);
            } else {
                final Button minusButton = new Button();
                minusButton.setIcon(new ThemeResource("../runo/icons/16/document-delete.png"));

                minusButton.addListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        searchCriterias.remove(searchCriteria);
                        searchStrategy.search(searchCriterias);
                        updateSearchCriterias();
                    }
                });
                horizontalLayout.addComponent(minusButton);
            }

            searchCriteriaList.addComponent(horizontalLayout);
        }
    }

    @Override
    public void init() {
        if (nodeDao == null) {
            throw new RuntimeException("nodeDao cannot be null.");
        }

        // setting up the table

        table = new Table();

        table.setImmediate(true);
        table.setSizeFull();

        // setting up initial search criteria

        searchCriterias.add(new SearchCriteria(this, SearchCriteria.Key.NODE, SearchCriteria.Operator.LIKE, "*"));

        // setting up the vertical layout

        final VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.setMargin(true);
        verticalLayout.addComponent(searchCriteriaList);

        verticalLayout.addComponent(table);
        verticalLayout.setSizeFull();
        verticalLayout.setExpandRatio(table, 1);

        searchStrategy = new SimpleSearchStrategy(nodeDao);

        updateSearchCriterias();

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
    public void search() {
        searchStrategy.search(searchCriterias);
    }
}
