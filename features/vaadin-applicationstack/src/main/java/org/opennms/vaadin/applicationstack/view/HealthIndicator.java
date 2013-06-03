package org.opennms.vaadin.applicationstack.view;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import org.opennms.vaadin.applicationstack.model.ApplicationLayer;

/**
 *
 * @author tak
 */
public class HealthIndicator extends HorizontalLayout {
    
    final Label goodLabel = createLabel("good");
    final Label problemsLabel = createLabel("problems");
    final Label deathLabel = createLabel("death");
    
    public HealthIndicator() { 
        setWidth(100, Unit.PERCENTAGE);
        
        addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (event.getClickedComponent() == goodLabel) Notification.show("good pressed");
                if (event.getClickedComponent() == problemsLabel) Notification.show("problems pressed");
                if (event.getClickedComponent() == deathLabel) Notification.show("death pressed");
            }
        });
    }
    
    // TODO fix rendering
    public void render(ApplicationLayer layer) {
        removeAllComponents();
        float good = 0; //layer.computeGood();
        final float problems = 0; //layer.computeProblems();
        final float death = 0; //layer.computeDeath();
        
        if (good == 0 && problems == 0 && death == 0) {
            good = 100;
        }
        
        if (good > 0) {
            goodLabel.setDescription(good + "%");
            addComponent(goodLabel);
            goodLabel.setWidth(good, Unit.PERCENTAGE);
        }
        if (problems > 0) {
            problemsLabel.setDescription(problems + "%");
            addComponent(problemsLabel);
            problemsLabel.setWidth(problems, Unit.PERCENTAGE);
        }
        if (death > 0) {
            deathLabel.setDescription(death + "%");
            addComponent(deathLabel);
            deathLabel.setWidth(death, Unit.PERCENTAGE);
        }
        
        for (int i=0; i<getComponentCount(); i++) {
            setExpandRatio(getComponent(i), getComponent(i).getWidth());
            getComponent(i).setWidth(100, Unit.PERCENTAGE);
        }
    }
    
    private static Label createLabel(String description) {
        Label label = new Label(" ");
        label.setStyleName(description);
        label.setWidth(100, Unit.PERCENTAGE);
        label.setHeight(15, Unit.PIXELS);
        return label;
    }
    
}
