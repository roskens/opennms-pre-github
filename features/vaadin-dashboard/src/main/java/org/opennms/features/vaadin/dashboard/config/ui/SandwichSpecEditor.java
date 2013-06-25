package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.data.Property;
import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.ui.*;
import org.opennms.features.vaadin.dashboard.config.DashletSelector;
import org.opennms.features.vaadin.dashboard.model.Sandwich;
import org.opennms.features.vaadin.dashboard.model.SandwichSpec;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SandwichSpecEditor extends Panel {
    private SandwichSpec sandwichSpec;
    private SandwichBoardModelEditor sandwichBoardModelEditor;
    private DashletSelector dashletSelector;
    private NativeSelect dashletSelect;


    public SandwichSpecEditor(SandwichBoardModelEditor sandwichBoardModelEditor, DashletSelector dashletSelector, SandwichSpec sandwichSpec) {
        this.sandwichBoardModelEditor = sandwichBoardModelEditor;
        this.sandwichSpec = sandwichSpec;
        this.dashletSelector = dashletSelector;

        setWidth(100.0f, Unit.PERCENTAGE);

        GridLayout gridLayout = new GridLayout();
        gridLayout.setColumns(4);
        gridLayout.setRows(1);

        gridLayout.setSpacing(true);
        gridLayout.setMargin(true);

        // Class selection

        dashletSelect = new NativeSelect();

        dashletSelect.setCaption("Dashlet");

        updateDashletSelection(dashletSelector.getSandwichList());

        dashletSelect.setImmediate(true);
        dashletSelect.setNewItemsAllowed(false);
        dashletSelect.setNullSelectionItemId("Undefined");
        dashletSelect.select(sandwichSpec.getDashlet());

        dashletSelect.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if (valueChangeEvent.getProperty().getValue() == null) {
                    SandwichSpecEditor.this.sandwichSpec.setDashlet("Undefined");
                } else {
                    SandwichSpecEditor.this.sandwichSpec.setDashlet(valueChangeEvent.getProperty().getValue().toString());
                }
                DashboardProvider.getInstance().save();
                notifyMessage("Data saved");
            }
        });

        FormLayout f1 = new FormLayout();
        f1.addComponent(dashletSelect);

        // priority fields

        final TextField priorityField = new TextField();
        priorityField.setValue(String.valueOf(sandwichSpec.getPriority()));
        priorityField.setImmediate(true);
        priorityField.setCaption("Priority");

        priorityField.addValidator(new AbstractStringValidator("Only numbers allowed here") {
            @Override
            protected boolean isValidValue(String s) {
                try {
                    Integer.parseInt(s);
                } catch (NumberFormatException numberFormatException) {
                    return false;
                }
                return true;
            }
        });

        priorityField.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if (priorityField.isValid()) {
                    SandwichSpecEditor.this.sandwichSpec.setPriority(Integer.valueOf((String) valueChangeEvent.getProperty().getValue()));
                    DashboardProvider.getInstance().save();
                    notifyMessage("Data saved");
                }
            }
        });

        // duration field
        final TextField durationField = new TextField();
        durationField.setValue(String.valueOf(sandwichSpec.getDuration()));
        durationField.setImmediate(true);
        durationField.setCaption("Duration");

        durationField.addValidator(new AbstractStringValidator("Only numbers allowed here") {
            @Override
            protected boolean isValidValue(String s) {
                try {
                    Integer.parseInt(s);
                } catch (NumberFormatException numberFormatException) {
                    return false;
                }
                return true;
            }
        });

        durationField.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if (durationField.isValid()) {
                    SandwichSpecEditor.this.sandwichSpec.setDuration(Integer.valueOf((String) valueChangeEvent.getProperty().getValue()));
                    DashboardProvider.getInstance().save();
                    notifyMessage("Data saved");
                }
            }
        });

        // adding to form layout
        FormLayout f2 = new FormLayout();
        f2.addComponent(priorityField);
        f2.addComponent(durationField);

        // pausable
        CheckBox pausableCheckbox = new CheckBox();
        pausableCheckbox.setValue(sandwichSpec.isPausable());
        pausableCheckbox.setCaption("Pausable");
        pausableCheckbox.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                SandwichSpecEditor.this.sandwichSpec.setPausable((Boolean) valueChangeEvent.getProperty().getValue());
                DashboardProvider.getInstance().save();
                notifyMessage("Data saved");
            }
        });

        //
        FormLayout f3 = new FormLayout();
        f3.addComponent(pausableCheckbox);

        Button propertiesButton = new Button("Properties");
        Button removeButton = new Button("Remove");

        FormLayout f4 = new FormLayout();
        f4.addComponent(propertiesButton);
        f4.addComponent(removeButton);

        removeButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                SandwichSpecEditor.this.sandwichBoardModelEditor.removeSandwichSpec(SandwichSpecEditor.this);
            }
        });

        gridLayout.addComponent(f1);
        gridLayout.addComponent(f2);
        gridLayout.addComponent(f3);
        gridLayout.addComponent(f4);

        setContent(gridLayout);
    }

    public void updateDashletSelection(List<Sandwich> serviceList) {
        String savedSelection = (dashletSelect.getValue() == null ? "undefined" : dashletSelect.getValue().toString());

        if (!dashletSelect.removeAllItems()) {
            LoggerFactory.getLogger(SandwichSpecEditor.class).warn("problem removing items");
        }

        for (Sandwich sandwich : serviceList) {
            dashletSelect.addItem(sandwich.getName());
        }

        dashletSelect.select(savedSelection);
    }

    public SandwichSpec getSandwichSpec() {
        return sandwichSpec;
    }

    private void notifyMessage(String message) {
        Notification notification = new Notification(message, Notification.Type.HUMANIZED_MESSAGE);
        notification.setDelayMsec(1000);
        notification.show(getUI().getPage());
    }
}
