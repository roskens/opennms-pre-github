package org.opennms.features.vaadin.nodebrowser.search;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanContainer;
import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.features.vaadin.nodebrowser.model.Criteria;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsNode;

import java.util.List;

public class SqlSearchStrategy implements SearchStrategy {

    private BeanContainer<Integer, NodeWrapper> beanContainer = new BeanContainer<Integer, NodeWrapper>(NodeWrapper.class);
    private NodeDao nodeDao;

    public SqlSearchStrategy(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }

    public void search(List<Criteria> criterias) {
        beanContainer.removeAllContainerFilters();
        beanContainer.removeAllItems();
        beanContainer.setBeanIdProperty("id");

        List<OnmsNode> onmsNodes = getNodesForCriterias(criterias);

        System.out.println(onmsNodes.size()+" node(s) found!");

        for (OnmsNode onmsNode : onmsNodes) {
            beanContainer.addBean(new NodeWrapper(onmsNode));
        }
    }

    public List<OnmsNode> getNodesForCriterias(List<Criteria> criterias) {
        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsNode.class);

        String query = "";

        System.out.println("Searching for...");

        for (Criteria criteria : criterias) {

            System.out.println(criteria);

            if (!"".equals(query)) {
                query += " INTERSECT ";
            }
            query += "(" + criteria.getEntityType().getSql(criteria.getOperator(), criteria.getSearch()) + ")";
        }

        criteriaBuilder.sql("{alias}.nodeId IN " + query);

        System.out.println("{alias}.nodeId IN " + query);

        criteriaBuilder.distinct();

        return (List<OnmsNode>) nodeDao.findMatching(criteriaBuilder.toCriteria());
    }

    public Container getContainer() {
        return beanContainer;
    }

}
