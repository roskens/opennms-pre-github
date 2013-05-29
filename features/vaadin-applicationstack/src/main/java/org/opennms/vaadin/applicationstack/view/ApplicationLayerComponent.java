package org.opennms.vaadin.applicationstack.view;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.opennms.vaadin.applicationstack.model.ApplicationLayer;
import org.opennms.vaadin.applicationstack.model.ApplicationStack;
import org.opennms.vaadin.applicationstack.model.Criteria;

import java.util.ArrayList;

/**
 * @author mvrueden
 */
public class ApplicationLayerComponent extends CustomComponent {
    private boolean editable = true;
    private final HealthIndicator healthIndicator = new HealthIndicator();
    private final Label label = new Label();

    public ApplicationLayerComponent() {
        setStyleName("applicationLayer");

        Panel p = new Panel();
        p.setSizeFull();
        p.setStyleName("applicationLayer");

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        if (isEditable()) {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setSpacing(true);
            Button editButton = new Button("(edit)");

            editButton.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent clickEvent) {
                    UI.getCurrent().addWindow(new CriteriaBuilderComponent(new ArrayList<Criteria>()));
                }
            });

            editButton.setStyleName(Reindeer.BUTTON_LINK);

            horizontalLayout.addComponent(label);
            horizontalLayout.addComponent(editButton);

            content.addComponent(horizontalLayout);
        } else {
            content.addComponent(label);
        }

        content.addComponent(healthIndicator);
        content.setComponentAlignment(content.getComponent(1), Alignment.BOTTOM_LEFT);
        content.setSpacing(false);
        content.setMargin(false);

        setCompositionRoot(p);
        p.setContent(content);
    }

    public ApplicationLayerComponent render(ApplicationStack stack, ApplicationLayer layer) {
        label.setCaption(layer.getLabel());
        setWidth(stack.computeColumnWidth() * layer.getWidth(), Unit.PIXELS);
        setHeight(stack.computeRowHeight() * layer.getHeight(), Unit.PIXELS);
        healthIndicator.render(layer);

        return this;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }
}
