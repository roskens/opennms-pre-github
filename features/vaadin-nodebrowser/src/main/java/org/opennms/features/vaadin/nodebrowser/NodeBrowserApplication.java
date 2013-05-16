package org.opennms.features.vaadin.nodebrowser;

import com.vaadin.Application;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.opennms.core.criteria.Alias;
import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.core.criteria.restrictions.IlikeRestriction;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsNode;

import java.util.List;

public class NodeBrowserApplication extends Application {


    private interface SearchStrategy {
        public void setup(NodeDao nodeDao, Table table);

        public void search(String s);
    }

    private class SimpleSearchStrategy implements SearchStrategy {
        NodeDao nodeDao;
        BeanContainer<Integer, OnmsNode> beanContainer = new BeanContainer<Integer, OnmsNode>(OnmsNode.class);

        public void setup(NodeDao nodeDao, Table table) {
            this.nodeDao = nodeDao;

            beanContainer.removeAllItems();
            beanContainer.removeAllContainerFilters();

            beanContainer.setBeanIdProperty("id");

            List<OnmsNode> nodes = nodeDao.findAll();

            for (OnmsNode onmsNode : nodes) {
                beanContainer.addBean(onmsNode);
            }

            table.setContainerDataSource(beanContainer);
            table.setVisibleColumns(new Object[]{"id", "label"});
        }

        public void search(String s) {
            beanContainer.removeAllContainerFilters();

            if (!"".equals(s)) {
                beanContainer.addContainerFilter(new Or(new SimpleStringFilter("id", s, true, false), new SimpleStringFilter("label", s, true, false)));
            }
        }
    }

    private class CriteriaSearchStrategy implements SearchStrategy {
        NodeDao nodeDao;
        BeanContainer<Integer, OnmsNode> beanContainer = new BeanContainer<Integer, OnmsNode>(OnmsNode.class);

        public void setup(NodeDao nodeDao, Table table) {
            this.nodeDao = nodeDao;

            beanContainer.removeAllItems();
            beanContainer.removeAllContainerFilters();

            beanContainer.setBeanIdProperty("id");

            List<OnmsNode> nodes = nodeDao.findAll();

            for (OnmsNode onmsNode : nodes) {
                beanContainer.addBean(onmsNode);
            }

            table.setContainerDataSource(beanContainer);
            table.setVisibleColumns(new Object[]{"id", "label"});
        }

        public void search(String s) {
            CriteriaBuilder builder = new CriteriaBuilder(OnmsNode.class);

            builder.alias("snmpInterfaces", "snmpInterface", Alias.JoinType.LEFT_JOIN);
            builder.alias("ipInterfaces", "ipInterface", Alias.JoinType.LEFT_JOIN);
            builder.alias("categories", "category", Alias.JoinType.LEFT_JOIN);


            //builder.or(new LikeRestriction("id", s), new LikeRestriction("label", s));

            if (!"".equals(s)) {
                builder.and( new IlikeRestriction("label", s), new IlikeRestriction("ipInterface.ipAddress",s)).distinct();
            }

            beanContainer.removeAllItems();

            List<OnmsNode> nodes = nodeDao.findMatching(builder.toCriteria());

            for (OnmsNode onmsNode : nodes) {
                beanContainer.addBean(onmsNode);
            }
        }
    }

    private NodeDao nodeDao;
    private TextField textField;
    private Table table;
    private SearchStrategy searchStrategy = new CriteriaSearchStrategy();

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
                searchStrategy.search(textChangeEvent.getText());
            }
        });

        // setting up the table

        table = new Table();

        table.setImmediate(true);
        table.setSizeFull();

        // setting up the vertical layout

        final VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.setMargin(true);

        verticalLayout.addComponent(textField);
        verticalLayout.addComponent(table);
        verticalLayout.setSizeFull();
        verticalLayout.setExpandRatio(table, 1);

        searchStrategy.setup(nodeDao, table);
        /*
        getContext().addTransactionListener(new ApplicationContext.TransactionListener() {
            public void transactionStart(Application application, Object o) {
                if (o.toString().indexOf("?") == -1) {
                    searchStrategy.setup(nodeDao, table);
                    searchStrategy.search(String.valueOf(textField.getValue()));
                    System.out.println("BMRHGA: setup() -> "+o);
                }

                ((ServletHandlerRequest)o).

            }

            public void transactionEnd(Application application, Object o) {
            }
        });
        */
        final Window mainWindow = new Window("NodeBrowser", verticalLayout);

        setMainWindow(mainWindow);
    }

    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }
}
