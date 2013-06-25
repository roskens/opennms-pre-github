package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.opennms.features.vaadin.dashboard.config.DashletSelector;
import org.opennms.features.vaadin.dashboard.dashlets.UndefinedDashlet;
import org.opennms.features.vaadin.dashboard.model.SandwichBoard;
import org.opennms.features.vaadin.dashboard.model.SandwichSpec;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@Theme("dashboard")
@Title("OpenNMS Dashboard")
public class DashboardConfigUI extends UI {
    private DashletSelector dashletSelector;

    public DashboardConfigUI() {
    }

    public void setDashletSelector(DashletSelector dashletSelector) {
        this.dashletSelector = dashletSelector;
    }

    @Override
    protected void init(VaadinRequest request) {
        List<SandwichBoard> sandwichBoardList = new ArrayList<SandwichBoard>();
        SandwichSpec sandwichSpec1 = new SandwichSpec();
        sandwichSpec1.setSandwichClass(UndefinedDashlet.class);
        SandwichSpec sandwichSpec2 = new SandwichSpec();
        sandwichSpec2.setSandwichClass(UndefinedDashlet.class);
        SandwichBoard sandwichBoard = new SandwichBoard();
        sandwichBoard.setTitle("Dummy");
        sandwichBoard.getSandwiches().add(sandwichSpec1);
        sandwichBoard.getSandwiches().add(sandwichSpec2);
        sandwichBoardList.add(sandwichBoard);

        setContent(new DashboardConfigView(dashletSelector));
    }
}
