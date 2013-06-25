package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.*;
import org.opennms.features.vaadin.dashboard.config.DashletSelector;
import org.opennms.features.vaadin.dashboard.model.Sandwich;
import org.opennms.features.vaadin.dashboard.model.SandwichBoard;
import org.opennms.features.vaadin.dashboard.model.SandwichSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SandwichBoardModelEditor extends VerticalLayout implements DashletSelector.ServiceListChangedListener {
    private TextField titleField = new TextField("Title");
    private TabSheet.Tab tab;
    private VerticalLayout verticalLayout = new VerticalLayout();
    private Button addButton;
    private DashletSelector dashletSelector;
    private SandwichBoard sandwichBoard;
    private Map<SandwichSpec, SandwichSpecEditor> sandwichSpecEditorMap = new HashMap<SandwichSpec, SandwichSpecEditor>();

    public SandwichBoardModelEditor(DashletSelector dashletSelector, SandwichBoard sandwichBoard) {
        this.dashletSelector = dashletSelector;
        this.sandwichBoard = sandwichBoard;

        for (SandwichSpec sandwichSpec : sandwichBoard.getSandwiches()) {
            addSandwichSpec(sandwichSpec);
        }

        setMargin(true);

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        addButton = new Button("Add dashlet");

        addButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                addSandwichSpec(new SandwichSpec());
            }
        });

        FormLayout formLayout1 = new FormLayout();
        formLayout1.addComponent(titleField);
        horizontalLayout.addComponent(formLayout1);

        FormLayout formLayout2 = new FormLayout();
        formLayout2.addComponent(addButton);
        horizontalLayout.addComponent(formLayout2);

        titleField.setValue(sandwichBoard.getTitle());
        titleField.setImmediate(true);
        titleField.addValidator(new AbstractStringValidator("Title must be unique") {
            @Override
            protected boolean isValidValue(String s) {
                return (!DashboardProvider.getInstance().containsDashboard(s) || DashboardProvider.getInstance().getDashboard(s).equals(SandwichBoardModelEditor.this.sandwichBoard)) && !"".equals(s);
            }
        });

        titleField.addTextChangeListener(new FieldEvents.TextChangeListener() {
            public void textChange(FieldEvents.TextChangeEvent textChangeEvent) {
                AbstractTextField source = (AbstractTextField) textChangeEvent.getSource();
                source.setValue(textChangeEvent.getText());
                if (source.isValid()) {
                    tab.setCaption(textChangeEvent.getText());
                    SandwichBoardModelEditor.this.sandwichBoard.setTitle(textChangeEvent.getText());
                    DashboardProvider.getInstance().save();
                    notifyMessage("Data saved!");
                }
            }
        });

        addComponent(horizontalLayout);
        addComponent(verticalLayout);

        dashletSelector.addServiceListChangedListener(this);
    }

    public void serviceListChanged(List<Sandwich> serviceList) {
        for (SandwichSpecEditor sandwichSpecEditor : sandwichSpecEditorMap.values()) {
            sandwichSpecEditor.updateDashletSelection(serviceList);
        }
        notifyMessage("Dashlet list changed!");
    }

    public SandwichBoard getSandwichBoard() {
        return sandwichBoard;
    }

    private void notifyMessage(String message) {
        Notification notification = new Notification(message, Notification.Type.HUMANIZED_MESSAGE);
        notification.setDelayMsec(1000);
        notification.show(getUI().getPage());
    }

    public void setTab(TabSheet.Tab tab) {
        this.tab = tab;
    }

    public void removeSandwichSpec(SandwichSpecEditor sandwichSpecEditor) {
        verticalLayout.removeComponent(sandwichSpecEditor);
        sandwichSpecEditorMap.remove(sandwichSpecEditor.getSandwichSpec());
        sandwichBoard.getSandwiches().remove(sandwichSpecEditor.getSandwichSpec());

        DashboardProvider.getInstance().save();
    }

    private void addSandwichSpec(SandwichSpec sandwichSpec) {
        SandwichSpecEditor sandwichSpecEditor = new SandwichSpecEditor(this, dashletSelector, sandwichSpec);

        sandwichSpecEditorMap.put(sandwichSpec, sandwichSpecEditor);
        verticalLayout.addComponent(sandwichSpecEditor);

        if (!sandwichBoard.getSandwiches().contains(sandwichSpec)) {
            sandwichBoard.getSandwiches().add(sandwichSpec);

            DashboardProvider.getInstance().save();
        }
    }
}
