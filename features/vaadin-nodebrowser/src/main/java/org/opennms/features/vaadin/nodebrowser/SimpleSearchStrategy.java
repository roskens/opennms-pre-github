package org.opennms.features.vaadin.nodebrowser;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.filter.Not;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsNode;

import java.util.List;

public class SimpleSearchStrategy implements SearchStrategy {
    private BeanContainer<Integer, NodeWrapper> beanContainer = new BeanContainer<Integer, NodeWrapper>(NodeWrapper.class);
    private NodeDao nodeDao;

    public SimpleSearchStrategy(NodeDao nodeDao) {
        this.nodeDao = nodeDao;

        refresh();
    }

    public void refresh() {
        beanContainer.removeAllItems();

        List<OnmsNode> nodes = nodeDao.findAll();

        beanContainer.setBeanIdProperty("id");

        for (OnmsNode onmsNode : nodes) {
            beanContainer.addBean(new NodeWrapper(onmsNode));
        }
    }

    public void search(List<SearchCriteria> searchCriterias) {
        beanContainer.removeAllContainerFilters();

        for (SearchCriteria searchCriteria : searchCriterias) {
            if (searchCriteria.getOperator().equals(SearchCriteria.Operator.LIKE)) {
                beanContainer.addContainerFilter(searchCriteria.getKey().getFilter(searchCriteria.getValue()));
            } else {
                beanContainer.addContainerFilter(new Not(searchCriteria.getKey().getFilter(searchCriteria.getValue())));
            }
        }
    }

    public Container getContainer() {
        return beanContainer;
    }
}
