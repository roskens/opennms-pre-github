package org.opennms.features.vaadin.nodebrowser.view;

import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import org.opennms.features.vaadin.nodebrowser.model.Criteria;

public class CriteriaComponent extends HorizontalLayout {
    private final Criteria criteria;
    private final TextField searchField = new TextField();
    private final NativeSelect entityField = new NativeSelect();
    private final NativeSelect operatorField = new NativeSelect();
    private final CriteriaBuilderComponent criteriaBuilderComponent;
    private String searchFieldValue = null;

    public CriteriaComponent(CriteriaBuilderComponent criteriaBuilderComponent, Criteria criteria) {
        this.criteriaBuilderComponent = criteriaBuilderComponent;
        this.criteria = criteria;

        setSizeFull();

        render();
    }

    public void render() {
        removeAllComponents();

        entityField.setNullSelectionAllowed(false);
        entityField.setMultiSelect(false);
        entityField.setNewItemsAllowed(false);

        for (Criteria.EntityType entityType : Criteria.EntityType.values()) {
            entityField.addItem(entityType);
        }

        entityField.setValue(criteria.getEntityType());
        entityField.setWidth(100, UNITS_PERCENTAGE);

        operatorField.setNullSelectionAllowed(false);
        operatorField.setMultiSelect(false);
        operatorField.setNewItemsAllowed(false);

        for (Criteria.Operator operator : Criteria.Operator.values()) {
            operatorField.addItem(operator);
        }

        operatorField.setValue(criteria.getOperator());
        operatorField.setWidth(100, UNITS_PERCENTAGE);

        searchField.setValue(criteria.getSearch());
        searchField.setWidth(100, UNITS_PERCENTAGE);

        addComponent(entityField);
        addComponent(operatorField);
        addComponent(searchField);

        setExpandRatio(entityField, 0.25f);
        setExpandRatio(operatorField, 0.25f);
        setExpandRatio(searchField, 0.50f);

        searchField.setImmediate(true);
        operatorField.setImmediate(true);
        entityField.setImmediate(true);

        searchField.addListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent textChangeEvent) {

                searchFieldValue = textChangeEvent.getText();

                CriteriaComponent.this.criteriaBuilderComponent.refresh();
            }
        });

        entityField.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                CriteriaComponent.this.criteriaBuilderComponent.refresh();
            }
        });

        operatorField.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                CriteriaComponent.this.criteriaBuilderComponent.refresh();
            }
        });
    }

    public Criteria getCriteria() {
        criteria.setEntityType((Criteria.EntityType) entityField.getValue());
        criteria.setOperator((Criteria.Operator) operatorField.getValue());
        if (searchFieldValue != null) {
            criteria.setSearch(searchFieldValue);
            if (searchFieldValue.equals(searchField.getValue())) {
                searchFieldValue = null;
            }
        } else {
            criteria.setSearch(String.valueOf(searchField.getValue()));
        }

        return criteria;
    }
}
