package org.opennms.vaadin.applicationstack.view;

import org.opennms.vaadin.applicationstack.model.ApplicationStack;
import org.opennms.vaadin.applicationstack.provider.ApplicationStackProvider;
import org.opennms.vaadin.applicationstack.provider.ApplicationStackProviderFactory;
import org.opennms.vaadin.applicationstack.provider.NodeListProvider;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Theme("applicationstack")
public class ApplicationStackUI extends UI {
    private NodeListProvider nodeListProvider;

    private ApplicationStackProvider applicationStackProvider =
            ApplicationStackProviderFactory.instance.createApplicationStackProvider();

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setContent(layout);
        setSizeFull();

        ApplicationStack stack = applicationStackProvider.loadApplicationStack();
        if (stack != null) {
            layout.addComponent(new ApplicationStackComponent().render(stack));
        } else {
            Notification.show("Could not load application stack file from disk", Notification.Type.ERROR_MESSAGE);
        }
    }

    public void setNodeListProvider(NodeListProvider nodeListProvider) {
        this.nodeListProvider = nodeListProvider;
    }

    public NodeListProvider getNodeListProvider() {
        return nodeListProvider;
    }
}