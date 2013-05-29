package org.opennms.vaadin.applicationstack.view;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import org.opennms.vaadin.applicationstack.model.Criteria;

public class CriteriaComponent extends HorizontalLayout {
    private final Criteria criteria;
    private final TextField searchField = new TextField();
    private final NativeSelect entityField = new NativeSelect();
    private final NativeSelect operatorField = new NativeSelect();

    public CriteriaComponent(Criteria criteria) {
        this.criteria = criteria;

        render();
    }

    public void render() {
        removeAllComponents();

        setSpacing(true);

        entityField.setNullSelectionAllowed(false);
        entityField.setMultiSelect(false);
        entityField.setNewItemsAllowed(false);

        for (Criteria.EntityType entityType : Criteria.EntityType.values()) {
            entityField.addItem(entityType);
        }

        entityField.setValue(criteria.getEntityType());
        entityField.setWidth(100, Unit.PERCENTAGE);

        operatorField.setNullSelectionAllowed(false);
        operatorField.setMultiSelect(false);
        operatorField.setNewItemsAllowed(false);

        for (Criteria.Operator operator : Criteria.Operator.values()) {
            operatorField.addItem(operator);
        }

        operatorField.setValue(criteria.getOperator());
        operatorField.setWidth(100, Unit.PERCENTAGE);

        searchField.setValue(criteria.getSearch());
        searchField.setWidth(100, Unit.PERCENTAGE);

        addComponent(entityField);
        addComponent(operatorField);
        addComponent(searchField);

        setExpandRatio(entityField, 1);
        setExpandRatio(operatorField, 1);
        setExpandRatio(searchField, 2);
    }

    public Criteria getCriteria() {
        criteria.setEntityType((Criteria.EntityType) entityField.getValue());
        criteria.setOperator((Criteria.Operator) operatorField.getValue());
        criteria.setSearch(searchField.getValue());

        return criteria;
    }
}
