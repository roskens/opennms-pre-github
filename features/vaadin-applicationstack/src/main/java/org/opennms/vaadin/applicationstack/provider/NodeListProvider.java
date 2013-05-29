package org.opennms.vaadin.applicationstack.provider;

import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.core.criteria.restrictions.AnyRestriction;
import org.opennms.core.criteria.restrictions.Restriction;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.vaadin.applicationstack.model.Criteria;

import java.util.ArrayList;
import java.util.List;

public class NodeListProvider {
    private NodeDao nodeDao;

    public NodeListProvider(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }

    public List<OnmsNode> getNodesForCriterias(List<Criteria> criterias) {
        CriteriaBuilder criteriaBuilder = new CriteriaBuilder(OnmsNode.class);
        criteriaBuilder.alias("ipInterfaces", "ipInterfacesAlias");
        criteriaBuilder.alias("ipInterfacesAlias.monitoredServices", "monitoredServicesAlias");
        criteriaBuilder.alias("monitoredServicesAlias.serviceType", "serviceTypeAlias");
        criteriaBuilder.alias("categories", "categoriesAlias");
        criteriaBuilder.ne("ipInterfacesAlias.isManaged", "D");


        org.opennms.core.criteria.Criteria searchCriteria = criteriaBuilder.toCriteria();

        for (Criteria criteria : criterias) {
            String[] properties = criteria.getEntityType().getProperties();

            List<Restriction> restrictions = new ArrayList<Restriction>();

            for (String property : properties) {
                restrictions.add(criteria.getOperator().getRestriction(property, criteria.getSearch()));
            }

            searchCriteria.addRestriction(new AnyRestriction(restrictions.toArray(new Restriction[0])));
        }

        searchCriteria.setDistinct(true);

        return (List<OnmsNode>) nodeDao.findMatching(searchCriteria);
    }
}
