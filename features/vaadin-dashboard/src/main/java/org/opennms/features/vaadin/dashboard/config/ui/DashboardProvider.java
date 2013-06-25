package org.opennms.features.vaadin.dashboard.config.ui;

import com.vaadin.data.util.BeanItemContainer;
import org.opennms.features.vaadin.dashboard.model.SandwichBoard;
import org.opennms.features.vaadin.dashboard.model.SandwichBoards;

import javax.xml.bind.JAXB;
import java.io.File;

public class DashboardProvider {
    private static DashboardProvider dashboardProvider = new DashboardProvider();
    private SandwichBoards sandwichBoards = null;
    private File cfgFile = new File("etc/dashboard-config.xml");
    private BeanItemContainer<SandwichBoard> beanItemContainer = new BeanItemContainer<SandwichBoard>(SandwichBoard.class);

    private DashboardProvider() {
        load();
    }

    public BeanItemContainer<SandwichBoard> getBeanContainer() {
        return beanItemContainer;
    }

    public static DashboardProvider getInstance() {
        return dashboardProvider;
    }

    public synchronized void save() {
        if (sandwichBoards == null) {
            load();
        }

        JAXB.marshal(sandwichBoards, cfgFile);
    }

    public synchronized void load() {
        if (!cfgFile.exists()) {
            sandwichBoards = new SandwichBoards();
        } else {
            sandwichBoards = JAXB.unmarshal(cfgFile, SandwichBoards.class);
        }

        updateBeanItemContainer();
    }

    private void updateBeanItemContainer() {
        beanItemContainer.removeAllItems();
        for (SandwichBoard sandwichBoard : sandwichBoards.getSandwichBoards()) {
            beanItemContainer.addItem(sandwichBoard);
        }
    }

    synchronized public boolean containsDashboard(String title) {
        for (SandwichBoard sandwichBoard : sandwichBoards.getSandwichBoards()) {
            if (sandwichBoard.getTitle().equals(title)) {
                return true;
            }
        }

        return false;
    }

    synchronized public SandwichBoard getDashboard(String title) {
        for (SandwichBoard sandwichBoard : sandwichBoards.getSandwichBoards()) {
            if (sandwichBoard.getTitle().equals(title)) {
                return sandwichBoard;
            }
        }

        return null;
    }

    synchronized public boolean containsDashboard(SandwichBoard sandwichBoard) {
        return sandwichBoards.getSandwichBoards().contains(sandwichBoard);
    }

    synchronized public void addDashboard(SandwichBoard sandwichBoard) {
        if (sandwichBoards == null) {
            load();
        }

        sandwichBoards.getSandwichBoards().add(sandwichBoard);

        save();

        updateBeanItemContainer();
    }

    synchronized public void removeDashboard(SandwichBoard sandwichBoard) {
        if (sandwichBoards == null) {
            load();
        }

        sandwichBoards.getSandwichBoards().remove(sandwichBoard);

        save();

        updateBeanItemContainer();
    }
}
