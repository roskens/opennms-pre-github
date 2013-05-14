package org.opennms.features.vaadin.nodebrowser;

import com.vaadin.Application;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.service.ApplicationContext;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsNode;

import java.util.List;

public class NodeBrowserApplication extends Application {
    private NodeDao nodeDao;
    private TextField textField;
    private Table table;
    private List<OnmsNode> nodes;
    private BeanContainer<Integer, OnmsNode> beanContainer = new BeanContainer<Integer, OnmsNode>(OnmsNode.class);

    private void setup() {
        nodes = nodeDao.findAll();

        beanContainer.setBeanIdProperty("id");

        for (OnmsNode onmsNode : nodes) {
            beanContainer.addBean(onmsNode);
        }
    }

    private void filterNodes(String s) {
        beanContainer.removeAllContainerFilters();

        if (!"".equals(s)) {
            beanContainer.addContainerFilter(new Or(new SimpleStringFilter("id", s, true, false), new SimpleStringFilter("label", s, true, false)));
        }
    }

    @Override
    public void init() {
        if (nodeDao == null) {
            throw new RuntimeException("nodeDao cannot be null.");
        }

        // setting up the search text field

        textField = new TextField();
        textField.setWidth("100%");

        textField.addListener(new FieldEvents.FocusListener() {
            public void focus(FieldEvents.FocusEvent focusEvent) {
                textField.selectAll();
            }
        });

        textField.addListener(new FieldEvents.TextChangeListener() {
            public void textChange(FieldEvents.TextChangeEvent textChangeEvent) {
                filterNodes(textChangeEvent.getText());
            }
        });

        // setting up the table

        table = new Table();

        table.setImmediate(true);
        table.setSizeFull();

        //table.addContainerProperty("Id", Integer.class, null);
        //table.addContainerProperty("Label", String.class, null);

        table.setContainerDataSource(beanContainer);
        table.setVisibleColumns(new Object[]{"id", "label"});

        // setting up the vertical layout

        final VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.setMargin(true);

        verticalLayout.addComponent(textField);
        verticalLayout.addComponent(table);
        verticalLayout.setSizeFull();
        verticalLayout.setExpandRatio(table, 1);

        getContext().addTransactionListener(new ApplicationContext.TransactionListener() {
            public void transactionStart(Application application, Object o) {
                if (o.toString().indexOf("?") == -1) {
                    setup();
                }

            }

            public void transactionEnd(Application application, Object o) {
            }
        });

        final Window mainWindow = new Window("NodeBrowser", verticalLayout);

        setMainWindow(mainWindow);
    }

    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }
}
