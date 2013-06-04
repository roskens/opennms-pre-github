package org.opennms.vaadin.applicationstack.view;

import org.opennms.vaadin.applicationstack.model.ApplicationStack;
import org.opennms.vaadin.applicationstack.provider.ApplicationStacksProvider;
import org.opennms.vaadin.applicationstack.provider.NodeListProvider;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.opennms.vaadin.applicationstack.provider.ApplicationStacksProviderFactory;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Theme("applicationstack")
public class ApplicationStackUI extends UI implements ApplicationStackChangedListener {
    private NodeListProvider nodeListProvider;
    private ComboBox stacksComboBox;
    private ApplicationStackComponent stackComponent;
    private EditStackComponent editComponent;
    private ApplicationStacksProvider stacksProvider = ApplicationStacksProviderFactory
            .instance.getApplicationStacksProvider();

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setContent(layout);
        setSizeFull();
        
        stackComponent = new ApplicationStackComponent();
        editComponent = new EditStackComponent();
        editComponent.addApplicationStackChangedListener(this);
        
        stacksComboBox = new ComboBox("Stack");
        stacksComboBox.setNullSelectionAllowed(false);
        stacksComboBox.setImmediate(true);
        stacksComboBox.setDescription("Select a Stack to show. You can also type in a non existing stack name. That stack is then created");
        stacksComboBox.setInputPrompt("Select a stack");
        
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.addComponent(stackComponent);
        contentLayout.addComponent(editComponent);
        
        layout.addComponent(stacksComboBox);
       	layout.addComponent(contentLayout);
       	layout.setExpandRatio(contentLayout, 1);
        
        render(stacksProvider.loadApplicationStacks().getFirst());
    }
    
    public void render(ApplicationStack stack) {
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
					stackComponent.render((ApplicationStack)value);
                                        editComponent.render((ApplicationStack)value);
				}
			}
		});
       	stacksComboBox.select(stack);
    }
     
    public void setNodeListProvider(NodeListProvider nodeListProvider) {
        this.nodeListProvider = nodeListProvider;
    }

    public NodeListProvider getNodeListProvider() {
        return nodeListProvider;
    }

    @Override
    public void applicationStackChanged(ApplicationStack stack) {
        ApplicationStack oldStack = stacksProvider.loadApplicationStack(stack.getLabel());
        if (oldStack == null) { // new stack?
            stacksProvider.saveApplicationStack(stack);
        } else { // replace existing one ?
            stacksProvider.removeApplicationStack(oldStack);
            stacksProvider.saveApplicationStack(stack);
        }
        render(stack);
    }
}