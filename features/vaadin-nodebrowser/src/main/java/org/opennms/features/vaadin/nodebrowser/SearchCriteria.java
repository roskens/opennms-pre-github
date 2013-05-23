package org.opennms.features.vaadin.nodebrowser;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteria {
    public static enum Operator {
        LIKE("like"),
        NOT_LIKE("not like");

        private String title;

        private Operator(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public static enum Key {
        NODE("Node", "id", "label"),
        INTERFACES("Interfaces", "ipAddresses"),
        CATEGORIES("Categories", "categories"),
        SERVICES("Services", "services");

        private String title;
        private String properties[];

        private Key(String title, String... properties) {
            this.title = title;
            this.properties = properties;
        }

        public String getTitle() {
            return title;
        }

        public Container.Filter getFilter(String s) {
            List<Container.Filter> filterList = new ArrayList<Container.Filter>();
            for (String property : properties) {
                filterList.add(new WildcardFilter(property, s));
            }

            return new Or(filterList.toArray(new Container.Filter[]{}));
        }
    }

    private Select selectOperator, selectKey;
    private TextField textField;
    private NodeSearch nodeSearch;
    private String value;

    public SearchCriteria(NodeSearch nodeSearch, Key keyToSet, Operator operatorToSet, String valueToSet) {
        this.nodeSearch = nodeSearch;

        selectKey = new Select();

        for (SearchCriteria.Key key : SearchCriteria.Key.values()) {
            selectKey.addItem(key);
            selectKey.setItemCaption(key, key.getTitle());
        }

        selectKey.setValue(keyToSet);
        selectKey.setNullSelectionAllowed(false);
        selectKey.setNewItemsAllowed(false);
        selectKey.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
        selectKey.setImmediate(true);

        selectKey.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                SearchCriteria.this.nodeSearch.search();
            }
        });

        selectOperator = new Select();

        for (SearchCriteria.Operator operator : SearchCriteria.Operator.values()) {
            selectOperator.addItem(operator);
            selectOperator.setItemCaption(operator, operator.getTitle());
        }

        selectOperator.setValue(operatorToSet);
        selectOperator.setNullSelectionAllowed(false);
        selectOperator.setNewItemsAllowed(false);
        selectOperator.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
        selectOperator.setNullSelectionAllowed(false);
        selectOperator.setImmediate(true);

        selectOperator.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                SearchCriteria.this.nodeSearch.search();
            }
        });

        textField = new TextField();
        textField.setValue(valueToSet);
        textField.setImmediate(true);
        textField.setWidth("100%");

        textField.addListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent textChangeEvent) {
                value = textChangeEvent.getText();
                SearchCriteria.this.nodeSearch.search();
                value = null;
            }
        });

        textField.setImmediate(true);
    }

    public HorizontalLayout getHorizontalLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.addComponent(selectKey);
        horizontalLayout.addComponent(selectOperator);
        horizontalLayout.addComponent(textField);
        horizontalLayout.setExpandRatio(textField, 1);
        horizontalLayout.setWidth("75%");

        return horizontalLayout;
    }

    public Key getKey() {
        return (Key) selectKey.getValue();
    }

    public Operator getOperator() {
        return (Operator) selectOperator.getValue();
    }

    public String getValue() {
        if (value != null) {
            return value;
        } else {
            return textField.getValue().toString();
        }
    }
}
