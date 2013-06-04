package org.opennms.vaadin.applicationstack.view;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennms.vaadin.applicationstack.model.ApplicationLayer;
import org.opennms.vaadin.applicationstack.model.ApplicationStack;
import org.opennms.vaadin.applicationstack.provider.CloneUtils;

/**
 *
 * @author marskuh
 */
public class EditStackComponent extends CustomComponent {

    private final VerticalLayout layout;
    private final List<ApplicationStackChangedListener> listeners = new ArrayList<ApplicationStackChangedListener>();
            
    private ApplicationStack model;
    private ApplicationStack original;
        
    public EditStackComponent() {
        setCaption("Edit");
        layout = new VerticalLayout();
        setCompositionRoot(layout);
    }
        
    public Component render(ApplicationStack stack) {
        this.model = CloneUtils.clone(stack);
        this.original = stack;
        renderInternal();
        return this;
    }
    
    private void renderInternal() {
        layout.removeAllComponents();
        final TextField stackLabelTextField = new TextField("Application name", model.getLabel());
        stackLabelTextField.setDescription("The name of the application");
        stackLabelTextField.addValueChangeListener(new BeanValueChangeListener<ApplicationStack, String>(model, "label") {

            @Override
            protected String getValue() {
                return stackLabelTextField.getValue();
            }
            
        });
    
        Button addButton = new Button("+", new ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                model.addLayer(new ApplicationLayer("new layer", model.getRowCount(), 0, model.getColumnCount(), 1));
                renderInternal();
            }
        });
        
        layout.addComponent(addButton);
        layout.addComponent(stackLabelTextField);
        
        if (!model.getLayers().isEmpty()) {
            layout.addComponent(new HeaderLayerComponent());
            Collections.sort(model.getLayers(), new Comparator<ApplicationLayer>() { // sort by row
                @Override
                public int compare(ApplicationLayer o1, ApplicationLayer o2) {
                    return Integer.valueOf(o1.getRow()).compareTo(Integer.valueOf(o2.getRow()));
                }
            });
            
            for (ApplicationLayer eachLayer : model.getLayers()) {
                layout.addComponent(new EditLayerComponent(eachLayer));
            }
        }
        
        layout.addComponent(createFooter());
    }

    private Layout createFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addComponent(new Button("ok", new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                fireApplicationStackChangedEvent(model);
            }
        }));
        
//        footer.addComponent(new Button("preview", new ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                Notification.show("NOT IMPLEMENTED YET");
//            }
//        }));
        
        footer.addComponent(new Button("cancel", new ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                fireApplicationStackChangedEvent(original);
            }
        }));
        
        return footer;
    }
    
    protected void fireApplicationStackChangedEvent(ApplicationStack stack) {
        for (ApplicationStackChangedListener eachListener : listeners) 
            eachListener.applicationStackChanged(stack);
    }

    public void addApplicationStackChangedListener(ApplicationStackChangedListener listener) {
        if (listener == null || listeners.contains(listener)) return;
        listeners.add(listener);
    }
    
    private class HeaderLayerComponent extends CustomComponent {
         private HeaderLayerComponent() {
            
            HorizontalLayout layout = new HorizontalLayout();
            layout.addComponent(new Label("Label"));
            layout.addComponent(new Label("Row"));
            layout.addComponent(new Label("Column"));
            layout.addComponent(new Label("Width"));
            layout.addComponent(new Label("Height"));
            layout.addComponent(new Label("Delete"));
            
            setCompositionRoot(layout);
        }
    }
    
    private class EditLayerComponent extends CustomComponent {
        private EditLayerComponent(final ApplicationLayer layer) {
            
            HorizontalLayout layout = new HorizontalLayout();
            
            final TextField label = new TextField(null, layer.getLabel());
            final TextField row = new TextField(null, Integer.toString(layer.getRow()));
            final TextField column = new TextField(null, Integer.toString(layer.getColumn()));
            final TextField width = new TextField(null, Integer.toString(layer.getWidth()));
            final TextField height = new TextField(null, Integer.toString(layer.getHeight()));
            
            label.addValueChangeListener(new BeanValueChangeListener<ApplicationLayer, String>(layer, "label") {
                @Override
                protected String getValue() {
                  return  label.getValue();
                }
            });
            row.addValueChangeListener(new BeanValueChangeListener<ApplicationLayer, Integer>(layer, "row") {
                @Override
                protected Integer getValue() {
                  return  Integer.parseInt(row.getValue());
                }
            });
            column.addValueChangeListener(new BeanValueChangeListener<ApplicationLayer, Integer>(layer, "column") {
                @Override
                protected Integer getValue() {
                  return Integer.parseInt(column.getValue());
                }
            });
            width.addValueChangeListener(new BeanValueChangeListener<ApplicationLayer, Integer>(layer, "width") {
                @Override
                protected Integer getValue() {
                  return  Integer.parseInt(width.getValue());
                }
            });
            height.addValueChangeListener(new BeanValueChangeListener<ApplicationLayer, Integer>(layer, "height") {
                @Override
                protected Integer getValue() {
                  return Integer.parseInt(height.getValue());
                }
            });
            Button removeButton = new Button("-", new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    model.removeLayer(layer);
                    renderInternal();
                }
            });
            
            layout.addComponent(label);
            layout.addComponent(row);
            layout.addComponent(column);
            layout.addComponent(width);
            layout.addComponent(height);
            layout.addComponent(removeButton);
            
            setCompositionRoot(layout);
        }
        
    }
    
    private static abstract class BeanValueChangeListener<BEANTYPE, PROPERTYTYPE> implements Property.ValueChangeListener {

        private final String propertyName;
        private final BEANTYPE bean;

        private BeanValueChangeListener(final BEANTYPE bean, final String propertyName) {
            this.propertyName = propertyName;
            this.bean = bean;
        }
        
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            try {
                new PropertyDescriptor(
                        propertyName, 
                        bean.getClass())
                    .getWriteMethod()
                    .invoke(bean, getValue());
            // TODO Exception handling
            } catch (IllegalAccessException ex) {
                Logger.getLogger(EditStackComponent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(EditStackComponent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(EditStackComponent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IntrospectionException ex) {
                Logger.getLogger(EditStackComponent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        protected abstract PROPERTYTYPE getValue();
    }
}
