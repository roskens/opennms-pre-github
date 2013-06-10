package org.opennms.features.vaadin.nodebrowser.view;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.opennms.features.vaadin.nodebrowser.model.Criteria;
import org.opennms.features.vaadin.nodebrowser.search.NodeSearch;
import org.opennms.netmgt.model.OnmsNode;

import java.util.ArrayList;
import java.util.List;

public class CriteriaBuilderComponent extends VerticalLayout {
    BeanContainer<Integer, OnmsNode> beanContainer = new BeanContainer<Integer, OnmsNode>(OnmsNode.class);
    private List<CriteriaComponent> criteriaComponents = new ArrayList<CriteriaComponent>();
    private NodeSearch nodeSearch;

    public CriteriaBuilderComponent(NodeSearch nodeSearch, List<Criteria> criterias) {
        this.nodeSearch = nodeSearch;

        beanContainer.setBeanIdProperty("id");

        setCriterias(criterias);

        setMargin(true);
    }

    public void refresh() {
        nodeSearch.search(getCriterias());
    }

    private void renderComponents() {
        removeAllComponents();

        boolean isFirst = true;

        for (final CriteriaComponent criteriaComponent : criteriaComponents) {
            criteriaComponent.render();

            if (!isFirst) {
                Button minusButton = new Button("-");
                //minusButton.setIcon(new ThemeResource("list-remove.png"));

                minusButton.addListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        criteriaComponents.remove(criteriaComponent);
                        renderComponents();
                    }
                });

                criteriaComponent.addComponent(minusButton);
            } else {
                Button plusButton = new Button("+");
                //plusButton.setIcon(new ThemeResource("list-add.png"));

                plusButton.addListener(new Button.ClickListener() {
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        criteriaComponents.add(new CriteriaComponent(CriteriaBuilderComponent.this, new Criteria(Criteria.EntityType.Id, Criteria.Operator.Equals, "*")));
                        renderComponents();
                    }
                });

                criteriaComponent.addComponent(plusButton);

                isFirst = false;
            }

            addComponent(criteriaComponent);
        }
    }

    public List<Criteria> getCriterias() {
        List<Criteria> criterias = new ArrayList<Criteria>();

        for (CriteriaComponent criteriaComponent : criteriaComponents) {
            criterias.add(criteriaComponent.getCriteria());
        }

        return criterias;
    }

    public void setCriterias(List<Criteria> criterias) {
        if (criterias == null) {
            criterias = new ArrayList<Criteria>();
        }

        if (criterias.size() == 0) {
            criterias.add(new Criteria());
        }

        criteriaComponents.clear();

        for (Criteria criteria : criterias) {
            criteriaComponents.add(new CriteriaComponent(CriteriaBuilderComponent.this, criteria));
        }

        renderComponents();
    }
}
