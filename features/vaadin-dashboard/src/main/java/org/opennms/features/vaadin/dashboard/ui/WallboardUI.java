package org.opennms.features.vaadin.dashboard.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.opennms.features.vaadin.dashboard.config.DashletSelector;
import org.opennms.features.vaadin.dashboard.model.DashletSelectorAccess;
import org.opennms.features.vaadin.dashboard.ui.dashboard.DashboardView;
import org.opennms.features.vaadin.dashboard.ui.wallboard.WallboardView;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Theme("dashboard")
@Title("OpenNMS Dashboard")
public class WallboardUI extends UI implements DashletSelectorAccess {
    DashletSelector dashletSelector;

    public void setDashletSelector(DashletSelector dashletSelector) {
        this.dashletSelector = dashletSelector;
    }

    public DashletSelector getDashletSelector() {
        return dashletSelector;
    }

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setSizeFull();
        rootLayout.setSpacing(true);
        HeaderLayout headerLayout = new HeaderLayout();
        rootLayout.addComponent(headerLayout);

        VerticalLayout portalWrapper = new VerticalLayout();
        portalWrapper.setSizeFull();
        portalWrapper.setMargin(true);

        rootLayout.addComponent(portalWrapper);
        rootLayout.setExpandRatio(portalWrapper, 1);
        setContent(rootLayout);

        Navigator navigator = new Navigator(this, portalWrapper);

        navigator.addView("dashboard", DashboardView.class);
        navigator.addView("wallboard", WallboardView.class);

        navigator.navigateTo("wallboard");
    }

}
