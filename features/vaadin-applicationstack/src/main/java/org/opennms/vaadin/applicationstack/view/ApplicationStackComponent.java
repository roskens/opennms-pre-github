package org.opennms.vaadin.applicationstack.view;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import org.opennms.vaadin.applicationstack.model.ApplicationLayer;
import org.opennms.vaadin.applicationstack.model.ApplicationStack;
import org.opennms.vaadin.applicationstack.model.Coordinates;

/**
 *
 * @author mvrueden
 */
public class ApplicationStackComponent extends CustomComponent {

    private final GridLayout gridLayout = new GridLayout();

    public ApplicationStackComponent() {
    }

    public ApplicationStackComponent render(ApplicationStack stack) {
        setCaption(stack.getLabel());
        setStyleName("applicationStack");
        setCompositionRoot(gridLayout);

        gridLayout.addStyleName("applicationStack");
        gridLayout.removeAllComponents();
        if (stack.getLayers().isEmpty()) {
            gridLayout.setRows(1);
            gridLayout.setColumns(1);
            gridLayout.addComponent(new Label("no layers defined"));
        } else {
            gridLayout.setRows(stack.getRowCount());
            gridLayout.setColumns(stack.getColumnCount());

            for (ApplicationLayer eachLayer : stack.getLayers()) {
                final Coordinates c = eachLayer.getCoordinates();
                gridLayout.addComponent(
                        new ApplicationLayerComponent()
                        .render(stack, eachLayer),
                        c.column1,
                        c.row1,
                        c.column2,
                        c.row2);
            }
        }
        return this;
    }
}
