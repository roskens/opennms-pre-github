package org.opennms.vaadin.applicationstack.view;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.vaadin.applicationstack.model.ApplicationLayer;
import org.opennms.vaadin.applicationstack.model.ApplicationStack;
import org.opennms.vaadin.applicationstack.model.Criteria;
import org.opennms.vaadin.applicationstack.model.NodeDummy;
import org.opennms.vaadin.applicationstack.provider.NodeListProvider;

import java.util.List;
import java.util.Random;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Theme("applicationstack")
public class ApplicationStackUI extends UI {
    private NodeListProvider nodeListProvider;

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setContent(layout);
        setSizeFull();

        final ApplicationStack stack = new ApplicationStack("Waschmaschine")
                .addLayer(new ApplicationLayer("Layer1", 0, 0, 4, 1))
                .addLayer(new ApplicationLayer("Layer2a", 1, 0, 2, 1))
                .addLayer(new ApplicationLayer("Layer2b", 1, 2, 2, 1))
                .addLayer(new ApplicationLayer("Layer3", 2, 0, 4, 1))
                .addLayer(new ApplicationLayer("Layer4", 0, 4, 1, 3))
                .addLayer(new ApplicationLayer("Layer5", 0, 5, 1, 3))
                .addLayer(new ApplicationLayer("Layer6a", 3, 0, 2, 1))
                .addLayer(new ApplicationLayer("Layer6b", 3, 2, 1, 1))
                .addLayer(new ApplicationLayer("Layer6c", 3, 3, 1, 1))
                .addLayer(new ApplicationLayer("Layer6d", 3, 4, 2, 1))
                .addLayer(new ApplicationLayer("Layer7", 4, 0, 6, 1))
                .addLayer(new ApplicationLayer("Layer8a", 5, 0, 1, 2))
                .addLayer(new ApplicationLayer("Layer8b", 5, 1, 1, 2))
                .addLayer(new ApplicationLayer("Layer8c", 5, 2, 1, 2))
                .addLayer(new ApplicationLayer("Layer8d", 5, 3, 1, 2))
                .addLayer(new ApplicationLayer("Layer8e_1", 5, 4, 1, 1))
                .addLayer(new ApplicationLayer("Layer8e_2", 6, 4, 1, 1))
                .addLayer(new ApplicationLayer("Layer8f", 5, 5, 1, 2))
                .addLayer(new ApplicationLayer("Layer9", 7, 0, 6, 1))
                .addLayer(new ApplicationLayer("Layer10a", 8, 0, 3, 1))
                .addLayer(new ApplicationLayer("Layer10b", 8, 3, 3, 1))
                .addLayer(new ApplicationLayer("Layer11", 9, 0, 6, 1));

        registerDummyNodes(stack);

        layout.addComponent(new ApplicationStackComponent(nodeListProvider).render(stack));

        testCriteria(new Criteria(Criteria.EntityType.Id, Criteria.Operator.Equals, "5"));
        testCriteria(new Criteria(Criteria.EntityType.Id, Criteria.Operator.In, "5,6,7"));
        testCriteria(new Criteria(Criteria.EntityType.Categories, Criteria.Operator.Equals, "VMware3"));
        testCriteria(new Criteria(Criteria.EntityType.Categories, Criteria.Operator.In, "VMware3,VMware4,VMware5"));
        testCriteria(new Criteria(Criteria.EntityType.Interfaces, Criteria.Operator.Equals, "193.174.29.3"));
        testCriteria(new Criteria(Criteria.EntityType.Interfaces, Criteria.Operator.In, "193.174.29.3,193.174.29.1"));
        testCriteria(new Criteria(Criteria.EntityType.Services, Criteria.Operator.Equals, "HTTP"));
        testCriteria(new Criteria(Criteria.EntityType.Services, Criteria.Operator.In, "ICMP,HTTP,SNMP"));

    }

    private void testCriteria(Criteria criteria) {
        try {
            System.out.println("Testing " + criteria);
            ApplicationLayer layer = new ApplicationLayer("LayerX", 5, 5, 3, 3);
            layer.addCriteria(criteria);

            List<OnmsNode> nodes = getNodeListProvider().getNodesForCriterias(layer.getCriterias());
            for (OnmsNode onmsNode : nodes) {
                System.out.println("BMRHGA: " + onmsNode.getId() + " " + onmsNode.getLabel());
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void registerDummyNodes(ApplicationStack stack) {
        for (ApplicationLayer eachLayer : stack.getLayers()) {
            int max = 100;
            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                int good = random.nextInt(max) + 1;
                max -= good - 1;
                int problems = random.nextInt(max) + 1;
                max -= problems - 1;
                int death = random.nextInt(max) + 1;
                stack.registerNode(eachLayer.getLabel(), new NodeDummy("node" + (i + 1), good, death, problems));
            }
        }
    }

    public void setNodeListProvider(NodeListProvider nodeListProvider) {
        this.nodeListProvider = nodeListProvider;
    }

    public NodeListProvider getNodeListProvider() {
        return nodeListProvider;
    }
}