package org.opennms.features.vaadin.nodebrowser;

import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;
import org.opennms.netmgt.dao.NodeDao;

public class NodeBrowserApplication extends Application {
    private NodeDao nodeDao;

    @Override
    public void init() {
        if (nodeDao == null) {
            throw new RuntimeException("nodeDao cannot be null.");
        }

        setTheme(Runo.THEME_NAME);

        final HorizontalSplitPanel mainPanel = new HorizontalSplitPanel();

        mainPanel.setSizeFull();
        mainPanel.setSplitPosition(25, Sizeable.UNITS_PERCENTAGE);
//        mainPanel.addComponent(mibPanel);
//        mainPanel.addComponent(mibConsole);

        final Window mainWindow = new Window("NodeBrowser Application", mainPanel);
        setMainWindow(mainWindow);
    }

    public void setNodeDao(NodeDao nodeDao) {
        this.nodeDao = nodeDao;
    }
}
