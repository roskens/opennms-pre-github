package org.opennms.vaadin.applicationstack.view;

import org.opennms.vaadin.applicationstack.model.ApplicationStack;
import org.opennms.vaadin.applicationstack.provider.ApplicationStacksProvider;
import org.opennms.vaadin.applicationstack.provider.ApplicationStacksProviderFactory;
import org.opennms.vaadin.applicationstack.provider.NodeListProvider;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Theme("applicationstack")
public class ApplicationStackUI extends UI {
    private NodeListProvider nodeListProvider;
    private ComboBox stacksComboBox;
    private ApplicationStackComponent stackComponent;
    
    private ApplicationStacksProvider stacksProvider =
            ApplicationStacksProviderFactory.instance.getApplicationStacksProvider();

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setContent(layout);
        setSizeFull();
        
        stackComponent = new ApplicationStackComponent();

        stacksComboBox = new ComboBox("Stack");
        stacksComboBox.setNullSelectionAllowed(false);
        stacksComboBox.setImmediate(true);
        stacksComboBox.setDescription("Select a Stack to show. You can also type in a non existing stack name. That stack is then created");
        stacksComboBox.setInputPrompt("Select a stack");
        stacksComboBox.setContainerDataSource(
        		new BeanItemContainer<ApplicationStack>(
        				ApplicationStack.class, 
        				stacksProvider.loadApplicationStacks().getStacks()));
        stacksComboBox.setItemCaptionPropertyId("label");
        
        for (ApplicationStack eachStack : stacksProvider.loadApplicationStacks().getStacks()) {
        	stacksComboBox.addItem(eachStack);
        }
        
        stacksComboBox.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Object value = event.getProperty().getValue();
				if (value == null) return;
				if (value instanceof ApplicationStack) {
					stackComponent.render((ApplicationStack)event.getProperty().getValue());
				}
				if (value instanceof String) {
					stackComponent.render(
							stacksProvider.loadApplicationStacks().addStack((String)value)
							.getStack((String)value));
				}
			}
		});
        
       	layout.addComponent(stacksComboBox);
       	layout.addComponent(stackComponent);
       	layout.setExpandRatio(stackComponent, 1);
       	
       	stacksComboBox.select(stacksProvider.loadApplicationStacks().getFirst());
    }

    public void setNodeListProvider(NodeListProvider nodeListProvider) {
        this.nodeListProvider = nodeListProvider;
    }

    public NodeListProvider getNodeListProvider() {
        return nodeListProvider;
    }
}