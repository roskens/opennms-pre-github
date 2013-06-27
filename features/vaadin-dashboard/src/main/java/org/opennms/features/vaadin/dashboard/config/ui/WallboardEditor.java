package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.*;
import org.opennms.features.vaadin.dashboard.config.DashletSelector;
import org.opennms.features.vaadin.dashboard.model.DashletFactory;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;
import org.opennms.features.vaadin.dashboard.model.Wallboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallboardEditor extends VerticalLayout {
    private TabSheet.Tab m_tab;
    private VerticalLayout m_verticalLayout = new VerticalLayout();
    private DashletSelector m_dashletSelector;
    private Wallboard m_wallboard;
    private Map<DashletSpec, DashletSpecEditor> m_dashletSpecEditorMap = new HashMap<DashletSpec, DashletSpecEditor>();

    public WallboardEditor(DashletSelector dashletSelector, Wallboard wallboard) {
        this.m_dashletSelector = dashletSelector;
        this.m_wallboard = wallboard;

        for (DashletSpec dashletSpec : wallboard.getDashletSpecs()) {
            addDashletSpec(dashletSpec);
        }

        setMargin(true);

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        final Button addButton = new Button("Add dashlet");

        addButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent clickEvent) {
                addDashletSpec(new DashletSpec());
            }
        });

        final TextField titleField = new TextField();
        titleField.setValue(wallboard.getTitle());
        titleField.setImmediate(true);
        titleField.addValidator(new AbstractStringValidator("Title must be unique") {
            @Override
            protected boolean isValidValue(String s) {
                return (!WallboardProvider.getInstance().containsWallboard(s) || WallboardProvider.getInstance().getWallboard(s).equals(m_wallboard)) && !"".equals(s);
            }
        });

        titleField.addTextChangeListener(new FieldEvents.TextChangeListener() {
            public void textChange(FieldEvents.TextChangeEvent textChangeEvent) {
                AbstractTextField source = (AbstractTextField) textChangeEvent.getSource();
                source.setValue(textChangeEvent.getText());
                if (source.isValid()) {
                    m_tab.setCaption(textChangeEvent.getText());
                    m_wallboard.setTitle(textChangeEvent.getText());
                    WallboardProvider.getInstance().save();
                    ((WallboardConfigUI) getUI()).notifyMessage("Data saved", "Title");
                }
            }
        });

        FormLayout formLayout1 = new FormLayout();
        formLayout1.addComponent(titleField);
        horizontalLayout.addComponent(formLayout1);

        FormLayout formLayout2 = new FormLayout();
        formLayout2.addComponent(addButton);
        horizontalLayout.addComponent(formLayout2);

        addComponent(horizontalLayout);
        addComponent(m_verticalLayout);
    }

    public void updateServiceList(List<DashletFactory> serviceList) {
        for (DashletSpecEditor dashletSpecEditor : m_dashletSpecEditorMap.values()) {
            dashletSpecEditor.updateDashletSelection(serviceList);
        }
        ((WallboardConfigUI) getUI()).notifyMessage("Configuration change", "Dashlet list modified");
    }

    public Wallboard getWallboard() {
        return m_wallboard;
    }

    public void setTab(TabSheet.Tab tab) {
        this.m_tab = tab;
    }

    public void removeDashletSpecEditor(DashletSpecEditor dashletSpecEditor) {
        m_verticalLayout.removeComponent(dashletSpecEditor);
        m_dashletSpecEditorMap.remove(dashletSpecEditor.getDashletSpec());
        m_wallboard.getDashletSpecs().remove(dashletSpecEditor.getDashletSpec());

        WallboardProvider.getInstance().save();
    }

    private void addDashletSpec(DashletSpec dashletSpec) {
        DashletSpecEditor dashletSpecEditor = new DashletSpecEditor(this, m_dashletSelector, dashletSpec);

        m_dashletSpecEditorMap.put(dashletSpec, dashletSpecEditor);
        m_verticalLayout.addComponent(dashletSpecEditor);

        if (!m_wallboard.getDashletSpecs().contains(dashletSpec)) {
            m_wallboard.getDashletSpecs().add(dashletSpec);

            WallboardProvider.getInstance().save();
        }
    }
}
