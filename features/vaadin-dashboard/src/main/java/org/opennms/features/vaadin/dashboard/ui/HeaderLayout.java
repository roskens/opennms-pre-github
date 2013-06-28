package org.opennms.features.vaadin.dashboard.ui;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.opennms.features.vaadin.dashboard.config.ui.WallboardProvider;

/**
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
public class HeaderLayout extends HorizontalLayout {

    public HeaderLayout() {
        addStyleName("header");
        setMargin(true);
        setSpacing(true);
        setWidth("100%");
        Image logo = new Image(null, new ThemeResource("img/logo.png"));
        addComponent(logo);
        setExpandRatio(logo, 1.0f);

        final NativeSelect nativeSelect = new NativeSelect();
        nativeSelect.setContainerDataSource(WallboardProvider.getInstance().getBeanContainer());
        nativeSelect.setItemCaptionPropertyId("title");
        nativeSelect.setNullSelectionAllowed(false);
        /*
        Button dashboardButton = new Button("Dashboard", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo("dashboard/" + nativeSelect.getContainerProperty(nativeSelect.getValue(), "title"));
            }
        });
        */

        Button wallboardButton = new Button("Wallboard", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().getNavigator().navigateTo("wallboard/" + nativeSelect.getContainerProperty(nativeSelect.getValue(), "title"));
            }
        });

        addComponents(nativeSelect, /*dashboardButton,*/ wallboardButton);
        setComponentAlignment(nativeSelect, Alignment.MIDDLE_CENTER);
        //setComponentAlignment(dashboardButton, Alignment.MIDDLE_CENTER);
        setComponentAlignment(wallboardButton, Alignment.MIDDLE_CENTER);
    }
}
