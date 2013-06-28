package org.opennms.features.vaadin.dashboard.ui.dashboard;

import org.opennms.features.vaadin.dashboard.config.DashletSelector;
import org.opennms.features.vaadin.dashboard.model.Dashlet;
import org.opennms.features.vaadin.dashboard.model.DashletSelectorAccess;
import org.opennms.features.vaadin.dashboard.model.DashletSpec;
import org.vaadin.addon.portallayout.container.PortalColumns;
import org.vaadin.addon.portallayout.portal.StackPortalLayout;

import java.util.List;

/**
 * @author Marcus Hellberg (marcus@vaadin.com)
 */
public class DashboardLayout extends PortalColumns {


    private StackPortalLayout[] columns = new StackPortalLayout[3];

    public DashboardLayout() {
        setSizeFull();

        for (int i = 0; i < columns.length; i++) {
            columns[i] = new StackPortalLayout();
            columns[i].setSizeFull();
            columns[i].setMargin(true);
            columns[i].setSpacing(true);
            appendPortal(columns[i]);
        }
    }

    private Dashlet getDashletInstance(DashletSpec dashletSpec) {
        DashletSelector dashletSelector = ((DashletSelectorAccess) getUI()).getDashletSelector();
        return dashletSelector.getFactoryForName(dashletSpec.getDashletName()).newDashletInstance(dashletSpec);
    }

    public void setDashletSpecs(List<DashletSpec> dashletSpecs) {

        int c = 0;
        int i = 0;

        for (DashletSpec dashletSpec : dashletSpecs) {
            Dashlet dashlet = getDashletInstance(dashletSpec);

            boolean boosted = dashlet.isBoosted();
            columns[i].portletFor(dashlet);

            i++;

            if (i % columns.length == 0) {
                c++;
                i = 0;
            }
        }
    }
}
