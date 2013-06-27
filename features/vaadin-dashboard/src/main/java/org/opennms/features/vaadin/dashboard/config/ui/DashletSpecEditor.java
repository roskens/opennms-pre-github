package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.opennms.features.vaadin.dashboard.config.DashletSelector;
import org.opennms.features.vaadin.dashboard.model.DashletFactory;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class DashletSpecEditor extends Panel {
    private DashletSpec m_dashletSpec;
    private WallboardEditor m_wallboardEditor;
    private NativeSelect m_dashletSelect;
    private boolean savingDisabled = false;
    private DashletSelector m_dashletSelector;
    private Button propertiesButton;

    public DashletSpecEditor(WallboardEditor wallboardEditor, DashletSelector dashletSelector, DashletSpec dashletSpec) {
        this.m_wallboardEditor = wallboardEditor;
        this.m_dashletSpec = dashletSpec;
        this.m_dashletSelector = dashletSelector;

        setWidth(100.0f, Unit.PERCENTAGE);

        GridLayout gridLayout = new GridLayout();
        gridLayout.setColumns(4);
        gridLayout.setRows(1);

        gridLayout.setSpacing(true);
        gridLayout.setMargin(true);

        // Class selection

        m_dashletSelect = new NativeSelect();

        m_dashletSelect.setCaption("Dashlet");

        updateDashletSelection(dashletSelector.getDashletList());

        m_dashletSelect.setImmediate(true);
        m_dashletSelect.setNewItemsAllowed(false);
        m_dashletSelect.setNullSelectionItemId("Undefined");
        m_dashletSelect.select(dashletSpec.getDashletName());

        m_dashletSelect.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if (savingDisabled) {
                    return;
                }

                if (valueChangeEvent.getProperty().getValue() == null) {
                    m_dashletSpec.setDashletName("Undefined");
                } else {
                    m_dashletSpec.setDashletName(valueChangeEvent.getProperty().getValue().toString());
                }

                m_dashletSpec.getParameters().clear();

                Map<String, String> requiredParameters = m_dashletSelector.getFactoryForName(m_dashletSpec.getDashletName()).getRequiredParameters();

                for (Map.Entry<String, String> entry : requiredParameters.entrySet()) {
                    m_dashletSpec.getParameters().put(entry.getKey(), entry.getValue());
                }

                propertiesButton.setEnabled(requiredParameters.size() > 0);

                WallboardProvider.getInstance().save();
                ((WallboardConfigUI) getUI()).notifyMessage("Data saved", "Dashlet");
            }
        });

        FormLayout f1 = new FormLayout();
        f1.addComponent(m_dashletSelect);

        // priority fields

        final TextField priorityField = new TextField();
        priorityField.setValue(String.valueOf(dashletSpec.getPriority()));
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
                    m_dashletSpec.setPriority(Integer.valueOf((String) valueChangeEvent.getProperty().getValue()));
                    WallboardProvider.getInstance().save();
                    ((WallboardConfigUI) getUI()).notifyMessage("Data saved", "Priority");
                }
            }
        });

        final TextField boostPriorityField = new TextField();
        boostPriorityField.setValue(String.valueOf(dashletSpec.getPriority()));
        boostPriorityField.setImmediate(true);
        boostPriorityField.setCaption("Boost-Priority");

        boostPriorityField.addValidator(new AbstractStringValidator("Only numbers allowed here") {
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

        boostPriorityField.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if (priorityField.isValid()) {
                    m_dashletSpec.setPriority(Integer.valueOf((String) valueChangeEvent.getProperty().getValue()));
                    WallboardProvider.getInstance().save();
                    ((WallboardConfigUI) getUI()).notifyMessage("Data saved", "Priority");
                }
            }
        });


        // duration field
        final TextField durationField = new TextField();
        durationField.setValue(String.valueOf(dashletSpec.getDuration()));
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
                    m_dashletSpec.setDuration(Integer.valueOf((String) valueChangeEvent.getProperty().getValue()));
                    WallboardProvider.getInstance().save();
                    ((WallboardConfigUI) getUI()).notifyMessage("Data saved", "Duration");
                }
            }
        });

        final TextField boostDurationField = new TextField();
        boostDurationField.setValue(String.valueOf(dashletSpec.getDuration()));
        boostDurationField.setImmediate(true);
        boostDurationField.setCaption("Boost-Duration");

        boostDurationField.addValidator(new AbstractStringValidator("Only numbers allowed here") {
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

        boostDurationField.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if (durationField.isValid()) {
                    m_dashletSpec.setDuration(Integer.valueOf((String) valueChangeEvent.getProperty().getValue()));
                    WallboardProvider.getInstance().save();
                    ((WallboardConfigUI) getUI()).notifyMessage("Data saved", "Duration");
                }
            }
        });

        // adding to form layout
        FormLayout f2 = new FormLayout();
        f2.addComponent(priorityField);
        f2.addComponent(durationField);

        // adding to form layout
        FormLayout f3 = new FormLayout();
        f3.addComponent(boostPriorityField);
        f3.addComponent(boostDurationField);

        propertiesButton = new Button("Properties");

        propertiesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                editProperties();
            }
        });

        propertiesButton.setEnabled(m_dashletSelector.getFactoryForName(m_dashletSpec.getDashletName()).getRequiredParameters().size() > 0);

        Button removeButton = new Button("Remove");

        FormLayout f4 = new FormLayout();
        f4.addComponent(propertiesButton);
        f4.addComponent(removeButton);

        removeButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                m_wallboardEditor.removeDashletSpecEditor(DashletSpecEditor.this);
            }
        });

        gridLayout.addComponent(f1);
        gridLayout.addComponent(f2);
        gridLayout.addComponent(f3);
        gridLayout.addComponent(f4);

        setContent(gridLayout);
    }

    protected void editProperties() {

        final Map<String, String> requiredParameters = m_dashletSelector.getFactoryForName(m_dashletSpec.getDashletName()).getRequiredParameters();

        final Window window = new Window("Properties");

        window.setModal(true);
        window.setClosable(false);
        window.setResizable(false);
        window.setWidth("60%");

        getUI().addWindow(window);

        window.setContent(new VerticalLayout() {
            {
                final Table table = new Table() {
                    {
                        setTableFieldFactory(new DefaultFieldFactory() {
                            @Override
                            public Field createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                                Field field = super.createField(container, itemId, propertyId, uiContext);
                                if (propertyId.equals("Key")) {
                                    field.setReadOnly(true);
                                } else {
                                    field.setSizeFull();
                                }
                                return field;
                            }
                        });
                        setEditable(true);
                        setSizeFull();
                        setMargin(true);
                        setImmediate(true);
                        addContainerProperty("Key", String.class, "");
                        addContainerProperty("Value", String.class, "");

                        for (Map.Entry<String, String> entry : requiredParameters.entrySet()) {
                            if (m_dashletSpec.getParameters().containsKey(entry.getKey())) {
                                addItem(new Object[]{entry.getKey(), m_dashletSpec.getParameters().get(entry.getKey())}, entry.getKey());
                            } else {
                                addItem(new Object[]{entry.getKey(), entry.getValue()}, entry.getKey());
                            }
                        }

                        setColumnWidth("Key", 100);
                        setColumnWidth("Value", -1);
                    }
                };

                addComponent(table);

                addComponent(new HorizontalLayout() {
                    {
                        setMargin(true);
                        setSpacing(true);
                        setWidth("100%");

                        Button cancel = new Button("Cancel");
                        cancel.addClickListener(new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                window.close();
                            }
                        });

                        cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE, null);
                        addComponent(cancel);
                        setExpandRatio(cancel, 1);
                        setComponentAlignment(cancel, Alignment.TOP_RIGHT);

                        Button ok = new Button("Save");

                        ok.addClickListener(new Button.ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                for (Map.Entry<String, String> entry : requiredParameters.entrySet()) {
                                    String newValue = table.getItem(entry.getKey()).getItemProperty("Value").getValue().toString();
                                    m_dashletSpec.getParameters().put(entry.getKey(), newValue);
                                }

                                WallboardProvider.getInstance().save();
                                ((WallboardConfigUI) getUI()).notifyMessage("Data saved", "Duration");

                                window.close();
                            }
                        });

                        ok.setClickShortcut(ShortcutAction.KeyCode.ENTER, null);

                        addComponent(ok);
                    }
                });
            }
        });
    }

    public void updateDashletSelection(List<DashletFactory> factoryList) {
        savingDisabled = true;

        String savedSelection = (m_dashletSelect.getValue() == null ? "Undefined" : m_dashletSelect.getValue().toString());

        if (!m_dashletSelect.removeAllItems()) {
            LoggerFactory.getLogger(DashletSpecEditor.class).warn("problem removing items");
        }

        for (DashletFactory dashletFactory : factoryList) {
            m_dashletSelect.addItem(dashletFactory.getName());
        }

        m_dashletSelect.select(savedSelection);

        savingDisabled = false;
    }

    public DashletSpec getDashletSpec() {
        return m_dashletSpec;
    }
}
